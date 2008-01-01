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
<%@ page import="java.util.Vector" %>
<%@ page import="com.increg.salon.bean.SalonSession,
	       com.increg.salon.bean.ClientBean,
	       com.increg.salon.bean.HistoPrestBean,
	       com.increg.salon.bean.CollabBean,
	       com.increg.salon.bean.PrestBean" %>
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
<title><i18n:message key="ficCli.title" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees" onLoad="document.fiche.NOM.focus()">
<%@ include file="include/commun.jsp" %>
<script language="JavaScript">
<!--
<%
   // Récupération des paramètres
   String Action = (String) request.getAttribute("Action");
   String NbPrest = (String) request.getAttribute("NbPrest");
   ClientBean aCli = (ClientBean) request.getAttribute("ClientBean");
   String CD_TYP_VENT = request.getParameter("CD_TYP_VENT");
   boolean peutCreerFacture = mySalon.peutCreerFacture();   
%>
   var Action="<%= Action %>";

function Init() {
<%
   // Positionne les liens d'actions
   if (! Action.equals("Creation")) { %>
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
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/ficCli.gif" alt=<salon:TimeStamp bean="<%= aCli %>" />></h1>
<salon:message salonSession="<%= mySalon %>" />
<form method="post" action="ficCli.srv" name="fiche">
    <p> 
        <salon:valeur valeurNulle="0" valeur="<%= aCli.getCD_CLI() %>" >
            <input type="hidden" name="CD_CLI" value="%%" >
        </salon:valeur>
        <input type="hidden" name="Action" value="<%=Action%>">
        <input type="hidden" name="NbPrest" value="<%=NbPrest%>">
        <span class="facultatif"><i18n:message key="label.civilite" /> :</span> 
        <i18n:message key="valeur.civilite" id="valeurCivilite" />
        <salon:selection valeur="<%= aCli.getCIVILITE() %>" valeurs='<%= "|Mle|Mme|M. " %>' libelle='<%= valeurCivilite %>'>
            <select name="CIVILITE">
                %%
            </select>
        </salon:selection>
        <span class="obligatoire"><i18n:message key="label.nom" /> :</span> 
        <salon:valeur valeurNulle="null" valeur="<%= aCli.getNOM() %>" >
            <input class="readonly" type="text" name="NOM" value="%%" >
        </salon:valeur>
        <span class="facultatif"><i18n:message key="label.prenom" /> :</span> 
        <salon:valeur valeurNulle="null" valeur="<%= aCli.getPRENOM() %>" >
            <input class="readonly" type="text" name="PRENOM" value="%%" >
        </salon:valeur>
        <span class="facultatif"><i18n:message key="label.dtAnniversaire" /> :</span> 
        <salon:valeur valeurNulle="null" valeur="<%= aCli.getDT_ANNIV() %>" >
            <input type="text" name="DT_ANNIV" size="10" maxlength="10" value="%%" onchange="FormateDate(this)">
        </salon:valeur>
    </p>
    <p>
	<table border="0" cellspacing="0">
            <tr> 
                <td> 
                    <p><span class="facultatif"><i18n:message key="label.adresse" /> :</span> 
                        <salon:valeur valeurNulle="null" valeur="<%= aCli.getRUE() %>" >
                            <textarea name="RUE" rows="2" cols="40">%%</textarea>
                        </salon:valeur>
                    </p>
                    <p><span class="facultatif"><i18n:message key="label.cdPostal" /> :</span> 
                        <salon:valeur valeurNulle="null" valeur="<%= aCli.getCD_POSTAL() %>" >
                            <input type="text" name="CD_POSTAL" size="6" maxlength="5" value="%%">
                        </salon:valeur>
                        <span class="facultatif"><i18n:message key="label.ville" /> :</span> 
                        <salon:valeur valeurNulle="null" valeur="<%= aCli.getVILLE() %>" >
                            <input type="text" name="VILLE" value="%%">
                        </salon:valeur>
                    </p>
                </td>
                <td> 
                    <p align="right"><span class="facultatif"><i18n:message key="label.telephone" /> :</span></p>
                    <p align="right"><span class="facultatif"><i18n:message key="label.email" /> :</span></p>
                </td>
                <td>
                    <p style="vertical-align:middle;">
                        <salon:valeur valeurNulle="null" valeur="<%= aCli.getTEL() %>" >
                            <input type="text" name="TEL" value="%%" size=14>
                        </salon:valeur>
                        <span class="facultatif"><i18n:message key="label.portable" />&nbsp;:&nbsp;</span>
                        <salon:valeur valeurNulle="null" valeur="<%= aCli.getPORTABLE() %>" >
                            <input type="text" name="PORTABLE" value="%%" size=14>
                        </salon:valeur>
                    </p>
                    <p>
                        <salon:valeur valeurNulle="null" valeur="<%= aCli.getEMAIL() %>" >
                            <input type="text" name="EMAIL" value="%%" size=30> <a href="mailto:%%"><img src="images/email.gif" border="0" align="absmiddle"></a>
                        </salon:valeur>
                    </p>
                </td>
            </tr>
	</table>
	</p>
	<p>
            <table border="0" cellspacing="2">
                <tr>
                    <td>
					<% if (mySalon.getMySociete().isSalon()) { %>
                        <span class="facultatif"><i18n:message key="label.typeCheveux" /> :</span> <br>
					<% } %>
                        <span class="facultatif"><i18n:message key="label.trancheAge" /> :</span> 
                    </td>
                    <td>
					<% if (mySalon.getMySociete().isSalon()) { %>
                        <salon:DBselection valeur="<%= aCli.getCD_TYP_CHEV() %>" sql="select CD_TYP_CHEV, LIB_TYP_CHEV from TYP_CHEV order by LIB_TYP_CHEV">
                        <select name="CD_TYP_CHEV">
                            <option value=""></option>
                            %%
                        </select><br>
                        </salon:DBselection>
					<% } %>
                        <salon:DBselection valeur="<%= aCli.getCD_TR_AGE() %>" sql="select CD_TR_AGE, LIB_TR_AGE from TR_AGE order by AGE_MIN">
                        <select name="CD_TR_AGE">
                            <option value=""></option>
                            %%
                        </select>
                        </salon:DBselection>
                    </td>
                    <td>
			    	<% if (mySalon.getMySociete().isInstitut()) { %>
                        <span class="facultatif"><i18n:message key="label.typePeau" /> :</span> <br>
					<% } %>
                        <span class="facultatif"><i18n:message key="label.origine" /> :</span> 
                    </td>
                    <td>
			    	<% if (mySalon.getMySociete().isInstitut()) { %>
                        <salon:DBselection valeur="<%= aCli.getCD_TYP_PEAU() %>" sql="select CD_TYP_PEAU, LIB_TYP_PEAU from TYP_PEAU order by LIB_TYP_PEAU">
                        <select name="CD_TYP_PEAU">
                            <option value=""></option>
                            %%
                        </select><br>
                        </salon:DBselection>
					<% } %>
                        <salon:DBselection valeur="<%= aCli.getCD_ORIG() %>" sql="select CD_ORIG, LIB_ORIG from ORIG order by LIB_ORIG">
                        <select name="CD_ORIG">
                            <option value=""></option>
                            %%
                        </select>
                        </salon:DBselection>
                    </td>
                    <td>
                        <span class="facultatif"><i18n:message key="label.categorieClient" /> :</span> <br>
                        <span class="obligatoire"><i18n:message key="label.clientActuel" /> :</span> 
                    </td>
                    <td>
                        <salon:DBselection valeur="<%= aCli.getCD_CATEG_CLI() %>" sql="select CD_CATEG_CLI, LIB_CATEG_CLI from CATEG_CLI order by LIB_CATEG_CLI">
                        <select name="CD_CATEG_CLI">
                            <option value=""></option>
                            %%
                        </select><br>
                        </salon:DBselection> 
                    	<i18n:message key="valeur.ouiNon" id="valeurOuiNon" />
                        <salon:selection valeur="<%= aCli.getINDIC_VALID() %>" valeurs='<%= "O|N" %>' libelle="<%= valeurOuiNon %>">
                            <select name="INDIC_VALID">
                                %%
                            </select>
                        </salon:selection>
                    </td>
                </tr>
            </table>
	</p>
	<table border="0" cellspacing="0">
            <tr>
                <td>
                    <span class="facultatif"><i18n:message key="label.commentaire" /> :</span> 
                    <salon:valeur valeurNulle="null" valeur="<%= aCli.getCOMM() %>" >
                        <textarea name="COMM" cols="40" rows="2">%%</textarea>
                    </salon:valeur>
                </td>
            </tr>
	</table>
        <hr>
<%
    if (Action != "Creation") { %>
        <table border="0" cellspacing="0" width="100%">
            <tr>
                <td>
                    <i18n:message key="ficCli.dernieresPrestations">
                    	<i18n:messageArg value="<%= NbPrest %>" />
                    </i18n:message> :
                    &nbsp;&nbsp;<a href="javascript:toutesPrest()"><i18n:message key="ficCli.toutesPrestations" /></a> 
                </td>
   				<td align="right">
                    <span class="souslien">
                    	<i18n:message key="ficCli.doublon" id="paramBouton1a" />
                    	<salon:bouton url="<%= \"ListeCli.jsp?Action=Doublon&CD_CLI=\" + aCli.getCD_CLI() %>" 
                                    alt="<%= paramBouton1a %>" 
                                    target="ClientFrame" 
                                    img="<%= \"images/recherche.gif\" %>" />&nbsp;&nbsp;
                    	<i18n:message key="ficCli.nouveauRDV" id="paramBouton1" />
                    	<salon:bouton url="<%= \"_FicheRDV.jsp?Action=Creation&CD_CLI=\" + aCli.getCD_CLI() %>" 
                                    alt="<%= paramBouton1 %>" 
                                    target="ClientFrame" 
                                    img="<%= \"images/\" + mySalon.getLangue().getLanguage() + \"/priseRDV.gif\" %>" />&nbsp;&nbsp;
                    	<i18n:message key="ficCli.fichesTechs" id="paramBouton2" />
                    	<salon:bouton url="<%= \"_FicheTech.jsp?CD_CLI=\" + aCli.getCD_CLI() %>" 
                                    alt="<%= paramBouton2 %>" 
                                    target="ClientFrame" 
                                    img="<%= \"images/\" + mySalon.getLangue().getLanguage() + \"/imprFicTech.gif\" %>" />&nbsp;&nbsp; 
                    	<i18n:message key="ficCli.abonnements" id="paramBouton3" />
                        <salon:bouton url="<%= \"ListeAbonnement.jsp?CD_CLI=\" + aCli.getCD_CLI() %>" 
                                    alt="<%= paramBouton3 %>" 
                                    target="ClientFrame" 
                                    img="<%= \"images/\" + mySalon.getLangue().getLanguage() + \"/Abonnement.gif\" %>" />&nbsp;&nbsp; 
                        <%
                        if (peutCreerFacture) { %>
                    		<i18n:message key="ficCli.nouvelleFacture" id="paramBouton4" />
                                <salon:bouton url="<%= \"_FicheFact.jsp?FACT_HISTO=N&CD_CLI=\" + aCli.getCD_CLI() %>" 
                                            alt="<%= paramBouton4 %>" 
                                            target="ClientFrame" 
                                            img="<%= \"images/\" + mySalon.getLangue().getLanguage() + \"/accueilClient.gif\" %>" 
                                            imgOn="<%= \"images/\" + mySalon.getLangue().getLanguage() + \"/accueilClient2.gif\" %>"/>&nbsp;&nbsp;
                        <%
                        } %>
                        <i18n:message key="ficCli.nouvelHisto" id="paramBouton5" />
                        <salon:bouton url="<%= \"_FicheFact.jsp?FACT_HISTO=O&CD_CLI=\" + aCli.getCD_CLI() %>" 
                                    alt="<%= paramBouton5 %>" 
                                    target="ClientFrame" 
                                    img="<%= \"images/\" + mySalon.getLangue().getLanguage() + \"/nouvHisto.gif\" %>" />
                    </span>
                </td>
            </tr>
        </table>
        <table border="0" width="100%">
            <tr>
                <th>
<%		       
                if (peutCreerFacture) { %>
					<a href="addCli.srv?Vide=1&CD_CLI=<%=aCli.getCD_CLI()%>" title="<i18n:message key="label.accueilClient" />" target=MenuFrame><img src=images/plus2.gif border="0" width="15" height="15"></a>
<%
                }
                else { %>
                    <img src=images/plusNon.gif border="0" width="15" height="15" alt="<i18n:message key="erreur.pasCollaborateurPresent" />"/>
<%
                }
%>
                </th>
                <th><i18n:message key="label.date" /></th>
                <th><i18n:message key="label.prestation" /> : 
                    <salon:DBselection valeur="<%= CD_TYP_VENT %>" sql="select CD_TYP_VENT, LIB_TYP_VENT from TYP_VENT order by LIB_TYP_VENT">
                        <select name="CD_TYP_VENT" onChange="toutesPrest()">
                            <option value=""><i18n:message key="label.toutes" /></option>
                            %%
                        </select>
                    </salon:DBselection>
                </th>
                <th><i18n:message key="label.collaborateur" /></th>
                <th><i18n:message key="label.commentaire" /></th>
            </tr>
<%
        // Recupère la liste
        Vector listePrest = (Vector) request.getAttribute("listePrest");
        long lastCD_FACT = -1;
        String finClass = "3";
        boolean changeFacture = false;	
        for (int i=0; i< listePrest.size(); i++) {
            changeFacture = false;
            HistoPrestBean aPrest = (HistoPrestBean) listePrest.get(i);
            if (lastCD_FACT != aPrest.getCD_FACT()) {
                // Changement de Facture : Change de class
                finClass = (finClass.equals("3")) ? "2" : "3";
                changeFacture = true;
            }
            lastCD_FACT = aPrest.getCD_FACT();
%>
            <tr class="ligneTab<%= finClass%>">
                <td class="tabDonnees">
<%
            if (changeFacture == true) {
                // On autorise la duplication de la facture uniquement si on a un collab
		       
                if (peutCreerFacture) { %>
                    <a href="addCli.srv?CD_CLI=<%=aCli.getCD_CLI()%>&CD_FACT=<%= aPrest.getCD_FACT()%>" title="<i18n:message key="label.dupliquerFacture" />" target="MenuFrame"><img src="images/plus.gif" border="0" width="15" height="15"></a>		      
<%
                }
                else { %>
                    <img src="images/plusNon.gif" border="0" width="15" height="15" alt="<i18n:message key="erreur.pasCollaborateurPresent" />"/>
<%
                }
            }
            else {
	           // On est toujours dans la même facture, on n'affiche rien.
            }
%>
                </td>
                <td class="tabDonnees">
                    <salon:valeur valeurNulle="null" valeur="<%= aPrest.getDT_PREST() %>" > 
                        <a href="_FicheFact.jsp?Action=Modification&CD_FACT=<%= aPrest.getCD_FACT() %>" target="ClientFrame">%%</a> 
                    </salon:valeur>
                </td>
                <td class="tabDonnees">
                    <salon:valeur valeurNulle="null" valeur="<%= PrestBean.getPrestBean(mySalon.getMyDBSession(), Long.toString(aPrest.getCD_PREST())).toString() %>" >
                        %%
                    </salon:valeur>
                </td>
                <td class="tabDonnees">
                    <salon:valeur valeur="<%= CollabBean.getCollabBean(mySalon.getMyDBSession(), Integer.toString(aPrest.getCD_COLLAB())).toString() %>" valeurNulle="null">
			%%
                    </salon:valeur>
                </td>
                <td>
                    <salon:valeur valeurNulle="null" valeur="<%= aPrest.getCOMM() %>" > 
                        <input type="hidden" name="COMM<%= i %>" value="%%">
                    </salon:valeur>
                    <salon:valeur expand="true" valeurNulle="null" valeur="<%= aPrest.getCOMM() %>" >
                        <a href="javascript:SaisieCommentaire(<%= i %>, <%= aPrest.getCD_FACT() %>, <%= aPrest.getNUM_LIG_FACT() %>)">%%
<%
            if (((Integer) request.getAttribute("Longueur")).intValue() == 0) { %>
                        <img src="images/vide.gif" border="0" width="11" height="13" alt="<i18n:message key="label.commentaire" />">
<%
            } %>
                        </a>
                    </salon:valeur>
                </td>
            </tr>
<%
        } %>
        </table>
<% } %> 
    </form>

    <span id="COMMENTAIRE" class="action" style="position:absolute; z-index:1; left: 15px; visibility: hidden">
        <form name="fComm" method="post" action="ficCli.srv">
            <table>
                <tr>
                    <td valign="top">
                        <span class="facultatif"><i18n:message key="label.commentaire" /> :</span>
                    </td>
                    <td>
                        <textarea name="COMM" cols="50" rows="4" align="middle">
                        </textarea>
                        <input type="hidden" name="CD_FACT">
                        <input type="hidden" name="NUM_LIG_FACT">
                        <input type="hidden" name="Action" value="<i18n:message key="label.commentaire" />">
                    </td>
                    <td valign="top">
                        <a href="javascript:FinSaisieCommentaire()"><i18n:message key="label.validerCommentaire" /></a>
                    </td>
                </tr>
            </table>
        </form>
    </span>

<script language="JavaScript">
// Fonctions d'action

// Affichage / Saisie du commentaire
function SaisieCommentaire (NumPrest, CD_FACT, NUM_LIG_FACT)
{
   MM_showHideLayers('COMMENTAIRE','','show');
   document.fComm.COMM.value = document.fiche.elements["COMM" + NumPrest].value;
   document.fComm.CD_FACT.value = CD_FACT;
   document.fComm.NUM_LIG_FACT.value = NUM_LIG_FACT;
   document.fComm.COMM.focus();
   return;
}

function FinSaisieCommentaire()
{
   document.fComm.submit()
}

// Affiche toutes les prestations
function toutesPrest()
{
   document.fiche.Action.value = "Complet";
   document.fiche.submit();

}

// Enregistrement des données du client
function Enregistrer()
{
   // Verification des données obligatoires
   if (document.fiche.NOM.value == "") {
      alert ("<i18n:message key="ficCli.nomClientManquant" />");
      return;
   }
   document.fiche.submit();
}

// Création d'un nouveau client
function Nouveau()
{
   parent.location.href = "_FicheCli.jsp";
}

// Suppression du client
function Supprimer()
{
    if ((document.fiche.CD_CLI.value != "0") && (document.fiche.CD_CLI.value != "")) {
        if (confirm ("<i18n:message key="message.suppressionDefinitiveConfirm" />")) {
            document.fiche.Action.value = "Suppression";
            document.fiche.submit();
        }
    }
}

function RetourListe()
{
   if (document.fiche.NOM.value != "") {
      parent.location.href = "ListeCli.jsp?premLettre=" + document.fiche.NOM.value.charAt(0).toUpperCase();
   }
   else {
      parent.location.href = "ListeCli.jsp?premLettre=A";
   }
}

// Affichage de l'aide
function Aide()
{
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideFicheCli.html");
}

</script>
</body>
</html>
