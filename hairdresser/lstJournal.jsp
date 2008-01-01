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
<%@ page import="java.util.TreeMap,java.util.Set,java.util.Iterator,java.util.Calendar" %>
<%@ page import="com.increg.salon.bean.ModReglBean,
	       com.increg.salon.request.Journal
	       " %>
<%@ page import="com.increg.salon.bean.SalonSession" %>
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
<title><i18n:message key="label.journal" /></title>
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
   String CD_MOD_REGL = (String) request.getAttribute("CD_MOD_REGL");
   Calendar DT_DEBUT = (Calendar) request.getAttribute("DT_DEBUT");
   Calendar DT_FIN = (Calendar) request.getAttribute("DT_FIN");
%>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/lstJournal.gif"></h1>
<form name="fiche" action="rechJournal.srv" method="post">
<p>
<i18n:message key="label.modePaiement" /> :
<salon:DBselection valeur="<%= CD_MOD_REGL %>" sql='<%= "select CD_MOD_REGL, LIB_MOD_REGL from MOD_REGL where CD_MOD_REGL not in (" + Integer.toString(ModReglBean.MOD_REGL_ESP_FRF) + "," + Integer.toString(ModReglBean.MOD_REGL_CHQ_FRF) + ") order by LIB_MOD_REGL" %>'>
   <select name="CD_MOD_REGL" onChange="document.fiche.submit()">
      %%
   </select>
</salon:DBselection>
</p>
<p>
<i18n:message key="label.entreLe" /> :
<i18n:message key="format.dateSimpleDefaut" id="formatDate" />
    <salon:date type="text" name="DT_DEBUT" valeurDate="<%= DT_DEBUT %>" valeurNulle="null" format="<%= formatDate %>" calendrier="true" onchange="document.fiche.submit()">%%</salon:date>
<i18n:message key="label.etLe" /> : 
    <salon:date type="text" name="DT_FIN" valeurDate="<%= DT_FIN %>" valeurNulle="null" format="<%= formatDate %>" calendrier="true" onchange="document.fiche.submit()">%%</salon:date>
</p>
</form>
<hr>
<%
// Recupère les listes
TreeMap lstTypes = (TreeMap) request.getAttribute("ListeType");
TreeMap lstLignes = (TreeMap) request.getAttribute("Liste");
Journal total = (Journal) request.getAttribute("Total");
%>
<table width="100%" border="1" rules="groups">
<colgroup>
<colgroup>
<colgroup span="<%= lstTypes.size() %>">
<colgroup>
	<thead>
	<tr>
		<th><i18n:message key="label.date" /></th>
		<th><i18n:message key="label.fondCaisse" /></th>
		<% {
		     Set keys = lstTypes.keySet();
		     for (Iterator i= keys.iterator(); i.hasNext(); ) { %>
			<th><%= (String) lstTypes.get(i.next()) %></th>
		  <% }
		  } %>
		<th><i18n:message key="label.nouveauFond" /></th>
	</tr>
	</thead>
	<tbody>
	<%    
	Set keys = lstLignes.keySet();
	for (Iterator i=keys.iterator(); i.hasNext(); ) {
	    Journal aJournal = (Journal) lstLignes.get(i.next());
	%>
	<tr>
	    <td class="tabDonnees">
	       <salon:valeur valeurNulle="null" valeur="<%= aJournal.getDT_PAIEMENT() %>" >
		  %%
	       </salon:valeur>
	    </td>
	    <td class="Nombre">
	       <salon:valeur valeurNulle="null" valeur="<%= aJournal.getSOLDE_INIT() %>" >
		  %%&nbsp;
	       </salon:valeur>
	    </td>
	    <% Set typesKeys = lstTypes.keySet();
	       for (Iterator j=typesKeys.iterator(); j.hasNext(); ) { %>
	       <td class="Nombre">
		  <salon:valeur valeurNulle="null" valeur="<%= aJournal.getSORTIE(((Integer) j.next()).intValue()) %>" >
		     %%&nbsp;
		  </salon:valeur>
	       </td>
	    <% } %>
	    <td class="Nombre">
	       <salon:valeur valeurNulle="null" valeur="<%= aJournal.getSOLDE_FINAL() %>" >
		  %%&nbsp;
	       </salon:valeur>
	    </td>
	</tr>
	<%
	}
	%>
	</tbody>
	<tfoot>
	<tr>
	    <td class="Nombre" colspan="2"><i18n:message key="label.totaux" /> :</td>
	    <% Set typesKeys = lstTypes.keySet();
	       for (Iterator j=typesKeys.iterator(); j.hasNext(); ) { %>
	       <td class="Nombre">
		  <salon:valeur valeurNulle="null" valeur="<%= total.getSORTIE(((Integer) j.next()).intValue()) %>" >
		     %%&nbsp;
		  </salon:valeur>
	       </td>
	    <% } %>
	    <td>&nbsp;</td>
	</tr>
	</tfoot>
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
