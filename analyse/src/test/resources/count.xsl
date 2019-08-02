<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema" version="2.0">

    <xsl:output method="xml" indent="yes"/>
    <xsl:template match="/">
        <xsl:element name="result">
            <xsl:element name="node_result">
                <xsl:for-each-group select="//*" group-by="name()">
                    <xsl:element name="node">
                        <xsl:attribute name="name">
                            <xsl:value-of select="current-grouping-key()"/>
                        </xsl:attribute>
                        <xsl:attribute name="count">
                            <xsl:value-of select="count(current-group())"/>
                        </xsl:attribute>
                        <xsl:attribute name="path">
                            <xsl:for-each select="ancestor-or-self::*">
                                <xsl:call-template name="print-step"/>
                            </xsl:for-each>
                            <!--<xsl:text>/text()</xsl:text>-->
                        </xsl:attribute>
                    </xsl:element>
                </xsl:for-each-group>
            </xsl:element>

            <xsl:element name="namespace_result">
                <xsl:call-template name="namespace"/>
            </xsl:element>
        </xsl:element>
    </xsl:template>

    <xsl:template name="namespace">
      <xsl:for-each select="//namespace::*[not(. = ../../namespace::*)]">
          <xsl:element name="namespaces">
              <xsl:attribute name="uri" select="."/>
              <xsl:attribute name="prefix" select="local-name(.)"/>
          </xsl:element>
    </xsl:for-each>
    </xsl:template>

    <xsl:template name="print-step">
        <xsl:text>/</xsl:text>
        <xsl:value-of select="name()"/>
        <xsl:variable name="attribute" select="@*"/>
        <xsl:if test="$attribute !=''">
            <xsl:text>[</xsl:text>
            <!--<xsl:value-of separator=" and " select="$attribute/local-name()"/>-->
            <xsl:variable name="local" select="$attribute/local-name()" />
            <xsl:for-each select="$local">
                <xsl:choose>
                    <xsl:when test="position() != last()">
                        <xsl:value-of select="concat('@', ., ' and ')" />
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="concat('@', .)" />
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:for-each>
            <xsl:text>]</xsl:text>
        </xsl:if>
    </xsl:template>
</xsl:stylesheet>