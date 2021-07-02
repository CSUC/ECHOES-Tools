<?php

if ( !class_exists( 'FooBox_Free_Settings' ) ) {

	class FooBox_Free_Settings {

		function __construct() {
			add_filter('foobox-free-admin_settings', array($this, 'create_settings'));
		}

		function create_settings() {
			//region General Tab
			$tabs['general'] = __('General', 'foobox-image-lightbox');

			$sections['attach'] = array(
				'tab' => 'general',
				'name' => __('What do you want to attach FooBox to?', 'foobox-image-lightbox')
			);

			$settings[] = array(
				'id'      => 'enable_galleries',
				'title'   => __( 'WordPress Galleries', 'foobox-image-lightbox' ),
				'desc'    => __( 'Enable FooBox for all WordPress image galleries.', 'foobox-image-lightbox' ),
				'default' => 'on',
				'type'    => 'checkbox',
				'section' => 'attach',
				'tab'     => 'general'
			);

			$settings[] = array(
				'id'      => 'enable_captions',
				'title'   => __( 'WordPress Images With Captions', 'foobox-image-lightbox' ),
				'desc'    => __( 'Enable FooBox for all WordPress images that have captions.', 'foobox-image-lightbox' ),
				'default' => 'on',
				'type'    => 'checkbox',
				'section' => 'attach',
				'tab'     => 'general'
			);

			$settings[] = array(
				'id'      => 'enable_attachments',
				'title'   => __( 'Attachment Images', 'foobox-image-lightbox' ),
				'desc'    => __( 'Enable FooBox for all media images included in posts or pages.', 'foobox-image-lightbox' ),
				'default' => 'on',
				'type'    => 'checkbox',
				'section' => 'attach',
				'tab'     => 'general'
			);

			$sections['settings'] = array(
				'tab' => 'settings',
				'name' => __('Display Settings', 'foobox-image-lightbox')
			);

			$settings[] = array(
				'id'      => 'fit_to_screen',
				'title'   => __( 'Fit To Screen', 'foobox-image-lightbox' ),
				'desc'    => __( 'Force smaller images to fit the screen dimensions.', 'foobox-image-lightbox' ),
				'default' => 'off',
				'type'    => 'checkbox',
				'section' => 'settings',
				'tab'     => 'general'
			);

			$settings[] = array(
				'id'      => 'hide_scrollbars',
				'title'   => __( 'Hide Page Scrollbars', 'foobox-image-lightbox' ),
				'desc'    => __( 'Hide the page\'s scrollbars when FooBox is visible.', 'foobox-image-lightbox' ),
				'default' => 'on',
				'type'    => 'checkbox',
				'section' => 'settings',
				'tab'     => 'general'
			);

			$settings[] = array(
				'id'      => 'show_count',
				'title'   => __( 'Show Counter', 'foobox-image-lightbox' ),
				'desc'    => __( 'Shows a counter under the FooBox modal when viewing a gallery of images.', 'foobox-image-lightbox' ),
				'default' => 'on',
				'type'    => 'checkbox',
				'section' => 'settings',
				'tab'     => 'general'
			);

			$settings[] = array(
				'id'      => 'count_message',
				'title'   => __( 'Count Message', 'foobox' ),
				'desc'    => __( 'the message to use as the item counter. The fields <code>%index</code> and <code>%total</code> can be used to substitute the correct values. <br/ >Example : <code>item %index / %total</code> would result in <code>item 1 / 7</code>', 'foobox-image-lightbox' ),
				'default' => 'item %index of %total',
				'type'    => 'text',
				'section' => 'settings',
				'tab'     => 'general'
			);

			$settings[] = array(
				'id'      => 'hide_caption',
				'title'   => __( 'Hide Captions', 'foobox' ),
				'desc'    => __( 'Whether or not to hide captions for images.', 'foobox' ),
				'type'    => 'checkbox',
				'section' => 'settings',
				'tab'     => 'general'
			);

			$settings[] = array(
				'id'      => 'captions_show_on_hover',
				'title'   => __( 'Show Captions On Hover', 'foobox-image-lightbox' ),
				'desc'    => __( 'Only show the caption when hovering over the image.', 'foobox-image-lightbox' ),
				'type'    => 'checkbox',
				'section' => 'settings',
				'tab'     => 'general'
			);

			$settings[] = array(
				'id'      => 'error_message',
				'title'   => __( 'Error Message', 'foobox-image-lightbox' ),
				'desc'    => __( 'The error message to display when an image has trouble loading.', 'foobox-image-lightbox' ),
				'default' => __( 'Could not load the item', 'foobox-image-lightbox' ),
				'type'    => 'text',
				'section' => 'settings',
				'tab'     => 'general'
			);

			//endregion

			//region Advanced Tab

			$tabs['advanced'] = __('Advanced', 'foobox-image-lightbox');

			$settings[] = array(
				'id'      => 'close_overlay_click',
				'title'   => __( 'Close On Overlay Click', 'foobox-image-lightbox' ),
				'desc'    => __( 'Should the FooBox lightbox close when the overlay is clicked.', 'foobox-image-lightbox' ),
				'default' => 'on',
				'type'    => 'checkbox',
				'tab'     => 'advanced'
			);

			$settings[] = array(
				'id'      => 'dropie7support',
				'title'   => __( 'Drop IE7 Support', 'foobox' ),
				'desc'    => __( 'Drop support for IE7, which removes some CSS hacks to get things working in IE7. This also allows the FooBox CSS to pass CSS validation.', 'foobox-image-lightbox' ),
				'type'    => 'checkbox',
				'tab'     => 'advanced'
			);

			$settings[] = array(
				'id'      => 'disable_others',
				'title'   => __( 'Disable Other Lightboxes', 'foobox-image-lightbox' ),
				'desc'    => __( 'Certain themes and plugins use a hard-coded lightbox, which make it very difficult to override.<br>By enabling this setting, we inject a small amount of javascript onto the page which attempts to get around this issue.<br>But please note this is not guaranteed, as we cannot account for every lightbox solution out there :)', 'foobox-image-lightbox' ),
				'type'    => 'checkbox',
				'tab'     => 'advanced'
			);

			$settings[] = array(
				'id'      => 'enable_debug',
				'title'   => __( 'Enable Debug Mode', 'foobox-image-lightbox' ),
				'desc'    => __( 'Show an extra debug information tab to help debug any issues.', 'foobox-image-lightbox' ),
				'type'    => 'checkbox',
				'tab'     => 'advanced'
			);

			$settings[] = array(
				'id'      => 'excludebydefault',
				'title'   => __( 'Exclude FooBox Assets', 'foobox-image-lightbox' ),
				'desc'    => __( 'By default, FooBox includes javascript and stylesheet assets into all your pages. We do this, because we do not know if the page content contains media or not.<br>If you want more control over when FooBox assets are included, you can now exclude FooBox assets by default, by enabling this setting. Then on each page, you can choose to include the assets if required.<br>Or you can leave the setting disabled, and then choose to exclude FooBox assets from particular pages. A new FooBox metabox is now available when editing your pages or posts.', 'foobox-image-lightbox' ),
				'type'    => 'checkbox',
				'section' => __( 'JS &amp; CSS', 'foobox-image-lightbox' ),
				'tab'     => 'advanced'
			);

			//endregion

			//region Debug Tab
			$foobox_free = Foobox_Free::get_instance();

			if ( $foobox_free->options()->is_checked( 'enable_debug', false ) ) {

				$tabs['debug'] = __('Debug', 'foobox-image-lightbox');

				$settings[] = array(
					'id'      => 'debug_output',
					'title'   => __( 'Debug Information', 'foobox-image-lightbox' ),
					'type'    => 'debug_output',
					'tab'     => 'debug'
				);
			}
			//endregion

			//region Upgrade tab
			$tabs['upgrade'] = __('Upgrade to PRO!', 'foobox-image-lightbox');

			$link_text = __('Upgrade in WP Admin!', 'foobox-image-lightbox');

			if ( foobox_hide_pricing_menu() ) {
				$link_text = '';
			}

			$link = sprintf( '<p><a href="%s">%s</a></p><br />',  esc_url ( foobox_pricing_url() ), $link_text );

			$settings[] = array(
				'id'    => 'upgrade',
				'title' => $link . __('There are tons of reasons...', 'foobox-image-lightbox'),
				'type'  => 'upgrade',
				'tab'   => 'upgrade'
			);
			//endregion

			return array(
				'tabs' => $tabs,
				'sections' => $sections,
				'settings' => $settings
			);
		}
	}
}