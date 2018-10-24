package org.csuc.entities;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import org.mongodb.morphia.annotations.*;

import java.util.UUID;

/**
 * @author amartinez
 */
@Entity(value = "recollect-error", noClassnameStored = true)
@Indexes(
        @Index(fields = {@Field("_id")}, options = @IndexOptions(unique = true))
)
public class RecollectError {

    @Id
    private String _id;

    @Property("exception")
    private String exception;

    public RecollectError() {
        this._id = UUID.randomUUID().toString();
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    @Override
    public String toString() {
        JsonAdapter<RecollectError> jsonAdapter = new Moshi.Builder().build().adapter(RecollectError.class);
        return jsonAdapter.toJson(this);
    }
}
