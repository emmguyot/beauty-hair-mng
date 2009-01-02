<%
/*
 * This program is part of InCrEG LibertyLook software http://beauty-hair-mng.sourceforge.net
 * Copyright (C) 2001-2009 Emmanuel Guyot <See emmguyot on SourceForge> 
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
	       com.increg.salon.bean.ArtBean,
	       com.increg.salon.bean.CatFournBean,
	       com.increg.salon.bean.FournBean
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
<title><i18n:message key="ficArt.title" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees" onLoad="Init();document.fiche.LIB_ART.focus()">
<%@ include file="include/commun.jsp" %>
<script language="JavaScript">
<!--
<%
   // Récupération des paramètres
   String Action = (String) request.getAttribute("Action");
   ArtBean aArt = (ArtBean) request.getAttribute("ArtBean");
%>
   var Action="<%=Action%>";

function Init() {
    <%
    // Positionne les liens d'actions
    if (! Action.equals("Creation")) {
        %>
        MM_showHideLayers('SUPPRIMER?bottomFrame','','show');
        <%
    }
    if (Action.equals("Creation")) { %>
        // Pas de lien supprimer
        MM_showHideLayers('SUPPRIMER?bottomFrame','','hide');
    <%
    } %>
    MM_showHideLayers('ENREGISTRER?bottomFrame','','show');
    MM_showHideLayers('DUPLIQUER?bottomFrame','','show');
    MM_showHideLayers('RETOUR_LISTE?bottomFrame','','show');
}
//-->
</script>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/ficArt.gif" alt=<salon:TimeStamp bean="<%= aArt %>" />></h1>
<salon:message salonSession="<%= mySalon %>" />
<form method="post" action="ficArt.srv" name="fiche">

<%@ include file="ficArt_Common.jsp" %>

<% if (Action != "Creation") { %>
   <table border="0" cellspacing="0" width="100%">
   <tr>
      <td align="right"> <span class="souslien"> 
		<a href="_FicheArt_Mvt.jsp?Action=Modification&CD_ART=<%= aArt.getCD_ART() %>" target="ClientFrame"><i18n:message key="ficArt.mvtArticle" /></a> </span> </td>
   </tr>
   </table>
<% } %>
   <table border="0" width="100%">
	 <tr>
		  <th><i18n:message key="label.fournisseur" /></th>
		  <th><i18n:message key="label.libelle" /></th>
		  <th><i18n:message key="label.reference" /></th>
		  <th><i18n:message key="label.miniCommande" /></th>
		  <th><i18n:message key="label.prixUnitaireHT" /></th>
		  <th><i18n:message key="label.principal" /></th>
		  <th>&nbsp;</th>
	 </tr>
	 <%
	 // Recupère la liste
	 Vector listeFourn = (Vector) request.getAttribute("listeFourn");
	 String finClass = "1";
	 int i = 1;
	       
	 %>
	 <input type="hidden" name="NbFourn" value="<%= listeFourn.size() %>">
         <i18n:message key="valeur.nonOui" id="valeurNonOui" />
	 <%
	 for (i=0; i< listeFourn.size(); i++) {
	       CatFournBean aFourn = (CatFournBean) listeFourn.get(i);
	       finClass = (finClass.equals("1")) ? "2" : "1";
	 %>
	 <tr class="ligneTab2">
	    <td class="tabDonnees">
	        <salon:valeur valeurNulle="null" valeur="<%= aFourn.getCD_FOURN() %>" >
		  <input type="hidden" name="CD_FOURN<%= i %>" value="%%">
                  <a href="_FicheFourn.jsp?Action=Modification&CD_FOURN=%%" target="_parent">
	        </salon:valeur>
	        <salon:valeur valeurNulle="null" valeur="<%= FournBean.getFournBean(mySalon.getMyDBSession(), Integer.toString(aFourn.getCD_FOURN())).toString() %>" >
		  %%
	        </salon:valeur>
                </a>
	    </td>
	    <td class="tabDonnees"><salon:valeur valeurNulle="null" valeur="<%= aFourn.getLIB_ART() %>" >
		  <input type="text" name="LIB_ART<%= i %>" value="%%">
	        </salon:valeur>
	    </td>
	    <td class="tabDonnees"><salon:valeur valeur="<%= aFourn.getREF_ART() %>" valeurNulle="null">
		  <input type="text" name="REF_ART<%= i %>" value="%%" size=10>
	       </salon:valeur>
	    </td>
	    <td class="tabDonnees"><salon:valeur valeur="<%= aFourn.getQTE_CMD_MIN() %>" valeurNulle="null">
			%%
		  <input type="hidden" name="QTE_CMD_MIN<%= i %>" value="%%">
	       </salon:valeur>
	    </td>
	    <td class="tabDonnees"><salon:valeur valeur="<%= aFourn.getPRX_UNIT_HT() %>" valeurNulle="null">
		  <input class="Nombre" type="text" name="PRX_UNIT_HT<%= i %>" value="%%" size="5">
	       </salon:valeur>
	    </td>
	    <td class="tabDonnees">
		<salon:selection valeur="<%= aFourn.getFOURN_PRINC() %>" valeurs='<%= "N|O" %>' libelle="<%= valeurNonOui %>">
		  <select name="FOURN_PRINC<%= i %>" onChange="exclusionPrinc(<%= i %>)">
		     %%
		  </select>
		</salon:selection>
	    </td>
	    <td class="tabDonnees">
		  <a href="javascript:SupprimerLigne(<%= i %>)"><img src=images/moins.gif width="15" height="15" border="0" alt="<i18n:message key="label.supprimerLigne" />"></a>
	    </td>
	 </tr>
	 <%
	 }
	 // ****************** Ligne vide *************************************
	 %>
	 <tr class="ligneTab1">
	    <td class="tabDonnees">
	       <salon:DBselection valeur="<%= (String) null %>" sql="select CD_FOURN, RAIS_SOC from FOURN order by RAIS_SOC">
		     <select name="CD_FOURN<%= i %>">
			%%
		     </select>
	       </salon:DBselection>
	    </td>
	    <td class="tabDonnees">
	       <salon:valeur valeurNulle="null" valeur="<%= aArt.getLIB_ART() %>" >
		  <input type="text" name="LIB_ART<%= i %>" value="%%">
	        </salon:valeur>
	    </td>
	    <td class="tabDonnees"><salon:valeur valeur="<%= aArt.getREF_ART() %>" valeurNulle="null">
		  <input type="text" name="REF_ART<%= i %>" value="%%" size=10>
	       </salon:valeur>
	    </td>
	    <td class="tabDonnees"><salon:valeur valeur="<%= 0 %>" valeurNulle="null">
		  <input class="Nombre" type="text" name="QTE_CMD_MIN<%= i %>" value="%%" size=5>
	       </salon:valeur>
	    </td>
	    <td class="tabDonnees"><salon:valeur valeur="<%= 0 %>" valeurNulle="null">
		  <input class="Nombre" type="text" name="PRX_UNIT_HT<%= i %>" value="%%" size=5>
	       </salon:valeur>
	    </td>
	    <td class="tabDonnees">
		<salon:selection valeur='<%= (listeFourn.size()>0) ? "N" : "O" %>' valeurs='<%= "N|O" %>' libelle="<%= valeurNonOui %>">
		  <select name="FOURN_PRINC<%= i %>" onChange="exclusionPrinc(<%= i %>)">
		     %%
		  </select>
		</salon:selection>
	    </td>
	    <td class="tabDonnees">
		  <a href="javascript:AjouterLigne(<%= i %>)"><img src=images/plus.gif width="15" height="15" border="0" alt="<i18n:message key="label.ajouterLigne" />"></a>
	    </td>
	 </tr>
   </table>
</form>

<script language="JavaScript">
// Fonctions d'action
function set(Champs)
{
   document.fiche.elements[Champs + document.fiche.NbFourn.value].value = document.fiche.elements[Champs].value;
}

function exclusionPrinc(numFourn)
{
   if (document.fiche.elements["FOURN_PRINC" + numFourn].options[1].selected) {
      // Passage à Oui : Tous les autres à Non
      for ( i = 0 ; i <= document.fiche.NbFourn.value ; i++ ) {
	 if (i != numFourn) {
	    document.fiche.elements["FOURN_PRINC" + i].options[1].selected = false;
	    document.fiche.elements["FOURN_PRINC" + i].options[0].selected = true;
	 }
      }
   }
}

// Contrôle des données avant enregistrement
function ControleEnreg ()
{
   // Verification des données obligatoires
   if (document.fiche.LIB_ART.value == "") {
      alert ("<i18n:message key="message.suppressionDefinitiveConfirm" />");
      return false;
   }
   return true;
}

// Supprime une ligne de prestation
function SupprimerLigne(NumPrest)
{
   // Verification des données obligatoires
   if (! ControleEnreg()) {
      return;
   }
   document.fiche.Action.value = "SuppressionLigne";
   document.fiche.ParamSup.value = NumPrest;
   document.fiche.submit();
}

// Ajoute une ligne de prestation
function AjouterLigne(NumPrest)
{
   // Verification des données obligatoires
   if (! ControleEnreg()) {
      return;
   }
   document.fiche.Action.value = "AjoutLigne";
   document.fiche.ParamSup.value = NumPrest;
   document.fiche.submit();
}

// Enregistrement des données du client
function Enregistrer()
{
   // Verification des données obligatoires
   if (document.fiche.LIB_ART.value == "") {
      alert ("<i18n:message key="ficArt_Mvt.libelleManquant" />");
      return;
   }
   document.fiche.submit();
}

// Suppression du client
function Supprimer()
{
   if ((document.fiche.CD_ART.value != "0") && (document.fiche.CD_ART.value != "")) {
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
   if (document.fiche.LIB_ART.value != "") {
      parent.location.href = "ListeArt.jsp?premLettre=" + document.fiche.LIB_ART.value.charAt(0).toUpperCase();
   }
   else {
      parent.location.href = "ListeArt.jsp?premLettre=A";
   }
}


// Affichage de l'aide
function Aide()
{
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideFicheArt.html");
}

</script>
</body>
</html>
