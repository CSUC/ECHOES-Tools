/*
 * XML Type:  OAI-PMHerrorType
 * Namespace: http://www.openarchives.org/OAI/2.0/
 * Java type: org.openarchives.oai.x20.OAIPMHerrorType
 *
 * Automatically generated - do not modify.
 */
package org.openarchives.oai.x20.impl;
/**
 * An XML OAI-PMHerrorType(@http://www.openarchives.org/OAI/2.0/).
 *
 * This is an atomic type that is a restriction of org.openarchives.oai.x20.OAIPMHerrorType.
 */
public class OAIPMHerrorTypeImpl extends org.apache.xmlbeans.impl.values.JavaStringHolderEx implements org.openarchives.oai.x20.OAIPMHerrorType
{
    private static final long serialVersionUID = 1L;
    
    public OAIPMHerrorTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType, true);
    }
    
    protected OAIPMHerrorTypeImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
    {
        super(sType, b);
    }
    
    private static final javax.xml.namespace.QName CODE$0 = 
        new javax.xml.namespace.QName("", "code");
    
    
    /**
     * Gets the "code" attribute
     */
    public org.openarchives.oai.x20.OAIPMHerrorcodeType.Enum getCode()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(CODE$0);
            if (target == null)
            {
                return null;
            }
            return (org.openarchives.oai.x20.OAIPMHerrorcodeType.Enum)target.getEnumValue();
        }
    }
    
    /**
     * Gets (as xml) the "code" attribute
     */
    public org.openarchives.oai.x20.OAIPMHerrorcodeType xgetCode()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.OAIPMHerrorcodeType target = null;
            target = (org.openarchives.oai.x20.OAIPMHerrorcodeType)get_store().find_attribute_user(CODE$0);
            return target;
        }
    }
    
    /**
     * Sets the "code" attribute
     */
    public void setCode(org.openarchives.oai.x20.OAIPMHerrorcodeType.Enum code)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(CODE$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(CODE$0);
            }
            target.setEnumValue(code);
        }
    }
    
    /**
     * Sets (as xml) the "code" attribute
     */
    public void xsetCode(org.openarchives.oai.x20.OAIPMHerrorcodeType code)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.OAIPMHerrorcodeType target = null;
            target = (org.openarchives.oai.x20.OAIPMHerrorcodeType)get_store().find_attribute_user(CODE$0);
            if (target == null)
            {
                target = (org.openarchives.oai.x20.OAIPMHerrorcodeType)get_store().add_attribute_user(CODE$0);
            }
            target.set(code);
        }
    }
}
