package org.csuc.Validation.Core.cli;

import eu.europeana.corelib.definitions.jibx.RDF;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.Validation.Core.schema.Schema;
import org.csuc.Validation.Core.schematron.Schematron;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.ForkJoinPool;

/**
 * @author amartinez
 *
 */
public class App {

	private static Logger logger = LogManager.getLogger(App.class);
    private static ArgsBean bean;

    public static void main(String[] args) {
        new App().doMain(args);
    }


    public void doMain(String[] args)  {
        bean = new ArgsBean(args);
        CmdLineParser parser = new CmdLineParser(bean);
        try {
            // parse the arguments.
            parser.parseArgument(args);
        } catch( CmdLineException e ) {
            System.exit(1);
        }

        new ForkJoinPool(bean.getThreads()).submit(() -> {
            try {
                Files.walk(bean.getInput())
                        .filter(Files::isRegularFile)
                        .parallel()
                        .forEach(f->{
                            try {
                                Schema schema = new Schema(new FileInputStream(f.toFile()), RDF.class);
                                if(schema.isValid()){
                                    Schematron schematron = new Schematron(f.toFile());
                                    boolean isValidSchematron = schematron.isValid();
                                    logger.info("{}:    schema:     {}      schematron:     {}", f.getFileName(), schema.isValid(), isValidSchematron);
                                    if(!isValidSchematron){
                                        logger.error("\t{}", schematron.getSVRLFailedAssert().toString());
                                    }
                                }else{
                                    logger.info("{}:    schema:     {}", f.getFileName(), schema.isValid());
                                    if(!schema.isValid()) {
                                        logger.error("\tMessage:    {}", schema.getError().getMessage());
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).join();
	}
}
