/**
 *
 */
package org.csuc.cli;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.format.Datastore;
import org.csuc.format.Json;
import org.csuc.format.Xml;
import org.csuc.quality.Quality;
import org.csuc.step.Content;
import org.csuc.step.Schema;
import org.csuc.step.Schematron;
import org.csuc.util.TimeUtils;
import org.csuc.util.format.FormatType;
import org.csuc.util.type.EnumTypes;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @author amartinez
 *
 */
public class App {

    private static Logger logger = LogManager.getLogger(App.class);

    private Instant inici = Instant.now();

    public static void main(String[] args) throws IOException, URISyntaxException {
        new App().doMain(args);
    }

    public void doMain(String[] args) throws IOException, URISyntaxException {
        ArgsBean bean = new ArgsBean(args);
        CmdLineParser parser = new CmdLineParser(bean);
        try {
            // parse the arguments.
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.exit(1);
        }

        Quality quality = null;

        try {
            if (bean.getFormatType().equals(FormatType.DATASTORE))
                quality = new Quality(new Datastore(bean.getHost(), bean.getPort(), bean.getName(), new org.csuc.dao.entity.Quality(), new Schema(new Schematron(new Content(bean.getQualityFile())))));
            if (bean.getFormatType().equals(FormatType.JSON))
                quality = new Quality(new Json(new Schema(new Schematron(new Content(bean.getQualityFile()))), bean.getOut()));
            if (bean.getFormatType().equals(FormatType.XML))
                quality = new Quality(new Xml(new Schema(new Schematron(new Content(bean.getQualityFile()))), bean.getOut()));

            if (Objects.nonNull(quality)) {
                if (bean.getType().equals(EnumTypes.FILE))
                    quality.getFormatInterface().execute(Paths.get(bean.getInput()));
                if (bean.getType().equals(EnumTypes.URL))
                    quality.getFormatInterface().execute(new URL(bean.getInput()));
            }

            logger.info(String.format("End Quality %s", TimeUtils.duration(inici, DateTimeFormatter.ISO_TIME)));
        } catch (Exception e) {
            logger.error(e);
        }
    }
}
