<?php

/*
Widget Name: Blog List
Description: Display blog in a list style.
Author: Ingenious Solutions
Author URI: http://ingenious-web.com/
*/

class Blog_Style_One extends SiteOrigin_Widget {
	function __construct() {

		parent::__construct(
			'blog-style-one',
			__('Blog List', 'addon-so-widgets-bundle'),
			array(
				'description' => __('Display blog in a list style.', 'addon-so-widgets-bundle'),
                'panels_icon' => 'dashicons dashicons-welcome-write-blog',
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
                    'label' => __('Select Posts', 'addon-so-widgets-bundle'),
                ),

                'alignment' => array(
                'type' => 'radio',
                'label' => __( 'Image Alignment', 'addon-so-widgets-bundle' ),
                'default' => 'left',
                'options' => array(
                    'left' => __( 'Left Aligne', 'addon-so-widgets-bundle' ),
                    'right' => __( 'Right Aligne', 'addon-so-widgets-bundle' ),
                )
                ),

                'layout' => array(
                    'type' => 'radio',
                    'label' => __( 'Select Layout', 'addon-so-widgets-bundle' ),
                    'default' => 'twelve columns',
                    'options' => array(
                        'six columns' => __( '2 Column Layout', 'addon-so-widgets-bundle' ),
                        'twelve columns' => __( 'Full Width Layout', 'addon-so-widgets-bundle' ),
                    )
                ),


                 'excerpt_lenght' => array(
                 'type' => 'number',
                 'label' => __( 'Excerpt length (words)', 'addon-so-widgets-bundle' ),
                 'default' => '10'
                 ),

                'read_more_text' => array(
                'type' => 'text',
                'label' => __('Read more text', 'addon-so-widgets-bundle'),
                'default' => 'Read More'
                ),

                  'title_linkable' => array(
                  'type' => 'checkbox',
                  'label' => __( 'Title Linkable', 'addon-so-widgets-bundle' ),
                  'default' => true
                 ),

                  'image_linkable' => array(
                  'type' => 'checkbox',
                  'label' => __( 'Image Linkable', 'addon-so-widgets-bundle' ),
                  'default' => true
                 ),

                'blog_one_styling' => array(
                    'type' => 'section',
                    'label' => __( 'Widget styling' , 'addon-so-widgets-bundle' ),
                    'hide' => true,
                    'fields' => array(

                        'title_color' => array(
                            'type' => 'color',
                            'label' => __( 'Title color', 'addon-so-widgets-bundle' ),
                            'default' => ''
                        ),

                        'content_color' => array(
                            'type' => 'color',
                            'label' => __( 'Content color', 'addon-so-widgets-bundle' ),
                            'default' => ''
                        ),

                        'meta_color' => array(
                            'type' => 'color',
                            'label' => __( 'Meta color', 'addon-so-widgets-bundle' ),
                            'default' => ''
                        ),


                    )
                ),



            ),


			plugin_dir_path(__FILE__)
		);
	}

	function get_template_name($instance) {
		return 'blog-style-one-template';
	}

	function get_style_name($instance) {
		return 'blog-style-one-style';
	}

    function get_less_variables( $instance ) {
        return array(
            'title_color' => $instance['blog_one_styling']['title_color'],
            'content_color' => $instance['blog_one_styling']['content_color'],
            'meta_color' => $instance['blog_one_styling']['meta_color'],
        );
    }




}

siteorigin_widget_register('blog-style-one', __FILE__, 'Blog_Style_One');


  