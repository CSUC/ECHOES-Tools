package org.csuc.analyse.core.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.analyse.core.strategy.ParserMethod;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

/**
 * @author amartinez
 */
public class ParserURL implements Parser {

    private static Logger logger = LogManager.getLogger(ParserURL.class);

    private ParserMethod method;

    public ParserURL(ParserMethod method){
        logger.debug(String.format("analyse: %s", getClass().getSimpleName()));
        this.method = method;
    }

    @Override
    public void execute(String fileOrPath) throws Exception {
        throw new IllegalArgumentException("execute fileOrPath is not valid for ParserURL!");
    }

    @Override
    public void execute(URL url) throws Exception {
        method.parser(url);
    }

    @Override
    public void XML(OutputStream outs) {
        method.createXML(outs);
    }

    @Override
    public void HDFS_XML(String uri, String user, String home, org.apache.hadoop.fs.Path dest) throws IOException {
        method.createHDFS_XML(uri, user, home, dest);
    }

    @Override
    public void JSON(OutputStream outs) {
        method.createJSON(outs);
    }

    @Override
    public void HDFS_JSON(String uri, String user, String home, org.apache.hadoop.fs.Path dest) throws IOException {
        method.createHDFS_JSON(uri, user, home, dest);
    }

}
