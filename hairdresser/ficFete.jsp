<%
/*
 * This program is part of InCrEG LibertyLook software http://beauty-hair-mng.sourceforge.net
 * Copyright (C) 2001-2008 Emmanuel Guyot <See emmguyot on SourceForge> 
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
                com.increg.salon.bean.FeteBean
	       " %>
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
<title><i18n:message key="ficFete.title" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees" onLoad="Init();document.fiche.PRENOM.focus()">
<%@ include file="include/commun.jsp" %>
<script language="JavaScript">
<!--
<%
   // Récupération des paramètres
   String Action = (String) request.getAttribute("Action");
   FeteBean aFete = (FeteBean) request.getAttribute("FeteBean");
%>
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
   MM_showHideLayers('DUPLIQUER?bottomFrame','','show');
   MM_showHideLayers('RETOUR_LISTE?bottomFrame','','show');
}
//-->
</script>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/ficParam.gif"><br><span class="ssTitre"><i18n:message key="ficFete.sousTitre" /></span></h1>
<salon:message salonSession="<%= mySalon %>" />
<form method="post" action="ficFete.srv" name="fiche">
	 <p> 
		<salon:valeur valeurNulle="0" valeur="<%= aFete.getCD_FETE() %>" >
		  <input type="hidden" name="CD_FETE" value="%%" >
	        </salon:valeur>
		<input type="hidden" name="Action" value="<%=Action%>">
		<span class="obligatoire"><i18n:message key="label.prenom" /> :</span> 
		<salon:valeur valeurNulle="null" valeur="<%= aFete.getPRENOM() %>" >
		  <input type="text" name="PRENOM" value="%%" size="30">
	        </salon:valeur>
	 </p>
	 <p>
		<span class="obligatoire"><i18n:message key="ficFete.dtFete" /> :</span> 
                <i18n:message key="format.dateSansAnnee" id="formatDate" />
        <salon:date type="text" name="DT_FETE" valeurDate="<%= aFete.getDT_FETE() %>" valeurNulle="null" format="<%= formatDate %>" calendrier="true">%%</salon:date>
	</p>
	</table>
</form>

<script language="JavaScript">
// Fonctions d'action

// Enregistrement des données
function Enregistrer()
{
   // Verification des données obligatoires
   if ((document.fiche.PRENOM.value == "")  || (document.fiche.DT_FETE.value == "")) {
      alert ("<i18n:message key="ficFete.champManquant" />");
      return;
   }
   document.fiche.submit();
}

// Duplication de la prestation
function Dupliquer()
{
   if ((document.fiche.PRENOM.value == "")  || (document.fiche.DT_FETE.value == "")) {
      alert ("<i18n:message key="ficFete.champManquant" />");
      return;
   }
   document.fiche.Action.value = "Duplication";
   document.fiche.submit();
}

// Suppression
function Supprimer()
{
    if ((document.fiche.CD_FETE.value != "0") && (document.fiche.CD_FETE.value != "")) {
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
   if (document.fiche.PRENOM.value != "") {
      parent.location.href = "ListeFete.jsp?premLettre=" + document.fiche.PRENOM.value.charAt(0).toUpperCase();
   }
   else {
      parent.location.href = "ListeFete.jsp?premLettre=A";
   }
}

</script>
</body>
</html>
