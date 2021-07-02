<?php

require_once dirname(__FILE__) . '/model.php';

class Webresource extends model {

    function get_num_webresource_by_search($search){
        if ($search != null){
            return $this->transform_results($this->query("
            SELECT (COUNT(?webresource_subject) as ?count) 
            WHERE {
              ?webresource_subject a edm:WebResource ; FILTER (REGEX(STR(?webresource_subject), \"$search\", \"i\"))       
            }
                "))[0]['count'];
        } else {
            return $this->get_fast_count(null, '<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>', '<http://www.europeana.eu/schemas/edm/WebResource>');
        }
        
    }

    function get_webresource_by_search($search, $limit = 25, $offset = 0, $order_field, $order_type){
        if ($search != null){
            return $this->transform_results($this->query("
                
            SELECT ?webresource_subject 
            WHERE {
                ?webresource_subject a edm:WebResource ;  FILTER (REGEX(STR(?webresource_subject), \"$search\", \"i\"))                            
            }
            {$this->get_order_clause($order_field, $order_type, 'webresource_subject')} 
      LIMIT $limit
      OFFSET $offset    
                  
                
                "));
        } else {
            return $this->transform_results($this->query("
SELECT ?webresource_subject ?providedCHO ?title
WHERE{ 
  ?webresource_subject a edm:WebResource  .
  ?aggregation a ore:Aggregation ;
                 edm:hasView ?webresource_subject ;
				 edm:aggregatedCHO ?providedCHO .
  ?providedCHO a edm:ProvidedCHO ;
                 	dc:title ?title
                 
}          
      LIMIT $limit
      OFFSET $offset    
                  
                
                "));
        }
        
    }

}
