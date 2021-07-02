<?php
$title = apply_filters( 'foobox_getting_started_title', __( 'Welcome to FooBox!', 'foobox-image-lightbox' ) );
$tagline = apply_filters( 'foobox_getting_started_tagline', __( 'Thank you for choosing FooBox as your lightbox! A great looking and responsive lightbox for your WordPress website!', 'foobox-image-lightbox' ) );
$test_link = apply_filters( 'foobox_getting_started_testlink', '<a rel="foobox" href="' . foobox_asset_url( 'img/foobot_red.png' ) . '" class="foobox">' . __( 'See it in action now!', 'foobox-image-lightbox' ) . '</a>' );
?>
<style>

	.foobox-badge-foobot {
		position: absolute;
		top: 0;
		right: 0;
		background:url(<?php echo foobox_asset_url( 'img/foobot.png'); ?>) no-repeat;
		width:109px;
		height:200px;
	}

	.feature-section h4 {
		text-align: center;
	}

	.feature-section h4 a {
		background: #0085ba;
		border-color: #0073aa #006799 #006799;
		-webkit-box-shadow: 0 1px 0 #006799;
		box-shadow: 0 1px 0 #006799;
		color: #fff;
		text-decoration: none;
		text-shadow: 0 -1px 1px #006799, 1px 0 1px #006799, 0 1px 1px #006799, -1px 0 1px #006799;
		padding: 5px 20px;
		border-radius: 3px;
	}

	@media only screen and (max-width: 500px) {
		.foobox-badge-foobot,
		.feature-section img {
			display: none;
		}
	}

	.about-wrap div.updated.fs-notice {
		display: block !important;
		width: 85%;
	}

</style>
<div class="wrap about-wrap foobox-getting-started">
	<h1><?php echo $title; ?></h1>
	<div class="about-text">
		<?php echo $tagline . ' ' . $test_link; ?>
	</div>
	<div class="foobox-badge-foobot"></div>
	<?php foobox_action_admin_menu_render_getting_started(); ?>
</div>
