package org.csuc.step.content;

import com.typesafe.config.Config;
import eu.europeana.corelib.definitions.jibx.PlaceType;
import org.csuc.dao.entity.Error;
import org.csuc.util.EntityType;
import org.csuc.util.LevelQuality;
import org.csuc.util.MetadataType;
import org.csuc.util.QualityType;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.csuc.util.DataType.*;

public class Place {

    private Config config;

    public Place(Config config) {
        this.config = config;
    }

    public org.csuc.dao.entity.edm.Place quality(PlaceType placeType) throws Exception {
        org.csuc.dao.entity.edm.Place place = new org.csuc.dao.entity.edm.Place();

        try{
            aboutType(placeType.getAbout());
            place.getData().setAbout(placeType.getAbout());
        }catch(Exception e){
            place.getErrorList().add(new Error(EntityType.Place, MetadataType.rdf_about, QualityType.AboutType, e.getMessage(), LevelQuality.ERROR));
        }

        //wgs84_pos:lat
        if(!Objects.equals(LevelQuality.convert(config.getString("\"wgs84_pos:lat\".level")), LevelQuality.OFF)) {
            if (Objects.nonNull(placeType.getLat()))
                place.getData().setLat(placeType.getLat());
        }

        //wgs84_pos:long
        if(!Objects.equals(LevelQuality.convert(config.getString("\"wgs84_pos:long\".level")), LevelQuality.OFF)) {
            if (Objects.nonNull(placeType.getLong()))
                place.getData().setLong(placeType.getLong());
        }

        //wgs84_pos:alt
        if(!Objects.equals(LevelQuality.convert(config.getString("\"wgs84_pos:alt\".level")), LevelQuality.OFF)) {
            if (Objects.nonNull(placeType.getAlt()))
                place.getData().setAlt(placeType.getAlt());
        }

        //skos:prefLabel
        if(!Objects.equals(LevelQuality.convert(config.getString("\"skos:prefLabel\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(placeType.getPrefLabelList()).ifPresent(prefLabels -> {
                place.getData().setPrefLabelList(
                        prefLabels.stream().map(m -> {
                            try {
                                literalType(m);
                                return m;
                            } catch (Exception e) {
                                place.getErrorList().add(new Error(EntityType.Place, MetadataType.skos_prefLabel, QualityType.LiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"skos:prefLabel\".level"))));
                                return null;
                            }
                        }).filter(Objects::nonNull).collect(Collectors.toList())
                );
            });
        }

        //skos:altLabel
        if(!Objects.equals(LevelQuality.convert(config.getString("\"skos:altLabel\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(placeType.getAltLabelList()).ifPresent(altLabels -> {
                place.getData().setAltLabelList(altLabels.stream()
                        .map(m -> {
                            try {
                                literalType(m);
                                return m;
                            } catch (Exception e) {
                                place.getErrorList().add(new Error(EntityType.Place, MetadataType.skos_altLabel, QualityType.LiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"skos:altLabel\".level"))));
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()));
            });
        }

        //skos:note
        if(!Objects.equals(LevelQuality.convert(config.getString("\"skos:note\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(placeType.getNoteList()).ifPresent(notes -> {
                place.getData().setNoteList(notes.stream()
                        .map(m -> {
                            try {
                                literalType(m);
                                return m;
                            } catch (Exception e) {
                                place.getErrorList().add(new Error(EntityType.Place, MetadataType.skos_note, QualityType.LiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"skos:note\".level"))));
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()));
            });
        }

        //dcterms:hasPart
        if(!Objects.equals(LevelQuality.convert(config.getString("\"dcterms:hasPart\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(placeType.getHasPartList()).ifPresent(hasParts -> {
                place.getData().setHasPartList(
                        hasParts.stream()
                                .map(m -> {
                                    try {
                                        resourceOrLiteralType(m);
                                        return m;
                                    } catch (Exception e) {
                                        place.getErrorList().add(new Error(EntityType.Place, MetadataType.dcterms_hasPart, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dcterms:hasPart\".level"))));
                                        return null;
                                    }
                                })
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList())
                );
            });
        }

        //dcterms:isPartOf
        if(!Objects.equals(LevelQuality.convert(config.getString("\"dcterms:isPartOf\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(placeType.getIsPartOfList()).ifPresent(isPartOfs -> {
                place.getData().setIsPartOfList(
                        isPartOfs.stream()
                                .map(m -> {
                                    try {
                                        resourceOrLiteralType(m);
                                        return m;
                                    } catch (Exception e) {
                                        place.getErrorList().add(new Error(EntityType.Place, MetadataType.dcterms_isPartOf, QualityType.ResourceOrLiteralType, e.getMessage(), LevelQuality.convert(config.getString("\"dcterms:isPartOf\".level"))));
                                        return null;
                                    }
                                })
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList())
                );
            });
        }

        //edm:isNextInSequence
        if(!Objects.equals(LevelQuality.convert(config.getString("\"edm:isNextInSequence\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(placeType.getIsNextInSequence())
                    .ifPresent(isNextInSequence -> {
                        try {
                            resourceType(isNextInSequence);
                            place.getData().setIsNextInSequence(isNextInSequence);
                        } catch (Exception e) {
                            place.getErrorList().add(new Error(EntityType.Place, MetadataType.edm_isNextInSequence, QualityType.ResourceType, e.getMessage(), LevelQuality.convert(config.getString("\"edm:isNextInSequence\".level"))));
                        }
                    });
        }

        //owl:sameAs
        if(!Objects.equals(LevelQuality.convert(config.getString("\"owl:sameAs\".level")), LevelQuality.OFF)) {
            Optional.ofNullable(placeType.getSameAList()).ifPresent(sameAs -> {
                place.getData().setSameAList(
                        sameAs.stream()
                                .map(m -> {
                                    try {
                                        resourceType(m);
                                        return m;
                                    } catch (Exception e) {
                                        place.getErrorList().add(new Error(EntityType.Place, MetadataType.owl_sameAs, QualityType.ResourceType, e.getMessage(), LevelQuality.convert(config.getString("\"owl:sameAs\".level"))));
                                        return null;
                                    }
                                })
                                .collect(Collectors.toList())
                );
            });
        }

//
//        if (Objects.isNull(place.getWgs84_lat())
//                && Objects.isNull(place.getWgs84_long())
//                && Objects.isNull(place.getWgs84_alt())
//                && Objects.nonNull(place.getSkos_prefLabel())) {
//
//            String placename = place.getSkos_prefLabel().stream().map(org.csuc.dao.entity.edm.Place.prefLabel::getValue).findFirst().orElse(null);
//
//            if (Objects.nonNull(placename)) {
//                try {
//                    System.out.println(getGeoNames(placename));
//                } catch (Exception e) {
//                    place.getErrorList().add(new Error("edm:Place", "geoNames", "API", e.getMessage(), LevelQuality.ERROR));
//                }
//            }
//        }
//
        return place;
    }
//
//    private List<Toponym> getGeoNames(String value) throws Exception {
//        WebService.setGeoNamesServer("http://api.geonames.org");
//
//        // System.out.println(WebService.getGeoNamesServer());
//
//        WebService.setUserName("demo"); // add your username here
//
//        ToponymSearchCriteria searchCriteria = new ToponymSearchCriteria();
//        searchCriteria.setName("Tietjerksteradeel");
//
//        ToponymSearchResult searchResult = WebService.search(searchCriteria);
//
////        searchResult.getToponyms().stream().map(Toponym::toString).forEach(System.out::println);
//
//        return searchResult.getToponyms();
//    }
}
