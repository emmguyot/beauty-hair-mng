<%@ page import="com.increg.salon.bean.SalonSession,java.util.Vector,
   com.increg.salon.bean.IdentBean,
   com.increg.salon.bean.DonneeRefBean" %>
<%
    SalonSession mySalon = (SalonSession) session.getAttribute("SalonSession");
    if (mySalon == null) {
        getServletConfig().getServletContext().getRequestDispatcher("/reconnect.html").forward(request, response);
    }
%>
<html>
<head>
<title>Liste des Identifications</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees">
<h1><img src="images/titres/lstIdent.gif"></h1>
<table width="100%" border="1" >
	<tr>
		<th>Libellé</th>
		<th>Profil</th>
		<th>Etat</th>
	</tr>
	<%
	// Recupère la liste
	Vector lstLignes = (Vector) request.getAttribute("Liste");
	    
	for (int i=0; i< lstLignes.size(); i++) {
	    IdentBean aIdent = (IdentBean) lstLignes.get(i);
	%>
	<tr>
	    <td><a href="_FicheIdent.jsp?Action=Modification&CD_IDENT=<%= aIdent.getCD_IDENT() %>" target="ClientFrame"><%= aIdent.toString() %></a></td>
	    <td><%= DonneeRefBean.getDonneeRefBean(mySalon.getMyDBSession(), "PROFIL", Integer.toString(aIdent.getCD_PROFIL())).toString() %></td>
	    <td><%= aIdent.getETAT_CPT() %></td>
	</tr>
	<%
	}
	%>
</table>
<script language="JavaScript">
function Nouveau()
{
   parent.location.href = "_FicheIdent.jsp";
}

// Affichage de l'aide
function Aide()
{
    window.open("aideListe.html");
}

</script>
</body>
</html>
