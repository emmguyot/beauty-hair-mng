<%
/*
 * This program is part of InCrEG LibertyLook software http://beauty-hair-mng.sourceforge.net
 * Copyright (C) 2001-2013 Emmanuel Guyot <See emmguyot on SourceForge> 
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
<%@ page import="com.increg.salon.bean.SalonSession
	       " %>
<%
SalonSession mySalon = com.increg.salon.servlet.ConnectedServlet.CheckOrGoHome(request, response);
%>
<%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %>
<%@ taglib uri="WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<i18n:bundle baseName="messages" locale="<%= request.getLocale() %>"/>
<html>
<head>
<title><i18n:message key="reload.title" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
<link rel="stylesheet" href="style/jquery-ui-1.8.16.custom.css" type="text/css">
<link rel="shortcut icon" href="images/favivon.ico" >
</head>

<body class="donnees">
<h1><i18n:message key="reload.title" /></h1>
<p class="erreur"><%= request.getAttribute("Erreur") %></p>
<p><span class="warning"><i18n:message key="reload.messageWarning" /></span> <i18n:message key="reload.messageWarning_P2" /><span class="important"><i18n:message key="reload.messageWarning_P3" /></span></p>
<p><i18n:message key="reload.messageWarning_P4" /></p>
<p class="important"><i18n:message key="reload.messageWarning_P5" /><a href="<%= request.getLocale().getLanguage() %>/contact.html" target="_blank"><i18n:message key="reload.messageWarning_P6" /></a></p>
<p><i18n:message key="reload.messageWarning_P7" /><a href="initPortail.srv?forceSequence=1"><i18n:message key="reload.messageWarning_P8" /></a>. <span class="important"><i18n:message key="reload.messageWarning_P9" /></span></p>
<i18n:message key="reload.boutonValider" id="paramBouton" />
<salon:bouton url="restaurationAuto.srv" imgOn="<%= \"images/\" + request.getLocale().getLanguage() + \"/valider2.gif\" %>" img="<%=  \"images/\" + request.getLocale().getLanguage() + \"/valider.gif\" %>" alt="<%= paramBouton %>"/>
</body>
</html>
