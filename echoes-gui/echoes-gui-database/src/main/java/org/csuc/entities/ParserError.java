package org.csuc.entities;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import org.mongodb.morphia.annotations.*;

import java.util.UUID;

/**
 * @author amartinez
 */
@Entity(value = "parser-error", noClassnameStored = true)
@Indexes(
        @Index(fields = {@Field("_id")}, options = @IndexOptions(unique = true))
)
public class ParserError {

    @Id
    private String _id;

    @Reference("parser-id")
    private Parser parser;

    @Property("exception")
    private String exception;

    public ParserError() {
        this._id = UUID.randomUUID().toString();
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Parser getParser() {
        return parser;
    }

    public void setParser(Parser parser) {
        this.parser = parser;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    @Override
    public String toString() {
        JsonAdapter<ParserError> jsonAdapter = new Moshi.Builder().build().adapter(ParserError.class);
        return jsonAdapter.toJson(this);
    }
}
