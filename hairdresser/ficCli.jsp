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
<html>
<head>
<title>Fiche client</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees" onLoad="document.fiche.NOM.focus()">
<%@ include file="include/commun.js" %>
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
<h1><img src="images/titres/ficCli.gif" alt=<salon:TimeStamp bean="<%= aCli %>" />></h1>
<salon:message salonSession="<%= mySalon %>" />
<form method="post" action="ficCli.srv" name="fiche">
    <p> 
        <salon:valeur valeurNulle="0" valeur="<%= aCli.getCD_CLI() %>" >
            <input type="hidden" name="CD_CLI" value="%%" >
        </salon:valeur>
        <input type="hidden" name="Action" value="<%=Action%>">
        <input type="hidden" name="NbPrest" value="<%=NbPrest%>">
        <span class="facultatif">Civilit&eacute; :</span> 
        <salon:selection valeur="<%= aCli.getCIVILITE() %>" valeurs='<%= "|Mle|Mme|M. " %>'>
            <select name="CIVILITE">
                %%
            </select>
        </salon:selection>
        <span class="obligatoire">Nom :</span> 
        <salon:valeur valeurNulle="null" valeur="<%= aCli.getNOM() %>" >
            <input class="readonly" type="text" name="NOM" value="%%" >
        </salon:valeur>
        <span class="facultatif">Pr&eacute;nom :</span> 
        <salon:valeur valeurNulle="null" valeur="<%= aCli.getPRENOM() %>" >
            <input class="readonly" type="text" name="PRENOM" value="%%" >
        </salon:valeur>
        <span class="facultatif">Date anniversaire :</span> 
        <salon:valeur valeurNulle="null" valeur="<%= aCli.getDT_ANNIV() %>" >
            <input type="text" name="DT_ANNIV" size="10" maxlength="10" value="%%" onchange="FormateDate(this)">
        </salon:valeur>
    </p>
    <p>
	<table border="0" cellspacing="0">
            <tr> 
                <td> 
                    <p><span class="facultatif">Adresse :</span> 
                        <salon:valeur valeurNulle="null" valeur="<%= aCli.getRUE() %>" >
                            <textarea name="RUE" rows="2" cols="40">%%</textarea>
                        </salon:valeur>
                    </p>
                    <p><span class="facultatif">Code postal :</span> 
                        <salon:valeur valeurNulle="null" valeur="<%= aCli.getCD_POSTAL() %>" >
                            <input type="text" name="CD_POSTAL" size="6" maxlength="5" value="%%">
                        </salon:valeur>
                        <span class="facultatif">Ville :</span> 
                        <salon:valeur valeurNulle="null" valeur="<%= aCli.getVILLE() %>" >
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
                        <salon:valeur valeurNulle="null" valeur="<%= aCli.getTEL() %>" >
                            <input type="text" name="TEL" value="%%" size=14>
                        </salon:valeur>
                        <span class="facultatif">Portable&nbsp;:&nbsp;</span>
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
                        <span class="facultatif">Type de cheveux :</span> <br>
					<% } %>
                        <span class="facultatif">Tranche d'&acirc;ge :</span> 
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
                        <span class="facultatif">Type de peau :</span> <br>
					<% } %>
                        <span class="facultatif">Origine :</span> 
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
                        <span class="facultatif">Cat&eacute;gorie client :</span> <br>
                        <span class="obligatoire">Client actuel :</span> 
                    </td>
                    <td>
                        <salon:DBselection valeur="<%= aCli.getCD_CATEG_CLI() %>" sql="select CD_CATEG_CLI, LIB_CATEG_CLI from CATEG_CLI order by LIB_CATEG_CLI">
                        <select name="CD_CATEG_CLI">
                            <option value=""></option>
                            %%
                        </select><br>
                        </salon:DBselection> 
                        <salon:selection valeur="<%= aCli.getINDIC_VALID() %>" valeurs='<%= "O|N" %>' libelle="Oui|Non">
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
                    <span class="facultatif">Commentaire :</span> 
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
                    <%=NbPrest%> derni&egrave;res prestations :
                    &nbsp;&nbsp;<a href="javascript:toutesPrest()">Toutes les prestations</a> 
                </td>
   				<td align="right">
                    <span class="souslien">
                        <a href="_FicheRDV.jsp?Action=Creation&CD_CLI=<%= aCli.getCD_CLI() %>" title="Nouveau rendez-vous" target="ClientFrame"><img src="images/priseRDV.gif" border="0"></a>&nbsp;&nbsp;
                        <a href="_FicheTech.jsp?CD_CLI=<%= aCli.getCD_CLI() %>" target="ClientFrame" title="Fiches techniques"><img src="images/imprFicTech.gif" border="0"></a>&nbsp;&nbsp; 
                        <a href="ListeAbonnement.jsp?CD_CLI=<%= aCli.getCD_CLI() %>" target="ClientFrame" title="Abonnements"><img src="images/Abonnement.gif" border="0"></a>&nbsp;&nbsp; 
                        <%
                        if (peutCreerFacture) { %>
                            <a href="_FicheFact.jsp?FACT_HISTO=N&CD_CLI=<%= aCli.getCD_CLI() %>" target="ClientFrame" title="Nouvelle facture" ><img src="images/accueilClient.gif" border="0"></a>&nbsp;&nbsp;
                        <%
                        } %>
                        <a href="_FicheFact.jsp?FACT_HISTO=O&CD_CLI=<%= aCli.getCD_CLI() %>" target="ClientFrame" title="Nouvel historique"><img src=images/nouvHisto.gif border="0"></a>
                    </span>
                </td>
            </tr>
        </table>
        <table border="0" width="100%">
            <tr>
                <th><a href="addCli.srv?Vide=1&CD_CLI=<%=aCli.getCD_CLI()%>" title="Accueillir ce client" target=MenuFrame><img src=images/plus2.gif border="0" width="15" height="15"></a></th>
                <th>Date</th>
                <th>Prestation : 
                    <salon:DBselection valeur="<%= CD_TYP_VENT %>" sql="select CD_TYP_VENT, LIB_TYP_VENT from TYP_VENT order by LIB_TYP_VENT">
                        <select name="CD_TYP_VENT" onChange="toutesPrest()">
                            <option value="">( Toutes )</option>
                            %%
                        </select>
                    </salon:DBselection>
                </th>
                <th>Collaborateur</th>
                <th>Commentaire</th>
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
                    <a href="addCli.srv?CD_CLI=<%=aCli.getCD_CLI()%>&CD_FACT=<%= aPrest.getCD_FACT()%>" title="Dupliquer sa facture" target=MenuFrame><img src=images/plus.gif border="0" width="15" height="15"></a>		      
<%
                }
                else { %>
                    <img src=images/plusNon.gif border="0" width="15" height="15" alt="Action impossible : Pas de collaborateur présent"/>
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
                        <img src="images/vide.gif" border="0" width="11" height="13" alt="Commentaire">
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

    <span id="COMMENTAIRE" class="action" style="position:absolute; height:23px; z-index:1; left: 15px; visibility: hidden">
        <form name="fComm" method="post" action="ficCli.srv">
            <table>
                <tr>
                    <td valign="top">
                        <span class="facultatif">Commentaire :</span>
                    </td>
                    <td>
                        <textarea name="COMM" cols="50" rows="4" align="middle">
                        </textarea>
                        <input type="hidden" name="CD_FACT">
                        <input type="hidden" name="NUM_LIG_FACT">
                        <input type="hidden" name="Action" value="Commentaire">
                    </td>
                    <td valign="top">
                        <a href="javascript:FinSaisieCommentaire()">Valider ce<br>commentaire</a>
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
      alert ("Le nom du client doit être saisi. L'enregistrement n'a pas pu avoir lieu.");
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
        if (confirm ("Cette suppression est définitive. Confirmez-vous cette action ?")) {
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
    window.open("aideFicheCli.html");
}

</script>
</body>
</html>
