<%@ page import="java.util.Vector,com.increg.salon.bean.ParamBean" %>
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
<title>Liste des paramètres de l'application</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
<%@ include file="include/commun.js" %>
<script language="JavaScript">
<!--
function Init() {
   <%
   // Positionne les liens d'actions
   %>
   MM_showHideLayers('NOUVEAU?bottomFrame','','hide');
}
//-->
</script>
</head>
<body class="donnees">
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/lstParam.gif"></h1>
<table width="100%" border="1" >
	<tr>
		<th>Description</th>
		<th>Valeur</th>
	</tr>
	<%
	// Recupère la liste
	Vector lstLignes = (Vector) request.getAttribute("Liste");
	    
	for (int i=0; i< lstLignes.size(); i++) {
	    ParamBean aParam = (ParamBean) lstLignes.get(i);
	%>
	<tr>
		<td><a href="_FicheParam.jsp?Action=Modification&CD_PARAM=<%= aParam.getCD_PARAM() %>" target="ClientFrame"><%= aParam.toString() %></a></td>
		<td><% if (aParam.getCD_PARAM() != ParamBean.CD_OP_EXCEPTIONNEL) { %>
                 <%= aParam.getVAL_PARAM() %>
                 <% } else { %>******<% } %></td>
	</tr>
	<%
	}
	%>
</table>
<script language="JavaScript">
<!--

// Affichage de l'aide
function Aide()
{
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideListeParam.html");
}

//-->
</script>
</body>
</html>
