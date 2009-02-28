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
<%@ page import="java.util.Vector,
java.util.List,
java.util.Map,
java.text.SimpleDateFormat" %>
<%@ page import="org.apache.commons.lang.ArrayUtils" %>
<%@ page import="com.increg.salon.bean.SalonSession,
                com.increg.salon.bean.FactBean,
	        com.increg.salon.bean.PaiementBean,
	        com.increg.salon.bean.ReglementBean,
	        com.increg.salon.bean.HistoPrestBean,
	        com.increg.salon.bean.ClientBean,
	        com.increg.salon.bean.PrestBean,
	        com.increg.salon.bean.TypVentBean,
	        com.increg.salon.bean.DonneeRefBean,
	        com.increg.salon.bean.CollabBean,
	        com.increg.salon.bean.DeviseBean,
                com.increg.salon.bean.ModReglBean" %>
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
<title><i18n:message key="ficFact.title" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees" onLoad="Init();document.fiche.CD_COLLAB.focus()">
<%@ include file="include/commun.jsp" %>
<script language="JavaScript">
<!--
<%
   // Récupération des paramètres
   String Action = (String) request.getAttribute("Action");
   String NbPrest = (String) request.getAttribute("NbPrest");
   FactBean aFact = (FactBean) request.getAttribute("FactBean");
   PaiementBean aPaiement = (PaiementBean) request.getAttribute("PaiementBean");
   Vector<ReglementBean> reglements = (Vector<ReglementBean>) request.getAttribute("Reglements");
   String totPrest = (String) request.getAttribute("totPrest");
   String CD_PREST_SELECT = (String) request.getAttribute("CD_PREST_SELECT");
   String CD_TYP_VENT_SELECT = (String) request.getAttribute("CD_TYP_VENT_SELECT");
   String CD_MARQUE_SELECT = (String) request.getAttribute("CD_MARQUE_SELECT");
   String CD_CATEG_PREST_SELECT = (String) request.getAttribute("CD_CATEG_PREST_SELECT");
   String COMM_SELECT = (String) request.getAttribute("COMM_SELECT");
   List collabs = (List) request.getAttribute("collabs");
   Map<Integer, ReglementBean> mapCD_MOD_REGL = (Map<Integer, ReglementBean>) request.getAttribute("mapCD_MOD_REGL");
%>
   var Action="<%=Action%>";

function Init() {
   <%
   // Positionne les liens d'actions
   if (! Action.equals("Creation")) {
      %>
      MM_showHideLayers('SUPPRIMER?bottomFrame','','show');
      MM_showHideLayers('ENREGISTRER?bottomFrame','','show');
      MM_showHideLayers('IMPRIMER?bottomFrame','','show');
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
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/ficFact.gif" alt=<salon:TimeStamp bean="<%= aFact %>" />></h1>
<salon:message salonSession="<%= mySalon %>" />

<form method="post" action="ficFact.srv" name="fiche">
   <salon:valeur valeurNulle="0" valeur="<%= aFact.getCD_FACT() %>" >
      <input type="hidden" name="CD_FACT" value="%%" >
   </salon:valeur>
   <salon:valeur valeurNulle="0" valeur="<%= aFact.getCD_CLI() %>" >
      <input type="hidden" name="CD_CLI" value="%%" >
   </salon:valeur>
   <input type="hidden" name="Action" value="<%=Action%>">
   <input type="hidden" name="NbPrest" value="<%=NbPrest%>">
   <input type="hidden" name="ParamSup" value="">

   <table width="100%"> 
      <tr>
	 <td class="label"><span class="obligatoire"><i18n:message key="label.client" /></span> : </td>
	 <td> 
	 <%
	    ClientBean aCli = ClientBean.getClientBean(mySalon.getMyDBSession(), Long.toString(aFact.getCD_CLI()), mySalon.getMessagesBundle());
	 %>
	    <span class="readonly"><a href="_FicheCli.jsp?Action=Modification&CD_CLI=<%= aFact.getCD_CLI() %>" target="ClientFrame"><%= aCli.toString() %></a></span> 
	 </td>
	 <td class="label"><span class="facultatif"><i18n:message key="label.dtPrest" /></span> : </td>
	 <td>
            <i18n:message key="format.dateSimpleDefaut" id="formatSimple" />
	    <salon:date type="text" name="DT_PREST" valeurDate="<%= aFact.getDT_PREST() %>" valeurNulle="null" format="<%= formatSimple %>" calendrier="true">%%</salon:date>
	 </td>
      </tr><tr>
	 <td class="label"><span class="obligatoire"><i18n:message key="label.collaborateur" /></span> : </td>
	 <td>
	    <salon:selection valeur="<%= aFact.getCD_COLLAB() %>" valeurs="<%= collabs %>">
	       <select name="CD_COLLAB">
		  %%
	       </select>
	    </salon:selection>
	 </td>
	 
	 <td class="label"><span class="obligatoire"><i18n:message key="label.typePrest" /></span> : </td>
	 <td>
	    <salon:DBselection valeur="<%= aFact.getCD_TYP_VENT() %>" sql='<%= "select CD_TYP_VENT, LIB_TYP_VENT from TYP_VENT where CIVILITE like \'%" + aCli.getCIVILITE() + "%\' or CIVILITE is null order by LIB_TYP_VENT" %>' >
	       <select name="CD_TYP_VENT">
		  %%
	       </select>
	    </salon:DBselection>
	    <% CD_TYP_VENT_SELECT = (CD_TYP_VENT_SELECT == null) ? 
					     Integer.toString(aFact.getCD_TYP_VENT()) : 
					     CD_TYP_VENT_SELECT;
	       // Tjs pas, on prend le premier de cette combo
	       CD_TYP_VENT_SELECT = ((CD_TYP_VENT_SELECT.equals("0")) || (CD_TYP_VENT_SELECT.length() == 0)) ? 
					     (String) request.getAttribute("Premier") : 
					     CD_TYP_VENT_SELECT;
	    %>
	 </td>
	 <td class="label"><span class="obligatoire"><i18n:message key="label.historique" /></span> : </td>
	 <td>
	    <salon:valeur valeurNulle="null" valeur="<%= aFact.getFACT_HISTO() %>" >
	       <input type="hidden" name="FACT_HISTO" value="%%">
	       %%
	    </salon:valeur>

	 </td>
      </tr>
   </table>
   <hr>
   <table border="0" cellspacing="0" width="100%">
      <tr> 
	 <th><span class="obligatoire"><i18n:message key="ficFact.tabTypePrest" /></span></th>
	 <th><span class="obligatoire"><i18n:message key="ficFact.tabCategMarque" /></span></th>
	 <th><span class="obligatoire"><i18n:message key="label.prestation" /></span></th>
	 <th><span class="obligatoire"><i18n:message key="label.collaborateur" /></span></th>
	 <th><span class="obligatoire"><i18n:message key="ficFact.tabQte" /></span></th>
	 <th><span class="obligatoire"><i18n:message key="ficFact.tabPrxUnit" /></span></th>
	 <th><span class="obligatoire"><i18n:message key="ficFact.tabPrxTotal" /></span></th>
	 <th><span class="facultatif"><i18n:message key="ficFact.tabComment" /></span></th>
	 <th><span class="facultatif"><i18n:message key="ficFact.satisfication" /></span></th>
	 <th>&nbsp;</th>
      </tr>
      <% 
      Vector lignes = aFact.getLignes(mySalon.getMyDBSession());
      int i=1;
      for (i=0; i< lignes.size(); i++) { 
	 HistoPrestBean aPrest = (HistoPrestBean) lignes.get(i);
	 PrestBean thePrest = PrestBean.getPrestBean(mySalon.getMyDBSession(), 
						   Long.toString(aPrest.getCD_PREST())); %>
	 <tr>
	    <td class="tabDonnees">
	       <salon:valeur valeurNulle="null" valeur="<%= aPrest.getNUM_LIG_FACT() %>" >
		  <input type="hidden" name="NUM_LIG_FACT<%= i %>" value="%%">
	       </salon:valeur>
		  
	       <% TypVentBean theTypVent = TypVentBean.getTypVentBean(mySalon.getMyDBSession(),
							       Integer.toString(thePrest.getCD_TYP_VENT())); %>
	       <salon:valeur valeur="<%= theTypVent.toString() %>" valeurNulle="null">
		  %%
	       </salon:valeur>
	    </td>
	    <td class="tabDonnees">
	       <% if (theTypVent.getMARQUE().equals ("O")) {
		     DonneeRefBean theMarque = DonneeRefBean.getDonneeRefBean(mySalon.getMyDBSession(), "MARQUE",
							       Integer.toString(thePrest.getCD_MARQUE())); %>
		     <salon:valeur valeur="<%= theMarque.toString() %>" valeurNulle="null">
			%%
		     </salon:valeur>
	       <% }
		  else {
		     DonneeRefBean theCategArt = DonneeRefBean.getDonneeRefBean(mySalon.getMyDBSession(), 
							       "CATEG_PREST",
							       Integer.toString(thePrest.getCD_CATEG_PREST())); %>
		     <salon:valeur valeur="<%= theCategArt.toString() %>" valeurNulle="null">
			%%
		     </salon:valeur>
	       <% } %>

	    </td>
	    <td class="tabDonnees">
	       <salon:valeur valeur="<%= thePrest.toString() %>" valeurNulle="null"> 
		  <a href="_FichePrest.jsp?Action=Modification&CD_PREST=<%= thePrest.getCD_PREST() %>" target="ClientFrame">%%</a> 
	       </salon:valeur>
	    </td>
	    <td class="tabDonnees">
	       <% if (aFact.getCD_PAIEMENT() == 0) { %>
		     <salon:selection valeur="<%= aPrest.getCD_COLLAB() %>" valeurs="<%= collabs %>">
			<select name="CD_COLLAB<%= i %>">
			   %%
			</select>
		     </salon:selection>		 
	       <% } 
		  else { %>
		     <salon:valeur valeur="<%= aPrest.getCD_COLLAB() %>" valeurNulle="null">
		        <input type="hidden" name="CD_COLLAB<%= i %>" value="%%">
		     </salon:valeur>
		     <salon:valeur valeur="<%= CollabBean.getCollabBean(mySalon.getMyDBSession(), Integer.toString(aPrest.getCD_COLLAB())).toString() %>" valeurNulle="null">
			%%
		     </salon:valeur>
	       <% } %>
	    </td>
	    <td class="tabDonnees">
	       <% if (aFact.getCD_PAIEMENT() == 0) { %>
		     <salon:valeur valeurNulle="null" valeur="<%= aPrest.getQTE() %>" > 
			<input class="nombre" type="text" name="QTE<%= i %>" size="2" value="%%">
		     </salon:valeur>
	       <% } 
		  else { %>
		     <salon:valeur valeurNulle="null" valeur="<%= aPrest.getQTE() %>" > 
			<input type="hidden" name="QTE<%= i %>" value="%%">
			   %%
		     </salon:valeur>
	       <% } %>
	    </td>
	    <td class="tabDonnees">
		  <% if (aFact.getCD_PAIEMENT() == 0) { %>
		  <salon:valeur valeurNulle="null" valeur="<%= aPrest.getPRX_UNIT_TTC() %>" > 
		     <input class="nombre" type="text" name="PRX_UNIT_TTC<%= i %>" size="6" value="%%">
		  </salon:valeur>
		  <% } 
		     else { %>
		  <salon:valeur valeurNulle="null" valeur="<%= aPrest.getPRX_UNIT_TTC() %>" > 
		     <input type="hidden" name="PRX_UNIT_TTC<%= i %>" value="%%">
		     %%
		  </salon:valeur>
		  <% } %>
		</td>
	    <td class="tabDonnees">
		  <salon:valeur valeurNulle="null" valeur="<%= aPrest.getPRX_UNIT_TTC().multiply(aPrest.getQTE()).setScale(2) %>" > 
		     %%
		  </salon:valeur>
		</td>
		<td class="tabDonnees">
		  <salon:valeur valeurNulle="null" valeur="<%= aPrest.getCOMM() %>" > 
		     <input type="hidden" name="COMM<%= i %>" value="%%">
		  </salon:valeur>
		  <a href="javascript:SaisieCommentaire(<%= i %>)" title='<i18n:message key="label.commentaire" />'><img src=<%= (((Integer) request.getAttribute("Longueur")).intValue() > 0) ? "images/plein.gif" : "images/vide.gif"%> border="0" width="11" height="13" alt="<i18n:message key="label.commentaire" />"></a>
		</td>
		<td class="tabDonnees">
		  <salon:selection valeur="<%= aPrest.getNIV_SATISF() %>" valeurs='<%= "|1|2|3|4|5" %>'>
		     <select name="NIV_SATISF<%= i %>">
			%%
		     </select>
		  </salon:selection>
		</td>
		<td class="tabDonnees">
		  <% if (aFact.getCD_PAIEMENT() == 0) { %>
		  <a href="javascript:SupprimerLigne(<%= i %>)" title='<i18n:message key="label.supprimerLigne" />'><img src=images/moins.gif width="15" height="15" border="0" alt="<i18n:message key="label.supprimerLigne" />"></a>
		  <% } %>
		</td>
		</tr>
		<% } 
		// ****************** Ligne vide *************************************
		   if (aFact.getCD_PAIEMENT() == 0) { %>
	        <tr class="ligneTab1">
		<td class="tabDonnees">
		  <salon:DBselection valeur="<%= CD_TYP_VENT_SELECT %>" sql="select CD_TYP_VENT, LIB_TYP_VENT from TYP_VENT order by LIB_TYP_VENT">
		     <select name="CD_TYP_VENT<%= i %>" onChange="Recharge(<%= i %>, 1)">
			%%
		     </select>
		  </salon:DBselection>
	        </td>
		<td class="tabDonnees">
		  <%
		     CD_TYP_VENT_SELECT = ((CD_TYP_VENT_SELECT.equals("0")) || (CD_TYP_VENT_SELECT.length() == 0)) ?
					     (String) request.getAttribute("Premier") : 
					     CD_TYP_VENT_SELECT;
		     TypVentBean theTypVent = TypVentBean.getTypVentBean(mySalon.getMyDBSession(), CD_TYP_VENT_SELECT);
		     if (theTypVent.getMARQUE().equals ("O")) { %>
			<salon:DBselection valeur="<%= CD_MARQUE_SELECT %>" sql="select CD_MARQUE, LIB_MARQUE from MARQUE order by LIB_MARQUE">
			   <select name="CD_MARQUE<%= i %>" onChange="Recharge(<%= i %>, 2)">
			      %%
			   </select>
			</salon:DBselection>
			<% CD_MARQUE_SELECT = ((CD_MARQUE_SELECT == null) || (CD_MARQUE_SELECT.length() == 0) || (CD_MARQUE_SELECT.equals("0"))) ?
					     (String) request.getAttribute("Premier") : 
					     CD_MARQUE_SELECT;
		     } else { %>
			<salon:DBselection valeur="<%= CD_CATEG_PREST_SELECT %>" sql="select CD_CATEG_PREST, LIB_CATEG_PREST from CATEG_PREST order by LIB_CATEG_PREST">
			   <select name="CD_CATEG_PREST<%= i %>" onChange="Recharge(<%= i %>, 2)">
			      %%
			   </select>
			</salon:DBselection>
			<% 
			   CD_CATEG_PREST_SELECT = ((CD_CATEG_PREST_SELECT == null) || (CD_CATEG_PREST_SELECT.length() == 0) || (CD_CATEG_PREST_SELECT.equals("0"))) ?
					     (String) request.getAttribute("Premier") : 
					     CD_CATEG_PREST_SELECT;
		     } %>
	        </td>
		<td class="tabDonnees">
		  <% String reqSQL = "select CD_PREST, LIB_PREST from PREST where CD_TYP_VENT=" + CD_TYP_VENT_SELECT;
		     if (theTypVent.getMARQUE().equals ("O")) { 
			reqSQL = reqSQL + " and CD_MARQUE=" + CD_MARQUE_SELECT;
		     }
		     else {
			reqSQL = reqSQL + " and CD_CATEG_PREST=" + CD_CATEG_PREST_SELECT;
		     }
                     reqSQL = reqSQL + " and INDIC_PERIM = 'N'";
		     reqSQL = reqSQL + " order by LIB_PREST";
		  %>
		  <salon:DBselection valeur="<%= CD_PREST_SELECT %>" sql='<%= reqSQL %>'>
		     <select name="CD_PREST<%= i %>" onChange="Recharge(<%= i %>, 3)">
			%%
		     </select>
		  </salon:DBselection>
		  <%
		     CD_PREST_SELECT =  (CD_PREST_SELECT == null) ? 
					     (String) request.getAttribute("Premier") : 
					     CD_PREST_SELECT;
		  %>
		</td>
		<td class="tabDonnees">
                    <salon:selection valeur="<%= aFact.getCD_COLLAB() %>" valeurs="<%= collabs %>">
                        <select name="CD_COLLAB<%= i %>">
			     %%
                        </select>
                    </salon:selection>		
	        </td>
		<td class="tabDonnees">
		  <salon:valeur valeurNulle="null" valeur="<%= 1 %>" > 
		     <input class="nombre" type="text" name="QTE<%= i %>" size="2" value="%%">
		  </salon:valeur>
		</td>
		<td class="tabDonnees">
		<% PrestBean aPrest = null;
		   if (CD_PREST_SELECT != null) {
		      aPrest = PrestBean.getPrestBean(mySalon.getMyDBSession(), CD_PREST_SELECT);
		   } %>
		  <salon:valeur valeurNulle="null" valeur="<%= ((aPrest == null) ? null : aPrest.getPRX_UNIT_TTC()) %>" > 
		     <input class="nombre" type="text" name="PRX_UNIT_TTC<%= i %>" size="6" value="%%">
		  </salon:valeur>
		</td>
		<td class="tabDonnees">&nbsp;</td>
		<td class="tabDonnees">
		  <salon:valeur valeurNulle="null" valeur="<%= COMM_SELECT %>" > 
		     <input type="hidden" name="COMM<%= i %>" value="%%">
		  </salon:valeur>
		  <a href="javascript:SaisieCommentaire(<%= i %>)" title='<i18n:message key="label.commentaire" />'><img src=<%= (((Integer) request.getAttribute("Longueur")).intValue() > 0) ? "images/plein.gif" : "images/vide.gif"%> border="0" width="11" height="13" alt="<i18n:message key="label.commentaire" />"></a>
		</td>
		<td class="tabDonnees">
		  <salon:selection valeur="<%= 0 %>" valeurs='<%= "|1|2|3|4|5" %>'>
		     <select name="NIV_SATISF<%= i %>">
			%%
		     </select>
		  </salon:selection>
		</td>
	       <td class="tabDonnees">
		  <a href="javascript:AjouterLigne(<%= i %>)" title='<i18n:message key="label.ajouterLigne" />'><img src=images/plus.gif width="15" height="15" border="0" alt="<i18n:message key="label.ajouterLigne" />"></a>
	       </td>
	       </tr>
	       <% } %>
	</table>
	<hr>
	<table width="100%">
	<tr>
	<td class="label"><span class="obligatoire"><i18n:message key="ficFact.totalPrest" /></span> : </td>
	<td><salon:valeur valeurNulle="null" valeur="<%= totPrest%>" >
	       <span class="readonly">%% <%= mySalon.getDevise().toString() %></span>
	    </salon:valeur>
	</td>
	<td class="label"><span class="facultatif"><i18n:message key="ficFact.remisePrc" /></span> : </td>
	<td>
	    <% if (aFact.getCD_PAIEMENT() == 0) { %>
	    <salon:valeur valeurNulle="null" valeur="<%= aFact.getREMISE_PRC() %>" >
		  <input class="nombre" type="text" name="REMISE_PRC" size="5" value="%%">
	    </salon:valeur>
	    <% }
	       else { %>
	    <salon:valeur valeurNulle="null" valeur="<%= aFact.getREMISE_PRC() %>" >
		  <input type="hidden" name="REMISE_PRC" value="%%">
		  %%
	    </salon:valeur>
	    <% } %>
        </td>
	<td class="label"><span class="facultatif"><i18n:message key="ficFact.remiseFixe" /></span> : </td>
	<td>
	    <% if (aFact.getCD_PAIEMENT() == 0) { %>
	    <salon:valeur valeurNulle="null" valeur="<%= aFact.getREMISE_FIXE() %>" >
	       <input class="nombre" type="text" name="REMISE_FIXE" size="5" value="%%">
	    </salon:valeur>
	    <% }
	       else { %>
	    <salon:valeur valeurNulle="null" valeur="<%= aFact.getREMISE_FIXE() %>" >
	       <input type="hidden" name="REMISE_FIXE" value="%%">
	       %%
	    </salon:valeur>
	    <% } %>
	    <%= mySalon.getDevise().toString() %>
        </td>
	</tr>
	<tr>
	<td class="label"><h2><span class="obligatoire"><i18n:message key="ficFact.totalPayer" /></span> : </h2></td>
	<td><h2><salon:valeur valeurNulle="null" valeur="<%= aFact.getPRX_TOT_TTC() %>" >
	       <span class="readonly">%% <%= mySalon.getDevise().toString() %></span>
	    </salon:valeur>
            <%
                // Boucle sur les autres devises
                Vector lstDevise = mySalon.getLstAutresDevises();
                for (int j = 0; j < lstDevise.size(); j++) {
                    DeviseBean aDevise = (DeviseBean) lstDevise.get(j);
               %> = 
                    <salon:valeur valeurNulle="null" valeur="<%= aDevise.convertiMontant(aFact.getPRX_TOT_TTC()) %>" >
                        <span class="readonly">%% <%= aDevise.toString() %></span>
                    </salon:valeur><%
                } // for 
                %>
        </h2></td>
	<td class="label"><span class="obligatoire"><i18n:message key="ficFact.dontTVA" /></span> : </td>
	<td class="tabDonneesGauche">
	    <salon:valeur valeurNulle="null" valeur="<%= aFact.getTVA() %>" > 
	       <span class="readonly">%% <%= mySalon.getDevise().toString() %></span>
	    </salon:valeur>
        </td>
	</tr>
	</table>
	<div ID="PAIEMENT" style="visibility:visible; width:100%">
	<table style="width:100%">
	<tr>
	<td class="label"><span class="obligatoire"><i18n:message key="label.modePaiement" /></span> : </td>
	<td>
	    <salon:valeur valeurNulle="0" valeur="<%= aPaiement.getCD_PAIEMENT() %>" >
	       <input type="hidden" name="CD_PAIEMENT" value="%%" >
	    </salon:valeur>
	    <%
	    long nbFact = aPaiement.getFact(mySalon.getMyDBSession()).size();
	    if (nbFact > 1) { 
	    	for (ReglementBean aReglement : reglements) { %>
		    	<input type="hidden" name="REGLEMENT<%= aReglement.getCD_MOD_REGL() %>" value="<%= aReglement.getMONTANT() %>">
		    	<span class="readonly"><%= DonneeRefBean.getDonneeRefBean(mySalon.getMyDBSession(), "MOD_REGL", Integer.toString(aReglement.getCD_MOD_REGL())).toString() %> (<%= aReglement.getMONTANT() %>)</span><br/>
	    <%
	    	}
	    }
        else {
        	// TODO Gestion du cas où une vieille facture est éditée et le mode de réglement n'est plus utilisable
              Vector<ModReglBean> lstModRegl = ModReglBean.getAllUtilisable(mySalon.getMyDBSession());
              for (ModReglBean modRegl : lstModRegl) { %>
                <input type="checkbox" id="CD_MOD_REGL<%= modRegl.getCD_MOD_REGL() %>" value="<%= modRegl.getCD_MOD_REGL() %>" onclick="return clickPaiement(this);" <%= 
                    mapCD_MOD_REGL.containsKey(modRegl.getCD_MOD_REGL()) ? "checked=\"checked\"" : "" 
                %> /><label for="CD_MOD_REGL<%= modRegl.getCD_MOD_REGL() %>" ><%= modRegl.getLIB_MOD_REGL() %></label>
                <span id="modregl<%= modRegl.getCD_MOD_REGL() %>" <%= mapCD_MOD_REGL.containsKey(modRegl.getCD_MOD_REGL()) ? "" : "style=\"visibility: hidden\"" %> >
                	<input type="text" size="6" name="REGLEMENT<%= modRegl.getCD_MOD_REGL() %>" 
                			value="<%= mapCD_MOD_REGL.containsKey(modRegl.getCD_MOD_REGL()) ? mapCD_MOD_REGL.get(modRegl.getCD_MOD_REGL()).getMONTANT() : "" %>" ></span><br/><%
              }
           %>
        <% } %>
        </td><td valign="top">
        <%
	       if (nbFact == 0) { %>
                    <a href="_FichePaiement.jsp" target="ClientFrame"><i18n:message key="ficFact.paiementRegroupe" /></a> 
        <% }
	       else if (nbFact > 1) { %>
                    <a href="_FichePaiement.jsp?Action=Modification&CD_PAIEMENT=<%= aPaiement.getCD_PAIEMENT() %>" target="ClientFrame"><i18n:message key="ficFact.paiementRegroupe" /></a> 
        <% } 
           else { 
                // 1 mode de règlement
                for (ReglementBean aReglement : reglements) {
                	ModReglBean aModRegl = ModReglBean.getModReglBean(mySalon.getMyDBSession(), Integer.toString(aReglement.getCD_MOD_REGL()));
	                if (aModRegl.getIMP_CHEQUE().equals("O")) { %>
    	                <a href="ficChqImpr.jsp?montant=<%= aReglement.getMONTANT() %>" target="_blank"><i18n:message key="ficFact.impressionCheque" /></a><br/>
             	<% } else if (aModRegl.getRENDU_MONNAIE().equals("O")) {%>                
                	    <a href="javascript:calculRendu(<%= aReglement.getMONTANT() %>)"><i18n:message key="ficFact.renduMonnaie" /></a><br/>
             	<% }
                }
          } %>
        </td>
	<td class="label"><span class="obligatoire"><i18n:message key="ficFact.dtPaiement" /></span> : </td>
	<td class="readonly">
            <salon:valeur valeurNulle="null" valeur="<%= aPaiement.getDT_PAIEMENT() %>" > 
                <input type="hidden" name="DT_PAIEMENT" value="%%">
                <span class="readonly">%%</span>
            </salon:valeur>
            &nbsp;
	</td>
	</tr>
	</table>
	</div><p>&nbsp;</p>
</form>

<span id="COMMENTAIRE" class="action" style="position:absolute; z-index:2; left: 15px; visibility: hidden">
   <form name="fComm">
   <table>
   <tr>
   <td valign="top">
      <span class="facultatif"><i18n:message key="label.commentaire" /> :</span>
   </td>
   <td>
      <textarea name="Comm" cols="50" rows="4" align="middle">
      </textarea>
      <input type="hidden" name="Num">
   </td>
   <td valign="top">
      <a href="javascript:FinSaisieCommentaire()"><i18n:message key="label.validerCommentaire" /></a>
   </td>
   </tr>
   </table>
   </form>
</span>

<div id="PRIX" style="position:absolute; height:20px; z-index:1; visibility:hidden" > 
<p class="label"><% if (mySalon.isAffichePrix()) { %><salon:inverse montant="<%= aFact.getPRX_TOT_TTC() %>" /><% } %></p>
</div>

<script language="JavaScript">
// Au chargement de la page : Recharge le menu pour MAJ de la liste des encours
top.MenuFrame.location.href = top.MenuFrame.location.href;
chgtFactHisto();
deplacePrix();

// Fonctions d'action
function deplacePrix() {
   MM_findObj("PRIX").style.left = document.body.clientWidth - 500;
   MM_findObj("PRIX").style.top = document.body.clientHeight - 70;
   MM_showHideLayers('PRIX','','show');
}

// Chg Historique
function chgtFactHisto ()
{
   if (document.fiche.FACT_HISTO.value == "N") {
      MM_showHideLayers('PAIEMENT','','show');
   }
   else {
      MM_showHideLayers('PAIEMENT','','hide');
   }
}

// Recharge l'écran pour mettre à jour les prestations ou le prix
function Recharge(NumPrest, niveau)
{
   if (niveau == 1) {
      // RAZ de la prestation
      document.fiche.Action.value = "Rechargement";
   }
   else if (niveau == 2) {
      document.fiche.Action.value = "Rechargement+";
   }
   else {
      document.fiche.Action.value = "Rechargement++";
   }
   document.fiche.ParamSup.value = NumPrest;
   document.fiche.submit();
}

// Contrôle des données avant enregistrement
function ControleEnreg ()
{
   // Verification des données obligatoires
   if (document.fiche.DT_PREST.value == "") {
      alert ("<i18n:message key="ficFact.datePrestManquant" />");
      return false;
   }
   if (parseInt(document.fiche.REMISE_PRC.value) >= 100) {
      alert ("<i18n:message key="ficFact.prcTropGros" />");
      return false;
   }
   return true;
}

// Affichage / Saisie du commentaire
function SaisieCommentaire (NumPrest)
{
   MM_showHideLayers('COMMENTAIRE','','show');
   document.fComm.Comm.value = document.fiche.elements["COMM" + NumPrest].value;
   document.fComm.Num.value = NumPrest;
   document.fComm.Comm.focus();
   return;
}

function FinSaisieCommentaire()
{
   var NumPrest = document.fComm.Num.value;
   document.fiche.elements["COMM" + NumPrest].value = document.fComm.Comm.value;

   MM_showHideLayers('COMMENTAIRE','','hide');
}

// Supprime une ligne de prestation
function SupprimerLigne(NumPrest)
{
   // Verification des données obligatoires
   if (! ControleEnreg()) {
      return;
   }
   document.fiche.Action.value = "SuppressionLigne";
   document.fiche.ParamSup.value = NumPrest;
   document.fiche.submit();
}

// Ajoute une ligne de prestation
function AjouterLigne(NumPrest)
{
   // Verification des données obligatoires
   if (! ControleEnreg()) {
      return;
   }
   document.fiche.Action.value = "AjoutLigne";
   document.fiche.ParamSup.value = NumPrest;
   document.fiche.submit();
}

// Enregistrement des données du client
function Enregistrer()
{
   // Verification des données obligatoires
   if (! ControleEnreg()) {
      return;
   }
   if (document.fiche.Action.value != "Creation") {
      document.fiche.Action.value = "Modification";
   }
   document.fiche.submit();
}

// Suppression du client
function Supprimer()
{
    if ((document.fiche.CD_FACT.value != "0") && (document.fiche.CD_FACT.value != "")) {
        if (confirm ("<i18n:message key="message.suppressionDefinitiveConfirm" />")) {
            document.fiche.Action.value = "Suppression";
            document.fiche.submit();
        }
    }
}

// Impression du ticket
function Imprimer()
{
   // Verification des données obligatoires
   factureReglee = false;
   for(i=0;i<document.fiche.elements.length;i++){
 		if (document.fiche.elements[i].id.indexOf("CD_MOD_REGL") == 0) {
 			factureReglee = factureReglee || document.fiche.elements[i].checked;
 		} 
   }
   if ((document.fiche.DT_PAIEMENT.value == "") || (!factureReglee)) {
      alert ("<i18n:message key="ficFact.reglementPourImp" />");
      return;
   }
   document.fiche.Action.value = "Impression";
   document.fiche.target="_blank";
   document.fiche.submit();
   document.fiche.Action.value = "";
   document.fiche.target="";
}

// Affichage de l'aide
function Aide()
{
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideFicheFact.html");
}

// Un paiement vient d'être cliqué
function clickPaiement(ctrl) {
    if (document.fiche.DT_PAIEMENT.value == "") {
        document.fiche.DT_PAIEMENT.value='<%= new SimpleDateFormat(formatSimple).format(aPaiement.getDT_PAIEMENT_defaut().getTime()) %>'; 
    }
    cd_mod_regl = ctrl.value; 
    if (MM_findObj('modregl' + cd_mod_regl).style.visibility == "hidden") {
    	MM_showHideLayers('modregl' + cd_mod_regl, '', 'show');
	    // Calcul le total des prix
	    total = <%= aFact.getPRX_TOT_TTC() %>;
	    for(i=0;i<document.fiche.elements.length;i++){
	  		if (document.fiche.elements[i].name.indexOf("REGLEMENT") == 0) {
	  			total -= new Number(document.fiche.elements[i].value);
	  		} 
		}
		document.fiche.elements["REGLEMENT"+cd_mod_regl].value = total.toFixed(2);
    }
    else {
    	MM_showHideLayers('modregl' + cd_mod_regl, '', 'hide');
		document.fiche.elements["REGLEMENT"+cd_mod_regl].value = "";
    }
}

//Ouverture du calcul de rendu
function calculRendu(montant) {
  window.open("ficRenduMonnaie.srv?montant=" + montant + "&montantRegle=" + montant + "&aRendre=", 'Calcul', config='height=250,width=440,left=312,top=234');
}

</script>
</body>
</html>
