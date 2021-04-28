<?php

require_once dirname(__FILE__) . '/model.php';

class Timespan extends model {


    function get_num_cho_per_date($min_date, $max_date){
        return $this->transform_results($this->query("SELECT ?date (COUNT(?cho) as ?cho_count)
            WHERE { 
            
            ?cho a edm:ProvidedCHO ; 
            dc:date ?date_str . BIND(strdt(?date_str, xsd:date) as ?date)
            FILTER (?date >= '$min_date'^^xsd:date && ?date <= '$max_date'^^xsd:date)       
                    
            }
            GROUP BY ?date
            ORDER BY DESC(?cho_count)
            LIMIT 100"));
    }

    function get_timeline_demo($year_begin, $year_end){
        return $this->fill_timeline_chos($this->transform_results($this->query("        
            SELECT distinct ?timeSpan (COUNT(?providedCho) as ?numProvidedCho) ?begin ?end ?providedCHOs
            WHERE {                    
                ?providedCho a edm:ProvidedCHO ; dcterms:temporal ?timeSpan .  
                ?timeSpan a edm:TimeSpan .
                BIND (xsd:string(?timeSpan) AS ?timeSpanStr) .
                BIND (xsd:integer(SUBSTR(?timeSpanStr,10,5)) AS ?begin) .
                BIND (xsd:integer(SUBSTR(?timeSpanStr,10,5)) AS ?end) .
                FILTER(?begin >= $year_begin && ?end <= $year_end)
            }
            GROUP BY ?timeSpan ?begin ?end ?providedCHOs
            ORDER BY DESC(?numProvidedCho) ?begin ?end ?timeSpan
            LIMIT 10
            "
        )));
    }

    private function fill_timeline_chos($timeline_result){
        for ($i = 0; $i < count($timeline_result); $i++) {
            $timeline_result[$i]['providedCHOs'] = $this->transform_results($this->query("
            SELECT (SAMPLE(?ProvidedCHO_subject) as ?subject) ?ProvidedCHO_title 
            WHERE {
              
              ?ProvidedCHO_subject a edm:ProvidedCHO ; dcterms:temporal ?timeSpan ; dc:title ?ProvidedCHO_title
              VALUES (?timeSpan) { (<{$timeline_result[$i]['timeSpan']}>) }
              }
                        
               GROUP BY ?ProvidedCHO_title
            LIMIT 1
            
            "));
        }
        return $timeline_result;
    }

    function get_all() {
        return $this->query('
        SELECT ?timeSpan ?begin ?end
        WHERE {              
            ?timeSpan a edm:TimeSpan
            BIND (xsd:string(?timeSpan) AS ?timeSpanStr) 
            BIND (SUBSTR(?timeSpanStr,10,5) AS ?begin)
            BIND (SUBSTR(?timeSpanStr,10,5) AS ?end)
        }
        LIMIT 100');
    }
    
    function get_num_timespan_by_search($begin){
        if ($begin != null){
            return $this->transform_results($this->query("
            SELECT (COUNT(?timeSpan) as ?count) 
            WHERE {
              ?timeSpan a edm:TimeSpan . BIND (xsd:string(?timeSpan) AS ?timeSpanStr) 
              BIND (SUBSTR(?timeSpanStr,10,5) AS ?begin) . FILTER(?begin=\"$begin\")      
            }
                "))[0]['count'];
        } else {
            /**return $this->get_fast_count(null, '<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>', '<http://www.europeana.eu/schemas/edm/TimeSpan>');**/
		return $this->transform_results($this->query("
		SELECT (COUNT(?timeSpan) as ?count)
                WHERE {
                    ?timeSpan a edm:TimeSpan ; 
                    BIND (xsd:string(?timeSpan) AS ?timeSpanStr) 
                    BIND (SUBSTR(?timeSpanStr,10,5) AS ?begin) 
                    BIND (SUBSTR(?timeSpanStr,10,5) AS ?end)                 
                }
		 "))[0]['count'];
        }
        
    }

    function search($begin, $limit = 25, $offset = 0, $order_field = 'ranking', $order_type = 'DESC') {
        if ($begin != null){
            return $this->transform_results($this->query("
                SELECT ?timeSpan ?begin ?end
                WHERE {
                    ?timeSpan a edm:TimeSpan ; 
                    BIND (xsd:string(?timeSpan) AS ?timeSpanStr) 
                    BIND (SUBSTR(?timeSpanStr,10,5) AS ?begin) 
                    BIND (SUBSTR(?timeSpanStr,10,5) AS ?end) 
                    FILTER(?begin=\"$begin\")                  
                }
                {$this->get_order_clause($order_field, $order_type, 'timeSpan')}
                LIMIT $limit
                OFFSET $offset  
                "));
        } else {
            return $this->transform_results($this->query("
                SELECT ?timeSpan ?begin ?end
                WHERE {
                    ?timeSpan a edm:TimeSpan ; 
                    BIND (xsd:string(?timeSpan) AS ?timeSpanStr) 
                    BIND (SUBSTR(?timeSpanStr,10,5) AS ?begin) 
                    BIND (SUBSTR(?timeSpanStr,10,5) AS ?end)                 
                }
                LIMIT $limit
                OFFSET $offset  
                "));
        }
        
    }
    
    function get_timeline(){
        return $this->query('
                SELECT ?s ?title ?timeSpan ?timeSpanStr ?begin ?end
                WHERE {
                    ?s a edm:ProvidedCHO ;
                    dc:title ?title ;
                    dcterms:temporal ?timeSpan .
                    ?timeSpan a edm:TimeSpan ; 
                    BIND (xsd:string(?timeSpan) AS ?timeSpanStr) 
                    BIND (SUBSTR(?timeSpanStr,10,5) AS ?begin) 
                    BIND (SUBSTR(?timeSpanStr,10,5) AS ?end) 
                }
                LIMIT 100');
    }
    
    function get_timeline_search($begin){
        return $this->query("
        SELECT ?s ?title ?timeSpan ?timeSpanStr ?begin ?end
        WHERE {
            ?s a edm:ProvidedCHO ;
            dc:title ?title ;
            dcterms:temporal ?timeSpan .
            ?timeSpan a edm:TimeSpan ; 
            BIND (xsd:string(?timeSpan) AS ?timeSpanStr) 
            BIND (SUBSTR(?timeSpanStr,10,5) AS ?begin) 
            BIND (SUBSTR(?timeSpanStr,10,5) AS ?end) 
            FILTER(?begin=\"$begin\")  
        }
        LIMIT 100");
    }

    public function get_details_popup($place_timespan){
        return $this->query("
        SELECT ?timespansubject ?timespanlabel ?ProvidedCHO_subject ?ProvidedCHO_title ?ProvidedCHO_type
        WHERE 
        {                
            ?ProvidedCHO_subject a edm:ProvidedCHO ; dcterms:temporal ?s.
            OPTIONAL {?ProvidedCHO_subject dc:title ?ProvidedCHO_title} .
            OPTIONAL {?ProvidedCHO_subject edm:type ?ProvidedCHO_type} .
            BIND(STR(?s) AS ?timespansubject) .
            OPTIONAL {?ProvidedCHO_subject dc:description ?ProvidedCHO_description} .                
            BIND(IF(BOUND(?ProvidedCHO_title), ?ProvidedCHO_title, ?ProvidedCHO_description) as ?ProvidedCHO_title) .
            FILTER(BOUND(?ProvidedCHO_title)) . 
            BIND(REPLACE(STR(?s), \"TimeSpan:\", \"\") AS ?timespanlabel) .
            FILTER(?s=<$place_timespan>)
        }
        ORDER BY ?ProvidedCHO_title ?ProvidedCHO_subject
        LIMIT 4
        ");
    }

    public function get_num_providedCHO_by_subjects($timespan_subject){
        return $this->query("
            SELECT (COUNT(?ProvidedCHO_subject) as ?ProvidedCHO_subject_count) WHERE {
                ?ProvidedCHO_subject a edm:ProvidedCHO .
                ?ProvidedCHO_subject ?ProvidedCHO_other_property ?ProvidedCHO_other_subject .
                FILTER (?ProvidedCHO_other_property IN (dcterms:temporal)) .
                FILTER (?ProvidedCHO_other_subject=<$timespan_subject>)
          } 
        ");
    }

    public function get_providedCHO_by_subjects($timespan_subjects, $limit = 3, $offset = 0){
        $subjects_str = $this->get_in_clause_from_subjects($timespan_subjects);
        return $this->query("
        SELECT *
        WHERE {          
          ?ProvidedCHO_subject a edm:ProvidedCHO .
          ?ProvidedCHO_subject ?ProvidedCHO_other_property ?ProvidedCHO_other_subject .
          FILTER (?ProvidedCHO_other_subject IN ($subjects_str))
          FILTER (?ProvidedCHO_other_property IN (dcterms:temporal)) .  
          OPTIONAL {?ProvidedCHO_subject dc:title ?ProvidedCHO_title} .
          OPTIONAL {?ProvidedCHO_subject edm:type ?ProvidedCHO_type} .
          OPTIONAL {?ProvidedCHO_subject dc:description ?ProvidedCHO_description} .                
                BIND(IF(BOUND(?ProvidedCHO_title), ?ProvidedCHO_title, ?ProvidedCHO_description) as ?ProvidedCHO_title) .
                FILTER(BOUND(?ProvidedCHO_title)) .
          {
            SELECT distinct ?ProvidedCHO_subject
                    
                    WHERE 
                    {
                        ?ProvidedCHO_subject a edm:ProvidedCHO ; ?ProvidedCHO_other_property ?ProvidedCHO_other_subject .
                        OPTIONAL {?ProvidedCHO_subject dc:title ?ProvidedCHO_title} .
                        OPTIONAL {?ProvidedCHO_subject dc:description ?ProvidedCHO_description} .              
                BIND(IF(BOUND(?ProvidedCHO_title), ?ProvidedCHO_title, ?ProvidedCHO_description) as ?ProvidedCHO_title) .
                FILTER(BOUND(?ProvidedCHO_title)) .
                        FILTER (?ProvidedCHO_other_subject IN ($subjects_str))
                    }
                    ORDER BY ?ProvidedCHO_title ?ProvidedCHO_subject
                    LIMIT $limit
                    OFFSET $offset              
          }
          } 
          ORDER BY ?ProvidedCHO_title ?ProvidedCHO_subject   
        ");
    }

    public function get_num_people_by_subject($timespan_subject){
        return $this->query("SELECT (COUNT(?s) as ?count) WHERE {
                                ?s rdf:type <http://www.europeana.eu/schemas/edm/Agent> .
                                ?s rdaGr2:dateOfBirth  ?dateBirth .
                                filter contains (?dateBirth,'$timespan_subject')}"
);
    }

    public function get_people_by_subject($date, $limit = 3, $offset = 0){
        return $this->query("SELECT ?s ?name ?placeBirth ?dateBirth ?placeDeath ?dateDeath WHERE {
                                ?s rdf:type <http://www.europeana.eu/schemas/edm/Agent> .
                                ?s foaf:name ?name .
                                OPTIONAL {?s rdaGr2:placeOfBirth ?placeBirth} .
                                OPTIONAL {?s rdaGr2:dateOfBirth  ?dateBirth} .

                                filter contains (?dateBirth,'$date')
                                }
                                LIMIT $limit
                                OFFSET $offset");
    }

    public function get_providedCHO_by_subject($date, $limit = 3, $offset = 0){
        return $this->query("SELECT ?s ?title ?providerType
                                WHERE {
                                        ?s a edm:ProvidedCHO ;
                                        dc:title ?title ;
                                        edm:type ?providerType ;
                                        dcterms:temporal ?timeSpan .
                                        filter (?timeSpan=<TimeSpan:$date>)
                                }
                                LIMIT $limit
                                OFFSET $offset");
    }

    public function get_num_providedCHO_by_subject($timespan_subject){
        return $this->query("SELECT (COUNT(?s) as ?count) 
                                WHERE {
                                        ?s a edm:ProvidedCHO ;
                                        dcterms:temporal ?timeSpan .
                                        filter (?timeSpan=<TimeSpan:$timespan_subject>)
                                }");
    }
}
