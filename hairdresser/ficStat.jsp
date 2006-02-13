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
	       com.increg.salon.bean.StatBean" %>
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
<title><i18n:message key="ficStat.title" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees" onLoad="Init();document.fiche.LIB_STAT.focus()">
<%@ include file="include/commun.jsp" %>
<script language="JavaScript">
<!--
<%
   // Récupération des paramètres
   String Action = (String) request.getAttribute("Action");
   StatBean aStat = (StatBean) request.getAttribute("StatBean");
%>
   var Action="<%=Action%>";

function Init() {
   <%
   // Positionne les liens d'actions
   if (! Action.equals("Creation")) {
      %>
      MM_showHideLayers('SUPPRIMER?bottomFrame','','show');
      MM_showHideLayers('ENREGISTRER?bottomFrame','','show');
      MM_showHideLayers('DUPLIQUER?bottomFrame','','show');
      <%
   }
   if (Action.equals("Creation")) { %>
      // Pas de lien supprimer
      MM_showHideLayers('SUPPRIMER?bottomFrame','','hide');
      MM_showHideLayers('ENREGISTRER?bottomFrame','','show');
      MM_showHideLayers('DUPLIQUER?bottomFrame','','show');
   <%
   } %>
   MM_showHideLayers('RETOUR_LISTE?bottomFrame','','show');
}
//-->
</script>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/ficStat.gif" alt=<salon:TimeStamp bean="<%= aStat %>" />></h1>
<salon:message salonSession="<%= mySalon %>" />
<form method="post" action="ficStat.srv" name="fiche">
	<p> 
		<salon:valeur valeurNulle="0" valeur="<%= aStat.getCD_STAT() %>" >
		  <input type="hidden" name="CD_STAT" value="%%" >
	        </salon:valeur>
		<input type="hidden" name="Action" value="<%=Action%>">
		<span class="obligatoire"><i18n:message key="label.libelle" /> :</span> 
		<salon:valeur valeurNulle="null" valeur="<%= aStat.getLIB_STAT() %>" >
		  <input type="text" name="LIB_STAT" value="%%" size=80>
	        </salon:valeur>
       </p>
       <p>
		<span class="facultatif"><i18n:message key="label.axeX" /> :</span> 
		<salon:valeur valeurNulle="null" valeur="<%= aStat.getLABEL_X() %>" >
		  <input type="text" name="LABEL_X" value="%%" >
	        </salon:valeur>
		<span class="facultatif"><i18n:message key="label.axeY" /> :</span> 
		<salon:valeur valeurNulle="null" valeur="<%= aStat.getLABEL_Y() %>" >
		  <input type="text" name="LABEL_Y" value="%%" >
	        </salon:valeur>
	</p>
	 <p>
	        <span class="obligatoire"><i18n:message key="label.requete" /> :</span> 
	        <salon:valeur valeurNulle="null" valeur="<%= aStat.getREQ_SQL() %>" >
		  <textarea name="REQ_SQL" rows="5" cols="80">%%</textarea>
	        </salon:valeur>
	        <a href="javascript:Visualiser()"><img src="images/stat2.gif" border=0 alt="<i18n:message key="ficStat.testRequete" />"></a>
	 </p>
</form>

<script language="JavaScript">
// Fonctions d'action

// Suppression du client
function Visualiser()
{
   parent.location.href = "_FicheStat.jsp?Action=Construction&CD_STAT=" + document.fiche.CD_STAT.value;
}


// Enregistrement des données du client
function Enregistrer()
{
   // Verification des données obligatoires
   if ((document.fiche.LIB_STAT.value == "") || (document.fiche.REQ_SQL.value == "")) {
      alert ("<i18n:message key="ficStat.libelleRequeteManquant" />");
      return;
   }
   if ((document.fiche.Action.value != "Creation") && (document.fiche.Action.value != "Modification")) {
      document.fiche.Action.value = "Modification";
   }
   document.fiche.submit();
}

// Suppression du client
function Supprimer()
{
    if ((document.fiche.CD_STAT.value != "0") && (document.fiche.CD_STAT.value != "")) {
        if (confirm ("<i18n:message key="message.suppressionDefinitiveConfirm" />")) {
            document.fiche.Action.value = "Suppression";
            document.fiche.submit();
        }
    }
}

// Duplication de la prestation
function Dupliquer()
{
   document.fiche.Action.value = "Duplication";
   document.fiche.submit();
}

function RetourListe()
{
   parent.location.href = "ListeStat.jsp";
}

// Affichage de l'aide
function Aide()
{
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideFiche.html");
}

</script>
</body>
</html>
