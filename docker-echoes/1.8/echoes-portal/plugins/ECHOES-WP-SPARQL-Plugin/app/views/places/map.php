<!DOCTYPE html>
<html>
<head>
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

	<title>Echoes Map</title>
	<script src="https://unpkg.com/leaflet@1.0.2/dist/leaflet.js" type="text/javascript" ></script>
	<link rel="stylesheet" href="https://unpkg.com/leaflet@1.0.2/dist/leaflet.css" />
	<!--
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">	
	<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
	-->
	<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
	<script src='https://api.mapbox.com/mapbox.js/plugins/leaflet-fullscreen/v1.0.1/Leaflet.fullscreen.min.js'></script>
	<link href='https://api.mapbox.com/mapbox.js/plugins/leaflet-fullscreen/v1.0.1/leaflet.fullscreen.css' rel='stylesheet' />
	
	<script src="<?php echo mvc_js_url('echoes_wp_sparql_plugin', 'mapa/Wicket/wicket.js') ?>" type="text/javascript"></script>
	<script src="<?php echo mvc_js_url('echoes_wp_sparql_plugin', 'mapa/Wicket/wicket-leaflet.js') ?>" type="text/javascript"></script>

        <script src="<?php echo mvc_js_url('echoes_wp_sparql_plugin', 'mapa/Leaflet.draw/dist/leaflet.draw.js') ?>" type="text/javascript"></script>
        <link rel="stylesheet" type="text/css" href="<?php echo mvc_js_url('echoes_wp_sparql_plugin', 'mapa/Leaflet.draw/dist/leaflet.draw.css') ?>">

	<link rel="stylesheet" type="text/css" href="<?php echo mvc_js_url('echoes_wp_sparql_plugin', 'mapa/Leaflet.markercluster/dist/MarkerCluster.css') ?>">
<link rel="stylesheet" type="text/css" href="<?php echo mvc_js_url('echoes_wp_sparql_plugin', 'mapa/Leaflet.markercluster/dist/MarkerCluster.Default.css') ?>">
	<script src="<?php echo mvc_js_url('echoes_wp_sparql_plugin', 'mapa/Leaflet.markercluster/dist/leaflet.markercluster.js') ?>" type="text/javascript"></script>
        <script src="<?php echo mvc_js_url('echoes_wp_sparql_plugin', 'mapa/leaflet-search-master/src/leaflet-search.js') ?>" type="text/javascript">></script>
        <script src="https://unpkg.com/leaflet.markercluster.layersupport@2.0.1/dist/leaflet.markercluster.layersupport.js"></script>
	<link rel="stylesheet" type="text/css" href="<?php echo mvc_js_url('echoes_wp_sparql_plugin', 'mapa/leaflet-search-master/src/leaflet-search.css') ?>" />
	<link rel="stylesheet" type="text/css" href="<?php echo mvc_js_url('echoes_wp_sparql_plugin', 'mapa/Leaflet.fullscreen-gh-pages/dist/leaflet.fullscreen.css') ?>" />

	<style>
		#map { 
			width: 100%;
			height: 580px;
			box-shadow: 5px 5px 5px #888;
		}
	</style>
</head>

<body>

	<div class="container-fluid">
		<div class="row">
			<div class="col-12">
				<div id="map"></div>
			</div>
		</div>
	</div>

	<script>

		/**
 		 @param String name
 		 @return String
		**/
		function getParameterByName(name) {
    			name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    			var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
    			results = regex.exec(location.search);
    			return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
		}
		let place = "<?php echo $place ?>";
		console.log(place)
		//let place = getParameterByName('place');

		var map = L.map('map',{
			fullscreenControl: true
		}).setView([51.505, -0.09], 2);

		L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
			attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
		}).addTo(map);
		
		var drawnItems = new L.FeatureGroup();
		map.addLayer(drawnItems);
		var drawControl = new L.Control.Draw({
			draw :{
				polyline: false,
				circlemarker: false
			},    		
			edit: {
				featureGroup: drawnItems
			}
		});

		map.addControl(drawControl);
		let featureGroup = new L.markerClusterGroup();
		let control;
		function searchByAjax(text, callResponse)
        		{
                		return $.ajax({
                        			url: '/places/getCityOnMap',
                        			type: 'GET',
                        			data: {q: text},
                        			dataType: 'json',
                        			success: function(json) {
                                			callResponse(json);
                        			}
                		});
        		}

		map.addControl( new L.Control.Search({sourceData: searchByAjax, textPlaceholder:'Place...', markerLocation: true,marker: {circle: {radius: 20,color: 'blue',opacity: 1}, icon:false}}));
	
		//let query = (place === "") ? '{"size":2467,"query":{"bool":{"must":{"match_all":{}},"filter":{"geo_distance":{"distance":"50km","http://www.opengis.net/def/wktLiteral.@value.geo_point":{"lat":42.162392,"lon":1.092892}}}}},"aggs":{"graphs":{"terms":{"field":"@graph"}}}}' : '{"query":{"bool":{"must":[{"match":{"http://www.w3.org/2004/02/skos/core#prefLabel.@value":{"query":"' + place + '"}}},{"match":{"@type": "http://www.europeana.eu/schemas/edm/Place"}},{"exists":{"field":"http://www.opengis.net/def/wktLiteral.@value"}}]}}}';	
		let query = ""

		if (place.includes(":")) {
                        query = '{"size":5000,"query":{"bool":{"must":{"match_all":{}},"filter":{"geo_distance":{"distance":"5km","http://www.opengis.net/def/wktLiteral.@value.geo_point":{"lat":' + place.split(":")[0] + ',"lon":' + place.split(":")[1] + '}}}}},"aggs":{"graphs":{"terms":{"field":"@graph"}}}}'
		} else {
                        query = '{"size":5000,"query":{"bool":{"must":{"match_all":{}},"filter":{"geo_distance":{"distance":"2500km","http://www.opengis.net/def/wktLiteral.@value.geo_point":{"lat":58.525000,"lon":12.040000}}}}},"aggs":{"graphs":{"terms":{"field":"@graph"}}}}'
		}

		_search(query);

		function _search(query){
			 $.ajax({
	            type: 'post',
	            url: '/places/getGeoSearch',
	            data: query,
	            contentType: "application/json; charset=utf-8",
	            traditional: true,
	            success: function (text) {
        		        let gene = new L.layerGroup(),
                    		    erfgoed = new L.layerGroup(),
                    		    tresoar = new L.layerGroup();

				if(text.aggregations){
					var graph = document.getElementById('graph');
					
					text.aggregations.graphs.buckets.forEach(function(g){
						
						if(document.getElementById(g.key))	document.getElementById(g.key).textContent = parseInt(document.getElementById(g.key).textContent) + parseInt(g.doc_count);
					});
				}
				
				text.hits.hits.forEach(function(item){
					if(item._source['http://www.opengis.net/def/wktLiteral']){
					let index = (Array.isArray(item._source['http://www.opengis.net/def/wktLiteral'])) ? item._source['http://www.opengis.net/def/wktLiteral'].length : 1;
					for(let i = 0; i < index; i++) {
						var wkt = new Wkt.Wkt();
						let itemSource = (Array.isArray(item._source['http://www.opengis.net/def/wktLiteral'])) ? item._source['http://www.opengis.net/def/wktLiteral'][index - 1]['@value'] : item._source['http://www.opengis.net/def/wktLiteral']['@value'];
						wkt.read(itemSource);
						let selectedLayer;
						if(item['_source']['@graph'] === 'vg:tresoar') {
							selectedLayer = tresoar;
						} else if(item['_source']['@graph'] === 'vg:erfgoed') {
							selectedLayer = erfgoed;
						} else {
							selectedLayer = gene;
						}
						var marker = 
							L.marker(wkt.toObject()._latlng)
							.bindPopup("<p style='width: 10em;text-align: center;'>"+item._source['http://www.w3.org/2004/02/skos/core#prefLabel']['@value']+"</p>", 
								{"@id": item._source['@id'], className: item._source['@id'], maxWidth: "auto"})
							.addTo(featureGroup)
							.addTo(selectedLayer)
							.on('popupopen', function (popup) {
						        let id = popup.popup.options["@id"];

						        fetch('/places/getGeoSearch', {
									body: '{"query":{"multi_match":{"fields":["*.@id.keyword"],"query":"' + id + '"}},"aggs":{"relations":{"terms":{"field":"@type"}},"graphs":{"terms":{"field":"@graph"}}},"size":0}',
									method: "POST",
									headers: {
										'Content-Type': 'application/json'
									}
								})
								.then(function(response) {
									return response.json();
								})
								.then(function(text) {
									var content = document.getElementsByClassName(id)[0].getElementsByClassName("leaflet-popup-content")[0]; 

									text.aggregations.relations.buckets.forEach(function(relation){
										let url = relation.key.split("/").pop().toLowerCase();
										url = (url == 'providedcho') ? 'providers' : url;
										url = (url == 'agent') ? 'agents' : url;
										var current_locale = "<?php echo $current_locale ?>"; 
										var agents_label = "<?php _e('People','echoes_wp_sparql_plugin') ?>";
										var cho_label = "<?php _e('Digital Objects', 'echoes_wp_sparql_plugin') ?>";
										var aggregation_label = "<?php _e('Aggregation', 'echoes_wp_sparql_plugin') ?>";
										var label_content = relation.key.split("/").pop();
										var label_def = "";
										if (label_content == "Agent") {
											label_def = agents_label; 
										} else if (label_content == "ProvidedCHO") {
											label_def = cho_label; 
										} else if (label_content == "Aggregation") {
											label_def = aggregation_label; 
										} else {
											label_def = label_content;
										}
										content.insertAdjacentHTML('beforeend',
										 "<p style='width: 10em;text-align: center;'><a href='" + current_locale + "/" + url + "?place=" + encodeURIComponent(id) + "'><strong>  " + label_def + ": </strong><span class='badge badge-pill badge-primary'>"+relation.doc_count+"</span></a></p>");
									});
								});
						    });
					}
					}
				});
				var layers = {
        					"Tresoar" : tresoar,
        					"Erfgoed" : erfgoed,
        					"Generalitat" : gene
				}
				if (control !== undefined) {
					control.removeLayer(tresoar);
					control.removeLayer(erfgoed,);
					control.removeLayer(gene);
					map.removeControl(control);
				}
				control = L.control.layers(null,layers,{collapsed:false});
				let mcgLayerSupportGroup = L.markerClusterGroup.layerSupport();
				mcgLayerSupportGroup.checkIn([tresoar, erfgoed, gene]);

				control.addTo(map);
				mcgLayerSupportGroup.addTo(map);
				//map.addLayer(featureGroup);
				map.fitBounds(featureGroup.getBounds());
				gene.addTo(map);
                                erfgoed.addTo(map);
                                tresoar.addTo(map);

				map.on(L.Draw.Event.CREATED, function (e) {
					var type = e.layerType
					var layer = e.layer;

					drawnItems.addLayer(layer);

					if (layer instanceof L.Circle) {
						var theCenterPt = layer.getLatLng();
						var theRadius = layer.getRadius() / 1000;

						_search(	
							'{"size":2467,"query":{"bool":{"must":{"match_all":{}},"filter":{"geo_shape":{"http://www.opengis.net/def/wktLiteral.@value.geo_shape":{"shape":{"type":"circle","coordinates":[' + theCenterPt.lng + ',' + theCenterPt.lat + '],"radius":"' + theRadius + 'km"},"relation":"within"}}}}},"aggs":{"graphs":{"terms":{"field":"@graph"}}}}'
							);
					}

					if (layer instanceof L.Marker) {
						var theCenterPt = layer.getLatLng();

						_search(
							'{"size":2467,"query":{"bool":{"must":{"match_all":{}},"filter":{"geo_distance":{"distance":"200km","http://www.opengis.net/def/wktLiteral.@value.geo_point":{"lat":' + theCenterPt.lat + ',"lon":' + theCenterPt.lng + '}}}}},"aggs":{"graphs":{"terms":{"field":"@graph"}}}}'		
							)
					}


					if ((layer instanceof L.Polygon) && ! (layer instanceof L.Rectangle)) {
						var geo = layer.toGeoJSON();

						_search(
							'{"size":2467,"query":{"bool":{"must":{"match_all":{}},"filter":{"geo_shape":{"http://www.opengis.net/def/wktLiteral.@value.geo_shape":{"shape":' + JSON.stringify(geo.geometry) + ',"relation":"within"}}}}},"aggs":{"graphs":{"terms":{"field":"@graph"}}}}'
							);
					}

					if (layer instanceof L.Rectangle) {
						var geo = layer.toGeoJSON();

						_search(
							'{"size":2467,"query":{"bool":{"must":{"match_all":{}},"filter":{"geo_shape":{"http://www.opengis.net/def/wktLiteral.@value.geo_shape":{"shape":' + JSON.stringify(geo.geometry) + ',"relation":"within"}}}}},"aggs":{"graphs":{"terms":{"field":"@graph"}}}}'
							);
					}
				});

				map.on(L.Draw.Event.EDITED, function (e) {
					var layers = e.layers;

					layers.eachLayer(function (layer) {
						if (layer instanceof L.Circle) {
							var theCenterPt = layer.getLatLng();
							var theRadius = layer.getRadius() / 1000;

							_search(	
								'{"size":2467,"query":{"bool":{"must":{"match_all":{}},"filter":{"geo_shape":{"http://www.opengis.net/def/wktLiteral.@value.geo_shape":{"shape":{"type":"circle","coordinates":[' + theCenterPt.lng + ',' + theCenterPt.lat + '],"radius":"' + theRadius + 'km"},"relation":"within"}}}}},"aggs":{"graphs":{"terms":{"field":"@graph"}}}}'
								);
						}

						if (layer instanceof L.Marker) {
							var theCenterPt = layer.getLatLng();

							_search(
								'{"size":2467,"query":{"bool":{"must":{"match_all":{}},"filter":{"geo_distance":{"distance":"200km","http://www.opengis.net/def/wktLiteral.@value.geo_point":{"lat":' + theCenterPt.lat + ',"lon":' + theCenterPt.lng + '}}}}},"aggs":{"graphs":{"terms":{"field":"@graph"}}}}'		
								)
						}


						if ((layer instanceof L.Polygon) && ! (layer instanceof L.Rectangle)) {
							var geo = layer.toGeoJSON();

							_search(
								'{"size":2467,"query":{"bool":{"must":{"match_all":{}},"filter":{"geo_shape":{"http://www.opengis.net/def/wktLiteral.@value.geo_shape":{"shape":' + JSON.stringify(geo.geometry) + ',"relation":"within"}}}}},"aggs":{"graphs":{"terms":{"field":"@graph"}}}}'
								);
						}

						if (layer instanceof L.Rectangle) {
							var geo = layer.toGeoJSON();

							_search(
								'{"size":2467,"query":{"bool":{"must":{"match_all":{}},"filter":{"geo_shape":{"http://www.opengis.net/def/wktLiteral.@value.geo_shape":{"shape":' + JSON.stringify(geo.geometry) + ',"relation":"within"}}}}},"aggs":{"graphs":{"terms":{"field":"@graph"}}}}'
								);
						}
					});
				});
	            }
	        });

}
</script>
</body>
</html>

