/**
 *
 */
package org.EDM.Transformations.formats.dc;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.XMLGregorianCalendar;

import eu.europeana.corelib.definitions.jibx.*;
import eu.europeana.corelib.definitions.jibx.Date;
import eu.europeana.corelib.definitions.jibx.ResourceOrLiteralType.Resource;
import org.EDM.Transformations.formats.EDM;
import org.EDM.Transformations.formats.utils.TimeUtil;
import org.EDM.Transformations.formats.xslt.XSLTTransformations;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.io.IoBuilder;
import org.csuc.deserialize.JibxUnMarshall;
import org.csuc.serialize.JibxMarshall;
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
        skosConcept();
        edmTimeSpan();
        edmWebResource();
        oreAggregation();
    }

    private void edmProvidedCHO() {
        try{
            ProvidedCHOType provided = new ProvidedCHOType();
            Choice choice = new Choice();

            if (Objects.nonNull(identifier)) {
                String iriToUri = IriToUri
                        .iriToUri(String.format("ProvidedCHO:%s", StringUtils.deleteWhitespace(identifier))).toString();

                provided.setAbout(iriToUri);

                getIdentifiers().add(iriToUri);
            }

            Optional.ofNullable(oaiDcType.getTitleOrCreatorOrSubject()).ifPresent((List<JAXBElement<ElementType>> present) -> {
                for (JAXBElement<ElementType> elementType : present) {
                    String localPart = elementType.getName().getLocalPart();

                    switch (localPart) {
                        case "description": {
                            Description description = new Description();
                            description.setString(elementType.getValue().getValue());
                            EuropeanaType.Choice c = new EuropeanaType.Choice();
                            c.setDescription(description);
                            provided.getChoiceList().add(c);
                            break;
                        }
                        case "publisher": {
                            Publisher publisher = new Publisher();
                            publisher.setString(elementType.getValue().getValue());
                            EuropeanaType.Choice c = new EuropeanaType.Choice();
                            c.setPublisher(publisher);
                            provided.getChoiceList().add(c);
                            break;
                        }
                        case "relation": {
                            Relation relation = new Relation();
                            relation.setString(elementType.getValue().getValue());
                            EuropeanaType.Choice c = new EuropeanaType.Choice();
                            c.setRelation(relation);
                            provided.getChoiceList().add(c);
                            break;
                        }
                        case "creator": {
                            Creator creator = new Creator();
                            creator.setString(elementType.getValue().getValue());
                            EuropeanaType.Choice c = new EuropeanaType.Choice();
                            c.setCreator(creator);
                            provided.getChoiceList().add(c);
                            break;
                        }
                        case "type": {
                            Type1 type = new Type1();
                            type.setString(elementType.getValue().getValue());
                            EuropeanaType.Choice c = new EuropeanaType.Choice();
                            c.setType(type);
                            provided.getChoiceList().add(c);
                            break;
                        }
                        case "source": {
                            Source source = new Source();
                            source.setString(elementType.getValue().getValue());
                            EuropeanaType.Choice c = new EuropeanaType.Choice();
                            c.setSource(source);
                            provided.getChoiceList().add(c);
                            break;
                        }
                        case "language":
                            if (Objects.nonNull(LanguageCodes.convert(elementType.getValue().getValue()))) {
                                Language language = new Language();
                                language.setString(LanguageCodes.convert(elementType.getValue().getValue()).xmlValue());
                                EuropeanaType.Choice c = new EuropeanaType.Choice();
                                c.setLanguage(language);
                                provided.getChoiceList().add(c);
                            }
                            break;
                        case "format": {
                            Format format = new Format();
                            format.setString(elementType.getValue().getValue());
                            EuropeanaType.Choice c = new EuropeanaType.Choice();
                            c.setFormat(format);
                            provided.getChoiceList().add(c);
                            break;
                        }
                        case "coverage": {
                            Coverage coverage = new Coverage();
                            coverage.setString(elementType.getValue().getValue());
                            EuropeanaType.Choice c = new EuropeanaType.Choice();
                            c.setCoverage(coverage);
                            provided.getChoiceList().add(c);
                            break;
                        }
                        case "identifier": {
                            Identifier id = new Identifier();
                            id.setString(elementType.getValue().getValue());
                            EuropeanaType.Choice c = new EuropeanaType.Choice();
                            c.setIdentifier(id);
                            provided.getChoiceList().add(c);
                            break;
                        }
                        case "subject": {
                            getSubjects().add(elementType.getValue().getValue());
                            Subject subject = new Subject();
                            Resource resource = new Resource();

                            String iriToUri = IriToUri.iriToUri(String.format("Concept:%s", StringUtils.deleteWhitespace(elementType.getValue().getValue())))
                                    .toString();

                            resource.setResource(iriToUri);
                            subject.setResource(resource);
                            subject.setString("");

                            EuropeanaType.Choice c = new EuropeanaType.Choice();
                            c.setSubject(subject);
                            provided.getChoiceList().add(c);
                            break;
                        }
                        case "date": {
                            try {
                                String resultDMY = TimeUtil.format(elementType.getValue().getValue(), TimeUtil.DAYMONTHYEAR);
                                String resultY = TimeUtil.format(resultDMY, TimeUtil.YEAR);

                                Date date = new Date();
                                date.setString(resultDMY);
                                EuropeanaType.Choice c = new EuropeanaType.Choice();
                                c.setDate(date);
                                provided.getChoiceList().add(c);

                                if(!getDates().contains(String.valueOf(resultY))){
                                    getDates().add(String.valueOf(resultY));

                                    c = new EuropeanaType.Choice();

                                    String iriToUri = IriToUri.iriToUri(String.format("TimeSpan:%s", resultY)).toString();

                                    Temporal temporal = new Temporal();
                                    Resource resource = new Resource();
                                    resource.setResource(iriToUri);
                                    temporal.setResource(resource);
                                    temporal.setString("");

                                    c.setTemporal(temporal);

                                    provided.getChoiceList().add(c);
                                }
                            }catch (Exception e) {
                                logger.error("date: {}", e.getMessage());
                            }

                            break;
                        }
                        case "rights": {
                            Rights rights = new Rights();
                            EuropeanaType.Choice c = new EuropeanaType.Choice();
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
                            EuropeanaType.Choice c = new EuropeanaType.Choice();
                            c.setTitle(title);
                            provided.getChoiceList().add(c);
                            break;
                        }
                        case "contributor": {
                            Contributor contributor = new Contributor();
                            contributor.setString(elementType.getValue().getValue());
                            EuropeanaType.Choice c = new EuropeanaType.Choice();
                            c.setContributor(contributor);
                            provided.getChoiceList().add(c);
                            break;
                        }
                        default:
                            System.err.println("UNKNOW metadataType");
                            break;
                    }

                    Optional.ofNullable(properties).map((Map<String, String> m) -> m.get("edmType")).ifPresent((String edmType) -> {
                        if (Objects.nonNull(EdmType.convert(edmType))) {
                            Type2 t = new Type2();
                            t.setType(EdmType.convert(edmType));

                            provided.setType(t);
                        }
                    });
                }
            });

            choice.setProvidedCHO(provided);
            this.getChoiceList().add(choice);
        } catch (Exception exception) {
            logger.error(String.format("[%s] error generate edmProvidedCHO \n%s", identifier, exception));
        }
    }

    private void edmAgent() {

    }

    private void edmPlace() {

    }

    private void edmTimeSpan() {
        try{
            if (!getDates().isEmpty()) {
                getDates().forEach((String date) -> {
                    try {
                        String resultY = TimeUtil.format(date, TimeUtil.YEAR);

                        eu.europeana.corelib.definitions.jibx.RDF.Choice choice = new eu.europeana.corelib.definitions.jibx.RDF.Choice();
                        TimeSpanType timeSpan = new TimeSpanType();

                        String iriToUri = IriToUri.iriToUri(String.format("TimeSpan:%s", resultY)).toString();

                        timeSpan.setAbout(iriToUri);
                        getIdentifiers().add(iriToUri);

                        PrefLabel prefLabel = new PrefLabel();
                        prefLabel.setString(resultY);

                        timeSpan.getPrefLabelList().add(prefLabel);

                        choice.setTimeSpan(timeSpan);
                        this.getChoiceList().add(choice);

                    } catch (Exception e) {
                        logger.error(String.format("date %s not cast", date));
                    }
                });
            }
        }catch (Exception exception) {
            logger.error(String.format("[%s] error generate edmTimeSpan \n%s", identifier, exception));
        }
    }

    private void skosConcept() {
        try {
            if (!getSubjects().isEmpty()) {
                getSubjects().forEach((String subject) -> {
                    eu.europeana.corelib.definitions.jibx.RDF.Choice choice = new eu.europeana.corelib.definitions.jibx.RDF.Choice();
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

    private void edmWebResource() {

    }

    private void oreAggregation() {
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

    @Override
    public XSLTTransformations transformation(OutputStream out, Map<String, String> xsltProperties) throws Exception {
        throw new IllegalArgumentException("transformation is not valid for DC2EDM!");
    }

    @Override
    public XSLTTransformations transformation(String xslt, OutputStream out, Map<String, String> xsltProperties) throws Exception {
        throw new IllegalArgumentException("transformation is not valid for DC2EDM!");
    }

    @Override
    public XSLTTransformations transformation(String xslt) throws Exception {
        throw new IllegalArgumentException("transformation is not valid for DC2EDM!");
    }

    @Override
    public void creation() {
        if (!Objects.equals(this, new RDF()))
            JibxMarshall.marshall(this, StandardCharsets.UTF_8.toString(),
                    false, IoBuilder.forLogger(DC2EDM.class).setLevel(Level.INFO).buildOutputStream(), RDF.class, -1);
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
