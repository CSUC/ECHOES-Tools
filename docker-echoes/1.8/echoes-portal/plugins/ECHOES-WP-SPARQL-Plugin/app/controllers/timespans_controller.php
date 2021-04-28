<?php
class TimespansController extends MvcPublicController {

    public function index(){
        $_GET['s'] = null;
        $_GET['page'] = 1;
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
        $type = $_GET['type'];
	$year = $_GET['year'];
	$typeDO = $_GET['typeDO'];
	$concept = $_GET['concept'];
	$typeYear = $_GET['typeYear'];

        if (empty($scroll->getNextScroll())) {
               	$raw_objects = $elasticClient->searchYearTime(true, $year, $type, $graph, $typeDO, $concept, $typeYear);
               	$count = $raw_objects['hits']['total']['value'];
               	$scroll->setTotal($count);
               	$scroll->setNumPages(ceil($count/$elasticClient->getCountPage()));
               	$scroll->setNextScroll($raw_objects['_scroll_id']);
               	(empty($name)) ? : $scroll->setName($name);
        } else if ($_POST['move'] == 'next') {
               	$raw_objects = $elasticClient->nextScroll($scroll, $_POST['move'], 'timespansYear');
               	$scroll->setPage($scroll->getPage() + 1);
               	$scroll->setNextScroll($raw_objects['_scroll_id']);
        } else if ($_POST['move'] == 'back') {
               	$raw_objects = $elasticClient->reScroll($scroll, $_POST['move'], 'timespansYear');
               	$scroll->setPage($scroll->getPage() - 1);
               	$scroll->setNextScroll($raw_objects['_scroll_id']);
        }

        $scroll->updateScrollTime($graph, $type, $year, $typeDO, $concept, $typeYear);
        $facetsName = $elasticClient->searchYearTime(false, $year, $type, $graph, $typeDO, $concept, $typeYear);
        $facets->updateFacetsTime($facetsName['aggregations'], $graph, $type, $year, $typeDO, $concept, $typeYear);
	$facets->setUrlType('timespans');
        $this->set('title', __('Searching by year', 'echoes_wp_sparql_plugin') . " " .$year);
        $total = number_format_i18n($scroll->getTotal());
        $this->set('search_title', $total);
        $objects = [];
        
	foreach ($raw_objects['hits']['hits'] as $source){
            $object = $source['_source'];
	    $isAgent = strpos($object['@id'], 'Agent');
	    if ($isAgent === false) {
		$name = $object['http://purl.org/dc/elements/1.1/title']['@value'];
		$photoPath = 'ico-digital-objets-violet.svg';
		$typePath = 'providers';
	    } else {
            	$name = (!empty($object['http://xmlns.com/foaf/0.1/name']['@value'])) ? $object['http://xmlns.com/foaf/0.1/name']['@value'] : $object['http://www.w3.org/2004/02/skos/core#prefLabel']['@value'] . " " . $object['http://www.w3.org/2004/02/skos/core#altLabel']['@value'];
		$photoPath = "ico-people-violet.svg";
		$typePath = 'agents';
	    }
            $dateOfBirth = (array_key_exists('http://rdvocab.info/ElementsGr2/dateOfBirth', $object)) ? date("d/m/Y", strtotime($object['http://rdvocab.info/ElementsGr2/dateOfBirth']['@value'])) : '';
            $placeOfBirth = (array_key_exists('http://rdvocab.info/ElementsGr2/placeOfBirth', $object)) ? urldecode(explode(':', $object['http://rdvocab.info/ElementsGr2/placeOfBirth']['@id'])[1]) : '';
	    $dateOfDeath = (array_key_exists('http://rdvocab.info/ElementsGr2/dateOfDeath', $object)) ? date("d/m/Y", strtotime($object['http://rdvocab.info/ElementsGr2/dateOfDeath']['@value'])) : '';
            $placeOfDeath = (array_key_exists('http://rdvocab.info/ElementsGr2/placeOfDeath', $object)) ? urldecode(explode(':', $object['http://rdvocab.info/ElementsGr2/placeOfDeath']['@id'])[1]) : '';
            $showBirth = (!empty($dateOfBirth) || !empty($placeOfBirth)) ? "<div class='people-details-text'><img src='" . get_bloginfo('template_url') . "/img/Assets/ico-birth.svg' style='width: 13px;'/>&nbsp;&nbsp;{$placeOfBirth}, {$dateOfBirth}</div>" : '';
            $showDead = (!empty($dateOfDeath)  || !empty($placeOfDeath)) ? "<div class='people-details-text'><img src='" . get_bloginfo('template_url') . "/img/Assets/ico-died.svg' />&nbsp;&nbsp;{$placeOfDeath},  {$dateOfDeath}</div>" : '';
            $objects[] = "<img src='" . get_bloginfo('template_url') . "/img/Assets/{$photoPath}' />&nbsp;&nbsp;<a href='" . WP_SITEURL . $current_locale . "/{$typePath}/details?subject={$object['@id']}'>{$name}</a>" . $showBirth . $showDead;
        }
        $this->set('objects', $objects);
        $serializeScroll = serialize($scroll);
        $this->set('serializeScroll', $serializeScroll);
        $this->set('scroll', $scroll);
        $this->set('search_bar', false);
	$this->set('search_barYear', true);
        $this->set('facets', $facets);
        $this->set('print_legend', true);
        $this->set('agents_legend', true);
        $this->set('chos_legend', false);
        $this->set('changeTitle', true);
        $this->render_view('general/search', array('layout' => 'public_improved'));

    }

    public function heatmap(){

        if (!isset($_GET['date_start']) || !isset($_GET['date_end'])){
            header('Location: ' . WP_SITEURL . '/timespans/heatmap?date_start=' . $_GET['date_start'] . '&date_end=' . $_GET['date_end']);
        }

        $this->load_helper('Choagents');
        $this->load_helper('Pagination');

        $count_per_page = 3;
        $count = ($_GET['date_end'] - $_GET['date_start']) + 1;
        $this->set('page', $_GET['page']);
	$this->set('type', $_GET['type']);
        $this->set('date_start', $_GET['date_start']);
        $this->set('date_end', $_GET['date_end']);
        $this->set('num_pages', $this->choagents->get_num_pages($count, $count_per_page));
    }

    public function getValuesHeatmap() {
        $this->load_model('Elastic');
        $elasticClient = new Elastic();
	$start = $_GET['start'];
	$end = $_GET['end'] + 2;
	$year = $_GET['start'];
	$type = $_GET['type'];
        $values = [];
        $index = 0;

	do {
        	$response = $elasticClient->getHeatMap($type, $start, $end, $afterKey);
                $objects = $response['aggregations']['result']['buckets'];
                $afterKey = $response['aggregations']['result']['after_key']['date'];

                foreach($objects as $object){
                        $currentYear = explode('-', $object['key']['date'])[0];
                        if ($currentYear != $year ) {
                                array_push($values, $info);
                                $values[$index]['year'] = $year;
                                $index++;
                                $info = [];
                                $year = $currentYear;
                        }
                        $info['info'][] = [ 'count' => $object['doc_count'], 'days' => $object['key']['date']];
                }

        } while (isset($response['aggregations']['result']['buckets']) && count($response['aggregations']['result']['buckets']) > 0);
	
        die(json_encode($values));
    }

    public function list_chos(){
        $this->load_model('Provider');
        $this->load_helper('Icons');
        $this->load_helper('Choagents');
        $fake_objects = [1 => ["subjects" => [], 'providedCHOs' => []]];
        $chos = $this->choagents->fill_objects_with_chos($fake_objects, $this->Provider->get_providedCHO_by_date($_GET['date']));
        $this->set('date', $_GET['date']);
        $this->set('chos', $chos);
        $this->render_view('providers/list', array('layout' => 'none'));
    }

    public function list_agents(){
        $this->load_model('Agent');
        $this->load_helper('Icons');
        $this->load_helper('Choagents');
        $fake_objects = [1 => ["subjects" => [], 'providedCHOs' => []]];
        $agents = $this->Agent->get_agents_by_date($_GET['date'])->results->bindings;
        $this->set('agents', $agents);
        $this->render_view('agents/list', array('layout' => 'none'));
    }
}
