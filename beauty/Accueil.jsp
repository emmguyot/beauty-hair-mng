<%@ page import="java.util.Vector,
		  com.increg.salon.bean.SalonSession,
		  com.increg.salon.bean.CollabBean,
		  com.increg.salon.bean.FeteBean,
		  com.increg.salon.bean.PointageBean" %>
<jsp:useBean id="SalonSession" scope="session" class="com.increg.salon.bean.SalonSession" />
<%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %>
<html>
<head>
<title><%=SalonSession.getMySociete().getRAIS_SOC()%></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<%@ include file="include/commun.js" %>
<body class="donnees">
<div id="coinHD" style="position:absolute; height=52px; z-index:1; visibility:hidden"><a class="nohover" href="javascript:top.close()" onMouseOver="document.quit_gif.src='images/quit2.gif'" onMouseOut="document.quit_gif.src='images/quit.gif'"><img name="quit_gif" src="images/quit.gif" border=0 alt="Fermer la fenêtre"></a></div>
<h1 align="center"><%=SalonSession.getMySociete().getRAIS_SOC()%></h1>
<p align="center"><img src="images/perso/Institut.jpg"></p>
<salon:message salonSession="<%= SalonSession %>" />
<form method="post" action="accueilPointage.srv" name="fiche">
<p align="center"><span class="Obligatoire">Collaborateurs pr&eacute;sents :</span>
   <input type="hidden" name="Action" value="Modification">
<%
   Vector lstCollab = (Vector) request.getAttribute("lstCollab");
   Vector lstPointage = (Vector) request.getAttribute("lstPointage");
   Vector lstFete = (Vector) request.getAttribute("lstFete");

   for (int i=0; i < lstCollab.size(); i++) {
      CollabBean aCollab = (CollabBean) lstCollab.get(i);
      PointageBean aPointage = (PointageBean) lstPointage.get(i);

      %>
      <input type="checkbox" name="CD_COLLAB<%= aCollab.getCD_COLLAB() %>" onClick="document.fiche.submit()" 
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
      <%= aCollab.toString() %>&nbsp;&nbsp;
      <%
   }
%>
</p>
</form>
<% if (lstFete.size() > 0) { %>
<p>Aujourd'hui, nous f&ecirc;tons les 
<%   for (int i=0; i < lstFete.size(); i++) { 
      FeteBean aFete = (FeteBean) lstFete.get(i); %>
      <b><%= aFete.getPRENOM() %></b><% if (i != (lstFete.size()-1)) { %>,<% }
     } %>
</p>
<% } %>
<p>
<a href="glossaire.html">Glossaire de l'application</a>
</p>
<salon:include file="include/salonNews.inc" />

<SCRIPT FOR=window EVENT=onscroll LANGUAGE="JScript">
PlaceCoins()
PlaceCoins()
</script>
<SCRIPT FOR=window EVENT=onresize LANGUAGE="JScript">
PlaceCoins()
PlaceCoins()
</script>
<script language="JavaScript">
<!--
PlaceCoins()
PlaceCoins()

function PlaceCoins() {
   document.all["coinHD"].style.left = document.body.clientWidth - 40 + document.body.scrollLeft;
   document.all["coinHD"].style.top = document.body.scrollTop;
   MM_showHideLayers("coinHD","","show");
}

</script>
</body>
</html>
