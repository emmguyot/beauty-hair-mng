<%
/*
 * This program is part of InCrEG LibertyLook software http://beauty-hair-mng.sourceforge.net
 * Copyright (C) 2001-2006 Emmanuel Guyot <See emmguyot on SourceForge> 
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
<%@ page import="java.util.Vector,com.increg.salon.bean.FeteBean" %>
<%@ page import="com.increg.salon.bean.SalonSession" %>
<%
    SalonSession mySalon = (SalonSession) session.getAttribute("SalonSession");
    if (mySalon == null) {
        getServletConfig().getServletContext().getRequestDispatcher("/reconnect.html").forward(request, response);
    }
%>
<%@ taglib uri="WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<i18n:bundle baseName="messages" locale="<%= mySalon.getLangue() %>"/>
<html>
<head>
<title><i18n:message key="title.lstFete" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees">
<%
   // Récupération des paramètres
   String premLettre = (String) request.getAttribute("premLettre");
%>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/lstParam.gif"><br><span class="ssTitre"><i18n:message key="label.fetes" /></span></h1>
<p><i18n:message key="label.premiereLettrePrenom" /> : 
<%
   // Affiche toutes les lettres avec un lien permettant de filtrer par cette lettre
   for (char c='A'; Character.isUpperCase(c); c++) { 
      if ((premLettre != null) && (premLettre.charAt(0) == c)) { %>
	 <%=c %>
      <% } else { %>
	 <a href="rechFete.srv?premLettre=<%=c %>"><%=c %></a>     
   <% }
   }
   if ((premLettre != null) && (premLettre.charAt(0) == ' ')) { %>
	 Tous
      <% } else { %>
	 <a href="rechFete.srv?premLettre=%20">Tous</a>     
   <% }
%>
</p>
<hr>
<table width="100%" border="1" >
	<tr>
		<th><i18n:message key="label.prenom" /></th>
		<th><i18n:message key="label.fete" /></th>
	</tr>
	<%
	// Recupère la liste
	Vector lstLignes = (Vector) request.getAttribute("Liste");
	    
	for (int i=0; i< lstLignes.size(); i++) {
	    FeteBean aFete = (FeteBean) lstLignes.get(i);
	%>
	<tr>
		<td><a href="_FicheFete.jsp?Action=Modification&CD_FETE=<%= aFete.getCD_FETE() %>" target="ClientFrame"><%= aFete.getPRENOM() %></a></td>
	    <td><%= aFete.toString() %>&nbsp;</td>
	</tr>
	<%
	}
	%>
</table>
<script language="JavaScript">
function Nouveau()
{
   parent.location.href = "_FicheFete.jsp";
}

// Affichage de l'aide
function Aide()
{
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideListe.html");
}

</script>
</body>
</html>
