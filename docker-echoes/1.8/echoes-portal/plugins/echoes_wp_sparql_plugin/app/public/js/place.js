let properties_map = {
    'http://www.europeana.eu/schemas/edm/hasMet': '<img title="Met this place" src="http://icons.iconarchive.com/icons/icons8/windows-8/16/Maps-Street-View-icon.png" style="vertical-align: middle; color:black; width: 16px"></img>',
    'http://rdvocab.info/ElementsGr2/placeOfBirth': '<img title="Was born in this place" src="https://cdn3.iconfinder.com/data/icons/people-professions/512/Baby-16.png" style="vertical-align: middle; width: 16px"></img>',
    'http://rdvocab.info/ElementsGr2/placeOfDeath': '<img title="Died in this place" src="https://www.shareicon.net/data/16x16/2016/01/20/706060_dead_512x512.png" style="vertical-align: middle; width: 16px"></img>',
    'unknown': '<img title="Unknown" src="https://www.shareicon.net/data/16x16/2015/12/10/685337_question_512x512.png" style="vertical-align: middle; width: 16px"></img>'
};

let properties_to_label = {
    'http://www.europeana.eu/schemas/edm/hasMet': 'Met this place',
    'http://rdvocab.info/ElementsGr2/placeOfBirth': 'Was born in this place',
    'http://rdvocab.info/ElementsGr2/placeOfDeath': 'Died in this place',
    'unknown': 'Unknown'
}

let edm_type_map = {
    'TEXT': '<img title="Text" style="vertical-align: middle; width: 16px" src="https://www.shareicon.net/data/16x16/2015/08/01/78642_document_512x512.png"></img>',
    'IMAGE': '<img title="Image" style="vertical-align: middle; width: 16px" src="https://cdn4.iconfinder.com/data/icons/pictograms-2/512/512-06-16.png"></img>',
    'SOUND': '<img title="Sound" style="vertical-align: middle; width: 16px" src="https://cdn2.iconfinder.com/data/icons/flat-ui-icons-24-px/24/volume-24-16.png"></img>',
    'VIDEO': '<img title="Video" style="vertical-align: middle; width: 16px" src="http://simpleicon.com/wp-content/uploads/video-64x64.png"></img>',
    '3D': '<img title="3D" style="vertical-align: middle; width: 16px" src="https://cdn2.iconfinder.com/data/icons/multimedia-icons-2/512/3D-16.png"></img>'
}

function get_place_popup_text(place, subjects = true, go_to_map = false){
    let title = place.name;
    text = `<h3>${title}</h3>`;
    
    if (go_to_map){
        text += `<div><a target="_blank" href="` + WP_SITEURL + `/places/demo/?subject=${place.subjects[0]}&name=${place.name}"><img style="vertical-align: middle; height: 16px" src="https://upload.wikimedia.org/wikipedia/commons/b/b0/Simpleicons_Places_map-marker-1.svg"></img> Show on map</a></div>`
    }   

    if (subjects){
        text += `<b>Subject(s): </b>`;
        for (let subject of place.subjects){
            text += `${subject}, `;
        }
        text = text.substring(0, text.length - 2) + '.';
    }  
    
    if (Object.keys(place.providedCHOs).length > 0){
        iCho = 0;
        text += '<h6 style="margin-top: 8px; margin-bottom: 3px">Digital Objects:</h6>';
        for (let cho_id in place.providedCHOs){
            if (iCho == 3){
                break;
            }
            let cho = place.providedCHOs[cho_id];
            let cho_title = null;
            if (cho.titles.length == 0){
                cho_title = cho_id;
            } else {
                cho_title = add3Dots(cho.titles[0], 100);
            }
            text += '<div>';
            for (let type of cho.types){
                text += edm_type_map[type];
            }
            text += `<a style="margin-left: 3px" target="_blank" href="` + WP_SITEURL + `/providers/details/?subject=${cho_id}">${cho_title}</a></div>`;
            iCho++;
        }  
        if (Object.keys(place.providedCHOs).length > 3){
            text += `<div style="padding-top: 3px"><a target="_blank" href="` + WP_SITEURL + `/places/relations?place_name=${place.name}&subjects=${encodeURIComponent(place.subjects.join('|'))}&type=providedCHO&page=1">Show more...</a></div>`;
        }      
    }
    if (Object.keys(place.agents).length > 0){
        iAgent = 0;
        text += '<h6 style="margin-top: 3px; margin-bottom: 3px">People:</h6>';
        for (let agent_id in place.agents){
            if (iAgent == 3){
                break;
            }
            let agent = place.agents[agent_id];
            let agent_title = null;
            if (agent.titles.length == 0){
                agent_title = agent_id;
            } else {
                agent_title = agent.titles.join(" ");
            }

            if (agent.subtitles.length > 0){
                agent_title += ' ' + agent.subtitles.join(" ");
            }
            text += '<div>';

            for (let property of agent.properties){
                if (!(property in properties_map)){
                    text += "<i>(" + property + ")</i>";
                } else {
                    text += properties_map[property];
                }                
            }
            text += `<a style="margin-left: 3px" target="_blank" href="` + WP_SITEURL + `/agents/details/?subject=${agent_id}">${agent_title}</a></div>`;
            iAgent++;
        }
        if (Object.keys(place.agents).length > 3){
            text += `<div style="padding-top: 3px"><a target="_blank" href="` + WP_SITEURL + `/places/relations?place_name=${place.name}&subjects=${encodeURIComponent(place.subjects.join('|'))}&type=agent&page=1">Show more...</a></div>`;
        }
    }
    return text;
}
