/*
 * XML Type:  IdentifyType
 * Namespace: http://www.openarchives.org/OAI/2.0/
 * Java type: org.openarchives.oai.x20.IdentifyType
 *
 * Automatically generated - do not modify.
 */
package org.openarchives.oai.x20.impl;
/**
 * An XML IdentifyType(@http://www.openarchives.org/OAI/2.0/).
 *
 * This is a complex type.
 */
public class IdentifyTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openarchives.oai.x20.IdentifyType
{
    private static final long serialVersionUID = 1L;
    
    public IdentifyTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName REPOSITORYNAME$0 = 
        new javax.xml.namespace.QName("http://www.openarchives.org/OAI/2.0/", "repositoryName");
    private static final javax.xml.namespace.QName BASEURL$2 = 
        new javax.xml.namespace.QName("http://www.openarchives.org/OAI/2.0/", "baseURL");
    private static final javax.xml.namespace.QName PROTOCOLVERSION$4 = 
        new javax.xml.namespace.QName("http://www.openarchives.org/OAI/2.0/", "protocolVersion");
    private static final javax.xml.namespace.QName ADMINEMAIL$6 = 
        new javax.xml.namespace.QName("http://www.openarchives.org/OAI/2.0/", "adminEmail");
    private static final javax.xml.namespace.QName EARLIESTDATESTAMP$8 = 
        new javax.xml.namespace.QName("http://www.openarchives.org/OAI/2.0/", "earliestDatestamp");
    private static final javax.xml.namespace.QName DELETEDRECORD$10 = 
        new javax.xml.namespace.QName("http://www.openarchives.org/OAI/2.0/", "deletedRecord");
    private static final javax.xml.namespace.QName GRANULARITY$12 = 
        new javax.xml.namespace.QName("http://www.openarchives.org/OAI/2.0/", "granularity");
    private static final javax.xml.namespace.QName COMPRESSION$14 = 
        new javax.xml.namespace.QName("http://www.openarchives.org/OAI/2.0/", "compression");
    private static final javax.xml.namespace.QName DESCRIPTION$16 = 
        new javax.xml.namespace.QName("http://www.openarchives.org/OAI/2.0/", "description");
    
    
    /**
     * Gets the "repositoryName" element
     */
    public java.lang.String getRepositoryName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(REPOSITORYNAME$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "repositoryName" element
     */
    public org.apache.xmlbeans.XmlString xgetRepositoryName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(REPOSITORYNAME$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "repositoryName" element
     */
    public void setRepositoryName(java.lang.String repositoryName)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(REPOSITORYNAME$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(REPOSITORYNAME$0);
            }
            target.setStringValue(repositoryName);
        }
    }
    
    /**
     * Sets (as xml) the "repositoryName" element
     */
    public void xsetRepositoryName(org.apache.xmlbeans.XmlString repositoryName)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(REPOSITORYNAME$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(REPOSITORYNAME$0);
            }
            target.set(repositoryName);
        }
    }
    
    /**
     * Gets the "baseURL" element
     */
    public java.lang.String getBaseURL()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(BASEURL$2, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "baseURL" element
     */
    public org.apache.xmlbeans.XmlAnyURI xgetBaseURL()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnyURI target = null;
            target = (org.apache.xmlbeans.XmlAnyURI)get_store().find_element_user(BASEURL$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "baseURL" element
     */
    public void setBaseURL(java.lang.String baseURL)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(BASEURL$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(BASEURL$2);
            }
            target.setStringValue(baseURL);
        }
    }
    
    /**
     * Sets (as xml) the "baseURL" element
     */
    public void xsetBaseURL(org.apache.xmlbeans.XmlAnyURI baseURL)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlAnyURI target = null;
            target = (org.apache.xmlbeans.XmlAnyURI)get_store().find_element_user(BASEURL$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlAnyURI)get_store().add_element_user(BASEURL$2);
            }
            target.set(baseURL);
        }
    }
    
    /**
     * Gets the "protocolVersion" element
     */
    public org.openarchives.oai.x20.ProtocolVersionType.Enum getProtocolVersion()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROTOCOLVERSION$4, 0);
            if (target == null)
            {
                return null;
            }
            return (org.openarchives.oai.x20.ProtocolVersionType.Enum)target.getEnumValue();
        }
    }
    
    /**
     * Gets (as xml) the "protocolVersion" element
     */
    public org.openarchives.oai.x20.ProtocolVersionType xgetProtocolVersion()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.ProtocolVersionType target = null;
            target = (org.openarchives.oai.x20.ProtocolVersionType)get_store().find_element_user(PROTOCOLVERSION$4, 0);
            return target;
        }
    }
    
    /**
     * Sets the "protocolVersion" element
     */
    public void setProtocolVersion(org.openarchives.oai.x20.ProtocolVersionType.Enum protocolVersion)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROTOCOLVERSION$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROTOCOLVERSION$4);
            }
            target.setEnumValue(protocolVersion);
        }
    }
    
    /**
     * Sets (as xml) the "protocolVersion" element
     */
    public void xsetProtocolVersion(org.openarchives.oai.x20.ProtocolVersionType protocolVersion)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.ProtocolVersionType target = null;
            target = (org.openarchives.oai.x20.ProtocolVersionType)get_store().find_element_user(PROTOCOLVERSION$4, 0);
            if (target == null)
            {
                target = (org.openarchives.oai.x20.ProtocolVersionType)get_store().add_element_user(PROTOCOLVERSION$4);
            }
            target.set(protocolVersion);
        }
    }
    
    /**
     * Gets array of all "adminEmail" elements
     */
    public java.lang.String[] getAdminEmailArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(ADMINEMAIL$6, targetList);
            java.lang.String[] result = new java.lang.String[targetList.size()];
            for (int i = 0, len = targetList.size() ; i < len ; i++)
                result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getStringValue();
            return result;
        }
    }
    
    /**
     * Gets ith "adminEmail" element
     */
    public java.lang.String getAdminEmailArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ADMINEMAIL$6, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) array of all "adminEmail" elements
     */
    public org.openarchives.oai.x20.EmailType[] xgetAdminEmailArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(ADMINEMAIL$6, targetList);
            org.openarchives.oai.x20.EmailType[] result = new org.openarchives.oai.x20.EmailType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets (as xml) ith "adminEmail" element
     */
    public org.openarchives.oai.x20.EmailType xgetAdminEmailArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.EmailType target = null;
            target = (org.openarchives.oai.x20.EmailType)get_store().find_element_user(ADMINEMAIL$6, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return (org.openarchives.oai.x20.EmailType)target;
        }
    }
    
    /**
     * Returns number of "adminEmail" element
     */
    public int sizeOfAdminEmailArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(ADMINEMAIL$6);
        }
    }
    
    /**
     * Sets array of all "adminEmail" element
     */
    public void setAdminEmailArray(java.lang.String[] adminEmailArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(adminEmailArray, ADMINEMAIL$6);
        }
    }
    
    /**
     * Sets ith "adminEmail" element
     */
    public void setAdminEmailArray(int i, java.lang.String adminEmail)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ADMINEMAIL$6, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(adminEmail);
        }
    }
    
    /**
     * Sets (as xml) array of all "adminEmail" element
     */
    public void xsetAdminEmailArray(org.openarchives.oai.x20.EmailType[]adminEmailArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(adminEmailArray, ADMINEMAIL$6);
        }
    }
    
    /**
     * Sets (as xml) ith "adminEmail" element
     */
    public void xsetAdminEmailArray(int i, org.openarchives.oai.x20.EmailType adminEmail)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.EmailType target = null;
            target = (org.openarchives.oai.x20.EmailType)get_store().find_element_user(ADMINEMAIL$6, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(adminEmail);
        }
    }
    
    /**
     * Inserts the value as the ith "adminEmail" element
     */
    public void insertAdminEmail(int i, java.lang.String adminEmail)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = 
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(ADMINEMAIL$6, i);
            target.setStringValue(adminEmail);
        }
    }
    
    /**
     * Appends the value as the last "adminEmail" element
     */
    public void addAdminEmail(java.lang.String adminEmail)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(ADMINEMAIL$6);
            target.setStringValue(adminEmail);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "adminEmail" element
     */
    public org.openarchives.oai.x20.EmailType insertNewAdminEmail(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.EmailType target = null;
            target = (org.openarchives.oai.x20.EmailType)get_store().insert_element_user(ADMINEMAIL$6, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "adminEmail" element
     */
    public org.openarchives.oai.x20.EmailType addNewAdminEmail()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.EmailType target = null;
            target = (org.openarchives.oai.x20.EmailType)get_store().add_element_user(ADMINEMAIL$6);
            return target;
        }
    }
    
    /**
     * Removes the ith "adminEmail" element
     */
    public void removeAdminEmail(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(ADMINEMAIL$6, i);
        }
    }
    
    /**
     * Gets the "earliestDatestamp" element
     */
    public java.util.Calendar getEarliestDatestamp()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(EARLIESTDATESTAMP$8, 0);
            if (target == null)
            {
                return null;
            }
            return target.getCalendarValue();
        }
    }
    
    /**
     * Gets (as xml) the "earliestDatestamp" element
     */
    public org.openarchives.oai.x20.UTCdatetimeType xgetEarliestDatestamp()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.UTCdatetimeType target = null;
            target = (org.openarchives.oai.x20.UTCdatetimeType)get_store().find_element_user(EARLIESTDATESTAMP$8, 0);
            return target;
        }
    }
    
    /**
     * Sets the "earliestDatestamp" element
     */
    public void setEarliestDatestamp(java.util.Calendar earliestDatestamp)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(EARLIESTDATESTAMP$8, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(EARLIESTDATESTAMP$8);
            }
            target.setCalendarValue(earliestDatestamp);
        }
    }
    
    /**
     * Sets (as xml) the "earliestDatestamp" element
     */
    public void xsetEarliestDatestamp(org.openarchives.oai.x20.UTCdatetimeType earliestDatestamp)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.UTCdatetimeType target = null;
            target = (org.openarchives.oai.x20.UTCdatetimeType)get_store().find_element_user(EARLIESTDATESTAMP$8, 0);
            if (target == null)
            {
                target = (org.openarchives.oai.x20.UTCdatetimeType)get_store().add_element_user(EARLIESTDATESTAMP$8);
            }
            target.set(earliestDatestamp);
        }
    }
    
    /**
     * Gets the "deletedRecord" element
     */
    public org.openarchives.oai.x20.DeletedRecordType.Enum getDeletedRecord()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DELETEDRECORD$10, 0);
            if (target == null)
            {
                return null;
            }
            return (org.openarchives.oai.x20.DeletedRecordType.Enum)target.getEnumValue();
        }
    }
    
    /**
     * Gets (as xml) the "deletedRecord" element
     */
    public org.openarchives.oai.x20.DeletedRecordType xgetDeletedRecord()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.DeletedRecordType target = null;
            target = (org.openarchives.oai.x20.DeletedRecordType)get_store().find_element_user(DELETEDRECORD$10, 0);
            return target;
        }
    }
    
    /**
     * Sets the "deletedRecord" element
     */
    public void setDeletedRecord(org.openarchives.oai.x20.DeletedRecordType.Enum deletedRecord)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DELETEDRECORD$10, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DELETEDRECORD$10);
            }
            target.setEnumValue(deletedRecord);
        }
    }
    
    /**
     * Sets (as xml) the "deletedRecord" element
     */
    public void xsetDeletedRecord(org.openarchives.oai.x20.DeletedRecordType deletedRecord)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.DeletedRecordType target = null;
            target = (org.openarchives.oai.x20.DeletedRecordType)get_store().find_element_user(DELETEDRECORD$10, 0);
            if (target == null)
            {
                target = (org.openarchives.oai.x20.DeletedRecordType)get_store().add_element_user(DELETEDRECORD$10);
            }
            target.set(deletedRecord);
        }
    }
    
    /**
     * Gets the "granularity" element
     */
    public org.openarchives.oai.x20.GranularityType.Enum getGranularity()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(GRANULARITY$12, 0);
            if (target == null)
            {
                return null;
            }
            return (org.openarchives.oai.x20.GranularityType.Enum)target.getEnumValue();
        }
    }
    
    /**
     * Gets (as xml) the "granularity" element
     */
    public org.openarchives.oai.x20.GranularityType xgetGranularity()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.GranularityType target = null;
            target = (org.openarchives.oai.x20.GranularityType)get_store().find_element_user(GRANULARITY$12, 0);
            return target;
        }
    }
    
    /**
     * Sets the "granularity" element
     */
    public void setGranularity(org.openarchives.oai.x20.GranularityType.Enum granularity)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(GRANULARITY$12, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(GRANULARITY$12);
            }
            target.setEnumValue(granularity);
        }
    }
    
    /**
     * Sets (as xml) the "granularity" element
     */
    public void xsetGranularity(org.openarchives.oai.x20.GranularityType granularity)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.GranularityType target = null;
            target = (org.openarchives.oai.x20.GranularityType)get_store().find_element_user(GRANULARITY$12, 0);
            if (target == null)
            {
                target = (org.openarchives.oai.x20.GranularityType)get_store().add_element_user(GRANULARITY$12);
            }
            target.set(granularity);
        }
    }
    
    /**
     * Gets array of all "compression" elements
     */
    public java.lang.String[] getCompressionArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(COMPRESSION$14, targetList);
            java.lang.String[] result = new java.lang.String[targetList.size()];
            for (int i = 0, len = targetList.size() ; i < len ; i++)
                result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getStringValue();
            return result;
        }
    }
    
    /**
     * Gets ith "compression" element
     */
    public java.lang.String getCompressionArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(COMPRESSION$14, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) array of all "compression" elements
     */
    public org.apache.xmlbeans.XmlString[] xgetCompressionArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(COMPRESSION$14, targetList);
            org.apache.xmlbeans.XmlString[] result = new org.apache.xmlbeans.XmlString[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets (as xml) ith "compression" element
     */
    public org.apache.xmlbeans.XmlString xgetCompressionArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(COMPRESSION$14, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return (org.apache.xmlbeans.XmlString)target;
        }
    }
    
    /**
     * Returns number of "compression" element
     */
    public int sizeOfCompressionArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(COMPRESSION$14);
        }
    }
    
    /**
     * Sets array of all "compression" element
     */
    public void setCompressionArray(java.lang.String[] compressionArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(compressionArray, COMPRESSION$14);
        }
    }
    
    /**
     * Sets ith "compression" element
     */
    public void setCompressionArray(int i, java.lang.String compression)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(COMPRESSION$14, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(compression);
        }
    }
    
    /**
     * Sets (as xml) array of all "compression" element
     */
    public void xsetCompressionArray(org.apache.xmlbeans.XmlString[]compressionArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(compressionArray, COMPRESSION$14);
        }
    }
    
    /**
     * Sets (as xml) ith "compression" element
     */
    public void xsetCompressionArray(int i, org.apache.xmlbeans.XmlString compression)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(COMPRESSION$14, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(compression);
        }
    }
    
    /**
     * Inserts the value as the ith "compression" element
     */
    public void insertCompression(int i, java.lang.String compression)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = 
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(COMPRESSION$14, i);
            target.setStringValue(compression);
        }
    }
    
    /**
     * Appends the value as the last "compression" element
     */
    public void addCompression(java.lang.String compression)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(COMPRESSION$14);
            target.setStringValue(compression);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "compression" element
     */
    public org.apache.xmlbeans.XmlString insertNewCompression(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().insert_element_user(COMPRESSION$14, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "compression" element
     */
    public org.apache.xmlbeans.XmlString addNewCompression()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(COMPRESSION$14);
            return target;
        }
    }
    
    /**
     * Removes the ith "compression" element
     */
    public void removeCompression(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(COMPRESSION$14, i);
        }
    }
    
    /**
     * Gets array of all "description" elements
     */
    public org.openarchives.oai.x20.DescriptionType[] getDescriptionArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(DESCRIPTION$16, targetList);
            org.openarchives.oai.x20.DescriptionType[] result = new org.openarchives.oai.x20.DescriptionType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "description" element
     */
    public org.openarchives.oai.x20.DescriptionType getDescriptionArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.DescriptionType target = null;
            target = (org.openarchives.oai.x20.DescriptionType)get_store().find_element_user(DESCRIPTION$16, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "description" element
     */
    public int sizeOfDescriptionArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(DESCRIPTION$16);
        }
    }
    
    /**
     * Sets array of all "description" element
     */
    public void setDescriptionArray(org.openarchives.oai.x20.DescriptionType[] descriptionArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(descriptionArray, DESCRIPTION$16);
        }
    }
    
    /**
     * Sets ith "description" element
     */
    public void setDescriptionArray(int i, org.openarchives.oai.x20.DescriptionType description)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.DescriptionType target = null;
            target = (org.openarchives.oai.x20.DescriptionType)get_store().find_element_user(DESCRIPTION$16, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(description);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "description" element
     */
    public org.openarchives.oai.x20.DescriptionType insertNewDescription(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.DescriptionType target = null;
            target = (org.openarchives.oai.x20.DescriptionType)get_store().insert_element_user(DESCRIPTION$16, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "description" element
     */
    public org.openarchives.oai.x20.DescriptionType addNewDescription()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.DescriptionType target = null;
            target = (org.openarchives.oai.x20.DescriptionType)get_store().add_element_user(DESCRIPTION$16);
            return target;
        }
    }
    
    /**
     * Removes the ith "description" element
     */
    public void removeDescription(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(DESCRIPTION$16, i);
        }
    }
}
