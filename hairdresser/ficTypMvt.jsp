<%@ page import="com.increg.salon.bean.SalonSession,
	       com.increg.salon.bean.TypMvtBean" %>
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
   TypMvtBean aTypMvt = (TypMvtBean) request.getAttribute("TypMvtBean");
%>
<title>Fiche Types de mouvements</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees" onLoad="Init();document.fiche.LIB_TYP_MVT.focus()">
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
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/ficParam.gif"><br><span class="ssTitre">Types de mouvements</span></h1>
<salon:message salonSession="<%= mySalon %>" />
<form method="post" action="ficTypMvt.srv" name="fiche">
	<p> 
		<salon:valeur valeurNulle="0" valeur="<%= aTypMvt.getCD_TYP_MVT() %>" >
		  <input type="hidden" name="CD_TYP_MVT" value="%%" >
	        </salon:valeur>
		<input type="hidden" name="Action" value="<%=Action%>">
		<span class="obligatoire">Libellé :</span> 
		<salon:valeur valeurNulle="null" valeur="<%= aTypMvt.getLIB_TYP_MVT() %>" >
		  <input type="text" name="LIB_TYP_MVT" value="%%" size=40>
	        </salon:valeur>
		<span class="obligatoire">Sens du mouvement :</span> 
		<salon:selection valeur="<%= aTypMvt.getSENS_MVT() %>" valeurs='<%= "E|S|I" %>' libelle="Entr&eacute;e|Sortie|Inventaire">
		  <select name="SENS_MVT">
		     %%
		  </select>
		</salon:selection>
		<span class="obligatoire">Basculement article mixte :</span> 
		<salon:selection valeur="<%= aTypMvt.getTRANSF_MIXTE() %>" valeurs='<%= "O|N" %>' libelle="Oui|Non">
		  <select name="TRANSF_MIXTE">
		     %%
		  </select>
		</salon:selection>
	</p>
</form>
<script language="JavaScript">
// Fonctions d'action

// Enregistrement des données du client
function Enregistrer()
{
   // Verification des données obligatoires
   if (document.fiche.LIB_TYP_MVT.value == "") {
      alert ("Le libellé doit être saisi. L'enregistrement n'a pas pu avoir lieu.");
      return;
   }
   document.fiche.submit();
}

// Suppression du client
function Supprimer()
{
    if ((document.fiche.CD_TYP_MVT.value != "0") && (document.fiche.CD_TYP_MVT.value != "")) {
        if (confirm ("Cette suppression est définitive et non conseillée. Confirmez-vous cette action ?")) {
            document.fiche.Action.value = "Suppression";
            document.fiche.submit();
        }
    }
}

// Duplication de la prestation
function Dupliquer()
{
   if (document.fiche.LIB_TYP_MVT.value == "") {
      alert ("Le libellé doit être saisi. L'enregistrement n'a pas pu avoir lieu.");
      return;
   }
   document.fiche.Action.value = "Duplication";
   document.fiche.submit();
}

// Affichage de l'aide
function Aide()
{
    window.open("aideFiche.html");
}

function RetourListe()
{
    parent.location.href = "ListeTypMvt.jsp";
}

</script>
</body>
</html>
