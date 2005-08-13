<%@ page import="java.util.Vector,com.increg.salon.bean.TypMcaBean" %>
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
<title>Liste de types de mouvements de caisse</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees">
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/lstParam.gif"><br><span class="ssTitre">Types de mouvements de caisse</span></h1>
<table width="100%" border="1" >
	<tr>
		<th>Libellé</th>
		<th>Sens</th>
	</tr>
	<%
	// Recupère la liste
	Vector lstLignes = (Vector) request.getAttribute("Liste");
	    
	for (int i=0; i< lstLignes.size(); i++) {
	    TypMcaBean aTypMca = (TypMcaBean) lstLignes.get(i);
	%>
	<tr>
		<td><a href="_FicheTypMca.jsp?Action=Modification&CD_TYP_MCA=<%= aTypMca.getCD_TYP_MCA() %>" target="ClientFrame"><%= aTypMca.toString() %></a></td>
		<td><%= aTypMca.getSENS_MCA() %></td>
	</tr>
	<%
	}
	%>
</table>
<script language="JavaScript">
function Nouveau()
{
   parent.location.href = "_FicheTypMca.jsp";
}

// Affichage de l'aide
function Aide()
{
    window.open("aideListe.html");
}

</script>
</body>
</html>
