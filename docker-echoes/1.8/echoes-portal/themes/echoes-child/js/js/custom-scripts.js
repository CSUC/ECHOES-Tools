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
            }

        } else {
            // verify if the header_language ul exists

            if($("#header_languages").length){
                if( !$("#header_languages li.header_languages_item").length){
                    $("#header_languages").append( $('#main-menu li.header_languages_item') );
                }
            }


            // $("#navbarNavDropdown #main-menu li.header_languages_item").remove();
        }

        // Invoke the resize event immediately
    }).resize();




});
