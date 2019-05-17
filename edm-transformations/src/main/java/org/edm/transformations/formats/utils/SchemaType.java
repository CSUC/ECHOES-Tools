package org.edm.transformations.formats.utils;

import isbn._1_931666_22_9.Ead;
import nl.memorix_maior.api.rest._3.Memorix;
import nl.mindbus.a2a.A2AType;
import org.openarchives.oai._2_0.oai_dc.OaiDcType;

/**
 * @author amartinez
 */
public enum SchemaType {

    A2A(A2AType.class),
    DC(OaiDcType.class),
    MEMORIX(Memorix.class),
    EAD(Ead.class);

    private Class<?> Class;

    SchemaType(Class<?> type){
        this.Class = type;
    }

    public Class<?> getType(){
        return Class;
    }

    public static SchemaType convert(String value) {
        for (SchemaType inst : values()) {
            if (inst.name().equals(value)) {
                return inst;
            }
        }
        return null;
    }
}
