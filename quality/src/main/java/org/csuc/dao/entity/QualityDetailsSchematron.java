package org.csuc.dao.entity;

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
@Entity(value = "quality-details-schematron", noClassnameStored = true)
@Indexes(
        @Index(fields = {@Field("_id")}, options = @IndexOptions(unique = true))
)
public class QualityDetailsSchematron {

    @Id
    protected String _id;

    @Property("input")
    protected String input;

    @Property("isValidSchema")
    private boolean isValidSchematron = false;

    @Embedded
    private List<Schematron> schematron;

    @Reference("quality")
    private Quality quality;

    public QualityDetailsSchematron(String uuid, String input) {
        this._id = uuid;
        this.input = input;
    }

    public QualityDetailsSchematron(String input) {
        this._id = UUID.randomUUID().toString();
        this.input = input;
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

    public Quality getQuality() {
        return quality;
    }

    public void setQuality(Quality quality) {
        this.quality = quality;
    }

    @Override
    public String toString() {
        JsonAdapter<QualityDetailsSchematron> jsonAdapter =
                new Moshi.Builder()
                        .add(LocalDateTime.class, new LocalDateTimeAdapter().nullSafe())
                        .build()
                        .adapter(QualityDetailsSchematron.class);

        return jsonAdapter.toJson(this);
    }
}
