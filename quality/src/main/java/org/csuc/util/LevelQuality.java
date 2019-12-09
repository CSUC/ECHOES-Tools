package org.csuc.util;

public enum LevelQuality {

    ERROR("error"),
    INFO("info"),
    WARNING("warning"),
    OFF("off");

    private final String level;

    LevelQuality(String level) {
        this.level = level;
    }

    public static LevelQuality convert(String value) {
        for (LevelQuality inst : values()) {
            if (inst.getLevel().equalsIgnoreCase(value)) {
                return inst;
            }
        }
        return INFO;
    }

    public String getLevel() {
        return level;
    }
}
