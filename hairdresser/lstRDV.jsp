<%@ page import="com.increg.salon.bean.SalonSession,java.util.Vector,java.net.URLEncoder,java.util.Calendar,
	       com.increg.salon.request.RDVFact,
	       com.increg.salon.bean.FactBean,
	       com.increg.salon.bean.HistoPrestBean,
	       com.increg.salon.bean.PrestBean,
	       com.increg.salon.bean.RDVBean" %>
<%
    SalonSession mySalon = (SalonSession) session.getAttribute("SalonSession");
    if (mySalon == null) {
        getServletConfig().getServletContext().getRequestDispatcher("/reconnect.html").forward(request, response);
    }
%>
<%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %>
<html>
<head>
<title>Liste des rendez-vous</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees">
<%@ include file="include/commun.js" %>
<%
   // Récupération des paramètres
   String CD_COLLAB = request.getParameter("CD_COLLAB");
   Calendar DT_DEBUT = (Calendar) request.getAttribute("DT_DEBUT");
   Calendar DT_FIN = (Calendar) request.getAttribute("DT_FIN");
   boolean peutCreerFacture = mySalon.peutCreerFacture();   
%>
<h1><img src="images/titres/ficRDV.gif"></h1>
<form name="fiche" action="rechRDV.srv" method="post">
<p>
Collaborateur :
<salon:DBselection valeur="<%= CD_COLLAB %>" sql="select CD_COLLAB, PRENOM from COLLAB order by PRENOM, NOM">
   <select name="CD_COLLAB" onChange="document.fiche.submit()">
      <option value="">( Tous )</option>
      %%
   </select>
</salon:DBselection>
</p>
<p>
Entre le :
    <salon:date type="text" name="DT_DEBUT" valeurDate="<%= DT_DEBUT %>" valeurNulle="null" format="dd/MM/yyyy HH:mm" calendrier="true" onchange="document.fiche.submit()">%%</salon:date>
   et le : 
    <salon:date type="text" name="DT_FIN" valeurDate="<%= DT_FIN %>" valeurNulle="null" format="dd/MM/yyyy HH:mm" calendrier="true" onchange="document.fiche.submit()">%%</salon:date>
    <input type="hidden" name="Action" value="refresh">
</p>
</form>
<hr>
<table width="100%" border="1" >
	<tr>
		<th></th>
		<th>Début</th>
                <th>Client</th>
		<th>Collaborateur</th>
		<th>Commentaire ou Prestations</th>
	</tr>
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
                        <a href="_FicheRDV.jsp?Action=Modification&CD_CLI=<%= aRDV.getCD_CLI() %>&DT_DEBUT=<%= URLEncoder.encode(SalonSession.dateToString(aRDV.getDT_DEBUT())) %>" target="ClientFrame" title="Modifier le rendez-vous"><img src="images/boutonRDV.gif" border="0"></a>
                    <%
                    }
                    if (aFact != null) {
                    %>
                        <a href="_FicheFact.jsp?Action=Modification&CD_FACT=<%= aFact.getCD_FACT() %>" target="ClientFrame" title="Voir la facture"><img src="images/fact.gif" border="0"></a>
                    <%
                    }
                    else {
                        if (peutCreerFacture) { %>
                            <a href="addCli.srv?CD_CLI=<%= aRDVFact.getClient().getCD_CLI() %>" title="Dupliquer sa facture" target=MenuFrame><img src=images/plus.gif border="0" width="15" height="15"></a>
                            <a href="addCli.srv?Vide=1&CD_CLI=<%= aRDVFact.getClient().getCD_CLI() %>" title="Accueillir ce client" target=MenuFrame><img src=images/plus2.gif border="0" width="15" height="15"></a>
                    <%
                        } 
                        else { %>
                            <img src=images/plusNon.gif border="0" width="15" height="15" alt="Action impossible : Pas de collaborateur présent"/>
                            <img src=images/plus2Non.gif border="0" width="15" height="15" alt="Action impossible : Pas de collaborateur présent"/>
                    <%
                        }
                    %>
                        <a href="ficTech.srv?Action=Impression&CD_CLI=<%= aRDVFact.getClient().getCD_CLI() %>" title="Imprimer la fiche technique" target="_blank"><img src=images/Tech.gif border="0" width="15" height="15"></a>
                    <%
                    } %>
                </td>
                <td class="tabDonneesGauche">
                    <salon:valeur valeur="<%= aRDVFact.getDate() %>" valeurNulle="null" format="dd/MM HH:mm"> %% </salon:valeur>
                    <%
                    if (aRDV != null) {
                    %>
                        <salon:valeur valeur="<%= aRDV.getDUREE() %>" valeurNulle="null"> (%% min) </salon:valeur>
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
        <a href="javascript:dupliquerFacture()" title="Dupliquer les factures"><img src=images/plus.gif border="0" width="15" height="15"> Dupliquer les factures de chaque rendez-vous</a><br/>
        <a href="javascript:accueillirClient()" title="Accueillir les clients"><img src=images/plus2.gif border="0" width="15" height="15"> Accueillir tous les clients des rendez-vous</a>
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
    window.open("aideListe.html");
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
