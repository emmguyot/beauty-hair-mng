<%@ page import="java.util.Vector,com.increg.salon.bean.TvaBean" %>
<%@ page import="com.increg.salon.bean.SalonSession" %>
<%
    SalonSession mySalon = (SalonSession) session.getAttribute("SalonSession");
    if (mySalon == null) {
        getServletConfig().getServletContext().getRequestDispatcher("/reconnect.html").forward(request, response);
    }
%>
<%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %>
<%@ taglib uri="WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<i18n:bundle baseName="messages" locale="<%= mySalon.getLangue() %>"/>
<html>
<head>
<title>Liste des taux de TVA</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
<%@ include file="include/commun.js" %>
<script language="JavaScript">
<!--
function Init() {
   <%
   // Positionne les liens d'actions
   %>
   MM_showHideLayers('NOUVEAU?bottomFrame','','show');
}
//-->
</script>
</head>
<body class="donnees">
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/lstParam.gif"><br><span class="ssTitre">Taux de TVA</span></h1>
<table width="100%" border="1" >
	<tr>
		<th>Libellé</th>
		<th>Taux</th>
	</tr>
	<%
	// Recupère la liste
	Vector lstLignes = (Vector) request.getAttribute("Liste");
	    
	for (int i=0; i < lstLignes.size(); i++) {
	    TvaBean aTva = (TvaBean) lstLignes.get(i);
	%>
	<tr>
		<td><a href="_FicheTxTVA.jsp?Action=Modification&CD_TVA=<%= aTva.getCD_TVA() %>" target="ClientFrame"><%= aTva.toString() %></a></td>
        <td><salon:valeur valeur="<%= aTva.getTX_TVA() %>" valeurNulle="null">%%</salon:valeur></td>
	</tr>
	<%
	}
	%>
</table>
<script language="JavaScript">
<!--

// Part en création
function Nouveau()
{
   parent.location.href = "_FicheTxTVA.jsp";
}

// Affichage de l'aide
function Aide()
{
    window.open("aideListe.html");
}

//-->
</script>
</body>
</html>
