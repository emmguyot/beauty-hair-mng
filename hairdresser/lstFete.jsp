<%@ page import="java.util.Vector,com.increg.salon.bean.FeteBean" %>
<html>
<head>
<title>Liste des Fêtes</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees">
<%
   // Récupération des paramètres
   String premLettre = (String) request.getAttribute("premLettre");
%>
<h1><img src="images/titres/lstParam.gif"><br><span class="ssTitre">Fêtes</span></h1>
<p>Premi&egrave;re lettre du prénom : 
<%
   // Affiche toutes les lettres avec un lien permettant de filtrer par cette lettre
   for (char c='A'; Character.isUpperCase(c); c++) { 
      if ((premLettre != null) && (premLettre.charAt(0) == c)) { %>
	 <%=c %>
      <% } else { %>
	 <a href="rechFete.srv?premLettre=<%=c %>"><%=c %></a>     
   <% }
   }
   if ((premLettre != null) && (premLettre.charAt(0) == ' ')) { %>
	 Tous
      <% } else { %>
	 <a href="rechFete.srv?premLettre=%20">Tous</a>     
   <% }
%>
</p>
<hr>
<table width="100%" border="1" >
	<tr>
		<th>Prénom</th>
		<th>Fête</th>
	</tr>
	<%
	// Recupère la liste
	Vector lstLignes = (Vector) request.getAttribute("Liste");
	    
	for (int i=0; i< lstLignes.size(); i++) {
	    FeteBean aFete = (FeteBean) lstLignes.get(i);
	%>
	<tr>
		<td><a href="_FicheFete.jsp?Action=Modification&CD_FETE=<%= aFete.getCD_FETE() %>" target="ClientFrame"><%= aFete.getPRENOM() %></a></td>
	    <td><%= aFete.toString() %>&nbsp;</td>
	</tr>
	<%
	}
	%>
</table>
<script language="JavaScript">
function Nouveau()
{
   parent.location.href = "_FicheFete.jsp";
}

// Affichage de l'aide
function Aide()
{
    window.open("aideListe.html");
}

</script>
</body>
</html>
