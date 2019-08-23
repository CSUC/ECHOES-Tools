<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" xml:lang="en">
   <sch:ns prefix="edm" uri="http://www.europeana.eu/schemas/edm/" />
   <sch:ns prefix="dc" uri="http://purl.org/dc/elements/1.1/" />
   <sch:ns prefix="dct" uri="http://purl.org/dc/terms/" />
   <sch:ns prefix="ore" uri="http://www.openarchives.org/ore/terms/" />
   <sch:ns prefix="owl" uri="http://www.w3.org/2002/07/owl#" />
   <sch:ns prefix="rdf" uri="http://www.w3.org/1999/02/22-rdf-syntax-ns#" />
   <sch:ns prefix="rdfs" uri="http://www.w3.org/2000/01/rdf-schema#" />
   <sch:ns prefix="skos" uri="http://www.w3.org/2004/02/skos/core#" />
   <sch:ns prefix="svcs" uri="http://rdfs.org/sioc/services#" />
   <sch:pattern>
      <sch:rule context="edm:ProvidedCHO">
         <sch:assert test="dc:subject or dc:type or dc:coverage or dct:temporal or dct:spatial">
            id:
            <sch:value-of select="@rdf:about" />
            - A Proxy must have a
				dc:subject or dc:type or dc:coverage or dct:temporal or
				dct:spatial.
         </sch:assert>
         <sch:assert test="((dc:subject and (exists(dc:subject/@rdf:resource) or normalize-space(dc:subject)!='')) or (dc:type and (exists(dc:type/@rdf:resource) or         normalize-space(dc:type)!='')) or (dc:coverage and (exists(dc:coverage/@rdf:resource) or normalize-space(dc:coverage)!='')) or (dct:temporal and          (exists(dct:temporal/@rdf:resource) or normalize-space(dct:temporal)!=''))  or (dct:spatial and (exists(dct:spatial/@rdf:resource) or normalize-space       (dct:spatial)!='')))">A Proxy must have a non empty
				dc:subject or dc:type or dc:coverage or dct:temporal or
				dct:spatial.</sch:assert>
         <!--<sch:assert test="dc:title or dc:description">
								id:
								<sch:value-of select="@rdf:about" />
								- A Proxy must have a dc:title or
								dc:description.
							</sch:assert> -->
         <sch:assert test="(dc:title and normalize-space(dc:title)!='') or (dc:description and (exists(dc:description/@rdf:resource) or normalize-space(dc:description)!=''))">A Proxy must have a non empty dc:title or a non empty
								dc:description</sch:assert>
         <sch:assert test="not(edm:type='TEXT') or (edm:type='TEXT' and exists(dc:language))">
            id:
            <sch:value-of select="@rdf:about" />
            - Within a Proxy
									context, dc:language is mandatory when dc:language has the value
									'TEXT'.
         </sch:assert>
      </sch:rule>
      <sch:rule context="*">
         <sch:assert test="not(@rdf:resource = '')">
            Empty rdf:resource attribute is not allowed for
            <sch:name />
            element.
         </sch:assert>
      </sch:rule>
      <sch:rule context="*">
         <sch:assert test="not(@rdf:about = '')">
            Empty rdf:about attribute is not allowed for
            <sch:name />
            element.
         </sch:assert>
      </sch:rule>
      <sch:rule context="*">
         <sch:assert test="not(@xml:lang = '')">
            Empty xml:lang attribute is not allowed for
            <sch:name />
            element.
         </sch:assert>
      </sch:rule>
      <sch:rule context="*">
         <sch:assert test="not(@rdf:resource and text())">
            Element
            <sch:name />
            should not have both rdf:resource attribute and text value
							populated.
         </sch:assert>
      </sch:rule>
      <sch:rule context="ore:Aggregation">
         <sch:assert test="(edm:isShownAt and exists(edm:isShownAt/@rdf:resource)) or (edm:isShownBy and exists(edm:isShownBy/@rdf:resource))">
            id:
            <sch:value-of select="@rdf:about" />
            An ore:Aggregation must have either
								edm:isShownAt or edm:isShownBy
         </sch:assert>
      </sch:rule>
      <sch:rule context="ore:Aggregation">
         <sch:assert test="edm:dataProvider">
            id:
            <sch:value-of select="@rdf:about" />
            An ore:Aggregation must have at least one instance of
								edm:dataProvider
         </sch:assert>
      </sch:rule>
      <sch:rule context="ore:Aggregation">
         <sch:assert test="edm:provider">
            id:
            <sch:value-of select="@rdf:about" />
            An ore:Aggregation must have at least one instance of
								edm:provider
         </sch:assert>
      </sch:rule>
      <sch:rule context="ore:Aggregation">
         <sch:assert test="edm:rights and exists(edm:rights/@rdf:resource)">
            id:
            <sch:value-of select="@rdf:about" />
            An ore:Aggregation must have at least one instance of
								edm:rights
         </sch:assert>
      </sch:rule>
      <sch:rule context="ore:Aggregation/edm:provider">
         <sch:assert test="exists(./@rdf:resource) or normalize-space(.)!=''">An ore:Aggregation must have a non
								empty edm:provider</sch:assert>
      </sch:rule>
      <sch:rule context="ore:Aggregation/edm:dataProvider">
         <sch:assert test="exists(./@rdf:resource) or normalize-space(.)!=''">An ore:Aggregation must have a non
								empty edm:dataProvider</sch:assert>
      </sch:rule>
      <sch:rule context="ore:Proxy">
         <sch:assert test="dc:subject or dc:type or dc:coverage or dct:temporal or dct:spatial">
            id:
            <sch:value-of select="@rdf:about" />
            - A Proxy must have a
								dc:subject or dc:type or dc:coverage or dct:temporal or
								dct:spatial.
         </sch:assert>
         <sch:assert test="((dc:subject and (exists(dc:subject/@rdf:resource) or normalize-space(dc:subject)!='')) or (dc:type and (exists(dc:type/@rdf:resource) or         normalize-space(dc:type)!='')) or (dc:coverage and (exists(dc:coverage/@rdf:resource) or normalize-space(dc:coverage)!='')) or (dct:temporal and          (exists(dct:temporal/@rdf:resource) or normalize-space(dct:temporal)!=''))  or (dct:spatial and (exists(dct:spatial/@rdf:resource) or normalize-space       (dct:spatial)!='')))">A Proxy must have a non empty
								dc:subject or dc:type or dc:coverage or dct:temporal or
								dct:spatial.</sch:assert>
         <!--<sch:assert test="dc:title or dc:description">
								id:
								<sch:value-of select="@rdf:about" />
								- A Proxy must have a dc:title or
								dc:description.
							</sch:assert> -->
         <sch:assert test="(dc:title and normalize-space(dc:title)!='') or (dc:description and (exists(dc:description/@rdf:resource) or normalize-space(dc:description)!=''))">A Proxy must have a non empty dc:title or a non empty
								dc:description</sch:assert>
         <sch:assert test="not(edm:type='TEXT') or (edm:type='TEXT' and exists(dc:language))">
            id:
            <sch:value-of select="@rdf:about" />
            - Within a Proxy
								context, dc:language is mandatory when dc:language has the value
								'TEXT'.
         </sch:assert>
         <sch:assert test="edm:type or (not(edm:type) and edm:europeanaProxy)">
            id:
            <sch:value-of select="@rdf:about" />
            edm:type should be present in an ore:Proxy context.
         </sch:assert>
         <sch:assert test="not(edm:type and edm:europeanaProxy)">
            id:
            <sch:value-of select="@rdf:about" />
            edm:type should not be present in an Europeana Proxy context
								(when the edm:europeanaProxy value is present).
         </sch:assert>
      </sch:rule>
      <sch:rule context="edm:EuropeanaAggregation">
         <sch:assert test="not(@rdf:about = '')">Empty rdf:about values are not allowed</sch:assert>
      </sch:rule>
      <sch:rule context="edm:WebResource">
         <sch:assert test="not(dct:hasPart[text()])">
            The element dcterms:isPartOf should not have a literal value in the edm:WebResource context
              with id:
            <sch:value-of select="@rdf:about" />
            . Use an rdf:resource instead.
         </sch:assert>
      </sch:rule>
   </sch:pattern>
</sch:schema>