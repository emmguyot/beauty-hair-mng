<%@ page import="com.increg.salon.bean.SalonSession,
	       com.increg.salon.bean.TrAgeBean" %>
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
   TrAgeBean aTrAge = (TrAgeBean) request.getAttribute("TrAgeBean");
%>
<title>Fiche tranches d'âge</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees" onLoad="Init();document.fiche.LIB_TR_AGE.focus()">
<%@ include file="include/commun.js" %>
<script language="JavaScript">
<!--
   var Action="<%=Action%>";

function Init() {
   <%
   // Positionne les liens d'actions
   if (! Action.equals("Creation")) {
      %>
      MM_showHideLayers('SUPPRIMER?bottomFrame','','show');
      MM_showHideLayers('ENREGISTRER?bottomFrame','','show');
      <%
   }
   if (Action.equals("Creation")) { %>
      // Pas de lien supprimer
      MM_showHideLayers('SUPPRIMER?bottomFrame','','hide');
      MM_showHideLayers('ENREGISTRER?bottomFrame','','show');
   <%
   } %>
   MM_showHideLayers('DUPLIQUER?bottomFrame','','show');
   MM_showHideLayers('RETOUR_LISTE?bottomFrame','','show');
}
//-->
</script>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/ficParam.gif"><br><span class="ssTitre">Tranches d'âge</span></h1>
<salon:message salonSession="<%= mySalon %>" />
<form method="post" action="ficTrAge.srv" name="fiche">
	<p> 
		<salon:valeur valeurNulle="0" valeur="<%= aTrAge.getCD_TR_AGE() %>" >
		  <input type="hidden" name="CD_TR_AGE" value="%%" >
	        </salon:valeur>
		<input type="hidden" name="Action" value="<%=Action%>">
		<span class="obligatoire">Libellé :</span> 
		<salon:valeur valeurNulle="null" valeur="<%= aTrAge.getLIB_TR_AGE() %>" >
		  <input type="text" name="LIB_TR_AGE" value="%%">
	        </salon:valeur>
        </p>
	<p>
		<span class="obligatoire">Age minimum :</span> 
		<salon:valeur valeur="<%= aTrAge.getAGE_MIN() %>" valeurNulle="null">
		  <input type="text" name="AGE_MIN" value="%%" size=3>
		</salon:valeur>
		&nbsp;
		<span class="obligatoire">Age maximum :</span> 
		<salon:valeur valeur="<%= aTrAge.getAGE_MAX() %>" valeurNulle="null">
		  <input type="text" name="AGE_MAX" value="%%" size=3>
		</salon:valeur>
	</p>
</form>
<script language="JavaScript">
// Fonctions d'action

// Enregistrement des données du client
function Enregistrer()
{
   // Verification des données obligatoires
   if ((document.fiche.LIB_TR_AGE.value == "") 
	 || (document.fiche.AGE_MIN.value == "") 
	 || (document.fiche.AGE_MAX.value == "")) {
      alert ("Toutes les données doivent être saisies. L'enregistrement n'a pas pu avoir lieu.");
      return;
   }
   document.fiche.submit();
}

// Duplication de la prestation
function Dupliquer()
{
   if ((document.fiche.LIB_TR_AGE.value == "") 
	 || (document.fiche.AGE_MIN.value == "") 
	 || (document.fiche.AGE_MAX.value == "")) {
      alert ("Toutes les données doivent être saisies. L'enregistrement n'a pas pu avoir lieu.");
      return;
   }
   document.fiche.Action.value = "Duplication";
   document.fiche.submit();
}

// Suppression du client
function Supprimer()
{
    if ((document.fiche.CD_TR_AGE.value != "0") && (document.fiche.CD_TR_AGE.value != "")) {
        if (confirm ("Cette suppression est définitive. Confirmez-vous cette action ?")) {
            document.fiche.Action.value = "Suppression";
            document.fiche.submit();
        }
    }
}

// Affichage de l'aide
function Aide()
{
    window.open("aideFiche.html");
}

function RetourListe()
{
    parent.location.href = "ListeTrAge.jsp";
}

</script>
</body>
</html>
