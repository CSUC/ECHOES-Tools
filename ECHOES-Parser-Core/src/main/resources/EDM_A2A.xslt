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
	<xsl:param name="dir" />
	<xsl:param name="folder" />
	<xsl:param name="collectionSet" />
	

	<xsl:template match="@*|node()">
		<xsl:for-each select="/ns1:OAI-PMH/ns1:ListRecords/ns1:record">			
			<xsl:result-document method="xml" href="{$folder}/{$collectionSet}/{$dir}/{$collectionSet}_file{position()}.xml">
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

					<!--<xsl:for-each select="/oai:OAI-PMH/oai:ListRecords/oai:record">-->
					<edm:ProvidedCHO>
						<xsl:attribute name="rdf:about">
							<xsl:variable name="providedCHOAbout" select="./ns1:header/ns1:identifier/text()"/>
							<xsl:value-of select="replace($providedCHOAbout,'\s','_')"/>
						</xsl:attribute>

						<xsl:if test="./ns1:metadata/oai_dc:dc/dc:contributor != ''">
							<xsl:for-each select="./ns1:metadata/oai_dc:dc/dc:contributor">
								<dc:contributor>
									<xsl:value-of select="text()"/>
								</dc:contributor>
							</xsl:for-each>							
						</xsl:if>

						<xsl:if test="./ns1:metadata/a2a:A2A/a2a:Source/a2a:SourcePlace/a2a:Place != '' ">
							<xsl:for-each select="./ns1:metadata/a2a:A2A/a2a:Source/a2a:SourcePlace/a2a:Place">
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

						<xsl:if test="./ns1:metadata/a2a:A2A/a2a:Source/a2a:SourceReference/a2a:Collection != ''">
							<xsl:for-each select="./ns1:metadata/a2a:A2A/a2a:Source/a2a:SourceReference/a2a:Collection">
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

						<!--<xsl:if test="./ns1:metadata/a2a:A2A/a2a:Source/a2a:SourceReference/a2a:Book != ''">
							<xsl:for-each select="./ns1:metadata/a2a:A2A/a2a:Source/a2a:SourceReference/a2a:Book">-->
								<dc:language>
									<xsl:text>nl</xsl:text>
									<!--<xsl:value-of select="text()"/>-->
								</dc:language>
							<!--</xsl:for-each>							
						</xsl:if>-->

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

						<xsl:if test="./ns1:metadata/a2a:A2A/a2a:Source/a2a:SourceReference/a2a:Book != ''">
							<xsl:for-each select="./ns1:metadata/a2a:A2A/a2a:Source/a2a:SourceReference/a2a:Book">
								<dc:title>
									<xsl:value-of select="text()"/>
								</dc:title>
							</xsl:for-each>							
						</xsl:if>

						<xsl:if test="./ns1:metadata/a2a:A2A/a2a:Source/a2a:SourceType != ''">
							<xsl:for-each select="./ns1:metadata/a2a:A2A/a2a:Source/a2a:SourceType">
								<dc:type>
									<xsl:value-of select="text()"/>
									<!--<xsl:text>TEXT</xsl:text>-->
								</dc:type>
							</xsl:for-each>							
						</xsl:if>

						<edm:type>TEXT</edm:type>

					</edm:ProvidedCHO>
					<!--</xsl:for-each>-->

					<xsl:for-each select="./ns1:metadata/a2a:A2A/a2a:Person">
						<xsl:variable name="pid" select="./@pid"/>
						<edm:Agent>
							<xsl:attribute name="rdf:about">
								<xsl:value-of select="replace($pid,'\s','_')"/>
								<!--<xsl:value-of select="$pid"/>-->
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
	    					
	    					<!--<xsl:if test="./a2a:PersonName/a2a:PersonNameFirstName != ''">
								<xsl:for-each select="./a2a:PersonName/a2a:PersonNameFirstName">-->
									<dc:identifier>
										<xsl:value-of select="$pid"/>
									</dc:identifier>
								<!--</xsl:for-each>
							</xsl:if>-->
						    	
						    <xsl:for-each select="../a2a:RelationEP">
						    	<xsl:variable name="PersonKeyRef" select="./a2a:PersonKeyRef"/>					    		
						    	<xsl:if test="$PersonKeyRef = $pid">
								 	<edm:isRelatedTo>
								 		<xsl:value-of select="./a2a:RelationType/text()"/>
								 	</edm:isRelatedTo>
								</xsl:if>	
							</xsl:for-each>						
							<!--</xsl:if>-->
							<xsl:if test="./a2a:Profession != ''">
								<xsl:for-each select="./a2a:Profession">
									<rdaGr2:professionOrOccupation>
										<xsl:value-of select="text()"/>
									</rdaGr2:professionOrOccupation>
								</xsl:for-each>
							</xsl:if>
						
						</edm:Agent>
					</xsl:for-each>

					<skos:Concept>
						<xsl:attribute name="rdf:about">
							<xsl:variable name="ConceptAbout" select="./ns1:metadata/a2a:A2A/a2a:Source/a2a:SourceType/text()"/>
							<xsl:value-of select="replace($ConceptAbout,'\s','_')"/>
							<!--<xsl:value-of select="./ns1:metadata/a2a:A2A/a2a:Source/a2a:SourceType/text()"/>-->
						</xsl:attribute>

						<xsl:if test="./ns1:metadata/a2a:A2A/a2a:Source/a2a:SourceType != ''">
					    	<skos:prefLabel>
					    		<xsl:value-of select="./ns1:metadata/a2a:A2A/a2a:Source/a2a:SourceType/text()"/>
					    	</skos:prefLabel>
					    </xsl:if>
					</skos:Concept>

					<edm:Place>
						<xsl:attribute name="rdf:about">
							<xsl:variable name="PlaceAbout" select="./ns1:metadata/a2a:A2A/a2a:Source/a2a:SourcePlace/a2a:Place/text()"/>
							<xsl:value-of select="replace($PlaceAbout,'\s','_')"/>
							<!--<xsl:value-of select="./ns1:metadata/a2a:A2A/a2a:Source/a2a:SourcePlace/a2a:Place/text()"/>-->
						</xsl:attribute>

				    	<xsl:if test="./ns1:metadata/a2a:A2A/a2a:Source/a2a:SourcePlace/a2a:Place != ''">
					    	<skos:prefLabel>
					    		<xsl:value-of select="./ns1:metadata/a2a:A2A/a2a:Source/a2a:SourcePlace/a2a:Place/text()"/>
					    	</skos:prefLabel>
					    </xsl:if>
				 	</edm:Place>

				 	<edm:TimeSpan>
				 		<xsl:attribute name="rdf:about">
				 			<xsl:variable name="TimeSpanAbout" select="./ns1:metadata/a2a:A2A/a2a:Source/a2a:SourceIndexDate/a2a:From/text()"/>
							<xsl:value-of select="replace($TimeSpanAbout,'\s','_')"/>
							<!--<xsl:value-of select="./ns1:metadata/a2a:A2A/a2a:Source/a2a:SourceIndexDate/a2a:From/text()"/>-->
						</xsl:attribute>

						<xsl:if test="./ns1:metadata/a2a:A2A/a2a:Source/a2a:SourceIndexDate/a2a:From != ''">
							<edm:begin>
								<xsl:value-of select="./ns1:metadata/a2a:A2A/a2a:Source/a2a:SourceIndexDate/a2a:From"/>
							</edm:begin>
						</xsl:if>

						<xsl:if test="./ns1:metadata/a2a:A2A/a2a:Source/a2a:SourceIndexDate/a2a:To != ''">
							<edm:end>
								<xsl:value-of select="./ns1:metadata/a2a:A2A/a2a:Source/a2a:SourceIndexDate/a2a:To"/>
							</edm:end>
						</xsl:if>
					    					    
					</edm:TimeSpan>

					<xsl:if test="./ns1:metadata/a2a:A2A/a2a:Source/a2a:SourceAvailableScans/a2a:Scan/a2a:Uri != ''">
						<xsl:for-each select="./ns1:metadata/a2a:A2A/a2a:Source/a2a:SourceAvailableScans/a2a:Scan/a2a:Uri">
							<edm:WebResource>
						 		<xsl:attribute name="rdf:about">
						 			<!--<xsl:variable name="WebResource" select="./ns1:metadata/a2a:A2A/a2a:Source/a2a:SourceAvailableScans/a2a:Scan/a2a:Uri/text()"/>-->
									<xsl:value-of select="replace(text(),'\s','_')"/>									
									<!--<xsl:value-of select="text()"/>-->
								</xsl:attribute>
								<xsl:if test="text() != ''">
									<dcterms:isFormatOf>
										<xsl:value-of select="text()"/>
									</dcterms:isFormatOf>
								</xsl:if>								
							</edm:WebResource>
						</xsl:for-each>
					</xsl:if>
					
					<xsl:if test="./ns1:metadata/a2a:A2A/a2a:Source/a2a:SourceAvailableScans/a2a:Scan/a2a:UriViewer != ''">
						<xsl:for-each select="./ns1:metadata/a2a:A2A/a2a:Source/a2a:SourceAvailableScans/a2a:Scan/a2a:UriViewer">
							<ore:Aggregation>
							 	<xsl:attribute name="rdf:about">
							 		<!--<xsl:variable name="AggregationAbout" select="./a2a:Scan/a2a:UriViewer/text()"/>-->
									<xsl:value-of select="replace(text(),'\s','_')"/>
									<!--<xsl:value-of select="./a2a:Scan/a2a:UriViewer/text()"/>-->
								</xsl:attribute>

								<xsl:if test="./a2a:Scan/a2a:UriViewer != ''">
									<edm:aggregatedCHO>
									 	<xsl:attribute name="rdf:resource">
											<xsl:value-of select="./a2a:Scan/a2a:UriViewer"/>	
										</xsl:attribute>
									 </edm:aggregatedCHO>
								</xsl:if>

								<xsl:if test="../a2a:SourceReference/a2a:InstitutionName != ''">
									<edm:dataProvider>
										<xsl:value-of select="../a2a:SourceReference/a2a:InstitutionName"/>
									</edm:dataProvider>
								</xsl:if>
							   
							    <xsl:if test="./a2a:Scan/a2a:UriViewer != ''">
							    	<edm:isShownAt>
							    		<xsl:attribute name="rdf:resource">
											<xsl:value-of select="./a2a:Scan/a2a:UriViewer"/>	
										</xsl:attribute>
							    	</edm:isShownAt>
							    </xsl:if>

							    <xsl:if test="./a2a:Scan/a2a:Uri != ''">
							    	<edm:isShownBy>
							    		<xsl:attribute name="rdf:resource">
											<xsl:value-of select="./a2a:Scan/a2a:Uri"/>	
										</xsl:attribute>
							    	</edm:isShownBy>
							    </xsl:if>

							    <xsl:if test="./a2a:Scan/a2a:UriPreview != ''">
							    	<edm:object>
							    		<xsl:attribute name="rdf:resource">
											<xsl:value-of select="./a2a:Scan/a2a:UriPreview"/>	
										</xsl:attribute>
							    	</edm:object>	
							    </xsl:if>
							    
							    <xsl:if test="../a2a:SourceReference/a2a:InstitutionName  != ''">
									<edm:provider>
										<xsl:value-of select="../a2a:SourceReference/a2a:InstitutionName"/>
									</edm:provider>
								</xsl:if>

							    <!--<xsl:if test=" != ''">-->
							    <edm:rights>
							    	<xsl:attribute name="rdf:resource">
							    		<xsl:text>http://creativecommons.org/publicdomain/mark/1.0/</xsl:text>
									</xsl:attribute>
							    </edm:rights>
							    <!--</xsl:if>-->

							</ore:Aggregation>

						</xsl:for-each>
						
					</xsl:if>
					
				</rdf:RDF>
			</xsl:result-document>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>
