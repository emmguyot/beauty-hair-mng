<%@ page import="com.increg.salon.bean.SalonSession
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
<title>Epuration des données</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees" onLoad="document.fiche.Date.focus()">
<%@ include file="include/commun.js" %>
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
    <p class="warning">Attention : L'épuration de données n'est réversible que<br>si vous avez effectué préalablement une sauvegarde.</p>
    <p>
        <input type="hidden" name="Action" value="Purge">
        <input type="hidden" name="lock" value="">
        <span class="obligatoire">Epuration jusqu'au :</span> 
        <salon:valeur valeur="<%= Date %>" valeurNulle="null">
            <input type="text" name="Date" value="%%" size="11" maxlength="10" onChange="FormateDate(this)">
        </salon:valeur>
    </p>
    <p>Données à épurer : <br>
    <blockquote>
        <input type="checkbox" name="Paiement" <%
            if (Paiement.booleanValue()) { %>
                checked
            <%
            }
            %> >Paiements<br>
        <input type="checkbox" name="Histo_prest" <%
            if (Histo_prest.booleanValue()) { %>
                checked
            <%
            }
            %> >Historique des prestations et factures<br>
        <input type="checkbox" name="RDV" <%
            if (RDV.booleanValue()) { %>
                checked
            <%
            }
            %> >Rendez-vous<br>
        <input type="checkbox" name="Mvt_caisse" <%
            if (Mvt_caisse.booleanValue()) { %>
                checked
            <%
            }
            %> >Mouvements caisse<br>
        <input type="checkbox" name="Mvt_stk" <%
            if (Mvt_stk.booleanValue()) { %>
                checked
            <%
            }
            %> >Mouvements de stock<br>
        <input type="checkbox" name="Client" <%
            if (Client.booleanValue()) { %>
                checked
            <%
            }
            %> >Clients sans historique<br>
        <input type="checkbox" name="Art" <%
            if (Art.booleanValue()) { %>
                checked
            <%
            }
            %> >Articles inutiles<br>
        <input type="checkbox" name="Prest" <%
            if (Prest.booleanValue()) { %>
                checked
            <%
            }
            %> >Prestations inutiles<br>
        <input type="checkbox" name="Client_perime" <%
            if (Client_perime.booleanValue()) { %>
                checked
            <%
            }
            %> >Clients périmés sans historique<br>
        <input type="checkbox" name="Pointage" <%
            if (Pointage.booleanValue()) { %>
                checked
            <%
            }
            %> >Pointages<br>
        <input type="checkbox" name="Collab" <%
            if (Collab.booleanValue()) { %>
                checked
            <%
            }
            %> >Anciens collaborateurs
    </blockquote>
</form>
<span id="AttenteSpan" style="visibility: hidden">
<p class="Warning"><img name="Attente" src="images/attente.gif" width="231" height="10" alt="Opération en cours..."></p>
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
        alert ("La date limite d'épuration doit être saisie.");
        return;
    }
    if (document.fiche.lock.value == "") {
        if (confirm("Attention vous allez supprimer les données existantes antérieures à la date saisie. Etes-vous sûr de vouloir lancer l'épuration ?")) {
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
