<%@ page import="java.util.Vector" %>
<%@ page import="com.increg.salon.bean.SalonSession,
	       com.increg.salon.bean.PaiementBean,
	       com.increg.salon.bean.FactBean,
	       com.increg.salon.bean.ClientBean,
	       com.increg.salon.bean.DeviseBean,
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
<title>Fiche Paiement</title>
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
   String Etat = (String) request.getAttribute("Etat");
   PaiementBean aPaiement = (PaiementBean) request.getAttribute("PaiementBean");
%>
   var Action="<%=Action%>";

function Init() {
   <%
   // Positionne les liens d'actions
   if (! Action.equals("Creation")) {
      %>
      MM_showHideLayers('SUPPRIMER?bottomFrame','','show');
      <%
   }
   if (Action.equals("Creation")) { %>
      // Pas de lien supprimer
      MM_showHideLayers('SUPPRIMER?bottomFrame','','hide');
   <%
   } %>
   MM_showHideLayers('ENREGISTRER?bottomFrame','','show');
   MM_showHideLayers('IMPRIMER?bottomFrame','','show');
}
//-->
</script>
<h1><img src="images/titres/ficPaiement.gif" alt=<salon:TimeStamp bean="<%= aPaiement %>" />></h1>
<salon:message salonSession="<%= mySalon %>" />
<form method="post" action="ficPaiement.srv" name="fiche">
	<p> 
		<salon:valeur valeurNulle="0" valeur="<%= aPaiement.getCD_PAIEMENT() %>" >
		  <input type="hidden" name="CD_PAIEMENT" value="%%" >
	        </salon:valeur>
		<input type="hidden" name="Action" value="<%=Action%>">
        </p>

	<table width="100%">
	<tr>
	<td class="label"><span class="obligatoire">Mode de paiement</span> : </td>
	<td>
	       <salon:DBselection valeur="<%= aPaiement.getCD_MOD_REGL() %>" sql='<%= "select CD_MOD_REGL, LIB_MOD_REGL from MOD_REGL where UTILISABLE=\'O\' or CD_MOD_REGL=" + Integer.toString(aPaiement.getCD_MOD_REGL()) + " order by LIB_MOD_REGL" %>'>
		  <select name="CD_MOD_REGL">
		     %%
		  </select>
	       </salon:DBselection>
	</td>
	<td class="label"><span class="obligatoire">Date de paiement</span> : </td>
	<td>
	       <salon:valeur valeurNulle="null" valeur="<%= aPaiement.getDT_PAIEMENT() %>" > 
		  <input type="hidden" name="DT_PAIEMENT" size="11" value="%%">
                  <span class="readonly">%%</span>
	       </salon:valeur>
               &nbsp;
	</td>
	</tr>
	<tr>
	<td class="label"><span class="obligatoire">Total :</span></td>
	<td>
	        <salon:valeur valeurNulle="null" valeur="<%= aPaiement.getPRX_TOT_TTC() %>" > 
		     <span class="readonly">%% <%= mySalon.getDevise().toString() %></span>
		</salon:valeur>
               <%
                    // Boucle sur les autres devises
                    Vector lstDevise = mySalon.getLstAutresDevises();
                    for (int i = 0; i < lstDevise.size(); i++) {
                        DeviseBean aDevise = (DeviseBean) lstDevise.get(i);
               %>= 
                        <salon:valeur valeurNulle="null" valeur="<%= aDevise.convertiMontant(aPaiement.getPRX_TOT_TTC()) %>" >
                            <span class="readonly">%% <%= aDevise.toString() %></span>
                        </salon:valeur><%
                    } // for 
                    %>
        </td>
        <td>
            <% if (!Action.equals("Creation")) { 
                  ModReglBean aModRegl = ModReglBean.getModReglBean(mySalon.getMyDBSession(), Integer.toString(aPaiement.getCD_MOD_REGL()));
                  if (aModRegl.getIMP_CHEQUE().equals("O")) { %>
                       <a href="ficChqImpr.jsp?montant=<%= aPaiement.getPRX_TOT_TTC() %>" target="_blank">Impression du chèque</a>
             <%   } else if (aModRegl.getRENDU_MONNAIE().equals("O")) {%>
                       <a href="javascript:calculRendu(<%= aPaiement.getPRX_TOT_TTC() %>)">Rendu de monnaie</a>
                 <%}
 
               } %>
        </td>
	</tr>
	</table>
	<hr>

   <table border="0" width="100%">
	 <tr>
		  <th>&nbsp;</th>
		  <th>Client</th>
		  <th>Date de facture</th>
		  <th>Montant <%= mySalon.getDevise().toString() %></th>
	 </tr>
	 <%
	 // Recupère la liste
	 Vector listeFact = (Vector) request.getAttribute("listeFact");
	 String finClass = "3";
	       
	 for (int i=0; i< listeFact.size(); i++) {
	       FactBean aFact = (FactBean) listeFact.get(i);
	       finClass = (finClass.equals("3")) ? "2" : "3";
	 %>
	 <tr class="ligneTab<%= finClass%>">
	    <td class="tabDonnees">
	       <input type="checkbox" name="AFFECT<%= aFact.getCD_FACT() %>" onClick="Update()"
	       <% if (((aPaiement.getCD_PAIEMENT() != 0) 
			&& (aFact.getCD_PAIEMENT() == aPaiement.getCD_PAIEMENT()) 
			&& (Etat == null)) 
		     || (request.getParameter("AFFECT" + Long.toString(aFact.getCD_FACT())) != null )) { %>
		  checked
	       <% } %>
	       >
	    </td>
	      <td><salon:valeur valeurNulle="null" valeur="<%= ClientBean.getClientBean(mySalon.getMyDBSession(), Long.toString(aFact.getCD_CLI())).toString() %>" > 
				<a href="_FicheCli.jsp?Action=Modification&CD_CLI=<%= aFact.getCD_CLI() %>" target="ClientFrame" title="Fiche client">%%</a> 
				</salon:valeur> </td>
	      <td class="tabDonnees"><salon:valeur valeur="<%= aFact.getDT_PREST() %>" valeurNulle="null"> 
				<a href="_FicheFact.jsp?Action=Modification&CD_FACT=<%= aFact.getCD_FACT() %>" target="ClientFrame" title="Fiche facture">%%</a> 
				</salon:valeur> </td>
	    <td class="label">
	        <salon:valeur valeurNulle="null" valeur="<%= aFact.getPRX_TOT_TTC() %>" > 
		     %%
		</salon:valeur>
	    </td>
	 </tr>
	 <%
	 } // for
	 %>
   </table>
</form>

<div id="PRIX" style="position:absolute; height=20px; z-index:1; visibility:hidden" > 
<p class="label"><% if (mySalon.isAffichePrix()) { %><salon:inverse montant="<%= aPaiement.getPRX_TOT_TTC() %>" /><% } %></p>
</div>

<script language="JavaScript">
// Au chargement de la page : Recharge le menu pour MAJ de la liste des encours
top.MenuFrame.location.href = top.MenuFrame.location.href;
deplacePrix();

// Fonctions d'action
function deplacePrix() {
   document.all["PRIX"].style.left = document.body.clientWidth - 500;
   document.all["PRIX"].style.top = document.body.clientHeight - 55;
   MM_showHideLayers('PRIX','','show');
}

// Mise à jour de la liste avec les totaux
function Update()
{
   document.fiche.Action.value = "Rafraichissement";
   document.fiche.submit();
}

// Fonctions d'action
function Verif()
{
   coche = false;
   for (i in document.fiche.elements) {
      if (i.indexOf("AFFECT") != -1) {
	 coche = coche || (document.fiche.elements[i].checked);
      }
   }
   // Verification des données obligatoires
   if (! coche) {
      alert ("Vous devez cochez au moins une facture.");
      return false;
   }
   return true;
}

// Enregistrement des données du client
function Enregistrer()
{
   if (! Verif()) {
      return;
   }
   document.fiche.submit();
}

// Suppression du client
function Supprimer()
{
   if ((document.fiche.CD_PAIEMENT.value != "0") && (document.fiche.CD_PAIEMENT.value != "")) {
       if (confirm ("Cette suppression annulera le paiement de ces factures. Confirmez-vous cette action ?")) {
          document.fiche.Action.value = "Suppression";
          document.fiche.submit();
       }
   }
}

// Impressions multiples
function Imprimer()
{
   if (! Verif()) {
      return;
   }
   document.fiche.Action.value = "Impression";
   document.fiche.target="_blank";
   document.fiche.submit();
   document.fiche.Action.value = "Modification";
   document.fiche.target="";
}

// Affichage de l'aide
function Aide()
{
    window.open("aideFichePaiement.html");
}

//Ouverture du calcul de rendu
function calculRendu(montant) {
    window.open("ficRenduMonnaie.srv?montant=" + montant + "&montantRegle=" + montant + "&aRendre=", 'Calcul', config='height=250,width=400,left=312,top=234');
}

</script>
</body>
</html>
