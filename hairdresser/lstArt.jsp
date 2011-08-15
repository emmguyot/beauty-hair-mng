<%
/*
 * This program is part of InCrEG LibertyLook software http://beauty-hair-mng.sourceforge.net
 * Copyright (C) 2001-2011 Emmanuel Guyot <See emmguyot on SourceForge> 
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
<%@ page import="java.util.Vector, java.math.BigDecimal" %>
<%@ page import="com.increg.salon.bean.SalonSession,
	       com.increg.salon.bean.ArtBean,
	       com.increg.salon.bean.MvtStkBean,
	       com.increg.salon.bean.DonneeRefBean" %>
<%
SalonSession mySalon = com.increg.salon.servlet.ConnectedServlet.CheckOrGoHome(request, response);
%>
<%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %>
<%@ taglib uri="WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<i18n:bundle baseName="messages" locale="<%= mySalon.getLangue() %>"/>
<html>
<head>
<title><i18n:message key="title.lstArt" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees">
<%
   // Récupération des paramètres
   String premLettre = (String) request.getAttribute("premLettre");
   String CD_TYP_ART = request.getParameter("CD_TYP_ART");
   String CD_CATEG_ART = request.getParameter("CD_CATEG_ART");
   String RUPTURE = request.getParameter("RUPTURE");
   String PERIME = request.getParameter("PERIME");
%>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/lstArt.gif"></h1>
<form name="fiche" action="rechArt.srv" method="post">
<p><i18n:message key="label.premiereLettre" /> : 
<input type="hidden" name="premLettre" value="<%= premLettre %>">
<%
   String lien = "";
   if ((CD_TYP_ART != null) && (CD_TYP_ART.length() > 0)) {
      lien = lien + "&CD_TYP_ART=" + CD_TYP_ART;
   }
   if ((CD_CATEG_ART != null) && (CD_CATEG_ART.length() > 0)) {
      lien = lien + "&CD_CATEG_ART=" + CD_CATEG_ART;
   }
   if ((RUPTURE != null) && (RUPTURE.length() > 0)) {
      lien = lien + "&RUPTURE=" + RUPTURE;
   }
   if ((PERIME != null) && (PERIME.length() > 0)) {
      lien = lien + "&PERIME=" + PERIME;
   }
   // Affiche toutes les lettres avec un lien permettant de filtrer par cette lettre
   for (char c='A'; Character.isUpperCase(c); c++) { 
      if ((premLettre != null) && (premLettre.charAt(0) == c)) { %>
	 <%=c %>
      <% } else { %>
	 <a href="rechArt.srv?premLettre=<%= c + lien %>"><%=c %></a>     
   <% }
   }
   if ((premLettre != null) && (premLettre.charAt(0) == ' ')) { %>
	 Tous
      <% } else { %>
	 <a href="rechArt.srv?premLettre=%20<%= lien %>">Tous</a>     
   <% }
%>
</p>
<i18n:message key="label.typeArticle" /> :
<salon:DBselection valeur="<%= CD_TYP_ART %>" sql="select CD_TYP_ART, LIB_TYP_ART from TYP_ART order by LIB_TYP_ART">
   <select name="CD_TYP_ART" onChange="document.fiche.submit()">
      <option value="">( Tous )</option>
      %%
   </select>
</salon:DBselection>
<i18n:message key="label.categorieArticle" /> :
<salon:DBselection valeur="<%= CD_CATEG_ART %>" sql="select CD_CATEG_ART, LIB_CATEG_ART from CATEG_ART order by LIB_CATEG_ART">
   <select name="CD_CATEG_ART" onChange="document.fiche.submit()">
      <option value=""><i18n:message key="label.toutes" /></option>
      %%
   </select>
</salon:DBselection>
<i18n:message key="label.enRupture" /> :
<input type="checkbox" name="RUPTURE"
   <% if ((RUPTURE != null) && (RUPTURE.equals("on"))) { %>
   checked 
   <% } %>
   onClick="document.fiche.submit()" >
<i18n:message key="label.affPerime" /> :
<input type="checkbox" name="PERIME"
   <% if ((PERIME != null) && (PERIME.equals("on"))) { %>
   checked 
   <% } %>
   onClick="document.fiche.submit()" >
</form>
<hr>
<table width="100%" border="1" >
	<tr>
		<th><i18n:message key="label.libelle" /></th>
		<th><i18n:message key="label.typeArticleListe" /></th>
		<th><i18n:message key="label.categorie" /></th>
		<th><i18n:message key="label.qteStockTableau" /></th>
		<th><i18n:message key="label.valeurStockUnitTableau" /></th>
		<th><i18n:message key="label.valeurStockGlobal" /></th>
		<th><i18n:message key="label.dernierMouvement" /></th>
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
                - <i18n:message key="label.articleMixte" />
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
	</tr>
	<%
	}
	%>
	<tr>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	    <td class="Nombre"><salon:valeur valeurNulle="null" valeur="<%= valeurTotale %>">%%</salon:valeur></td>
	    <td>&nbsp;</td>
	</tr>
</table>
<salon:madeBy />
<script language="JavaScript">

// Part en création de prestation
function Nouveau()
{
   parent.location.href = "_FicheArt.jsp";
}

// Affichage de l'aide
function Aide()
{
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideListeArt.html");
}

</script>
</body>
</html>
