<%@ page import="java.util.Vector,com.increg.salon.bean.TrAgeBean" %>
<html>
<head>
<title>Liste des tranches d'âge</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
<%@ include file="include/commun.js" %>
<script language="JavaScript">
<!--
function Init() {
   <%
   // Positionne les liens d'actions
   %>
}
//-->
</script>
</head>
<body class="donnees">
<h1><img src="images/titres/lstParam.gif"><br><span class="ssTitre">Tranches d'âge</span></h1>
<table width="100%" border="1" >
	<tr>
		<th>Libellé</th>
		<th>Age minimum</th>
		<th>Age maximum</th>
	</tr>
	<%
	// Recupère la liste
	Vector lstLignes = (Vector) request.getAttribute("Liste");
	    
	for (int i=0; i< lstLignes.size(); i++) {
	    TrAgeBean aTrAge = (TrAgeBean) lstLignes.get(i);
	%>
	<tr>
		<td><a href="_FicheTrAge.jsp?Action=Modification&CD_TR_AGE=<%= aTrAge.getCD_TR_AGE() %>" target="ClientFrame"><%= aTrAge.toString() %></a></td>
		<td><%= aTrAge.getAGE_MIN() %></td>
		<td><%= aTrAge.getAGE_MAX() %></td>
	</tr>
	<%
	}
	%>
</table>
<script language="JavaScript">
function Nouveau()
{
   parent.location.href = "_FicheTrAge.jsp";
}

// Affichage de l'aide
function Aide()
{
    window.open("aideListe.html");
}

</script>
</body>
</html>
