<%
/*
 * This program is part of InCrEG LibertyLook software http://beauty-hair-mng.sourceforge.net
 * Copyright (C) 2001-2009 Emmanuel Guyot <See emmguyot on SourceForge> 
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
<%@ page import="java.util.Vector,java.math.BigDecimal,java.util.Calendar" %>
<%@ page import="com.increg.salon.request.CA
	       " %>
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
<title><i18n:message key="label.ca" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
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
   String CD_COLLAB = request.getParameter("CD_COLLAB");
   Calendar DT_DEBUT = (Calendar) request.getAttribute("DT_DEBUT");
   Calendar DT_FIN = (Calendar) request.getAttribute("DT_FIN");
%>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/lstCA.gif"></h1>
<form name="fiche" action="rechCA.srv" method="post">
<p>
<i18n:message key="label.collaborateur" /> :
<salon:DBselection valeur="<%= CD_COLLAB %>" sql="select CD_COLLAB, PRENOM from COLLAB order by PRENOM, NOM">
   <select name="CD_COLLAB" onChange="document.fiche.submit()">
      <option value=""><i18n:message key="label.tousDsListe" /></option>
      %%
   </select>
</salon:DBselection>
</p>
<p>
<i18n:message key="label.entreLe" /> :
    <i18n:message key="format.dateSimpleDefaut" id="formatDate" />
    <salon:date type="text" name="DT_DEBUT" valeurDate="<%= DT_DEBUT %>" valeurNulle="null" format="<%= formatDate %>" calendrier="true" onchange="document.fiche.submit()">%%</salon:date>
<i18n:message key="label.etLe" /> : 
    <salon:date type="text" name="DT_FIN" valeurDate="<%= DT_FIN %>" valeurNulle="null" format="<%= formatDate %>" calendrier="true" onchange="document.fiche.submit()">%%</salon:date>
</p>
</form>
<hr>
<%
// Recupère les listes
Vector lstTypes = (Vector) request.getAttribute("ListeType");
Vector lstLignes = (Vector) request.getAttribute("Liste");
%>
<table width="100%" border="1" rules="groups">
<colgroup>
<colgroup>
<colgroup span="<%= lstTypes.size() %>">
<colgroup>
	<tr>
		<th><i18n:message key="label.mois" /></th>
		<th><i18n:message key="label.collaborateur" /></th>
		<% for (int i=0; i< lstTypes.size(); i++) { %>
		  <th><%= (String) lstTypes.get(i) %></th>
	        <% } %>
		<th><i18n:message key="label.caTotalHorsRemise" /></th>
	</tr>
	<tbody>
	<%    
    BigDecimal fullTotal = new BigDecimal(0);
    BigDecimal fullTotalType[] = new BigDecimal[lstTypes.size()];
	for (int j=0; j < lstTypes.size(); j++) {
		fullTotalType[j] = new BigDecimal(0);
	}
	for (int i=0; i< lstLignes.size(); ) {
	    CA aCA = (CA) lstLignes.get(i);
	%>
	<tr>
	    <td class="tabDonnees">
	    <% 
		  java.text.SimpleDateFormat formatDate2  = new java.text.SimpleDateFormat("MMMM yyyy");
		  String mois = formatDate2.format(aCA.getDT_PREST().getTime());
		  Calendar DT_PREST_orig = aCA.getDT_PREST();
          String PRENOM_orig = aCA.getPRENOM();
	    %>
	       <salon:valeur valeurNulle="null" valeur="<%= mois %>" >
		     %%
	       </salon:valeur>
	    </td>
	    <td class="tabDonnees">
	       <salon:valeur valeurNulle="null" valeur="<%= aCA.getPRENOM() %>" >
		     %%
	       </salon:valeur>
	    </td>
	    <%
	    BigDecimal total = new BigDecimal(0);
	    total.setScale(2);
	    for (int j=0; j< lstTypes.size(); j++) { %>
	       <td class="Nombre">
		  
	       <%
	       if (DT_PREST_orig.equals(aCA.getDT_PREST())
                    && PRENOM_orig.equals(aCA.getPRENOM())
					&& (aCA.getLIB_TYP_VENT() != null) 
					&& (aCA.getLIB_TYP_VENT().equals((String) lstTypes.get(j)))) { %>
			  <salon:valeur valeurNulle="null" valeur="<%= aCA.getMONTANT() %>" >
			     %%
			  </salon:valeur>
	       <%
	          if (aCA.getMONTANT() != null) {
			    total = total.add(aCA.getMONTANT());
		      }
      		  fullTotalType[j] = fullTotalType[j].add(aCA.getMONTANT());
		      
		      i++;
		      if (i < lstLignes.size()) {
			    aCA = (CA) lstLignes.get(i);
		      }
		      else {
			    aCA = new CA();
		      }
		   }
		   else { %>
		      &nbsp;
	       <%
	       } %>
	       </td>
	    <%
	    } 
        fullTotal = fullTotal.add(total);
        %>
	    <td class="Nombre">
	       <%= total %>
	    </td>
	</tr>
	<%
	}
	%>
	</tbody>
        <tfoot>
        <tr>
            <td colspan="2" class="Total"><i18n:message key="label.total" /></td>
            <%
			for (int j=0; j < lstTypes.size(); j++) {
			%>
            <td class="Nombre">
			  <salon:valeur valeurNulle="null" valeur="<%= fullTotalType[j] %>" >
			     %%
			  </salon:valeur>
            </td>
            <%
			}
			%>
            <td class="Nombre">
                <salon:valeur valeur="<%= fullTotal %>" valeurNulle="null"> %% </salon:valeur>
            </td>
        </tr>
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
