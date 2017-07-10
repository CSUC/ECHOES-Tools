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

					<xsl:variable name="identifier_status" select="./ns1:about/mmm:memorix/ns1:status/text()"/>
					<xsl:variable name="identifier_object" select="./ns1:about/mmm:memorix/ns1:adressen[1]/ns1:object/text()"/>
					<xsl:variable name="concat_identifier" select="concat(replace($identifier_status,'\s','_'),':',replace($identifier_object,'\s','_'))"/>
						
<!-- 				<xsl:variable name="dc:contributor" select=""/> -->
					<xsl:variable name="dc:coverage" select="./ns1:about/mmm:memorix/contribution"/>
					<xsl:variable name="dc:creator" select="./ns1:about/mmm:memorix/ns1:ontwerper"/>
					<xsl:variable name="dc:date" select="./ns1:about/mmm:memorix/ns1:besluit_bw_datum"/>
					<xsl:variable name="dc:description" select="./ns1:about/mmm:memorix/ns1:redengevende_omschrijving"/>
					<xsl:variable name="dc:description_2" select="./ns1:metadata/oai_dc:dc/dc:description"/>
<!-- 				<xsl:variable name="dc:format" select="./ns1:about/mmm:memorix/contribution"/> -->
					<xsl:variable name="dc:identifier" select="./ns1:about/mmm:memorix/ns1:monumentnummer"/>
<!-- 				<xsl:variable name="dc:publisher" select=""/> -->
<!-- 				<xsl:variable name="dc:relation" select=""/> -->
<!-- 				<xsl:variable name="dc:rights" select=""/> -->
<!-- 				<xsl:variable name="dc:source" select=""/> -->
					<xsl:variable name="dc:subject" select="$identifier_status"/>
					<xsl:variable name="dc:title" select="$concat_identifier"/>
					<xsl:variable name="dc:type" select="./ns1:about/mmm:memorix/ns1:bouwstijl"/>
						
					<xsl:variable name="adressen" select="./ns1:about/mmm:memorix/ns1:adressen"/>
					<xsl:variable name="stadsdeel" select="distinct-values(./ns1:about/mmm:memorix/ns1:adressen/ns1:stadsdeel)"/>
					<xsl:variable name="buurt" select="distinct-values(./ns1:about/mmm:memorix/ns1:adressen/ns1:buurt)"/>
					<xsl:variable name="plaats" select="distinct-values(./ns1:about/mmm:memorix/ns1:adressen/ns1:plaats)"/>	
						
					<edm:ProvidedCHO>
						<xsl:attribute name="rdf:about">
							<xsl:value-of select="$concat_identifier"/>
						</xsl:attribute>
						
						<!-- dc:contributor -->
<!-- 						<xsl:if test="$dc:contributor != ''"> -->
<!-- 							<xsl:for-each select="$dc:contributor"> -->
<!-- 								<dc:contributor> -->
<!-- 									<xsl:value-of select="text()"/> -->
<!-- 								</dc:contributor> -->
<!-- 							</xsl:for-each>							 -->
<!-- 						</xsl:if> -->
						
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
						

						<!-- dc:format -->
<!-- 						<xsl:if test="$dc:format != ''"> -->
<!-- 							<xsl:for-each select="$dc:format"> -->
<!-- 								<dc:format> -->
<!-- 									<xsl:value-of select="text()"/> -->
<!-- 								</dc:format> -->
<!-- 							</xsl:for-each>							 -->
<!-- 						</xsl:if> -->

						<!-- dc:identifier -->
						<xsl:if test="$dc:identifier != ''">
							<xsl:for-each select="$dc:identifier">
								<dc:identifier>
									<xsl:value-of select="text()"/>
								</dc:identifier>
							</xsl:for-each>							
						</xsl:if>

						<!-- dc:language -->
						<!--<xsl:if test=" != ''">
							<xsl:for-each select="">-->
								<dc:language>
									<!--<xsl:value-of select="text()"/>-->
									<xsl:text>nl</xsl:text>									
								</dc:language>
							<!--</xsl:for-each>							
						</xsl:if>-->

						<!-- dc:publisher -->
<!-- 						<xsl:if test="$dc:publisher != ''"> -->
<!-- 							<xsl:for-each select="$dc:publisher"> -->
<!-- 								<dc:publisher> -->
<!-- 									<xsl:value-of select="text()"/> -->
<!-- 								</dc:publisher> -->
<!-- 							</xsl:for-each>							 -->
<!-- 						</xsl:if> -->

						<!-- dc:relation -->
<!-- 						<xsl:if test="$dc:relation != ''"> -->
<!-- 							<xsl:for-each select="$dc:relation"> -->
<!-- 								<dc:relation> -->
<!-- 									<xsl:value-of select="text()"/> -->
<!-- 								</dc:relation> -->
<!-- 							</xsl:for-each>							 -->
<!-- 						</xsl:if> -->

						<!-- dc:rights -->
<!-- 						<xsl:if test="$dc:rights != ''"> -->
<!-- 							<xsl:for-each select="$dc:rights"> -->
<!-- 								<dc:rights> -->
<!-- 									<xsl:value-of select="text()"/> -->
<!-- 								</dc:rights> -->
<!-- 							</xsl:for-each>							 -->
<!-- 						</xsl:if> -->

						<!-- dc:source -->
<!-- 						<xsl:if test="$dc:source != ''"> -->
<!-- 							<xsl:for-each select="$dc:source"> -->
<!-- 								<dc:source> -->
<!-- 									<xsl:value-of select="text()"/> -->
<!-- 								</dc:source> -->
<!-- 							</xsl:for-each>							 -->
<!-- 						</xsl:if> -->
	
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
						
						<xsl:if test="./ns1:about/mmm:memorix/ns1:bouwperiode != ''">
							<edm:isRelatedTo>
								<xsl:attribute name="rdf:resource">
									<xsl:variable name="TimeSpanAbout" select="./ns1:about/mmm:memorix/ns1:bouwperiode/text()"/>
									<xsl:value-of select="replace($TimeSpanAbout,'\s','_')"/>
								</xsl:attribute>
							</edm:isRelatedTo>
						</xsl:if>
						
						<xsl:if test="$stadsdeel != ''">
							<xsl:for-each select="$stadsdeel">
								<edm:isRelatedTo>
									<xsl:attribute name="rdf:resource">
										<xsl:value-of select="replace(.,'\s','_')"/>							
									</xsl:attribute>												
								</edm:isRelatedTo>						
							</xsl:for-each>
						</xsl:if>
						
						<xsl:if test="$buurt != ''">
							<xsl:for-each select="$buurt">
								<edm:isRelatedTo>
									<xsl:attribute name="rdf:resource">
										<xsl:value-of select="replace(.,'\s','_')"/>							
									</xsl:attribute>												
								</edm:isRelatedTo>						
							</xsl:for-each>
						</xsl:if>
						
						<xsl:if test="$plaats != ''">
							<xsl:for-each select="$plaats">
								<edm:isRelatedTo>
									<xsl:attribute name="rdf:resource">
										<xsl:value-of select="replace(.,'\s','_')"/>							
									</xsl:attribute>												
								</edm:isRelatedTo>						
							</xsl:for-each>
						</xsl:if>
						
						<xsl:for-each select="$adressen">
							<xsl:if test="$adressen != ''">								
								<xsl:if test="./ns1:verblijfseenheid_id != ''">
									<edm:isRelatedTo>
										<xsl:attribute name="rdf:resource">
											<xsl:variable name="verblijfseenheid_id" select="./ns1:verblijfseenheid_id/text()"/>										
											<xsl:value-of select="replace($verblijfseenheid_id,'\s','_')"/>							
										</xsl:attribute>
								</edm:isRelatedTo>
								</xsl:if>								
							</xsl:if>							
						</xsl:for-each>
						
						<edm:type>IMAGE</edm:type>
						
					</edm:ProvidedCHO>
					
					<xsl:if test="./ns1:about/mmm:memorix/ns1:bouwperiode != ''">
						<edm:TimeSpan>
							<xsl:attribute name="rdf:about">
				 				<xsl:variable name="TimeSpanAbout" select="./ns1:about/mmm:memorix/ns1:bouwperiode/text()"/>
								<xsl:value-of select="replace($TimeSpanAbout,'\s','_')"/>								
							</xsl:attribute>
						</edm:TimeSpan>
					</xsl:if>
															
					<xsl:if test="$stadsdeel != ''">
						<xsl:for-each select="$stadsdeel">
							<edm:Place>
								<xsl:attribute name="rdf:about">
									<xsl:value-of select="replace(.,'\s','_')"/>							
								</xsl:attribute>		
								
								<skos:prefLabel>
					    			<xsl:value-of select="."/>
					    		</skos:prefLabel>
					    		
					    		<xsl:if test="$buurt != ''">
									<xsl:for-each select="$buurt">
										<dcterms:hasPart>
											<xsl:attribute name="rdf:resource">
												<xsl:value-of select="replace(.,'\s','_')"/>							
											</xsl:attribute>											
										</dcterms:hasPart>						
									</xsl:for-each>
								</xsl:if>
									
								<xsl:if test="$plaats != ''">
									<xsl:for-each select="$plaats">
										<dcterms:hasPart>
											<xsl:attribute name="rdf:resource">
												<xsl:value-of select="replace(.,'\s','_')"/>							
											</xsl:attribute>											
										</dcterms:hasPart>				
									</xsl:for-each>
								</xsl:if>
					    			
					    		<xsl:if test="$concat_identifier != ''">
									<dcterms:isPartOf>
										<xsl:attribute name="rdf:resource">
											<xsl:value-of select="$concat_identifier"/>							
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
									<xsl:value-of select="replace(.,'\s','_')"/>							
								</xsl:attribute>
								
								<skos:prefLabel>
					    			<xsl:value-of select="."/>
					    		</skos:prefLabel>
								
								<xsl:if test="$plaats != ''">
									<xsl:for-each select="$plaats">
										<dcterms:hasPart>
											<xsl:attribute name="rdf:resource">
												<xsl:value-of select="replace(.,'\s','_')"/>							
											</xsl:attribute>											
										</dcterms:hasPart>				
									</xsl:for-each>
								</xsl:if>
								
								<xsl:if test="$stadsdeel != ''">
									<xsl:for-each select="$stadsdeel">
										<dcterms:isPartOf>
											<xsl:attribute name="rdf:resource">
												<xsl:value-of select="replace(.,'\s','_')"/>							
											</xsl:attribute>											
										</dcterms:isPartOf>				
									</xsl:for-each>
								</xsl:if>
								
								<xsl:if test="$concat_identifier != ''">
									<dcterms:isPartOf>
										<xsl:attribute name="rdf:resource">
											<xsl:value-of select="$concat_identifier"/>							
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
									<xsl:value-of select="replace(.,'\s','_')"/>							
								</xsl:attribute>
								
								<skos:prefLabel>
					    			<xsl:value-of select="."/>
					    		</skos:prefLabel>
					    		
					    		<xsl:if test="$stadsdeel != ''">
									<xsl:for-each select="$stadsdeel">
										<dcterms:isPartOf>
											<xsl:attribute name="rdf:resource">
												<xsl:value-of select="replace(.,'\s','_')"/>							
											</xsl:attribute>											
										</dcterms:isPartOf>				
									</xsl:for-each>
								</xsl:if>
								
								<xsl:if test="$concat_identifier != ''">
									<dcterms:isPartOf>
										<xsl:attribute name="rdf:resource">
											<xsl:value-of select="$concat_identifier"/>							
										</xsl:attribute>															
									</dcterms:isPartOf>		
								</xsl:if>
								
								<xsl:if test="$buurt != ''">
									<xsl:for-each select="$buurt">
										<dcterms:isPartOf>
											<xsl:attribute name="rdf:resource">
												<xsl:value-of select="replace(.,'\s','_')"/>							
											</xsl:attribute>											
										</dcterms:isPartOf>				
									</xsl:for-each>
								</xsl:if>
								
							</edm:Place>						
						</xsl:for-each>
					</xsl:if>
					
					<xsl:for-each select="$adressen">
							<xsl:if test="$adressen != ''">
								<xsl:variable name="verblijfseenheid_id" select="./ns1:verblijfseenheid_id/text()"/>
								<xsl:if test="$verblijfseenheid_id != ''">
									<edm:Place>
										<xsl:attribute name="rdf:about">										
	 										<xsl:value-of select="replace($verblijfseenheid_id,'\s','_')"/>								
										</xsl:attribute>
										
										<xsl:variable name="postcode" select="./ns1:postcode"/>
										<xsl:variable name="huidigefunctie" select="./ns1:huidigefunctie"/>
										<xsl:variable name="straat" select="./ns1:straat"/>	
										<xsl:variable name="huisnummer" select="./ns1:huisnummer"/>	
										
										<xsl:variable name="gemeente_y" select="./ns1:gemeente_y"/>	
										<xsl:variable name="gemeente_x" select="./ns1:gemeente_x"/>	
										
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
										
										<xsl:if test="$concat_identifier != ''">
											<dcterms:isPartOf>
												<xsl:attribute name="rdf:resource">
													<xsl:value-of select="$concat_identifier"/>							
												</xsl:attribute>															
											</dcterms:isPartOf>		
										</xsl:if>
										
										<xsl:if test="$stadsdeel != ''">
											<xsl:for-each select="$stadsdeel">
												<dcterms:isPartOf>
													<xsl:attribute name="rdf:resource">
														<xsl:value-of select="replace(.,'\s','_')"/>							
													</xsl:attribute>															
												</dcterms:isPartOf>						
											</xsl:for-each>
										</xsl:if>
										
										<xsl:if test="$buurt != ''">
											<xsl:for-each select="$buurt">
												<dcterms:isPartOf>
													<xsl:attribute name="rdf:resource">
														<xsl:value-of select="replace(.,'\s','_')"/>							
													</xsl:attribute>												
												</dcterms:isPartOf>						
											</xsl:for-each>
										</xsl:if>
											
										<xsl:if test="$plaats != ''">
											<xsl:for-each select="$plaats">
												<dcterms:isPartOf>
													<xsl:attribute name="rdf:resource">
														<xsl:value-of select="replace(.,'\s','_')"/>							
													</xsl:attribute>												
												</dcterms:isPartOf>						
											</xsl:for-each>
										</xsl:if>									
									</edm:Place>
								</xsl:if>
							</xsl:if>							
						</xsl:for-each>
				
				</rdf:RDF>
			</xsl:result-document>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>