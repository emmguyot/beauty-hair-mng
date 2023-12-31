<%
/*
 * This program is part of InCrEG LibertyLook software http://beauty-hair-mng.sourceforge.net
 * Copyright (C) 2001-2022 Emmanuel Guyot <See emmguyot on SourceForge> 
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
		  com.increg.salon.bean.SalonSession,
		  com.increg.salon.bean.CollabBean,
		  com.increg.salon.bean.FeteBean,
		  com.increg.salon.bean.PointageBean,
		  com.increg.salon.bean.ClientBean" %>
<%
SalonSession mySalon = com.increg.salon.servlet.ConnectedServlet.CheckOrGoHome(request, response);
%>
<%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %>
<%@ taglib uri="WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<i18n:bundle baseName="messages" locale="<%= mySalon.getLangue() %>"/>
<html>
<head>
<title><%=mySalon.getMySociete().getRAIS_SOC()%></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
<link rel="stylesheet" href="style/jquery-ui-1.8.16.custom.css" type="text/css">
</head>
<%@ include file="include/commun.jsp" %>
<body class="donnees">
<i18n:message key="message.fermer" id="msgFermer" />
<div id="coinHD" style="position:absolute; height:52px; z-index:1; visibility:hidden"><salon:bouton url="javascript:top.close()" imgOn="images/quit2.gif" img="images/quit.gif" alt="<%= msgFermer %>"/></div>
<h1 align="center"><%=mySalon.getMySociete().getRAIS_SOC()%></h1>
<p align="center"><img src="../perso/Institut.jpg"></p>
<salon:message salonSession="<%= mySalon %>" />
<form method="post" action="accueilPointage.srv" name="fiche">
<p align="center"><span class="Obligatoire"><i18n:message key="accueil.collabPresent" /> :</span>
   <input type="hidden" name="Action" value="Modification">
<%
   Vector lstCollab = (Vector) request.getAttribute("lstCollab");
   Vector lstPointage = (Vector) request.getAttribute("lstPointage");
   Vector lstFete = (Vector) request.getAttribute("lstFete");
   List<ClientBean> lstAnniv = (List<ClientBean>) request.getAttribute("lstAnniv");

   for (int i=0; i < lstCollab.size(); i++) {
      CollabBean aCollab = (CollabBean) lstCollab.get(i);
      PointageBean aPointage = (PointageBean) lstPointage.get(i);

      %>
      <input type="checkbox" name="CD_COLLAB<%= aCollab.getCD_COLLAB() %>" id="CD_COLLAB<%= aCollab.getCD_COLLAB() %>" onClick="document.fiche.submit()" 
      <% if ((aPointage != null) 
	       && (aPointage.getDT_DEBUT() != null) 
	       &&(aPointage.getDT_FIN() == null) 
	       && (aPointage.getCD_TYP_POINTAGE() == PointageBean.TYP_PRESENCE)) { %>
	 checked
      <% } %>
      <% if ((aPointage != null) 
	       && (aPointage.getDT_DEBUT() != null) 
	       && (aPointage.getDT_FIN() != null)) { %>
	 disabled
      <% } %>
      >
      <label for="CD_COLLAB<%= aCollab.getCD_COLLAB() %>"><%= aCollab.toString() %></label>&nbsp;&nbsp;
      <%
   }
%>
</p>
</form>
<% if (lstFete.size() > 0) { %>
<p class="listItem"><i18n:message key="accueil.feteJour" /><ul class="listItem">
<%   for (int i=0; i < lstFete.size(); i++) { 
      FeteBean aFete = (FeteBean) lstFete.get(i); 
      %><li><b><%= aFete.getPRENOM() %></b></li><%
   	} %>
     </ul>
</p>
<% } %>
<% if (lstAnniv.size() > 0) { %>
<p class="listItem"><i18n:message key="accueil.annivJour" /><ul class="listItem">
<%   for (ClientBean cli : lstAnniv) { 
		%><li><b><a href="_FicheCli.jsp?Action=Modification&CD_CLI=<%= cli.getCD_CLI() %>" target="ClientFrame"><%= cli.toString() %></a></b></li><%
	} %>
	</ul>
</p>
<% } %>
<p>
<a href="<%= mySalon.getLangue().getLanguage() %>/glossaire.html"><i18n:message key="accueil.glossaire" /></a>
</p>
<salon:include file="<%= \"include/\" + mySalon.getLangue().getLanguage() + \"/salonNews.inc\" %>" />

<script language="JavaScript">
<!--
Place2Coins();
window.onresize = Place2Coins;
window.onscroll = Place2Coins;

function Place2Coins() {
    PlaceCoins();
    PlaceCoins();
}

function PlaceCoins() {
   MM_findObj("coinHD").style.left = document.body.clientWidth - 40 + document.body.scrollLeft;
   MM_findObj("coinHD").style.top = document.body.scrollTop;
   MM_showHideLayers("coinHD","","show");
}

</script>
</body>
</html>
