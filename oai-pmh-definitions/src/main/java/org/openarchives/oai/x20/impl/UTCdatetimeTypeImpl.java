/*
 * XML Type:  UTCdatetimeType
 * Namespace: http://www.openarchives.org/OAI/2.0/
 * Java type: org.openarchives.oai.x20.UTCdatetimeType
 *
 * Automatically generated - do not modify.
 */
package org.openarchives.oai.x20.impl;
/**
 * An XML UTCdatetimeType(@http://www.openarchives.org/OAI/2.0/).
 *
 * This is a union type. Instances are of one of the following types:
 *     org.apache.xmlbeans.XmlDate
 *     org.openarchives.oai.x20.UTCdateTimeZType
 */
public class UTCdatetimeTypeImpl extends org.apache.xmlbeans.impl.values.XmlUnionImpl implements org.openarchives.oai.x20.UTCdatetimeType, org.apache.xmlbeans.XmlDate, org.openarchives.oai.x20.UTCdateTimeZType
{
    private static final long serialVersionUID = 1L;
    
    public UTCdatetimeTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType, false);
    }
    
    protected UTCdatetimeTypeImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
    {
        super(sType, b);
    }
}
