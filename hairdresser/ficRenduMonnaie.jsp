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
<%@ include file="include/commun.js" %>

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
