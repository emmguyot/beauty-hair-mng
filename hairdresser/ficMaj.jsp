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
<title><i18n:message key="ficMaj.title" /></title>
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
	<p class="warning"> <i18n:message key="ficMaj.warning" /> </p>
	<p>
		<input type="hidden" name="Action" value="MiseAJour">
		<input type="hidden" name="lock" value="">
		<span class="obligatoire"><i18n:message key="ficMaj.source" />  :</span> 
                <i18n:message key="valeur.sourceMaj" id="valeurSource" />
		<salon:selection valeur="<%= Type %>" valeurs='<%= "I|D" %>' libelle="<%= valeurSource %>">
		  <select name="Type" onChange="rechargeListe()">
		     %%
		  </select>
		</salon:selection>
		<span class="obligatoire"><i18n:message key="label.versionUpdate" /> :</span> 
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
<p class="Warning"><img name="Attente" src="images/attente.gif" width="231" height="10" alt="<i18n:message key="message.patience" />"></p>
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
        if (confirm("<i18n:message key="ficMaj.warningAvantAction" />")) {
            MM_showHideLayers('AttenteSpan','','show');
            document.fiche.lock.value = "xx";
            document.fiche.submit();
        }
    }
}

// Affichage de l'aide
function Aide()
{
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideFiche.html");
}

</script>
</body>
</html>
