<%@ page import="java.util.Vector, java.util.List" %>
<%@ page import="com.increg.salon.bean.SalonSession,
	       com.increg.salon.bean.ClientBean,
	       com.increg.salon.bean.RDVBean
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
<%
    // Récupération des paramètres
    String Action = (String) request.getAttribute("Action");
    RDVBean aRDV = (RDVBean) request.getAttribute("RDVBean");
    ClientBean aCli = ClientBean.getClientBean(mySalon.getMyDBSession(), Long.toString(aRDV.getCD_CLI()));
    Vector dispo = (Vector) request.getAttribute("Dispo");
    List collabs = (List) request.getAttribute("collabs");
%>
<title>Fiche Rendez-vous</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees" onLoad="Init()">
<%@ include file="include/commun.js" %>
<script language="JavaScript">
<!--
   var Action="<%=Action%>";

function Init() {
   <%
   // Positionne les liens d'actions '
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
   MM_showHideLayers('RETOUR_LISTE?bottomFrame','','show');
   MM_showHideLayers('NOUVEAU?bottomFrame','','show');
   document.fiche.CD_COLLAB.focus();
}
//-->
</script>
<h1><img src="images/titres/ficRDV.gif" alt=<salon:TimeStamp bean="<%= aRDV %>" />></h1>
<salon:message salonSession="<%= mySalon %>" />

<form method="post" action="ficRDV.srv" name="fiche">
    <table><tr><td valign="top">
	 <p><span class="obligatoire">Client :</span> 
            <input type="hidden" name="CD_CLI" value="<%= aRDV.getCD_CLI() %>">
            <input type="hidden" name="Action" value="<%= Action %>">
            <salon:valeur valeurNulle="null" valeur="<%= aCli.toString() %>" >
                <span class="readonly"><a href="_FicheCli.jsp?Action=Modification&CD_CLI=<%= aCli.getCD_CLI() %>" target="ClientFrame">%%</a></span> 
            </salon:valeur>
            <span class="obligatoire">Collaborateur :</span> 
		    <salon:selection valeur="<%= aRDV.getCD_COLLAB() %>" valeurs="<%= collabs %>">
		       <select name="CD_COLLAB" onchange="RechargeDispo()">
				  %%
		       </select>
		    </salon:selection>
	 </p>
	 <p><span class="obligatoire">Début :</span> 
            <%
            if (Action.equals("Creation")) { %>
                <salon:date type="text" name="DT_DEBUT" valeurDate="<%= aRDV.getDT_DEBUT() %>" valeurNulle="null" format="dd/MM/yyyy" calendrier="true" onchange="RechargeDispo()">%%</salon:date>
                <salon:date type="text" name="HR_DEBUT" valeurNulle="null" valeurDate="<%= aRDV.getDT_DEBUT() %>" format="HH:mm">%%</salon:date>
            <%
            } 
            else { %>
                <salon:date type="readonly" name="DT_DEBUT" valeurDate="<%= aRDV.getDT_DEBUT() %>" valeurNulle="null" format="dd/MM/yyyy">%%</salon:date>
                <salon:date type="readonly" name="HR_DEBUT" valeurNulle="null" valeurDate="<%= aRDV.getDT_DEBUT() %>" format="HH:mm">%%</salon:date>
            <%
            } %>
	    <span class="obligatoire">Durée (minutes) :</span> 
	       <salon:valeur valeurNulle="null" valeur="<%= aRDV.getDUREE() %>">
		  <input type="text" name="DUREE" size="3" value="%%">
	       </salon:valeur>
	 </p>
	<p>
	<span class="facultatif">Commentaire :</span> 
	    <salon:valeur valeurNulle="null" valeur="<%= aRDV.getCOMM() %>" >
		<textarea name="COMM" cols="40" rows="2">%%</textarea>
	    </salon:valeur>
	</p>
        <p>Commentaire du client :
	    <salon:valeur valeurNulle="null" valeur="<%= aCli.getCOMM() %>" expand="true">
		<span class="readonly">%%</span>
	    </salon:valeur>
        </p>
    </td>
    <td valign="top" style="padding-left: 30px">
        <p>Disponibilités :</p>
        <table>
        <%
            for (int i = 0; i < dispo.size(); i++) {
                RDVBean dispoRDV = (RDVBean) dispo.get(i);
        %>
                <tr><td>
                    <salon:date type="readonly" name="HR_DEBUT" valeurNulle="null" valeurDate="<%= dispoRDV.getDT_DEBUT() %>" format="HH:mm">%%</salon:date>
                    ...
                    <salon:date type="readonly" name="HR_DEBUT" valeurNulle="null" valeurDate="<%= dispoRDV.getDT_FIN() %>" format="HH:mm">%%</salon:date>
                
                </td></tr>
        <%
            }
        %>
        </table>
    </td></tr></table>
</form>


<script language="JavaScript">
// Fonctions d'action
lock = false;

// Enregistrement des données
function Enregistrer()
{
   // Verification des données obligatoires
   if ((document.fiche.DT_DEBUT.value == "") || (document.fiche.HR_DEBUT.value == "")) {
      alert ("La date de début du pointage doit être saisie");
      return;
   }
   lock = true;
   document.fiche.submit();
}

// Suppression
function Supprimer()
{
    if (document.fiche.DT_DEBUT.value != "") {
        if (confirm ("Cette suppression est définitive. Confirmez-vous cette action ?")) {
            document.fiche.Action.value = "Suppression";
            document.fiche.submit();
        }
    }
}

function RetourListe() {
    if (document.fiche.DT_DEBUT.value != "") {
        parent.location.href = "ListeRDV.jsp?DT_DEBUT=" + doEscape(document.fiche.DT_DEBUT.value + " 00:00") 
                + "&DT_FIN=" + doEscape(document.fiche.DT_DEBUT.value + " 23:59");
    }
    else {
        parent.location.href = "ListeRDV.jsp";
    }
}

function RechargeDispo() {
    setTimeout("RechargeDispoReel()", 100);
}

function RechargeDispoReel() {
	if (!lock) {
		document.fiche.Action.value = "Rechargement";
		document.fiche.submit();
	}
}

// Affichage de l'aide
function Aide()
{
    window.open("aideFiche.html");
}

function Nouveau()
{
    // La création se fait à partir de la liste des clients
    parent.location.href = "ListeCli.jsp";
}

</script>
</body>
</html>
