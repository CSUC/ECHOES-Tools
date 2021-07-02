if (window.location.href.indexOf('/ca/') !== -1) {
	var translations = {
                'places_label': "Llocs",
                'people_label': "Persones", 
                'related_people': "persona relacionada",
                'related_people_pl': "persones relacionades"
	}
} else if (window.location.href.indexOf('/nl/') !== -1) {
	var translations = {
		'places_label': "Plaatsen",
                'people_label': "Mensen", 
                'related_people': "gerelateerde mensen",
                'related_people_pl': "gerelateerde mensen"
	}
} else { 
	var translations = {
		'places_label': "Places",
                'people_label': "People", 
                'related_people': "related person",
                'related_people_pl': "related people"
        }
}  


function createMap(place)
{
//var mymap = L.map('mapid').setView([52.15833, 4.49306], 7);

var mymap = L.map('mapid',{
    zoomAnimation: false,
    fullscreenControl: {
        pseudoFullscreen: false
    }
});

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="http://openstreetmap.org/copyright">OpenStreetMap</a> contributors - <a href="http://csuc.cat/">CSUC</a>',
        maxZoom: 19.5
}).addTo(mymap);

mymap.fitWorld().zoomIn();

function buildPopUp(feature) {
	var html = '<div style="font-size: 1.25rem;">' + translations['places_label'] + ': <strong>' + feature.properties.label + '</strong></div>' ;
        if(feature.properties.type){
                let uri = WP_SITEURL + '/places/relations/?place_name=' +  feature.properties.label + '&subjects=Place%3A' + feature.properties.label + '&type=agent&page=1';
                if (feature.properties.type["<http://www.europeana.eu/schemas/edm/Agent>"] == 1) {
                        html += translations['people_label'] + ': ' + '<a href="' + uri + '">' + feature.properties.type["<http://www.europeana.eu/schemas/edm/Agent>"] + ' ' + translations['related_people'] + '</a><br>'
                } else {
                        html += translations['people_label'] + ': ' + '<a href="' + uri + '">' + feature.properties.type["<http://www.europeana.eu/schemas/edm/Agent>"] + ' ' + translations['related_people_pl'] + '</a><br>'
                }
        }
    	return html;
}

function buildPopUpGene(feature) {
        var html = '<strong>' + feature.properties.title+ '</strong><br><br>' ;
        if(feature.properties.label){
                html += feature.properties.label + '<br>'
        }
        if(feature.properties.comarca){
                html += feature.properties.comarca + '<br>'
        }
        if(feature.properties.showby[0]){
                html += '<br><img src="'+feature.properties.showby[0]+'" style="width: 200px;display: block;margin-left: auto;margin-right: auto;width: 50%;"></img><br>';
        }
        if(feature.properties.related){
                var ref = 'https://calaix.gencat.cat/handle'+feature.properties.related.substring(21,feature.properties.related.length);
                html += 'Veure a: <a href="'+ref+'">'+ ref +'</a><br><br>';
        }
        if(feature.properties.description.length > 0){
                html += '<br><strong>Description</strong><br>';
                for(i = 0 ; i < feature.properties.description.length; i++){
                        if(i == 3){
                                break;
                        }
                        html += (feature.properties.description[i].length > 200) ? feature.properties.description[i].substring(0, 200) + '...' + '<br><br>' : feature.properties.description[i] +'<br><br>';
                }
        }
        if(feature.properties.typeCount){
            typeCountHtml = "<strong>Tipus relacionats</strong><br>"
                var types = feature.properties.typeCount;
                var i = 0;
                Object.keys(types).forEach(key=>{
                        var typeArr = key.split("/");
                        var type = typeArr[typeArr.length - 1];
                        var typeCount;

                        if(type == 'ProvidedCHO') {
                                typeCount = 'Digital Object '+ types[key];

                                var linkTypeCountHtml = '<a href="' + WP_SITEURL + '/providers/details/?subject='+ feature.id + '">'+ typeCount+'</a><br>';
                                typeCountHtml += linkTypeCountHtml;
                        }
                });

                html += typeCountHtml;

        }

        return html;
}


function onEachFeature(feature, layer) {
	if (feature.properties) {
		var html = buildPopUp(feature);
		layer.bindPopup(html);
	}
}

function onEachFeatureGene(feature, layer) {
    if (feature.properties) {
	var html = buildPopUpGene(feature);
        layer.bindPopup(html);
    }

}


var smallIconOrange = new L.Icon({

                        iconSize: [48, 48],
                        iconAnchor: [13, 27],
                        popupAnchor:  [1, -24],
                        iconUrl: 'https://echoes.community/wp-content/plugins/echoes_wp_sparql_plugin/app/public/js/mapa/markers/icons8-marker-filled-48_orange.png'

                });

var smallIconBlue = new L.Icon({

                        iconSize: [48, 48],
                        iconAnchor: [13, 27],
                        popupAnchor:  [1, -24],
                        iconUrl: 'https://echoes.community/wp-content/plugins/echoes_wp_sparql_plugin/app/public/js/mapa/markers/icons8-marker-filled-48_blue.png'

                });

var smallIconPurple = new L.Icon({

                        iconSize: [48, 48],
                        iconAnchor: [13, 27],
                        popupAnchor:  [1, -24],
                        iconUrl: 'https://echoes.community/wp-content/plugins/echoes_wp_sparql_plugin/app/public/js/mapa/markers/icons8-marker-filled-48_purple.png'

                });

geoLayer = L.geoJSON(info, {
    filter: function(feature, layer) {
	return (feature.properties.vg[0] == '<vg:tresoar>');
    },
    pointToLayer: function(feature, latlng) {
		feature.properties.myKey = feature.properties.label;
                return L.marker(latlng, {icon: smallIconOrange});
            },
    onEachFeature: onEachFeature
});

geoLayer2 = L.geoJSON(info, {
    filter: function(feature, layer) {
	if (feature.properties.vg.length == 1) {
		return (feature.properties.vg[0] == '<vg:erfgoed>');
	} else {
		return (feature.properties.vg[1] == '<vg:erfgoed>');
	}
    },
    pointToLayer: function(feature, latlng) {
		feature.properties.myKey = feature.properties.label;
                return L.marker(latlng, {icon: smallIconBlue});
            },
    onEachFeature: onEachFeature
});
/**
geoLayer3 = L.geoJSON(infoGene, {
    pointToLayer: function(feature, latlng) {
		feature.properties.myKey = feature.properties.title + ', ' + feature.properties.label;
                return L.marker(latlng, {icon: smallIconPurple});
            },
    onEachFeature: onEachFeatureGene
});
**/

geoLayer3 = L.geoJSON(infoGene, {
    filter: function(feature, layer) {
        if (feature.properties.showby.length == 0) {
                return (feature.id.includes("Arque") || feature.id.includes("Arqui"));
        }else if (feature.properties.showby.length != 0) {
		return (feature.id.includes("Arque") || feature.id.includes("Arqui"));
        }
    },
    pointToLayer: function(feature, latlng) {
                feature.properties.myKey = feature.properties.title + ', ' + feature.properties.label;
                return L.marker(latlng, {icon: smallIconPurple});
            },
    onEachFeature: onEachFeatureGene
});

var layers = {
        "Tresoar" : geoLayer,
	"Erfgoed" : geoLayer2,
	"Generalitat" : geoLayer3
}

//var control = L.control.layers(null,diba)
var control = L.control.layers(null,layers);
var mcgLayerSupportGroup = L.markerClusterGroup.layerSupport();

mcgLayerSupportGroup.checkIn([geoLayer, geoLayer2, geoLayer3]);

control.addTo(mymap);
mcgLayerSupportGroup.addTo(mymap);

var fuseOptions = {
  shouldSort: true,
  tokenize: true,
  matchAllTokens: true,
  threshold: 0.0,
  location: 0,
  maxPatternLength: 32,
  minMatchCharLength: 2,
  keys: ['properties.myKey']
};

var fuse = new Fuse(info.features.concat(infoGene.features), fuseOptions);
	
var searchControl = new L.Control.Search({
       layer: L.featureGroup([geoLayer, geoLayer2, geoLayer3]),
       textPlaceholder:'Search by place or title',
       propertyName: 'myKey',
       circleLocation: true,
       moveToLocation: function(latlng, title, map) {
                var zoom = 15;
                map.setView(latlng, zoom); // access the zoom
       },
       filterData: function(text, records) {
                var ret = {}, key;
                var jsons = fuse.search(text);
                for(var i in jsons) {
                        key = jsons[i].properties.myKey;
                        ret[key]= records[key];
                }
                return ret;
        }
});

searchControl.on('search:locationfound', function(e) {
	if(e.layer._popup)
		e.layer.openPopup();
});

mymap.addControl(searchControl);

if (place !== '') {
	for (var key in info['features']) {
        	var obj = info['features'][key];
		place = place.replace(/^./, place[0].toUpperCase());
		if (obj['properties']['label'] == place) {
			mymap.setView([obj['geometry']['coordinates'][1], obj['geometry']['coordinates'][0]], 7);
			var html = buildPopUp(obj);
			var marker = L.marker([obj['geometry']['coordinates'][1], obj['geometry']['coordinates'][0]], {
        				       smallIconOrange
      				}).addTo(mymap);

			marker.bindPopup(html).openPopup();
		}
	}

	for (var key in infoGene['features']) {
                var obj = infoGene['features'][key];
		place = place.toUpperCase();
                if (obj['properties']['label'] == place) {
                        var html = buildPopUpGene(obj);
                        var marker = L.marker([obj['geometry']['coordinates'][1], obj['geometry']['coordinates'][0]], {
                                               smallIconOrange
                                }).addTo(mymap);

                        marker.bindPopup(html).openPopup();
                }
        }	
}
}

/**
 *  * @param String name
 *   * @return String
 *    */
function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
    results = regex.exec(location.search);
    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

window.onload = function(){
 	var height =  "600px";
        document.getElementById("mapid").style.height = height;
	var place = getParameterByName('place');
        createMap(place);
}
