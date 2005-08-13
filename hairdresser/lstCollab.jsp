<%@ page import="com.increg.salon.bean.SalonSession,java.util.Vector,
	       com.increg.salon.bean.CollabBean,
	       com.increg.salon.bean.DonneeRefBean" %>
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
<title>Liste des collaborateurs</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees">
<%
   // Récupération des paramètres
   String INDIC_VALID = (String) request.getAttribute("INDIC_VALID");
%>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/lstCollab.gif"></h1>
<form name="fiche" action="rechCollab.srv" method="post">
Affiche anciens collaborateurs : 
   <input type="checkbox" name="INDIC_VALID"
   <% if ((INDIC_VALID != null) && (INDIC_VALID.equals("on"))) { %>
   checked 
   <% } %>
   onClick="document.fiche.submit()" >
</form>
<hr>
<table width="100%" border="1" >
	<tr>
		<th>Collaborateur</th>
		<th>Ville</th>
		<th>Fonction</th>
	</tr>
	<%
	// Recupère la liste
	Vector lstLignes = (Vector) request.getAttribute("Liste");
	    
	for (int i=0; i< lstLignes.size(); i++) {
	    CollabBean aCollab = (CollabBean) lstLignes.get(i);
	%>
	<tr>
		<td><a href="_FicheCollab.jsp?Action=Modification&CD_COLLAB=<%= aCollab.getCD_COLLAB() %>" target="ClientFrame"><%= aCollab.toString() %></a></td>
	<td><%= aCollab.getVILLE() %>&nbsp;</td>
	<td>
	    <% DonneeRefBean FCT = DonneeRefBean.getDonneeRefBean(mySalon.getMyDBSession(), "FCT",
							       Integer.toString(aCollab.getCD_FCT())); %>
	    <salon:valeur valeur="<%= (FCT == null) ? null : FCT.toString() %>" valeurNulle="null"> %% </salon:valeur>
	    &nbsp;
	</td>
	</tr>
	<%
	}
	%>
</table>
<script language="JavaScript">
function Nouveau()
{
   parent.location.href = "_FicheCollab.jsp";
}

// Affichage de l'aide
function Aide()
{
    window.open("aideListe.html");
}

</script>
</body>
</html>
