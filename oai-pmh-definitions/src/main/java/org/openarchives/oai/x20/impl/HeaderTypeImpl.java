/*
 * XML Type:  headerType
 * Namespace: http://www.openarchives.org/OAI/2.0/
 * Java type: org.openarchives.oai.x20.HeaderType
 *
 * Automatically generated - do not modify.
 */
package org.openarchives.oai.x20.impl;
/**
 * An XML headerType(@http://www.openarchives.org/OAI/2.0/).
 *
 * This is a complex type.
 */
public class HeaderTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openarchives.oai.x20.HeaderType
{
    private static final long serialVersionUID = 1L;
    
    public HeaderTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName IDENTIFIER$0 = 
        new javax.xml.namespace.QName("http://www.openarchives.org/OAI/2.0/", "identifier");
    private static final javax.xml.namespace.QName DATESTAMP$2 = 
        new javax.xml.namespace.QName("http://www.openarchives.org/OAI/2.0/", "datestamp");
    private static final javax.xml.namespace.QName SETSPEC$4 = 
        new javax.xml.namespace.QName("http://www.openarchives.org/OAI/2.0/", "setSpec");
    private static final javax.xml.namespace.QName STATUS$6 = 
        new javax.xml.namespace.QName("", "status");
    
    
    /**
     * Gets the "identifier" element
     */
    public java.lang.String getIdentifier()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(IDENTIFIER$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "identifier" element
     */
    public org.openarchives.oai.x20.IdentifierType xgetIdentifier()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.IdentifierType target = null;
            target = (org.openarchives.oai.x20.IdentifierType)get_store().find_element_user(IDENTIFIER$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "identifier" element
     */
    public void setIdentifier(java.lang.String identifier)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(IDENTIFIER$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(IDENTIFIER$0);
            }
            target.setStringValue(identifier);
        }
    }
    
    /**
     * Sets (as xml) the "identifier" element
     */
    public void xsetIdentifier(org.openarchives.oai.x20.IdentifierType identifier)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.IdentifierType target = null;
            target = (org.openarchives.oai.x20.IdentifierType)get_store().find_element_user(IDENTIFIER$0, 0);
            if (target == null)
            {
                target = (org.openarchives.oai.x20.IdentifierType)get_store().add_element_user(IDENTIFIER$0);
            }
            target.set(identifier);
        }
    }
    
    /**
     * Gets the "datestamp" element
     */
    public java.util.Calendar getDatestamp()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DATESTAMP$2, 0);
            if (target == null)
            {
                return null;
            }
            return target.getCalendarValue();
        }
    }
    
    /**
     * Gets (as xml) the "datestamp" element
     */
    public org.openarchives.oai.x20.UTCdatetimeType xgetDatestamp()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.UTCdatetimeType target = null;
            target = (org.openarchives.oai.x20.UTCdatetimeType)get_store().find_element_user(DATESTAMP$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "datestamp" element
     */
    public void setDatestamp(java.util.Calendar datestamp)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DATESTAMP$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DATESTAMP$2);
            }
            target.setCalendarValue(datestamp);
        }
    }
    
    /**
     * Sets (as xml) the "datestamp" element
     */
    public void xsetDatestamp(org.openarchives.oai.x20.UTCdatetimeType datestamp)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.UTCdatetimeType target = null;
            target = (org.openarchives.oai.x20.UTCdatetimeType)get_store().find_element_user(DATESTAMP$2, 0);
            if (target == null)
            {
                target = (org.openarchives.oai.x20.UTCdatetimeType)get_store().add_element_user(DATESTAMP$2);
            }
            target.set(datestamp);
        }
    }
    
    /**
     * Gets array of all "setSpec" elements
     */
    public java.lang.String[] getSetSpecArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(SETSPEC$4, targetList);
            java.lang.String[] result = new java.lang.String[targetList.size()];
            for (int i = 0, len = targetList.size() ; i < len ; i++)
                result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getStringValue();
            return result;
        }
    }
    
    /**
     * Gets ith "setSpec" element
     */
    public java.lang.String getSetSpecArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SETSPEC$4, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) array of all "setSpec" elements
     */
    public org.openarchives.oai.x20.SetSpecType[] xgetSetSpecArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(SETSPEC$4, targetList);
            org.openarchives.oai.x20.SetSpecType[] result = new org.openarchives.oai.x20.SetSpecType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets (as xml) ith "setSpec" element
     */
    public org.openarchives.oai.x20.SetSpecType xgetSetSpecArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.SetSpecType target = null;
            target = (org.openarchives.oai.x20.SetSpecType)get_store().find_element_user(SETSPEC$4, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return (org.openarchives.oai.x20.SetSpecType)target;
        }
    }
    
    /**
     * Returns number of "setSpec" element
     */
    public int sizeOfSetSpecArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(SETSPEC$4);
        }
    }
    
    /**
     * Sets array of all "setSpec" element
     */
    public void setSetSpecArray(java.lang.String[] setSpecArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(setSpecArray, SETSPEC$4);
        }
    }
    
    /**
     * Sets ith "setSpec" element
     */
    public void setSetSpecArray(int i, java.lang.String setSpec)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SETSPEC$4, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(setSpec);
        }
    }
    
    /**
     * Sets (as xml) array of all "setSpec" element
     */
    public void xsetSetSpecArray(org.openarchives.oai.x20.SetSpecType[]setSpecArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(setSpecArray, SETSPEC$4);
        }
    }
    
    /**
     * Sets (as xml) ith "setSpec" element
     */
    public void xsetSetSpecArray(int i, org.openarchives.oai.x20.SetSpecType setSpec)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.SetSpecType target = null;
            target = (org.openarchives.oai.x20.SetSpecType)get_store().find_element_user(SETSPEC$4, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(setSpec);
        }
    }
    
    /**
     * Inserts the value as the ith "setSpec" element
     */
    public void insertSetSpec(int i, java.lang.String setSpec)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = 
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(SETSPEC$4, i);
            target.setStringValue(setSpec);
        }
    }
    
    /**
     * Appends the value as the last "setSpec" element
     */
    public void addSetSpec(java.lang.String setSpec)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(SETSPEC$4);
            target.setStringValue(setSpec);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "setSpec" element
     */
    public org.openarchives.oai.x20.SetSpecType insertNewSetSpec(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.SetSpecType target = null;
            target = (org.openarchives.oai.x20.SetSpecType)get_store().insert_element_user(SETSPEC$4, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "setSpec" element
     */
    public org.openarchives.oai.x20.SetSpecType addNewSetSpec()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.SetSpecType target = null;
            target = (org.openarchives.oai.x20.SetSpecType)get_store().add_element_user(SETSPEC$4);
            return target;
        }
    }
    
    /**
     * Removes the ith "setSpec" element
     */
    public void removeSetSpec(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(SETSPEC$4, i);
        }
    }
    
    /**
     * Gets the "status" attribute
     */
    public org.openarchives.oai.x20.StatusType.Enum getStatus()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(STATUS$6);
            if (target == null)
            {
                return null;
            }
            return (org.openarchives.oai.x20.StatusType.Enum)target.getEnumValue();
        }
    }
    
    /**
     * Gets (as xml) the "status" attribute
     */
    public org.openarchives.oai.x20.StatusType xgetStatus()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.StatusType target = null;
            target = (org.openarchives.oai.x20.StatusType)get_store().find_attribute_user(STATUS$6);
            return target;
        }
    }
    
    /**
     * True if has "status" attribute
     */
    public boolean isSetStatus()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(STATUS$6) != null;
        }
    }
    
    /**
     * Sets the "status" attribute
     */
    public void setStatus(org.openarchives.oai.x20.StatusType.Enum status)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(STATUS$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(STATUS$6);
            }
            target.setEnumValue(status);
        }
    }
    
    /**
     * Sets (as xml) the "status" attribute
     */
    public void xsetStatus(org.openarchives.oai.x20.StatusType status)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.StatusType target = null;
            target = (org.openarchives.oai.x20.StatusType)get_store().find_attribute_user(STATUS$6);
            if (target == null)
            {
                target = (org.openarchives.oai.x20.StatusType)get_store().add_attribute_user(STATUS$6);
            }
            target.set(status);
        }
    }
    
    /**
     * Unsets the "status" attribute
     */
    public void unsetStatus()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(STATUS$6);
        }
    }
}
