<?php

class FacetsHelper extends MvcHelper {

	private $name;
	private $graphs;
	private $place;
        private $year;
	private $years;
	private $typeYear;
	private $yearsDeath;
        private $type;
	private $urlType;
	private $typeDO;
	private $concept;
	private $options;
	private $maxViews = 5;
	private $nameFacets;
	private $nameAggregations = ["graphs", "names", "rdaGr2:dateOfBirth", "rdaGr2:dateOfDeath", "dc:date", "type", "typeDO", "concepts"];
	private $nameParameters = ["graph", "name", "years", "yearsDeath", "year", "type", "typeDO", "concept", "place"];
	private $photoFacets = [
				"graphs" => "institution.png",
				"names" => "ico-people-violet.svg",
				"rdaGr2:dateOfBirth" => "ico-birth.svg",
				"rdaGr2:dateOfDeath" => "ico-died.svg",
				"People" => "ico-people-violet.svg",
				"dc:date" => "ico-digital-objets-violet.svg",
				"http://www.europeana.eu/schemas/edm/ProvidedCHO" => "ico-digital-objets-violet.svg",
				"http://www.europeana.eu/schemas/edm/Agent" => "ico-people-violet.svg",
				"typeDO" => "ico-digital-objets-violet.svg",
				"concepts" => "ico-concepts-violet.svg"
			       ];

	function __construct() {
		$this->nameFacets = [
			__('Institution', 'echoes_wp_sparql_plugin'), 
			__('Name', 'echoes_wp_sparql_plugin'), 
			__('Year of birth', 'echoes_wp_sparql_plugin'),
                        __('Year of death', 'echoes_wp_sparql_plugin'),
			__('Year of Digital Object', 'echoes_wp_sparql_plugin'),
			__('Type', 'echoes_wp_sparql_plugin'),
			__('Type Digital Object', 'echoes_wp_sparql_plugin'),
			__('Concepts', 'echoes_wp_sparql_plugin')
		];
	}

	private function get_current_locale() {
                $translation = new TranslationHelper();
                return $translation->get_current_locale();
	}


        public function getOptions() {
                return $this->options;
        }

        public function setOptions($options) {
                $this->options = $options;
        }

        public function getTypeYear() {
                return $this->typeYear;
        }

        public function setTypeYear($typeYear) {
                $this->typeYear = $typeYear;
        }

        public function getName() {
                return $this->name;
        }
	
        public function setName($name) {
                $this->name = $name;
        }

        public function getGraphs() {
                return $this->graphs;
        }

        public function setGraphs($graphs) {
                $this->graphs = $graphs;
        }

        public function getPlace() {
                return $this->place;
        }

	public function setPlace($place) {
		$this->place = $place;
	}

        public function getYear() {
                return $this->year;
        }

        public function setYear($year) {
                $this->year = $year;
        }

        public function getYears() {
                return $this->years;
        }

        public function setYears($years) {
                $this->years = $years;
        }

        public function getYearsDeath() {
                return $this->yearsDeath;
        }

        public function setYearsDeath($yearsDeath) {
                $this->yearsDeath = $yearsDeath;
        }

        public function getType() {
                return $this->type;
        }

        public function setType($type) {
                $this->type = $type;
        }

        public function getUrlType() {
                return $this->urlType;
        }

        public function setUrlType($urlType) {
                $this->urlType = $urlType;
        }

        public function getTypeDO() {
                return $this->typeDO;
        }

        public function setTypeDO($typeDO) {
                $this->typeDO = $typeDO;
        }

        public function getConcept() {
                return $this->concept;
        }

        public function setConcept($concept) {
                $this->concept = $concept;
        }

  	public function updateFacetsTime($options, $graph, $type, $year, $typeDO, $concept, $typeYear) {
        	$this->graphs = $graph;
        	$this->year = $year;
        	$this->type = $type;
		$this->typeDO = $typeDO;
		$this->options = $options;
		$this->concept = $concept;
		$this->typeYear = $typeYear;
	}

	public function updateFacetsPeople($options, $name, $graph, $years, $place, $concept, $yearsDeath) {
		$this->name = $name;
        	$this->graphs = $graph;
        	$this->place = $place;
        	$this->options = $options;
                $this->years = $years;
		$this->concept = $concept;
		$this->yearsDeath = $yearsDeath;
	}

        public function updateFacetsDO($options, $place, $graph, $typeDO, $concept, $year) {
                $this->graphs = $graph;
                $this->place = $place;
		$this->typeDO = $typeDO;
                $this->options = $options;
		$this->concept = $concept;
		$this->year = $year;
        }

	private function getParameters($parameter) {
		$returnParameter = "";
		switch ($parameter) {
   	 			case 'name':
        				$returnParameter .= (empty($this->graphs)) ? "" : "graph=" . $this->graphs ."&";
					$returnParameter .= (empty($this->years)) ? "" : "years=" . $this->years ."&";
					$returnParameter .= (empty($this->yearsDeath)) ? "" : "yearsDeath=" . $this->yearsDeath ."&";
					$returnParameter .= (empty($this->place)) ? "" : "place=" . $this->place ."&";
					$returnParameter .= (empty($this->concept)) ? "" : "concept=" . $this->concept ."&";
        				break;
    				case 'graph':
        				$returnParameter .= (empty($this->name)) ? "" : "name=" . $this->name ."&";
					$returnParameter .= (empty($this->years)) ? "" : "years=" . $this->years ."&";
					$returnParameter .= (empty($this->yearsDeath)) ? "" : "yearsDeath=" . $this->yearsDeath ."&";
					$returnParameter .= (empty($this->place)) ? "" : "place=" . $this->place ."&";
                                        $returnParameter .= (empty($this->type)) ? "" : "type=" . $this->type ."&";
					$returnParameter .= (empty($this->year)) ? "" : "year=" . $this->year ."&";
					$returnParameter .= (empty($this->typeDO)) ? "" : "typeDO=" . $this->typeDO ."&";
					$returnParameter .= (empty($this->concept)) ? "" : "concept=" . $this->concept ."&";
					$returnParameter .= (empty($this->typeYear)) ? "" : "typeYear=" . $this->typeYear ."&";
        				break;
    				case 'years':
        				$returnParameter .= (empty($this->name)) ? "" : "name=" . $this->name ."&";
					$returnParameter .= (empty($this->graphs)) ? "" : "graph=" . $this->graphs ."&";
					$returnParameter .= (empty($this->place)) ? "" : "place=" . $this->place ."&";
					$returnParameter .= (empty($this->concept)) ? "" : "concept=" . $this->concept ."&";
					$returnParameter .= (empty($this->yearsDeath)) ? "" : "yearsDeath=" . $this->yearsDeath ."&";
					$returnParameter .= (empty($this->type)) ? "" : "type=" . $this->type ."&";
       	 				break;
                                case 'yearsDeath':
                                        $returnParameter .= (empty($this->name)) ? "" : "name=" . $this->name ."&";
                                        $returnParameter .= (empty($this->graphs)) ? "" : "graph=" . $this->graphs ."&";
                                        $returnParameter .= (empty($this->place)) ? "" : "place=" . $this->place ."&";
                                        $returnParameter .= (empty($this->concept)) ? "" : "concept=" . $this->concept ."&";
					$returnParameter .= (empty($this->years)) ? "" : "years=" . $this->years ."&";
					$returnParameter .= (empty($this->type)) ? "" : "type=" . $this->type ."&";
                                        break;
                                case 'type':
                                        $returnParameter .= (empty($this->graphs)) ? "" : "graph=" . $this->graphs ."&";
                                        $returnParameter .= (empty($this->year)) ? "" : "year=" . $this->year ."&";
                                        $returnParameter .= (empty($this->typeDO)) ? "" : "typeDO=" . $this->typeDO ."&";
					$returnParameter .= (empty($this->concept)) ? "" : "concept=" . $this->concept ."&";
					$returnParameter .= (empty($this->typeYear)) ? "" : "typeYear=" . $this->typeYear ."&";
                                        break;
                                case 'year':
                                        $returnParameter .= (empty($this->graphs)) ? "" : "graph=" . $this->graphs ."&";
                                        $returnParameter .= (empty($this->place)) ? "" : "place=" . $this->place ."&";
					$returnParameter .= (empty($this->type)) ? "" : "type=" . $this->type ."&";
                                        $returnParameter .= (empty($this->typeDO)) ? "" : "typeDO=" . $this->typeDO ."&";
					$returnParameter .= (empty($this->concept)) ? "" : "concept=" . $this->concept ."&";
					break;
				case 'typeDO':
                                        $returnParameter .= (empty($this->graphs)) ? "" : "graph=" . $this->graphs ."&";
                                        $returnParameter .= (empty($this->place)) ? "" : "place=" . $this->place ."&";
                                        $returnParameter .= (empty($this->type)) ? "" : "type=" . $this->type ."&";
                                        $returnParameter .= (empty($this->year)) ? "" : "year=" . $this->year ."&";
					$returnParameter .= (empty($this->concept)) ? "" : "concept=" . $this->concept ."&";
					$returnParameter .= (empty($this->typeYear)) ? "" : "typeYear=" . $this->typeYear ."&";
                                        break;
                                case 'concept':
					$returnParameter .= (empty($this->name)) ? "" : "name=" . $this->name ."&";
                                        $returnParameter .= (empty($this->graphs)) ? "" : "graph=" . $this->graphs ."&";
                                        $returnParameter .= (empty($this->years)) ? "" : "years=" . $this->years ."&";
                                        $returnParameter .= (empty($this->place)) ? "" : "place=" . $this->place ."&";
					$returnParameter .= (empty($this->typeDO)) ? "" : "typeDO=" . $this->typeDO ."&";
					$returnParameter .= (empty($this->year)) ? "" : "year=" . $this->year ."&";
					$returnParameter .= (empty($this->type)) ? "" : "type=" . $this->type ."&";
					$returnParameter .= (empty($this->typeYear)) ? "" : "typeYear=" . $this->typeYear ."&";
                                        break;
				case 'place':
                                        $returnParameter .= (empty($this->name)) ? "" : "name=" . $this->name ."&";
					$returnParameter .= (empty($this->graphs)) ? "" : "graph=" . $this->graphs ."&";
                                        $returnParameter .= (empty($this->years)) ? "" : "years=" . $this->years ."&";
					$returnParameter .= (empty($this->yearsDeath)) ? "" : "yearsDeath=" . $this->yearsDeath ."&";
                                        $returnParameter .= (empty($this->typeDO)) ? "" : "typeDO=" . $this->typeDO ."&";
                                        $returnParameter .= (empty($this->concept)) ? "" : "concept=" . $this->concept ."&";
					$returnParameter .= (empty($this->year)) ? "" : "year=" . $this->year ."&";
					$returnParameter .= (empty($this->typeYear)) ? "" : "typeYear=" . $this->typeYear ."&";
                                        break;
				case 'all':
					$returnParameter .= (empty($this->name)) ? "" : "name=" . $this->name ."&";
                                        $returnParameter .= (empty($this->graphs)) ? "" : "graph=" . $this->graphs ."&";
					$returnParameter .= (empty($this->place)) ? "" : "place=" . $this->place ."&";
                                        $returnParameter .= (empty($this->years)) ? "" : "years=" . $this->years ."&";
					$returnParameter .= (empty($this->yearsDeath)) ? "" : "yearsDeath=" . $this->yearsDeath ."&";
                                        $returnParameter .= (empty($this->typeDO)) ? "" : "typeDO=" . $this->typeDO ."&";
                                        $returnParameter .= (empty($this->concept)) ? "" : "concept=" . $this->concept ."&";
                                        $returnParameter .= (empty($this->year)) ? "" : "year=" . $this->year ."&";
					$returnParameter .= (empty($this->typeYear)) ? "" : "typeYear=" . $this->typeYear ."&";
                                        break;
		}
		return $returnParameter;
	}

	private function generateNameParameter($j, $i, $key, $option) {
        	if(($this->nameAggregations[$j] == 'rdaGr2:dateOfBirth' || $this->nameAggregations[$j] == 'rdaGr2:dateOfDeath') && strpos($this->options[$this->nameAggregations[$j]]['interval'], 'y') !== false){
                	if($i+1 !== count($this->options[$this->nameAggregations[$j]]['buckets'])){
                        	$nameOption = $option[$key] . " .... " . $this->options[$this->nameAggregations[$j]]['buckets'][$i+1][$key];
                                $parameters = $this->getParameters($this->nameParameters[$j]) . $this->nameParameters[$j] . "=" . $option[$key] . "-" . $this->options[$this->nameAggregations[$j]]['buckets'][$i+1][$key];
                        } else if (strpos($this->years, "-") !== false) {
                        	$nameOption = $option[$key] . " .... " . explode("-", $this->years)[1];
                                $parameters = $this->getParameters($this->nameParameters[$j]) . $this->nameParameters[$j] . "=" . $option[$key] . "-" . explode("-", $this->years)[1];
                        } else {
                        	$nameOption = $option[$key] . " .... " . date("Y");
                                $parameters = $this->getParameters($this->nameParameters[$j]) . $this->nameParameters[$j] . "=" . $option[$key];
                        }
                } else if($this->nameAggregations[$j] == 'dc:date' || $this->nameAggregations[$j] == 'rdaGr2:dateOfBirth' || $this->nameAggregations[$j] == 'rdaGr2:dateOfDeath') {
			$nameOption = $option[$key];
			$parameters = $this->getParameters($this->nameParameters[$j]) . "year=" . $option[$key] . "&typeYear=" . $this->nameParameters[$j];
		} else if ($this->nameAggregations[$j] == 'type') {
			$defineParam = [
                                'http://www.europeana.eu/schemas/edm/ProvidedCHO' => 'providedCHO',
                                'http://www.europeana.eu/schemas/edm/Agent' => 'agent'
                	];

                        $defineName = [
                                'http://www.europeana.eu/schemas/edm/ProvidedCHO' => 'Digital objects',
                                'http://www.europeana.eu/schemas/edm/Agent' => 'People'
                        ];

                        $nameOption = __($defineName[$option[$key]], 'echoes_wp_sparql_plugin');
                        $parameters = $this->getParameters($this->nameParameters[$j]) . $this->nameParameters[$j] . "=" . $defineParam[$option[$key]];
		} else {
                	$nameOption = ($this->nameAggregations[$j] == 'graphs' || $this->nameAggregations[$j] == 'concepts') ? explode(":" , $option[$key])[count(explode(":" , $option[$key])) - 1] : $option[$key];
                        $parameters = $this->getParameters($this->nameParameters[$j]) . $this->nameParameters[$j] . "=" . $option[$key];
                }
		$values = ["name" => $nameOption, "parameters" => $parameters];
		return $values;
	}

	private function selectFacetsToShow($j) {
		return ($this->nameAggregations[$j] == 'graphs' && count($this->options[$this->nameAggregations[$j]]['buckets']) != 0) ||
			($this->nameAggregations[$j] == 'dc:date' && count($this->options[$this->nameAggregations[$j]]['buckets']) != 0) ||
		       ($this->nameAggregations[$j] == 'type' && count($this->options[$this->nameAggregations[$j]]['buckets']) != 0) ||
		       ($this->nameAggregations[$j] == 'rdaGr2:dateOfBirth' && count($this->options[$this->nameAggregations[$j]]['buckets']) != 0 && (strpos($this->options[$this->nameAggregations[$j]]['interval'], 'y') !== false || empty($this->options[$this->nameAggregations[$j]]['interval']))) ||
                       ($this->nameAggregations[$j] == 'rdaGr2:dateOfDeath' && count($this->options[$this->nameAggregations[$j]]['buckets']) != 0 && (strpos($this->options[$this->nameAggregations[$j]]['interval'], 'y') !== false || empty($this->options[$this->nameAggregations[$j]]['interval']))) ||
		       ($this->nameAggregations[$j] == 'names' && count($this->options[$this->nameAggregations[$j]]['buckets']) != 0) ||
		       ($this->nameAggregations[$j] == 'typeDO' && count($this->options[$this->nameAggregations[$j]]['buckets']) != 0) ||
                       ($this->nameAggregations[$j] == 'concepts' && count($this->options[$this->nameAggregations[$j]]['buckets']) != 0);
	}

        public function show() {
                $siteUrl   = get_site_url();
                $current_locale = $this->get_current_locale();
                $photoPath = get_bloginfo('template_url');
                $index = 0;
                for($j = 0; $j < count($this->nameAggregations); $j++) {
	                $showMore = (($this->nameAggregations[$j] == 'rdaGr2:dateOfBirth' || $this->nameAggregations[$j] == 'rdaGr2:dateOfDeath') && !empty($this->options[$this->nameAggregations[$j]]['interval'])) ? __('Year by year', 'echoes_wp_sparql_plugin') : __('Show More', 'echoes_wp_sparql_plugin');
                        if ($this->selectFacetsToShow($j)) {
                        echo do_shortcode(
                        <<<EOT
				<div class="row" style="margin-top: 2em;">
    					<div class="col">
                                		<div class="row">
                                			<div class="col title-browse">
                                				<span class="categories"><img src="$photoPath/img/Assets/ico-menu-categories.svg" /><h3><strong>{$this->nameFacets[$j]}</strong></h3></span>
							</div>
						</div>
EOT
                        );
                        $photoPathFacet = get_bloginfo('template_url') . "/img/Assets/" . $this->photoFacets[$this->nameAggregations[$j]];
                        for($i = 0; $i < count($this->options[$this->nameAggregations[$j]]['buckets']); $i++) {
                                $style = ($this->nameAggregations[$j] != 'rdaGr2:dateOfBirth' || $this->nameAggregations[$j] != 'rdaGr2:dateOfDeath') ?: 'padding: 364px 45px 326px 0px;';
                                $option = $this->options[$this->nameAggregations[$j]]['buckets'][$i];
                                if ($option['doc_count'] == '0') {continue;};
				if ($i > $this->maxViews && empty($this->options[$this->nameAggregations[$j]]['interval'])) {break;};
                                $key = ($this->nameAggregations[$j] == 'rdaGr2:dateOfBirth' || $this->nameAggregations[$j] == 'dc:date' || $this->nameAggregations[$j] == 'rdaGr2:dateOfDeath') ? 'key_as_string' : 'key';
                                $total = number_format_i18n($option['doc_count']);
                                $optionsNameParameters = $this->generateNameParameter($j, $i, $key, $option);
                                $nameOption = urldecode($optionsNameParameters["name"]);
                                $nameOption = (strlen($nameOption) > 17) ? substr($nameOption, 0, 17) . "..." : $nameOption;
                                $parameters = $optionsNameParameters["parameters"];
				$photoPathFacet = ($this->nameAggregations[$j] == 'type') ? get_bloginfo('template_url') . "/img/Assets/" . $this->photoFacets[$option[$key]] : $photoPathFacet;
                        echo do_shortcode(
                        <<<EOT
				<div class="row">
					<div class="col">
                                		<div class="category-list row">
                                        		<div class="col-9" style="padding-left:0px">
								<img src="$photoPathFacet" /><a href="$siteUrl$current_locale/$this->urlType/?$parameters">&nbsp;&nbsp;&nbsp;&nbsp;{$nameOption}</a>
							</div>
                                        		<div class="col-2 badge badge-pill badge-primary" style="padding: 6px 0px 0px 0px;">
								{$total}
							</div>
                                		</div>
					</div>
				</div>
EOT
                        );
                        }
			if($this->nameParameters[$j] == 'concept' || $this->nameParameters[$j] == 'name' || $this->nameParameters[$j] == 'years' || $this->nameParameters[$j] == 'yearsDeath' || $this->nameParameters[$j] == 'year'){
				if (count($this->options[$this->nameAggregations[$j]]['buckets']) >= $this->maxViews) {
					$type = (($this->nameAggregations[$j] == 'rdaGr2:dateOfBirth' || $this->nameAggregations[$j] == 'rdaGr2:dateOfDeath') && empty($this->options[$this->nameAggregations[$j]]['interval'])) ? "year" : $this->nameParameters[$j];
					$textSearchParameter = "&" . $this->getParameters('all');
                        		echo do_shortcode(
                        		<<<EOT
							<div class="row">
								<div class="col category-list">
									<a href="$siteUrl$current_locale/searches/showAllOptionsFacets/?type={$type}&url=$this->urlType{$textSearchParameter}">&nbsp;&nbsp;&nbsp;&nbsp;{$showMore}</a>
								</div>
							</div>
EOT
					);
				}
			}
			echo do_shortcode(
			<<<EOT
                                </div>
                        </div>
EOT
                        );
                }
        }
        }


	private function isActiveParameter($parameter) {
                $activeParameter = false;
                switch ($parameter) {
                                case 'name':
                                        $activeParameter = (!empty($this->name)) ? true : false;
                                        break;
                                case 'graph':
                                        $activeParameter = (!empty($this->graphs)) ? true : false;
                                        break;
                                case 'years':
                                        $activeParameter = (!empty($this->years)) ? true : false;
                                        break;
                                case 'yearsDeath':
                                        $activeParameter = (!empty($this->yearsDeath)) ? true : false;
                                        break;
                                case 'type':
                                        $activeParameter = (!empty($this->type)) ? true : false;
                                        break;
                                case 'year':
                                        $activeParameter = (!empty($this->year)) ? true : false;
                                        break;
                                case 'typeDO':
                                        $activeParameter = (!empty($this->typeDO)) ? true : false;
                                        break;
                                case 'concept':
                                        $activeParameter = (!empty($this->concept)) ? true : false;
                                        break;
                                case 'place':
                                        $activeParameter = (!empty($this->place)) ? true : false;
                                        break;
                }
                return $activeParameter;
	
	}

        private function returnOptionFacets($parameter) {
                $nameOption = "";
                switch ($parameter) {
                                case 'name':
                                        $nameOption = $this->name;
                                        break;
                                case 'graph':
                                        $nameOption = $this->graphs;
                                        break;
                                case 'years':
                                        $nameOption = $this->years;
                                        break;	
                                case 'type':
                                        $nameOption = $this->type;
                                        break;
                                case 'year':
                                        $nameOption = $this->year;
                                        break;
                                case 'yearsDeath':
                                        $nameOption = $this->yearsDeath;
                                        break;
                                case 'typeDO':
                                        $nameOption = $this->typeDO;
                                        break;
                                case 'concept':
                                        $nameOption = $this->concept;
                                        break;
                                case 'place':
                                        $nameOption = $this->place;
                                        break;
                }
                return $nameOption;

        }

	public function ShowDeleteOptionFacets() {
                        $defineName = [
                                'providedCHO' => 'Digital objects',
                                'agent' => 'People'
                        ];
			$siteUrl   = get_site_url();
			$current_locale = $this->get_current_locale();
			echo do_shortcode(
			<<<EOT
				<div class="row sizeDeleteFacets" style="height: 2em;">	
EOT
                	);
			for ($i=0; $i < count($this->nameParameters); $i++) {
				$isActive = $this->isActiveParameter($this->nameParameters[$i]);
				if ($isActive) {
					$parameter = $this->getParameters($this->nameParameters[$i]);
					$name = $this->returnOptionFacets($this->nameParameters[$i]);
					$name = ($this->nameParameters[$i] == 'graph' || $this->nameParameters[$i] == 'concept' || $this->nameParameters[$i] == 'place') ? explode(":" , $name)[count(explode(":" , $name)) - 1] : $name;
					$name = ($this->nameParameters[$i] == 'type') ? __($defineName[$name], 'echoes_wp_sparql_plugin') : $name;
					$name = urldecode($name); 
					$name = (strlen($name) > 11) ? substr($name, 0, 11) . "..." : $name;
					echo do_shortcode(
                        		<<<EOT
						<div class="col-2 deleteFacets">
						   <a href="$siteUrl$current_locale/$this->urlType/?$parameter">
						   <div class="row">
                                                        <div class="col-8 textDeleteFacets">
                                                                $name
                                                        </div>
							<div class="col-3 textDeleteFacets">
								x
							</div>
						   </div>
						   </a>
						</div>	
EOT
					);
				}	
			}
			echo do_shortcode(
                	<<<EOT
                        	</div><br><br>
EOT
                	);
	
	}
}
