<%@ page import="java.util.TreeMap,
				java.util.Set,
				java.util.Iterator,
				java.math.BigDecimal,
				java.util.List,
				java.util.Date" %>
<%@ page import="com.increg.salon.request.Presence,
				com.increg.salon.bean.CollabBean,
	        com.increg.salon.bean.SalonSession
	       " %>
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
<title>Présences</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees">
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
<%
   // Récupération des paramètres
   String CD_COLLAB = request.getParameter("CD_COLLAB");
   Date DT_DEBUT = (Date) request.getAttribute("DT_DEBUT");
   Date DT_FIN = (Date) request.getAttribute("DT_FIN");
%>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/lstPresence.gif"></h1>
<form name="fiche" action="rechPresence.srv" method="post">
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
</p>
<p>
Entre le :
    <salon:date type="text" name="DT_DEBUT" valeurDate="<%= DT_DEBUT %>" valeurNulle="null" format="dd/MM/yyyy" calendrier="true" onchange="document.fiche.submit()">%%</salon:date>
   et le : 
    <salon:date type="text" name="DT_FIN" valeurDate="<%= DT_FIN %>" valeurNulle="null" format="dd/MM/yyyy" calendrier="true" onchange="document.fiche.submit()">%%</salon:date>
</p>
</form>
<hr>
<%
// Recupère les listes
TreeMap lstTypes = (TreeMap) request.getAttribute("ListeType");
TreeMap lstLignes = (TreeMap) request.getAttribute("Liste");
BigDecimal totaux[] = new BigDecimal[lstTypes.size()];
%>
<table width="100%" border="1" rules="groups">
<colgroup>
<colgroup>
<colgroup span="<%= lstTypes.size() %>">
<colgroup>
	<thead>
	<tr>
		<th>Semaine du</th>
		<th>Collaborateur</th>
		<% {
		     Set keys = lstTypes.keySet();
                     int cnt = 0;
		     for (Iterator i= keys.iterator(); i.hasNext(); cnt++) { %>
			<th><%= (String) lstTypes.get(i.next()) %></th>
		  <%    totaux[cnt] = new BigDecimal(0);
                     }
		  } %>
	</tr>
	</thead>
	<tbody>
	<%    
	Set keys = lstLignes.keySet();
	for (Iterator i=keys.iterator(); i.hasNext(); ) {
	    Presence aPresence = (Presence) lstLignes.get(i.next());
	%>
	<tr>
	    <td class="tabDonnees">
	       <salon:valeur valeurNulle="null" valeur="<%= aPresence.getDebut() %>" format="dd/MM/yyyy">
		  %%
	       </salon:valeur>
	    </td>
	    <td class="tabDonnees">
	       <salon:valeur valeurNulle="null" valeur="<%= aPresence.getPRENOM() %>" >
		  %%&nbsp;
	       </salon:valeur>
	    </td>
	    <% Set typesKeys = lstTypes.keySet();
               int cnt = 0;
	       for (Iterator j=typesKeys.iterator(); j.hasNext(); cnt++) { 
                BigDecimal valeurDec = aPresence.getPointage(((Integer) j.next()).intValue()); %>
                <td class="Nombre">
		  <salon:valeur valeurNulle="0" valeur="<%= valeurDec %>" heureDec="true">
		     %%&nbsp;
		  </salon:valeur>
                </td>
	    <%  if (valeurDec != null) {
                    totaux[cnt] = totaux[cnt].add(valeurDec);
                }
               } %>
	</tr>
	<%
	}
	%>
	</tbody>
        <tfoot>
        <tr>
            <td colspan=2>&nbsp;</td>
            <%  Set typesKeys = lstTypes.keySet();
                for (int cnt=0; cnt < totaux.length; cnt++) { %>
                    <td class="Nombre">
                        <salon:valeur valeurNulle="0" valeur="<%= totaux[cnt] %>" heureDec="true">
                            %%&nbsp;
                        </salon:valeur>
                    </td>
	    <% } %>
        </tfoot>
</table>
<salon:madeBy />
<script language="JavaScript">
<!--

// Affichage de l'aide
function Aide()
{
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideListe.html");
}

//-->
</script>
</body>
</html>
