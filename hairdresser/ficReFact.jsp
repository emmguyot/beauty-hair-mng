<%@ page import="com.increg.salon.bean.SalonSession,java.util.Date" %>
<%
    SalonSession mySalon = (SalonSession) session.getAttribute("SalonSession");
    if (mySalon == null) {
        getServletConfig().getServletContext().getRequestDispatcher("/reconnect.html").forward(request, response);
    }
%>
<%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %>
<html>
<head>
<%
   // Récupération des paramètres
   Date DT_DEBUT = (Date) request.getAttribute("DT_DEBUT");
   Date DT_FIN = (Date) request.getAttribute("DT_FIN");
%>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title>Réédition de factures</title>
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees" onLoad="document.fiche.DT_DEBUT.focus()">
<%@ include file="include/commun.js" %>
<script language="JavaScript">
<!--

function Init() {
      MM_showHideLayers('SUPPRIMER?bottomFrame','','hide');
      MM_showHideLayers('ENREGISTRER?bottomFrame','','hide');
      MM_showHideLayers('DUPLIQUER?bottomFrame','','hide');
      MM_showHideLayers('IMPRIMER?bottomFrame','','hide');
      MM_showHideLayers('VALIDER?bottomFrame','','show');
}
//-->
</script>
<h1><img src="images/titres/ficReFact.gif"></h1>
<salon:message salonSession="<%= mySalon %>" />
<form method="post" action="reFact.srv" name="fiche" target="_blank">
	<p> 
		<span class="obligatoire">Entre le :</span> 
        <salon:date type="text" name="DT_DEBUT" valeurDate="<%= DT_DEBUT %>" valeurNulle="null" format="dd/MM/yyyy" calendrier="true">%%</salon:date>
		<span class="obligatoire">et le :</span> 
        <salon:date type="text" name="DT_FIN" valeurDate="<%= DT_FIN %>" valeurNulle="null" format="dd/MM/yyyy" calendrier="true">%%</salon:date>
	</p>
        <p>
		<span class="obligatoire">Format d'édition :</span>
		  <select name="format">
		     <option value="F">Fiche</option>
		     <option value="L">Liste</option>
		  </select>
        </p>
</form>
<script language="JavaScript">
// Fonctions d'action

// Enregistrement des données du client
function Valider()
{
   document.fiche.submit();
}

// Affichage de l'aide
function Aide()
{
    window.open("aideFiche.html");
}

</script>
</body>
</html>
