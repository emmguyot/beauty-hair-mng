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
<%@ page import="java.util.Vector,java.math.BigDecimal,java.util.Calendar" %>
<%@ page import="com.increg.salon.bean.SalonSession,
               com.increg.salon.bean.ArtBean,
	       com.increg.salon.bean.MvtStkBean,
	       com.increg.salon.bean.TypMvtBean,
	       com.increg.salon.bean.DonneeRefBean" %>
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
<title><i18n:message key="ficAchat.title" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees">
<%@ include file="include/commun.jsp" %>
<script language="JavaScript">
<!--
<%
   // Récupération des paramètres
   String Action = (String) request.getAttribute("Action");
   String CD_FOURN = request.getParameter("CD_FOURN");
   String CD_TYP_MVT = request.getParameter("CD_TYP_MVT");
   String CD_CMD_FOURN = request.getParameter("CD_CMD_FOURN");

   String CD_CATEG_ART = (String) request.getAttribute("CD_CATEG_ART");
   String CD_ART = (String) request.getAttribute("CD_ART");

   BigDecimal QTE_STK = (BigDecimal) request.getAttribute("QTE_STK");
   BigDecimal VAL_STK_HT = (BigDecimal) request.getAttribute("VAL_STK_HT");
   Calendar DT_MVT = (Calendar) request.getAttribute("DT_MVT");
   BigDecimal QTE = (BigDecimal) request.getAttribute("QTE");
   BigDecimal VAL_MVT_HT = (BigDecimal) request.getAttribute("VAL_MVT_HT");
%>
   var Action="<%=Action%>";

function Init() {
    // Pas de lien supprimer
    MM_showHideLayers('SUPPRIMER?bottomFrame','','hide');
    MM_showHideLayers('ENREGISTRER?bottomFrame','','hide');
}
//-->
</script>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/ficAchat.gif"></h1>
<salon:message salonSession="<%= mySalon %>" />
<form method="post" action="ficAchat.srv" name="fiche">
<p>
<i18n:message key="label.commande" /> : 
<salon:valeur valeur="<%= CD_CMD_FOURN %>" valeurNulle="null">
    <input name="CD_CMD_FOURN" type="text" maxlength="10" size="10" value="%%">
</salon:valeur>
<i18n:message key="label.fournisseur" /> :
<salon:DBselection valeur="<%= CD_FOURN %>" sql="select CD_FOURN, RAIS_SOC from FOURN order by RAIS_SOC">
   <select name="CD_FOURN" onChange="Recharge(0)">
      %%
   </select>
</salon:DBselection>
<%
CD_FOURN = ((CD_FOURN == null) || (CD_FOURN.length() == 0) || (CD_FOURN.equals("0"))) ?
                    (String) request.getAttribute("Premier") : 
                    CD_FOURN;
%>
</p>
<input name="Action" type="hidden" value="">
<input name="paramSup1" type="hidden" value="">
<input name="paramSup2" type="hidden" value="">
<i18n:message key="label.typeMouvement" /> :
<salon:DBselection valeur="<%= CD_TYP_MVT %>" sql='<%= "select CD_TYP_MVT, LIB_TYP_MVT from TYP_MVT where SENS_MVT=\'" + TypMvtBean.SENS_ENTREE + "\' order by LIB_TYP_MVT" %>'>
   <select name="CD_TYP_MVT">
      %%
   </select>
</salon:DBselection>
<table width="100%" border="0" >
	<tr>
		<th><i18n:message key="label.categorie" /></th>
		<th><i18n:message key="label.libelle" /></th>
		<th><i18n:message key="label.qteStockTableau" /></th>
		<th><i18n:message key="label.valeurStockUnitTableau" /></th>
		<th><i18n:message key="label.dernierAchat" /></th>
		<th><i18n:message key="label.qteAchat" /></th>
		<th><i18n:message key="label.valeurAchatUnit" /></th>
                <th></th>
	</tr>
	<%
	// Recupère la liste
	Vector lstLignes = (Vector) request.getAttribute("Liste");
	Vector lstMvt = (Vector) request.getAttribute("ListeMvt");
	BigDecimal valeurTotale = new BigDecimal("0.00");
	    
        // Ligne de travail
	%>
	<tr class="ligneTab1">
	    <td class="tabDonnees">
                <%
                String reqSQL = "select distinct CATEG_ART.CD_CATEG_ART, CATEG_ART.LIB_CATEG_ART from CATEG_ART, ART, CAT_FOURN"
                            + " where ART.CD_CATEG_ART=CATEG_ART.CD_CATEG_ART"
                            + " and CAT_FOURN.CD_ART = ART.CD_ART and CAT_FOURN.CD_FOURN=" + CD_FOURN
                            + " and ART.INDIC_PERIM='N'"
                            + " order by CATEG_ART.LIB_CATEG_ART";
                %>
                <salon:DBselection valeur="<%= CD_CATEG_ART %>" sql='<%= reqSQL %>'>
                    <select name="CD_CATEG_ART" onChange="Recharge(1)">
                    <option value=""></option>
                    %%
                    </select>
                </salon:DBselection>
                <%
                CD_CATEG_ART = ((CD_CATEG_ART == null) || (CD_CATEG_ART.length() == 0) || (CD_CATEG_ART.equals("0"))) ?
                                    (String) request.getAttribute("Premier") : 
                                    CD_CATEG_ART;
                %>
	    </td>
            <td class="tabDonnees">
                <%
                reqSQL = "select ART.CD_ART, ART.LIB_ART from ART, CAT_FOURN where CD_CATEG_ART=" + CD_CATEG_ART
                            + " and CAT_FOURN.CD_ART = ART.CD_ART and CAT_FOURN.CD_FOURN=" + CD_FOURN
                            + " and ART.INDIC_PERIM='N'"
                            + " order by ART.LIB_ART";
                %>
                <salon:DBselection valeur="<%= CD_ART %>" sql='<%= reqSQL %>'>
                    <select name="CD_ART" onChange="Recharge(2)">
                    <option value=""></option>
                    %%
                    </select>
                </salon:DBselection>
            </td>
	    <td class="Nombre"><salon:valeur valeur="<%= QTE_STK %>" valeurNulle="null">%%&nbsp;</salon:valeur></td>
	    <td class="Nombre"><salon:valeur valeur="<%= VAL_STK_HT %>" valeurNulle="null">%%&nbsp;</salon:valeur></td>
            <i18n:message key="format.dateSimpleDefaut" id="formatDate" />
	    <td class="tabDonnees"><salon:valeur valeur="<%= DT_MVT %>" valeurNulle="null" format="<%= formatDate %>">%%&nbsp;</salon:valeur></td>
	    <td class="tabDonnees">
                <salon:valeur valeur='<%= QTE %>' valeurNulle="null">
                    <input type="text" name="QTE" value="%%" size="3">
                </salon:valeur>
            </td>
	    <td class="tabDonnees">
                <salon:valeur valeur='<%= VAL_MVT_HT %>' valeurNulle="null">
                    <input type="text" name="VAL_MVT_HT" value="%%" size="5">
                </salon:valeur>
            </td>
            <td class="tabDonnees">
                <a href="javascript:AjouterLigne()"><img src=images/plus.gif width="15" height="15" border="0" alt="<i18n:message key="label.enregistrerLigne" />"></a>
            </td>
        </tr>
        <%
        // Lignes déjà saisies
	for (int i = lstLignes.size() - 1; i >= 0; i--) {
	    MvtStkBean aMvtAchat = (MvtStkBean) lstLignes.get(i);
	    ArtBean aArt = ArtBean.getArtBean(mySalon.getMyDBSession(), Long.toString(aMvtAchat.getCD_ART()), mySalon.getMessagesBundle());
	    MvtStkBean aMvt = (MvtStkBean) lstMvt.get(i);
	%>
	<tr>
	    <td class="tabDonnees">
	    <% String LIB_CATEG_ART = "";
	       if (aArt.getCD_CATEG_ART() != 0) {
		  LIB_CATEG_ART = DonneeRefBean.getDonneeRefBean(mySalon.getMyDBSession(), "CATEG_ART",
							       Integer.toString(aArt.getCD_CATEG_ART())).toString();
	       } %>
	    <salon:valeur valeur="<%= LIB_CATEG_ART %>" valeurNulle="null"> %%&nbsp; </salon:valeur>
	    </td>
            <td class="tabDonnees"><a href="_FicheArt.jsp?Action=Modification&CD_ART=<%= aArt.getCD_ART() %>" target="ClientFrame"><%= aArt.toString() %></a></td>
	    <td class="Nombre"><salon:valeur valeur="<%= aArt.getQTE_STK() %>" valeurNulle="null">%%</salon:valeur></td>
	    <td class="Nombre"><salon:valeur valeur="<%= aArt.getVAL_STK_HT() %>" valeurNulle="null">%%</salon:valeur></td>
	    <td class="tabDonnees"><salon:valeur valeur="<%= (aMvt != null) ? aMvt.getDT_MVT() : null %>" valeurNulle="null" format="<%= formatDate %>"><a href="_FicheArt_Mvt.jsp?Action=Modification&CD_ART=<%= aArt.getCD_ART() %>" target="ClientFrame">%%</a>&nbsp;</salon:valeur></td>
	    <td class="Nombre">
                <salon:valeur valeur='<%= aMvtAchat.getQTE() %>' valeurNulle="null">
                        %%
                </salon:valeur>
            </td>
	    <td class="Nombre">
                <salon:valeur valeur='<%= aMvtAchat.getVAL_MVT_HT() %>' valeurNulle="null">
                    %%
                </salon:valeur>
            </td>
            <td>
                <salon:valeur valeur='<%= aMvtAchat.getDT_MVT() %>' valeurNulle="null" timezone="true">
                    <a href="javascript:SupprimerLigne(<%= aArt.getCD_ART() %>, '%%')"><img src=images/moins.gif width="15" height="15" border="0" alt="<i18n:message key="label.supprimerLigne" />"></a>
                </salon:valeur>
            </td>
	</tr>
	<%
	}
        %>
</table>
<salon:madeBy />
</form>

<script language="JavaScript">
// Fonctions d'action

// Recharge l'écran pour mettre à jour les prestations ou le prix
function Recharge(niveau)
{
   submitOk = false;
   if (niveau == 0) {
      document.fiche.Action.value = "RechargementFourn";
      submitOk = true;
   }
   else if (niveau == 1) {
      // RAZ de la prestation
      if (document.fiche.CD_CATEG_ART.selectedIndex > 0) {
		  document.fiche.Action.value = "Rechargement";
		  submitOk = true;
	  }
   }
   else if (niveau == 2) {
      if (document.fiche.CD_ART.selectedIndex > 0) {
		  document.fiche.Action.value = "Rechargement+";
		  submitOk = true;
	  }
   }
   if (submitOk) {
	   document.fiche.submit();
   }
}

// Contrôle des données avant enregistrement
function ControleEnreg ()
{
   // Verification des données obligatoires
   if (document.fiche.CD_CMD_FOURN.value == "") {
      alert ("<i18n:message key="ficAchat.commandeManquant" />");
      return false;
   }
   if (document.fiche.CD_CATEG_ART.selectedIndex <= 0) {
      alert ("<i18n:message key="ficAchat.categorieManquant" />");
      return false;
   }
   if (document.fiche.CD_ART.selectedIndex <= 0) {
      alert ("<i18n:message key="ficAchat.articleManquant" />");
      return false;
   }
   // Verification des données obligatoires
   if (document.fiche.QTE.value == "") {
      alert ("<i18n:message key="ficAchat.quantiteManquant" />");
      return false;
   }
   if (document.fiche.VAL_MVT_HT.value == "") {
      alert ("<i18n:message key="ficAchat.valeurAchatManquant" />");
      return false;
   }
   return true;
}

// Ajoute une ligne de prestation
function AjouterLigne()
{
   // Verification des données obligatoires
   if (! ControleEnreg()) {
      return;
   }
   document.fiche.Action.value = "AjoutLigne";
   document.fiche.submit();
}

// Supprime une ligne d'achat
function SupprimerLigne(cdArt, dtMvt)
{
   document.fiche.Action.value = "SuppressionLigne";
   document.fiche.paramSup1.value = cdArt;
   document.fiche.paramSup2.value = dtMvt;
   document.fiche.submit();
}

// Affichage de l'aide
function Aide()
{
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideFicheAchat.html");
}

</script>
</body>
</html>
