/*
 * XML Type:  verbType
 * Namespace: http://www.openarchives.org/OAI/2.0/
 * Java type: org.openarchives.oai.x20.VerbType
 *
 * Automatically generated - do not modify.
 */
package org.openarchives.oai.x20;


/**
 * An XML verbType(@http://www.openarchives.org/OAI/2.0/).
 *
 * This is an atomic type that is a restriction of org.openarchives.oai.x20.VerbType.
 */
public interface VerbType extends org.apache.xmlbeans.XmlString
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(VerbType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s7B9B63BB2BFC0F19BAF6B9787119208B").resolveHandle("verbtype49e6type");
    
    org.apache.xmlbeans.StringEnumAbstractBase enumValue();
    void set(org.apache.xmlbeans.StringEnumAbstractBase e);
    
    static final Enum IDENTIFY = Enum.forString("Identify");
    static final Enum LIST_METADATA_FORMATS = Enum.forString("ListMetadataFormats");
    static final Enum LIST_SETS = Enum.forString("ListSets");
    static final Enum GET_RECORD = Enum.forString("GetRecord");
    static final Enum LIST_IDENTIFIERS = Enum.forString("ListIdentifiers");
    static final Enum LIST_RECORDS = Enum.forString("ListRecords");
    
    static final int INT_IDENTIFY = Enum.INT_IDENTIFY;
    static final int INT_LIST_METADATA_FORMATS = Enum.INT_LIST_METADATA_FORMATS;
    static final int INT_LIST_SETS = Enum.INT_LIST_SETS;
    static final int INT_GET_RECORD = Enum.INT_GET_RECORD;
    static final int INT_LIST_IDENTIFIERS = Enum.INT_LIST_IDENTIFIERS;
    static final int INT_LIST_RECORDS = Enum.INT_LIST_RECORDS;
    
    /**
     * Enumeration value class for org.openarchives.oai.x20.VerbType.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_IDENTIFY
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
        
        static final int INT_IDENTIFY = 1;
        static final int INT_LIST_METADATA_FORMATS = 2;
        static final int INT_LIST_SETS = 3;
        static final int INT_GET_RECORD = 4;
        static final int INT_LIST_IDENTIFIERS = 5;
        static final int INT_LIST_RECORDS = 6;
        
        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table
        (
            new Enum[]
            {
                new Enum("Identify", INT_IDENTIFY),
                new Enum("ListMetadataFormats", INT_LIST_METADATA_FORMATS),
                new Enum("ListSets", INT_LIST_SETS),
                new Enum("GetRecord", INT_GET_RECORD),
                new Enum("ListIdentifiers", INT_LIST_IDENTIFIERS),
                new Enum("ListRecords", INT_LIST_RECORDS),
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
        public static org.openarchives.oai.x20.VerbType newValue(java.lang.Object obj) {
          return (org.openarchives.oai.x20.VerbType) type.newValue( obj ); }
        
        public static org.openarchives.oai.x20.VerbType newInstance() {
          return (org.openarchives.oai.x20.VerbType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.openarchives.oai.x20.VerbType newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.openarchives.oai.x20.VerbType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.openarchives.oai.x20.VerbType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.openarchives.oai.x20.VerbType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.openarchives.oai.x20.VerbType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.openarchives.oai.x20.VerbType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.openarchives.oai.x20.VerbType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openarchives.oai.x20.VerbType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.openarchives.oai.x20.VerbType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openarchives.oai.x20.VerbType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.openarchives.oai.x20.VerbType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openarchives.oai.x20.VerbType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.openarchives.oai.x20.VerbType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openarchives.oai.x20.VerbType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.openarchives.oai.x20.VerbType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openarchives.oai.x20.VerbType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.openarchives.oai.x20.VerbType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openarchives.oai.x20.VerbType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.openarchives.oai.x20.VerbType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openarchives.oai.x20.VerbType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.openarchives.oai.x20.VerbType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openarchives.oai.x20.VerbType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.openarchives.oai.x20.VerbType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.openarchives.oai.x20.VerbType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.openarchives.oai.x20.VerbType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.openarchives.oai.x20.VerbType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.openarchives.oai.x20.VerbType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.openarchives.oai.x20.VerbType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.openarchives.oai.x20.VerbType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.openarchives.oai.x20.VerbType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.openarchives.oai.x20.VerbType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.openarchives.oai.x20.VerbType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.openarchives.oai.x20.VerbType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.openarchives.oai.x20.VerbType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
