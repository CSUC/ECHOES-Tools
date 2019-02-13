package org.csuc.entities.validation;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

/**
 * @author amartinez
 */
public class Schema {

    private boolean isValid;
    private String message;

    public Schema() {
    }

    public Schema(boolean isValid, String message) {
        this.isValid = isValid;
        this.message = message;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
