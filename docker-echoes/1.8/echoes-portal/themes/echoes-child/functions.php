<?php
if ( ! defined( 'ABSPATH' ) ) {
	exit; // Exit if accessed directly.
}

function understrap_remove_scripts() {
    wp_dequeue_style( 'understrap-styles' );
    wp_deregister_style( 'understrap-styles' );

    wp_dequeue_script( 'understrap-scripts' );
    wp_deregister_script( 'understrap-scripts' );

    // Removes the parent themes stylesheet and scripts from inc/enqueue.php
}
add_action( 'wp_enqueue_scripts', 'understrap_remove_scripts', 20 );

add_action( 'wp_enqueue_scripts', 'theme_enqueue_styles' );
function theme_enqueue_styles() {

	// Get the theme data
	$the_theme = wp_get_theme();
    wp_enqueue_style( 'child-understrap-styles', get_stylesheet_directory_uri() . '/css/child-theme.min.css', array(), $the_theme->get( 'Version' ) );
    wp_enqueue_style( 'child-understrap-custom-styles', get_stylesheet_directory_uri() . '/style.css', array(), $the_theme->get( 'Version' ) );


    wp_enqueue_script( 'jquery');
    wp_enqueue_script( 'child-understrap-scripts', get_stylesheet_directory_uri() . '/js/child-theme.min.js', array(), $the_theme->get( 'Version' ), true );
    if ( is_singular() && comments_open() && get_option( 'thread_comments' ) ) {
        wp_enqueue_script( 'comment-reply' );
    }
}

function add_child_theme_textdomain() {
    load_child_theme_textdomain( 'understrap-child', get_stylesheet_directory() . '/languages' );
}
add_action( 'after_setup_theme', 'add_child_theme_textdomain' );


function custom_add_google_fonts() {
    wp_enqueue_style( 'custom-google-fonts', 'https://fonts.googleapis.com/css?family=Montserrat:300,400,500', false );
}
add_action( 'wp_enqueue_scripts', 'custom_add_google_fonts' );


//**************************************************************
//********************* Accordeon filter ***********************
//**************************************************************

add_image_size( 'news-featured', 670, 350, array( 'left', 'top' ) );
add_image_size( 'news-thumbnail', 360, 200, array( 'left', 'top' ) );
add_image_size( 'news-siderbar-thumbnail', 110, 80, array( 'left', 'top' ) );




/* ****************************************************************** */
/* ********************  Resources Filter   ************************** */
/* ****************************************************************** */

function resourcesShortcode() {

    do_shortcode('[asr_ajax]');

}
add_shortcode('resourcesShortcode', 'resourcesShortcode');


// load custom scripts

add_action("wp_enqueue_scripts", "dcms_load_custom_js", 1);

function dcms_load_custom_js(){
//    global $post;
//    $post_id = $post->ID;
    wp_register_script('dcms_miscript', get_stylesheet_directory_uri() . '/js/custom-scripts.js', array('jquery'), '1', true);
    wp_enqueue_script('dcms_miscript');
}



// shortcode to allow you to get news cut by letters
function ondeuev_excerpt_by_letters ( $atts = array() ) {

    $atts = shortcode_atts(
        array(
            'letters' => '',
        ), $atts
    );

    $letters = $atts['letters'];

    $news = ondeuev_get_excerpt_by_letters( $letters );

    return $news;

}
add_shortcode('excerpt_by_letters', 'ondeuev_excerpt_by_letters');

// get the latest news and cut by letters the content...
function ondeuev_get_excerpt_by_letters ( $letters ) {

    $args = array(
        'post_type' => 'news',
        'post_status' => 'publish',
        'posts_per_page' => -1,
    );

    $query = new WP_Query( $args );

    // The Loop
    if ( $query->have_posts() ) {

        $newshtml = '';

        while ( $query->have_posts() ) {

            $query->the_post();

            $title = get_the_title( get_the_ID() );
            $content = get_the_content( get_the_ID() );
            $newcontent = substr( $content, 0, $letters ) . "...";
            $permalink = get_the_permalink();
            $image = get_the_post_thumbnail_url( '', array(360, 200) );
            $date = get_the_date();


            $newshtml .= '<div class="auto_height one-third column" style="height: 580px;">';

            $newshtml .= '<div class="blog_two_style clearfix">';

            $newshtml .= '<div class="main">';


            $newshtml .= '<div class="image">';

            $newshtml .= '<a href='. $permalink .'>
                                            <img src="'. $image .'" class="attachment-news-thumbnail size-news-thumbnail wp-post-image" alt="'. $title .'">
                                        </a>';

            $newshtml .= '</div>'; // image


            $newshtml .= '<div class="content clearfix">';


            $newshtml .= '<h4><a href="'. $permalink .'">'. $title .'</a></h4>';

            $newshtml .= '<div class="meta">' . $date . '</div>';

            $newshtml .= '<div class="border"></div>';

            $newshtml .= $newcontent;

            $newshtml .= '<div class="clearfix"></div>';

            $newshtml .= '<small><a href="' . $permalink . '">'. __('Read more') .'</a></small>'; // content

            $newshtml .= '</div>'; // content




            $newshtml .= '</div>'; // main

            $newshtml .= '</div>'; // blog_two_style

            $newshtml .= '</div>'; // auto_height


        } // end while

    } // end if
    /* Restore original Post Data */
    wp_reset_postdata();

    return $newshtml;

}
