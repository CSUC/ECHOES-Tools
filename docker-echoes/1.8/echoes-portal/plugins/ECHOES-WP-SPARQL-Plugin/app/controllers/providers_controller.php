<?php

function cut_label($label, $providersController){
    if (strlen($label) > 25){
        return substr($label, 0, 22) . '...';
    }
    return $label;
}

function personId_to_label($personId, $providersController){
    if (count($providersController->Provider->get_label($personId)->results->bindings) === 0){
        return "";
    } else {
        return $providersController->Provider->get_label($personId)->results->bindings[0]->persons->value;
    }   
}

function decode_label($place_label, $providersController){
    return html_entity_decode($place_label, ENT_QUOTES | ENT_HTML5);
}

class ProvidersController extends MvcPublicController {


    public function index(){
        $this->search();      
    }

    public function search(){
	$this->load_helper('Details');
        $this->load_helper('Icons');
        $this->load_helper('Scroll');
        $this->load_helper('Facets');
        $this->load_helper('Choagents');
        $this->load_model('model');
        $this->load_model('Elastic'); 
        $this->load_helper('Translation');
        $translation = new TranslationHelper();
        $current_locale = $translation->get_current_locale();
        $elasticClient = new Elastic();
        $scroll = (empty($_POST['scroll'])) ? new ScrollHelper() : unserialize(base64_decode($_POST['scroll']));
        $facets = new FacetsHelper();
	$place = str_replace('\\', '', $_GET['place']);$_GET['place'];
	$graph = $_GET['graph'];
	$typeDO = $_GET['typeDO'];
	$conceptParameter = explode(':', $_GET['concept']);
        $concept = (!empty($conceptParameter[0])) ? $conceptParameter[0] . ':' . urlencode($conceptParameter[1]) : '';
	$year = $_GET['year'];

        if (empty($scroll->getNextScroll())) {
                $raw_objects = $elasticClient->searchProvided(true, $place, $graph, $typeDO, $concept, $year);
                $count = $raw_objects['hits']['total']['value'];
                $scroll->setTotal($count);
                $scroll->setNumPages(ceil($count/$elasticClient->getCountPage()));
                $scroll->setNextScroll($raw_objects['_scroll_id']);
        } else if ($_POST['move'] == 'next') {
                $raw_objects = $elasticClient->nextScroll($scroll, $_POST['move'], 'provided');
                $scroll->setPage($scroll->getPage() + 1);
                $scroll->setNextScroll($raw_objects['_scroll_id']);
        } else if ($_POST['move'] == 'back') {
                $raw_objects = $elasticClient->reScroll($scroll, $_POST['move'], 'provided');
                $scroll->setPage($scroll->getPage() - 1);
                $scroll->setNextScroll($raw_objects['_scroll_id']);
        }

        $scroll->updateScrollDO($place, $graph, $typeDO, $concept, $year);
        $facetsName = $elasticClient->searchProvided(false, $place, $graph, $typeDO, $concept, $year);
	for($i =0; $i < count($facetsName['aggregations']['concepts']['buckets']); $i++) {
		$labelConcept = $this->Provider->getConceptLabel($facetsName['aggregations']['concepts']['buckets'][$i]['key']);
		$facetsName['aggregations']['concepts']['buckets'][$i]['key'] = $labelConcept->results->bindings[0]->label->value;
	}
        $facets->updateFacetsDO($facetsName['aggregations'], $place, $graph, $typeDO, $concept, $year);
	$facets->setUrlType('providers');
	(!empty($place)) ? $this->set('title', __('Searching place', 'echoes_wp_sparql_plugin') . ' ' . $elasticClient->getLabelPlace($place)) : $this->set('title', __('Full list of digital objects', 'echoes_wp_sparql_plugin'));
	$scroll_total = $scroll->getTotal(); 
	$title = sprintf(_n('%s Digital Object', '%s Digital Objects', $scroll_total,'echoes_wp_sparql_plugin'), number_format_i18n($scroll->getTotal()));
	$this->set('search_title', "$title");
	$objects = [];

	foreach ($raw_objects['hits']['hits'] as $source){
		$object = $source['_source'];
		$objects[] = "<img src='" . get_bloginfo('template_url') . "/img/Assets/ico-digital-objets-violet.svg' />&nbsp;&nbsp;<a href='" . WP_SITEURL . $current_locale . "/providers/details?subject={$object['@id']}'>{$object['http://purl.org/dc/elements/1.1/title']['@value']}</a>";
	}
	
        $this->set('objects', $objects);
        $serializeScroll = serialize($scroll);
        $this->set('serializeScroll', $serializeScroll);
        $this->set('scroll', $scroll);
        $this->set('search_bar', false);
        $this->set('search_barYear', false);
        $this->set('facets', $facets);
        $this->set('print_legend', false);
        $this->set('agents_legend', false);
        $this->set('chos_legend', false);
        $this->set('changeTitle', true);
        $this->render_view('general/search', array('layout' => 'public_improved'));

    }

    public function details() {
        $this->load_helper('Icons');
        $this->load_helper('Details');
        $this->load_model('Agent');
	$this->load_model('Place');
        $this->load_helper('Translation');
	$this->load_model('Elastic');
	$elasticClient = new Elastic();
        $translation = new TranslationHelper();
        $current_locale = $translation->get_current_locale();
        $_GET['subject'] = str_replace('\\', '', $_GET['subject']);
	$isSubjectGene = (strpos($_GET['subject'], 'GENE') !== false || strpos($_GET['subject'], 'handle') !== false) ? true : false; 
        $res = $this->Provider->get_details($_GET['subject']);
	$placePrefLabels = "";
	$placeIdsLabels = explode('|',$res->results->bindings[0]->placeIds->value);
	foreach($placeIdsLabels as $ids) {
		$placePrefLabels .= $elasticClient->getLabelPlace($ids) . '|';
	}
	$placePrefLabels = substr_replace($placePrefLabels ,"",-1);
	$res->results->bindings[0]->placePrefLabels->value = $placePrefLabels;
        $dcCreators = explode('|', $res->results->bindings[0]->dcCreators->value);
        $dcCreatorLabels = [];
            foreach ($dcCreators as $dcCreator){
                if (!empty($dcCreator)){
                    if (strpos($dcCreator, 'Agent:') === 0 || strpos($dcCreator, 'Autor:') === 0) {
                        $dcCreatorLabels[] = $this->Agent->get_label($dcCreator)->results->bindings[0]->persons->value;
                    } else {
                        $dcCreatorLabels[] = $dcCreator;
                    }                    
                }
            }
        $res->results->bindings[0]->dcCreatorLabels->value = implode('|', $dcCreatorLabels);
        $showns = explode('|', $res->results->bindings[0]->showns->value);
        if (!empty($res->results->bindings[0]->personsId->value) ||
                !empty($res->results->bindings[0]->timeSpanIds->value) ||
                !empty($res->results->bindings[0]->conceptIds->value) ||
                !empty($res->results->bindings[0]->placeIds->value)
        ) {
            $label = $res->results->bindings[0]->titles->value;
            $label = (strlen($label) > 50) ? substr($label, 0, 50) . "..." : $label;  
            $graph = $this->constructGraph(
                    $res->results->bindings[0], [
                'id' => $res->results->bindings[0]->titles->value,
                'label' => $label,
                'group' => 'center',
                    ], [
                ['field_id' => 'persons', 'field_value' => 'persons', 'label_value' => '?relatedTos', 'label_function' => 'personId_to_label', 'group_value' => '?personsGender'],
                ['field_id' => 'historicalNewIds', 'field_value' => 'historicalNews', 'label_value' => __('Historical News', 'echoes_wp_sparql_plugin'), 'label_function' => 'cut_label', 'group' => 'historicalNews'],
                ['field_id' => 'timeSpanIds', 'field_value' => 'timeSpans', 'label_value' => __('TimeSpan', 'echoes_wp_sparql_plugin'), 'group' => 'timespans'],
                ['field_id' => 'conceptIds', 'field_value' => 'conceptPrefLabels', 'label_value' => __('Concept','echoes_wp_sparql_plugin'), 'group' => 'concepts'],
                ['field_id' => 'placeIds', 'field_value' => 'placePrefLabels', 'label_value' => __('Place', 'echoes_wp_sparql_plugin'), 'label_function' => 'decode_label', 'group' => 'places']
                    ]
            );
        }
        $this->set('title', __("Details of Digital Object", 'echoes_wp_sparql_plugin'));
        $subtitle = "";
        if ($res->results->bindings[0]->titles->value){
            $subtitle .= $this->icons->edm_type_map[$res->results->bindings[0]->edmInternalTypes->value] . " " . $res->results->bindings[0]->titles->value;
        }
        $this->set('print_legend', true);
        $this->set('agents_legend', false);
        $this->set('chos_legend', true);
	$this->set('search_bar', false);
	$this->set('cat_legend', true);
        $this->set('search_link', WP_SITEURL . $current_locale . '/providers/search'); 
        $this->set('subtitle', $subtitle);
	if ($isSubjectGene) {
		$this->set('extra_view', 'providers/detailsGene');
		$municipi_id = "<Municipality:" . explode(":", $_GET['subject'])[1] . ">";
		$objects = $this->Place->get_type_by_place_subject($municipi_id);
		$related = [];
		$imgRelated = [];
		foreach($objects->results->bindings as $object) {
			if($_GET['subject'] != $object->related->value){
				array_push($imgRelated,$this->Provider->imgCHO($object->related->value)->results->bindings[0]->shown->value);
				array_push($related,$object->related->value);
			}
		}
		$res->results->bindings[0]->imgRelatedProvidedCHO = $imgRelated;
		$res->results->bindings[0]->relatedProvidedCHO = $related;
        } else {
        	$this->set('extra_view', 'providers/details');
	}
        $this->set('extra_view_vars', [
            'bindings' => $res->results->bindings[0],
            'showns' => $showns,
            'graph' => $graph,
	    'annotations' => $annotations,
	    'provided' => $_GET['subject']
        ]);
        $this->render_view('general/details', array('layout' => 'public_improved'));
    }

    public function gene(){
	$this->load_helper('Icons');
	$this->load_helper('Details');
	$this->load_model('Place');
	$this->load_helper('Translation');
        $translation = new TranslationHelper();
        $current_locale = $translation->get_current_locale();
	$subject = $_GET['subject'];
	$res = $this->Provider->get_details($subject);
	$municipi_id = "<Municipality:" . explode(":", $subject)[1] . ">";
        $objectsPlace = $this->Place->get_type_by_place_subject($municipi_id);
	$objects = [];	
        foreach($objectsPlace->results->bindings as $object) {
        	if($_GET['subject'] != $object->related->value){
                	$objects[] = "<img src='" . get_bloginfo('template_url') . "/img/Assets/ico-digital-objets-violet.svg' />&nbsp;&nbsp;<a href='" . WP_SITEURL . $current_locale . "/providers/details?subject={$object->related->value}'>{$object->title->value}</a>";	
                }
        }
        $padding = (count($objects) > 25) ? 180 : count($objects) * 8;
        $padding = ($padding > 75)  ? $padding : 75;
        $this->set('objects', $objects);
        $this->set('padding', $padding);
        $this->set('print_legend', true);
        $this->set('agents_legend', false);
        $this->set('chos_legend', false);
        $this->set('changeTitle', true);

        $this->render_view('general/search', array('layout' => 'public_improved'));
    }

    private function constructGraph($results, $centralNode, $options) {
        $nodes = [$centralNode];
        $edges = [];	
        for ($j = 0; $j < count($options); $j++) {
            $field_value = $options[$j]['field_value'];
            $field_split = explode('|', $results->$field_value->value);
            $field_id = $options[$j]['field_id'];
            $id_split = explode('|', $results->$field_id->value);
            $label_value = $options[$j]['label_value'];
            if (substr($label_value, 0, 1) == '?') {
                $label_no_question = substr($label_value, 1);
                $label_value = explode('|', $results->$label_no_question->value);
            }    
            if (array_key_exists('group_value', $options[$j])){
                $group_value = $options[$j]['group_value'];
                $group_no_question = substr($group_value, 1);  
                $group_values = explode('|', $results->$group_no_question->value);
            }        
            for ($i = 0; $i < count($id_split); $i++) {
                if (array_key_exists('group', $options[$j])){
                    $group = $options[$j]['group'];
                } else if (array_key_exists('group_value', $options[$j])){                
                    $group = $group_values[$i];
                } else {
                    $group = $j + 1;
                }
                $id = $id_split[$i];
	
                if (array_key_exists('label_function', $options[$j])) {
                    $label = call_user_func($options[$j]['label_function'], $field_split[$i], $this);
                } else {
                    @$label = $field_split[$i];
                }

                if (empty($id) || empty($field_split[$i])){
                    continue;
                }

                $node = ['id' => $id, 'label' => $label, 'group' => $group];
                if (array_key_exists('prefix', $options[$j])){
                    $node['url'] = $options[$j]['prefix'] . $id;
                }
                if (array_key_exists('additional_properties', $options[$j])) {
                    foreach ($options[$j]['additional_properties'] as $property => $value) {
                        $node[$property] = $value;
                    }
                }
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
        return ['nodes' => json_encode($nodes), 'edges' => json_encode($edges)];
    }

    public function getInfoAnnotation($annotation){
	$this->load_model('model');
	$res = $this->model->getInfoAnnotation($annotation)->results->bindings;
	return $res;
    }

    public function getAnnotations(){
        $this->load_model('model');
        $res = $this->model->getAnnotations($_GET['provided'])->results->bindings;
        $annotations = [];
        $isCalixInfo = false;

        foreach($res as $annotation){
                foreach($this->getInfoAnnotation($annotation->body->value) as $info){
                        $isCalixInfo = (strpos($info->col1->value, 'creator') !== false && strpos($info->col2->value, 'calaix') !== false) ? true : $isCalixInfo;
                        if(strpos($info->col1->value, 'value') !== false) {
                                $jsonInfo = json_decode($info->col2->value);
                                $logoInfo = ($isCalixInfo) ? '<img src="/wp-content/themes/echoes/img/Assets/calaix-logo.png" style="width: 2em">&nbsp;&nbsp;' : '';
                                $annotations[] = '<div class="personal-info col-3">' . $logoInfo . '<a href="' . $jsonInfo->handle . '">' . $jsonInfo->title . '-' . $jsonInfo->type . '</a></div>';
                        }
                }
        }

        die(json_encode($annotations));

    }

    public function searchByDate(){
        $this->load_helper('Details');
        $this->load_helper('Pagination');
        $this->load_helper('Icons');
        $this->load_helper('Choagents');
        $this->load_helper('Translation');
        $translation = new TranslationHelper();
        $current_locale = $translation->get_current_locale();
        $count_per_page = 75;
        $count = $this->Provider->getCountByDate($_GET['date'])->results->bindings[0]->count->value;
        $cho = (empty($_GET['s'])) ? 'fill_objects_with_providers' : 'fill_objects_with_chos';
        $objects_with_cho = $this->Provider->get_providedCHO_by_date($_GET['date'], $count_per_page, $this->choagents->get_offset($_GET['page'],$count_per_page))->results->bindings;
        $this->set('search_link', WP_SITEURL . $current_locale . '/providers/search');
        $objects = [];

        foreach ($objects_with_cho as $object){
            $objects[] = $this->icons->get_providedCHO_icon($object->type->value) . ' <a target="_blank" href="' . WP_SITEURL . $current_locale . '/providers/details/?subject=' . $object->ProvidedCHO_subject->value . '">'. $object->ProvidedCHO_title->value . '</a>';
        }
        $padding = (count($objects) > 25) ? 180 : count($objects) * 8;
        $this->set('padding', $padding);
        $this->set('objects', $objects);
        $this->set('title', __('Digital Objects', 'echoes_wp_sparql_plugin'));
        $this->set('search_title', sprintf(esc_html(_n('%d Digital Object', '%d Digital Objects', $count, 'echoes_wp_sparql_plugin')), $count));
        $this->set('order_entity', 'providers');
        $this->set('num_pages', $this->choagents->get_num_pages($count, $count_per_page));
        $this->set('print_legend', true);
        $this->set('agents_legend', false);
        $this->set('chos_legend', true);
        $this->set('search_bar', false);
        $this->render_view('general/search', array('layout' => 'public_improved'));
    }

}
