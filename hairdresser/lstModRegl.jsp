<%@ page import="java.util.Vector,com.increg.salon.bean.ModReglBean" %>
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
<title>Liste de modes de règlement</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees">
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/lstParam.gif"><br><span class="ssTitre">Modes de règlement</span></h1>
<table width="100%" border="1" >
	<tr>
		<th>Libellé</th>
		<th>Actuel</th>
		<th>Impression de chèques</th>
		<th>Rendu de monnaie</th>
	</tr>
	<%
	// Recupère la liste
	Vector lstLignes = (Vector) request.getAttribute("Liste");
	    
	for (int i=0; i< lstLignes.size(); i++) {
	    ModReglBean aModRegl = (ModReglBean) lstLignes.get(i);
	%>
	<tr>
		<td><a href="_FicheModRegl.jsp?Action=Modification&CD_MOD_REGL=<%= aModRegl.getCD_MOD_REGL() %>" target="ClientFrame"><%= aModRegl.toString() %></a></td>
		<td><%= aModRegl.getUTILISABLE() %></td>
		<td><%= aModRegl.getIMP_CHEQUE() %></td>
		<td><%= aModRegl.getRENDU_MONNAIE() %></td>
	</tr>
	<%
	}
	%>
</table>
<script language="JavaScript">
function Nouveau()
{
   parent.location.href = "_FicheModRegl.jsp";
}

// Affichage de l'aide
function Aide()
{
    window.open("aideListe.html");
}

</script>
</body>
</html>
