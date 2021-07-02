<?php

class Model extends MvcModel
{
    const DOWNLOAD_QUERY = "
    SELECT ?identifier ?attribute ?value
    WHERE {
        { ?identifier ?attribute ?value . VALUES ?identifier { <{subject}> } 
    }
    UNION { 
        ?identifier ?attribute ?value . VALUES ?value { <{subject}> } }
    }
    ";
    const EXTENSION_SPARQL_FORMAT_MAP = [
        "json" => "application/sparql-results+json",
        "tsv" => "text/tab-separated-values",
        "csv" => "text/csv",
        "bin" => "application/x-binary-rdf-results-table",
        "xml" => "application/sparql-results+xml"
    ];

    const EXTENSION_FORMAT_MAP = [
        "json" => "application/json",
        "tsv" => "text/tab-separated-values",
        "csv" => "text/csv",
        "bin" => "application/x-binary-rdf-results-table",
        "xml" => "application/xml"
    ];

    private function get_curl($namespace, $query = null)
    {
        $curl = curl_init();       
        if (!empty(trim(SPARQL_PROXY))) {
            curl_setopt($curl, CURLOPT_PROXY, SPARQL_PROXY);
        }
        $url = str_replace('{namespace}', $namespace, SPARQL_URL);
        if ($query) {
            $url .= '?query=' . urlencode($query);
        }
        curl_setopt($curl, CURLOPT_URL, $url);
        return $curl;
    }

    private function inject_graph_into_query($query){
	if (SPARQL_GRAPHS[0] != 'all'){
		$from_clause = '';
		foreach (SPARQL_GRAPHS as $graph){
            if ($graph == 'bd:nullGraph'){
                $from_clause .= "FROM <bd:nullGraph> ";
            } else {
                $from_clause .= "FROM <vg:$graph> ";
            }			
        }
        $nullGraph = mvc_setting('EchoesWpSparqlPluginSettings', 'sparql_query_default_graph');
        if ($nullGraph){
            $from_clause .= "FROM bd:nullGraph";
        }
		$query = preg_replace('/WHERE/', "$from_clause WHERE", $query, 1);
	}
	return $query;
    }
    public function query_another_namespace($query, $namespace) {
	$curl = $this->get_curl($namespace, $query);
        curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($curl, CURLOPT_HTTPHEADER, ['Accept: ' . self::EXTENSION_SPARQL_FORMAT_MAP['json']]);
	$res = curl_exec($curl);
	return json_decode($res);
    }

    private function query_all($query, $decode = true)
    {
        $final_res = new stdClass();
        $final_res->head = new stdClass();
        $final_res->head->vars = [];
        $final_res->results = new stdClass();
        $final_res->results->bindings = [];

        $first_res = null;
        curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($curl, CURLOPT_HTTPHEADER, ['Accept: ' . self::EXTENSION_SPARQL_FORMAT_MAP['json']]);
        foreach (SPARQL_NAMESPACES as $namespace) {
            $curl = $this->get_curl($namespace, $query);
            $res = json_decode(curl_exec($curl));
            if ($first_res === null) {
                $first_res = $res;
            }
            if (!empty($res->results->bindings)) {
                $final_res->results->bindings = $res->results->bindings;
            }
        }
        $final_res->head->vars = $first_res->head->vars;
        if ($decode) {
            return $final_res;
        } else {
            return json_encode($final_res);
        }
    }

    public function query($query, $format = 'json', $download = false, $decode = true)
    {
	//$query = $this->inject_graph_into_query($query);
        if (count(SPARQL_NAMESPACES) > 1 && $format == 'json' && !$download) {
            return $this->query_all($query, $decode);
        }        
        $curl = $this->get_curl(SPARQL_NAMESPACES[0], $query);
        if (!$download) {
            curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
        } else {
            header("Content-disposition: attachment; filename=\"results.$format\"");
        }
        curl_setopt($curl, CURLOPT_HTTPHEADER, ['Accept: ' . self::EXTENSION_SPARQL_FORMAT_MAP[$format]]);
        $res = curl_exec($curl);
        if (!$download) {
            if ($decode && $format == 'json') {
                return json_decode($res);
            }
            return $res;
        }

    }

    public function transform_results($results)
    {
        $transformed = [];
        $vars = $results->head->vars;
        foreach ($results->results->bindings as $row) {
            $transformed_row = [];
            foreach ($vars as $var) {
                if (property_exists($row, $var)) {
                    $transformed_row[$var] = $row->$var->value;
                }
            }
            $transformed[] = $transformed_row;
        }
        return $transformed;
    }

    public function get_fast_count($subject = null, $property = null, $object = null){
        $count = 0;
        $nullGraph = mvc_setting('EchoesWpSparqlPluginSettings', 'sparql_query_default_graph');
        foreach (SPARQL_NAMESPACES as $namespace){
            $params = ['ESTCARD'];
            $curl = $this->get_curl($namespace);
            curl_setopt($curl, CURLOPT_POST, 1);
            curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
            if (SPARQL_GRAPHS[0] != 'all'){
                if ($nullGraph){
                    $params[] = "c=<bd:nullGraph>";
                }
                foreach (SPARQL_GRAPHS as $graph){
                    if ($graph == 'bd:nullGraph'){
                        $params[] = "c=<bd:nullGraph>";
                    } else {
                        $params[] = "c=<vg:$graph>";
                    }                    
                }
            }           
            if ($subject){
                $params[] = "s=$subject";
            }
            if ($property){
                $params[] = "p=$property";
            }
            if ($object){
                $params[] = "o=$object";
            }
            $params_unique = array_unique($params);
            curl_setopt($curl, CURLOPT_POSTFIELDS, implode('&', $params));
            $res = new SimpleXMLElement(curl_exec($curl));
            $count += $res['rangeCount'];
        }        
        return $count;
    }

    public function download($subject, $format = 'json', $download = true)
    {
        $query = str_replace('{subject}', $subject, self::DOWNLOAD_QUERY);
        header('Content-type: ' . self::EXTENSION_FORMAT_MAP[$format]);
        if ($download) {
            $this->query($query, $format, $download, false);
        } else {
            echo $this->query($query, $format, $download, false);
        }
    }

    public function get_in_clause_from_subjects($subjects)
    {
        $subjects_str = '';
        foreach ($subjects as $subject) {
            $subjects_str .= "<$subject>, ";
        }
        $subjects_str = substr($subjects_str, 0, strlen($subjects_str) - 2);
        return $subjects_str;
    }

    public function get_order_clause($field, $type, $unique_field)
    {
        return "ORDER BY $type(?$field) ?$unique_field";
    }

    public function getAnnotations ($provided) {
                $provided = "<" . $provided . ">";
                return $this->query_another_namespace("SELECT ?body WHERE { ?source a <http://www.w3.org/ns/oa#SpecificResource>; ?p $provided . ?annotation a <http://www.w3.org/ns/oa#Annotation> ; <http://www.w3.org/ns/oa#hasTarget> ?source ; <http://www.w3.org/ns/oa#hasBody> ?body }", "annotations_gencat");
    }

    public function getInfoAnnotation ($annotation) {
                $annotation = "<" . $annotation . ">";
                return $this->query_another_namespace("select ?col1 ?col2 {{ $annotation ?col1 ?col2 } union { ?col1 ?col2 $annotation }}", "annotations_gencat");
    }

}
