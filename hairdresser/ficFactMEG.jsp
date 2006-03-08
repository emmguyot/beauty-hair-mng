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
<%@ page import="java.util.Vector,
	       java.util.List" %>
<%@ page import="com.increg.salon.bean.SalonSession,
	       com.increg.salon.bean.FactBean,
	       com.increg.salon.bean.PaiementBean,
	       com.increg.salon.bean.ClientBean,
	       com.increg.salon.bean.DonneeRefBean,
	       com.increg.salon.bean.DeviseBean" %>
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
<title><i18n:message key="ficFactMEG.title" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees" onLoad="Init()">
<%@ include file="include/commun.jsp" %>
<script language="JavaScript">
<!--
<%
   // Récupération des paramètres
   String Action = (String) request.getAttribute("Action");
   String NbPrest = (String) request.getAttribute("NbPrest");
   FactBean aFact = (FactBean) request.getAttribute("FactBean");
   PaiementBean aPaiement = (PaiementBean) request.getAttribute("PaiementBean");
   String totPrest = (String) request.getAttribute("totPrest");
   List collabs = (List) request.getAttribute("collabs");
%>
   var Action="<%=Action%>";

function Init() {
    MM_showHideLayers('VALIDER?bottomFrame','','show');
    MM_showHideLayers('ENREGISTRER?bottomFrame','','hide');
    MM_showHideLayers('SUPPRIMER?bottomFrame','','hide');
    MM_showHideLayers('IMPRIMER?bottomFrame','','hide');
    document.fiche.MOT_PASSE.focus();
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
    <input type="hidden" name="Action" value="Suppression+">

    <table width="100%"> 
        <tr>
            <td class="label"><i18n:message key="label.client" /> : </td>
            <td> 
            <%
                ClientBean aCli = ClientBean.getClientBean(mySalon.getMyDBSession(), Long.toString(aFact.getCD_CLI()), mySalon.getMessagesBundle());
            %>
                <span class="readonly"><a href="_FicheCli.jsp?Action=Modification&CD_CLI=<%= aFact.getCD_CLI() %>" target="ClientFrame"><%= aCli.toString() %></a></span> 
            </td>
            <td class="label"><i18n:message key="label.dtPrest" /> : </td>
            <td>
                <salon:valeur valeurNulle="null" valeur="<%= aFact.getDT_PREST() %>" >
                    <span class="readonly">%%</span>
                </salon:valeur>
            </td>
        </tr>
        <tr>
            <td class="label"><h2><i18n:message key="ficFact.totalPayer" /> : </h2></td>
            <td><h2><salon:valeur valeurNulle="null" valeur="<%= aFact.getPRX_TOT_TTC() %>" >
                        <span class="readonly">%% <%= mySalon.getDevise().toString() %></span>
                    </salon:valeur>
               <%
                    // Boucle sur les autres devises
                    Vector lstDevise = mySalon.getLstAutresDevises();
                    for (int i = 0; i < lstDevise.size(); i++) {
                        DeviseBean aDevise = (DeviseBean) lstDevise.get(i);
               %>= 
                        <salon:valeur valeurNulle="null" valeur="<%= aDevise.convertiMontant(aFact.getPRX_TOT_TTC()) %>" >
                            <span class="readonly">%% <%= aDevise.toString() %></span>
                        </salon:valeur><%
                    } // for 
                    %>
            </h2></td>
            <td class="label"><i18n:message key="ficFact.dontTVA" /> : </td>
            <td class="tabDonneesGauche">
                <salon:valeur valeurNulle="null" valeur="<%= aFact.getTVA() %>" > 
                    <span class="readonly">%% <%= mySalon.getDevise().toString() %></span>
                </salon:valeur>
            </td>
	</tr>
	<tr>
	<td class="label"><i18n:message key="label.modePaiement" /> : </td>
	<td>
	    <salon:valeur valeurNulle="0" valeur="<%= aPaiement.getCD_PAIEMENT() %>" >
	       <input type="hidden" name="CD_PAIEMENT" value="%%" >
	    </salon:valeur>
	    <% long nbFact = aPaiement.getFact(mySalon.getMyDBSession()).size();
	        %>
                <salon:valeur valeur='<%= aPaiement.getCD_MOD_REGL() %>' valeurNulle="null">
                    <input type="hidden" name="CD_MOD_REGL" value="%%">
                </salon:valeur>
                <salon:valeur valeur='<%= DonneeRefBean.getDonneeRefBean(mySalon.getMyDBSession(), "MOD_REGL", Integer.toString(aPaiement.getCD_MOD_REGL())).toString() %>' valeurNulle="null">
                    <span class="readonly">%%</span>
                </salon:valeur>
            <% if (nbFact > 1) { %>
                    <a href="_FichePaiement.jsp?Action=Modification&CD_PAIEMENT=<%= aPaiement.getCD_PAIEMENT() %>" target="ClientFrame">Paiements regroupés</a> 
            <% } %>
        </td>
	<td class="label"><i18n:message key="ficFact.dtPaiement" /> : </td>
	<td>
            <salon:valeur valeurNulle="null" valeur="<%= aPaiement.getDT_PAIEMENT() %>" > 
                <input type="hidden" name="DT_PAIEMENT" value="%%">
                <span class="readonly">%%</span>
            </salon:valeur>
	</td>
	</tr>
	</table>
        
    <hr>
   <p class="warning"><span class="big"><i18n:message key="ficFactMEG.sousTitre" /></span></p>
   <p class="big"><i18n:message key="ficFactMEG.texte" /></p>
   <p class="big"><i18n:message key="ficFactMEG.texte2" /> :
   
   <input type="password" name="MOT_PASSE"></p>
</form>

<script language="JavaScript">

// Validation du mot de passe
function Valider()
{
    document.fiche.submit();
}

// Affichage de l'aide
function Aide()
{
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideFicheFact.html");
}

</script>
</body>
</html>
