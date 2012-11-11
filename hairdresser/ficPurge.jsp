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
<%@ page import="com.increg.salon.bean.SalonSession
	       " %>
<%
SalonSession mySalon = com.increg.salon.servlet.ConnectedServlet.CheckOrGoHome(request, response);
%>
<%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %>
<%@ taglib uri="WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<i18n:bundle baseName="messages" locale="<%= mySalon.getLangue() %>"/>
<html>
<head>
<%
   // Récupération des paramètres
   Boolean Paiement = (Boolean) request.getAttribute("Paiement");
   Boolean Histo_prest = (Boolean) request.getAttribute("Histo_prest");
   Boolean RDV = (Boolean) request.getAttribute("RDV");
   Boolean Mvt_caisse = (Boolean) request.getAttribute("Mvt_caisse");
   Boolean Mvt_stk = (Boolean) request.getAttribute("Mvt_stk");
   Boolean Client = (Boolean) request.getAttribute("Client");
   Boolean Art = (Boolean) request.getAttribute("Art");
   Boolean Prest = (Boolean) request.getAttribute("Prest");
   Boolean Client_perime = (Boolean) request.getAttribute("Client_perime");
   Boolean Pointage = (Boolean) request.getAttribute("Pointage");
   Boolean Collab = (Boolean) request.getAttribute("Collab");
   String MsgInfo = (String) request.getAttribute("MsgInfo");
   String Date = request.getParameter("Date");
%>
<title><i18n:message key="ficPurge.title" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
<link rel="stylesheet" href="style/jquery-ui-1.8.16.custom.css" type="text/css">
</head>
<body class="donnees" onLoad="document.fiche.Date.focus()">
<%@ include file="include/commun.jsp" %>
<script language="JavaScript">
<!--
function Init() {
      MM_showHideLayers('SUPPRIMER?bottomFrame','','hide');
      MM_showHideLayers('ENREGISTRER?bottomFrame','','hide');
      MM_showHideLayers('DUPLIQUER?bottomFrame','','hide');
      MM_showHideLayers('VALIDER?bottomFrame','','show');
}
//-->
</script>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/ficPurge.gif"></h1>
<salon:message salonSession="<%= mySalon %>" />
<form method="post" action="ficPurge.srv" name="fiche">
    <p class="warning"><i18n:message key="ficPurge.warning" /></p>
    <p>
        <input type="hidden" name="Action" value="Purge">
        <input type="hidden" name="lock" value="">
        <span class="obligatoire"><i18n:message key="label.limiteEpuration" /> :</span> 
        <salon:valeur valeur="<%= Date %>" valeurNulle="null">
            <input type="text" name="Date" value="%%" size="11" maxlength="10" onChange="FormateDate(this)">
        </salon:valeur>
    </p>
    <p><i18n:message key="label.donneeEpurees" /> : <br>
    <blockquote>
        <input type="checkbox" name="Paiement" <%
            if (Paiement.booleanValue()) { %>
                checked
            <%
            }
            %> ><i18n:message key="label.paiement" /><br>
        <input type="checkbox" name="Histo_prest" <%
            if (Histo_prest.booleanValue()) { %>
                checked
            <%
            }
            %> ><i18n:message key="label.historiquePrestFact" /><br>
        <input type="checkbox" name="RDV" <%
            if (RDV.booleanValue()) { %>
                checked
            <%
            }
            %> ><i18n:message key="label.RDV" /><br>
        <input type="checkbox" name="Mvt_caisse" <%
            if (Mvt_caisse.booleanValue()) { %>
                checked
            <%
            }
            %> ><i18n:message key="label.mvtCaisse" /><br>
        <input type="checkbox" name="Mvt_stk" <%
            if (Mvt_stk.booleanValue()) { %>
                checked
            <%
            }
            %> ><i18n:message key="label.mvtStock" /><br>
        <input type="checkbox" name="Client" <%
            if (Client.booleanValue()) { %>
                checked
            <%
            }
            %> ><i18n:message key="label.clientSsHisto" /><br>
        <input type="checkbox" name="Art" <%
            if (Art.booleanValue()) { %>
                checked
            <%
            }
            %> ><i18n:message key="label.articleInutiles" /><br>
        <input type="checkbox" name="Prest" <%
            if (Prest.booleanValue()) { %>
                checked
            <%
            }
            %> ><i18n:message key="label.prestationInutile" /><br>
        <input type="checkbox" name="Client_perime" <%
            if (Client_perime.booleanValue()) { %>
                checked
            <%
            }
            %> ><i18n:message key="label.clientPerime" /><br>
        <input type="checkbox" name="Pointage" <%
            if (Pointage.booleanValue()) { %>
                checked
            <%
            }
            %> ><i18n:message key="label.pointage" /><br>
        <input type="checkbox" name="Collab" <%
            if (Collab.booleanValue()) { %>
                checked
            <%
            }
            %> ><i18n:message key="label.collabAncien" />
    </blockquote>
</form>
<span id="AttenteSpan" style="visibility: hidden">
<p class="Warning"><img name="Attente" src="images/attente.gif" width="231" height="10" alt="<i18n:message key="label.operationEnCours" />"></p>
</span>
<salon:valeur valeur="<%= MsgInfo %>" valeurNulle="null" expand="true">
    %%
</salon:valeur>
<script language="JavaScript">
// Fonctions d'action

// Enregistrement des données du client
function Valider()
{
    if (document.fiche.Date.value == "") {
        alert ("<i18n:message key="ficPurge.limiteManquant" />");
        return;
    }
    if (document.fiche.lock.value == "") {
        if (confirm("<i18n:message key="ficPurge.warningAction" />")) {
            MM_showHideLayers('AttenteSpan','','show');
            document.fiche.lock.value = "xx";
            document.fiche.submit();
        }
   }
}

// Affichage de l'aide
function Aide()
{
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideFichePurge.html");
}

</script>
</body>
</html>
