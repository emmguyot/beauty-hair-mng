<%@ page import="com.increg.salon.bean.SalonSession,
	       com.increg.salon.bean.TvaBean" %>
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
   TvaBean aTva = (TvaBean) request.getAttribute("TvaBean");
%>
<title>Fiche taux de TVA</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees" onLoad="Init();document.fiche.LIB_TVA.focus()">
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
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/ficParam.gif"><br><span class="ssTitre">Taux de TVA</span></h1>
<salon:message salonSession="<%= mySalon %>" />
<form method="post" action="ficTxTVA.srv" name="fiche">
	<p> 
		<salon:valeur valeurNulle="0" valeur="<%= aTva.getCD_TVA() %>" >
		  <input type="hidden" name="CD_TVA" value="%%" >
        </salon:valeur>
		<input type="hidden" name="Action" value="<%=Action%>">
		<span class="obligatoire">Libellé :</span> 
		<salon:valeur valeurNulle="null" valeur="<%= aTva.getLIB_TVA() %>" >
          <input type="text" name="LIB_TVA" value="%%" size=30>
        </salon:valeur>
        </p>
	<p>
		<span class="obligatoire">Taux de la TVA :</span> 
        <salon:valeur valeur='<%= aTva.getTX_TVA() %>' valeurNulle="null" >
          <input type="text" name="TX_TVA" value="%%" size=6>
        </salon:valeur>
        </p>
</form>
<script language="JavaScript">
// Fonctions d'action

// Enregistrement des données 
function Enregistrer()
{
   // Verification des données obligatoires
   if (document.fiche.LIB_TVA.value == "") {
      alert ("Le libellé doit être saisi. L'enregistrement n'a pas pu avoir lieu.");
      return;
   }
   document.fiche.submit();
}

// Duplication de la prestation
function Dupliquer()
{
   if (document.fiche.LIB_TVA.value == "") {
      alert ("Le libellé doit être saisi. L'enregistrement n'a pas pu avoir lieu.");
      return;
   }
   document.fiche.Action.value = "Duplication";
   document.fiche.submit();
}

// Suppression de la fiche
function Supprimer()
{
    if ((document.fiche.CD_TVA.value != "0") && (document.fiche.CD_TVA.value != "")) {
        if (confirm ("Cette suppression est définitive. Confirmez-vous cette action ?")) {
            document.fiche.Action.value = "Suppression";
            document.fiche.submit();
        }
    }
}

// Affichage de l'aide
function Aide()
{
    window.open("aideFicheParam.html");
}

function RetourListe()
{
    parent.location.href = "ListeTxTVA.jsp";
}

</script>
</body>
</html>
