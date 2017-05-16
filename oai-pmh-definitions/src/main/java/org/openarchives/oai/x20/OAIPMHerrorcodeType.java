/*
 * XML Type:  OAI-PMHerrorcodeType
 * Namespace: http://www.openarchives.org/OAI/2.0/
 * Java type: org.openarchives.oai.x20.OAIPMHerrorcodeType
 *
 * Automatically generated - do not modify.
 */
package org.openarchives.oai.x20;


/**
 * An XML OAI-PMHerrorcodeType(@http://www.openarchives.org/OAI/2.0/).
 *
 * This is an atomic type that is a restriction of org.openarchives.oai.x20.OAIPMHerrorcodeType.
 */
public interface OAIPMHerrorcodeType extends org.apache.xmlbeans.XmlString
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(OAIPMHerrorcodeType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s7B9B63BB2BFC0F19BAF6B9787119208B").resolveHandle("oaipmherrorcodetype71e5type");
    
    org.apache.xmlbeans.StringEnumAbstractBase enumValue();
    void set(org.apache.xmlbeans.StringEnumAbstractBase e);
    
    static final Enum CANNOT_DISSEMINATE_FORMAT = Enum.forString("cannotDisseminateFormat");
    static final Enum ID_DOES_NOT_EXIST = Enum.forString("idDoesNotExist");
    static final Enum BAD_ARGUMENT = Enum.forString("badArgument");
    static final Enum BAD_VERB = Enum.forString("badVerb");
    static final Enum NO_METADATA_FORMATS = Enum.forString("noMetadataFormats");
    static final Enum NO_RECORDS_MATCH = Enum.forString("noRecordsMatch");
    static final Enum BAD_RESUMPTION_TOKEN = Enum.forString("badResumptionToken");
    static final Enum NO_SET_HIERARCHY = Enum.forString("noSetHierarchy");
    
    static final int INT_CANNOT_DISSEMINATE_FORMAT = Enum.INT_CANNOT_DISSEMINATE_FORMAT;
    static final int INT_ID_DOES_NOT_EXIST = Enum.INT_ID_DOES_NOT_EXIST;
    static final int INT_BAD_ARGUMENT = Enum.INT_BAD_ARGUMENT;
    static final int INT_BAD_VERB = Enum.INT_BAD_VERB;
    static final int INT_NO_METADATA_FORMATS = Enum.INT_NO_METADATA_FORMATS;
    static final int INT_NO_RECORDS_MATCH = Enum.INT_NO_RECORDS_MATCH;
    static final int INT_BAD_RESUMPTION_TOKEN = Enum.INT_BAD_RESUMPTION_TOKEN;
    static final int INT_NO_SET_HIERARCHY = Enum.INT_NO_SET_HIERARCHY;
    
    /**
     * Enumeration value class for org.openarchives.oai.x20.OAIPMHerrorcodeType.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_CANNOT_DISSEMINATE_FORMAT
     * Enum.forString(s); // returns the enum value for a string
     * Enum.forInt(i); // returns the enum value for an int
     * </pre>
     * Enumeration objects are immutable singleton objects that
     * can be compared using == object equality. They have no
     * public constructor. See the constants defined within this
     * class for all the valid values.
     */
    static final class Enum extends org.apache.xmlbeans.StringEnumAbstractBase
    {
        /**
         * Returns the enum value for a string, or null if none.
         */
        public static Enum forString(java.lang.String s)
            { return (Enum)table.forString(s); }
        /**
         * Returns the enum value corresponding to an int, or null if none.
         */
        public static Enum forInt(int i)
            { return (Enum)table.forInt(i); }
        
        private Enum(java.lang.String s, int i)
            { super(s, i); }
        
        static final int INT_CANNOT_DISSEMINATE_FORMAT = 1;
        static final int INT_ID_DOES_NOT_EXIST = 2;
        static final int INT_BAD_ARGUMENT = 3;
        static final int INT_BAD_VERB = 4;
        static final int INT_NO_METADATA_FORMATS = 5;
        static final int INT_NO_RECORDS_MATCH = 6;
        static final int INT_BAD_RESUMPTION_TOKEN = 7;
        static final int INT_NO_SET_HIERARCHY = 8;
        
        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table
        (
            new Enum[]
            {
                new Enum("cannotDisseminateFormat", INT_CANNOT_DISSEMINATE_FORMAT),
                new Enum("idDoesNotExist", INT_ID_DOES_NOT_EXIST),
                new Enum("badArgument", INT_BAD_ARGUMENT),
                new Enum("badVerb", INT_BAD_VERB),
                new Enum("noMetadataFormats", INT_NO_METADATA_FORMATS),
                new Enum("noRecordsMatch", INT_NO_RECORDS_MATCH),
                new Enum("badResumptionToken", INT_BAD_RESUMPTION_TOKEN),
                new Enum("noSetHierarchy", INT_NO_SET_HIERARCHY),
            }
        );
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() { return forInt(intValue()); } 
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.openarchives.oai.x20.OAIPMHerrorcodeType newValue(java.lang.Object obj) {
          return (org.openarchives.oai.x20.OAIPMHerrorcodeType) type.newValue( obj ); }
        
        public static org.openarchives.oai.x20.OAIPMHerrorcodeType newInstance() {
          return (org.openarchives.oai.x20.OAIPMHerrorcodeType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.openarchives.oai.x20.OAIPMHerrorcodeType newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.openarchives.oai.x20.OAIPMHerrorcodeType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.openarchives.oai.x20.OAIPMHerrorcodeType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.openarchives.oai.x20.OAIPMHerrorcodeType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.openarchives.oai.x20.OAIPMHerrorcodeType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.openarchives.oai.x20.OAIPMHerrorcodeType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.openarchives.oai.x20.OAIPMHerrorcodeType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openarchives.oai.x20.OAIPMHerrorcodeType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.openarchives.oai.x20.OAIPMHerrorcodeType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openarchives.oai.x20.OAIPMHerrorcodeType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.openarchives.oai.x20.OAIPMHerrorcodeType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openarchives.oai.x20.OAIPMHerrorcodeType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.openarchives.oai.x20.OAIPMHerrorcodeType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openarchives.oai.x20.OAIPMHerrorcodeType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.openarchives.oai.x20.OAIPMHerrorcodeType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openarchives.oai.x20.OAIPMHerrorcodeType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.openarchives.oai.x20.OAIPMHerrorcodeType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openarchives.oai.x20.OAIPMHerrorcodeType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.openarchives.oai.x20.OAIPMHerrorcodeType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openarchives.oai.x20.OAIPMHerrorcodeType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.openarchives.oai.x20.OAIPMHerrorcodeType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openarchives.oai.x20.OAIPMHerrorcodeType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.openarchives.oai.x20.OAIPMHerrorcodeType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.openarchives.oai.x20.OAIPMHerrorcodeType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.openarchives.oai.x20.OAIPMHerrorcodeType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.openarchives.oai.x20.OAIPMHerrorcodeType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.openarchives.oai.x20.OAIPMHerrorcodeType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.openarchives.oai.x20.OAIPMHerrorcodeType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.openarchives.oai.x20.OAIPMHerrorcodeType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.openarchives.oai.x20.OAIPMHerrorcodeType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.openarchives.oai.x20.OAIPMHerrorcodeType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.openarchives.oai.x20.OAIPMHerrorcodeType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.openarchives.oai.x20.OAIPMHerrorcodeType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.openarchives.oai.x20.OAIPMHerrorcodeType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
