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
<%@ page import="java.util.Vector,java.math.BigDecimal" %>
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
<title><i18n:message key="ficInventaire.title" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees">
<%@ include file="include/commun.js" %>
<script language="JavaScript">
<!--
<%
   // Récupération des paramètres
   String Action = (String) request.getAttribute("Action");
   String NbArt = (String) request.getAttribute("NbArt");
   String CD_TYP_ART = request.getParameter("CD_TYP_ART");
   String CD_CATEG_ART = request.getParameter("CD_CATEG_ART");
   String CD_TYP_MVT = request.getParameter("CD_TYP_MVT");
%>
   var Action="<%=Action%>";

function Init() {
    // Pas de lien supprimer
    MM_showHideLayers('SUPPRIMER?bottomFrame','','hide');
    MM_showHideLayers('ENREGISTRER?bottomFrame','','show');
}
//-->
</script>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/ficInventaire.gif"></h1>
<salon:message salonSession="<%= mySalon %>" />
<form method="post" action="ficInventaire.srv" name="fiche">
<p><i18n:message key="label.typeArticle" /> :
<salon:DBselection valeur="<%= CD_TYP_ART %>" sql="select CD_TYP_ART, LIB_TYP_ART from TYP_ART order by LIB_TYP_ART">
   <select name="CD_TYP_ART" onChange="document.fiche.submit()">
      <option value=""><i18n:message key="label.tousDsListe" /></option>
      %%
   </select>
</salon:DBselection>
<i18n:message key="label.categorieArticle" /> :
<salon:DBselection valeur="<%= CD_CATEG_ART %>" sql="select CD_CATEG_ART, LIB_CATEG_ART from CATEG_ART order by LIB_CATEG_ART">
   <select name="CD_CATEG_ART" onChange="document.fiche.submit()">
      <option value=""><i18n:message key="label.toutesDsListe" /></option>
      %%
   </select>
</salon:DBselection>
<input name="Action" type="hidden" value="">
</p>
<hr>
<i18n:message key="label.typeMouvement" /> :
<salon:DBselection valeur="<%= CD_TYP_MVT %>" sql='<%= "select CD_TYP_MVT, LIB_TYP_MVT from TYP_MVT where SENS_MVT=\'" + TypMvtBean.SENS_INVENTAIRE + "\' order by LIB_TYP_MVT" %>'>
   <select name="CD_TYP_MVT">
      %%
   </select>
</salon:DBselection>
<table width="100%" border="1" >
	<tr>
		<th><i18n:message key="label.libelle" /></th>
		<th><i18n:message key="label.typeArticleListe" /></th>
		<th><i18n:message key="label.categorieArticle" /></th>
		<th><i18n:message key="label.qteStockTableau" /></th>
		<th><i18n:message key="label.valeurStockUnitTableau" /></th>
		<th><i18n:message key="label.valeurStockGlobal" /></th>
		<th><i18n:message key="label.dernierInventaire" /></th>
		<th><i18n:message key="label.qteCompte" /></th>
		<th><i18n:message key="label.valeurInventaireUnit" /></th>
	</tr>
	<%
	// Recupère la liste
	Vector lstLignes = (Vector) request.getAttribute("Liste");
	Vector lstMvt = (Vector) request.getAttribute("ListeMvt");
	BigDecimal valeurTotale = new BigDecimal("0.00");
	    
	for (int i=0; i< lstLignes.size(); i++) {
	    ArtBean aArt = (ArtBean) lstLignes.get(i);
	    MvtStkBean aMvt = (MvtStkBean) lstMvt.get(i);
	%>
	<tr>
	    
		<td><a href="_FicheArt.jsp?Action=Modification&CD_ART=<%= aArt.getCD_ART() %>" target="ClientFrame"><%= aArt.toString() %></a></td>
	    <td>
	    <% String LIB_TYP_ART = DonneeRefBean.getDonneeRefBean(mySalon.getMyDBSession(), "TYP_ART",
							       Integer.toString(aArt.getCD_TYP_ART())).toString(); %>
	    <salon:valeur valeur="<%= LIB_TYP_ART %>" valeurNulle="null"> %% </salon:valeur>
            <% if (aArt.getINDIC_MIXTE().equals("O")) { %>
                - Mixte
            <% } %>
	    </td>
	    <td>
	    <% String LIB_CATEG_ART = "";
	       if (aArt.getCD_CATEG_ART() != 0) {
		  LIB_CATEG_ART = DonneeRefBean.getDonneeRefBean(mySalon.getMyDBSession(), "CATEG_ART",
							       Integer.toString(aArt.getCD_CATEG_ART())).toString();
	       } %>
	    <salon:valeur valeur="<%= LIB_CATEG_ART %>" valeurNulle="null"> %%&nbsp; </salon:valeur>
	    </td>
	    <td class="Nombre"><salon:valeur valeur="<%= aArt.getQTE_STK() %>" valeurNulle="null">%%</salon:valeur></td>
	    <td class="Nombre"><salon:valeur valeur="<%= aArt.getVAL_STK_HT() %>" valeurNulle="null">%%</salon:valeur></td>
	    <%
	       BigDecimal valeurGlobale = null;
	       if ((aArt.getQTE_STK() != null) && (aArt.getVAL_STK_HT() != null)) {
		  valeurGlobale = aArt.getVAL_STK_HT().multiply(aArt.getQTE_STK()).setScale(2, BigDecimal.ROUND_HALF_UP);
		  valeurTotale = valeurTotale.add(valeurGlobale);
	       }
	    %>
	    <td class="Nombre"><salon:valeur valeur="<%= valeurGlobale %>" valeurNulle="null">%%</salon:valeur></td>
            <i18n:message key="format.dateSimpleDefaut" id="formatDate" />
	    <td><salon:valeur valeur="<%= aMvt.getDT_MVT() %>" valeurNulle="null" format="<%= formatDate %>"><a href="_FicheArt_Mvt.jsp?Action=Modification&CD_ART=<%= aArt.getCD_ART() %>" target="ClientFrame">%%</a>&nbsp;</salon:valeur></td>
	    <td class="Nombre">
            <% if (request.getAttribute("RAZ") != null) { %>
                <input type="text" name="QTE_STK<%= aArt.getCD_ART() %>" value="" size=5>
            <% } else { %>
                <salon:valeur valeur='<%= request.getParameter("QTE_STK" + Long.toString(aArt.getCD_ART())) %>' valeurNulle="null">
                    <input type="text" name="QTE_STK<%= aArt.getCD_ART() %>" value="%%" size=5></salon:valeur>
            <% } %>
            </td>
            <%
                String val = request.getParameter("VAL_STK_HT" + Long.toString(aArt.getCD_ART()));
                if (val == null) {
                    val = aArt.getVAL_STK_HT().toString();
                }
            %>
	    <td class="Nombre"><salon:valeur valeur='<%= val %>' valeurNulle="null">
                <input type="text" name="VAL_STK_HT<%= aArt.getCD_ART() %>" value="%%" size=5></salon:valeur></td>
	</tr>
	<%
	}
	%>
	<tr>
	    <td></td>
	    <td></td>
	    <td></td>
	    <td></td>
	    <td></td>
	    <td class="Nombre"><salon:valeur valeurNulle="null" valeur="<%= valeurTotale %>">%%</salon:valeur></td>
	    <td></td>
	    <td></td>
	    <td></td>
	</tr>
</table>
<salon:madeBy />
</form>

<script language="JavaScript">
// Fonctions d'action

// Enregistrement des données d'inventaire
function Enregistrer()
{
   document.fiche.Action.value="Creation";
   document.fiche.submit();
}

// Affichage de l'aide
function Aide()
{
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideFicheInventaire.html");
}

</script>
</body>
</html>
