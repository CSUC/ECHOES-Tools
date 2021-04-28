<?php

class EchoesWpSparqlPluginSettings extends MvcSettings {

  var $settings = array(
    'sparql_endpoint' => array(
      'type' => 'text',
      'default' => WP_SITEURL . '/namespace/{namespace}/sparql',
      'label' => 'Endpoint (use {namespace} placeholder for namespace)'
    ),
    'sparql_default_namespace' => array(
      'type' => 'text',
      'default' => 'echoes',
      'label' => 'Default namespaces (separated with comma)'
    ),
    'sparql_default_graph' => array(
      'type' => 'text',
      'default' => 'all',
      'label' => 'Default graphs (separated with comma) or \'all\''
    ),
    'sparql_query_default_graph' => array(
      'type' => 'checkbox',
      'default' => 0,
      'label' => '(Debug) Also query entities with no graph'
    ),
    'sparql_proxy' => array(
      'type' => 'text',
      'default' => '',
      'label' => 'Proxy'
    ),
    'sparql_enable_agent_subclassing' => array(
      'type' => 'checkbox',
      'default' => 0,
      'label' => '(Experimental) Enable agent subclassing'
    ),
  );
  
}
