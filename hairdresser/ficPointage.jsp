<%@ page import="com.increg.salon.bean.SalonSession,
                com.increg.salon.bean.CollabBean,
	        com.increg.salon.bean.PointageBean
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
   // R�cup�ration des param�tres
   String Action = (String) request.getAttribute("Action");
   PointageBean aPointage = (PointageBean) request.getAttribute("PointageBean");
%>
<title>Fiche Pointage</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees" onLoad="Init();
   <% if (Action.equals("Creation")) { %>
      document.fiche.CD_COLLAB.focus()
   <% } else { %>
      document.fiche.DT_FIN.focus()
   <% } %> ">
<%@ include file="include/commun.js" %>
<script language="JavaScript">
<!--
   var Action="<%=Action%>";

function Init() {
   <%
   // Positionne les liens d'actions
   if (! Action.equals("Creation")) {
      %>
      MM_showHideLayers('SUPPRIMER?bottomFrame','','show');
      MM_showHideLayers('ENREGISTRER?bottomFrame','','show');
      <%
   }
   if (Action.equals("Creation")) { %>
      // Pas de lien supprimer
      MM_showHideLayers('SUPPRIMER?bottomFrame','','hide');
      MM_showHideLayers('ENREGISTRER?bottomFrame','','show');
   <%
   } %>
   MM_showHideLayers('NOUVEAU?bottomFrame','','show');
   MM_showHideLayers('RETOUR_LISTE?bottomFrame','','show');
}
//-->
</script>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/ficPointage.gif" alt=<salon:TimeStamp bean="<%= aPointage %>" />></h1>
<salon:message salonSession="<%= mySalon %>" />
<form method="post" action="ficPointage.srv" name="fiche">
	 <p><span class="obligatoire">Collaborateur :</span> 
	    <% if (Action.equals("Creation")) { %>
		<salon:DBselection valeur="<%= aPointage.getCD_COLLAB() %>" sql="select CD_COLLAB, PRENOM from COLLAB order by PRENOM, NOM">
		  <select name="CD_COLLAB">
		     %%
		  </select>
	        </salon:DBselection>
	    <% } 
	       else { %>
		  <salon:valeur valeurNulle="null" valeur="<%= aPointage.getCD_COLLAB() %>" >
		     <input type="hidden" name="CD_COLLAB" value="%%">
		  </salon:valeur>
		  <salon:valeur valeurNulle="null" valeur="<%= CollabBean.getCollabBean(mySalon.getMyDBSession(), Integer.toString(aPointage.getCD_COLLAB())).toString() %>" >
		     <span class="readonly">%%</span>
		  </salon:valeur>

	    <% } %>
		<input type="hidden" name="Action" value="<%=Action%>">
	 </p>
	 <p><span class="obligatoire">D�but :</span> 
       	<%
		if (Action.equals("Creation")) { %>
			<salon:date type="text" name="DT_DEBUT" valeurDate="<%= aPointage.getDT_DEBUT() %>" valeurNulle="null" format="dd/MM/yyyy" calendrier="true" onchange="synchroDates()">%%</salon:date>
			<salon:date type="text" name="HR_DEBUT" valeurNulle="null" valeurDate="<%= aPointage.getDT_DEBUT() %>" format="HH:mm">%%</salon:date>
       	<%
       	} 
	  	else { %>
            <salon:date type="readonly" name="DT_DEBUT" valeurDate="<%= aPointage.getDT_DEBUT() %>" valeurNulle="null" format="dd/MM/yyyy">%%</salon:date>
            <salon:date type="readonly" name="HR_DEBUT" valeurNulle="null" valeurDate="<%= aPointage.getDT_DEBUT() %>" format="HH:mm:ss">%%</salon:date>
       	<%
       	} %>
	    <span class="facultatif">Fin :</span> 
        <salon:date type="text" name="DT_FIN" valeurDate="<%= aPointage.getDT_FIN() %>" valeurNulle="null" format="dd/MM/yyyy" calendrier="true">%%</salon:date>
        <salon:date type="text" name="HR_FIN" valeurDate="<%= aPointage.getDT_FIN() %>" valeurNulle="null" format="HH:mm">%%</salon:date>
	 </p>
	 <p><span class="obligatoire">Type de pointage :</span> 
		<salon:DBselection valeur="<%= aPointage.getCD_TYP_POINTAGE() %>" sql="select CD_TYP_POINTAGE, LIB_TYP_POINTAGE from TYP_POINTAGE order by LIB_TYP_POINTAGE">
		  <select name="CD_TYP_POINTAGE">
		     %%
		  </select>
	        </salon:DBselection>
	</p>
	<p>
	<span class="facultatif">Commentaire :</span> 
	    <salon:valeur valeurNulle="null" valeur="<%= aPointage.getCOMM() %>" >
		<textarea name="COMM" cols="40" rows="2">%%</textarea>
	    </salon:valeur>
	</p>
</form>

<script language="JavaScript">
// Fonctions d'action

// Synchronises les dates d�but et fin
function synchroDates() {
	document.fiche.DT_FIN.value = document.fiche.DT_DEBUT.value;
}

// Enregistrement des donn�es
function Enregistrer()
{
   // Verification des donn�es obligatoires
   if ((document.fiche.DT_DEBUT.value == "") || (document.fiche.HR_DEBUT.value == "")) {
      alert ("La date de d�but du pointage doit �tre saisie");
      return;
   }
   document.fiche.submit();
}

// Cr�ation d'un nouveau pointage
function Nouveau()
{
   parent.location.href = "_FichePointage.jsp";
}

// Suppression
function Supprimer()
{
    if (document.fiche.DT_DEBUT.value != "") {
        if (confirm ("Cette suppression est d�finitive. Confirmez-vous cette action ?")) {
            document.fiche.Action.value = "Suppression";
            document.fiche.submit();
        }
    }
}

// Affichage de l'aide
function Aide()
{
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideFiche.html");
}

function RetourListe()
{
    parent.location.href = "ListePointage.jsp";
}


</script>
</body>
</html>
