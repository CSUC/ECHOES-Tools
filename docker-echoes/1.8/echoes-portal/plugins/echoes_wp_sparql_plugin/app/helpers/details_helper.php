<?php

class DetailsHelper extends MvcHelper {

  private function get_current_locale() {
        $url = $_SERVER['REQUEST_URI'];
        if (strpos($url, '/ca/') !== false) {
                return '/ca';
        } elseif (strpos($url, '/nl/') !== false) {
                return '/nl';
	} elseif (strpos($url, '/fy/') !== false) {
                return '/fy';
        } else {
                return '';
        }
  }
 
  public function print_table($result, $var_to_labels_match = [], $fields_to_ignore = []) {
    $html = '<table style="width: 100%">';
    foreach ($result->head->vars as $header_cell) {
        if (in_array($header_cell, $fields_to_ignore) || !isset($result->results->bindings[0]->$header_cell->value) || empty($result->results->bindings[0]->$header_cell->value)){
            continue;
        }
        $html .= '<tr>';
        if (array_key_exists($header_cell, $var_to_labels_match)){
            $header = $var_to_labels_match[$header_cell];
        } else {
            $header = $header_cell;
        }
        $html .= "<th>$header</th>";
        $html .= "<td>{$result->results->bindings[0]->$header_cell->value}</td>";
        $html .= '</tr>';
    }
    $html .= '</table>';
    echo $html;
  }
  
  public function print_download_sidebar($subject){
    $photoPath = get_bloginfo('template_url');
    $get_data_translation = __('Get Data', 'echoes_wp_sparql_plugin'); 
    $binary_translation = __('Binary', 'echoes_wp_sparql_plugin');
    $download_translation = __('Download', 'echoes_wp_sparql_plugin'); 
    $view_translation = __('View', 'echoes_wp_sparql_plugin'); 
    $sidebar = <<<EOT
<div class="getData">
    <div class="getDataTitle">
        <img src="$photoPath/img/Assets/ico-download-blue.svg" /><h3 class="widgettitle"><strong>$get_data_translation</strong></h3>
    <div class="avia-icon-list-container avia-builder-el-7 el_before_av_sidebar avia-builder-el-first"><ul class="avia-icon-list avia-icon-list-left av-iconlist-small avia_animate_when_almost_visible">
    <li><div style="background-color:#f05a1a; color:#ffffff; " class="iconlist_icon  avia-font-entypo-fontello"><span class="iconlist-char " aria-hidden="true" data-av_icon="" data-av_iconfont="entypo-fontello"></span></div><article class="article-icon-entry " itemscope="itemscope" itemtype="https://schema.org/CreativeWork"><div class="iconlist_content_wrap"><header style="margin-bottom: 10px" class="entry-content-header"><div class="av_iconlist_title iconlist_title_small imgData" itemprop="headline"><img src="$photoPath/img/Assets/Bitmap-01.png" />JSON: <a target="_blank" href="/exports/download/?subject=$subject&format=json">$download_translation</a> | <a target="_blank" href="/exports/view/?subject=$subject&format=json">$view_translation</a></div></header><div class="iconlist_content  " itemprop="text">
                    
                </div></div><footer class="entry-footer"></footer></article><div class="iconlist-timeline"></div></li>
    <li><div style="background-color:#f05a1a; color:#ffffff; " class="iconlist_icon  avia-font-entypo-fontello"><span class="iconlist-char " aria-hidden="true" data-av_icon="" data-av_iconfont="entypo-fontello"></span></div><article class="article-icon-entry " itemscope="itemscope" itemtype="https://schema.org/CreativeWork"><div class="iconlist_content_wrap"><header style="margin-bottom: 10px" class="entry-content-header"><div class="av_iconlist_title iconlist_title_small imgData" itemprop="headline"><img src="$photoPath/img/Assets/Bitmap-02.png" />XML: <a target="_blank" href="/exports/download/?subject=$subject&format=xml">$download_translation</a> | <a target="_blank" href="/exports/view/?subject=$subject&format=xml">$view_translation</a></div></header><div class="iconlist_content  " itemprop="text">
                    
                </div></div><footer class="entry-footer"></footer></article><div class="iconlist-timeline"></div></li>
    <li><div style="background-color:#f05a1a; color:#ffffff; " class="iconlist_icon  avia-font-entypo-fontello"><span class="iconlist-char " aria-hidden="true" data-av_icon="" data-av_iconfont="entypo-fontello"></span></div><article class="article-icon-entry " itemscope="itemscope" itemtype="https://schema.org/CreativeWork"><div class="iconlist_content_wrap"><header style="margin-bottom: 10px" class="entry-content-header"><div class="av_iconlist_title iconlist_title_small imgData" itemprop="headline"><img src="$photoPath/img/Assets/Bitmap-03.png" />CSV: <a target="_blank" href="/exports/download/?subject=$subject&format=csv">$download_translation</a> | <a target="_blank" href="/exports/view/?subject=$subject&format=csv">$view_translation</a></div></header><div class="iconlist_content  " itemprop="text">
                    
                </div></div><footer class="entry-footer"></footer></article><div class="iconlist-timeline"></div></li>
                <li><div style="background-color:#f05a1a; color:#ffffff; " class="iconlist_icon  avia-font-entypo-fontello"><span class="iconlist-char" aria-hidden="true" data-av_icon="" data-av_iconfont="entypo-fontello"></span></div><article class="article-icon-entry " itemscope="itemscope" itemtype="https://schema.org/CreativeWork"><div class="iconlist_content_wrap"><header style="margin-bottom: 10px" class="entry-content-header"><div class="av_iconlist_title iconlist_title_small imgData" itemprop="headline"><img src="$photoPath/img/Assets/Bitmap-03.png" />TSV: <a target="_blank" href="/exports/download/?subject=$subject&format=tsv">$download_translation</a> | <a target="_blank" href="/exports/view/?subject=$subject&format=tsv">$view_translation</a></div></header><div class="iconlist_content" itemprop="text">
                    
                </div></div><footer class="entry-footer"></footer></article><div class="iconlist-timeline"></div></li>
                <li><div style="background-color:#f05a1a; color:#ffffff; " class="iconlist_icon  avia-font-entypo-fontello"><span class="iconlist-char" aria-hidden="true" data-av_icon="" data-av_iconfont="entypo-fontello"></span></div><article class="article-icon-entry " itemscope="itemscope" itemtype="https://schema.org/CreativeWork"><div class="iconlist_content_wrap"><header style="margin-bottom: 10px" class="entry-content-header"><div class="av_iconlist_title iconlist_title_small imgData" itemprop="headline"><img src="$photoPath/img/Assets/Bitmap-05.png" />$binary_translation: <a target="_blank" href="/exports/download/?subject=$subject&format=bin">$download_translation</a> | <a target="_blank" href="/exports/view/?subject=$subject&format=bin">$view_translation</a></div></header><div class="iconlist_content" itemprop="text">
                    
                </div></div><footer class="entry-footer"></footer></article><div class="iconlist-timeline"></div></li>
    </ul></div>
</div>
EOT;
    echo str_replace("/exports", WP_SITEURL . "/exports", $sidebar);

  }

  public function search_bar(){
    if (isset($_GET['s'])){
        $s = $_GET['s'];
    } else {
        $s = '';
    }
    $current_locale = $this->get_current_locale(); 
    echo '
    <div style="margin-top: 15px;" class="avia-builder-widget-area clearfix avia-builder-el-8 el_after_av_iconlist avia-builder-el-last "><section id="widget_blazegraph_search-9" class="widget clearfix widget_widget_blazegraph_search">

        <form action="'. $current_locale . '/agents/search" id="searchform" method="get" autocomplete="off" >
                <div>
                        <input id="s" type="text" class="button-search-bar" placeholder="' . __('First name', 'echoes_wp_sparql_plugin') . '" name="first_name">
                        <input id="date" type="text" class="button-search-bar" placeholder="' . __('Last name', 'echoes_wp_sparql_plugin') . '" name="last_name">
                        <input value="' . __('Search', 'echoes_wp_sparql_plugin') . '" id="searchsubmit" class="search-bar-submit" type="submit">
                </div>
        </form>'; 
  }

  public function search_barYear(){
    if (isset($_GET['s'])){
        $s = $_GET['s'];
    } else {
        $s = '';
    }
    $current_locale = $this->get_current_locale();
    echo '
    <div style="margin-top: 15px;" class="avia-builder-widget-area clearfix avia-builder-el-8 el_after_av_iconlist avia-builder-el-last "><section id="widget_blazegraph_search-9" class="widget clearfix widget_widget_blazegraph_search">

        <form action="'. $current_locale . '/timespans" id="searchform" method="get" autocomplete="off" >
                <div>
                        <input id="s" type="text" class="button-search-bar" placeholder="' . __('Year', 'echoes_wp_sparql_plugin') . '" name="year">
                        <input value="' . __('Search', 'echoes_wp_sparql_plugin') . '" id="searchsubmit" class="search-bar-submit" type="submit">
                </div>
        </form>';
  }

  public function list($objects){
      $count = 0;
      $html .= '<div class="row">';
      foreach ($objects as $object){
          if ($count >= 3) {
                $html .= '</div>';
                $html .= '<div class="row">';
		$count = 0;
          }
          $html .= "<div class='col-4 people-details'>$object</div>";
          $count++;
      }
      $html .= '</div>';
      return $html;
  }

  public function list_timespans($objects){
      $count = 0;
      $html .= '<div class="people-list">';
      foreach ($objects as $object){
          if ($count == 25 || $count == 50) {
                $html .= '</div>';
                $html .= '<div class="people-list">';
          }
          $html .= "<div class='people-details' style='height: 2em; margin-left: 5em;'>$object</div><br/>";
          $count++;
      }
      $html .= '</div>';
      return $html;
  }


}

