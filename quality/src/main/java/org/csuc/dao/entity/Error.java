package org.csuc.dao.entity;

import org.csuc.util.LevelQuality;
import org.mongodb.morphia.annotations.Property;

/**
 * @author amartinez
 */
public class Error {

    @Property("type")
    protected String type;

    @Property("metadata")
    protected String metadata;

    @Property("validation-type")
    protected String validationType;

    @Property("message")
    protected String message;

    @Property("level")
    protected LevelQuality levelQuality;

    public Error(String type, String metadata, String validationType, String message, LevelQuality levelQuality) {
        this.type = type;
        this.metadata = metadata;
        this.validationType = validationType;
        this.message = message;
        this.levelQuality = levelQuality;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
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

    public String getValidationType() {
        return validationType;
    }

    public void setValidationType(String validationType) {
        this.validationType = validationType;
    }

    public LevelQuality getLevelQuality() {
        return levelQuality;
    }

    public void setLevelQuality(LevelQuality levelQuality) {
        this.levelQuality = levelQuality;
    }
}
