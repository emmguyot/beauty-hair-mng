<%@ page import="java.util.Vector,com.increg.salon.bean.CriterePubBean" %>
<%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %>
<html>
<head>
<title>Liste des Critères de publipostage</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees">
<h1><img src="images/titres/lstPub.gif"></h1>
<table width="100%" border="1" >
	<tr>
        <salon:autorisation entite="Publipostage" action="Modification">
		<th></th>
        </salon:autorisation>
		<th>Libellé</th>
	</tr>
	<%
	// Recupère la liste
	Vector lstLignes = (Vector) request.getAttribute("Liste");
	    
	for (int i=0; i < lstLignes.size(); i++) {
	    CriterePubBean aCriterePub = (CriterePubBean) lstLignes.get(i);
	%>
	<tr>
        <salon:autorisation entite="Publipostage" action="Modification">
	    <td width=20><a href="_FicheCriterePub.jsp?Action=Modification&CD_CRITERE_PUB=<%= aCriterePub.getCD_CRITERE_PUB() %>" target="ClientFrame"><img src="images/pub2.gif" border=0></a></td>
        </salon:autorisation>
	    <td><a href="_FicheCriterePub.jsp?Action=Construction&CD_CRITERE_PUB=<%= aCriterePub.getCD_CRITERE_PUB() %>" target="ClientFrame"><%= aCriterePub.toString() %></a></td>
	</tr>
	<%
	}
	%>
</table>
<script language="JavaScript">
function Nouveau()
{
   parent.location.href = "_FicheCriterePub.jsp";
}

// Affichage de l'aide
function Aide()
{
    window.open("aideListe.html");
}

</script>
</body>
</html>
