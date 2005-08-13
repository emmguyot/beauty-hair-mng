<%@ page import="java.util.TreeSet, java.util.Iterator" %>
<%@ page import="com.increg.salon.bean.SalonSession
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
<%
   // Récupération des paramètres
   String Action = (String) request.getAttribute("Action");
   TreeSet listeFichier = (TreeSet) request.getAttribute("listeFichier");
   String Type = (String) request.getAttribute("Type");
%>
<title>Mise à jour</title>
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
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/ficMaj.gif"></h1>
<salon:message salonSession="<%= mySalon %>" />
<form method="post" action="miseAJour.srv" name="fiche">
	<p class="warning"> Attention : La mise à jour peut être longue. </p>
	<p>
		<input type="hidden" name="Action" value="MiseAJour">
		<input type="hidden" name="lock" value="">
		<span class="obligatoire">Mise à jour à partir de  :</span> 
		<salon:selection valeur="<%= Type %>" valeurs='<%= "I|D" %>' libelle="Internet|Disque dur">
		  <select name="Type" onChange="rechargeListe()">
		     %%
		  </select>
		</salon:selection>
		<span class="obligatoire">Version à installer :</span> 
		  <select name="nomFichier">
		<%
		  for (Iterator i=listeFichier.iterator(); i.hasNext(); ) {
		     String nom = (String) i.next();
	        %>
		<salon:valeur valeurNulle="null" valeur="<%= nom %>" >
		  <option value="%%">%%</option>
	        </salon:valeur>
	        <% } %>
		</select>
	</p>
</form>
<span id="AttenteSpan" style="visibility: hidden">
<p class="Warning"><img name="Attente" src="images/attente.gif" width="231" height="10" alt="Opération en cours..."></p>
</span>
<script language="JavaScript">
// Fonctions d'action

function rechargeListe()
{
   document.fiche.Action.value = "Liste";
   document.fiche.submit();
}

// Enregistrement des données du client
function Valider()
{
    if (document.fiche.lock.value == "") {
        if (confirm("Attention vous allez supprimer le programme actuel pour le remplacer par cette nouvelle version. Cette mise à jour peut prendre plusieurs minutes. Etes-vous sur de vouloir lancer la mise à jour ?")) {
            MM_showHideLayers('AttenteSpan','','show');
            document.fiche.lock.value = "xx";
            document.fiche.submit();
        }
    }
}

// Affichage de l'aide
function Aide()
{
    window.open("aideFiche.html");
}

</script>
</body>
</html>
