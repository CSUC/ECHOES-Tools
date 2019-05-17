/**
 * 
 */

package org.transformation.parameters;

import org.transformation.util.DateProvider;
import org.transformation.util.Granularity;
import org.transformation.util.UTCDateProvider;
import org.transformation.util.Verb;
import org.transformation.util.URLEncoder;
import org.apache.commons.lang3.StringUtils;

import static java.net.URLDecoder.decode;
import static java.util.stream.Collectors.toList;
import static org.transformation.util.URLEncoder.encode;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Pattern;


public class Parameters {
    private static DateProvider formatter = new UTCDateProvider();

    public static Parameters parameters () {
        return new Parameters();
    }

    private Verb.Type verb;
    private String metadataPrefix;
    private String set;
    private Date from;
    private Date until;
    private String identifier;
    private String resumptionToken;
	private String granularity;

    public Parameters withVerb(Verb.Type verb) {
        this.verb = verb;
        return this;
    }

    public Parameters withUntil(Date until) {
        this.until = until;
        return this;
    }


    public Parameters withFrom(Date from) {
        this.from = from;
        return this;
    }

    public Parameters withSet(String value) {
        this.set = value;
        return this;
    }


    public Parameters identifier(String value) {
        this.identifier = value;
        return this;
    }

    public Parameters withResumptionToken(String value) {
        this.resumptionToken = value;
        this.metadataPrefix = null;
        this.until = null;
        this.set = null;
        this.from = null;
        return this;
    }

    public Parameters withoutResumptionToken () {
        this.resumptionToken = null;
        return this;
    }

    public Parameters withMetadataPrefix(String value) {
        this.metadataPrefix = value;
        return this;
    }

    public String toUrl(String baseUrl) {
        List<String> string = new ArrayList<String>();
        
        string.add("verb=" + this.verb.name());
        Granularity granularity = granularity();
        if (set != null) string.add("set=" + encode(set));
        if (from != null) string.add("from=" + encode(formatter.format(from,granularity)));
        if (until != null) string.add("until=" + encode(formatter.format(until,granularity)));
        if (identifier != null) string.add("identifier=" + encode(identifier));
        if (metadataPrefix != null) string.add("metadataPrefix=" + encode(metadataPrefix));
        if (resumptionToken != null) string.add("resumptionToken=" + encode(resumptionToken));
        return baseUrl + "?" + StringUtils.join(string, URLEncoder.SEPARATOR);
    }

    public void parserUrl(String input) throws MalformedURLException {
        URL url = new URL(input);

        List<AbstractMap.SimpleEntry<String, String>> list =
                Pattern.compile("&").splitAsStream(url.getQuery())
                        .map(s -> Arrays.copyOf(s.split("="), 2))
                        .map(o -> new AbstractMap.SimpleEntry<String, String>(decode(o[0]), decode(o[1])))
                        .collect(toList());

        list.forEach(stringStringSimpleEntry -> {
            if(stringStringSimpleEntry.getKey().equals("verb")){
                withVerb(Verb.Type.fromValue(stringStringSimpleEntry.getValue()));
            }
            //metadataPrefix;
            if(stringStringSimpleEntry.getKey().equals("metadataPrefix")){
                withMetadataPrefix(stringStringSimpleEntry.getValue());
            }
            //set;
            if(stringStringSimpleEntry.getKey().equals("set")){
                withSet(stringStringSimpleEntry.getValue());
            }
            //granularity;
            if(stringStringSimpleEntry.getKey().equals("granularity")){
                withGranularity(stringStringSimpleEntry.getValue());
            }
            //from;
            if(stringStringSimpleEntry.getKey().equals("from")){
                try {
                    withFrom(formatter.parse(stringStringSimpleEntry.getValue(), granularity()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            //until;
            if(stringStringSimpleEntry.getKey().equals("until")){
                try {
                    withUntil(formatter.parse(stringStringSimpleEntry.getValue(), granularity()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            //identifier;
            if(stringStringSimpleEntry.getKey().equals("identifier")){
                identifier(stringStringSimpleEntry.getValue());
            }
            //resumptionToken;
            if(stringStringSimpleEntry.getKey().equals("resumptionToken")){
                withResumptionToken(stringStringSimpleEntry.getValue());
            }
        });
    }

    /**
     * If a valid granularity field exists, return corresponding granularity.
     * Defaults to: Second
     * @return
     */
    private Granularity granularity() {
		if(granularity != null){
			for (int i = 0; i < Granularity.values().length; i++) {
				Granularity possibleGranularity = Granularity.values()[i];
				if(granularity.equals(possibleGranularity.toString())){
					return possibleGranularity;
				}
				
			}
			
		}
		return Granularity.Second;
	}

	public Parameters include(ListMetadataParameters parameters) {
        this.identifier = parameters.getIdentifier();
        return this;
    }

    public Parameters include(GetRecordParameters parameters) {
        this.identifier = parameters.getIdentifier();
        this.metadataPrefix = parameters.getMetadataPrefix();
        return this;
    }

    public Parameters include(ListRecordsParameters parameters) {
        this.metadataPrefix = parameters.getMetadataPrefix();
        this.set = parameters.getSetSpec();
        this.until = parameters.getUntil();
        this.from = parameters.getFrom();
        this.granularity = parameters.getGranularity();

        return this;
    }

    public Parameters include(ListIdentifiersParameters parameters) {
        this.metadataPrefix = parameters.getMetadataPrefix();
        this.set = parameters.getSetSpec();
        this.until = parameters.getUntil();
        this.from = parameters.getFrom();
        this.granularity = parameters.getGranularity();

        return this;
    }

    public Verb.Type getVerb() {
        return verb;
    }

    public String getMetadataPrefix() {
        return metadataPrefix;
    }

    public String getSet() {
        return set;
    }

    public Date getFrom() {
        return from;
    }

    public Date getUntil() {
        return until;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getResumptionToken() {
        return resumptionToken;
    }

	public void withGranularity(String granularity) {
		this.granularity = granularity;
		
	}

	public Object getGranularity() {
		return granularity;
	}
}