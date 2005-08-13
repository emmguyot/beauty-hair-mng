<%@ page import="java.util.Vector,java.net.URLEncoder,com.increg.salon.bean.DonneeRefBean" %>
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
<%
   // Récupération des paramètres
   String nomTable = request.getParameter("nomTable");
   String chaineTable = request.getParameter("chaineTable");
%>
<title>Liste de <%= chaineTable %></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees">
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/lstParam.gif"><br><span class="ssTitre"><%= chaineTable %></span></h1>
<table width="100%" border="1" >
	<tr>
		<th>Libellé</th>
	</tr>
	<%
	// Recupère la liste
	Vector lstLignes = (Vector) request.getAttribute("Liste");
	    
	for (int i=0; i< lstLignes.size(); i++) {
	    DonneeRefBean aDonneeRef = (DonneeRefBean) lstLignes.get(i);
	%>
	<tr>
		<td><a href="_FicheDonneeRef.jsp?nomTable=<%= nomTable %>&chaineTable=<%= URLEncoder.encode(chaineTable) %>&Action=Modification&CD=<%= aDonneeRef.getCD() %>" target="ClientFrame"><%= aDonneeRef.toString() %></a></td>
	</tr>
	<%
	}
	%>
</table>
<script language="JavaScript">
function Nouveau()
{
   parent.location.href = "_FicheDonneeRef.jsp?nomTable=<%= nomTable %>&chaineTable=<%= URLEncoder.encode(chaineTable) %>";
}

// Affichage de l'aide
function Aide()
{
    window.open("aideListe.html");
}

</script>
</body>
</html>
