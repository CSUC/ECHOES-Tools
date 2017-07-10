<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:mmm="http://api.memorix-maior.nl/REST/3.0/"
	xmlns:ns1="http://www.openarchives.org/OAI/2.0/"
	xmlns:oai_dc="http://www.openarchives.org/OAI/2.0/oai_dc/"
	xmlns:dc="http://purl.org/dc/elements/1.1/"
	xmlns:a2a="http://Mindbus.nl/A2A"
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
			<xsl:result-document method="xml" href="{$folder}/{$collectionSet}/{$year}/{$month}/{$day}/{$dir}/{$collectionSet}_{$dir}_file{position()}.xml">
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


					<xsl:variable name="providedCHOAbout" select="./ns1:header/ns1:identifier/text()"/>
					<xsl:variable name="dc:contributor" select="./ns1:metadata/oai_dc:dc/dc:contributor"/>
					<xsl:variable name="dc:coverage" select="./ns1:metadata/a2a:A2A/a2a:Source/a2a:SourcePlace/a2a:Place"/>
					<xsl:variable name="dc:creator" select="./ns1:metadata/oai_dc:dc/dc:creator"/>
					<xsl:variable name="dc:date" select="./ns1:metadata/oai_dc:dc/dc:date"/>
					<xsl:variable name="dc:description" select="./ns1:metadata/a2a:A2A/a2a:Source/a2a:SourceReference/a2a:Collection"/>
					<xsl:variable name="dc:format" select="./ns1:metadata/oai_dc:dc/dc:format"/>
					<xsl:variable name="dc:identifier" select="./ns1:metadata/oai_dc:dc/dc:identifier"/>
					<xsl:variable name="dc:publisher" select="./ns1:metadata/oai_dc:dc/dc:publisher"/>
					<xsl:variable name="dc:relation" select="./ns1:metadata/oai_dc:dc/dc:relation"/>
					<xsl:variable name="dc:rights" select="./ns1:metadata/oai_dc:dc/dc:rights"/>
					<xsl:variable name="dc:source" select="./ns1:metadata/oai_dc:dc/dc:source"/>
					<xsl:variable name="dc:subject" select="./ns1:metadata/oai_dc:dc/dc:subject"/>
					<xsl:variable name="dc:title" select="./ns1:metadata/a2a:A2A/a2a:Source/a2a:SourceReference/a2a:Book"/>
					<xsl:variable name="dc:relation" select="./ns1:metadata/oai_dc:dc/dc:relation"/>
					<xsl:variable name="dc:type" select="./ns1:metadata/a2a:A2A/a2a:Source/a2a:SourceType"/>
					<xsl:variable name="dc:relation" select="./ns1:metadata/oai_dc:dc/dc:relation"/>

					<xsl:variable name="edm:isRelatedToAgents" select="./ns1:metadata/a2a:A2A/a2a:Person"/>
					<xsl:variable name="edm:isRelatedToConcept" select="./ns1:metadata/a2a:A2A/a2a:Source/a2a:SourceType/text()"/>
					<xsl:variable name="edm:isRelatedToPlace" select="./ns1:metadata/a2a:A2A/a2a:Source/a2a:SourcePlace/a2a:Place/text()"/>
					<xsl:variable name="edm:isRelatedToTimeSpan" select="./ns1:metadata/a2a:A2A/a2a:Source/a2a:SourceIndexDate/a2a:From/text()"/>

					<xsl:variable name="edm:WebResource" select="./ns1:metadata/a2a:A2A/a2a:Source/a2a:SourceAvailableScans/a2a:Scan/a2a:Uri"/>
					<xsl:variable name="ore:Aggregation" select="./ns1:metadata/a2a:A2A/a2a:Source/a2a:SourceAvailableScans/a2a:Scan/a2a:UriViewer"/>

					<!-- ProvidedCHO -->
					<xsl:if test="$providedCHOAbout != ''">
						<edm:ProvidedCHO>
							<xsl:attribute name="rdf:about">							
								<xsl:value-of select="replace($providedCHOAbout,'\s','_')"/>
							</xsl:attribute>

							<xsl:if test="$dc:contributor != ''">
								<xsl:for-each select="$dc:contributor">
									<dc:contributor>
										<xsl:value-of select="text()"/>
									</dc:contributor>
								</xsl:for-each>							
							</xsl:if>

							<xsl:if test="$dc:coverage != '' ">
								<xsl:for-each select="$dc:coverage">
									<dc:coverage>
										<xsl:value-of select="text()"/>
									</dc:coverage>
								</xsl:for-each>							
							</xsl:if>

							<xsl:if test="$dc:creator != ''">
								<xsl:for-each select="$dc:creator">
									<dc:creator>
										<xsl:value-of select="text()"/>
									</dc:creator>
								</xsl:for-each>							
							</xsl:if>

							<xsl:if test="$dc:date != ''">
								<xsl:for-each select="$dc:date">
									<dc:date>
										<xsl:value-of select="text()"/>
									</dc:date>
								</xsl:for-each>							
							</xsl:if>

							<xsl:if test="$dc:description != ''">
								<xsl:for-each select="$dc:description">
									<dc:description>
										<xsl:value-of select="text()"/>
									</dc:description>
								</xsl:for-each>							
							</xsl:if>

							<xsl:if test="$dc:format != ''">
								<xsl:for-each select="$dc:format">
									<dc:format>
										<xsl:value-of select="text()"/>
									</dc:format>
								</xsl:for-each>							
							</xsl:if>

							<xsl:if test="$dc:identifier != ''">
								<xsl:for-each select="$dc:identifier">
									<dc:identifier>
										<xsl:value-of select="text()"/>
									</dc:identifier>
								</xsl:for-each>							
							</xsl:if>

							<dc:language>
								<xsl:text>nl</xsl:text>
							</dc:language>
							
							<xsl:if test="$dc:publisher != ''">
								<xsl:for-each select="$dc:publisher">
									<dc:publisher>
										<xsl:value-of select="text()"/>
									</dc:publisher>
								</xsl:for-each>							
							</xsl:if>

							<xsl:if test="$dc:relation != ''">
								<xsl:for-each select="$dc:relation">
									<dc:relation>
										<xsl:value-of select="text()"/>
									</dc:relation>
								</xsl:for-each>							
							</xsl:if>

							<xsl:if test="$dc:rights != ''">
								<xsl:for-each select="$dc:rights">
									<dc:rights>
										<xsl:value-of select="text()"/>
									</dc:rights>
								</xsl:for-each>							
							</xsl:if>

							<xsl:if test="$dc:source != ''">
								<xsl:for-each select="$dc:source">
									<dc:source>
										<xsl:value-of select="text()"/>
									</dc:source>
								</xsl:for-each>							
							</xsl:if>

							<xsl:if test="$dc:subject != ''">
								<xsl:for-each select="$dc:subject">
									<dc:subject>
										<xsl:value-of select="text()"/>
									</dc:subject>
								</xsl:for-each>							
							</xsl:if>

							<xsl:if test="$dc:title != ''">
								<xsl:for-each select="$dc:title">
									<dc:title>
										<xsl:value-of select="text()"/>
									</dc:title>
								</xsl:for-each>							
							</xsl:if>

							<xsl:if test="$dc:type != ''">
								<xsl:for-each select="$dc:type">
									<dc:type>
										<xsl:value-of select="text()"/>
									</dc:type>
								</xsl:for-each>							
							</xsl:if>
							
							<!-- Relation Persons -->
							<xsl:if test="$edm:isRelatedToAgents != ''">
								<xsl:for-each select="$edm:isRelatedToAgents">
									<xsl:variable name="pid" select="./@pid"/>
									<edm:isRelatedTo>
										<xsl:attribute name="rdf:resource">
											<xsl:value-of select="replace($pid,'\s','_')"/>									
										</xsl:attribute>
									</edm:isRelatedTo>
								</xsl:for-each>
							</xsl:if>
							
							<xsl:if test="$edm:isRelatedToConcept">
								<!-- Relation Concept -->
								<edm:isRelatedTo>
									<xsl:attribute name="rdf:resource">
										<xsl:variable name="ConceptAbout" select="$edm:isRelatedToConcept"/>
										<xsl:value-of select="replace($ConceptAbout,'\s','_')"/>							
									</xsl:attribute>
								</edm:isRelatedTo>		
							</xsl:if>
											
							<xsl:if test="$edm:isRelatedToPlace != ''">
								<!-- Relation Place -->
								<edm:isRelatedTo>
									<xsl:attribute name="rdf:resource">
										<xsl:variable name="PlaceAbout" select="$edm:isRelatedToPlace"/>
										<xsl:value-of select="replace($PlaceAbout,'\s','_')"/>							
									</xsl:attribute>
								</edm:isRelatedTo>
							</xsl:if>
							
							<xsl:if test="$edm:isRelatedToTimeSpan != ''">
								<!-- Relation TimeSpan -->
								<edm:isRelatedTo>
									<xsl:attribute name="rdf:resource">
										<xsl:variable name="TimeSpanAbout" select="$edm:isRelatedToTimeSpan"/>
										<xsl:value-of select="replace($TimeSpanAbout,'\s','_')"/>							
									</xsl:attribute>
								</edm:isRelatedTo>
							</xsl:if>
									
							<!-- Relation Aggregation -->
							<xsl:if test="$ore:Aggregation != ''">
								<xsl:for-each select="$ore:Aggregation">
									<edm:isRelatedTo>
										<xsl:attribute name="rdf:resource">
											<xsl:value-of select="replace($ore:Aggregation,'\s','_')"/>							
										</xsl:attribute>
									</edm:isRelatedTo>
								</xsl:for-each>
							</xsl:if>
											
							<edm:type>
								<xsl:text>TEXT</xsl:text>
							</edm:type>

						</edm:ProvidedCHO>
					</xsl:if>

					<!-- Agent -->
					<xsl:for-each select="$edm:isRelatedToAgents">
						<xsl:variable name="pid" select="./@pid"/>
						<edm:Agent>
							<xsl:attribute name="rdf:about">
								<xsl:value-of select="replace($pid,'\s','_')"/>
							</xsl:attribute>
							
							<xsl:if test="./a2a:PersonName/a2a:PersonNameFirstName/text() != ''">
								<skos:prefLabel>
									<xsl:value-of select="./a2a:PersonName/a2a:PersonNameFirstName/text()"/>
								</skos:prefLabel>
							</xsl:if>						
							
							<xsl:if test="./a2a:PersonName/a2a:PersonNamePrefixLastName != '' or ./a2a:PersonName/a2a:PersonNameLastName != ''">
								<xsl:for-each select="./a2a:PersonName/a2a:PersonNamePrefixLastName | ./a2a:PersonName/a2a:PersonNameLastName">
									<skos:altLabel>
										<xsl:value-of select="text()"/>
									</skos:altLabel>
								</xsl:for-each>
							</xsl:if>
	    					
	    					<dc:identifier>
								<xsl:value-of select="$pid"/>
							</dc:identifier>
								
							<xsl:if test="$providedCHOAbout != ''">
								<dcterms:isPartOf>
									<xsl:value-of select="$providedCHOAbout"/>
								</dcterms:isPartOf>
							</xsl:if>
							
						    <xsl:for-each select="../a2a:RelationEP">
						    	<xsl:variable name="PersonKeyRef" select="./a2a:PersonKeyRef"/>					    		
						    	<xsl:if test="$PersonKeyRef = $pid">
								 	<edm:isRelatedTo>
								 		<xsl:value-of select="./a2a:RelationType/text()"/>
								 	</edm:isRelatedTo>
								</xsl:if>	
							</xsl:for-each>						

							<xsl:if test="./a2a:Profession != ''">
								<xsl:for-each select="./a2a:Profession">
									<rdaGr2:professionOrOccupation>
										<xsl:value-of select="text()"/>
									</rdaGr2:professionOrOccupation>
								</xsl:for-each>
							</xsl:if>
						
						</edm:Agent>
					</xsl:for-each>

					<!-- Concept -->
					<xsl:if test="$edm:isRelatedToConcept != ''">
						<skos:Concept>
							<xsl:attribute name="rdf:about">
								<xsl:variable name="ConceptAbout" select="$edm:isRelatedToConcept"/>
								<xsl:value-of select="replace($ConceptAbout,'\s','_')"/>
							</xsl:attribute>

						    <skos:prefLabel>
						    	<xsl:value-of select="$edm:isRelatedToConcept"/>
						    </skos:prefLabel>

						    <xsl:if test="$providedCHOAbout != ''">
								<dcterms:isPartOf>
									<xsl:value-of select="$providedCHOAbout"/>
								</dcterms:isPartOf>
							</xsl:if>							
						</skos:Concept>
					</xsl:if>
					
					<!-- Place -->
					<xsl:if test="$edm:isRelatedToPlace != ''">
						<edm:Place>
							<xsl:attribute name="rdf:about">
								<xsl:variable name="PlaceAbout" select="$edm:isRelatedToPlace"/>
								<xsl:value-of select="replace($PlaceAbout,'\s','_')"/>
							</xsl:attribute>

						    <skos:prefLabel>
						    	<xsl:value-of select="$edm:isRelatedToPlace"/>
						    </skos:prefLabel>

						    <xsl:if test="$providedCHOAbout != ''">
								<dcterms:isPartOf>
									<xsl:value-of select="$providedCHOAbout"/>
								</dcterms:isPartOf>
							</xsl:if>

					 	</edm:Place>
					</xsl:if>
					
					<!-- TimeSpan -->
					<xsl:if test="$edm:isRelatedToTimeSpan != ''">
						<edm:TimeSpan>
					 		<xsl:attribute name="rdf:about">
					 			<xsl:variable name="TimeSpanAbout" select="$edm:isRelatedToTimeSpan"/>
								<xsl:value-of select="replace($TimeSpanAbout,'\s','_')"/>
							</xsl:attribute>

							<edm:begin>
								<xsl:value-of select="./ns1:metadata/a2a:A2A/a2a:Source/a2a:SourceIndexDate/a2a:From"/>
							</edm:begin>

							<edm:end>
								<xsl:value-of select="./ns1:metadata/a2a:A2A/a2a:Source/a2a:SourceIndexDate/a2a:To"/>
							</edm:end>

							<xsl:if test="$providedCHOAbout != ''">
								<dcterms:isPartOf>
									<xsl:value-of select="$providedCHOAbout"/>
								</dcterms:isPartOf>
							</xsl:if>

						</edm:TimeSpan>
					</xsl:if>
				 	
				 	<xsl:if test="$edm:WebResource != ''">
						<xsl:for-each select="$edm:WebResource">
							<edm:WebResource>
						 		<xsl:attribute name="rdf:about">
									<xsl:value-of select="replace(text(),'\s','_')"/>									
								</xsl:attribute>
								<xsl:if test="text() != ''">
									<dcterms:isFormatOf>
										<xsl:value-of select="text()"/>
									</dcterms:isFormatOf>
								</xsl:if>								
							</edm:WebResource>
						</xsl:for-each>
					</xsl:if>
					<xsl:if test="$ore:Aggregation != ''">
						<xsl:for-each select="$ore:Aggregation">
							<ore:Aggregation>
							 	<xsl:attribute name="rdf:about">
									<xsl:value-of select="replace(text(),'\s','_')"/>
								</xsl:attribute>

								<edm:aggregatedCHO>
								 	<xsl:attribute name="rdf:resource">
										<xsl:value-of select="replace(text(),'\s','_')"/>	
									</xsl:attribute>
								 </edm:aggregatedCHO>

								<xsl:if test="../../../a2a:SourceReference/a2a:InstitutionName != ''">
									<edm:dataProvider>
										<xsl:value-of select="../../../a2a:SourceReference/a2a:InstitutionName"/>
									</edm:dataProvider>
								</xsl:if>
							   
							    <edm:isShownAt>
							    	<xsl:attribute name="rdf:resource">
										<xsl:value-of select="replace(text(),'\s','_')"/>	
									</xsl:attribute>
							    </edm:isShownAt>

							    <xsl:if test="../a2a:Uri != ''">
							    	<edm:isShownBy>
							    		<xsl:attribute name="rdf:resource">
											<xsl:value-of select="../a2a:Uri"/>	
										</xsl:attribute>
							    	</edm:isShownBy>
							    </xsl:if>

						    	<edm:object>
						    		<xsl:attribute name="rdf:resource">
										<xsl:value-of select="replace(text(),'\s','_')"/>	
									</xsl:attribute>
						    	</edm:object>	
							    
							    <xsl:if test="../../../a2a:SourceReference/a2a:InstitutionName  != ''">
									<edm:provider>
										<xsl:value-of select="../../../a2a:SourceReference/a2a:InstitutionName"/>
									</edm:provider>
								</xsl:if>

							    <edm:rights>
							    	<xsl:attribute name="rdf:resource">
							    		<xsl:text>http://creativecommons.org/publicdomain/mark/1.0/</xsl:text>
									</xsl:attribute>
							    </edm:rights>
							</ore:Aggregation>

						</xsl:for-each>
						
					</xsl:if>
										
				</rdf:RDF>
			</xsl:result-document>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>
