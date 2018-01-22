/**
 * 
 */
package org.Validation.Core;

import eu.europeana.corelib.definitions.jibx.*;
import org.EDM.Transformations.formats.a2a.A2A2EDM;
import org.Validation.Core.edm.*;
import org.Validation.Core.edm.Aggregation;
import org.Validation.Core.edm.Concept;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.io.IoBuilder;
import org.csuc.deserialize.JibxUnMarshall;
import org.csuc.serialize.JibxMarshall;

import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author amartinez
 *
 */
public class Validate extends JibxUnMarshall {

	private AtomicInteger providedCHO = new AtomicInteger(0);
	private AtomicInteger aggregation = new AtomicInteger(0);
	private AtomicInteger webResource = new AtomicInteger(0);
	private AtomicInteger agent = new AtomicInteger(0);
	private AtomicInteger place = new AtomicInteger(0);
	private AtomicInteger timeSpan = new AtomicInteger(0);
	private AtomicInteger Concept = new AtomicInteger(0);


	public Validate(InputStream ins, Charset enc, Class<?> classType) {
		super(ins, enc, classType);
	}

	public Validate(InputStream ins, String name, Charset enc, Class<?> classType) {
		super(ins, name, enc, classType);
	}

	public Validate(Reader rdr, Class<?> classType) {
		super(rdr, classType);
	}

	public Validate(Reader rdr, String name, Class<?> classType) {
		super(rdr, name, classType);
	}

	public boolean isValid() {
		return Objects.nonNull(getRDF()) ? true : false;
    }

    public RDF getRDF(){
        if(Objects.nonNull(getElement()))
            return (RDF) getElement();
        return null;
    }

    public RDF walk(){
	    RDF rdf = new RDF();

        getRDF().getChoiceList().forEach(c -> {
            if (c.ifProvidedCHO()) {
                ProvidedCHOType providedCHOType = FactoryCoreClasses.createFactory(new ProvidedCHO(getRDF().getChoiceList())).validate(c.getProvidedCHO());
                if(Objects.nonNull(providedCHOType)){
                    RDF.Choice choice = new RDF.Choice();
                    choice.setProvidedCHO(providedCHOType);
                    rdf.getChoiceList().add(choice);
                }

                providedCHO.getAndIncrement();
            }if (c.ifAggregation()){
                eu.europeana.corelib.definitions.jibx.Aggregation aggregationType = FactoryCoreClasses.createFactory(new Aggregation(getRDF().getChoiceList())).validate(c.getAggregation());
                if(Objects.nonNull(aggregationType)){
                    RDF.Choice choice = new RDF.Choice();
                    choice.setAggregation(aggregationType);
                    rdf.getChoiceList().add(choice);
                }

                aggregation.getAndIncrement();
            }if (c.ifWebResource()) {
                WebResourceType webResourceType = FactoryCoreClasses.createFactory(new WebResource(getRDF().getChoiceList())).validate(c.getWebResource());
                if(Objects.nonNull(webResourceType)){
                    RDF.Choice choice = new RDF.Choice();
                    choice.setWebResource(webResourceType);
                    rdf.getChoiceList().add(choice);
                }

                webResource.getAndIncrement();
            }if (c.ifAgent()){
                AgentType agentType = FactoryCoreClasses.createFactory(new Agent(getRDF().getChoiceList())).validate(c.getAgent());
                if(Objects.nonNull(agentType)){
                    RDF.Choice choice = new RDF.Choice();
                    choice.setAgent(agentType);
                    rdf.getChoiceList().add(choice);
                }

                agent.getAndIncrement();
            }if (c.ifPlace()){
                PlaceType placeType = FactoryCoreClasses.createFactory(new Place(getRDF().getChoiceList())).validate(c.getPlace());
                if(Objects.nonNull(placeType)){
                    RDF.Choice choice = new RDF.Choice();
                    choice.setPlace(placeType);
                    rdf.getChoiceList().add(choice);
                }

                place.getAndIncrement();
            }if (c.ifTimeSpan()){
                TimeSpanType timeSpanType = FactoryCoreClasses.createFactory(new TimeSpan(getRDF().getChoiceList())).validate(c.getTimeSpan());
                if(Objects.nonNull(timeSpanType)){
                    RDF.Choice choice = new RDF.Choice();
                    choice.setTimeSpan(timeSpanType);
                    rdf.getChoiceList().add(choice);
                }

                timeSpan.getAndIncrement();
            }if (c.ifConcept()){
                eu.europeana.corelib.definitions.jibx.Concept conceptType = FactoryCoreClasses.createFactory(new Concept(getRDF().getChoiceList())).validate(c.getConcept());
                if(Objects.nonNull(conceptType)){
                    RDF.Choice choice = new RDF.Choice();
                    choice.setConcept(conceptType);
                    rdf.getChoiceList().add(choice);
                }
                Concept.getAndIncrement();
            }
        });

//        JibxMarshall.marshall(rdf, StandardCharsets.UTF_8.toString(),
//                false, IoBuilder.forLogger(Validate.class).setLevel(Level.DEBUG).buildOutputStream(), RDF.class, -1);

        return rdf;
    }

    public AtomicInteger getProvidedCHO() {
        return providedCHO;
    }

    public AtomicInteger getAggregation() {
        return aggregation;
    }

    public AtomicInteger getWebResource() {
        return webResource;
    }

    public AtomicInteger getAgent() {
        return agent;
    }

    public AtomicInteger getPlace() {
        return place;
    }

    public AtomicInteger getTimeSpan() {
        return timeSpan;
    }

    public AtomicInteger getConcept() {
        return Concept;
    }
}
