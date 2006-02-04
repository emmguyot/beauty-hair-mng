<%
/*
 * This program is part of InCrEG LibertyLook software http://beauty-hair-mng.sourceforge.net
 * Copyright (C) 2001-2006 Emmanuel Guyot <See emmguyot on SourceForge> 
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
<%@ page import="java.util.Vector, java.util.List" %>
<%@ page import="com.increg.salon.bean.SalonSession,
	       com.increg.salon.bean.ClientBean,
	       com.increg.salon.bean.RDVBean
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
<%
    // Récupération des paramètres
    String Action = (String) request.getAttribute("Action");
    RDVBean aRDV = (RDVBean) request.getAttribute("RDVBean");
    ClientBean aCli = ClientBean.getClientBean(mySalon.getMyDBSession(), Long.toString(aRDV.getCD_CLI()), mySalon.getMessagesBundle());
    Vector dispo = (Vector) request.getAttribute("Dispo");
    List collabs = (List) request.getAttribute("collabs");
%>
<title><i18n:message key="ficRDV.title" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees" onLoad="Init()">
<%@ include file="include/commun.js" %>
<script language="JavaScript">
<!--
   var Action="<%=Action%>";

function Init() {
   <%
   // Positionne les liens d'actions '
   if (! Action.equals("Creation")) {
      %>
      MM_showHideLayers('SUPPRIMER?bottomFrame','','show');
      <%
   }
   if (Action.equals("Creation")) { %>
      // Pas de lien supprimer
      MM_showHideLayers('SUPPRIMER?bottomFrame','','hide');
   <%
   } %>
   MM_showHideLayers('ENREGISTRER?bottomFrame','','show');
   MM_showHideLayers('RETOUR_LISTE?bottomFrame','','show');
   MM_showHideLayers('NOUVEAU?bottomFrame','','show');
   document.fiche.CD_COLLAB.focus();
}
//-->
</script>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/ficRDV.gif" alt=<salon:TimeStamp bean="<%= aRDV %>" />></h1>
<salon:message salonSession="<%= mySalon %>" />

<form method="post" action="ficRDV.srv" name="fiche">
    <table><tr><td valign="top">
	 <p><span class="obligatoire"><i18n:message key="label.client" /> :</span> 
            <input type="hidden" name="CD_CLI" value="<%= aRDV.getCD_CLI() %>">
            <input type="hidden" name="Action" value="<%= Action %>">
            <salon:valeur valeurNulle="null" valeur="<%= aCli.toString() %>" >
                <span class="readonly"><a href="_FicheCli.jsp?Action=Modification&CD_CLI=<%= aCli.getCD_CLI() %>" target="ClientFrame">%%</a></span> 
            </salon:valeur>
            <span class="obligatoire"><i18n:message key="label.collaborateur" /> :</span> 
		    <salon:selection valeur="<%= aRDV.getCD_COLLAB() %>" valeurs="<%= collabs %>">
		       <select name="CD_COLLAB" onchange="RechargeDispo()">
				  %%
		       </select>
		    </salon:selection>
	 </p>
	 <p><span class="obligatoire"><i18n:message key="label.debut" /> :</span> 
            <i18n:message key="format.dateSimpleDefaut" id="paramDate" />
            <i18n:message key="format.heureSimpleDefaut" id="paramHeure" />
            <%
            if (Action.equals("Creation")) { %>
                <salon:date type="text" name="DT_DEBUT" valeurDate="<%= aRDV.getDT_DEBUT() %>" valeurNulle="null" format="<%= paramDate %>" calendrier="true" onchange="RechargeDispo()">%%</salon:date>
                <salon:date type="text" name="HR_DEBUT" valeurNulle="null" valeurDate="<%= aRDV.getDT_DEBUT() %>" format="<%= paramHeure %>">%%</salon:date>
            <%
            } 
            else { %>
                <salon:date type="readonly" name="DT_DEBUT" valeurDate="<%= aRDV.getDT_DEBUT() %>" valeurNulle="null" format="<%= paramDate %>">%%</salon:date>
                <salon:date type="readonly" name="HR_DEBUT" valeurNulle="null" valeurDate="<%= aRDV.getDT_DEBUT() %>" format="<%= paramHeure %>">%%</salon:date>
            <%
            } %>
	    <span class="obligatoire"><i18n:message key="label.duree" /> :</span> 
	       <salon:valeur valeurNulle="null" valeur="<%= aRDV.getDUREE() %>">
		  <input type="text" name="DUREE" size="3" value="%%">
	       </salon:valeur>
	 </p>
	<p>
	<span class="facultatif"><i18n:message key="label.commentaire" /> :</span> 
	    <salon:valeur valeurNulle="null" valeur="<%= aRDV.getCOMM() %>" >
		<textarea name="COMM" cols="40" rows="2">%%</textarea>
	    </salon:valeur>
	</p>
        <p><i18n:message key="label.commentaireClient" /> :
	    <salon:valeur valeurNulle="null" valeur="<%= aCli.getCOMM() %>" expand="true">
		<span class="readonly">%%</span>
	    </salon:valeur>
        </p>
    </td>
    <td valign="top" style="padding-left: 30px">
        <p><i18n:message key="label.disponibilite" /> :</p>
        <table>
        <%
            for (int i = 0; i < dispo.size(); i++) {
                RDVBean dispoRDV = (RDVBean) dispo.get(i);
        %>
                <tr><td>
                    <salon:date type="readonly" name="HR_DEBUT" valeurNulle="null" valeurDate="<%= dispoRDV.getDT_DEBUT() %>" format="<%= paramHeure %>">%%</salon:date>
                    ...
                    <salon:date type="readonly" name="HR_DEBUT" valeurNulle="null" valeurDate="<%= dispoRDV.getDT_FIN() %>" format="<%= paramHeure %>">%%</salon:date>
                
                </td></tr>
        <%
            }
        %>
        </table>
    </td></tr></table>
</form>


<script language="JavaScript">
// Fonctions d'action
lock = false;

// Enregistrement des données
function Enregistrer()
{
   // Verification des données obligatoires
   if ((document.fiche.DT_DEBUT.value == "") || (document.fiche.HR_DEBUT.value == "")) {
      alert ("<i18n:message key="ficRDV.debutManquant" />");
      return;
   }
   lock = true;
   document.fiche.submit();
}

// Suppression
function Supprimer()
{
    if (document.fiche.DT_DEBUT.value != "") {
        if (confirm ("Cette suppression est définitive. Confirmez-vous cette action ?")) {
            document.fiche.Action.value = "Suppression";
            document.fiche.submit();
        }
    }
}

function RetourListe() {
    if (document.fiche.DT_DEBUT.value != "") {
        parent.location.href = "ListeRDV.jsp?DT_DEBUT=" + doEscape(document.fiche.DT_DEBUT.value + " <i18n:message key="valeur.minuit" />") 
                + "&DT_FIN=" + doEscape(document.fiche.DT_DEBUT.value + " <i18n:message key="valeur.minuitMoins1" />");
    }
    else {
        parent.location.href = "ListeRDV.jsp";
    }
}

function RechargeDispo() {
    setTimeout("RechargeDispoReel()", 100);
}

function RechargeDispoReel() {
	if (!lock) {
		document.fiche.Action.value = "Rechargement";
		document.fiche.submit();
	}
}

// Affichage de l'aide
function Aide()
{
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideFiche.html");
}

function Nouveau()
{
    // La création se fait à partir de la liste des clients
    parent.location.href = "ListeCli.jsp";
}

</script>
</body>
</html>
