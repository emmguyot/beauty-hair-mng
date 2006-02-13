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
<%@ page import="com.increg.salon.bean.SalonSession,
	       com.increg.salon.bean.ParamBean" %>
<%
    SalonSession mySalon = (SalonSession) session.getAttribute("SalonSession");
    if (mySalon == null) {
        getServletConfig().getServletContext().getRequestDispatcher("/reconnect.html").forward(request, response);
    }
%>
<%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %>
<%@ taglib uri="WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<i18n:bundle baseName="messages" locale="<%= mySalon.getLangue() %>"/>
<html>
<head>
<%
   // R�cup�ration des param�tres
   String Action = (String) request.getAttribute("Action");
   ParamBean aParam = (ParamBean) request.getAttribute("ParamBean");
%>
<title><i18n:message key="ficParam.title" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees" onLoad="document.fiche.VAL_PARAM.focus()">
<%@ include file="include/commun.jsp" %>
<script language="JavaScript">
<!--
   var Action="<%=Action%>";

function Init() {
   MM_showHideLayers('ENREGISTRER?bottomFrame','','show');
   MM_showHideLayers('RETOUR_LISTE?bottomFrame','','show');
}
//-->
</script>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/ficParam.gif"></h1>
<salon:message salonSession="<%= mySalon %>" />
<form method="post" action="ficParam.srv" name="fiche">
	<p> 
		<salon:valeur valeurNulle="0" valeur="<%= aParam.getCD_PARAM() %>" >
		  <input type="hidden" name="CD_PARAM" value="%%" >
	        </salon:valeur>
		<input type="hidden" name="Action" value="<%=Action%>">
		<span class="obligatoire"><i18n:message key="label.description" /> :</span> 
		<salon:valeur valeurNulle="null" valeur="<%= aParam.getLIB_PARAM() %>" >
                    <input type="hidden" name="LIB_PARAM" value="%%">
                    <span class="readOnly">%%</span>
	        </salon:valeur>
        </p>
	<p>
		<span class="obligatoire"><i18n:message key="label.paramValue" /> :</span> 
		<salon:valeur valeur="<%= aParam.getVAL_PARAM() %>" valeurNulle="null">
                    <% if (aParam.getCD_PARAM() != ParamBean.CD_OP_EXCEPTIONNEL) { %>
                        <input type="text" name="VAL_PARAM" value="%%" size=80>
                    <% } else { %>
                        <input type="password" name="VAL_PARAM" value="%%" size=80></p><p>
                        <span class="obligatoire"><i18n:message key="label.paramValueVerif" /> :</span> 
                        <input type="password" name="VAL_PARAM2" value="%%" size=80>
                    <% } %></td>
		</salon:valeur>
	</p>
</form>
<script language="JavaScript">
// Fonctions d'action

// Enregistrement des donn�es du client
function Enregistrer()
{
   // Verification des donn�es obligatoires
   if (document.fiche.VAL_PARAM.value == "") {
      alert ("<i18n:message key="ficParam.valeurManquante" />");
      return;
   }
   document.fiche.submit();
}

// Affichage de l'aide
function Aide()
{
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideFicheParam.html");
}

function RetourListe()
{
    parent.location.href = "ListeParam.jsp";
}


</script>
</body>
</html>
