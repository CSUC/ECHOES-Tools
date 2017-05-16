/*
 * XML Type:  metadataFormatType
 * Namespace: http://www.openarchives.org/OAI/2.0/
 * Java type: org.openarchives.oai.x20.MetadataFormatType
 *
 * Automatically generated - do not modify.
 */
package org.openarchives.oai.x20.impl;
/**
 * An XML metadataFormatType(@http://www.openarchives.org/OAI/2.0/).
 *
 * This is a complex type.
 */
public class MetadataFormatTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openarchives.oai.x20.MetadataFormatType
{
    private static final long serialVersionUID = 1L;
    
    public MetadataFormatTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName METADATAPREFIX$0 = 
        new javax.xml.namespace.QName("http://www.openarchives.org/OAI/2.0/", "metadataPrefix");
    private static final javax.xml.namespace.QName SCHEMA$2 = 
        new javax.xml.namespace.QName("http://www.openarchives.org/OAI/2.0/", "schema");
    private static final javax.xml.namespace.QName METADATANAMESPACE$4 = 
        new javax.xml.namespace.QName("http://www.openarchives.org/OAI/2.0/", "metadataNamespace");
    
    
    /**
     * Gets the "metadataPrefix" element
     */
    public java.lang.String getMetadataPrefix()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(METADATAPREFIX$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "metadataPrefix" element
     */
    public org.openarchives.oai.x20.MetadataPrefixType xgetMetadataPrefix()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.MetadataPrefixType target = null;
            target = (org.openarchives.oai.x20.MetadataPrefixType)get_store().find_element_user(METADATAPREFIX$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "metadataPrefix" element
     */
    public void setMetadataPrefix(java.lang.String metadataPrefix)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(METADATAPREFIX$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(METADATAPREFIX$0);
            }
            target.setStringValue(metadataPrefix);
        }
    }
    
    /**
     * Sets (as xml) the "metadataPrefix" element
     */
    public void xsetMetadataPrefix(org.openarchives.oai.x20.MetadataPrefixType metadataPrefix)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.MetadataPrefixType target = null;
            target = (org.openarchives.oai.x20.MetadataPrefixType)get_store().find_element_user(METADATAPREFIX$0, 0);
            if (target == null)
            {
                target = (org.openarchives.oai.x20.MetadataPrefixType)get_store().add_element_user(METADATAPREFIX$0);
            }
            target.set(metadataPrefix);
        }
    }
    
    /**
     * Gets the "schema" element
     */
    public java.lang.String getSchema()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SCHEMA$2, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "schema" element
     */
    public org.apache.xmlbeans.XmlAnyURI xgetSchema()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnyURI target = null;
            target = (org.apache.xmlbeans.XmlAnyURI)get_store().find_element_user(SCHEMA$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "schema" element
     */
    public void setSchema(java.lang.String schema)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SCHEMA$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(SCHEMA$2);
            }
            target.setStringValue(schema);
        }
    }
    
    /**
     * Sets (as xml) the "schema" element
     */
    public void xsetSchema(org.apache.xmlbeans.XmlAnyURI schema)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnyURI target = null;
            target = (org.apache.xmlbeans.XmlAnyURI)get_store().find_element_user(SCHEMA$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlAnyURI)get_store().add_element_user(SCHEMA$2);
            }
            target.set(schema);
        }
    }
    
    /**
     * Gets the "metadataNamespace" element
     */
    public java.lang.String getMetadataNamespace()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(METADATANAMESPACE$4, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "metadataNamespace" element
     */
    public org.apache.xmlbeans.XmlAnyURI xgetMetadataNamespace()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnyURI target = null;
            target = (org.apache.xmlbeans.XmlAnyURI)get_store().find_element_user(METADATANAMESPACE$4, 0);
            return target;
        }
    }
    
    /**
     * Sets the "metadataNamespace" element
     */
    public void setMetadataNamespace(java.lang.String metadataNamespace)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(METADATANAMESPACE$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(METADATANAMESPACE$4);
            }
            target.setStringValue(metadataNamespace);
        }
    }
    
    /**
     * Sets (as xml) the "metadataNamespace" element
     */
    public void xsetMetadataNamespace(org.apache.xmlbeans.XmlAnyURI metadataNamespace)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnyURI target = null;
            target = (org.apache.xmlbeans.XmlAnyURI)get_store().find_element_user(METADATANAMESPACE$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlAnyURI)get_store().add_element_user(METADATANAMESPACE$4);
            }
            target.set(metadataNamespace);
        }
    }
}
