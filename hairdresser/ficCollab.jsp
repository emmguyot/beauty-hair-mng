<%
/*
 * This program is part of InCrEG LibertyLook software http://beauty-hair-mng.sourceforge.net
 * Copyright (C) 2001-2009 Emmanuel Guyot <See emmguyot on SourceForge> 
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
	       com.increg.salon.bean.CollabBean
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
<title><i18n:message key="ficCollab.title" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees" onLoad="Init();document.fiche.NOM.focus()">
<%@ include file="include/commun.jsp" %>
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
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/ficCollab.gif" alt=<salon:TimeStamp bean="<%= aCollab %>" />></h1>
<salon:message salonSession="<%= mySalon %>" />
<form method="post" action="ficCollab.srv" name="fiche">
	 <p> 
		<salon:valeur valeurNulle="0" valeur="<%= aCollab.getCD_COLLAB() %>" >
		  <input type="hidden" name="CD_COLLAB" value="%%" >
	        </salon:valeur>
		<input type="hidden" name="Action" value="<%=Action%>">
		<span class="facultatif"><i18n:message key="label.civilite" /> :</span> 
                <i18n:message key="valeur.civilite" id="valeurCivilite" />
		<salon:selection valeur="<%= aCollab.getCIVILITE() %>" valeurs='<%= "|Mle|Mme|M. " %>' libelle="<%= valeurCivilite %>">
		  <select name="CIVILITE">
		     %%
		  </select>
		</salon:selection>
		<span class="obligatoire"><i18n:message key="label.nom" /> :</span> 
		<salon:valeur valeurNulle="null" valeur="<%= aCollab.getNOM() %>" >
		  <input type="text" name="NOM" value="%%" >
	        </salon:valeur>
		<span class="obligatoire"><i18n:message key="label.prenom" /> :</span> 
		<salon:valeur valeurNulle="null" valeur="<%= aCollab.getPRENOM() %>" >
		  <input type="text" name="PRENOM" value="%%" >
	        </salon:valeur>
	</p>
	<table border="0" cellspacing="0">
		<tr> 
			<td> 
				<p><span class="facultatif"><i18n:message key="label.adresse" /> :</span> 
				       <salon:valeur valeurNulle="null" valeur="<%= aCollab.getRUE() %>" >
					  <textarea name="RUE" rows="2" cols="40">%%</textarea>
				       </salon:valeur>
				</p>
				<p><span class="facultatif"><i18n:message key="label.cdPostal" /> :</span> 
				       <salon:valeur valeurNulle="null" valeur="<%= aCollab.getCD_POSTAL() %>" >
					  <input type="text" name="CD_POSTAL" size="6" maxlength="5" value="%%">
				       </salon:valeur>
					<span class="facultatif"><i18n:message key="label.ville" /> :</span> 
				       <salon:valeur valeurNulle="null" valeur="<%= aCollab.getVILLE() %>" >
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
				    <salon:valeur valeurNulle="null" valeur="<%= aCollab.getTEL() %>" >
					<input type="text" name="TEL" value="%%" size=14>
				    </salon:valeur>
				<span class="facultatif"><i18n:message key="label.portable" />&nbsp;:&nbsp;</span>
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
	<span class="facultatif"><i18n:message key="label.dtNaissance" /> :</span> 
	    <salon:valeur valeurNulle="null" valeur="<%= aCollab.getDT_NAIS() %>" >
		  <input type="text" name="DT_NAIS" value="%%" size=10 onChange="FormateDate(this)">
	    </salon:valeur>
	<span class="facultatif"><i18n:message key="label.numSecu" /> :</span> 
	    <salon:valeur valeurNulle="null" valeur="<%= aCollab.getNUM_SECU() %>" >
		  <input type="text" name="NUM_SECU" value="%%" size=15>
	    </salon:valeur>
        </p>
	<p>
	<span class="facultatif"><i18n:message key="label.fonction" /> :</span> 
		<salon:DBselection valeur="<%= aCollab.getCD_FCT() %>" sql="select CD_FCT, LIB_FCT from FCT order by LIB_FCT">
		  <select name="CD_FCT">
		     <option value=""></option>
		     %%
		  </select>
	        </salon:DBselection>
	<span class="facultatif"><i18n:message key="label.typeContrat" /> :</span> 
		<salon:DBselection valeur="<%= aCollab.getCD_TYP_CONTR() %>" sql="select CD_TYP_CONTR, LIB_TYP_CONTR from TYP_CONTR order by LIB_TYP_CONTR">
		  <select name="CD_TYP_CONTR">
		     <option value=""></option>
		     %%
		  </select>
	        </salon:DBselection>
	<span class="obligatoire"><i18n:message key="label.collabActuel" /> :</span> 
                <i18n:message key="valeur.ouiNon" id="valeurOuiNon" />
		<salon:selection valeur="<%= aCollab.getINDIC_VALID() %>" valeurs='<%= "O|N" %>' libelle="<%= valeurOuiNon %>">
		  <select name="INDIC_VALID">
		     %%
		  </select>
		</salon:selection>
	</p>
	<p>
	<span class="facultatif"><i18n:message key="label.categorie" /> :</span> 
	    <salon:valeur valeurNulle="null" valeur="<%= aCollab.getCATEG() %>" >
		  <input type="text" name="CATEG" value="%%" size=3>
	    </salon:valeur>
	<span class="facultatif"><i18n:message key="label.echelon" /> :</span> 
	    <salon:valeur valeurNulle="null" valeur="<%= aCollab.getECHELON() %>" >
		  <input type="text" name="ECHELON" value="%%" size=3>
	    </salon:valeur>
	<span class="facultatif"><i18n:message key="label.coef" /> :</span> 
	    <salon:valeur valeurNulle="null" valeur="<%= aCollab.getCOEF() %>" >
		  <input type="text" name="COEF" value="%%" size=3>
	    </salon:valeur>
	<span class="facultatif"><i18n:message key="label.heureParJour" /> :</span> 
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
      alert ("<i18n:message key="ficCollab.nomPrenomManquant" />");
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
        if (confirm ("<i18n:message key="message.suppressionDefinitiveConfirm" />")) {
            document.fiche.Action.value = "Suppression";
            document.fiche.submit();
        }
    }
}

// Affichage de l'aide
function Aide()
{
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideFiche.html");
}

function RetourListe()
{
    parent.location.href = "ListeCollab.jsp";
}

</script>
</body>
</html>
