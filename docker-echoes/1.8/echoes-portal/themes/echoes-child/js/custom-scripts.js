// var j = jQuery.noConflict();
// j(document).ready(function() {
//     // alert("custom js loaded!");
// });





jQuery(document).ready(function( $ ) {

    $(window).on("resize", function () {

        // console.log($(this).width());

        var screewidth = $(this).width();

        if (screewidth < 1201) {
            // verify if the wpml is inside the mobile menu
            if(!$("#header_languages #main-menu li.header_languages_item").length){
                // insert the wpml menu inside the mobile menu
                $("#navbarNavDropdown #main-menu").append($('#header_languages li.header_languages_item'));
                // $("#header_languages").remove();

                // console.log('submenu explore opened!');

            }
            // open the submenu blue 'Explorer'
            $("#main-menu .header-search .dropdown-menu").addClass('show');

        } else {
            // verify if the header_language ul exists

            if($("#header_languages").length){
                if( !$("#header_languages li.header_languages_item").length){
                    $("#header_languages").append( $('#main-menu li.header_languages_item') );
                }
            }

            if ( $("#main-menu .header-search .dropdown-menu").hasClass('show') ) {
                $("#main-menu .header-search .dropdown-menu").removeClass('show');
            }

        }

        // Invoke the resize event immediately
    }).resize();






    /* resources filter menu responsive */


    // onClick new options list of new select
    $('.ae-select-content').text($('.dropdown-menu-filter > li.selected').text());

    var newOptions = $('.dropdown-menu-filter > li');

    newOptions.click(function() {
        $('.ae-select-content').text($(this).text());
        $('.dropdown-menu-filter > li').removeClass('selected');
        $(this).addClass('selected');

    });

    var aeDropdown = $('.ae-dropdown-filter');

    aeDropdown.click(function() {
        $('.dropdown-menu-filter').toggleClass('ae-hide');

        $('.ae-select').toggleClass('ae-select-display');

        console.log('opened submenu explore');
    });


});
