<%@ page import="java.util.Vector" %>
<%@ page import="com.increg.salon.bean.SalonSession,
	       com.increg.salon.bean.StatBean" %>
<%
    SalonSession mySalon = (SalonSession) session.getAttribute("SalonSession");
    if (mySalon == null) {
        getServletConfig().getServletContext().getRequestDispatcher("/reconnect.html").forward(request, response);
    }
%>
<%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %>
<html>
<head>
<title>Graphe statistique</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees">
<%@ include file="include/commun.js" %>
<script language="JavaScript">
<!--
<%
   // Récupération des paramètres
   String Action = (String) request.getAttribute("Action");
   StatBean aStat = (StatBean) request.getAttribute("StatBean");
   Vector listeParam = (Vector) request.getAttribute("listeParam");
%>
   var Action="<%=Action%>";

function Init() {
   MM_showHideLayers('VALIDER?bottomFrame','','show');
   MM_showHideLayers('SUPPRIMER?bottomFrame','','hide');
   MM_showHideLayers('ENREGISTRER?bottomFrame','','hide');
   MM_showHideLayers('RETOUR_LISTE?bottomFrame','','show');

   Valider();
}
//-->
</script>
<salon:valeur valeurNulle="0" valeur="<%= aStat.getLIB_STAT() %>" >
    <h1><img src="images/titres/ficStat.gif" alt=<salon:TimeStamp bean="<%= aStat %>" />><br><span class="ssTitre">%%</span></h1>
</salon:valeur>
<salon:message salonSession="<%= mySalon %>" />
<form method="post" action="ficStat.srv" name="fiche">
		<salon:valeur valeurNulle="0" valeur="<%= aStat.getCD_STAT() %>" >
		  <input type="hidden" name="CD_STAT" value="%%" >
	        </salon:valeur>
		<salon:valeur valeurNulle="0" valeur="<%= aStat.getLIB_STAT() %>" >
		  <input type="hidden" name="LIB_STAT" value="%%" >
	        </salon:valeur>
		<salon:valeur valeurNulle="0" valeur="<%= aStat.getLABEL_X() %>" >
		  <input type="hidden" name="LABEL_X" value="%%" >
	        </salon:valeur>
		<salon:valeur valeurNulle="0" valeur="<%= aStat.getLABEL_Y() %>" >
		  <input type="hidden" name="LABEL_Y" value="%%" >
	        </salon:valeur>

	 <table>
		<%
                int nbItem = 5; %>
                <%
		if (listeParam.contains("PeriodeTemps")) { %>
		  <tr><td><span class="obligatoire">Période : </span></td>
                  <salon:selection valeur='<%= (String) request.getAttribute("PeriodeTemps$0") %>' valeurs='<%= "month|day" %>' libelle="Mois|Journée">
                    <td><select name="PeriodeTemps">
		     %%
                    </select></td>
		  </salon:selection>
		  </tr>
	        <% } %>
                <tr><td></td>
                <%
                for (int i = 0; i < nbItem; i++ ) { %>
                    <td>
                    <%
                    if (i == 0) { %>
                        <span class="obligatoire">
                    <%
                    }
                    else { %>
                        <span class="facultatif">
                    <%
                    } %>
                    Graphe N°<%= i+1 %></span></td>
                <%
                } %>
                </tr>
		<%
                if (listeParam.contains("DateDebut")) { %>
		  <tr><td><span class="obligatoire">Date&nbsp;de&nbsp;début&nbsp;: </span></td>
                  <%
                  for (int i = 0; i < nbItem; i++ ) { %>
		  <salon:valeur valeurNulle="null" valeur='<%= (String) request.getAttribute("DateDebut$" + i) %>' >
		     <td><input type="text" name="DateDebut$<%= i %>" value="%%" size=10 onChange="FormateDate(this)"></td>
		  </salon:valeur>
                  <% 
                  } %>
		  </tr>
	        <% } 
		if (listeParam.contains("DateFin")) { %>
		  <tr><td><span class="obligatoire">Date&nbsp;de&nbsp;fin&nbsp;: </span></td>
                  <%
                  for (int i = 0; i < nbItem; i++ ) { %>
		  <salon:valeur valeurNulle="null" valeur='<%= (String) request.getAttribute("DateFin$" + i) %>' >
		     <td><input type="text" name="DateFin$<%= i %>" value="%%" size=10 onChange="FormateDate(this)"></td>
		  </salon:valeur>
                  <% 
                  } %>
		  </tr>
	        <% } 
		if (listeParam.contains("CD_COLLAB")) { %>
		  <tr><td><span class="obligatoire">Collaborateur&nbsp;: </span></td>
                  <%
                  for (int i = 0; i < nbItem; i++ ) { %>
		  <salon:DBselection valeur='<%= (String) request.getAttribute("CD_COLLAB$" + i) %>' sql="select CD_COLLAB, PRENOM from COLLAB order by PRENOM, NOM">
		     <td><select name="CD_COLLAB$<%= i %>">
			%%
		     </select></td>
		  </salon:DBselection>
                  <% 
                  } %>
		  </tr>
	        <% } 
		if (listeParam.contains("CD_TYP_CHEV")) { %>
		  <tr><td><span class="obligatoire">Type&nbsp;de&nbsp;peau&nbsp;: </span></td>
                  <%
                  for (int i = 0; i < nbItem; i++ ) { %>
		  <salon:DBselection valeur='<%= (String) request.getAttribute("CD_TYP_CHEV$" + i) %>' sql="select CD_TYP_CHEV, LIB_TYP_CHEV from TYP_CHEV order by LIB_TYP_CHEV">
		     <td><select name="CD_TYP_CHEV$<%= i %>">
			%%
		     </select></td>
		  </salon:DBselection>
                  <% 
                  } %>
		  </tr>
	        <% } 
		if (listeParam.contains("CD_CATEG_CLI")) { %>
		  <tr><td><span class="obligatoire">Catégorie&nbsp;de&nbsp;clients&nbsp;: </span></td>
                  <%
                  for (int i = 0; i < nbItem; i++ ) { %>
		  <salon:DBselection valeur='<%= (String) request.getAttribute("CD_CATEG_CLI$" + i) %>' sql="select CD_CATEG_CLI, LIB_CATEG_CLI from CATEG_CLI order by LIB_CATEG_CLI">
		     <td><select name="CD_CATEG_CLI$<%= i %>">
			%%
		     </select></td>
		  </salon:DBselection>
                  <% 
                  } %>
		  </tr>
	        <% } 
		if (listeParam.contains("CD_TR_AGE")) { %>
		  <tr><td><span class="obligatoire">Tranche&nbsp;d'âge&nbsp;: </span></td>
                  <%
                  for (int i = 0; i < nbItem; i++ ) { %>
		  <salon:DBselection valeur='<%= (String) request.getAttribute("CD_TR_AGE$" + i) %>' sql="select CD_TR_AGE, LIB_TR_AGE from TR_AGE order by AGE_MIN">
		     <td><select name="CD_TR_AGE$<%= i %>">
			%%
		     </select></td>
		  </salon:DBselection>
                  <% 
                  } %>
		  </tr>
	        <% } 
		if (listeParam.contains("CD_ORIG")) { %>
		  <tr><td><span class="obligatoire">Origine&nbsp;du&nbsp;client&nbsp;: </span></td>
                  <%
                  for (int i = 0; i < nbItem; i++ ) { %>
		  <salon:DBselection valeur='<%= (String) request.getAttribute("CD_ORIG$" + i) %>' sql="select CD_ORIG, LIB_ORIG from ORIG order by LIB_ORIG">
		     <td><select name="CD_ORIG$<%= i %>">
			%%
		     </select></td>
		  </salon:DBselection>
                  <% 
                  } %>
		  </tr>
	        <% } 
		if (listeParam.contains("CD_MOD_REGL")) { %>
		  <tr><td><span class="obligatoire">Mode&nbsp;de&nbsp;réglement&nbsp;: </span></td>
                  <%
                  for (int i = 0; i < nbItem; i++ ) { %>
		  <salon:DBselection valeur='<%= (String) request.getAttribute("CD_MOD_REGL$" + i) %>' sql="select CD_MOD_REGL, LIB_MOD_REGL from MOD_REGL order by LIB_MOD_REGL">
		     <td><select name="CD_MOD_REGL$<%= i %>">
			%%
		     </select></td>
		  </salon:DBselection>
                  <% 
                  } %>
		  </tr>
	        <% } 
		if (listeParam.contains("CD_TYP_VENT")) { %>
		  <tr><td><span class="obligatoire">Type&nbsp;de&nbsp;prestation&nbsp;: </span></td>
                  <%
                  for (int i = 0; i < nbItem; i++ ) { %>
		  <salon:DBselection valeur='<%= (String) request.getAttribute("CD_TYP_VENT$" + i) %>' sql="select CD_TYP_VENT, LIB_TYP_VENT from TYP_VENT order by LIB_TYP_VENT">
		     <td><select name="CD_TYP_VENT$<%= i %>">
			%%
		     </select></td>
		  </salon:DBselection>
                  <% 
                  } %>
		  </tr>
	        <% } 
		if (listeParam.contains("CD_CATEG_PREST")) { %>
		  <tr><td><span class="obligatoire">Catégorie&nbsp;de&nbsp;prestation&nbsp;: </span></td>
                  <%
                  for (int i = 0; i < nbItem; i++ ) { %>
		  <salon:DBselection valeur='<%= (String) request.getAttribute("CD_CATEG_PREST$" + i) %>' sql="select CD_CATEG_PREST, LIB_CATEG_PREST from CATEG_PREST order by LIB_CATEG_PREST">
		     <td><select name="CD_CATEG_PREST$<%= i %>">
			%%
		     </select></td>
		  </salon:DBselection>
                  <% 
                  } %>
		  </tr>
	        <% } 
		if (listeParam.contains("CD_TYP_ART")) { %>
		  <tr><td><span class="obligatoire">Type&nbsp;d'article&nbsp;: </span></td>
                  <%
                  for (int i = 0; i < nbItem; i++ ) { %>
		  <salon:DBselection valeur='<%= (String) request.getAttribute("CD_TYP_ART$" + i) %>' sql="select CD_TYP_ART, LIB_TYP_ART from TYP_ART order by LIB_TYP_ART">
		     <td><select name="CD_TYP_ART$<%= i %>">
			%%
		     </select></td>
		  </salon:DBselection>
                  <% 
                  } %>
		  </tr>
	        <% } 
		if (listeParam.contains("CD_CATEG_ART")) { %>
		  <tr><td><span class="obligatoire">Catégorie&nbsp;d'article&nbsp;: </span></td>
                  <%
                  for (int i = 0; i < nbItem; i++ ) { %>
		  <salon:DBselection valeur='<%= (String) request.getAttribute("CD_CATEG_ART$" + i) %>' sql="select CD_CATEG_ART, LIB_CATEG_ART from CATEG_ART order by LIB_CATEG_ART">
		     <td><select name="CD_CATEG_ART$<%= i %>">
			%%
		     </select></td>
		  </salon:DBselection>
                  <% 
                  } %>
		  </tr>
	        <% } 
		if (listeParam.contains("CD_MARQUE")) { %>
		  <tr><td><span class="obligatoire">Marque&nbsp;: </span></td>
                  <%
                  for (int i = 0; i < nbItem; i++ ) { %>
		  <salon:DBselection valeur='<%= (String) request.getAttribute("CD_MARQUE$" + i) %>' sql="select CD_MARQUE, LIB_MARQUE from MARQUE order by LIB_MARQUE">
		     <td><select name="CD_MARQUE$<%= i %>">
			%%
		     </select></td>
		  </salon:DBselection>
                  <% 
                  } %>
		  </tr>
	        <% } 
		if (listeParam.contains("CD_TYP_MVT")) { %>
		  <tr><td><span class="obligatoire">Type&nbsp;de&nbsp;mouvement&nbsp;de&nbsp;stock&nbsp;: </span></td>
                  <%
                  for (int i = 0; i < nbItem; i++ ) { %>
		  <salon:DBselection valeur='<%= (String) request.getAttribute("CD_TYP_MVT$" + i) %>' sql="select CD_TYP_MVT, LIB_TYP_MVT from TYP_MVT order by LIB_TYP_MVT">
		     <td><select name="CD_TYP_MVT$<%= i %>">
			%%
		     </select></td>
		  </salon:DBselection>
                  <% 
                  } %>
		  </tr>
	        <% } 
		if (listeParam.contains("CD_TYP_MCA")) { %>
		  <tr><td><span class="obligatoire">Type&nbsp;de&nbsp;mouvement&nbsp;de&nbsp;caisse&nbsp;: </span></td>
                  <%
                  for (int i = 0; i < nbItem; i++ ) { %>
		  <salon:DBselection valeur='<%= (String) request.getAttribute("CD_TYP_MCA$" + i) %>' sql="select CD_TYP_MCA, LIB_TYP_MCA from TYP_MCA order by LIB_TYP_MCA">
		     <td><select name="CD_TYP_MCA$<%= i %>">
			%%
		     </select></td>
		  </salon:DBselection>
                  <% 
                  } %>
		  </tr>
	        <% } 
		if (listeParam.contains("CD_FCT")) { %>
		  <tr><td><span class="obligatoire">Type&nbsp;de&nbsp;mouvement&nbsp;de&nbsp;caisse&nbsp;: </span></td>
                  <%
                  for (int i = 0; i < nbItem; i++ ) { %>
		  <salon:DBselection valeur='<%= (String) request.getAttribute("CD_FCT$" + i) %>' sql="select CD_FCT, LIB_FCT from FCT order by LIB_FCT">
		     <td><select name="CD_FCT$<%= i %>">
			%%
		     </select></td>
		  </salon:DBselection>
                  <% 
                  } %>
		  </tr>
	        <% } 
		if (listeParam.contains("CD_TYP_CONTR")) { %>
		  <tr><td><span class="obligatoire">Type&nbsp;de&nbsp;contrat&nbsp;: </span></td>
                  <%
                  for (int i = 0; i < nbItem; i++ ) { %>
		  <salon:DBselection valeur='<%= (String) request.getAttribute("CD_TYP_CONTR$" + i) %>' sql="select CD_TYP_CONTR, LIB_TYP_CONTR from TYP_CONTR order by LIB_TYP_CONTR">
		     <td><select name="CD_TYP_CONTR$<%= i %>">
			%%
		     </select></td>
		  </salon:DBselection>
                  <% 
                  } %>
		  </tr>
	        <% } 
		if (listeParam.contains("CD_TYP_POINTAGE")) { %>
		  <tr><td><span class="obligatoire">Type&nbsp;de&nbsp;pointage&nbsp;: </span></td>
                  <%
                  for (int i = 0; i < nbItem; i++ ) { %>
		  <salon:DBselection valeur='<%= (String) request.getAttribute("CD_TYP_POINTAGE$" + i) %>' sql="select CD_TYP_POINTAGE, LIB_TYP_POINTAGE from TYP_POINTAGE order by LIB_TYP_POINTAGE">
		     <td><select name="CD_TYP_POINTAGE$<%= i %>">
			%%
		     </select></td>
		  </salon:DBselection>
                  <% 
                  } %>
		  </tr>
	        <% } 
		if (listeParam.contains("Genre")) { %>
		  <tr><td><span class="obligatoire">Genre : </span></td>
                  <%
                  for (int i = 0; i < nbItem; i++ ) { %>
                  <salon:selection valeur='<%= (String) request.getAttribute("Genre$" + i) %>' valeurs='<%= "F|M" %>' libelle="Femme|Homme">
                    <td><select name="Genre$<%= i %>">
		     %%
                    </select></td>
		  </salon:selection>
                  <% 
                  } %>
		  </tr>
	        <% } %>
                <tr>
                    <td><span class="obligatoire">Couleur&nbsp;:</span></td>
                  <%
                  for (int i = 0; i < nbItem; i++ ) { %>
                    <salon:selection valeur='<%= (String) request.getAttribute("Couleur$" + i) %>' 
                            valeurs='<%= "0x950793|0xf600a2|0xee6ff9|0xbe8e8e|0xbb2603|0xf97f6f|0xffa200|0xfdd343|0xfaf982|0xbe9e08|0x25656c|0x079095|0x03c3c1|0xacd0c7|0x9db4bf|0x71b1f9|0x0792fd|0x4f70a1|0x796694|0x997cec|0x6e04f2" %>'
                            libelle="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;|&nbsp;|&nbsp;|&nbsp;|&nbsp;|&nbsp;|&nbsp;|&nbsp;|&nbsp;|&nbsp;|&nbsp;|&nbsp;|&nbsp;|&nbsp;|&nbsp;|&nbsp;|&nbsp;|&nbsp;|&nbsp;|&nbsp;">
                    <td><select name="Couleur$<%= i %>" class="important">
                        %%
                    </select></td>
                    </salon:selection>
                  <% 
                  } %>
                </tr>
      </table>

       <p class="tabDonnees">
	        <a href="javascript:Zoom()"><img name="graph" src="images/bspacer.gif" alt="Cliquez pour agrandir" border="0"></a>
       </p>
<salon:madeBy />
</form>

<script language="JavaScript">
// Fonctions d'action
<% 
    for (int i = 0; i < nbItem; i++) { %>
        changeCouleur(<%= i %>);
<% } %>

function changeCouleur(i) {
    for (j=0; j < document.fiche.elements["Couleur$" + i].length; j++) {
        cdCouleur = "#" + document.fiche.elements["Couleur$" + i].options[j].value.substring(2)
        document.fiche.elements["Couleur$" + i].options[j].style.color = cdCouleur;
        document.fiche.elements["Couleur$" + i].options[j].style.background = cdCouleur;
    }
}

function Zoom()
{
   URLImage = "ficStat.srv?Action=Graphe&Width=975&Height=680";
   for (i=0; i < document.fiche.length; i++) {
      if (document.fiche.elements[i].type == "select-one") {
	 for (j=0; j < document.fiche.elements[i].length; j++) {
	    if (document.fiche.elements[i].options[j].selected) {
	       URLImage += "&" + document.fiche.elements[i].name + "=" + doEscape(document.fiche.elements[i].options[j].value);
	    }
	 }
      }
      else if (document.fiche.elements[i].type == "hidden") {
	 URLImage += "&" + document.fiche.elements[i].name + "=" + doEscape(document.fiche.elements[i].value);
      }
      else if (document.fiche.elements[i].type == "text") {
	 URLImage += "&" + document.fiche.elements[i].name + "=" + doEscape(document.fiche.elements[i].value);
      }
      else {
         alert (document.fiche.elements[i].type);
      }
   }
   window.open(URLImage, "Zoom", "toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=no,resize=yes,width=1010,height=710,top=0,left=0");
}

// Suppression du client
function Valider()
{
   URLImage = "ficStat.srv?Action=Graphe&Width=700&Height=500";
   for (i=0; i < document.fiche.length; i++) {
      if (document.fiche.elements[i].type == "select-one") {
	 for (j=0; j < document.fiche.elements[i].length; j++) {
	    if (document.fiche.elements[i].options[j].selected) {
	       URLImage += "&" + document.fiche.elements[i].name + "=" + doEscape(document.fiche.elements[i].options[j].value);
	    }
	 }
      }
      else if (document.fiche.elements[i].type == "hidden") {
	 URLImage += "&" + document.fiche.elements[i].name + "=" + doEscape(document.fiche.elements[i].value);
      }
      else if (document.fiche.elements[i].type == "text") {
	 URLImage += "&" + document.fiche.elements[i].name + "=" + doEscape(document.fiche.elements[i].value);
      }
      else {
         alert (document.fiche.elements[i].type);
      }
   }
   document.graph.src = URLImage;
}


// Enregistrement des données du client
function Enregistrer()
{
   // Verification des données obligatoires
   if ((document.fiche.LIB_STAT.value == "") || (document.fiche.REQ_SQL.value == "")) {
      alert ("Le libellé de la statistique et la requête doivent être saisis. L'enregistrement n'a pas pu avoir lieu.");
      return;
   }
   if ((document.fiche.Action.value != "Creation") && (document.fiche.Action.value != "Modification")) {
      document.fiche.Action.value = "Modification";
   }
   document.fiche.submit();
}

// Suppression du client
function Supprimer()
{
   if (confirm ("Cette suppression est définitive. Confirmez-vous cette action ?")) {
      document.fiche.Action.value = "Suppression";
      document.fiche.submit();
   }
}

function RetourListe()
{
   parent.location.href = "ListeStat.jsp";
}

// Affichage de l'aide
function Aide()
{
    window.open("aideFicheStat.html");
}

</script>
</body>
</html>
