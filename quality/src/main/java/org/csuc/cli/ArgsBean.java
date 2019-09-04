package org.csuc.cli;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.util.format.FormatType;
import org.csuc.util.type.EnumTypes;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.ParserProperties;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.Scanner;
import java.util.UUID;

/**
 * @author amartinez
 */
public class ArgsBean {

    private static Logger logger = LogManager.getLogger(ArgsBean.class);

    @Option(name = "-h", aliases = "--help", help = true, required = false)
    private boolean help = false;

    @Option(name = "-i", aliases = "--input", usage = "data input", required = true)
    private String input;

    @Option(name = "-q", aliases = "--quality-config", usage = "quality file config", required = true)
    private Path qualityFile;

    @Option(name = "-f", aliases = "--format", usage = "format", required = true)
    private FormatType formatType;

    @Option(name = "-t", aliases = "--type", usage = "type", required = true)
    private EnumTypes type;

    @Option(name = "--datastore-host")
    private String host = "localhost";

    @Option(name = "--datastore-port")
    private int port = 27017;

    @Option(name = "--datastore-name")
    private String name = "quality";

    private Path out;

    private String job = UUID.randomUUID().toString();

    public ArgsBean(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);

        try {
            ParserProperties.defaults().withUsageWidth(500);
            // parse the arguments.
            parser.parseArgument(args);

            if (this.help) {
                System.err.println("Usage: ");
                parser.printUsage(System.err);
                System.err.println();
                System.exit(1);
            }

            this.run();

            promptEnterKey();
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

    private static void promptEnterKey() {
        System.out.println("Press \"ENTER\" to continue...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    public boolean isHelp() {
        return help;
    }

    public void setHelp(boolean help) {
        this.help = help;
    }

    public String getInput() throws FileNotFoundException, URISyntaxException, MalformedURLException {
        if (type.equals(EnumTypes.URL)) {
            URL url = new URL(input);
            url.toURI();
        }
        if (type.equals(EnumTypes.FILE))
            if (Files.notExists(Paths.get(input)))
                throw new FileNotFoundException(MessageFormat.format("{0} File not Found!", input));
        return input;
    }

    public void setInput(String input) throws FileNotFoundException {
        if (Files.notExists(Paths.get(input)))
            throw new FileNotFoundException(MessageFormat.format("{0} File not Found!", input));
        this.input = input;
    }

    public Path getQualityFile() {
        return qualityFile;
    }

    public void setQualityFile(Path qualityFile) throws FileNotFoundException {
        if (Files.notExists(qualityFile))
            throw new FileNotFoundException(MessageFormat.format("{0} File not Found!", qualityFile));
        this.qualityFile = qualityFile;
    }

    public FormatType getFormatType() {
        return formatType;
    }

    public void setFormatType(FormatType formatType) {
        this.formatType = formatType;
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

    public Path getOut() {
        return out;
    }

    @Option(name = "-o", aliases = "--out", usage = "out")
    public void setOut(Path out) throws IOException {
        if (Files.notExists(Paths.get(String.format("%s/%s", out, job))))
            Files.createDirectories(Paths.get(String.format("%s/%s", out, job)));
        this.out = Paths.get(String.format("%s/%s", out, job));
    }

    public EnumTypes getType() {
        return type;
    }

    public void setType(EnumTypes type) {
        this.type = type;
    }

    public void run() {
        logger.info("   Input                        :   {}", input);
        logger.info("   QualityInterface config      :   {}", qualityFile);

        logger.info("   Type                         :   {}", type);
        logger.info("   Format                       :   {}", formatType);

        if (formatType.equals(FormatType.DATASTORE)) {
            logger.info("   QualityFile host                 :   {}", host);
            logger.info("   QualityFile port                 :   {}", port);
            logger.info("   QualityFile name                 :   {}", name);
        } else if (Objects.nonNull(out))
            logger.info("   out                          :   {}", getOut());
    }
}
