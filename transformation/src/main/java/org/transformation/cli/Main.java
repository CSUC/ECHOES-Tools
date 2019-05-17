package org.transformation.cli;

import isbn._1_931666_22_9.Ead;
import nl.memorix_maior.api.rest._3.Memorix;
import nl.mindbus.a2a.A2AType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.openarchives.oai._2.OAIPMHtype;
import org.openarchives.oai._2_0.oai_dc.OaiDcType;
import org.transformation.factory.Transformation;
import org.transformation.factory.TransformationFile;
import org.transformation.factory.TransformationOai;
import org.transformation.factory.TransformationUrl;
import org.transformation.util.EnumTypes;
import org.transformation.util.TimeUtils;

import java.net.URL;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

/**
 * @author amartinez
 */
public class Main {

    private static Logger logger = LogManager.getLogger(Main.class);

    private static Instant inici = Instant.now();

    private static Class<?>[] classType = new Class[]{OAIPMHtype.class, A2AType.class, OaiDcType.class, Memorix.class, Ead.class};

    private static String job = UUID.randomUUID().toString();

    private static ArgsBean bean;

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        bean = new ArgsBean(args);
        CmdLineParser parser = new CmdLineParser(bean);
        try {
            // parse the arguments.
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.exit(1);
        }

        if(Objects.nonNull(bean.getLog()))  Configurator.initialize(null, bean.getLog().toString());

        try {
            Transformation transformation = null;
            
            if (bean.getType().equals(EnumTypes.OAI))   transformation = new TransformationOai(new URL(bean.getInput()), classType, bean.getThreads());
            else if (bean.getType().equals(EnumTypes.URL))  transformation = new TransformationUrl(new URL(bean.getInput()), classType);
            else if (bean.getType().equals(EnumTypes.FILE)) transformation = new TransformationFile(Paths.get(bean.getInput()), classType);

            if (Objects.nonNull(transformation)) {
                if (bean.isHdfs()) {
                    org.apache.hadoop.fs.Path path = Objects.isNull(bean.getArguments().get("set"))
                            ? new org.apache.hadoop.fs.Path(MessageFormat.format("transformation/{0}", job))
                            : new org.apache.hadoop.fs.Path(MessageFormat.format("transformation/{0}/{1}", job, bean.getArguments().get("set")));

                    transformation.hdfs(bean.getHdfsuri(),bean.getHdfsuser(), bean.getHdfshome(), path, bean.getSchema(), bean.getArguments(), bean.getFormat());
                } else {
                    if (Objects.nonNull(bean.getOut())) transformation.path(bean.getOut(), bean.getSchema(), bean.getArguments(), bean.getFormat());
                    else   transformation.console(bean.getSchema(), bean.getArguments(), bean.getFormat());
                }

            }
        } catch (Exception e) {
            logger.error(e);
        }

        Thread.sleep(3000);
        logger.info(String.format("End Transformation %s", TimeUtils.duration(inici, DateTimeFormatter.ISO_TIME)));
    }
}
