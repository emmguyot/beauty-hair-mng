<%@ page import="com.increg.salon.bean.SalonSession" %>
<%
    SalonSession mySalon = (SalonSession) session.getAttribute("SalonSession");
    if (mySalon == null) {
        getServletConfig().getServletContext().getRequestDispatcher("/reconnect.html").forward(request, response);
    }
%>
<html>
<head>
<title><%=mySalon.getMySociete().getRAIS_SOC()%></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
<link rel="shortcut icon" href="images/favicon.ico" >
<script language="JavaScript">
function checkAndSubmit()
{
   if (document.cnx.MOT_PASSE.value == "") {
		alert ("Le mot de passe est obligatoire pour utiliser l'application.");
   }
   else {
		document.cnx.submit();
   }
}
</script>
</head>

<body class="donnees" onLoad="cnx.MOT_PASSE.focus()">
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
                                    <p><img src="images/titres/Portail.gif" width="448" height="72" border="0"></p>
                                    <h1><%=mySalon.getMySociete().getRAIS_SOC()%></h1>
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
                                        <form method="post" action="ident.srv" name="cnx">
                                            <p class="obligatoire" align="center">Merci de 
                                                vous identifier pour utiliser l'application.</p>
                                            <p class="obligatoire" align="center">Mot de 
                                                passe : 
                                                <input type="password" name="MOT_PASSE" maxlength="20">
                                            <a class="nohover" href="javascript:checkAndSubmit()" onMouseOver="document.valider_gif.src='images/valider2.gif'" onMouseOut="document.valider_gif.src='images/valider.gif'"><img name="valider_gif" src="images/valider.gif" border=0 alt="Valider" align="middle"></a>
                                                <% if ((mySalon.getMessage("Erreur") != null) 
            && (mySalon.getMessage("Erreur").length() > 0)) { %>
                                            </p>
                                            <p class="erreur" align="center"><%= mySalon.getMessage("Erreur") %> 
                                                <% } %>
                                            </p>
                                        </form>
                                        <div align="center"><font size="-1"><a href="histo.html"><%@ include file="include/version.inc" %></a> &copy; 
                                                2002-2005 Valérie, Alexandre et Emmanuel Guyot<br>Ce logiciel n'offre ABSOLUMENT AUCUNE GARANTIE;<br/> Ce logiciel est gratuit et nous vous encourageons à le redistribuer selon les termes de la <a href="contact.html" target="_new">licence GPL.</a></font></div>
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
