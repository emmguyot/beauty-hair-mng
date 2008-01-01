<%
/*
 * This program is part of InCrEG LibertyLook software http://beauty-hair-mng.sourceforge.net
 * Copyright (C) 2001-2008 Emmanuel Guyot <See emmguyot on SourceForge> 
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms 
 * of the GNU General Public License as published by the Free Software Foundation; either 
 * version 2 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; 
 * if not, write to the 
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 */
%>
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
<title><i18n:message key="ficSoc.title" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees" onLoad="document.fiche.RAIS_SOC.focus()">
<%@ include file="include/commun.jsp" %>
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
		<span class="obligatoire"><i18n:message key="label.raisSoc" /> :</span> 
		<salon:valeur valeurNulle="null" valeur="<%= aSoc.getRAIS_SOC() %>" >
		  <input type="text" name="RAIS_SOC" value="%%" size="30">
	        </salon:valeur>
	 </p>
	 <p>
		<font size="+1"><i18n:message key="label.gerant" /> :</font><br>
		<span class="facultatif"><i18n:message key="label.civilite" /> :</span> 
                <i18n:message key="valeur.civilite" id="valeurCivilite" />
                <salon:selection valeur="<%= aSoc.getCIVILITE_GER() %>" valeurs='<%= "|Mle|Mme|M. " %>' libelle='<%= valeurCivilite %>'>

		  <select name="CIVILITE_GER">
		     %%
		  </select>
		</salon:selection>
		<span class="obligatoire"><i18n:message key="label.nom" /> :</span> 
		<salon:valeur valeurNulle="null" valeur="<%= aSoc.getNOM_GER() %>" >
		  <input type="text" name="NOM_GER" value="%%" >
	        </salon:valeur>
		<span class="facultatif"><i18n:message key="label.prenom" /> :</span> 
		<salon:valeur valeurNulle="null" valeur="<%= aSoc.getPRENOM_GER() %>" >
		  <input type="text" name="PRENOM_GER" value="%%" >
	        </salon:valeur>
	</p>
	<table border="0" cellspacing="0">
		<tr> 
			<td> 
				<p><span class="facultatif"><i18n:message key="label.adresse" /> :</span> 
				       <salon:valeur valeurNulle="null" valeur="<%= aSoc.getRUE() %>" >
					  <textarea name="RUE" rows="2" cols="40">%%</textarea>
				       </salon:valeur>
				</p>
				<p><span class="facultatif"><i18n:message key="label.cdPostal" /> :</span> 
				       <salon:valeur valeurNulle="null" valeur="<%= aSoc.getCD_POSTAL() %>" >
					  <input type="text" name="CD_POSTAL" size="6" maxlength="5" value="%%">
				       </salon:valeur>
					<span class="facultatif"><i18n:message key="label.ville" /> :</span> 
				       <salon:valeur valeurNulle="null" valeur="<%= aSoc.getVILLE() %>" >
					  <input type="text" name="VILLE" value="%%">
				       </salon:valeur>
				</p>
			</td>
			<td> 
				<p align="right"><span class="facultatif"><i18n:message key="label.telephone" /> :</span></p>
				<p align="right"><span class="facultatif"><i18n:message key="label.email" /> :</span></p>
			</td>
			<td>
				<p>
				    <salon:valeur valeurNulle="null" valeur="<%= aSoc.getTEL() %>" >
					<input type="text" name="TEL" value="%%" size=14>
				    </salon:valeur>
				<span class="facultatif"><i18n:message key="label.portable" />&nbsp;:&nbsp;</span>
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
            <span class="obligatoire"><i18n:message key="label.siret" /> :</span> 
            <salon:valeur valeurNulle="null" valeur="<%= aSoc.getCD_SIRET() %>" >
                <input type="text" name="CD_SIRET" value="%%" maxlength="14" size="15">
            </salon:valeur>
            <span class="obligatoire"><i18n:message key="label.naf" /> :</span> 
            <salon:valeur valeurNulle="null" valeur="<%= aSoc.getCD_APE() %>" >
                <input type="text" name="CD_APE" value="%%" maxlength="4" size="5">
            </salon:valeur>
        </p>
        <p>
            <span class="obligatoire"><i18n:message key="label.donneesSalon" /> :</span> 
			<salon:selection valeur="<%= aSoc.getFLG_SALON() %>" valeurs='<%= "O|N" %>' libelle='<%= "Oui|Non" %>' >
			  <select name="FLG_SALON">
			     %%
			  </select>
			</salon:selection>
            <span class="obligatoire"><i18n:message key="label.donneesInstitut" /> :</span> 
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
        alert ("<i18n:message key="ficSoc.raiSocManquant" />");
        return;
    }
    if (document.fiche.CD_SIRET.value == "") {
        document.fiche.CD_SIRET.value = "<i18n:message key="valeur.SO" />";
    }
    if (document.fiche.CD_APE.value == "") {
        document.fiche.CD_APE.value = "<i18n:message key="valeur.SO" />";
    }
    document.fiche.submit();
}

// Affichage de l'aide
function Aide()
{
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideFiche.html");
}

</script>
</body>
</html>
