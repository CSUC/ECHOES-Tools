/**
 * 
 */
package org.EDM.Transformations.formats.dc;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

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

/**
 * @author amartinez
 *
 */
public class DC2EDM extends RDF implements EDM {

	private static Logger logger = LogManager.getLogger(DC2EDM.class);

	private OaiDcType oaiDcType;
	private String identifier;
	private OutputStream out;

	private Map<String, String> properties = new HashMap<String, String>();

	private Set<String> identifiers = new HashSet<String>();
	private Set<String> dates = new HashSet<String>();
	private Set<String> subjects = new HashSet<String>();

	public DC2EDM(String identifier, OaiDcType dcType, Map<String, String> properties, OutputStream outs) {
		this.identifier = identifier;
		this.oaiDcType = dcType;
		this.properties = properties;
		this.out = outs;

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

		Optional.ofNullable(oaiDcType.getTitleOrCreatorOrSubject()).ifPresent(present -> {
			present.forEach(elementType -> {
				String localPart = elementType.getName().getLocalPart();
				if (localPart.equals("description")) {
					Description description = new Description();
					description.setString(elementType.getValue().getValue());
					eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();
					c.setDescription(description);
					provided.getChoiceList().add(c);
				} else if (localPart.equals("publisher")) {
					Publisher publisher = new Publisher();
					publisher.setString(elementType.getValue().getValue());
					eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();
					c.setPublisher(publisher);
					provided.getChoiceList().add(c);
				} else if (localPart.equals("relation")) {
					Relation relation = new Relation();
					relation.setString(elementType.getValue().getValue());
					eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();
					c.setRelation(relation);
					provided.getChoiceList().add(c);
				} else if (localPart.equals("creator")) {
					Creator creator = new Creator();
					creator.setString(elementType.getValue().getValue());
					eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();
					c.setCreator(creator);
					provided.getChoiceList().add(c);
				} else if (localPart.equals("type")) {
					Type1 type = new Type1();
					type.setString(elementType.getValue().getValue());
					eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();
					c.setType(type);
					provided.getChoiceList().add(c);
				} else if (localPart.equals("source")) {
					Source source = new Source();
					source.setString(elementType.getValue().getValue());
					eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();
					c.setSource(source);
					provided.getChoiceList().add(c);
				} else if (localPart.equals("language")) {
					Language language = new Language();
					language.setString(LanguageCodes.convert(elementType.getValue().getValue()).xmlValue());
					eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();
					c.setLanguage(language);
					provided.getChoiceList().add(c);
				} else if (localPart.equals("format")) {
					Format format = new Format();
					format.setString(elementType.getValue().getValue());
					eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();
					c.setFormat(format);
					provided.getChoiceList().add(c);
				} else if (localPart.equals("coverage")) {
					Coverage coverage = new Coverage();
					coverage.setString(elementType.getValue().getValue());
					eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();
					c.setCoverage(coverage);
					provided.getChoiceList().add(c);
				} else if (localPart.equals("identifier")) {
					Identifier id = new Identifier();
					id.setString(elementType.getValue().getValue());
					eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();
					c.setIdentifier(id);
					provided.getChoiceList().add(c);
				} else if (localPart.equals("subject")) {
					subjects.add(elementType.getValue().getValue());
					Subject subject = new Subject();
					subject.setString(elementType.getValue().getValue());
					eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();
					c.setSubject(subject);
					provided.getChoiceList().add(c);
				} else if (localPart.equals("date")) {
					dates.add(elementType.getValue().getValue());
					Date date = new Date();
					date.setString(elementType.getValue().getValue());
					eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();
					c.setDate(date);
					provided.getChoiceList().add(c);
				} else if (localPart.equals("rights")) {
					Rights rights = new Rights();
					eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();
					Resource resource = new Resource();
					resource.setResource(elementType.getValue().getValue());
					rights.setResource(resource);
					rights.setString(new String());
					c.setRights(rights);
					provided.getChoiceList().add(c);
				} else if (localPart.equals("title")) {
					Title title = new Title();
					title.setString(elementType.getValue().getValue());
					eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();
					c.setTitle(title);
					provided.getChoiceList().add(c);
				} else if (localPart.equals("contributor")) {
					Contributor contributor = new Contributor();
					contributor.setString(elementType.getValue().getValue());
					eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();
					c.setContributor(contributor);
					provided.getChoiceList().add(c);
				} else {
					System.err.println("UNKNOW metadataType");
				}

				Optional.of(properties).map(m -> m.get("edmType")).ifPresent(edmType -> {
					Type2 t = new Type2();
					t.setType(EdmType.convert(edmType));

					provided.setType(t);
				});
			});
		});

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
			dates.forEach(date -> {
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
			subjects.forEach(subject -> {
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
	public void marshal(Charset encoding, boolean alone) {
		if (Objects.nonNull(this) && !Objects.equals(this, new RDF()))
			JibxMarshall.marshall(this, encoding.displayName(), alone, out, RDF.class);

	}
}
