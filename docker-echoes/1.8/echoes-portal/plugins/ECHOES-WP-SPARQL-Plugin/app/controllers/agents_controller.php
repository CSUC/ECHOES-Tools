<?php
class AgentsController extends MvcPublicController {

    public function index(){
        $this->search();      
    }

    public function search() {
        $this->load_helper('Details');
        $this->load_helper('Icons');
        $this->load_helper('Scroll');
	$this->load_helper('Facets');
        $this->load_helper('Choagents');
        $this->load_model('model');
	$this->load_model('Elastic');
	// Get language to redirect correctly
        $this->load_helper('Translation');
        $translation = new TranslationHelper();
        $current_locale = $translation->get_current_locale(); 
	$elasticClient = new Elastic();
	$scroll = (empty($_POST['scroll'])) ? new ScrollHelper() : unserialize(base64_decode($_POST['scroll']));
	$facets = new FacetsHelper();
	$graph = $_GET['graph'];
	$years = $_GET['years'];
	$placeParameter = explode(':', $_GET['place']);
	$place = (!empty($placeParameter[0])) ? $placeParameter[0] . ':' . urlencode($placeParameter[1]) : '';
	$concept = $_GET['concept'];
	$type = $_GET['type'];
	$yearsDeath = $_GET['yearsDeath'];
	
	if (!empty($_GET['first_name']) && !empty($_GET['last_name'])) {
		$name = $_GET['first_name'] ." ". $_GET['last_name'];
	} else if (!empty($_GET['first_name']) && empty($_GET['last_name'])){
		$name = $_GET['first_name'];
	} else if (empty($_GET['first_name']) && !empty($_GET['last_name'])){
		$name = $_GET['last_name'];
	} else if (!empty($_GET['name'])) {
		$name = $_GET['name'];
	} else {
		$name="";
	}

	if (empty($scroll->getNextScroll())) {
		$raw_objects = $elasticClient->searchPeople(true, $name, $graph, $years, $place, $concept, $type, $yearsDeath);
		$count = $raw_objects['hits']['total']['value'];
		$scroll->setTotal($count);
		$scroll->setNumPages(ceil($count/$elasticClient->getCountPage()));
		$scroll->setNextScroll($raw_objects['_scroll_id']);
		(empty($name)) ? : $scroll->setName($name);
	} else if ($_POST['move'] == 'next') {
		$raw_objects = $elasticClient->nextScroll($scroll, $_POST['move'], 'agents');
		$scroll->setPage($scroll->getPage() + 1);
		$scroll->setNextScroll($raw_objects['_scroll_id']);
	} else if ($_POST['move'] == 'back') {
		$raw_objects = $elasticClient->reScroll($scroll, $_POST['move'], 'agents');
		$scroll->setPage($scroll->getPage() - 1);
		$scroll->setNextScroll($raw_objects['_scroll_id']);
	}
	$scroll->updateScrollPeople($name, $graph, $years, $place, $concept, $type, $yearsDeath);
	$facetsName = $elasticClient->searchPeople(false, $name, $graph, $years, $place, $concept, $type, $yearsDeath);
	$facets->updateFacetsPeople($facetsName['aggregations'], $name, $graph, $years, $place, $concept, $yearsDeath);
	$facets->setUrlType('agents');
	$placeName = (!empty($place)) ? $elasticClient->getLabelPlace($place) : $name;
	(empty($placeName)) ? $this->set('title', __('Full list of people', 'echoes_wp_sparql_plugin')) : $this->set('title', __('Searching by', 'echoes_wp_sparql_plugin') . ' ' . $placeName);
	$num_people = $scroll->getTotal(); 
	$title = sprintf(_n('%s Person', '%s People', $num_people, 'echoes_wp_sparql_plugin'), number_format_i18n($num_people)); 
        $this->set('search_title', "$title"); 
        $objects = [];
        $gender_to_icon = [
            'Man' => 'ico-man',
            'Vrouw' => 'ico-woman',
            'Onbekend' => 'ico-people-violet'
        ];
        foreach ($raw_objects['hits']['hits'] as $source){
	    $object = $source['_source'];
	    $id = urlencode($object['@id']);
	    $name = (!empty($object['http://xmlns.com/foaf/0.1/name']['@value'])) ? $object['http://xmlns.com/foaf/0.1/name']['@value'] :$object['http://www.w3.org/2004/02/skos/core#prefLabel']['@value'] . " " . $object['http://www.w3.org/2004/02/skos/core#altLabel']['@value'];
	    $dateOfBirth = (array_key_exists('http://rdvocab.info/ElementsGr2/dateOfBirth', $object)) ? (strpos($this->returnFirstValueInArrayOrValue($object['http://rdvocab.info/ElementsGr2/dateOfBirth']), '-') === false) ? $this->returnFirstValueInArrayOrValue($object['http://rdvocab.info/ElementsGr2/dateOfBirth']) : date("d/m/Y", strtotime($this->returnFirstValueInArrayOrValue($object['http://rdvocab.info/ElementsGr2/dateOfBirth']))) : '';
            $placeOfBirth = (array_key_exists('http://rdvocab.info/ElementsGr2/placeOfBirth', $object)) ? $elasticClient->getLabelPlace($this->returnFirstValueInArrayOrValue($object['http://rdvocab.info/ElementsGr2/placeOfBirth'])) : '';
            $dateOfDeath = (array_key_exists('http://rdvocab.info/ElementsGr2/dateOfDeath', $object)) ? (strpos($this->returnFirstValueInArrayOrValue($object['http://rdvocab.info/ElementsGr2/dateOfDeath']), '-') === false) ? $this->returnFirstValueInArrayOrValue($object['http://rdvocab.info/ElementsGr2/dateOfDeath']) : date("d/m/Y", strtotime($this->returnFirstValueInArrayOrValue($object['http://rdvocab.info/ElementsGr2/dateOfDeath']))) : '';
            $placeOfDeath = (array_key_exists('http://rdvocab.info/ElementsGr2/placeOfDeath', $object)) ? $elasticClient->getLabelPlace($this->returnFirstValueInArrayOrValue($object['http://rdvocab.info/ElementsGr2/placeOfDeath'])) : '';
            $showBirth = (!empty($dateOfBirth) || !empty($placeOfBirth)) ? "<div class='people-details-text'><img src='" . get_bloginfo('template_url') . "/img/Assets/ico-birth.svg' style='width: 13px;'/>&nbsp;&nbsp;{$placeOfBirth}, {$dateOfBirth}</div>" : '';
            $showDead = (!empty($dateOfDeath)  || !empty($placeOfDeath)) ? "<div class='people-details-text'><img src='" . get_bloginfo('template_url') . "/img/Assets/ico-died.svg' />&nbsp;&nbsp;{$placeOfDeath},  {$dateOfDeath}</div>" : '';
	    $objects[] = "<img src='" . get_bloginfo('template_url') . "/img/Assets/{$gender_to_icon['Onbekend']}.svg' />&nbsp;&nbsp;<a href='" . WP_SITEURL . $current_locale . "/agents/details?subject={$id}'>{$name}</a>" . $showBirth . $showDead;
        }
        $this->set('objects', $objects);
	$serializeScroll = serialize($scroll);
	$this->set('serializeScroll', $serializeScroll); 
        $this->set('scroll', $scroll);
	$this->set('search_bar', true);
        $this->set('search_barYear', false);
	$this->set('facets', $facets);
        $this->set('print_legend', true);
        $this->set('agents_legend', true);
        $this->set('chos_legend', false);
	$this->set('changeTitle', true);
        $this->render_view('general/search', array('layout' => 'public_improved'));
    }

    public function details(){
        $this->load_helper('Icons');
        $this->load_helper('Details');
	$this->load_model('Provider');
        $this->load_helper('Translation');
        $translation = new TranslationHelper();
        $current_locale = $translation->get_current_locale();
        $_GET['subject'] = str_replace('\\', '', $_GET['subject']);   
        $res = $this->Agent->get_details($_GET['subject']);
	$annotations = $this->Agent->getAnnotations($_GET['subject']);
	$res->results->bindings[0]->label = $this->Agent->get_label($_GET['subject'])->results->bindings[0]->persons;
	$extraInfo = explode("|", $res->results->bindings[0]->extraInfo->value);
	foreach($extraInfo as $info){
		$wikipedia = (strpos($info, "wikipedia") === false) ? $wikipedia : $info;
		$viaf = (strpos($info, "viaf") === false) ? $viaf : $info;
	}
        $label = trim(chunk_split($label, 20));
        $labelCHO = $results->results->bindings[0]->titles->value;
        $labelCHO = trim(chunk_split($labelCHO, 20)); 
	$nameAgent = (!empty($res->results->bindings[0]->name->value)) ? $res->results->bindings[0]->name->value : $res->results->bindings[0]->label->value; 
        $graph = $this->constructGraph(
                 $res->results->bindings[0], [
                'id' => $nameAgent,
                'label' => $label,
                'group' => 'centerCHO',
                    ], 
		[
                ['field_id' => 'TimeSpan', 'field_value' => 'dateOfBirth', 'label_value' => __('Date Of Birth', 'echoes_wp_sparql_plugin'), 'group' => 'timespans'],
		['field_id' => 'TimeSpan', 'field_value' => 'dateOfDeath', 'label_value' => __('Date Of Death','echoes_wp_sparql_plugin'), 'group' => 'timespans'],
                ['field_id' => 'concepts', 'field_value' => 'conceptLabels', 'label_value' => __('Role', 'echoes_wp_sparql_plugin'), 'group' => 'concepts'],
		['field_id' => 'providers', 'field_value' => 'providerTitles', 'label_value' => __('Digital object', 'echoes_wp_sparql_plugin'), 'group' => 'digitalObjects'],
                ['field_id' => 'placeOfBirth', 'field_value' => 'placeOfBirthName', 'label_value' => __('Place Of Birth', 'echoes_wp_sparql_plugin'), 'label_function' => 'decode_label', 'group' => 'places'],
		['field_id' => 'placeOfDeath', 'field_value' => 'placeOfDeathName', 'label_value' => __('Place Of Death', 'echoes_wp_sparql_plugin'), 'label_function' => 'decode_label', 'group' => 'places'],
		['field_id' => 'hasMetPlace', 'field_value' => 'hasMetPlaceName', 'label_value' => __('Met Place','echoes_wp_sparql_plugin'), 'label_function' => 'decode_label', 'group' => 'places']
                    ]
            );

        $this->set('print_legend', true);
	$this->set('cat_legend', true);
        $this->set('agents_legend', true);
        $this->set('chos_legend', true);
	$this->set('search_bar', true);
        $this->set('search_link', WP_SITEURL . $current_locale . '/agents/search');
        $this->set('title', __('Details of Person', 'echoes_wp_sparql_plugin'));
        $this->set('subtitle', $nameAgent);
        $this->set('extra_view', 'agents/details');
        $this->set('extra_view_vars', [
            'results'   => $res,
	    'graph'     => $graph,
	    'wikipedia' => $wikipedia,
	    'viaf'      => $viaf,
	    'annotations' => $annotations,
            'agent' => $_GET['subject'],
            'nameAgent' => $nameAgent
        ]);

        $this->render_view('general/details', array('layout' => 'public_improved'));
    }

    private function constructGraph($results, $centralNode, $options) {
        $nodes = [$centralNode];

        for ($j = 0; $j < count($options); $j++) {
            $field_value = $options[$j]['field_value'];
	    $field_split = explode('|', $results->$field_value->value);
		
	    if ($options[$j]['field_id'] == 'TimeSpan' && !empty($results->$field_value->value)) {
		$id_split = [];
		$field_id = $options[$j]['field_id'];
		$date = explode('-', $results->$field_value->value);
		$year = $date[0];
	    	$id_split[] = $field_id . ":" . $year;
	    } else {
		$field_id = $options[$j]['field_id'];
                $id_split = explode('|', $results->$field_id->value);
            }
            $label_value = $options[$j]['label_value'];

            for ($i = 0; $i < count($id_split); $i++) {
                if (array_key_exists('group', $options[$j])){
                    $group = $options[$j]['group'];
                } else {
                    $group = $j + 1;
                }
                $id = $id_split[$i];
                if (empty($id)){
                    continue;
                }
                if (array_key_exists('label_function', $options[$j])) {
                    $label = call_user_func($options[$j]['label_function'], $field_split[$i], $this);
                } else {
                    @$label = $field_split[$i];
                }
		if ($options[$j]['field_id'] == 'TimeSpan' && !empty($results->$field_value->value)) {
                        $label = date("d/m/Y", strtotime($label));
                }

		$label = (strlen($label) > 50) ? substr($label, 0, 50) . "..." : $label;
                $node = ['id' => $id, 'label' => $label, 'group' => $group];
                
                $nodes[] = $node;
                if (!is_array($label_value)) {
                    $final_label_value = $label_value;
                } else {
                    $final_label_value = $label_value[$i];
                }
                if (strlen($final_label_value ) > 15){
                    $final_label_value = substr($final_label_value, 0, 15) . '...';
                }
                $edges[] = [
                    'font' => ['align' => 'top'],
                    'color' => ['inherit' => 'to'],
                    'from' => $nodes[0]['id'],
                    'to' => $id,
                    'label' => $final_label_value
                ];
            }
        }
	$nodes = array_values(array_unique( $nodes, SORT_REGULAR ));
        return ['nodes' => json_encode($nodes, JSON_PARTIAL_OUTPUT_ON_ERROR), 'edges' => json_encode($edges, JSON_PARTIAL_OUTPUT_ON_ERROR)];
    }

    public function getInfoAnnotation(){
        $this->load_model('model');
        $res = $this->model->getInfoAnnotation($_GET['annotation'])->results->bindings;
        die(json_encode($res));
    }

    public function getAnnotations(){
        $this->load_model('model');
        $res = $this->model->getAnnotations($_GET['agent'])->results->bindings;
        die(json_encode($res));
    }

    private function returnFirstValueInArrayOrValue($value){

	$valueReturn = array_key_exists('0', $value) ? $value[0][array_keys($value[0])[0]] : $value[array_keys($value)[0]];

	return $valueReturn;
    }
}
