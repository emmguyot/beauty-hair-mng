<%@ page import="java.util.Vector" %>
<%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %>
<html>
<head>
<%
   // Récupération des paramètres
   String Action = (String) request.getAttribute("Action");
   String Erreur = (String) request.getAttribute("Erreur");
   String Info = (String) request.getAttribute("Info");
%>
<title>Mise à jour</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees">
<%@ include file="include/commun.js" %>
<script language="JavaScript">
<!--
   var Action="<%=Action%>";

   MM_showHideLayers('VALIDER?bottomFrame','','hide');
//-->
</script>
<h1>Mise à jour du logiciel</h1>
<% if (Erreur != null) { %>
   <p class="Erreur"><%= Erreur %></p>
<% } 
   if (Info != null) { %>
   <p class="Info"><%= Info %></p>
<% } %>

<p>
Pour terminer cette mise à jour, vous devez arrêter le logiciel (icône "feu rouge") et le relancer (icône "feu vert").
</p>

</script>
</body>
</html>
