( function( $ ) {
    $( window ).load( function() {

        // Tell Isotope to watch the .portfolio container
        var $container = $( '.portfolio' );

        $container.isotope( {
            filter: '*',
            layoutMode: 'fitRows',
            resizable: false,
        } );

        // When the portfolio category is clicked, filter.
        $( '.portfolio-filter li' ).click( function(){
            var selector = $( this ).attr( 'data-filter' );
            $container.isotope( {
                filter: selector,
            } );
            $('.portfolio-filter li').removeClass('active');
            $(this).addClass('active');
            return false;
        } );
    } );

} )( jQuery );