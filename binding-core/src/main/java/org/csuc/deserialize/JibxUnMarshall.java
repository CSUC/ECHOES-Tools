/**
 * 
 */
package org.csuc.deserialize;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;

import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;

/**
 * @author amartinez
 *
 */
public class JibxUnMarshall {
	
	private Object element;
	private Class<?> type;
	
	private JiBXException error;
	
	private InputStream ins;
	private Reader rdr;
	private Charset enc;
	private String name;

	public JibxUnMarshall(InputStream ins, Charset enc, Class<?> classType) {
		this.type = classType;
		this.ins = ins;
		this.enc = enc;
		
		try {
			IBindingFactory jc = BindingDirectory.getFactory(classType);
			IUnmarshallingContext unmarshaller = jc.createUnmarshallingContext();
			
			element = unmarshaller.unmarshalDocument(ins, enc.toString());
			
		}catch (JiBXException e) {
			error = e;			
		} 
	}
	
	public JibxUnMarshall(InputStream ins, String name, Charset enc, Class<?> classType) {
		this.type = classType;
		this.ins = ins;
		this.enc = enc;
		this.name = name;
		
		try {
			IBindingFactory jc = BindingDirectory.getFactory(classType);
			IUnmarshallingContext unmarshaller = jc.createUnmarshallingContext();
			
			element = unmarshaller.unmarshalDocument(ins, name, enc.toString());
			
		}catch (JiBXException e) {
			error = e;
		} 
	}
	
	public JibxUnMarshall(Reader rdr, Class<?> classType) {
		this.type = classType;
		this.rdr = rdr;
		
		try {
			IBindingFactory jc = BindingDirectory.getFactory(classType);
			IUnmarshallingContext unmarshaller = jc.createUnmarshallingContext();
			
			element = unmarshaller.unmarshalDocument(rdr);
			
		}catch (JiBXException e) {
			error = e;
		} 
	}
	
	public JibxUnMarshall(Reader rdr, String name, Class<?> classType) {
		this.type = classType;
		this.rdr = rdr;
		this.name = name;
		 
		try {
			IBindingFactory jc = BindingDirectory.getFactory(classType);
			IUnmarshallingContext unmarshaller = jc.createUnmarshallingContext();
			
			element = unmarshaller.unmarshalDocument(rdr, name);
			
		}catch (JiBXException e) {
			error = e;
		}
	}
	
	public Object getElement() {
		return element;
	}
	
	public Class<?> getType() {
		return type;
	}
	
	public InputStream getIns() {
		return ins;
	}
	
	public Reader getRdr() {
		return rdr;
	}
	
	public Charset getEnc() {
		return enc;
	}
	
	public String getName() {
		return name;
	}
	
	public JiBXException getError() {
		return error;
	}
}
