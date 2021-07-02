<?php
/**
 * FooBox Actions
 * Date: 28/08/2016
 */

define( 'FOOBOX_ACTION_ADMIN_MENU_AFTER', 'foobox_admin_menu_after' );
define( 'FOOBOX_ACTION_ADMIN_MENU_RENDER_SETTINGS', 'foobox_admin_menu_render_settings' );
define( 'FOOBOX_ACTION_ADMIN_MENU_RENDER_GETTING_STARTED', 'foobox_admin_menu_render_getting_started' );

/*
 * Action when FooBox admin menus added
 *
 * Called from /includes/admin/menu.php
 */
function foobox_action_admin_menu_after() {
	do_action( FOOBOX_ACTION_ADMIN_MENU_AFTER );
}

/*
 * Action when FooBox admin settings menu clicked.
 *
 * Called from /includes/admin/menu.php
 */
function foobox_action_admin_menu_render_settings() {
	do_action( FOOBOX_ACTION_ADMIN_MENU_RENDER_SETTINGS );
}

/*
 * Action when FooBox admin getting started menu clicked.
 *
 * Called from /includes/admin/menu.php
 */
function foobox_action_admin_menu_render_getting_started() {
	do_action( FOOBOX_ACTION_ADMIN_MENU_RENDER_GETTING_STARTED );
}