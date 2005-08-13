<%@ page import="com.increg.salon.bean.SalonSession,
				java.util.Vector,
				java.util.HashMap,
				com.increg.salon.bean.ClientBean" %>
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
<title>Liste des clients pour un RDV</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees">
<%
    // Récupération des paramètres : Attention du fait du passage par 2 frames, les attributes sont en session
    HashMap attributes = (HashMap) session.getAttribute("attributes");
    session.removeAttribute("attributes");
    // Recupère la liste
    Vector lstLignes = (Vector) attributes.get("Liste");
    
    String CD_COLLAB = (String) attributes.get("CD_COLLAB");
    String DT_DEBUT = (String) attributes.get("DT_DEBUT");
    String HR_DEBUT = (String) attributes.get("HR_DEBUT");
    String NOM = (String) attributes.get("NOM");
    String PRENOM = (String) attributes.get("PRENOM");
%>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/lstCli.gif"></h1>
<form name="fiche" action="ficRDV.srv" method="post">

	<input type="hidden" name="Action" value="Creation">
	<input type="hidden" name="CD_COLLAB" value="<%= CD_COLLAB %>">
	<input type="hidden" name="DT_DEBUT" value="<%= DT_DEBUT %>">
	<input type="hidden" name="HR_DEBUT" value="<%= HR_DEBUT %>">
	<input type="hidden" name="NOM" value="<%= NOM %>">
	<salon:valeur valeurNulle="null" valeur="<%= PRENOM %>" >
		<input type="hidden" name="PRENOM" value="%%">
    </salon:valeur>
	<input type="hidden" name="COMM" value="">
	<input type="hidden" name="DUREE" value="30">

<table width="100%" border="1" >
	<tr>
		<th></th>
		<th>Nom client</th>
		<th>Adresse</th>
		<th>Ville</th>
	</tr>
	<%
	    
	for (int i = 0; i < lstLignes.size(); i++) {
	    ClientBean aCli = (ClientBean) lstLignes.get(i);

	%>
	<tr>
	    <td width=75>
		    <input type="checkbox" name="CD_CLI" value="<%= aCli.getCD_CLI() %>" onclick="document.fiche.submit()">
		</td>
	    <td><a href="_FicheCli.jsp?Action=Modification&CD_CLI=<%= aCli.getCD_CLI() %>" target="ClientFrame"><%= aCli.toStringListe() %></a></td>
	    <td>
			<salon:valeur valeurNulle="0" valeur="<%= aCli.getRUE() %>" expand="true">
		  		%%
	        </salon:valeur>
	    	&nbsp;</td>
	    <td><%= aCli.getVILLE() %>&nbsp;</td>
	    
	</tr>
	<%
	}
	
	if (lstLignes.size() == 0) {
		// Pas de client... Prévoit un champ quand même
	%>
		<input type="hidden" name="CD_CLI">
	<%
	}
	%>
</table>
<script language="JavaScript">
function Nouveau()
{
	document.fiche.CD_CLI.value = "0";
	document.fiche.Action.value = "Creation+";
    document.fiche.submit();
}

// Affichage de l'aide
function Aide()
{
    window.open("aideListeCli.html");
}

</script>
</form>
</body>
</html>
