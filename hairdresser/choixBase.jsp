<%@ page import="com.increg.salon.bean.MultiConfigBean" %>
<%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %>
<html>
<head>
<title>Choix de la base à utiliser</title>
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
                                    <p><img src="images/fr/titres/Portail.gif" width="448" height="72" border="0"></p>
                                    <h1>Choix de la base</h1>
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
                                            <p class="obligatoire" align="center">Veuillez choisir la base que vous souhaitez utiliser :</p>
                                            <p class="obligatoire" align="center">
                                                <salon:selection valeur="<%= 0 %>" valeurs="<%= aMCBean.base2Map() %>">
                                                    <select name="numBase">
                                                    %%
                                                    </select>
                                                </salon:selection>		 
                                            <salon:bouton url="javascript:document.base.submit()" imgOn="images/fr/valider2.gif" img="images/fr/valider.gif" alt="Valider" />
                                            </p>
                                        </form>
                                        <div align="center"><font size="-1"><a href="fr/histo.html"><%@ include file="include/version.inc" %></a> &copy; 
                                                2002-2005 Valérie, Alexandre et Emmanuel Guyot<br>Ce logiciel n'offre ABSOLUMENT AUCUNE GARANTIE;<br/> Ce logiciel est gratuit et nous vous encourageons à le redistribuer selon les termes de la <a href="fr/contact.html" target="_new">licence GPL.</a></font></div>
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
