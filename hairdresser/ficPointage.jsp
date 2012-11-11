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
                com.increg.salon.bean.CollabBean,
	        com.increg.salon.bean.PointageBean
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
   PointageBean aPointage = (PointageBean) request.getAttribute("PointageBean");
%>
<title><i18n:message key="ficPointage.title" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
<link rel="stylesheet" href="style/jquery-ui-1.8.16.custom.css" type="text/css">
</head>
<body class="donnees" onLoad="Init();
   <% if (Action.equals("Creation")) { %>
      document.fiche.CD_COLLAB.focus()
   <% } else { %>
      document.fiche.DT_FIN.focus()
   <% } %> ">
<%@ include file="include/commun.jsp" %>
<script language="JavaScript">
<!--
   var Action="<%=Action%>";

function Init() {
   <%
   // Positionne les liens d'actions
   if (! Action.equals("Creation")) {
      %>
      MM_showHideLayers('SUPPRIMER?bottomFrame','','show');
      MM_showHideLayers('ENREGISTRER?bottomFrame','','show');
      <%
   }
   if (Action.equals("Creation")) { %>
      // Pas de lien supprimer
      MM_showHideLayers('SUPPRIMER?bottomFrame','','hide');
      MM_showHideLayers('ENREGISTRER?bottomFrame','','show');
   <%
   } %>
   MM_showHideLayers('NOUVEAU?bottomFrame','','show');
   MM_showHideLayers('RETOUR_LISTE?bottomFrame','','show');
}
//-->
</script>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/ficPointage.gif" alt=<salon:TimeStamp bean="<%= aPointage %>" />></h1>
<salon:message salonSession="<%= mySalon %>" />
<form method="post" action="ficPointage.srv" name="fiche">
	 <p><span class="obligatoire"><i18n:message key="label.collaborateur" /> :</span> 
	    <% if (Action.equals("Creation")) { %>
		<salon:DBselection valeur="<%= aPointage.getCD_COLLAB() %>" sql="select CD_COLLAB, PRENOM from COLLAB order by PRENOM, NOM">
		  <select name="CD_COLLAB">
		     %%
		  </select>
	        </salon:DBselection>
	    <% } 
	       else { %>
		  <salon:valeur valeurNulle="null" valeur="<%= aPointage.getCD_COLLAB() %>" >
		     <input type="hidden" name="CD_COLLAB" value="%%">
		  </salon:valeur>
		  <salon:valeur valeurNulle="null" valeur="<%= CollabBean.getCollabBean(mySalon.getMyDBSession(), Integer.toString(aPointage.getCD_COLLAB())).toString() %>" >
		     <span class="readonly">%%</span>
		  </salon:valeur>

	    <% } %>
		<input type="hidden" name="Action" value="<%=Action%>">
	 </p>
	 <p><span class="obligatoire"><i18n:message key="label.debut" /> :</span> 
            <i18n:message key="format.dateSimpleDefaut" id="formatDate" />
            <i18n:message key="format.heureSimpleDefaut" id="formatHeure" />
        <%
		if (Action.equals("Creation")) { %>
			<salon:date type="text" name="DT_DEBUT" valeurDate="<%= aPointage.getDT_DEBUT() %>" valeurNulle="null" format="<%= formatDate %>" calendrier="true" onchange="synchroDates()">%%</salon:date>
			<salon:date type="text" name="HR_DEBUT" valeurNulle="null" valeurDate="<%= aPointage.getDT_DEBUT() %>" format="<%= formatHeure %>">%%</salon:date>
       	<%
       	} 
	  	else { %>
            <salon:date type="readonly" name="DT_DEBUT" valeurDate="<%= aPointage.getDT_DEBUT() %>" valeurNulle="null" format="<%= formatDate %>">%%</salon:date>
            <salon:date type="readonly" name="HR_DEBUT" valeurNulle="null" valeurDate="<%= aPointage.getDT_DEBUT() %>" format="<%= formatHeure %>">%%</salon:date>
       	<%
       	} %>
	    <span class="facultatif"><i18n:message key="label.fin" /> :</span> 
        <salon:date type="text" name="DT_FIN" valeurDate="<%= aPointage.getDT_FIN() %>" valeurNulle="null" format="<%= formatDate %>" calendrier="true">%%</salon:date>
        <salon:date type="text" name="HR_FIN" valeurDate="<%= aPointage.getDT_FIN() %>" valeurNulle="null" format="<%= formatHeure %>">%%</salon:date>
	 </p>
	 <p><span class="obligatoire"><i18n:message key="label.typePointage" /> :</span> 
		<salon:DBselection valeur="<%= aPointage.getCD_TYP_POINTAGE() %>" sql="select CD_TYP_POINTAGE, LIB_TYP_POINTAGE from TYP_POINTAGE order by LIB_TYP_POINTAGE" msgManquant="true">
		  <select name="CD_TYP_POINTAGE">
		     %%
		  </select>
	        </salon:DBselection>
	</p>
	<p>
	<span class="facultatif"><i18n:message key="label.commentaire" /> :</span> 
	    <salon:valeur valeurNulle="null" valeur="<%= aPointage.getCOMM() %>" >
		<textarea name="COMM" cols="40" rows="2">%%</textarea>
	    </salon:valeur>
	</p>
</form>

<script language="JavaScript">
// Fonctions d'action

// Synchronises les dates début et fin
function synchroDates() {
	document.fiche.DT_FIN.value = document.fiche.DT_DEBUT.value;
}

// Enregistrement des données
function Enregistrer()
{
   // Verification des données obligatoires
   if ((document.fiche.DT_DEBUT.value == "") || (document.fiche.HR_DEBUT.value == "")) {
      alert ("<i18n:message key="ficPointage.dateDebutManquant" />");
      return;
   }
   document.fiche.submit();
}

// Création d'un nouveau pointage
function Nouveau()
{
   parent.location.href = "_FichePointage.jsp";
}

// Suppression
function Supprimer()
{
    if (document.fiche.DT_DEBUT.value != "") {
        if (confirm ("<i18n:message key="message.suppressionDefinitiveConfirm" />")) {
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

function RetourListe()
{
    parent.location.href = "ListePointage.jsp";
}


</script>
</body>
</html>
