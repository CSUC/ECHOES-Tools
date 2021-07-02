<?php

require_once dirname(__FILE__) . '/elastic/vendor/autoload.php';

class Elastic {
	
	private $client;
	private $countXpage = 75;
	private $index = 'echoes';
	private $maxResults = '10000';
	private $minResults = '1';

	public function __construct() { $this->connect(); }
	
	public function getCountPage() {
		return $this->countXpage;
	}
	
	private function connect() { 
		try {
			/**
			$host = [
    					[
        					'host' => '192.168.74.125',
        					'port' => 9200,
        					'scheme' => 'http'
    					]
				];
			**/
                        $host = [
                                        [
                                                'host' => ELASTIC_URL,
                                                'port' => ELASTIC_PORT,
                                                'scheme' => ELASTIC_SCHEME
                                        ]
                                ];

                	$this->client = Elasticsearch\ClientBuilder::create()
                        	        ->setHosts($host)
                                	->build();
		} catch (Exception $e) {
			echo 'No connect ElasticDB: ',  $e->getMessage(), "\n";
		}
	}

	public function nextScroll($scroll, $move , $type) {

    		$results = $this->client->scroll([
            					'scroll_id' => $scroll->getNextScroll(),
						'scroll' => '2m'
        		]
		);
		
		if (!isset($results['hits']['hits']) && count($results['hits']['hits']) < 0) {
			$results = $this->reScroll($scroll, $move, $type);
		} 
		return $results;
	}

	public function reScroll($scroll, $move, $type) {
		$page = ($move == 'next') ? $scroll->getPage() : ($scroll->getPage() - 1);
		$results = [];
		for($i = 0; $i < $page; $i++) {
			if ($i == 0) {
				if ($type == 'agents') {
        				$results = $this->searchPeople(true, $scroll->getName(), $scroll->getGraph(), $scroll->getYears(), $scroll->getPlace(), $scroll->getConcept(), $scroll->getType(), $scroll->getYearsDeath());
				} else if ($type == 'provided') {
					$results = $this->searchProvided(true, $scroll->getPlace(), $scroll->getGraph(), $scroll->getTypeDO(), $scroll->getConcept(), $scroll->getYear());   
				} else if ($type == 'timespansYear') {
					$results = $this->searchYearTime(true, $scroll->getYear(), $scroll->getType(), $scroll->getGraph(), $scroll->getTypeDO(), $scroll->getConcept());
				}
			} else {
				$results = $this->nextScroll($scroll, $move, $type);
			}
			$scroll->setNextScroll($results['_scroll_id']);
		}
		return $results;	
	}

	public function getAllGeo($query){
		
                $params['index'] = $this->index;

		$params['body'] = $query;
	
                $results = $this->client->search($params);
                
		return json_encode($results);
		
        }

	public function detailsPerson($id){
		
		$params = [
                		'index' => $this->index,
                                'body' => ['query' =>
                                                ['match' =>
                                                        ['@id' =>
                                                                [
                                                                        'query'=> $id
                                                                ]
                                                        ]
                                                ]
                                          ]
                          ];
		$results = $this->client->search($params);
                return $results;
	}

	public function addFacets() {
		
		$facets = [/**
			   'names' =>
                          		['terms' =>
                                        		[
                                                        	'field' => 'http://xmlns.com/foaf/0.1/name.@value.keyword'
                                                        ]
                                        ],
		           **/
                           'graphs' =>
                                        ['terms' =>
                                                        [
                                                                'field' => '@graph'
                                                        ]
                                        ],
                           'rdaGr2:dateOfBirth' =>
                                        ['auto_date_histogram' =>
                                                        [
                                                                'field' => 'http://rdvocab.info/ElementsGr2/dateOfBirth.@value.date',
                                                                'format' => 'yyyy',
                                                        ]
                                        ],
                           'rdaGr2:dateOfDeath' =>
                                        ['auto_date_histogram' =>
                                                        [
                                                                'field' => 'http://rdvocab.info/ElementsGr2/dateOfDeath.@value.date',
                                                                'format' => 'yyyy',
                                                        ]
                                        ],
        		   'concepts' => 
					['terms' =>
							[
								'field' => 'http://www.europeana.eu/schemas/edm/isRelatedTo.@id.keyword'
							]
					]
                          ];
		return $facets;

	}

	public function searchPeople($size, $name, $graph, $dateBirth, $place, $concept, $type, $dateDeath) {
		$sizeScroll = ($size) ? $this->countXpage : 0;
                $params['index'] = $this->index;
		$params['size'] = $sizeScroll;
		$startBirthPeriod = (empty($dateBirth)) ?: explode("-", $dateBirth)[0];
		$startDeathPeriod = (empty($dateDeath)) ?: explode("-", $dateDeath)[0];
		$endBirthPeriod = (!empty($dateBirth) && strpos($dateBirth, "-") === false) ? 'now' : explode("-", $dateBirth)[1];
		$endDeathPeriod = (!empty($dateDeath) && strpos($dateDeath, "-") === false) ? 'now' : explode("-", $dateDeath)[1];
		if ($sizeScroll != 0) {	
			$params['scroll'] = '2m';
		}
		$match = ($type == 'keyword') ? 'http://xmlns.com/foaf/0.1/name.@value.keyword' : 'http://xmlns.com/foaf/0.1/name.@value';
		$params['body']['query']['bool']['must'][]  = (!empty($name)) ? ['match' => [$match => ['query' => $name, 'operator' => 'and']]] : ['match' => ['@type' => 'http://www.europeana.eu/schemas/edm/Agent']];
    		(empty($graph)) ?: $params['body']['query']['bool']['must'][] = ['match' => ['@graph' => ['query' => $graph]]];
    		(empty($dateBirth)) ?: $params['body']['query']['bool']['must'][] = ['range' => ['http://rdvocab.info/ElementsGr2/dateOfBirth.@value.date' => ['gte' => $startBirthPeriod, 'lte' => $endBirthPeriod]]];
                (empty($dateDeath)) ?: $params['body']['query']['bool']['must'][] = ['range' => ['http://rdvocab.info/ElementsGr2/dateOfDeath.@value.date' => ['gte' => $startDeathPeriod, 'lte' => $endDeathPeriod]]];
		(empty($place)) ?: $params['body']['query']['bool']['must'][]  = ['multi_match' => ['fields' => '*.@id.keyword', 'query' => $place]];
		(empty($concept)) ?: $params['body']['query']['bool']['must'][] = ['match' => ['http://www.europeana.eu/schemas/edm/isRelatedTo.@id' => ['query' => $concept]]];
		
		$params['body']['aggs'] = $this->addFacets();

		$results = $this->client->search($params);

		return $results;	
	}

	public function getCityGeo($location) {

		$results = $this->getCoordinatesPlacesRegion($location,$this->getCoordinatesPlace($location, $this->maxResults), 10);

		return $results;
	}

	public function getCoordinatesPlace($location, $size) {
	
		$params['size'] = $size;

                $params['index'] = $this->index;

                $params['body']['query']['bool']['must'][]  = ['match' => ['http://www.w3.org/2004/02/skos/core#prefLabel.@value' => ['query' => $location]]];

                $params['body']['query']['bool']['must'][]  = ['match' => ['@type' => 'http://www.europeana.eu/schemas/edm/Place']];

                $params['body']['query']['bool']['must'][]  = ['exists' => ['field' => 'http://www.opengis.net/def/wktLiteral.@value']];

                $results = $this->client->search($params);

                return $results;
	}

	private function getCoordinatesPlacesRegion($location, $resultsSearchCity, $size) {

		$resultsSearchCity['hits']['hits'] = array(); 

                $params['size'] = $size;

                $params['index'] = $this->index;

                $params['body']['query']['bool']['must'][]  = ['match' => ['http://www.w3.org/2004/02/skos/core#prefLabel.@value' => ['query' => $location]]];

                $params['body']['query']['bool']['must'][]  = ['match' => ['@type' => 'http://www.europeana.eu/schemas/edm/Place']];

		$resultsToSearch = $this->client->search($params);

		foreach($resultsToSearch['hits']['hits'] as $idPlace) {

			$params = [];

			$params['size'] = $this->maxResults;
	
			$params['body']['query']['bool']['must'][]  = ['multi_match' => ['fields' => '*.@id.keyword', 'query' => $idPlace['_source']['@id']]];

			$params['body']['query']['bool']['must'][]  = ['match' => ['@type' => 'http://www.europeana.eu/schemas/edm/ProvidedCHO']];

			$results = $this->client->search($params);

			$citiesRegion = [];

			foreach($results['hits']['hits'] as $result) {
			
				foreach($result['_source']['http://purl.org/dc/elements/1.1/coverage'] as $city) {
					array_push($citiesRegion,$city['@id']);
				}
			}
			$citiesRegion = array_unique($citiesRegion);

			foreach($citiesRegion as $cityId) {
			                
				$params = [];

                		$params['size'] = $this->maxResults;

                		$params['index'] = $this->index;

                		$params['body']['query']['bool']['must'][]  = ['match' => ['@id' => $cityId]];

                		$params['body']['query']['bool']['must'][]  = ['match' => ['@type' => 'http://www.europeana.eu/schemas/edm/Place']];

				$resultsCoor = $this->client->search($params)['hits']['hits'][0];

				if(empty($resultsCoor['_source']['http://www.w3.org/2003/01/geo/wgs84_pos#lat']['@value']) || empty($resultsCoor['_source']['http://www.w3.org/2003/01/geo/wgs84_pos#long']['@value'])) continue;
				$resultsCoor['_source']['http://www.w3.org/2004/02/skos/core#prefLabel']['@value'] = $location . " | " . $resultsCoor['_source']['http://www.w3.org/2004/02/skos/core#prefLabel']['@value'] . ' (' . $idPlace['_source']['http://www.w3.org/2004/02/skos/core#prefLabel']['@value'] . ')';

				array_push($resultsSearchCity['hits']['hits'],$resultsCoor);
			}
		}
		return $resultsSearchCity;
	}

	public function ifMunicipiOrComarca($place){
		$results['hits']['hits'] = [];
		$results = $this->getCoordinatesPlacesRegion($place, $results, $this->minResults);
		$results = (empty($results['hits']['hits'])) ? $this->getCoordinatesPlace($place, $this->minResults) : $results;
                if(!empty($results['hits']['hits'][0]['_source']['http://www.w3.org/2003/01/geo/wgs84_pos#lat']['@value']) || !empty($results['hits']['hits'][0]['_source']['http://www.w3.org/2003/01/geo/wgs84_pos#long']['@value'])) return $results['hits']['hits'][0]['_source']['http://www.w3.org/2003/01/geo/wgs84_pos#lat']['@value'] . ':' . $results['hits']['hits'][0]['_source']['http://www.w3.org/2003/01/geo/wgs84_pos#long']['@value'];
		
		return "";
	}

        public function searchProvided($size, $place, $graph, $typeDO, $concept, $year) {
 
                $sizeScroll = ($size) ? $this->countXpage : 0;
                $params['index'] = $this->index;
                $params['size'] = $sizeScroll;
		if ($sizeScroll != 0) {
                       $params['scroll'] = '2m';
                }

                $params['body']['query']['bool']['must'][] = ['match' => ['@type' => 'http://www.europeana.eu/schemas/edm/ProvidedCHO']];

		(empty($graph)) ?: $params['body']['query']['bool']['must'][] = ['match' => ['@graph' => ['query' => $graph]]];		
                (empty($place)) ?: $params['body']['query']['bool']['must'][] = ['multi_match' => ['fields' => '*.@id.keyword', 'query' => $place]];

		(empty($typeDO)) ?: $params['body']['query']['bool']['must'][] = ['match' => ['http://www.europeana.eu/schemas/edm/type.@value' => ['query' => $typeDO]]];
		 
                (empty($concept)) ?: $params['body']['query']['bool']['must'][] = ['match' => ['http://www.europeana.eu/schemas/edm/isRelatedTo.@id' => ['query' => 'Concept:' . urlencode(explode(':',$concept)[1])]]];

		(empty($year)) ?: $params['body']['query']['bool']['must'][] = ['range' => ['http://purl.org/dc/elements/1.1/date.@value.date' => ['gte' => $year, 'lte' => $year]]];

                $params['body']['aggs'] = $this->addFacetsProviders();
                $results = $this->client->search($params);
 
                return $results;
        }

	public function addFacetsProviders() {

		$facets = ['graphs' =>
                                        ['terms' =>
                                                        [
                                                                'field' => '@graph'
                                                        ]
                                        ],
			   'typeDO' =>
                                        ['terms' =>
                                                        [
                                                                'field' => 'http://www.europeana.eu/schemas/edm/type.@value.keyword'
                                                        ]
                                        ],
                           'concepts' =>
                                        ['terms' =>
                                                        [
                                                                'field' => 'http://www.europeana.eu/schemas/edm/isRelatedTo.@id.keyword'
                                                        ]
                                        ],
                           'dc:date' =>
                                        ['date_histogram' =>
                                                        [
                                                                'min_doc_count' => 1,
                                                                'field' => 'http://purl.org/dc/elements/1.1/date.@value.date',
                                                                'interval' => 'year',
                                                                'format' => 'yyyy',
                                                                'order' => [ '_count' => 'desc' ]
                                                        ]
                                        ]
                          ];
		return $facets;
	}

        public function addFacetsTime() {

                $facets = ['type' =>
                                        ['terms' =>
                                                        [
                                                                'field' => '@type'
                                                        ]
                                        ],
                           'graphs' =>
                                        ['terms' =>
                                                        [
                                                                'field' => '@graph'
                                                        ]
                                        ],
			   'dc:date' =>
                                        ['date_histogram' =>
                                                        [
                                                                'min_doc_count' => 1,
                                                                'field' => 'http://purl.org/dc/elements/1.1/date.@value.date',
                                                                'interval' => 'year',
                                                                'format' => 'yyyy',
                                                                'order' => [ '_count' => 'desc' ]
                                                        ]
                                        ],
                           'rdaGr2:dateOfBirth' =>
                                        ['date_histogram' =>
                                                        [
                                                                'min_doc_count' => 1,
                                                                'field' => 'http://rdvocab.info/ElementsGr2/dateOfBirth.@value.date',
                                                                'interval' => 'year',
                                                                'format' => 'yyyy',
                                                                'order' => [ '_count' => 'desc' ]
                                                        ]
                                        ],
                           'rdaGr2:dateOfDeath' =>
                                        ['date_histogram' =>
                                                        [
                                                                'min_doc_count' => 1,
                                                                'field' => 'http://rdvocab.info/ElementsGr2/dateOfDeath.@value.date',
                                                                'interval' => 'year',
                                                                'format' => 'yyyy',
                                                                'order' => [ '_count' => 'desc' ]
                                                        ]
                                        ],
			   'typeDO' => 
					['terms' => 
							[
								'field' => 'http://www.europeana.eu/schemas/edm/type.@value.keyword'
							]
					],
                           'concepts' =>
                                        ['terms' =>
                                                        [
                                                                'field' => 'http://www.europeana.eu/schemas/edm/isRelatedTo.@id.keyword'
                                                        ]
                                        ]
			  ];

                return $facets;

        }

        public function searchYearTime($size, $year, $type, $graph, $typeDO, $concept, $typeYear) {

                $sizeScroll = ($size) ? $this->countXpage : 0;
                $params['index'] = $this->index;
                $params['size'] = $sizeScroll;

		$defineType = [
				'providedCHO' => 'http://www.europeana.eu/schemas/edm/ProvidedCHO',
				'agent' => 'http://www.europeana.eu/schemas/edm/Agent'
		];
                

 		($sizeScroll == 0) ?: $params['scroll'] = '2m';

 		$year = (empty($year)) ? 'now' : $year . "||/y";

 		(empty($type)) ?: $params['body']['query']['bool']['must'][] = ['match' => ['@type' => $defineType[$type]]];

 		(empty($graph)) ?: $params['body']['query']['bool']['must'][] = ['match' => ['@graph' => ['query' => $graph]]];

		(empty($typeDO)) ?: $params['body']['query']['bool']['must'][] = ['match' => ['http://www.europeana.eu/schemas/edm/type.@value' => ['query' => $typeDO]]];

                (empty($concept)) ?: $params['body']['query']['bool']['must'][] = ['match' => ['http://www.europeana.eu/schemas/edm/isRelatedTo.@id' => ['query' => $concept]]];
               
		if ($typeYear == 'year') { 
                	$should['bool']['should'][] = ['range' => ['http://purl.org/dc/elements/1.1/date.@value.date' => ['gte' => $year, 'lte' => $year]]];
                } elseif ($typeYear == 'years') {
			$should['bool']['should'][] = ['range' => ['http://rdvocab.info/ElementsGr2/dateOfBirth.@value.date' => ['gte' => $year, 'lte' => $year]]];
		} elseif ($typeYear == 'yearsDeath') {
			$should['bool']['should'][] = ['range' => ['http://rdvocab.info/ElementsGr2/dateOfDeath.@value.date' => ['gte' => $year, 'lte' => $year]]];
		} elseif ($year != 'now') {
                        $should['bool']['should'][] = ['range' => ['http://purl.org/dc/elements/1.1/date.@value.date' => ['gte' => $year, 'lte' => $year]]];
                        $should['bool']['should'][] = ['range' => ['http://rdvocab.info/ElementsGr2/dateOfBirth.@value.date' => ['gte' => $year, 'lte' => $year]]];
                        $should['bool']['should'][] = ['range' => ['http://rdvocab.info/ElementsGr2/dateOfDeath.@value.date' => ['gte' => $year, 'lte' => $year]]];

		} else {
			$should['bool']['should'][] = ['range' => ['http://purl.org/dc/elements/1.1/date.@value.date' => ['lt' => $year]]];
			$should['bool']['should'][] = ['range' => ['http://rdvocab.info/ElementsGr2/dateOfBirth.@value.date' => ['lt' => $year]]];
			$should['bool']['should'][] = ['range' => ['http://rdvocab.info/ElementsGr2/dateOfDeath.@value.date' => ['lt' => $year]]];
		}

 		$params['body']['query']['bool']['must'][] = $should;
 		$params['body']['aggs'] = $this->addFacetsTime();
 		$results = $this->client->search($params);
                return $results;
        }

	public function getAllAggregation($field, $afterKey, $url, $type, $name, $concept, $year, $years, $place, $graph, $typeDO, $yearsDeath) {
		
		$params['size'] = 0;
		$params['index'] = $this->index;
		$year = (empty($year)) ? 'now' : $year . "||/y";;
		
                $startBirthPeriod = explode("-", $years)[0];
                $endBirthPeriod = (strpos($years, "-") === false) ? 'now' : explode("-", $years)[1];

                $startDeathPeriod = explode("-", $yearsDeath)[0];
                $endDeathPeriod = (strpos($yearsDeath, "-") === false) ? 'now' : explode("-", $yearsDeath)[1];

		if($url == 'timespans'){
			(empty($concept)) ?: $params['body']['query']['bool']['must'][] = ['match' => ['http://www.europeana.eu/schemas/edm/isRelatedTo.@id' => ['query' => $concept]]];
			(empty($place)) ?: $params['body']['query']['bool']['must'][]  = ['multi_match' => ['fields' => '*.@id.keyword', 'query' => $place]];
			(empty($graph)) ?: $params['body']['query']['bool']['must'][] = ['match' => ['@graph' => ['query' => $graph]]];
			(empty($typeDO)) ?: $params['body']['query']['bool']['must'][] = ['match' => ['http://www.europeana.eu/schemas/edm/type.@value' => ['query' => $typeDO]]];

	                $should['bool']['should'][] = ($year != 'now') ? ['range' => ['http://purl.org/dc/elements/1.1/date.@value.date' => ['gte' => $year, 'lte' => $year]]] : ['range' => ['http://purl.org/dc/elements/1.1/date.@value.date' => ['lt' => $year]]];

	                $should['bool']['should'][] = ($year != 'now') ? ['range' => ['http://rdvocab.info/ElementsGr2/dateOfBirth.@value.date' => ['gte' => $year, 'lte' => $year]]] : ['range' => ['http://rdvocab.info/ElementsGr2/dateOfBirth.@value.date' => ['lt' => $year]]];

			$params['body']['query']['bool']['must'][] = $should;
		}else{
			$params['body']['query']['bool']['must'][]  = (!empty($name)) ? ['match' => ['http://xmlns.com/foaf/0.1/name.@value' => ['query' => $name, 'operator' => 'and']]] : ['match' => ['@type' => 'http://www.europeana.eu/schemas/edm/Agent']];
			(empty($years)) ?: $params['body']['query']['bool']['must'][] = ['range' => ['http://rdvocab.info/ElementsGr2/dateOfBirth.@value.date' => ['gte' => $startBirthPeriod, 'lte' => $endBirthPeriod]]];
                        (empty($yearsDeath)) ?: $params['body']['query']['bool']['must'][] = ['range' => ['http://rdvocab.info/ElementsGr2/dateOfDeath.@value.date' => ['gte' => $startDeathPeriod, 'lte' => $endDeathPeriod]]];
        	        (empty($place)) ?: $params['body']['query']['bool']['must'][]  = ['multi_match' => ['fields' => '*.@id.keyword', 'query' => $place]];
	                (empty($concept)) ?: $params['body']['query']['bool']['must'][] = ['match' => ['http://www.europeana.eu/schemas/edm/isRelatedTo.@id' => ['query' => $concept]]];
			(empty($graph)) ?: $params['body']['query']['bool']['must'][] = ['match' => ['@graph' => ['query' => $graph]]];
			(empty($typeDO)) ?: $params['body']['query']['bool']['must'][] = ['match' => ['http://www.europeana.eu/schemas/edm/type.@value' => ['query' => $typeDO]]];
		}

		if($type == 'years' || $type == 'year' || $type == 'yearsDeath'){
	                $params['body']['aggs']['result']['composite']['sources'] = ['date' => ['date_histogram' => [ 'field' => $field, 'calendar_interval' => '1y', 'format' => 'yyyy']]];
                        (empty($afterKey)) ?: $params['body']['aggs']['result']['composite']['after'] = ['date' => $afterKey];
		}else{
	                $params['body']['aggs']['result']['composite']['sources'] = ['result' => ['terms' => ['field' => $field]]];
                        (empty($afterKey)) ?: $params['body']['aggs']['result']['composite']['after'] = ['result' => $afterKey];
		}
		$results = $this->client->search($params);
                return $results;
	}

        public function getHeatMap($type, $yearStart, $yearEnd, $afterKey) {
		
		$params['size'] = 0;
                $params['index'] = $this->index;              		
		$field = ($type == 'agent') ? 'http://rdvocab.info/ElementsGr2/dateOfBirth.@value.date' : 'http://purl.org/dc/elements/1.1/date.@value.date';
		$match = ($type == 'agent') ? 'http://www.europeana.eu/schemas/edm/Agent' : 'http://www.europeana.eu/schemas/edm/ProvidedCHO';

		
                $should['bool']['should'][] = ['range' => [$field => ['gte' => $yearStart, 'lte' => $yearEnd]]];
                $params['body']['query']['bool']['must'][] = $should;
                
                $params['body']['query']['bool']['must'][]  = ['match' => ['@type' => $match]];

                $params['body']['aggs']['result']['composite']['sources'] = ['date' => ['date_histogram' => [ 'field' => $field, 'calendar_interval' => '1d', 'format' => 'yyyy-MM-dd']]];
                (empty($afterKey)) ?: $params['body']['aggs']['result']['composite']['after'] = ['date' => $afterKey];
		
                $results = $this->client->search($params);
                return $results;
        }

	public function getLabelPlace($place) {

                $params = [
                                'index' => $this->index,
                                'body' => ['query' =>
                                                ['match' =>
                                                        ['@id' =>
                                                                [
                                                                        'query'=> $place
                                                                ]
                                                        ]
                                                ]
                                          ]
                          ];
                $results = $this->client->search($params);
		return $results['hits']['hits'][0]['_source']['http://www.w3.org/2004/02/skos/core#prefLabel']['@value'];
	}

}
