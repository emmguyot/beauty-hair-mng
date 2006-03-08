<%
/*
 * This program is part of InCrEG LibertyLook software http://beauty-hair-mng.sourceforge.net
 * Copyright (C) 2001-2006 Emmanuel Guyot <See emmguyot on SourceForge> 
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
<%@ page import="java.util.Vector" %>
<%@ page import="com.increg.salon.bean.SalonSession,
	       com.increg.salon.bean.PrestBean,
	       com.increg.salon.bean.TypVentBean,
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
<title><i18n:message key="title.lstPrest" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees">
<%
   // Récupération des paramètres
   String premLettre = (String) request.getAttribute("premLettre");
   String CD_TYP_VENT = request.getParameter("CD_TYP_VENT");
   String CD_CATEG_PREST = request.getParameter("CD_CATEG_PREST");
   String CD_MARQUE = request.getParameter("CD_MARQUE");
   String PERIME = request.getParameter("PERIME");
%>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/lstPrest.gif"></h1>
<form name="fiche" action="rechPrest.srv" method="post">
<p><i18n:message key="label.premiereLettre" /> : 
<input type="hidden" name="premLettre" value="<%= premLettre %>">
<%
   String lien = "";
   if ((CD_TYP_VENT != null) && (CD_TYP_VENT.length() > 0)) {
      lien = lien + "&CD_TYP_VENT=" + CD_TYP_VENT;
   }
   if ((CD_CATEG_PREST != null) && (CD_CATEG_PREST.length() > 0)) {
      lien = lien + "&CD_CATEG_PREST=" + CD_CATEG_PREST;
   }
   if ((CD_MARQUE != null) && (CD_MARQUE.length() > 0)) {
      lien = lien + "&CD_MARQUE=" + CD_MARQUE;
   }
   if ((PERIME != null) && (PERIME.length() > 0)) {
      lien = lien + "&PERIME=" + PERIME;
   }
   // Affiche toutes les lettres avec un lien permettant de filtrer par cette lettre
   for (char c='A'; Character.isUpperCase(c); c++) { 
      if ((premLettre != null) && (premLettre.charAt(0) == c)) { %>
	 <%=c %>
      <% } else { %>
	 <a href="rechPrest.srv?premLettre=<%= c + lien %>"><%=c %></a>     
   <% }
   }
   if ((premLettre != null) && (premLettre.charAt(0) == ' ')) { %>
	 Tous
      <% } else { %>
	 <a href="rechPrest.srv?premLettre=%20<%= lien %>">Tous</a>     
   <% }
%>
</p>
<i18n:message key="label.typePrest" /> :
<salon:DBselection valeur="<%= CD_TYP_VENT %>" sql="select CD_TYP_VENT, LIB_TYP_VENT from TYP_VENT order by LIB_TYP_VENT">
   <select name="CD_TYP_VENT" onChange="document.fiche.submit()">
      <option value=""><i18n:message key="label.toutesDsListe" /></option>
      %%
   </select>
</salon:DBselection>
<i18n:message key="label.categorieArticle" /> :
<salon:DBselection valeur="<%= CD_CATEG_PREST %>" sql="select CD_CATEG_PREST, LIB_CATEG_PREST from CATEG_PREST order by LIB_CATEG_PREST">
   <select name="CD_CATEG_PREST" onChange="document.fiche.submit()">
      <option value=""><i18n:message key="label.toutesDsListe" /></option>
      %%
   </select>
</salon:DBselection>
<i18n:message key="label.marque" /> :
<salon:DBselection valeur="<%= CD_MARQUE %>" sql="select CD_MARQUE, LIB_MARQUE from MARQUE order by LIB_MARQUE">
   <select name="CD_MARQUE" onChange="document.fiche.submit()">
      <option value=""><i18n:message key="label.toutesDsListe" /></option>
      %%
   </select>
</salon:DBselection>
<i18n:message key="label.affPerime" /> :
<input type="checkbox" name="PERIME"
   <% if ((PERIME != null) && (PERIME.equals("on"))) { %>
   checked 
   <% } %>
   onClick="document.fiche.submit()" >
</form>
<hr>
<table width="100%" border="1" >
	<tr>
		<th><i18n:message key="label.libelle" /></th>
		<th><i18n:message key="label.typePrest" /></th>
		<th><i18n:message key="label.categorie" /></th>
		<th><i18n:message key="label.marque" /></th>
		<th><i18n:message key="label.prixUnitaireTableau" /></th>
	</tr>
	<%
	// Recupère la liste
	Vector lstLignes = (Vector) request.getAttribute("Liste");
	    
	for (int i=0; i< lstLignes.size(); i++) {
	    PrestBean aPrest = (PrestBean) lstLignes.get(i);
	%>
	<tr>
	    
		<td><a href="_FichePrest.jsp?Action=Modification&CD_PREST=<%= aPrest.getCD_PREST() %>" target="ClientFrame"><%= aPrest.toString() %></a></td>
	    <td>
	    <% String LIB_TYP_VENT = TypVentBean.getTypVentBean(mySalon.getMyDBSession(),
							       Integer.toString(aPrest.getCD_TYP_VENT())).toString(); %>
	    <salon:valeur valeur="<%= LIB_TYP_VENT %>" valeurNulle="null"> %% </salon:valeur>
	    </td>
	    <td>
	    <% String LIB_CATEG_PREST = "";
	       if (aPrest.getCD_CATEG_PREST() != 0) {
		  LIB_CATEG_PREST = DonneeRefBean.getDonneeRefBean(mySalon.getMyDBSession(), "CATEG_PREST",
							       Integer.toString(aPrest.getCD_CATEG_PREST())).toString();
	       } %>
	    <salon:valeur valeur="<%= LIB_CATEG_PREST %>" valeurNulle="null"> %%&nbsp; </salon:valeur>
	    </td>
	    <td>
	    <% String LIB_MARQUE = "";
	       if (aPrest.getCD_MARQUE() != 0) {
		  LIB_MARQUE = DonneeRefBean.getDonneeRefBean(mySalon.getMyDBSession(), "MARQUE",
							       Integer.toString(aPrest.getCD_MARQUE())).toString();
	       } %>
	    <salon:valeur valeur="<%= LIB_MARQUE %>" valeurNulle="null"> %%&nbsp; </salon:valeur>
	    </td>
	    <td class="Nombre"><salon:valeur valeur="<%= aPrest.getPRX_UNIT_TTC() %>" valeurNulle="null">%%</salon:valeur></td>
	</tr>
	<%
	}
	%>
</table>
<script language="JavaScript">

// Part en création de prestation
function Nouveau()
{
   parent.location.href = "_FichePrest.jsp";
}

// Affichage de l'aide
function Aide()
{
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideListe.html");
}

</script>
</body>
</html>
