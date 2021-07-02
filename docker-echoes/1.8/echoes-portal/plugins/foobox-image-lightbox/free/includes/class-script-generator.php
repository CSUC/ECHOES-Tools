<?php

if ( !class_exists( 'FooBox_Free_Script_Generator' ) ) {
	class FooBox_Free_Script_Generator {

		static function get_option($options, $key, $default = false) {
			if ( $options ) {
				return (array_key_exists( $key, $options )) ? $options[$key] : $default;
			}

			return $default;
		}

		static function is_option_checked($options, $key, $default = false) {
			if ( $options ) {
				return array_key_exists( $key, $options );
			}

			return $default;
		}

		static function generate_js_options($fbx_options) {
			$options = array();

			$debug               = self::is_option_checked( $fbx_options, 'enable_debug' );
			$forceDelay          = self::get_option( $fbx_options, 'force_delay', '0' );
			$fitToScreen         = self::is_option_checked( $fbx_options, 'fit_to_screen' );
			$hideScrollbars      = self::is_option_checked( $fbx_options, 'hide_scrollbars', true );
			$close_overlay_click = self::is_option_checked( $fbx_options, 'close_overlay_click', true );
			$show_count          = self::is_option_checked( $fbx_options, 'show_count', true );
			$count_message       = self::get_option( $fbx_options, 'count_message', 'image %index of %total' );
			$powered_by_link     = self::is_option_checked( $fbx_options, 'powered_by_link', false );
			$hide_caption        = self::is_option_checked( $fbx_options, 'hide_caption', false );
			$captions_hover      = self::is_option_checked( $fbx_options, 'captions_show_on_hover', false );

			//force to show the powered by link on the settings page, so the user can see what it will look like
			if ( foo_check_plugin_settings_page( FOOBOXFREE_SLUG ) ) {
				$powered_by_link = true;
			}

			$powered_by_url      = self::get_option( $fbx_options, 'powered_by_url', Foobox_Free::FOOBOX_URL );
			$error_msg           = self::get_option( $fbx_options, 'error_message', '' );

			if (empty($powered_by_url)) {
				$powered_by_url = Foobox_Free::FOOBOX_URL;
			}

			$options['wordpress'] = 'wordpress: { enabled: true }';

			if ( intval( $forceDelay ) > 0 ) {
				$options['loadDelay'] = 'loadDelay:' . $forceDelay;
			}

			if ( $fitToScreen ) {
				$options['fitToScreen'] = 'fitToScreen:true';
			}
			if ( !$hideScrollbars ) {
				$options['hideScrollbars'] = 'hideScrollbars:false';
			}
			if ( !$close_overlay_click ) {
				$options['closeOnOverlayClick'] = 'closeOnOverlayClick:false';
			}
			if ( !$show_count ) {
				$options['showCount'] = 'showCount:false';
			}
			if ( $count_message != 'item %index of %total' ) {
				$options['countMessage'] = 'countMessage:\'' . addslashes( $count_message ) . '\'';
			}
			if ( $hide_caption ) {
				$options['images'] = 'images: { showCaptions:false }';
			} else {
				if ( $captions_hover ) {
					$options['captions'] = 'captions: { onlyShowOnHover: true }';
				}
			}

			$js_excludes[] = '.fbx-link';
			$js_excludes[] = '.nofoobox';
			$js_excludes[] = '.nolightbox';
			$js_excludes[] = 'a[href*="pinterest.com/pin/create/button/"]';

			$options['excludes'] = 'excludes:\'' . implode( ',', $js_excludes ) . '\'';

			//Affiliates
			if ( $powered_by_link ) {
				$affiliate = 'affiliate : { enabled: true, url: \'' . $powered_by_url . '\' }';
			} else {
				$affiliate = 'affiliate : { enabled: false }';
			}

			$options['affiliate'] = $affiliate;

			if ( $error_msg != '' ) {
				$options['error'] = 'error: "' . addslashes( $error_msg ) . '"';
			}

			if ( sizeof( $options ) > 0 ) {
				$seperator = $debug ? ',
		' : ', ';

				return '{' . implode( $seperator, $options ) . '}';
			}

			return false;
		}

		static function generate_javascript_call($selector, $options, $bindings = false) {
			$js = "    $({$selector})";
			if ( $bindings !== false ) {
				$js .= $bindings;
			}
			$js .= ".foobox({$options});";

			return $js;
		}

		/**
		 * @param $foobox Foobox_Free
		 * @param $debug  boolean
		 *
		 * @return string
		 */
		static function generate_javascript($foobox, $debug = false) {
			$fbx_options = $foobox->options()->get_all();

			$info = $foobox->get_plugin_info();

			$seperator = $debug ? '",
		"' : '", "';

			$foobox_selectors = array();

			if ( foo_check_plugin_settings_page( FOOBOXFREE_SLUG ) ) {
				$foobox_selectors[] = '.demo-gallery,.bad-image';
			}

			//add support for foogallery!
			if ( class_exists('FooGallery_Plugin') ) {
				$foobox_selectors[] = '.foogallery-container.foogallery-lightbox-foobox';
				$foobox_selectors[] = '.foogallery-container.foogallery-lightbox-foobox-free';
			}

			if ( self::is_option_checked( $fbx_options, 'enable_galleries', true ) ) {
				$foobox_selectors[] = '.gallery';
				$foobox_selectors[] = '.wp-block-gallery';
			}

			if ( self::is_option_checked( $fbx_options, 'enable_captions', true ) ) {
				$foobox_selectors[] = '.wp-caption';
			}

			if ( self::is_option_checked( $fbx_options, 'enable_attachments', true ) ) {
				$foobox_selectors[] = '.wp-block-image';
				$foobox_selectors[] = 'a:has(img[class*=wp-image-])';
				if ( $foobox->render_for_archive() ) {
					$foobox_selectors[] = '.post a:has(img[class*=wp-image-])'; //archive selector
				}
			}

			$foobox_selectors[] = '.foobox';

			$js_options = self::generate_js_options( $fbx_options );
			$disable_others = self::is_option_checked( $fbx_options, 'disable_others', false );
			$foobox_ready_event = apply_filters( 'foobox_generate_javascript_ready_event', true );
			$preload_foobox_font = apply_filters( 'foobox_generate_javascript_preload_font', true );
			if ( $debug ) {
				$pre_js      = apply_filters( 'foobox_generate_javascript_pre', 'console.log("FooBox - Custom JavaScript (Pre)");' );
				$post_js     = apply_filters( 'foobox_generate_javascript_post', 'console.log("FooBox - Custom JavaScript (Post)");' );
				$captions_js = apply_filters( 'foobox_generate_javascript_captions', 'console.log("FooBox - Custom Captions Code");' );
				$custom_js   = apply_filters( 'foobox_generate_javascript_custom', 'console.log("FooBox - Custom Extra JS");' );
			} else {
				$pre_js      = apply_filters( 'foobox_generate_javascript_pre', '' );
				$post_js     = apply_filters( 'foobox_generate_javascript_post', '' );
				$captions_js = apply_filters( 'foobox_generate_javascript_captions', '' );
				$custom_js   = apply_filters( 'foobox_generate_javascript_custom', '' );
			}
			$js = sprintf( '/* Run FooBox FREE (v%s) */
var FOOBOX = window.FOOBOX = {
	ready: %s,
	preloadFont: %s,
	disableOthers: %s,
	o: %s,
	selectors: [
		%s
	],
	pre: function( $ ){
		// Custom JavaScript (Pre)
		%s
	},
	post: function( $ ){
		// Custom JavaScript (Post)
		%s
		// Custom Captions Code
		%s
	},
	custom: function( $ ){
		// Custom Extra JS
		%s
	}
};',
				$info['version'],
				$foobox_ready_event ? 'true' : 'false',
				$preload_foobox_font ? 'true' : 'false',
				$disable_others ? 'true' : 'false',
				$js_options,
				'"' . implode($seperator, $foobox_selectors) . '"',
				$pre_js,
				$post_js,
				$captions_js,
				$custom_js
				);

			return $js;
		}
	}
}