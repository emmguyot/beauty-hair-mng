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
<%@ page import="java.util.TreeSet, java.util.Iterator" %>
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
<title><i18n:message key="ficGestSauv.title" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees">
<%@ include file="include/commun.jsp" %>
<script language="JavaScript">
<!--
<%
   // Récupération des paramètres
   String Action = request.getParameter("Action");
   String Type = request.getParameter("Type");
   TreeSet listeFichier = (TreeSet) request.getAttribute("listeFichier");
%>
   var Action="<%=Action%>";

function Init() {
    // Juste le lien supprimer
    MM_showHideLayers('SUPPRIMER?bottomFrame','','show');
    MM_showHideLayers('ENREGISTRER?bottomFrame','','hide');
}
//-->
</script>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/ficGestSauv.gif"></h1>
<form name="fiche" action="restauration.srv" method="post">
<p>
    <input type="hidden" name="Action" value="Gestion">
    <i18n:message key="label.typeSauvegarde" /> :
        <i18n:message key="valeur.typeSauvegarde" id="valeurType" />
	<salon:selection valeur="<%= Type %>" valeurs='<%= "D|M|I" %>' libelle="<%= valeurType %>">
        <select name="Type" onChange="rechargeListe()">
            %%
        </select>
    </salon:selection>
<hr>
<table width="100%" border="1" >
	<tr>
		<th><i18n:message key="ficGestSauv.selectSuppr" /><br/><a href="javascript:selection()"><i18n:message key="label.toutSelection" /></a></th>
		<th><i18n:message key="label.nomSauvegarde" /></th>
	</tr>
        <%
        for (Iterator i=listeFichier.iterator(); i.hasNext(); ) {
                String nom = (String) i.next();
        %>
            <tr>
            <td align="right"><salon:valeur valeur="<%= nom %>" valeurNulle="null"><input type="checkbox" name="%%"></salon:valeur></td>
            <td>
	    <salon:valeur valeur="<%= nom %>" valeurNulle="null"> %% </salon:valeur>
            </td>
            </tr>
        <% } %>
</table>
</form>
<script language="JavaScript">

function rechargeListe()
{
   document.fiche.Action.value = "Gestion";
   document.fiche.submit();
}

// Sélectionne toutes les sauvegardes
function selection()
{
    for (var i = 0; i < document.fiche.elements.length; i++) {
        var aCheckBox = document.fiche.elements[i];

        if (aCheckBox.type == "checkbox") {
            aCheckBox.checked = true;
        }
    }
}

// Suppression de la prestation
function Supprimer()
{
    var ok = false;
    for (var i = 0; !ok && (i < document.fiche.elements.length); i++) {
        var aCheckBox = document.fiche.elements[i];

        if (aCheckBox.checked == true) {
            ok = true;
        }
    }
    if (ok) {
        if (confirm ("<i18n:message key="ficGestSauv.confirmSuppr" />")) {
            document.fiche.Action.value = "Suppression";
            document.fiche.submit();
        }
    }
}

// Affichage de l'aide
function Aide()
{
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideFiche.html");
}



</script>
</body>
</html>
