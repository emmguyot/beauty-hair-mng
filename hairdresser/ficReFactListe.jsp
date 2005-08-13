<%@ page import="java.util.Vector" %>
<%@ page import="com.increg.salon.bean.SalonSession,
	       com.increg.salon.bean.FactBean,
	       com.increg.salon.bean.PaiementBean,
	       com.increg.salon.bean.HistoPrestBean,
	       com.increg.salon.bean.ClientBean,
	       com.increg.salon.bean.PrestBean,
	       com.increg.salon.bean.DonneeRefBean,
	       com.increg.salon.request.EditionFacture" %>
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

<table width="100%" border=1 rules="groups">
<tr>
   <th>Date</th>
   <th>Client</th>
   <th>Prestation</th>
   <th>Qté</th>
   <th>Prix<br>Unitaire</th>
   <th>Remise<br>%</th>
   <th>Remise<br>fixe</th>
   <th>Total<br>Facture</th>
   <th>Mode<br>Paiement</th>
</tr>
   
<% for (int iListe=0; iListe < listeEdition.size(); iListe++) { 
      FactBean aFact = ((EditionFacture) listeEdition.get(iListe)).getMyFact();
      PaiementBean aPaiement = ((EditionFacture) listeEdition.get(iListe)).getMyPaiement();
      String totPrest = aFact.getTotPrest().toString();

      Vector lignes = aFact.getLignes(mySalon.getMyDBSession());
      int i=1;
      for (i=0; i< lignes.size(); i++) { 
	 HistoPrestBean aPrest = (HistoPrestBean) lignes.get(i);
	 PrestBean thePrest = PrestBean.getPrestBean(mySalon.getMyDBSession(), 
						   Long.toString(aPrest.getCD_PREST())); 
	 
	 if (i == 0) { %>
	    <tbody>
	    <tr>
	       <td>
		  <salon:valeur valeurNulle="null" valeur="<%= aFact.getDT_PREST() %>" >
		     %%
		  </salon:valeur>
	       </td>
	       <td>
	       <%
		  ClientBean aCli = ClientBean.getClientBean(mySalon.getMyDBSession(), Long.toString(aFact.getCD_CLI()));
	       %>
		  <%= aCli.toString() %>
	       </td>
      <% }
         else { %>
	    <tr>
	       <td>&nbsp;</td>
	       <td>&nbsp;</td>
      <% } %>
	 <td>
	    <salon:valeur valeur="<%= thePrest.toString() %>" valeurNulle="null">
		  %%
	    </salon:valeur>
	 </td>
	 <td class="Nombre">
	    <salon:valeur valeurNulle="null" valeur="<%= aPrest.getQTE().setScale(0) %>" > 
	       %%
	    </salon:valeur>
	 </td>
	 <td class="Nombre">
	    <salon:valeur valeurNulle="null" valeur="<%= aPrest.getPRX_UNIT_TTC() %>" > 
	       %%
	    </salon:valeur>
	 </td>
      <% if (i == (lignes.size() -1)) { %>
	       <td class="Nombre">
		  <salon:valeur valeurNulle="null" valeur="<%= aFact.getREMISE_PRC() %>" > 
		     %%&nbsp;
		  </salon:valeur>
	       </td>
	       <td class="Nombre">
		  <salon:valeur valeurNulle="null" valeur="<%= aFact.getREMISE_FIXE() %>" > 
		     %%&nbsp;
		  </salon:valeur>
	       </td>
	       <td class="Nombre">
		  <salon:valeur valeurNulle="null" valeur="<%= aFact.getPRX_TOT_TTC() %>" > 
		     %%
		  </salon:valeur>
	       </td>
	       <td>
		  <salon:valeur valeur='<%= DonneeRefBean.getDonneeRefBean(mySalon.getMyDBSession(), "MOD_REGL", Integer.toString(aPaiement.getCD_MOD_REGL())).toString() %>' valeurNulle="null">
		     %%
		  </salon:valeur>
	       </td>
	    </tr>
	    </tbody>
      <% } 
         else { %>
	       <td>&nbsp;</td>
	       <td>&nbsp;</td>
	       <td>&nbsp;</td>
	       <td>&nbsp;</td>
	    </tr>
      <% } 
      }
   } %>
</table>
<script language="JavaScript">

// Impression du ticket
function Imprimer()
{
   window.print();
   window.close();
}
</script>
</body>
</html>
