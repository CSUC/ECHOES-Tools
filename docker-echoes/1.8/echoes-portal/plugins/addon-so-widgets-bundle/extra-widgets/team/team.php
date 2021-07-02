<?php

/*
Widget Name: Team
Description: Team.
Author: Ingenious Solutions
Author URI: http://ingenious-web.com/
*/

class Team extends SiteOrigin_Widget
{
    function __construct()
    {

        parent::__construct(
            'team',
            __('Team', 'team-text-domain'),
            array(
                'description' => __('Team.', 'addon-so-widgets-bundle'),
                'panels_icon' => 'dashicons dashicons-leftright',
                'panels_groups' => array('addonso')
            ),
            array(),

            array(
                'widget_title' => array(
                    'type' => 'text',
                    'label' => __('Widget Title.', 'addon-so-widgets-bundle'),
                    'default' => ''
                ),


                'team_image' => array(
                'type' => 'media',
                'label' => __( 'Avatar Image', 'addon-so-widgets-bundle' ),
                'choose' => __( 'Choose image', 'addon-so-widgets-bundle' ),
                'update' => __( 'Set image', 'addon-so-widgets-bundle' ),
                'library' => 'image'
                 ),

                'team_title' => array(
                    'type' => 'text',
                    'label' => __('Name', 'addon-so-widgets-bundle'),
                    'default' => ''
                ),

                'company_title' => array(
                    'type' => 'text',
                    'label' => __('Position', 'addon-so-widgets-bundle'),
                    'default' => ''
                ),


                'detail_box' => array(
                    'type' => 'textarea',
                    'label' => __( 'Short Details', 'addon-so-widgets-bundle' ),
                    'rows' => 10
                ),

                'team_social_repeater' => array(
                'type' => 'repeater',
                'label' => __( 'Add Social Icons' , 'addon-so-widgets-bundle' ),
                'item_name'  => __( 'Add item', 'addon-so-widgets-bundle' ),
                'item_label' => array(
                    'selector'     => "[id*='repeat_text']",
                    'update_event' => 'change',
                    'value_method' => 'val'
                ),
                'fields' => array(

                    'team_social_url' => array(
                        'type' => 'text',
                        'label' => __('Social profile link', 'addon-so-widgets-bundle'),
                        'default' => ''
                    ),


                         'team_social_icon' => array(
                         'type' => 'icon',
                         'label' => __('Social profile icon', 'addon-so-widgets-bundle'),
                     ),

                )
                ),

                'team_styling' => array(
                    'type' => 'section',
                    'label' => __( 'Widget styling' , 'addon-so-widgets-bundle' ),
                    'hide' => true,
                    'fields' => array(


                        'title_color' => array(
                            'type' => 'color',
                            'label' => __( 'Name text color', 'addon-so-widgets-bundle' ),
                            'default' => ''
                        ),

                        'company_name_color' => array(
                            'type' => 'color',
                            'label' => __( 'Position text color', 'addon-so-widgets-bundle' ),
                            'default' => ''
                        ),

                        'content_color' => array(
                            'type' => 'color',
                            'label' => __( 'Short details text color', 'addon-so-widgets-bundle' ),
                            'default' => ''
                        ),

                        'social_icon_color' => array(
                            'type' => 'color',
                            'label' => __( 'Social Icons color', 'addon-so-widgets-bundle' ),
                            'default' => ''
                        ),

                        'social_icon_size' => array(
                            'type' => 'slider',
                            'label' => __( 'Social icons size', 'addon-so-widgets-bundle' ),
                            'default' => 24,
                            'min' => 2,
                            'max' => 500,
                            'integer' => true
                        ),


                    )
                ),

            ),


            plugin_dir_path(__FILE__)
        );
    }

    function get_template_name($instance)
    {
        return 'team-template';
    }

    function get_style_name($instance)
    {
        return 'team-style';
    }

    function get_less_variables( $instance ) {
        return array(
            'title_color' => $instance['team_styling']['title_color'],
            'company_name_color' => $instance['team_styling']['company_name_color'],
            'content_color' => $instance['team_styling']['content_color'],
            'social_icon_color' => $instance['team_styling']['social_icon_color'],
            'social_icon_size' => $instance['team_styling']['social_icon_size'].'px',
        );
    }

}

siteorigin_widget_register('team', __FILE__, 'Team');