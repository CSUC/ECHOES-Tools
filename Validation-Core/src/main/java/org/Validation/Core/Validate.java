/**
 * 
 */
package org.Validation.Core;

import org.csuc.deserialize.JibxUnMarshall;

import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Objects;

/**
 * @author amartinez
 *
 */
public class Validate extends JibxUnMarshall {

	
	public Validate(InputStream ins, Charset enc, Class<?> classType) {
		super(ins, enc, classType);
	}

	public Validate(InputStream ins, String name, Charset enc, Class<?> classType) {
		super(ins, name, enc, classType);
	}

	public Validate(Reader rdr, Class<?> classType) {
		super(rdr, classType);
	}

	public Validate(Reader rdr, String name, Class<?> classType) {
		super(rdr, name, classType);
	}

	public boolean isValid() {		
		return Objects.nonNull(getElement()) ? true : false;
	}
	
	
}
