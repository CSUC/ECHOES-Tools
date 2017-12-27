package org.csuc.Parser.Core.util.xml;

import com.fasterxml.jackson.annotation.JsonGetter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author amartinez
 */

public class NodeResult{

    private List<Node> node;

    @JsonGetter("node")
    public List<Node> getNode() {
        if(Objects.isNull(node)) node = new ArrayList<>();
        return node;
    }

    public void setNode(List<Node> node) {
        this.node = node;
    }
}
