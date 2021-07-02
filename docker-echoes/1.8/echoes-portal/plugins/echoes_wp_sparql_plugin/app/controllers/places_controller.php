<?php

class PlacesController extends MvcPublicController {
    private $last_nominamim_query = null;
    public function search() {
        $this->transform_search($this->Place->search($_GET['s']));
    }

    public function map(){
	$place = (!empty($_GET['place'])) ? $_GET['place'] : "";
	$elasticClient = new Elastic();
	$result = $elasticClient->ifMunicipiOrComarca($place);
	$this->set('place', $result);
    }

    public function getGeoSearch(){
	$elasticClient = new Elastic();
	$query = file_get_contents('php://input');
	header('Content-Type: application/json');
	die($elasticClient->getAllGeo($query));
    }

    public function getCityOnMap(){
	$elasticClient = new Elastic();
	$location = $_GET['q'];
	$resultsElastic = $elasticClient->getCityGeo($location);
	$results = [];
	$resultsElastic['hits']['hits'] = array_filter($resultsElastic['hits']['hits']);
	
	foreach($resultsElastic['hits']['hits'] as $result) {
		$results[] = ['loc' => [$result['_source']['http://www.w3.org/2003/01/geo/wgs84_pos#lat']['@value'], $result['_source']['http://www.w3.org/2003/01/geo/wgs84_pos#long']['@value']],'title' => $result['_source']['http://www.w3.org/2004/02/skos/core#prefLabel']['@value']];
	}
	die(json_encode($results));
    }

    public function index(){
        header('Location: ' . WP_SITEURL . '/places/map');
    }
}
