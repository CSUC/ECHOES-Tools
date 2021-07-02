<?php

/*
Widget Name: Tabs
Description: This widget Display Tabs.
Author: Ingenious Solutions
Author URI: http://ingenious-web.com/
*/

class Tabs extends SiteOrigin_Widget {
	function __construct() {

		parent::__construct(
			'tabs',
			__('Tabs', 'addon-so-widgets-bundle'),
			array(
				'description' => __('Tabs Component.', 'addon-so-widgets-bundle'),
                'panels_icon' => 'dashicons dashicons-welcome-widgets-menus',
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


                'repeater' => array(
                    'type' => 'repeater',
                    'label' => __( 'Tabs' , 'addon-so-widgets-bundle' ),
                    'item_name'  => __( 'Tab', 'addon-so-widgets-bundle' ),
                    'item_label' => array(
                        'selector'     => "[id*='repeat_text']",
                        'update_event' => 'change',
                        'value_method' => 'val'
                    ),
                    'fields' => array(

                        'tab_icon' => array(
                            'type' => 'icon',
                            'label' => __('Select an Teb icon', 'addon-so-widgets-bundle'),
                        ),

                        'tab_title' => array(
                            'type' => 'text',
                            'label' => __('Tab Title', 'addon-so-widgets-bundle'),
                            'default' => ''
                        ),

                        'tab_content' => array(
                            'type' => 'tinymce',
                            'label' => __( 'Tab Content', 'addon-so-widgets-bundle' ),
                            'default' => '',
                            'rows' => 10,
                            'default_editor' => 'html',
                            'button_filters' => array(
                                'mce_buttons' => array( $this, 'filter_mce_buttons' ),
                                'mce_buttons_2' => array( $this, 'filter_mce_buttons_2' ),
                                'mce_buttons_3' => array( $this, 'filter_mce_buttons_3' ),
                                'mce_buttons_4' => array( $this, 'filter_mce_buttons_5' ),
                                'quicktags_settings' => array( $this, 'filter_quicktags_settings' ),
                            ),
                        )



                    )
                ),

                'tabs_selection' => array(
                    'type' => 'radio',
                    'label' => __( 'Choose Tabs Style', 'addon-so-widgets-bundle' ),
                    'default' => 'horizontal',
                    'options' => array(
                        'horizontal' => __( 'Horizontal Tabs', 'addon-so-widgets-bundle' ),
                        'vertical' => __( 'Vertical Tabs', 'addon-so-widgets-bundle' ),
                    )
                ),


                'tabs_styling' => array(
                    'type' => 'section',
                    'label' => __( 'Widget styling' , 'addon-so-widgets-bundle' ),
                    'hide' => true,
                    'fields' => array(

                        'tab_icon_position' => array(
                            'type' => 'select',
                            'label' => __( 'Choose a icon position', 'addon-so-widgets-bundle' ),
                            'default' => 'left',
                            'options' => array(
                                'left' => __( 'Left', 'addon-so-widgets-bundle' ),
                                'right' => __( 'Right', 'addon-so-widgets-bundle' ),
                                'top' => __( 'Top', 'addon-so-widgets-bundle' ),
                            )
                        ),

                        'inactive_tab_color' => array(
                            'type' => 'color',
                            'label' => __( 'Inactive Tab Font Color', 'addon-so-widgets-bundle' ),
                            'default' => ''
                        ),

                        'active_tab_color' => array(
                            'type' => 'color',
                            'label' => __( 'Active Tab Font Color', 'addon-so-widgets-bundle' ),
                            'default' => ''
                        ),

                        'active_tab_bg_color' => array(
                            'type' => 'color',
                            'label' => __( 'Active Tab Bg Color', 'addon-so-widgets-bundle' ),
                            'default' => ''
                        ),

                        'active_tab_top_border' => array(
                            'type' => 'checkbox',
                            'label' => __( 'Disable Top Border', 'addon-so-widgets-bundle' ),
                            'default' => true
                        ),

                        'active_tab_right_border' => array(
                            'type' => 'checkbox',
                            'label' => __( 'Disable Right Border', 'addon-so-widgets-bundle' ),
                            'default' => true
                        ),

                        'active_tab_bottom_border' => array(
                            'type' => 'checkbox',
                            'label' => __( 'Disable Bottm Border', 'addon-so-widgets-bundle' ),
                            'default' => true
                        ),

                        'active_tab_left_border' => array(
                            'type' => 'checkbox',
                            'label' => __( 'Disable Left Border', 'addon-so-widgets-bundle' ),
                            'default' => true
                        ),   

                        'tab_border_width' => array(
                            'type' => 'text',
                            'label' => __( 'Border Width (without px)', 'widget-form-fields-text-domain' ),
                            'default' => ''
                        ), 

                        'tab_border_color' => array(
                            'type' => 'color',
                            'label' => __( 'Tab Border Color', 'addon-so-widgets-bundle' ),
                            'default' => ''
                        ),                    

                        'tab_content_color' => array(
                            'type' => 'color',
                            'label' => __( 'Tab Content Color', 'addon-so-widgets-bundle' ),
                            'default' => ''
                        ),





                    )
                ),


			),
			plugin_dir_path(__FILE__)
		);
	}

	function get_template_name($instance) {
		return 'tabs-template';
	}

	function get_style_name($instance) {
		return 'tabs-style';
	}

    function get_less_variables( $instance ) {
        return array(
            'tab_border_top' => $instance['tabs_styling']['active_tab_top_border'],
            'tab_border_right' => $instance['tabs_styling']['active_tab_right_border'],
            'tab_border_bottom' => $instance['tabs_styling']['active_tab_bottom_border'],
            'tab_border_left' => $instance['tabs_styling']['active_tab_left_border'],
            'tab_border_width' => $instance['tabs_styling']['tab_border_width']. "px",
            'tab_border_color' => $instance['tabs_styling']['tab_border_color'],


            'inactive_tab_color' => $instance['tabs_styling']['inactive_tab_color'],
            'active_tab_bg_color' => $instance['tabs_styling']['active_tab_bg_color'],
            'active_tab_color' => $instance['tabs_styling']['active_tab_color'],
            'tab_content_color' => $instance['tabs_styling']['tab_content_color'],
        );
    }

}

siteorigin_widget_register('tabs', __FILE__, 'Tabs');