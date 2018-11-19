package org.Recollect.Core.cli;

import org.EDM.Transformations.formats.utils.SchemaType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.util.FormatType;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.IntOptionHandler;
import org.kohsuke.args4j.spi.MapOptionHandler;
import org.openarchives.oai._2.VerbType;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * @author amartinez
 */
public class ArgsBean {

    private static Logger logger = LogManager.getLogger(ArgsBean.class);

    @Option(name = "-h", aliases = "--help", help = true)
    private boolean help = false;

    @Option(name = "--host", usage = "host", required = true)
    private String host;

    @Option(name = "-v", aliases = "--verb", usage = "verb ", required = true)
    private VerbType verb;

    @Option(name = "-i", aliases = "--identifier", usage = "identifier ")
    private String identifier;

    @Option(name = "-m", aliases = "--metadataPrefix", usage = "metadataPrefix ")
    private String metadataPrefix;

    @Option(name = "-f", aliases = "--from", usage = "from ")
    private String from;

    @Option(name = "-u", aliases = "--until", usage = "until ")
    private String until;

    @Option(name = "-s", aliases = "--set", usage = "set ")
    private String set;

    @Option(name = "-g", aliases = "--granularity", usage = "granularity")
    private String granularity;

    @Option(name = "-r", aliases = "--resumptionToken", usage = "resumptionToken")
    private String resumptionToken;

    @Argument(index = 0, multiValued = true, handler = MapOptionHandler.class,
            metaVar = "{edmType | provider | rights | language | dataProvider ... }")
    private Map<String, String> arguments = new HashMap<>();

    @Option(name = "-o", aliases = "--out", usage = "out")
    private Path out;

//    @Option(name = "-x", aliases = "--xslt", usage = "xslt")
//    private Path xslt;

    @Option(name = "-t", aliases = "--threads", usage = "threads", handler = IntOptionHandler.class, metaVar = "<int>")
    private Integer threads = 1;

    @Option(name = "--format", usage = "format out")
    private FormatType format = FormatType.RDFXML;

    @Option(name = "--schema", usage="SCHEMA", required = true)
    private SchemaType schema;


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
        }
    }

    public String getHost() {
        return host;
    }


    public void setHost(String host) throws MalformedURLException, URISyntaxException {
        URL url = new URL(host);
        url.toURI();
        this.host = host;
    }

    public VerbType getVerb() {
        return verb;
    }

    public void setVerb(VerbType verb) {
        this.verb = verb;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getMetadataPrefix() {
        return metadataPrefix;
    }

    public void setMetadataPrefix(String metadataPrefix) {
        this.metadataPrefix = metadataPrefix;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getUntil() {
        return until;
    }

    public void setUntil(String until) {
        this.until = until;
    }

    public String getSet() {
        return set;
    }

    public void setSet(String set) {
        this.set = set;
    }

    public String getGranularity() {
        return granularity;
    }

    public void setGranularity(String granularity) {
        this.granularity = granularity;
    }

    public String getResumptionToken() {
        return resumptionToken;
    }

    public void setResumptionToken(String resumptionToken) {
        this.resumptionToken = resumptionToken;
    }

    public Path getOut() {
        return out;
    }

    public void setOut(Path out) {
        this.out = out;
    }

//    public String getXslt() {
//        return Objects.isNull(xslt) ? null : xslt.toString();
//    }


//    public void setXslt(Path xslt) throws FileNotFoundException {
//        if (Files.notExists(xslt)) throw new FileNotFoundException("File not exist!");
//        this.xslt = xslt;
//    }

    public Map<String, String> getArguments() {
        return arguments;
    }

    public void setArguments(Map<String, String> arguments) {
        this.arguments = arguments;
    }


    public Integer getThreads() {
        return threads;
    }

    public void setThreads(Integer threads) {
        if(threads == 0)    throw new IllegalThreadStateException("Threads > 0");
        this.threads = threads;
    }

    public FormatType getFormat() {
        return format;
    }

    public void setFormat(FormatType format) {
        this.format = format;
    }

    public SchemaType getSchema() {
        return schema;
    }

    public void setSchema(SchemaType schema) {
        this.schema = schema;
    }

    public boolean isHelp() {
        return help;
    }

    public void setHelp(boolean help) {
        this.help = help;
    }

    public void run() {
        logger.info("   Host                :   {}", host);
        logger.info("   Verb                :   {}", verb.value());
        logger.info("   Identifier          :   {}", identifier);
        logger.info("   MetadataPrefix      :   {}", metadataPrefix);
        logger.info("   From                :   {}", from);
        logger.info("   Until               :   {}", until);
        logger.info("   Set                 :   {}", set);
        logger.info("   Granularity         :   {}", granularity);
        logger.info("   resumptionToken     :   {}", resumptionToken);
//        logger.info("   XSLT                :   {}", xslt);
        logger.info("   out                 :   {}", out);
        logger.info("   RDFFormat           :   {}", format);
        logger.info("   Schema              :   {}", schema);
        logger.info("   Threads             :   {}", threads);
        logger.info("   Edm properties      :   {}", arguments);
    }
}
