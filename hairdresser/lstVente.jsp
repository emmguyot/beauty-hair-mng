<%@ page import="java.util.Vector, java.math.BigDecimal,java.util.Date" %>
<%@ page import="com.increg.salon.bean.SalonSession,
                com.increg.salon.request.RecapVente,
	        com.increg.salon.bean.TypVentBean
	        " %>
<%
    SalonSession mySalon = (SalonSession) session.getAttribute("SalonSession");
    if (mySalon == null) {
        getServletConfig().getServletContext().getRequestDispatcher("/reconnect.html").forward(request, response);
    }
%>
<%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %>
<html>
<head>
<title>Récap des ventes</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees" onLoad="document.fiche.DT_DEBUT.focus()">
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
%>
<h1><img src="images/titres/lstRecapVentes.gif"></h1>
<form name="fiche" action="rechVente.srv" method="post">
<p>
Entre le :
    <salon:date type="text" name="DT_DEBUT" valeurDate="<%= DT_DEBUT %>" valeurNulle="null" format="dd/MM/yyyy" calendrier="true" onchange="document.fiche.submit()">%%</salon:date>
   et le : 
    <salon:date type="text" name="DT_FIN" valeurDate="<%= DT_FIN %>" valeurNulle="null" format="dd/MM/yyyy" calendrier="true" onchange="document.fiche.submit()">%%</salon:date>
</p>
</form>
<hr>
<table width="100%" border="1" >
	<tr>
		<th>Prestation</th>
		<th>Quantité</th>
		<th>HT</th>
		<th>TVA</th>
		<th>TTC</th>
	</tr>
	<%
	// Recupère la liste
	Vector lstLignes = (Vector) request.getAttribute("Liste");
    BigDecimal fullTotal = new BigDecimal(0);
    BigDecimal fullTotalHT = new BigDecimal(0);
    BigDecimal fullTotalTTC = new BigDecimal(0);
	    
	for (int i=0; i< lstLignes.size(); i++) {
	    RecapVente aRecap = (RecapVente) lstLignes.get(i);
        fullTotal = fullTotal.add(aRecap.getTVA());
        fullTotalHT = fullTotalHT.add(aRecap.getHT());
        fullTotalTTC = fullTotalTTC.add(aRecap.getTTC());
	%>
	<tr>
		<td><%= aRecap.getPrest().toString() %></td>
	<td class="Nombre">
	    <salon:valeur valeur="<%= aRecap.getQte() %>" valeurNulle="null"> %% </salon:valeur>
	</td>
	<td class="Nombre">
	    <salon:valeur valeur="<%= aRecap.getHT() %>" valeurNulle="null"> %% </salon:valeur>
	</td>
	<td class="Nombre">
	    <salon:valeur valeur="<%= aRecap.getTVA() %>" valeurNulle="null"> %% </salon:valeur>
	</td>
	<td class="Nombre">
	    <salon:valeur valeur="<%= aRecap.getTTC() %>" valeurNulle="null"> %% </salon:valeur>
	</td>
	</tr>
	<%
	}
	%>
        <tfoot>
        <tr>
            <td class="Total">Total</td>
            <td class="Nombre">&nbsp;</td>
            <td class="Nombre">
                <salon:valeur valeur="<%= fullTotalHT %>" valeurNulle="null"> %% </salon:valeur>
            </td>
            <td class="Nombre">
                <salon:valeur valeur="<%= fullTotal %>" valeurNulle="null"> %% </salon:valeur>
            </td>
            <td class="Nombre">
                <salon:valeur valeur="<%= fullTotalTTC %>" valeurNulle="null"> %% </salon:valeur>
            </td>
        </tr>
        </tfoot>
</table>
Le calcul de ces chiffres comprend des arrondis : Les centimes sont donc donnés uniquement à titre indicatif.
<salon:madeBy />
<script language="JavaScript">
<!--

// Affichage de l'aide
function Aide()
{
    window.open("aideListeTVA.html");
}

//-->
</script>
</body>
</html>
