<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:mmm="http://api.memorix-maior.nl/REST/3.0/" xmlns:ns1="http://www.openarchives.org/OAI/2.0/"
	xmlns:oai_dc="http://www.openarchives.org/OAI/2.0/oai_dc/" xmlns:dc="http://purl.org/dc/elements/1.1/"
	xmlns:a2a="http://Mindbus.nl/A2A" xmlns:xs="http://www.w3.org/2001/XMLSchema"
	version="2.0">
	<xsl:output omit-xml-declaration="no" method="xml" indent="yes" />
	<xsl:param name="identifier" />
	<xsl:param name="edmType" />
	<xsl:param name="dataProvider" />
	<xsl:param name="set" />

	<xsl:template match="@*|node()">
		<rdf:RDF xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
			xmlns:dcterms="http://purl.org/dc/terms/" xmlns:edm="http://www.europeana.eu/schemas/edm/"
			xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:ore="http://www.openarchives.org/ore/terms/"
			xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:rdaGr2="http://rdvocab.info/ElementsGr2/"
			xmlns:skos="http://www.w3.org/2004/02/skos/core#" xmlns:wgs84_pos="http://www.w3.org/2003/01/geo/wgs84_pos#">

			<xsl:variable name="providedCHOAbout" select="replace($identifier,'\s','_')" />

			<xsl:variable name="dc:contributor" select="/oai_dc:dc/dc:contributor" />
			<xsl:variable name="dc:coverage"
				select="/a2a:A2A/a2a:Source/a2a:SourcePlace/a2a:Place" />
			<xsl:variable name="dc:creator" select="/oai_dc:dc/dc:creator" />
			<xsl:variable name="dc:date" select="/oai_dc:dc/dc:date" />
			<xsl:variable name="dc:description"
				select="/a2a:A2A/a2a:Source/a2a:SourceReference/a2a:Collection" />
			<xsl:variable name="dc:format" select="/oai_dc:dc/dc:format" />
			<xsl:variable name="dc:identifier" select="/oai_dc:dc/dc:identifier" />
			<xsl:variable name="dc:publisher" select="/oai_dc:dc/dc:publisher" />
			<xsl:variable name="dc:relation" select="/oai_dc:dc/dc:relation" />
			<xsl:variable name="dc:rights" select="/oai_dc:dc/dc:rights" />
			<xsl:variable name="dc:source" select="/oai_dc:dc/dc:source" />
			<xsl:variable name="dc:subject" select="/oai_dc:dc/dc:subject" />
			<xsl:variable name="dc:title"
				select="/a2a:A2A/a2a:Source/a2a:SourceReference/a2a:Book" />
			<xsl:variable name="dc:relation" select="/oai_dc:dc/dc:relation" />
			<xsl:variable name="dc:type" select="./a2a:A2A/a2a:Source/a2a:SourceType" />
			<xsl:variable name="dc:relation" select="/oai_dc:dc/dc:relation" />

			<xsl:variable name="edm:isRelatedToAgents" select="/a2a:A2A/a2a:Person" />
			<xsl:variable name="edm:isRelatedToConcept"
				select="/a2a:A2A/a2a:Source/a2a:SourceType/text()" />
			<xsl:variable name="edm:isRelatedToPlace"
				select="/a2a:A2A/a2a:Source/a2a:SourcePlace/a2a:Place/text()" />
			<xsl:variable name="edm:isRelatedToTimeSpan"
				select="/a2a:A2A/a2a:Source/a2a:SourceIndexDate/a2a:From/text()" />

			<xsl:variable name="edm:WebResource"
				select="/a2a:A2A/a2a:Source/a2a:SourceAvailableScans/a2a:Scan/a2a:Uri" />
			<xsl:variable name="ore:Aggregation"
				select="distinct-values(/a2a:A2A/a2a:Source/a2a:SourceAvailableScans/a2a:Scan/a2a:UriViewer)" />

			<xsl:variable name="a2a:SourceDigitalOriginal"
				select="/a2a:A2A/a2a:Source/a2a:SourceDigitalOriginal/text()" />

			<xsl:variable name="edm:TimeSpan"
				select="/a2a:A2A/a2a:Source/a2a:SourceIndexDate" />


			<xsl:if test="$providedCHOAbout != ''">
				<edm:ProvidedCHO>
					<xsl:choose>
						<xsl:when test="starts-with($providedCHOAbout, 'http')">
							<xsl:attribute name="rdf:about">
							<xsl:value-of select="iri-to-uri($providedCHOAbout)" />									
							</xsl:attribute>
						</xsl:when>
						<xsl:otherwise>
							<xsl:attribute name="rdf:about">
							<xsl:value-of
								select="iri-to-uri(concat('ProvidedCHO:', $providedCHOAbout))" />									
							</xsl:attribute>
						</xsl:otherwise>
					</xsl:choose>

					<xsl:if test="$dc:contributor != ''">
						<xsl:for-each select="$dc:contributor">
							<dc:contributor>
								<xsl:value-of select="text()" />
							</dc:contributor>
						</xsl:for-each>
					</xsl:if>

					<xsl:if test="$dc:creator != ''">
						<xsl:for-each select="$dc:creator">
							<dc:creator>
								<xsl:value-of select="text()" />
							</dc:creator>
						</xsl:for-each>
					</xsl:if>

					<xsl:if test="$dc:date != ''">
						<xsl:for-each select="$dc:date">
							<dc:date>
								<xsl:value-of select="text()" />
							</dc:date>
						</xsl:for-each>
					</xsl:if>

					<xsl:if test="$dc:description != ''">
						<xsl:for-each select="$dc:description">
							<dc:description>
								<xsl:value-of select="text()" />
							</dc:description>
						</xsl:for-each>
					</xsl:if>

					<xsl:if test="$dc:format != ''">
						<xsl:for-each select="$dc:format">
							<dc:format>
								<xsl:value-of select="text()" />
							</dc:format>
						</xsl:for-each>
					</xsl:if>

					<xsl:if test="$dc:identifier != ''">
						<xsl:for-each select="$dc:identifier">
							<dc:identifier>
								<xsl:value-of select="text()" />
							</dc:identifier>
						</xsl:for-each>
					</xsl:if>

					<dc:language>
						<xsl:text>nl</xsl:text>
					</dc:language>

					<xsl:if test="$dc:publisher != ''">
						<xsl:for-each select="$dc:publisher">
							<dc:publisher>
								<xsl:value-of select="text()" />
							</dc:publisher>
						</xsl:for-each>
					</xsl:if>

					<xsl:if test="$dc:relation != ''">
						<xsl:for-each select="$dc:relation">
							<dc:relation>
								<xsl:value-of select="text()" />
							</dc:relation>
						</xsl:for-each>
					</xsl:if>

					<xsl:if test="$dc:rights != ''">
						<xsl:for-each select="$dc:rights">
							<dc:rights>
								<xsl:value-of select="text()" />
							</dc:rights>
						</xsl:for-each>
					</xsl:if>

					<xsl:if test="$dc:source != ''">
						<xsl:for-each select="$dc:source">
							<dc:source>
								<xsl:value-of select="text()" />
							</dc:source>
						</xsl:for-each>
					</xsl:if>

					<xsl:if test="$dc:subject != ''">
						<xsl:for-each select="$dc:subject">
							<dc:subject>
								<xsl:value-of select="text()" />
							</dc:subject>
						</xsl:for-each>
					</xsl:if>

					<xsl:if test="$dc:title != ''">
						<xsl:for-each select="$dc:title">
							<dc:title>
								<xsl:value-of select="text()" />
							</dc:title>
						</xsl:for-each>
					</xsl:if>

					<xsl:if test="$dc:type != ''">
						<xsl:for-each select="$dc:type">
							<dc:type>
								<xsl:value-of select="text()" />
							</dc:type>
						</xsl:for-each>
					</xsl:if>

					<!-- Relation Persons -->
					<xsl:if test="$edm:isRelatedToAgents != ''">
						<xsl:for-each select="$edm:isRelatedToAgents">
							<xsl:variable name="pid" select="./@pid" />
							<dc:contributor>
								<xsl:choose>
									<xsl:when test="starts-with($pid, 'http')">
										<xsl:attribute name="rdf:resource">
										<xsl:value-of select="iri-to-uri(replace($pid,'\s','_'))" />									
										</xsl:attribute>
									</xsl:when>
									<xsl:otherwise>
										<xsl:attribute name="rdf:resource">
										<xsl:value-of
											select="iri-to-uri(concat('Agent:', replace($pid, '\s','_')))" />									
										</xsl:attribute>
									</xsl:otherwise>
								</xsl:choose>
							</dc:contributor>
						</xsl:for-each>
					</xsl:if>

					<xsl:if test="$edm:isRelatedToConcept">
						<!-- Relation Concept -->
						<edm:isRelatedTo>
							<xsl:choose>
								<xsl:when test="starts-with($edm:isRelatedToConcept, 'http')">
									<xsl:attribute name="rdf:resource">
										<xsl:value-of
										select="iri-to-uri(replace($edm:isRelatedToConcept, '\s','_'))" />									
										</xsl:attribute>
								</xsl:when>
								<xsl:otherwise>
									<xsl:attribute name="rdf:resource">
										<xsl:value-of
										select="iri-to-uri(concat('Concept:', replace($edm:isRelatedToConcept, '\s','_')))" />									
										</xsl:attribute>
								</xsl:otherwise>
							</xsl:choose>
						</edm:isRelatedTo>
					</xsl:if>

					<xsl:if test="$edm:isRelatedToPlace != ''">
						<!-- Relation Place -->
						<dc:coverage>
							<xsl:choose>
								<xsl:when test="starts-with($edm:isRelatedToPlace, 'http')">
									<xsl:attribute name="rdf:resource">
										<xsl:value-of
										select="iri-to-uri(replace($edm:isRelatedToPlace, '\s','_'))" />									
										</xsl:attribute>
								</xsl:when>
								<xsl:otherwise>
									<xsl:attribute name="rdf:resource">
										<xsl:value-of
										select="iri-to-uri(concat('Place:', replace($edm:isRelatedToPlace, '\s','_')))" />								
										</xsl:attribute>
								</xsl:otherwise>
							</xsl:choose>
						</dc:coverage>
					</xsl:if>

					<xsl:if test="$edm:TimeSpan != ''">
						<!-- Relation TimeSpan -->
						<xsl:for-each select="$edm:TimeSpan">
							<xsl:variable name="from" select="a2a:From" />
							<xsl:if test="$from castable as xs:date">
								<xsl:variable name="dt" as="xs:date" select="xs:date($from)" />
								<dcterms:temporal>
									<xsl:attribute name="rdf:resource">
											<xsl:value-of select="concat('TimeSpan:', year-from-date($dt))" />	
										</xsl:attribute>
								</dcterms:temporal>
							</xsl:if>

						</xsl:for-each>
					</xsl:if>
					<edm:type>
						<xsl:value-of select="$edmType" />
					</edm:type>

				</edm:ProvidedCHO>
			</xsl:if>

			<xsl:for-each select="$edm:isRelatedToAgents">
				<xsl:variable name="pid" select="./@pid" />
				<edm:Agent>
					<xsl:choose>
						<xsl:when test="starts-with($pid, 'http')">
							<xsl:attribute name="rdf:about">
							<xsl:value-of select="iri-to-uri(replace($pid, '\s','_'))" />									
							</xsl:attribute>
						</xsl:when>
						<xsl:otherwise>
							<xsl:attribute name="rdf:about">
							<xsl:value-of
								select="iri-to-uri(concat('Agent:', replace($pid, '\s','_')))" />									
							</xsl:attribute>
						</xsl:otherwise>
					</xsl:choose>

					<xsl:if test="./a2a:PersonName/a2a:PersonNameFirstName/text() != ''">
						<skos:prefLabel>
							<xsl:value-of select="./a2a:PersonName/a2a:PersonNameFirstName/text()" />
						</skos:prefLabel>
					</xsl:if>

					<xsl:if
						test="./a2a:PersonName/a2a:PersonNamePrefixLastName != '' or ./a2a:PersonName/a2a:PersonNameLastName != ''">
						<xsl:for-each
							select="./a2a:PersonName/a2a:PersonNamePrefixLastName | ./a2a:PersonName/a2a:PersonNameLastName">
							<skos:altLabel>
								<xsl:value-of select="text()" />
							</skos:altLabel>
						</xsl:for-each>
					</xsl:if>

					<dc:identifier>
						<xsl:value-of select="$pid" />
					</dc:identifier>


					<xsl:if test="../a2a:RelationEP = $pid">
						<xsl:for-each select="../a2a:RelationEP">
							<xsl:variable name="PersonKeyRef" select="./a2a:PersonKeyRef" />
							<xsl:if test="$PersonKeyRef = $pid">
								<edm:isRelatedTo>
									<xsl:attribute name="rdf:resource">									
										<xsl:value-of
										select="iri-to-uri(concat('Concept:', replace(./a2a:RelationType/text(), '\s','_')))" />
									</xsl:attribute>
								</edm:isRelatedTo>
							</xsl:if>
						</xsl:for-each>
					</xsl:if>

					<xsl:if test="./a2a:Gender != ''">
						<rdaGr2:gender>
							<xsl:value-of select="./a2a:Gender" />
						</rdaGr2:gender>
					</xsl:if>


					<xsl:if test="./a2a:Profession != ''">
						<xsl:for-each select="./a2a:Profession">
							<rdaGr2:professionOrOccupation>
								<xsl:value-of select="text()" />
							</rdaGr2:professionOrOccupation>
						</xsl:for-each>
					</xsl:if>

				</edm:Agent>
			</xsl:for-each>

			<!-- Concept -->
			<xsl:if test="/a2a:A2A/a2a:RelationEP != ''">
				<xsl:for-each select="/a2a:A2A/a2a:RelationEP">
					<skos:Concept>
						<xsl:attribute name="rdf:about">									
							<xsl:value-of
							select="iri-to-uri(concat('Concept:', replace(./a2a:RelationType/text(), '\s','_')))" />
						</xsl:attribute>

						<xsl:if test="./a2a:RelationType != ''">
							<skos:prefLabel>
								<xsl:value-of select="./a2a:RelationType" />
							</skos:prefLabel>
						</xsl:if>

						<xsl:if test="./a2a:PersonKeyRef != ''">
							<xsl:choose>
								<xsl:when test="starts-with(./a2a:PersonKeyRef, 'http')">
									<edm:isRelatedTo>
										<xsl:attribute name="rdf:resource">
											<xsl:value-of
											select="iri-to-uri(replace(./a2a:PersonKeyRef, '\s','_'))" />			
										</xsl:attribute>
									</edm:isRelatedTo>
								</xsl:when>
								<xsl:otherwise>
									<edm:isRelatedTo>
										<xsl:attribute name="rdf:resource">
											<xsl:value-of
											select="iri-to-uri(concat('Agent:', replace(./a2a:PersonKeyRef, '\s','_')))" />			
										</xsl:attribute>
									</edm:isRelatedTo>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:if>
					</skos:Concept>
				</xsl:for-each>
			</xsl:if>

			<xsl:if test="$edm:isRelatedToConcept != ''">
				<skos:Concept>
					<xsl:choose>
						<xsl:when test="starts-with($edm:isRelatedToConcept, 'http')">
							<xsl:attribute name="rdf:about">
							<xsl:value-of
								select="iri-to-uri(replace($edm:isRelatedToConcept, '\s','_'))" />									
							</xsl:attribute>
						</xsl:when>
						<xsl:otherwise>
							<xsl:attribute name="rdf:about">
							<xsl:value-of
								select="iri-to-uri(concat('Concept:', replace($edm:isRelatedToConcept, '\s','_')))" />									
							</xsl:attribute>
						</xsl:otherwise>
					</xsl:choose>

					<skos:prefLabel>
						<xsl:value-of select="$edm:isRelatedToConcept" />
					</skos:prefLabel>

				</skos:Concept>
			</xsl:if>

			<!-- Place -->
			<xsl:if test="$edm:isRelatedToPlace != ''">
				<edm:Place>
					<xsl:choose>
						<xsl:when test="starts-with($edm:isRelatedToPlace, 'http')">
							<xsl:attribute name="rdf:about">
							<xsl:value-of
								select="iri-to-uri(replace($edm:isRelatedToPlace, '\s','_'))" />									
							</xsl:attribute>
						</xsl:when>
						<xsl:otherwise>
							<xsl:attribute name="rdf:about">
							<xsl:value-of
								select="iri-to-uri(concat('Place:', replace($edm:isRelatedToPlace, '\s','_')))" />									
							</xsl:attribute>
						</xsl:otherwise>
					</xsl:choose>

					<skos:prefLabel>
						<xsl:value-of select="$edm:isRelatedToPlace" />
					</skos:prefLabel>


				</edm:Place>
			</xsl:if>

			<!-- TimeSpan -->
			<xsl:if test="$edm:TimeSpan != ''">
				<xsl:for-each select="$edm:TimeSpan">
					<xsl:variable name="from" select="a2a:From" />
					<xsl:if test="$from castable as xs:date">
						<xsl:variable name="dt" as="xs:date" select="xs:date($from)" />
						<edm:TimeSpan>
							<xsl:attribute name="rdf:about">
								<xsl:value-of select="concat('TimeSpan:', year-from-date($dt))" />	
							</xsl:attribute>
						</edm:TimeSpan>
					</xsl:if>

				</xsl:for-each>
			</xsl:if>


			<xsl:if test="$edm:WebResource != ''">
				<xsl:for-each select="$edm:WebResource">
					<edm:WebResource>
						<xsl:choose>
							<xsl:when test="starts-with(text(), 'http')">
								<xsl:attribute name="rdf:about">
							<xsl:value-of select="iri-to-uri(replace(text(),'\s','_'))" />									
							</xsl:attribute>
							</xsl:when>
							<xsl:otherwise>
								<xsl:attribute name="rdf:about">
							<xsl:value-of
									select="iri-to-uri(concat('WebResource:', replace(text(),'\s','_')))" />									
							</xsl:attribute>
							</xsl:otherwise>
						</xsl:choose>

						<xsl:if test="text() != ''">
							<dcterms:isFormatOf>
								<xsl:value-of select="text()" />
							</dcterms:isFormatOf>
						</xsl:if>
					</edm:WebResource>
				</xsl:for-each>
			</xsl:if>
			<xsl:if test="$a2a:SourceDigitalOriginal != ''">
				<ore:Aggregation>
					<xsl:choose>
						<xsl:when test="starts-with($a2a:SourceDigitalOriginal, 'http')">
							<xsl:attribute name="rdf:about">
							<xsl:value-of select="iri-to-uri($a2a:SourceDigitalOriginal)" />									
							</xsl:attribute>
						</xsl:when>
						<xsl:otherwise>
							<xsl:attribute name="rdf:about">
							<xsl:value-of
								select="iri-to-uri(concat('Aggregation:', $a2a:SourceDigitalOriginal))" />									
							</xsl:attribute>
						</xsl:otherwise>
					</xsl:choose>

					<edm:aggregatedCHO>
						<xsl:choose>
							<xsl:when test="starts-with($a2a:SourceDigitalOriginal, 'http')">
								<xsl:attribute name="rdf:resource">
							<xsl:value-of select="iri-to-uri($a2a:SourceDigitalOriginal)" />									
							</xsl:attribute>
							</xsl:when>
							<xsl:otherwise>
								<xsl:attribute name="rdf:resource">
							<xsl:value-of
									select="iri-to-uri(concat('Aggregation:', $a2a:SourceDigitalOriginal))" />									
							</xsl:attribute>
							</xsl:otherwise>
						</xsl:choose>
					</edm:aggregatedCHO>
					<!--<xsl:if test="../../../a2a:SourceReference/a2a:InstitutionName != 
						''"> -->
					<xsl:if test="$dataProvider != ''">
						<edm:dataProvider>
							<!--<xsl:value-of select="../../../a2a:SourceReference/a2a:InstitutionName"/> -->
							<xsl:value-of select="$dataProvider" />
						</edm:dataProvider>
					</xsl:if>


					<xsl:if test="$edm:WebResource != ''">
						<xsl:for-each select="$edm:WebResource">
							<edm:hasView>
								<xsl:value-of select="replace(text(),'\s','_')" />
							</edm:hasView>

						</xsl:for-each>
					</xsl:if>

					<edm:isShownBy>
						<xsl:attribute name="rdf:resource">
							<xsl:value-of
							select="concat('ProvidedCHO:', replace($providedCHOAbout,'\s','_'))" />	
						</xsl:attribute>
					</edm:isShownBy>

					<edm:object>
						<xsl:attribute name="rdf:resource">
							<xsl:value-of
							select="concat('ProvidedCHO:', replace($providedCHOAbout,'\s','_'))" />
						</xsl:attribute>
					</edm:object>

					<xsl:if test="$dataProvider  != ''">
						<edm:provider>
							<xsl:value-of select="$dataProvider" />
						</edm:provider>
					</xsl:if>

					<xsl:if test="$set  != ''">
						<edm:intermediateProvider>
							<xsl:value-of select="$set" />
						</edm:intermediateProvider>
					</xsl:if>

					<edm:rights>
						<xsl:attribute name="rdf:resource">
							<xsl:text>http://creativecommons.org/publicdomain/mark/1.0/</xsl:text>
						</xsl:attribute>
					</edm:rights>
				</ore:Aggregation>
			</xsl:if>

		</rdf:RDF>
	</xsl:template>
</xsl:stylesheet>