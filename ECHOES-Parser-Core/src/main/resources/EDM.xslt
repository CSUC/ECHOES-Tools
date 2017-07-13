<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:mmm="http://api.memorix-maior.nl/REST/3.0/"
	xmlns:ns1="http://www.openarchives.org/OAI/2.0/"
	xmlns:oai_dc="http://www.openarchives.org/OAI/2.0/oai_dc/"
	xmlns:dc="http://purl.org/dc/elements/1.1/"
	version="2.0">
	<xsl:output omit-xml-declaration="no" method="xml" indent="yes" />
	<xsl:param name="year" />
	<xsl:param name="month" />
	<xsl:param name="day" />
	<xsl:param name="dir" />
	<xsl:param name="folder" />
	<xsl:param name="collectionSet" />
	

	<xsl:template match="@*|node()">
		<xsl:for-each select="/ns1:OAI-PMH/ns1:ListRecords/ns1:record">			
			<xsl:result-document method="xml" href="{$folder}/{$year}/{$month}/{$day}/{$collectionSet}/{$dir}/{$collectionSet}_{$dir}_file{position()}.xml">
				<rdf:RDF xmlns:dc="http://purl.org/dc/elements/1.1/"
					xmlns:edm="http://www.europeana.eu/schemas/edm/"
					xmlns:wgs84_pos="http://www.w3.org/2003/01/geo/wgs84_pos#"
					xmlns:foaf="http://xmlns.com/foaf/0.1/"
					xmlns:rdaGr2="http://rdvocab.info/ElementsGr2/"
					xmlns:owl="http://www.w3.org/2002/07/owl#"
					xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
					xmlns:ore="http://www.openarchives.org/ore/terms/"
					xmlns:skos="http://www.w3.org/2004/02/skos/core#"
					xmlns:dcterms="http://purl.org/dc/terms/">

					<edm:ProvidedCHO>
						<xsl:attribute name="rdf:about">
							<xsl:value-of select="./ns1:header/ns1:identifier/text()"/>	
						</xsl:attribute>

						<xsl:if test="./ns1:metadata/oai_dc:dc/dc:contributor != ''">
							<xsl:for-each select="./ns1:metadata/oai_dc:dc/dc:contributor">
								<dc:contributor>
									<xsl:value-of select="text()"/>
								</dc:contributor>
							</xsl:for-each>							
						</xsl:if>

						<xsl:if test="./ns1:metadata/oai_dc:dc/dc:coverage != '' ">
							<xsl:for-each select="./ns1:metadata/oai_dc:dc/dc:coverage">
								<dc:coverage>
									<xsl:value-of select="text()"/>
								</dc:coverage>
							</xsl:for-each>							
						</xsl:if>

						<xsl:if test="./ns1:metadata/oai_dc:dc/dc:creator != ''">
							<xsl:for-each select="./ns1:metadata/oai_dc:dc/dc:creator">
								<dc:creator>
									<xsl:value-of select="text()"/>
								</dc:creator>
							</xsl:for-each>							
						</xsl:if>

						<xsl:if test="./ns1:metadata/oai_dc:dc/dc:date != ''">
							<xsl:for-each select="./ns1:metadata/oai_dc:dc/dc:date">
								<dc:date>
									<xsl:value-of select="text()"/>
								</dc:date>
							</xsl:for-each>							
						</xsl:if>

						<xsl:if test="./ns1:metadata/oai_dc:dc/dc:description != ''">
							<xsl:for-each select="./ns1:metadata/oai_dc:dc/dc:description">
								<dc:description>
									<xsl:value-of select="text()"/>
								</dc:description>
							</xsl:for-each>							
						</xsl:if>

						<xsl:if test="./ns1:metadata/oai_dc:dc/dc:format != ''">
							<xsl:for-each select="./ns1:metadata/oai_dc:dc/dc:format">
								<dc:format>
									<xsl:value-of select="text()"/>
								</dc:format>
							</xsl:for-each>							
						</xsl:if>

						<xsl:if test="./ns1:metadata/oai_dc:dc/dc:identifier != ''">
							<xsl:for-each select="./ns1:metadata/oai_dc:dc/dc:identifier">
								<dc:identifier>
									<xsl:value-of select="text()"/>
								</dc:identifier>
							</xsl:for-each>							
						</xsl:if>

						<xsl:if test="./ns1:metadata/oai_dc:dc/dc:language != ''">
							<xsl:for-each select="./ns1:metadata/oai_dc:dc/dc:language">
								<dc:language>
									<xsl:value-of select="text()"/>
								</dc:language>
							</xsl:for-each>							
						</xsl:if>

						<xsl:if test="./ns1:metadata/oai_dc:dc/dc:publisher != ''">
							<xsl:for-each select="./ns1:metadata/oai_dc:dc/dc:publisher">
								<dc:publisher>
									<xsl:value-of select="text()"/>
								</dc:publisher>
							</xsl:for-each>							
						</xsl:if>

						<xsl:if test="./ns1:metadata/oai_dc:dc/dc:relation != ''">
							<xsl:for-each select="./ns1:metadata/oai_dc:dc/dc:relation">
								<dc:relation>
									<xsl:value-of select="text()"/>
								</dc:relation>
							</xsl:for-each>							
						</xsl:if>

						<xsl:if test="./ns1:metadata/oai_dc:dc/dc:rights != ''">
							<xsl:for-each select="./ns1:metadata/oai_dc:dc/dc:rights">
								<dc:rights>
									<xsl:value-of select="text()"/>
								</dc:rights>
							</xsl:for-each>							
						</xsl:if>

						<xsl:if test="./ns1:metadata/oai_dc:dc/dc:source != ''">
							<xsl:for-each select="./ns1:metadata/oai_dc:dc/dc:source">
								<dc:source>
									<xsl:value-of select="text()"/>
								</dc:source>
							</xsl:for-each>							
						</xsl:if>

						<xsl:if test="./ns1:metadata/oai_dc:dc/dc:subject != ''">
							<xsl:for-each select="./ns1:metadata/oai_dc:dc/dc:subject">
								<dc:subject>
									<xsl:value-of select="text()"/>
								</dc:subject>
							</xsl:for-each>							
						</xsl:if>

						<xsl:if test="./ns1:metadata/oai_dc:dc/dc:title != ''">
							<xsl:for-each select="./ns1:metadata/oai_dc:dc/dc:title">
								<dc:title>
									<xsl:value-of select="text()"/>
								</dc:title>
							</xsl:for-each>							
						</xsl:if>

						<xsl:if test="./ns1:metadata/oai_dc:dc/dc:type != ''">
							<xsl:for-each select="./ns1:metadata/oai_dc:dc/dc:type">
								<dc:type>
									<xsl:value-of select="text()"/>
								</dc:type>
							</xsl:for-each>							
						</xsl:if>

						<edm:type>IMAGE</edm:type>

					</edm:ProvidedCHO>					
				</rdf:RDF>
			</xsl:result-document>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>