package org.csuc.dao.entity;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import org.csuc.adapter.LocalDateTimeAdapter;
import org.mongodb.morphia.annotations.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author amartinez
 */
@Entity(value = "quality", noClassnameStored = true)
@Indexes(
        @Index(fields = {@Field("_id")}, options = @IndexOptions(unique = true))
)
public class Quality {

    @Id
    private String _id;

    @Property("timestamp")
    private LocalDateTime timestamp = LocalDateTime.now();

    @Property("content-type")
    private String contentType;

    @Property("status")
    private Status status;

    @Property("user")
    private String user;

    @Property("duration")
    private String duration;

    @Property("data")
    private String data;

    @Property("dataset-size")
    private int datasetSize;

    @Property("quality-size")
    private int qualitySize;

    @Property("error-size")
    private int errorSize;

    @Property("quality-config")
    private String qualityConfig;

    public Quality() {
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

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getDatasetSize() {
        return datasetSize;
    }

    public void setDatasetSize(int datasetSize) {
        this.datasetSize = datasetSize;
    }

    public int getQualitySize() {
        return qualitySize;
    }

    public void setQualitySize(int qualitySize) {
        this.qualitySize = qualitySize;
    }

    public int getErrorSize() {
        return errorSize;
    }

    public void setErrorSize(int errorSize) {
        this.errorSize = errorSize;
    }

    public String getQualityConfig() {
        return qualityConfig;
    }

    public void setQualityConfig(String qualityConfig) {
        this.qualityConfig = qualityConfig;
    }

    @Override
    public String toString() {
        JsonAdapter<Quality> jsonAdapter =
                new Moshi.Builder()
                        .add(LocalDateTime.class, new LocalDateTimeAdapter().nullSafe())
                        .build()
                        .adapter(Quality.class);

        return jsonAdapter.toJson(this);
    }
}
