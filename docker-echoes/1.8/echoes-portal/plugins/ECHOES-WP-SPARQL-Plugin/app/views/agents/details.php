    <?php $bindings = $results->results->bindings[0]; ?>
    <?php 
	$url = $_SERVER['REQUEST_URI'];
        if (strpos($url, '/ca/') !== false) {
                $current_locale = '/ca';
        } elseif (strpos($url, '/nl/') !== false) {
                $current_locale = '/nl';
	} elseif (strpos($url, '/fy/') !== false) {
                $current_locale = '/fy';
        } else {
                $current_locale = '';
        }
    ?>
    <style type="text/css">
        .title_element {
            font-weight: bold;
            font-size: 18px;
        }
        #mynetwork {
            width: 100%;
            height: 500px;
        }
    </style>
   <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
	<link rel="stylesheet" href="<?php echo mvc_css_url('echoes_wp_sparql_plugin', 'leaflet.css') ?>"
   integrity="sha512-Rksm5RenBEKSKFjgI3a41vrjkw4EVPlJ3+OiI65vTjIdo9brlAacEuKOiQ5OFh7cOI1bkDwLqdLw3Zg0cRJAAQ=="
   crossorigin=""/>
    <link href="<?php echo mvc_css_url('echoes_wp_sparql_plugin', 'font-awesome.min.css') ?>" />
    <link rel="stylesheet" href="<?php echo mvc_css_url('echoes_wp_sparql_plugin', 'vis-network.min.css') ?>" />
    <script src="<?php echo mvc_js_url('echoes_wp_sparql_plugin', 'vis-network.min.js') ?>"></script>
    <div class="personal-info"> 
    <?php if (!empty($bindings->gender)): ?><span class="title_element"><?php _e('Gender', 'echoes_wp_sparql_plugin') ?>: </span> <?php echo $bindings->gender->value; ?><br/><br/><?php endif; ?>
    <?php if (!empty($bindings->professionOccupation)): ?><span class="title_element"><?php _e('Profession or Ocupation', 'echoes_wp_sparql_plugin') ?>: </span> <?php echo $bindings->professionOccupation->value; ?><br/><br/><?php endif; ?>
    <?php if (!empty($bindings->placeOfBirth) || !empty($bindings->dateOfBirth)): ?>
        <span class="title_element"><?php _e('Birth', 'echoes_wp_sparql_plugin') ?>: </span>
        <?php if (!empty($bindings->dateOfBirth)): ?>
        <?php $dateBirth = (strpos($bindings->dateOfBirth->value, '-') === false) ? $bindings->dateOfBirth->value : date("d/m/Y", strtotime($bindings->dateOfBirth->value));
              echo $dateBirth ?>
        <?php endif; ?>
        <?php if (!empty($bindings->placeOfBirthName)): ?>
            <?php if (!empty($bindings->dateOfBirth)): ?>
            in 
            <?php endif; ?>
            <a href="<?php echo WP_SITEURL . $current_locale; ?>/agents/?place=<?php echo $bindings->placeOfBirth->value; ?>"><?php echo $bindings->placeOfBirthName->value; ?></a>
        <?php endif; ?>
    <br/><br/>
    <?php endif; ?>
    <?php if (!empty($bindings->placeOfDeath) || !empty($bindings->dateOfDeath)): ?>
        <span class="title_element"><?php _e('Death', 'echoes_wp_sparql_plugin') ?>: </span>
        <?php if (!empty($bindings->dateOfDeath)): ?>
            <?php $dateDead = (strpos($bindings->dateOfDeath->value, '-') === false) ? $bindings->dateOfDeath->value : date("d/m/Y", strtotime($bindings->dateOfDeath->value));
                  echo $dateDead ?>
        <?php endif; ?>
        <?php if (!empty($bindings->placeOfDeathName)): ?>
            <?php if (!empty($bindings->dateOfDeath)): ?>
            in 
            <?php endif; ?>
            <a href="<?php echo WP_SITEURL . $current_locale; ?>/agents/?place=<?php echo $bindings->placeOfDeath->value; ?>"><?php echo $bindings->placeOfDeathName->value; ?></a>
        <?php endif; ?>
    <br/><br/>
    <?php endif; ?>
    <?php if (!empty($bindings->hasMetPlaceName->value)): ?>
        <span class="title_element"><?php _e('Met Place', 'echoes_wp_sparql_plugin') ?>: </span>
            <a href="<?php echo WP_SITEURL . $current_locale; ?>/agents?place=<?php echo $bindings->hasMetPlace->value; ?>"><?php echo $bindings->hasMetPlaceName->value; ?></a>
    <br/><br/>
    <?php endif; ?>
    <?php if (!empty($wikipedia)): ?>
	<span class="title_element"><?php _e('Wikipedia', 'echoes_wp_sparql_plugin') ?>: </span>
	<a href="<?php echo $wikipedia ?>"><?php echo $wikipedia; ?></a>
    <?php endif; ?>
    <br/><br/>
    <?php if (!empty($viaf)): ?>
        <span class="title_element"><?php _e('Viaf', 'echoes_wp_sparql_plugin') ?>: </span>
        <a href="<?php echo $viaf ?>"><?php echo $viaf; ?></a>
    <?php endif; ?>
    </div>
    <hr/>

<div style="width: 100%; float: left; margin-top: 20px; margin-bottom: 20px; float">
    <?php if (isset($graph)): ?>
        <script type="text/javascript">
            let nodes = <?php echo $graph['nodes'] ?>;
            let edges = <?php echo $graph['edges'] ?>;
        </script>
        <br/>
        <span class="title_element"><?php _e('Relations', 'echoes_wp_sparql_plugin') ?>: </span><br/>

        <div id="mynetwork" style="background-color: white"></div>
	<script src="<?php echo mvc_js_url('echoes_wp_sparql_plugin', 'screenfull.min.js') ?>"></script>
        <button class="wpcf7-form-control wpcf7-submit go-full-screen-button"  onclick="javascript:if(screenfull.enabled) screenfull.request(document.getElementById('mynetwork'));"><?php _e('Go fullscreen', 'echoes_wp_sparql_plugin') ?></button>
        <script src="<?php echo mvc_js_url('echoes_wp_sparql_plugin', 'providedCHO_details') ?>"></script>
    <?php endif; ?>
    </div>
<div class="leaflet-container" style="display: none"><div class="leaflet-pane leaflet-popup-pane"><div class="leaflet-popup  leaflet-zoom-animated" style="opacity: 1;"><div class="leaflet-popup-content-wrapper"><div class="leaflet-popup-content" style="width: 232px;"></div></div><div style="margin-top: -1px" class="leaflet-popup-tip-container"><div class="leaflet-popup-tip"></div></div><a class="leaflet-popup-close-button" href="#close">x</a></div></div></div>
    <div class="personal-info">

    <?php if (!empty($bindings->providers->value)): ?>
        <span class="title_element" style="margin-top: 10px"><?php _e('Digital objects', 'echoes_wp_sparql_plugin') ?>: </span>
	<div class="personal-info">
        <?php $providersId = explode('|', $bindings->providers->value); ?>
        <?php $providersTitle = explode('|', $bindings->providerTitles->value); ?>
        <?php $providersTypes = explode('|', $bindings->providerTypes->value); ?>
            <?php for ($i = 0; $i < count($providersId); $i++): ?>
                <div><img src="/wp-content/themes/echoes/img/Assets/ico-picture.svg">&nbsp;&nbsp;<a style="vertical-align: middle" href="<?php echo WP_SITEURL . $current_locale; ?>/providers/details?subject=<?php echo $providersId[$i]; ?>"><?php echo $providersTitle[$i]; ?></a></div>
            <?php endfor; ?>
        </div><br>
    <?php endif; ?>
    <?php if (!empty($bindings->hasMetPlace->value)): ?>
        <span class="title_element" style="margin-top: 10px"><?php _e('Places Met', 'echoes_wp_sparql_plugin') ?>: </span>
        <div class="personal-info">
        <?php $hasMetPlace = explode('|', $bindings->hasMetPlace->value); ?>
        <?php $hasMetPlaceName = explode('|', $bindings->hasMetPlaceName->value); ?>
            <?php for ($i = 0; $i < count($hasMetPlace); $i++): ?>
                <?php if ($i == 3) break; ?>
                <div><img src="/wp-content/themes/echoes/img/Assets/ico-man-pointer.svg">&nbsp;&nbsp;<a style="vertical-align: middle" href="<?php echo WP_SITEURL . $current_locale; ?>/agents?place=<?php echo $hasMetPlace[$i]; ?>"><?php echo $hasMetPlaceName[$i]; ?></a></div>
            <?php endfor; ?>
        </div><br>
    <?php endif; ?>
    <?php if (!empty($bindings->concepts->value)): ?>
        <span class="title_element" style="margin-top: 10px"><?php _e('Concepts', 'echoes_wp_sparql_plugin') ?>: </span>
        <div class="personal-info">
        <?php $concepts = explode('|', $bindings->conceptLabels->value); ?>
            <?php for ($i = 0; $i < count($concepts); $i++): ?>
                <?php if ($i == 3) break; ?>
                <?php $pos = strpos($concepts[$i], ':');
                      if ($pos === false) {
                         $concept = $concepts[$i];
                      } else {
                         $concept = end(explode(':', $concepts[$i]));
                      } ?>
                <div><img src="/wp-content/themes/echoes/img/Assets/ico-concepts-violet.svg">&nbsp;&nbsp;<a style="vertical-align: middle" href="<?php echo WP_SITEURL . $current_locale; ?>/agents?concept=Concept:<?= $bindings->conceptLabels->value ?>" ><?php echo $concept ?></a></div>
            <?php endfor; ?>
        </div><br>
    <?php endif; ?>
   <?php if (!empty($bindings->placeOfBirthName->value) || !empty($bindings->placeOfDeathName->value)): ?>
        <span class="title_element" style="margin-top: 10px"><?php _e('Places', 'echoes_wp_sparql_plugin') ?>:</span>
        <div class="personal-info">
	<?php $places = [];
	      $placesIds = [];
              if (!empty($bindings->placeOfBirthName->value)) {
	      	  $places[] = $bindings->placeOfBirthName->value;
		  $placesIds[] = $bindings->placeOfBirth->value;
	      }
	      if (!empty($bindings->placeOfDeathName->value)) {
		  $places[] = $bindings->placeOfDeathName->value;
		  $placesIds[] = $bindings->placeOfDeath->value;
	      } 
	      for ($i = 0; $i < count($places); $i++): ?> 
                <div><img src="/wp-content/themes/echoes/img/Assets/ico-map-violet.svg">&nbsp;&nbsp;<a style="vertical-align: middle" href="<?php echo WP_SITEURL . $current_locale; ?>/agents?place=<?= $placesIds[$i] ?>"><?php echo $places[$i] ?></a></div><br>
            <?php endfor; ?>
        </div><br>
    <?php endif; ?>
   <?php if (!empty($bindings->dateOfBirth->value) || !empty($bindings->dateOfDeath->value)): ?>
        <span class="title_element" style="margin-top: 10px"><?php _e('Dates', 'echoes_wp_sparql_plugin') ?>: </span>
        <div class="personal-info">
	<?php $dates = [];
	      if (!empty($bindings->dateOfBirth->value)) {
                  $dates[] = explode('-', $bindings->dateOfBirth->value)[0];
              }
              if (!empty($bindings->dateOfDeath->value)) {
                  $dates[] = explode('-', $bindings->dateOfDeath->value)[0];
              }
              for ($i = 0; $i < count($dates); $i++): ?>
                <div><img src="/wp-content/themes/echoes/img/Assets/ico-calendar-violet.svg">&nbsp;&nbsp;<a style="vertical-align: middle" href="<?php echo WP_SITEURL . $current_locale; ?>/timespans?year=<?= $dates[$i] ?>&type=agent"><?php echo $dates[$i]; ?></a></div><br>
            <?php endfor; ?>
        </div>
    <?php endif; ?>
	<div id="relatedLinks" class="container" style="padding-left: 0px;"></div>
</div>
<script>
	let nameAgent = '<?php echo $nameAgent ?>';
	function _showInfoWikipedia(nameAgent){
		let xhr = new XMLHttpRequest();
		let lang = ["en","ca","nl","fy"];
		let promises = [];
		for(let i = 0; i < lang.length; i++){
			let url = "https://" + lang[i] + ".wikipedia.org/w/api.php";
			let params = {
    				action: "query",
				srsort: "just_match",
    				list: "search",
    				srsearch: nameAgent,
    				format: "json",
				srlimit: 1
			};
			url = url + "?origin=*";
			let request = $.ajax({
	                        url: url,
                                type: 'GET',
                                data: params,
                                dataType: 'json',
				async: false
                        }).done(function(json) {
                                if(json.query.search.length > 0){
                                        let title = json.query.search[0].title
                                        let pageId = json.query.search[0].pageid;
                                        let wikipediUrl = "https://" + lang[i] + ".wikipedia.org/?curid=";
                                        let html = '<div class="personal-info col-3">';
                                        html += '<div><img src="/wp-content/themes/echoes/img/Assets/ico-wikipedia.png">    <a style="vertical-align: middle; color: #f31869; cursor: pointer" href=' + wikipediUrl + pageId + '>' + title + ' (' + lang[i] + ')</a></div>';
                                        html += '</div>';
					promises.push(html);
                                }
			});
		}

	        let url = "https://www.wikidata.org/w/api.php";
		nameAgent = (nameAgent.includes(",")) ? nameAgent.split(",")[1] + " " + nameAgent.split(",")[0] : nameAgent;
                let params = {
                	action: "wbsearchentities",
                        language: "en",
                        search: nameAgent,
                        format: "json"
                };
                url = url + "?origin=*";

                let request = $.ajax({
                        url: url,
                        type: 'GET',
                        data: params,
                        dataType: 'json',
                        async: false
                }).done(function(json) {
                        if(json.search.length > 0){
                        	let title = json.searchinfo.search;
                                json.search.forEach(function(info){
                                	let html = '<div class="personal-info col-3">';
					let desciption = (info['description'] !== undefined) ? ' - ' + info['description'] : '';
                                        html += '<div><img src="/wp-content/themes/echoes/img/Assets/ico-wikidata.png" style="width: 3em">    <a style="vertical-align: middle; color: #f31869; cursor: pointer" href=' + info['concepturi'] +'>' + info['label'] + desciption +'</a></div>';
                                        html += '</div>';
                                        promises.push(html);
                                        });
                        }
                let html = (promises.length > 0) ? '<span class="title_element" style="margin-top: 10px">Related Links: </span><div class="row">' : "";

                for(let i = 0; i < promises.length; i++){
                        html += promises[i];
                }
		html += '</div>';
                $('#relatedLinks').html(html);
	
                });
	}

	_showInfoWikipedia(nameAgent);
</script>
