let gender_to_icon = {
    'Man': 'ico-man',
    'Vrouw': 'ico-woman',
    'Onbekend': 'ico-people-violet-big'
}
let network = null;
let networkCanvas = null;
let data = {
    nodes: nodes,
    edges: edges
};
            jQuery(document).ready(function(){
                var container = document.getElementById('mynetwork');
            
            
            var options = {
                interaction:{
                    zoomView: false,
    dragView: true,
    dragNodes: true,
    hover: true
  },

                groups: {
                    center: {
                                shape: 'circularImage',
                                size:  31,
                		shapeProperties: {
                    			useImageSize: true
                		},
                                color: {
                                        background: '#ffffff',
                                        border: '#ffffff'
                                       },
                                image: WP_SITEURL + '/wp-content/themes/echoes/img/Assets/ico-digital-objets-violet-big.svg',
                                font: {strokeWidth: 3, strokeColor: 'white'}

                    },
                    centerCHO: {
                                shape: 'circularImage',
				size:  30,
                                shapeProperties: {
                                        useImageSize: true
                                },
                                color: {
			        	background: '#ffffff',
					border: '#ffffff'
				       },
                                image: WP_SITEURL + '/wp-content/themes/echoes/img/Assets/' + gender_to_icon['Onbekend'] + '.svg',
				font: {strokeWidth: 3, strokeColor: 'white'}
			       },
                    Onbekend: {
            shape: 'icon',
            color: '#26298e',
            icon: {
              face: 'FontAwesome',
              code: '\uf007',
              size: 50,
              color: '#26298e',
              
            },
            
            font: {strokeWidth: 3, strokeColor: 'white'}
        },
        Vrouw: {
            shape: 'icon',
            color: '#26298e',
            icon: {
              face: 'FontAwesome',
              code: '\uf182',
              size: 50,
              color: '#26298e',
              
            },
            
            font: {strokeWidth: 3, strokeColor: 'white'}
        },
        Man: {
            shape: 'icon',
            color: '#26298e',
            icon: {
              face: 'FontAwesome',
              code: '\uf183',
              size: 50,
              color: '#26298e',
              
            },
            
            font: {strokeWidth: 3, strokeColor: 'white'}
        },
            places: {
                shape: 'image',
                color: '#26298e',
                shapeProperties: {
                    useImageSize: false
                },
                labelHighlightBold: false,
                image: WP_SITEURL + '/wp-content/themes/echoes/img/Assets/ico-map-violet.svg',
            font: {strokeWidth: 3, strokeColor: 'white'} 
            },
            digitalObjects: {
                shape: 'image',
                color: '#26298e',
                shapeProperties: {
                    useImageSize: false
                },
                labelHighlightBold: false,
                image: WP_SITEURL + '/wp-content/themes/echoes/img/Assets/ico-digital-objets-violet.svg',
            font: {strokeWidth: 3, strokeColor: 'white'}
            },
            timespans: {
                shape: 'image',
                color: '#26298e',
                shapeProperties: {
                    useImageSize: false
                },
                labelHighlightBold: false,
		image: WP_SITEURL + '/wp-content/themes/echoes/img/Assets/ico-calendar-violet.svg',
            font: {strokeWidth: 3, strokeColor: 'white'} 
        },
        historicalNews: {
            shape: 'icon',
            color: '#26298e',
            icon: {
                face: 'FontAwesome',
                code: '\uf1ea',
                size: 50,
                color: '#26298e'
            }
        },
        concepts: {
                shape: 'image',
                color: '#26298e',
                shapeProperties: {
                    useImageSize: false
                },
                labelHighlightBold: false,
                image: WP_SITEURL + '/wp-content/themes/echoes/img/Assets/ico-concepts-violet.svg',
            font: {strokeWidth: 3, strokeColor: 'white'} 
        }

            
          
                },
                physics: {
                    barnesHut: {
                        gravitationalConstant: -10000
                    }
                },
                nodes: {
                    borderWidth: 1,
                    shape: 'dot',
                    font: {
                            multi: 'html'
                        }
                },
                edges: {
                    arrows: {
                        to: {
                            type: 'circle'
                        }
                    },
                    width: 1,
                    font: {
                        'size': 12,
                        strokeWidth: 3, 
                        strokeColor: 'white'
                    },
                    arrowStrikethrough: false,
                    shadow: true
                },
            };
            network = new vis.Network(container, data, options);
            networkCanvas = document.getElementById("mynetwork").getElementsByTagName("canvas")[0]
            network.on("doubleClick", function (params) {
                var node = params.nodes[0];
                if (node !== undefined) {
                    let nodeConfig = get_node_config_by_id(node);
                    if (nodeConfig.url){
                        window.open(nodeConfig.url);
                    }                    
                }
            });

            network.on("selectNode", function(params){
                let curNode = params.nodes[0];
                let curNodeConfig = get_node_config_by_id(curNode);
                switch (curNodeConfig.group){
                    case "historicalNews":
                        location.href=  WP_SITEURL + `/providers/details/?subject=${curNode}`;
                        break;
                    case "concepts":
                    case "timespans":
                    case "places":
                    case "Onbekend":
                    case "Vrouw":
                    case "Man":
		    case "digitalObjects":
                        //loadShowPopup(curNode, curNodeConfig);
                        loadPageNode(curNode, curNodeConfig);
                        break;
                    default:  
                        hidePopup();                 
                }               
            });

            network.on('hoverNode', function() {
                networkCanvas.style.cursor = 'pointer';
            });

            network.on('blurNode', function() {
                networkCanvas.style.cursor = 'default';
            });

            network.on("deselectNode", function(params){
                hidePopup();
            });

            jQuery(".leaflet-popup-close-button").click(function(){
                hidePopup();
                network.unselectAll();
            });

            new Promise(resolve => setTimeout(() => { network.selectNodes([data.nodes[0].id]); }, 1000));
            
            });

            function get_node_config_by_id(id){
                return data.nodes.filter((n) => { return n.id == id })[0];
            }

            function hidePopup(){
                jQuery(".leaflet-container").fadeOut(300);
            }

	    function loadPageNode(node, config){
		var URLsection = window.location.toString().split("/")[3];
		console.log(config);
		switch (config.group){
		    case "places":
			window.location.href = WP_SITEURL + `/` + URLsection + `?place=` + encodeURI(config.id);	
		        break;
		    case "Onbekend":
		    case "Vrouw":
                    case "Man":
			window.location.href = WP_SITEURL + `/agents/details/?subject=` + config.id;
                        break;
		    case "timespans":
			var type = (URLsection == 'providers') ? 'providedCHO' : 'agent';
                        window.location.href = WP_SITEURL + `/timespans?year=` + config.id.split(":")[1] + `&type=` + type;
			break;
		    case "concepts":
                        window.location.href = WP_SITEURL + `/` + URLsection + `?concept=Concept:` + config.label;
                        break;
		    case "digitalObjects":
			window.location.href = WP_SITEURL + `/providers/details/?subject=` + config.id;
                        break;
		}
            }
            function loadShowPopup(node, config){
                switch (config.group){
                    case "places":
                        networkCanvas.style.cursor = "progress";
                        fetch(WP_SITEURL + `/places/get_by_subject/?subject=${config.id}`, headers={credentials: "same-origin"}).then((response) => {
                            response.json().then((json) => {
                                jQuery(".leaflet-popup-content").html(get_place_popup_text(obj(json, 0), false, true));
                                showPopup(node);
                                networkCanvas.style.cursor = "default";
                            });
                        });
                        break;
                    case "Onbekend":
                    case "Vrouw":
                    case "Man":
                        networkCanvas.style.cursor = "progress";
                        fetch(WP_SITEURL + `/agents/get_by_subject/?subject=${config.id}`, headers={credentials: "same-origin"}, headers={credentials: "same-origin"}).then((response) => {
                            response.json().then((json) => {
                                jQuery(".leaflet-popup-content").html(get_agent_popup_text(json));
                                showPopup(node);
                                networkCanvas.style.cursor = "default";
                            });
                        });
                        break;
                    case "timespans":
                    networkCanvas.style.cursor = "progress";
                    fetch(WP_SITEURL + `/timespans/get_by_subject/?subject=${config.id}`, headers={credentials: "same-origin"}).then((response) => {
                        response.json().then((json) => {
                            jQuery(".leaflet-popup-content").html(get_timespan_popup_text(json));
                            showPopup(node);
                            networkCanvas.style.cursor = "default";
                        });
                    });
                    break;
                    case "concepts":
                    networkCanvas.style.cursor = "progress";
                    fetch(WP_SITEURL + `/conceptss/get_by_subject/?subject=${config.id}`, headers={credentials: "same-origin"}).then((response) => {
                        response.json().then((json) => {
                            jQuery(".leaflet-popup-content").html(get_concept_popup_text(json));
                            showPopup(node);
                            networkCanvas.style.cursor = "default";
                        });
                    });
                    break;
                }
                
                
            }

            function get_concept_popup_text(json){ 
                console.log(json);                
                let text = "";
                text = `<h3>${json.concept_label}</h3>`;
                let providedcho_results = json.providedCHOs;
                let agent_results = json.agents;
                if (len(providedcho_results.results.bindings) > 0){
                    console.log(providedcho_results.results.bindings);
                    let iCho = 1;
                    text += '<h6 style="margin-top: 8px; margin-bottom: 3px">Digital Objects:</h6>';
                    for (let res of providedcho_results.results.bindings){
                        if (iCho > 3){
                            break;
                        }
                        let cho_title = null;
                        if (res.ProvidedCHO_title){
                            cho_title = res.ProvidedCHO_title.value;
                        } else {
                            cho_title = res.ProvidedCHO_subject.value;
                        }
                        text += '<div>';
                        text += edm_type_map[res.ProvidedCHO_type.value];
                        text += `<a style="margin-left: 3px" target="_blank" href=` + WP_SITEURL + `/providers/details/?subject=${res.ProvidedCHO_subject.value}">${cho_title}</a></div>`;
                        iCho++;
                    }   
                    if (len(providedcho_results.results.bindings) > 3){
                        text += `<div style="padding-top: 3px"><a target="_blank" href="` + WP_SITEURL + `/conceptss/relations?name=${json.concept_label}&subject=${encodeURIComponent(json.concept_subject)}&type=providedCHO&page=1">Show more...</a></div>`;
                    }  
            }   
            if (len(agent_results.results.bindings) > 0){
                text += '<h6 style="margin-top: 8px; margin-bottom: 3px">People:</h6>';
                let iAgent = 1;
                    for (let res of agent_results.results.bindings) {
                        if (iAgent > 3){
                            break;
                        }
                        let agent_title = res.Agent_label.value;
                        text += '<div>';
                        text += `<i class="fa ${gender_to_icon[res.Agent_gender.value]}" aria-hidden="true"></i><a style="margin-left: 3px" target="_blank" href="` + WP_SITEURL + `/agents/details/?subject=${res.Agent_subject.value}">${agent_title}</a></div>`;
                        iAgent++;
                    }   
                    if (len(agent_results.results.bindings) > 3){
                        text += `<div style="padding-top: 3px"><a target="_blank" href="` + WP_SITEURL + `/conceptss/relations?concept_name=${json.concept_label}&subject=${encodeURIComponent(json.concept_subject)}&type=agent&page=1">Show more...</a></div>`;
                    } 
            }           
                return text;
            }

            function get_timespan_popup_text(json){
                console.log(json);
                let results = json.results.bindings;
                text = `<h3>${results[0].timespanlabel.value}</h3>`;
                text += '<h6 style="margin-top: 8px; margin-bottom: 3px">Digital Objects:</h6>';
                let iCho = 1;
                for (let res of results){
                    if (iCho > 3){
                        break;
                    }
                    let cho_title = null;
                    if (res.ProvidedCHO_title){
                        cho_title = res.ProvidedCHO_title.value;
                    } else {
                        cho_title = res.ProvidedCHO_subject.value;
                    }
                    text += '<div>';
                    text += edm_type_map[res.ProvidedCHO_type.value];
                    text += `<a style="margin-left: 3px" target="_blank" href="` + WP_SITEURL + `/providers/details/?subject=${res.ProvidedCHO_subject.value}">${cho_title}</a></div>`;
                    iCho++;
                }  
                if (results.length > 3){
                    text += `<div style="padding-top: 3px"><a target="_blank" href="` + WP_SITEURL + `/timespans/relations?timespan_name=${results[0].timespanlabel.value}&subject=${encodeURIComponent(results[0].timespansubject.value)}&page=1">Show more...</a></div>`;
                }                 
                return text;
            }
            function get_agent_popup_text(json){
                let agent = json.results.bindings[0]
                text = `<h3>${agent.label.value}</h3>`;                
                if (agent.gender){
                    text += `<b>Gender:</b> ${agent.gender.value}<br/>`;
                }
                if (agent.professionOccupation){
                    text += `<b>Profession or Occupation:</b> ${agent.professionOccupation.value}<br/>`;
                }
                if (agent.dateOfBirth || agent.placeOfBirth){
                    text += "<b>Birth:</b> ";
                    if (agent.dateOfBirth){
                        text += `${agent.dateOfBirth.value} `;
                        if (agent.placeOfBirth){
                            text += " in ";
                        }
                    }
                    if (agent.placeOfBirth){                        
                        text += `<a target="_blank" href=` + WP_SITEURL + `"/places/demo/?subject=${agent.placeOfBirth.value}&name=${agent.placeOfBirthName.value}">${agent.placeOfBirthName.value}</a>`;
                    }
                    text += "<br/>";
                }
                if (agent.dateOfDeath || agent.placeOfDeath){
                    text += "<b>Death:</b> ";
                    if (agent.dateOfDeath){
                        text += `${agent.dateOfDeath.value} `;
                        if (agent.placeOfDeath){
                            text += " in ";
                        }
                    }
                    if (agent.placeOfDeath){                        
                        text += `<a target="_blank" href="` + WP_SITEURL + `/places/demo/?subject=${agent.placeOfDeath.value}&name=${agent.placeOfDeathName.value}">${agent.placeOfDeathName.value}</a>`;
                    }
                    text += "<br/>";
                }
                
                if (agent.hasMetPlace.value.length > 0){
                    text += '<h6 style="margin-top: 8px; margin-bottom: 3px">Places Met:</h6>';
                    let places_met_subject = agent.hasMetPlace.value.split("\|");
                    let places_met_name = agent.hasMetPlaceName.value.split("\|");
                    for (let i = 0; i < places_met_subject.length; i++){
                        text += `<a target="_blank" href="` + WP_SITEURL + `/places/demo/?subject=${places_met_subject[i]}&name=${places_met_name[i]}"><img style="vertical-align: middle; height: 16px" src="https://upload.wikimedia.org/wikipedia/commons/b/b0/Simpleicons_Places_map-marker-1.svg"></img> ${places_met_name[i]}</a><br/>`
                    }
                }               
                
                text += `<div style="margin-top: 5px"><a style="margin-top: 10px" target="_blank" href="` + WP_SITEURL + `/agents/details/?subject=${agent.s.value}">Show more info...</a></div>`
                return text;
            }

            function showPopup(node){                
                let nodePosition = network.getPositions([node]);
                let nodeXY = network.canvasToDOM({x: nodePosition[node].x, y: nodePosition[node].y});                
                jQuery(".leaflet-container").fadeIn(300);
                console.log(document.getElementById("mynetwork").getBoundingClientRect().top);
                jQuery(".leaflet-popup").css('transform', `translate3d(${nodeXY.x - 136}px, ${nodeXY.y + jQuery('#mynetwork').offset().top - 155 - jQuery(".leaflet-popup").height()}px, 0px)`);

            }
