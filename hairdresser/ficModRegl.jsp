<%@ page import="com.increg.salon.bean.SalonSession,
	       com.increg.salon.bean.ModReglBean" %>
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
   ModReglBean aModRegl = (ModReglBean) request.getAttribute("ModReglBean");
%>
<title>Fiche modes de règlement</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees" onLoad="Init();document.fiche.LIB_MOD_REGL.focus()">
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
}
//-->
</script>
<h1><img src="images/titres/ficParam.gif"><br><span class="ssTitre">Modes de règlement</span></h1>
<salon:message salonSession="<%= mySalon %>" />
<form method="post" action="ficModRegl.srv" name="fiche">
	<p> 
		<salon:valeur valeurNulle="0" valeur="<%= aModRegl.getCD_MOD_REGL() %>" >
		  <input type="hidden" name="CD_MOD_REGL" value="%%" >
	        </salon:valeur>
		<input type="hidden" name="Action" value="<%=Action%>">
		<span class="obligatoire">Libellé :</span> 
		<salon:valeur valeurNulle="null" valeur="<%= aModRegl.getLIB_MOD_REGL() %>" >
		  <input type="text" name="LIB_MOD_REGL" value="%%" size=40>
	        </salon:valeur>
		<span class="obligatoire">Actuel :</span> 
		<salon:selection valeur="<%= aModRegl.getUTILISABLE() %>" valeurs='<%= "O|N" %>' libelle="Oui|Non">
		  <select name="UTILISABLE">
		     %%
		  </select>
		</salon:selection>
	</p>
        <p>
		<span class="obligatoire">Impression de chèques :</span> 
		<salon:selection valeur="<%= aModRegl.getIMP_CHEQUE() %>" valeurs='<%= "O|N" %>' libelle="Oui|Non">
		  <select name="IMP_CHEQUE">
		     %%
		  </select>
		</salon:selection>
		<span class="obligatoire">Rendu de monnaie :</span> 
		<salon:selection valeur="<%= aModRegl.getRENDU_MONNAIE() %>" valeurs='<%= "O|N" %>' libelle="Oui|Non">
		  <select name="RENDU_MONNAIE">
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
   if (document.fiche.LIB_MOD_REGL.value == "") {
      alert ("Le libellé doit être saisi. L'enregistrement n'a pas pu avoir lieu.");
      return;
   }
   document.fiche.submit();
}

// Suppression du client
function Supprimer()
{
    if ((document.fiche.CD_MOD_REGL.value != "0") && (document.fiche.CD_MOD_REGL.value != "")) {
        if (confirm ("Cette suppression est définitive et non conseillée. Confirmez-vous cette action ?")) {
            document.fiche.Action.value = "Suppression";
            document.fiche.submit();
        }
    }
}

// Duplication de la prestation
function Dupliquer()
{
   if (document.fiche.LIB_MOD_REGL.value == "") {
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

</script>
</body>
</html>
