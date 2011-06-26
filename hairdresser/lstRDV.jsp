<%
/*
 * This program is part of InCrEG LibertyLook software http://beauty-hair-mng.sourceforge.net
 * Copyright (C) 2001-2011 Emmanuel Guyot <See emmguyot on SourceForge> 
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
<%@ page import="com.increg.salon.bean.SalonSession,java.util.Vector,java.net.URLEncoder,java.util.Calendar,
	       com.increg.salon.request.RDVFact,
	       com.increg.salon.bean.FactBean,
	       com.increg.salon.bean.HistoPrestBean,
	       com.increg.salon.bean.PrestBean,
	       com.increg.salon.bean.RDVBean" %>
<%
SalonSession mySalon = com.increg.salon.servlet.ConnectedServlet.CheckOrGoHome(request, response);
%>
<%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %>
<%@ taglib uri="WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<i18n:bundle baseName="messages" locale="<%= mySalon.getLangue() %>"/>
<html>
<head>
<title><i18n:message key="title.lstRDV" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees">
<%@ include file="include/commun.jsp" %>
<%
   // Récupération des paramètres
   String CD_COLLAB = request.getParameter("CD_COLLAB");
   Calendar DT_DEBUT = (Calendar) request.getAttribute("DT_DEBUT");
   Calendar DT_FIN = (Calendar) request.getAttribute("DT_FIN");
   boolean peutCreerFacture = mySalon.peutCreerFacture();   
%>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/ficRDV.gif"></h1>
<form name="fiche" action="rechRDV.srv" method="post">
<p>
<i18n:message key="label.collaborateur" /> :
<salon:DBselection valeur="<%= CD_COLLAB %>" sql="select CD_COLLAB, PRENOM from COLLAB order by PRENOM, NOM">
   <select name="CD_COLLAB" onChange="document.fiche.submit()">
      <option value=""><i18n:message key="label.tousDsListe" /></option>
      %%
   </select>
</salon:DBselection>
</p>
<p>
<i18n:message key="label.entreLe" /> :
<i18n:message key="format.dateHeureSimpleSansSeconde" id="formatDate" />
    <salon:date type="text" name="DT_DEBUT" valeurDate="<%= DT_DEBUT %>" valeurNulle="null" format="<%= formatDate %>" calendrier="true" onchange="document.fiche.submit()">%%</salon:date>
<i18n:message key="label.etLe" /> : 
    <salon:date type="text" name="DT_FIN" valeurDate="<%= DT_FIN %>" valeurNulle="null" format="<%= formatDate %>" calendrier="true" onchange="document.fiche.submit()">%%</salon:date>
    <input type="hidden" name="Action" value="refresh">
</p>
</form>
<hr>
<table width="100%" border="1" >
	<tr>
		<th></th>
		<th><i18n:message key="label.debut" /></th>
                <th><i18n:message key="label.client" /></th>
		<th><i18n:message key="label.collaborateur" /></th>
		<th><i18n:message key="label.commentairePrestation" /></th>
	</tr>
        <i18n:message key="label.modifierRDV" id="modifRDV" />
        <i18n:message key="label.voirFacture" id="voirFacture" />
        <i18n:message key="label.dupliquerFacture" id="dupliquerFact" />
        <i18n:message key="label.accueilClient" id="accueil" />
        <i18n:message key="erreur.pasCollaborateurPresent" id="pasCollab" />
        <i18n:message key="label.imprimerFicheTech" id="imprimerFiche" />
        <i18n:message key="format.dateHeureSansAnnee" id="formatDateTableau" />
	<%
	// Recupère la liste
	Vector lstLignes = (Vector) request.getAttribute("Liste");
	    
	for (int i=0; i< lstLignes.size(); i++) {
	    RDVFact aRDVFact = (RDVFact) lstLignes.get(i);
	    RDVBean aRDV = aRDVFact.getRdv();
	    FactBean aFact = aRDVFact.getFact();
            %>
            <tr>
                <td class="label" width="80">
                    <%
                    if (aRDV != null) {
                    %>
                        <salon:valeur valeurNulle="null" valeur="<%= aRDV.getDT_DEBUT() %>" url="true">
                        <a href="_FicheRDV.jsp?Action=Modification&CD_CLI=<%= aRDV.getCD_CLI() %>&DT_DEBUT=%%" target="ClientFrame" title="<%= modifRDV %>"><img src="images/boutonRDV.gif" border="0"></a>
                        </salon:valeur>
                    <%
                    }
                    if (aFact != null) {
                    %>
                        <a href="_FicheFact.jsp?Action=Modification&CD_FACT=<%= aFact.getCD_FACT() %>" target="ClientFrame" title="<%= voirFacture %>"><img src="images/fact.gif" border="0"></a>
                    <%
                    }
                    else {
                        if (peutCreerFacture) { %>
                            <a href="addCli.srv?CD_CLI=<%= aRDVFact.getClient().getCD_CLI() %>" title="<%= dupliquerFact %>" target=MenuFrame><img src=images/plus.gif border="0" width="15" height="15"></a>
                            <a href="addCli.srv?Vide=1&CD_CLI=<%= aRDVFact.getClient().getCD_CLI() %>" title="<%= accueil %>" target=MenuFrame><img src=images/plus2.gif border="0" width="15" height="15"></a>
                    <%
                        } 
                        else { %>
                            <img src=images/plusNon.gif border="0" width="15" height="15" alt="<%= pasCollab %>"/>
                            <img src=images/plus2Non.gif border="0" width="15" height="15" alt="<%= pasCollab %>"/>
                    <%
                        }
                    %>
                        <a href="ficTech.srv?Action=Impression&CD_CLI=<%= aRDVFact.getClient().getCD_CLI() %>" title="<%= imprimerFiche %>" target="_blank"><img src=images/Tech.gif border="0" width="15" height="15"></a>
                    <%
                    } %>
                </td>
                <td class="tabDonneesGauche">
                    <salon:valeur valeur="<%= aRDVFact.getDate() %>" valeurNulle="null" format="<%= formatDateTableau %>"> %% </salon:valeur>
                    <%
                    if (aRDV != null) {
                    %>
                        <i18n:message key="label.valeurDuree"> 
                            <i18n:messageArg value="<%= Integer.toString(aRDV.getDUREE()) %>" />
                        </i18n:message>
                    <%
                    }
                    %>
                </td>
                <td class="tabDonneesGauche">
                    <%= aRDVFact.getClient().toStringListe() %>
                </td>
                <td class="tabDonneesGauche"><%= aRDVFact.getCollab().toString() %></td>
                <td>
                    <%
                    if (aFact != null) {
                        Vector lignes = aFact.getLignes();
                        for (int iPrest = 0; iPrest < lignes.size(); iPrest++) {
                            HistoPrestBean aPrest = (HistoPrestBean) lignes.get(iPrest);
                            if (iPrest > 0) {
                    %>
                                <br/>
                        <%
                            }
                        %>
                            <salon:valeur valeurNulle="null" valeur="<%= PrestBean.getPrestBean(mySalon.getMyDBSession(), Long.toString(aPrest.getCD_PREST())).toString() %>" >
                                %%
                            </salon:valeur>
                    <%
                        }
                    }
                    if ((aRDV != null) && (aFact == null)) {
                        // Le commentaire n'est affiché que si pas de facture
                    %>
                        <salon:valeur valeurNulle="null" valeur="<%= aRDV.getCOMM() %>" expand="true"> %% </salon:valeur>
                    <%
                    }
                    %>&nbsp;
                </td>
            </tr>
        <%
	}
	%>
</table>
<hr/>
<%
    if (peutCreerFacture) { %>
        <p>
        <a href="javascript:dupliquerFacture()" title="<i18n:message key="label.dupliquerFactures" />"><img src=images/plus.gif border="0" width="15" height="15"> <i18n:message key="label.dupliquerFacturesRDV" /></a><br/>
        <a href="javascript:accueillirClient()" title="<i18n:message key="label.accueilClients" />"><img src=images/plus2.gif border="0" width="15" height="15"> <i18n:message key="label.accueilClientsRDV" /></a>
        </p>
<%
    } 
%>
<salon:madeBy />
<script language="JavaScript">
// Au chargement de la page : Recharge le menu pour MAJ de la liste des encours
top.MenuFrame.location.href = top.MenuFrame.location.href;

function Nouveau()
{
    // La création se fait à partir de la liste des clients
    parent.location.href = "ListeCli.jsp";
}

// Affichage de l'aide
function Aide()
{
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideListe.html");
}

function dupliquerFacture() {
    document.fiche.Action.value = "Duplication";
    document.fiche.submit();
}

function accueillirClient() {
    document.fiche.Action.value = "Accueil";
    document.fiche.submit();
}

</script>
</body>
</html>
