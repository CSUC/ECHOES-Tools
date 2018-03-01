/**
 * 
 */
package org.csuc.serialize;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.JiBXException;

import java.io.OutputStream;
import java.io.Writer;

/**
 * @author amartinez
 *
 */
public class JibxMarshall {

	private static Logger logger = LogManager.getLogger(JibxMarshall.class);
	
//	private static int ident = 4;
	
	/**
	 * 
	 * @param root
	 * @param classType
	 */
	public static void marshall(Object root, Class<?> classType) {
		try {
			IBindingFactory jc = BindingDirectory.getFactory(classType);
			IMarshallingContext marshaller = jc.createMarshallingContext();

			marshaller.marshalDocument(root);
		}catch (JiBXException exception) {
			logger.error(exception);
		}
	}
	
	/**
	 * 
	 * @param root
	 * @param enc
	 * @param alone
	 * @param classType
	 */
	public  static void marshall(Object root, String enc, Boolean alone, Class<?> classType, Integer ident) {
		try {
			IBindingFactory jc = BindingDirectory.getFactory(classType);
			IMarshallingContext marshaller = jc.createMarshallingContext();
			
			marshaller.setIndent(ident);
			marshaller.marshalDocument(root, enc, alone);
		}catch (JiBXException exception) {
			logger.error(exception);
		}
	}
	
	/**
	 * 
	 * @param root
	 * @param enc
	 * @param alone
	 * @param outs
	 * @param classType
	 */
	public  static void marshall(Object root, String enc, Boolean alone, OutputStream outs, Class<?> classType, Integer ident) {
		try {
			IBindingFactory jc = BindingDirectory.getFactory(classType);
			IMarshallingContext marshaller = jc.createMarshallingContext();
			
			marshaller.setIndent(ident);
			marshaller.marshalDocument(root, enc, alone, outs);
		}catch (JiBXException exception) {
			logger.error(exception);
		}
	}
	
	/**
	 * 
	 * @param root
	 * @param enc
	 * @param alone
	 * @param outw
	 * @param classType
	 */
	public  static void marshall(Object root, String enc, Boolean alone, Writer outw, Class<?> classType, Integer ident) {
		try {
			IBindingFactory jc = BindingDirectory.getFactory(classType);
			IMarshallingContext marshaller = jc.createMarshallingContext();
			
			marshaller.setIndent(ident);
			marshaller.marshalDocument(root, enc, alone, outw);
		}catch (JiBXException exception) {
			logger.error(exception);
		}
	}
	
}
