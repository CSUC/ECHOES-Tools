/*
 * XML Type:  OAI-PMHtype
 * Namespace: http://www.openarchives.org/OAI/2.0/
 * Java type: org.openarchives.oai.x20.OAIPMHtype
 *
 * Automatically generated - do not modify.
 */
package org.openarchives.oai.x20.impl;
/**
 * An XML OAI-PMHtype(@http://www.openarchives.org/OAI/2.0/).
 *
 * This is a complex type.
 */
public class OAIPMHtypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openarchives.oai.x20.OAIPMHtype
{
    private static final long serialVersionUID = 1L;
    
    public OAIPMHtypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName RESPONSEDATE$0 = 
        new javax.xml.namespace.QName("http://www.openarchives.org/OAI/2.0/", "responseDate");
    private static final javax.xml.namespace.QName REQUEST$2 = 
        new javax.xml.namespace.QName("http://www.openarchives.org/OAI/2.0/", "request");
    private static final javax.xml.namespace.QName ERROR$4 = 
        new javax.xml.namespace.QName("http://www.openarchives.org/OAI/2.0/", "error");
    private static final javax.xml.namespace.QName IDENTIFY$6 = 
        new javax.xml.namespace.QName("http://www.openarchives.org/OAI/2.0/", "Identify");
    private static final javax.xml.namespace.QName LISTMETADATAFORMATS$8 = 
        new javax.xml.namespace.QName("http://www.openarchives.org/OAI/2.0/", "ListMetadataFormats");
    private static final javax.xml.namespace.QName LISTSETS$10 = 
        new javax.xml.namespace.QName("http://www.openarchives.org/OAI/2.0/", "ListSets");
    private static final javax.xml.namespace.QName GETRECORD$12 = 
        new javax.xml.namespace.QName("http://www.openarchives.org/OAI/2.0/", "GetRecord");
    private static final javax.xml.namespace.QName LISTIDENTIFIERS$14 = 
        new javax.xml.namespace.QName("http://www.openarchives.org/OAI/2.0/", "ListIdentifiers");
    private static final javax.xml.namespace.QName LISTRECORDS$16 = 
        new javax.xml.namespace.QName("http://www.openarchives.org/OAI/2.0/", "ListRecords");
    
    
    /**
     * Gets the "responseDate" element
     */
    public java.util.Calendar getResponseDate()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(RESPONSEDATE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getCalendarValue();
        }
    }
    
    /**
     * Gets (as xml) the "responseDate" element
     */
    public org.apache.xmlbeans.XmlDateTime xgetResponseDate()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDateTime target = null;
            target = (org.apache.xmlbeans.XmlDateTime)get_store().find_element_user(RESPONSEDATE$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "responseDate" element
     */
    public void setResponseDate(java.util.Calendar responseDate)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(RESPONSEDATE$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(RESPONSEDATE$0);
            }
            target.setCalendarValue(responseDate);
        }
    }
    
    /**
     * Sets (as xml) the "responseDate" element
     */
    public void xsetResponseDate(org.apache.xmlbeans.XmlDateTime responseDate)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDateTime target = null;
            target = (org.apache.xmlbeans.XmlDateTime)get_store().find_element_user(RESPONSEDATE$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlDateTime)get_store().add_element_user(RESPONSEDATE$0);
            }
            target.set(responseDate);
        }
    }
    
    /**
     * Gets the "request" element
     */
    public org.openarchives.oai.x20.RequestType getRequest()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.RequestType target = null;
            target = (org.openarchives.oai.x20.RequestType)get_store().find_element_user(REQUEST$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "request" element
     */
    public void setRequest(org.openarchives.oai.x20.RequestType request)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.RequestType target = null;
            target = (org.openarchives.oai.x20.RequestType)get_store().find_element_user(REQUEST$2, 0);
            if (target == null)
            {
                target = (org.openarchives.oai.x20.RequestType)get_store().add_element_user(REQUEST$2);
            }
            target.set(request);
        }
    }
    
    /**
     * Appends and returns a new empty "request" element
     */
    public org.openarchives.oai.x20.RequestType addNewRequest()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.RequestType target = null;
            target = (org.openarchives.oai.x20.RequestType)get_store().add_element_user(REQUEST$2);
            return target;
        }
    }
    
    /**
     * Gets array of all "error" elements
     */
    public org.openarchives.oai.x20.OAIPMHerrorType[] getErrorArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(ERROR$4, targetList);
            org.openarchives.oai.x20.OAIPMHerrorType[] result = new org.openarchives.oai.x20.OAIPMHerrorType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "error" element
     */
    public org.openarchives.oai.x20.OAIPMHerrorType getErrorArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.OAIPMHerrorType target = null;
            target = (org.openarchives.oai.x20.OAIPMHerrorType)get_store().find_element_user(ERROR$4, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "error" element
     */
    public int sizeOfErrorArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(ERROR$4);
        }
    }
    
    /**
     * Sets array of all "error" element
     */
    public void setErrorArray(org.openarchives.oai.x20.OAIPMHerrorType[] errorArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(errorArray, ERROR$4);
        }
    }
    
    /**
     * Sets ith "error" element
     */
    public void setErrorArray(int i, org.openarchives.oai.x20.OAIPMHerrorType error)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.OAIPMHerrorType target = null;
            target = (org.openarchives.oai.x20.OAIPMHerrorType)get_store().find_element_user(ERROR$4, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(error);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "error" element
     */
    public org.openarchives.oai.x20.OAIPMHerrorType insertNewError(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.OAIPMHerrorType target = null;
            target = (org.openarchives.oai.x20.OAIPMHerrorType)get_store().insert_element_user(ERROR$4, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "error" element
     */
    public org.openarchives.oai.x20.OAIPMHerrorType addNewError()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.OAIPMHerrorType target = null;
            target = (org.openarchives.oai.x20.OAIPMHerrorType)get_store().add_element_user(ERROR$4);
            return target;
        }
    }
    
    /**
     * Removes the ith "error" element
     */
    public void removeError(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(ERROR$4, i);
        }
    }
    
    /**
     * Gets the "Identify" element
     */
    public org.openarchives.oai.x20.IdentifyType getIdentify()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.IdentifyType target = null;
            target = (org.openarchives.oai.x20.IdentifyType)get_store().find_element_user(IDENTIFY$6, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "Identify" element
     */
    public boolean isSetIdentify()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(IDENTIFY$6) != 0;
        }
    }
    
    /**
     * Sets the "Identify" element
     */
    public void setIdentify(org.openarchives.oai.x20.IdentifyType identify)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.IdentifyType target = null;
            target = (org.openarchives.oai.x20.IdentifyType)get_store().find_element_user(IDENTIFY$6, 0);
            if (target == null)
            {
                target = (org.openarchives.oai.x20.IdentifyType)get_store().add_element_user(IDENTIFY$6);
            }
            target.set(identify);
        }
    }
    
    /**
     * Appends and returns a new empty "Identify" element
     */
    public org.openarchives.oai.x20.IdentifyType addNewIdentify()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.IdentifyType target = null;
            target = (org.openarchives.oai.x20.IdentifyType)get_store().add_element_user(IDENTIFY$6);
            return target;
        }
    }
    
    /**
     * Unsets the "Identify" element
     */
    public void unsetIdentify()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(IDENTIFY$6, 0);
        }
    }
    
    /**
     * Gets the "ListMetadataFormats" element
     */
    public org.openarchives.oai.x20.ListMetadataFormatsType getListMetadataFormats()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.ListMetadataFormatsType target = null;
            target = (org.openarchives.oai.x20.ListMetadataFormatsType)get_store().find_element_user(LISTMETADATAFORMATS$8, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "ListMetadataFormats" element
     */
    public boolean isSetListMetadataFormats()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(LISTMETADATAFORMATS$8) != 0;
        }
    }
    
    /**
     * Sets the "ListMetadataFormats" element
     */
    public void setListMetadataFormats(org.openarchives.oai.x20.ListMetadataFormatsType listMetadataFormats)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.ListMetadataFormatsType target = null;
            target = (org.openarchives.oai.x20.ListMetadataFormatsType)get_store().find_element_user(LISTMETADATAFORMATS$8, 0);
            if (target == null)
            {
                target = (org.openarchives.oai.x20.ListMetadataFormatsType)get_store().add_element_user(LISTMETADATAFORMATS$8);
            }
            target.set(listMetadataFormats);
        }
    }
    
    /**
     * Appends and returns a new empty "ListMetadataFormats" element
     */
    public org.openarchives.oai.x20.ListMetadataFormatsType addNewListMetadataFormats()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.ListMetadataFormatsType target = null;
            target = (org.openarchives.oai.x20.ListMetadataFormatsType)get_store().add_element_user(LISTMETADATAFORMATS$8);
            return target;
        }
    }
    
    /**
     * Unsets the "ListMetadataFormats" element
     */
    public void unsetListMetadataFormats()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(LISTMETADATAFORMATS$8, 0);
        }
    }
    
    /**
     * Gets the "ListSets" element
     */
    public org.openarchives.oai.x20.ListSetsType getListSets()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.ListSetsType target = null;
            target = (org.openarchives.oai.x20.ListSetsType)get_store().find_element_user(LISTSETS$10, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "ListSets" element
     */
    public boolean isSetListSets()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(LISTSETS$10) != 0;
        }
    }
    
    /**
     * Sets the "ListSets" element
     */
    public void setListSets(org.openarchives.oai.x20.ListSetsType listSets)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.ListSetsType target = null;
            target = (org.openarchives.oai.x20.ListSetsType)get_store().find_element_user(LISTSETS$10, 0);
            if (target == null)
            {
                target = (org.openarchives.oai.x20.ListSetsType)get_store().add_element_user(LISTSETS$10);
            }
            target.set(listSets);
        }
    }
    
    /**
     * Appends and returns a new empty "ListSets" element
     */
    public org.openarchives.oai.x20.ListSetsType addNewListSets()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.ListSetsType target = null;
            target = (org.openarchives.oai.x20.ListSetsType)get_store().add_element_user(LISTSETS$10);
            return target;
        }
    }
    
    /**
     * Unsets the "ListSets" element
     */
    public void unsetListSets()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(LISTSETS$10, 0);
        }
    }
    
    /**
     * Gets the "GetRecord" element
     */
    public org.openarchives.oai.x20.GetRecordType getGetRecord()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.GetRecordType target = null;
            target = (org.openarchives.oai.x20.GetRecordType)get_store().find_element_user(GETRECORD$12, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "GetRecord" element
     */
    public boolean isSetGetRecord()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(GETRECORD$12) != 0;
        }
    }
    
    /**
     * Sets the "GetRecord" element
     */
    public void setGetRecord(org.openarchives.oai.x20.GetRecordType getRecord)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.GetRecordType target = null;
            target = (org.openarchives.oai.x20.GetRecordType)get_store().find_element_user(GETRECORD$12, 0);
            if (target == null)
            {
                target = (org.openarchives.oai.x20.GetRecordType)get_store().add_element_user(GETRECORD$12);
            }
            target.set(getRecord);
        }
    }
    
    /**
     * Appends and returns a new empty "GetRecord" element
     */
    public org.openarchives.oai.x20.GetRecordType addNewGetRecord()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.GetRecordType target = null;
            target = (org.openarchives.oai.x20.GetRecordType)get_store().add_element_user(GETRECORD$12);
            return target;
        }
    }
    
    /**
     * Unsets the "GetRecord" element
     */
    public void unsetGetRecord()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(GETRECORD$12, 0);
        }
    }
    
    /**
     * Gets the "ListIdentifiers" element
     */
    public org.openarchives.oai.x20.ListIdentifiersType getListIdentifiers()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.ListIdentifiersType target = null;
            target = (org.openarchives.oai.x20.ListIdentifiersType)get_store().find_element_user(LISTIDENTIFIERS$14, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "ListIdentifiers" element
     */
    public boolean isSetListIdentifiers()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(LISTIDENTIFIERS$14) != 0;
        }
    }
    
    /**
     * Sets the "ListIdentifiers" element
     */
    public void setListIdentifiers(org.openarchives.oai.x20.ListIdentifiersType listIdentifiers)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.ListIdentifiersType target = null;
            target = (org.openarchives.oai.x20.ListIdentifiersType)get_store().find_element_user(LISTIDENTIFIERS$14, 0);
            if (target == null)
            {
                target = (org.openarchives.oai.x20.ListIdentifiersType)get_store().add_element_user(LISTIDENTIFIERS$14);
            }
            target.set(listIdentifiers);
        }
    }
    
    /**
     * Appends and returns a new empty "ListIdentifiers" element
     */
    public org.openarchives.oai.x20.ListIdentifiersType addNewListIdentifiers()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.ListIdentifiersType target = null;
            target = (org.openarchives.oai.x20.ListIdentifiersType)get_store().add_element_user(LISTIDENTIFIERS$14);
            return target;
        }
    }
    
    /**
     * Unsets the "ListIdentifiers" element
     */
    public void unsetListIdentifiers()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(LISTIDENTIFIERS$14, 0);
        }
    }
    
    /**
     * Gets the "ListRecords" element
     */
    public org.openarchives.oai.x20.ListRecordsType getListRecords()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.ListRecordsType target = null;
            target = (org.openarchives.oai.x20.ListRecordsType)get_store().find_element_user(LISTRECORDS$16, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "ListRecords" element
     */
    public boolean isSetListRecords()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(LISTRECORDS$16) != 0;
        }
    }
    
    /**
     * Sets the "ListRecords" element
     */
    public void setListRecords(org.openarchives.oai.x20.ListRecordsType listRecords)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.ListRecordsType target = null;
            target = (org.openarchives.oai.x20.ListRecordsType)get_store().find_element_user(LISTRECORDS$16, 0);
            if (target == null)
            {
                target = (org.openarchives.oai.x20.ListRecordsType)get_store().add_element_user(LISTRECORDS$16);
            }
            target.set(listRecords);
        }
    }
    
    /**
     * Appends and returns a new empty "ListRecords" element
     */
    public org.openarchives.oai.x20.ListRecordsType addNewListRecords()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.ListRecordsType target = null;
            target = (org.openarchives.oai.x20.ListRecordsType)get_store().add_element_user(LISTRECORDS$16);
            return target;
        }
    }
    
    /**
     * Unsets the "ListRecords" element
     */
    public void unsetListRecords()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(LISTRECORDS$16, 0);
        }
    }
}
