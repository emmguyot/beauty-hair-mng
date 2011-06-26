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
<%@ page import="com.increg.salon.bean.SalonSession,java.util.Calendar" %>
<%
SalonSession mySalon = com.increg.salon.servlet.ConnectedServlet.CheckOrGoHome(request, response);
%>
<%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %>
<%@ taglib uri="WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<i18n:bundle baseName="messages" locale="<%= mySalon.getLangue() %>"/>
<html>
<head>
<%
   // Récupération des paramètres
   Calendar DT_DEBUT = (Calendar) request.getAttribute("DT_DEBUT");
   Calendar DT_FIN = (Calendar) request.getAttribute("DT_FIN");
%>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title><i18n:message key="title.ficReFact" /></title>
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees" onLoad="document.fiche.DT_DEBUT.focus()">
<%@ include file="include/commun.jsp" %>
<script language="JavaScript">
<!--

function Init() {
      MM_showHideLayers('SUPPRIMER?bottomFrame','','hide');
      MM_showHideLayers('ENREGISTRER?bottomFrame','','hide');
      MM_showHideLayers('DUPLIQUER?bottomFrame','','hide');
      MM_showHideLayers('IMPRIMER?bottomFrame','','hide');
      MM_showHideLayers('VALIDER?bottomFrame','','show');
}
//-->
</script>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/ficReFact.gif"></h1>
<salon:message salonSession="<%= mySalon %>" />
<form method="post" action="reFact.srv" name="fiche" target="_blank">
	<p> 
            <span class="obligatoire"><i18n:message key="label.debut" /> :</span> 
            <i18n:message key="format.dateSimpleDefaut" id="formatDate" />
            <salon:date type="text" name="DT_DEBUT" valeurDate="<%= DT_DEBUT %>" valeurNulle="null" format="<%= formatDate %>" calendrier="true">%%</salon:date>
            <span class="obligatoire"><i18n:message key="label.fin" /> :</span> 
            <salon:date type="text" name="DT_FIN" valeurDate="<%= DT_FIN %>" valeurNulle="null" format="<%= formatDate %>" calendrier="true">%%</salon:date>
	</p>
        <p>
            <span class="obligatoire"><i18n:message key="label.formatEdition" /> :</span>
            <select name="format">
                <option value="F"><i18n:message key="valeur.editionFiche" /></option>
                <option value="L"><i18n:message key="valeur.editionEtat" /></option>
            </select>
        </p>
</form>
<script language="JavaScript">
// Fonctions d'action

// Enregistrement des données du client
function Valider()
{
   document.fiche.submit();
}

// Affichage de l'aide
function Aide()
{
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideFiche.html");
}

</script>
</body>
</html>
