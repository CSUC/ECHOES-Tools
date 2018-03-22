package org.csuc.Validation.Core.cli;

import eu.europeana.corelib.definitions.jibx.RDF;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.Validation.Core.Validate;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.io.IOException;
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
            Validate validate = new Validate(bean.getInput(), bean.getCharset(), RDF.class, null);
            try {
                validate.isValid(bean.getOut());
            } catch (IOException e) {
                logger.error(e);
            }
            validate.status();
        }).join();
	}
}
