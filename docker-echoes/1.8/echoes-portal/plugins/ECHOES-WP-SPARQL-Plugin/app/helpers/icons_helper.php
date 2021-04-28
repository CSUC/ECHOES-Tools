<?php

class IconsHelper extends MvcHelper {
	
    private $properties_to_label = [
        'http://www.europeana.eu/schemas/edm/hasMet' => 'Met this place',
        'http://rdvocab.info/ElementsGr2/placeOfBirth' => 'Was born in this place',
        'http://rdvocab.info/ElementsGr2/placeOfDeath' => 'Died in this place'
        
    ];
   
    //TODO Declarat al contructor, es pot eliminar (?) 
/*   public $edm_type_map = [
    'TEXT' => '<img title="Text" style="vertical-align: middle; width: 16px" src="/wp-content/themes/echoes/img/Assets/ico-text.svg"></img>',
    'IMAGE' => '<img title="Image" style="vertical-align: middle; width: 16px" src="/wp-content/themes/echoes/img/Assets/ico-picture.svg"></img>',
    'SOUND' => '<img title="Sound" style="vertical-align: middle; width: 16px" src="/wp-content/themes/echoes/img/Assets/ico-speaker.svg"></img>',
    'VIDEO' => '<img title="Video" style="vertical-align: middle; width: 16px" src="/wp-content/themes/echoes/img/Assets/ico-video.svg"></img>',
    '3D' => '<img title="3D" style="vertical-align: middle; width: 16px" src="/wp-content/themes/echoes/img/Assets/ico-3D.svg"></img>'
    ]; */

    private $properties_map = [
        'http://www.europeana.eu/schemas/edm/hasMet' => '<img title="Met this place" src="/wp-content/themes/echoes/img/Assets/ico-man-pointer.svg" style="vertical-align: middle; color:black; width: 16px"></img>',
        'http://rdvocab.info/ElementsGr2/placeOfBirth' => '<img title="Was born in this place" src="/wp-content/themes/echoes/img/Assets/ico-birth.svg" style="vertical-align: middle; width: 16px"></img>',
        'http://rdvocab.info/ElementsGr2/placeOfDeath' => '<img title="Died in this place" src="/wp-content/themes/echoes/img/Assets/ico-died.svg" style="vertical-align: middle; width: 16px"></img>',
        'Man' => '<i class="fa fa-male" aria-hidden="true"></i>',
        'Vrouw' => '<i class="fa fa-female" aria-hidden="true"></i>',
        'Onbekend' => '<i class="fa fa-user" aria-hidden="true"></i>'
    ];

    /*
    private $explore_type_map = [
            'Concepts' => '<img title="Text" style="vertical-align: middle; width: 16px" src="/wp-content/themes/echoes/img/Assets/ico-concepts-violet.svg"></img>',
            'Digital Objects' => '<img title="Image" style="vertical-align: middle; width: 16px" src="/wp-content/themes/echoes/img/Assets/ico-digital-objets-violet.svg"></img>',
            'People' => '<img title="Sound" style="vertical-align: middle; width: 16px" src="/wp-content/themes/echoes/img/Assets/ico-people-violet.svg"></img>',
            'Places' => '<img title="Video" style="vertical-align: middle; width: 16px" src="/wp-content/themes/echoes/img/Assets/ico-map-violet.svg"></img>',
            'Time' => '<img title="3D" style="vertical-align: middle; width: 16px" src="/wp-content/themes/echoes/img/Assets/ico-calendar-violet.svg"></img>'
        ];
    */
  function __construct() {
    // Properties to translate 
    $this->properties_to_label['http://www.europeana.eu/schemas/edm/hasMet'] = __('Met this place', 'echoes_wp_sparql_plugin'); 
    $this->properties_to_label['http://rdvocab.info/ElementsGr2/placeOfBirth'] = __('Was born in this place', 'echoes_wp_sparql_plugin');
    $this->properties_to_label['http://rdvocab.info/ElementsGr2/placeOfDeath'] = __('Died in this place', 'echoes_wp_sparql_plugin');

    $this->edm_type_map = [
    	__('TEXT', 'echoes_wp_sparql_plugin') => '<img title="Text" style="vertical-align: middle; width: 16px" src="/wp-content/themes/echoes/img/Assets/ico-text.svg"></img>',
    	__('IMAGE', 'echoes_wp_sparql_plugin') => '<img title="Image" style="vertical-align: middle; width: 16px" src="/wp-content/themes/echoes/img/Assets/ico-picture.svg"></img>',
    	__('SOUND', 'echoes_wp_sparql_plugin')  => '<img title="Sound" style="vertical-align: middle; width: 16px" src="/wp-content/themes/echoes/img/Assets/ico-speaker.svg"></img>',
    	__('VIDEO', 'echoes_wp_sparql_plugin') => '<img title="Video" style="vertical-align: middle; width: 16px" src="/wp-content/themes/echoes/img/Assets/ico-video.svg"></img>',
    	__('3D', 'echoes_wp_sparql_plugin') => '<img title="3D" style="vertical-align: middle; width: 16px" src="/wp-content/themes/echoes/img/Assets/ico-3D.svg"></img>'
    ];

    $this->explore_type_map = [
        __('Concepts', 'echoes_wp_sparql_plugin') => '<img title="Text" style="vertical-align: middle; width: 16px" src="/wp-content/themes/echoes/img/Assets/ico-concepts-violet.svg"></img>',
        __('Digital Objects', 'echoes_wp_sparql_plugin') => '<img title="Image" style="vertical-align: middle; width: 16px" src="/wp-content/themes/echoes/img/Assets/ico-digital-objets-violet.svg"></img>',
        __('People', 'echoes_wp_sparql_plugin') => '<img title="Sound" style="vertical-align: middle; width: 16px" src="/wp-content/themes/echoes/img/Assets/ico-people-violet.svg"></img>',
        __('Places', 'echoes_wp_sparql_plugin') => '<img title="Video" style="vertical-align: middle; width: 16px" src="/wp-content/themes/echoes/img/Assets/ico-map-violet.svg"></img>',
        __('Time', 'echoes_wp_sparql_plugin') => '<img title="3D" style="vertical-align: middle; width: 16px" src="/wp-content/themes/echoes/img/Assets/ico-calendar-violet.svg"></img>'
    ];

  }

  public function get_providedCHO_icon($cho_types) {
    
    $html = '';
    foreach ($cho_types as $type){
        $html .= $this->edm_type_map[$type];
    }
    return $html;
  }

  public function get_agent_icon($agent_properties) {
    
    $html = '';
    foreach ($agent_properties as $property){
        $html .= $this->properties_map[$property];
    }
    return $html;
  }

  public function print_legend($vertical = true, $agents = true, $providedCHOs = true, $categories = true) {
    if (!$vertical){
        $extraLiStyle = 'display: inline;';
    } else {
        $extraLiStyle = '';
}
    $html = '<ul style="background-color:f9f9f9;list-style-type: none; margin: 10px; padding: 15px; margin-bottom: 15px;">';
    if ($providedCHOs){
        $html .= "<li style='$extraLiStyle'><h6 style='padding-bottom: 5px; font-weight: bold; font-size: 14px;'>". esc_html__('Digital objects types', 'echoes_wp_sparql_plugin') . ":</h6></li>";
        $i = 0;
        foreach ($this->edm_type_map as $edm_type => $img){
            if (strtolower($edm_type) != '3d') {
                $edm_type = strtolower($edm_type);
            } 
            if ($i == 0 && !$vertical){
                $html .= "<li style='$extraLiStyle margin-left: 0px'> $img  " . ucfirst($edm_type) . "</li>";
            } else {
                $html .= "<li style='$extraLiStyle margin-left: 25px'> $img  " . ucfirst($edm_type) . "</li>";
            }
            $i++;
        }
        $html .= "</li>";

    }
    if ($agents){
        $html .= "<li style='$extraLiStyle'><h6 style='padding-bottom: 5px; font-weight: bold; font-size: 14px; margin-top: 20px;'>" . esc_html__('People\'s relation to Places', 'echoes_wp_sparql_plugin') . ":</h6></li>";
        $i = 0;
        foreach ($this->properties_map as $property => $img){
            if ($i == 0 && !$vertical){
                $html .= "<li style='$extraLiStyle margin-left: 0px'> $img {$this->properties_to_label[$property]}</li>";
            } else if (array_key_exists($property, $this->properties_to_label)){
                $html .= "<li style='$extraLiStyle margin-left: 25px'> $img {$this->properties_to_label[$property]}</li>";
            }
            $i++;
        }
        $html .= "</li>";
    }
    if ($categories){
        $html .= "<li style='$extraLiStyle'><h6 style='padding-bottom: 5px; font-weight: bold; font-size: 14px; margin-top: 20px;'>". esc_html__('Categories', 'echoes_wp_sparql_plugin') . ":</h6></li>";
        $i = 0;
        foreach ($this->explore_type_map as $cat_type => $cat){
            if ($i == 0 && !$vertical){
                $html .= "<li style='$extraLiStyle margin-left: 0px'> $cat   $cat_type</li>";
            } else {
                $html .= "<li style='$extraLiStyle margin-left: 25px'> $cat   $cat_type</li>";
            }
            $i++;
        }
        $html .= "</li>";

    }
    $html .= '</ul>';
    return $html;
  }

  public function get_icon_edm_type_map($type){
    return $this->edm_type_map[$type];
  }
 
}


