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
<%@ page import="java.util.Vector,java.util.Calendar" %>
<%@ page import="com.increg.salon.bean.SalonSession,
                com.increg.salon.bean.DonneeRefBean,
	        com.increg.salon.bean.MvtCaisseBean,
	        com.increg.salon.bean.PaiementBean,
	        com.increg.salon.bean.TypMcaBean" %>
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
<title><i18n:message key="title.lstMvtCaisse" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees">
<%@ include file="include/commun.jsp" %>
<script language="JavaScript">
<!--
function Init() {
}
//-->
</script>
<%
   // Récupération des paramètres
   String CD_MOD_REGL = request.getParameter("CD_MOD_REGL");
   Calendar DT_DEBUT = (Calendar) request.getAttribute("DT_DEBUT");
   Calendar DT_FIN = (Calendar) request.getAttribute("DT_FIN");
   String CD_TYP_MCA = request.getParameter("CD_TYP_MCA");
%>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/lstMvtCaisse.gif"></h1>
<form name="fiche" action="rechMca.srv" method="post">
<p>
<i18n:message key="label.modeRegl" /> :
<salon:DBselection valeur="<%= CD_MOD_REGL %>" sql="select CD_MOD_REGL, LIB_MOD_REGL from MOD_REGL order by LIB_MOD_REGL">
   <select name="CD_MOD_REGL" onChange="document.fiche.submit()">
      <option value=""><i18n:message key="label.tousDsListe" /></option>
      %%
   </select>
</salon:DBselection>
<i18n:message key="label.typeMouvement" /> :
<salon:DBselection valeur="<%= CD_TYP_MCA %>" sql="select CD_TYP_MCA, LIB_TYP_MCA from TYP_MCA order by LIB_TYP_MCA">
   <select name="CD_TYP_MCA" onChange="document.fiche.submit()">
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
		<th><i18n:message key="label.modeRegl" /></th>
		<th><i18n:message key="label.montant" /></th>
		<th><i18n:message key="label.soldeAvant" /></th>
		<th><i18n:message key="label.commentaire" /></th>
	</tr>
	<%
	// Recupère la liste
	Vector lstLignes = (Vector) request.getAttribute("Liste");
	    
	for (int i=0; i< lstLignes.size(); i++) {
	    MvtCaisseBean aMvt = (MvtCaisseBean) lstLignes.get(i);
	%>
	<tr>
	    <td class="tabDonnees">
	       <salon:valeur valeurNulle="null" valeur="<%= aMvt.getDT_MVT() %>" >
		  %%
	       </salon:valeur>
	    </td>
	    <td class="tabDonnees">
	    <% String LIB_TYP_MCA = TypMcaBean.getTypMcaBean(mySalon.getMyDBSession(), 
							       Integer.toString(aMvt.getCD_TYP_MCA())).toString(); %>
	    <%= LIB_TYP_MCA %>
            <% if ((aMvt.getCD_PAIEMENT() != 0) 
                    && (PaiementBean.getPaiementBean(mySalon.getMyDBSession(), Long.toString(aMvt.getCD_PAIEMENT()), mySalon.getMessagesBundle()) 
                        != null)) { %>
                <a href="_FichePaiement.jsp?Action=Modification&CD_PAIEMENT=<%= aMvt.getCD_PAIEMENT() %>" target="ClientFrame" title="Fiche paiement">
                <img src="images/fact.gif" border=0 align=top></a>
            <% } %>
	    </td>
	    <td class="tabDonnees">
	    <% String LIB_MOD_REGL = DonneeRefBean.getDonneeRefBean(mySalon.getMyDBSession(), "MOD_REGL",
							       Integer.toString(aMvt.getCD_MOD_REGL())).toString(); %>
	    <%= LIB_MOD_REGL %>
	    </td>
	    <td class="Nombre"><salon:valeur valeur="<%= aMvt.getMONTANT() %>" valeurNulle="null">%%</salon:valeur></td>
	    <td class="Nombre"><salon:valeur valeur="<%= aMvt.getSOLDE_AVANT() %>" valeurNulle="null">%%</salon:valeur></td>
	    <td><salon:valeur valeur="<%= aMvt.getCOMM() %>" valeurNulle="null" expand="true">%%</salon:valeur>&nbsp;</td>
	</tr>
	<%
	}
	%>
</table>
<salon:madeBy />
<script language="JavaScript">
function Nouveau()
{
   parent.location.href = "_FicheMvtCaisse.jsp";
}

// Affichage de l'aide
function Aide()
{
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideListe.html");
}

</script>
</body>
</html>
