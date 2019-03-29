package org.csuc.entities.quality;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import org.csuc.adapter.LocalDateTimeAdapter;
import org.mongodb.morphia.annotations.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author amartinez
 */
@Entity(value = "quality-details", noClassnameStored = true)
@Indexes(
        @Index(fields = {@Field("_id")}, options = @IndexOptions(unique = true))
)
public class QualityDetails {

    @Id
    private String _id;

    @Property("value")
    private String value;

    @Property("isValidSchema")
    private boolean isValidSchema = false;

    @Property("isValidSchematron")
    private boolean isValidSchematron = false;

    @Embedded
    private Schema schema;

    @Embedded
    private List<Schematron> schematron;

    @Reference("quality")
    private Quality quality;

    public QualityDetails() {
        this._id = UUID.randomUUID().toString();
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    public Quality getQuality() {
        return quality;
    }

    public void setQuality(Quality quality) {
        this.quality = quality;
    }

    public boolean isValidSchema() {
        return isValidSchema;
    }

    public void setValidSchema(boolean validSchema) {
        isValidSchema = validSchema;
    }

    public boolean isValidSchematron() {
        return isValidSchematron;
    }

    public void setValidSchematron(boolean validSchematron) {
        isValidSchematron = validSchematron;
    }

    public List<Schematron> getSchematron() {
        return schematron;
    }

    public void setSchematron(List<Schematron> schematron) {
        this.schematron = schematron;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        JsonAdapter<QualityDetails> jsonAdapter =
                new Moshi.Builder()
                        .add(LocalDateTime.class, new LocalDateTimeAdapter().nullSafe())
                        .build()
                        .adapter(QualityDetails.class);

        return jsonAdapter.toJson(this);
    }
}
