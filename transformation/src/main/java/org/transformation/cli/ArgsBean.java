package org.transformation.cli;

import org.edm.transformations.formats.utils.SchemaType;
import org.apache.commons.io.FileExistsException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.typesafe.HDFSConfig;
import org.csuc.util.FormatType;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.IntOptionHandler;
import org.kohsuke.args4j.spi.MapOptionHandler;
import org.transformation.util.EnumTypes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * @author amartinez
 */
public class ArgsBean {

    private static Logger logger = LogManager.getLogger(ArgsBean.class);

    private HDFSConfig hdfsConfig = HDFSConfig.config(null);

    @Option(name = "-h", aliases = "--help", help = true)
    private boolean help = false;

    @Option(name = "-t", aliases = "--type", usage = "choice type", required = true)
    private EnumTypes type;

    @Option(name = "--format", usage = "format out")
    private FormatType format = FormatType.RDFXML;

    @Option(name = "--input", aliases = "-i", usage = "input", required = true)
    private String input;

    private Path out;

    @Option(name = "--hdfs", forbids = {"-o", "--out"}, usage = "hdfs")
    private boolean hdfs = false;

    @Option(name = "--hdfs-uri", depends = {"--hdfs"}, usage = "hdfs-uri")
    private String hdfsuri = hdfsConfig.getUri();

    @Option(name = "--hdfs-user", depends = {"--hdfs"}, usage = "hdfs-user")
    private String hdfsuser = hdfsConfig.getUser();

    @Option(name = "--hdfs-home", depends = {"--hdfs"}, usage = "hdfs-home")
    private String hdfshome = hdfsConfig.getHome();

    @Option(name = "-th", aliases = "--threads", usage = "threads", handler = IntOptionHandler.class, metaVar = "<int>")
    private Integer threads = 1;

    @Option(name = "-l", aliases = "--log-file", usage= "log4j2.xml")
    private Path log;

    @Argument(index = 0, multiValued = true, handler = MapOptionHandler.class,
            metaVar = "{edmType | provider | rights | language | dataProvider}",
            usage = "Example: { edmType=IMAGE,language=ca ... }")
    private Map<String, String> arguments = new HashMap<>();

    private String job = UUID.randomUUID().toString();

    public ArgsBean(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);

        try {
            parser.setUsageWidth(500);
            // parse the arguments.
            parser.parseArgument(args);

            if (this.help) {
                System.err.println("Usage: ");
                parser.printUsage(System.err);
                System.err.println();
                System.exit(1);
            }

            this.run();
        } catch (CmdLineException e) {
            if (this.help) {
                System.err.println("Usage: ");
                parser.printUsage(System.err);
                System.err.println();
                return;
            } else {
                System.err.println(e.getMessage());
                parser.printUsage(System.err);
                System.err.println();
                return;
            }
        } catch (IOException e) {
            logger.error(e);
        }
    }

    public HDFSConfig getHdfsConfig() {
        return hdfsConfig;
    }

    public void setHdfsConfig(HDFSConfig hdfsConfig) {
        this.hdfsConfig = hdfsConfig;
    }

    public boolean isHelp() {
        return help;
    }

    public void setHelp(boolean help) {
        this.help = help;
    }

    public EnumTypes getType() {
        return type;
    }

    public void setType(EnumTypes type) {
        this.type = type;
    }

    public FormatType getFormat() {
        return format;
    }

    public void setFormat(FormatType format) {
        this.format = format;
    }

    public String getInput() throws FileExistsException, MalformedURLException, URISyntaxException {
        if (type.equals(EnumTypes.URL) || type.equals(EnumTypes.OAI)) {
            URL url = new URL(input);
            url.toURI();
        }
        if (type.equals(EnumTypes.FILE))
            if (!Files.exists(Paths.get(input))) throw new FileExistsException("File not exist!");
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public Path getOut() {
        return out;
    }

    @Option(name = "-o", aliases = "--out", usage = "out")
    public void setOut(Path out) throws IOException {
        if (Files.notExists(Paths.get(String.format("%s/%s", out, job)))) Files.createDirectories(Paths.get(String.format("%s/%s", out, job)));
        this.out = Paths.get(String.format("%s/%s", out, job));
    }

    public boolean isHdfs() {
        return hdfs;
    }

    public void setHdfs(boolean hdfs) {
        this.hdfs = hdfs;
    }

    public String getHdfsuri() {
        return hdfsuri;
    }

    public void setHdfsuri(String hdfsuri) {
        this.hdfsuri = hdfsuri;
    }

    public String getHdfsuser() {
        return hdfsuser;
    }

    public void setHdfsuser(String hdfsuser) {
        this.hdfsuser = hdfsuser;
    }

    public String getHdfshome() {
        return hdfshome;
    }

    public void setHdfshome(String hdfshome) {
        this.hdfshome = hdfshome;
    }

    public Integer getThreads() {
        return threads;
    }

    public void setThreads(Integer threads) {
        this.threads = threads;
    }

    public Path getLog() throws FileNotFoundException {
        if(Objects.nonNull(log) && Files.notExists(log))    throw new FileNotFoundException(MessageFormat.format("File {0} not found!", log));
        return log;
    }

    public void setLog(Path log) {
        this.log = log;
    }

    public Map<String, String> getArguments() {
        return arguments;
    }

    public void setArguments(Map<String, String> arguments) {
        this.arguments = arguments;
    }

    public void run() throws IOException {
        logger.info("   Type                    :   {}", type);
        logger.info("   Format                  :   {}", format);
        logger.info("   Input                   :   {}", input);
        if (isHdfs()) {
            logger.info("   hdfshome                :   {}", hdfshome);
            logger.info("   hdfsuri                 :   {}", hdfsuri);
            logger.info("   hdfsuser                :   {}", hdfsuser);
        }
        if (Objects.nonNull(out))
            logger.info("   out                     :   {}", getOut());
        logger.info("   Log Properties          :   {}", log);
        logger.info("   Threads                 :   {}", threads);
        logger.info("   Edm properties          :   {}", arguments);
    }
}
