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
<%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %>
<%@ taglib uri="WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<i18n:bundle baseName="messages" locale="<%= request.getLocale() %>"/>
<html>
<head>
<title>Rechargement de secours de la base</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
<link rel="shortcut icon" href="images/favivon.ico" >
</head>

<body class="donnees">
<h1>Rechargement de secours de la base</h1>
<p class="erreur"><%= request.getAttribute("Erreur") %></p>
<p><span class="warning">Attention : La base de donn�es n'est plus coh�rente.</span> Ceci peut provenir d'un probl�me durant la derni�re restauration de la base. Afin de r�tablir la situation, la base va �tre automatiquement r�initialis�e. <span class="important">Ne jamais interrompre cette proc�dure. Cette proc�dure n'est pas compatible avec une configuration multi-salons, si c'est votre cas, contactez-nous.</span></p>
<p>Une fois cette r�initialisation effectu�e, vous pourrez restaurer depuis Internet ou depuis votre disque pour r�cup�rer vos donn�es.</p>
<p class="important">Si vous n'�tes pas familier avec cette proc�dure, <a href="<%= request.getLocale().getLanguage() %>/contact.html" target="_blank">contactez-nous.</a></p>
<salon:bouton url="restaurationAuto.srv" imgOn="<%= \"images/\" + request.getLocale().getLanguage() + \"/valider2.gif\" %>" img="<%=  \"images/\" + request.getLocale().getLanguage() + \"/valider.gif\" %>" alt="Valider la restauration automatique"/>
</body>
</html>
