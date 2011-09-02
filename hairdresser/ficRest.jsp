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
<%@ page import="java.util.TreeSet, java.util.Iterator" %>
<%@ page import="com.increg.salon.bean.SalonSession
	       " %>
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
   String Action = (String) request.getAttribute("Action");
   TreeSet listeFichier = (TreeSet) request.getAttribute("listeFichier");
   String Type = (String) request.getAttribute("Type");
%>
<title><i18n:message key="ficRest.title" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees" onLoad="document.fiche.Type.focus()">
<%@ include file="include/commun.jsp" %>
<script language="JavaScript">
<!--
   var Action="<%=Action%>";

function Init() {
      MM_showHideLayers('SUPPRIMER?bottomFrame','','hide');
      MM_showHideLayers('ENREGISTRER?bottomFrame','','hide');
      MM_showHideLayers('DUPLIQUER?bottomFrame','','hide');
      MM_showHideLayers('VALIDER?bottomFrame','','show');
}
//-->
</script>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/ficRest.gif"></h1>
<salon:message salonSession="<%= mySalon %>" />
<form method="post" action="restauration.srv" name="fiche">
	<p class="warning"> <i18n:message key="ficRest.warning" /> </p>
	<p>
		<input type="hidden" name="Action" value="Restauration">
		<input type="hidden" name="lock" value="">
		<span class="obligatoire"><i18n:message key="label.typeSauvegarde" /> :</span> 
                <i18n:message key="valeur.typeSauvegarde" id="valeurType" />
		<salon:selection valeur="<%= Type %>" valeurs='<%= "D|M|I" %>' libelle="<%= valeurType %>">
		  <select name="Type" onChange="rechargeListe()">
		     %%
		  </select>
		</salon:selection>
		<span class="obligatoire"><i18n:message key="label.nomSauvegarde" /> :</span> 
		  <select name="nomFichier">
		<%
		  for (Iterator i=listeFichier.iterator(); i.hasNext(); ) {
		     String nom = (String) i.next();
	        %>
		<salon:valeur valeurNulle="null" valeur="<%= nom %>" >
		  <option value="%%">%%</option>
	        </salon:valeur>
	        <% } %>
		</select>
	</p>
</form>
<span id="AttenteSpan" style="visibility: hidden">
<p class="Warning"><img name="Attente" src="images/attente.gif" width="231" height="10" alt="<i18n:message key="message.patience" />"></p>
</span>
<salon:include file="<%= \"include/\" + mySalon.getLangue().getLanguage() + \"/salonNews.inc\" %>" />
<script language="JavaScript">
// Fonctions d'action

function rechargeListe()
{
   document.fiche.Action.value = "Liste";
   document.fiche.submit();
}

// Enregistrement des données du client
function Valider()
{
   if (document.fiche.lock.value == "") {
        if (confirm("<i18n:message key="ficRest.restaure" />")) {
            MM_showHideLayers('AttenteSpan','','show');
            document.fiche.lock.value = "xx";
            document.fiche.target = "ClientFrame";
            document.fiche.submit();
        }
   }
}

// Affichage de l'aide
function Aide()
{
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideFicheRest.html");
}

</script>
</body>
</html>
