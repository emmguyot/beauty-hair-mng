<%@ page import="java.util.Vector,com.increg.salon.bean.TrAgeBean" %>
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
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/lstParam.gif"><br><span class="ssTitre">Tranches d'âge</span></h1>
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
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideListe.html");
}

</script>
</body>
</html>
