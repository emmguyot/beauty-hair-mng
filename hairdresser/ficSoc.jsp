<%@ page import="com.increg.salon.bean.SalonSession,
	       com.increg.salon.bean.SocieteBean
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
<title>Fiche Société</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees" onLoad="document.fiche.RAIS_SOC.focus()">
<%@ include file="include/commun.js" %>
<script language="JavaScript">
<!--
<%
   // Récupération des paramètres
   String Action = (String) request.getAttribute("Action");
   SocieteBean aSoc = (SocieteBean) request.getAttribute("SocBean");
%>
   var Action="<%=Action%>";

function Init() {
    // Positionne les liens d'actions
    MM_showHideLayers('ENREGISTRER?bottomFrame','','show');
}
//-->
</script>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/ficSoc.gif" alt=<salon:TimeStamp bean="<%= aSoc %>" />></h1>
<salon:message salonSession="<%= mySalon %>" />
<form method="post" action="ficSoc.srv" name="fiche">
	 <p> 
		<input type="hidden" name="Action" value="<%=Action%>">
		<span class="obligatoire">Raison sociale :</span> 
		<salon:valeur valeurNulle="null" valeur="<%= aSoc.getRAIS_SOC() %>" >
		  <input type="text" name="RAIS_SOC" value="%%" size="30">
	        </salon:valeur>
	 </p>
	 <p>
		<font size="+1">Gérant :</font><br>
		<span class="facultatif">Civilit&eacute; :</span> 
		<salon:selection valeur="<%= aSoc.getCIVILITE_GER() %>" valeurs='<%= "|Mle|Mme|M. " %>'>
		  <select name="CIVILITE_GER">
		     %%
		  </select>
		</salon:selection>
		<span class="obligatoire">Nom :</span> 
		<salon:valeur valeurNulle="null" valeur="<%= aSoc.getNOM_GER() %>" >
		  <input type="text" name="NOM_GER" value="%%" >
	        </salon:valeur>
		<span class="facultatif">Pr&eacute;nom :</span> 
		<salon:valeur valeurNulle="null" valeur="<%= aSoc.getPRENOM_GER() %>" >
		  <input type="text" name="PRENOM_GER" value="%%" >
	        </salon:valeur>
	</p>
	<table border="0" cellspacing="0">
		<tr> 
			<td> 
				<p><span class="facultatif">Adresse :</span> 
				       <salon:valeur valeurNulle="null" valeur="<%= aSoc.getRUE() %>" >
					  <textarea name="RUE" rows="2" cols="40">%%</textarea>
				       </salon:valeur>
				</p>
				<p><span class="facultatif">Code postal :</span> 
				       <salon:valeur valeurNulle="null" valeur="<%= aSoc.getCD_POSTAL() %>" >
					  <input type="text" name="CD_POSTAL" size="6" maxlength="5" value="%%">
				       </salon:valeur>
					<span class="facultatif">Ville :</span> 
				       <salon:valeur valeurNulle="null" valeur="<%= aSoc.getVILLE() %>" >
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
				    <salon:valeur valeurNulle="null" valeur="<%= aSoc.getTEL() %>" >
					<input type="text" name="TEL" value="%%" size=14>
				    </salon:valeur>
				<span class="facultatif">Portable&nbsp;:&nbsp;</span>
				    <salon:valeur valeurNulle="null" valeur="<%= aSoc.getPORTABLE() %>" >
					<input type="text" name="PORTABLE" value="%%" size=14>
				    </salon:valeur>
				</p>
				<p>
				    <salon:valeur valeurNulle="null" valeur="<%= aSoc.getEMAIL() %>" >
					<input type="text" name="EMAIL" value="%%" size=30>
				    </salon:valeur>
				</p>
			</td>
		</tr>
	</table>
        <p>
            <span class="obligatoire">Code Siret :</span> 
            <salon:valeur valeurNulle="null" valeur="<%= aSoc.getCD_SIRET() %>" >
                <input type="text" name="CD_SIRET" value="%%" maxlength="14" size="15">
            </salon:valeur>
            <span class="obligatoire">Code NAF :</span> 
            <salon:valeur valeurNulle="null" valeur="<%= aSoc.getCD_APE() %>" >
                <input type="text" name="CD_APE" value="%%" maxlength="4" size="5">
            </salon:valeur>
        </p>
        <p>
            <span class="obligatoire">Données Salon de Coiffure :</span> 
			<salon:selection valeur="<%= aSoc.getFLG_SALON() %>" valeurs='<%= "O|N" %>' libelle='<%= "Oui|Non" %>' >
			  <select name="FLG_SALON">
			     %%
			  </select>
			</salon:selection>
            <span class="obligatoire">Données Institut de Beauté :</span> 
			<salon:selection valeur="<%= aSoc.getFLG_INSTITUT() %>" valeurs='<%= "O|N" %>' libelle='<%= "Oui|Non" %>' >
			  <select name="FLG_INSTITUT">
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
    if ((document.fiche.RAIS_SOC.value == "") || (document.fiche.NOM_GER.value == "")) {
        alert ("La raison sociale et le nom doivent être saisis. L'enregistrement n'a pas pu avoir lieu.");
        return;
    }
    if (document.fiche.CD_SIRET.value == "") {
        document.fiche.CD_SIRET.value = "SO";
    }
    if (document.fiche.CD_APE.value == "") {
        document.fiche.CD_APE.value = "SO";
    }
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
