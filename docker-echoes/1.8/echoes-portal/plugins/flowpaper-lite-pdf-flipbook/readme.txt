=== flowpaper ===
Contributors: Devaldi
Tags: flipbook, pdf viewer, viewer, web pdf viewer, flip book
Requires at least: 3.0
Tested up to: 5.0
Stable tag: 4.3
License: GPLv3
Donate link: https://flowpaper.com/download/
License URI: http://www.gnu.org/licenses/gpl.html

Flipbook PDF viewer - all you need is a PDF : [flipbook pdf="https://flowpaper.com/example.pdf"]

== Description ==

This is a PDF viewer plugin that allows you to embed your PDF catalogs, magazines and brochures as flipbooks on your web site in HTML5 format for free.

[FlowPaper Home Page](https://flowpaper.com/) | [Examples](https://flowpaper.com/demo/)

The plugin supports the majority of browsers and devices. It is currently limited to PDF documents that are 15 megabytes or smaller for PDFs that are not uploaded to FlowPaper cloud.
To publish larger PDF documents, customize the look and to show the viewer without FlowPaper branding, please visit our homepage. It supports both light and dark themes.

[youtube https://www.youtube.com/watch?v=tdCxHbfrYQY]

**How to embed a PDF using a URL:**

1. Add a the plug in shortcode to a post like this: `[flipbook pdf="http://full-url/document.pdf"]`
2. Change the PDF Url ("http://full-url/document.pdf") to a PDF file you want to display. Voila!


**How to embed a new PDF uploaded to Wordpress:**

1. Click on the "Media" link in the left panel
2. Upload your PDF document
3. Click on the uploaded PDF document and copy the URL to the PDF file
4. Create a new post and add the shortcode `[flipbook pdf="http://full-url/document.pdf"]` to your post. Update the url in the shortcode to the URL you copied from your media library


**How to embed a publication uploaded to FlowPaper Cloud:**

1. Make sure the publication is uploaded to FlowPaper cloud using the desktop publisher
2. Copy the link that the desktop publisher created for you
3. Create a new post and add the shortcode `[flipbook pdf="http://online.flowpaper.com/publication"]` to your post. Update the url in the shortcode to the URL you copied from the desktop publisher


**Customizing Theme**

Style of the viewer theme (default: "dark") by adding the "theme" parameter and setting it to either "light" or "dark" like this: `[flipbook pdf="http://full-url/document.pdf" theme="light"]`

**Opening in a Lightbox overlay**

You can display a cover of your publication and open it in a lightbox overlay by supplying the "lightbox" and "cover" parameters like this: `[flipbook pdf="http://full-url/document.pdf" lightbox="true" cover="https://flowpaper.com/images/demo/zine-magazine@2x.jpg"]`

**Setting Header**

You can modify the header that is displayed (default: "Loading viewer ...") by adding the "header" to the embed code like this: `[flipbook pdf="http://full-url/document.pdf" header="Opening catalog..."]`

**Setting Publication Title**

You can modify the publication title that is displayed (default: PDF title) by adding the "title" to the embed code like this: `[flipbook pdf="http://full-url/document.pdf" title="Fashion catalog 2016"]`



**About FlowPaper**

FlowPaper is a web pdf viewer that uses a number of technologies to bring your PDF documents to the web in the most beautiful friendliest ways possible.
For a full set of features and an unbranded viewer, please visit the [FlowPaper home page](https://flowpaper.com/ "FlowPaper home page"). This plug in is built so that people can experience one of our pdf viewers as easy as possible.

== Customizing ==

= Parameters: =
* **pdf** - source of the PDF you want to embed: `[flipbook pdf="http://flowpaper.com/example.pdf"]`;
* **width** - width in pixels or in percents: `[flipbook width="100%"]` or `[flipbook width="600"]`; by default width="100%";
* **height** - height in pixels: `[flipbook height="500"]`; by default height="500";
* **theme** - sets the theme for the viewer: `[flipbook theme="light"]`; "dark" by default;
* **singlepage** - lets you to force the viewer to only show one page at the time: `[flipbook singlepage="true"]`; "auto" by default which selects single page if the publication uses landscape pages;
* **lightbox** - opens the viewer in a lightbox overlay when clicked: `[flipbook lightbox="true"]`; "false" by default;
* **cover** - sets a image cover to be used for the viewer. Useful in combination with the lightbox setting: `[flipbook cover="http://domain.com/myimage.png"]`;
* **header** - sets the header for the viewer: `[flipbook header="Custom Header"]`; "Loading viewer ..." by default;
* **title** - sets the title for the viewer: `[flipbook title="Custom Title"]`; taken from the PDF by default;
* **id** - allows to add the id of the flowpaper frame: `[flipbook id="custom_id"]`; removed by default;
* **same_height_as** - allows to set the height of flowpaper same as target element: `[flipbook same_height_as="div.sidebar"]`, `[flipbook same_height_as="div#content"]`, `[flipbook same_height_as="body"]`, `[flipbook same_height_as="html"]`; removed by default;

== Screenshots ==

1. FlowPaper PDF flipbook

== Upgrade Notice ==
### 1.8.4 ###
Fix for set_magic_quotes_runtime which was being set (depricated in PHP 5.3 and higher)

== Changelog ==

= 1.0 =
* Initial release

= 1.1 =
* Slight change in URL for the iframe that gets embedded

= 1.2 =
* Use HTTPS/SSL if the page is using SSL

= 1.3 =
* Implemented method for loading the PDF directly from Wordpress if hosted as wp-content

= 1.4 =
* Improved http headers
* Improved support for browsers not supporting iframes

= 1.5 =
* Tighter access control checks

= 1.6 =
* Fixed possible character encoding issue

= 1.7 =
* Renamed parse request function name to avoid conflict with other plugins
* Wrapped flowpaper_plugin_parse_request content in try/catch to allow fallback to work

= 1.7.2 =
* Minor changes to url that gets passed to the viewer

= 1.7.3 =
* Corrected an issue related to wp_upload_dir which was giving errors on some multi-site scenarios

= 1.7.4 =
* Another small correction for wp_upload_dir related to older versions of PHP

= 1.7.5 =
* Another the ability to set theme as part of the tag. Set it to either theme="light" or theme="dark"

= 1.7.6 =
* Added the ability to handle options requests, will speed up Wordpress hosted PDFs

= 1.7.7 =
* Minor correction to previous version

= 1.8.0 =
* Added options for allowing "title" and "header" to be set

= 1.8.1 =
* Bump version to refresh read me

= 1.8.2 =
* Fixed so viewer refreshed when post gets modified to make sure the latest version of the PDF is always shown

= 1.8.3 =
* Added support for publications hosted on FlowPaper cloud

= 1.8.4 =
* Fix for set_magic_quotes_runtime which was being set (depricated in PHP 5.3 and higher)

= 1.8.5 =
* Fixed an issue where https/SSL wasn't used when using cloud hosting

= 1.8.6 =
* Added support for opening the viewer in a fancybox/modal/popover overlay. Use it in combination with the cover setting to show a cover of the publication. For example:
`[flipbook pdf="https://flowpaper.com/Paper.pdf" **lightbox="true"** **cover="https://flowpaper.com/images/demo/zine-magazine@2x.jpg"**]`

= 1.8.7 =
* General code cleanup

= 1.8.8 =
* General read me update

= 1.8.9 =
* Added missing js and css files

= 1.9.0 =
* Added support for the "singlepage" parameter which forces the viewer to show one page at the time
* Resolved a function that was conflicting with woocommerce

= 1.9.1 =
* Improved the ability to embed self hosted publications created with the desktop publisher

== Installation ==

1. install and activate the plugin on the Plugins page
2. add shortcode `[flipbook pdf="https://flowpaper.com/Paper.pdf"]` to page or post content (change the URL to a PDF file you want to show)

== Frequently Asked Questions ==
= What is the limitation of this version? =
It has a limitation of 15 megabytes for publications that are not hosted on FlowPaper cloud. Self hosted publications reaches the majority of devices and browsers. For full browser coverage and for more options on styling the viewer, please visit the FlowPaper home page.

== Installation ==
