/**
 * 
 */

package org.Recollect.Core.parameters;

public class GetRecordParameters {
    public static GetRecordParameters request () {
        return new GetRecordParameters();
    }

    private String identifier;
    private String metadataPrefix;

    public String getIdentifier() {
        return identifier;
    }

    public GetRecordParameters withIdentifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

    public String getMetadataPrefix() {
        return metadataPrefix;
    }

    public GetRecordParameters withMetadataFormatPrefix(String metadataFormatPrefix) {
        this.metadataPrefix = metadataFormatPrefix;
        return this;
    }

    public boolean areValid() {
        return identifier != null && metadataPrefix != null;
    }
}
