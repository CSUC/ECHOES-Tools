<?php

class TranslationHelper extends MvcHelper {

  public static function get_current_locale() {
    $url = $_SERVER['REQUEST_URI'];
    if (strpos($url, '/ca/') !== false) {
      return '/ca';
    } elseif (strpos($url, '/nl/') !== false) {
      return '/nl';
    } elseif (strpos($url, '/fy/') !== false) {
      return '/fy';
    } else {
      return '';         }
  }
}

?>	
