<?php
/**
 * Foobox_Free_Exclude class
 * class which allows you to exclude FooBox from specific pages and posts
 */

if ( !class_exists( 'FooBox_Free_Exclude' ) ) {

	class FooBox_Free_Exclude {

		function __construct() {
			add_action( 'init', array($this, 'init') );
		}

		function init() {
			if ( is_admin() ) {
				add_action( 'add_meta_boxes', array($this, 'add_metaboxes') );
				add_action( 'save_post', array($this, 'save_meta') );
			} else {
				add_filter( 'foobox_enqueue_scripts', array($this, 'enqueue_scripts_or_styles'), 99 );
				add_filter( 'foobox_enqueue_styles', array($this, 'enqueue_scripts_or_styles'), 99 );
			}
		}

		function add_metaboxes() {
			$public_post_types = get_post_types( array( 'public' => true ), 'names' );

			$post_types = apply_filters( 'foobox_metabox_post_types', $public_post_types );

			foreach ( $post_types as $post_type ) {

				$metabox_title = apply_filters( 'foobox_metabox_title', __( 'FooBox Javascript &amp; CSS', 'foobox' ) );

				add_meta_box(
					$post_type . '_foobox_exclude'
					, $metabox_title
					, array($this, 'render_meta_box')
					, $post_type
					, 'side'
					, 'default'
				);

			}
		}

		function exclude_by_default_option_checked() {
			$instance = Foobox_Free::get_instance();
			return $instance->is_option_checked( 'excludebydefault' );
		}

		function render_meta_box($post) {
			$show_include = $this->exclude_by_default_option_checked();

			$exclude = get_post_meta( $post->ID, '_foobox_exclude', true );
			$include = get_post_meta( $post->ID, '_foobox_include', true );
			$metabox_desc_exclude = apply_filters( 'foobox_metabox_desc_exclude', __( 'Exclude FooBox from this page or post? By default, FooBox will be included.', 'foobox' ) );
			$metabox_desc_include = apply_filters( 'foobox_metabox_desc_include', __( 'Include FooBox on this page or post? By default, FooBox will be excluded!', 'foobox' ) );
			?>
			<input type="hidden" name="foobox_exclude_nonce"
				   value="<?php echo wp_create_nonce( plugin_basename( __FILE__ ) ); ?>"/>
			<table class="form-table">
			<?php if ( !$show_include ) { ?>
				<tr>
					<td colspan="2">
						<input id="foobox_exclude_check"
							   name="foobox_exclude_check" <?php echo ($exclude == "exclude") ? 'checked="checked"' : ""; ?>
							   type="checkbox" value="exclude">
						<label for="foobox_exclude_check"><?php echo $metabox_desc_exclude; ?></label>
					</td>
				</tr>
			<?php } else { ?>
				<tr>
					<td colspan="2">
						<input id="foobox_include_check"
						       name="foobox_include_check" <?php echo ($include == "include") ? 'checked="checked"' : ""; ?>
						       type="checkbox" value="include">
						<label for="foobox_include_check"><?php echo $metabox_desc_include; ?></label>
					</td>
				</tr>
			<?php } ?>
			</table>
		<?php
		}

		function save_meta($post_id) {

			// check autosave
			if ( defined( 'DOING_AUTOSAVE' ) && DOING_AUTOSAVE ) {
				return $post_id;
			}

			// verify nonce
			if ( array_key_exists( 'foobox_exclude_nonce', $_POST ) &&
				wp_verify_nonce( $_POST['foobox_exclude_nonce'], plugin_basename( __FILE__ ) )
			) {

				if ( !$this->exclude_by_default_option_checked() ) {

					$exclude = array_key_exists( 'foobox_exclude_check', $_POST ) ? $_POST['foobox_exclude_check'] : '';
					if ( empty( $exclude ) ) {
						delete_post_meta( $post_id, '_foobox_exclude' );
					} else {
						update_post_meta( $post_id, '_foobox_exclude', $exclude );
					}

				} else {

					$include = array_key_exists( 'foobox_include_check', $_POST ) ? $_POST['foobox_include_check'] : '';
					if ( empty($include) ) {
						delete_post_meta( $post_id, '_foobox_include' );
					} else {
						update_post_meta( $post_id, '_foobox_include', $include );
					}
				}
			}
		}

		function enqueue_scripts_or_styles() {
			global $post;
			if (isset($post) && is_singular($post)) {
				if ( !$this->exclude_by_default_option_checked() ) {

					//check if we are excluding
					$exclude = get_post_meta( $post->ID, '_foobox_exclude', true );
					if ( 'exclude' === $exclude ) {
						return false;
					}
				} else {

					$include = get_post_meta( $post->ID, '_foobox_include', true );
					if ( 'include' === $include ) {
						return true;
					}

					//otherwise, by default, do not include the assets
					return false;
				}
			}
			return true;
		}
	}
}