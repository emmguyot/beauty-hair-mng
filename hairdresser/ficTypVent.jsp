<%@ page import="com.increg.salon.bean.SalonSession,
	       com.increg.salon.bean.TypVentBean" %>
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
   TypVentBean aTypVent = (TypVentBean) request.getAttribute("TypVentBean");
%>
<title>Fiche type de prestations</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees" onLoad="Init();document.fiche.LIB_TYP_VENT.focus()">
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
<h1><img src="images/titres/ficParam.gif"><br><span class="ssTitre">Type de prestations</span></h1>
<salon:message salonSession="<%= mySalon %>" />
<form method="post" action="ficTypVent.srv" name="fiche">
	<p> 
		<salon:valeur valeurNulle="0" valeur="<%= aTypVent.getCD_TYP_VENT() %>" >
		  <input type="hidden" name="CD_TYP_VENT" value="%%" >
	        </salon:valeur>
		<input type="hidden" name="Action" value="<%=Action%>">
		<span class="obligatoire">Libellé :</span> 
		<salon:valeur valeurNulle="null" valeur="<%= aTypVent.getLIB_TYP_VENT() %>" >
                    <input type="text" name="LIB_TYP_VENT" value="%%" size=30>
	        </salon:valeur>
        </p>
	<p>
		<span class="obligatoire">Article associé :</span> 
                <salon:selection valeur='<%= aTypVent.getMARQUE() %>' valeurs='<%= "N|O" %>' libelle="Non|Oui">
                    <select name="MARQUE">
                        %%
                    </select>
                </salon:selection>
        </p>
	<p>
		<span class="obligatoire">Civilités associées :</span> 
                <salon:checkbox valeurs="Mle|Mme|M. " libelle="Mle|Mme|M. "
                                nom="CIVILITE" tabValeur='<%= aTypVent.getCIVILITE() %>'>
                    %%
                </salon:checkbox>
	</p>
</form>
<script language="JavaScript">
// Fonctions d'action

// Enregistrement des données 
function Enregistrer()
{
   // Verification des données obligatoires
   if (document.fiche.LIB_TYP_VENT.value == "") {
      alert ("Le libellé doit être saisi. L'enregistrement n'a pas pu avoir lieu.");
      return;
   }
   document.fiche.submit();
}

// Duplication de la prestation
function Dupliquer()
{
   if (document.fiche.LIB_TYP_VENT.value == "") {
      alert ("Le libellé doit être saisi. L'enregistrement n'a pas pu avoir lieu.");
      return;
   }
   document.fiche.Action.value = "Duplication";
   document.fiche.submit();
}

// Suppression de la fiche
function Supprimer()
{
    if ((document.fiche.CD_TYP_VENT.value != "0") && (document.fiche.CD_TYP_VENT.value != "")) {
        if (confirm ("Cette suppression est définitive. Confirmez-vous cette action ?")) {
            document.fiche.Action.value = "Suppression";
            document.fiche.submit();
        }
    }
}

// Affichage de l'aide
function Aide()
{
    window.open("aideFicheTypVent.html");
}

function RetourListe()
{
    parent.location.href = "ListeTypVent.jsp";
}


</script>
</body>
</html>
