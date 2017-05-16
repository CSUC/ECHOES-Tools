/*
 * XML Type:  OAI-PMHtype
 * Namespace: http://www.openarchives.org/OAI/2.0/
 * Java type: org.openarchives.oai.x20.OAIPMHtype
 *
 * Automatically generated - do not modify.
 */
package org.openarchives.oai.x20;


/**
 * An XML OAI-PMHtype(@http://www.openarchives.org/OAI/2.0/).
 *
 * This is a complex type.
 */
public interface OAIPMHtype extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(OAIPMHtype.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s7B9B63BB2BFC0F19BAF6B9787119208B").resolveHandle("oaipmhtype1c42type");
    
    /**
     * Gets the "responseDate" element
     */
    java.util.Calendar getResponseDate();
    
    /**
     * Gets (as xml) the "responseDate" element
     */
    org.apache.xmlbeans.XmlDateTime xgetResponseDate();
    
    /**
     * Sets the "responseDate" element
     */
    void setResponseDate(java.util.Calendar responseDate);
    
    /**
     * Sets (as xml) the "responseDate" element
     */
    void xsetResponseDate(org.apache.xmlbeans.XmlDateTime responseDate);
    
    /**
     * Gets the "request" element
     */
    org.openarchives.oai.x20.RequestType getRequest();
    
    /**
     * Sets the "request" element
     */
    void setRequest(org.openarchives.oai.x20.RequestType request);
    
    /**
     * Appends and returns a new empty "request" element
     */
    org.openarchives.oai.x20.RequestType addNewRequest();
    
    /**
     * Gets array of all "error" elements
     */
    org.openarchives.oai.x20.OAIPMHerrorType[] getErrorArray();
    
    /**
     * Gets ith "error" element
     */
    org.openarchives.oai.x20.OAIPMHerrorType getErrorArray(int i);
    
    /**
     * Returns number of "error" element
     */
    int sizeOfErrorArray();
    
    /**
     * Sets array of all "error" element
     */
    void setErrorArray(org.openarchives.oai.x20.OAIPMHerrorType[] errorArray);
    
    /**
     * Sets ith "error" element
     */
    void setErrorArray(int i, org.openarchives.oai.x20.OAIPMHerrorType error);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "error" element
     */
    org.openarchives.oai.x20.OAIPMHerrorType insertNewError(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "error" element
     */
    org.openarchives.oai.x20.OAIPMHerrorType addNewError();
    
    /**
     * Removes the ith "error" element
     */
    void removeError(int i);
    
    /**
     * Gets the "Identify" element
     */
    org.openarchives.oai.x20.IdentifyType getIdentify();
    
    /**
     * True if has "Identify" element
     */
    boolean isSetIdentify();
    
    /**
     * Sets the "Identify" element
     */
    void setIdentify(org.openarchives.oai.x20.IdentifyType identify);
    
    /**
     * Appends and returns a new empty "Identify" element
     */
    org.openarchives.oai.x20.IdentifyType addNewIdentify();
    
    /**
     * Unsets the "Identify" element
     */
    void unsetIdentify();
    
    /**
     * Gets the "ListMetadataFormats" element
     */
    org.openarchives.oai.x20.ListMetadataFormatsType getListMetadataFormats();
    
    /**
     * True if has "ListMetadataFormats" element
     */
    boolean isSetListMetadataFormats();
    
    /**
     * Sets the "ListMetadataFormats" element
     */
    void setListMetadataFormats(org.openarchives.oai.x20.ListMetadataFormatsType listMetadataFormats);
    
    /**
     * Appends and returns a new empty "ListMetadataFormats" element
     */
    org.openarchives.oai.x20.ListMetadataFormatsType addNewListMetadataFormats();
    
    /**
     * Unsets the "ListMetadataFormats" element
     */
    void unsetListMetadataFormats();
    
    /**
     * Gets the "ListSets" element
     */
    org.openarchives.oai.x20.ListSetsType getListSets();
    
    /**
     * True if has "ListSets" element
     */
    boolean isSetListSets();
    
    /**
     * Sets the "ListSets" element
     */
    void setListSets(org.openarchives.oai.x20.ListSetsType listSets);
    
    /**
     * Appends and returns a new empty "ListSets" element
     */
    org.openarchives.oai.x20.ListSetsType addNewListSets();
    
    /**
     * Unsets the "ListSets" element
     */
    void unsetListSets();
    
    /**
     * Gets the "GetRecord" element
     */
    org.openarchives.oai.x20.GetRecordType getGetRecord();
    
    /**
     * True if has "GetRecord" element
     */
    boolean isSetGetRecord();
    
    /**
     * Sets the "GetRecord" element
     */
    void setGetRecord(org.openarchives.oai.x20.GetRecordType getRecord);
    
    /**
     * Appends and returns a new empty "GetRecord" element
     */
    org.openarchives.oai.x20.GetRecordType addNewGetRecord();
    
    /**
     * Unsets the "GetRecord" element
     */
    void unsetGetRecord();
    
    /**
     * Gets the "ListIdentifiers" element
     */
    org.openarchives.oai.x20.ListIdentifiersType getListIdentifiers();
    
    /**
     * True if has "ListIdentifiers" element
     */
    boolean isSetListIdentifiers();
    
    /**
     * Sets the "ListIdentifiers" element
     */
    void setListIdentifiers(org.openarchives.oai.x20.ListIdentifiersType listIdentifiers);
    
    /**
     * Appends and returns a new empty "ListIdentifiers" element
     */
    org.openarchives.oai.x20.ListIdentifiersType addNewListIdentifiers();
    
    /**
     * Unsets the "ListIdentifiers" element
     */
    void unsetListIdentifiers();
    
    /**
     * Gets the "ListRecords" element
     */
    org.openarchives.oai.x20.ListRecordsType getListRecords();
    
    /**
     * True if has "ListRecords" element
     */
    boolean isSetListRecords();
    
    /**
     * Sets the "ListRecords" element
     */
    void setListRecords(org.openarchives.oai.x20.ListRecordsType listRecords);
    
    /**
     * Appends and returns a new empty "ListRecords" element
     */
    org.openarchives.oai.x20.ListRecordsType addNewListRecords();
    
    /**
     * Unsets the "ListRecords" element
     */
    void unsetListRecords();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.openarchives.oai.x20.OAIPMHtype newInstance() {
          return (org.openarchives.oai.x20.OAIPMHtype) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.openarchives.oai.x20.OAIPMHtype newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.openarchives.oai.x20.OAIPMHtype) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.openarchives.oai.x20.OAIPMHtype parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.openarchives.oai.x20.OAIPMHtype) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.openarchives.oai.x20.OAIPMHtype parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.openarchives.oai.x20.OAIPMHtype) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.openarchives.oai.x20.OAIPMHtype parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openarchives.oai.x20.OAIPMHtype) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.openarchives.oai.x20.OAIPMHtype parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openarchives.oai.x20.OAIPMHtype) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.openarchives.oai.x20.OAIPMHtype parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openarchives.oai.x20.OAIPMHtype) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.openarchives.oai.x20.OAIPMHtype parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openarchives.oai.x20.OAIPMHtype) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.openarchives.oai.x20.OAIPMHtype parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openarchives.oai.x20.OAIPMHtype) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.openarchives.oai.x20.OAIPMHtype parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openarchives.oai.x20.OAIPMHtype) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.openarchives.oai.x20.OAIPMHtype parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openarchives.oai.x20.OAIPMHtype) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.openarchives.oai.x20.OAIPMHtype parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openarchives.oai.x20.OAIPMHtype) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.openarchives.oai.x20.OAIPMHtype parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.openarchives.oai.x20.OAIPMHtype) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.openarchives.oai.x20.OAIPMHtype parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.openarchives.oai.x20.OAIPMHtype) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.openarchives.oai.x20.OAIPMHtype parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.openarchives.oai.x20.OAIPMHtype) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.openarchives.oai.x20.OAIPMHtype parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.openarchives.oai.x20.OAIPMHtype) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.openarchives.oai.x20.OAIPMHtype parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.openarchives.oai.x20.OAIPMHtype) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.openarchives.oai.x20.OAIPMHtype parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.openarchives.oai.x20.OAIPMHtype) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
