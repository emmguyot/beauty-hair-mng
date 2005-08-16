<%@ page import="com.increg.salon.bean.SalonSession,
				java.util.HashMap,
				java.util.Set,
				java.util.Iterator,
				java.util.Vector,
	       		com.increg.salon.bean.CollabBean,
				com.increg.salon.bean.ClientBean,
				com.increg.salon.bean.PrestBean,
				com.increg.salon.bean.HistoPrestBean" %>
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
<title>Liste des abonnements du clients</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees">
<%@ include file="include/commun.js" %>
<script language="JavaScript">
<!--
function Init() {
   <%
   // Positionne les liens d'actions
   %>
   MM_showHideLayers('NOUVEAU?bottomFrame','','hide');
}
//-->
</script>
<%
    // Récupération des paramètres
    ClientBean aCli = (ClientBean) request.getAttribute("ClientBean");
    String NbPrest = (String) request.getAttribute("NbPrest");
    // Recupère la liste
    HashMap lstLignes = aCli.getAbonnements();
%>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/lstAbonnement.gif"></h1>

<p>
	<span class="obligatoire">Client</span> :
	<span class="readonly"><a href="_FicheCli.jsp?Action=Modification&CD_CLI=<%= aCli.getCD_CLI() %>" target="ClientFrame"><%= aCli.toString() %></a></span> 
</p>

<table border="1">
	<tr>
		<th>Prestation</th>
		<th>Nombre restant</th>
	</tr>
	<%
	Set prestSet = lstLignes.keySet();
	Iterator prestIter = prestSet.iterator();
	
	while (prestIter.hasNext()) {
		Long CD_PREST = (Long) prestIter.next();
		PrestBean aPrest = PrestBean.getPrestBean(mySalon.getMyDBSession(), CD_PREST.toString());
		
		
	%>
		<tr>
		    <td><%= aPrest.toString() %></td>
		    <td class="tabDonnees">
			<salon:valeur valeurNulle="-1" valeur="<%= (Integer) lstLignes.get(CD_PREST) %>" expand="true">
			  %%
	        </salon:valeur>
		    &nbsp;</td>
		</tr>
	<%
	}
	%>
</table>
<p>
<hr/>
</p>
<table border="0" cellspacing="0" width="100%">
    <tr>
        <td>
            <%=NbPrest%> derni&egrave;res prestations liées à un abonnement :
                    &nbsp;&nbsp;<a href="javascript:toutesPrest()">Toutes les prestations abonnements</a> 
        </td>
    </tr>
</table>

<table border="0" width="100%">
    <tr>
        <th>Date</th>
        <th>Prestation</th>
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
            <salon:valeur expand="true" valeurNulle="null" valeur="<%= aPrest.getCOMM() %>" >
				%%
<%
            if (((Integer) request.getAttribute("Longueur")).intValue() == 0) { %>
                &nbsp;
<%
            } %>
            </salon:valeur>
        </td>
    </tr>
<%
        } %>
</table>
<form method="post" action="rechAbonnement.srv" name="fiche">
    <salon:valeur valeurNulle="0" valeur="<%= aCli.getCD_CLI() %>" >
        <input type="hidden" name="CD_CLI" value="%%" >
    </salon:valeur>
	<input type="hidden" name="Action">
</form>

<script language="JavaScript">

// Affiche toutes les prestations
function toutesPrest()
{
   document.fiche.Action.value = "Complet";
   document.fiche.submit();

}

// Affichage de l'aide
function Aide()
{
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideFicheCli.html");
}

</script>
</body>
</html>
