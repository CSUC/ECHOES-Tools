/**
 * 
 */
package org.Validation.Core;

import eu.europeana.corelib.definitions.jibx.RDF;
import org.Validation.Core.edm.ProvidedCHO;
import org.Validation.Core.edm.WebResource;
import org.csuc.deserialize.JibxUnMarshall;

import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
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

    public void walk(){
        getRDF().getChoiceList().forEach(c -> {
            if (c.ifProvidedCHO()) {
                FactoryCoreClasses.createFactory(new ProvidedCHO(getRDF().getChoiceList())).validate(c.getProvidedCHO());
                providedCHO.getAndIncrement();
            }if (c.ifAggregation())
                aggregation.getAndIncrement();
            if (c.ifWebResource())
                FactoryCoreClasses.createFactory(new WebResource(getRDF().getChoiceList())).validate(c.getWebResource());
                webResource.getAndIncrement();
            if (c.ifAgent())
                agent.getAndIncrement();
            if (c.ifPlace())
                place.getAndIncrement();
            if (c.ifTimeSpan())
                timeSpan.getAndIncrement();
            if (c.ifConcept())
                Concept.getAndIncrement();
        });
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
