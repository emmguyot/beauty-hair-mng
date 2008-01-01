<%
/*
 * This program is part of InCrEG LibertyLook software http://beauty-hair-mng.sourceforge.net
 * Copyright (C) 2001-2008 Emmanuel Guyot <See emmguyot on SourceForge> 
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms 
 * of the GNU General Public License as published by the Free Software Foundation; either 
 * version 2 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; 
 * if not, write to the 
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 */
%>
<%@ page import="java.util.Vector,
			java.util.List,
			java.net.URLEncoder,
			java.util.Calendar,
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
<title><i18n:message key="title.lstPointage" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees">
<%@ include file="include/commun.jsp" %>
<%
   // Récupération des paramètres
   String CD_COLLAB = request.getParameter("CD_COLLAB");
   Calendar DT_DEBUT = (Calendar) request.getAttribute("DT_DEBUT");
   Calendar DT_FIN = (Calendar) request.getAttribute("DT_FIN");
   String CD_TYP_POINTAGE = request.getParameter("CD_TYP_POINTAGE");
%>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/lstPointage.gif"></h1>
<form name="fiche" action="rechPointage.srv" method="post">
<p>
<i18n:message key="label.collaborateur" /> :
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
      <option value=""><i18n:message key="label.tousDsListe" /></option>
      %%
   </select>
</salon:selection>
<i18n:message key="label.typePointage" /> :
<salon:DBselection valeur="<%= CD_TYP_POINTAGE %>" sql="select CD_TYP_POINTAGE, LIB_TYP_POINTAGE from TYP_POINTAGE order by LIB_TYP_POINTAGE">
   <select name="CD_TYP_POINTAGE" onChange="document.fiche.submit()">
      <option value=""><i18n:message key="label.tousDsListe" /></option>
      %%
   </select>
</salon:DBselection>
</p>
<p>
<i18n:message key="label.entreLe" /> :
<i18n:message key="format.dateDefaut" id="formatDate" />
    <salon:date type="text" name="DT_DEBUT" valeurDate="<%= DT_DEBUT %>" valeurNulle="null" format="<%= formatDate %>" calendrier="true" onchange="document.fiche.submit()">%%</salon:date>
<i18n:message key="label.etLe" /> : 
    <salon:date type="text" name="DT_FIN" valeurDate="<%= DT_FIN %>" valeurNulle="null" format="<%= formatDate %>" calendrier="true" onchange="document.fiche.submit()">%%</salon:date>
</p>
</form>
<hr>
<table width="100%" border="1" >
	<tr>
		<th><i18n:message key="label.collaborateur" /></th>
		<th><i18n:message key="label.debut" /></th>
		<th><i18n:message key="label.fin" /></th>
		<th><i18n:message key="label.typePointage" /></th>
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
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideListe.html");
}

</script>
</body>
</html>
