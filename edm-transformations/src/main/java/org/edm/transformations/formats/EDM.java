/**
 * 
 */
package org.edm.transformations.formats;

import eu.europeana.corelib.definitions.jibx.RDF;
import org.csuc.deserialize.JibxUnMarshall;
import org.csuc.util.FormatType;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Map;


/**
 * 
 * 
 * 
 * @author amartinez
 *
 */
public interface EDM {

    /**
     *
     */
    void transformation(OutputStream out, Map<String,String> xsltProperties) throws Exception;

	/**
	 *
	 */
    void transformation(String xslt, OutputStream out, Map<String,String> xsltProperties) throws Exception;

    /**
     *
     */
    void transformation(String xslt) throws Exception;

	/**
	 *
	 */
	void creation();

    /**
     *
     */
    void creation(FormatType formatType) throws Exception;

    /**
     *
     */
    void creation(Charset encoding, boolean alone, OutputStream outs);

    /**
     *
     */
    void creation(Charset encoding, boolean alone, OutputStream outs, FormatType formatType) throws Exception;


    /**
     *
     */
    void creation(Charset encoding, boolean alone, Writer writer);

	/**
	 *
	 */
    JibxUnMarshall validateSchema(InputStream ins, Charset enc, Class<?> classType);

    /**
     *
     */
    JibxUnMarshall validateSchema(InputStream ins, String name, Charset enc, Class<?> classType);


    /**
     *
     */
    JibxUnMarshall validateSchema(Reader rdr, Class<?> classType);


    /**
     *
     */
    JibxUnMarshall validateSchema(Reader rdr, String name, Class<?> classType);


    /**
     *
     */
    void modify(RDF rdf);
}
