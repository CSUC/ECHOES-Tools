package org.Recollect.Core.parameters;

import java.util.Date;

public class ListRecordsParameters {
	
    public static ListRecordsParameters request() {
        return new ListRecordsParameters();
    }

    private String metadataPrefix;
    private String setSpec;
    private Date from;
    private Date until;
	private String granularity;
	
	private String resumptionToken;

    public String getMetadataPrefix() {
        return metadataPrefix;
    }

    public ListRecordsParameters withMetadataPrefix(String metadataPrefix) {
        this.metadataPrefix = metadataPrefix;
        return this;
    }

    public String getSetSpec() {
        return setSpec;
    }

    public ListRecordsParameters withSetSpec(String setSpec) {
        this.setSpec = setSpec;
        return this;
    }

    public Date getFrom() {
        return from;
    }

    public ListRecordsParameters withFrom(Date from) {
        this.from = from;
        return this;
    }

    public Date getUntil() {
        return until;
    }

    public ListRecordsParameters withUntil(Date until) {
        this.until = until;
        return this;
    }

    public boolean areValid() {
        return metadataPrefix != null;
    }

	public void withGranularity(String granularity) {
		this.granularity = granularity;
		
	}

	public String getGranularity() {
		return granularity;
	}
	
	public String getResumptionToken() {
		return resumptionToken;
	}
	
	public void withgetResumptionToken(String resumptionToken) {
		this.resumptionToken = resumptionToken;
		
	}
}
