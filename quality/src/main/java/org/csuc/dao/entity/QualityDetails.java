package org.csuc.dao.entity;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import org.csuc.adapter.BigIntegerAdapter;
import org.csuc.adapter.LocalDateTimeAdapter;
import org.csuc.dao.entity.edm.Edm;
import org.mongodb.morphia.annotations.*;

import java.math.BigInteger;
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
    protected String _id;

    @Property("input")
    protected String input;

    @Property("isValidSchema")
    private boolean isValidSchema = false;

    @Property("isValidSchematron")
    private boolean isValidSchematron = false;

    @Property("isValidContent")
    private boolean isValidContent = false;

    @Embedded
    private Schema schema;

    @Embedded
    private List<Schematron> schematron;

    @Embedded
    private Edm edm;

    @Reference("quality")
    private Quality quality;

    public QualityDetails(String uuid, String input) {
        this._id = uuid;
        this.input = input;
    }

    public QualityDetails(String input) {
        this._id = UUID.randomUUID().toString();
        this.input = input;
    }

    public QualityDetails() {
        this._id = UUID.randomUUID().toString();
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public boolean isValidSchema() {
        return isValidSchema;
    }

    public void setValidSchema(boolean validSchema) {
        isValidSchema = validSchema;
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

    public boolean isValidContent() {
        return isValidContent;
    }

    public void setValidContent(boolean validContent) {
        isValidContent = validContent;
    }

    public Edm getEdm() {
        return edm;
    }

    public void setEdm(Edm edm) {
        this.edm = edm;
    }

    @Override
    public String toString() {
        JsonAdapter<QualityDetails> jsonAdapter =
                new Moshi.Builder()
                        .add(LocalDateTime.class, new LocalDateTimeAdapter().nullSafe())
                        .add(BigInteger.class, new BigIntegerAdapter().nullSafe())
                        .build()
                        .adapter(QualityDetails.class);

        return jsonAdapter.toJson(this);
    }
}
