/*
 * XML Type:  resumptionTokenType
 * Namespace: http://www.openarchives.org/OAI/2.0/
 * Java type: org.openarchives.oai.x20.ResumptionTokenType
 *
 * Automatically generated - do not modify.
 */
package org.openarchives.oai.x20.impl;
/**
 * An XML resumptionTokenType(@http://www.openarchives.org/OAI/2.0/).
 *
 * This is an atomic type that is a restriction of org.openarchives.oai.x20.ResumptionTokenType.
 */
public class ResumptionTokenTypeImpl extends org.apache.xmlbeans.impl.values.JavaStringHolderEx implements org.openarchives.oai.x20.ResumptionTokenType
{
    private static final long serialVersionUID = 1L;
    
    public ResumptionTokenTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType, true);
    }
    
    protected ResumptionTokenTypeImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
    {
        super(sType, b);
    }
    
    private static final javax.xml.namespace.QName EXPIRATIONDATE$0 = 
        new javax.xml.namespace.QName("", "expirationDate");
    private static final javax.xml.namespace.QName COMPLETELISTSIZE$2 = 
        new javax.xml.namespace.QName("", "completeListSize");
    private static final javax.xml.namespace.QName CURSOR$4 = 
        new javax.xml.namespace.QName("", "cursor");
    
    
    /**
     * Gets the "expirationDate" attribute
     */
    public java.util.Calendar getExpirationDate()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(EXPIRATIONDATE$0);
            if (target == null)
            {
                return null;
            }
            return target.getCalendarValue();
        }
    }
    
    /**
     * Gets (as xml) the "expirationDate" attribute
     */
    public org.apache.xmlbeans.XmlDateTime xgetExpirationDate()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDateTime target = null;
            target = (org.apache.xmlbeans.XmlDateTime)get_store().find_attribute_user(EXPIRATIONDATE$0);
            return target;
        }
    }
    
    /**
     * True if has "expirationDate" attribute
     */
    public boolean isSetExpirationDate()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(EXPIRATIONDATE$0) != null;
        }
    }
    
    /**
     * Sets the "expirationDate" attribute
     */
    public void setExpirationDate(java.util.Calendar expirationDate)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(EXPIRATIONDATE$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(EXPIRATIONDATE$0);
            }
            target.setCalendarValue(expirationDate);
        }
    }
    
    /**
     * Sets (as xml) the "expirationDate" attribute
     */
    public void xsetExpirationDate(org.apache.xmlbeans.XmlDateTime expirationDate)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDateTime target = null;
            target = (org.apache.xmlbeans.XmlDateTime)get_store().find_attribute_user(EXPIRATIONDATE$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlDateTime)get_store().add_attribute_user(EXPIRATIONDATE$0);
            }
            target.set(expirationDate);
        }
    }
    
    /**
     * Unsets the "expirationDate" attribute
     */
    public void unsetExpirationDate()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(EXPIRATIONDATE$0);
        }
    }
    
    /**
     * Gets the "completeListSize" attribute
     */
    public java.math.BigInteger getCompleteListSize()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(COMPLETELISTSIZE$2);
            if (target == null)
            {
                return null;
            }
            return target.getBigIntegerValue();
        }
    }
    
    /**
     * Gets (as xml) the "completeListSize" attribute
     */
    public org.apache.xmlbeans.XmlPositiveInteger xgetCompleteListSize()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlPositiveInteger target = null;
            target = (org.apache.xmlbeans.XmlPositiveInteger)get_store().find_attribute_user(COMPLETELISTSIZE$2);
            return target;
        }
    }
    
    /**
     * True if has "completeListSize" attribute
     */
    public boolean isSetCompleteListSize()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(COMPLETELISTSIZE$2) != null;
        }
    }
    
    /**
     * Sets the "completeListSize" attribute
     */
    public void setCompleteListSize(java.math.BigInteger completeListSize)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(COMPLETELISTSIZE$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(COMPLETELISTSIZE$2);
            }
            target.setBigIntegerValue(completeListSize);
        }
    }
    
    /**
     * Sets (as xml) the "completeListSize" attribute
     */
    public void xsetCompleteListSize(org.apache.xmlbeans.XmlPositiveInteger completeListSize)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlPositiveInteger target = null;
            target = (org.apache.xmlbeans.XmlPositiveInteger)get_store().find_attribute_user(COMPLETELISTSIZE$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlPositiveInteger)get_store().add_attribute_user(COMPLETELISTSIZE$2);
            }
            target.set(completeListSize);
        }
    }
    
    /**
     * Unsets the "completeListSize" attribute
     */
    public void unsetCompleteListSize()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(COMPLETELISTSIZE$2);
        }
    }
    
    /**
     * Gets the "cursor" attribute
     */
    public java.math.BigInteger getCursor()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(CURSOR$4);
            if (target == null)
            {
                return null;
            }
            return target.getBigIntegerValue();
        }
    }
    
    /**
     * Gets (as xml) the "cursor" attribute
     */
    public org.apache.xmlbeans.XmlNonNegativeInteger xgetCursor()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlNonNegativeInteger target = null;
            target = (org.apache.xmlbeans.XmlNonNegativeInteger)get_store().find_attribute_user(CURSOR$4);
            return target;
        }
    }
    
    /**
     * True if has "cursor" attribute
     */
    public boolean isSetCursor()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(CURSOR$4) != null;
        }
    }
    
    /**
     * Sets the "cursor" attribute
     */
    public void setCursor(java.math.BigInteger cursor)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(CURSOR$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(CURSOR$4);
            }
            target.setBigIntegerValue(cursor);
        }
    }
    
    /**
     * Sets (as xml) the "cursor" attribute
     */
    public void xsetCursor(org.apache.xmlbeans.XmlNonNegativeInteger cursor)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlNonNegativeInteger target = null;
            target = (org.apache.xmlbeans.XmlNonNegativeInteger)get_store().find_attribute_user(CURSOR$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlNonNegativeInteger)get_store().add_attribute_user(CURSOR$4);
            }
            target.set(cursor);
        }
    }
    
    /**
     * Unsets the "cursor" attribute
     */
    public void unsetCursor()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(CURSOR$4);
        }
    }
}
