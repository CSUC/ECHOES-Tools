/**
 * 
 */
package org.EDM.Transformations.formats.dc;

import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.*;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.EDM.Transformations.formats.EDM;
import org.EDM.Transformations.serialize.JibxMarshall;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openarchives.oai._2_0.oai_dc.OaiDcType;

import eu.europeana.corelib.definitions.jibx.Concept;
import eu.europeana.corelib.definitions.jibx.Contributor;
import eu.europeana.corelib.definitions.jibx.Coverage;
import eu.europeana.corelib.definitions.jibx.Creator;
import eu.europeana.corelib.definitions.jibx.Date;
import eu.europeana.corelib.definitions.jibx.Description;
import eu.europeana.corelib.definitions.jibx.EdmType;
import eu.europeana.corelib.definitions.jibx.Format;
import eu.europeana.corelib.definitions.jibx.Identifier;
import eu.europeana.corelib.definitions.jibx.Language;
import eu.europeana.corelib.definitions.jibx.LanguageCodes;
import eu.europeana.corelib.definitions.jibx.PrefLabel;
import eu.europeana.corelib.definitions.jibx.ProvidedCHOType;
import eu.europeana.corelib.definitions.jibx.Publisher;
import eu.europeana.corelib.definitions.jibx.RDF;
import eu.europeana.corelib.definitions.jibx.Relation;
import eu.europeana.corelib.definitions.jibx.ResourceOrLiteralType.Resource;
import eu.europeana.corelib.definitions.jibx.Rights;
import eu.europeana.corelib.definitions.jibx.Source;
import eu.europeana.corelib.definitions.jibx.Subject;
import eu.europeana.corelib.definitions.jibx.TimeSpanType;
import eu.europeana.corelib.definitions.jibx.Title;
import eu.europeana.corelib.definitions.jibx.Type1;
import eu.europeana.corelib.definitions.jibx.Type2;
import net.sf.saxon.functions.IriToUri;
import org.purl.dc.elements._1.ElementType;

/**
 * @author amartinez
 *
 */
public class DC2EDM extends RDF implements EDM {

	private static Logger logger = LogManager.getLogger(DC2EDM.class);

	private OaiDcType oaiDcType;
	private String identifier;

	private Map<String, String> properties = new HashMap<>();

	private Set<String> identifiers = new HashSet<>();
	private Set<String> dates = new HashSet<>();
	private Set<String> subjects = new HashSet<>();

	public DC2EDM(String identifier, OaiDcType dcType, Map<String, String> properties) {
		this.identifier = identifier;
		this.oaiDcType = dcType;
		this.properties = properties;

		edmProvidedCHO();
		edmAgent();
		edmPlace();
		edmTimeSpan();
		skosConcept();
		edmWebResource();
		oreAggregation();
	}

	@Override
	public void edmProvidedCHO() {
		ProvidedCHOType provided = new ProvidedCHOType();
		Choice choice = new Choice();

		if (Objects.nonNull(identifier)) {
			String iriToUri = IriToUri
					.iriToUri(String.format("ProvidedCHO:%s", StringUtils.deleteWhitespace(identifier))).toString();

			provided.setAbout(iriToUri);

			identifiers.add(iriToUri);
		}

		Optional.ofNullable(oaiDcType.getTitleOrCreatorOrSubject()).ifPresent((List<JAXBElement<ElementType>> present) -> present.forEach((JAXBElement<ElementType> elementType) -> {
            String localPart = elementType.getName().getLocalPart();

			switch (localPart) {
				case "description": {
					Description description = new Description();
					description.setString(elementType.getValue().getValue());
					eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();
					c.setDescription(description);
					provided.getChoiceList().add(c);
					break;
				}
				case "publisher": {
					Publisher publisher = new Publisher();
					publisher.setString(elementType.getValue().getValue());
					eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();
					c.setPublisher(publisher);
					provided.getChoiceList().add(c);
					break;
				}
				case "relation": {
					Relation relation = new Relation();
					relation.setString(elementType.getValue().getValue());
					eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();
					c.setRelation(relation);
					provided.getChoiceList().add(c);
					break;
				}
				case "creator": {
					Creator creator = new Creator();
					creator.setString(elementType.getValue().getValue());
					eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();
					c.setCreator(creator);
					provided.getChoiceList().add(c);
					break;
				}
				case "type": {
					Type1 type = new Type1();
					type.setString(elementType.getValue().getValue());
					eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();
					c.setType(type);
					provided.getChoiceList().add(c);
					break;
				}
				case "source": {
					Source source = new Source();
					source.setString(elementType.getValue().getValue());
					eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();
					c.setSource(source);
					provided.getChoiceList().add(c);
					break;
				}
				case "language":
					if (Objects.nonNull(LanguageCodes.convert(elementType.getValue().getValue()))) {
						Language language = new Language();
						language.setString(LanguageCodes.convert(elementType.getValue().getValue()).xmlValue());
						eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();
						c.setLanguage(language);
						provided.getChoiceList().add(c);
					}
					break;
				case "format": {
					Format format = new Format();
					format.setString(elementType.getValue().getValue());
					eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();
					c.setFormat(format);
					provided.getChoiceList().add(c);
					break;
				}
				case "coverage": {
					Coverage coverage = new Coverage();
					coverage.setString(elementType.getValue().getValue());
					eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();
					c.setCoverage(coverage);
					provided.getChoiceList().add(c);
					break;
				}
				case "identifier": {
					Identifier id = new Identifier();
					id.setString(elementType.getValue().getValue());
					eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();
					c.setIdentifier(id);
					provided.getChoiceList().add(c);
					break;
				}
				case "subject": {
					subjects.add(elementType.getValue().getValue());
					Subject subject = new Subject();
					subject.setString(elementType.getValue().getValue());
					eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();
					c.setSubject(subject);
					provided.getChoiceList().add(c);
					break;
				}
				case "date": {
					dates.add(elementType.getValue().getValue());
					Date date = new Date();
					date.setString(elementType.getValue().getValue());
					eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();
					c.setDate(date);
					provided.getChoiceList().add(c);
					break;
				}
				case "rights": {
					Rights rights = new Rights();
					eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();
					Resource resource = new Resource();
					resource.setResource(elementType.getValue().getValue());
					rights.setResource(resource);
					rights.setString("");
					c.setRights(rights);
					provided.getChoiceList().add(c);
					break;
				}
				case "title": {
					Title title = new Title();
					title.setString(elementType.getValue().getValue());
					eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();
					c.setTitle(title);
					provided.getChoiceList().add(c);
					break;
				}
				case "contributor": {
					Contributor contributor = new Contributor();
					contributor.setString(elementType.getValue().getValue());
					eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();
					c.setContributor(contributor);
					provided.getChoiceList().add(c);
					break;
				}
				default:
					System.err.println("UNKNOW metadataType");
					break;
			}

            Optional.ofNullable(properties).map((Map<String, String> m) -> m.get("edmType")).ifPresent((String edmType) -> {
                if(Objects.nonNull(EdmType.convert(edmType))) {
                    Type2 t = new Type2();
                    t.setType(EdmType.convert(edmType));

                    provided.setType(t);
                }
            });
        }));

		choice.setProvidedCHO(provided);
		this.getChoiceList().add(choice);
	}

	@Override
	public void edmAgent() {

	}

	@Override
	public void edmPlace() {

	}

	@Override
	public void edmTimeSpan() {
		if (!dates.isEmpty()) {
			dates.forEach((String date) -> {
				try {
					XMLGregorianCalendar result = DatatypeFactory.newInstance().newXMLGregorianCalendar(date);

					eu.europeana.corelib.definitions.jibx.RDF.Choice choice = new eu.europeana.corelib.definitions.jibx.RDF.Choice();
					TimeSpanType timeSpan = new TimeSpanType();

					String iriToUri = IriToUri.iriToUri(String.format("TimeSpan:%s", result.getYear())).toString();

					timeSpan.setAbout(iriToUri);
					identifiers.add(iriToUri);

					PrefLabel prefLabel = new PrefLabel();
					prefLabel.setString(Integer.toString(result.getYear()));
					timeSpan.getPrefLabelList().add(prefLabel);

					choice.setTimeSpan(timeSpan);
					this.getChoiceList().add(choice);

				} catch (Exception e) {
					logger.error(String.format("date %s not cast to XMLGregorianCalendar", date));
				}
			});
		}
	}

	@Override
	public void skosConcept() {
		if (!subjects.isEmpty()) {
			eu.europeana.corelib.definitions.jibx.RDF.Choice choice = new eu.europeana.corelib.definitions.jibx.RDF.Choice();
			subjects.forEach((String subject) -> {
				Concept concept = new Concept();

				String iriToUri = IriToUri.iriToUri(String.format("Concept:%s", StringUtils.deleteWhitespace(subject)))
						.toString();

				concept.setAbout(iriToUri);
				identifiers.add(iriToUri);

				PrefLabel prefLabel = new PrefLabel();
				prefLabel.setString(subject);
				eu.europeana.corelib.definitions.jibx.Concept.Choice c = new eu.europeana.corelib.definitions.jibx.Concept.Choice();
				c.setPrefLabel(prefLabel);

				concept.getChoiceList().add(c);

				choice.setConcept(concept);
				this.getChoiceList().add(choice);
			});
		}
	}

	@Override
	public void edmWebResource() {

	}

	@Override
	public void oreAggregation() {

	}

	@Override
	public void marshal(Charset encoding, boolean alone, OutputStream outs) {
		if (!Objects.equals(this, new RDF()))
			JibxMarshall.marshall(this, encoding.displayName(), alone, outs, RDF.class);
	}

	@Override
	public void marshal(Charset encoding, boolean alone, Writer writer) {
		if (!Objects.equals(this, new RDF()))
			JibxMarshall.marshall(this, encoding.displayName(), alone, writer, RDF.class);
	}
}
