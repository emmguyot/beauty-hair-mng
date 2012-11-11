<%
/*
 * This program is part of InCrEG LibertyLook software http://beauty-hair-mng.sourceforge.net
 * Copyright (C) 2001-2012 Emmanuel Guyot <See emmguyot on SourceForge> 
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
		java.util.Map,
		java.text.SimpleDateFormat,
		java.math.BigDecimal" %>
<%@ page import="com.increg.salon.bean.SalonSession,
	       com.increg.salon.bean.PaiementBean,
	       com.increg.salon.bean.FactBean,
	       com.increg.salon.bean.ClientBean,
	       com.increg.salon.bean.DeviseBean,
           com.increg.salon.bean.ModReglBean,
           com.increg.salon.bean.ReglementBean
	       " %>
<%
SalonSession mySalon = com.increg.salon.servlet.ConnectedServlet.CheckOrGoHome(request, response);
%>
<%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %>
<%@ taglib uri="WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<i18n:bundle baseName="messages" locale="<%= mySalon.getLangue() %>"/>
<html>
<head>
<title><i18n:message key="ficPaiement.title" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
<link rel="stylesheet" href="style/jquery-ui-1.8.16.custom.css" type="text/css">
</head>
<body class="donnees" onLoad="Init();document.fiche.CD_MOD_REGL.focus()">
<%@ include file="include/commun.jsp" %>
<script language="JavaScript">
<!--
<%
   // Récupération des paramètres
   String Action = (String) request.getAttribute("Action");
   String Etat = (String) request.getAttribute("Etat");
   PaiementBean aPaiement = (PaiementBean) request.getAttribute("PaiementBean");
   Vector<ReglementBean> reglements = (Vector<ReglementBean>) request.getAttribute("Reglements");
   BigDecimal totPrest = (BigDecimal) request.getAttribute("totPrest");
   Map<Integer, ReglementBean> mapCD_MOD_REGL = (Map<Integer, ReglementBean>) request.getAttribute("mapCD_MOD_REGL");
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
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/ficPaiement.gif" alt=<salon:TimeStamp bean="<%= aPaiement %>" />></h1>
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
	<td class="label"><span class="obligatoire"><i18n:message key="label.modePaiement" /></span> : </td>
	<td><%
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
	</td>
	<td>
         <% if (!Action.equals("Creation")) { 
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
	<td class="label"><span class="obligatoire"><i18n:message key="label.dtPaiement" /></span> : </td>
	<td class="readonly">
	       <salon:valeur valeurNulle="null" valeur="<%= aPaiement.getDT_PAIEMENT() %>" > 
		  <input type="hidden" name="DT_PAIEMENT" size="11" value="%%">
                  <span class="readonly">%%</span>
	       </salon:valeur>
               &nbsp;
	</td>
	</tr>
	<tr>
	<td class="label"><span class="obligatoire"><i18n:message key="label.total" /> :</span></td>
	<td>
	        <salon:valeur valeurNulle="null" valeur="<%= totPrest %>" > 
		     <span class="readonly">%% <%= mySalon.getDevise().toString() %></span>
		</salon:valeur>
               <%
                    // Boucle sur les autres devises
                    Vector lstDevise = mySalon.getLstAutresDevises();
                    for (int i = 0; i < lstDevise.size(); i++) {
                        DeviseBean aDevise = (DeviseBean) lstDevise.get(i);
               %>= 
                        <salon:valeur valeurNulle="null" valeur="<%= aDevise.convertiMontant(totPrest) %>" >
                            <span class="readonly">%% <%= aDevise.toString() %></span>
                        </salon:valeur><%
                    } // for 
                    %>
        </td>
	</tr>
	</table>
	<hr>

   <table border="0" width="100%">
	 <tr>
		  <th>&nbsp;</th>
		  <th><i18n:message key="label.client" /></th>
		  <th><i18n:message key="label.dtFact" /></th>
		  <th><i18n:message key="label.montant" /> <%= mySalon.getDevise().toString() %></th>
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
	      <td><salon:valeur valeurNulle="null" valeur="<%= ClientBean.getClientBean(mySalon.getMyDBSession(), Long.toString(aFact.getCD_CLI()), mySalon.getMessagesBundle()).toString() %>" > 
				<a href="_FicheCli.jsp?Action=Modification&CD_CLI=<%= aFact.getCD_CLI() %>" target="ClientFrame" title="<i18n:message key="ficCli.title" />">%%</a> 
				</salon:valeur> </td>
	      <td class="tabDonnees"><salon:valeur valeur="<%= aFact.getDT_PREST() %>" valeurNulle="null"> 
				<a href="_FicheFact.jsp?Action=Modification&CD_FACT=<%= aFact.getCD_FACT() %>" target="ClientFrame" title="<i18n:message key="ficFact.title" />">%%</a> 
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

<div id="PRIX" style="position:absolute; height:20px; z-index:1; visibility:hidden" > 
<p class="label"><% if (mySalon.isAffichePrix()) { %><salon:inverse montant="<%= totPrest %>" /><% } %></p>
</div>

<script language="JavaScript">
// Au chargement de la page : Recharge le menu pour MAJ de la liste des encours
top.MenuFrame.location.href = top.MenuFrame.location.href;
deplacePrix();

// Fonctions d'action
function deplacePrix() {
   MM_findObj("PRIX").style.left = document.body.clientWidth - 500;
   MM_findObj("PRIX").style.top = document.body.clientHeight - 70;
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
   for (var i=0; i < document.fiche.elements.length; i++) {
      if (document.fiche.elements[i].name.indexOf("AFFECT") != -1) {
	 coche = coche || (document.fiche.elements[i].checked);
      }
   }
   // Verification des données obligatoires
   if (! coche) {
      alert ("<i18n:message key="ficPaiement.cocheFacture" />");
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
       if (confirm ("<i18n:message key="ficPaiement.suppressionDefinitiveConfirm" />")) {
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
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideFichePaiement.html");
}

    <i18n:message key="format.dateSimpleDefaut" id="formatSimple" />
// Un paiement vient d'être cliqué
function clickPaiement(ctrl) {
    if (document.fiche.DT_PAIEMENT.value == "") {
        document.fiche.DT_PAIEMENT.value='<%= new SimpleDateFormat(formatSimple).format(aPaiement.getDT_PAIEMENT_defaut().getTime()) %>'; 
    }
    cd_mod_regl = ctrl.value; 
    if (MM_findObj('modregl' + cd_mod_regl).style.visibility == "hidden") {
    	MM_showHideLayers('modregl' + cd_mod_regl, '', 'show');
	    // Calcul le total des prix
	    total = <%= totPrest %>;
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
    window.open("ficRenduMonnaie.srv?montant=" + montant + "&montantRegle=" + montant + "&aRendre=", 'Calcul', config='height=250,width=400,left=312,top=234');
}

</script>
</body>
</html>
