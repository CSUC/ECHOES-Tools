package org.csuc.entities.validation;

import org.csuc.utils.Status;
import org.mongodb.morphia.annotations.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author amartinez
 */
@Entity(value = "validation", noClassnameStored = true)
@Indexes(
        @Index(fields = {@Field("_id")}, options = @IndexOptions(unique = true))
)
public class Validation {

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

    public Validation() {
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


}
