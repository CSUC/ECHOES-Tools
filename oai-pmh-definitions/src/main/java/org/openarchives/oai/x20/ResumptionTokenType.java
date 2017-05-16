/*
 * XML Type:  resumptionTokenType
 * Namespace: http://www.openarchives.org/OAI/2.0/
 * Java type: org.openarchives.oai.x20.ResumptionTokenType
 *
 * Automatically generated - do not modify.
 */
package org.openarchives.oai.x20;


/**
 * An XML resumptionTokenType(@http://www.openarchives.org/OAI/2.0/).
 *
 * This is an atomic type that is a restriction of org.openarchives.oai.x20.ResumptionTokenType.
 */
public interface ResumptionTokenType extends org.apache.xmlbeans.XmlString
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ResumptionTokenType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s7B9B63BB2BFC0F19BAF6B9787119208B").resolveHandle("resumptiontokentype8featype");
    
    /**
     * Gets the "expirationDate" attribute
     */
    java.util.Calendar getExpirationDate();
    
    /**
     * Gets (as xml) the "expirationDate" attribute
     */
    org.apache.xmlbeans.XmlDateTime xgetExpirationDate();
    
    /**
     * True if has "expirationDate" attribute
     */
    boolean isSetExpirationDate();
    
    /**
     * Sets the "expirationDate" attribute
     */
    void setExpirationDate(java.util.Calendar expirationDate);
    
    /**
     * Sets (as xml) the "expirationDate" attribute
     */
    void xsetExpirationDate(org.apache.xmlbeans.XmlDateTime expirationDate);
    
    /**
     * Unsets the "expirationDate" attribute
     */
    void unsetExpirationDate();
    
    /**
     * Gets the "completeListSize" attribute
     */
    java.math.BigInteger getCompleteListSize();
    
    /**
     * Gets (as xml) the "completeListSize" attribute
     */
    org.apache.xmlbeans.XmlPositiveInteger xgetCompleteListSize();
    
    /**
     * True if has "completeListSize" attribute
     */
    boolean isSetCompleteListSize();
    
    /**
     * Sets the "completeListSize" attribute
     */
    void setCompleteListSize(java.math.BigInteger completeListSize);
    
    /**
     * Sets (as xml) the "completeListSize" attribute
     */
    void xsetCompleteListSize(org.apache.xmlbeans.XmlPositiveInteger completeListSize);
    
    /**
     * Unsets the "completeListSize" attribute
     */
    void unsetCompleteListSize();
    
    /**
     * Gets the "cursor" attribute
     */
    java.math.BigInteger getCursor();
    
    /**
     * Gets (as xml) the "cursor" attribute
     */
    org.apache.xmlbeans.XmlNonNegativeInteger xgetCursor();
    
    /**
     * True if has "cursor" attribute
     */
    boolean isSetCursor();
    
    /**
     * Sets the "cursor" attribute
     */
    void setCursor(java.math.BigInteger cursor);
    
    /**
     * Sets (as xml) the "cursor" attribute
     */
    void xsetCursor(org.apache.xmlbeans.XmlNonNegativeInteger cursor);
    
    /**
     * Unsets the "cursor" attribute
     */
    void unsetCursor();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.openarchives.oai.x20.ResumptionTokenType newInstance() {
          return (org.openarchives.oai.x20.ResumptionTokenType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.openarchives.oai.x20.ResumptionTokenType newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.openarchives.oai.x20.ResumptionTokenType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.openarchives.oai.x20.ResumptionTokenType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.openarchives.oai.x20.ResumptionTokenType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.openarchives.oai.x20.ResumptionTokenType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.openarchives.oai.x20.ResumptionTokenType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.openarchives.oai.x20.ResumptionTokenType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openarchives.oai.x20.ResumptionTokenType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.openarchives.oai.x20.ResumptionTokenType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openarchives.oai.x20.ResumptionTokenType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.openarchives.oai.x20.ResumptionTokenType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openarchives.oai.x20.ResumptionTokenType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.openarchives.oai.x20.ResumptionTokenType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openarchives.oai.x20.ResumptionTokenType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.openarchives.oai.x20.ResumptionTokenType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openarchives.oai.x20.ResumptionTokenType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.openarchives.oai.x20.ResumptionTokenType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openarchives.oai.x20.ResumptionTokenType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.openarchives.oai.x20.ResumptionTokenType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openarchives.oai.x20.ResumptionTokenType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.openarchives.oai.x20.ResumptionTokenType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openarchives.oai.x20.ResumptionTokenType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.openarchives.oai.x20.ResumptionTokenType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.openarchives.oai.x20.ResumptionTokenType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.openarchives.oai.x20.ResumptionTokenType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.openarchives.oai.x20.ResumptionTokenType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.openarchives.oai.x20.ResumptionTokenType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.openarchives.oai.x20.ResumptionTokenType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.openarchives.oai.x20.ResumptionTokenType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.openarchives.oai.x20.ResumptionTokenType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.openarchives.oai.x20.ResumptionTokenType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.openarchives.oai.x20.ResumptionTokenType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.openarchives.oai.x20.ResumptionTokenType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.openarchives.oai.x20.ResumptionTokenType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
