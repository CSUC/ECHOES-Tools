package org.csuc.entities;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import org.csuc.adapter.LocalDateTimeAdapter;
import org.csuc.utils.Status;
import org.csuc.utils.recollect.TransformationType;
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
    private LocalDateTime timestamp = LocalDateTime.now();

    @Property("type")
    private TransformationType type;

    @Property("input")
    private String input;

    @Property("filename")
    private String filename;

    @Property("format")
    private String format;

    @Property("schema")
    private String schema;

    @Property("status")
    private Status status;

    @Property("user")
    private String user;

    @Property("duration")
    private String duration;

    @Property("size")
    private int size;

    @Property("download")
    private int download;

    @Embedded("properties")
    private Map<String, String> properties;

    @Reference(value = "link", ignoreMissing = true)
    private RecollectLink link;

    @Reference(value = "error", ignoreMissing = true)
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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public TransformationType getType() {
        return type;
    }

    public void setType(TransformationType type) {
        this.type = type;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getDownload() {
        return download;
    }

    public void setDownload(int download) {
        this.download = download;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public String toString() {
        JsonAdapter<Recollect> jsonAdapter =
                new Moshi.Builder()
                        .add(LocalDateTime.class, new LocalDateTimeAdapter().nullSafe())
                        .build()
                        .adapter(Recollect.class);

        return jsonAdapter.toJson(this);
    }
}
