package org.recollect.core.cli;

import org.EDM.Transformations.formats.utils.SchemaType;
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

    private HDFSConfig hdfsConfig = HDFSConfig.config(null);

    @Option(name = "-h", aliases = "--help", help = true)
    private boolean help = false;

    @Option(name = "--host", usage = "host", required = true)
    private String host;

    @Option(name="--identify",
            forbids={"-i", "--identifier", "-m", "--metadataPrefix", "-f", "--from", "-u", "--until", "-s",
                    "--set", "-r", "--resumptionToken", "--schema", "--format", "--hdfs", "-o", "--out", "--listMetadataFormats", "--listSets", "--getRecord", "--listRecords", "--listIdentifiers"},
            usage = "This verb is used to retrieve information about a repository")
    private boolean identify = false;

    @Option(name="--listMetadataFormats",
            forbids={"-i", "--identifier", "-m", "--metadataPrefix", "-f", "--from", "-u", "--until", "-s", "--set", "--schema", "--format",
                    "--hdfs", "-o", "--out", "--identify", "--listSets", "--getRecord", "--listRecords", "--listIdentifiers"},
            usage = "This verb is used to retrieve the metadata formats available from a repository")
    private boolean listMetadataFormats = false;


    @Option(name="--listSets",
            forbids={"-i", "--identifier", "-f", "--from", "-u", "--until", "-s", "--set", "--schema", "--format",
                    "--hdfs", "-o", "--out", "--identify", "--listMetadataFormats", "--getRecord", "--listRecords", "--listIdentifiers"},
            usage = "This verb is used to retrieve the set structure of a repository")
    private boolean listSets = false;

    @Option(name="--getRecord",
            depends = { "-i", "--identifier", "-m", "--metadataPrefix", "--schema"},
            forbids={"-f", "--from", "-u", "--until", "-s", "--set", "-r", "--resumptionToken", "--schema", "--identify", "--listMetadataFormats", "--listSets", "--listRecords", "--listIdentifiers"},
            usage = "This verb is used to retrieve an individual metadata record from a repository")
    private boolean getRecord = false;

    @Option(name="--listRecords",
            depends = {"-m", "--metadataPrefix", "--schema"},
            forbids={"-i", "--identifier", "--identify", "--listMetadataFormats", "--listSets", "--getRecord", "--listIdentifiers"},
            usage = "This verb is used to harvest records from a repository")
    private boolean listRecords = false;

    @Option(name="--listIdentifiers",
            depends = {"-m", "--metadataPrefix"},
            forbids={"-i", "--identifier", "--schema", "--format", "--hdfs", "-o", "--out", "--identify", "--listMetadataFormats", "--listSets", "--getRecord", "--listRecords"},
            usage = "This verb is an abbreviated form of ListRecords, retrieving only headers rather than records")
    private boolean listIdentifiers = false;


    @Option(name = "-i", aliases = "--identifier", usage = "identifier ")
    private String identifier;

    @Option(name = "-m", aliases = "--metadataPrefix", usage = "metadataPrefix")
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

    @Option(name="--hdfs", forbids={"-o", "--out"}, usage = "hdfs")
    private boolean hdfs = false;

    @Option(name="--hdfs-uri", depends={"--hdfs"}, usage = "hdfs-uri")
    private String hdfsuri = hdfsConfig.getUri();

    @Option(name="--hdfs-user", depends={"--hdfs"}, usage = "hdfs-user")
    private String hdfsuser = hdfsConfig.getUser();

    @Option(name="--hdfs-home", depends={"--hdfs"}, usage = "hdfs-home")
    private String hdfshome = hdfsConfig.getHome();

    @Option(name = "-t", aliases = "--threads", usage = "threads", handler = IntOptionHandler.class, metaVar = "<int>")
    private Integer threads = 1;

    @Option(name = "--format", usage = "format out")
    private FormatType format = FormatType.RDFXML;

    @Option(name = "--schema", usage="SCHEMA")
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

    public boolean isHdfs() {
        return hdfs;
    }

    public void setHdfs(boolean hdfs) {
        this.hdfs = hdfs;
    }

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


    public boolean isIdentify() {
        return identify;
    }

    public void setIdentify(boolean identify) {
        this.identify = identify;
    }

    public boolean isListMetadataFormats() {
        return listMetadataFormats;
    }

    public void setListMetadataFormats(boolean listMetadataFormats) {
        this.listMetadataFormats = listMetadataFormats;
    }

    public boolean isListSets() {
        return listSets;
    }

    public void setListSets(boolean listSets) {
        this.listSets = listSets;
    }

    public boolean isGetRecord() {
        return getRecord;
    }

    public void setGetRecord(boolean getRecord) {
        this.getRecord = getRecord;
    }

    public boolean isListRecords() {
        return listRecords;
    }

    public void setListRecords(boolean listRecords) {
        this.listRecords = listRecords;
    }

    public boolean isListIdentifiers() {
        return listIdentifiers;
    }

    public void setListIdentifiers(boolean listIdentifiers) {
        this.listIdentifiers = listIdentifiers;
    }

    public void run() {
        if(identify){
            logger.info("   verb                    :   {}", VerbType.IDENTIFY);
            logger.info("   Host                    :   {}", host);
        }
        if(listMetadataFormats){
            logger.info("   verb                    :   {}", VerbType.LIST_METADATA_FORMATS);
            logger.info("   Host                    :   {}", host);
            logger.info("   Identifier              :   {}", identifier);
        }
        if(listSets){
            logger.info("   verb                    :   {}", VerbType.LIST_SETS);
            logger.info("   Host                    :   {}", host);
            logger.info("   resumptionToken         :   {}", resumptionToken);
        }
        if(getRecord){
            logger.info("   verb                    :   {}", VerbType.GET_RECORD);
            logger.info("   Host                    :   {}", host);
            logger.info("   resumptionToken         :   {}", resumptionToken);
            logger.info("   Identifier              :   {}", identifier);
            logger.info("   MetadataPrefix          :   {}", metadataPrefix);
        }
        if(listRecords) logger.info("   verb                    :   {}", VerbType.LIST_RECORDS);
        if(listIdentifiers) logger.info("   verb                    :   {}", VerbType.LIST_IDENTIFIERS);
        if(listRecords || listIdentifiers){
            logger.info("   Host                    :   {}", host);
            logger.info("   resumptionToken         :   {}", resumptionToken);
            logger.info("   MetadataPrefix          :   {}", metadataPrefix);
            logger.info("   From                    :   {}", from);
            logger.info("   Until                   :   {}", until);
            logger.info("   Set                     :   {}", set);
        }
        if(getRecord || listRecords){
            logger.info("   RDFFormat               :   {}", format);
            logger.info("   Schema                  :   {}", schema);
        }
        if((getRecord && hdfs) || (listRecords && hdfs)) {
            logger.info("   hdfs                    :   {}", hdfs);
            logger.info("   hdfs-uri                :   {}", hdfsuri);
            logger.info("   hdfs-user               :   {}", hdfsuser);
            logger.info("   hdfs-home               :   {}", hdfshome);
        }else{
            logger.info("   out                     :   {}", out);
        }
        logger.info("   Threads                 :   {}", threads);
        logger.info("   Edm properties          :   {}", arguments);
    }
}
