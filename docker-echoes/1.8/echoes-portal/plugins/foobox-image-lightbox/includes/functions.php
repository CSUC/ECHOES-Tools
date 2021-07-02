<?php
/**
 * Common Foobox functions
 * Date: 27/08/2016
 */

function foobox_asset_url( $asset_relative_url ) {
	return trailingslashit(FOOBOX_BASE_URL) . 'assets/' . ltrim( $asset_relative_url, '/' );
}

function foobox_settings_url() {
	return admin_url( 'admin.php?page=' . FOOBOX_BASE_PAGE_SLUG_SETTINGS );
}

function foobox_pricing_url() {
	return admin_url( 'admin.php?page=foobox-image-lightbox-pricing' );
}

function foobox_freetrial_url() {
	return foobox_pricing_url() . '&trial=true';
}

function foobox_hide_pricing_menu() {
	return false;
}