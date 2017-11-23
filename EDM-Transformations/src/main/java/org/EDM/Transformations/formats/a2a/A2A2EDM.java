/**
 * 
 */
package org.EDM.Transformations.formats.a2a;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.EDM.Transformations.formats.EDM;
import org.EDM.Transformations.serialize.JibxMarshall;
import org.apache.commons.lang3.StringUtils;

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
import nl.mindbus.a2a.A2AType;

/**
 * @author amartinez
 *
 */
public class A2A2EDM extends RDF implements EDM {

	private A2AType a2a;
	private String identifier;
	private OutputStream out;

	private Set<String> identifiers = new HashSet<String>();
	private Map<String, String> properties = new HashMap<String, String>();

	/**
	 * 
	 * @param identifier
	 * @param edmType
	 * @param a2atype
	 * @param properties
	 * @param outs
	 */
	public A2A2EDM(String identifier, A2AType a2atype, Map<String, String> properties, OutputStream outs) {
		this.identifier = identifier;
		this.a2a = a2atype;
		this.properties = properties;
		this.out = outs;

		edmProvidedCHO();
		edmAgent();
		edmPlace();
		skosConcept();
		edmTimeSpan();
		edmWebResource();
		oreAggregation();
	}

	@Override
	public void edmAgent() {
		Optional.ofNullable(a2a.getPerson()).map(m -> m.stream()).ifPresent(p -> {
			p.forEach(person -> {
				Choice choice = new Choice();

				AgentType a = new AgentType();

				Optional.ofNullable(person.getPersonName()).map(m -> m.getPersonNameFirstName()).ifPresent(present -> {
					PrefLabel preflabel = new PrefLabel();
					preflabel.setString(present.getValue());
					a.getPrefLabelList().add(preflabel);
				});

				Optional.ofNullable(person.getPersonName()).map(m -> m.getPersonNameLastName()).ifPresent(present -> {
					AltLabel altlabel = new AltLabel();
					altlabel.setString(present.getValue());
					a.getAltLabelList().add(altlabel);
				});

				Optional.ofNullable(person.getPid()).ifPresent(present -> {
					identifiers.add(present);

					a.setAbout(person.getPid());

					Identifier identifier = new Identifier();
					identifier.setString(person.getPid());
					a.getIdentifierList().add(identifier);

					IsRelatedTo isRelated = new IsRelatedTo();
					Resource resourceIsRelated = new Resource();
					resourceIsRelated.setResource(IriToUri.iriToUri(String.format("Concept:%s",
							StringUtils.deleteWhitespace(a2a.getRelationEP().stream()
									.filter(f -> f.getPersonKeyRef().equals(person.getPid())).findFirst().get()
									.getRelationType())))
							.toString());

					isRelated.setResource(resourceIsRelated);
					isRelated.setString(new String());
					a.getIsRelatedToList().add(isRelated);
				});

				Optional.ofNullable(person.getResidence()).map(m -> m.getPlace()).ifPresent(present -> {
					HasMet residence = new HasMet();

					residence.setResource(IriToUri
							.iriToUri(String.format("Place:%s", StringUtils.deleteWhitespace(present.getValue())))
							.toString());
					a.getHasMetList().add(residence);

					PlaceOfBirth placeOfBirth = new PlaceOfBirth();
					Resource resourcePlaceOfBirth = new Resource();
					resourcePlaceOfBirth.setResource(IriToUri
							.iriToUri(String.format("Place:%s", StringUtils.deleteWhitespace(present.getValue())))
							.toString());
					placeOfBirth.setResource(resourcePlaceOfBirth);
					placeOfBirth.setString(new String());
					a.setPlaceOfBirth(placeOfBirth);
				});

				Optional.ofNullable(person.getBirthDate()).filter(f -> Objects.nonNull(f.getYear())
						&& Objects.nonNull(f.getMonth()) && Objects.nonNull(f.getDay())).ifPresent(present -> {

							DateOfBirth birth = new DateOfBirth();
							birth.setString(String.format("%s-%s-%s", present.getYear().getValue(),
									present.getMonth().getValue(), present.getDay().getValue()));
							a.setDateOfBirth(birth);
						});

				Optional.ofNullable(person.getGender()).ifPresent(present -> {
					Gender gender = new Gender();
					gender.setString(present);
					a.setGender(gender);
				});

				Optional.ofNullable(person.getProfession()).ifPresent(present -> {
					present.stream().forEach(profession -> {
						ProfessionOrOccupation professionOrOccupation = new ProfessionOrOccupation();
						professionOrOccupation.setString(profession.getValue());

						a.getProfessionOrOccupationList().add(professionOrOccupation);
					});
				});

				choice.setAgent(a);
				this.getChoiceList().add(choice);
			});
		});
	}

	@Override
	public void edmProvidedCHO() {
		Optional.ofNullable(a2a.getSource()).ifPresent(source -> {

			Choice choice = new Choice();

			ProvidedCHOType provided = new ProvidedCHOType();

			Optional.ofNullable(identifier).ifPresent(present -> {
				eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();

				String iriToUri = IriToUri
						.iriToUri(String.format("ProvidedCHO:%s", StringUtils.deleteWhitespace(identifier))).toString();

				provided.setAbout(iriToUri);

				identifiers.add(iriToUri);

				Identifier id = new Identifier();
				id.setString(identifier);

				c.setIdentifier(id);
				provided.getChoiceList().add(c);
			});

			Optional.ofNullable(source.getSourcePlace()).map(m -> m.getPlace()).ifPresent(present -> {
				eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();

				Coverage coverage = new Coverage();
				Resource coverageResource = new Resource();
				coverageResource.setResource(
						IriToUri.iriToUri(String.format("Place:%s", StringUtils.deleteWhitespace(present.getValue())))
								.toString());
				coverage.setResource(coverageResource);
				coverage.setString(new String());

				c.setCoverage(coverage);
				provided.getChoiceList().add(c);
			});

			Optional.ofNullable(source.getSourceReference()).ifPresent(present -> {
				eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();

				Description description = new Description();
				description.setString(present.getCollection());

				c.setDescription(description);
				c.clearChoiceListSelect();

				Title title = new Title();
				title.setString(present.getBook());

				c.setTitle(title);
				provided.getChoiceList().add(c);
			});

			Optional.ofNullable(source.getSourceType()).ifPresent(present -> {
				eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();

				Type1 type = new Type1();
				type.setString(present);

				c.setType(type);
				provided.getChoiceList().add(c);
			});

			Optional.ofNullable(source.getSourceIndexDate()).ifPresent(present -> {
				Set<String> setDate = new HashSet<String>();
				setDate.add(present.getFrom().toString());
				setDate.add(present.getTo().toString());

				Set<Integer> setYear = new HashSet<Integer>();
				setYear.add(present.getFrom().getYear());
				setYear.add(present.getTo().getYear());

				setDate.stream().filter(Objects::nonNull).forEach(d -> {
					eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();

					Date date = new Date();
					date.setString(d);

					c.setDate(date);
					provided.getChoiceList().add(c);
				});

				setYear.stream().filter(Objects::nonNull).forEach(y -> {
					eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();

					Temporal temporal = new Temporal();
					Resource resource = new Resource();
					resource.setResource(
							IriToUri.iriToUri(String.format("TimeSpan:%s", StringUtils.deleteWhitespace(y.toString())))
									.toString());

					temporal.setResource(resource);
					temporal.setString(new String());
					c.setTemporal(temporal);
					provided.getChoiceList().add(c);
				});
			});

			Optional.ofNullable(a2a.getPerson()).map(m -> m.stream()).ifPresent(present -> {
				present.map(m -> m.getPid()).forEach(person -> {
					eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();

					Contributor contributor = new Contributor();
					Resource resource = new Resource();
					resource.setResource(IriToUri
							.iriToUri(String.format("Agent:%s", StringUtils.deleteWhitespace(person))).toString());
					contributor.setResource(resource);
					contributor.setString(new String());

					c.setContributor(contributor);
					provided.getChoiceList().add(c);
				});
			});

			Optional.ofNullable(source.getSourceType()).ifPresent(present -> {
				IsRelatedTo isRelatedTo = new IsRelatedTo();
				Resource resource = new Resource();
				resource.setResource(IriToUri
						.iriToUri(String.format("Concept:%s", StringUtils.deleteWhitespace(present))).toString());
				isRelatedTo.setResource(resource);
				isRelatedTo.setString(new String());

				provided.getIsRelatedToList().add(isRelatedTo);
			});

			Optional.of(properties).map(m -> m.get("language")).ifPresent(present -> {
				eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();

				Language language = new Language();
				language.setString(LanguageCodes.convert(present).xmlValue());

				c.setLanguage(language);
				provided.getChoiceList().add(c);
			});

			Optional.of(properties).map(m -> m.get("edmType")).ifPresent(present -> {
				Type2 t = new Type2();
				t.setType(EdmType.convert(present));

				provided.setType(t);
			});

			choice.setProvidedCHO(provided);
			this.getChoiceList().add(choice);
		});
	}

	@Override
	public void skosConcept() {
		Optional.ofNullable(a2a.getRelationEP()).ifPresent(present -> {
			present.forEach(relationType -> {
				eu.europeana.corelib.definitions.jibx.RDF.Choice choice = new eu.europeana.corelib.definitions.jibx.RDF.Choice();

				Concept concept = new Concept();

				Optional.ofNullable(relationType.getRelationType()).ifPresent(type -> {
					String iriToUri = IriToUri.iriToUri(
							String.format("Concept:%s", StringUtils.deleteWhitespace(relationType.getRelationType())))
							.toString();

					concept.setAbout(iriToUri);
					identifiers.add(iriToUri);

					eu.europeana.corelib.definitions.jibx.Concept.Choice c = new eu.europeana.corelib.definitions.jibx.Concept.Choice();
					PrefLabel prefLabel = new PrefLabel();
					prefLabel.setString(type);

					c.setPrefLabel(prefLabel);
					concept.getChoiceList().add(c);

				});

				Optional.ofNullable(relationType.getPersonKeyRef()).ifPresent(keyRef -> {
					eu.europeana.corelib.definitions.jibx.Concept.Choice c = new eu.europeana.corelib.definitions.jibx.Concept.Choice();
					Related related = new Related();
					related.setResource(IriToUri
							.iriToUri(String.format("Agent:%s", StringUtils.deleteWhitespace(keyRef))).toString());

					c.setRelated(related);
					concept.getChoiceList().add(c);

				});
				choice.setConcept(concept);
				this.getChoiceList().add(choice);
			});
		});
	}

	@Override
	public void edmPlace() {
		Optional.ofNullable(a2a.getPerson()).map(m -> m.stream()).ifPresent(present -> {
			present.forEach(person -> {
				eu.europeana.corelib.definitions.jibx.RDF.Choice choice = new eu.europeana.corelib.definitions.jibx.RDF.Choice();
				Set<String> place = new HashSet<String>();
				Optional.ofNullable(person.getResidence()).map(m -> m.getPlace()).ifPresent(p -> {
					place.add(p.getValue());
				});
				Optional.ofNullable(person.getBirthPlace()).map(m -> m.getPlace()).ifPresent(p -> {
					place.add(p.getValue());
				});

				place.stream().filter(Objects::nonNull).forEach(p -> {
					PlaceType placeType = new PlaceType();

					String iriToUri = IriToUri.iriToUri(String.format("Place:%s", StringUtils.deleteWhitespace(p)))
							.toString();

					placeType.setAbout(iriToUri);

					placeType.setAbout(iriToUri);
					identifiers.add(iriToUri);

					PrefLabel prefLabel = new PrefLabel();
					prefLabel.setString(p);

					placeType.getPrefLabelList().add(prefLabel);

					choice.setPlace(placeType);
					this.getChoiceList().add(choice);
				});
			});
		});

		Optional.ofNullable(a2a.getSource()).map(m -> m.getSourcePlace()).map(m -> m.getPlace()).ifPresent(present -> {
			eu.europeana.corelib.definitions.jibx.RDF.Choice choice = new eu.europeana.corelib.definitions.jibx.RDF.Choice();

			PlaceType placeType = new PlaceType();

			String iriToUri = IriToUri
					.iriToUri(String.format("Place:%s", StringUtils.deleteWhitespace(present.getValue()))).toString();

			placeType.setAbout(iriToUri);
			identifiers.add(iriToUri);

			PrefLabel prefLabel = new PrefLabel();
			prefLabel.setString(present.getValue());

			placeType.getPrefLabelList().add(prefLabel);

			choice.setPlace(placeType);
			this.getChoiceList().add(choice);
		});
	}

	@Override
	public void edmTimeSpan() {
		Optional.ofNullable(a2a.getSource()).map(m -> m.getSourceIndexDate()).ifPresent(present -> {
			Set<Integer> setYear = new HashSet<Integer>();
			setYear.add(present.getFrom().getYear());
			setYear.add(present.getTo().getYear());

			setYear.stream().filter(Objects::nonNull).forEach(y -> {
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
	}

	/**
	 * 
	 */
	@Override
	public void edmWebResource() {
		Optional.ofNullable(a2a.getSource()).map(m -> m.getSourceAvailableScans()).map(m -> m.getScan())
				.ifPresent(present -> {
					present.forEach(ctScan -> {
						eu.europeana.corelib.definitions.jibx.RDF.Choice choice = new eu.europeana.corelib.definitions.jibx.RDF.Choice();
						WebResourceType webResource = new WebResourceType();
						String iriToUri = IriToUri.iriToUri(ctScan.getUri()).toString();
						identifiers.add(iriToUri);

						webResource.setAbout(iriToUri);
						choice.setWebResource(webResource);
						this.getChoiceList().add(choice);
					});
				});
	}

	@Override
	public void oreAggregation() {
		Optional.ofNullable(a2a.getSource()).ifPresent(present -> {
			eu.europeana.corelib.definitions.jibx.RDF.Choice choice = new eu.europeana.corelib.definitions.jibx.RDF.Choice();

			Aggregation aggregation = new Aggregation();

			if (Objects.nonNull(present.getSourceAvailableScans())) {
				try {
					Optional.ofNullable(present.getSourceAvailableScans().getScan()).ifPresent(p -> {
						p.forEach(scan -> {
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
						});
					});
				} catch (Exception e) {
					System.err.println(String.format("getSourceAvailableScans %s", e));
				}
			} else {
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

			Optional.of(properties).map(m -> m.get("dataProvider")).ifPresent(data -> {
				DataProvider dataProvider = new DataProvider();
				dataProvider.setString(data);

				aggregation.setDataProvider(dataProvider);
			});

			Optional.of(properties).map(m -> m.get("provider")).ifPresent(data -> {
				Provider provider = new Provider();
				provider.setString(data);

				aggregation.setProvider(provider);
			});

			Optional.of(properties).map(m -> m.get("rights")).ifPresent(data -> {
				Rights1 rights = new Rights1();
				rights.setResource(IriToUri.iriToUri(data).toString());
				aggregation.setRights(rights);
			});

			Optional.of(properties).map(m -> m.get("set")).ifPresent(data -> {
				IntermediateProvider intermediate = new IntermediateProvider();
				intermediate.setString(data);

				aggregation.getIntermediateProviderList().add(intermediate);
			});

			identifiers.forEach(id -> {
				HasView hasView = new HasView();
				hasView.setResource(id);

				aggregation.getHasViewList().add(hasView);
			});
			choice.setAggregation(aggregation);
			this.getChoiceList().add(choice);
		});
	}

	@Override
	public void marshal(Charset encoding, boolean alone) {
		if (Objects.nonNull(this) && !Objects.equals(this, new RDF()))
			JibxMarshall.marshall(this, "UTF-8", true, out, RDF.class);
	}

}
