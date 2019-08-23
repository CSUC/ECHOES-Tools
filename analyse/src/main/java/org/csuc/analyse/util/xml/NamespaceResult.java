package org.csuc.analyse.util.xml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author amartinez
 */
public class NamespaceResult implements Serializable {

    private CopyOnWriteArrayList<Namespace> namespaces;

    public CopyOnWriteArrayList<Namespace> getNamespaces() {
        if(Objects.isNull(namespaces)) namespaces = new CopyOnWriteArrayList<>();
        return namespaces;
    }

    public void setNamespaces(CopyOnWriteArrayList<Namespace> namespaces) {
        this.namespaces = namespaces;
    }
}
