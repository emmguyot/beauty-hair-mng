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
