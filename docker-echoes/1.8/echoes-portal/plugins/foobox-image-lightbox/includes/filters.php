<?php
/**
 * FooBox Filters
 * Date: 30/08/2016
 */

define( 'FOOBOX_FILTER_SUPPORT_MENU_URL', 'foobox_admin_support_menu_url' );

/*
 * Filter for admin support menu url
 *
 * Called from /foobox-free.php
 */
function foobox_filter_admin_menu_after( $url ) {
	return apply_filters( FOOBOX_FILTER_SUPPORT_MENU_URL, $url );
}