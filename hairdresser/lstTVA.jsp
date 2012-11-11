<%
/*
 * This program is part of InCrEG LibertyLook software http://beauty-hair-mng.sourceforge.net
 * Copyright (C) 2001-2011 Emmanuel Guyot <See emmguyot on SourceForge> 
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms 
 * of the GNU General Public License as published by the Free Software Foundation; either 
 * version 2 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; 
 * if not, write to the 
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 */
%>
<%@ page import="java.util.Vector, java.math.BigDecimal,java.util.Calendar" %>
<%@ page import="com.increg.salon.bean.SalonSession,
                com.increg.salon.request.TVA,
	        com.increg.salon.bean.TypVentBean
	        " %>
<%
SalonSession mySalon = com.increg.salon.servlet.ConnectedServlet.CheckOrGoHome(request, response);
%>
<%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %>
<%@ taglib uri="WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<i18n:bundle baseName="messages" locale="<%= mySalon.getLangue() %>"/>
<html>
<head>
<title><i18n:message key="title.lstTVA" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
<style>
.TVASuppl { display:none; }
</style>
</head>
<body class="donnees" onLoad="document.fiche.DT_DEBUT.focus()">
<%@ include file="include/commun.jsp" %>
<script language="JavaScript">
<!--
function Init() {
   <%
   // Positionne les liens d actions
   %>
   MM_showHideLayers('NOUVEAU?bottomFrame','','hide');
}
//-->
</script>
<%
   // Récupération des paramètres
   Calendar DT_DEBUT = (Calendar) request.getAttribute("DT_DEBUT");
   Calendar DT_FIN = (Calendar) request.getAttribute("DT_FIN");
%>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/lstTVA.gif"></h1>
<form name="fiche" action="rechTVA.srv" method="post">
<p>
<i18n:message key="label.entreLe" /> :
<i18n:message key="format.dateSimpleDefaut" id="formatDate" />
    <salon:date type="text" name="DT_DEBUT" valeurDate="<%= DT_DEBUT %>" valeurNulle="null" format="<%= formatDate %>" calendrier="true" onchange="document.fiche.submit()">%%</salon:date>
<i18n:message key="label.etLe" /> : 
    <salon:date type="text" name="DT_FIN" valeurDate="<%= DT_FIN %>" valeurNulle="null" format="<%= formatDate %>" calendrier="true" onchange="document.fiche.submit()">%%</salon:date>
</p>
</form>
<hr>
<table width="100%" border="1" >
	<tr>
		<th><i18n:message key="label.typePrest" /></th>
		<th><i18n:message key="label.HT" /></th>
		<th><i18n:message key="label.TVA" /></th>
		<th class="TVASuppl"><i18n:message key="label.TVASuppl" /></th>
		<th><i18n:message key="label.TTC" /></th>
	</tr>
	<%
	// Recupère la liste
	Vector lstLignes = (Vector) request.getAttribute("Liste");
    BigDecimal fullTotal = new BigDecimal(0);
    BigDecimal fullTotalSuppl = new BigDecimal(0);
    BigDecimal fullTotalHT = new BigDecimal(0);
    BigDecimal fullTotalTTC = new BigDecimal(0);
	    
	for (int i=0; i< lstLignes.size(); i++) {
	    TVA aTVA = (TVA) lstLignes.get(i);
        fullTotal = fullTotal.add(aTVA.getTotal());
        if (aTVA.getTotalSuppl() != null) {
        	fullTotalSuppl = fullTotalSuppl.add(aTVA.getTotalSuppl());
        }
        fullTotalHT = fullTotalHT.add(aTVA.getTotalHT());
        fullTotalTTC = fullTotalTTC.add(aTVA.getTotalTTC());
	%>
	<tr>
		<td><%= TypVentBean.getTypVentBean(mySalon.getMyDBSession(), Integer.toString(aTVA.getCD_TYP_VENT())).toString() %></td>
	<td class="Nombre">
	    <salon:valeur valeur="<%= aTVA.getTotalHT() %>" valeurNulle="null"> %% </salon:valeur>
	</td>
	<td class="Nombre">
	    <salon:valeur valeur="<%= aTVA.getTotal() %>" valeurNulle="null"> %% </salon:valeur>
	</td>
	<td class="Nombre TVASuppl">
	    <salon:valeur valeur="<%= aTVA.getTotalSuppl() %>" valeurNulle="null"> %% </salon:valeur>
	</td>
	<td class="Nombre">
	    <salon:valeur valeur="<%= aTVA.getTotalTTC() %>" valeurNulle="null"> %% </salon:valeur>
	</td>
	</tr>
	<%
	}
	%>
        <tfoot>
        <tr>
            <td class="Total"><i18n:message key="label.total" /></td>
            <td class="Nombre">
                <salon:valeur valeur="<%= fullTotalHT %>" valeurNulle="null"> %% </salon:valeur>
            </td>
            <td class="Nombre">
                <salon:valeur valeur="<%= fullTotal %>" valeurNulle="null"> %% </salon:valeur>
            </td>
			<td class="Nombre TVASuppl">
			    <salon:valeur valeur="<%= fullTotalSuppl %>" valeurNulle="null"> %% </salon:valeur>
			</td>
            <td class="Nombre">
                <salon:valeur valeur="<%= fullTotalTTC %>" valeurNulle="null"> %% </salon:valeur>
            </td>
        </tr>
        </tfoot>
</table>
<i18n:message key="message.chiffresArrondis" /><salon:madeBy />
<script language="JavaScript">
<!--

<% if (fullTotalSuppl.compareTo(BigDecimal.ZERO) != 0) { %>
	$(".TVASuppl").show();
<% } %>

// Affichage de l'aide
function Aide()
{
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideListeTVA.html");
}

//-->
</script>
</body>
</html>
