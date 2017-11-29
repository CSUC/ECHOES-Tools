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

import eu.europeana.corelib.definitions.jibx.AggregatedCHO;
import eu.europeana.corelib.definitions.jibx.Aggregation;
import eu.europeana.corelib.definitions.jibx.Concept;
import eu.europeana.corelib.definitions.jibx.Contributor;
import eu.europeana.corelib.definitions.jibx.Coverage;
import eu.europeana.corelib.definitions.jibx.DataProvider;
import eu.europeana.corelib.definitions.jibx.Date;
import eu.europeana.corelib.definitions.jibx.Description;
import eu.europeana.corelib.definitions.jibx.EdmType;
import eu.europeana.corelib.definitions.jibx.HasView;
import eu.europeana.corelib.definitions.jibx.Identifier;
import eu.europeana.corelib.definitions.jibx.IntermediateProvider;
import eu.europeana.corelib.definitions.jibx.Language;
import eu.europeana.corelib.definitions.jibx.LanguageCodes;
import eu.europeana.corelib.definitions.jibx.PrefLabel;
import eu.europeana.corelib.definitions.jibx.ProvidedCHOType;
import eu.europeana.corelib.definitions.jibx.Provider;
import eu.europeana.corelib.definitions.jibx.RDF;
import eu.europeana.corelib.definitions.jibx.TimeSpanType;
import eu.europeana.corelib.definitions.jibx.ResourceOrLiteralType.Resource;
import eu.europeana.corelib.definitions.jibx.Rights1;
import eu.europeana.corelib.definitions.jibx.Title;
import eu.europeana.corelib.definitions.jibx.Type1;
import eu.europeana.corelib.definitions.jibx.Type2;
import eu.europeana.corelib.definitions.jibx.Creator;
import eu.europeana.corelib.definitions.jibx.Format;
import eu.europeana.corelib.definitions.jibx.Publisher;
import eu.europeana.corelib.definitions.jibx.Relation;
import eu.europeana.corelib.definitions.jibx.Rights;
import eu.europeana.corelib.definitions.jibx.Source;
import eu.europeana.corelib.definitions.jibx.Subject;
import org.EDM.Transformations.formats.EDM;
import org.EDM.Transformations.serialize.JibxMarshall;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openarchives.oai._2_0.oai_dc.OaiDcType;

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

    private Set<String> identifiers;
    private Set<String> dates;
    private Set<String> subjects;

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
        try{
            ProvidedCHOType provided = new ProvidedCHOType();
            Choice choice = new Choice();

            if (Objects.nonNull(identifier)) {
                String iriToUri = IriToUri
                        .iriToUri(String.format("ProvidedCHO:%s", StringUtils.deleteWhitespace(identifier))).toString();

                provided.setAbout(iriToUri);

                getIdentifiers().add(iriToUri);
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
                        getSubjects().add(elementType.getValue().getValue());
                        Subject subject = new Subject();
                        subject.setString(elementType.getValue().getValue());
                        eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice c = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();
                        c.setSubject(subject);
                        provided.getChoiceList().add(c);
                        break;
                    }
                    case "date": {
                        getDates().add(elementType.getValue().getValue());
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
        } catch (Exception exception) {
            logger.error(String.format("[%s] error generate edmProvidedCHO \n%s", identifier, exception));
        }
    }

    @Override
    public void edmAgent() {

    }

    @Override
    public void edmPlace() {

    }

    @Override
    public void edmTimeSpan() {
        try{
            if (!getDates().isEmpty()) {
                getDates().forEach((String date) -> {
                    try {
                        XMLGregorianCalendar result = DatatypeFactory.newInstance().newXMLGregorianCalendar(date);

                        eu.europeana.corelib.definitions.jibx.RDF.Choice choice = new eu.europeana.corelib.definitions.jibx.RDF.Choice();
                        TimeSpanType timeSpan = new TimeSpanType();

                        String iriToUri = IriToUri.iriToUri(String.format("TimeSpan:%s", result.getYear())).toString();

                        timeSpan.setAbout(iriToUri);
                        getIdentifiers().add(iriToUri);

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
        }catch (Exception exception) {
            logger.error(String.format("[%s] error generate edmTimeSpan \n%s", identifier, exception));
        }
    }

    @Override
    public void skosConcept() {
        try {
            if (!getSubjects().isEmpty()) {
                eu.europeana.corelib.definitions.jibx.RDF.Choice choice = new eu.europeana.corelib.definitions.jibx.RDF.Choice();
                getSubjects().forEach((String subject) -> {
                    Concept concept = new Concept();

                    String iriToUri = IriToUri.iriToUri(String.format("Concept:%s", StringUtils.deleteWhitespace(subject)))
                            .toString();

                    concept.setAbout(iriToUri);
                    getIdentifiers().add(iriToUri);

                    PrefLabel prefLabel = new PrefLabel();
                    prefLabel.setString(subject);
                    eu.europeana.corelib.definitions.jibx.Concept.Choice c = new eu.europeana.corelib.definitions.jibx.Concept.Choice();
                    c.setPrefLabel(prefLabel);

                    concept.getChoiceList().add(c);

                    choice.setConcept(concept);
                    this.getChoiceList().add(choice);
                });
            }
        } catch (Exception exception) {
            logger.error(String.format("[%s] error generate skosConcept \n%s", identifier, exception));
        }
    }

    @Override
    public void edmWebResource() {

    }

    @Override
    public void oreAggregation() {
        try {
            Choice choice = new Choice();
            Aggregation aggregation = new Aggregation();

            aggregation.setAbout(IriToUri.iriToUri(
                    String.format("Aggregation:ProvidedCHO:%s", StringUtils.deleteWhitespace(identifier)))
                    .toString());

            AggregatedCHO aggregatedCHO = new AggregatedCHO();
            aggregatedCHO.setResource(
                    IriToUri.iriToUri(String.format("ProvidedCHO:%s", StringUtils.deleteWhitespace(identifier)))
                            .toString());

            aggregation.setAggregatedCHO(aggregatedCHO);

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

            getIdentifiers().forEach((String id) -> {
                HasView hasView = new HasView();
                hasView.setResource(id);

                aggregation.getHasViewList().add(hasView);
            });
            choice.setAggregation(aggregation);
            this.getChoiceList().add(choice);

        }catch (Exception exception) {
            logger.error(String.format("[%s] error generate oreAggregation \n%s", identifier, exception));
        }
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

    private Set<String> getIdentifiers() {
        if(Objects.isNull(identifiers)) identifiers = new HashSet<>();
        return identifiers;
    }

    private Set<String> getDates() {
        if(Objects.isNull(dates)) dates = new HashSet<>();
        return dates;
    }

    private Set<String> getSubjects() {
        if(Objects.isNull(subjects)) subjects = new HashSet<>();
        return subjects;
    }
}
