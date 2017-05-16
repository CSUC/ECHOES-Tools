/*
 * XML Type:  ListIdentifiersType
 * Namespace: http://www.openarchives.org/OAI/2.0/
 * Java type: org.openarchives.oai.x20.ListIdentifiersType
 *
 * Automatically generated - do not modify.
 */
package org.openarchives.oai.x20.impl;
/**
 * An XML ListIdentifiersType(@http://www.openarchives.org/OAI/2.0/).
 *
 * This is a complex type.
 */
public class ListIdentifiersTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openarchives.oai.x20.ListIdentifiersType
{
    private static final long serialVersionUID = 1L;
    
    public ListIdentifiersTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName HEADER$0 = 
        new javax.xml.namespace.QName("http://www.openarchives.org/OAI/2.0/", "header");
    private static final javax.xml.namespace.QName RESUMPTIONTOKEN$2 = 
        new javax.xml.namespace.QName("http://www.openarchives.org/OAI/2.0/", "resumptionToken");
    
    
    /**
     * Gets array of all "header" elements
     */
    public org.openarchives.oai.x20.HeaderType[] getHeaderArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(HEADER$0, targetList);
            org.openarchives.oai.x20.HeaderType[] result = new org.openarchives.oai.x20.HeaderType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "header" element
     */
    public org.openarchives.oai.x20.HeaderType getHeaderArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.HeaderType target = null;
            target = (org.openarchives.oai.x20.HeaderType)get_store().find_element_user(HEADER$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "header" element
     */
    public int sizeOfHeaderArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(HEADER$0);
        }
    }
    
    /**
     * Sets array of all "header" element
     */
    public void setHeaderArray(org.openarchives.oai.x20.HeaderType[] headerArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(headerArray, HEADER$0);
        }
    }
    
    /**
     * Sets ith "header" element
     */
    public void setHeaderArray(int i, org.openarchives.oai.x20.HeaderType header)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.HeaderType target = null;
            target = (org.openarchives.oai.x20.HeaderType)get_store().find_element_user(HEADER$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(header);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "header" element
     */
    public org.openarchives.oai.x20.HeaderType insertNewHeader(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.HeaderType target = null;
            target = (org.openarchives.oai.x20.HeaderType)get_store().insert_element_user(HEADER$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "header" element
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
     * Removes the ith "header" element
     */
    public void removeHeader(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(HEADER$0, i);
        }
    }
    
    /**
     * Gets the "resumptionToken" element
     */
    public org.openarchives.oai.x20.ResumptionTokenType getResumptionToken()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.ResumptionTokenType target = null;
            target = (org.openarchives.oai.x20.ResumptionTokenType)get_store().find_element_user(RESUMPTIONTOKEN$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "resumptionToken" element
     */
    public boolean isSetResumptionToken()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(RESUMPTIONTOKEN$2) != 0;
        }
    }
    
    /**
     * Sets the "resumptionToken" element
     */
    public void setResumptionToken(org.openarchives.oai.x20.ResumptionTokenType resumptionToken)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.ResumptionTokenType target = null;
            target = (org.openarchives.oai.x20.ResumptionTokenType)get_store().find_element_user(RESUMPTIONTOKEN$2, 0);
            if (target == null)
            {
                target = (org.openarchives.oai.x20.ResumptionTokenType)get_store().add_element_user(RESUMPTIONTOKEN$2);
            }
            target.set(resumptionToken);
        }
    }
    
    /**
     * Appends and returns a new empty "resumptionToken" element
     */
    public org.openarchives.oai.x20.ResumptionTokenType addNewResumptionToken()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.ResumptionTokenType target = null;
            target = (org.openarchives.oai.x20.ResumptionTokenType)get_store().add_element_user(RESUMPTIONTOKEN$2);
            return target;
        }
    }
    
    /**
     * Unsets the "resumptionToken" element
     */
    public void unsetResumptionToken()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(RESUMPTIONTOKEN$2, 0);
        }
    }
}
