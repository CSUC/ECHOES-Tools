<?php

class SearchesController extends MvcPublicController {

        private $photoFacets = [
                                "name" => "ico-people-violet.svg",
                                "years" => "ico-birth.svg",
				"yearsDeath" => "ico-died.svg",
                                "year" => "ico-digital-objets-violet.svg",
                                "concept" => "ico-concepts-violet.svg"
                               ];

        public function showAllOptionsFacets() {
		$types = ['concept' => __('Concepts', 'echoes_wp_sparql_plugin'),
                          'name' => __('People', 'echoes_wp_sparql_plugin'),
                          'years' => __('Birth Years', 'echoes_wp_sparql_plugin'),
			  'yearsDeath' => __('Death Years', 'echoes_wp_sparql_plugin'),
                          'year' => __('Year', 'echoes_wp_sparql_plugin')
                ];
                $type = $_GET['type'];
		$url = $_GET['url'];
		$concept = $_GET['concept'];
		$name = $_GET['name'];
		$year = $_GET['year'];
		$years = $_GET['years'];
		$place = $_GET['place'];
		$graph = $_GET['graph'];
		$typeDO = $_GET['typeDO'];
		$yearsDeath = $_GET['yearsDeath'];

		$title = (!empty($name) || !empty($year) || !empty($years) || !empty($concept) || !empty($place) || !empty($graph) || !empty($typeDO) || !empty($yearsDeath)) ? __('Searching by', 'echoes_wp_sparql_plugin') . " " . $name . " " . $year . " " . $years . " " . $concept . " " . $place . " " . explode(":", $graph)[1] . " " . $typeDO . " " . $yearsDeath : $types[$type] . " " . __('Full List', 'echoes_wp_sparql_plugin');
		$this->set('url', $url);
		$this->set('type', $type);
                $this->set('nameFacet', $name);
		$this->set('conceptFacet', $concept);
                $this->set('yearFacet', $year);
		$this->set('yearsFacet', $years);
		$this->set('placeFacet', $place);
                $this->set('graphFacet', $graph);
                $this->set('typeDOFacet', $typeDO);
		$this->set('yearsDeathFacet', $yearsDeath);
		$this->set('changeTitle', true);
		$this->set('title', $title);
		$this->render_view('searches/search', array('layout' => 'public_improved'));

                return $buckets;
        }

	public function getAllOptionsFacets() {
		$this->load_model('Elastic');
		$elasticClient = new Elastic();
		$types = ['concept' => 'http://www.europeana.eu/schemas/edm/isRelatedTo.@id.keyword',
			  'name' => 'http://xmlns.com/foaf/0.1/name.@value.keyword',
			  'years' => 'http://rdvocab.info/ElementsGr2/dateOfBirth.@value.date',
			  'yearsDeath' => 'http://rdvocab.info/ElementsGr2/dateOfDeath.@value.date',
			  'year' => 'http://purl.org/dc/elements/1.1/date.@value.date'
			 ];
		$type = $_GET['type'];
		$url = ($type == 'years' || $type == 'year') ? 'timespans' : $_GET['url'];
		$afterKey = $_GET['afterKey'];
                $concept = $_GET['conceptFacet'];
                $name = $_GET['nameFacet'];
                $year = $_GET['yearFacet'];
                $years = $_GET['yearsFacet'];
		$place = $_GET['placeFacet'];
		$graph = $_GET['graphFacet'];
                $typeDO = $_GET['typeDOFacet'];
		$yearsDeath = $_GET['yearsDeathFacet'];

		$target = ($type == 'years' || $type == 'year' || $type = 'yearsDeath')  ? 'date' : 'result';
		
                $response = $elasticClient->getAllAggregation($types[$type], $afterKey, $url, $type, $name, $concept, $year, $years, $place, $graph, $typeDO, $yearsDeath);
                $buckets = $response['aggregations']['result']['buckets'];

		if($type != 'name') {
                	while (isset($response['aggregations']['result']['buckets']) && count($response['aggregations']['result']['buckets']) > 0) {
                        	$afterKey = $response['aggregations']['result']['after_key'][$target];
                        	$response = $elasticClient->getAllAggregation($types[$type], $afterKey, $url, $type, $name, $concept, $year, $years, $place, $graph, $typeDO, $yearsDeath);
                        	$elements = $response['aggregations']['result']['buckets'];

                        	for($i = 0; $i < count($elements); $i++) {
                                	$buckets[] = $elements[$i];
                        	}
                	}
                	usort($buckets, function($a, $b) {
                        	return $b['doc_count'] <=> $a['doc_count'];
                	});
		}else {
			$count = 0;
			while (isset($response['aggregations']['result']['buckets']) && count($response['aggregations']['result']['buckets']) > 0 && $count < 8) {
				$afterKey = $response['aggregations']['result']['after_key'][$target];
                        	$response = $elasticClient->getAllAggregation($types[$type], $afterKey, $url, $type, $name, $concept, $year, $years, $place, $graph, $typeDO, $yearsDeath);
                        	$elements = $response['aggregations']['result']['buckets'];
 
                        	for($j = 0; $j < count($elements); $j++) {
                        		$buckets[] = $elements[$j];
                        	}
				$count++;
			}

		}
                $objects = [];
                foreach ($buckets as $bucket){
			$parameter = ($type == 'years') ? 'year' : $type; 
			if($type == 'years') {
				$agentOrDO = '&type=agent';
			}elseif($type == 'year'){
				$agentOrDO = '&type=providedCHO';
			}elseif($type == 'name'){
				$agentOrDO = '&type=keyword';
			}
			$paramName = (!empty($name) && $url == "agents") ? "&name=" . $name . "&type=keyword" : "";
			$paramConcept = (!empty($concept)) ? "&concept=" . $concept : "";
			$paramYear = (!empty($year)) ? "&year=" . $year : "";
			$paramsYears = (!empty($years)) ? "&years=" . $years : "";
			$data = ($type == 'concept') ? explode(":" , $bucket['key'][$target])[count(explode(":" , $bucket['key'][$target])) - 1] : $bucket['key'][$target];
                        $objects[] = ['html' => "<img src='" . get_bloginfo('template_url') . "/img/Assets/{$this->photoFacets[$type]}' />&nbsp;&nbsp;<a href='" . WP_SITEURL . "/{$url}/?{$parameter}={$bucket['key'][$target]}{$agentOrDO}{$paramName}{$paramConcept}{$paramYear}{$paramsYears}'>{$data} ({$bucket['doc_count']})</a>", 'afterKey' => $response['aggregations']['result']['after_key'][$target]];
                }
		die(json_encode($objects));
	}
}

