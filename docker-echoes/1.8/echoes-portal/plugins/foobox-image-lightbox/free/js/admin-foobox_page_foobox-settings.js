jQuery(document).ready(function($) {
    $.admin_tabs = {

        init : function() {
          $("a.nav-tab").click( function(e) {
              e.preventDefault();

              $this = $(this);

              $this.parents(".nav-tab-wrapper:first").find(".nav-tab-active").removeClass("nav-tab-active");
              $this.addClass("nav-tab-active");

              $(".nav-container:visible").hide();

              var hash = $this.attr("href");

              $(hash+'_tab').show();

              //fix the referer so if changes are saved, we come back to the same tab
              var referer = $("input[name=_wp_http_referer]").val();
              if (referer.indexOf("#") >= 0) {
                referer = referer.substr(0, referer.indexOf("#"));
              }
              referer += hash;

              window.location.hash = hash;

              $("input[name=_wp_http_referer]").val(referer);
          });

          if (window.location.hash) {
            $('a.nav-tab[href="' + window.location.hash + '"]').click();
          }

          return false;
        }

    }; //End of admin_tabs

    $.admin_tabs.init();

    $('.radio_selector a').click(function(e) {
		var $div = $(this).parents('.radio_selector:first');
		$div.find('a').removeClass('selected');
		var inputId = $(this).parents('label:first').attr('for');
		$('#' + inputId).click();
        $(this).addClass('selected');
    });

    //$('.foobox-colorpicker').minicolors();

	$('.foobox_insert_code').click(function(e) {
		e.preventDefault();

		var $target = $( $(this).attr('href') );
		var code = $(this).data('code');

		if ( $target.val() !== '' ) {
			if (!confirm('The existing text will be overwritten. Proceed?')) {
				return;
			}
		}

		$target.val(code);
	});

});