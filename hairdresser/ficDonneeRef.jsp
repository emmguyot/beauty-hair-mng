<%@ page import="java.net.URLEncoder, com.increg.salon.bean.SalonSession,
	       com.increg.salon.bean.DonneeRefBean" %>
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
   String nomTable= request.getParameter("nomTable");
   String chaineTable= request.getParameter("chaineTable");
   String Action = (String) request.getAttribute("Action");
   DonneeRefBean aDonneeRef = (DonneeRefBean) request.getAttribute("DonneeRefBean");
%>
<title><i18n:message key="ficDonneeRef.title" >
    <i18n:messageArg value="<%= chaineTable %>" />
</i18n:message></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees" onLoad="Init();document.fiche.LIB.focus()">
<%@ include file="include/commun.js" %>
<script language="JavaScript">
<!--
   var Action="<%=Action%>";

function Init() {
   <%
   // Positionne les liens d actions
   if (! Action.equals("Creation")) {
      %>
      MM_showHideLayers('SUPPRIMER?bottomFrame','','show');
      MM_showHideLayers('ENREGISTRER?bottomFrame','','show');
      MM_showHideLayers('DUPLIQUER?bottomFrame','','show');
      <%
   }
   if (Action.equals("Creation")) { %>
      // Pas de lien supprimer
      MM_showHideLayers('SUPPRIMER?bottomFrame','','hide');
      MM_showHideLayers('ENREGISTRER?bottomFrame','','show');
      MM_showHideLayers('DUPLIQUER?bottomFrame','','show');
   <%
   } %>
   MM_showHideLayers('RETOUR_LISTE?bottomFrame','','show');
}
//-->
</script>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/ficParam.gif"><br><span class="ssTitre"><%= chaineTable %></span></h1>
<salon:message salonSession="<%= mySalon %>" />
<form method="post" action="ficDonneeRef.srv" name="fiche">
	<p> 
		<salon:valeur valeurNulle="0" valeur="<%= aDonneeRef.getCD() %>" >
		  <input type="hidden" name="CD" value="%%" >
	        </salon:valeur>
		<input type="hidden" name="Action" value="<%=Action%>">
		<input type="hidden" name="nomTable" value="<%=nomTable%>">
		<input type="hidden" name="chaineTable" value="<%=chaineTable%>">
		<span class="obligatoire"><i18n:message key="label.libelle" /> :</span> 
		<salon:valeur valeurNulle="null" valeur="<%= aDonneeRef.getLIB() %>" >
		  <input type="text" name="LIB" value="%%" size=40>
	        </salon:valeur>
	</p>
</form>
<script language="JavaScript">
// Fonctions d'action

// Enregistrement des données du client
function Enregistrer()
{
   // Verification des données obligatoires
   if (document.fiche.LIB.value == "") {
      alert ("<i18n:message key="ficDonneeRef.libelleManquant" />");
      return;
   }
   document.fiche.submit();
}

// Suppression du client
function Supprimer()
{
    if ((document.fiche.CD.value != "0") && (document.fiche.CD.value != "")) {
        if (confirm ("<i18n:message key="message.suppressionDefinitiveConfirm" />")) {
            document.fiche.Action.value = "Suppression";
            document.fiche.submit();
        }
    }
}

// Duplication de la données
function Dupliquer()
{
   document.fiche.Action.value = "Duplication";
   document.fiche.submit();
}

// Affichage de l'aide
function Aide()
{
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideFiche.html");
}

function RetourListe()
{
   parent.location.href = "ListeDonneeRef.jsp?nomTable=<%= nomTable %>&chaineTable=<%= URLEncoder.encode(chaineTable) %>";
}

</script>
</body>
</html>
