=== FooBox Image Lightbox WordPress Plugin ===
Contributors: bradvin, fooplugins, freemius
Donate link: http://fooplugins.com
Tags: lightbox,media,images,gallery,modal,gutenberg
Requires at least: 3.5.1
Tested up to: 5.2.2
Stable tag: trunk
License: GPLv2 or later
License URI: http://www.gnu.org/licenses/gpl-2.0.html

A responsive image lightbox for WordPress galleries, WordPress attachments & FooGallery

== Description ==

FooBox was the first lightbox to take responsive layout seriously. Not only does it scale images to look better on phones, but it rearranges it's button controls to look great in both portrait or landscape orientation.

Works with most image gallery plugins, but works best with our [FooGallery Gallery WordPress Plugin](http://foo.gallery).

**New Feature : Gutenberg Ready!**

FooBox works out of the box with the image block and gallery block in the new Gutenberg Editor. Image captions set in the editor are also automatically picked up by FooBox. All you need to do is make sure your images and galleries link to the media file.

**Complete FooBox Asset Control**

By default, FooBox includes javascript and stylesheet assets into all your pages. We do this, because we do not know if the page content contains media or not.
If you want more control over when FooBox assets are included, you can now exclude the assets by default, by enabling a setting. Then on each page, you can choose to include them when required.
Alternatively, you can leave the setting disabled, and then choose to exclude the FooBox assets from particular pages. A new metabox is now available when editing your pages or posts.
This new feature was only available in the PRO version beforehand, but we feel control over your website performance is something you should not have to pay for. Enjoy!

**Now includes a 7-day free trial of FooBox Pro!**

**FooBox Image Lightbox Features:**

*	Responsive design
*	Sexy lightbox design
*	Zero configuration!
*	Works with WordPress galleries
*	Works with WordPress captioned images
*	Control when to exclude / include FooBox JS &amp; CSS assets

**[FooBox PRO](http://fooplugins.com/plugins/foobox/?utm_source=fooboxfreeplugin&utm_medium=fooboxfreeprolink&utm_campaign=foobox_free_wprepo) Features:**

*	Social sharing (10+ networks)
*	Video support
*	HTML support
*	iFrame support
*	Deeplinking
*	Fullscreen and slideshow modes
*	Metro lightbox style
*	Flat lightbox style
*	5 color schemes, 12 buttons icons and 11 loader icons
*	85+ settings to customize

**[FooBox PRO](http://fooplugins.com/plugins/foobox/?utm_source=fooboxfreeplugin&utm_medium=fooboxfreeprolink&utm_campaign=foobox_free_wprepo) Works With:**

*	[The Best Image Gallery Plugin for WordPress](http://foo.gallery)
*	NextGen
*	[Justified Image Grid](http://codecanyon.net/item/justified-image-grid-premium-wordpress-gallery/2594251)
*   Envira Gallery
*	WooCommerce product images (Works with WooCommerce v3+)
*	JetPack Tiled Gallery
*	AutOptimize

Check out the [full feature comparison](http://fooplugins.com/foobox-feature-comparison/?utm_source=fooboxfreeplugin&utm_medium=fooboxcomparelink&utm_campaign=foobox_free_wprepo).

**Translations**

* [Serbo-Croatian by Borisa Djuraskovic](http://www.webhostinghub.com/)

== Installation ==

1. Upload `foobox-free` folder to the `/wp-content/plugins/` directory
2. Activate the plugin through the 'Plugins' menu in WordPress

== Frequently Asked Questions ==

= FooBox is not working. There is an error in the console "Uncaught ReferenceError: FooBox is not defined" =

Some plugins or themes defer javascript in the page, which causes the FooBox initialization code to run BEFORE the FooBox main script is loaded. This has been fixed in version 1.2.24. Please upgrade.

= My theme has a built-in lightbox and it shows under FooBox. What can I do? =

There is a setting to try and disable hard coded lightboxes, but this is not a sure-fire solution for every scenario. If that setting does not work for you, you might need to deregister certain javascript files, or uncomment certain lines of code in your theme to remove it's lightbox.

== Screenshots ==

1. Frontend example
2. Phone example

== Changelog ==

= 2.7.3 =
* Fix : Fixed get_blog_list error
* Update : Freemius SDK 2.3.0

= 2.7.1 =
* Fix : Fixed swipe issues in Chrome
* Update : Updated to latest FooBox client JS & CSS

= 2.7.0 =
* New : Reworked how FooBox loads, to work better with optimization plugins (Autoptimize / WP Rocket)
* Remove : Removed support for Google+ in social sharing
* Update : Updated to handle Chrome's new allow attribute in videos
* Update : Updated to latest FooBox client JS & CSS

= 2.6.5 =
* New : Added support for SVG images
* New : Added support for WebP images
* Fix : Fixed scroll blocking violation warnings in dev tools
* Update : Updated to latest FooBox client JS & CSS

= 2.6.4 =
* IMPORTANT : Please update to address a security vulnerability.
* Fix : Security vulnerability
* Fix : Included font-display for improved pagespeed score
* Update : Freemius SDK 2.2.4

= 2.6.3 =
* Added support for loading FooGallery galleries within a FooBox
* Fixed layout bugs with certain notched iPhones
* Updated to latest client JS & CSS fixing a few bugs
* Updated to latest Freemius SDK 2.2.3

= 2.6.0 =
* Added support for the Gutenberg gallery and image blocks
* Updated to latest Freemius SDK 2.1.1
* Fixed bug with horizontal scrollbar
* Fixed bug with paging + filtering in FooGallery
* Forced Youtube videos to open using youtube-nocookie.com
* Updated to latest client JS & CSS fixing a few bugs

= 2.5.3 =
* Updated to latest Freemius SDK 2.0.1

= 2.5.2 =
* Updated to latest client JS & CSS fixing a few bugs
* Removed deprecated functions for PHP 7.2 compatibility
* Updated to latest Freemius SDK 1.2.4

= 2.5.1 =
* Dropped support for IE7 and IE8 (removing CSS validation errors)
* Updated to latest client JS & CSS fixing a few bugs

= 2.5.0 =
* Added new setting to exclude FooBox assets by default
* Added metabox on all public post types to include/exclude FooBox assets
* Major version bump to avoid confusion with FooBox PRO 2.4.0.0

= 1.2.34 =
* Fix : lightbox was not working with FooGallery paging
* Updated to latest client CSS
* Updated to latest Freemius SDK 1.2.1.10
* Removed FooGallery admin notices

= 1.2.27 =
* Fix : default caption state was disabled when no settings were saved.
* Updated to latest Freemius SDK 1.2.1.7.1

= 1.2.26 =
* Fix : disappearing captions. Renamed "Show Captions" setting to "Hide Captions" and default to disabled.

= 1.2.25 =
* Added setting to disable captions
* Added setting to change image counter text

= 1.2.24 =
* Added better browser support for defer javascript loading added in 1.2.23

= 1.2.23 =
* Added support for plugins that defer javascript loading, e.g. AutOptimize

= 1.2.22 =
* Updated to latest JS and CSS fixing multiple issues and bugs

= 1.1.11 =
* Updated to latest JS and CSS fixing some bugs
* Updated to latest Freemius 1.2.1.5 SDK
* Free trial for PRO now included in getting started page

= 1.1.10 =
* Fix deactivation issue when PRO is activated

= 1.1.9 =
* New setting for dropping IE7 support (for valid CSS)
* Fix for when multiple jQuery versions loaded on page!
* Fix for not including scripts for setting 'disable other lightboxes'

= 1.1.8 =
* IMPORTANT : clear your site cache when updating - if you use a caching plugin.
* Added clear cache message to getting started page
* Removed duplicate settings page
* Updated opt-in message
* Fix : loosing scroll position when scrollbars are hidden

= 1.1.7 =
* Integrated Freemius tracking and upgrade system
* Moved FooBox into top-level menu item
* Complete overhaul of Getting Started page, including demo
* Updated to use latest FooBox JS and CSS

= 1.0.14 =
* Hide foo admin notice on mobile devices
* More CSS tweaks for admin on smaller screen sizes

= 1.0.13 =
* Updated settings page to be responsive
* Tested with WP 4.6

= 1.0.12 =
* Updated to use latest FooBox JS and CSS
* Removed discount for FooBox PRO

= 1.0.11 =
* Updated to use latest FooBox JS and CSS
* Updated settings to include demo tab
* Updated admin screens be responsive on phones

= 1.0.10 =
* Updated to use latest FooBox JS fixing few bugs
* Smarter admin warnings when using with FooGallery

= 1.0.9 =
* Updated to use latest FooBox JS fixing few bugs
* Reorder selectors so FooGallery can take preference in some cases

= 1.0.8 =
* Updated to use latest FooBox JS
* Added new Getting Started landing page on activation
* Added support for wp.org language packs
* Better FooGallery integration

= 1.0.7 =
* Updated to latest version of javascript and CSS files
* Plays better with FooBox PRO now

= 1.0.6 =
* Fixed navbar issues in Chrome on IOS

= 1.0.5 =
* Fixed very minor vulnerability with add_query_arg function used in admin plugins page

= 1.0.4 =
* Improved FooGallery support
* Added keyboard navigation support!
* 50% offer included for PRO version

= 1.0.3 =
* Added FooGallery support
* Added .nolightbox to exclusions
* Added .pot translation file
* Added Bottomless design banner to "FooBot Says..." tab

= 1.0.2.1 =
* Fixed jQuery dependency issue with themes that do not load jQuery by default

= 1.0.2 =
* Added setting "Show Captions On Hover"
* Added "FooBot Says..." tab on settings page

= 1.0.1 =
* first version!