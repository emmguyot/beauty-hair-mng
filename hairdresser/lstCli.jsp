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
<link rel="stylesheet" href="style/jquery-ui-1.8.16.custom.css" type="text/css">
</head>
<body class="donnees">
<%@ include file="include/commun.jsp" %>
<%
    // Récupération des paramètres
    String premLettre = (String) request.getAttribute("premLettre");
    String INDIC_VALID = (String) request.getAttribute("INDIC_VALID");
    Vector listeLignes = (Vector) request.getAttribute("Liste");
    int longueurCle = 2;
%>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/lstCli.gif"></h1>
<form name="fiche" action="rechCli.srv" method="post">
	<p><i18n:message key="label.premiereLettreNom" /> : 
	<input type="hidden" name="premLettre" value="<%= premLettre %>">
	<%
	    String lien = "";
	    if ((INDIC_VALID != null) && (INDIC_VALID.length() > 0)) {
	        lien = lien + "&INDIC_VALID=" + INDIC_VALID;
	    }
	    // Affiche toutes les lettres avec un lien permettant de filtrer par cette lettre
	    for (char c='A'; Character.isUpperCase(c); c++) { 
	        if ((premLettre != null) && (premLettre.charAt(0) == c)) { %>
	            <%=c %>
	        <% } else { %>
	            <a href="rechCli.srv?premLettre=<%= c + lien %>"><%=c %></a>     
	    <% }
	    }
	%>
	&nbsp;&nbsp; <i18n:message key="label.affAncienClient" /> : 
    <input type="checkbox" name="INDIC_VALID"
	    <% if ((INDIC_VALID != null) && (INDIC_VALID.equals("on"))) { %> checked <% } %>
    	onClick="document.fiche.submit()" >
    <%
    if (listeLignes.size() > 20) { 
    %>
        <br/><i18n:message key="label.secondeLettre" /> :
        <%
        String lastKey = "";
        for (int i = 0; i < listeLignes.size(); i++) {
        ClientBean aCli = (ClientBean) listeLignes.get(i);

            String nextKey = aCli.getNOM().substring(0,Math.min(aCli.getNOM().length(), longueurCle)); 
            if (!nextKey.equals(lastKey))  {
        %>
                <a href="#<%= nextKey %>"><%= nextKey %></a>&nbsp;&nbsp;&nbsp;
        <%
            }
            lastKey = nextKey;
        }
    }
    %>
        <i18n:message key="label.rechercheAvancee" id="rechAvancee" />
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<salon:bouton url="rechCli.srv?type=advanced" img="images/rechAvancee.gif" alt="<%= rechAvancee %>"/>
	<input type="hidden" name="type" value="simple">
	</p>
</form>

<%@ include file="lstCli_Common.jsp" %>

</body>
</html>
