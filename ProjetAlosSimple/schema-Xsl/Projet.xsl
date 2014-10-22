<?xml version="1.0" encoding="utf-8" ?>
     <xsl:stylesheet version="1.0" 
	 xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	 xmlns:fo="http://www.w3.org/1999/XSL/Format">
     <xsl:output method="xml"  encoding="utf-8" />

    <xsl:template match="/">
<fo:root
	 
	>
  <fo:layout-master-set>
		<fo:simple-page-master master-name="Projet"
                  page-height="29.7cm"
                  page-width="21cm"
                  margin-top="1cm"
                  margin-bottom="2cm"
                  margin-left="2.5cm"
                  margin-right="2.5cm">
      <fo:region-body margin-top="3cm"/>
      <fo:region-after extent="1.5cm"/>
    </fo:simple-page-master>
  </fo:layout-master-set>
  <fo:page-sequence master-reference="Projet">
	
	<fo:flow flow-name="xsl-region-body">
	<!-- Titre -->
		<fo:block font-size="18pt"
            font-family="sans-serif"
            line-height="24pt"
			font-weight="bold"
            space-after.optimum="15pt"
            text-align="center"
            padding-top="3pt">
		 <xsl:value-of select="*[name()='article']/*[name()='titre']" />
      </fo:block>
					<!-- Resume-->
	  <fo:table>
		  <fo:table-column column-width="20mm"/>
		  <fo:table-column column-width="50mm"/>

		  <fo:table-body>
			<fo:table-row>
			  <fo:table-cell>
				<fo:block font-size="11pt"
				font-family="sans-serif"
				font-weight="bold"
				line-height="24pt"
				space-after.optimum="15pt"
				text-align="left"
				padding-top="2pt">Résume:</fo:block>
			  </fo:table-cell>
			  <fo:table-cell>
				<fo:block font-size="11pt"
				font-family="sans-serif"
				line-height="24pt"
				space-after.optimum="15pt"
				text-align="left"
				padding-top="2pt"><xsl:value-of select="*[name()='article']/*[name()='resume']" /></fo:block>
			  </fo:table-cell>
			</fo:table-row>
			
						<fo:table-row>
			  <fo:table-cell>
				<fo:block font-size="11pt"
				font-family="sans-serif"
				font-weight="bold"
				line-height="24pt"
				space-after.optimum="15pt"
				text-align="left"
				padding-top="2pt">Mots Clés:</fo:block>
			  </fo:table-cell>
			  <fo:table-cell>
				<fo:block font-size="11pt"
				font-family="sans-serif"
				line-height="24pt"
				space-after.optimum="15pt"
				text-align="left"
				padding-top="2pt">
				
				<xsl:for-each select="*[name()='article']/*[name()='motcles']/*[name()='motcle']">
				 
				 <xsl:value-of select="."/>    								
					<xsl:choose>
							<xsl:when test="position() != last()">,
							</xsl:when>
					</xsl:choose>
				 </xsl:for-each>
				
				</fo:block>
			  </fo:table-cell>
			</fo:table-row>
		  </fo:table-body>
		</fo:table>

	  
		<!-- Les mots clés -->
	  
	  <xsl:for-each select="*[name()='article']/*[name()='section']">
		<xsl:if test="@titre='introduction'">
			<fo:block font-size="12pt"
				font-family="sans-serif"
				line-height="24pt"
				font-weight="bold"
				space-after.optimum="15pt"
				text-align="left"
				padding-top="6pt">
				1	Introduction
			</fo:block>
			<fo:block font-size="12pt"
				font-family="sans-serif"
				line-height="24pt"
				space-after.optimum="15pt"
				text-align="left"
				padding-top="3pt">
					<xsl:value-of select="." />
			</fo:block>
		</xsl:if>
		
		<xsl:if test="@titre='corps'">
			<fo:block font-size="12pt"
				font-family="sans-serif"
				line-height="24pt"
				font-weight="bold"
				space-after.optimum="15pt"
				text-align="left"
				padding-top="3pt">
				2	Corps
			</fo:block>
			<fo:block font-size="12pt"
				font-family="sans-serif"
				line-height="24pt"
				space-after.optimum="15pt"
				text-align="left"
				padding-top="3pt">
					<xsl:value-of select="." />
			</fo:block>
		</xsl:if>
		
		<xsl:if test="@titre='conclusion'">
			<fo:block font-size="12pt"
				font-family="sans-serif"
				line-height="24pt"
				font-weight="bold"
				space-after.optimum="15pt"
				text-align="left"
				padding-top="3pt">
				3	Conclusion
			</fo:block>
			<fo:block font-size="12pt"
				font-family="sans-serif"
				line-height="24pt"
				space-after.optimum="15pt"
				text-align="left"
				padding-top="3pt">
					<xsl:value-of select="." />
			</fo:block>
		</xsl:if>
		
	  </xsl:for-each>
	  <!-- Introduction -->
	 </fo:flow> 
  </fo:page-sequence>

	</fo:root>
	 
 </xsl:template>
	
 </xsl:stylesheet>