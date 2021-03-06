package org.csuc.analyse.util.xml;

import com.fasterxml.jackson.annotation.JsonGetter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import java.io.Serializable;

/**
 * @author amartinez
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Namespace implements Serializable {

    @XmlAttribute(name="uri")
    private String uri;

    @XmlAttribute(name="prefix")
    private String prefix;

    @JsonGetter("node_result")
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @JsonGetter("prefix")
    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
