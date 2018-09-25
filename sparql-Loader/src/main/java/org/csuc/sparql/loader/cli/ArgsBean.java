    package org.csuc.sparql.loader.cli;

import org.apache.jena.riot.RDFFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    /**
 * @author amartinez
 */
public class ArgsBean {

    private static Logger logger = LogManager.getLogger(ArgsBean.class);

    @Option(name="-h", aliases = "--help", help = true, required = false)
    private boolean help = false;

    @Option(name = "--host", usage= "host", required = false)
    private String host = "localhost";

    @Option(name = "-p", aliases = "--port", usage= "port", required = false, handler = IntOptionHandler.class)
    private int port = 9999;

    private Path input;

    @Option(name = "--contentType", usage= "content type", required = false)
    private String contentType = RDFFormat.RDFXML_ABBREV.toString();

    @Option(name = "--context", usage= "context", required = false)
    private String context = "blazegraph";

    @Option(name = "-n", aliases = "--namespace", usage= "context", required = false)
    private String namespace;

    private String charset = StandardCharsets.UTF_8.name();


    private Integer threads = 1;

    public ArgsBean(String[] args){
        CmdLineParser parser = new CmdLineParser(this);

        try {
            parser.setUsageWidth(500);
            // parse the arguments.
            parser.parseArgument(args);

            if(this.help){
                System.err.println("Usage: ");
                parser.printUsage(System.err);
                System.err.println();
                System.exit(1);
            }

            this.run();
        } catch( CmdLineException e ) {
            if(this.help){
                System.err.println("Usage: ");
                parser.printUsage(System.err);
                System.err.println();
                return;
            }else{
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

    @Option(name = "-i", aliases = "--input", usage= "file or folder input", required = true)
    public void setInput(Path input) throws FileNotFoundException {
        if(Files.notExists(input)) throw new FileNotFoundException(MessageFormat.format("{0} File not Found!", input));
        this.input = input;
    }

    public Charset getCharset() {
        return Charset.forName(charset);
    }

    @Option(name = "-c", aliases = "--charset", usage = "charset out",
            required = false, metaVar = "[UTF-8, ISO_8859_1, US_ASCII, UTF_16, UTF_16BE, UTF_16LE]")
    public void setCharset(String charset) {
        this.charset = Charset.forName(charset).toString();
    }

    public Integer getThreads() {
        return threads;
    }

    @Option(name = "-t", aliases = "--threads", usage = "threads", required = false, handler = IntOptionHandler.class)
    public void setThreads(Integer threads) {
        if(threads == 0)    throw new IllegalThreadStateException("Threads > 0");
        this.threads = threads;
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

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public void run(){
        logger.info("   Host            :   {}", host);
        logger.info("   Port            :   {}", port);
        logger.info("   Content-Type    :   {}", contentType);
        logger.info("   Context         :   {}", context);
        logger.info("   Namespace       :   {}", namespace);
        logger.info("   Input           :   {}", input);
        logger.info("   Charset         :   {}", charset);
        logger.info("   Threads         :   {}", threads);
    }
}
