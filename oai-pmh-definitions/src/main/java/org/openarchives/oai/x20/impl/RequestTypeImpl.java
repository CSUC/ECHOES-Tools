/*
 * XML Type:  requestType
 * Namespace: http://www.openarchives.org/OAI/2.0/
 * Java type: org.openarchives.oai.x20.RequestType
 *
 * Automatically generated - do not modify.
 */
package org.openarchives.oai.x20.impl;
/**
 * An XML requestType(@http://www.openarchives.org/OAI/2.0/).
 *
 * This is an atomic type that is a restriction of org.openarchives.oai.x20.RequestType.
 */
public class RequestTypeImpl extends org.apache.xmlbeans.impl.values.JavaUriHolderEx implements org.openarchives.oai.x20.RequestType
{
    private static final long serialVersionUID = 1L;
    
    public RequestTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType, true);
    }
    
    protected RequestTypeImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
    {
        super(sType, b);
    }
    
    private static final javax.xml.namespace.QName VERB$0 = 
        new javax.xml.namespace.QName("", "verb");
    private static final javax.xml.namespace.QName IDENTIFIER$2 = 
        new javax.xml.namespace.QName("", "identifier");
    private static final javax.xml.namespace.QName METADATAPREFIX$4 = 
        new javax.xml.namespace.QName("", "metadataPrefix");
    private static final javax.xml.namespace.QName FROM$6 = 
        new javax.xml.namespace.QName("", "from");
    private static final javax.xml.namespace.QName UNTIL$8 = 
        new javax.xml.namespace.QName("", "until");
    private static final javax.xml.namespace.QName SET$10 = 
        new javax.xml.namespace.QName("", "set");
    private static final javax.xml.namespace.QName RESUMPTIONTOKEN$12 = 
        new javax.xml.namespace.QName("", "resumptionToken");
    
    
    /**
     * Gets the "verb" attribute
     */
    public org.openarchives.oai.x20.VerbType.Enum getVerb()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(VERB$0);
            if (target == null)
            {
                return null;
            }
            return (org.openarchives.oai.x20.VerbType.Enum)target.getEnumValue();
        }
    }
    
    /**
     * Gets (as xml) the "verb" attribute
     */
    public org.openarchives.oai.x20.VerbType xgetVerb()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.VerbType target = null;
            target = (org.openarchives.oai.x20.VerbType)get_store().find_attribute_user(VERB$0);
            return target;
        }
    }
    
    /**
     * True if has "verb" attribute
     */
    public boolean isSetVerb()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(VERB$0) != null;
        }
    }
    
    /**
     * Sets the "verb" attribute
     */
    public void setVerb(org.openarchives.oai.x20.VerbType.Enum verb)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(VERB$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(VERB$0);
            }
            target.setEnumValue(verb);
        }
    }
    
    /**
     * Sets (as xml) the "verb" attribute
     */
    public void xsetVerb(org.openarchives.oai.x20.VerbType verb)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.VerbType target = null;
            target = (org.openarchives.oai.x20.VerbType)get_store().find_attribute_user(VERB$0);
            if (target == null)
            {
                target = (org.openarchives.oai.x20.VerbType)get_store().add_attribute_user(VERB$0);
            }
            target.set(verb);
        }
    }
    
    /**
     * Unsets the "verb" attribute
     */
    public void unsetVerb()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(VERB$0);
        }
    }
    
    /**
     * Gets the "identifier" attribute
     */
    public java.lang.String getIdentifier()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(IDENTIFIER$2);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "identifier" attribute
     */
    public org.openarchives.oai.x20.IdentifierType xgetIdentifier()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.IdentifierType target = null;
            target = (org.openarchives.oai.x20.IdentifierType)get_store().find_attribute_user(IDENTIFIER$2);
            return target;
        }
    }
    
    /**
     * True if has "identifier" attribute
     */
    public boolean isSetIdentifier()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(IDENTIFIER$2) != null;
        }
    }
    
    /**
     * Sets the "identifier" attribute
     */
    public void setIdentifier(java.lang.String identifier)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(IDENTIFIER$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(IDENTIFIER$2);
            }
            target.setStringValue(identifier);
        }
    }
    
    /**
     * Sets (as xml) the "identifier" attribute
     */
    public void xsetIdentifier(org.openarchives.oai.x20.IdentifierType identifier)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.IdentifierType target = null;
            target = (org.openarchives.oai.x20.IdentifierType)get_store().find_attribute_user(IDENTIFIER$2);
            if (target == null)
            {
                target = (org.openarchives.oai.x20.IdentifierType)get_store().add_attribute_user(IDENTIFIER$2);
            }
            target.set(identifier);
        }
    }
    
    /**
     * Unsets the "identifier" attribute
     */
    public void unsetIdentifier()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(IDENTIFIER$2);
        }
    }
    
    /**
     * Gets the "metadataPrefix" attribute
     */
    public java.lang.String getMetadataPrefix()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(METADATAPREFIX$4);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "metadataPrefix" attribute
     */
    public org.openarchives.oai.x20.MetadataPrefixType xgetMetadataPrefix()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.MetadataPrefixType target = null;
            target = (org.openarchives.oai.x20.MetadataPrefixType)get_store().find_attribute_user(METADATAPREFIX$4);
            return target;
        }
    }
    
    /**
     * True if has "metadataPrefix" attribute
     */
    public boolean isSetMetadataPrefix()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(METADATAPREFIX$4) != null;
        }
    }
    
    /**
     * Sets the "metadataPrefix" attribute
     */
    public void setMetadataPrefix(java.lang.String metadataPrefix)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(METADATAPREFIX$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(METADATAPREFIX$4);
            }
            target.setStringValue(metadataPrefix);
        }
    }
    
    /**
     * Sets (as xml) the "metadataPrefix" attribute
     */
    public void xsetMetadataPrefix(org.openarchives.oai.x20.MetadataPrefixType metadataPrefix)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.MetadataPrefixType target = null;
            target = (org.openarchives.oai.x20.MetadataPrefixType)get_store().find_attribute_user(METADATAPREFIX$4);
            if (target == null)
            {
                target = (org.openarchives.oai.x20.MetadataPrefixType)get_store().add_attribute_user(METADATAPREFIX$4);
            }
            target.set(metadataPrefix);
        }
    }
    
    /**
     * Unsets the "metadataPrefix" attribute
     */
    public void unsetMetadataPrefix()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(METADATAPREFIX$4);
        }
    }
    
    /**
     * Gets the "from" attribute
     */
    public java.util.Calendar getFrom()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(FROM$6);
            if (target == null)
            {
                return null;
            }
            return target.getCalendarValue();
        }
    }
    
    /**
     * Gets (as xml) the "from" attribute
     */
    public org.openarchives.oai.x20.UTCdatetimeType xgetFrom()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.UTCdatetimeType target = null;
            target = (org.openarchives.oai.x20.UTCdatetimeType)get_store().find_attribute_user(FROM$6);
            return target;
        }
    }
    
    /**
     * True if has "from" attribute
     */
    public boolean isSetFrom()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(FROM$6) != null;
        }
    }
    
    /**
     * Sets the "from" attribute
     */
    public void setFrom(java.util.Calendar from)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(FROM$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(FROM$6);
            }
            target.setCalendarValue(from);
        }
    }
    
    /**
     * Sets (as xml) the "from" attribute
     */
    public void xsetFrom(org.openarchives.oai.x20.UTCdatetimeType from)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.UTCdatetimeType target = null;
            target = (org.openarchives.oai.x20.UTCdatetimeType)get_store().find_attribute_user(FROM$6);
            if (target == null)
            {
                target = (org.openarchives.oai.x20.UTCdatetimeType)get_store().add_attribute_user(FROM$6);
            }
            target.set(from);
        }
    }
    
    /**
     * Unsets the "from" attribute
     */
    public void unsetFrom()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(FROM$6);
        }
    }
    
    /**
     * Gets the "until" attribute
     */
    public java.util.Calendar getUntil()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(UNTIL$8);
            if (target == null)
            {
                return null;
            }
            return target.getCalendarValue();
        }
    }
    
    /**
     * Gets (as xml) the "until" attribute
     */
    public org.openarchives.oai.x20.UTCdatetimeType xgetUntil()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.UTCdatetimeType target = null;
            target = (org.openarchives.oai.x20.UTCdatetimeType)get_store().find_attribute_user(UNTIL$8);
            return target;
        }
    }
    
    /**
     * True if has "until" attribute
     */
    public boolean isSetUntil()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(UNTIL$8) != null;
        }
    }
    
    /**
     * Sets the "until" attribute
     */
    public void setUntil(java.util.Calendar until)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(UNTIL$8);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(UNTIL$8);
            }
            target.setCalendarValue(until);
        }
    }
    
    /**
     * Sets (as xml) the "until" attribute
     */
    public void xsetUntil(org.openarchives.oai.x20.UTCdatetimeType until)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.UTCdatetimeType target = null;
            target = (org.openarchives.oai.x20.UTCdatetimeType)get_store().find_attribute_user(UNTIL$8);
            if (target == null)
            {
                target = (org.openarchives.oai.x20.UTCdatetimeType)get_store().add_attribute_user(UNTIL$8);
            }
            target.set(until);
        }
    }
    
    /**
     * Unsets the "until" attribute
     */
    public void unsetUntil()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(UNTIL$8);
        }
    }
    
    /**
     * Gets the "set" attribute
     */
    public java.lang.String getSet()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(SET$10);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "set" attribute
     */
    public org.openarchives.oai.x20.SetSpecType xgetSet()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.SetSpecType target = null;
            target = (org.openarchives.oai.x20.SetSpecType)get_store().find_attribute_user(SET$10);
            return target;
        }
    }
    
    /**
     * True if has "set" attribute
     */
    public boolean isSetSet()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(SET$10) != null;
        }
    }
    
    /**
     * Sets the "set" attribute
     */
    public void setSet(java.lang.String set)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(SET$10);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(SET$10);
            }
            target.setStringValue(set);
        }
    }
    
    /**
     * Sets (as xml) the "set" attribute
     */
    public void xsetSet(org.openarchives.oai.x20.SetSpecType set)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openarchives.oai.x20.SetSpecType target = null;
            target = (org.openarchives.oai.x20.SetSpecType)get_store().find_attribute_user(SET$10);
            if (target == null)
            {
                target = (org.openarchives.oai.x20.SetSpecType)get_store().add_attribute_user(SET$10);
            }
            target.set(set);
        }
    }
    
    /**
     * Unsets the "set" attribute
     */
    public void unsetSet()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(SET$10);
        }
    }
    
    /**
     * Gets the "resumptionToken" attribute
     */
    public java.lang.String getResumptionToken()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(RESUMPTIONTOKEN$12);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "resumptionToken" attribute
     */
    public org.apache.xmlbeans.XmlString xgetResumptionToken()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(RESUMPTIONTOKEN$12);
            return target;
        }
    }
    
    /**
     * True if has "resumptionToken" attribute
     */
    public boolean isSetResumptionToken()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(RESUMPTIONTOKEN$12) != null;
        }
    }
    
    /**
     * Sets the "resumptionToken" attribute
     */
    public void setResumptionToken(java.lang.String resumptionToken)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(RESUMPTIONTOKEN$12);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(RESUMPTIONTOKEN$12);
            }
            target.setStringValue(resumptionToken);
        }
    }
    
    /**
     * Sets (as xml) the "resumptionToken" attribute
     */
    public void xsetResumptionToken(org.apache.xmlbeans.XmlString resumptionToken)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(RESUMPTIONTOKEN$12);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(RESUMPTIONTOKEN$12);
            }
            target.set(resumptionToken);
        }
    }
    
    /**
     * Unsets the "resumptionToken" attribute
     */
    public void unsetResumptionToken()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(RESUMPTIONTOKEN$12);
        }
    }
}
