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

				window.location.hash = hash;
			});

			if (window.location.hash) {
				$('a.nav-tab[href="' + window.location.hash + '"]').click();
			}

			return false;
		}

	}; //End of admin_tabs

	$.admin_tabs.init();
});