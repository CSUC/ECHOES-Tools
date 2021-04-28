
<?php

class PaginationHelper extends MvcHelper {
  
  public function get_link($page) {
    $ret = $this->get_link_ret($page);
    echo $ret;
  }

  public function get_link_ret($page) {
    $ret = preg_replace('/&page=\d+/', "&page=$page", $_SERVER['REQUEST_URI'], -1, $count);
    if (!$count){
      $ret = $_SERVER['REQUEST_URI'] . "search?s=&page=$page";
    }
    return $ret;
  }
  
  public function show($num_pages){
    $page = $_GET['page'];  
    $html = <<<EOT
    <nav style="margin-left:22em;" class="pagination">
      <span class="pagination-meta">Page $page of $num_pages</span>
EOT;
    $html = "<nav style='margin-left:22em; display: flex !important;' class='pagination'>
    <span class='pagination-meta'>" . sprintf(esc_html__('Page %1$d of %2$d', 'echoes_wp_sparql_plugin'), $page, $num_pages) . "</span>"; # $page $num_pages 
    # $html_aux = "<nav style='margin-left:22em;' class='pagination'><span class='pagination-meta'>".sprintf(__('Page %1$i of %2$i'), $page, $num_pages)."</span>"
    $html .= '<div style="margin-left:15px; margin-bottom: 25px;">';

       if ($page > 2): 
          $html .= "<a href='{$this->get_link_ret(1)}'>«</a>";
       endif;   

       if (($page - 1) >= 1): 
          $html .= "<a style='margin-left:2px' href='{$this->get_link_ret($page - 1)}'>‹</a>";
       endif; 

       if (($page - 2) >= 1): 
          $html .= "<a style='margin-left:2px' href='{$this->get_link_ret($page - 2)}'>" . ($page - 2) . "</a>";
       endif; 

       if (($page - 1) >= 1): 
        $html .= "<a style='margin-left:2px' href='{$this->get_link_ret($page - 1)}'>" . ($page - 1) . "</a>";
       endif; 

       $html .= "<span style='margin-left:2px' class='current'>$page</span>";
       if (($page + 1) <= $num_pages): 
        $html .= "<a style='margin-left:2px' href='{$this->get_link_ret($page + 1)}'>" . ($page + 1) . "</a>";
       endif; 

       if (($page + 2) <= $num_pages): 
        $html .= "<a style='margin-left:2px' href='{$this->get_link_ret($page + 2)}'>" . ($page + 2) . "</a>";
       endif; 

       if (($page + 1) <= $num_pages): 
        $html .= "<a style='margin-left:2px' href='{$this->get_link_ret($page + 1)}'>›</a>";
       endif; 

       if (($page + 2) <= $num_pages): 
        $html .= "<a style='margin-left:2px' href='{$this->get_link_ret($num_pages)}'>»</a>";
       endif;  

  $html .= '</div>';
  $html .= '</nav>';
 return $html;

  }
}
