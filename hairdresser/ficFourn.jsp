<%@ page import="com.increg.salon.bean.SalonSession,
	       com.increg.salon.bean.FournBean
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
<title>Fiche Fournisseur</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees" onLoad="Init();document.fiche.RAIS_SOC.focus()">
<%@ include file="include/commun.js" %>
<script language="JavaScript">
<!--
<%
   // Récupération des paramètres
   String Action = (String) request.getAttribute("Action");
   FournBean aFourn = (FournBean) request.getAttribute("FournBean");
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
   MM_showHideLayers('NOUVEAU?bottomFrame','','show');
}
//-->
</script>
<h1><img src="images/titres/ficFourn.gif" alt=<salon:TimeStamp bean="<%= aFourn %>" />></h1>
<salon:message salonSession="<%= mySalon %>" />
<form method="post" action="ficFourn.srv" name="fiche">
	 <p> 
		<salon:valeur valeurNulle="0" valeur="<%= aFourn.getCD_FOURN() %>" >
		  <input type="hidden" name="CD_FOURN" value="%%" >
	        </salon:valeur>
		<input type="hidden" name="Action" value="<%=Action%>">
		<span class="obligatoire">Raison sociale :</span> 
		<salon:valeur valeurNulle="null" valeur="<%= aFourn.getRAIS_SOC() %>" >
		  <input type="text" name="RAIS_SOC" value="%%" size="30">
	        </salon:valeur>
	 </p>
	 <p>
		<font size="+1">Contact :</font><br>
		<span class="facultatif">Civilit&eacute; :</span> 
		<salon:selection valeur="<%= aFourn.getCIVILITE_CONT() %>" valeurs='<%= "|Mle|Mme|M. " %>'>
		  <select name="CIVILITE_CONT">
		     %%
		  </select>
		</salon:selection>
		<span class="obligatoire">Nom :</span> 
		<salon:valeur valeurNulle="null" valeur="<%= aFourn.getNOM_CONT() %>" >
		  <input type="text" name="NOM_CONT" value="%%" >
	        </salon:valeur>
		<span class="facultatif">Pr&eacute;nom :</span> 
		<salon:valeur valeurNulle="null" valeur="<%= aFourn.getPRENOM_CONT() %>" >
		  <input type="text" name="PRENOM_CONT" value="%%" >
	        </salon:valeur>
	</p>
	<table border="0" cellspacing="0">
		<tr> 
			<td> 
				<p><span class="facultatif">Adresse :</span> 
				       <salon:valeur valeurNulle="null" valeur="<%= aFourn.getRUE() %>" >
					  <textarea name="RUE" rows="2" cols="40">%%</textarea>
				       </salon:valeur>
				</p>
				<p><span class="facultatif">Code postal :</span> 
				       <salon:valeur valeurNulle="null" valeur="<%= aFourn.getCD_POSTAL() %>" >
					  <input type="text" name="CD_POSTAL" size="6" maxlength="5" value="%%">
				       </salon:valeur>
					<span class="facultatif">Ville :</span> 
				       <salon:valeur valeurNulle="null" valeur="<%= aFourn.getVILLE() %>" >
					  <input type="text" name="VILLE" value="%%">
				       </salon:valeur>
				</p>
			</td>
			<td> 
				<p align="right"><span class="facultatif">T&eacute;l&eacute;phone :</span></p>
				<p align="right"><span class="facultatif">Fax :</span></p>
				<p align="right"><span class="facultatif">Email :</span></p>
			</td>
			<td>
				<p>
				    <salon:valeur valeurNulle="null" valeur="<%= aFourn.getTEL() %>" >
					<input type="text" name="TEL" value="%%" size=14>
				    </salon:valeur>
				<span class="facultatif">Portable&nbsp;:&nbsp;</span>
				    <salon:valeur valeurNulle="null" valeur="<%= aFourn.getPORTABLE() %>" >
					<input type="text" name="PORTABLE" value="%%" size=14>
				    </salon:valeur>
				</p>
				<p>
				    <salon:valeur valeurNulle="null" valeur="<%= aFourn.getFAX() %>" >
					<input type="text" name="FAX" value="%%" size=14>
				    </salon:valeur>
				</p>
				<p>
				    <salon:valeur valeurNulle="null" valeur="<%= aFourn.getEMAIL() %>" >
					<input type="text" name="EMAIL" value="%%" size=30> <a href="mailto:%%"><img src="images/email.gif" border="0" align="absmiddle"></a>
				    </salon:valeur>
				</p>
			</td>
		</tr>
	</table>
	<p><span class="facultatif">Mode de règlement :</span> 
	       <salon:DBselection valeur="<%= aFourn.getCD_MOD_REGL() %>" sql='<%= "select CD_MOD_REGL, LIB_MOD_REGL from MOD_REGL where UTILISABLE=\'O\' or CD_MOD_REGL=" + Integer.toString(aFourn.getCD_MOD_REGL()) + " order by LIB_MOD_REGL" %>'>
		  <select name="CD_MOD_REGL">
		     <option value=""></option>
		     %%
		  </select>
	        </salon:DBselection>
	</p>
	<table border="0" cellspacing="0">
        <tr>
	<td>
	 <span class="facultatif">Commentaire :</span> 
	    <salon:valeur valeurNulle="null" valeur="<%= aFourn.getCOMM() %>" >
		<textarea name="COMM" cols="40" rows="2">%%</textarea>
	    </salon:valeur>
	</td>
	</tr>
	</table>
</form>

<script language="JavaScript">
// Fonctions d'action

// Enregistrement des données
function Enregistrer()
{
   // Verification des données obligatoires
   if ((document.fiche.RAIS_SOC.value == "") || (document.fiche.NOM_CONT.value == "")) {
      alert ("La raison sociale et le nom doivent être saisis. L'enregistrement n'a pas pu avoir lieu.");
      return;
   }
   document.fiche.submit();
}

// Création d'un nouveau fournisseur
function Nouveau()
{
   parent.location.href = "_FicheFourn.jsp";
}

// Suppression
function Supprimer()
{
    if ((document.fiche.CD_FOURN.value != "0") && (document.fiche.CD_FOURN.value != "")) {
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
