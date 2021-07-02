<?php
/**
 * Template Name: News
 *
 * The template for displaying News page.
 */

if ( ! defined( 'ABSPATH' ) ) {
	exit; // Exit if accessed directly.
}

get_header();

$container = get_theme_mod( 'understrap_container_type' );

?>

<div class="wrapper" id="page-wrapper">

	<div class="<?php echo esc_attr( $container ); ?>" id="content" tabindex="-1">

		<div class="row">

			<!-- Do the left sidebar check -->
			<?php get_template_part( 'global-templates/left-sidebar-check' ); ?>

			<main class="site-main" id="main">


				<?php while ( have_posts() ) : the_post(); ?>

                    <article class="post-1210 page type-page status-publish hentry" id="post-1210">

                        <header class="entry-header">
                            <h1 class="entry-title"><?php echo get_the_title(); ?></h1>
                        </header><!-- .entry-header -->


                        <div class="entry-content">

                            <div id="" class="panel-layout">
                                <div id="" class="panel-grid panel-no-style">
                                    <div id="pgc-1210-0-0" class="panel-grid-cell">
                                        <div id="panel-1210-0-0-0" class="widget_text so-panel widget widget_custom_html panel-first-child panel-last-child" data-index="0">

                                            <div class="so-widget-blog-style-two so-widget-blog-style-two-blog-style-two-style-f70b9422c3b0">

                                                <div id="news" class="widget_text panel-widget-style panel-widget-style-for-1210-0-0-0">

                                                    <div class="all_main">
                                                        <?php echo do_shortcode ( get_the_content() ); ?>
                                                    </div>

                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                        </div><!-- .entry-content -->

                    </article>

				<?php endwhile; // end of the loop. ?>


			</main><!-- #main -->

			<!-- Do the right sidebar check -->
			<?php get_template_part( 'global-templates/right-sidebar-check' ); ?>

		</div><!-- .row -->

	</div><!-- #content -->

</div><!-- #page-wrapper -->

<?php get_footer(); ?>
