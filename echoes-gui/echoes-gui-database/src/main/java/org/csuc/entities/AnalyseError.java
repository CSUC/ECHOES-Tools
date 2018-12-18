package org.csuc.entities;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import org.mongodb.morphia.annotations.*;

import java.util.UUID;

/**
 * @author amartinez
 */
@Entity(value = "analyse-error", noClassnameStored = true)
@Indexes(
        @Index(fields = {@Field("_id")}, options = @IndexOptions(unique = true))
)
public class AnalyseError {

    @Id
    private String _id;

    @Reference("analyse-id")
    private Analyse analyse;

    @Property("exception")
    private String exception;

    public AnalyseError() {
        this._id = UUID.randomUUID().toString();
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Analyse getAnalyse() {
        return analyse;
    }

    public void setAnalyse(Analyse analyse) {
        this.analyse = analyse;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    @Override
    public String toString() {
        JsonAdapter<AnalyseError> jsonAdapter = new Moshi.Builder().build().adapter(AnalyseError.class);
        return jsonAdapter.toJson(this);
    }
}
