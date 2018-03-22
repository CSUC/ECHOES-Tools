/**
 * 
 */
package org.EDM.Transformations.formats.a2a;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Stream;

import nl.mindbus.a2a.*;
import org.EDM.Transformations.formats.EDM;
import org.EDM.Transformations.formats.xslt.XSLTTransformations;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import eu.europeana.corelib.definitions.jibx.AgentType;
import eu.europeana.corelib.definitions.jibx.AggregatedCHO;
import eu.europeana.corelib.definitions.jibx.Aggregation;
import eu.europeana.corelib.definitions.jibx.AltLabel;
import eu.europeana.corelib.definitions.jibx.Concept;
import eu.europeana.corelib.definitions.jibx.Contributor;
import eu.europeana.corelib.definitions.jibx.Coverage;
import eu.europeana.corelib.definitions.jibx.DataProvider;
import eu.europeana.corelib.definitions.jibx.Date;
import eu.europeana.corelib.definitions.jibx.DateOfBirth;
import eu.europeana.corelib.definitions.jibx.Description;
import eu.europeana.corelib.definitions.jibx.EdmType;
import eu.europeana.corelib.definitions.jibx.Gender;
import eu.europeana.corelib.definitions.jibx.HasMet;
import eu.europeana.corelib.definitions.jibx.HasView;
import eu.europeana.corelib.definitions.jibx.Identifier;
import eu.europeana.corelib.definitions.jibx.IntermediateProvider;
import eu.europeana.corelib.definitions.jibx.IsRelatedTo;
import eu.europeana.corelib.definitions.jibx.IsShownAt;
import eu.europeana.corelib.definitions.jibx.Language;
import eu.europeana.corelib.definitions.jibx.LanguageCodes;
import eu.europeana.corelib.definitions.jibx.PlaceOfBirth;
import eu.europeana.corelib.definitions.jibx.PlaceType;
import eu.europeana.corelib.definitions.jibx.PrefLabel;
import eu.europeana.corelib.definitions.jibx.ProfessionOrOccupation;
import eu.europeana.corelib.definitions.jibx.ProvidedCHOType;
import eu.europeana.corelib.definitions.jibx.Provider;
import eu.europeana.corelib.definitions.jibx.RDF;
import eu.europeana.corelib.definitions.jibx.Related;
import eu.europeana.corelib.definitions.jibx.Temporal;
import eu.europeana.corelib.definitions.jibx.TimeSpanType;
import eu.europeana.corelib.definitions.jibx.ResourceOrLiteralType.Resource;
import eu.europeana.corelib.definitions.jibx.Rights1;
import eu.europeana.corelib.definitions.jibx.Title;
import eu.europeana.corelib.definitions.jibx.Type1;
import eu.europeana.corelib.definitions.jibx.Type2;
import eu.europeana.corelib.definitions.jibx.WebResourceType;
import net.sf.saxon.functions.IriToUri;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.io.IoBuilder;
import org.csuc.deserialize.JibxUnMarshall;
import org.csuc.serialize.JibxMarshall;

/**
 * @author amartinez
 *
 */
public class A2A2EDM extends RDF implements EDM {

	private static Logger logger = LogManager.getLogger(A2A2EDM.class);

	private A2AType a2a;
	private String identifier;

	private Set<String> identifiers = new HashSet<>();
	private Map<String, String> properties;

	/**
	 *
	 * @param identifier
	 * @param type
	 * @param properties
	 */
	public A2A2EDM(String identifier, A2AType type, Map<String, String> properties) {
		this.identifier = identifier;
		this.a2a = type;
		this.properties = properties;

        edmProvidedCHO();
        edmAgent();
        edmPlace();
        skosConcept();
        edmTimeSpan();
        edmWebResource();
        oreAggregation();
    }


    private void edmAgent() {
		try {
			Optional.ofNullable(a2a.getPerson()).ifPresent((List<CtPerson> p) -> p.forEach((CtPerson person) -> {
                Choice choice = new Choice();

                AgentType a = new AgentType();

                Optional.ofNullable(person.getPersonName()).map(CtPersonName::getPersonNameFirstName)
                        .ifPresent((CtTransString present) -> {
                            PrefLabel preflabel = new PrefLabel();
                            preflabel.setString(present.getValue());
                            a.getPrefLabelList().add(preflabel);
                        });

                Optional.ofNullable(person.getPersonName()).map(CtPersonName::getPersonNameLastName)
                        .ifPresent((CtTransString present) -> {
                            AltLabel altlabel = new AltLabel();
                            altlabel.setString(present.getValue());
                            a.getAltLabelList().add(altlabel);
                        });

                Optional.ofNullable(person.getPid()).ifPresent((String present) -> {
                    String iriToUri = IriToUri
                            .iriToUri(String.format("Agent:%s", StringUtils.deleteWhitespace(present))).toString();

                    identifiers.add(iriToUri);

                    a.setAbout(iriToUri);

                    Identifier identifier = new Identifier();
                    identifier.setString(person.getPid());
                    a.getIdentifierList().add(identifier);

                    IsRelatedTo isRelated = new IsRelatedTo();
                    Resource resourceIsRelated = new Resource();
                    resourceIsRelated.setResource(IriToUri.iriToUri(String.format("Concept:%s",
                            StringUtils.deleteWhitespace(a2a.getRelationEP().stream()
                                    .filter((CtRelationEP f) -> f.getPersonKeyRef().equals(person.getPid())).findFirst().get()
                                    .getRelationType())))
                            .toString());

                    isRelated.setResource(resourceIsRelated);
                    isRelated.setString("");
                    a.getIsRelatedToList().add(isRelated);
                });

                Optional.ofNullable(person.getResidence()).ifPresent((CtDetailPlace place) ->{
                    Optional.ofNullable(place.getCountry()).ifPresent((CtTransString country) ->{
                        HasMet residence = new HasMet();

                        residence.setResource(IriToUri
                                .iriToUri(String.format("%s:%s", org.EDM.Transformations.formats.a2a.PlaceType.COUNTRY.value(),
                                        StringUtils.deleteWhitespace(country.getValue())))
                                .toString());
                        a.getHasMetList().add(residence);
                    });

                    Optional.ofNullable(place.getProvince()).ifPresent((CtTransString province) ->{
                        HasMet residence = new HasMet();

                        residence.setResource(IriToUri
                                .iriToUri(String.format("%s:%s", org.EDM.Transformations.formats.a2a.PlaceType.PROVINCE.value(),
                                        StringUtils.deleteWhitespace(province.getValue())))
                                .toString());
                        a.getHasMetList().add(residence);
                    });

                    Optional.ofNullable(place.getState()).ifPresent((CtTransString state) ->{
                        HasMet residence = new HasMet();

                        residence.setResource(IriToUri
                                .iriToUri(String.format("%s:%s", org.EDM.Transformations.formats.a2a.PlaceType.STATE.value(),
                                        StringUtils.deleteWhitespace(state.getValue())))
                                .toString());
                        a.getHasMetList().add(residence);
                    });

                    Optional.ofNullable(place.getPlace()).ifPresent((CtTransString pl) ->{
                        HasMet residence = new HasMet();

                        residence.setResource(IriToUri
                                .iriToUri(String.format("%s:%s", org.EDM.Transformations.formats.a2a.PlaceType.PLACE.value(),
                                        StringUtils.deleteWhitespace(pl.getValue())))
                                .toString());
                        a.getHasMetList().add(residence);
                    });
                });

                Optional.ofNullable(person.getBirthPlace()).map(CtDetailPlace::getPlace).ifPresent((CtTransString birthPlace) ->{
                    PlaceOfBirth placeOfBirth = new PlaceOfBirth();
                        Resource resourcePlaceOfBirth = new Resource();
                        resourcePlaceOfBirth.setResource(IriToUri
                                .iriToUri(String.format("%s:%s",
                                        org.EDM.Transformations.formats.a2a.PlaceType.PLACE.value(),StringUtils.deleteWhitespace(birthPlace.getValue())))
                                .toString());
                        placeOfBirth.setResource(resourcePlaceOfBirth);
                        placeOfBirth.setString("");
                        a.setPlaceOfBirth(placeOfBirth);
                });

                Optional.ofNullable(person.getBirthDate()).filter((CtTransDate f) -> Objects.nonNull(f.getYear())
                        && Objects.nonNull(f.getMonth()) && Objects.nonNull(f.getDay())).ifPresent(present -> {

                            DateOfBirth birth = new DateOfBirth();
                            birth.setString(String.format("%s-%s-%s", present.getYear().getValue(),
                                    present.getMonth().getValue(), present.getDay().getValue()));
                            a.setDateOfBirth(birth);
                        });

                Optional.ofNullable(person.getGender()).ifPresent((String present) -> {
                    Gender gender = new Gender();
                    gender.setString(present);
                    a.setGender(gender);
                });

                Optional.ofNullable(person.getProfession()).ifPresent((List<CtTransString> present) -> present.stream().forEach((CtTransString profession) -> {
                    ProfessionOrOccupation professionOrOccupation = new ProfessionOrOccupation();
                    professionOrOccupation.setString(profession.getValue());

                    a.getProfessionOrOccupationList().add(professionOrOccupation);
                }));

                choice.setAgent(a);
                this.getChoiceList().add(choice);
            }));
		} catch (Exception exception) {
			logger.error(String.format("[%s] error generate edmAgent \n%s", identifier, exception));
		}
	}

	private void edmProvidedCHO() {
        try {
            Optional.ofNullable(a2a.getSource()).ifPresent((CtSource source) -> {

                Choice choice = new Choice();

                ProvidedCHOType provided = new ProvidedCHOType();

                Optional.ofNullable(identifier).ifPresent((String present) -> {
                    eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();

                    String iriToUri = IriToUri
                            .iriToUri(String.format("ProvidedCHO:%s", StringUtils.deleteWhitespace(identifier)))
                            .toString();

                    provided.setAbout(iriToUri);

                    identifiers.add(iriToUri);

                    Identifier id = new Identifier();
                    id.setString(identifier);

                    c.setIdentifier(id);
                    provided.getChoiceList().add(c);
                });

                Optional.ofNullable(source.getSourcePlace()).ifPresent((CtPlace place) ->{
                    Optional.ofNullable(place.getCountry()).ifPresent((CtTransString country) ->{
                        eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();

                        Coverage coverage = new Coverage();
                        Resource coverageResource = new Resource();
                        coverageResource.setResource(IriToUri
                                .iriToUri(String.format("%s:%s", org.EDM.Transformations.formats.a2a.PlaceType.COUNTRY.value(),
                                        StringUtils.deleteWhitespace(country.getValue())))
                                .toString());
                        coverage.setResource(coverageResource);
                        coverage.setString("");

                        c.setCoverage(coverage);
                        provided.getChoiceList().add(c);
                    });

                    Optional.ofNullable(place.getProvince()).ifPresent((CtTransString province) ->{
                        eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();

                        Coverage coverage = new Coverage();
                        Resource coverageResource = new Resource();
                        coverageResource.setResource(IriToUri
                                .iriToUri(String.format("%s:%s", org.EDM.Transformations.formats.a2a.PlaceType.PROVINCE.value(),
                                        StringUtils.deleteWhitespace(province.getValue())))
                                .toString());
                        coverage.setResource(coverageResource);
                        coverage.setString("");

                        c.setCoverage(coverage);
                        provided.getChoiceList().add(c);
                    });

                    Optional.ofNullable(place.getState()).ifPresent((CtTransString state) ->{
                        eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();

                        Coverage coverage = new Coverage();
                        Resource coverageResource = new Resource();
                        coverageResource.setResource(IriToUri
                                .iriToUri(String.format("%s:%s", org.EDM.Transformations.formats.a2a.PlaceType.STATE.value(),
                                        StringUtils.deleteWhitespace(state.getValue())))
                                .toString());
                        coverage.setResource(coverageResource);
                        coverage.setString("");

                        c.setCoverage(coverage);
                        provided.getChoiceList().add(c);
                    });

                    Optional.ofNullable(place.getPlace()).ifPresent((CtTransString pl) ->{
                        eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();

                        Coverage coverage = new Coverage();
                        Resource coverageResource = new Resource();
                        coverageResource.setResource(IriToUri
                                .iriToUri(String.format("%s:%s", org.EDM.Transformations.formats.a2a.PlaceType.PLACE.value(),
                                        StringUtils.deleteWhitespace(pl.getValue())))
                                .toString());
                        coverage.setResource(coverageResource);
                        coverage.setString("");

                        c.setCoverage(coverage);
                        provided.getChoiceList().add(c);
                    });
                });

                Optional.ofNullable(source.getSourceReference()).ifPresent((CtSourceReference present) -> {
                    eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();

                    if(Objects.nonNull(present.getCollection())){
                        Description description = new Description();
                        description.setString(present.getCollection());

                        c.setDescription(description);
                        c.clearChoiceListSelect();
                    }

                    if(Objects.nonNull(present.getBook())){
                        Title title = new Title();
                        title.setString(present.getBook());

                        c.setTitle(title);
                    }

                    provided.getChoiceList().add(c);
                });

                Optional.ofNullable(source.getSourceType()).ifPresent((String present) -> {
                    eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();

                    Type1 type = new Type1();
                    type.setString(present);

                    c.setType(type);
                    provided.getChoiceList().add(c);
                });

                Optional.ofNullable(source.getSourceIndexDate()).ifPresent((CtIndexDate present) -> {
                    Set<String> setDate = new HashSet<>();
                    setDate.add(present.getFrom().toString());
                    setDate.add(present.getTo().toString());

                    Set<Integer> setYear = new HashSet<>();
                    setYear.add(present.getFrom().getYear());
                    setYear.add(present.getTo().getYear());

                    setDate.stream().filter(Objects::nonNull).forEach((String d) -> {
                        eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();

                        Date date = new Date();
                        date.setString(d);

                        c.setDate(date);
                        provided.getChoiceList().add(c);
                    });

                    setYear.stream().filter(Objects::nonNull).forEach((Integer y) -> {
                        eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();

                        Temporal temporal = new Temporal();
                        Resource resource = new Resource();
                        resource.setResource(IriToUri
                                .iriToUri(String.format("TimeSpan:%s", StringUtils.deleteWhitespace(y.toString())))
                                .toString());

                        temporal.setResource(resource);
                        temporal.setString("");
                        c.setTemporal(temporal);
                        provided.getChoiceList().add(c);
                    });
                });

                Optional.ofNullable(a2a.getPerson()).map(Collection::stream).ifPresent((Stream<CtPerson> present) -> present.map(CtPerson::getPid).forEach((String person) -> {
                    eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();

                    Contributor contributor = new Contributor();
                    Resource resource = new Resource();
                    resource.setResource(IriToUri
                            .iriToUri(String.format("Agent:%s", StringUtils.deleteWhitespace(person))).toString());
                    contributor.setResource(resource);
                    contributor.setString("");

                    c.setContributor(contributor);
                    provided.getChoiceList().add(c);
                }));

                Optional.ofNullable(source.getSourceType()).ifPresent((String present) -> {
                    IsRelatedTo isRelatedTo = new IsRelatedTo();
                    Resource resource = new Resource();
                    resource.setResource(IriToUri
                            .iriToUri(String.format("Concept:%s", StringUtils.deleteWhitespace(present))).toString());
                    isRelatedTo.setResource(resource);
                    isRelatedTo.setString("");

                    provided.getIsRelatedToList().add(isRelatedTo);
                });

                Optional.ofNullable(properties).map((Map<String, String> m) -> m.get("language")).ifPresent((String present) -> {
                    if (Objects.nonNull(LanguageCodes.convert(present))) {
                        eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();

                        Language language = new Language();
                        language.setString(LanguageCodes.convert(present).xmlValue());

                        c.setLanguage(language);
                        provided.getChoiceList().add(c);
                    }
                });

                Optional.ofNullable(properties).map((Map<String, String> m) -> m.get("edmType")).ifPresent((String present) -> {
                    if (Objects.nonNull(EdmType.convert(present))) {
                        Type2 t = new Type2();
                        t.setType(EdmType.convert(present));

                        provided.setType(t);
                    }
                });

                choice.setProvidedCHO(provided);
                this.getChoiceList().add(choice);
            });
        } catch (Exception exception) {
            logger.error(String.format("[%s] error generate edmProvidedCHO \n%s", identifier, exception));
        }
	}

    private void skosConcept() {
		try {
			Optional.ofNullable(a2a.getRelationEP()).ifPresent((List<CtRelationEP> present) -> present.forEach((CtRelationEP relationType) -> {
                Choice choice = new Choice();

                Concept concept = new Concept();

                Optional.ofNullable(relationType.getRelationType()).ifPresent((String type) -> {
                    String iriToUri = IriToUri.iriToUri(String.format("Concept:%s",
                            StringUtils.deleteWhitespace(relationType.getRelationType()))).toString();

                    concept.setAbout(iriToUri);
                    identifiers.add(iriToUri);

                    Concept.Choice c = new Concept.Choice();
                    PrefLabel prefLabel = new PrefLabel();
                    prefLabel.setString(type);

                    c.setPrefLabel(prefLabel);
                    concept.getChoiceList().add(c);

                });

                Optional.ofNullable(relationType.getPersonKeyRef()).ifPresent((String keyRef) -> {
                    Concept.Choice c = new Concept.Choice();
                    Related related = new Related();
                    related.setResource(IriToUri
                            .iriToUri(String.format("Agent:%s", StringUtils.deleteWhitespace(keyRef))).toString());

                    c.setRelated(related);
                    concept.getChoiceList().add(c);

                });
                choice.setConcept(concept);
                this.getChoiceList().add(choice);
            }));

			Optional.ofNullable(a2a.getSource()).map(m-> m.getSourceType()).ifPresent(present->{
                Choice choice = new Choice();
                Concept concept = new Concept();

                String iriToUri = IriToUri
                        .iriToUri(String.format("Concept:%s", StringUtils.deleteWhitespace(present))).toString();

                concept.setAbout(iriToUri);
                identifiers.add(iriToUri);

                Concept.Choice c = new Concept.Choice();
                PrefLabel prefLabel = new PrefLabel();
                prefLabel.setString(present);

                c.setPrefLabel(prefLabel);
                concept.getChoiceList().add(c);

                choice.setConcept(concept);
                this.getChoiceList().add(choice);
            });
		} catch (Exception exception) {
			logger.error(String.format("[%s] error generate skosConcept \n%s", identifier, exception));
		}
	}


    private void edmPlace() {
		try {
            Map<String, Set<String>> mapPlace = new HashMap<>();
			Optional.ofNullable(a2a.getPerson()).map(Collection::stream).ifPresent((Stream<CtPerson> present) -> {
				present.forEach((CtPerson person) -> {
					Optional.ofNullable(person.getResidence()).ifPresent((CtDetailPlace p) ->{
						Optional.ofNullable(p.getCountry()).ifPresent((CtTransString country) ->{
                            mapPlace.computeIfAbsent(org.EDM.Transformations.formats.a2a.PlaceType.COUNTRY.value(),
                                    ((String x) -> new HashSet<String>())).add(country.getValue());
						});

						Optional.ofNullable(p.getProvince()).ifPresent((CtTransString province) ->{
                            mapPlace.computeIfAbsent(org.EDM.Transformations.formats.a2a.PlaceType.PROVINCE.value(),
                                    (x -> new HashSet<String>())).add(province.getValue());
						});

						Optional.ofNullable(p.getState()).ifPresent((CtTransString state) ->{
                            mapPlace.computeIfAbsent(org.EDM.Transformations.formats.a2a.PlaceType.STATE.value(),
                                    ((String x) -> new HashSet<String>())).add(state.getValue());
						});

						Optional.ofNullable(p.getPlace()).ifPresent((CtTransString pl) ->{
                            mapPlace.computeIfAbsent(org.EDM.Transformations.formats.a2a.PlaceType.PLACE.value(),
                                    ((String x) -> new HashSet<String>())).add(pl.getValue());
						});
					});

                    Optional.ofNullable(person.getBirthPlace()).ifPresent((CtDetailPlace p) -> {
                        Optional.ofNullable(p.getCountry()).ifPresent((CtTransString country) ->{
                            mapPlace.computeIfAbsent(org.EDM.Transformations.formats.a2a.PlaceType.COUNTRY.value(),
                                    ((String x) -> new HashSet<String>())).add(country.getValue());
                        });

                        Optional.ofNullable(p.getProvince()).ifPresent((CtTransString province) ->{
                            mapPlace.computeIfAbsent(org.EDM.Transformations.formats.a2a.PlaceType.PROVINCE.value(),
                                    ((String x) -> new HashSet<String>())).add(province.getValue());
                        });

                        Optional.ofNullable(p.getState()).ifPresent((CtTransString state) ->{
                            mapPlace.computeIfAbsent(org.EDM.Transformations.formats.a2a.PlaceType.STATE.value(),
                                    ((String x) -> new HashSet<String>())).add(state.getValue());
                        });

                        Optional.ofNullable(p.getPlace()).ifPresent((CtTransString pl) ->{
                            mapPlace.computeIfAbsent(org.EDM.Transformations.formats.a2a.PlaceType.PLACE.value(),
                                    ((String x) -> new HashSet<String>())).add(pl.getValue());
                        });
					});
				});
			});

            Optional.ofNullable(a2a.getSource()).map(CtSource::getSourcePlace).ifPresent((CtPlace present) ->{
                Optional.ofNullable(present.getCountry()).ifPresent((CtTransString country) ->{
                    mapPlace.computeIfAbsent(org.EDM.Transformations.formats.a2a.PlaceType.COUNTRY.value(),
                            ((String x) -> new HashSet<String>())).add(country.getValue());
                });

                Optional.ofNullable(present.getProvince()).ifPresent((CtTransString province) ->{
                    mapPlace.computeIfAbsent(org.EDM.Transformations.formats.a2a.PlaceType.PROVINCE.value(),
                            ((String x) -> new HashSet<String>())).add(province.getValue());
                });

                Optional.ofNullable(present.getState()).ifPresent((CtTransString state) ->{
                    mapPlace.computeIfAbsent(org.EDM.Transformations.formats.a2a.PlaceType.STATE.value(),
                            ((String x) -> new HashSet<String>())).add(state.getValue());
                });

                Optional.ofNullable(present.getPlace()).ifPresent((CtTransString pl) ->{
                    mapPlace.computeIfAbsent(org.EDM.Transformations.formats.a2a.PlaceType.PLACE.value(),
                            ((String x) -> new HashSet<String>())).add(pl.getValue());
                });
            });

            mapPlace.entrySet().forEach((Map.Entry<String, Set<String>> entry) -> entry.getValue().forEach((String value) ->{
                RDF.Choice choice = new RDF.Choice();
                PlaceType placeType = new PlaceType();

                String iriToUri = IriToUri.iriToUri(String.format("%s:%s", entry.getKey(), StringUtils.deleteWhitespace(value)))
                        .toString();

                placeType.setAbout(iriToUri);

                placeType.setAbout(iriToUri);
                identifiers.add(iriToUri);

                PrefLabel prefLabel = new PrefLabel();
                prefLabel.setString(value);

                AltLabel altLabel = new AltLabel();
                altLabel.setString(entry.getKey());

                placeType.getAltLabelList().add(altLabel);

                placeType.getPrefLabelList().add(prefLabel);

                choice.setPlace(placeType);
                this.getChoiceList().add(choice);
            }));
		}catch (Exception exception) {
			logger.error(String.format("[%s] error generate edmPlace \n%s", identifier, exception));
		}
	}


    private void edmTimeSpan() {
		try {
			Optional.ofNullable(a2a.getSource()).map(CtSource::getSourceIndexDate).ifPresent((CtIndexDate present) -> {
				Set<Integer> setYear = new HashSet<Integer>();
				setYear.add(present.getFrom().getYear());
				setYear.add(present.getTo().getYear());

				setYear.stream().filter(Objects::nonNull).forEach((Integer y) -> {
					eu.europeana.corelib.definitions.jibx.RDF.Choice choice = new eu.europeana.corelib.definitions.jibx.RDF.Choice();
					TimeSpanType timeSpan = new TimeSpanType();

					String iriToUri = IriToUri
							.iriToUri(String.format("TimeSpan:%s", StringUtils.deleteWhitespace(y.toString()))).toString();

					timeSpan.setAbout(iriToUri);
					identifiers.add(iriToUri);

					PrefLabel prefLabel = new PrefLabel();
					prefLabel.setString(y.toString());
					timeSpan.getPrefLabelList().add(prefLabel);

					choice.setTimeSpan(timeSpan);
					this.getChoiceList().add(choice);
				});
			});
		}catch (Exception exception) {
			logger.error(String.format("[%s] error generate edmTimeSpan \n%s", identifier, exception));
		}		
	}

	/**
	 * 
	 */

    private void edmWebResource() {
		try {
			Optional.ofNullable(a2a.getSource()).map(CtSource::getSourceAvailableScans).map(CtScans::getScan)
			.ifPresent((List<CtScan> present) -> {
				present.forEach((CtScan ctScan) -> {
					eu.europeana.corelib.definitions.jibx.RDF.Choice choice = new eu.europeana.corelib.definitions.jibx.RDF.Choice();
					WebResourceType webResource = new WebResourceType();
					String iriToUri = IriToUri.iriToUri(ctScan.getUri()).toString();
					identifiers.add(iriToUri);

					webResource.setAbout(iriToUri);
					choice.setWebResource(webResource);
					this.getChoiceList().add(choice);
				});
			});
//            Optional.ofNullable(a2a.getSource()).map(CtSource::getSourceDigitalOriginal).ifPresent(present->{
//                eu.europeana.corelib.definitions.jibx.RDF.Choice choice = new eu.europeana.corelib.definitions.jibx.RDF.Choice();
//                WebResourceType webResource = new WebResourceType();
//                String iriToUri = IriToUri.iriToUri(present).toString();
//                identifiers.add(iriToUri);
//
//                webResource.setAbout(iriToUri);
//                choice.setWebResource(webResource);
//                this.getChoiceList().add(choice);
//            });
		}catch (Exception exception) {
			logger.error(String.format("[%s] error generate edmWebResource \n%s", identifier, exception));
		}
	}


    private void oreAggregation() {
		try {
			Optional.ofNullable(a2a.getSource()).ifPresent((CtSource present) -> {
				eu.europeana.corelib.definitions.jibx.RDF.Choice choice = new eu.europeana.corelib.definitions.jibx.RDF.Choice();

				Aggregation aggregation = new Aggregation();

				if (Objects.nonNull(present.getSourceAvailableScans())) {
					try {
						Optional.ofNullable(present.getSourceAvailableScans()).map(CtScans::getScan).ifPresent((List<CtScan> p) -> p.forEach((CtScan scan) -> {
                            aggregation.setAbout(scan.getUriViewer());

                            AggregatedCHO aggregatedCHO = new AggregatedCHO();
                            aggregatedCHO.setResource(IriToUri
                                    .iriToUri(String.format("ProvidedCHO:%s", StringUtils.deleteWhitespace(identifier)))
                                    .toString());

                            if (Objects.nonNull(scan.getUriViewer())) {
                                IsShownAt isShownAt = new IsShownAt();
                                isShownAt.setResource(scan.getUriViewer());

                                aggregation.setIsShownAt(isShownAt);
                            }

                            aggregation.setAggregatedCHO(aggregatedCHO);
                        }));
					} catch (Exception e) {
						System.err.println(String.format("getSourceAvailableScans %s", e));
					}
				}if (Objects.nonNull(present.getSourceDigitalOriginal())) {
                    try {
                        Optional.ofNullable(present.getSourceDigitalOriginal()).ifPresent(p->{
                            aggregation.setAbout(p);

                            AggregatedCHO aggregatedCHO = new AggregatedCHO();
                            aggregatedCHO.setResource(IriToUri
                                    .iriToUri(String.format("ProvidedCHO:%s", StringUtils.deleteWhitespace(identifier)))
                                    .toString());

                            IsShownAt isShownAt = new IsShownAt();
                            isShownAt.setResource(p);

                            aggregation.setIsShownAt(isShownAt);

                            aggregation.setAggregatedCHO(aggregatedCHO);
                        });
                    } catch (Exception e) {
                        System.err.println(String.format("getSourceAvailableScans %s", e));
                    }
                }else {
					try {
						aggregation.setAbout(IriToUri.iriToUri(
								String.format("Aggregation:ProvidedCHO:%s", StringUtils.deleteWhitespace(identifier)))
								.toString());

						AggregatedCHO aggregatedCHO = new AggregatedCHO();
						aggregatedCHO.setResource(
								IriToUri.iriToUri(String.format("ProvidedCHO:%s", StringUtils.deleteWhitespace(identifier)))
										.toString());

						aggregation.setAggregatedCHO(aggregatedCHO);
					} catch (Exception e) {
						System.err.println(String.format("else %s", e));
					}
				}

				Optional.ofNullable(properties).map((Map<String, String> m) -> m.get("dataProvider")).ifPresent((String data) -> {
					DataProvider dataProvider = new DataProvider();
					dataProvider.setString(data);

					aggregation.setDataProvider(dataProvider);
				});

				Optional.ofNullable(properties).map((Map<String, String> m) -> m.get("provider")).ifPresent((String data) -> {
					Provider provider = new Provider();
					provider.setString(data);

					aggregation.setProvider(provider);
				});

				Optional.ofNullable(properties).map((Map<String, String> m) -> m.get("rights")).ifPresent((String data) -> {
					Rights1 rights = new Rights1();
					rights.setResource(IriToUri.iriToUri(data).toString());
					aggregation.setRights(rights);
				});

				Optional.ofNullable(properties).map((Map<String, String> m) -> m.get("set")).ifPresent((String data) -> {
					IntermediateProvider intermediate = new IntermediateProvider();
					intermediate.setString(data);

					aggregation.getIntermediateProviderList().add(intermediate);
				});

				identifiers.forEach((String id) -> {
					HasView hasView = new HasView();
					hasView.setResource(id);

					aggregation.getHasViewList().add(hasView);
				});
				choice.setAggregation(aggregation);
				this.getChoiceList().add(choice);
			});
		}catch (Exception exception) {
			logger.error(String.format("[%s] error generate oreAggregation \n%s", identifier, exception));
		}
	}

    @Override
    public XSLTTransformations transformation(OutputStream out, Map<String, String> xsltProperties) throws Exception {
        throw new IllegalArgumentException("transformation is not valid for A2A2EDM!");
    }

    @Override
    public XSLTTransformations transformation(String xslt, OutputStream out, Map<String, String> xsltProperties) throws Exception {
        throw new IllegalArgumentException("transformation is not valid for A2A2EDM!");
    }

    @Override
    public XSLTTransformations transformation(String xslt) throws Exception {
        throw new IllegalArgumentException("transformation is not valid for A2A2EDM!");
    }

    @Override
    public void creation() {
        if (!Objects.equals(this, new RDF()))
            JibxMarshall.marshall(this, StandardCharsets.UTF_8.toString(),
                    false, IoBuilder.forLogger(A2A2EDM.class).setLevel(Level.INFO).buildOutputStream(), RDF.class, -1);
    }

    @Override
    public void creation(Charset encoding, boolean alone, OutputStream outs) {
        if (!Objects.equals(this, new RDF()))
            JibxMarshall.marshall(this, encoding.toString(), alone, outs, RDF.class, -1);
    }

    @Override
    public void creation(Charset encoding, boolean alone, Writer writer) {
        if (!Objects.equals(this, new RDF()))
            JibxMarshall.marshall(this, encoding.toString(), alone, writer, RDF.class, -1);
    }

    @Override
    public JibxUnMarshall validateSchema(InputStream ins, Charset enc, Class<?> classType) {
        return new JibxUnMarshall(ins, enc, classType);
    }

    @Override
    public JibxUnMarshall validateSchema(InputStream ins, String name, Charset enc, Class<?> classType) {
        return new JibxUnMarshall(ins, name, enc, classType);
    }

    @Override
    public JibxUnMarshall validateSchema(Reader rdr, Class<?> classType) {
        return new JibxUnMarshall(rdr, classType);
    }

    @Override
    public JibxUnMarshall validateSchema(Reader rdr, String name, Class<?> classType) {
        return new JibxUnMarshall(rdr, name, classType);
    }

    @Override
    public void modify(RDF rdf) {

    }
}
