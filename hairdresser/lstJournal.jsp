<%@ page import="java.util.TreeMap,java.util.Set,java.util.Iterator,java.util.Date" %>
<%@ page import="com.increg.salon.bean.ModReglBean,
	       com.increg.salon.request.Journal
	       " %>
<%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %>
<html>
<head>
<title>Journal</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees">
<%@ include file="include/commun.js" %>
<script language="JavaScript">
<!--
function Init() {
   <%
   // Positionne les liens d'actions
   %>
   MM_showHideLayers('NOUVEAU?bottomFrame','','hide');
}
//-->
</script>
<%
   // R�cup�ration des param�tres
   String CD_MOD_REGL = (String) request.getAttribute("CD_MOD_REGL");
   Date DT_DEBUT = (Date) request.getAttribute("DT_DEBUT");
   Date DT_FIN = (Date) request.getAttribute("DT_FIN");
%>
<h1><img src="images/titres/lstJournal.gif"></h1>
<form name="fiche" action="rechJournal.srv" method="post">
<p>
Mode de paiement :
<salon:DBselection valeur="<%= CD_MOD_REGL %>" sql='<%= "select CD_MOD_REGL, LIB_MOD_REGL from MOD_REGL where CD_MOD_REGL not in (" + Integer.toString(ModReglBean.MOD_REGL_ESP_FRF) + "," + Integer.toString(ModReglBean.MOD_REGL_CHQ_FRF) + ") order by LIB_MOD_REGL" %>'>
   <select name="CD_MOD_REGL" onChange="document.fiche.submit()">
      %%
   </select>
</salon:DBselection>
</p>
<p>
Entre le :
    <salon:date type="text" name="DT_DEBUT" valeurDate="<%= DT_DEBUT %>" valeurNulle="null" format="dd/MM/yyyy" calendrier="true" onchange="document.fiche.submit()">%%</salon:date>
   et le : 
    <salon:date type="text" name="DT_FIN" valeurDate="<%= DT_FIN %>" valeurNulle="null" format="dd/MM/yyyy" calendrier="true" onchange="document.fiche.submit()">%%</salon:date>
</p>
</form>
<hr>
<%
// Recup�re les listes
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
		<th>Date</th>
		<th>Fond de caisse</th>
		<% {
		     Set keys = lstTypes.keySet();
		     for (Iterator i= keys.iterator(); i.hasNext(); ) { %>
			<th><%= (String) lstTypes.get(i.next()) %></th>
		  <% }
		  } %>
		<th>Nouveau fond<br><i>calcul�</i></th>
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
	    <td class="Nombre" colspan="2">Totaux :</td>
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
    window.open("aideListe.html");
}

//-->
</script>
</body>
</html>
