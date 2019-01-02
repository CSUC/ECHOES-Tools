package org.csuc;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.riot.system.ErrorHandlerFactory;
import org.csuc.util.FormatType;

import java.io.*;

/**
 * @author amartinez
 */
public class Format {

    public static void format(File file, FormatType formatType, OutputStream outputStream){
        Model model = RDFDataMgr.loadModel(file.toString());

        RDFDataMgr.write(outputStream, model, formatType.lang());
    }

    /**
     *
     * auto read model
     *
     *
     * @param file
     * @return
     */
    public static boolean isValid(File file){
        try{
            RDFDataMgr.loadModel(file.toString());
            return true;
        }catch (Exception e){
            System.err.println(e);
            return false;
        }
    }

    /**
     *
     * @param file
     * @param formatType
     * @return
     */
    public static boolean isValid(File file, FormatType formatType){
        try (InputStream in = new FileInputStream(file)) {
            RDFParser.create()
                    .source(in)
                    .lang(formatType.lang())
                    .errorHandler(ErrorHandlerFactory.errorHandlerStrict)
                    .parse(ModelFactory.createDefaultModel());

            return true;
        } catch (IOException e) {

            System.err.println(e);
            return false;
        }
    }
}
