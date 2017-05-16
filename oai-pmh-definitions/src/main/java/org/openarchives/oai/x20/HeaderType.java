/*
 * XML Type:  headerType
 * Namespace: http://www.openarchives.org/OAI/2.0/
 * Java type: org.openarchives.oai.x20.HeaderType
 *
 * Automatically generated - do not modify.
 */
package org.openarchives.oai.x20;


/**
 * An XML headerType(@http://www.openarchives.org/OAI/2.0/).
 *
 * This is a complex type.
 */
public interface HeaderType extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(HeaderType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s7B9B63BB2BFC0F19BAF6B9787119208B").resolveHandle("headertyped678type");
    
    /**
     * Gets the "identifier" element
     */
    java.lang.String getIdentifier();
    
    /**
     * Gets (as xml) the "identifier" element
     */
    org.openarchives.oai.x20.IdentifierType xgetIdentifier();
    
    /**
     * Sets the "identifier" element
     */
    void setIdentifier(java.lang.String identifier);
    
    /**
     * Sets (as xml) the "identifier" element
     */
    void xsetIdentifier(org.openarchives.oai.x20.IdentifierType identifier);
    
    /**
     * Gets the "datestamp" element
     */
    java.util.Calendar getDatestamp();
    
    /**
     * Gets (as xml) the "datestamp" element
     */
    org.openarchives.oai.x20.UTCdatetimeType xgetDatestamp();
    
    /**
     * Sets the "datestamp" element
     */
    void setDatestamp(java.util.Calendar datestamp);
    
    /**
     * Sets (as xml) the "datestamp" element
     */
    void xsetDatestamp(org.openarchives.oai.x20.UTCdatetimeType datestamp);
    
    /**
     * Gets array of all "setSpec" elements
     */
    java.lang.String[] getSetSpecArray();
    
    /**
     * Gets ith "setSpec" element
     */
    java.lang.String getSetSpecArray(int i);
    
    /**
     * Gets (as xml) array of all "setSpec" elements
     */
    org.openarchives.oai.x20.SetSpecType[] xgetSetSpecArray();
    
    /**
     * Gets (as xml) ith "setSpec" element
     */
    org.openarchives.oai.x20.SetSpecType xgetSetSpecArray(int i);
    
    /**
     * Returns number of "setSpec" element
     */
    int sizeOfSetSpecArray();
    
    /**
     * Sets array of all "setSpec" element
     */
    void setSetSpecArray(java.lang.String[] setSpecArray);
    
    /**
     * Sets ith "setSpec" element
     */
    void setSetSpecArray(int i, java.lang.String setSpec);
    
    /**
     * Sets (as xml) array of all "setSpec" element
     */
    void xsetSetSpecArray(org.openarchives.oai.x20.SetSpecType[] setSpecArray);
    
    /**
     * Sets (as xml) ith "setSpec" element
     */
    void xsetSetSpecArray(int i, org.openarchives.oai.x20.SetSpecType setSpec);
    
    /**
     * Inserts the value as the ith "setSpec" element
     */
    void insertSetSpec(int i, java.lang.String setSpec);
    
    /**
     * Appends the value as the last "setSpec" element
     */
    void addSetSpec(java.lang.String setSpec);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "setSpec" element
     */
    org.openarchives.oai.x20.SetSpecType insertNewSetSpec(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "setSpec" element
     */
    org.openarchives.oai.x20.SetSpecType addNewSetSpec();
    
    /**
     * Removes the ith "setSpec" element
     */
    void removeSetSpec(int i);
    
    /**
     * Gets the "status" attribute
     */
    org.openarchives.oai.x20.StatusType.Enum getStatus();
    
    /**
     * Gets (as xml) the "status" attribute
     */
    org.openarchives.oai.x20.StatusType xgetStatus();
    
    /**
     * True if has "status" attribute
     */
    boolean isSetStatus();
    
    /**
     * Sets the "status" attribute
     */
    void setStatus(org.openarchives.oai.x20.StatusType.Enum status);
    
    /**
     * Sets (as xml) the "status" attribute
     */
    void xsetStatus(org.openarchives.oai.x20.StatusType status);
    
    /**
     * Unsets the "status" attribute
     */
    void unsetStatus();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.openarchives.oai.x20.HeaderType newInstance() {
          return (org.openarchives.oai.x20.HeaderType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.openarchives.oai.x20.HeaderType newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.openarchives.oai.x20.HeaderType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.openarchives.oai.x20.HeaderType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.openarchives.oai.x20.HeaderType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.openarchives.oai.x20.HeaderType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.openarchives.oai.x20.HeaderType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.openarchives.oai.x20.HeaderType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openarchives.oai.x20.HeaderType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.openarchives.oai.x20.HeaderType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openarchives.oai.x20.HeaderType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.openarchives.oai.x20.HeaderType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openarchives.oai.x20.HeaderType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.openarchives.oai.x20.HeaderType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openarchives.oai.x20.HeaderType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.openarchives.oai.x20.HeaderType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openarchives.oai.x20.HeaderType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.openarchives.oai.x20.HeaderType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openarchives.oai.x20.HeaderType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.openarchives.oai.x20.HeaderType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openarchives.oai.x20.HeaderType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.openarchives.oai.x20.HeaderType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openarchives.oai.x20.HeaderType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.openarchives.oai.x20.HeaderType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.openarchives.oai.x20.HeaderType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.openarchives.oai.x20.HeaderType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.openarchives.oai.x20.HeaderType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.openarchives.oai.x20.HeaderType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.openarchives.oai.x20.HeaderType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.openarchives.oai.x20.HeaderType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.openarchives.oai.x20.HeaderType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.openarchives.oai.x20.HeaderType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.openarchives.oai.x20.HeaderType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.openarchives.oai.x20.HeaderType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.openarchives.oai.x20.HeaderType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
