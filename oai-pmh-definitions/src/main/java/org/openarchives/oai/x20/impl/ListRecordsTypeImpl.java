/*
 * XML Type:  ListRecordsType
 * Namespace: http://www.openarchives.org/OAI/2.0/
 * Java type: org.openarchives.oai.x20.ListRecordsType
 *
 * Automatically generated - do not modify.
 */
package org.openarchives.oai.x20.impl;
/**
 * An XML ListRecordsType(@http://www.openarchives.org/OAI/2.0/).
 *
 * This is a complex type.
 */
public class ListRecordsTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openarchives.oai.x20.ListRecordsType
{
    private static final long serialVersionUID = 1L;
    
    public ListRecordsTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName RECORD$0 = 
        new javax.xml.namespace.QName("http://www.openarchives.org/OAI/2.0/", "record");
    private static final javax.xml.namespace.QName RESUMPTIONTOKEN$2 = 
        new javax.xml.namespace.QName("http://www.openarchives.org/OAI/2.0/", "resumptionToken");
    
    
    /**
     * Gets array of all "record" elements
     */
    public org.openarchives.oai.x20.RecordType[] getRecordArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(RECORD$0, targetList);
            org.openarchives.oai.x20.RecordType[] result = new org.openarchives.oai.x20.RecordType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "record" element
     */
    public org.openarchives.oai.x20.RecordType getRecordArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.RecordType target = null;
            target = (org.openarchives.oai.x20.RecordType)get_store().find_element_user(RECORD$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "record" element
     */
    public int sizeOfRecordArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(RECORD$0);
        }
    }
    
    /**
     * Sets array of all "record" element
     */
    public void setRecordArray(org.openarchives.oai.x20.RecordType[] recordArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(recordArray, RECORD$0);
        }
    }
    
    /**
     * Sets ith "record" element
     */
    public void setRecordArray(int i, org.openarchives.oai.x20.RecordType record)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.RecordType target = null;
            target = (org.openarchives.oai.x20.RecordType)get_store().find_element_user(RECORD$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(record);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "record" element
     */
    public org.openarchives.oai.x20.RecordType insertNewRecord(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.RecordType target = null;
            target = (org.openarchives.oai.x20.RecordType)get_store().insert_element_user(RECORD$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "record" element
     */
    public org.openarchives.oai.x20.RecordType addNewRecord()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.RecordType target = null;
            target = (org.openarchives.oai.x20.RecordType)get_store().add_element_user(RECORD$0);
            return target;
        }
    }
    
    /**
     * Removes the ith "record" element
     */
    public void removeRecord(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(RECORD$0, i);
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
