<?php

require_once dirname(__FILE__) . '/model.php';

class Agent extends model {

    function get_details($subject){
        return $this->query('
        select ?s ?professionOccupation ?label ?name ?dateOfBirth ?placeOfBirth ?placeOfBirthName ?dateOfDeath ?placeOfDeath ?placeOfDeathName 
        ?gender (group_concat(distinct ?place;separator="|") as ?hasMetPlace) (group_concat(distinct ?placeName;separator="|") as ?hasMetPlaceName) 
        (group_concat(?provider;separator="|") as ?providers) (group_concat(?providerTitle;separator="|") as ?providerTitles) (group_concat(?providerType;separator="|") as ?providerTypes)
        (group_concat(?c;separator="|") as ?concepts) (group_concat(?conceptLabel;separator="|") as ?conceptLabels) (group_concat(distinct ?sameAs;separator="|") as ?extraInfo)
		where {
            OPTIONAL {?s rdaGr2:placeOfBirth ?placeOfBirth . ?placeOfBirth skos:prefLabel ?placeOfBirthName } .
            OPTIONAL {?s rdaGr2:dateOfBirth ?dateOfBirth} .
            OPTIONAL {?s rdaGr2:placeOfDeath ?placeOfDeath . ?placeOfDeath skos:prefLabel ?placeOfDeathName } .
            OPTIONAL {?s rdaGr2:dateOfDeath ?dateOfDeath} .     
          	OPTIONAL {?s rdaGr2:gender ?gender} .
            OPTIONAL {?s rdaGr2:professionOrOccupation ?professionOccupation} .  
            OPTIONAL {?s edm:hasMet ?place . ?place skos:prefLabel ?placeName } .
            OPTIONAL {?provider dc:contributor ?s ; dc:title ?providerTitle; edm:type ?providerType} .
            OPTIONAL {?s edm:isRelatedTo ?c . ?c a skos:Concept ; skos:prefLabel ?conceptLabel} .
	    OPTIONAL {?s foaf:name ?name} .
	    OPTIONAL {?s owl:sameAs ?sameAs}
            }   
			GROUP BY ?s ?professionOccupation ?label ?dateOfBirth ?placeOfBirth ?placeOfBirthName ?dateOfDeath ?placeOfDeath ?placeOfDeathName ?gender ?name
            ORDER BY ?providerTitle ?provider
            LIMIT 1			
            BINDINGS ?s {(<' . $subject . '>)}');
    }

    function get_label($agent){
        return $this->query('
        SELECT ?persons
                  
        WHERE {
          
          {
            SELECT (group_concat(distinct ?personPrefLabel;separator=" ") as ?personPrefLabels) (group_concat(distinct ?personAltLabel;separator=" ") as ?personAltLabels)
      WHERE {
        FILTER(?personId=<' . $agent .'>)
        ?personId skos:prefLabel ?personPrefLabel . OPTIONAL { ?personId skos:altLabel ?personAltLabel }                                                              
      }
      GROUP BY ?personId 
            } .
          BIND(CONCAT(STR(?personPrefLabels), " ", STR(?personAltLabels)) as ?persons)
          }
           
    
        ');
    }

    public function get_agents_by_date($date, $limit = true, $offset = 0){
        $query = "SELECT ?s ?name ?placeBirth ?dateBirth ?placeDeath ?dateDeath WHERE {
                                ?s rdf:type <http://www.europeana.eu/schemas/edm/Agent> .
                                ?s foaf:name ?name .
                                OPTIONAL {?s rdaGr2:placeOfBirth ?placeBirth} .
                                OPTIONAL {?s rdaGr2:dateOfBirth  ?dateBirth} .
                                OPTIONAL {?s rdaGr2:placeOfDeath ?placeDeath} .
                                OPTIONAL {?s rdaGr2:dateOfDeath  ?dateDeath} .
                                {?s rdaGr2:dateOfBirth '$date'} UNION {?s rdaGr2:dateOfDeath '$date'}
                             }";

        $query = ($limit) ? $query ."LIMIT 3" : $query;

        $query = $query . " OFFSET " . $offset;

        return $this->query($query);
    }
}
