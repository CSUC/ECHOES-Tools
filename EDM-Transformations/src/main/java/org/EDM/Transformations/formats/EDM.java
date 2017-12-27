/**
 * 
 */
package org.EDM.Transformations.formats;

import eu.europeana.corelib.definitions.jibx.RDF;
import org.EDM.Transformations.formats.xslt.XSLTTransformations;
import org.csuc.deserialize.JibxUnMarshall;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
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
    XSLTTransformations transformation(OutputStream out, Map<String,String> xsltProperties) throws Exception;

	/**
	 *
	 */
    XSLTTransformations transformation(String xslt, OutputStream out, Map<String,String> xsltProperties) throws Exception;

    /**
     *
     */
    XSLTTransformations transformation(String xslt) throws Exception;

	/**
	 *
	 */
	void creation();

    /**
     *
     */
    void creation(Charset encoding, boolean alone, OutputStream outs);

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
