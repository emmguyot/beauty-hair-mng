<%@ page import="com.increg.salon.bean.SalonSession
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
<%
   // Récupération des paramètres
   String Action = (String) request.getAttribute("Action");
   String Fichier = (String) request.getAttribute("Fichier");
   String Type = request.getParameter("Type");
%>
<title>Sauvegarde</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees" onLoad="document.fiche.Type.focus()">
<%@ include file="include/commun.js" %>
<script language="JavaScript">
<!--
   var Action="<%=Action%>";

function Init() {
      MM_showHideLayers('SUPPRIMER?bottomFrame','','hide');
      MM_showHideLayers('ENREGISTRER?bottomFrame','','hide');
      MM_showHideLayers('DUPLIQUER?bottomFrame','','hide');
      MM_showHideLayers('VALIDER?bottomFrame','','show');
}
//-->
</script>
<h1><img src="images/titres/ficSauv.gif"></h1>
<salon:message salonSession="<%= mySalon %>" />
<form method="post" action="sauvegarde.srv" name="fiche">
	<p> 
		<input type="hidden" name="Action" value="Sauvegarde">
		<input type="hidden" name="lock" value="">
		<span class="obligatoire">Nom de la sauvegarde :</span> 
		<salon:valeur valeurNulle="null" valeur="<%= Fichier %>" >
		  <input type="text" name="Fichier" value="%%" size=40>
	        </salon:valeur>
		<span class="obligatoire">Type de sauvegarde :</span> 
		<salon:selection valeur="<%= Type %>" valeurs='<%= "D|M|I" %>' libelle="Disque dur (tous les jours)|Média amovible (une fois par semaine)|Internet (une fois par semaine)">
		  <select name="Type">
		     %%
		  </select>
		</salon:selection>
	</p>
</form>
<span id="AttenteSpan" style="visibility: hidden">
<p class="Warning"><img name="Attente" src="images/attente.gif" width="231" height="10" alt="Opération en cours..."></p>
</span>
<salon:include file="include/salonNews.inc" />
<script language="JavaScript">
// Fonctions d'action

// Enregistrement des données du client
function Valider()
{
    if (document.fiche.lock.value == "") {
        // Verification des données obligatoires
        if (document.fiche.Fichier.value == "") {
            alert ("Le nom de la sauvegarde doit être saisi. La sauvegarde ne peut avoir lieu.");
            return;
        }
        MM_showHideLayers('AttenteSpan','','show');
        document.fiche.lock.value = "xx";
        document.fiche.submit();
    }
}

// Affichage de l'aide
function Aide()
{
    window.open("aideFicheRest.html");
}

</script>
</body>
</html>
