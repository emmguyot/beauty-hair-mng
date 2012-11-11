<%
/*
 * This program is part of InCrEG LibertyLook software http://beauty-hair-mng.sourceforge.net
 * Copyright (C) 2001-2012 Emmanuel Guyot <See emmguyot on SourceForge> 
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
<%@ page import="java.util.Vector,com.increg.salon.bean.CriterePubBean" %>
<%@ page import="com.increg.salon.bean.SalonSession" %>
<%
SalonSession mySalon = com.increg.salon.servlet.ConnectedServlet.CheckOrGoHome(request, response);
%>
<%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %>
<%@ taglib uri="WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<i18n:bundle baseName="messages" locale="<%= mySalon.getLangue() %>"/>
<html>
<head>
<title><i18n:message key="title.lstCriterePub" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
<link rel="stylesheet" href="style/jquery-ui-1.8.16.custom.css" type="text/css">
</head>
<body class="donnees">
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/lstPub.gif"></h1>
<table width="100%" border="1" >
	<tr>
        <salon:autorisation entite="Publipostage" action="Modification">
		<th></th>
        </salon:autorisation>
		<th><i18n:message key="label.libelle" /></th>
	</tr>
	<%
	// Recupère la liste
	Vector lstLignes = (Vector) request.getAttribute("Liste");
	    
	for (int i=0; i < lstLignes.size(); i++) {
	    CriterePubBean aCriterePub = (CriterePubBean) lstLignes.get(i);
	%>
	<tr>
        <salon:autorisation entite="Publipostage" action="Modification">
	    <td width=20><a href="_FicheCriterePub.jsp?Action=Modification&CD_CRITERE_PUB=<%= aCriterePub.getCD_CRITERE_PUB() %>" target="ClientFrame"><img src="images/pub2.gif" border=0></a></td>
        </salon:autorisation>
	    <td><a href="_FicheCriterePub.jsp?Action=Construction&CD_CRITERE_PUB=<%= aCriterePub.getCD_CRITERE_PUB() %>" target="ClientFrame"><%= aCriterePub.toString() %></a></td>
	</tr>
	<%
	}
	%>
</table>
<script language="JavaScript">
function Nouveau()
{
   parent.location.href = "_FicheCriterePub.jsp";
}

// Affichage de l'aide
function Aide()
{
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideListe.html");
}

</script>
</body>
</html>
