<?php

/*
Widget Name: Toggles
Description: This widget Display Toggle.
Author: Ingenious Solutions
Author URI: http://ingenious-web.com/
*/

class Toggles extends SiteOrigin_Widget {
	function __construct() {

		parent::__construct(
			'toggles',
			__('Toggle', 'addon-so-widgets-bundle'),
			array(
				'description' => __('Toggle Component.', 'addon-so-widgets-bundle'),
                'panels_icon' => 'dashicons dashicons-list-view',
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


                'toggle_repeater' => array(
                    'type' => 'repeater',
                    'label' => __( 'Toggles' , 'addon-so-widgets-bundle' ),
                    'item_name'  => __( 'Toggle', 'addon-so-widgets-bundle' ),
                    'item_label' => array(
                        'selector'     => "[id*='repeat_text']",
                        'update_event' => 'change',
                        'value_method' => 'val'
                    ),
                    'fields' => array(

                        'toggle_icon' => array(
                            'type' => 'icon',
                            'label' => __('Select an icon', 'widget-form-fields-text-domain'),
                        ),


                        'icon_position' => array(
                            'type' => 'select',
                            'prompt' => __( 'Choose an Icon position', 'widget-form-fields-text-domain' ),
                            'options' => array(
                            'left' => __( 'Left Side', 'widget-form-fields-text-domain' ),
                            'right' => __( 'Right Side', 'widget-form-fields-text-domain' ),        
                            )
                        ),

                        'toggle_title' => array(
                            'type' => 'text',
                            'label' => __('Toggle Title', 'addon-so-widgets-bundle'),
                            'default' => ''
                        ),


                        'toggle_content' => array(
                            'type' => 'tinymce',
                            'label' => __( 'Toggle Content', 'addon-so-widgets-bundle' ),
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
                        ),


                    )
                ),

                'toggle_styling' => array(
                    'type' => 'section',
                    'label' => __( 'Widget styling' , 'addon-so-widgets-bundle' ),
                    'hide' => true,
                    'fields' => array(

                        'toggle_padding' => array(
                            'type' => 'text',
                            'label' => __('Title Padding (shorthand Format)', 'widget-form-fields-text-domain'),
                            'default' => '0px 0px 0px 0px'
                        ),

                        'bg' => array(
                            'type' => 'color',
                            'label' => __( 'backgorund color', 'addon-so-widgets-bundle' ),
                            'default' => ''
                        ),

                        'bg_hover' => array(
                            'type' => 'color',
                            'label' => __( 'backgorund Hover color', 'addon-so-widgets-bundle' ),
                            'default' => ''
                        ),

                        'icon_color' => array(
                            'type' => 'color',
                            'label' => __( 'icon color', 'addon-so-widgets-bundle' ),
                            'default' => ''
                        ),

                        'title_color' => array(
                            'type' => 'color',
                            'label' => __( 'Title color', 'addon-so-widgets-bundle' ),
                            'default' => ''
                        ),

                        'title_bar_alignment' => array(
                                'type' => 'select',
                                'label' => __( 'Title Bar Alignment', 'addon-so-widgets-bundle' ),
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
                            'label' => __( 'Content color', 'addon-so-widgets-bundle' ),
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

	function get_template_name($instance) {
		return 'toggles-template';
	}

	function get_style_name($instance) {
		return 'toggles-style';
	}

    function get_less_variables( $instance ) {
        return array(
            'title_bar_alignment' => $instance['toggle_styling']['title_bar_alignment'],
            'bg' => $instance['toggle_styling']['bg'],
            'bg_hover' => $instance['toggle_styling']['bg_hover'],
            'icon_color' => $instance['toggle_styling']['icon_color'],
            'title_color' => $instance['toggle_styling']['title_color'],
            'content_bg_color' => $instance['toggle_styling']['content_bg_color'],
            'content_color' => $instance['toggle_styling']['content_color'],
            'toggle_border' => $instance['toggle_styling']['toggle_border'],
            'toggle_border_width' => $instance['toggle_styling']['toggle_border_width'].'px',
            'toggle_border_color' => $instance['toggle_styling']['toggle_border_color'],
            'toggle_padding' => $instance['toggle_styling']['toggle_padding'],
        );
    }

}


siteorigin_widget_register('toggles', __FILE__, 'Toggles');