<%@ page import="java.util.Vector" %>
<%@ page import="com.increg.salon.bean.SalonSession,
	       com.increg.salon.bean.MvtCaisseBean,
	       com.increg.salon.bean.CaisseBean,
	       com.increg.salon.bean.ModReglBean
	       " %>
<%
    SalonSession mySalon = (SalonSession) session.getAttribute("SalonSession");
    if (mySalon == null) {
        getServletConfig().getServletContext().getRequestDispatcher("/reconnect.html").forward(request, response);
    }
%>
<%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %>
<html>
<head>
<title>Fiche mouvement de caisse</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees" onLoad="Init();document.fiche.CD_MOD_REGL.focus()">
<%@ include file="include/commun.js" %>
<script language="JavaScript">
<!--
<%
   // Récupération des paramètres
   String Action = (String) request.getAttribute("Action");
   MvtCaisseBean aMvt = (MvtCaisseBean) request.getAttribute("MvtCaisseBean");
   Vector listeCaisse = (Vector) request.getAttribute("listeCaisse");
%>
   var Action="<%=Action%>";

function Init() {
   <%
   // Positionne les liens d'actions
   if (! Action.equals("Creation")) {
      %>
      MM_showHideLayers('SUPPRIMER?bottomFrame','','hide');
      MM_showHideLayers('ENREGISTRER?bottomFrame','','show');
      <%
   }
   if (Action.equals("Creation")) { %>
      // Pas de lien supprimer
      MM_showHideLayers('SUPPRIMER?bottomFrame','','hide');
      MM_showHideLayers('ENREGISTRER?bottomFrame','','show');
   <%
   } %>
}
//-->
</script>
<h1><img src="images/titres/ficMvtCaisse.gif" alt=<salon:TimeStamp bean="<%= aMvt %>" />></h1>
<salon:message salonSession="<%= mySalon %>" />
<form method="post" action="ficMvtCaisse.srv" name="fiche">
	<p> 
		<input type="hidden" name="Action" value="<%=Action%>">
		<span class="obligatoire">Date du mouvement :</span> 
	        <salon:valeur valeurNulle="null" valeur="<%= aMvt.getDT_MVT() %>" >
		  <input type="hidden" name="DT_MVT" value="%%">
		  <span class="readonly">%%</span>
	        </salon:valeur>
	 </p>
	 <p>
	       <span class="obligatoire">Mode de réglement :</span> 
	       <salon:DBselection valeur="<%= aMvt.getCD_MOD_REGL() %>" sql='<%= "select CD_MOD_REGL, LIB_MOD_REGL from MOD_REGL where CD_MOD_REGL not in (" + Integer.toString(ModReglBean.MOD_REGL_ESP_FRF) + "," + Integer.toString(ModReglBean.MOD_REGL_CHQ_FRF) + ") order by LIB_MOD_REGL"%>'>
		     <select name="CD_MOD_REGL" onchange="ChangeMvt()">
			%%
		     </select>
	       </salon:DBselection>
	       Solde de la caisse : 
	       <input class="Nombre" type="text" name="SOLDE_AVANT" disabled value="" size=7 onchange="ChangeMvt()">
	       <%= mySalon.getDevise().toString() %>
	 </p>
	 <p>
	       <span class="obligatoire">Type de mouvement :</span> 
	       <salon:DBselection valeur="<%= aMvt.getCD_TYP_MCA() %>" sql="select CD_TYP_MCA, LIB_TYP_MCA || ' (' || SENS_MCA::varchar || ')' from TYP_MCA order by LIB_TYP_MCA">
		     <select name="CD_TYP_MCA" onchange="ChangeMvt()">
			%%
		     </select>
	       </salon:DBselection>
	 </p>
	 <p>
	       <span class="obligatoire">Montant :</span> 
	       <salon:valeur valeurNulle="null" valeur="<%= aMvt.getMONTANT() %>" >
		  <input class="Nombre" type="text" name="MONTANT" value="%%" size="8">
	       </salon:valeur>
	       <%= mySalon.getDevise().toString() %>
	</p>
	<p>
	 <span class="facultatif">Commentaire :</span> 
	    <salon:valeur valeurNulle="null" valeur="<%= aMvt.getCOMM() %>" >
		<textarea name="COMM" cols="40" rows="2">%%</textarea>
	    </salon:valeur>
	</p>
</form>

<script language="JavaScript">
// Fonctions d'action
ChangeMvt();

// Affiche le solde en fonction du mode de réglement selectionné
function ChangeMvt() {

CD_MOD_REGL = new Array();
SOLDE = new Array();
   
   <%
   for (int i = 0; i < listeCaisse.size(); i++) {
      CaisseBean aCaisse = (CaisseBean) listeCaisse.get(i);

      %>
      CD_MOD_REGL[<%= i %>] = <%= aCaisse.getCD_MOD_REGL() %>;
      SOLDE[<%= i %>] = <%= aCaisse.getSOLDE() %>;
      <%
   }
   %>
   for (i = 0; i < document.fiche.CD_MOD_REGL.options.length; i++) {
      if (document.fiche.CD_MOD_REGL.options[i].selected) {
	 for (k = 0; k < CD_MOD_REGL.length; k++) {
	    if (CD_MOD_REGL[k] == document.fiche.CD_MOD_REGL.options[i].value) {
	       document.fiche.SOLDE_AVANT.value = SOLDE[k];
	    }
	 }
      }
   }
}

// Contrôle des données avant enregistrement
function ControleEnreg ()
{
   // Verification des données obligatoires
   if (document.fiche.MONTANT.value == "") {
      alert ("Le montant doit être saisi. L'enregistrement n'a pas pu avoir lieu.");
      return false;
   }
   return true;
}

// Enregistrement des données du client
function Enregistrer()
{
   // Verification des données obligatoires
   if (! ControleEnreg()) {
      return;
   }
   document.fiche.submit();
}

// Affichage de l'aide
function Aide()
{
    window.open("aideFiche.html");
}

</script>
</body>
</html>
