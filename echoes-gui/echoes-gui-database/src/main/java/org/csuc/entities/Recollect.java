package org.csuc.entities;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import org.csuc.utils.Status;
import org.mongodb.morphia.annotations.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * @author amartinez
 */
@Entity(value = "recollect", noClassnameStored = true)
@Indexes(
        @Index(fields = {@Field("_id")}, options = @IndexOptions(unique = true))
)
public class Recollect {

    @Id
    private String _id;

    @Property("timestamp")
    private String timestamp = LocalDateTime.now().toString();

    @Property("host")
    private String host;

    @Property("set")
    private String set;

    @Property("metadataPrefix")
    private String metadataPrefix;

    @Property("from")
    private String from;

    @Property("until")
    private String until;

    @Property("granularity")
    private String Granularity;

    @Property("status")
    private Status status;

    @Property("user")
    private String user;

    @Property("duration")
    private String duration;

    @Embedded("properties")
    private Map<String, String> properties;

    @Reference("link")
    private RecollectLink link;

    @Reference("error")
    private RecollectError error;


    public Recollect() {
        this._id = UUID.randomUUID().toString();
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getSet() {
        return set;
    }

    public void setSet(String set) {
        this.set = set;
    }

    public String getMetadataPrefix() {
        return metadataPrefix;
    }

    public void setMetadataPrefix(String metadataPrefix) {
        this.metadataPrefix = metadataPrefix;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getUntil() {
        return until;
    }

    public void setUntil(String until) {
        this.until = until;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public String getGranularity() {
        return Granularity;
    }

    public void setGranularity(String granularity) {
        Granularity = granularity;
    }

    public RecollectLink getLink() {
        return link;
    }

    public void setLink(RecollectLink link) {
        this.link = link;
    }

    public RecollectError getError() {
        return error;
    }

    public void setError(RecollectError error) {
        this.error = error;
    }

    @Override
    public String toString() {
        JsonAdapter<Recollect> jsonAdapter = new Moshi.Builder().build().adapter(Recollect.class);
        return jsonAdapter.toJson(this);
    }
}
