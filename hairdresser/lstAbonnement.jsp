<%
/*
 * This program is part of InCrEG LibertyLook software http://beauty-hair-mng.sourceforge.net
 * Copyright (C) 2001-2012 Emmanuel Guyot <See emmguyot on SourceForge> 
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
<%@ page import="com.increg.salon.bean.SalonSession,
				java.util.HashMap,
				java.util.Set,
				java.util.Iterator,
				java.util.Vector,
	       		com.increg.salon.bean.CollabBean,
				com.increg.salon.bean.ClientBean,
				com.increg.salon.bean.PrestBean,
				com.increg.salon.bean.HistoPrestBean" %>
<%
SalonSession mySalon = com.increg.salon.servlet.ConnectedServlet.CheckOrGoHome(request, response);
%>
<%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %>
<%@ taglib uri="WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<i18n:bundle baseName="messages" locale="<%= mySalon.getLangue() %>"/>
<html>
<head>
<title><i18n:message key="lstAbonnement.title" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
<link rel="stylesheet" href="style/jquery-ui-1.8.16.custom.css" type="text/css">
</head>
<body class="donnees">
<%@ include file="include/commun.jsp" %>
<script language="JavaScript">
<!--
function Init() {
   <%
   // Positionne les liens d actions
   %>
   MM_showHideLayers('NOUVEAU?bottomFrame','','hide');
}
//-->
</script>
<%
    // Récupération des paramètres
    ClientBean aCli = (ClientBean) request.getAttribute("ClientBean");
    String NbPrest = (String) request.getAttribute("NbPrest");
    // Recupère la liste
    HashMap lstLignes = aCli.getAbonnements();
%>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/lstAbonnement.gif"></h1>

<p>
	<span class="obligatoire"><i18n:message key="label.client" /></span> :
	<span class="readonly"><a href="_FicheCli.jsp?Action=Modification&CD_CLI=<%= aCli.getCD_CLI() %>" target="ClientFrame"><%= aCli.toString() %></a></span> 
</p>

<table border="1">
	<tr>
		<th><i18n:message key="label.prestation" /></th>
		<th><i18n:message key="label.nbRestant" /></th>
	</tr>
	<%
	Set prestSet = lstLignes.keySet();
	Iterator prestIter = prestSet.iterator();
	
	while (prestIter.hasNext()) {
		Long CD_PREST = (Long) prestIter.next();
		PrestBean aPrest = PrestBean.getPrestBean(mySalon.getMyDBSession(), CD_PREST.toString());
		
		
	%>
		<tr>
		    <td><%= aPrest.toString() %></td>
		    <td class="tabDonnees">
			<salon:valeur valeurNulle="-1" valeur="<%= (Integer) lstLignes.get(CD_PREST) %>" expand="true">
			  %%
	        </salon:valeur>
		    &nbsp;</td>
		</tr>
	<%
	}
	%>
</table>
<p>
<hr/>
</p>
<table border="0" cellspacing="0" width="100%">
    <tr>
        <td>
            <i18n:message key="lstAbonnement.dernieresPrestations">
                <i18n:messageArg value="<%= NbPrest %>" />
            </i18n:message> :
            &nbsp;&nbsp;<a href="javascript:toutesPrest()"><i18n:message key="lstAbonnement.toutesPrestations" /></a> 
        </td>
    </tr>
</table>

<table border="0" width="100%">
    <tr>
        <th><i18n:message key="label.date" /></th>
        <th><i18n:message key="label.prestation" /></th>
        <th><i18n:message key="label.collaborateur" /></th>
        <th><i18n:message key="label.commentaire" /></th>
    </tr>
<%
        // Recupère la liste
        Vector listePrest = (Vector) request.getAttribute("listePrest");
        long lastCD_FACT = -1;
        String finClass = "3";
        boolean changeFacture = false;	
        for (int i=0; i< listePrest.size(); i++) {
            changeFacture = false;
            HistoPrestBean aPrest = (HistoPrestBean) listePrest.get(i);
            if (lastCD_FACT != aPrest.getCD_FACT()) {
                // Changement de Facture : Change de class
                finClass = (finClass.equals("3")) ? "2" : "3";
                changeFacture = true;
            }
            lastCD_FACT = aPrest.getCD_FACT();
%>
    <tr class="ligneTab<%= finClass%>">
        <td class="tabDonnees">
            <salon:valeur valeurNulle="null" valeur="<%= aPrest.getDT_PREST() %>" > 
                <a href="_FicheFact.jsp?Action=Modification&CD_FACT=<%= aPrest.getCD_FACT() %>" target="ClientFrame">%%</a> 
            </salon:valeur>
        </td>
        <td class="tabDonnees">
            <salon:valeur valeurNulle="null" valeur="<%= PrestBean.getPrestBean(mySalon.getMyDBSession(), Long.toString(aPrest.getCD_PREST())).toString() %>" >
                %%
            </salon:valeur>
        </td>
        <td class="tabDonnees">
            <salon:valeur valeur="<%= CollabBean.getCollabBean(mySalon.getMyDBSession(), Integer.toString(aPrest.getCD_COLLAB())).toString() %>" valeurNulle="null">
	%%
            </salon:valeur>
        </td>
        <td>
            <salon:valeur expand="true" valeurNulle="null" valeur="<%= aPrest.getCOMM() %>" >
                %%
<%
            if (((Integer) request.getAttribute("Longueur")).intValue() == 0) { %>
                &nbsp;
<%
            } %>
            </salon:valeur>
        </td>
    </tr>
<%
        } %>
</table>
<form method="post" action="rechAbonnement.srv" name="fiche">
    <salon:valeur valeurNulle="0" valeur="<%= aCli.getCD_CLI() %>" >
        <input type="hidden" name="CD_CLI" value="%%" >
    </salon:valeur>
	<input type="hidden" name="Action">
</form>

<script language="JavaScript">

// Affiche toutes les prestations
function toutesPrest()
{
   document.fiche.Action.value = "Complet";
   document.fiche.submit();

}

// Affichage de l'aide
function Aide()
{
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideFicheCli.html");
}

</script>
</body>
</html>
