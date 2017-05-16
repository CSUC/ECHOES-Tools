/*
 * XML Type:  ListMetadataFormatsType
 * Namespace: http://www.openarchives.org/OAI/2.0/
 * Java type: org.openarchives.oai.x20.ListMetadataFormatsType
 *
 * Automatically generated - do not modify.
 */
package org.openarchives.oai.x20.impl;
/**
 * An XML ListMetadataFormatsType(@http://www.openarchives.org/OAI/2.0/).
 *
 * This is a complex type.
 */
public class ListMetadataFormatsTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openarchives.oai.x20.ListMetadataFormatsType
{
    private static final long serialVersionUID = 1L;
    
    public ListMetadataFormatsTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName METADATAFORMAT$0 = 
        new javax.xml.namespace.QName("http://www.openarchives.org/OAI/2.0/", "metadataFormat");
    
    
    /**
     * Gets array of all "metadataFormat" elements
     */
    public org.openarchives.oai.x20.MetadataFormatType[] getMetadataFormatArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(METADATAFORMAT$0, targetList);
            org.openarchives.oai.x20.MetadataFormatType[] result = new org.openarchives.oai.x20.MetadataFormatType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "metadataFormat" element
     */
    public org.openarchives.oai.x20.MetadataFormatType getMetadataFormatArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.MetadataFormatType target = null;
            target = (org.openarchives.oai.x20.MetadataFormatType)get_store().find_element_user(METADATAFORMAT$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "metadataFormat" element
     */
    public int sizeOfMetadataFormatArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(METADATAFORMAT$0);
        }
    }
    
    /**
     * Sets array of all "metadataFormat" element
     */
    public void setMetadataFormatArray(org.openarchives.oai.x20.MetadataFormatType[] metadataFormatArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(metadataFormatArray, METADATAFORMAT$0);
        }
    }
    
    /**
     * Sets ith "metadataFormat" element
     */
    public void setMetadataFormatArray(int i, org.openarchives.oai.x20.MetadataFormatType metadataFormat)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.MetadataFormatType target = null;
            target = (org.openarchives.oai.x20.MetadataFormatType)get_store().find_element_user(METADATAFORMAT$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(metadataFormat);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "metadataFormat" element
     */
    public org.openarchives.oai.x20.MetadataFormatType insertNewMetadataFormat(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.MetadataFormatType target = null;
            target = (org.openarchives.oai.x20.MetadataFormatType)get_store().insert_element_user(METADATAFORMAT$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "metadataFormat" element
     */
    public org.openarchives.oai.x20.MetadataFormatType addNewMetadataFormat()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.MetadataFormatType target = null;
            target = (org.openarchives.oai.x20.MetadataFormatType)get_store().add_element_user(METADATAFORMAT$0);
            return target;
        }
    }
    
    /**
     * Removes the ith "metadataFormat" element
     */
    public void removeMetadataFormat(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(METADATAFORMAT$0, i);
        }
    }
}
