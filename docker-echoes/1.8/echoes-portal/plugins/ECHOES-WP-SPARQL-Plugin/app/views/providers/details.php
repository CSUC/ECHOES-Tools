    <style type="text/css">
        #mynetwork {
            width: 100%;
            height: 500px;
        }
        .hr > .hr-inner:first-child {
            margin-left: 0px;
        }
        .title_element {
            font-weight: bold;
            font-size: 18px;
        }
        header {
            margin-bottom: 10px;
        }
        div.images > div:not(:first-child) a{
display: none;
}
div.images > div a{
position: inherit !important;
overflow: visible !important;
}

article + [data-readmore-toggle], article[data-readmore]{display: block; width: 100%;}article[data-readmore]{transition: height 100ms;overflow: hidden;}
    </style>
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
   <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <link rel="stylesheet" href="<?php echo mvc_css_url('echoes_wp_sparql_plugin', 'leaflet.css') ?>"
   integrity="sha512-Rksm5RenBEKSKFjgI3a41vrjkw4EVPlJ3+OiI65vTjIdo9brlAacEuKOiQ5OFh7cOI1bkDwLqdLw3Zg0cRJAAQ=="
   crossorigin=""/>
    <link href="<?php echo mvc_css_url('echoes_wp_sparql_plugin', 'font-awesome.min.css') ?>" rel="stylesheet">
    <link rel="stylesheet" href="<?php echo mvc_css_url('echoes_wp_sparql_plugin', 'vis-network.min.css') ?>" />
    <script src="<?php echo mvc_js_url('echoes_wp_sparql_plugin', 'vis-network.min.js') ?>"></script>
    <script src="<?php echo mvc_js_url('echoes_wp_sparql_plugin', 'readmore/readmore.min.js') ?>"></script>
    <div class="personal-info"> 
    <?php if (!empty($bindings->dcIdentifiers->value)): ?><span class="title_element"><?php _e('Identifier(s)', 'echoes_wp_sparql_plugin') ?>: </span> <?php echo strtoupper($bindings->dcIdentifiers->value); ?><br/><br/><?php endif; ?>
    <?php if (!empty($bindings->alternatives->value)): ?><span class="title_element"><?php _e('Alternative name(s)', 'echoes_wp_sparql_plugin') ?>: </span> <?php echo strtoupper($bindings->alternatives->value); ?><br/><br/><?php endif; ?>
<?php if (!empty($bindings->descriptions->value)): ?><article><span class="title_element"><?php _e('Description', 'echoes_wp_sparql_plugin') ?>: </span><span><?php echo ucfirst($bindings->descriptions->value); ?></span></article><br/><br/><?php endif; ?>
    <span class="title_element"><?php _e('Type', 'echoes_wp_sparql_plugin') ?>: </span> <?php echo str_replace('Concept:','',ucfirst($bindings->dcTypes->value)); ?><br/><br/>
    <?php if (!empty($bindings->languages->value)): ?><span class="title_element"><?php _e('Language', 'echoes_wp_sparql_plugin') ?>: </span> <?php echo strtoupper($bindings->languages->value); ?><br/><br/><?php endif; ?>
<?php if (!empty($bindings->dcCreators->value)): ?><span class="title_element"><?php _e('Creator', 'echoes_wp_sparql_plugin') ?>: </span><?php if (strpos($bindings->dcCreators->value, 'Agent:') === 0): ?><a href="<?php echo WP_SITEURL . $current_locale; ?>/agents/details/?subject=<?php echo $bindings->dcCreators->value; ?>"><?php echo $bindings->dcCreatorLabels->value; ?></a><?php else: ?><?php echo $bindings->dcCreatorLabels->value; ?><?php endif; ?><br/><br/><?php endif; ?>
<?php if (!empty($bindings->dcPublishers->value)): ?><span class="title_element"><?php _e('Publisher(s)', 'echoes_wp_sparql_plugin') ?>: </span><?php echo $bindings->dcPublishers->value; ?><br/><br/><?php endif; ?> 
    <?php if (!empty($bindings->dcFormats->value)): ?><span class="title_element"><?php _e('Format', 'echoes_wp_sparql_plugin') ?>: </span> <?php echo strtoupper($bindings->dcFormats->value); ?><br/><br/><?php endif; ?>
<?php if (!empty($bindings->dcRights->value)): ?><span class="title_element"><?php _e('Rights', 'echoes_wp_sparql_plugin') ?>: </span><?php if (strpos($bindings->dcRights->value, 'http') === 0): ?><a href="<?php echo $bindings->dcRights->value; ?>"><?php echo ucfirst($bindings->dcRights->value); ?></a><?php else: ?><?php echo $bindings->dcRights->value; ?><?php endif; ?><br/><br/><?php endif; ?>
<?php if (!empty($bindings->dcSources->value)): ?><span class="title_element"><?php _e('Source', 'echoes_wp_sparql_plugin') ?>: </span><?php if (strpos($bindings->dcSources->value, 'http') === 0): ?> <a href="<?php echo $bindings->dcSources->value; ?>"><?php echo $bindings->dcSources->value; ?></a><?php else: ?><?php echo $bindings->dcSources->value; ?><?php endif; ?><br/><br/><?php endif; ?> 
    <?php if (count($showns) > 0 && !empty($showns[0])): $images = true ?>
        <?php foreach ($showns as $shown): ?>
		<div style="width: 10em;">
                	<a data-fancybox="gallery" href="<?php echo $shown ?>"><img src="<?php echo $shown ?>"></a>
		</div>
        <?php endforeach; ?>
        <?php if (count($showns) > 1): ?>
            <div style="margin-top: 20px; text-align: center"><?php _e('Number of images', 'echoes_wp_sparql_plugin') ?>: <?php echo count($showns); ?><br></div>
        <?php endif; ?>
<?php else: $images = false; endif; ?>
 <hr/>

<div style="width: 100%; float: left; margin-top: 20px; margin-bottom: 20px; float">
    <?php if (isset($graph)): ?>
        <script type="text/javascript">
            let nodes = <?php echo $graph['nodes'] ?>;
            let edges = <?php echo $graph['edges'] ?>;
        </script>
        <br/>
        <span class="title_element"><?php _e('Relations', 'echoes_wp_sparql_plugin') ?>: </span><br/><br/>
        <div id="mynetwork" style="background-color: white"></div>
        <script src="<?php echo mvc_js_url('echoes_wp_sparql_plugin', 'screenfull.min.js') ?>"></script>
        <button class="go-full-screen-button" onclick="javascript:if(screenfull.enabled) screenfull.request(document.getElementById('mynetwork'));"><?php _e('Go fullscreen', 'echoes_wp_sparql_plugin') ?></button>
        <script src="<?php echo mvc_js_url('echoes_wp_sparql_plugin', 'providedCHO_details') ?>"></script>
    <?php endif; ?>
</div>
<div class="leaflet-container" style="display: none"><div class="leaflet-pane leaflet-popup-pane"><div class="leaflet-popup  leaflet-zoom-animated" style="opacity: 1;"><div class="leaflet-popup-content-wrapper"><div class="leaflet-popup-content" style="width: 232px;"></div></div><div style="margin-top: -1px" class="leaflet-popup-tip-container"><div class="leaflet-popup-tip"></div></div><a class="leaflet-popup-close-button" href="#close">x</a></div></div></div>
    <div class="personal-info">

   <?php if (!empty($bindings->placeIds->value)): ?>
        <span class="title_element" style="margin-top: 10px"><?php _e('Places', 'echoes_wp_sparql_plugin') ?>: </span>
        <div class="personal-info">
        <?php $places = explode('|', $bindings->placeIds->value); 
	      $placesLabels = explode('|', $bindings->placePrefLabels->value); ?>
            <?php for ($i = 0; $i < count($places); $i++): ?>
                <div><img src="/wp-content/themes/echoes/img/Assets/ico-map-violet.svg">&nbsp;&nbsp;<a style="vertical-align: middle" href="<?php echo WP_SITEURL . $current_locale; ?>/providers?place=<?php echo urlencode($places[$i]) ?>"><?php echo $placesLabels[$i]; ?></a></div>
            <?php endfor; ?>
        </div><br>
    <?php endif; ?>
   <?php if (!empty($bindings->timeSpans->value)): ?>
        <span class="title_element" style="margin-top: 10px"><?php _e('Dates', 'echoes_wp_sparql_plugin') ?>: </span>
        <div class="personal-info">
        <?php $dates = explode('|', $bindings->timeSpans->value); ?>
            <?php for ($i = 0; $i < count($dates); $i++): ?>
                <div><img src="/wp-content/themes/echoes/img/Assets/ico-calendar-violet.svg">&nbsp;&nbsp;<a style="vertical-align: middle" href="<?php echo WP_SITEURL . $current_locale; ?>/timespans?year=<?= $dates[$i] ?>&type=providedCHO"><?php echo $dates[$i]; ?></a></div><br>
            <?php endfor; ?>
        </div><br>
    <?php endif; ?>
    <?php if (!empty($bindings->conceptPrefLabels->value)): ?>
        <span class="title_element" style="margin-top: 10px"><?php _e('Concepts', 'echoes_wp_sparql_plugin') ?>: </span>
        <div class="personal-info">
        <?php $concepts = explode('|', $bindings->conceptPrefLabels->value); 
	      $conceptsIds = explode('|', $bindings->conceptIds->value); ?>
            <?php for ($i = 0; $i < count($concepts); $i++): ?>
                <?php if ($i == 3) break; ?>
                <div><img src="/wp-content/themes/echoes/img/Assets/ico-concepts-violet.svg">&nbsp;&nbsp;<a style="vertical-align: middle" href="<?php echo WP_SITEURL . $current_locale; ?>/providers?concept=<?= $conceptsIds[$i] ?>" ><?php echo $concepts[$i]; ?></a></div>
            <?php endfor; ?>
        </div><br>
    <?php endif; ?>
    <?php $graphValues = json_decode($graph['nodes']); ?>
    <?php if (count($graphValues[0]->group) > 0): 
		$index = 0; ?>
         <?php for ($i = 0; $i < count($graphValues); $i++) {
                $value = $graphValues[$i]->group;
                if($value == 'Vrouw' || $value == 'Onbekend' || $value == 'Man') { ?>
			<?php if ($index == 0) { ?> <span class="title_element" style="margin-top: 10px"><?php _e('People', 'echoes_wp_sparql_plugin') ?>: </span> <?php $index++; } ?>
                        <div class="personal-info">
                        <div><img src="/wp-content/themes/echoes/img/Assets/ico-people-violet.svg">&nbsp;&nbsp;<a style="vertical-align: middle" href="<?php echo WP_SITEURL . $current_locale; ?>/agents/details/?subject=<?= $graphValues[$i]->id ?>"><?= $graphValues[$i]->label ?></a></div>  
                        </div>
                <?php }
        } ?>
        <br>
    <?php endif; ?>
    <div id="relatedLinks" class="container" style="padding-left: 0px;"></div>
</div>
</div>
<script>
	$('article').readmore({
  		speed: 75,
		moreLink: '<a href="#">[+]</a>',
  		lessLink: '<a href="#">[-]</a>',
  		collapsedHeight: 100
	});
        let nameDO = "<?php echo $bindings->titles->value ?>";
	let provided = "<?php echo $provided ?>";
        function _showInfoWikipedia(nameDO){
                let xhr = new XMLHttpRequest();
                let lang = ["en","ca","nl","fy"];
                let promises = [];
		let html = "";
                for(let i = 0; i < lang.length; i++){
                        let url = "https://" + lang[i] + ".wikipedia.org/w/api.php";
                        let params = {
                                action: "query",
				srsort: "just_match",
                                list: "search",
                                srsearch: nameDO,
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
                let params = {
                        action: "wbsearchentities",
                        language: "en",
                        search: nameDO,
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

                html = '<span class="title_element" style="margin-top: 10px">Related Links: </span><div class="row">';

                for(let i = 0; i < promises.length; i++){
                        html += promises[i];
                }
        
                });

                $.ajax({
                        url: '/providers/getAnnotations',
                        type: 'GET',
                        data: {provided: provided},
                        dataType: 'json',
                        async: false,
                        success: function(json) {
                                for(let i = 0; i < json.length; i++){
					html = (html === "") ? '<span class="title_element" style="margin-top: 10px">Related Links: </span><div class="row">' : html; 
                                        html += json[i];
                                }
                                html += (html === "") ? "" : '</div>';
                                $('#relatedLinks').html(html);
                        }
                });

        }

        _showInfoWikipedia(nameDO);

</script>
