<?php

/*
Widget Name: Testimonials
Description: Testimonials.
Author: Ingenious Solutions
Author URI: http://ingenious-web.com/
*/

class Testimonials extends SiteOrigin_Widget {
	function __construct() {

		parent::__construct(
			'testimonials',
			__('Testimonials', 'addon-so-widgets-bundle'),
			array(
				'description' => __('Testimonials.', 'addon-so-widgets-bundle'),
                'panels_icon' => 'dashicons dashicons-testimonial',
                'panels_groups' => array('addonso')
			),
			array(

			),
			array(
                'widget_title' => array(
                    'type' => 'text',
                    'label' => __('Widget Title', 'addon-so-widgets-bundle'),
                    'default' => ''
                ),


                'posts' => array(
                    'type' => 'posts',
                    'label' => __('Select Testimonials', 'addon-so-widgets-bundle'),
                ),


                'layout_selection' => array(
                    'type' => 'radio',
                    'label' => __( 'Choose a style', 'addon-so-widgets-bundle' ),
                    'default' => 'rotator',
                    'options' => array(
                        'rotator' => __( 'Testimonial Rotator', 'addon-so-widgets-bundle' ),
                        'grid' => __( 'Testimonial Gird ', 'addon-so-widgets-bundle' ),
                    )
                ),



                'grid_selection' => array(
                    'type' => 'radio',
                    'label' => __( 'Choose a Gird Layout ( Only for Testimonial Grid )', 'addon-so-widgets-bundle' ),
                    'default' => 'six columns',
                    'options' => array(
                    'six columns' => __( 'Two Columns', 'addon-so-widgets-bundle' ),
                    'four columns' => __( 'Three Columns', 'addon-so-widgets-bundle' ),
                    'three columns' => __( 'Four Columns', 'addon-so-widgets-bundle' ),
                    'twelve columns' => __( 'Full Width', 'addon-so-widgets-bundle' ),
                    )
                ),


                'testimonial_styling' => array(
                    'type' => 'section',
                    'label' => __( 'Widget styling' , 'addon-so-widgets-bundle' ),
                    'hide' => true,
                    'fields' => array(

                        'bg_color' => array(
                            'type' => 'color',
                            'label' => __( 'background color', 'addon-so-widgets-bundle' ),
                            'default' => ''
                        ),

                        'content_color' => array(
                            'type' => 'color',
                            'label' => __( 'Content color', 'addon-so-widgets-bundle' ),
                            'default' => ''
                        ),
                       

                    )
                ),
                
                



			),
			plugin_dir_path(__FILE__)
		);
	}

	function get_template_name($instance) {
		return 'testimonials-template';
	}

	function get_style_name($instance) {
		return 'testimonials-style';
	}

    function get_less_variables( $instance ) {
        return array(
            'bg_color' => $instance['testimonial_styling']['bg_color'] . '!important',
            'content_color' => $instance['testimonial_styling']['content_color'] . '!important',
        );
    }

}


function my_custom_post_product() {
    $labels = array(
        'name'               => _x( 'Testimonial', 'post type general name' ),
        'singular_name'      => _x( 'Testimonial', 'post type singular name' ),
        'add_new'            => _x( 'Add New', 'testimonial' ),
        'add_new_item'       => __( 'Add New Testimonial' ),
        'edit_item'          => __( 'Edit Testimonial' ),
        'new_item'           => __( 'New Testimonial' ),
        'all_items'          => __( 'All Testimonial' ),
        'view_item'          => __( 'View Testimonial' ),
        'search_items'       => __( 'Search Testimonial' ),
        'not_found'          => __( 'No testimonial found' ),
        'not_found_in_trash' => __( 'No testimonial found in the Trash' ),
        'parent_item_colon'  => '',
        'menu_name'          => 'Testimonial'
    );
    $args = array(
        'labels'        => $labels,
        'description'   => 'Holds our products and product specific data',
        'public'        => true,
        'menu_position' => 5,
        'supports'      => array( 'title', 'editor', 'thumbnail', 'excerpt', 'comments' ),
        'has_archive'   => true,
    );
    register_post_type( 'testimonial', $args );
}
add_action( 'init', 'my_custom_post_product' );




// script

add_action('wp_footer','carousel_scripts');

function carousel_scripts()
{

    echo "<script>

jQuery(document).ready(function($){

        (function ($) {

            $('.testimonial_carousel').owlCarousel({
                autoPlay: 3000 , //Set AutoPlay to 3 seconds
                navigation:false,
                items : 1
            });

        })(jQuery);

    });

</script>";

}





siteorigin_widget_register('testimonials', __FILE__, 'Testimonials');