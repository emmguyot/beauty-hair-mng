<%@ page import="java.util.Vector" %>
<%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %>
<html>
<head>
<%
   // R�cup�ration des param�tres
   String Erreur = (String) request.getAttribute("Erreur");
   String Info = (String) request.getAttribute("Info");
%>
<title>Rechargement de secours de la base</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees">
<%@ include file="include/commun.js" %>
<h1>Rechargement de secours de la base</h1>
<% if (Erreur != null) { %>
   <p class="Erreur"><%= Erreur %></p>
<% } 
   if (Info != null) { %>
   <p class="Info"><%= Info %></p>
<% } %>

<p>
Pour terminer cette restauration de secours, vous devez arr�ter le logiciel (ic�ne "feu rouge") et le relancer (ic�ne "feu vert").
</p>

</script>
</body>
</html>
