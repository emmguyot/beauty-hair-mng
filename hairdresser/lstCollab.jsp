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
<%@ page import="com.increg.salon.bean.SalonSession,java.util.Vector,
	       com.increg.salon.bean.CollabBean,
	       com.increg.salon.bean.DonneeRefBean" %>
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
<title><i18n:message key="title.lstCollab" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees">
<%
   // Récupération des paramètres
   String INDIC_VALID = (String) request.getAttribute("INDIC_VALID");
%>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/lstCollab.gif"></h1>
<form name="fiche" action="rechCollab.srv" method="post">
<i18n:message key="label.affAncienCollab" /> : 
   <input type="checkbox" name="INDIC_VALID"
   <% if ((INDIC_VALID != null) && (INDIC_VALID.equals("on"))) { %>
   checked 
   <% } %>
   onClick="document.fiche.submit()" >
</form>
<hr>
<table width="100%" border="1" >
	<tr>
		<th><i18n:message key="label.collaborateur" /></th>
		<th><i18n:message key="label.ville" /></th>
		<th><i18n:message key="label.fonction" /></th>
	</tr>
	<%
	// Recupère la liste
	Vector lstLignes = (Vector) request.getAttribute("Liste");
	    
	for (int i=0; i< lstLignes.size(); i++) {
	    CollabBean aCollab = (CollabBean) lstLignes.get(i);
	%>
	<tr>
		<td><a href="_FicheCollab.jsp?Action=Modification&CD_COLLAB=<%= aCollab.getCD_COLLAB() %>" target="ClientFrame"><%= aCollab.toString() %></a></td>
	<td><%= aCollab.getVILLE() %>&nbsp;</td>
	<td>
	    <% DonneeRefBean FCT = DonneeRefBean.getDonneeRefBean(mySalon.getMyDBSession(), "FCT",
							       Integer.toString(aCollab.getCD_FCT())); %>
	    <salon:valeur valeur="<%= (FCT == null) ? null : FCT.toString() %>" valeurNulle="null"> %% </salon:valeur>
	    &nbsp;
	</td>
	</tr>
	<%
	}
	%>
</table>
<script language="JavaScript">
function Nouveau()
{
   parent.location.href = "_FicheCollab.jsp";
}

// Affichage de l'aide
function Aide()
{
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideListe.html");
}

</script>
</body>
</html>
