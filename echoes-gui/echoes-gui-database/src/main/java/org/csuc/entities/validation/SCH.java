package org.csuc.entities.validation;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import org.bson.Document;
import org.json.JSONArray;

import java.util.List;


/**
 * @author amartinez
 */
public class SCH {

    private boolean isValid;

    private List<Document> message;

    public SCH() {
    }

    public SCH(boolean isValid, List<Document> message) {
        this.isValid = isValid;
        this.message = message;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public List<Document> getMessage() {
        return message;
    }

    public void setMessage(List<Document> message) {
        this.message = message;
    }

}
