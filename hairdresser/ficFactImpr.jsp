<%@ page import="java.util.Vector,
	       java.math.BigDecimal" %>
<%@ page import="com.increg.salon.bean.SalonSession,
                com.increg.salon.bean.FactBean,
	        com.increg.salon.bean.PaiementBean,
	        com.increg.salon.bean.HistoPrestBean,
	        com.increg.salon.bean.ClientBean,
	        com.increg.salon.bean.PrestBean,
	        com.increg.salon.bean.SocieteBean,
	        com.increg.salon.bean.DonneeRefBean,
	        com.increg.salon.bean.DeviseBean,
	        com.increg.salon.request.EditionFacture,
	        com.increg.salon.bean.CollabBean" %>
<%
    SalonSession mySalon = (SalonSession) session.getAttribute("SalonSession");
    if (mySalon == null) {
        getServletConfig().getServletContext().getRequestDispatcher("/reconnect.html").forward(request, response);
    }
%>
<%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %>
<html>
<head>
<title>Fiche Facture</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="corpsImpr" onLoad="Imprimer()">
<%@ include file="include/commun.js" %>
<%
   // Récupération des paramètres
   Vector listeEdition = (Vector) request.getAttribute("listeEdition");
%>

<% for (int iListe=0; iListe < listeEdition.size(); iListe++) { 
      FactBean aFact = ((EditionFacture) listeEdition.get(iListe)).getMyFact();
      PaiementBean aPaiement = ((EditionFacture) listeEdition.get(iListe)).getMyPaiement();
      String totPrest = aFact.getTotPrest(mySalon.getMyDBSession()).toString();
%>
   <table class="ficheImpr" style="{ width: <%= mySalon.getLargeurFiche() %> }" width="400">
   <tr>
   <td>
	 <table width="100%"> 
	 <tr>
	 <td class="tabDonnees" colspan="2">
	       <% SocieteBean mySoc = mySalon.getMySociete(); %>
	       <font size=+1><b><%= mySoc.toString() %></b></font><br>
		  <salon:valeur valeurNulle="null" valeur="<%= mySoc.getRUE() %>" expand="true">
		     %%
		  </salon:valeur><br>
		  <salon:valeur valeurNulle="null" valeur="<%= mySoc.getCD_POSTAL() %>" >
		     %%
		  </salon:valeur>
		  <salon:valeur valeurNulle="null" valeur="<%= mySoc.getVILLE() %>" >
		     %%
		  </salon:valeur><br>
		  <salon:valeur valeurNulle="null" valeur="<%= mySoc.getTEL() %>" >
		     Tél : %%
		  </salon:valeur>
	 </td>
	 </tr>
	 <tr>
	 <td>
	 <%
	 	CollabBean collab = CollabBean.getCollabBean(mySalon.getMyDBSession(), Integer.toString(aFact.getCD_COLLAB()));
	 	if (collab.getCIVILITE().equals("M. ")) {
	 %>
	 		Coiffeur
	 <%
	 	}
	 	else {
	 %>
	 		Coiffeuse
	 <%
	 	}
	 %> : <%= collab.toString() %>
	 </td>
	 <td>Le :
		  <salon:valeur valeurNulle="null" valeur="<%= aFact.getDT_PREST() %>" >
		     %%
		  </salon:valeur>
	 </td></tr>
	 <tr>
	 <td colspan="2">Client :
	 <%
	       ClientBean aCli = ClientBean.getClientBean(mySalon.getMyDBSession(), Long.toString(aFact.getCD_CLI()));
	 %>
	       <%= aCli.toString() %>
	 </td>
	 </tr>
	 </table>
	 <hr>
	 <table border="0" cellspacing="0" width="90%">
		  <% 
		     Vector lignes = aFact.getLignes(mySalon.getMyDBSession());
		     int i=1;
		     for (i=0; i< lignes.size(); i++) { 
			HistoPrestBean aPrest = (HistoPrestBean) lignes.get(i);
			PrestBean thePrest = PrestBean.getPrestBean(mySalon.getMyDBSession(), 
								  Long.toString(aPrest.getCD_PREST())); %>
		  <tr>
		  <td class="Nombre">
		     <font size=-1>
		     <salon:valeur valeurNulle="null" valeur="<%= aPrest.getQTE().setScale(0) %>" > 
			%%
		     </salon:valeur>
		     </font>
		  </td>
		  <td class="tabDonnees">
		     <font size=-1>
		     <salon:valeur valeur="<%= thePrest.toString() %>" valeurNulle="null">
			   %%
		     </salon:valeur>
		     </font>
		  </td>
		  <td class="Nombre" width="20%">
		     <font size=-1>
		     <salon:valeur valeurNulle="null" valeur="<%= aPrest.getPRX_UNIT_TTC().multiply(aPrest.getQTE()).setScale(2) %>" > 
			%%
		     </salon:valeur>
		     </font>
		  </td>
		  </tr>
		  <% } 
		  %>
	 </table>
	 <hr>
	 <table width="100%">
	 <% if (((aFact.getREMISE_PRC() != null) && (aFact.getREMISE_PRC().compareTo(new BigDecimal(0)) == 1))
		  || ((aFact.getREMISE_FIXE() != null) && (aFact.getREMISE_FIXE().compareTo(new BigDecimal(0)) == 1))) { %>
	 <tr>
	 <td>Total prestations : </td>
	 <td class="Nombre"><salon:valeur valeurNulle="null" valeur="<%= totPrest%>" >
		  %%
	       </salon:valeur>
	    </td>
	    <td width="10%">
	       <%= mySalon.getDevise().toString() %>
	 </td>
	 </tr>
	 <% if ((aFact.getREMISE_PRC() != null) && (aFact.getREMISE_PRC().compareTo(new BigDecimal(0)) == 1)) { %>
	 <tr>
	 <td>Remise en % : </td>
	 <td class="Nombre">
	       <% if (aFact.getCD_FACT() == 0) { 
		  // Facture regroupée : Remise approximative %>
		  &asymp;
	       <% } %>
	       <salon:valeur valeurNulle="null" valeur="<%= aFact.getREMISE_PRC() %>" >
		     %%
	       </salon:valeur>
	 </td>
	 </tr>
	 <% } %>
	 <% if ((aFact.getREMISE_FIXE() != null) && (aFact.getREMISE_FIXE().compareTo(new BigDecimal(0)) == 1)) { %>
	 <tr>
	 <td>Remise fixe : </td>
	 <td class="Nombre"><salon:valeur valeurNulle="null" valeur="<%= aFact.getREMISE_FIXE() %>" >
		  %%
	       </salon:valeur>
	    </td>
	    <td>
	       <%= mySalon.getDevise().toString() %>
	 </td>
	 </tr>
	 <% }
	 } %>
	 <tr>
	 <td>Total à payer : </td>
	 <td class="Nombre"><salon:valeur valeurNulle="null" valeur="<%= aFact.getPRX_TOT_TTC() %>" >
		  <span class="readonly">%%</span>
	       </salon:valeur>
               <%
                    // Boucle sur les autres devises
                    Vector lstDevise = mySalon.getLstAutresDevises();
                    for (int j = 0; j < lstDevise.size(); j++) {
                        DeviseBean aDevise = (DeviseBean) lstDevise.get(j);
               %><br> = 
                        <salon:valeur valeurNulle="null" valeur="<%= aDevise.convertiMontant(aFact.getPRX_TOT_TTC()) %>" >
                            %%
                        </salon:valeur><%
                    } // for 
                    %>
	    </td>
	    <td>
	       <span class="readonly"><%= mySalon.getDevise().toString() %></span>
               <%
                    // Boucle sur les autres devises
                    for (int j = 0; j < lstDevise.size(); j++) {
                        DeviseBean aDevise = (DeviseBean) lstDevise.get(j);
               %><br><%= aDevise.toString() %><%
                    } // for 
                    %>
	 </td>
	 </tr>
	 <tr>
	 <td><font size=-2>dont TVA <%= mySalon.getTxTVA() %>% : </font></td>
	 <td class="Nombre">
	       <font size=-2>
	       <salon:valeur valeurNulle="null" valeur="<%= aFact.getTVA() %>" > 
		  %%
	       </salon:valeur>
	       </font>
	    </td>
	    <td>
	       <font size=-2>
	       <%= mySalon.getDevise().toString() %>
	       </font>
	 </td>
	 </tr>
	 <tr>
	 <td><font size=-2>Taxe et service 15% compris</font></td>
	 </tr>
	 </table>
	 <table width="100%">
	 <tr>
	 <td>Mode de paiement : 
		     <salon:valeur valeur='<%= DonneeRefBean.getDonneeRefBean(mySalon.getMyDBSession(), "MOD_REGL", Integer.toString(aPaiement.getCD_MOD_REGL())).toString() %>' valeurNulle="null">
			%%
		     </salon:valeur>
	 </td>
	 </tr>
	 <tr>
	 <td class="tabDonnees">
	       <b><salon:valeur valeur='<%= mySalon.getMsgTicket() %>' valeurNulle="null">%%</salon:valeur></b>
	 </td>
	 </tr>
	 </table>
   </td>
   </tr>
   </table>
   <% if (listeEdition.size() > 1) { %>
      <p class="newpage">&nbsp;</p>
   <% }
   } // For %>
<script language="JavaScript">

// Impression du ticket
function Imprimer()
{
   window.print();
   <% if (listeEdition.size() == 1) { %>
   window.opener.document.fiche.Action.value="Rafraichissement";
   window.opener.document.fiche.CD_PAIEMENT.value="<%= ((EditionFacture) listeEdition.get(0)).getMyPaiement().getCD_PAIEMENT() %>";
   window.opener.document.fiche.submit();
   <% } %>
   window.close();
}

</script>
</body>
</html>
