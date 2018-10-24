<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:mmm="http://api.memorix-maior.nl/REST/3.0/" xmlns:oai_dc="http://www.openarchives.org/OAI/2.0/oai_dc/" version="2.0">
    <xsl:output omit-xml-declaration="no" method="xml" indent="yes" />
    <xsl:param name="identifier" />
    <xsl:param name="edmType" />
    <xsl:param name="dataProvider" />
    <xsl:param name="set" />
    <xsl:param name="language" />
    <xsl:param name="provider" />
    <xsl:param name="rights" />
    <xsl:template match="@*|node()">
        <rdf:RDF xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:edm="http://www.europeana.eu/schemas/edm/" xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:ore="http://www.openarchives.org/ore/terms/" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:rdaGr2="http://rdvocab.info/ElementsGr2/" xmlns:skos="http://www.w3.org/2004/02/skos/core#" xmlns:wgs84_pos="http://www.w3.org/2003/01/geo/wgs84_pos#">

            <xsl:variable name="identifier_status" select="/mmm:memorix/mmm:status/text()"/>
            <xsl:variable name="identifier_object" select="/mmm:memorix/mmm:adressen[1]/mmm:object/text()"/>
            <xsl:variable name="concat_identifier" select="concat(replace($identifier_status,'\s','_'),':',replace($identifier_object,'\s','_'))"/>

            <xsl:variable name="dc:coverage" select="/mmm:memorix/contribution"/>
            <xsl:variable name="dc:creator" select="/mmm:memorix/mmm:ontwerper"/>
            <xsl:variable name="dc:date" select="/mmm:memorix/mmm:besluit_bw_datum"/>
            <xsl:variable name="dc:description" select="/mmm:memorix/mmm:redengevende_omschrijving"/>
            <xsl:variable name="dc:description_2" select="./mmm:metadata/oai_dc:dc/dc:description"/>
            <xsl:variable name="dc:identifier" select="/mmm:memorix/mmm:monumentnummer"/>
            <xsl:variable name="dc:subject" select="$identifier_status"/>
            <xsl:variable name="dc:title" select="$concat_identifier"/>
            <xsl:variable name="dc:type" select="/mmm:memorix/mmm:bouwstijl"/>

            <xsl:variable name="adressen" select="/mmm:memorix/mmm:adressen"/>
            <xsl:variable name="stadsdeel" select="distinct-values(/mmm:memorix/mmm:adressen/mmm:stadsdeel)"/>
            <xsl:variable name="buurt" select="distinct-values(/mmm:memorix/mmm:adressen/mmm:buurt)"/>
            <xsl:variable name="plaats" select="distinct-values(/mmm:memorix/mmm:adressen/mmm:plaats)"/>

            <edm:ProvidedCHO>
                <xsl:attribute name="rdf:about">
                    <xsl:value-of
                            select="iri-to-uri(concat('ProvidedCHO:', replace($identifier, '\s','_')))" />
                </xsl:attribute>

                <!-- dc:coverage -->
                <xsl:if test="$dc:coverage != '' ">
                    <xsl:for-each select="$dc:coverage">
                        <dc:coverage>
                            <xsl:value-of select="text()"/>
                        </dc:coverage>
                    </xsl:for-each>
                </xsl:if>

                <!-- dc:creator -->
                <xsl:if test="$dc:creator != ''">
                    <xsl:for-each select="$dc:creator">
                        <dc:creator>
                            <xsl:value-of select="text()"/>
                        </dc:creator>
                    </xsl:for-each>
                </xsl:if>

                <!-- dc:date -->
                <xsl:if test="$dc:date != ''">
                    <xsl:for-each select="$dc:date">
                        <dc:date>
                            <xsl:value-of select="text()"/>
                        </dc:date>
                    </xsl:for-each>
                </xsl:if>

                <!-- dc:description -->
                <xsl:if test="$dc:description != ''">
                    <xsl:for-each select="$dc:description">
                        <dc:description>
                            <xsl:value-of select="text()"/>
                        </dc:description>
                    </xsl:for-each>
                </xsl:if>
                <xsl:if test="$dc:description_2 != ''">
                    <xsl:for-each select="$dc:description_2">
                        <dc:description>
                            <xsl:value-of select="text()"/>
                        </dc:description>
                    </xsl:for-each>
                </xsl:if>

                <!-- dc:identifier -->
                <xsl:if test="$dc:identifier != ''">
                    <xsl:for-each select="$dc:identifier">
                        <dc:identifier>
                            <xsl:value-of select="text()"/>
                        </dc:identifier>
                    </xsl:for-each>
                </xsl:if>

                <!-- dc:language -->
                <xsl:if test="$language != ''">
                    <dc:language>
                        <xsl:value-of select="$language" />
                    </dc:language>
                </xsl:if>

                <!-- dc:subject -->
                <xsl:if test="$dc:subject != ''">
                    <xsl:for-each select="$dc:subject">
                        <dc:subject>
                            <xsl:value-of select="replace(.,'\s','_')"/>
                        </dc:subject>
                    </xsl:for-each>
                </xsl:if>

                <!-- dc:title -->
                <xsl:if test="$concat_identifier != ''">
                    <dc:title>
                        <xsl:value-of select="$concat_identifier"/>
                    </dc:title>
                </xsl:if>

                <!-- dc:type -->
                <xsl:if test="$dc:type != ''">
                    <xsl:for-each select="$dc:type">
                        <dc:type>
                            <xsl:value-of select="text()"/>
                        </dc:type>
                    </xsl:for-each>
                </xsl:if>

                <xsl:if test="/mmm:memorix/mmm:bouwperiode != ''">
                    <edm:isRelatedTo>
                        <xsl:attribute name="rdf:resource">
                            <xsl:variable name="TimeSpanAbout" select="/mmm:memorix/mmm:bouwperiode/text()"/>
                            <xsl:value-of
                                    select="iri-to-uri(concat('TimeSpan:', replace($TimeSpanAbout, '\s','_')))" />
                        </xsl:attribute>
                    </edm:isRelatedTo>
                </xsl:if>

                <xsl:if test="$stadsdeel != ''">
                    <xsl:for-each select="$stadsdeel">
                        <edm:isRelatedTo>
                            <xsl:attribute name="rdf:resource">
                                <xsl:value-of
                                        select="iri-to-uri(concat('Place:', replace(., '\s','_')))" />
                            </xsl:attribute>
                        </edm:isRelatedTo>
                    </xsl:for-each>
                </xsl:if>

                <xsl:if test="$buurt != ''">
                    <xsl:for-each select="$buurt">
                        <edm:isRelatedTo>
                            <xsl:attribute name="rdf:resource">
                                <xsl:value-of
                                        select="iri-to-uri(concat('Place:', replace(., '\s','_')))" />
                            </xsl:attribute>
                        </edm:isRelatedTo>
                    </xsl:for-each>
                </xsl:if>

                <xsl:if test="$plaats != ''">
                    <xsl:for-each select="$plaats">
                        <edm:isRelatedTo>
                            <xsl:attribute name="rdf:resource">
                                <xsl:value-of
                                        select="iri-to-uri(concat('Place:', replace(., '\s','_')))" />
                            </xsl:attribute>
                        </edm:isRelatedTo>
                    </xsl:for-each>
                </xsl:if>

                <xsl:for-each select="$adressen">
                    <xsl:if test="$adressen != ''">
                        <xsl:if test="./mmm:verblijfseenheid_id != ''">
                            <edm:isRelatedTo>
                                <xsl:attribute name="rdf:resource">
                                    <xsl:variable name="verblijfseenheid_id" select="./mmm:verblijfseenheid_id/text()"/>
                                    <xsl:value-of
                                            select="iri-to-uri(concat('Place:', replace($verblijfseenheid_id, '\s','_')))" />
                                </xsl:attribute>
                            </edm:isRelatedTo>
                        </xsl:if>
                    </xsl:if>
                </xsl:for-each>

                <xsl:if test="$edmType != ''">
                    <edm:type>
                        <xsl:value-of select="$edmType" />
                    </edm:type>
                </xsl:if>

            </edm:ProvidedCHO>

            <xsl:if test="/mmm:memorix/mmm:bouwperiode != ''">
                <edm:TimeSpan>
                    <xsl:attribute name="rdf:about">
                        <xsl:variable name="TimeSpanAbout" select="/mmm:memorix/mmm:bouwperiode/text()"/>
                        <xsl:value-of
                                select="iri-to-uri(concat('TimeSpan:', replace($TimeSpanAbout, '\s','_')))" />
                    </xsl:attribute>
                </edm:TimeSpan>
            </xsl:if>

            <xsl:if test="$stadsdeel != ''">
                <xsl:for-each select="$stadsdeel">
                    <edm:Place>
                        <xsl:attribute name="rdf:about">
                            <xsl:value-of
                                    select="iri-to-uri(concat('Place:', replace(., '\s','_')))" />
                        </xsl:attribute>

                        <skos:prefLabel>
                            <xsl:value-of select="."/>
                        </skos:prefLabel>

                        <xsl:if test="$buurt != ''">
                            <xsl:for-each select="$buurt">
                                <dcterms:hasPart>
                                    <xsl:attribute name="rdf:resource">
                                        <xsl:value-of
                                                select="iri-to-uri(concat('Place:', replace(., '\s','_')))" />
                                    </xsl:attribute>
                                </dcterms:hasPart>
                            </xsl:for-each>
                        </xsl:if>

                        <xsl:if test="$plaats != ''">
                            <xsl:for-each select="$plaats">
                                <dcterms:hasPart>
                                    <xsl:attribute name="rdf:resource">
                                        <xsl:value-of
                                                select="iri-to-uri(concat('Place:', replace(., '\s','_')))" />
                                    </xsl:attribute>
                                </dcterms:hasPart>
                            </xsl:for-each>
                        </xsl:if>

                        <xsl:if test="$identifier != ''">
                            <dcterms:isPartOf>
                                <xsl:attribute name="rdf:resource">
                                    <xsl:value-of
                                            select="iri-to-uri(concat('ProvidedCHO:', replace($identifier, '\s','_')))" />
                                </xsl:attribute>
                            </dcterms:isPartOf>
                        </xsl:if>

                    </edm:Place>
                </xsl:for-each>
            </xsl:if>

            <xsl:if test="$buurt != ''">
                <xsl:for-each select="$buurt">
                    <edm:Place>
                        <xsl:attribute name="rdf:about">
                            <xsl:value-of
                                    select="iri-to-uri(concat('Place:', replace(., '\s','_')))" />
                        </xsl:attribute>

                        <skos:prefLabel>
                            <xsl:value-of select="."/>
                        </skos:prefLabel>

                        <xsl:if test="$plaats != ''">
                            <xsl:for-each select="$plaats">
                                <dcterms:hasPart>
                                    <xsl:attribute name="rdf:resource">
                                        <xsl:value-of
                                                select="iri-to-uri(concat('Place:', replace(., '\s','_')))" />
                                    </xsl:attribute>
                                </dcterms:hasPart>
                            </xsl:for-each>
                        </xsl:if>

                        <xsl:if test="$stadsdeel != ''">
                            <xsl:for-each select="$stadsdeel">
                                <dcterms:isPartOf>
                                    <xsl:attribute name="rdf:resource">
                                        <xsl:value-of
                                                select="iri-to-uri(concat('Place:', replace(., '\s','_')))" />
                                    </xsl:attribute>
                                </dcterms:isPartOf>
                            </xsl:for-each>
                        </xsl:if>

                        <xsl:if test="$identifier != ''">
                            <dcterms:isPartOf>
                                <xsl:attribute name="rdf:resource">
                                    <xsl:value-of
                                            select="iri-to-uri(concat('ProvidedCHO:', replace($identifier, '\s','_')))" />
                                </xsl:attribute>
                            </dcterms:isPartOf>
                        </xsl:if>

                    </edm:Place>
                </xsl:for-each>
            </xsl:if>

            <xsl:if test="$plaats != ''">
                <xsl:for-each select="$plaats">
                    <edm:Place>
                        <xsl:attribute name="rdf:about">
                            <xsl:value-of
                                    select="iri-to-uri(concat('Place:', replace(., '\s','_')))" />
                        </xsl:attribute>

                        <skos:prefLabel>
                            <xsl:value-of select="."/>
                        </skos:prefLabel>

                        <xsl:if test="$stadsdeel != ''">
                            <xsl:for-each select="$stadsdeel">
                                <dcterms:isPartOf>
                                    <xsl:attribute name="rdf:resource">
                                        <xsl:value-of
                                                select="iri-to-uri(concat('Place:', replace(., '\s','_')))" />
                                    </xsl:attribute>
                                </dcterms:isPartOf>
                            </xsl:for-each>
                        </xsl:if>

                        <xsl:if test="$identifier != ''">
                            <dcterms:isPartOf>
                                <xsl:attribute name="rdf:resource">
                                    <xsl:value-of
                                            select="iri-to-uri(concat('ProvidedCHO:', replace($identifier, '\s','_')))" />
                                </xsl:attribute>
                            </dcterms:isPartOf>
                        </xsl:if>

                        <xsl:if test="$buurt != ''">
                            <xsl:for-each select="$buurt">
                                <dcterms:isPartOf>
                                    <xsl:attribute name="rdf:resource">
                                        <xsl:value-of
                                                select="iri-to-uri(concat('Place:', replace(., '\s','_')))" />
                                    </xsl:attribute>
                                </dcterms:isPartOf>
                            </xsl:for-each>
                        </xsl:if>

                    </edm:Place>
                </xsl:for-each>
            </xsl:if>

            <xsl:for-each select="$adressen">
                <xsl:if test="$adressen != ''">
                    <xsl:variable name="verblijfseenheid_id" select="./mmm:verblijfseenheid_id/text()"/>
                    <xsl:if test="$verblijfseenheid_id != ''">
                        <edm:Place>
                            <xsl:attribute name="rdf:about">
                                <xsl:value-of
                                        select="iri-to-uri(concat('Place:', replace($verblijfseenheid_id, '\s','_')))" />
                            </xsl:attribute>

                            <xsl:variable name="postcode" select="./mmm:postcode"/>
                            <xsl:variable name="huidigefunctie" select="./mmm:huidigefunctie"/>
                            <xsl:variable name="straat" select="./mmm:straat"/>
                            <xsl:variable name="huisnummer" select="./mmm:huisnummer"/>

                            <xsl:variable name="gemeente_y" select="./mmm:gemeente_y"/>
                            <xsl:variable name="gemeente_x" select="./mmm:gemeente_x"/>

                            <xsl:if test="$gemeente_x != ''">
                                <xsl:for-each select="$gemeente_x">
                                    <wgs84_pos:lat>
                                        <xsl:value-of select="."/>
                                    </wgs84_pos:lat>
                                </xsl:for-each>
                            </xsl:if>

                            <xsl:if test="$gemeente_y != ''">
                                <xsl:for-each select="$gemeente_y">
                                    <wgs84_pos:long>
                                        <xsl:value-of select="."/>
                                    </wgs84_pos:long>
                                </xsl:for-each>
                            </xsl:if>

                            <xsl:if test="$straat != ''">
                                <xsl:for-each select="$straat">
                                    <skos:prefLabel>
                                        <xsl:value-of select="."/>
                                    </skos:prefLabel>
                                </xsl:for-each>
                            </xsl:if>

                            <xsl:if test="$postcode != ''">
                                <xsl:for-each select="$postcode">
                                    <skos:note>
                                        <xsl:value-of select="."/>
                                    </skos:note>
                                </xsl:for-each>
                            </xsl:if>

                            <xsl:if test="$huidigefunctie != ''">
                                <xsl:for-each select="$huidigefunctie">
                                    <skos:note>
                                        <xsl:value-of select="."/>
                                    </skos:note>
                                </xsl:for-each>
                            </xsl:if>

                            <xsl:if test="$huisnummer != ''">
                                <xsl:for-each select="$huisnummer">
                                    <skos:note>
                                        <xsl:value-of select="."/>
                                    </skos:note>
                                </xsl:for-each>
                            </xsl:if>

                            <xsl:if test="$identifier != ''">
                                <dcterms:isPartOf>
                                    <xsl:attribute name="rdf:resource">
                                        <xsl:value-of
                                                select="iri-to-uri(concat('ProvidedCHO:', replace($identifier, '\s','_')))" />
                                    </xsl:attribute>
                                </dcterms:isPartOf>
                            </xsl:if>

                            <xsl:if test="$stadsdeel != ''">
                                <xsl:for-each select="$stadsdeel">
                                    <dcterms:isPartOf>
                                        <xsl:attribute name="rdf:resource">
                                            <xsl:value-of
                                                    select="iri-to-uri(concat('Place:', replace(., '\s','_')))" />
                                        </xsl:attribute>
                                    </dcterms:isPartOf>
                                </xsl:for-each>
                            </xsl:if>

                            <xsl:if test="$buurt != ''">
                                <xsl:for-each select="$buurt">
                                    <dcterms:isPartOf>
                                        <xsl:attribute name="rdf:resource">
                                            <xsl:value-of
                                                    select="iri-to-uri(concat('Place:', replace(., '\s','_')))" />
                                        </xsl:attribute>
                                    </dcterms:isPartOf>
                                </xsl:for-each>
                            </xsl:if>

                            <xsl:if test="$plaats != ''">
                                <xsl:for-each select="$plaats">
                                    <dcterms:isPartOf>
                                        <xsl:attribute name="rdf:resource">
                                            <xsl:value-of
                                                    select="iri-to-uri(concat('Place:', replace(., '\s','_')))" />
                                        </xsl:attribute>
                                    </dcterms:isPartOf>
                                </xsl:for-each>
                            </xsl:if>
                        </edm:Place>
                    </xsl:if>
                </xsl:if>
            </xsl:for-each>
        </rdf:RDF>
    </xsl:template>
</xsl:stylesheet>