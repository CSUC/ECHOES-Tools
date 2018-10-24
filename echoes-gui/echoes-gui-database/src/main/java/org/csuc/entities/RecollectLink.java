package org.csuc.entities;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import org.csuc.utils.recollect.StatusLink;
import org.mongodb.morphia.annotations.*;

import java.util.UUID;

/**
 * @author amartinez
 */
@Entity(value = "recollect-link", noClassnameStored = true)
@Indexes(
        @Index(fields = {@Field("_id")}, options = @IndexOptions(unique = true))
)
public class RecollectLink {

    @Id
    private String _id;

    @Property("status")
    private StatusLink statusLink;

    public RecollectLink() {
        this._id = UUID.randomUUID().toString();
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public StatusLink getStatusLink() {
        return statusLink;
    }

    public void setStatusLink(StatusLink statusLink) {
        this.statusLink = statusLink;
    }

    @Override
    public String toString() {
        JsonAdapter<RecollectLink> jsonAdapter = new Moshi.Builder().build().adapter(RecollectLink.class);
        return jsonAdapter.toJson(this);
    }
}
