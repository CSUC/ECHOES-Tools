package org.csuc.Parser.Core.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.Parser.Core.strategy.ParserMethod;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author amartinez
 */
public class ParserFILE implements Parser {

    private static Logger logger = LogManager.getLogger(ParserFILE.class);

    private ParserMethod method;

    private AtomicInteger iter = new AtomicInteger(0);

    public ParserFILE(ParserMethod method) {
        logger.info(String.format("Parser: %s", getClass().getSimpleName()));
        this.method = method;
    }

    @Override
    public void execute(String fileOrPath) {
        Path path = Paths.get(fileOrPath);
        if(Objects.nonNull(path) && Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
            try {
                Files.walk(path)
                        .filter(Files::isRegularFile)
                        .filter(f-> f.toString().endsWith(".xml"))
                        .forEach(f->{
                            logger.info(String.format("%s file: %s", iter.incrementAndGet(), f.getFileName()));
                            method.parser(f.toString());
                        });
            } catch (IOException e) {
                logger.error(e);
            }
        }
    }

    @Override
    public void execute(URL url) {
        throw new IllegalArgumentException("execute URL is not valid for ParserFile!");
    }

    @Override
    public void XML(OutputStream outs) {
        method.createXML(outs);
    }

    @Override
    public void JSON(OutputStream outs) {
        method.createJSON(outs);
    }

}
