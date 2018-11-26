package org.csuc.analyse.core.cli;

import org.apache.commons.io.FileExistsException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.analyse.core.util.EnumTypes;
import org.csuc.analyse.core.util.FormatType;
import org.csuc.analyse.core.util.MethodType;
import org.csuc.typesafe.HDFSConfig;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author amartinez
 */
public class ArgsBean {

    private static Logger logger = LogManager.getLogger(ArgsBean.class);

    private HDFSConfig hdfsConfig = HDFSConfig.config(null);

    @Option(name="-h", aliases = "--help", help = true, required = false)
    private boolean help = false;

    @Option(name="-t", aliases ="--type" , usage="choice type", required = true)
    private EnumTypes type;

    @Option(name="-m", aliases = "--method", usage="choice method", required = true)
    private MethodType method;

    private String input;

    @Option(name = "-f", aliases = "--format", usage = "format out", required = false)
    private FormatType format = FormatType.XML;

    private Path out;

    @Option(name="--hdfs", forbids={"-o", "--out"}, usage = "hdfs")
    private boolean hdfs = false;

    @Option(name="--hdfs-uri", depends={"--hdfs"}, usage = "hdfs-uri")
    private String hdfsuri = hdfsConfig.getUri();

    @Option(name="--hdfs-user", depends={"--hdfs"}, usage = "hdfs-user")
    private String hdfsuser = hdfsConfig.getUser();

    @Option(name="--hdfs-home", depends={"--hdfs"}, usage = "hdfs-home")
    private String hdfshome = hdfsConfig.getHome();

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

    public EnumTypes getType() {
        return type;
    }

    public void setType(EnumTypes type) {
        this.type = type;
    }

    public MethodType getMethod() {
        return method;
    }

    public void setMethod(MethodType method) {
        this.method = method;
    }

    public String getInput() throws URISyntaxException, MalformedURLException, FileExistsException {
        if(type.equals(EnumTypes.URL) || type.equals(EnumTypes.OAI)){
            URL url = new URL(input);
            url.toURI();
        }
        if(type.equals(EnumTypes.FILE))
            if(!Files.exists(Paths.get(input))) throw new FileExistsException("File not exist!");
        return input;
    }

    @Option(name = "-i", aliases = "--input", usage= "file, oai or url input", required = true)
    public void setInput(String input) throws MalformedURLException, FileExistsException, URISyntaxException {
        this.input = input;
    }

    public FormatType getFormat() {
        return format;
    }

    public void setFormat(FormatType format) {
        this.format = format;
    }

    public Path getOut() {
        return out;
    }

    @Option(name = "-o", aliases = "--out", usage = "out", required = false)
    public void setOut(Path out) throws IOException {
        if(Files.notExists(out))  Files.createDirectories(out);
        this.out = out;
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

    public void run(){
        logger.info("   Type        :   {}", type);
        logger.info("   Method      :   {}", method);
        logger.info("   Format      :   {}", format);
        logger.info("   Input       :   {}", input);
        logger.info("   hdfs        :   {}", hdfs);
        logger.info("   hdfs-uri    :   {}", hdfsuri);
        logger.info("   hdfs-user   :   {}", hdfsuser);
        logger.info("   hdfs-home   :   {}", hdfshome);
        logger.info("   Out         :   {}", out);
    }
}
