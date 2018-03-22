package org.csuc.Validation.Core.cli;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.IntOptionHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;

/**
 * @author amartinez
 */
public class ArgsBean {

    private static Logger logger = LogManager.getLogger(ArgsBean.class);

    @Option(name="-h", aliases = "--help", help = true, required = false)
    private boolean help = false;

    private Path input;

    private String charset = StandardCharsets.UTF_8.name();

    //private Path schematron;

    private Integer threads = 1;

    private Path out;
    private Path valid;
    private Path invalid;


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

    @Option(name = "-i", aliases = "--input", usage= "file or folder input", required = true, metaVar = "<Path>")
    public void setInput(Path input) throws IOException {
        if(Files.notExists(input)) throw new FileNotFoundException(MessageFormat.format("{0} File not Found!", input));

        if(Files.isDirectory(input))    this.out = input;
        else   this.out = input.getParent();

        setValid(Files.createDirectories(Paths.get(out + File.separator + "valid")));
        setInvalid(Files.createDirectories(Paths.get(out + File.separator + "invalid")));

        this.input = input;
    }

    public Charset getCharset() {
        return Charset.forName(charset);
    }

    @Option(name = "-c", aliases = "--charset", usage = "charset",
            required = false, metaVar = "[UTF-8, ISO_8859_1, US_ASCII, UTF_16, UTF_16BE, UTF_16LE]")
    public void setCharset(String charset) {
        this.charset = Charset.forName(charset).toString();
    }

//    public Path getSchematron() {
//        return schematron;
//    }

//    @Option(name = "-s", aliases = "--schematron", usage = "schmematron", required = false, metaVar = "<Path>")
//    public void setSchematron(Path schematron) throws FileNotFoundException {
//        if(Files.notExists(schematron)) throw new FileNotFoundException(MessageFormat.format("{0} File not Found!", schematron));
//        this.schematron = schematron;
//    }

    public Integer getThreads() {
        return threads;
    }

    @Option(name = "-t", aliases = "--threads", usage = "threads", required = false, handler = IntOptionHandler.class, metaVar = "<int>")
    public void setThreads(Integer threads) {
        if(threads == 0)    throw new IllegalThreadStateException("Threads > 0");
        this.threads = threads;
    }

    public Path getOut() {
        return out;
    }

    public Path getValid() {
        return valid;
    }

    public Path getInvalid() {
        return invalid;
    }

    public void setValid(Path valid) {
        this.valid = valid;
    }

    public void setInvalid(Path invalid) {
        this.invalid = invalid;
    }

    public void run(){
        logger.info("   Input       :   {}", input);
        logger.info("   Charset     :   {}", charset);
//        logger.info("   Schematron  :   {}", schematron);
        logger.info("   Threads     :   {}", threads);
    }
}
