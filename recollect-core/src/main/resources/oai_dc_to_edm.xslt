<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:mmm="http://api.memorix-maior.nl/REST/3.0/" xmlns:ns1="http://www.openarchives.org/OAI/2.0/" xmlns:oai_dc="http://www.openarchives.org/OAI/2.0/oai_dc/" version="2.0">
   <xsl:output omit-xml-declaration="no" method="xml" indent="yes" />
   <xsl:param name="identifier" />
   <xsl:param name="edmType" />
   <xsl:param name="dataProvider" />
   <xsl:template match="@*|node()">
      <rdf:RDF xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:edm="http://www.europeana.eu/schemas/edm/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:ore="http://www.openarchives.org/ore/terms/" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:rdaGr2="http://rdvocab.info/ElementsGr2/" xmlns:skos="http://www.w3.org/2004/02/skos/core#" xmlns:wgs84_pos="http://www.w3.org/2003/01/geo/wgs84_pos#">
         <xsl:variable name="providedCHOAbout" select="iri-to-uri(concat('ProvidedCHO:', replace($identifier,'\s','_')))" />
         <xsl:variable name="AggregationAbout" select="iri-to-uri(concat('Aggregation:', replace($identifier,'\s','_')))" />
         <edm:ProvidedCHO>
            <xsl:attribute name="rdf:about">
               <xsl:value-of select="$providedCHOAbout" />
            </xsl:attribute>
            <xsl:if test="/oai_dc:dc/dc:contributor != ''">
               <xsl:for-each select="/oai_dc:dc/dc:contributor">
                  <dc:contributor>
                     <xsl:value-of select="text()" />
                  </dc:contributor>
               </xsl:for-each>
            </xsl:if>
            <xsl:if test="/oai_dc:dc/dc:coverage != '' ">
               <xsl:for-each select="/oai_dc:dc/dc:coverage">
                  <dc:coverage>
                     <xsl:value-of select="text()" />
                  </dc:coverage>
               </xsl:for-each>
            </xsl:if>
            <xsl:if test="/oai_dc:dc/dc:creator != ''">
               <xsl:for-each select="/oai_dc:dc/dc:creator">
                  <dc:creator>
                     <xsl:value-of select="text()" />
                  </dc:creator>
               </xsl:for-each>
            </xsl:if>
            <xsl:if test="/oai_dc:dc/dc:date != ''">
               <xsl:for-each select="/oai_dc:dc/dc:date">
                  <dc:date>
                     <xsl:value-of select="text()" />
                  </dc:date>
               </xsl:for-each>
            </xsl:if>
            <xsl:if test="/oai_dc:dc/dc:description != ''">
               <xsl:for-each select="/oai_dc:dc/dc:description">
                  <dc:description>
                     <xsl:value-of select="text()" />
                  </dc:description>
               </xsl:for-each>
            </xsl:if>
            <xsl:if test="/oai_dc:dc/dc:format != ''">
               <xsl:for-each select="/oai_dc:dc/dc:format">
                  <dc:format>
                     <xsl:value-of select="text()" />
                  </dc:format>
               </xsl:for-each>
            </xsl:if>
            <xsl:if test="/oai_dc:dc/dc:identifier != ''">
               <xsl:for-each select="/oai_dc:dc/dc:identifier">
                  <dc:identifier>
                     <xsl:value-of select="text()" />
                  </dc:identifier>
               </xsl:for-each>
            </xsl:if>
            <xsl:if test="/oai_dc:dc/dc:language != ''">
               <xsl:for-each select="/oai_dc:dc/dc:language">
                  <dc:language>
                     <xsl:value-of select="text()" />
                  </dc:language>
               </xsl:for-each>
            </xsl:if>
            <xsl:if test="/oai_dc:dc/dc:publisher != ''">
               <xsl:for-each select="/oai_dc:dc/dc:publisher">
                  <dc:publisher>
                     <xsl:value-of select="text()" />
                  </dc:publisher>
               </xsl:for-each>
            </xsl:if>
            <xsl:if test="/oai_dc:dc/dc:relation != ''">
               <xsl:for-each select="/oai_dc:dc/dc:relation">
                  <dc:relation>
                     <xsl:value-of select="text()" />
                  </dc:relation>
               </xsl:for-each>
            </xsl:if>
            <xsl:if test="/oai_dc:dc/dc:rights != ''">
               <xsl:for-each select="/oai_dc:dc/dc:rights">
                  <dc:rights>
                     <xsl:value-of select="text()" />
                  </dc:rights>
               </xsl:for-each>
            </xsl:if>
            <xsl:if test="/oai_dc:dc/dc:source != ''">
               <xsl:for-each select="/oai_dc:dc/dc:source">
                  <dc:source>
                     <xsl:value-of select="text()" />
                  </dc:source>
               </xsl:for-each>
            </xsl:if>
            <xsl:if test="/oai_dc:dc/dc:subject != ''">
               <xsl:for-each select="/oai_dc:dc/dc:subject">
                  <dc:subject>
                     <xsl:value-of select="text()" />
                  </dc:subject>
               </xsl:for-each>
            </xsl:if>
            <xsl:if test="/oai_dc:dc/dc:title != ''">
               <xsl:for-each select="/oai_dc:dc/dc:title">
                  <dc:title>
                     <xsl:value-of select="text()" />
                  </dc:title>
               </xsl:for-each>
            </xsl:if>
            <xsl:if test="/oai_dc:dc/dc:type != ''">
               <xsl:for-each select="/oai_dc:dc/dc:type">
                  <dc:type>
                     <xsl:value-of select="text()" />
                  </dc:type>
               </xsl:for-each>
            </xsl:if>
            <edm:type>
            	<xsl:value-of select="$edmType" />
            </edm:type>
         </edm:ProvidedCHO>
         <ore:Aggregation>
            <xsl:attribute name="rdf:about">
               <xsl:value-of select="$AggregationAbout" />
            </xsl:attribute>
            <edm:aggregatedCHO>
               <xsl:attribute name="rdf:resource">
                  <xsl:value-of select="$providedCHOAbout" />
               </xsl:attribute>
            </edm:aggregatedCHO>
            <xsl:if test="$dataProvider != ''">
               <edm:dataProvider>
                  <xsl:value-of select="$dataProvider" />
               </edm:dataProvider>
            </xsl:if>            
            <xsl:if test="$dataProvider != ''">
               <edm:provider>
                  <xsl:value-of select="$dataProvider" />
               </edm:provider>
            </xsl:if>
            <edm:rights>
               <xsl:attribute name="rdf:resource">
                  <xsl:text>http://creativecommons.org/publicdomain/mark/1.0/</xsl:text>
               </xsl:attribute>
            </edm:rights>
         </ore:Aggregation>
      </rdf:RDF>
   </xsl:template>
</xsl:stylesheet>