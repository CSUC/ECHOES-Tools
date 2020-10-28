package org.csuc.publish.cli;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.util.FormatType;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.IntOptionHandler;

import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.Scanner;

/**
 * @author amartinez
 */
public class ArgsBean {

    private static Logger logger = LogManager.getLogger(ArgsBean.class);

    @Option(name = "-h", aliases = "--help", help = true)
    private boolean help = false;

    @Option(name = "--deleteFiles", aliases = "--deleteFiles", usage = "delete files")
    private boolean deleteFiles = false;

    @Option(name = "--hostname", usage = "hostname")
    private String hostname = "localhost:9999";

    @Option(name = "-i", aliases = "--input", usage = "file or folder input")
    private Path input;

    @Option(name = "--content-type", usage = "content type")
    private String contentType = FormatType.RDFXML.toString();

    @Option(name = "-n", aliases = "--namespace", usage = "namespace")
    private String namespace = "kb";

    @Option(name = "-c", aliases = "--charset", usage = "charset out", metaVar = "[UTF-8, ISO_8859_1, US_ASCII, UTF_16, UTF_16BE, UTF_16LE]")
    private String charset = StandardCharsets.UTF_8.name();

    @Option(name = "-t", aliases = "--threads", usage = "threads", handler = IntOptionHandler.class)
    private Integer threads = 1;

    @Option(name = "--context-uri", usage = "virtual graph")
    private String context_uri;

    @Option(name = "--datastore", forbids = {"-i", "--deleteFiles"}, usage = "datastore")
    private boolean datastore = false;

    @Option(name = "--datastore-host", depends = {"--datastore"}, forbids = {"-i"}, usage = "host")
    private String host = "localhost";

    @Option(name = "--datastore-port",  depends = {"--datastore"}, forbids = {"-i"}, usage = "port")
    private int port = 27017;

    @Option(name = "--datastore-name",  depends = {"--datastore"}, forbids = {"-i"}, usage = "database name")
    private String name = "quality";

    //Option(name = "--quality-id", depends = {"--datastore"}, forbids = {"-i"}, usage = "database name")
    @Option(name = "--quality-id", depends = {"--datastore"}, forbids = {"-i"}, usage = "quality identifier")
    private String quality_id;

    @Option(name = "--replace-data", usage = "replace data")
    private boolean replace = true;

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

    public boolean isHelp() {
        return help;
    }

    public void setHelp(boolean help) {
        this.help = help;
    }

    public Path getInput() {
        return input;
    }

    public void setInput(Path input) throws FileNotFoundException {
        if (Files.notExists(input)) throw new FileNotFoundException(MessageFormat.format("{0} File not Found!", input));
        this.input = input;
    }

    public Charset getCharset() {
        return Charset.forName(charset);
    }

    public void setCharset(String charset) {
        this.charset = Charset.forName(charset).toString();
    }

    public Integer getThreads() {
        return threads;
    }


    public void setThreads(Integer threads) {
        if (threads == 0) throw new IllegalThreadStateException("Threads > 0");
        this.threads = threads;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public boolean isDeleteFiles() {
        return deleteFiles;
    }

    public void setDeleteFiles(boolean deleteFiles) {
        this.deleteFiles = deleteFiles;
    }

    public String getContext_uri() {
        return context_uri;
    }

    public void setContext_uri(String context_uri) {
        this.context_uri = context_uri;
    }

    public boolean isDatastore() {
        return datastore;
    }

    public void setDatastore(boolean datastore) {
        this.datastore = datastore;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuality_id() {
        return quality_id;
    }

    public void setQuality_id(String quality_id) {
        this.quality_id = quality_id;
    }

    public boolean isReplace() {
        return replace;
    }

    public void setReplace(boolean replace) {
        this.replace = replace;
    }

    public void run() {
        logger.info("   Hostname        :   {}", hostname);
        logger.info("   Content-Type    :   {}", contentType);
        logger.info("   Namespace       :   {}", namespace);
        logger.info("   context_uri     :   {}", context_uri);
        logger.info("   Input           :   {}", input);
        logger.info("   Charset         :   {}", charset);
        logger.info("   Delete files    :   {}", deleteFiles);
        logger.info("   Threads         :   {}", threads);
        logger.info("   Replace data    :   {}", replace);
    }

    public void promptEnterKey() {
        System.out.println("Press \"ENTER\" to continue...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }
}
