<%@ page import="java.util.Vector,com.increg.salon.bean.TypVentBean" %>
<%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %>
<html>
<head>
<title>Liste des types de prestations</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
<%@ include file="include/commun.js" %>
<script language="JavaScript">
<!--
function Init() {
   <%
   // Positionne les liens d'actions
   %>
   MM_showHideLayers('NOUVEAU?bottomFrame','','show');
}
//-->
</script>
</head>
<body class="donnees">
<h1><img src="images/titres/lstParam.gif"><br><span class="ssTitre">Types de prestations</span></h1>
<table width="100%" border="1" >
	<tr>
		<th>Libellé</th>
		<th>Article associé</th>
		<th>Civilités associées</th>
	</tr>
	<%
	// Recupère la liste
	Vector lstLignes = (Vector) request.getAttribute("Liste");
	    
	for (int i=0; i< lstLignes.size(); i++) {
	    TypVentBean aTypVent = (TypVentBean) lstLignes.get(i);
	%>
	<tr>
		<td><a href="_FicheTypVent.jsp?Action=Modification&CD_TYP_VENT=<%= aTypVent.getCD_TYP_VENT() %>" target="ClientFrame"><%= aTypVent.toString() %></a></td>
                <td><salon:valeur valeur="<%= aTypVent.getMARQUE() %>" valeurNulle="null">%%</salon:valeur></td>
                <%
                String civilite = aTypVent.getCIVILITE();
                if (civilite != null) {
                    civilite = civilite.replace('|', ' ');
                }
                %>
                <td><salon:valeur valeur="<%= civilite %>" valeurNulle="null">%%&nbsp;</salon:valeur></td>
	</tr>
	<%
	}
	%>
</table>
<script language="JavaScript">
<!--

// Part en création
function Nouveau()
{
   parent.location.href = "_FicheTypVent.jsp";
}

// Affichage de l'aide
function Aide()
{
    window.open("aideListe.html");
}

//-->
</script>
</body>
</html>
