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
<%@ page import="java.math.BigDecimal" %>
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
<title><i18n:message key="ficRenduMonnaie.title" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">

</head>
<body class="donnees" onLoad="javascript:document.fiche.montantRegle.select();">
<%@ include file="include/commun.jsp" %>

<%
   // Récupération des paramètres
   String montant = (String) request.getAttribute("montant");
   String montantRegle = (String) request.getAttribute("montantRegle");
   BigDecimal aRendre = (BigDecimal) request.getAttribute("aRendre");

%>

<i18n:message key="message.fermer" id="msgFermer" />
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/ficRenduMonnaie.gif">
<salon:bouton url="javascript:window.close()" imgOn="images/quit2.gif" img="images/quit.gif" alt="<%= msgFermer %>" /></h1>
<salon:message salonSession="<%= mySalon %>" />
<br>
<form method="post" action="ficRenduMonnaie.srv" name="fiche"> 

<table>
<tr>
    <td>
        <span class="obligatoire"><i18n:message key="label.montantARegler" /> : </span>
    </td>
    <td>
        <salon:valeur valeurNulle="null" valeur="<%= montant %>" >
	    <input type="hidden" name="montant" value="%%">
	    %% <%= mySalon.getDevise().toString() %>
	</salon:valeur>
    </td>
</tr>
<tr>
    <td>
        <span class="obligatoire"><i18n:message key="label.montantRegle" /> : </span>
    </td>
    <td>
        <salon:valeur valeurNulle="null" valeur="<%= montantRegle %>" >
	    <input type="text" name="montantRegle" value="%%" size=10 onChange="document.fiche.submit()" >
        </salon:valeur>
        <input type="image" src="images/calculatrice.gif" alt="<i18n:message key="message.calculer" />">
    </td>
</tr>
</table>
</form>


<%  // Si on a fait le calcul, on affiche le resultat et la decomposition --> 
if ((aRendre != null) && (aRendre.signum() >= 0)) { %>
    <h2 align = "left">Montant à rendre : 
        <%  BigDecimal montantARendre = aRendre;%>
        <%=montantARendre%>
    </h2>
<%}%>
    
</body>
</html>
