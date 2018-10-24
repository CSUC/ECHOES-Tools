package org.Recollect.Core.util;

public interface Verb {

	 public static enum Type {
	        Identify("Identify"),
	        ListMetadataFormats("ListMetadataFormats"),
	        ListSets("ListSets"),
	        GetRecord("GetRecord"),
	        ListIdentifiers("ListIdentifiers"),
	        ListRecords("ListRecords");

	        private final String value;

	        Type(String value) {
	            this.value = value;
	        }

	        public String displayName() {
	            return value;
	        }

	        public static Type fromValue(String value) {
	            for (Type c : Type.values()) {
	                if (c.value.equals(value)) {
	                    return c;
	                }
	            }
	            throw new IllegalArgumentException(value);
	        }

	    }

	    Type getType ();
}
