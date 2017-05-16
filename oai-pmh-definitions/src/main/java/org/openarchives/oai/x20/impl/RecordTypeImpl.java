/*
 * XML Type:  recordType
 * Namespace: http://www.openarchives.org/OAI/2.0/
 * Java type: org.openarchives.oai.x20.RecordType
 *
 * Automatically generated - do not modify.
 */
package org.openarchives.oai.x20.impl;
/**
 * An XML recordType(@http://www.openarchives.org/OAI/2.0/).
 *
 * This is a complex type.
 */
public class RecordTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openarchives.oai.x20.RecordType
{
    private static final long serialVersionUID = 1L;
    
    public RecordTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName HEADER$0 = 
        new javax.xml.namespace.QName("http://www.openarchives.org/OAI/2.0/", "header");
    private static final javax.xml.namespace.QName METADATA$2 = 
        new javax.xml.namespace.QName("http://www.openarchives.org/OAI/2.0/", "metadata");
    private static final javax.xml.namespace.QName ABOUT$4 = 
        new javax.xml.namespace.QName("http://www.openarchives.org/OAI/2.0/", "about");
    
    
    /**
     * Gets the "header" element
     */
    public org.openarchives.oai.x20.HeaderType getHeader()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.HeaderType target = null;
            target = (org.openarchives.oai.x20.HeaderType)get_store().find_element_user(HEADER$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "header" element
     */
    public void setHeader(org.openarchives.oai.x20.HeaderType header)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.HeaderType target = null;
            target = (org.openarchives.oai.x20.HeaderType)get_store().find_element_user(HEADER$0, 0);
            if (target == null)
            {
                target = (org.openarchives.oai.x20.HeaderType)get_store().add_element_user(HEADER$0);
            }
            target.set(header);
        }
    }
    
    /**
     * Appends and returns a new empty "header" element
     */
    public org.openarchives.oai.x20.HeaderType addNewHeader()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.HeaderType target = null;
            target = (org.openarchives.oai.x20.HeaderType)get_store().add_element_user(HEADER$0);
            return target;
        }
    }
    
    /**
     * Gets the "metadata" element
     */
    public org.openarchives.oai.x20.MetadataType getMetadata()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.MetadataType target = null;
            target = (org.openarchives.oai.x20.MetadataType)get_store().find_element_user(METADATA$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "metadata" element
     */
    public boolean isSetMetadata()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(METADATA$2) != 0;
        }
    }
    
    /**
     * Sets the "metadata" element
     */
    public void setMetadata(org.openarchives.oai.x20.MetadataType metadata)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.MetadataType target = null;
            target = (org.openarchives.oai.x20.MetadataType)get_store().find_element_user(METADATA$2, 0);
            if (target == null)
            {
                target = (org.openarchives.oai.x20.MetadataType)get_store().add_element_user(METADATA$2);
            }
            target.set(metadata);
        }
    }
    
    /**
     * Appends and returns a new empty "metadata" element
     */
    public org.openarchives.oai.x20.MetadataType addNewMetadata()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.MetadataType target = null;
            target = (org.openarchives.oai.x20.MetadataType)get_store().add_element_user(METADATA$2);
            return target;
        }
    }
    
    /**
     * Unsets the "metadata" element
     */
    public void unsetMetadata()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(METADATA$2, 0);
        }
    }
    
    /**
     * Gets array of all "about" elements
     */
    public org.openarchives.oai.x20.AboutType[] getAboutArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(ABOUT$4, targetList);
            org.openarchives.oai.x20.AboutType[] result = new org.openarchives.oai.x20.AboutType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "about" element
     */
    public org.openarchives.oai.x20.AboutType getAboutArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.AboutType target = null;
            target = (org.openarchives.oai.x20.AboutType)get_store().find_element_user(ABOUT$4, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "about" element
     */
    public int sizeOfAboutArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(ABOUT$4);
        }
    }
    
    /**
     * Sets array of all "about" element
     */
    public void setAboutArray(org.openarchives.oai.x20.AboutType[] aboutArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(aboutArray, ABOUT$4);
        }
    }
    
    /**
     * Sets ith "about" element
     */
    public void setAboutArray(int i, org.openarchives.oai.x20.AboutType about)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.AboutType target = null;
            target = (org.openarchives.oai.x20.AboutType)get_store().find_element_user(ABOUT$4, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(about);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "about" element
     */
    public org.openarchives.oai.x20.AboutType insertNewAbout(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.AboutType target = null;
            target = (org.openarchives.oai.x20.AboutType)get_store().insert_element_user(ABOUT$4, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "about" element
     */
    public org.openarchives.oai.x20.AboutType addNewAbout()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.AboutType target = null;
            target = (org.openarchives.oai.x20.AboutType)get_store().add_element_user(ABOUT$4);
            return target;
        }
    }
    
    /**
     * Removes the ith "about" element
     */
    public void removeAbout(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(ABOUT$4, i);
        }
    }
}
