<%@ page import="java.util.TreeMap,java.util.Set,java.util.Iterator,java.math.BigDecimal,java.util.Date" %>
<%@ page import="com.increg.salon.request.Brouillard
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
<title>Brouillard</title>
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
   // Récupération des paramètres
   Date DT_DEBUT = (Date) request.getAttribute("DT_DEBUT");
   Date DT_FIN = (Date) request.getAttribute("DT_FIN");
   Brouillard brouillardTotal = (Brouillard) request.getAttribute("Total");
%>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/lstBrouillard.gif"></h1>
<form name="fiche" action="rechBrouillard.srv" method="post">
<p>
Entre le :
    <salon:date type="text" name="DT_DEBUT" valeurDate="<%= DT_DEBUT %>" valeurNulle="null" format="dd/MM/yyyy" calendrier="true" onchange="document.fiche.submit()">%%</salon:date>
   et le : 
    <salon:date type="text" name="DT_FIN" valeurDate="<%= DT_FIN %>" valeurNulle="null" format="dd/MM/yyyy" calendrier="true" onchange="document.fiche.submit()">%%</salon:date>
</p>
</form>
<hr>
<%
// Recupère les listes
TreeMap lstTypes = (TreeMap) request.getAttribute("ListeType");
TreeMap lstTypesRem = (TreeMap) request.getAttribute("ListeTypeRem");
TreeMap lstTypeMca = (TreeMap) request.getAttribute("ListeTypeMca");
TreeMap lstLignes = (TreeMap) request.getAttribute("Liste");
%>
<table width="100%" border="1" rules="groups">
<colgroup>
<colgroup span="<%= lstTypes.size() %>">
<colgroup span="<%= lstTypesRem.size() %>">
<colgroup span="<%= lstTypeMca.size() %>">
	<tr>
		<th rowspan=2>Date</th>
	        <th colspan="<%= lstTypes.size() %>">Factures</th>
		<th colspan="<%= lstTypesRem.size() %>">Remise</th>
		<th colspan="<%= lstTypeMca.size() %>">Encaissements</th>
	</tr>
	<% if (lstLignes.size() > 0) { %>
	<tr>
		<% {
		     Set keys = lstTypes.keySet();
		     for (Iterator i= keys.iterator(); i.hasNext(); ) { %>
			<th><%= (String) lstTypes.get(i.next()) %></th>
		  <% }
		  } 
                    if (lstTypes.size() == 0) { 
                        // Cas particulier : Des lignes, mais pas de facture %>
                        <th>
                        &nbsp;
                    </th>
                <% } %>
		<% { // 2ème fois pour les remises
		     Set keys = lstTypesRem.keySet();
		     for (Iterator i= keys.iterator(); i.hasNext(); ) { %>
			<th><%= (String) lstTypesRem.get(i.next()) %></th>
		  <% }
		  } 
                    if (lstTypesRem.size() == 0) { 
                        // Cas particulier : Des lignes, mais pas de facture %>
                        <th>
                        &nbsp;
                    </th>
                <% } %>
		<% {
		     Set keys = lstTypeMca.keySet();
		     for (Iterator i= keys.iterator(); i.hasNext(); ) { %>
			<th><%= (String) lstTypeMca.get(i.next()) %></th>
		  <% }
		  } %>
	</tr>
	<tbody>
	<%    
	Set keys = lstLignes.keySet();
	for (Iterator i=keys.iterator(); i.hasNext(); ) {
	    Brouillard aBrouillard = (Brouillard) lstLignes.get(i.next());
	%>
	<tr>
	    <td class="tabDonnees">
	       <salon:valeur valeurNulle="null" valeur="<%= aBrouillard.getDT_PAIEMENT() %>" >
		  %%
	       </salon:valeur>
	    </td>
	    <% Set typesKeys = lstTypes.keySet();
	       for (Iterator j=typesKeys.iterator(); j.hasNext(); ) { %>
	       <td class="Nombre">
		  <salon:valeur valeurNulle="null" valeur="<%= aBrouillard.getENTREE(((Integer) j.next()).intValue()) %>" >
		     %%&nbsp;
		  </salon:valeur>
	       </td>
	    <% }
               if (lstTypes.size() == 0) { 
                // Cas particulier : Des lignes, mais pas de facture %>
                <td class="Nombre">
                    -
                </td>
            <% } %>
	    <% typesKeys = lstTypesRem.keySet();
	       for (Iterator j=typesKeys.iterator(); j.hasNext(); ) { %>
	       <td class="Nombre">
		  <salon:valeur valeurNulle="null" valeur="<%= aBrouillard.getREMISE(((Integer) j.next()).intValue()) %>" >
		     %%&nbsp;
		  </salon:valeur>
	       </td>
	    <% }
               if (lstTypesRem.size() == 0) { 
                // Cas particulier : Des lignes, mais pas de facture %>
                <td class="Nombre">
                    -
                </td>
            <% } %>
	    <% Set typeMcaKeys = lstTypeMca.keySet();
	       for (Iterator j=typeMcaKeys.iterator(); j.hasNext(); ) { %>
	       <td class="Nombre">
		  <salon:valeur valeurNulle="null" valeur="<%= aBrouillard.getSORTIE(((Integer) j.next()).intValue()) %>" >
		     %%&nbsp;
		  </salon:valeur>
	       </td>
	    <% } %>
	</tr>
	<%
	}
	%>
	</tbody>
	<tbody>
	<tr border=5>
	    <td class="tabDonnees">
	       Totaux
	    </td>
	    <% Set typesKeys = lstTypes.keySet();
	       BigDecimal totalFact = new BigDecimal("0.00");
	       totalFact.setScale(2);
	       for (Iterator j=typesKeys.iterator(); j.hasNext(); ) { 
		  BigDecimal total = brouillardTotal.getENTREE(((Integer) j.next()).intValue());
		  totalFact = totalFact.add(total); %>
	       <td class="Nombre">
		  <salon:valeur valeurNulle="null" valeur="<%= total %>" >
		     %%&nbsp;
		  </salon:valeur>
	       </td>
	    <% } %>
	    <% typesKeys = lstTypesRem.keySet();
	       BigDecimal totalRemise = new BigDecimal("0.00");
	       totalRemise.setScale(2);
	       for (Iterator j=typesKeys.iterator(); j.hasNext(); ) { 
		  BigDecimal total = brouillardTotal.getREMISE(((Integer) j.next()).intValue());
		  totalRemise = totalRemise.add(total); %>
	       <td class="Nombre">
		  <salon:valeur valeurNulle="null" valeur="<%= total %>" >
		     %%&nbsp;
		  </salon:valeur>
	       </td>
	    <% } 
               if (lstTypesRem.size() == 0) { 
                    // Cas particulier : Des lignes, mais pas de facture %>
                    <td>
                        &nbsp;
                    </td>
	    <% }
               Set typeMcaKeys = lstTypeMca.keySet();
	       BigDecimal totalEnc = new BigDecimal("0.00");
	       totalEnc.setScale(2);
	       for (Iterator j=typeMcaKeys.iterator(); j.hasNext(); ) {
		  BigDecimal total = brouillardTotal.getSORTIE(((Integer) j.next()).intValue());
		  totalEnc = totalEnc.add(total); %>
	       <td class="Nombre">
		  <salon:valeur valeurNulle="null" valeur="<%= total %>" >
		     %%&nbsp;
		  </salon:valeur>
	       </td>
	    <% } %>
	</tr>
	<tr>
	    <td>&nbsp;</td>
	    <td class="tabDonnees" colspan="<%= lstTypes.size() %>">
	       <salon:valeur valeurNulle="null" valeur="<%= totalFact %>" >
		  %%&nbsp;
	       </salon:valeur>
	    </td>
	    <td class="tabDonnees" colspan="<%= lstTypesRem.size() %>">
	       <salon:valeur valeurNulle="null" valeur="<%= totalRemise %>" >
		  %%&nbsp;
	       </salon:valeur>
	    </td>
	    <td class="tabDonnees" colspan="<%= lstTypeMca.size() %>">
	       <salon:valeur valeurNulle="null" valeur="<%= totalEnc %>" >
		  %%&nbsp;
	       </salon:valeur>
	    </td>
	</tr>
	<tr>
	    <td class="tabDonnees">
	       Répartition
	    </td>
	    <% Set typesKeys2 = lstTypes.keySet();
	       for (Iterator j=typesKeys2.iterator(); j.hasNext(); ) { %>
	       <td class="Nombre">
                <% try { %>
		  <salon:valeur valeurNulle="null" valeur="<%= brouillardTotal.getENTREE(((Integer) j.next()).intValue()).multiply(new BigDecimal(100)).divide(totalFact, 2, BigDecimal.ROUND_HALF_UP) %>" >
		     %%&nbsp;%
		  </salon:valeur>
                <% }
                   catch (ArithmeticException e) { %>
                    &nbsp;
                <% } %>
	       </td>
	    <% } %>
	    <% typesKeys2 = lstTypesRem.keySet();
	       for (Iterator j=typesKeys2.iterator(); j.hasNext(); ) { %>
	       <td class="Nombre">
                <% try { %>
		  <salon:valeur valeurNulle="null" valeur="<%= brouillardTotal.getREMISE(((Integer) j.next()).intValue()).multiply(new BigDecimal(100)).divide(totalRemise, 2, BigDecimal.ROUND_HALF_UP) %>" >
		     %%&nbsp;%
		  </salon:valeur>
                <% }
                   catch (ArithmeticException e) { %>
                    &nbsp;
                <% } %>
	       </td>
	    <% } 
                if (lstTypesRem.size() == 0) { 
                    // Cas particulier : Des lignes, mais pas de facture %>
                    <td>
                        &nbsp;
                    </td>
	    <% }
               Set typeMcaKeys2 = lstTypeMca.keySet();
	       for (Iterator j=typeMcaKeys2.iterator(); j.hasNext(); ) { %>
	       <td class="Nombre">
                <% try { %>
		  <salon:valeur valeurNulle="null" valeur="<%= brouillardTotal.getSORTIE(((Integer) j.next()).intValue()).multiply(new BigDecimal(100)).divide(totalEnc, 2, BigDecimal.ROUND_HALF_UP) %>" >
		     %%&nbsp;%
		  </salon:valeur>
                <% }
                   catch (ArithmeticException e) { %>
                    &nbsp;
                <% } %>
	       </td>
	    <% } %>
	</tr>
	</tbody>
	<% } %>
</table>
<salon:madeBy />
<script language="JavaScript">
<!--

// Affichage de l'aide
function Aide()
{
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideListeBrouillard.html");
}

//-->
</script>
</body>
</html>
