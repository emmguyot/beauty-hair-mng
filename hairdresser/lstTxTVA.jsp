<%
/*
 * This program is part of InCrEG LibertyLook software http://beauty-hair-mng.sourceforge.net
 * Copyright (C) 2001-2009 Emmanuel Guyot <See emmguyot on SourceForge> 
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
<%@ page import="java.util.Vector,com.increg.salon.bean.TvaBean" %>
<%@ page import="com.increg.salon.bean.SalonSession" %>
<%
    SalonSession mySalon = (SalonSession) session.getAttribute("SalonSession");
    if (mySalon == null) {
        getServletConfig().getServletContext().getRequestDispatcher("/reconnect.html").forward(request, response);
    }
%>
<%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %>
<%@ taglib uri="WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<i18n:bundle baseName="messages" locale="<%= mySalon.getLangue() %>"/>
<html>
<head>
<title><i18n:message key="title.lstTxTVA" /></title>
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
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/lstParam.gif"><br><span class="ssTitre"><i18n:message key="label.tauxTVA" /></span></h1>
<table width="100%" border="1" >
	<tr>
		<th><i18n:message key="label.libelle" /></th>
		<th><i18n:message key="label.taux" /></th>
	</tr>
	<%
	// Recup�re la liste
	Vector lstLignes = (Vector) request.getAttribute("Liste");
	    
	for (int i=0; i < lstLignes.size(); i++) {
	    TvaBean aTva = (TvaBean) lstLignes.get(i);
	%>
	<tr>
		<td><a href="_FicheTxTVA.jsp?Action=Modification&CD_TVA=<%= aTva.getCD_TVA() %>" target="ClientFrame"><%= aTva.toString() %></a></td>
        <td><salon:valeur valeur="<%= aTva.getTX_TVA() %>" valeurNulle="null">%%</salon:valeur></td>
	</tr>
	<%
	}
	%>
</table>
<script language="JavaScript">
<!--

// Part en cr�ation
function Nouveau()
{
   parent.location.href = "_FicheTxTVA.jsp";
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
