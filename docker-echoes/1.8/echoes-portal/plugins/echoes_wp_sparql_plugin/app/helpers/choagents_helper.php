<?php

class ChoagentsHelper extends MvcHelper {
    public function fill_objects_with_chos($objects, $results){      
        foreach ($objects as $object_id => $object_value){
                $choInfos = $results;                
                foreach ($choInfos->results->bindings as $choInfo){                    
                    $cho_subject = $choInfo->ProvidedCHO_subject->value;
                    $cho_property = null;
                    if (property_exists($choInfo, 'ProvidedCHO_other_property')){
                        $cho_property = $choInfo->ProvidedCHO_other_property->value;
                    }                    
                    $cho_title = $choInfo->ProvidedCHO_title->value;
                    $cho_type = $choInfo->ProvidedCHO_type->value;
                    if (!array_key_exists($cho_subject, $objects[$object_id]['providedCHOs'])){
                        $objects[$object_id]['providedCHOs'][$cho_subject] = ['properties' => [], 'titles' => [], 'types' => []];
                    }
                    if ($cho_property != null){
                        $objects[$object_id]['providedCHOs'][$cho_subject]['properties'][] = $cho_property;
                    }                    
                    $objects[$object_id]['providedCHOs'][$cho_subject]['titles'][] = $cho_title;
                    $objects[$object_id]['providedCHOs'][$cho_subject]['types'][] = $cho_type;
                }
            
        }
        foreach ($objects as $object_id => $object_value){
            foreach ($object_value['providedCHOs'] as $cho_subject => $cho_value){
                $objects[$object_id]['providedCHOs'][$cho_subject]['properties'] = array_values(array_unique($cho_value['properties']));
                $objects[$object_id]['providedCHOs'][$cho_subject]['titles'] = array_values(array_unique($cho_value['titles']));
                $objects[$object_id]['providedCHOs'][$cho_subject]['types'] = array_values(array_unique($cho_value['types']));
                sort($objects[$object_id]['providedCHOs'][$cho_subject]['properties']);
                sort($objects[$object_id]['providedCHOs'][$cho_subject]['titles']);
                sort($objects[$object_id]['providedCHOs'][$cho_subject]['types']);
            }
        }
        return $objects;
    }

    public function fill_objects_with_providers($objects, $results){
        foreach ($objects as $object_id => $object_value){
                $choInfos = $results;
                foreach ($choInfos->results->bindings as $choInfo){
                    $cho_subject = $choInfo->providedCHO->value;
                    $cho_property = null;
                    if (property_exists($choInfo, 'ProvidedCHO_other_property')){
                        $cho_property = $choInfo->ProvidedCHO_other_property->value;
                    }
                    $cho_title = $choInfo->title->value;
                    $cho_type = $choInfo->type->value;
                    if (!array_key_exists($cho_subject, $objects[$object_id]['providedCHOs'])){
                        $objects[$object_id]['providedCHOs'][$cho_subject] = ['properties' => [], 'titles' => [], 'types' => []];
                    }
                    if ($cho_property != null){
                        $objects[$object_id]['providedCHOs'][$cho_subject]['properties'][] = $cho_property;
                    }
                    $objects[$object_id]['providedCHOs'][$cho_subject]['titles'][] = $cho_title;
                    $objects[$object_id]['providedCHOs'][$cho_subject]['types'][] = $cho_type;
                }

        }
        foreach ($objects as $object_id => $object_value){
            foreach ($object_value['providedCHOs'] as $cho_subject => $cho_value){
                $objects[$object_id]['providedCHOs'][$cho_subject]['properties'] = array_values(array_unique($cho_value['properties']));
                $objects[$object_id]['providedCHOs'][$cho_subject]['titles'] = array_values(array_unique($cho_value['titles']));
                $objects[$object_id]['providedCHOs'][$cho_subject]['types'] = array_values(array_unique($cho_value['types']));
                sort($objects[$object_id]['providedCHOs'][$cho_subject]['properties']);
                sort($objects[$object_id]['providedCHOs'][$cho_subject]['titles']);
                sort($objects[$object_id]['providedCHOs'][$cho_subject]['types']);
            }
        }
        return $objects;
    }

    public function fill_objects_with_agents($objects, $results){
        foreach ($objects as $object_id => $object_value){   
                $agentsInfos = $results;         
                    foreach ($agentsInfos->results->bindings as $agentInfo){                    
                        $agent_subject = $agentInfo->Agent_subject->value;
                        $agent_property = $agentInfo->Agent_other_property->value;
                        if (property_exists($agentInfo, 'Agent_title')){
                            $agent_title = $agentInfo->Agent_title->value;
                        } else {
                            $agent_title = '';
                        }
                        if (property_exists($agentInfo, 'Agent_subtitle')){
                            $agent_subtitle = $agentInfo->Agent_subtitle->value;
                        } else {
                            $agent_subtitle = '';
                        }
                        if (!array_key_exists($agent_subject, $objects[$object_id]['agents'])){
                            $objects[$object_id]['agents'][$agent_subject] = ['properties' => [], 'titles' => [], 'subtitles' => []];
                        }
                        $objects[$object_id]['agents'][$agent_subject]['properties'][] = $agent_property;
                        $objects[$object_id]['agents'][$agent_subject]['titles'][] = $agent_title;
                        $objects[$object_id]['agents'][$agent_subject]['subtitles'][] = $agent_subtitle;
                    }           
                  
        }
        foreach ($objects as $object_id => $object_value){
            foreach ($object_value['agents'] as $agent_subject => $agent_value){
                $objects[$object_id]['agents'][$agent_subject]['properties'] = array_values(array_unique($agent_value['properties']));
                $objects[$object_id]['agents'][$agent_subject]['titles'] = array_values(array_unique($agent_value['titles']));
                sort($objects[$object_id]['agents'][$agent_subject]['properties']);
                sort($objects[$object_id]['agents'][$agent_subject]['titles']);
                sort($objects[$object_id]['agents'][$agent_subject]['subtitles']);
            }
        }
        return $objects;
    }

    public function get_offset($page, $count_per_page){
        return ($page - 1) * $count_per_page;
    }

    public function get_num_pages($count, $count_per_page){
        return ceil($count / $count_per_page);
    }
}
