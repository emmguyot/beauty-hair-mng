<%
/*
 * This program is part of InCrEG LibertyLook software http://beauty-hair-mng.sourceforge.net
 * Copyright (C) 2001-2011 Emmanuel Guyot <See emmguyot on SourceForge> 
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
<%@ page import="java.text.SimpleDateFormat,java.util.Date" %>
<%@ page import="com.increg.salon.bean.SalonSession,com.increg.util.Montant" %>
<%
SalonSession mySalon = com.increg.salon.servlet.ConnectedServlet.CheckOrGoHome(request, response);
%>
<%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %>
<%@ taglib uri="WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<i18n:bundle baseName="messages" locale="<%= mySalon.getLangue() %>"/>
<html>
<head>
<title>Chèque</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="corpsImpr" onLoad="Imprimer()">
<%@ include file="include/commun.jsp" %>
<%
   // Récupération des paramètres
   String montant = request.getParameter("montant");
%>

   
<table class="chequeImpr" width="400">
   <tr> 
      <td height="31" colspan="6">&nbsp;</td>
   </tr>
   <tr> 
      <td width="7" height="36"></td>
      <td width="84"></td>
      <td width="368" valign="top"><%
            String montantEnLettre = new Montant(montant).toLetters(mySalon.getDevise().getLIB_DEVISE());
            // Premier lettre en majuscule
            montantEnLettre = montantEnLettre.substring(0,1).toUpperCase() + montantEnLettre.substring(1);
            %><%= montantEnLettre %></td>
      <td width="10"></td>
      <td width="14"></td>
      <td width="138"></td>
   </tr>
   <tr> 
      <td height="27"></td>
      <td colspan="2" valign="top"><%= mySalon.getMySociete().getRAIS_SOC() %></td>
      <td></td>
      <td colspan="2" rowspan="2" valign="top"><%= new Montant(montant).setScale(2) %></td>
   </tr>
   <tr> 
      <td height="12"></td>
      <td></td>
      <td></td>
      <td></td>
   </tr>
   <tr> 
      <td height="38"></td>
      <td></td>
      <td></td>
      <td></td>
      <td></td>
      <td valign="top"><%= mySalon.getMySociete().getVILLE() %><br>
         <%= new SimpleDateFormat("dd/MM/yyyy").format(new Date()) %></td>
   </tr>
   <tr> 
      <td height="147"></td>
      <td></td>
      <td></td>
      <td></td>
      <td></td>
      <td></td>
   </tr>
</table>
<script language="JavaScript">

// Impression du ticket
function Imprimer()
{
   window.print();
   var obj_window = window.open('', '_self');
   obj_window.opener = window;
   obj_window.focus();
   opener=self;
   self.close();
}

</script>
</body>
</html>
