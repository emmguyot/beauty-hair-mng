<%@ page import="java.util.Vector,com.increg.salon.bean.TypMvtBean" %>
<html>
<head>
<title>Liste de types de mouvements</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees">
<h1><img src="images/titres/lstParam.gif"><br><span class="ssTitre">Types de mouvements</span></h1>
<table width="100%" border="1" >
	<tr>
		<th>Libellé</th>
		<th>Sens</th>
		<th>Basculement article mixte</th>
	</tr>
	<%
	// Recupère la liste
	Vector lstLignes = (Vector) request.getAttribute("Liste");
	    
	for (int i=0; i< lstLignes.size(); i++) {
	    TypMvtBean aTypMvt = (TypMvtBean) lstLignes.get(i);
	%>
	<tr>
		<td><a href="_FicheTypMvt.jsp?Action=Modification&CD_TYP_MVT=<%= aTypMvt.getCD_TYP_MVT() %>" target="ClientFrame"><%= aTypMvt.toString() %></a></td>
		<td><%= aTypMvt.getSENS_MVT() %></td>
		<td><%= aTypMvt.getTRANSF_MIXTE() %></td>
	</tr>
	<%
	}
	%>
</table>
<script language="JavaScript">
function Nouveau()
{
   parent.location.href = "_FicheTypMvt.jsp";
}

// Affichage de l'aide
function Aide()
{
    window.open("aideListe.html");
}

</script>
</body>
</html>
