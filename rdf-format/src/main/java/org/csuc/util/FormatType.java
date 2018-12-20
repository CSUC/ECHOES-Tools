package org.csuc.util;

import org.apache.jena.riot.Lang;

import java.util.List;

/**
 * @author amartinez
 */
public enum FormatType {

    RDFXML(Lang.RDFXML, Lang.RDFXML.getFileExtensions()),
    NTRIPLES(Lang.NTRIPLES, Lang.NTRIPLES.getFileExtensions()),
    TURTLE(Lang.TURTLE, Lang.TURTLE.getFileExtensions()),
    JSONLD(Lang.JSONLD, Lang.JSONLD.getFileExtensions()),
    RDFJSON(Lang.RDFJSON, Lang.RDFJSON.getFileExtensions()),
    NQ(Lang.NQ, Lang.NQ.getFileExtensions()),
    NQUADS(Lang.NQUADS, Lang.NQUADS.getFileExtensions()),
    TRIG(Lang.TRIG, Lang.TRIG.getFileExtensions()),
    RDFTHRIFT(Lang.RDFTHRIFT, Lang.RDFTHRIFT.getFileExtensions()),
    TRIX(Lang.TRIX, Lang.TRIX.getFileExtensions()),
    CSV(Lang.CSV, Lang.CSV.getFileExtensions()),
    TSV(Lang.TSV, Lang.TSV.getFileExtensions());

    private final Lang lang;
    private final List<String> extensions;

    FormatType(Lang lang, List<String> extensions){
        this.lang = lang;
        this.extensions = extensions;
    }

    public Lang lang() {
        return lang;
    }

    public List<String> extensions(){
        return extensions;
    }

    public static FormatType convert(String value) {
        for (FormatType inst : values()) {
            if (inst.name().equals(value)) {
                return inst;
            }
        }
        return null;
    }
}
