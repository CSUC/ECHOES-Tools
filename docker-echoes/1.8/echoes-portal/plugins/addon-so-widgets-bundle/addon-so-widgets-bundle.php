<?php
/*
Plugin Name: Ultimate Addons for SiteOrigin
Description: An ultimate collection of addons for SiteOrigin. SiteOrigin Widgets Bundle is required.
Version: 2.4.1
Author: Ingenious Solution
Author URI: http://ingenious-web.com/
Plugin URI: http://ingenious-web.com/ultimate-addons-for-siteorigin/
License: GPL3
License URI: https://www.gnu.org/licenses/gpl-3.0.txt
*/

require_once('cmb-functions.php');


function addon_so_widgets_bundle($folders){
	$folders[] = plugin_dir_path(__FILE__).'extra-widgets/';
	return $folders;
}
add_filter('siteorigin_widgets_widget_folders', 'addon_so_widgets_bundle');



add_action('wp_footer','scripts');
function scripts()
{
    wp_enqueue_style( 'owl-css', plugin_dir_url(__FILE__) . 'css/owl.carousel.css');
    wp_enqueue_style( 'widgets-css', plugin_dir_url(__FILE__) . 'css/widgets.css');
    wp_enqueue_script( 'owl-js', plugin_dir_url(__FILE__) .'js/owl.carousel.min.js');

}


//footer
add_action('wp_footer','f_scripts');

function f_scripts()
{ ?>

  <script>
      jQuery(document).ready(function($){


      equalheight = function(container){

          var currentTallest = 0,
              currentRowStart = 0,
              rowDivs = new Array(),
              $el,
              topPosition = 0;
          $(container).each(function() {

              $el = $(this);
              $($el).height('auto')
              topPostion = $el.position().top;

              if (currentRowStart != topPostion) {
                  for (currentDiv = 0 ; currentDiv < rowDivs.length ; currentDiv++) {
                      rowDivs[currentDiv].height(currentTallest);
                  }
                  rowDivs.length = 0; // empty the array
                  currentRowStart = topPostion;
                  currentTallest = $el.height();
                  rowDivs.push($el);
              } else {
                  rowDivs.push($el);
                  currentTallest = (currentTallest < $el.height()) ? ($el.height()) : (currentTallest);
              }
              for (currentDiv = 0 ; currentDiv < rowDivs.length ; currentDiv++) {
                  rowDivs[currentDiv].height(currentTallest);
              }
          });
      }

      $(window).load(function() {
          equalheight('.all_main .auto_height');
      });


      $(window).resize(function(){
          equalheight('.all_main .auto_height');
      });


      });
  </script>

   <?php



    echo "<script>



//    tabs

jQuery(document).ready(function($){

	(function ($) {
		$('.soua-tab ul.soua-tabs').addClass('active').find('> li:eq(0)').addClass('current');

		$('.soua-tab ul.soua-tabs li a').click(function (g) {
			var tab = $(this).closest('.soua-tab'),
				index = $(this).closest('li').index();

			tab.find('ul.soua-tabs > li').removeClass('current');
			$(this).closest('li').addClass('current');

			tab.find('.tab_content').find('div.tabs_item').not('div.tabs_item:eq(' + index + ')').slideUp();
			tab.find('.tab_content').find('div.tabs_item:eq(' + index + ')').slideDown();

			g.preventDefault();
		} );





//accordion

//    $('.accordion > li:eq(0) a').addClass('active').next().slideDown();

    $('.soua-main .soua-accordion-title').click(function(j) {
        var dropDown = $(this).closest('.soua-accordion').find('.soua-accordion-content');

        $(this).closest('.soua-accordion').find('.soua-accordion-content').not(dropDown).slideUp();

        if ($(this).hasClass('active')) {
            $(this).removeClass('active');
        } else {
            $(this).closest('.soua-accordion').find('.soua-accordion-title .active').removeClass('active');
            $(this).addClass('active');
        }

        dropDown.stop(false, true).slideToggle();

        j.preventDefault();
    });
})(jQuery);



});

</script>";

}


//pannel Group

function addonso($tabs) {
    $tabs[] = array(
        'title' => __('Ultimate Addon Bundle', 'addonso'),
        'filter' => array(
            'groups' => array('addonso')
        )
    );

    return $tabs;
}
add_filter('siteorigin_panels_widget_dialog_tabs', 'addonso', 20);



function excerpt($limit) {
      $excerpt = explode(' ', get_the_excerpt(), $limit);
      if (count($excerpt)>=$limit) {
        array_pop($excerpt);
        $excerpt = implode(" ",$excerpt).'';
      } else {
        $excerpt = implode(" ",$excerpt);
      } 
      $excerpt = preg_replace('`\[[^\]]*\]`','',$excerpt);
      return $excerpt;
    }


add_image_size( 'widgets_image_size', 800, 9999 , true);

