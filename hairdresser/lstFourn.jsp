<%@ page import="java.util.Vector,com.increg.salon.bean.FournBean" %>
<html>
<head>
<title>Liste des Fournisseurs</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees">
<h1><img src="images/titres/lstFourn.gif"></h1>
<table width="100%" border="1" >
	<tr>
		<th>Raison sociale</th>
		<th>Nom contact</th>
		<th>Adresse</th>
		<th>Ville</th>
	</tr>
	<%
	// Recup�re la liste
	Vector lstLignes = (Vector) request.getAttribute("Liste");
	    
	for (int i=0; i< lstLignes.size(); i++) {
	    FournBean aFourn = (FournBean) lstLignes.get(i);
	%>
	<tr>
		<td><a href="_FicheFourn.jsp?Action=Modification&CD_FOURN=<%= aFourn.getCD_FOURN() %>" target="ClientFrame"><%= aFourn.toString() %></a></td>
	    <td><%= aFourn.getNOM_CONT() %>&nbsp;</td>
	    <td><%= aFourn.getRUE() %>&nbsp;</td>
	    <td><%= aFourn.getVILLE() %>&nbsp;</td>
	</tr>
	<%
	}
	%>
</table>
<script language="JavaScript">
function Nouveau()
{
   parent.location.href = "_FicheFourn.jsp";
}

// Affichage de l'aide
function Aide()
{
    window.open("aideListe.html");
}

</script>
</body>
</html>
