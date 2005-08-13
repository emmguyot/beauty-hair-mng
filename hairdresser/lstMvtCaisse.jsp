<%@ page import="java.util.Vector,java.util.Date" %>
<%@ page import="com.increg.salon.bean.SalonSession,
                com.increg.salon.bean.DonneeRefBean,
	        com.increg.salon.bean.MvtCaisseBean,
	        com.increg.salon.bean.PaiementBean,
	        com.increg.salon.bean.TypMcaBean" %>
<%
    SalonSession mySalon = (SalonSession) session.getAttribute("SalonSession");
    if (mySalon == null) {
        getServletConfig().getServletContext().getRequestDispatcher("/reconnect.html").forward(request, response);
    }
%>
<%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %>
<html>
<head>
<title>Liste des mouvements de caisse</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees">
<%@ include file="include/commun.js" %>
<script language="JavaScript">
<!--
function Init() {
}
//-->
</script>
<%
   // Récupération des paramètres
   String CD_MOD_REGL = request.getParameter("CD_MOD_REGL");
   Date DT_DEBUT = (Date) request.getAttribute("DT_DEBUT");
   Date DT_FIN = (Date) request.getAttribute("DT_FIN");
   String CD_TYP_MCA = request.getParameter("CD_TYP_MCA");
%>
<h1><img src="images/titres/lstMvtCaisse.gif"></h1>
<form name="fiche" action="rechMca.srv" method="post">
<p>
Mode de réglement :
<salon:DBselection valeur="<%= CD_MOD_REGL %>" sql="select CD_MOD_REGL, LIB_MOD_REGL from MOD_REGL order by LIB_MOD_REGL">
   <select name="CD_MOD_REGL" onChange="document.fiche.submit()">
      <option value="">( Tous )</option>
      %%
   </select>
</salon:DBselection>
Type de mouvement :
<salon:DBselection valeur="<%= CD_TYP_MCA %>" sql="select CD_TYP_MCA, LIB_TYP_MCA from TYP_MCA order by LIB_TYP_MCA">
   <select name="CD_TYP_MCA" onChange="document.fiche.submit()">
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
		<th>Mode de réglement</th>
		<th>Montant</th>
		<th>Solde<br>avant</th>
		<th>Commentaire</th>
	</tr>
	<%
	// Recupère la liste
	Vector lstLignes = (Vector) request.getAttribute("Liste");
	    
	for (int i=0; i< lstLignes.size(); i++) {
	    MvtCaisseBean aMvt = (MvtCaisseBean) lstLignes.get(i);
	%>
	<tr>
	    <td class="tabDonnees">
	       <salon:valeur valeurNulle="null" valeur="<%= aMvt.getDT_MVT() %>" >
		  %%
	       </salon:valeur>
	    </td>
	    <td class="tabDonnees">
	    <% String LIB_TYP_MCA = TypMcaBean.getTypMcaBean(mySalon.getMyDBSession(), 
							       Integer.toString(aMvt.getCD_TYP_MCA())).toString(); %>
	    <%= LIB_TYP_MCA %>
            <% if ((aMvt.getCD_PAIEMENT() != 0) 
                    && (PaiementBean.getPaiementBean(mySalon.getMyDBSession(), Long.toString(aMvt.getCD_PAIEMENT()), mySalon.getMessagesBundle()) 
                        != null)) { %>
                <a href="_FichePaiement.jsp?Action=Modification&CD_PAIEMENT=<%= aMvt.getCD_PAIEMENT() %>" target="ClientFrame" title="Fiche paiement">
                <img src="images/fact.gif" border=0 align=top></a>
            <% } %>
	    </td>
	    <td class="tabDonnees">
	    <% String LIB_MOD_REGL = DonneeRefBean.getDonneeRefBean(mySalon.getMyDBSession(), "MOD_REGL",
							       Integer.toString(aMvt.getCD_MOD_REGL())).toString(); %>
	    <%= LIB_MOD_REGL %>
	    </td>
	    <td class="Nombre"><salon:valeur valeur="<%= aMvt.getMONTANT() %>" valeurNulle="null">%%</salon:valeur></td>
	    <td class="Nombre"><salon:valeur valeur="<%= aMvt.getSOLDE_AVANT() %>" valeurNulle="null">%%</salon:valeur></td>
	    <td><salon:valeur valeur="<%= aMvt.getCOMM() %>" valeurNulle="null" expand="true">%%</salon:valeur>&nbsp;</td>
	</tr>
	<%
	}
	%>
</table>
<salon:madeBy />
<script language="JavaScript">
function Nouveau()
{
   parent.location.href = "_FicheMvtCaisse.jsp";
}

// Affichage de l'aide
function Aide()
{
    window.open("aideListe.html");
}

</script>
</body>
</html>
