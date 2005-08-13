<%@ page import="java.util.Vector, java.math.BigDecimal,java.util.Date" %>
<%@ page import="com.increg.salon.bean.SalonSession,
                com.increg.salon.request.TVA,
	        com.increg.salon.bean.TypVentBean
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
<title>TVA encaissée</title>
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
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/lstTVA.gif"></h1>
<form name="fiche" action="rechTVA.srv" method="post">
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
		<th>Type de prestation</th>
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
	    TVA aTVA = (TVA) lstLignes.get(i);
        fullTotal = fullTotal.add(aTVA.getTotal());
        fullTotalHT = fullTotalHT.add(aTVA.getTotalHT());
        fullTotalTTC = fullTotalTTC.add(aTVA.getTotalTTC());
	%>
	<tr>
		<td><%= TypVentBean.getTypVentBean(mySalon.getMyDBSession(), Integer.toString(aTVA.getCD_TYP_VENT())).toString() %></td>
	<td class="Nombre">
	    <salon:valeur valeur="<%= aTVA.getTotalHT() %>" valeurNulle="null"> %% </salon:valeur>
	</td>
	<td class="Nombre">
	    <salon:valeur valeur="<%= aTVA.getTotal() %>" valeurNulle="null"> %% </salon:valeur>
	</td>
	<td class="Nombre">
	    <salon:valeur valeur="<%= aTVA.getTotalTTC() %>" valeurNulle="null"> %% </salon:valeur>
	</td>
	</tr>
	<%
	}
	%>
        <tfoot>
        <tr>
            <td class="Total">Total</td>
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
