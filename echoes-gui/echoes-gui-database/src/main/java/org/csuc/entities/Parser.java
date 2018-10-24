/**
 *
 */
package org.csuc.entities;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import org.csuc.utils.Status;
import org.csuc.utils.parser.ParserFormat;
import org.csuc.utils.parser.ParserMethod;
import org.csuc.utils.parser.ParserType;
import org.mongodb.morphia.annotations.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author amartinez
 */
@Entity(value = "parser", noClassnameStored = true)
@Indexes(
        @Index(fields = {@Field("_id")}, options = @IndexOptions(unique = true))
)
public class Parser {

    @Id
    private String _id;

    @Property("timestamp")
    private String timestamp = LocalDateTime.now().toString();

    @Property("type")
    private ParserType type;

    @Property("method")
    private ParserMethod method;

    @Property("value")
    private String value;

    @Property("status")
    private Status status;

    @Property("format")
    private ParserFormat format;

    @Property("duration")
    private String duration;

    @Property("user")
    private String user;


    public Parser() {
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

    public ParserType getType() {
        return type;
    }

    public void setType(ParserType type) {
        this.type = type;
    }

    public ParserMethod getMethod() {
        return method;
    }

    public void setMethod(ParserMethod method) {
        this.method = method;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ParserFormat getFormat() {
        return format;
    }

    public void setFormat(ParserFormat format) {
        this.format = format;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        JsonAdapter<Parser> jsonAdapter = new Moshi.Builder().build().adapter(Parser.class);
        return jsonAdapter.toJson(this);
    }
}
