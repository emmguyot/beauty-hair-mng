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
<%@ page import="com.increg.salon.bean.MultiConfigBean" %>
<%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %>
<%@ taglib uri="WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<i18n:bundle baseName="messages" locale="<%= request.getLocale() %>"/>
<html>
<head>
<title><i18n:message key="title.choixBase" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
<link rel="shortcut icon" href="images/favivon.ico" >
</head>
<%
    MultiConfigBean aMCBean = (MultiConfigBean) request.getAttribute("MultiConfigBean");
%>
<body class="donnees">
<div align="center">
    <table width="469" border="0" cellpadding="0" cellspacing="0">
        <tr>
            <td height="50">
            </td>
        </tr>
    </table>
    <table width="469" border="1" cellpadding="0" cellspacing="0" bordercolor="#000000">
        <tr> 
	    <td height="526" width="469" valign="top"> 
                <table width="469" border="0" cellpadding="0" cellspacing="0" bordercolor="#000000">
                    <tr> 
                        <td height="291" width="469" valign="top" class="action" align="center"> 
                                    <p>&nbsp;</p>
                                    <p><img src="images/<%= request.getLocale().getLanguage() %>/titres/Portail.gif" width="448" height="72" border="0"></p>
                                    <h1><i18n:message key="label.choixBase" /></h1>
                                    <p><img src="images/perso/Logo.gif" width="130" height="68" border="0"></p>
                                    <p>&nbsp;</p>
                        </td>
                    </tr>
                    <tr>
                        <td height="62"></td>
                    </tr>
                    <tr> 
                        <td height="173"> 
                            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                <tr> 
                                    <td width="469" height="173" valign="top" bordercolor="#000000"> 
                                        <form method="post" action="initPortail.srv" name="base">
                                            <p class="obligatoire" align="center"><i18n:message key="label.choixBaseAUtiliser" /> :</p>
                                            <p class="obligatoire" align="center">
                                                <salon:selection valeur="<%= 0 %>" valeurs="<%= aMCBean.base2Map() %>">
                                                    <select name="numBase">
                                                    %%
                                                    </select>
                                                </salon:selection>		 
                                            <i18n:message key="bouton.Valider" id="paramBouton2" />
                                            <salon:bouton url="javascript:document.base.submit()" imgOn="<%= \"images/\" + request.getLocale().getLanguage() + \"/valider2.gif\" %>" img="<%= \"images/\" + request.getLocale().getLanguage() + \"/valider.gif\" %>" alt="<%= paramBouton2 %>" />
                                            </p>
                                        </form>
                                        <div align="center"><font size="-1"><a href="<%= request.getLocale().getLanguage() %>/histo.html"><%@ include file="include/version.inc" %></a> &copy; 
                                                <i18n:message key="message.copyrightLicence" /></font></div>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</div>
</body>
</html>
