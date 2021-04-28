<?php

require_once dirname(__FILE__) . '/model.php';

class Provider extends model {

    function imgCHO($id) {
	return $this->query("
			SELECT * where{     
    				?s a ?o .
				?sAgg edm:aggregatedCHO ?s ; edm:hasView ?shown . FILTER regex(STR(?shown),'^https?://', 'i') .
        			FILTER(?s=<" . $id . ">)
			}
	");
    }

    public function getConceptLabel($concept){
	return $this->query('
		            SELECT ?label WHERE {
                	    <' . $concept . '> skos:prefLabel ?label 
            }
        ');
    }                                                          

    function get_label($agent){
        return $this->query('
        SELECT ?persons
                  
        WHERE {
          
          {
            SELECT (group_concat(distinct ?personFoafLabel;separator=" ") as ?personFoafLabels)
      WHERE {
        FILTER(?personId=<' . $agent .'>)
        ?personId foaf:name ?personFoafLabel                                                             
      }
      GROUP BY ?personId 
            } .
          BIND(STR(?personFoafLabels) as ?persons)
          }
           
    
        ');
    }

    function get_details($subject) {
	$aux = str_replace(' ', '%20', $subject);
        $query = '
            SELECT 
                    (group_concat(distinct ?dcIdentifier;separator=", ") as ?dcIdentifiers) 
                    (group_concat(distinct ?alternative;separator=", ") as ?alternatives) 
                    (group_concat(distinct ?edmType;separator="|") as ?edmType) 
                    (group_concat(distinct ?shown;separator="|") as ?showns) 
                    (group_concat(distinct ?personId;separator="|") as ?persons)
                    (group_concat(distinct ?personId;separator="|") as ?personsId)
                    (group_concat(?gender;separator="|") as ?personsGender)
                    (group_concat(?relatedTo;separator="|") as ?relatedTos)
                    (group_concat(distinct ?historicalNewId;separator="|") as ?historicalNewIds)
                    (group_concat(distinct ?historicalNew;separator="|") as ?historicalNews)
                    (group_concat(distinct ?timeSpanId;separator="|") as ?timeSpanIds)
                    (group_concat(distinct ?timeSpan;separator="|") as ?timeSpans)
                    (group_concat(distinct ?conceptId;separator="|") as ?conceptIds)
                    (group_concat(distinct ?conceptPrefLabel;separator="|") as ?conceptPrefLabels)
                    (group_concat(distinct ?placeId;separator="|") as ?placeIds)
                    (group_concat(distinct ?placePrefLabel;separator="|") as ?placePrefLabels)
                    (group_concat(distinct ?title;separator=", ") as ?titles) 
                    (group_concat(distinct ?description;separator=", ") as ?descriptions) 
                    (group_concat(distinct ?language;separator=", ") as ?languages) 
                    (group_concat(distinct ?subject;separator=", ") as ?subjects)
                    (group_concat(distinct ?dcType;separator=", ") as ?dcTypes) 
                    (group_concat(distinct ?dcCreatorLabel;separator=", ") as ?dcCreatorLabels) 
                    (group_concat(distinct ?dcCreator;separator=", ") as ?dcCreators) 
                    (group_concat(distinct ?dcPublisher;separator=", ") as ?dcPublishers) 
                    (group_concat(distinct ?dcRight;separator=", ") as ?dcRights) 
                    (group_concat(distinct ?dcSource;separator=", ") as ?dcSources) 
                    (group_concat(distinct ?dcFormat;separator=", ") as ?dcFormats) 
                    (group_concat(distinct ?edmInternalType;separator=", ") as ?edmInternalTypes) 
                    (group_concat(distinct ?spatial;separator=", ") as ?spatials)
            WHERE { 
              ?s a ?edmType . 
              OPTIONAL {?s dc:identifier ?dcIdentifier} . 
              OPTIONAL {?s dc:title ?title} . 
              OPTIONAL {?s dcterms:alternative ?alternative}.
              OPTIONAL {?s dc:description ?description} . 
              OPTIONAL {?s dc:language ?language} . 
              OPTIONAL {?s dc:subject ?subject} . 
              OPTIONAL {?s edm:hasType ?dcType} . 
              OPTIONAL {?s dc:type ?dcType} . 
              OPTIONAL {?s dc:creator ?dcCreator . OPTIONAL {?dcCreator skos:prefLabel ?dcCreatorLabel} } . 
              OPTIONAL {?s dc:publisher ?dcPublisher} . 
              OPTIONAL {?s dc:format ?dcFormat} . 
              OPTIONAL {?s dc:rights ?dcRight} .
              OPTIONAL {?s dc:source ?dcSource} .
              OPTIONAL {?s dcterms:spatial ?spatial} . 
              OPTIONAL {?s dc:contributor ?personId . ?personId rdf:type edm:Agent . OPTIONAL{ ?personId rdaGr2:gender ?gender }} .
              BIND(IF(BOUND(?gender), ?gender, "Onbekend") as ?gender) .
              OPTIONAL {?aggHistoricalnews edm:aggregatedCHO ?s . ?historicalNewId edm:isRelatedTo ?aggHistoricalnews ; edm:hasType "HistoricalNews" ; dc:description ?historicalNew} .
              OPTIONAL {?sAgg edm:aggregatedCHO ?s ; edm:hasView ?shown . FILTER regex(STR(?shown),"^https?://", "i")} .
              OPTIONAL {?s dcterms:temporal ?timeSpanId . ?timeSpanId rdf:type edm:TimeSpan . OPTIONAL {?timeSpanId skos:prefLabel ?timeSpan}  . BIND(IF(BOUND(?timeSpan), ?timeSpan, ?timeSpanId) as ?timeSpan)} .
              OPTIONAL {?s edm:isRelatedTo ?conceptId . ?conceptId rdf:type skos:Concept ; skos:prefLabel ?conceptPrefLabel} .
              OPTIONAL {?conceptId skos:related ?s ; rdf:type skos:Concept ; skos:prefLabel ?conceptPrefLabel} .
              OPTIONAL {?s dc:subject ?conceptId . ?conceptId rdf:type skos:Concept ; skos:prefLabel ?conceptPrefLabel} .
              OPTIONAL {?s dcterms:spatial ?placeId . ?placeId rdf:type edm:Place ; skos:prefLabel ?placePrefLabel} . 
              OPTIONAL {?s dc:coverage ?placeId . ?placeId rdf:type edm:Place ; skos:prefLabel ?placePrefLabel} . 
              ?s edm:type ?edmInternalType .
              FILTER(?s=<' . $aux . '>)
            }
            GROUP BY ?s             
            ';
        return $this->query($query);
    }

    public function get_providedCHO_by_date($date, $limit = 3, $offset = 0){
        $query = "SELECT ?ProvidedCHO_subject ?ProvidedCHO_title ?ProvidedCHO_type
            WHERE 
            {
                ?ProvidedCHO_subject a edm:ProvidedCHO .
                ?ProvidedCHO_subject dc:date '$date' .            
                OPTIONAL {?ProvidedCHO_subject dc:title ?ProvidedCHO_title} .
                OPTIONAL {?ProvidedCHO_subject dc:description ?ProvidedCHO_description} .                
                OPTIONAL {?ProvidedCHO_subject edm:type ?ProvidedCHO_type}
            }";

        $query = $query ."LIMIT " . $limit;

        $query = $query . " OFFSET " . $offset;

        return $this->query($query);

    }

    public function getCountByDate($date){
                return $this->query("SELECT (COUNT(?ProvidedCHO_subject) as ?count)
                                        WHERE 
                                                {
                                                        ?ProvidedCHO_subject a edm:ProvidedCHO .
                                                        ?ProvidedCHO_subject dc:date '$date';          
                                                }");
    }
}
