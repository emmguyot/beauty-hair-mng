<%
/*
 * This program is part of InCrEG LibertyLook software http://beauty-hair-mng.sourceforge.net
 * Copyright (C) 2001-2006 Emmanuel Guyot <See emmguyot on SourceForge> 
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
<%@ page import="java.util.Vector, java.util.Calendar, java.math.BigDecimal" %>
<%@ page import="com.increg.salon.bean.SalonSession,
	       com.increg.salon.bean.ArtBean,
	       com.increg.salon.bean.MvtStkBean,
	       com.increg.salon.bean.TypMvtBean,
	       com.increg.salon.bean.FactBean
	       " %>
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
<title><i18n:message key="ficArt.title" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees" onLoad="document.fiche.LIB_ART.focus()">
<%@ include file="include/commun.jsp" %>
<script language="JavaScript">
<!--
<%
   // Récupération des paramètres
   String Action = (String) request.getAttribute("Action");
   int NbMvt = ((Integer) request.getAttribute("NbMvt")).intValue();
   int DebMvt = ((Integer) request.getAttribute("DebMvt")).intValue();
   ArtBean aArt = (ArtBean) request.getAttribute("ArtBean");
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
    MM_showHideLayers('DUPLIQUER?bottomFrame','','show');
    MM_showHideLayers('RETOUR_LISTE?bottomFrame','','show');
}
//-->
</script>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/ficArt.gif" alt=<salon:TimeStamp bean="<%= aArt %>" />></h1>
<salon:message salonSession="<%= mySalon %>" />
<form method="post" action="ficArt_Mvt.srv" name="fiche">

<%@ include file="ficArt_Common.jsp" %>

<% if (Action != "Creation") { %>
   <table border="0" cellspacing="0" width="100%">
   <tr>
      <td align="right"> <span class="souslien"> 
        <a href="_FicheArt.jsp?Action=Modification&CD_ART=<%= aArt.getCD_ART() %>" target="ClientFrame"><i18n:message key="ficArt_Mvt.fournArticle" /></a> </span> </td>
   </tr>
   </table>
<% } %>
   <table border="0" width="100%">
	 <tr>
		  <th><i18n:message key="label.date" /></th>
		  <th><i18n:message key="label.typeMouvement" /></th>
		  <th><i18n:message key="label.qte" /></th>
		  <th><i18n:message key="label.valeurUnit" /></th>
		  <th><i18n:message key="label.stockAvant" /></th>
		  <th>&nbsp;</th>
	 </tr>
	 <%
	 // Recupère la liste
	 Vector listeMvt = (Vector) request.getAttribute("listeMvt");
	 int i = 1;
	       
	 %>
	 <input type="hidden" name="NbMvt" value="<%= NbMvt %>">
	 <input type="hidden" name="DebMvt" value="<%= DebMvt %>">
	 <tr class="ligneTab1">
	    <td class="tabDonnees">
	        <salon:valeur valeurNulle="null" valeur="<%= Calendar.getInstance() %>" >
		  <input type="hidden" name="DT_MVT" value="%%">
		  <span class="readonly">%%</span>
	        </salon:valeur>
	    </td>
	    <td class="tabDonnees">
	       <salon:DBselection valeur="<%= (String) null %>" sql="select CD_TYP_MVT, LIB_TYP_MVT || ' (' || SENS_MVT::varchar || ')' from TYP_MVT order by LIB_TYP_MVT">
		     <select name="CD_TYP_MVT" onchange="ChangeMvt(true)">
			%%
		     </select>
	       </salon:DBselection>
	    </td>
	    <td class="Nombre">
	       <input class="Nombre" type="text" name="QTE" value="1" size="3">
	    </td>
	    <td class="Nombre">
                <salon:valeur valeur="<%= aArt.getVAL_STK_HT() %>" valeurNulle="null">
                    <input class="Nombre" type="text" name="VAL_MVT_HT" value="%%" size=7 onchange="ChangeMvt(false)">
                </salon:valeur>
	    </td>
	    <td class="Nombre"><salon:valeur valeur="<%= aArt.getQTE_STK() %>" valeurNulle="null">
		  %%
	       </salon:valeur>
	    </td>
	    <td class="tabDonnees">
		  <a href="javascript:AjouterLigne(<%= i %>)"><img src=images/plus.gif width="15" height="15" border="0" alt="<i18n:message key="label.ajouterMouvement" />"></a>
	    </td>
	 </tr>
	 <%
	 for (i=0; i< listeMvt.size(); i++) {
	       MvtStkBean aMvt = (MvtStkBean) listeMvt.get(i);
	 %>
	 <tr class="ligneTab2">
	    <td class="tabDonnees">
	       <salon:valeur valeurNulle="null" valeur="<%= aMvt.getDT_MVT() %>">
		  %%
	       </salon:valeur>
	       <% if ((i == 0)  && (DebMvt == 0)) { %>
		  <salon:valeur valeurNulle="null" valeur="<%= aMvt.getDT_MVT() %>" timezone="true">
		     <input type="hidden" name="DT_MVT_LAST" value="%%">
		  </salon:valeur>
		  <salon:valeur valeurNulle="null" valeur="<%= aMvt.getCD_FACT() %>" >
		     <input type="hidden" name="CD_FACT_LAST" value="%%">
		  </salon:valeur>
	       <% } %>
	    </td>
            <td class="tabDonnees">
	    <% String LIB_TYP_MVT = TypMvtBean.getTypMvtBean(mySalon.getMyDBSession(), 
							       Integer.toString(aMvt.getCD_TYP_MVT())).toString(); %>
	    <%= LIB_TYP_MVT %>
            <% if ((aMvt.getCD_FACT() != 0) 
                    && (FactBean.getFactBean(mySalon.getMyDBSession(), Long.toString(aMvt.getCD_FACT()), mySalon.getMessagesBundle()) 
                        != null)){ %>
                <a href="_FicheFact.jsp?Action=Modification&CD_FACT=<%= aMvt.getCD_FACT() %>" target="ClientFrame" title="<i18n:message key="ficFact.title" />">
                <img src="images/fact.gif" border=0 align=top></a>
            <% } %>
            </td>
	    <td class="Nombre"><salon:valeur valeur="<%= aMvt.getQTE() %>" valeurNulle="null">
		  %%
	       </salon:valeur>
	    </td>
	    <td class="Nombre"><salon:valeur valeur="<%= aMvt.getVAL_MVT_HT() %>" valeurNulle="null">
			%%
	       </salon:valeur>
	    </td>
	    <td class="Nombre"><salon:valeur valeur="<%= aMvt.getSTK_AVANT() %>" valeurNulle="null">
		  %%
	       </salon:valeur>
	    </td>
	    <td class="tabDonnees">
	       <% if ((i==0) && (DebMvt > 0)) { %>
		     <a href="javascript:Precedent()"><img src="images/haut.gif" width="17" height="9" border="0" ></a>
	       <% }
	          else if ((i == 0)  && (DebMvt == 0)) { %>
		     <a href="javascript:SupprimerLigne()"><img src="images/moins.gif" width="15" height="15" border="0" alt="<i18n:message key="label.supprimerMouvement" />"></a>
	       <% } 
	          else if ((i+1) == listeMvt.size()) { %>
		     <a href="javascript:Suivant()"><img src="images/bas.gif" border="0" width="17" height="9" ></a>
	       <% } 
	          else {%>
		      &nbsp;
	       <% } %>
	   </td>
	 </tr>
	 <%
	 }
	 %>
   </table>
</form>

<script language="JavaScript">
function set(Champs)
{
}

ChangeMvt(true);

// Fonctions d'action

function ChangeMvt(full) {
   
   for (i = 0; i < document.fiche.CD_TYP_MVT.options.length; i++) {
      if (document.fiche.CD_TYP_MVT.options[i].selected) {
	 if (document.fiche.CD_TYP_MVT.options[i].text.indexOf("(S)") != -1) {
	    val_mvt = "<%= aArt.getVAL_STK_HT() %>";
	    if (val_mvt != document.fiche.VAL_MVT_HT.value) {
	       if (confirm ("Voulez-vous utiliser la valeur actuelle du stock ?") == 1) {   
		  document.fiche.VAL_MVT_HT.value = val_mvt;
	       }
	    }
	    if (full) {
	    <%
	    BigDecimal dec = new BigDecimal(0);
            if (aArt.getQTE_STK() != null) {
                dec = aArt.getQTE_STK().add(aArt.getQTE_STK().setScale(0, BigDecimal.ROUND_FLOOR).negate());
            }

	    if (dec.compareTo(new BigDecimal(0)) == 0) { %>
	       document.fiche.QTE.value = "1";
	    <% }
	    else { %>
	       document.fiche.QTE.value = "<%= dec %>";
	    <% } %>
	    }
	 }
	 else {
	    if (full) {
	       document.fiche.QTE.value = "1";
	    }
	 }
      }
   }
}

// Contrôle des données avant enregistrement
function ControleEnreg ()
{
   // Verification des données obligatoires
   if (document.fiche.LIB_ART.value == "") {
      alert ("<i18n:message key="ficArt_Mvt.libelleManquant" />");
      return false;
   }
   return true;
}

// Supprime le dernier mouvement
function SupprimerLigne()
{
   document.fiche.Action.value = "SuppressionLigne";
   document.fiche.submit();
}

// Ajoute une ligne de prestation
function AjouterLigne(NumPrest)
{
   ChangeMvt(false);

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
   if (document.fiche.LIB_ART.value == "") {
      alert ("<i18n:message key="ficArt_Mvt.libelleManquant" />");
      return;
   }
   document.fiche.submit();
}

// Suppression du client
function Supprimer()
{
    if ((document.fiche.CD_ART.value != "0") && (document.fiche.CD_ART.value != "")) {
        if (confirm ("<i18n:message key="message.suppressionDefinitiveConfirm" />")) {
            document.fiche.Action.value = "Suppression";
            document.fiche.submit();
        }
    }
}

// Duplication de la prestation
function Dupliquer()
{
   document.fiche.Action.value = "Duplication";
   document.fiche.submit();
}

function Suivant()
{
   document.fiche.Action.value = "Suivant";
   document.fiche.submit();
}

function Precedent()
{
   document.fiche.Action.value = "Precedent";
   document.fiche.submit();
}

function RetourListe()
{
   if (document.fiche.LIB_ART.value != "") {
      parent.location.href = "ListeArt.jsp?premLettre=" + document.fiche.LIB_ART.value.charAt(0).toUpperCase();
   }
   else {
      parent.location.href = "ListeArt.jsp?premLettre=A";
   }
}

// Affichage de l'aide
function Aide()
{
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideFicheArt.html");
}

</script>
</body>
</html>
