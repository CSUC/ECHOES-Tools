package org.csuc.quality.assurance.core.schema;

import org.csuc.deserialize.JibxUnMarshall;
import org.jibx.runtime.JiBXException;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author amartinez
 */
public class Schema {

    private JibxUnMarshall jibxUnMarshall;

    public Schema(InputStream inputStream, Class<?> classType) {
        jibxUnMarshall = new JibxUnMarshall(inputStream, StandardCharsets.UTF_8, classType);
    }

    public Schema(InputStream inputStream, Charset enc, Class<?> classType) {
        jibxUnMarshall = new JibxUnMarshall(inputStream, enc, classType);
    }

    public boolean isValid(){
        return Objects.isNull(jibxUnMarshall.getError());
    }


    public JiBXException getError() {
        return jibxUnMarshall.getError();
    }
}
