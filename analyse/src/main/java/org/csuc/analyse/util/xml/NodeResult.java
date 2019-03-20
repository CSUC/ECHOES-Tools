package org.csuc.analyse.util.xml;

import com.fasterxml.jackson.annotation.JsonGetter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author amartinez
 */

public class NodeResult implements Serializable {

    private CopyOnWriteArrayList<Node> node;

    @JsonGetter("node")
    public CopyOnWriteArrayList<Node> getNode() {
        if(Objects.isNull(node)) node = new CopyOnWriteArrayList<>();
        return node;
    }

    public void setNode(CopyOnWriteArrayList<Node> node) {
        this.node = node;
    }
}
