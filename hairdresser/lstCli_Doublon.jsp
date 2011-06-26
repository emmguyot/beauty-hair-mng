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
<%@ page import="com.increg.salon.bean.SalonSession,
				java.util.Vector,
				com.increg.salon.bean.ClientBean" %>
<%
SalonSession mySalon = com.increg.salon.servlet.ConnectedServlet.CheckOrGoHome(request, response);
%>
<%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %>
<%@ taglib uri="WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<i18n:bundle baseName="messages" locale="<%= mySalon.getLangue() %>"/>
<html>
<head>
<title><i18n:message key="title.lstCli" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees">
<%@ include file="include/commun.jsp" %>
<%
    // Récupération des paramètres
    String Action = (String) request.getAttribute("Action");
    String CD_CLI = (String) request.getAttribute("CD_CLI");
    Vector lstLignes = (Vector) request.getAttribute("Liste");
%>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/lstCli.gif"></h1>
<salon:message salonSession="<%= mySalon %>" />
<form name="fiche" action="rechCli.srv" method="post">
	<p>
	<input type="hidden" name="Action" value="<%= Action %>">
	<input type="hidden" name="CD_CLI" value="<%= CD_CLI %>">
	</p>
<table width="100%" border="1" >
	<tr>
		<th><i18n:message key="label.regrouper" /></th>
		<th><i18n:message key="label.nomClient" /></th>
		<th><i18n:message key="label.adresse" /></th>
		<th><i18n:message key="label.ville" /></th>
		<th><i18n:message key="label.telephone" /></th>
	</tr>
	<%
        String lastKey = "";
	    
	for (int i = 0; i < lstLignes.size(); i++) {
	    ClientBean aCli = (ClientBean) lstLignes.get(i);

	%>
	<tr><td width="40">
              <%
                if (aCli.getCD_CLI() == Long.parseLong(CD_CLI)) {
              %>
                    <input type="checkbox" disabled="disabled" checked="checked" name="DOUBLON_<%= aCli.getCD_CLI() %>" />
              <%
                }
                else {
              %>
                    <input type="checkbox" name="DOUBLON_<%= aCli.getCD_CLI() %>" />
              <%
                }
              %>
            </td>
	    <td><a href="_FicheCli.jsp?Action=Modification&CD_CLI=<%= aCli.getCD_CLI() %>" target="ClientFrame"><%= aCli.toStringListe() %></a></td>
	    <td>
		<salon:valeur valeurNulle="0" valeur="<%= aCli.getRUE() %>" expand="true">
		  %%
                </salon:valeur>
	    &nbsp;</td>
	    <td><%= aCli.getVILLE() %>&nbsp;</td>
	    <td><%= aCli.getTEL() %>&nbsp;</td>
	</tr>
	<%
	}
	%>
</table>
</form>
<script language="JavaScript">
function Init() {
   <%
   // Positionne les liens d actions
   %>
   MM_showHideLayers('NOUVEAU?bottomFrame','','hide');
   MM_showHideLayers('DOUBLON?bottomFrame','','show');
}

function Doublon()
{
    var cpt = 0;
    for (i = 0; i < document.fiche.elements.length; i++) {
        if (document.fiche.elements[i].name.substr(0, 7) == "DOUBLON") {
            if (document.fiche.elements[i].checked) {
                cpt++;
            }
        }
    }

    if (cpt < 2) {
        alert("<i18n:message key="lstCli.selectionDoublonManquant" />");
        return;
    }
    document.fiche.Action.value = "Groupe";
    document.fiche.submit();
}

// Affichage de l'aide
function Aide()
{
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideListeCli.html");
}

</script>

</body>
</html>
