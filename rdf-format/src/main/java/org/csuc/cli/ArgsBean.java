package org.csuc.cli;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.util.FormatType;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.nio.file.Path;
import java.util.Scanner;

/**
 * @author amartinez
 */
public class ArgsBean {

    private static Logger logger = LogManager.getLogger(ArgsBean.class);

    @Option(name="-h", aliases = "--help", help = true)
    private boolean help = false;

    @Option(name="-p", aliases = "--p", required = true, usage = "origin folder")
    private Path path;

    @Option(name="-w", aliases = "--weight", usage = "weight")
    private double weight = 0.002;

    @Option(name="-m", aliases = "--max-files", usage = "max files result")
    private int max_files = 1;

    @Option(name="-f", aliases = "--format", usage = "format result")
    private FormatType format = FormatType.TURTLE;

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

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getMax_files() {
        return max_files;
    }

    public void setMax_files(int max_files) {
        this.max_files = max_files;
    }

    public FormatType getFormat() {
        return format;
    }

    public void setFormat(FormatType format) {
        this.format = format;
    }

    public void run() {
        logger.info("   origin               :   {}", path);
        logger.info("   weight               :   {}", weight);
        logger.info("   max_files            :   {}", max_files);
        logger.info("   format               :   {}", format);
    }

    public void promptEnterKey() {
        System.out.println("Press \"ENTER\" to continue...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

}
