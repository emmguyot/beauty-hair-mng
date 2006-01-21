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
	       com.increg.salon.bean.ModReglBean" %>
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
<%
   // Récupération des paramètres
   String Action = (String) request.getAttribute("Action");
   ModReglBean aModRegl = (ModReglBean) request.getAttribute("ModReglBean");
%>
<title><i18n:message key="ficModRegl.title" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees" onLoad="Init();document.fiche.LIB_MOD_REGL.focus()">
<%@ include file="include/commun.js" %>
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
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/ficParam.gif"><br><span class="ssTitre"><i18n:message key="ficModRegl.sousTitre" /></span></h1>
<salon:message salonSession="<%= mySalon %>" />
<form method="post" action="ficModRegl.srv" name="fiche">
	<p> 
		<salon:valeur valeurNulle="0" valeur="<%= aModRegl.getCD_MOD_REGL() %>" >
		  <input type="hidden" name="CD_MOD_REGL" value="%%" >
	        </salon:valeur>
		<input type="hidden" name="Action" value="<%=Action%>">
		<span class="obligatoire"><i18n:message key="label.libelle" />:</span> 
		<salon:valeur valeurNulle="null" valeur="<%= aModRegl.getLIB_MOD_REGL() %>" >
		  <input type="text" name="LIB_MOD_REGL" value="%%" size=40>
	        </salon:valeur>
		<span class="obligatoire"><i18n:message key="" /><i18n:message key="label.modReglActuel" /> :</span> 
                <i18n:message key="valeur.ouiNon" id="paramActuel" />
		<salon:selection valeur="<%= aModRegl.getUTILISABLE() %>" valeurs='<%= "O|N" %>' libelle="<%= paramActuel %>">
		  <select name="UTILISABLE">
		     %%
		  </select>
		</salon:selection>
	</p>
        <p>
		<span class="obligatoire"><i18n:message key="label.impressionCheque" /> :</span> 
                <i18n:message key="valeur.ouiNon" id="paramCheque" />
		<salon:selection valeur="<%= aModRegl.getIMP_CHEQUE() %>" valeurs='<%= "O|N" %>' libelle="<%= paramCheque %>">
		  <select name="IMP_CHEQUE">
		     %%
		  </select>
		</salon:selection>
		<span class="obligatoire"><i18n:message key="label.renduMonnaie" /> :</span> 
                <i18n:message key="valeur.ouiNon" id="paramMonnaie" />
		<salon:selection valeur="<%= aModRegl.getRENDU_MONNAIE() %>" valeurs='<%= "O|N" %>' libelle="<%= paramMonnaie %>">
		  <select name="RENDU_MONNAIE">
		     %%
		  </select>
		</salon:selection>
        </p>
</form>
<script language="JavaScript">
// Fonctions d'action

// Enregistrement des données du client
function Enregistrer()
{
   // Verification des données obligatoires
   if (document.fiche.LIB_MOD_REGL.value == "") {
      alert ("<i18n:message key="ficModRegl.libelleManquant" />");
      return;
   }
   document.fiche.submit();
}

// Suppression du client
function Supprimer()
{
    if ((document.fiche.CD_MOD_REGL.value != "0") && (document.fiche.CD_MOD_REGL.value != "")) {
        if (confirm ("<i18n:message key="message.suppressionDefinitiveConfirm" />")) {
            document.fiche.Action.value = "Suppression";
            document.fiche.submit();
        }
    }
}

// Duplication de la prestation
function Dupliquer()
{
   if (document.fiche.LIB_MOD_REGL.value == "") {
      alert ("<i18n:message key="ficModRegl.libelleManquant" />");
      return;
   }
   document.fiche.Action.value = "Duplication";
   document.fiche.submit();
}

// Affichage de l'aide
function Aide()
{
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideFiche.html");
}

function RetourListe()
{
    parent.location.href = "ListeModRegl.jsp";
}

</script>
</body>
</html>
