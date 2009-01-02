<%
/*
 * This program is part of InCrEG LibertyLook software http://beauty-hair-mng.sourceforge.net
 * Copyright (C) 2001-2009 Emmanuel Guyot <See emmguyot on SourceForge> 
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
<%@ page import="java.util.Vector" %>
<%@ page import="com.increg.salon.bean.SalonSession,
	       com.increg.salon.bean.FactBean,
	       com.increg.salon.bean.PaiementBean,
	       com.increg.salon.bean.ReglementBean,
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
<title><i18n:message key="title.ficReFactListe" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="corpsImpr" onLoad="Imprimer()">
<%@ include file="include/commun.jsp" %>
<%
   // Récupération des paramètres
   Vector listeEdition = (Vector) request.getAttribute("listeEdition");
%>

<table width="100%" border=1 rules="groups">
<tr>
   <th><i18n:message key="label.date" /></th>
   <th><i18n:message key="label.client" /></th>
   <th><i18n:message key="label.prestation" /></th>
   <th><i18n:message key="label.qte" /></th>
   <th><i18n:message key="label.prixUnitaireTableau" /></th>
   <th><i18n:message key="label.remisePrc" /></th>
   <th><i18n:message key="label.remiseFixe" /></th>
   <th><i18n:message key="label.totalFacture" /></th>
   <th><i18n:message key="label.modePaiementTableau" /></th>
</tr>
   
<% for (int iListe=0; iListe < listeEdition.size(); iListe++) { 
      FactBean aFact = ((EditionFacture) listeEdition.get(iListe)).getMyFact();
      PaiementBean aPaiement = ((EditionFacture) listeEdition.get(iListe)).getMyPaiement();
      Vector<ReglementBean> reglements = ((EditionFacture) listeEdition.get(iListe)).getReglements();
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
		  ClientBean aCli = ClientBean.getClientBean(mySalon.getMyDBSession(), Long.toString(aFact.getCD_CLI()), mySalon.getMessagesBundle());
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
                    <%  
                    String sep = "";	
                    for (ReglementBean aReglement : reglements) { 
                        %><salon:valeur valeur='<%= sep + DonneeRefBean.getDonneeRefBean(mySalon.getMyDBSession(), "MOD_REGL", Integer.toString(aReglement.getCD_MOD_REGL())).toString() %>' valeurNulle="null">%%</salon:valeur><% 
                            sep = ", ";
                    } %>
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
   var obj_window = window.open('', '_self');
   obj_window.opener = window;
   obj_window.focus();
   opener=self;
   self.close();
}
</script>
</body>
</html>
