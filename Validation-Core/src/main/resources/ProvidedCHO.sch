<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" xml:lang="en"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://purl.oclc.org/dsdl/schematron"
            queryBinding="xslt2" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
            version="2.0">

    <sch:ns prefix="edm" uri="http://www.europeana.eu/schemas/edm/"/>
    <sch:ns prefix="dc" uri="http://purl.org/dc/elements/1.1/"/>
    <sch:ns prefix="dcterms" uri="http://purl.org/dc/terms/"/>
    <sch:ns prefix="ore" uri="http://www.openarchives.org/ore/terms/"/>
    <sch:ns prefix="owl" uri="http://www.w3.org/2002/07/owl#"/>
    <sch:ns prefix="rdf" uri="http://www.w3.org/1999/02/22-rdf-syntax-ns#"/>
    <sch:ns prefix="rdfs" uri="http://www.w3.org/2000/01/rdf-schema#"/>
    <sch:ns prefix="skos" uri="http://www.w3.org/2004/02/skos/core#"/>
    <sch:ns prefix="svcs" uri="http://rdfs.org/sioc/services#"/>
    <sch:ns prefix="xs" uri="http://www.w3.org/2001/XMLSchema"/>

    <sch:pattern>
        <sch:rule context="edm:ProvidedCHO/dc:contributor">
            <!-- Reported -->
            <!--<sch:report test="exists(@rdf:resource)"> <sch:name/> @resource '<sch:value-of select="@rdf:resource"/></sch:report>-->
            <!--<sch:report test="normalize-space(text())!=''"> <sch:name/> @textt '<sch:value-of select="text()"/>'</sch:report>-->
            <!--<sch:report test="every $resource in @rdf:resource satisfies exists(../../edm:Agent[@rdf:about = $resource ])"> @resource '<sch:value-of select="@rdf:resource"/>' from <sch:name/> exists/>'</sch:report>-->

            <sch:assert test="(exists(@rdf:resource) or normalize-space(text())!='')" diagnostics="empty" role="warning"></sch:assert>

            <sch:assert test="every $resource in @rdf:resource satisfies exists(../../edm:Agent[@rdf:about = $resource ])" diagnostics="resource" role="warning"></sch:assert>
        </sch:rule>

        <sch:rule context="edm:ProvidedCHO/dc:coverage">
            <sch:assert test="(exists(@rdf:resource) or normalize-space(text())!='')" diagnostics="empty" role="warning"></sch:assert>

            <sch:assert
                    test="every $resource in @rdf:resource satisfies exists((../../edm:TimeSpan[@rdf:about = $resource ],../../edm:Place[@rdf:about = $resource ]))" diagnostics="resource" role="warning"></sch:assert>

        </sch:rule>
        <sch:rule context="edm:ProvidedCHO/dc:creator">
            <sch:assert test="(exists(@rdf:resource) or normalize-space(text())!='')" diagnostics="empty" role="warning"></sch:assert>

            <sch:assert test="every $resource in @rdf:resource satisfies exists(../../edm:Agent[@rdf:about = $resource ])" diagnostics="resource" role="warning"></sch:assert>

        </sch:rule>
        <sch:rule context="edm:ProvidedCHO/dc:date">
            <sch:assert test="(exists(@rdf:resource) or normalize-space(text())!='')" diagnostics="empty" role="warning"></sch:assert>

            <sch:assert test="text() castable as xs:date" diagnostics="castableDate" role="warning"></sch:assert>
        </sch:rule>
    </sch:pattern>


    <sch:diagnostics>
        <sch:diagnostic id="empty">'<sch:value-of select="name()"/>'must have a non empty</sch:diagnostic>
        <sch:diagnostic id="castableDate">'<sch:value-of select="name()"/>' value '<sch:value-of select="text()"/>' elements or attributes should have an xs:date type value.</sch:diagnostic>
        <sch:diagnostic id="resource">@resource '<sch:value-of select="for $resource in @rdf:resource return $resource"/>' from '<sch:value-of select="name()"/>' must have exist in '<sch:value-of select="../..//name()"/>'</sch:diagnostic>
    </sch:diagnostics>

    <!--<sch:let name="contributor" value="dc:contributor"/>-->
    <!--<sch:let name="coverage" value="dc:coverage"/>-->
    <!--<sch:let name="creator" value="dc:creator"/>-->
    <!--<sch:let name="date" value="dc:date"/>-->
    <!--<sch:let name="description" value="dc:description"/>-->
    <!--<sch:let name="format" value="dc:format"/>-->
    <!--<sch:let name="provenance" value="dcterms:provenance"/>-->
    <!--<sch:let name="identifier" value="dc:identifier"/>-->
    <!--<sch:let name="language" value="dc:language"/>-->
    <!--<sch:let name="publisher" value="dc:publisher"/>-->
    <!--<sch:let name="spatial" value="dcterms:spatial"/>-->
    <!--<sch:let name="relation" value="dc:relation"/>-->
    <!--<sch:let name="rights" value="dc:rights"/>-->
    <!--<sch:let name="source" value="dc:source"/>-->
    <!--<sch:let name="temporal" value="dcterms:temporal"/>-->
    <!--<sch:let name="subject" value="dc:subject"/>-->
    <!--<sch:let name="title" value="dc:title"/>-->
    <!--<sch:let name="type" value="dc:type"/>-->
    <!--<sch:let name="alternative" value="dcterms:alternative"/>-->
    <!--<sch:let name="created" value="dcterms:created"/>-->
    <!--<sch:let name="isNextInSequence" value="edm:isNextInSequence"/>-->
    <!--<sch:let name="extent" value="dcterms:extent"/>-->
    <!--<sch:let name="isRelatedTo" value="edm:isRelatedTo"/>-->
    <!--<sch:let name="hasFormat" value="dcterms:hasFormat"/>-->
    <!--<sch:let name="hasPart" value="dcterms:hasPart"/>-->
    <!--<sch:let name="isPartOf" value="dcterms:isPartOf"/>-->
    <!--<sch:let name="isReferencedBy" value="dcterms:isReferencedBy"/>-->
</sch:schema>