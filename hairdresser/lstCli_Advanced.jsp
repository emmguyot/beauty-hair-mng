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
<%@ page import="com.increg.salon.bean.SalonSession,
				java.util.Vector,
				com.increg.salon.bean.ClientBean" %>
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
<title>Liste des clients</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees">
<%
    // Récupération des paramètres
    String nom = request.getParameter("NOM");
    String prenom = request.getParameter("PRENOM");
    String civilite = request.getParameter("CIVILITE");
    String sexe = request.getParameter("sexe");
    String ville = request.getParameter("VILLE");
    String abonnement = request.getParameter("CD_PREST");
    String INDIC_VALID = request.getParameter("INDIC_VALID");
    Vector listeLignes = (Vector) request.getAttribute("Liste");
    int longueurCle = 1;
%>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/lstCli.gif"></h1>
<salon:message salonSession="<%= mySalon %>" />
<form name="fiche" action="rechCli.srv" method="post">
	<p>Nom :
	<salon:valeur valeurNulle="null" valeur="<%= nom %>" expand="true">
		<input type="text" name="NOM" value="%%" size="15">
    </salon:valeur>
	Prénom :
	<salon:valeur valeurNulle="null" valeur="<%= prenom %>" expand="true">
		<input type="text" name="PRENOM" value="%%" size="15">
    </salon:valeur>
    Civilit&eacute; :
    <salon:selection valeur="<%= civilite %>" libelle="( Toutes )|Mle|Mme|M." valeurs='<%= "|Mle|Mme|M. " %>'>
        <select name="CIVILITE">%%</select>
    </salon:selection>
    Sexe :
    <salon:selection valeur="<%= sexe %>" libelle="( Tous )|Femme|Homme" valeurs='<%= "|F|H" %>'>
        <select name="sexe">%%</select>
    </salon:selection>
	<br/>
	Ville :
	<salon:valeur valeurNulle="null" valeur="<%= ville %>" expand="true">
		<input type="text" name="VILLE" value="%%" size="15">
    </salon:valeur>
	Abonnement :
    <salon:DBselection valeur="<%= abonnement %>" sql="select CD_PREST, LIB_PREST from PREST where INDIC_ABONNEMENT='O' order by LIB_PREST">
		<select name="CD_PREST">
		    <option value="">( Non significatif )</option>
		    <option value="*"
				<% if ("*".equals(abonnement)) { %>
			    	selected="selected"
				<% } %>
			>( Abonnement quelconque )</option>
		    %%
		</select>
    </salon:DBselection>
	&nbsp;&nbsp; Affiche anciens clients : 
    <input type="checkbox" name="INDIC_VALID"
	    <% if ((INDIC_VALID != null) && (INDIC_VALID.equals("on"))) { %> checked <% } %>
    >
	&nbsp;&nbsp;<salon:bouton url="javascript:document.fiche.submit()" img="images/actualiserRech.gif" alt="Actualiser la liste" imgOn="images/actualiserRech2.gif"/>
    <%
    if (listeLignes.size() > 20) { 
    %>
        <br/>Première lettre :
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
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<salon:bouton url="rechCli.srv" img="images/rechSimplifiee.gif" alt="Recherche simplifiée..."/>
	<input type="hidden" name="type" value="advanced">
	</p>
</form>

<%@ include file="lstCli_Common.jsp" %>

</body>
</html>
