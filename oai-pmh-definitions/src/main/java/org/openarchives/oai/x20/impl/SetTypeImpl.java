/*
 * XML Type:  setType
 * Namespace: http://www.openarchives.org/OAI/2.0/
 * Java type: org.openarchives.oai.x20.SetType
 *
 * Automatically generated - do not modify.
 */
package org.openarchives.oai.x20.impl;
/**
 * An XML setType(@http://www.openarchives.org/OAI/2.0/).
 *
 * This is a complex type.
 */
public class SetTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openarchives.oai.x20.SetType
{
    private static final long serialVersionUID = 1L;
    
    public SetTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SETSPEC$0 = 
        new javax.xml.namespace.QName("http://www.openarchives.org/OAI/2.0/", "setSpec");
    private static final javax.xml.namespace.QName SETNAME$2 = 
        new javax.xml.namespace.QName("http://www.openarchives.org/OAI/2.0/", "setName");
    private static final javax.xml.namespace.QName SETDESCRIPTION$4 = 
        new javax.xml.namespace.QName("http://www.openarchives.org/OAI/2.0/", "setDescription");
    
    
    /**
     * Gets the "setSpec" element
     */
    public java.lang.String getSetSpec()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SETSPEC$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "setSpec" element
     */
    public org.openarchives.oai.x20.SetSpecType xgetSetSpec()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.SetSpecType target = null;
            target = (org.openarchives.oai.x20.SetSpecType)get_store().find_element_user(SETSPEC$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "setSpec" element
     */
    public void setSetSpec(java.lang.String setSpec)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SETSPEC$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(SETSPEC$0);
            }
            target.setStringValue(setSpec);
        }
    }
    
    /**
     * Sets (as xml) the "setSpec" element
     */
    public void xsetSetSpec(org.openarchives.oai.x20.SetSpecType setSpec)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.SetSpecType target = null;
            target = (org.openarchives.oai.x20.SetSpecType)get_store().find_element_user(SETSPEC$0, 0);
            if (target == null)
            {
                target = (org.openarchives.oai.x20.SetSpecType)get_store().add_element_user(SETSPEC$0);
            }
            target.set(setSpec);
        }
    }
    
    /**
     * Gets the "setName" element
     */
    public java.lang.String getSetName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SETNAME$2, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "setName" element
     */
    public org.apache.xmlbeans.XmlString xgetSetName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SETNAME$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "setName" element
     */
    public void setSetName(java.lang.String setName)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SETNAME$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(SETNAME$2);
            }
            target.setStringValue(setName);
        }
    }
    
    /**
     * Sets (as xml) the "setName" element
     */
    public void xsetSetName(org.apache.xmlbeans.XmlString setName)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SETNAME$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(SETNAME$2);
            }
            target.set(setName);
        }
    }
    
    /**
     * Gets array of all "setDescription" elements
     */
    public org.openarchives.oai.x20.DescriptionType[] getSetDescriptionArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(SETDESCRIPTION$4, targetList);
            org.openarchives.oai.x20.DescriptionType[] result = new org.openarchives.oai.x20.DescriptionType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "setDescription" element
     */
    public org.openarchives.oai.x20.DescriptionType getSetDescriptionArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.DescriptionType target = null;
            target = (org.openarchives.oai.x20.DescriptionType)get_store().find_element_user(SETDESCRIPTION$4, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "setDescription" element
     */
    public int sizeOfSetDescriptionArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(SETDESCRIPTION$4);
        }
    }
    
    /**
     * Sets array of all "setDescription" element
     */
    public void setSetDescriptionArray(org.openarchives.oai.x20.DescriptionType[] setDescriptionArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(setDescriptionArray, SETDESCRIPTION$4);
        }
    }
    
    /**
     * Sets ith "setDescription" element
     */
    public void setSetDescriptionArray(int i, org.openarchives.oai.x20.DescriptionType setDescription)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.DescriptionType target = null;
            target = (org.openarchives.oai.x20.DescriptionType)get_store().find_element_user(SETDESCRIPTION$4, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(setDescription);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "setDescription" element
     */
    public org.openarchives.oai.x20.DescriptionType insertNewSetDescription(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.DescriptionType target = null;
            target = (org.openarchives.oai.x20.DescriptionType)get_store().insert_element_user(SETDESCRIPTION$4, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "setDescription" element
     */
    public org.openarchives.oai.x20.DescriptionType addNewSetDescription()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.DescriptionType target = null;
            target = (org.openarchives.oai.x20.DescriptionType)get_store().add_element_user(SETDESCRIPTION$4);
            return target;
        }
    }
    
    /**
     * Removes the ith "setDescription" element
     */
    public void removeSetDescription(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(SETDESCRIPTION$4, i);
        }
    }
}
