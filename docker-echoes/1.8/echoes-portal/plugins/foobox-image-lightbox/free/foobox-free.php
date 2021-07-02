<?php
/*
FooBox Free Image Lightbox
*/

define( 'FOOBOXFREE_SLUG', 'foobox-free' );
define( 'FOOBOXFREE_PATH', plugin_dir_path( __FILE__ ));
define( 'FOOBOXFREE_URL', plugin_dir_url( __FILE__ ));
define( 'FOOBOXFREE_FILE', __FILE__ );

if (!class_exists('Foobox_Free')) {

	// Includes
	require_once FOOBOXFREE_PATH . "includes/class-settings.php";
	require_once FOOBOXFREE_PATH . "includes/class-script-generator.php";
	require_once FOOBOXFREE_PATH . "includes/class-foogallery-foobox-free-extension.php";
	require_once FOOBOXFREE_PATH . "includes/foopluginbase/bootstrapper.php";
	require_once FOOBOXFREE_PATH . 'includes/class-exclude.php';

	class Foobox_Free extends Foo_Plugin_Base_v2_1 {

		const JS                   = 'foobox.free.min.js';
		const CSS                  = 'foobox.free.min.css';
		const CSS_NOIE7            = 'foobox.noie7.min.css';
		const FOOBOX_URL           = 'http://fooplugins.com/plugins/foobox/?utm_source=fooboxfreeplugin&utm_medium=fooboxfreeprolink&utm_campaign=foobox_free_pro_tab';
		const BECOME_AFFILIATE_URL = 'http://fooplugins.com/affiliate-program/';

		private static $instance;

		public static function get_instance() {
			if ( ! isset( self::$instance ) && ! ( self::$instance instanceof Foobox_Free ) ) {
				self::$instance = new Foobox_Free();
			}
			return self::$instance;
		}

		/**
		 * Initialize the plugin by setting localization, filters, and administration functions.
		 */
		private function __construct() {
			//init FooPluginBase
			$this->init( FOOBOXFREE_FILE, FOOBOXFREE_SLUG, FOOBOX_BASE_VERSION, 'FooBox FREE' );

			if (is_admin()) {
				//enqueue FooBox assets in the admin if necessary
				add_action('admin_enqueue_scripts', array($this, 'admin_enqueue'), 20);

				add_action('foobox-free-settings_custom_type_render', array($this, 'custom_admin_settings_render'));
				new FooBox_Free_Settings();

				add_action( FOOBOX_ACTION_ADMIN_MENU_RENDER_GETTING_STARTED, array( $this, 'render_page_getting_started' ) );
				add_action( FOOBOX_ACTION_ADMIN_MENU_RENDER_SETTINGS, array( $this, 'render_page_settings' ) );

				add_filter( 'foobox-free-has_settings_page', '__return_false' );

				add_action( 'enqueue_block_editor_assets', array( $this, 'enqueue_block_editor_assets' ) );

			} else {

				// Render JS to the front-end pages
				add_action('wp_enqueue_scripts', array($this, 'frontend_print_scripts'), 20);

				// Render CSS to the front-end pages
				add_action('wp_enqueue_scripts', array($this, 'frontend_print_styles'));

				if ( $this->is_option_checked('disable_others') ) {
					add_action('wp_footer', array($this, 'disable_other_lightboxes'), 200);
				}
			}

			new FooBox_Free_Exclude();
		}

		function enqueue_block_editor_assets() {
			$this->frontend_print_scripts();
			$this->frontend_print_styles();
		}

		function custom_admin_settings_render($args = array()) {
			$type = '';

			extract($args);

			if ($type == 'debug_output') {
				echo '</td></tr><tr valign="top"><td colspan="2">';
				$this->render_debug_info();
			} else if ($type == 'upgrade') {
				echo '</td></tr><tr valign="top"><td colspan="2">';
				$this->render_upgrade_notice();
			}
		}

		function generate_javascript($debug = false) {
			return FooBox_Free_Script_Generator::generate_javascript($this, $debug);
		}

		function render_for_archive() {
			if (is_admin()) return true;

			return !is_singular();
		}

		function render_debug_info() {

			echo '<strong>Javascript:<br /><pre style="width:600px; overflow:scroll;">';

			echo htmlentities($this->generate_javascript(true));

			echo '</pre><br />Settings:<br /><pre style="width:600px; overflow:scroll;">';

			echo htmlentities( print_r(get_option($this->plugin_slug), true) );

			echo '</pre>';
		}

		function render_upgrade_notice() {
			require_once FOOBOXFREE_PATH . "includes/upgrade.php";
		}

		function admin_enqueue() {
			$screen_id = foo_current_screen_id();

			if ( 'toplevel_page_' . FOOBOX_BASE_SLUG === $screen_id ||
				 'foobox_page_' . FOOBOX_BASE_PAGE_SLUG_SETTINGS === $screen_id ) {
				$this->frontend_print_scripts();
				$this->frontend_print_styles();
			}
		}

		function frontend_print_styles() {
			if ( !apply_filters('foobox_enqueue_styles', true) ) return;

			//enqueue foobox CSS
			if ( $this->is_option_checked( 'dropie7support' ) ) {
				$this->register_and_enqueue_css(self::CSS_NOIE7);
			} else {
				$this->register_and_enqueue_css(self::CSS);
			}
		}

		function frontend_print_scripts() {
			if (!apply_filters('foobox_enqueue_scripts', true)) return;

			$this->register_and_enqueue_js(
				$file = self::JS,
				$d = array('jquery'),
				$v = false,
				$f = false);

			$foobox_js = $this->generate_javascript();

			wp_add_inline_script(
				'foobox-free-min',
				$foobox_js,
				'before'
			);
		}

		/**
		 * PLEASE NOTE : This is only here to avoid the problem of hard-coded lightboxes.
		 * This is not meant to be malicious code to override all lightboxes in favour of FooBox.
		 * But sometimes theme authors hard code galleries to use their built-in lightbox of choice, which is not the desired solution for everyone.
		 * This can be turned off in the FooBox settings page
		 */
		function disable_other_lightboxes() {
			if ( !apply_filters('foobox_enqueue_scripts', true ) ) return;

			?>
			<script type="text/javascript">
				jQuery.fn.prettyPhoto   = function () { return this; };
				jQuery.fn.fancybox      = function () { return this; };
				jQuery.fn.fancyZoom     = function () { return this; };
				jQuery.fn.colorbox      = function () { return this; };
				jQuery.fn.magnificPopup = function () { return this; };
			</script>
		<?php
		}

		function render_page_getting_started() {
			require_once FOOBOXFREE_PATH . 'includes/view-getting-started.php';
		}

		function render_page_settings() {
			if ( isset( $_GET['settings-updated'] ) ) {
				if ( false === get_option( FOOBOXFREE_SLUG ) ) { ?>
					<div id="message" class="updated">
						<p>
							<strong><?php _e( 'FooBox settings restored to defaults.', 'foobox-image-lightbox' ); ?></strong>
						</p>
					</div>
				<?php } else { ?>
					<div id="message" class="updated">
						<p><strong><?php _e( 'FooBox settings updated.', 'foobox-image-lightbox' ); ?></strong></p>
					</div>
				<?php }
			}

			$instance = Foobox_Free::get_instance();
			$instance->admin_settings_render_page();
		}

		function is_option_checked($key) {
			$options = $this->options()->get_all();

			if ($options) {
				return array_key_exists($key, $options);
			}

			return false;
		}
	}
}

Foobox_Free::get_instance();
