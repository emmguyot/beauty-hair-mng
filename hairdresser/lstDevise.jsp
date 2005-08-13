<%@ page import="java.util.Vector,com.increg.salon.bean.DeviseBean" %>
<%@ page import="com.increg.salon.bean.SalonSession" %>
<%
    SalonSession mySalon = (SalonSession) session.getAttribute("SalonSession");
    if (mySalon == null) {
        getServletConfig().getServletContext().getRequestDispatcher("/reconnect.html").forward(request, response);
    }
%>
<%@ taglib uri="WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<i18n:bundle baseName="messages" locale="<%= mySalon.getLangue() %>"/>
<html>
<head>
<title>Liste des devises</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees">
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/lstParam.gif"><br><span class="ssTitre">Devises</span></h1>
<table width="100%" border="1" >
	<tr>
		<th>Libellé court</th>
		<th>Libellé</th>
		<th>Rapport à la<br>devise principale</th>
	</tr>
	<%
	// Recupère la liste
	Vector lstLignes = (Vector) request.getAttribute("Liste");
	    
	for (int i=0; i< lstLignes.size(); i++) {
	    DeviseBean aDevise = (DeviseBean) lstLignes.get(i);
	%>
	<tr>
		<td><a href="_FicheDevise.jsp?Action=Modification&CD_DEVISE=<%= aDevise.getCD_DEVISE() %>" target="ClientFrame"><%= aDevise.toString() %></a></td>
		<td><%= aDevise.getLIB_DEVISE() %></td>
		<td><%= aDevise.getRATIO() %></td>
	</tr>
	<%
	}
	%>
</table>
<script language="JavaScript">
function Nouveau()
{
   parent.location.href = "_FicheDevise.jsp";
}

// Affichage de l'aide
function Aide()
{
    window.open("aideListe.html");
}

</script>
</body>
</html>
