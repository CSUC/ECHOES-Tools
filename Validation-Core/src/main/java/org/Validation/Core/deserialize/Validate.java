/**
 * 
 */
package org.Validation.Core.deserialize;

import java.io.InputStream;
import java.io.Reader;
import java.util.Objects;

import org.Validation.Core.deserialize.JibxUnMarshall;

/**
 * @author amartinez
 *
 */
public class Validate extends JibxUnMarshall {

	
	public Validate(InputStream ins, String enc, Class<?> classType) {
		super(ins, enc, classType);
	}

	public Validate(InputStream ins, String name, String enc, Class<?> classType) {
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
