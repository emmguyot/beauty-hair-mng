<%@ page import="com.increg.salon.bean.SalonSession,
				java.util.HashMap,
				java.util.Set,
				java.util.Iterator,
				com.increg.salon.bean.ClientBean,
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
    // Recupère la liste
    HashMap lstLignes = aCli.getAbonnements();
    %>
<h1><img src="images/titres/lstAbonnement.gif"></h1>

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
<script language="JavaScript">
// Affichage de l'aide
function Aide()
{
    window.open("aideFicheCli.html");
}

</script>
</body>
</html>
