package org.csuc.dao.entity;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import org.csuc.util.EntityType;
import org.csuc.util.LevelQuality;
import org.csuc.util.MetadataType;
import org.csuc.util.QualityType;
import org.mongodb.morphia.annotations.Property;

/**
 * @author amartinez
 */
public class Error {

    @Property("type")
    private EntityType type;

    @Property("metadata")
    private String metadata;

    @Property("validation-type")
    private QualityType validationType;

    @Property("message")
    private String message;

    @Property("level")
    private LevelQuality levelQuality;

    public Error(EntityType type, MetadataType metadata, QualityType validationType, String message, LevelQuality levelQuality) {
        this.type = type;
        this.metadata = metadata.getType();
        this.validationType = validationType;
        this.message = message;
        this.levelQuality = levelQuality;
    }

    public Error() {
    }

    public EntityType getType() {
        return type;
    }

    public void setType(EntityType type) {
        this.type = type;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public QualityType getValidationType() {
        return validationType;
    }

    public void setValidationType(QualityType validationType) {
        this.validationType = validationType;
    }

    public LevelQuality getLevelQuality() {
        return levelQuality;
    }

    public void setLevelQuality(LevelQuality levelQuality) {
        this.levelQuality = levelQuality;
    }

    @Override
    public String toString() {
        JsonAdapter<Error> jsonAdapter = new Moshi.Builder().build().adapter(Error.class);
        return jsonAdapter.toJson(this);
    }
}
