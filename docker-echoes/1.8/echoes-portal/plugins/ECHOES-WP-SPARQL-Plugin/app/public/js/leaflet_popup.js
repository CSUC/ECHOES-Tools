function leaflet_popup_setup(callback = null){
    jQuery(".leaflet-popup-close-button").click(function(){
        leaflet_popup_hide();
        if (callback != null){
            callback();
        }
    });
}

function leaflet_popup_hide(){
    jQuery(".leaflet-container").fadeOut(300);
}

function leaflet_popup_show(x, y){                                 
    jQuery(".leaflet-container").fadeIn(300);
    jQuery(".leaflet-popup").css('transform', `translate3d(${x}px, ${y}px, 0px)`);
}

function leaflet_set_html_content(html){
    jQuery(".leaflet-popup-content").html(html);
}
