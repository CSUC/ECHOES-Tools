package org.Validation.Core.cli;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.File;

import org.Validation.Core.Validate;
import org.Validation.Core.schematron.SchematronUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.europeana.corelib.definitions.jibx.RDF;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

/**
 * @author amartinez
 *
 */
public class App {

	private static Logger logger = LogManager.getLogger("Validation-Core");

	private static AtomicInteger providedCHO = new AtomicInteger(0);
	private static AtomicInteger aggregation = new AtomicInteger(0);
	private static AtomicInteger webResource = new AtomicInteger(0);
	private static AtomicInteger agent = new AtomicInteger(0);
	private static AtomicInteger place = new AtomicInteger(0);
	private static AtomicInteger timeSpan = new AtomicInteger(0);
	private static AtomicInteger Concept = new AtomicInteger(0);


    public static void main(String[] args) throws Exception {
        new App().doMain(args);
    }

    public void doMain(String[] args)  {

        ArgsBean bean = new ArgsBean(args);
        CmdLineParser parser = new CmdLineParser(bean);
        try {
            // parse the arguments.
            parser.parseArgument(args);
        } catch( CmdLineException e ) {
            System.exit(1);
        }

        new ForkJoinPool(bean.getThreads()).submit(() -> {

            try {
                Files.walk(bean.getInput()).filter(Files::isRegularFile).filter(f -> f.toString().endsWith(".xml"))
                        .parallel().forEach(f->{
                    try {
                        Validate validate = new Validate(new FileInputStream(f.toFile()), bean.getCharset(), RDF.class);
                        if(validate.isValid()){
                            logger.info("[{}] {} isValid XML true", Thread.currentThread().getName(), f);
                            if(Objects.nonNull(bean.getSchematron())){
                                SchematronUtil schUtil = new SchematronUtil(bean.getSchematron().toFile(), f.toFile());
                                try {
                                    logger.info("[{}] {} isValid SCH {}", Thread.currentThread().getName(), f, schUtil.isValid());
                                    if (!schUtil.isValid()){
                                        logger.info("[{}] {}", Thread.currentThread().getName(), schUtil.getFailedAssert());
                                        bean.moveTo(f, bean.getInvalid());
                                    }else {
                                        bean.moveTo(f, bean.getValid());
                                        validate.walk();
                                        count(validate);
                                    }
                                } catch (Exception e) {
                                    logger.error("[{}] {}", Thread.currentThread().getName(), e);
                                }
                            }else{
                                bean.moveTo(f, bean.getValid());
                                validate.walk();
                                count(validate);
                            }
						}else{
                            logger.info("[{}] {} isValid XML false\n{}", Thread.currentThread().getName(), f, validate.getError());
                            bean.moveTo(f, bean.getInvalid());
                        }
                    } catch (FileNotFoundException e) {
                        logger.error("[{}] {}", Thread.currentThread().getName(), e);
                    }
                });
            } catch (IOException e) {
                logger.error("[{}] {}", Thread.currentThread().getName(), e);
            }
        }).join();

        logger.info("[{}] ProvidedCHO: {} WebResource: {} Agent: {} Place: {} TimeSpan: {} Concept: {} Aggregation: {}",
                Thread.currentThread().getName(), providedCHO.get(), webResource.get(), agent.get(), place.get(), timeSpan.get(), Concept.get(),
                aggregation.get());

	}

	private static void count(Validate v){
	   providedCHO.getAndAdd(v.getProvidedCHO().get());
	   agent.getAndAdd(v.getAgent().get());
	   aggregation.getAndAdd(v.getAggregation().get());
	   webResource.getAndAdd(v.getWebResource().get());
       place.getAndAdd(v.getPlace().get());
       timeSpan.getAndAdd(v.getTimeSpan().get());
       Concept.getAndAdd(v.getConcept().get());

    }
}