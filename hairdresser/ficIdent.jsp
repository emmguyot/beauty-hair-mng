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
	       com.increg.salon.bean.IdentBean
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
<title><i18n:message key="ficIdent.title" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees" onLoad="Init();document.fiche.LIB_IDENT.focus()">
<%@ include file="include/commun.jsp" %>
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
   MM_showHideLayers('RETOUR_LISTE?bottomFrame','','show');
}
//-->
</script>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/ficIdent.gif" alt=<salon:TimeStamp bean="<%= aIdent %>" />></h1>
<salon:message salonSession="<%= mySalon %>" />
<form method="post" action="ficIdent.srv" name="fiche">
	 <p> 
		<salon:valeur valeurNulle="0" valeur="<%= aIdent.getCD_IDENT() %>" >
		  <input type="hidden" name="CD_IDENT" value="%%" >
	        </salon:valeur>
		<input type="hidden" name="Action" value="<%=Action%>">
		<span class="obligatoire"><i18n:message key="label.libelle" /> :</span> 
		<salon:valeur valeurNulle="null" valeur="<%= aIdent.getLIB_IDENT() %>" >
		  <input type="text" name="LIB_IDENT" value="%%" size="30">
	        </salon:valeur>
	 </p>
	 <p>
		<span class="obligatoire"><i18n:message key="label.motPasse" /> :</span> 
		<salon:valeur valeurNulle="null" valeur="<%= aIdent.getMOT_PASSE() %>" >
		  <input type="password" name="MOT_PASSE" value="%%" >
                  <span class="obligatoire"><i18n:message key="label.motPasseVerif" /> :</span> 
		  <input type="password" name="MOT_PASSE2" value="%%" >
	        </salon:valeur>
        </p>
        <p>
	        <span class="obligatoire"><i18n:message key="label.profil" /> :</span> 
	        <salon:DBselection valeur="<%= aIdent.getCD_PROFIL() %>" sql='<%= "select CD_PROFIL, LIB_PROFIL from PROFIL order by LIB_PROFIL" %>'>
		  <select name="CD_PROFIL">
		     %%
		  </select>
		</salon:DBselection>
	        <span class="obligatoire"><i18n:message key="label.etatCompte" /> :</span> 
                <i18n:message key="valeur.etatCompte" id="valeurEtat" />
		<salon:selection valeur="<%= aIdent.getETAT_CPT() %>" valeurs='<%= "A|B" %>' libelle="<%= valeurEtat %>">
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
      alert ("<i18n:message key="ficIdent.libelleMotPasseManquant" />");
      return;
   }
   document.fiche.submit();
}

// Suppression
function Supprimer()
{
    if ((document.fiche.CD_IDENT.value != "0") && (document.fiche.CD_IDENT.value != "")) {
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
    parent.location.href = "ListeIdent.jsp";
}

</script>
</body>
</html>
