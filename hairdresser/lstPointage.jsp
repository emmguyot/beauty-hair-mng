<%@ page import="java.util.Vector,
			java.util.List,
			java.net.URLEncoder,
			java.util.Date,
			java.util.Iterator,
	        com.increg.salon.bean.SalonSession,
            com.increg.salon.bean.CollabBean,
	        com.increg.salon.bean.PointageBean,
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
<title>Liste des pointages</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees">
<%@ include file="include/commun.js" %>
<%
   // Récupération des paramètres
   String CD_COLLAB = request.getParameter("CD_COLLAB");
   Date DT_DEBUT = (Date) request.getAttribute("DT_DEBUT");
   Date DT_FIN = (Date) request.getAttribute("DT_FIN");
   String CD_TYP_POINTAGE = request.getParameter("CD_TYP_POINTAGE");
%>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/lstPointage.gif"></h1>
<form name="fiche" action="rechPointage.srv" method="post">
<p>
Collaborateur :
<%
        List collabsList = CollabBean.getAllCollabsAsList(mySalon.getMyDBSession());
        String valeurs="";
        String libelles="";
        Iterator collabIter = collabsList.iterator();

        // On parcourt la liste pour mettre a jour valeur et libelle
        while (collabIter.hasNext()) {
            CollabBean aCollab = (CollabBean) collabIter.next();

			if (valeurs.length() > 0) {
				valeurs = valeurs + "|";
			}
			valeurs = valeurs + aCollab.getCD_COLLAB();

			if (libelles.length() > 0) {
				libelles = libelles + "|";
			}
			libelles = libelles + aCollab.getPRENOM();
		}
%>
<salon:selection valeur="<%= CD_COLLAB %>" valeurs="<%= valeurs %>" libelle="<%= libelles %>">
   <select name="CD_COLLAB" onChange="document.fiche.submit()">
      <option value="">( Tous )</option>
      %%
   </select>
</salon:selection>
Type de pointage :
<salon:DBselection valeur="<%= CD_TYP_POINTAGE %>" sql="select CD_TYP_POINTAGE, LIB_TYP_POINTAGE from TYP_POINTAGE order by LIB_TYP_POINTAGE">
   <select name="CD_TYP_POINTAGE" onChange="document.fiche.submit()">
      <option value="">( Tous )</option>
      %%
   </select>
</salon:DBselection>
</p>
<p>
Entre le :
    <salon:date type="text" name="DT_DEBUT" valeurDate="<%= DT_DEBUT %>" valeurNulle="null" format="dd/MM/yyyy HH:mm:ss" calendrier="true" onchange="document.fiche.submit()">%%</salon:date>
   et le : 
    <salon:date type="text" name="DT_FIN" valeurDate="<%= DT_FIN %>" valeurNulle="null" format="dd/MM/yyyy HH:mm:ss" calendrier="true" onchange="document.fiche.submit()">%%</salon:date>
</p>
</form>
<hr>
<table width="100%" border="1" >
	<tr>
		<th>Collaborateur</th>
		<th>Début</th>
		<th>Fin</th>
		<th>Type de pointage</th>
	</tr>
	<%
	// Recupère la liste
	Vector lstLignes = (Vector) request.getAttribute("Liste");
	    
	for (int i=0; i< lstLignes.size(); i++) {
	    PointageBean aPointage = (PointageBean) lstLignes.get(i);
	%>
	<tr>
		<td><a href="_FichePointage.jsp?Action=Modification&CD_COLLAB=<%= aPointage.getCD_COLLAB() %>&DT_DEBUT=<%= URLEncoder.encode(SalonSession.dateToString(aPointage.getDT_DEBUT())) %>" target="ClientFrame"><%= CollabBean.getCollabBean(mySalon.getMyDBSession(), Integer.toString(aPointage.getCD_COLLAB())).toString() %></a></td>
	<td>
	    <salon:valeur valeur="<%= aPointage.getDT_DEBUT() %>" valeurNulle="null"> %% </salon:valeur>
	</td>
	<td>
	    <salon:valeur valeur="<%= aPointage.getDT_FIN() %>" valeurNulle="null"> %% </salon:valeur>&nbsp;
	</td>
        <td>
	    <% DonneeRefBean TYP = DonneeRefBean.getDonneeRefBean(mySalon.getMyDBSession(), "TYP_POINTAGE",
							       Integer.toString(aPointage.getCD_TYP_POINTAGE())); %>
	    <salon:valeur valeur="<%= TYP.toString() %>" valeurNulle="null"> %% </salon:valeur>
	    &nbsp;
	</td>
	</tr>
	<%
	}
	%>
</table>
<salon:madeBy />
<script language="JavaScript">
function Nouveau()
{
   parent.location.href = "_FichePointage.jsp";
}

// Affichage de l'aide
function Aide()
{
    window.open("aideListe.html");
}

</script>
</body>
</html>
