package org.csuc.step.content;

import com.typesafe.config.Config;
import eu.europeana.corelib.definitions.jibx.PlaceType;
import org.csuc.dao.entity.Error;
import org.csuc.util.LevelQuality;
import org.geonames.Toponym;
import org.geonames.ToponymSearchCriteria;
import org.geonames.ToponymSearchResult;
import org.geonames.WebService;

import java.util.List;
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

        if (aboutType(placeType.getAbout())) place.setAbout(placeType.getAbout());

        //wgs84:lat
        if (Objects.nonNull(placeType.getLat()))
            place.setWgs84_lat(placeType.getLat().getLat().toString());
        //wgs84:long
        if (Objects.nonNull(placeType.getLong()))
            place.setWgs84_long(placeType.getLong().getLong().toString());
        //wgs84:lat
        if (Objects.nonNull(placeType.getAlt()))
            place.setWgs84_alt(placeType.getAlt().getAlt().toString());

        //skos:prefLabel
        Optional.ofNullable(placeType.getPrefLabelList()).ifPresent(prefLabels -> {
            place.setSkos_prefLabel(prefLabels.stream()
                    .map(m -> {
                        try {
                            literalType(m);
                            return new org.csuc.dao.entity.edm.Place.prefLabel(m.getString(), (Objects.nonNull(m.getLang())) ? m.getLang().getLang() : null);
                        } catch (Exception e) {
                            place.getErrorList().add(new Error("edm:Place", "skos:altLabel", "LiteralType", e.getMessage(), LevelQuality.convert(config.getString("\"skos:prefLabel\".level"))));
                            return null;
                        }
                    }).filter(Objects::nonNull).collect(Collectors.toSet()));
        });

        //skos:altLabel
        Optional.ofNullable(placeType.getAltLabelList()).ifPresent(altLabels -> {
            place.setSkos_altLabel(altLabels.stream()
                    .map(m -> {
                        try {
                            literalType(m);
                            return new org.csuc.dao.entity.edm.Place.altLabel(m.getString(), (Objects.nonNull(m.getLang())) ? m.getLang().getLang() : null);
                        } catch (Exception e) {
                            place.getErrorList().add(new Error("edm:Place", "skos:altLabel", "LiteralType", e.getMessage(), LevelQuality.convert(config.getString("\"skos:altLabel\".level"))));
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet()));
        });

        //skos:note
        Optional.ofNullable(placeType.getNoteList()).ifPresent(notes -> {
            place.setSkos_note(notes.stream()
                    .map(m -> {
                        try {
                            literalType(m);
                            return new org.csuc.dao.entity.edm.Place.skosNote(m.getString(), (Objects.nonNull(m.getLang())) ? m.getLang().getLang() : null);
                        } catch (Exception e) {
                            place.getErrorList().add(new Error("edm:Place", "skos:altLabel", "LiteralType", e.getMessage(), LevelQuality.convert(config.getString("\"skos:note\".level"))));
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet()));
        });

        //dcterms:hasPart
        Optional.ofNullable(placeType.getHasPartList()).ifPresent(hasParts -> {
            place.setDcterms_hasPart(
                    hasParts.stream()
                            .map(m -> {
                                try {
                                    resourceOrLiteralType(m);
                                    return new org.csuc.dao.entity.edm.Place.hasPart((Objects.nonNull(m.getResource()) ? m.getResource().getResource() : null),
                                            m.getString(),
                                            (Objects.nonNull(m.getLang()) ? m.getLang().getLang() : null));
                                } catch (Exception e) {
                                    place.getErrorList().add(new Error("edm:Place", "dcterms:hasPart", "ResourceOrLiteralType", e.getMessage(), LevelQuality.convert(config.getString("\"dcterms:hasPart\".level"))));
                                    return null;
                                }
                            })
                            .filter(Objects::nonNull)
                            .collect(Collectors.toSet())
            );
        });

        //dcterms:isPartOf
        Optional.ofNullable(placeType.getIsPartOfList()).ifPresent(isPartOfs -> {
            place.setDcterms_isPartOf(
                    isPartOfs.stream()
                            .map(m -> {
                                try {
                                    resourceOrLiteralType(m);
                                    return new org.csuc.dao.entity.edm.Place.isPartOf((Objects.nonNull(m.getResource()) ? m.getResource().getResource() : null),
                                            m.getString(),
                                            (Objects.nonNull(m.getLang()) ? m.getLang().getLang() : null));
                                } catch (Exception e) {
                                    place.getErrorList().add(new Error("edm:Place", "dcterms:isPartOf", "ResourceOrLiteralType", e.getMessage(), LevelQuality.convert(config.getString("\"dcterms:isPartOf\".level"))));
                                    return null;
                                }
                            })
                            .filter(Objects::nonNull)
                            .collect(Collectors.toSet())
            );
        });

        //edm:isNextInSequence
        Optional.ofNullable(placeType.getIsNextInSequence())
                .ifPresent(isNextInSequence -> {
                    try {
                        place.setEdm_isNextInSequence(new org.csuc.dao.entity.edm.Place.isNexInSequence((Objects.nonNull(isNextInSequence.getResource())) ? isNextInSequence.getResource() : null));
                    } catch (Exception e) {
                        place.getErrorList().add(new Error("edm:Place", "edm:isNextInSequence", "ResourceType", e.getMessage(), LevelQuality.convert(config.getString("\"edm:isNextInSequence\".level"))));
                    }
                });

        //owl:sameAs
        Optional.ofNullable(placeType.getSameAList()).ifPresent(sameAs -> {
            place.setOwl_sameAs(
                    sameAs.stream()
                            .map(m -> {
                                try {
                                    resourceType(m);
                                    return new org.csuc.dao.entity.edm.Place.sameAs(m.getResource());
                                } catch (Exception e) {
                                    place.getErrorList().add(new Error("edm:Place", "owl:sameAs", "ResourceType", e.getMessage(), LevelQuality.convert(config.getString("\"owl:sameAs\".level"))));
                                    return null;
                                }

                            })
                            .collect(Collectors.toSet())
            );
        });

        if (Objects.isNull(place.getWgs84_lat())
                && Objects.isNull(place.getWgs84_long())
                && Objects.isNull(place.getWgs84_alt())
                && Objects.nonNull(place.getSkos_prefLabel())) {

            String placename = place.getSkos_prefLabel().stream().map(org.csuc.dao.entity.edm.Place.prefLabel::getValue).findFirst().orElse(null);

            if (Objects.nonNull(placename)) {
                try {
                    System.out.println(getGeoNames(placename));
                } catch (Exception e) {
                    place.getErrorList().add(new Error("edm:Place", "geoNames", "API", e.getMessage(), LevelQuality.ERROR));
                }
            }
        }

        return place;
    }

    private List<Toponym> getGeoNames(String value) throws Exception {
        WebService.setGeoNamesServer("http://api.geonames.org");

        // System.out.println(WebService.getGeoNamesServer());

        WebService.setUserName("demo"); // add your username here

        ToponymSearchCriteria searchCriteria = new ToponymSearchCriteria();
        searchCriteria.setName("Tietjerksteradeel");

        ToponymSearchResult searchResult = WebService.search(searchCriteria);

//        searchResult.getToponyms().stream().map(Toponym::toString).forEach(System.out::println);

        return searchResult.getToponyms();
    }
}
