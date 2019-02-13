package org.csuc.entities.validation;

import org.mongodb.morphia.annotations.*;

import java.util.UUID;

/**
 * @author amartinez
 */
@Entity(value = "validation-details", noClassnameStored = true)
@Indexes(
        @Index(fields = {@Field("_id")}, options = @IndexOptions(unique = true))
)
public class ValidationDetails {

    @Id
    private String _id;

    @Property
    private String uuid;

    @Embedded
    private SCH sch;

    @Embedded
    private Schema schema;

    @Reference("validation")
    private Validation validation;

    public ValidationDetails() {
        this._id = UUID.randomUUID().toString();
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public SCH getSch() {
        return sch;
    }

    public void setSch(SCH sch) {
        this.sch = sch;
    }

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    public Validation getValidation() {
        return validation;
    }

    public void setValidation(Validation validation) {
        this.validation = validation;
    }


}
