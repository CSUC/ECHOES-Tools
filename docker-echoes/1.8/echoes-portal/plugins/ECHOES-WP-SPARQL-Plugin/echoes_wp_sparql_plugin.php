<?php
/*
* Plugin Name: ECHOES WP SPARQL Plugin
* Plugin URI: https://github.com/CSUC/ECHOES-WP-SPARQL-Plugin
* Description: Queries a SPARQL endpoint based on EDM RDF metadata to show different visualizations
* Author: CSUC
* Version: 1.0
* Author URI: http://www.csuc.cat/en
* Text Domain: echoes_wp_sparql_plugin
* Domain Path: /languages
*/

register_activation_hook(__FILE__, 'echoes_wp_sparql_plugin_activate');
register_deactivation_hook(__FILE__, 'echoes_wp_sparql_plugin_deactivate');

function echoes_wp_sparql_plugin_activate() {
    global $wp_rewrite;
    require_once dirname(__FILE__).'/echoes_wp_sparql_plugin_loader.php';
    $loader = new EchoesWpSparqlPluginLoader();
    $loader->activate();
    $wp_rewrite->flush_rules( true );
}

function echoes_wp_sparqlplugin_deactivate() {
    global $wp_rewrite;
    require_once dirname(__FILE__).'/echoes_wp_sparql_plugin_loader.php';
    $loader = new EchoesWpSparqlPluginLoader();
    $loader->deactivate();
    $wp_rewrite->flush_rules( true );
}

add_action('plugins_loaded', 'echoes_wp_sparqlplugin_textdomain');

function echoes_wp_sparqlplugin_textdomain() {
	
	$text_domain	= 'echoes_wp_sparql_plugin';
	$path_languages = basename(dirname(__FILE__)).'/languages/';

 	load_plugin_textdomain($text_domain, false, $path_languages );
}
