<%@ page import="java.util.Vector,
			com.increg.salon.bean.TypVentBean,
			com.increg.salon.bean.TvaBean,
			com.increg.salon.bean.SalonSession" %>
<%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %>
<%
    SalonSession mySalon = (SalonSession) session.getAttribute("SalonSession");
    if (mySalon == null) {
        getServletConfig().getServletContext().getRequestDispatcher("/reconnect.html").forward(request, response);
    }
%>
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
		<th>TVA applicable</th>
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
        <%
        String tva = "";
        if (aTypVent.getCD_TVA() != 0) {
        	tva = TvaBean.getTvaBean(mySalon.getMyDBSession(), Integer.toString(aTypVent.getCD_TVA())).getLIB_TVA();
        }
        %>
        <td><salon:valeur valeur="<%= tva %>" valeurNulle="null">%%</salon:valeur></td>
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
