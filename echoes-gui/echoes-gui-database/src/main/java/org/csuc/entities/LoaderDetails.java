package org.csuc.entities;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import org.csuc.adapter.LocalDateTimeAdapter;
import org.csuc.utils.Status;
import org.mongodb.morphia.annotations.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author amartinez
 */
@Entity(value = "loader-details", noClassnameStored = true)
@Indexes(
        @Index(fields = {@Field("_id")}, options = @IndexOptions(unique = true))
)
public class LoaderDetails {

    @Id
    private String _id;

    @Property("status-code")
    private int status;

    @Property("message")
    private String message;

    @Reference("loader")
    private Loader loader;

    public LoaderDetails() {
        this._id = UUID.randomUUID().toString();
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Loader getLoader() {
        return loader;
    }

    public void setLoader(Loader loader) {
        this.loader = loader;
    }

    @Override
    public String toString() {
        JsonAdapter<LoaderDetails> jsonAdapter =
                new Moshi.Builder()
                        .add(LocalDateTime.class, new LocalDateTimeAdapter().nullSafe())
                        .build()
                        .adapter(LoaderDetails.class);

        return jsonAdapter.toJson(this);
    }
}
