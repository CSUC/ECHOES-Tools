<?php

/*
Widget Name: Divider
Description: Divider.
Author: Ingenious Solutions
Author URI: http://ingenious-web.com/
*/

class Divider extends SiteOrigin_Widget {
	function __construct() {

		parent::__construct(
			'divider',
			__('Divider', 'addon-so-widgets-bundle'),
			array(
				'description' => __('Divider .', 'addon-so-widgets-bundle'),
                'panels_icon' => 'dashicons dashicons-minus',
                'panels_groups' => array('addonso')
			),
			array(

			),
			array(
                'widget_title' => array(
                    'type' => 'text',
                    'label' => __('Widget Title.', 'addon-so-widgets-bundle'),
                    'default' => ''
                ),

                'divider_style' => array(
                    'type' => 'select',
                    'label' => __( 'Style', 'addon-so-widgets-bundle' ),
                    'default' => 'solid',
                    'options' => array(
                        'solid' => __( 'Solid', 'addon-so-widgets-bundle' ),
                        'double' => __( 'Double', 'addon-so-widgets-bundle' ),
                        'dashed' => __( 'Dashed', 'addon-so-widgets-bundle' ),
                        'dotted' => __( 'Dotted', 'addon-so-widgets-bundle' ),
                    )
                ),


                'border_width' => array(
                    'type' => 'slider',
                    'label' => __( 'Border Bottom Width', 'addon-so-widgets-bundle' ),
                    'default' => 3,
                    'min' => 2,
                    'max' => 50,
                    'integer' => true
                ),

                'divider_color' => array(
                    'type' => 'color',
                    'label' => __( 'Divder color', 'addon-so-widgets-bundle' ),
                    'default' => '#ccc'
                ),

                'margin_top' => array(
                    'type' => 'number',
                    'label' => __( 'Top Margin (without px)', 'addon-so-widgets-bundle' ),
                    'default' => ''
                ),

                'margin_bottom' => array(
                    'type' => 'number',
                    'label' => __( 'Bottom Margin (without px)', 'addon-so-widgets-bundle' ),
                    'default' => ''
                ),

			),
			plugin_dir_path(__FILE__)
		);
	}

	function get_template_name($instance) {
		return 'divider-template';
	}

	function get_style_name($instance) {
		return 'divider-style';
	}

}

siteorigin_widget_register('divider', __FILE__, 'Divider');