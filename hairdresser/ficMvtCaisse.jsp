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
<%@ page import="java.util.Vector" %>
<%@ page import="com.increg.salon.bean.SalonSession,
	       com.increg.salon.bean.MvtCaisseBean,
	       com.increg.salon.bean.CaisseBean,
	       com.increg.salon.bean.ModReglBean
	       " %>
<%
SalonSession mySalon = com.increg.salon.servlet.ConnectedServlet.CheckOrGoHome(request, response);
%>
<%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %>
<%@ taglib uri="WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<i18n:bundle baseName="messages" locale="<%= mySalon.getLangue() %>"/>
<html>
<head>
<title><i18n:message key="ficMvtCaisse.title" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
<link rel="stylesheet" href="style/jquery-ui-1.8.16.custom.css" type="text/css">
</head>
<body class="donnees" onLoad="Init();document.fiche.CD_MOD_REGL.focus()">
<%@ include file="include/commun.jsp" %>
<script language="JavaScript">
<!--
<%
   // Récupération des paramètres
   String Action = (String) request.getAttribute("Action");
   MvtCaisseBean aMvt = (MvtCaisseBean) request.getAttribute("MvtCaisseBean");
   Vector listeCaisse = (Vector) request.getAttribute("listeCaisse");
%>
   var Action="<%=Action%>";

function Init() {
   <%
   // Positionne les liens d'actions
   if (! Action.equals("Creation")) {
      %>
      MM_showHideLayers('SUPPRIMER?bottomFrame','','hide');
      MM_showHideLayers('ENREGISTRER?bottomFrame','','show');
      <%
   }
   if (Action.equals("Creation")) { %>
      // Pas de lien supprimer
      MM_showHideLayers('SUPPRIMER?bottomFrame','','hide');
      MM_showHideLayers('ENREGISTRER?bottomFrame','','show');
   <%
   } %>
   MM_showHideLayers('RETOUR_LISTE?bottomFrame','','show');
}
//-->
</script>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/ficMvtCaisse.gif" alt=<salon:TimeStamp bean="<%= aMvt %>" />></h1>
<salon:message salonSession="<%= mySalon %>" />
<form method="post" action="ficMvtCaisse.srv" name="fiche">
	<p> 
		<input type="hidden" name="Action" value="<%=Action%>">
		<span class="obligatoire"><i18n:message key="label.dtMouvement" /> :</span> 
	        <salon:valeur valeurNulle="null" valeur="<%= aMvt.getDT_MVT() %>" >
		  <input type="hidden" name="DT_MVT" value="%%">
		  <span class="readonly">%%</span>
	        </salon:valeur>
	 </p>
	 <p>
	       <span class="obligatoire"><i18n:message key="label.modeRegl" /> :</span> 
	       <salon:DBselection valeur="<%= aMvt.getCD_MOD_REGL() %>" sql='<%= "select CD_MOD_REGL, LIB_MOD_REGL from MOD_REGL where CD_MOD_REGL not in (" + Integer.toString(ModReglBean.MOD_REGL_ESP_FRF) + "," + Integer.toString(ModReglBean.MOD_REGL_CHQ_FRF) + ") order by LIB_MOD_REGL"%>' msgManquant="true">
		     <select name="CD_MOD_REGL" onchange="ChangeMvt()">
			%%
		     </select>
	       </salon:DBselection>
	       <i18n:message key="label.soldeCaisse" /> : 
	       <input class="Nombre" type="text" name="SOLDE_AVANT" disabled value="" size=7 onchange="ChangeMvt()">
	       <%= mySalon.getDevise().toString() %>
	 </p>
	 <p>
	       <span class="obligatoire"><i18n:message key="label.typeMouvement" /> :</span> 
	       <salon:DBselection valeur="<%= aMvt.getCD_TYP_MCA() %>" sql="select CD_TYP_MCA, LIB_TYP_MCA || ' (' || SENS_MCA::varchar || ')' from TYP_MCA order by LIB_TYP_MCA" msgManquant="true">
		     <select name="CD_TYP_MCA" onchange="ChangeMvt()">
			%%
		     </select>
	       </salon:DBselection>
	 </p>
	 <p>
	       <span class="obligatoire"><i18n:message key="label.montant" /> :</span> 
	       <salon:valeur valeurNulle="null" valeur="<%= aMvt.getMONTANT() %>" >
		  <input class="Nombre" type="text" name="MONTANT" value="%%" size="8">
	       </salon:valeur>
	       <%= mySalon.getDevise().toString() %>
	</p>
	<p>
	 <span class="facultatif"><i18n:message key="label.commentaire" /> :</span> 
	    <salon:valeur valeurNulle="null" valeur="<%= aMvt.getCOMM() %>" >
		<textarea name="COMM" cols="40" rows="2">%%</textarea>
	    </salon:valeur>
	</p>
</form>

<script language="JavaScript">
// Fonctions d'action
ChangeMvt();

// Affiche le solde en fonction du mode de réglement selectionné
function ChangeMvt() {

CD_MOD_REGL = new Array();
SOLDE = new Array();
   
   <%
   for (int i = 0; i < listeCaisse.size(); i++) {
      CaisseBean aCaisse = (CaisseBean) listeCaisse.get(i);

      %>
      CD_MOD_REGL[<%= i %>] = <%= aCaisse.getCD_MOD_REGL() %>;
      SOLDE[<%= i %>] = <%= aCaisse.getSOLDE() %>;
      <%
   }
   %>
   for (i = 0; i < document.fiche.CD_MOD_REGL.options.length; i++) {
      if (document.fiche.CD_MOD_REGL.options[i].selected) {
	 for (k = 0; k < CD_MOD_REGL.length; k++) {
	    if (CD_MOD_REGL[k] == document.fiche.CD_MOD_REGL.options[i].value) {
	       document.fiche.SOLDE_AVANT.value = SOLDE[k];
	    }
	 }
      }
   }
}

// Contrôle des données avant enregistrement
function ControleEnreg ()
{
   // Verification des données obligatoires
   if (document.fiche.MONTANT.value == "") {
      alert ("<i18n:message key="ficMvtCaisse.montantManquant" />");
      return false;
   }
   return true;
}

// Enregistrement des données du client
function Enregistrer()
{
   // Verification des données obligatoires
   if (! ControleEnreg()) {
      return;
   }
   document.fiche.submit();
}

// Affichage de l'aide
function Aide()
{
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideFiche.html");
}

function RetourListe()
{
    parent.location.href = "ListeMvtCaisse.jsp";
}

</script>
</body>
</html>
