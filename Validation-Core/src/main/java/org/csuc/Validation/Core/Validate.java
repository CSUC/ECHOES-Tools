/**
 * 
 */
package org.csuc.Validation.Core;

import eu.europeana.corelib.definitions.jibx.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.Validation.Core.edm.*;
import org.csuc.Validation.Core.schematron.SchematronUtil;
import org.csuc.Validation.Core.utils.FileUtils;
import org.csuc.deserialize.JibxUnMarshall;
import org.csuc.serialize.JibxMarshall;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author amartinez
 *
 */
public class Validate {

    private static Logger logger = LogManager.getLogger(Validate.class);

	private AtomicInteger providedCHO = new AtomicInteger(0);
	private AtomicInteger aggregation = new AtomicInteger(0);
	private AtomicInteger webResource = new AtomicInteger(0);
	private AtomicInteger agent = new AtomicInteger(0);
	private AtomicInteger place = new AtomicInteger(0);
	private AtomicInteger timeSpan = new AtomicInteger(0);
	private AtomicInteger Concept = new AtomicInteger(0);

	private Path input;
    private Charset enc;
    private Class<RDF> object;
    private Path sch;

	public Validate(Path input, Charset enc, Class<?> object, Path sch) {
	    this.input = input;
	    this.enc = enc;
	    this.object = (Class<RDF>) object;
	    this.sch = sch;
    }

    public void isValid(Path out) throws IOException {
        Files.walk(input).filter(Files::isRegularFile).filter(f -> f.toString().endsWith(".xml")).parallel().forEach(f-> {
            try {
                JibxUnMarshall jibxUnMarshall = new JibxUnMarshall(new FileInputStream(f.toFile()), enc, object);

                if(Objects.nonNull(jibxUnMarshall.getElement())){
                    RDF object = execute((RDF) jibxUnMarshall.getElement(), new AtomicInteger(0));

                    //create a temp file
                    File temp = File.createTempFile(FilenameUtils.getBaseName(f.getFileName().toString()) + "_", ".xml");
                    JibxMarshall.marshall(object, enc.toString(), true, new FileOutputStream(temp), RDF.class, -1);

                    if(Objects.nonNull(sch)){
                        SchematronUtil schUtil = new SchematronUtil(sch.toFile(), temp);

                        logger.info("{} isValid SCH {}", temp.toPath(), schUtil.isValid());
                        if (!schUtil.isValid()) {
                            FileUtils.moveTo(temp.toPath(), Paths.get(out + File.separator + "invalid"));
                            logger.error("{}", schUtil.getFailedAssert());
                        }else{
                            FileUtils.moveTo(temp.toPath(), Paths.get(out + File.separator + "valid"));
                            logger.info("{} isValid XML true", temp.toPath());
                        }
                    }else{
                        FileUtils.moveTo(temp.toPath(), Paths.get(out + File.separator + "valid"));
                        logger.info("{} isValid XML true", f);
                    }
                }else{
                    FileUtils.moveTo(f, Paths.get(out + File.separator + "invalid"));
                    logger.info("{} isValid XML false\n{}", f, jibxUnMarshall.getError());
                }
            } catch (Exception e) {
                logger.error("{}:     {}", f.getFileName(), e);
            }
        });
    }

    private RDF execute(RDF input, AtomicInteger iter){
        if(iter.incrementAndGet() <= 2){
            RDF rdf = new RDF();

            input.getChoiceList().forEach(c -> {
                if (c.ifProvidedCHO()) {
                    ProvidedCHOType providedCHOType = FactoryCoreClasses.createFactory(new ProvidedCHO()).validate(c.getProvidedCHO());
                    if(Objects.nonNull(providedCHOType)){
                        RDF.Choice choice = new RDF.Choice();
                        choice.setProvidedCHO(providedCHOType);
                        rdf.getChoiceList().add(choice);
                    }

                    providedCHO.getAndIncrement();
                }
                if (c.ifAggregation()){
                    eu.europeana.corelib.definitions.jibx.Aggregation aggregationType = FactoryCoreClasses.createFactory(new org.csuc.Validation.Core.edm.Aggregation()).validate(c.getAggregation());
                    if(Objects.nonNull(aggregationType)){
                        RDF.Choice choice = new RDF.Choice();
                        choice.setAggregation(aggregationType);
                        rdf.getChoiceList().add(choice);
                    }

                    aggregation.getAndIncrement();
                }if (c.ifWebResource()) {
                    WebResourceType webResourceType = FactoryCoreClasses.createFactory(new WebResource()).validate(c.getWebResource());
                    if(Objects.nonNull(webResourceType)){
                        RDF.Choice choice = new RDF.Choice();
                        choice.setWebResource(webResourceType);
                        rdf.getChoiceList().add(choice);
                    }

                    webResource.getAndIncrement();
                }if (c.ifAgent()){
                    AgentType agentType = FactoryCoreClasses.createFactory(new Agent()).validate(c.getAgent());
                    if(Objects.nonNull(agentType)){
                        RDF.Choice choice = new RDF.Choice();
                        choice.setAgent(agentType);
                        rdf.getChoiceList().add(choice);
                    }

                    agent.getAndIncrement();
                }if (c.ifPlace()){
                    PlaceType placeType = FactoryCoreClasses.createFactory(new Place()).validate(c.getPlace());
                    if(Objects.nonNull(placeType)){
                        RDF.Choice choice = new RDF.Choice();
                        choice.setPlace(placeType);
                        rdf.getChoiceList().add(choice);
                    }

                    place.getAndIncrement();
                }if (c.ifTimeSpan()){
                    TimeSpanType timeSpanType = FactoryCoreClasses.createFactory(new TimeSpan()).validate(c.getTimeSpan());
                    if(Objects.nonNull(timeSpanType)){
                        RDF.Choice choice = new RDF.Choice();
                        choice.setTimeSpan(timeSpanType);
                        rdf.getChoiceList().add(choice);
                    }

                    timeSpan.getAndIncrement();
                }if (c.ifConcept()){
                    eu.europeana.corelib.definitions.jibx.Concept conceptType = FactoryCoreClasses.createFactory(new org.csuc.Validation.Core.edm.Concept()).validate(c.getConcept());
                    if(Objects.nonNull(conceptType)){
                        RDF.Choice choice = new RDF.Choice();
                        choice.setConcept(conceptType);
                        rdf.getChoiceList().add(choice);
                    }
                    Concept.getAndIncrement();
                }
            });
            return (Objects.equals(input, rdf)) ? execute(input, iter) : execute(rdf, iter);
        }
        return input;
    }

    public void status(){
        logger.info("ProvidedCHO: {} WebResource: {} Agent: {} Place: {} TimeSpan: {} Concept: {} Aggregation: {}",
                providedCHO.get(), webResource.get(), agent.get(), place.get(), timeSpan.get(), Concept.get(),
                aggregation.get());
    }

}
