<?php
/*
Plugin Name: FlowPaper
Plugin URI: https://wordpress.org/plugins/flowpaper-lite-pdf-flipbook
Description: Shortcode for adding a PDF flipbook to a post: [flipbook pdf="http://yourwebsite.com/yourdocument.pdf"]. Replace the URL with a URL to a PDF document you want to publish.
Version: 1.9.1
Author: Devaldi Ltd
Author URI: https://flowpaper.com
License: GPLv3
*/

define('FLOWPAPER_PLUGIN_VERSION', '1.9.1');

function flowpaper_plugin_parse_request($wp) {
    try {
    if (array_key_exists('flowpaper-lite-plugin', $wp->query_vars)
            && $wp->query_vars['flowpaper-lite-plugin'] == 'get-pdf') {

            $uploadDir      = wp_upload_dir();
            $pdf_dir 	      = is_array($uploadDir) && sizeof($uploadDir)>0?$uploadDir['basedir']:WP_CONTENT_DIR . '/uploads';
            $path_parts     = pathinfo($_GET['pdf']);
            $file_name      = $path_parts['basename'];
            $attachment_id  = get_attachment_id_by_url_flowpaper($_GET['pdf']);
            $file_path      = ( $attachment_id ) ? get_attached_file( $attachment_id ) : null;
            $filepathinfo   = pathinfo($file_path);

          if($file_path ==null || strpos($filepathinfo['dirname'],$pdf_dir) != 0 || !validPDFParams($file_path,$file_name)){
            die();
            return;
          }else{
          $filesize = filesize($file_path);

          //unset magic quotes; otherwise, file contents will be modified
          if(version_compare(PHP_VERSION, '5.3.0', '<')){
		          set_magic_quotes_runtime(0);
		      }

          //do not send cache limiter header
          ini_set('session.cache_limiter','none');

          if($_SERVER['REQUEST_METHOD']=='HEAD'){
              $filesize=filesize($file_path);

              $browserRequestHeaders = getallheaders();
              $hasOrigin = false;

              foreach ($browserRequestHeaders as $name => $value) {
                if(strpos($name,'Origin') !== false && strpos($value,'flowpaper.com') !== false){
                      $hasOrigin = true;
                }
              }

              // Cors
              header('X-Robots-Tag: noindex, nofollow');
              header('Access-Control-Allow-Headers: range');
              header('Access-Control-Expose-Headers: Accept-Ranges, Content-Encoding, Content-Length, Content-Range');
              header('X-Content-Type-Options: nosniff');
              header('Content-Disposition: Attachment');

              if(!empty($_SERVER['HTTPS'])){
                  header('Access-Control-Allow-Origin: https://flowpaper.com');
              }else{
                  header('Access-Control-Allow-Origin: http://flowpaper.com');
              }

              header('Content-type: application/pdf');
              header('Content-Length: ' . $filesize);

              exit(0);
          }else if ($_SERVER['REQUEST_METHOD'] == 'OPTIONS') {
            if (isset($_SERVER['HTTP_ACCESS_CONTROL_REQUEST_METHOD']))
                header("Access-Control-Allow-Methods: GET, OPTIONS");

            if (isset($_SERVER['HTTP_ACCESS_CONTROL_REQUEST_HEADERS']))
                header('Access-Control-Allow-Headers: range');

              if(!empty($_SERVER['HTTPS'])){
                  header('Access-Control-Allow-Origin: https://flowpaper.com');
              }else{
                  header('Access-Control-Allow-Origin: http://flowpaper.com');
              }

            exit(0);
          }else{
              byteserve($file_path);
          }

          die();
        }
    }
    }catch (Exception $ex) {
        header('HTTP/1.1 500 Internal Server Error');
        die('Error loading PDF');
        // ignore errors as the viewer will use fallback method if pdf fails to load
    }
}

function set_range($range, $filesize, &$first, &$last){
  $dash=strpos($range,'-');
  $first=trim(substr($range,0,$dash));
  $last=trim(substr($range,$dash+1));
  if ($first=='') {
    //suffix byte range: gets last n bytes
    $suffix=$last;
    $last=$filesize-1;
    $first=$filesize-$suffix;
    if($first<0) $first=0;
  } else {
    if ($last=='' || $last>$filesize-1) $last=$filesize-1;
  }
  if($first>$last){
    //unsatisfiable range
    header("Status: 416 Requested range not satisfiable");
    header("Content-Range: */$filesize");
    exit;
  }
}

function buffered_read($file, $bytes, $buffer_size=1024){
  /*
  Outputs up to $bytes from the file $file to standard output, $buffer_size bytes at a time.
  */
  $bytes_left=$bytes;
  while($bytes_left>0 && !feof($file)){
    if($bytes_left>$buffer_size)
      $bytes_to_read=$buffer_size;
    else
      $bytes_to_read=$bytes_left;
    $bytes_left-=$bytes_to_read;
    $contents=fread($file, $bytes_to_read);
    echo $contents;
    flush();
  }
}

/* Byteserves the file $filename. */
function byteserve($filename){
  $filesize=filesize($filename);
  $file=fopen($filename,"rb");

  $ranges=NULL;
  if ($_SERVER['REQUEST_METHOD']=='GET' && isset($_SERVER['HTTP_RANGE']) && $range=stristr(trim($_SERVER['HTTP_RANGE']),'bytes=')){
    $range=substr($range,6);
    $boundary='g45d64df96bmdf4sdgh45hf5';//set a random boundary
    $ranges=explode(',',$range);
  }

  $browserRequestHeaders = getallheaders();
  $hasOrigin = false;

  foreach ($browserRequestHeaders as $name => $value) {
    if(strpos($name,'Origin') !== false && strpos($value,'flowpaper.com') !== false){
          $hasOrigin = true;
    }
  }

  // Cors
  header('X-Robots-Tag: noindex, nofollow');
  header('Access-Control-Allow-Headers: range');
  header('Access-Control-Expose-Headers: Accept-Ranges, Content-Encoding, Content-Length, Content-Range');
  header('X-Content-Type-Options: nosniff');
  header('Content-Disposition: Attachment');

  if(!empty($_SERVER['HTTPS'])){
      header('Access-Control-Allow-Origin: https://flowpaper.com');
  }else{
      header('Access-Control-Allow-Origin: http://flowpaper.com');
  }

  if($ranges && count($ranges)){
    header("HTTP/1.1 206 Partial content");
    header("Accept-Ranges: bytes");

    if(count($ranges)>1){
      /*
      More than one range is requested.
      */

      //compute content length
      $content_length=0;
      foreach ($ranges as $range){
        set_range($range, $filesize, $first, $last);
        $content_length+=strlen("\r\n--$boundary\r\n");
        $content_length+=strlen("Content-type: application/pdf\r\n");
        $content_length+=strlen("Content-range: bytes $first-$last/$filesize\r\n\r\n");
        $content_length+=$last-$first+1;
      }
      $content_length+=strlen("\r\n--$boundary--\r\n");

      //output headers
      header("Content-Length: $content_length");
      //see http://httpd.apache.org/docs/misc/known_client_problems.html for an discussion of x-byteranges vs. byteranges
      header("Content-Type: multipart/x-byteranges; boundary=$boundary");

      //output the content
      foreach ($ranges as $range){
        set_range($range, $filesize, $first, $last);
        echo "\r\n--$boundary\r\n";
        echo "Content-type: application/pdf\r\n";
        echo "Content-range: bytes $first-$last/$filesize\r\n\r\n";
        fseek($file,$first);
        buffered_read ($file, $last-$first+1);
      }
      echo "\r\n--$boundary--\r\n";
    } else {
      /*
      A single range is requested.
      */
      $range=$ranges[0];
      set_range($range, $filesize, $first, $last);
      header("Content-Length: ".($last-$first+1) );
      header("Content-Range: bytes $first-$last/$filesize");
      header("Content-Type: application/pdf");
      fseek($file,$first);
      buffered_read($file, $last-$first+1);
    }
  } else{
    //no byteserving
    header("Accept-Ranges: bytes");
    header("Content-Length: $filesize");
    header("Content-Type: application/pdf");
    readfile($filename);
  }
  fclose($file);
}

function serve($filename, $download=0){
  //Just serves the file without byteserving
  //if $download=true, then the save file dialog appears
  $filesize=filesize($filename);
  header("Content-Length: $filesize");
  header("Content-Type: application/pdf");
  $filename_parts=pathinfo($filename);
  if($download) header('Content-disposition: attachment; filename='.$filename_parts['basename']);
  readfile($filename);
}

function endsWith($haystack, $needle) {
    // search forward starting from end minus needle length characters
    return $needle === "" || (($temp = strlen($haystack) - strlen($needle)) >= 0 && strpos($haystack, $needle, $temp) !== false);
}

if (!function_exists("getallheaders")) {
  //Adapted from http://www.php.net/manual/en/function.getallheaders.php#99814
  function getallheaders() {
    $result = array();
    foreach($_SERVER as $key => $value) {
      if (substr($key, 0, 5) == "HTTP_") {
        $key = str_replace(" ", "-", ucwords(strtolower(str_replace("_", " ", substr($key, 5)))));
        $result[$key] = $value;
      }
    }
    return $result;
  }
}

function validPDFParams($path,$doc){
		return
          !(basename(realpath($path)) != $doc ||
          !endsWith(basename(realpath($path)),".pdf") ||
          !endsWith($doc,".pdf") ||
				 	strlen($doc) > 255 ||
				 	strpos($path . $doc, "../") > 0 ||
				 	preg_match("=^[^/?*;:{}\\\\]+[^/?*;:{}\\\\]+$=",$doc)==0
				);
}

function get_attachment_id_by_url_flowpaper( $url ) {
	// Split the $url into two parts with the wp-content directory as the separator
	$parsed_url  = explode( parse_url( WP_CONTENT_URL, PHP_URL_PATH ), $url );

	// Get the host of the current site and the host of the $url, ignoring www
	$this_host = str_ireplace( 'www.', '', parse_url( home_url(), PHP_URL_HOST ) );
	$file_host = str_ireplace( 'www.', '', parse_url( $url, PHP_URL_HOST ) );

	// Return nothing if there aren't any $url parts or if the current host and $url host do not match
	if ( ! isset( $parsed_url[1] ) || empty( $parsed_url[1] ) || ( $this_host != $file_host ) ) {
		return;
	}

	// Now we're going to quickly search the DB for any attachment GUID with a partial path match
	// Example: /uploads/2013/05/test-image.jpg
	global $wpdb;

	$attachment = $wpdb->get_col( $wpdb->prepare( "SELECT ID FROM {$wpdb->prefix}posts WHERE guid RLIKE %s;", $parsed_url[1] ) );

	// Returns null if no attachment is found
	return $attachment[0];
}

function add_lity_script(){
    wp_register_script('lity-js', plugins_url( '/assets/lity/lity.min.js', __FILE__ ), array( 'jquery' ), NULL, false);
    wp_enqueue_script( 'lity-js' );
    wp_enqueue_style('lity-css', plugins_url( '/assets/lity/lity.min.css', __FILE__ ), false, NULL, 'all');
}

function flowpaper_plugin_add_shortcode_cb( $atts, $content="" ) {
	$defaults = array(
		'width' => '100%',
		'height' => '500',
		'scrolling' => 'no',
		'class' => 'flowpaper-class',
		'frameborder' => '0',
		'allowFullScreen' => 'true'
	);

	foreach ( $defaults as $default => $value ) { // add defaults
		if ( ! @array_key_exists( $default, $atts ) ) { // mute warning with "@" when no params at all
			$atts[$default] = $value;
		}
	}

    // Check if we're embedding through a link or iframe
    $linkEmbed = false;
    foreach( $atts as $attr => $value ) {
        if(strtolower($attr) == 'lightbox' && $value == 'true'){
            $linkEmbed = true;
        }
    }

    $html           = "\n".'<!-- FlowPaper PDF flipbook plugin v.'.FLOWPAPER_PLUGIN_VERSION.' wordpress.org/plugins/flowpaper/ -->'."\n";

    if($linkEmbed){
        $title          = "";
        $cover          = "";
        $pdfUrl         = "";
        $theme          = "";
        $singlepage     = "auto";
        $thumbs         = true;
        $title          = "";
        $header         = "";
        $cloudhosted    = false;

        foreach( $atts as $attr => $value ) {
            if ( strtolower($attr) != 'same_height_as' AND strtolower($attr) != 'onload'
                AND strtolower($attr) != 'onpageshow' AND strtolower($attr) != 'onclick') { // remove some attributes

                if(strtolower($attr) == 'pdf'){ // append url at the end of the base url
                if(strpos($value,'publ.flowpaper.com') > 0 || strpos($value,'online.flowpaper.com') > 0 || !strpos(strtolower($value),'.pdf')){
                    $pdfUrl = esc_attr( $value );
                    $cloudhosted = true;

                    // make sure we're using https if the website that has embedded the viewer is using https
                    if(!empty($_SERVER['HTTPS'])){
                        $pdfUrl = str_replace("http://","https://",$pdfUrl);
                    }
                }else{
                    $pdfUrl = 'http://flowpaper.com/flipbook/?pdf=' . esc_attr( $value ) . (strpos(esc_attr($value),WP_CONTENT_URL)===0?"?wp-hosted=1":"");

                    if(!empty($_SERVER['HTTPS'])){ // use https if PDF is on a https url
                        $pdfUrl = 'https://flowpaper.com/flipbook/?pdf=' . esc_attr( $value ) . (strpos(esc_attr($value),WP_CONTENT_URL)===0?"?wp-hosted=1":"");
                    }
                }
              }else if(strtolower($attr) == 'theme'){
                 $theme = esc_attr( $value );
              }else if(strtolower($attr) == 'title'){
                 $title = esc_attr( $value );
              }else if(strtolower($attr) == 'cover'){
                 $cover = esc_attr( $value );
              }else if(strtolower($attr) == 'singlepage'){
                 $singlepage = esc_attr( $value );
              }
            }
        }

        $html          .= '<a data-lity href="' . $pdfUrl . (!$cloudhosted?('&title=' . $title . '&header=' . $header . '&theme=' . $theme . '&singlepage=' . $singlepage . '&thumbs=' . $thumbs . '&modified=' . get_the_modified_date('ymdgi')):'') . '">';
        if(strlen($cover) == 0 && strlen($title) > 0){
            $html      .= $title;
        }else if(strlen($cover) > 0){
            $html      .= '<img src="' . $cover . '" class="aligncenter">';
        }else{
            $html      .= $pdfUrl;
        }
        $html          .= '</a>';
    }else{
        $html          .= '<iframe title="FlowPaper website pdf viewer"';
        $pdfUrl         = "";
        $theme          = "";
        $singlepage     = "auto";
        $thumbs         = true;
        $title          = "";
        $header         = "";
        $cloudhosted    = false;

        foreach( $atts as $attr => $value ) {
            if ( strtolower($attr) != 'same_height_as' AND strtolower($attr) != 'onload'
                AND strtolower($attr) != 'onpageshow' AND strtolower($attr) != 'onclick') { // remove some attributes

                if(strtolower($attr) == 'pdf'){ // append url at the end of the base url
                    if(strpos($value,'publ.flowpaper.com') > 0 || strpos($value,'online.flowpaper.com') > 0 || !strpos(strtolower($value),'.pdf')){
                        $pdfUrl = esc_attr( $value );
                        $cloudhosted = true;

                        // make sure we're using https if the website that has embedded the viewer is using https
                        if(!empty($_SERVER['HTTPS'])){
                            $pdfUrl = str_replace("http://","https://",$pdfUrl);
                        }
                    }else{
                        $pdfUrl = 'http://flowpaper.com/flipbook/?pdf=' . esc_attr( $value ) . (strpos(esc_attr($value),WP_CONTENT_URL)===0?"?wp-hosted=1":"");

                        if(!empty($_SERVER['HTTPS'])){ // use https if PDF is on a https url
                            $pdfUrl = 'https://flowpaper.com/flipbook/?pdf=' . esc_attr( $value ) . (strpos(esc_attr($value),WP_CONTENT_URL)===0?"?wp-hosted=1":"");
                        }
                    }
                }else if(strtolower($attr) == 'theme'){
                      $theme = esc_attr( $value );
                }else if(strtolower($attr) == 'title'){
                      $title = esc_attr( $value );
                }else if(strtolower($attr) == 'header'){
                      $header = esc_attr( $value );
                }else if(strtolower($attr) == 'singlepage'){
                      $singlepage = esc_attr( $value );
                }else if(strtolower($attr) == 'thumbs'){
                      $thumbs = esc_attr( $value );
                } else if ( $value != '' ) { // adding all attributes
                      $html .= ' ' . esc_attr( $attr ) . '="' . esc_attr( $value ) . '"';
                } else { // adding empty attributes
                      $html .= ' ' . esc_attr( $attr );
                }
            }
        }

        $html .= ' src = "' . $pdfUrl . (!$cloudhosted?('&title=' . $title . '&header=' . $header . '&theme=' . $theme . '&singlepage=' . $singlepage . '&thumbs=' . $thumbs . '&modified=' . get_the_modified_date('ymdgi')):'') . '"';
        $html .= ' seamless="seamless">Your browser does not seem to support iframes. <a href="' . $pdfUrl . '" target="_blank">Click here to read this PDF</a>.</iframe>'."\n";

        if ( isset( $atts["same_height_as"] ) ) {
            $html .= '
                <script>
                document.addEventListener("DOMContentLoaded", function(){
                    var target_element, iframe_element;
                    iframe_element = document.querySelector("iframe.' . esc_attr( $atts["class"] ) . '");
                    target_element = document.querySelector("' . esc_attr( $atts["same_height_as"] ) . '");
                    iframe_element.style.height = target_element.offsetHeight + "px";
                });
                </script>
            ';
        }
    }
	return $html;
}

add_shortcode( 'flipbook', 'flowpaper_plugin_add_shortcode_cb' );
add_action('parse_request', 'flowpaper_plugin_parse_request');
add_action('wp_enqueue_scripts', 'add_lity_script');

function flowpaper_lite_queryvars($vars) {
    $vars[] = 'flowpaper-lite-plugin';
    return $vars;
}
add_filter('query_vars', 'flowpaper_lite_queryvars');

function flowpaper_plugin_row_meta_cb( $links, $file ) {
	if ( $file == plugin_basename( __FILE__ ) ) {
		$row_meta = array(
			'support' => '<a href="https://flowpaper.com/questions-and-answers.jsp" target="_blank"><span class="dashicons dashicons-editor-help"></span> ' . __( 'FlowPaper', 'flowpaper' ) . '</a>',
			'FlowPaper Publisher' => '<a href="https://flowpaper.com/" target="_blank"><span class="dashicons dashicons-star-filled"></span> ' . __( 'FlowPaper Publisher', 'flowpaper' ) . '</a>'
		);
		$links = array_merge( $links, $row_meta );
	}
	return (array) $links;
}
add_filter( 'plugin_row_meta', 'flowpaper_plugin_row_meta_cb', 10, 2 );
