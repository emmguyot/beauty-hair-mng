<%@ page import="com.increg.salon.bean.SalonSession,
	       com.increg.salon.bean.CollabBean
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
<title>Fiche Collaborateur</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees" onLoad="Init();document.fiche.NOM.focus()">
<%@ include file="include/commun.js" %>
<script language="JavaScript">
<!--
<%
   // Récupération des paramètres
   String Action = (String) request.getAttribute("Action");
   CollabBean aCollab = (CollabBean) request.getAttribute("CollabBean");
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
   MM_showHideLayers('RETOUR_LISTE?bottomFrame','','show');
}
//-->
</script>
<h1><img src="images/titres/ficCollab.gif" alt=<salon:TimeStamp bean="<%= aCollab %>" />></h1>
<salon:message salonSession="<%= mySalon %>" />
<form method="post" action="ficCollab.srv" name="fiche">
	 <p> 
		<salon:valeur valeurNulle="0" valeur="<%= aCollab.getCD_COLLAB() %>" >
		  <input type="hidden" name="CD_COLLAB" value="%%" >
	        </salon:valeur>
		<input type="hidden" name="Action" value="<%=Action%>">
		<span class="facultatif">Civilit&eacute; :</span> 
		<salon:selection valeur="<%= aCollab.getCIVILITE() %>" valeurs='<%= "|Mle|Mme|M. " %>'>
		  <select name="CIVILITE">
		     %%
		  </select>
		</salon:selection>
		<span class="obligatoire">Nom :</span> 
		<salon:valeur valeurNulle="null" valeur="<%= aCollab.getNOM() %>" >
		  <input type="text" name="NOM" value="%%" >
	        </salon:valeur>
		<span class="obligatoire">Pr&eacute;nom :</span> 
		<salon:valeur valeurNulle="null" valeur="<%= aCollab.getPRENOM() %>" >
		  <input type="text" name="PRENOM" value="%%" >
	        </salon:valeur>
	</p>
	<table border="0" cellspacing="0">
		<tr> 
			<td> 
				<p><span class="facultatif">Adresse :</span> 
				       <salon:valeur valeurNulle="null" valeur="<%= aCollab.getRUE() %>" >
					  <textarea name="RUE" rows="2" cols="40">%%</textarea>
				       </salon:valeur>
				</p>
				<p><span class="facultatif">Code postal :</span> 
				       <salon:valeur valeurNulle="null" valeur="<%= aCollab.getCD_POSTAL() %>" >
					  <input type="text" name="CD_POSTAL" size="6" maxlength="5" value="%%">
				       </salon:valeur>
					<span class="facultatif">Ville :</span> 
				       <salon:valeur valeurNulle="null" valeur="<%= aCollab.getVILLE() %>" >
					  <input type="text" name="VILLE" value="%%">
				       </salon:valeur>
				</p>
			</td>
			<td> 
				<p align="right"><span class="facultatif">T&eacute;l&eacute;phone :</span></p>
				<p align="right"><span class="facultatif">Email :</span></p>
			</td>
			<td>
				<p>
				    <salon:valeur valeurNulle="null" valeur="<%= aCollab.getTEL() %>" >
					<input type="text" name="TEL" value="%%" size=14>
				    </salon:valeur>
				<span class="facultatif">Portable&nbsp;:&nbsp;</span>
				    <salon:valeur valeurNulle="null" valeur="<%= aCollab.getPORTABLE() %>" >
					<input type="text" name="PORTABLE" value="%%" size=14>
				    </salon:valeur>
				</p>
				<p>
				    <salon:valeur valeurNulle="null" valeur="<%= aCollab.getEMAIL() %>" >
					<input type="text" name="EMAIL" value="%%" size=30> <a href="mailto:%%"><img src="images/email.gif" border="0" align="absmiddle"></a>
				    </salon:valeur>
				</p>
			</td>
		</tr>
	</table>
	<p>
	<span class="facultatif">Date de naissance :</span> 
	    <salon:valeur valeurNulle="null" valeur="<%= aCollab.getDT_NAIS() %>" >
		  <input type="text" name="DT_NAIS" value="%%" size=10 onChange="FormateDate(this)">
	    </salon:valeur>
	<span class="facultatif">N° sécurité sociale :</span> 
	    <salon:valeur valeurNulle="null" valeur="<%= aCollab.getNUM_SECU() %>" >
		  <input type="text" name="NUM_SECU" value="%%" size=15>
	    </salon:valeur>
        </p>
	<p>
	<span class="facultatif">Fonction :</span> 
		<salon:DBselection valeur="<%= aCollab.getCD_FCT() %>" sql="select CD_FCT, LIB_FCT from FCT order by LIB_FCT">
		  <select name="CD_FCT">
		     <option value=""></option>
		     %%
		  </select>
	        </salon:DBselection>
	<span class="facultatif">Type de contrat :</span> 
		<salon:DBselection valeur="<%= aCollab.getCD_TYP_CONTR() %>" sql="select CD_TYP_CONTR, LIB_TYP_CONTR from TYP_CONTR order by LIB_TYP_CONTR">
		  <select name="CD_TYP_CONTR">
		     <option value=""></option>
		     %%
		  </select>
	        </salon:DBselection>
	<span class="obligatoire">Collaborateur valide :</span> 
		<salon:selection valeur="<%= aCollab.getINDIC_VALID() %>" valeurs='<%= "O|N" %>' libelle="Oui|Non">
		  <select name="INDIC_VALID">
		     %%
		  </select>
		</salon:selection>
	</p>
	<p>
	<span class="facultatif">Catégorie :</span> 
	    <salon:valeur valeurNulle="null" valeur="<%= aCollab.getCATEG() %>" >
		  <input type="text" name="CATEG" value="%%" size=3>
	    </salon:valeur>
	<span class="facultatif">Echelon :</span> 
	    <salon:valeur valeurNulle="null" valeur="<%= aCollab.getECHELON() %>" >
		  <input type="text" name="ECHELON" value="%%" size=3>
	    </salon:valeur>
	<span class="facultatif">Coefficient :</span> 
	    <salon:valeur valeurNulle="null" valeur="<%= aCollab.getCOEF() %>" >
		  <input type="text" name="COEF" value="%%" size=3>
	    </salon:valeur>
	<span class="facultatif">Heures par jour :</span> 
	    <salon:valeur valeurNulle="null" valeur="<%= aCollab.getQUOTA_HEURE() %>" >
		  <input type="text" name="QUOTA_HEURE" value="%%" size=4>
	    </salon:valeur>
	</p>
</form>

<script language="JavaScript">
// Fonctions d'action

// Enregistrement des données
function Enregistrer()
{
   // Verification des données obligatoires
   if ((document.fiche.NOM.value == "") || (document.fiche.PRENOM.value == "")) {
      alert ("Le nom et le prénom doivent être saisis. L'enregistrement n'a pas pu avoir lieu.");
      return;
   }
   document.fiche.submit();
}

// Création d'un nouveau collab
function Nouveau()
{
   parent.location.href = "_FicheCollab.jsp";
}

// Suppression
function Supprimer()
{
    if ((document.fiche.CD_COLLAB.value != "0") && (document.fiche.CD_COLLAB.value != "")) {
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
    parent.location.href = "ListeCollab.jsp";
}

</script>
</body>
</html>
