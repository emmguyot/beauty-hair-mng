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
<%@ page import="java.util.Vector,
			com.increg.salon.bean.TypVentBean,
			com.increg.salon.bean.TvaBean,
			com.increg.salon.bean.SalonSession" %>
<%
SalonSession mySalon = com.increg.salon.servlet.ConnectedServlet.CheckOrGoHome(request, response);
%>
<%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %>
<%@ taglib uri="WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<i18n:bundle baseName="messages" locale="<%= mySalon.getLangue() %>"/>
<html>
<head>
<title><i18n:message key="title.lstTypVent" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
<%@ include file="include/commun.jsp" %>
<script language="JavaScript">
<!--
function Init() {
   <%
   // Positionne les liens d actions
   %>
   MM_showHideLayers('NOUVEAU?bottomFrame','','show');
}
//-->
</script>
</head>
<body class="donnees">
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/lstParam.gif"><br><span class="ssTitre"><i18n:message key="label.typesPrest" /></span></h1>
<table width="100%" border="1" >
	<tr>
		<th><i18n:message key="label.libelle" /></th>
		<th><i18n:message key="label.articleAssocie" /></th>
		<th><i18n:message key="label.civiliteAssocie" /></th>
		<th><i18n:message key="label.TVAapplicable" /></th>
		<th><i18n:message key="label.TVAapplicableSuppl" /></th>
	</tr>
	<%
	// Recupère la liste
	Vector lstLignes = (Vector) request.getAttribute("Liste");
	    
	for (int i=0; i< lstLignes.size(); i++) {
	    TypVentBean aTypVent = (TypVentBean) lstLignes.get(i);
	%>
	<tr>
		<td><a href="_FicheTypVent.jsp?Action=Modification&CD_TYP_VENT=<%= aTypVent.getCD_TYP_VENT() %>" target="ClientFrame"><%= aTypVent.toString() %></a></td>
        <td><salon:valeur valeur="<%= aTypVent.getMARQUE() %>" valeurNulle="null">%%</salon:valeur></td>
        <%
        String civilite = aTypVent.getCIVILITE();
        if (civilite != null) {
            civilite = civilite.replace('|', ' ');
        }
        %>
        <td><salon:valeur valeur="<%= civilite %>" valeurNulle="null">%%&nbsp;</salon:valeur></td>
        <%
        String tva = "";
        if (aTypVent.getCD_TVA() != 0) {
        	tva = TvaBean.getTvaBean(mySalon.getMyDBSession(), Integer.toString(aTypVent.getCD_TVA())).getLIB_TVA();
        }
        %>
        <td><salon:valeur valeur="<%= tva %>" valeurNulle="null">%%</salon:valeur></td>
        <%
        tva = "";
        if (aTypVent.getCD_TVA_SUPPL() != 0) {
        	tva = TvaBean.getTvaBean(mySalon.getMyDBSession(), Integer.toString(aTypVent.getCD_TVA_SUPPL())).getLIB_TVA();
        }
        %>
        <td><salon:valeur valeur="<%= tva %>" valeurNulle="null">%%</salon:valeur>&nbsp;</td>
	</tr>
	<%
	}
	%>
</table>
<script language="JavaScript">
<!--

// Part en création
function Nouveau()
{
   parent.location.href = "_FicheTypVent.jsp";
}

// Affichage de l'aide
function Aide()
{
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideListe.html");
}

//-->
</script>
</body>
</html>
