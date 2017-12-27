/**
 * 
 */
package org.Validation.Core;

import eu.europeana.corelib.definitions.jibx.RDF;
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

		//return Objects.nonNull(getElement()) ? true : false;

		if(Objects.nonNull(getElement())){
            RDF rdf = (RDF) getElement();
            rdf.getChoiceList().forEach(c -> {
                if (c.ifProvidedCHO())
                    providedCHO.getAndIncrement();
                if (c.ifAggregation())
                    aggregation.getAndIncrement();
                if (c.ifWebResource())
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
		    return true;
        }
		return false;
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
