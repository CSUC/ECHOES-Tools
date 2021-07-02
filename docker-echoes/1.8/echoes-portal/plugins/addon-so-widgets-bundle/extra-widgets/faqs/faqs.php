<?php

/*
Widget Name: FAQs
Description: This widget Display Faq.
Author: Ingenious Solutions
Author URI: http://ingenious-web.com/
*/

class Faqs extends SiteOrigin_Widget
{
    function __construct()
    {

        parent::__construct(
            'faqs',
            __('Faqs', 'addon-so-widgets-bundle'),
            array(
                'description' => __('FAQs Component', 'addon-so-widgets-bundle'),
                'panels_icon' => 'dashicons dashicons-exerpt-view',
                'panels_groups' => array('addonso')
            ),
            array(),
            array(
                'widget_title' => array(
                    'type' => 'text',
                    'label' => __('Widget Title.', 'addon-so-widgets-bundle'),
                    'default' => ''
                ),

                'posts' => array(
                    'type' => 'posts',
                    'label' => __('Select FAQs', 'addon-so-widgets-bundle'),
                ),

                'faqs_styling' => array(
                    'type' => 'section',
                    'label' => __( 'Widget styling' , 'addon-so-widgets-bundle' ),
                    'hide' => true,
                    'fields' => array(

                        'toggle_padding' => array(
                            'type' => 'text',
                            'label' => __('Title Padding (shorthand Format)', 'widget-form-fields-text-domain'),
                            'default' => '0px 0px 0px 0px'
                        ),

                        'title_bg' => array(
                            'type' => 'color',
                            'label' => __( 'backgorund color', 'addon-so-widgets-bundle' ),
                            'default' => ''
                        ),

                        'title_bg_hover' => array(
                            'type' => 'color',
                            'label' => __( 'backgorund Hover color', 'addon-so-widgets-bundle' ),
                            'default' => ''
                        ),

                        'title_color' => array(
                            'type' => 'color',
                            'label' => __( 'Title color', 'addon-so-widgets-bundle' ),
                            'default' => ''
                        ),

                        'title_bar_alignment' => array(
                            'type' => 'select',
                            'label' => __( 'Title Alignment', 'addon-so-widgets-bundle' ),
                            'default' => 'left',
                            'options' => array(
                                'left' => __( 'Left Align', 'addon-so-widgets-bundle' ),
                                'center' => __( 'Center Align', 'addon-so-widgets-bundle' ),
                                'right' => __( 'Right Align', 'addon-so-widgets-bundle' ),
                            )
                        ),

                        'content_bg_color' => array(
                            'type' => 'color',
                            'label' => __( 'Content backgorund color', 'addon-so-widgets-bundle' ),
                            'default' => ''
                        ),

                        'content_color' => array(
                            'type' => 'color',
                            'label' => __( 'Content Text color', 'addon-so-widgets-bundle' ),
                            'default' => ''
                        ),


                        'toggle_border' => array(
                            'type' => 'select',
                            'label' => __( 'Border style', 'addon-so-widgets-bundle' ),
                            'default' => '',
                            'options' => array(
                                'none' => __( 'No Border', 'addon-so-widgets-bundle' ),
                                'solid' => __( 'Solid', 'addon-so-widgets-bundle' ),
                                'dashed' => __( 'Dashed', 'addon-so-widgets-bundle' ),
                                'dotted' => __( 'Dotted', 'addon-so-widgets-bundle' ),
                            )
                        ),

                        'toggle_border_width' => array(
                            'type' => 'number',
                            'label' => __( 'Border Width (without px)', 'addon-so-widgets-bundle' ),
                            'default' => ''
                        ),

                        'toggle_border_color' => array(
                            'type' => 'color',
                            'label' => __( 'Border color', 'addon-so-widgets-bundle' ),
                            'default' => ''
                        ),

                        'toggle_top_border' => array(
                            'type' => 'checkbox',
                            'label' => __( 'Box Top Border', 'widget-form-fields-text-domain' ),
                            'default' => false
                        ),

                        'toggle_right_border' => array(
                            'type' => 'checkbox',
                            'label' => __( 'Box Right Border', 'widget-form-fields-text-domain' ),
                            'default' => false
                        ),

                        'toggle_bottom_border' => array(
                            'type' => 'checkbox',
                            'label' => __( 'Box Bottom Border', 'widget-form-fields-text-domain' ),
                            'default' => false
                        ),

                        'toggle_left_border' => array(
                            'type' => 'checkbox',
                            'label' => __( 'Box Left Border', 'widget-form-fields-text-domain' ),
                            'default' => false
                        ),




                    )
                ),


            ),
            plugin_dir_path(__FILE__)
        );
    }

    function get_template_name($instance)
    {
        return 'faqs-template';
    }

    function get_style_name($instance)
    {
        return 'faqs-style';
    }

    function get_less_variables( $instance ) {
        return array(
            'title_bar_alignment' => $instance['faqs_styling']['title_bar_alignment'],
            'title_bg' => $instance['faqs_styling']['title_bg'],
            'title_bg_hover' => $instance['faqs_styling']['title_bg_hover'],
            'title_color' => $instance['faqs_styling']['title_color'],
            'content_bg_color' => $instance['faqs_styling']['content_bg_color'],
            'content_color' => $instance['faqs_styling']['content_color'],
            'toggle_border' => $instance['faqs_styling']['toggle_border'],
            'toggle_border_width' => $instance['faqs_styling']['toggle_border_width'].'px',
            'toggle_border_color' => $instance['faqs_styling']['toggle_border_color'],
            'toggle_padding' => $instance['faqs_styling']['toggle_padding'],
        );
    }

}


function faq() {
    $labels = array(
        'name'               => _x( 'Faq', 'post type general name' ),
        'singular_name'      => _x( 'Faq', 'post type singular name' ),
        'add_new'            => _x( 'Add New', 'Faq' ),
        'add_new_item'       => __( 'Add New Faq' ),
        'edit_item'          => __( 'Edit Faq' ),
        'new_item'           => __( 'New Faq' ),
        'all_items'          => __( 'All Faqs' ),
        'view_item'          => __( 'View Faq' ),
        'search_items'       => __( 'Search Faq' ),
        'not_found'          => __( 'No Faq found' ),
        'not_found_in_trash' => __( 'No Faq found in the Trash' ),
        'parent_item_colon'  => '',
        'menu_name'          => 'Faq'
    );
    $args = array(
        'labels'        => $labels,
        'description'   => 'Holds our products and product specific data',
        'public'        => true,
        'menu_position' => 5,
        'supports'      => array( 'title', 'editor', 'thumbnail', 'excerpt', 'comments' ),
        'has_archive'   => true,
    );
    register_post_type( 'faq', $args );
}
add_action( 'init', 'faq' );




siteorigin_widget_register('faqs', __FILE__, 'Faqs');