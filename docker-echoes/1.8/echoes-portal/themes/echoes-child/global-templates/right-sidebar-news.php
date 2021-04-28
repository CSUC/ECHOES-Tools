<?php
/**
 * Right sidebar news.
 *
 * @package understrap
 */
global $wpdb;
global $post;

if ( ! defined( 'ABSPATH' ) ) {
	exit; // Exit if accessed directly.
}
?>

</div><!-- #closing the primary container from /global-templates/left-sidebar-check.php -->

<?php //$sidebar_pos = get_theme_mod( 'understrap_sidebar_position' ); ?>

<?php //if ( 'right' === $sidebar_pos || 'both' === $sidebar_pos ) : ?>

	<?php //get_template_part( 'sidebar-templates/sidebar', 'right' ); ?>

<?php // endif; ?>

    <div class="col-md-1"> </div>
    <div class="col-md-4 widget-area" id="news-right-sidebar" role="complementary">

<?php

echo "<h2>News</h2>";

$idpost = get_the_ID();

//echo $idpost;

$args = array(
    'posts_per_page' => '2',
    'post_type' => 'news',
//    'orderby' => 'date',
//    'order' => 'DESC',
    'post__not_in' => array($idpost),
);
$query = new WP_Query($args);

if ( $query->have_posts() ) {

    while ($query->have_posts()) {

        $query->the_post();

//        var_dump($query);

        if ($post->post_content) {

            $news_date = date('Y-m-d', strtotime($post->post_date));

            if (has_post_thumbnail( $post->ID ) ) {
                $image = wp_get_attachment_image_src( get_post_thumbnail_id( $post->ID ), 'news-siderbar-thumbnail' );
            }

            echo "<div class='news-sidebar-image'>" . "<img src=" . $image[0] . " style='max-width: 555px;' width='100%'></div>";
            echo "<div class='news-content'>";
            echo "<h3><a class='news-title' href='". $post->guid ."'>" . $post->post_title . "</a></h3>";
            echo "<div class='news-date'>" . $news_date . "</div>";
            echo "</div>";

        }
    }

} else {
    echo "No posts";
}

?>
    </div>
