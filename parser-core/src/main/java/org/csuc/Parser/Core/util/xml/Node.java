package org.csuc.Parser.Core.util.xml;

import com.fasterxml.jackson.annotation.JsonGetter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author amartinez
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Node {

    @XmlAttribute(name="name")
    private  String name;

    @XmlAttribute(name="count")
    private int count;

    @XmlAttribute(name="path")
    private  String path;

    @JsonGetter("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonGetter("count")
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @JsonGetter("path")
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
