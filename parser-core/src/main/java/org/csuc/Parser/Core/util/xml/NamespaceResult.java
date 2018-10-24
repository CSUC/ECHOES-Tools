package org.csuc.Parser.Core.util.xml;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author amartinez
 */
public class NamespaceResult {

    private List<Namespace> namespaces;

    public List<Namespace> getNamespaces() {
        if(Objects.isNull(namespaces)) namespaces = new ArrayList<>();
        return namespaces;
    }

    public void setNamespaces(List<Namespace> namespaces) {
        this.namespaces = namespaces;
    }
}
