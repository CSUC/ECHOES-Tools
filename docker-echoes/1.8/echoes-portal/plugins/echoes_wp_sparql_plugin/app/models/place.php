<?php

require_once dirname(__FILE__) . '/model.php';

class Place extends model {

    public function search($prefLabel) {
        return $this->query("
                select distinct ?s ?prefLabel ?lat ?long where { 
                    ?s a edm:Place ; 
                    skos:prefLabel ?prefLabel .                     
                    ?prefLabel bds:search \"$prefLabel\" .
                    ?prefLabel bds:matchAllTerms 'true' .
                    ?prefLabel bds:rank ?rank .
                    OPTIONAL {?s wgs84_pos:lat ?lat} . 
                    OPTIONAL {?s wgs84_pos:long ?long} } 
                ORDER BY ?rank
                LIMIT 100
                ");
    }

    public function search_by_text($text){
        return $this->query("
        SELECT *
        WHERE {
          {
            ?place_object bds:search \"*$text*\" .
                ?place_subject ?place_property ?place_object .
                ?place_subject a edm:Place .
                ?place_object bds:rank ?rank  
          }
                UNION 
                {
                ?ProvidedCHO_object bds:search \"*$text*\" .
                ?ProvidedCHO_subject ?ProvidedCHO_property ?ProvidedCHO_object .
                ?ProvidedCHO_subject a edm:ProvidedCHO .
                ?ProvidedCHO_subject ?ProvidedCHO_other_property ?ProvidedCHO_other_object .
                FILTER (?ProvidedCHO_other_property IN (dcterms:spatial, dc:coverage)) .
                ?ProvidedCHO_object bds:rank ?rank                    
          }
                UNION 
                {
                ?Agent_object bds:search \"*$text*\" .
                ?Agent_subject ?Agent_property ?Agent_object .
                ?Agent_subject a edm:Agent .
                ?Agent_subject ?Agent_other_property ?Agent_other_object .
                FILTER (?Agent_other_property IN (edm:hasMet, rdaGr2:placeOfBirth, rdaGr2:placeOfDeath)) .
                ?Agent_object bds:rank ?rank                    
          }
          }
                ORDER BY ?rank
                LIMIT 10
        ");
    }

    public function search_by_place($place){
        if (empty($place)){
            return $this->query("
                SELECT ?place_object ?place_subject
                WHERE 
                {
                    ?place_subject a edm:Place ;
                    skos:prefLabel ?place_object
                }
                LIMIT 10
            ");
        } else {
            return $this->query("
                SELECT ?place_object ?place_subject
                WHERE 
                {
                    ?place_object bds:search \"*$place*\" .
                    ?place_subject ?place_property ?place_object .
                    ?place_subject a edm:Place .
                    ?place_object bds:rank ?rank  
                }
                ORDER BY ?rank
                LIMIT 10
            ");
        }
    }


    public function search_by_bounds($latStart, $latEnd, $lonStart, $lonEnd, $publisher = null){
        if ($publisher == null){
            return $this->query("
            SELECT ?place_object ?place_subject ?place_lan ?place_lon
            WHERE 
            {
                ?place_subject a edm:Place ;
                wgs84_pos:lat ?place_lan ;
                wgs84_pos:long ?place_lon ;
                skos:prefLabel ?place_object .
                FILTER (xsd:float(?place_lan) >= $latStart && xsd:float(?place_lan) <= $latEnd) .
                FILTER (xsd:float(?place_lon) >= $lonStart && xsd:float(?place_lon) <= $lonEnd)
            }
            #ORDER BY ?place_subject
            LIMIT 10
        ");
        } else {
            return $this->query("
                SELECT distinct ?place_object ?place_subject ?place_lan ?place_lon
                WHERE 
                {
                    ?place_subject a edm:Place ;
                    wgs84_pos:lat ?place_lan ;
                    wgs84_pos:long ?place_lon ;
                    skos:prefLabel ?place_object .
                    FILTER (xsd:float(?place_lan) >= $latStart && xsd:float(?place_lan) <= $latEnd) .
                    FILTER (xsd:float(?place_lon) >= $lonStart && xsd:float(?place_lon) <= $lonEnd) .
                    ?cho dcterms:spatial ?place ; a edm:ProvidedCHO ; dc:publisher <$publisher>
                }
                #ORDER BY ?place_subject
                LIMIT 10
            ");
        }
        
    }

    public function search_by_place_subject($place_subject){
        return $this->query("
                SELECT ?place_object ?place_subject ?place_lan ?place_lon
                WHERE 
                {
                    ?place_subject a edm:Place ;
                    wgs84_pos:lat ?place_lan ;
                    wgs84_pos:long ?place_lon ;
                    skos:prefLabel ?place_object
                }                
                LIMIT 1
                BINDINGS ?place_subject {(<$place_subject>)}
            ");
    }

    public function search_by_place_coords($place, $publisher){
        if (empty($place)){
            return $this->query("
                SELECT ?place_object ?place_subject ?place_lan ?place_lon
                WHERE 
                {
                    ?place_subject a edm:Place ;
                    wgs84_pos:lat ?place_lan ;
                    wgs84_pos:long ?place_lon ;
                    skos:prefLabel ?place_object
                }
                LIMIT 10
            ");
        } else if ($publisher == null){
            return $this->query("
                SELECT ?place_object ?place_subject ?place_lan ?place_lon
                WHERE 
                {
                    ?place_object bds:search \"*$place*\" .
                    ?place_subject ?place_property ?place_object ;
                    a edm:Place ;                    
                    wgs84_pos:lat ?place_lan ;
                    wgs84_pos:long ?place_lon .
                    ?place_object bds:rank ?rank  
                }
                ORDER BY ?rank
                LIMIT 10
            ");
        } else {
            return $this->query("
            SELECT ?place_object ?place_subject ?place_lan ?place_lon
            WHERE 
            {
                ?place_object bds:search \"*$place*\" .
                ?place_subject ?place_property ?place_object ;
                a edm:Place ;                
                wgs84_pos:lat ?place_lan ;
                wgs84_pos:long ?place_lon .
                ?place_object bds:rank ?rank  .
                ?cho a edm:ProvidedCHO ; dcterms:spatial ?place_subject ; dc:publisher <$publisher>
            }
            ORDER BY ?rank
            LIMIT 20
        ");
        }
    }

    public function get_place_name_by_subject($place_subject){
        return $this->query("
            SELECT ?place_name
            WHERE 
            {
                ?place_subject skos:prefLabel ?place_name .
                FILTER (?place_subject=<$place_subject>)
            }
            LIMIT 1
        ");
    }

    private function get_in_clause_from_place_subjects($place_subjects){
        $place_subjects_str = '';
        foreach ($place_subjects as $place_subject){
            $place_subjects_str .= "<$place_subject>, ";
        }
        $place_subjects_str = substr($place_subjects_str, 0, strlen($place_subjects_str) - 2);
        return $place_subjects_str;
    }

    public function get_num_providedCHO_by_place_subjects($place_subjects){
        $place_subjects_str = $this->get_in_clause_from_place_subjects($place_subjects);
        return $this->query("
            SELECT (COUNT(?ProvidedCHO_subject) as ?ProvidedCHO_subject_count) WHERE {
                ?ProvidedCHO_subject a edm:ProvidedCHO .
                ?ProvidedCHO_subject ?ProvidedCHO_other_property ?ProvidedCHO_other_subject .
                FILTER (?ProvidedCHO_other_property IN (dcterms:spatial, dc:coverage)) .
                FILTER (?ProvidedCHO_other_subject IN ($place_subjects_str))
          } 
        ");
    }

    public function get_num_agent_by_place_subjects($place_subjects, $subclassing = false){
        $place_subjects_str = $this->get_in_clause_from_place_subjects($place_subjects);
        if ($subclassing){
            return $this->query("
            SELECT (COUNT(?Agent_subject) as ?Agent_subject_count) WHERE {
                ?Agent_subject a edm:Agent .
                ?Agent_subject ?Agent_other_property ?Agent_other_subject .
		OPTIONAL { ?Agent_other_property rdfs:subClassOf ?class_relation } .
                FILTER (?Agent_other_property IN (edm:hasMet, rdaGr2:placeOfBirth, rdaGr2:placeOfDeath) || ?class_relation IN (edm:hasMet, rdaGr2:placeOfBirth, rdaGr2:placeOfDeath)) .
                FILTER (?Agent_other_subject IN ($place_subjects_str))
          } 
        ");
        } else {
            return $this->query("
            SELECT (COUNT(?Agent_subject) as ?Agent_subject_count) WHERE {
                ?Agent_subject a edm:Agent .
                ?Agent_subject ?Agent_other_property ?Agent_other_subject .
                FILTER (?Agent_other_property IN (edm:hasMet, rdaGr2:placeOfBirth, rdaGr2:placeOfDeath))
                FILTER (?Agent_other_subject IN ($place_subjects_str))
          } 
        ");
        }        
    }


    public function get_providedCHO_by_place_subject($place_subject, $publisher = null){
        if ($publisher == null){
            return $this->query("
                SELECT ?ProvidedCHO_subject ?ProvidedCHO_other_property ?ProvidedCHO_title ?ProvidedCHO_type
                WHERE 
                {
                    ?ProvidedCHO_subject a edm:ProvidedCHO .
                    ?ProvidedCHO_subject ?ProvidedCHO_other_property ?ProvidedCHO_other_subject .
                    FILTER (?ProvidedCHO_other_property IN (dcterms:spatial, dc:coverage)) .                
                    FILTER (?ProvidedCHO_other_subject=<$place_subject>) .
                    OPTIONAL {?ProvidedCHO_subject dc:title ?ProvidedCHO_title} .
                    OPTIONAL {?ProvidedCHO_subject dc:description ?ProvidedCHO_description} .                
                    BIND(IF(BOUND(?ProvidedCHO_title), ?ProvidedCHO_title, ?ProvidedCHO_description) as ?ProvidedCHO_title) .
                    FILTER(BOUND(?ProvidedCHO_title)) . 
                    OPTIONAL {?ProvidedCHO_subject edm:type ?ProvidedCHO_type}
                }
                ORDER BY ?ProvidedCHO_title ?ProvidedCHO_subject
                LIMIT 10
            ");
        } else {
            return $this->query("
                SELECT ?ProvidedCHO_subject ?ProvidedCHO_other_property ?ProvidedCHO_title ?ProvidedCHO_type
                WHERE 
                {
                    ?ProvidedCHO_subject a edm:ProvidedCHO .
                    ?ProvidedCHO_subject dc:publisher <$publisher> .
                    ?ProvidedCHO_subject ?ProvidedCHO_other_property ?ProvidedCHO_other_subject .
                    FILTER (?ProvidedCHO_other_property IN (dcterms:spatial, dc:coverage)) .                
                    FILTER (?ProvidedCHO_other_subject=<$place_subject>) .
                    OPTIONAL {?ProvidedCHO_subject dc:title ?ProvidedCHO_title} .
                    OPTIONAL {?ProvidedCHO_subject dc:description ?ProvidedCHO_description} .                
                    BIND(IF(BOUND(?ProvidedCHO_title), ?ProvidedCHO_title, ?ProvidedCHO_description) as ?ProvidedCHO_title) .
                    FILTER(BOUND(?ProvidedCHO_title)) . 
                    OPTIONAL {?ProvidedCHO_subject edm:type ?ProvidedCHO_type}
                }
                ORDER BY ?ProvidedCHO_title ?ProvidedCHO_subject
                LIMIT 10
            ");
        }
    }

    public function get_agent_by_place_subjects($place_subjects, $limit = 4, $offset = 0, $publisher = null){
        $place_subjects_str = $this->get_in_clause_from_place_subjects($place_subjects);
        return $this->query("
        prefix skos: <http://www.w3.org/2004/02/skos/core#>

        SELECT *
                WHERE {          
                  ?Agent_subject a edm:Agent .
                  ?Agent_subject ?Agent_other_property ?Agent_other_subject .
                  FILTER (?Agent_other_subject IN ($place_subjects_str)) .
                  FILTER (?Agent_other_property IN (edm:hasMet, rdaGr2:placeOfBirth, rdaGr2:placeOfDeath)) .
                  
                  {
                    SELECT distinct ?Agent_subject ?Agent_title ?Agent_gender ?Agent_birth ?Agent_death
                            
                            WHERE 
                            {
				?Agent_subject a edm:Agent ; ?Agent_other_property ?Agent_other_subject ;
                                foaf:name ?Agent_title .
                                OPTIONAL {?Agent_subject rdaGr2:gender ?Agent_gender} .
                                OPTIONAL {?Agent_subject rdaGr2:dateOfBirth ?Agent_birth} .
                                OPTIONAL {?Agent_subject rdaGr2:dateOfDeath ?Agent_death} .
                                FILTER (?Agent_other_subject IN ($place_subjects_str))
                            }
                            ORDER BY ?Agent_title ?Agent_subtitle  
                            LIMIT $limit
                            OFFSET $offset             
                  }
                  }  
                  ORDER BY ?Agent_title ?Agent_subtitle              
        ");
    }

    public function get_providedCHO_by_place_subjects($place_subjects, $limit = 3, $offset = 0, $publisher = null){
        $place_subjects_str = $this->get_in_clause_from_place_subjects($place_subjects);
        if ($publisher === null){
            return $this->query("
            SELECT *
            WHERE {          
              ?ProvidedCHO_subject a edm:ProvidedCHO .
              ?ProvidedCHO_subject ?ProvidedCHO_other_property ?ProvidedCHO_other_subject .
              FILTER (?ProvidedCHO_other_subject IN ($place_subjects_str))
              FILTER (?ProvidedCHO_other_property IN (dcterms:spatial, dc:coverage)) .  
              OPTIONAL {?ProvidedCHO_subject dc:title ?ProvidedCHO_title} .
              OPTIONAL {?ProvidedCHO_subject edm:type ?ProvidedCHO_type} .
              {
                SELECT distinct ?ProvidedCHO_subject
                        
                        WHERE 
                        {
                            ?ProvidedCHO_subject a edm:ProvidedCHO ; ?ProvidedCHO_other_property ?ProvidedCHO_other_subject .
                            OPTIONAL {?ProvidedCHO_subject dc:title ?ProvidedCHO_title} .
                              FILTER (?ProvidedCHO_other_subject IN ($place_subjects_str))
                        }
                        ORDER BY ?ProvidedCHO_title ?ProvidedCHO_subject
                        LIMIT $limit
                        OFFSET $offset              
              }
              }
              ORDER BY ?ProvidedCHO_title ?ProvidedCHO_subject    
            ");
        } else {
            return $this->query("
        SELECT *
        WHERE {          
          ?ProvidedCHO_subject a edm:ProvidedCHO .
          ?ProvidedCHO_subject ?ProvidedCHO_other_property ?ProvidedCHO_other_subject .
          FILTER (?ProvidedCHO_other_subject IN ($place_subjects_str))
          FILTER (?ProvidedCHO_other_property IN (dcterms:spatial, dc:coverage)) .  
          OPTIONAL {?ProvidedCHO_subject dc:title ?ProvidedCHO_title} .
          OPTIONAL {?ProvidedCHO_subject edm:type ?ProvidedCHO_type} .
          {
            SELECT distinct ?ProvidedCHO_subject
                    
                    WHERE 
                    {
                        ?ProvidedCHO_subject a edm:ProvidedCHO ; ?ProvidedCHO_other_property ?ProvidedCHO_other_subject ; dc:publisher <$publisher>.
                        OPTIONAL {?ProvidedCHO_subject dc:title ?ProvidedCHO_title} .
                          FILTER (?ProvidedCHO_other_subject IN ($place_subjects_str))
                    }
                    ORDER BY ?ProvidedCHO_title ?ProvidedCHO_subject
                    LIMIT $limit
                    OFFSET $offset              
          }
          }
          ORDER BY ?ProvidedCHO_title ?ProvidedCHO_subject    
        ");
        }
        
    }

    public function get_num_providedCHO_by_place_subject($place_subjects){
        return $this->query("
            SELECT (COUNT(?ProvidedCHO_subject) as ?ProvidedCHO_subject_count) WHERE {
                ?ProvidedCHO_subject a edm:ProvidedCHO .
                ?ProvidedCHO_subject ?ProvidedCHO_other_property ?ProvidedCHO_other_subject .
                FILTER (?ProvidedCHO_other_property IN (dcterms:spatial, dc:coverage)) .
                FILTER (?ProvidedCHO_other_subject=<$place_subject>)
          } 
        ");
    }

    public function get_num_agent_by_place_subject($place_subject){
        return $this->query("
            SELECT (COUNT(?Agent_subject) as ?Agent_subject_count) WHERE {
                ?Agent_subject a edm:Agent .
                ?Agent_subject ?Agent_other_property ?Agent_other_subject .
                FILTER (?Agent_other_property IN (edm:hasMet, rdaGr2:placeOfBirth, rdaGr2:placeOfDeath))
                FILTER (?Agent_other_subject=<$place_subject>)
          } 
        ");
    }

    public function get_agent_by_place_subject($place_subject){
        
        return $this->query("
        SELECT ?Agent_subject ?Agent_other_property ?Agent_title ?Agent_subtitle
        WHERE 
        {
            ?Agent_subject a edm:Agent .
            ?Agent_subject ?Agent_other_property ?Agent_other_subject .
            FILTER (?Agent_other_property IN (edm:hasMet, rdaGr2:placeOfBirth, rdaGr2:placeOfDeath))
              FILTER (?Agent_other_subject=<$place_subject>) .
              OPTIONAL {?Agent_subject skos:prefLabel ?Agent_title} .
              OPTIONAL {?Agent_subject skos:altLabel ?Agent_subtitle}
        }
        ORDER BY ?Agent_title ?Agent_subtitle
        LIMIT 4
        ");
    }

    public function get_details($subject) {
        return $this->query('
                select ?prefLabel ?altLabel ?note ?lat ?long ?alt ?abstract ?areaTotal ?country ?populationTotal ?homepage ?depiction
                where {
                    ?s a edm:Place ;
                    skos:prefLabel ?prefLabel .
                    OPTIONAL {?s skos:altLabel ?altLabel} . 
                    OPTIONAL {?s skos:note ?note} . 
                    OPTIONAL {?s wgs84_pos:lat ?lat} .
                    OPTIONAL {?s wgs84_pos:long ?long} . 
                    OPTIONAL {?s wgs84_pos:alt ?alt} .
                    OPTIONAL {?s <http://dbpedia.org/ontology/abstract> ?abstract . FILTER(LANG(?abstract)=\'en\')} .
                    OPTIONAL {?s <http://dbpedia.org/ontology/PopulatedPlace/areaTotal> ?areaTotal} .
                    OPTIONAL {?s <http://dbpedia.org/ontology/country> ?country}
                    OPTIONAL {?s <http://dbpedia.org/ontology/populationTotal> ?populationTotal} .
                    OPTIONAL {?s foaf:homepage ?homepage} .
                    OPTIONAL {?s foaf:depiction ?depiction} .
                    FILTER(?s=<' . $subject . '>)} 
                LIMIT 1'
        );
    }

    public function get_all() {
        return $this->query('
                select distinct ?s ?prefLabel ?lat ?long where { 
                    ?s a edm:Place ; 
                    skos:prefLabel ?prefLabel .                     
                    OPTIONAL {?s wgs84_pos:lat ?lat} . 
                    OPTIONAL {?s wgs84_pos:long ?long} } 
                LIMIT 100'
        );        
    }


    public function get_type_by_place_subject($place_subject){
        return $this->query("select * from <vg:gene> where{     
                                                                ?provided a edm:ProvidedCHO;
                                                                dc:title ?title;                 
                                                                dc:coverage $place_subject;                 
                                                                edm:type ?provided_type ;                 
                                                                edm:isRelatedTo ?related .     
                                                                ?related a edm:ProvidedCHO ;               
                                                                dc:title ?title2 ;               
                                                                edm:type ?provided_type2 ;                            
                                                          }");
    }

    public function get_place_info($place_subject){
        return $this->query("prefix dcterm: <http://purl.org/dc/terms/> 
                             select * from <vg:gene> where{      
                                                                ?place a edm:Place;          
                                                                dcterm:isPartOf $place_subject;          
                                                                skos:prefLabel ?label .                 
                                                                $place_subject ?o edm:Place;                     
                                                                skos:prefLabel ?nom      
                                                          }order by ?place limit 10");
    }
}
