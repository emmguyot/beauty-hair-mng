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
<%@ page import="java.util.Vector,java.util.Calendar" %>
<%@ page import="com.increg.salon.bean.SalonSession,
                com.increg.salon.bean.ArtBean,
	        com.increg.salon.bean.MvtStkBean,
	        com.increg.salon.bean.FactBean,
	        com.increg.salon.bean.TypMvtBean" %>
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
<title><i18n:message key="title.lstMvtStk" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees">
<%@ include file="include/commun.jsp" %>
<script language="JavaScript">
<!--
function Init() {
   <%
   // Positionne les liens d actions
   %>
   MM_showHideLayers('NOUVEAU?bottomFrame','','hide');
}
//-->
</script>
<%
   // Récupération des paramètres
   String CD_ART = request.getParameter("CD_ART");
   Calendar DT_DEBUT = (Calendar) request.getAttribute("DT_DEBUT");
   Calendar DT_FIN = (Calendar) request.getAttribute("DT_FIN");
   String CD_TYP_MVT = request.getParameter("CD_TYP_MVT");
%>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/lstMvtStk.gif"></h1>
<form name="fiche" action="rechMvt.srv" method="post">
<p>
<i18n:message key="label.article" />:
<salon:DBselection valeur="<%= CD_ART %>" sql="select CD_ART, LIB_ART from ART order by LIB_ART">
   <select name="CD_ART" onChange="document.fiche.submit()">
      <option value=""><i18n:message key="label.tousDsListe" /></option>
      %%
   </select>
</salon:DBselection>
<i18n:message key="label.typeMouvement" /> :
<salon:DBselection valeur="<%= CD_TYP_MVT %>" sql="select CD_TYP_MVT, LIB_TYP_MVT from TYP_MVT order by LIB_TYP_MVT">
   <select name="CD_TYP_MVT" onChange="document.fiche.submit()">
      <option value=""><i18n:message key="label.tousDsListe" /></option>
      %%
   </select>
</salon:DBselection>
</p>
<p>
<i18n:message key="label.entreLe" /> :
<i18n:message key="format.dateDefaut" id="formatDate" />
    <salon:date type="text" name="DT_DEBUT" valeurDate="<%= DT_DEBUT %>" valeurNulle="null" format="<%= formatDate %>" calendrier="true" onchange="document.fiche.submit()">%%</salon:date>
<i18n:message key="label.etLe" /> : 
    <salon:date type="text" name="DT_FIN" valeurDate="<%= DT_FIN %>" valeurNulle="null" format="<%= formatDate %>" calendrier="true" onchange="document.fiche.submit()">%%</salon:date>
</p>
</form>
<hr>
<table width="100%" border="1" >
	<tr>
		<th><i18n:message key="label.date" /></th>
		<th><i18n:message key="label.type" /></th>
		<th><i18n:message key="label.article" /></th>
		<th><i18n:message key="label.qte" /></th>
		<th><i18n:message key="label.valeurUnitMouvement" /></th>
		<th><i18n:message key="label.stockAvantTableau" /></th>
	</tr>
	<%
	// Recupère la liste
	Vector lstLignes = (Vector) request.getAttribute("Liste");
	    
	for (int i=0; i< lstLignes.size(); i++) {
	    MvtStkBean aMvt = (MvtStkBean) lstLignes.get(i);
	%>
	<tr>
	    <td class="tabDonnees">
	       <salon:valeur valeurNulle="null" valeur="<%= aMvt.getDT_MVT() %>" >
		  %%
	       </salon:valeur>
	    </td>
	    <td class="tabDonnees">
	    <% String LIB_TYP_MVT = TypMvtBean.getTypMvtBean(mySalon.getMyDBSession(), 
							       Integer.toString(aMvt.getCD_TYP_MVT())).toString(); %>
	    <%= LIB_TYP_MVT %>
            <% if ((aMvt.getCD_FACT() != 0) 
                    && (FactBean.getFactBean(mySalon.getMyDBSession(), Long.toString(aMvt.getCD_FACT()), mySalon.getMessagesBundle()) 
                        != null)){ %>
                <a href="_FicheFact.jsp?Action=Modification&CD_FACT=<%= aMvt.getCD_FACT() %>" target="ClientFrame" title="Fiche facture">
                <img src="images/fact.gif" border=0 align=top></a>
            <% } %>
	    </td>
	    <td class="tabDonnees">
	    <% String LIB_ART = ArtBean.getArtBean(mySalon.getMyDBSession(), 
							       Long.toString(aMvt.getCD_ART()), mySalon.getMessagesBundle()).toString(); %>
	    <a href="_FicheArt_Mvt.jsp?Action=Modification&CD_ART=<%= aMvt.getCD_ART() %>" target="ClientFrame"><%= LIB_ART %></a>
	    </td>
	    <td class="Nombre"><salon:valeur valeur="<%= aMvt.getQTE() %>" valeurNulle="null">%%</salon:valeur></td>
	    <td class="Nombre"><salon:valeur valeur="<%= aMvt.getVAL_MVT_HT() %>" valeurNulle="null">%%</salon:valeur></td>
	    <td class="Nombre"><salon:valeur valeur="<%= aMvt.getSTK_AVANT() %>" valeurNulle="null">%%</salon:valeur></td>
	</tr>
	<%
	}
	%>
</table>
<salon:madeBy />
<script language="JavaScript">
<!--

// Affichage de l'aide
function Aide()
{
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideListe.html");
}

//-->
</script>
</body>
</html>
