<?php
$foogallery_url = 'http://foo.gallery?utm_source=foobox_free_plugin&utm_medium=foobox_free_link&utm_campaign=foobox_free_admin_getting_started';

$fs_instance = freemius( FOOBOX_BASE_SLUG );
$show_trial_message = !$fs_instance->is_trial_utilized();
$tab_text = $show_trial_message ? __( 'Free Trial', 'foobox-image-lightbox' ) : __( 'Upgrade to PRO', 'foobox-image-lightbox' );
$button_text = $show_trial_message ? __( 'Already convinced? Upgrade to PRO!', 'foobox-image-lightbox' ) : __( 'Upgrade to PRO!', 'foobox-image-lightbox' );

?>
<style>
	.feature-section .dashicons {
		font-size: 1.8em;
		color: green;
		padding-right: 10px;
	}
</style>
	<h2 class="nav-tab-wrapper">
		<a class="nav-tab nav-tab-active" href="#getting-started">
			<?php _e( 'Getting Started', 'foobox-image-lightbox' ); ?>
		</a>
		<a class="nav-tab" href="#pro-features">
			<?php echo $tab_text; ?>
		</a>
		<a class="nav-tab" href="#demo">
			<?php _e( 'Demo', 'foobox-image-lightbox' ); ?>
		</a>
	</h2>
	<div id="getting-started_tab" class="feature-section nav-container">
		<div>
			<h2><?php _e( 'Zero Configuration', 'foobox-image-lightbox' );?></h2>
			<p><?php _e( 'FooBox FREE works out-of-the-box with the following standard features:', 'foobox-image-lightbox' );?></p>
			<p> → <strong><?php _e( 'Gutenberg Editor', 'foobox-image-lightbox' ); ?></strong> - <?php _e('images within gallery and image blocks will open in FooBox (set Linked To -&gt; "Media File").', 'foobox-image-lightbox' ); ?></p>
			<p> → <strong><a href="https://codex.wordpress.org/Caption_Shortcode" target="_blank"><?php _e( 'Captioned Images', 'foobox-image-lightbox' ); ?></a></strong> - <?php _e('images that use the <code>[caption]</code> shortcode.', 'foobox-image-lightbox' ); ?></p>
			<p> → <strong><a href="https://codex.wordpress.org/The_WordPress_Gallery" target="_blank"><?php _e( 'WordPress Galleries', 'foobox-image-lightbox' ); ?></a></strong> - <?php _e('image galleries that use the <code>[gallery]</code> shortcode.', 'foobox-image-lightbox' ); ?></p>
			<p> → <strong><a href="https://codex.wordpress.org/Inserting_Media_into_Posts_and_Pages" target="_blank"><?php _e( 'Attachment Images', 'foobox-image-lightbox' ); ?></a></strong> - <?php _e('images that are added using the "Add Media" tool.', 'foobox-image-lightbox' ); ?></p>
			<p>
				<?php _e( 'No configuration for the above is needed! But you can still customize the plugin from the settings page if you want to.', 'foobox-image-lightbox' ); ?>
			</p>
			<p style="text-align: center">
				<strong><?php printf( '<a href="%s">%s</a>', esc_url ( foobox_settings_url() ), __( 'visit the FooBox settings page', 'foobox-image-lightbox' ) ); ?></strong>
			</p>
		</div>
		<div>
			<h2><?php _e( 'class="foobox"', 'foobox-image-lightbox' );?></h2>
			<p>
				<?php _e( 'Use your own links to open images in FooBox. Just add a class of "<strong>foobox</strong>" to your links, and make sure the link points to an image, for example:', 'foobox-image-lightbox' ); ?>
			</p>
			<p style="text-align: center;">
				<code>&lt;a href=&quot;point/to/image.jpg&quot; class=&quot;foobox&quot;&gt;open with FooBox!&lt;/a&gt;</code>
			</p>
			<p style="text-align: center"><strong><a href="<?php echo foobox_asset_url( 'img/foobot_red.png' ); ?>" class="foobox"><?php _e( 'open an image', 'foobox-image-lightbox' ); ?></a></strong></p>
		</div>
		<div>
			<h2><?php _e( 'rel="gallery"', 'foobox-image-lightbox' );?></h2>
			<p>
				<?php _e( 'Group your images into galleries that open in FooBox. Just add the same rel attribute to the links you want to group together, for example:', 'foobox-image-lightbox' ); ?>
			</p>
			<p style="text-align: center;">
				<code>
					&lt;a href=&quot;image1.jpg&quot; class=&quot;foobox&quot; rel=&quot;gallery&quot;&gt;1&lt;/a&gt;<br />
					&lt;a href=&quot;image2.jpg&quot; class=&quot;foobox&quot; rel=&quot;gallery&quot;&gt;2&lt;/a&gt;<br />
					&lt;a href=&quot;image3.jpg&quot; class=&quot;foobox&quot; rel=&quot;gallery&quot;&gt;3&lt;/a&gt;
				</code>
			</p>
			<p style="text-align: center"><strong><a rel="foobox" href="<?php echo foobox_asset_url( 'img/foobot_red.png' ); ?>" class="foobox"><?php _e( 'open a gallery', 'foobox-image-lightbox' ); ?></a></strong></p>
		</div>
		<div>
			<h2><?php _e( 'Captions', 'foobox-image-lightbox' );?></h2>
			<p>
				<?php _e( 'FooBox will try to find captions from the link and image titles, but you can be sure by adding <strong>data-caption-title</strong> and <strong>data-caption-desc</strong> attributes onto your links, for example:', 'foobox-image-lightbox' );?>
			</p>
			<p style="text-align: center;">
				<code>
					&lt;a data-caption-title=&quot;A Caption Title&quot;<br />
					data-caption-desc=&quot;A longer caption description&quot;<br />
					href=&quot;image1.jpg&quot; class=&quot;foobox&quot;&gt;1&lt;/a&gt;
				</code>
			</p>
			<p style="text-align: center"><strong><a data-caption-title="A Caption Title" data-caption-desc="A longer caption description" href="<?php echo foobox_asset_url( 'img/foobot_red.png' ); ?>" class="foobox"><?php _e( 'open a captioned image', 'foobox-image-lightbox' ); ?></a></strong></p>
		</div>
	</div>
	<div id="pro-features_tab" class="feature-section nav-container" style="display: none">
		<?php if ( $show_trial_message ) { ?>
		<div>
			<h2><?php _e( 'FooBox PRO Free Trial', 'foobox-image-lightbox' );?></h2>
			<p><?php _e( 'Want to test out all the PRO features? No problem! You can start a 7-day free trial immediately. No credit card is required!', 'foobox-image-lightbox' );?></p>
			<h4><?php printf( '<a href="%s">%s</a>', esc_url ( foobox_freetrial_url() ), __( 'Start Your 7-day Free Trial', 'foobox-image-lightbox' ) ); ?></h4>
		</div>
		<?php } ?>
		<div id="foobox-free-upgrade">
			<h2><?php _e( 'PRO Features', 'foobox-image-lightbox' );?></h2>
			<p><span class="dashicons dashicons-yes"></span><strong><?php _e( 'ALL content types', 'foobox-image-lightbox' ); ?></strong> - <?php _e( 'Images, galleries, videos, iframes, html. You name it, FooBox can open it.', 'foobox-image-lightbox' ); ?>
			<p><span class="dashicons dashicons-yes"></span><strong><?php _e( 'Supported Galleries', 'foobox-image-lightbox' ); ?></strong> - <?php _e( 'WP Gallery, FooGallery, Justified Image Grid, JetPack Tiled Gallery, NextGen Gallery, Envira Gallery, WooCommerce product images.', 'foobox-image-lightbox' ); ?>
			<p><span class="dashicons dashicons-yes"></span><strong><?php _e( 'Social Sharing', 'foobox-image-lightbox' ); ?></strong> - <?php _e( 'Share content to your favourite social networks with ease.', 'foobox-image-lightbox' ); ?>
			<p><span class="dashicons dashicons-yes"></span><strong><?php _e( 'Look and feel', 'foobox-image-lightbox' ); ?></strong> - <?php _e( 'Default, Metro and Flat styles available. You can also change color schemes and icons.', 'foobox-image-lightbox' ); ?>
			<p><span class="dashicons dashicons-yes"></span><strong><?php _e( 'Fullscreen Mode', 'foobox-image-lightbox' ); ?></strong> - <?php _e( 'Use the browser\'s native fullscreen mode to showcase your media.', 'foobox-image-lightbox' ); ?>
			<p><span class="dashicons dashicons-yes"></span><strong><?php _e( 'Custom JS &amp; CSS', 'foobox-image-lightbox' ); ?></strong> - <?php _e( 'Power-users can add custom JS and CSS using our advanced settings.', 'foobox-image-lightbox' ); ?>
			<p><span class="dashicons dashicons-yes"></span><strong><?php _e( 'PLUS tons more!', 'foobox-image-lightbox' ); ?></strong> - <?php _e( 'With 85+ settings available, you can customize FooBox to your heart\'s content.', 'foobox-image-lightbox' ); ?>

			<?php if ( !foobox_hide_pricing_menu() ) { ?>
			<h4><?php printf( '<a href="%s">%s</a>', esc_url ( foobox_pricing_url() ), $button_text ); ?></h4>
			<?php } ?>

		</div>
	</div>
	<div id="demo_tab" class="feature-section nav-container" style="display: none">
		<?php
		$size     = 70;
		$location = 'https://s3.amazonaws.com/foocdn/';
		$demo_images = array(
			array(
				'src'  => '1.jpg',
				'title' => __( 'Your Image Title Goes Here', 'foobox-image-lightbox' ),
				'desc'  => __( 'You can have a nice long image description that goes here', 'foobox-image-lightbox' ),
			),
			array(
				'src'  => '2.jpg',
				'title' => __( 'Beach Sandcastle', 'foobox' ),
				'desc'  => __( 'HTML is also <a href=\'#\'>allowed</a> in your <em>descriptions</em>!', 'foobox-image-lightbox' )
			),
			array(
				'src'  => '3.jpg',
				'title' => __( 'Title With No Description', 'foobox-image-lightbox' ),
			),
			array(
				'src'  => '4.jpg',
				'desc'  => __( 'A caption with no title, and only a long description describing the image', 'foobox' )
			),
			array(
				'src'  => '6.jpg'
			)
		);
		?>
		<style>
			.about-wrap .feature-section p.demo-gallery {
				max-width: 40em;
			}
			.demo-gallery a
			{
				display: inline-block;
				float: left;
				margin: 10px;
				text-align: center;
				padding: 2px;
				border: 1px solid #9D9B8B;
				-webkit-box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
				-moz-box-shadow: 0 0 10px rgba(0,0,0,0.5);
				box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
			}

			.demo-gallery a img
			{
				padding: 0px;
				width: 70px;
				display: block;
			}

			.demo-gallery a:hover
			{
				opacity: 0.8;
			}
		</style>
		<p class="demo-gallery">
			<?php foreach ($demo_images as $demo_image) {
				$a_href = ' href="' . $location . $demo_image['src'] . '"';
				$a_title = isset( $demo_image['title'] ) ? ' data-caption-title="' . $demo_image['title'] . '"' : '';
				$a_desc = isset( $demo_image['desc'] ) ? ' data-caption-desc="' . $demo_image['desc'] . '"' : '';

				$img_src = ' src="' . $location . 'thumbs/' . $demo_image['src'] . '"';
				$img_width = ' width="' . $size . '"';
				$img_height = ' height="' . $size . '"';
				?>
				<a<?php echo $a_href . $a_title . $a_desc; ?> class="foobox" rel="foobox"><img <?php echo $img_src . $img_width . $img_height; ?>/></a>
			<?php } ?>
		</p>
		<div style="clear:both"></div>
		<p style="text-align: center">
			<a target="_blank" href="https://pixabay.com"/><?php _e( 'images found on pixabay.com', 'foobox-image-lightbox' );?></a>
		</p>
		<?php if ( !class_exists( 'FooGallery_Plugin' ) ) { ?>
		<h2><?php _e( 'Looking for a Gallery Plugin?', 'foobox-image-lightbox' );?></h2>
		<p>
			<?php printf( __( 'Creating galleries has never been easier with our free %s plugin and our premium %s extension, both of which work beautifully with FooBox!', 'foobox-image-lightbox' ),
					'<strong><a target="_blank" href="' . $foogallery_url . '">FooGallery</a></strong>',
					'<strong><a target="_blank" href="http://fooplugins.com/plugins/foovideo?utm_source=fooboxfreeplugin&utm_medium=fooboxfreefoovideolink&utm_campaign=foobox_free_admin_notice">FooVideo</a></strong>'); ?>
		</p>
		<h4><?php printf( '<a href="%s" target="_blank">%s</a>', $foogallery_url, __( 'Download FooGallery', 'foobox-image-lightbox' ) ); ?></h4>
		<?php } ?>
	</div>
