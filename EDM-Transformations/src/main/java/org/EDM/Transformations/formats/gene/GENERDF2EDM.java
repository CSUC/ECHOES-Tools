package org.EDM.Transformations.formats.gene;

import cat.gencat.*;
import eu.europeana.corelib.definitions.jibx.*;
import net.sf.saxon.functions.IriToUri;
import org.EDM.Transformations.formats.EDM;
import org.EDM.Transformations.formats.xslt.XSLTTransformations;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.io.IoBuilder;
import org.csuc.deserialize.JibxUnMarshall;
import org.csuc.serialize.JibxMarshall;
import org.osgeo.proj4j.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class GENERDF2EDM extends RDF implements EDM {
    private static Logger logger = LogManager.getLogger(GENERDF2EDM.class);
    private org.w3._1999._02._22_rdf_syntax_ns_.RDF GENERDF;
    //private String identifier;
    private Map<String, String> properties;

    public GENERDF2EDM(/*String identifier, */org.w3._1999._02._22_rdf_syntax_ns_.RDF type, Map<String, String> properties) {
        //this.identifier = identifier;
        this.GENERDF = type;
        this.properties = properties;

        edmProvidedCHO();
        edmAgent();
        edmPlace();
        skosConcept();
        edmTimeSpan();
    }

    private void edmTimeSpan() {
        try {
            // Datacio
            GENERDF.getDatacio().forEach((Datacio datacio) -> {
                Choice choice = new Choice();
                TimeSpanType timeSpan = new TimeSpanType();
                // Datacio[rdf:about]
                timeSpan.setAbout(datacio.getAbout());
                // Datacio -> anyInici
                if (datacio.getAnyInici() != null) {
                    PrefLabel prefLabel = new PrefLabel();
                    prefLabel.setString(yearMinFourDigits(datacio.getAnyInici()));
                    timeSpan.getPrefLabelList().add(prefLabel);
                    Begin begin = new Begin();
                    begin.setString(prefLabel.getString() + "-01-01");
                    timeSpan.setBegin(begin);
                }
                // Datacio -> anyFi
                if (datacio.getAnyFi() != null) {
                    AltLabel altLabel = new AltLabel();
                    altLabel.setString(yearMinFourDigits(datacio.getAnyFi()));
                    timeSpan.getAltLabelList().add(altLabel);
                    End end = new End();
                    end.setString(altLabel.getString() + "-01-01");
                    timeSpan.setEnd(end);
                }
                choice.setTimeSpan(timeSpan);
                this.getChoiceList().add(choice);
            });
        } catch (Exception exception) {
            logger.error(String.format("[%s] error generate edmTimeSpan \n", exception));
        }
    }

    private void skosConcept() {
        try {
            // Us
            GENERDF.getUs().forEach((Us us) -> {
                Choice choice = new Choice();
                Concept concept = new Concept();
                // "Us" + Us -> originalActual
                concept.setAbout("Us:" + IriToUri.iriToUri(StringUtils.deleteWhitespace(us.getOriginalActual())));
                // Us -> originalActual
                PrefLabel prefLabel = new PrefLabel();
                prefLabel.setString(us.getOriginalActual());
                Concept.Choice cChoice = new Concept.Choice();
                cChoice.setPrefLabel(prefLabel);
                concept.getChoiceList().add(cChoice);
                // Relation with Identificacio
                cChoice = new Concept.Choice();
                Related related = new Related();
                related.setResource(us.getIdentificador().getResource());
                cChoice.setRelated(related);
                concept.getChoiceList().add(cChoice);

                choice.setConcept(concept);
                this.getChoiceList().add(choice);
            });
        } catch (Exception exception) {
            logger.error(String.format("[%s] error generate skosConcept \n", exception));
        }
    }

    private ProjCoordinate UTMToWGS84(Float x, Float y){
        CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
        CRSFactory csFactory = new CRSFactory();
        CoordinateReferenceSystem utm31 = csFactory.createFromName("EPSG:32631");
        CoordinateReferenceSystem wgs = csFactory.createFromName("EPSG:4326");
        CoordinateTransform trans = ctFactory.createTransform(utm31, wgs);
        ProjCoordinate p1 = new ProjCoordinate();
        ProjCoordinate p2 = new ProjCoordinate();
        p1.x = x;
        p1.y = y;
        trans.transform(p1, p2);
        return p2;
    }

    private void edmPlace() {
        try {
            // Localitzacio
            GENERDF.getLocalitzacio().forEach((Localitzacio localitzacio) -> {
                Choice choice = new Choice();
                PlaceType place = new PlaceType();
                // Localitzacio[rdf:about]
                place.setAbout(localitzacio.getAbout());
                /*
                    Localitzacio -> X
                    Localitzacio -> Y
                */
                if (localitzacio.getX() != null && localitzacio.getY() != null) {
                    ProjCoordinate convertedCoords = UTMToWGS84(localitzacio.getX(), localitzacio.getY());
                    _Long _long = new _Long();
                    _long.setLong((float)convertedCoords.x);
                    place.setLong(_long);
                    Lat lat = new Lat();
                    lat.setLat((float)convertedCoords.y);
                    place.setLat(lat);
                }
                String prefLabelStr = "";
                // Localitzacio -> adreca
                if (localitzacio.getAdreca() != null) {
                    prefLabelStr += localitzacio.getAdreca() + ", ";
                }
                if (localitzacio.getTerritori() != null) {
                    // TODO: Use more than 1 Territori
                    Localitzacio.Territori locTerritori = localitzacio.getTerritori().get(0);
                    Optional<Territori> territoriOpt = GENERDF.getTerritori().stream().filter(t -> t.getAbout().equals(locTerritori.getResource())).findFirst();
                    Territori territori = territoriOpt.get();
                    // Localitzacio -> territori -> comarca
                    prefLabelStr += territori.getMunicipi() + ", " + territori.getComarca();
                } else {
                    prefLabelStr = prefLabelStr.substring(0, prefLabelStr.length() - 2);
                }
                // Localitzacio -> agregat
                if (localitzacio.getAgregat() != null) {
                    prefLabelStr += "(" + localitzacio.getAgregat() + ")";
                }
                if (!prefLabelStr.isEmpty()) {
                    PrefLabel prefLabel = new PrefLabel();
                    prefLabel.setString(prefLabelStr);
                    place.getPrefLabelList().add(prefLabel);
                }
                // Localitzacio -> locDescripcio
                if (localitzacio.getLocDescripcio() != null) {
                    Note note = new Note();
                    note.setString(localitzacio.getLocDescripcio());
                    place.getNoteList().add(note);
                }
                choice.setPlace(place);
                this.getChoiceList().add(choice);
            });
        } catch (Exception exception) {
            logger.error(String.format("[%s] error generate edmPlace \n", exception));
        }
    }

    private String yearMinFourDigits(int year){
        boolean isNegative = year < 0;
        String year_converted = String.format("%04d", year);
        if (isNegative){
            year_converted = '-' + year_converted;
        }
        return year_converted;
    }

    private void edmAgent() {
        try {
            // Autor
            GENERDF.getAutor().forEach((Autor autor) -> {
                Choice choice = new Choice();
                AgentType agent = new AgentType();
                // Autor[rdf:about]
                agent.setAbout(autor.getAbout());
                // Autor -> noms
                if (autor.getNoms() != null){
                    PrefLabel prefLabel = new PrefLabel();
                    prefLabel.setString(autor.getNoms());
                    agent.getPrefLabelList().add(prefLabel);
                }
                // Autor -> cognoms
                if (autor.getCognoms() != null){
                    autor.getCognoms().forEach((String cognom) -> {
                        AltLabel altLabel = new AltLabel();
                        altLabel.setString(cognom);
                        agent.getAltLabelList().add(altLabel);
                    });
                }

                if (autor.getAnyInici() != null || autor.getAnyFi() != null){
                    Choice choiceDatacio = new Choice();
                    TimeSpanType timeSpan = new TimeSpanType();
                    // Autor[rdf:about]
                    timeSpan.setAbout(autor.getAbout() + ":Datacio");
                    // Autor -> anyInici
                    if (autor.getAnyInici() != null) {
                        PrefLabel prefLabel = new PrefLabel();
                        String anyInici = yearMinFourDigits(autor.getAnyInici());
                        prefLabel.setString(anyInici);
                        timeSpan.getPrefLabelList().add(prefLabel);
                        Begin begin = new Begin();
                        begin.setString(prefLabel.getString() + "-01-01");
                        timeSpan.setBegin(begin);
                    }
                    // Autor -> anyFi
                    if (autor.getAnyFi() != null) {
                        End end = new End();
                        end.setString(yearMinFourDigits(autor.getAnyFi()) + "-01-01");
                        timeSpan.setEnd(end);
                    }
                    choiceDatacio.setTimeSpan(timeSpan);
                    this.getChoiceList().add(choiceDatacio);

                    Date date = new Date();
                    ResourceOrLiteralType.Resource resource = new ResourceOrLiteralType.Resource();
                    resource.setResource(timeSpan.getAbout());
                    date.setResource(resource);
                    date.setString("");
                }
                // Autor -> professio
                if (autor.getProfessio() != null){
                    ProfessionOrOccupation professio = new ProfessionOrOccupation();
                    professio.setString(autor.getProfessio());
                    agent.getProfessionOrOccupationList().add(professio);
                }
                choice.setAgent(agent);
                this.getChoiceList().add(choice);
            });
        } catch (Exception exception) {
            logger.error(String.format("[%s] error generate edmAgent \n", exception));
        }
    }

    private void edmProvidedCHO() {
        try {
            // Identificacio
            GENERDF.getIdentificacio().forEach((Identificacio identificacio) -> {
                Choice choice = new Choice();
                ProvidedCHOType provided = new ProvidedCHOType();
                // Identificacio[rdf:about]
                provided.setAbout(identificacio.getAbout());
                // Descripcio -> descDescripcio
                GENERDF.getDescripcio().stream().filter(d -> d.getIdentificador().getResource().equals(identificacio.getAbout())).forEach((Descripcio descripcio) -> {
                    eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice cDesc = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();
                    Description desc = new Description();
                    desc.setString(descripcio.getDescDescripcio());
                    cDesc.setDescription(desc);
                    provided.getChoiceList().add(cDesc);
                });
                // Identificacio -> codiIntern
                eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice cCodiIntern = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();
                Identifier codiIntern = new Identifier();
                codiIntern.setString(identificacio.getCodiIntern());
                cCodiIntern.setIdentifier(codiIntern);
                provided.getChoiceList().add(cCodiIntern);
                // Identificacio -> codiInventari
                if (identificacio.getCodiInventari() != null) {
                    eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice cCodiInventari = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();
                    Identifier codiInventari = new Identifier();
                    codiInventari.setString(identificacio.getCodiInventari());
                    cCodiInventari.setIdentifier(codiInventari);
                    provided.getChoiceList().add(cCodiInventari);
                }
                // Identificacio -> proveidor
                identificacio.getProveidor().forEach((String proveidor) -> {
                    eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice cProveidor = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();
                    Publisher publisher = new Publisher();
                    publisher.setString(proveidor);
                    cProveidor.setPublisher(publisher);
                    provided.getChoiceList().add(cProveidor);
                });
                // propietari -> tipusRegim
                GENERDF.getPropietari().stream().filter(p -> p.getIdentificador().getResource().equals(identificacio.getAbout())).forEach((Propietari propietari) -> {
                    eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice cPropietari = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();
                    Rights rights = new Rights();
                    rights.setString(propietari.getTipusRegim());
                    cPropietari.setRights(rights);
                    provided.getChoiceList().add(cPropietari);
                });
                // Identificacio -> nom
                eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice cNom = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();
                Title title = new Title();
                title.setString(identificacio.getNom());
                cNom.setTitle(title);
                provided.getChoiceList().add(cNom);
                // Tipologia -> tipologia
                GENERDF.getTipologia().stream().filter(t -> t.getIdentificador().getResource().equals(identificacio.getAbout())).forEach((Tipologia tipologia) -> {
                    eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice cTipologia = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();
                    Type1 type = new Type1();
                    type.setString(tipologia.getTipologia());
                    cTipologia.setType(type);
                    provided.getChoiceList().add(cTipologia);
                });
                // Identificacio -> tipusPatrimoni
                if (identificacio.getTipusPatrimoni() != null) {
                    HasType hasType = new HasType();
                    hasType.setString(identificacio.getTipusPatrimoni().value());
                    provided.getHasTypeList().add(hasType);
                }
                // Identificacio -> altresNoms
                if (identificacio.getAltresNoms() != null) {
                    eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice cAltresNoms = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();
                    Alternative alternative = new Alternative();
                    alternative.setString(identificacio.getAltresNoms());
                    cAltresNoms.setAlternative(alternative);
                    provided.getChoiceList().add(cAltresNoms);
                }
                // Parameter("edmType")
                Optional.ofNullable(properties).map((Map<String, String> m) -> m.get("edmType")).ifPresent((String present) -> {
                    if (Objects.nonNull(EdmType.convert(present))) {
                        Type2 t = new Type2();
                        t.setType(EdmType.convert(present));
                        provided.setType(t);
                    }
                });
                long numNoticiesHistoriques = GENERDF.getTipologia().stream().filter(t -> t.getIdentificador().getResource().equals(identificacio.getAbout())).count();
                if (numNoticiesHistoriques > 0){
                    Choice aggChoice = new Choice();
                    Aggregation agg = new Aggregation();
                    // Identificacio[rdf:about] + ":Aggregation"
                    agg.setAbout(identificacio.getAbout() + ":Aggregation");
                    // Identificacio[rdf:about]
                    AggregatedCHO aggregatedCHO = new AggregatedCHO();
                    aggregatedCHO.setResource(identificacio.getAbout());
                    agg.setAggregatedCHO(aggregatedCHO);
                    // Parameter("dataProvider")
                    Optional.ofNullable(properties).map((Map<String, String> m) -> m.get("dataProvider")).ifPresent((String data) -> {
                        DataProvider dataProvider = new DataProvider();
                        dataProvider.setString(data);
                        agg.setDataProvider(dataProvider);
                    });
                    // Parameter("provider")
                    Optional.ofNullable(properties).map((Map<String, String> m) -> m.get("provider")).ifPresent((String data) -> {
                        Provider provider = new Provider();
                        provider.setString(data);
                        agg.setProvider(provider);
                    });
                    // Parameter("rights")
                    Optional.ofNullable(properties).map((Map<String, String> m) -> m.get("rights")).ifPresent((String data) -> {
                        Rights1 rights = new Rights1();
                        rights.setResource(IriToUri.iriToUri(data).toString());
                        agg.setRights(rights);
                    });
                    // URL in ECHOES environment
                    IsShownAt isShownAt = new IsShownAt();
                    isShownAt.setResource("https://echoes.pre.csuc.cat/providers/details_improved/?subject=" + identificacio.getAbout());
                    agg.setIsShownAt(isShownAt);
                    aggChoice.setAggregation(agg);
                    this.getChoiceList().add(aggChoice);
                }
                // dcterms:temporal -> datacio
                GENERDF.getDatacio().stream().filter(d -> d.getIdentificador().getResource().equals(identificacio.getAbout())).forEach((Datacio datacio) -> {
                    eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice cTemporal = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();
                    Temporal temporal = new Temporal();
                    ResourceOrLiteralType.Resource resource = new ResourceOrLiteralType.Resource();
                    resource.setResource(datacio.getAbout());
                    temporal.setResource(resource);
                    temporal.setString("");
                    cTemporal.setTemporal(temporal);
                    provided.getChoiceList().add(cTemporal);
                });
                // dcterms:spatial -> localitzacio
                GENERDF.getLocalitzacio().stream().filter(l -> l.getIdentificador().getResource().equals(identificacio.getAbout())).forEach((Localitzacio localitzacio) -> {
                    eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice cSpatial = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();
                    Spatial spatial = new Spatial();
                    ResourceOrLiteralType.Resource resource = new ResourceOrLiteralType.Resource();
                    resource.setResource(localitzacio.getAbout());
                    spatial.setResource(resource);
                    spatial.setString("");
                    cSpatial.setSpatial(spatial);
                    provided.getChoiceList().add(cSpatial);
                });
                // dc:creator -> autor
                GENERDF.getAutor().stream().filter(a -> a.getIdentificador().getResource().equals(identificacio.getAbout())).forEach((Autor autor) -> {
                    eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice cCreator = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();
                    Creator creator = new Creator();
                    ResourceOrLiteralType.Resource resource = new ResourceOrLiteralType.Resource();
                    resource.setResource(autor.getAbout());
                    creator.setResource(resource);
                    creator.setString("");
                    cCreator.setCreator(creator);
                    provided.getChoiceList().add(cCreator);
                });
                choice.setProvidedCHO(provided);
                this.getChoiceList().add(choice);
            });
            // noticiaHistorica
            GENERDF.getNoticiaHistorica().forEach((NoticiaHistorica noticiaHistorica) -> {
                Choice choice = new Choice();
                ProvidedCHOType provided = new ProvidedCHOType();
                // noticiaHistorica[rdf:about]
                provided.setAbout(noticiaHistorica.getAbout());
                // noticiaHistorica -> dataNoticiaHistorica
                if (noticiaHistorica.getDataNoticiaHistorica() != null) {
                    eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice cData = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();
                    Date date = new Date();
                    date.setString(noticiaHistorica.getDataNoticiaHistorica().toString());
                    provided.getChoiceList().add(cData);
                }
                // noticiaHistorica -> comentariNoticiaHistorica
                if (noticiaHistorica.getComentariNoticiaHistorica() != null) {
                    eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice cComentari = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();
                    Description description = new Description();
                    description.setString(noticiaHistorica.getComentariNoticiaHistorica());
                    provided.getChoiceList().add(cComentari);
                }
                // noticiaHistorica -> nomNoticiaHistorica
                if (noticiaHistorica.getNomNoticiaHistorica() != null) {
                    eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice cNom = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();
                    Title title = new Title();
                    title.setString(noticiaHistorica.getNomNoticiaHistorica());
                    provided.getChoiceList().add(cNom);
                }
                // noticiaHistorica -> tipusNoticiaHistorica
                if (noticiaHistorica.getTipusNoticiaHistorica() != null) {
                    eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice cTipus = new eu.europeana.corelib.definitions.jibx.EuropeanaType.Choice();
                    Type1 type = new Type1();
                    type.setString(noticiaHistorica.getTipusNoticiaHistorica());
                    cTipus.setType(type);
                    provided.getChoiceList().add(cTipus);
                }
                // HasType -> "HistoricalNews"
                HasType hasType = new HasType();
                hasType.setString("HistoricalNews");
                provided.getHasTypeList().add(hasType);
                // Relation to Ore:Aggregation
                IsRelatedTo isRelated = new IsRelatedTo();
                ResourceOrLiteralType.Resource resourceIsRelated = new ResourceOrLiteralType.Resource();
                resourceIsRelated.setResource(noticiaHistorica.getIdentificador().getResource() + ":Aggregation");
                isRelated.setResource(resourceIsRelated);
                isRelated.setString("");
                provided.getIsRelatedToList().add(isRelated);

                // edm:type = TEXT
                Type2 t = new Type2();
                t.setType(EdmType.convert("TEXT"));
                provided.setType(t);
                choice.setProvidedCHO(provided);
                this.getChoiceList().add(choice);
            });
        } catch (Exception exception) {
            logger.error(String.format("[%s] error generate edmProvidedCHO \n", exception));
        }
    }

    @Override
    public XSLTTransformations transformation(OutputStream out, Map<String, String> xsltProperties) throws Exception {
        throw new IllegalArgumentException("transformation is not valid for GENERDF2EDM!");
    }

    @Override
    public XSLTTransformations transformation(String xslt, OutputStream out, Map<String, String> xsltProperties) throws Exception {
        throw new IllegalArgumentException("transformation is not valid for GENERDF2EDM!");
    }

    @Override
    public XSLTTransformations transformation(String xslt) throws Exception {
        throw new IllegalArgumentException("transformation is not valid for GENERDF2EDM!");
    }

    @Override
    public void creation() {
        if (!Objects.equals(this, new RDF()))
            JibxMarshall.marshall(this, StandardCharsets.UTF_8.toString(),
                    false, IoBuilder.forLogger(GENERDF2EDM.class).setLevel(Level.INFO).buildOutputStream(), RDF.class, -1);
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
