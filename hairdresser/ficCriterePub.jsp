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
	       com.increg.salon.bean.CriterePubBean" %>
<%
SalonSession mySalon = com.increg.salon.servlet.ConnectedServlet.CheckOrGoHome(request, response);
%>
<%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %>
<%@ taglib uri="WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<i18n:bundle baseName="messages" locale="<%= mySalon.getLangue() %>"/>
<html>
<head>
<title><i18n:message key="ficCriterePub.title" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
<link rel="stylesheet" href="style/jquery-ui-1.8.16.custom.css" type="text/css">
</head>
<body class="donnees" onLoad="Init();document.fiche.LIB_CRITERE_PUB.focus()">
<%@ include file="include/commun.jsp" %>
<script language="JavaScript">
<!--
<%
   // R�cup�ration des param�tres
   String Action = (String) request.getAttribute("Action");
   CriterePubBean aCriterePub = (CriterePubBean) request.getAttribute("CriterePubBean");
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
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/ficPub.gif" alt=<salon:TimeStamp bean="<%= aCriterePub %>" />></h1>
<salon:message salonSession="<%= mySalon %>" />
<form method="post" action="ficCriterePub.srv" name="fiche">
	<p> 
		<salon:valeur valeurNulle="0" valeur="<%= aCriterePub.getCD_CRITERE_PUB() %>" >
		  <input type="hidden" name="CD_CRITERE_PUB" value="%%" >
	        </salon:valeur>
		<input type="hidden" name="Action" value="<%=Action%>">
		<span class="obligatoire"><i18n:message key="label.libelle" /> :</span> 
		<salon:valeur valeurNulle="null" valeur="<%= aCriterePub.getLIB_CRITERE_PUB() %>" >
		  <input type="text" name="LIB_CRITERE_PUB" value="%%" size=80>
	        </salon:valeur>
       </p>
	 <p>
	        <span class="obligatoire"><i18n:message key="label.clause" /> :</span> 
	        <salon:valeur valeurNulle="null" valeur="<%= aCriterePub.getCLAUSE() %>" >
		  <textarea name="CLAUSE" rows="5" cols="80">%%</textarea>
	        </salon:valeur>
	        <a href="javascript:Visualiser()"><img src="images/pub2.gif" border=0 alt="Test de la requ�te"></a>
	 </p>
</form>

<script language="JavaScript">
// Fonctions d'action

// Suppression du client
function Visualiser()
{
   parent.location.href = "_FicheCriterePub.jsp?Action=Construction&CD_CRITERE_PUB=" + document.fiche.CD_CRITERE_PUB.value;
}


// Enregistrement des donn�es du client
function Enregistrer()
{
   // Verification des donn�es obligatoires
   if ((document.fiche.LIB_CRITERE_PUB.value == "") || (document.fiche.CLAUSE.value == "")) {
      alert ("<i18n:message key="ficCriterePub.libelleClauseManquant" />");
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
    if ((document.fiche.CD_CRITERE_PUB.value != "0") && (document.fiche.CD_CRITERE_PUB.value != "")) {
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
   parent.location.href = "ListeCriterePub.jsp";
}

// Affichage de l'aide
function Aide()
{
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideFiche.html");
}

</script>
</body>
</html>
