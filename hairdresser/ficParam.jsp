<%@ page import="com.increg.salon.bean.SalonSession,
	       com.increg.salon.bean.ParamBean" %>
<%
    SalonSession mySalon = (SalonSession) session.getAttribute("SalonSession");
    if (mySalon == null) {
        getServletConfig().getServletContext().getRequestDispatcher("/reconnect.html").forward(request, response);
    }
%>
<%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %>
<html>
<head>
<%
   // R�cup�ration des param�tres
   String Action = (String) request.getAttribute("Action");
   ParamBean aParam = (ParamBean) request.getAttribute("ParamBean");
%>
<title>Fiche param�tre de l'application</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees" onLoad="document.fiche.VAL_PARAM.focus()">
<%@ include file="include/commun.js" %>
<script language="JavaScript">
<!--
   var Action="<%=Action%>";

function Init() {
   MM_showHideLayers('ENREGISTRER?bottomFrame','','show');
}
//-->
</script>
<h1><img src="images/titres/ficParam.gif"></h1>
<salon:message salonSession="<%= mySalon %>" />
<form method="post" action="ficParam.srv" name="fiche">
	<p> 
		<salon:valeur valeurNulle="0" valeur="<%= aParam.getCD_PARAM() %>" >
		  <input type="hidden" name="CD_PARAM" value="%%" >
	        </salon:valeur>
		<input type="hidden" name="Action" value="<%=Action%>">
		<span class="obligatoire">Description :</span> 
		<salon:valeur valeurNulle="null" valeur="<%= aParam.getLIB_PARAM() %>" >
                    <input type="hidden" name="LIB_PARAM" value="%%">
                    <span class="readOnly">%%</span>
	        </salon:valeur>
        </p>
	<p>
		<span class="obligatoire">Valeur du param�tre :</span> 
		<salon:valeur valeur="<%= aParam.getVAL_PARAM() %>" valeurNulle="null">
                    <% if (aParam.getCD_PARAM() != ParamBean.CD_OP_EXCEPTIONNEL) { %>
                        <input type="text" name="VAL_PARAM" value="%%" size=80>
                    <% } else { %>
                        <input type="password" name="VAL_PARAM" value="%%" size=80></p><p>
                        <span class="obligatoire">Valeur du param�tre (pour v�rification) :</span> 
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
      alert ("La valeur doit �tre saisie. L'enregistrement n'a pas pu avoir lieu.");
      return;
   }
   document.fiche.submit();
}

// Affichage de l'aide
function Aide()
{
    window.open("aideFicheParam.html");
}

</script>
</body>
</html>
