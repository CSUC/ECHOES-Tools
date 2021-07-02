<?php
require_once(dirname(__FILE__) . '/config.php');

session_start();
$NAMESPACES = explode(',', mvc_setting('EchoesWpSparqlPluginSettings', 'sparql_default_namespace'));
if (isset($_GET['namespace'])){
    $NAMESPACES = explode(',', $_GET['namespace']);
    $_SESSION['NAMESPACES'] = $NAMESPACES;
} else if (isset($_SESSION['NAMESPACES'])){
    $NAMESPACES = $_SESSION['NAMESPACES'];
}

$GRAPHS = explode(',', mvc_setting('EchoesWpSparqlPluginSettings', 'sparql_default_graph'));
if (isset($_GET['graph'])){
    $GRAPHS = explode(',', $_GET['graph']);
    $_SESSION['GRAPHS'] = $GRAPHS;
} else if (isset($_SESSION['GRAPHS'])){
    $GRAPHS = $_SESSION['GRAPHS'];
}

define('SPARQL_NAMESPACES', $NAMESPACES);
define('SPARQL_GRAPHS', $GRAPHS);
define('SPARQL_PROXY', mvc_setting('EchoesWpSparqlPluginSettings', 'sparql_proxy'));
define('SPARQL_URL', mvc_setting('EchoesWpSparqlPluginSettings', 'sparql_endpoint'));
define('ELASTIC_URL', mvc_setting('EchoesWpSparqlPluginSettings', 'elastic_endpoint'));
define('ELASTIC_PORT', mvc_setting('EchoesWpSparqlPluginSettings', 'elastic_port'));
define('ELASTIC_SCHEME', mvc_setting('EchoesWpSparqlPluginSettings', 'elastic_scheme'));
