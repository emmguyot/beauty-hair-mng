<%@ page import="com.increg.salon.bean.SalonSession,
	       com.increg.salon.bean.IdentBean
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
<title>Fiche Identification</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees" onLoad="Init();document.fiche.LIB_IDENT.focus()">
<%@ include file="include/commun.js" %>
<script language="JavaScript">
<!--
<%
   // Récupération des paramètres
   String Action = (String) request.getAttribute("Action");
   IdentBean aIdent = (IdentBean) request.getAttribute("IdentBean");
%>
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
}
//-->
</script>
<h1><img src="images/titres/ficIdent.gif" alt=<salon:TimeStamp bean="<%= aIdent %>" />></h1>
<salon:message salonSession="<%= mySalon %>" />
<form method="post" action="ficIdent.srv" name="fiche">
	 <p> 
		<salon:valeur valeurNulle="0" valeur="<%= aIdent.getCD_IDENT() %>" >
		  <input type="hidden" name="CD_IDENT" value="%%" >
	        </salon:valeur>
		<input type="hidden" name="Action" value="<%=Action%>">
		<span class="obligatoire">Libellé :</span> 
		<salon:valeur valeurNulle="null" valeur="<%= aIdent.getLIB_IDENT() %>" >
		  <input type="text" name="LIB_IDENT" value="%%" size="30">
	        </salon:valeur>
	 </p>
	 <p>
		<span class="obligatoire">Mot de passe :</span> 
		<salon:valeur valeurNulle="null" valeur="<%= aIdent.getMOT_PASSE() %>" >
		  <input type="password" name="MOT_PASSE" value="%%" >
                  <span class="obligatoire">Mot de passe (pour vérification) :</span> 
		  <input type="password" name="MOT_PASSE2" value="%%" >
	        </salon:valeur>
        </p>
        <p>
	        <span class="obligatoire">Profil :</span> 
	        <salon:DBselection valeur="<%= aIdent.getCD_PROFIL() %>" sql='<%= "select CD_PROFIL, LIB_PROFIL from PROFIL order by LIB_PROFIL" %>'>
		  <select name="CD_PROFIL">
		     %%
		  </select>
		</salon:DBselection>
	        <span class="obligatoire">Etat du compte :</span> 
		<salon:selection valeur="<%= aIdent.getETAT_CPT() %>" valeurs='<%= "A|B" %>' libelle="Actif|Bloqué">
		  <select name="ETAT_CPT">
		     %%
		  </select>
		</salon:selection>
	</p>
</form>

<script language="JavaScript">
// Fonctions d'action

// Enregistrement des données
function Enregistrer()
{
   // Verification des données obligatoires
   if ((document.fiche.LIB_IDENT.value == "") || (document.fiche.MOT_PASSE.value == "")) {
      alert ("Le libellé et le mot de passe doivent être saisis. L'enregistrement n'a pas pu avoir lieu.");
      return;
   }
   document.fiche.submit();
}

// Suppression
function Supprimer()
{
    if ((document.fiche.CD_IDENT.value != "0") && (document.fiche.CD_IDENT.value != "")) {
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

</script>
</body>
</html>
