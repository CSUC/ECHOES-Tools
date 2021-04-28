<?php

require_once dirname(__FILE__) . '/model.php';

class Elastic extends model {

	function connectClientElastic() {

		$url = get_site_url() . '/wordpress/wp-content/plugins/echoes_wp_sparql_plugin/app/elastic/vendor/autoload.php';
		var_dump($url);
		require $url;

		$hosts = ['http://echoes-elastic.pre.csuc.cat'];
		
		$client = ClientBuilder::create()           // Instantiate a new ClientBuilder
                    ->setHosts($hosts)      		    // Set the hosts
                    ->build();
		
		return $client;
	}
}

?>
