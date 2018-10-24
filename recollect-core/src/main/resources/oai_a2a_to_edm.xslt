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
	<xsl:param name="language" />
	<xsl:param name="provider" />
	<xsl:param name="rights" />
	<xsl:template match="@*|node()">
		<rdf:RDF xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
			xmlns:dcterms="http://purl.org/dc/terms/" xmlns:edm="http://www.europeana.eu/schemas/edm/"
			xmlns:foaf="http://xmlns.com/foaf/0.1/" xmlns:ore="http://www.openarchives.org/ore/terms/"
			xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:rdaGr2="http://rdvocab.info/ElementsGr2/"
			xmlns:skos="http://www.w3.org/2004/02/skos/core#" xmlns:wgs84_pos="http://www.w3.org/2003/01/geo/wgs84_pos#">

			<!-- A2A -->
			<xsl:variable name="a2a:Person" select="/a2a:A2A[@Version]/a2a:Person" />
			<xsl:variable name="a2a:Event" select="/a2a:A2A[@Version]/a2a:Event[@eid]" />
			<xsl:variable name="a2a:RelationEP" select="/a2a:A2A[@Version]/a2a:RelationEP" />

			<xsl:variable name="a2a:SourcePlace"
				select="/a2a:A2A[@Version]/a2a:Source/a2a:SourcePlace" />
			<xsl:variable name="a2a:SourceIndexDate"
				select="/a2a:A2A[@Version]/a2a:Source/a2a:SourceIndexDate" />
			<xsl:variable name="a2a:SourceDate"
				select="/a2a:A2A[@Version]/a2a:Source/a2a:SourceDate" />
			<xsl:variable name="a2a:SourceType"
				select="/a2a:A2A[@Version]/a2a:Source/a2a:SourceType" />
			<xsl:variable name="a2a:SourceReference"
				select="/a2a:A2A[@Version]/a2a:Source/a2a:SourceReference" />
			<xsl:variable name="a2a:SourceLastChangeDate"
				select="/a2a:A2A[@Version]/a2a:Source/a2a:SourceLastChangeDate" />
			<xsl:variable name="a2a:SourceAvailableScans"
				select="/a2a:A2A[@Version]/a2a:Source/a2a:SourceAvailableScans" />
			<xsl:variable name="a2a:SourceDigitalOriginal"
				select="/a2a:A2A[@Version]/a2a:Source/a2a:SourceDigitalOriginal" />
			<xsl:variable name="a2a:RecordGUID"
				select="/a2a:A2A[@Version]/a2a:Source/a2a:RecordGUID" />

			<!-- EDM -->

			<xsl:variable name="ProvidedCHOAbout" select="replace($identifier,'\s','_')" />

			<xsl:variable name="dc:contributor" select="/oai_dc:dc/dc:contributor" />
			<xsl:variable name="dc:coverage"
				select="/a2a:A2A[@Version]/a2a:Source/a2a:SourcePlace/a2a:Place/text()" />
			<xsl:variable name="dc:creator" select="/oai_dc:dc/dc:creator" />
			<xsl:variable name="dc:date" select="/oai_dc:dc/dc:date" />
			<xsl:variable name="dc:description"
				select="/a2a:A2A[@Version]/a2a:Source/a2a:SourceReference/a2a:Collection" />
			<xsl:variable name="dc:format" select="/oai_dc:dc/dc:format" />
			<xsl:variable name="dc:identifier" select="/oai_dc:dc/dc:identifier" />
			<xsl:variable name="dc:publisher" select="/oai_dc:dc/dc:publisher" />
			<xsl:variable name="dc:relation" select="/oai_dc:dc/dc:relation" />
			<xsl:variable name="dc:rights" select="/oai_dc:dc/dc:rights" />
			<xsl:variable name="dc:source" select="/oai_dc:dc/dc:source" />
			<xsl:variable name="dc:subject" select="/oai_dc:dc/dc:subject" />
			<xsl:variable name="dc:title"
				select="/a2a:A2A[@Version]/a2a:Source/a2a:SourceReference/a2a:Book" />
			<xsl:variable name="dc:relation" select="/oai_dc:dc/dc:relation" />
			<xsl:variable name="dc:type"
				select="/a2a:A2A[@Version]/a2a:Source/a2a:SourceType" />
			<xsl:variable name="dc:relation" select="/oai_dc:dc/dc:relation" />

			<!-- edm:ProvidedCHO -->
			<xsl:if test="$ProvidedCHOAbout != ''">
				<edm:ProvidedCHO>
					<xsl:choose>
						<xsl:when test="starts-with($ProvidedCHOAbout, 'http')">
							<xsl:attribute name="rdf:about">
							<xsl:value-of select="iri-to-uri($ProvidedCHOAbout)" />									
							</xsl:attribute>
						</xsl:when>
						<xsl:otherwise>
							<xsl:attribute name="rdf:about">
							<xsl:value-of
								select="iri-to-uri(concat('ProvidedCHO:', replace($ProvidedCHOAbout, '\s','_')))" />									
							</xsl:attribute>
						</xsl:otherwise>
					</xsl:choose>

					<!-- dc:contributor -->
					<xsl:if test="$dc:contributor != ''">
						<xsl:for-each select="$dc:contributor">
							<dc:contributor>
								<xsl:value-of select="text()" />
							</dc:contributor>
						</xsl:for-each>
					</xsl:if>

					<!-- dc:creator -->
					<xsl:if test="$dc:creator != ''">
						<xsl:for-each select="$dc:creator">
							<dc:creator>
								<xsl:value-of select="text()" />
							</dc:creator>
						</xsl:for-each>
					</xsl:if>

					<!-- dc:date -->
					<xsl:if test="$a2a:SourceIndexDate != ''">
						<xsl:variable name="fromTo"
							select="distinct-values(($a2a:SourceIndexDate/a2a:From,$a2a:SourceIndexDate/a2a:To))" />

						<xsl:for-each select="$fromTo">
							<xsl:if test="$fromTo castable as xs:date">
								<xsl:variable name="dt" as="xs:date" select="xs:date($fromTo)" />
								<dc:date>
									<xsl:value-of select="$dt" />
								</dc:date>
							</xsl:if>
						</xsl:for-each>
					</xsl:if>

					<!-- dc:description -->
					<xsl:if test="$dc:description != ''">
						<xsl:for-each select="$dc:description">
							<dc:description>
								<xsl:value-of select="text()" />
							</dc:description>
						</xsl:for-each>
					</xsl:if>

					<!-- dc:format -->
					<xsl:if test="$dc:format != ''">
						<xsl:for-each select="$dc:format">
							<dc:format>
								<xsl:value-of select="text()" />
							</dc:format>
						</xsl:for-each>
					</xsl:if>

					<!-- dc:identifier -->
					<xsl:if test="$identifier != ''">
						<dc:identifier>
							<xsl:value-of select="$identifier" />
						</dc:identifier>
					</xsl:if>

					<!-- dc:language -->
					<xsl:if test="$language != ''">
						<dc:language>
							<xsl:value-of select="$language" />
						</dc:language>
					</xsl:if>

					<!-- dc:publisher -->
					<xsl:if test="$dc:publisher != ''">
						<xsl:for-each select="$dc:publisher">
							<dc:publisher>
								<xsl:value-of select="text()" />
							</dc:publisher>
						</xsl:for-each>
					</xsl:if>

					<!-- dc:relation -->
					<xsl:if test="$dc:relation != ''">
						<xsl:for-each select="$dc:relation">
							<dc:relation>
								<xsl:value-of select="text()" />
							</dc:relation>
						</xsl:for-each>
					</xsl:if>

					<!-- dc:rights -->
					<xsl:if test="$dc:rights != ''">
						<xsl:for-each select="$dc:rights">
							<dc:rights>
								<xsl:value-of select="text()" />
							</dc:rights>
						</xsl:for-each>
					</xsl:if>

					<!-- dc:source -->
					<xsl:if test="$dc:source != ''">
						<xsl:for-each select="$dc:source">
							<dc:source>
								<xsl:value-of select="text()" />
							</dc:source>
						</xsl:for-each>
					</xsl:if>

					<!-- dc:subject -->
					<xsl:if test="$dc:subject != ''">
						<xsl:for-each select="$dc:subject">
							<dc:subject>
								<xsl:value-of select="text()" />
							</dc:subject>
						</xsl:for-each>
					</xsl:if>

					<!-- dc:title -->
					<xsl:if test="$dc:title != ''">
						<xsl:for-each select="$dc:title">
							<dc:title>
								<xsl:value-of select="text()" />
							</dc:title>
						</xsl:for-each>
					</xsl:if>

					<!-- dc:type -->
					<xsl:if test="$dc:type != ''">
						<xsl:for-each select="$dc:type">
							<dc:type>
								<xsl:value-of select="text()" />
							</dc:type>
						</xsl:for-each>
					</xsl:if>

					<!-- Relation TimeSpan -->
					<xsl:if test="$a2a:SourceIndexDate != ''">
						<xsl:variable name="from" select="$a2a:SourceIndexDate/a2a:From" />
						<xsl:if test="$from castable as xs:date">
							<xsl:variable name="dt" as="xs:date" select="xs:date($from)" />
							<dcterms:temporal>
								<xsl:attribute name="rdf:resource">
									<xsl:value-of select="concat('TimeSpan:', year-from-date($dt))" />	
								</xsl:attribute>
							</dcterms:temporal>
						</xsl:if>
					</xsl:if>

					<!-- Relation Place -->
					<xsl:if test="$a2a:SourcePlace != ''">
						<xsl:variable name="a2a:Place"
							select="$a2a:SourcePlace/a2a:Place/text()" />
						<dc:coverage>
							<xsl:attribute name="rdf:resource">
								<xsl:value-of
								select="iri-to-uri(concat('Place:', replace($a2a:Place, '\s','_')))" />								
								</xsl:attribute>
						</dc:coverage>
					</xsl:if>

					<!-- Relation Persons -->
					<xsl:if test="$a2a:Person != ''">
						<xsl:for-each select="$a2a:Person">
							<xsl:variable name="pid" select="./@pid" />
							<dc:contributor>
								<xsl:attribute name="rdf:resource">
								<xsl:value-of
									select="iri-to-uri(concat('Agent:', replace($pid, '\s','_')))" />									
								</xsl:attribute>
							</dc:contributor>
						</xsl:for-each>
					</xsl:if>

					<!-- Relation Concept -->
					<xsl:if test="$a2a:SourceType">
						<edm:isRelatedTo>
							<xsl:attribute name="rdf:resource">
							<xsl:value-of
								select="iri-to-uri(concat('Concept:', replace($a2a:SourceType, '\s','_')))" />									
							</xsl:attribute>
						</edm:isRelatedTo>
					</xsl:if>

					<!-- edm:Type -->
					<xsl:if test="$edmType != ''">
						<edm:type>
							<xsl:value-of select="$edmType" />
						</edm:type>
					</xsl:if>
				</edm:ProvidedCHO>
			</xsl:if>

			<!-- edm:Agent -->
			<xsl:if test="$a2a:Person != ''">
				<xsl:for-each select="$a2a:Person">
					<xsl:variable name="pid" select="./@pid" />
					<edm:Agent>
						<xsl:attribute name="rdf:about">
							<xsl:value-of
							select="iri-to-uri(concat('Agent:', replace($pid, '\s','_')))" />									
						</xsl:attribute>

						<xsl:if test="a2a:PersonName/a2a:PersonNameFirstName/text() != ''">
							<skos:prefLabel>
								<xsl:value-of select="a2a:PersonName/a2a:PersonNameFirstName/text()" />
							</skos:prefLabel>
						</xsl:if>

						<xsl:if
							test="a2a:PersonName/a2a:PersonNamePrefixLastName != '' or a2a:PersonName/a2a:PersonNameLastName != ''">
							<xsl:for-each
								select="a2a:PersonName/a2a:PersonNamePrefixLastName | a2a:PersonName/a2a:PersonNameLastName">
								<skos:altLabel>
									<xsl:value-of select="text()" />
								</skos:altLabel>
							</xsl:for-each>
						</xsl:if>

						<dc:identifier>
							<xsl:value-of select="$pid" />
						</dc:identifier>

						<xsl:if test="a2a:Residence != ''">
							<edm:hasMet>
								<xsl:attribute name="rdf:resource">									
									<xsl:value-of
									select="iri-to-uri(concat('Place:', replace(a2a:Residence/a2a:Place, '\s','_')))" />
								</xsl:attribute>
							</edm:hasMet>
						</xsl:if>

						<xsl:if test="$a2a:RelationEP != ''">
							<xsl:for-each select="$a2a:RelationEP">
								<xsl:variable name="PersonKeyRef" select="a2a:PersonKeyRef" />
								<xsl:if test="$PersonKeyRef = $pid">
									<edm:isRelatedTo>
										<xsl:attribute name="rdf:resource">									
										<xsl:value-of
											select="iri-to-uri(concat('Concept:', replace(a2a:RelationType, '\s','_')))" />
									</xsl:attribute>
									</edm:isRelatedTo>
								</xsl:if>
							</xsl:for-each>
						</xsl:if>

						<xsl:if test="a2a:BirthDate != ''">
							<xsl:variable name="a2a:Year" select="a2a:BirthDate/a2a:Year" />
							<xsl:variable name="a2a:Month" select="a2a:BirthDate/a2a:Month" />
							<xsl:variable name="a2a:Day" select="a2a:BirthDate/a2a:Day" />

							<xsl:if
								test="string-join( ($a2a:Year, $a2a:Month, $a2a:Day), '-') castable as xs:date">
								<xsl:variable name="dt" as="xs:date"
									select="xs:date(string-join( ($a2a:Year, $a2a:Month, $a2a:Day), '-'))" />

								<rdaGr2:dateOfBirth>
									<xsl:value-of select="$dt" />
								</rdaGr2:dateOfBirth>

							</xsl:if>
						</xsl:if>

						<xsl:if test="a2a:Gender != ''">
							<rdaGr2:gender>
								<xsl:value-of select="a2a:Gender" />
							</rdaGr2:gender>
						</xsl:if>

						<xsl:if test="a2a:BirthPlace">
							<rdaGr2:placeOfBirth>
								<xsl:attribute name="rdf:resource">									
									<xsl:value-of
									select="iri-to-uri(concat('Place:', replace(a2a:BirthPlace/a2a:Place, '\s','_')))" />
								</xsl:attribute>
							</rdaGr2:placeOfBirth>
						</xsl:if>

						<xsl:if test="a2a:Profession != ''">
							<xsl:for-each select="a2a:Profession">
								<rdaGr2:professionOrOccupation>
									<xsl:value-of select="text()" />
								</rdaGr2:professionOrOccupation>
							</xsl:for-each>
						</xsl:if>

					</edm:Agent>
				</xsl:for-each>
			</xsl:if>

			<!-- skos:Concept -->
			<xsl:if test="$a2a:SourceType != ''">
				<xsl:for-each select="$a2a:SourceType">
					<skos:Concept>
						<xsl:attribute name="rdf:about">									
							<xsl:value-of
							select="iri-to-uri(concat('Concept:', replace(text(), '\s','_')))" />
						</xsl:attribute>

						<skos:prefLabel>
							<xsl:value-of select="text()" />
						</skos:prefLabel>

					</skos:Concept>
				</xsl:for-each>
			</xsl:if>

			<!-- skos:Concept (Relation Type) -->
			<xsl:if test="$a2a:RelationEP != ''">
				<xsl:for-each select="$a2a:RelationEP">
					<xsl:if test="a2a:RelationType != ''">
						<skos:Concept>
							<xsl:attribute name="rdf:about">									
								<xsl:value-of
								select="iri-to-uri(concat('Concept:', replace(a2a:RelationType, '\s','_')))" />
							</xsl:attribute>

							<skos:prefLabel>
								<xsl:value-of select="a2a:RelationType" />
							</skos:prefLabel>

							<xsl:if test="a2a:PersonKeyRef">
								<skos:related>
									<xsl:attribute name="rdf:resource">
										<xsl:value-of
										select="iri-to-uri(concat('Agent:', replace(a2a:PersonKeyRef, '\s','_')))" />			
									</xsl:attribute>
								</skos:related>
							</xsl:if>


						</skos:Concept>
					</xsl:if>
				</xsl:for-each>
			</xsl:if>

			<!-- edm:Place -->
			<xsl:variable name="distinctPlace"
				select="distinct-values( ($a2a:Person/a2a:Residence/a2a:Place, $a2a:Person/a2a:BirthPlace/a2a:Place, $a2a:SourcePlace/a2a:Place) )" />

			<xsl:if test="$distinctPlace != ''">
				<xsl:for-each select="$distinctPlace">
					<edm:Place>
						<xsl:attribute name="rdf:about">
							<xsl:value-of
							select="iri-to-uri(concat('Place:', replace(., '\s','_')))" />									
						</xsl:attribute>

						<skos:prefLabel>
							<xsl:value-of select="." />
						</skos:prefLabel>
					</edm:Place>
				</xsl:for-each>
			</xsl:if>

			<!-- edm:TimeSpan -->
			<xsl:if test="$a2a:Event != ''">
				<xsl:if test="$a2a:Event/a2a:EventDate castable as xs:gYear">
					<xsl:variable name="dt" as="xs:gYear"
						select="xs:gYear($a2a:Event/a2a:EventDate)" />
					<edm:TimeSpan>
						<xsl:attribute name="rdf:about">
							<xsl:value-of select="concat('TimeSpan:', $dt)" />	
						</xsl:attribute>
					</edm:TimeSpan>
				</xsl:if>
			</xsl:if>

			<!-- edm:WebResource -->
			<xsl:if test="$a2a:SourceAvailableScans != ''">
				<xsl:variable name="a2a:Scan" select="$a2a:SourceAvailableScans/a2a:Scan" />

				<xsl:if test="$a2a:Scan != ''">
					<xsl:for-each select="$a2a:Scan">
						<xsl:variable name="a2a:Uri" select="a2a:Uri" />
						<xsl:variable name="a2a:UriPreview" select="a2a:UriPreview" />

						<edm:WebResource>
							<xsl:choose>
								<xsl:when test="starts-with($a2a:Uri, 'http')">
									<xsl:attribute name="rdf:about">
										<xsl:value-of select="iri-to-uri($a2a:Uri)" />									
									</xsl:attribute>
								</xsl:when>
								<xsl:otherwise>
									<xsl:attribute name="rdf:about">
										<xsl:value-of
										select="iri-to-uri(concat('WebResource:', replace($a2a:Uri,'\s','_')))" />									
									</xsl:attribute>
								</xsl:otherwise>
							</xsl:choose>
						</edm:WebResource>
					</xsl:for-each>
				</xsl:if>
			</xsl:if>

			<!-- ore:Aggregation -->
			<xsl:choose>
				<xsl:when test="$a2a:SourceAvailableScans != ''">
					<xsl:variable name="a2a:UriViewer"
						select="distinct-values($a2a:SourceAvailableScans/a2a:Scan/a2a:UriViewer)" />

					<xsl:variable name="a2a:Scan"
						select="$a2a:SourceAvailableScans/a2a:Scan" />

					<xsl:for-each select="$a2a:UriViewer">
						<xsl:variable name="a2a:Scan"
							select="$a2a:SourceAvailableScans/a2a:Scan" />
						<ore:Aggregation>
							<xsl:choose>
								<xsl:when test="starts-with($a2a:UriViewer, 'http')">
									<xsl:attribute name="rdf:about">
										<xsl:value-of select="iri-to-uri($a2a:UriViewer)" />									
									</xsl:attribute>
								</xsl:when>
								<xsl:otherwise>
									<xsl:attribute name="rdf:about">
											<xsl:value-of
										select="iri-to-uri(concat('Aggregation:', replace($a2a:UriViewer, '\s','_')))" />									
										</xsl:attribute>
								</xsl:otherwise>
							</xsl:choose>

							<edm:aggregatedCHO>
								<xsl:choose>
									<xsl:when test="starts-with($ProvidedCHOAbout, 'http')">
										<xsl:attribute name="rdf:resource">
												<xsl:value-of select="iri-to-uri($ProvidedCHOAbout)" />									
											</xsl:attribute>
									</xsl:when>
									<xsl:otherwise>
										<xsl:attribute name="rdf:resource">
												<xsl:value-of
											select="iri-to-uri(concat('ProvidedCHO:', replace($ProvidedCHOAbout, '\s','_')))" />									
											</xsl:attribute>
									</xsl:otherwise>
								</xsl:choose>
							</edm:aggregatedCHO>

							<xsl:if test="$dataProvider != ''">
								<edm:dataProvider>
									<xsl:value-of select="$dataProvider" />
								</edm:dataProvider>
							</xsl:if>

							<xsl:if test="$a2a:Scan != ''">
								<xsl:for-each select="$a2a:Scan">
									<xsl:variable name="a2a:Uri" select="a2a:Uri" />
									<xsl:variable name="a2a:UriPreview" select="a2a:UriPreview" />

									<xsl:if test="$a2a:UriPreview != ''">
										<edm:hasView>
											<xsl:choose>
												<xsl:when test="starts-with($a2a:UriPreview, 'http')">
													<xsl:attribute name="rdf:resource">
																<xsl:value-of select="iri-to-uri($a2a:UriPreview)" />									
															</xsl:attribute>
												</xsl:when>
												<xsl:otherwise>
													<xsl:attribute name="rdf:resource">
																<xsl:value-of
														select="iri-to-uri(replace($a2a:UriPreview,'\s','_'))" />									
															</xsl:attribute>
												</xsl:otherwise>
											</xsl:choose>
										</edm:hasView>
									</xsl:if>
								</xsl:for-each>
							</xsl:if>

							<xsl:if test="$distinctPlace != ''">
								<xsl:for-each select="$distinctPlace">
									<edm:hasView>
										<xsl:attribute name="rdf:resource">
											<xsl:value-of
											select="iri-to-uri(concat('Place:', replace(., '\s','_')))" />									
										</xsl:attribute>
									</edm:hasView>
								</xsl:for-each>
							</xsl:if>


							<xsl:if test="$a2a:Person != ''">
								<xsl:for-each select="$a2a:Person">
									<xsl:variable name="pid" select="./@pid" />
									<edm:hasView>
										<xsl:attribute name="rdf:resource">
											<xsl:value-of
											select="iri-to-uri(concat('Agent:', replace($pid, '\s','_')))" />									
										</xsl:attribute>
									</edm:hasView>
								</xsl:for-each>
							</xsl:if>

							<!-- skos:Concept -->
							<xsl:if test="$a2a:SourceType != ''">
								<xsl:for-each select="$a2a:SourceType">
									<edm:hasView>
										<xsl:attribute name="rdf:resource">									
											<xsl:value-of
											select="iri-to-uri(concat('Concept:', replace(text(), '\s','_')))" />
										</xsl:attribute>
									</edm:hasView>
								</xsl:for-each>
							</xsl:if>

							<!-- skos:Concept (Relation Type) -->
							<xsl:if test="$a2a:RelationEP != ''">
								<xsl:for-each select="$a2a:RelationEP">
									<xsl:if test="a2a:RelationType != ''">
										<edm:hasView>
											<xsl:attribute name="rdf:resource">									
												<xsl:value-of
												select="iri-to-uri(concat('Concept:', replace(a2a:RelationType, '\s','_')))" />
											</xsl:attribute>
										</edm:hasView>
									</xsl:if>
								</xsl:for-each>
							</xsl:if>

							<!-- edm:TimeSpan -->
							<xsl:if test="$a2a:Event != ''">
								<xsl:if test="$a2a:Event/a2a:EventDate castable as xs:gYear">
									<xsl:variable name="dt" as="xs:gYear"
										select="xs:gYear($a2a:Event/a2a:EventDate)" />
									<edm:hasView>
										<xsl:attribute name="rdf:resource">
											<xsl:value-of select="concat('TimeSpan:', $dt)" />	
										</xsl:attribute>
									</edm:hasView>
								</xsl:if>
							</xsl:if>

							<!-- Relation TimeSpan -->
							<xsl:if test="$a2a:SourceIndexDate != ''">
								<xsl:variable name="from" select="$a2a:SourceIndexDate/a2a:From" />
								<xsl:if test="$from castable as xs:date">
									<xsl:variable name="dt" as="xs:date" select="xs:date($from)" />
									<edm:hasView>
										<xsl:attribute name="rdf:resource">
											<xsl:value-of select="concat('TimeSpan:', year-from-date($dt))" />	
										</xsl:attribute>
									</edm:hasView>
								</xsl:if>
							</xsl:if>

							<edm:isShownAt>
								<xsl:choose>
									<xsl:when test="starts-with(., 'http')">
										<xsl:attribute name="rdf:resource">
												<xsl:value-of select="iri-to-uri(.)" />									
											</xsl:attribute>
									</xsl:when>
									<xsl:otherwise>
										<xsl:attribute name="rdf:resource">
												<xsl:value-of select="iri-to-uri(replace(., '\s','_'))" />									
											</xsl:attribute>
									</xsl:otherwise>
								</xsl:choose>
							</edm:isShownAt>

							<xsl:if test="$dataProvider  != ''">
								<edm:provider>
									<xsl:value-of select="$dataProvider" />
								</edm:provider>
							</xsl:if>

							<edm:rights>
								<xsl:attribute name="rdf:resource">
											<xsl:text>http://creativecommons.org/publicdomain/mark/1.0/</xsl:text>
										</xsl:attribute>
							</edm:rights>

							<xsl:if test="$set  != ''">
								<edm:intermediateProvider>
									<xsl:value-of select="$set" />
								</edm:intermediateProvider>
							</xsl:if>
						</ore:Aggregation>
					</xsl:for-each>
				</xsl:when>
				<xsl:otherwise>
					<ore:Aggregation>
						<xsl:attribute name="rdf:about">
							<xsl:value-of
							select="iri-to-uri(concat('Aggregation:', concat('ProvidedCHO:', replace($ProvidedCHOAbout, '\s','_'))))" />									
						</xsl:attribute>

						<edm:aggregatedCHO>
							<xsl:choose>
								<xsl:when test="starts-with($ProvidedCHOAbout, 'http')">
									<xsl:attribute name="rdf:resource">
										<xsl:value-of select="iri-to-uri($ProvidedCHOAbout)" />									
									</xsl:attribute>
								</xsl:when>
								<xsl:otherwise>
									<xsl:attribute name="rdf:resource">
										<xsl:value-of
										select="iri-to-uri(concat('ProvidedCHO:', replace($ProvidedCHOAbout, '\s','_')))" />									
									</xsl:attribute>
								</xsl:otherwise>
							</xsl:choose>
						</edm:aggregatedCHO>

						<xsl:if test="$dataProvider != ''">
							<edm:dataProvider>
								<xsl:value-of select="$dataProvider" />
							</edm:dataProvider>
						</xsl:if>

						<xsl:if test="$distinctPlace != ''">
							<xsl:for-each select="$distinctPlace">
								<edm:hasView>
									<xsl:attribute name="rdf:resource">
											<xsl:value-of
										select="iri-to-uri(concat('Place:', replace(., '\s','_')))" />									
										</xsl:attribute>
								</edm:hasView>
							</xsl:for-each>
						</xsl:if>


						<xsl:if test="$a2a:Person != ''">
							<xsl:for-each select="$a2a:Person">
								<xsl:variable name="pid" select="./@pid" />
								<edm:hasView>
									<xsl:attribute name="rdf:resource">
											<xsl:value-of
										select="iri-to-uri(concat('Agent:', replace($pid, '\s','_')))" />									
										</xsl:attribute>
								</edm:hasView>
							</xsl:for-each>
						</xsl:if>

						<!-- skos:Concept -->
						<xsl:if test="$a2a:SourceType != ''">
							<xsl:for-each select="$a2a:SourceType">
								<edm:hasView>
									<xsl:attribute name="rdf:resource">									
											<xsl:value-of
										select="iri-to-uri(concat('Concept:', replace(text(), '\s','_')))" />
										</xsl:attribute>
								</edm:hasView>
							</xsl:for-each>
						</xsl:if>

						<!-- skos:Concept (Relation Type) -->
						<xsl:if test="$a2a:RelationEP != ''">
							<xsl:for-each select="$a2a:RelationEP">
								<xsl:if test="a2a:RelationType != ''">
									<edm:hasView>
										<xsl:attribute name="rdf:resource">									
												<xsl:value-of
											select="iri-to-uri(concat('Concept:', replace(a2a:RelationType, '\s','_')))" />
											</xsl:attribute>
									</edm:hasView>
								</xsl:if>
							</xsl:for-each>
						</xsl:if>

						<!-- edm:TimeSpan -->
						<xsl:if test="$a2a:Event != ''">
							<xsl:if test="$a2a:Event/a2a:EventDate castable as xs:gYear">
								<xsl:variable name="dt" as="xs:gYear"
									select="xs:gYear($a2a:Event/a2a:EventDate)" />
								<edm:hasView>
									<xsl:attribute name="rdf:resource">
											<xsl:value-of select="concat('TimeSpan:', $dt)" />	
										</xsl:attribute>
								</edm:hasView>
							</xsl:if>
						</xsl:if>

						<!-- Relation TimeSpan -->
						<xsl:if test="$a2a:SourceIndexDate != ''">
							<xsl:variable name="from" select="$a2a:SourceIndexDate/a2a:From" />
							<xsl:if test="$from castable as xs:date">
								<xsl:variable name="dt" as="xs:date" select="xs:date($from)" />
								<edm:hasView>
									<xsl:attribute name="rdf:resource">
											<xsl:value-of select="concat('TimeSpan:', year-from-date($dt))" />	
										</xsl:attribute>
								</edm:hasView>
							</xsl:if>
						</xsl:if>


						<xsl:if test="$dataProvider  != ''">
							<edm:provider>
								<xsl:value-of select="$dataProvider" />
							</edm:provider>
						</xsl:if>

						<edm:rights>
							<xsl:attribute name="rdf:resource">
									<xsl:text>http://creativecommons.org/publicdomain/mark/1.0/</xsl:text>
								</xsl:attribute>
						</edm:rights>

						<xsl:if test="$set  != ''">
							<edm:intermediateProvider>
								<xsl:value-of select="$set" />
							</edm:intermediateProvider>
						</xsl:if>

					</ore:Aggregation>

				</xsl:otherwise>
			</xsl:choose>

		</rdf:RDF>
	</xsl:template>
</xsl:stylesheet>