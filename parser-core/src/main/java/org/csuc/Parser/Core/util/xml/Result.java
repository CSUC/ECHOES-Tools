package org.csuc.Parser.Core.util.xml;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

/**
 * @author amartinez
 */
@XmlRootElement(name = "result")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonRootName("result")
@JsonPropertyOrder({"node_result","namespace_result"})
public class Result {

    @XmlElement(name="node_result")
    private NodeResult node;

    @XmlElement(name="namespace_result")
    private NamespaceResult namespace;

    @JsonGetter("node_result")
    public NodeResult getNodeResult() {
        if(Objects.isNull(node)) node = new NodeResult();
        return node;
    }

    public void setNodeResult(NodeResult node) {
        this.node = node;
    }

    @JsonGetter("namespace_result")
    public NamespaceResult getNamespaceResult() {
        if(Objects.isNull(namespace)) namespace = new NamespaceResult();
        return namespace;
    }

    public void setNamespaceResult(NamespaceResult namespace) {
        this.namespace = namespace;
    }
}

