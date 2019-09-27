package org.csuc.echoes.gui.consumer.publish.utils;

import eu.europeana.corelib.definitions.jibx.RDF;
import org.csuc.dao.entity.QualityDetails;
import org.csuc.serialize.JibxMarshall;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

public class RdfDAO<T> {

    private Supplier<T> supplier;

    private T createContents() {
        return supplier.get();
    }

    private QualityDetails qualityDetails;

    public RdfDAO(QualityDetails qualityDetails, Supplier supplier) {
        this.qualityDetails = qualityDetails;
        this.supplier = supplier;
    }

    @Override
    public String toString() {
        return qualityDetails.toString();
    }

    public  T toRDF() throws Exception {
        RDF rdf = rdf();

        T result = (T) createContents();

        JibxMarshall.marshall(rdf, StandardCharsets.UTF_8.toString(), false, (OutputStream) result, RDF.class, -1 );

        return result;
    }

    private RDF rdf(){
        RDF rdf = new RDF();

        qualityDetails.getEdm().getProvidedCHO().forEach(providedCHO -> {
            RDF.Choice choice = new RDF.Choice();
            choice.setProvidedCHO(providedCHO.getData());
            rdf.getChoiceList().add(choice);
        });

        qualityDetails.getEdm().getAgent().forEach(agent -> {
            RDF.Choice choice = new RDF.Choice();
            choice.setAgent(agent.getData());
            rdf.getChoiceList().add(choice);
        });
        qualityDetails.getEdm().getConcept().forEach(concept -> {
            RDF.Choice choice = new RDF.Choice();
            choice.setConcept(concept.getData());
            rdf.getChoiceList().add(choice);
        });
        qualityDetails.getEdm().getAggregation().forEach(aggregation -> {
            RDF.Choice choice = new RDF.Choice();
            choice.setAggregation(aggregation.getData());
            rdf.getChoiceList().add(choice);
        });
        qualityDetails.getEdm().getTimeSpan().forEach(timeSpan -> {
            RDF.Choice choice = new RDF.Choice();
            choice.setTimeSpan(timeSpan.getData());
            rdf.getChoiceList().add(choice);
        });
        qualityDetails.getEdm().getWebResource().forEach(webResource -> {
            RDF.Choice choice = new RDF.Choice();
            choice.setWebResource(webResource.getData());
            rdf.getChoiceList().add(choice);
        });
        qualityDetails.getEdm().getPlace().forEach(place -> {
            RDF.Choice choice = new RDF.Choice();
            choice.setPlace(place.getData());
            rdf.getChoiceList().add(choice);
        });

        return rdf;
    }
}
