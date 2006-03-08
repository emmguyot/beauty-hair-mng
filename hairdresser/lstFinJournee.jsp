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
<%@ page import="java.util.TreeMap,java.util.Set,java.util.Vector,java.util.Iterator,java.math.BigDecimal,java.util.Calendar" %>
<%@ page import="com.increg.salon.bean.SalonSession,
                com.increg.salon.request.Brouillard,
	        com.increg.salon.request.CA,
	        com.increg.salon.bean.CaisseBean,
	        com.increg.salon.bean.DonneeRefBean
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
<title><i18n:message key="label.finJournee" /></title>
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
    Calendar DT_JOUR = (Calendar) request.getAttribute("DT_JOUR");
    // Recupère les listes pour le Brouillard
    Brouillard brouillardTotal = (Brouillard) request.getAttribute("TotalB");
    TreeMap lstTypesB = (TreeMap) request.getAttribute("ListeTypeB");
    TreeMap lstTypesRemB = (TreeMap) request.getAttribute("ListeTypeRemB");
    TreeMap lstTypeMcaB = (TreeMap) request.getAttribute("ListeTypeMcaB");
    TreeMap lstLignesB = (TreeMap) request.getAttribute("ListeB");
    // Récupère les listes pour le CA
    Vector lstTypesC = (Vector) request.getAttribute("ListeTypeC");
    Vector lstLignesC = (Vector) request.getAttribute("ListeC");
    // Récupère les listes des caisses
    Vector listeCaisse = (Vector) request.getAttribute("ListeCaisse");
    
%>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/lstFinJournee.gif"></h1>
<form name="fiche" action="rechFinJournee.srv" method="post">
<p>
<i18n:message key="label.journeeDu" /> :
    <i18n:message key="format.dateSimpleDefaut" id="formatDate" />
    <salon:valeur valeurNulle="null" valeur="<%= DT_JOUR %>" format="<%= formatDate %>">
      %%
      <input type="hidden" name="DT_JOUR" value="%%">
    </salon:valeur>
</p>
</form>
<table width="100%" border="1" rules="groups">
<colgroup>
<colgroup span="<%= lstTypesB.size() %>">
<colgroup span="<%= lstTypesRemB.size() %>">
<colgroup span="<%= lstTypeMcaB.size() %>">
	<tr>
		<th rowspan=2></th>
	        <th colspan="<%= lstTypesB.size() %>"><i18n:message key="label.factures" /></th>
		<th colspan="<%= lstTypesRemB.size() %>"><i18n:message key="label.remise" /></th>
		<th colspan="<%= lstTypeMcaB.size() %>"><i18n:message key="label.encaissement" /></th>
	</tr>
	<% if (lstLignesB.size() > 0) { %>
	<tr>
		<% {
		     Set keys = lstTypesB.keySet();
		     for (Iterator i= keys.iterator(); i.hasNext(); ) { %>
			<th><%= (String) lstTypesB.get(i.next()) %></th>
		  <% }
		  } 
                    if (lstTypesB.size() == 0) { 
                        // Cas particulier : Des lignes, mais pas de facture %>
                        <th>
                        &nbsp;
                    </th>
                <% } %>
		<% { // 2ème fois pour les remises
		     Set keys = lstTypesRemB.keySet();
		     for (Iterator i= keys.iterator(); i.hasNext(); ) { %>
			<th><%= (String) lstTypesRemB.get(i.next()) %></th>
		  <% }
		  } 
                    if (lstTypesRemB.size() == 0) { 
                        // Cas particulier : Des lignes, mais pas de facture %>
                        <th>
                        &nbsp;
                    </th>
                <% } %>
		<% {
		     Set keys = lstTypeMcaB.keySet();
		     for (Iterator i= keys.iterator(); i.hasNext(); ) { %>
			<th><%= (String) lstTypeMcaB.get(i.next()) %></th>
		  <% }
		  } %>
	</tr>
	<tbody>
	<tr border=5>
	    <td class="tabDonnees">
	       <i18n:message key="label.ca" />
	    </td>
	    <% Set typesKeys = lstTypesB.keySet();
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
	    <% typesKeys = lstTypesRemB.keySet();
	       BigDecimal totalRem = new BigDecimal("0.00");
	       totalRem.setScale(2);
	       for (Iterator j=typesKeys.iterator(); j.hasNext(); ) { 
                    BigDecimal total = brouillardTotal.getREMISE(((Integer) j.next()).intValue());
                    totalRem = totalRem.add(total); %>
	       <td class="Nombre">
		  <salon:valeur valeurNulle="null" valeur="<%= total %>" >
		     %%&nbsp;
		  </salon:valeur>
	       </td>
	    <% }
               if (lstTypesRemB.size() == 0) { 
                // Cas particulier : Des lignes, mais pas de facture %>
                <td class="Nombre">
                    -
                </td>
            <% } %>
	    <% Set typeMcaKeys = lstTypeMcaB.keySet();
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
	    <td class="tabDonnees"><i18n:message key="label.total" /></td>
	    <td class="tabDonnees" colspan="<%= lstTypesB.size() %>">
	       <salon:valeur valeurNulle="null" valeur="<%= totalFact %>" >
		  %%&nbsp;
	       </salon:valeur>
	    </td>
	    <td class="tabDonnees" colspan="<%= lstTypesRemB.size() %>">
	       <salon:valeur valeurNulle="null" valeur="<%= totalRem %>" >
		  %%&nbsp;
	       </salon:valeur>
	    </td>
	    <td class="tabDonnees" colspan="<%= lstTypeMcaB.size() %>">
	       <salon:valeur valeurNulle="null" valeur="<%= totalEnc %>" >
		  %%&nbsp;
	       </salon:valeur>
	    </td>
	</tr>
	<tr>
	    <td class="tabDonnees">
	       Répartition
	    </td>
	    <% Set typesKeys2 = lstTypesB.keySet();
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
	    <% typesKeys2 = lstTypesRemB.keySet();
	       for (Iterator j=typesKeys2.iterator(); j.hasNext(); ) { %>
	       <td class="Nombre">
                <% try { %>
		  <salon:valeur valeurNulle="null" valeur="<%= brouillardTotal.getREMISE(((Integer) j.next()).intValue()).multiply(new BigDecimal(100)).divide(totalRem, 2, BigDecimal.ROUND_HALF_UP) %>" >
		     %%&nbsp;%
		  </salon:valeur>
                <% }
                   catch (ArithmeticException e) { %>
                    &nbsp;
                <% } %>
	       </td>
	    <% }
               if (lstTypesRemB.size() == 0) { 
                // Cas particulier : Des lignes, mais pas de facture %>
                <td class="Nombre">
                    -
                </td>
            <% } %>
	    <% Set typeMcaKeys2 = lstTypeMcaB.keySet();
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
<p></p>
<table width="100%">
<tr>
<td>
<table width="100%" border="1" rules="groups">
<colgroup>
<colgroup span="<%= lstTypesC.size() %>">
<colgroup>
	<tr>
		<th><i18n:message key="label.collaborateur" /></th>
		<% for (int i=0; i< lstTypesC.size(); i++) { %>
		  <th><%= (String) lstTypesC.get(i) %></th>
	        <% } %>
		<th><i18n:message key="label.caTotalHorsRemise" /></th>
	</tr>
	<tbody>
	<%    
    BigDecimal fullTotal = new BigDecimal(0);
    BigDecimal fullTotalType[] = new BigDecimal[lstTypesC.size()];
	for (int j=0; j < lstTypesC.size(); j++) {
		fullTotalType[j] = new BigDecimal(0);
	}
	for (int i=0; i< lstLignesC.size(); ) {
	    CA aCA = (CA) lstLignesC.get(i);
            String PRENOM_orig = aCA.getPRENOM();
	%>
	<tr>
	    <td class="tabDonnees">
	       <salon:valeur valeurNulle="null" valeur="<%= aCA.getPRENOM() %>" >
		  %%
	       </salon:valeur>
	    </td>
	    <% BigDecimal total = new BigDecimal(0);
	       total.setScale(2);
	       for (int j=0; j< lstTypesC.size(); j++) { %>
	       <td class="Nombre">
		  
                       
	       <% if (PRENOM_orig.equals(aCA.getPRENOM())
                        && (aCA.getLIB_TYP_VENT() != null) 
			&& (aCA.getLIB_TYP_VENT().equals((String) lstTypesC.get(j)))) { %>
		  <salon:valeur valeurNulle="null" valeur="<%= aCA.getMONTANT() %>" >
		     %%
		  </salon:valeur>
	       <%    if (aCA.getMONTANT() != null) {
			total = total.add(aCA.getMONTANT());
		     }
      		  fullTotalType[j] = fullTotalType[j].add(aCA.getMONTANT());
		     i++;
		     if (i < lstLignesC.size()) {
			aCA = (CA) lstLignesC.get(i);
		     }
		     else {
			aCA = new CA();
		     }
		  }
		  else { %>
		     &nbsp;
	       <% } %>
	       </td>
	    <% } 
               fullTotal = fullTotal.add(total);
            %>
	    <td class="Nombre">
	       <%= total %>
	    </td>
	</tr>
	<%
	}
	%>
	</tbody>
        <tfoot>
        <tr>
            <td class="Total"><i18n:message key="label.total" /></td>
            <%
			for (int j=0; j < lstTypesC.size(); j++) {
			%>
            <td class="Nombre">
			  <salon:valeur valeurNulle="null" valeur="<%= fullTotalType[j] %>" >
			     %%
			  </salon:valeur>
            </td>
            <%
			}
			%>
            <td class="Nombre">
                <salon:valeur valeur="<%= fullTotal %>" valeurNulle="null"> %% </salon:valeur>
            </td>
        </tr>
        </tfoot>
</table>
</td>
<td class="labelBas">
<table border="1">
    <tr>
        <th><i18n:message key="label.caisse" /></th>
        <th><i18n:message key="label.soldeCaisse" /></th>
    </tr>
    <tbody>
        <%    
	for (int i=0; i< listeCaisse.size(); i++) {
	    CaisseBean aCaisseBean = (CaisseBean) listeCaisse.get(i);
	%>
	<tr>
	    <td class="tabDonnees">
                <salon:valeur valeur='<%= DonneeRefBean.getDonneeRefBean(mySalon.getMyDBSession(), "MOD_REGL", Integer.toString(aCaisseBean.getCD_MOD_REGL())).toString() %>' valeurNulle="null">
                    %%
                </salon:valeur>
            </td>
            <td class="Nombre"><%= aCaisseBean.getSOLDE() %>
            </td>
        </tr>
        <% } %>
    </tbody>
</table>
</td>
</tr>
</table>
<salon:madeBy />
<script language="JavaScript">
<!--
// Affichage de l'aide
function Aide()
{
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideListeFinJournee.html");
}

//-->
</script>
</body>
</html>
