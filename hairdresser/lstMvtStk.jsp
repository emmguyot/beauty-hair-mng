<%@ page import="java.util.Vector,java.util.Date" %>
<%@ page import="com.increg.salon.bean.SalonSession,
                com.increg.salon.bean.ArtBean,
	        com.increg.salon.bean.MvtStkBean,
	        com.increg.salon.bean.FactBean,
	        com.increg.salon.bean.TypMvtBean" %>
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
<title>Liste des mouvements</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees">
<%@ include file="include/commun.js" %>
<script language="JavaScript">
<!--
function Init() {
   <%
   // Positionne les liens d'actions
   %>
   MM_showHideLayers('NOUVEAU?bottomFrame','','hide');
}
//-->
</script>
<%
   // R�cup�ration des param�tres
   String CD_ART = request.getParameter("CD_ART");
   Date DT_DEBUT = (Date) request.getAttribute("DT_DEBUT");
   Date DT_FIN = (Date) request.getAttribute("DT_FIN");
   String CD_TYP_MVT = request.getParameter("CD_TYP_MVT");
%>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/lstMvtStk.gif"></h1>
<form name="fiche" action="rechMvt.srv" method="post">
<p>
Article :
<salon:DBselection valeur="<%= CD_ART %>" sql="select CD_ART, LIB_ART from ART order by LIB_ART">
   <select name="CD_ART" onChange="document.fiche.submit()">
      <option value="">( Tous )</option>
      %%
   </select>
</salon:DBselection>
Type de mouvement :
<salon:DBselection valeur="<%= CD_TYP_MVT %>" sql="select CD_TYP_MVT, LIB_TYP_MVT from TYP_MVT order by LIB_TYP_MVT">
   <select name="CD_TYP_MVT" onChange="document.fiche.submit()">
      <option value="">( Tous )</option>
      %%
   </select>
</salon:DBselection>
</p>
<p>
Entre le :
    <salon:date type="text" name="DT_DEBUT" valeurDate="<%= DT_DEBUT %>" valeurNulle="null" format="dd/MM/yyyy HH:mm:ss" calendrier="true" onchange="document.fiche.submit()">%%</salon:date>
   et le : 
    <salon:date type="text" name="DT_FIN" valeurDate="<%= DT_FIN %>" valeurNulle="null" format="dd/MM/yyyy HH:mm:ss" calendrier="true" onchange="document.fiche.submit()">%%</salon:date>
</p>
</form>
<hr>
<table width="100%" border="1" >
	<tr>
		<th>Date</th>
		<th>Type</th>
		<th>Article</th>
		<th>Quantit�</th>
		<th>Valeur<br>unitaire<br>mouvement</th>
		<th>Stock<br>avant</th>
	</tr>
	<%
	// Recup�re la liste
	Vector lstLignes = (Vector) request.getAttribute("Liste");
	    
	for (int i=0; i< lstLignes.size(); i++) {
	    MvtStkBean aMvt = (MvtStkBean) lstLignes.get(i);
	%>
	<tr>
	    <td class="tabDonnees">
	       <salon:valeur valeurNulle="null" valeur="<%= aMvt.getDT_MVT() %>" >
		  %%
	       </salon:valeur>
	    </td>
	    <td class="tabDonnees">
	    <% String LIB_TYP_MVT = TypMvtBean.getTypMvtBean(mySalon.getMyDBSession(), 
							       Integer.toString(aMvt.getCD_TYP_MVT())).toString(); %>
	    <%= LIB_TYP_MVT %>
            <% if ((aMvt.getCD_FACT() != 0) 
                    && (FactBean.getFactBean(mySalon.getMyDBSession(), Long.toString(aMvt.getCD_FACT()), mySalon.getMessagesBundle()) 
                        != null)){ %>
                <a href="_FicheFact.jsp?Action=Modification&CD_FACT=<%= aMvt.getCD_FACT() %>" target="ClientFrame" title="Fiche facture">
                <img src="images/fact.gif" border=0 align=top></a>
            <% } %>
	    </td>
	    <td class="tabDonnees">
	    <% String LIB_ART = ArtBean.getArtBean(mySalon.getMyDBSession(), 
							       Long.toString(aMvt.getCD_ART()), mySalon.getMessagesBundle()).toString(); %>
	    <a href="_FicheArt_Mvt.jsp?Action=Modification&CD_ART=<%= aMvt.getCD_ART() %>" target="ClientFrame"><%= LIB_ART %></a>
	    </td>
	    <td class="Nombre"><salon:valeur valeur="<%= aMvt.getQTE() %>" valeurNulle="null">%%</salon:valeur></td>
	    <td class="Nombre"><salon:valeur valeur="<%= aMvt.getVAL_MVT_HT() %>" valeurNulle="null">%%</salon:valeur></td>
	    <td class="Nombre"><salon:valeur valeur="<%= aMvt.getSTK_AVANT() %>" valeurNulle="null">%%</salon:valeur></td>
	</tr>
	<%
	}
	%>
</table>
<salon:madeBy />
<script language="JavaScript">
<!--

// Affichage de l'aide
function Aide()
{
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideListe.html");
}

//-->
</script>
</body>
</html>
