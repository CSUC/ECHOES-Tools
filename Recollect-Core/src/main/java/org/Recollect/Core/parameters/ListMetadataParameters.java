/**
 * 
 */

package org.Recollect.Core.parameters;

public class ListMetadataParameters {
    public static ListMetadataParameters request() {
        return new ListMetadataParameters();
    }

    private String identifier;

    public String getIdentifier() {
        return identifier;
    }

    public ListMetadataParameters withIdentifier(String identifier) {
        this.identifier = identifier;
        return this;
    }
}
