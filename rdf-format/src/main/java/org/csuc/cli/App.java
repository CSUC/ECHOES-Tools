/**
 * 
 */
package org.csuc.cli;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.merge.Merge;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.time.Instant;


/**
 * @author amartinez
 *
 */
public class App {

    private static Logger logger = LogManager.getLogger(App.class);

    public static void main(String[] args) throws Exception {
        new App().doMain(args);
    }

    public void doMain(String[] args) throws Exception {
        ArgsBean bean = new ArgsBean(args);
        CmdLineParser parser = new CmdLineParser(bean);
        try {
            // parse the arguments.
            parser.parseArgument(args);
        } catch( CmdLineException e ) {
            System.exit(1);
        }
        bean.promptEnterKey();

        long startTime = System.currentTimeMillis();

        Merge merge = new Merge(bean.getWeight(), bean.getMax_files(), bean.getFormat());
        merge.execute(bean.getPath());

        logger.info("result: {}", merge.getTemporal());

        long finishTime = System.currentTimeMillis();
        long elapsedTime = finishTime - startTime; // elapsed time in milliseconds

        logger.info("end: {} milliseconds", elapsedTime);
    }
}
