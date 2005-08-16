<%@ page import="java.util.Vector" %>
<%@ page import="com.increg.salon.bean.SalonSession,
	       com.increg.salon.bean.CriterePubBean" %>
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
<title>Publipostage</title>
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
   CriterePubBean aCriterePub = (CriterePubBean) request.getAttribute("CriterePubBean");
   Vector listeParam = (Vector) request.getAttribute("listeParam");
%>
   var Action="<%=Action%>";

function Init() {
   MM_showHideLayers('VALIDER?bottomFrame','','show');
   MM_showHideLayers('SUPPRIMER?bottomFrame','','hide');
   MM_showHideLayers('ENREGISTRER?bottomFrame','','hide');
   MM_showHideLayers('RETOUR_LISTE?bottomFrame','','show');
}
//-->
</script>
<salon:valeur valeurNulle="0" valeur="<%= aCriterePub.getLIB_CRITERE_PUB() %>" >
    <h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/ficPub.gif" alt=<salon:TimeStamp bean="<%= aCriterePub %>" />><br><span class="ssTitre">%%</span></h1>
</salon:valeur>
<salon:message salonSession="<%= mySalon %>" />
<form method="post" action="ficCriterePub.srv" name="fiche">
		<salon:valeur valeurNulle="0" valeur="<%= aCriterePub.getCD_CRITERE_PUB() %>" >
		  <input type="hidden" name="CD_CRITERE_PUB" value="%%" >
	        </salon:valeur>
		<salon:valeur valeurNulle="0" valeur="<%= aCriterePub.getLIB_CRITERE_PUB() %>" >
		  <input type="hidden" name="LIB_CRITERE_PUB" value="%%" >
	        </salon:valeur>

	 <table>
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
                </tr>
		<%
                if (listeParam.contains("DateDebut")) { %>
		  <tr><td><span class="obligatoire">Date&nbsp;de&nbsp;début&nbsp;: </span></td>
		  <salon:valeur valeurNulle="null" valeur='<%= (String) request.getAttribute("DateDebut") %>' >
		     <td><input type="text" name="DateDebut" value="%%" size=10 onChange="FormateDate(this)"></td>
		  </salon:valeur>
		  </tr>
	        <% } 
		if (listeParam.contains("DateFin")) { %>
		  <tr><td><span class="obligatoire">Date&nbsp;de&nbsp;fin&nbsp;: </span></td>
		  <salon:valeur valeurNulle="null" valeur='<%= (String) request.getAttribute("DateFin") %>' >
		     <td><input type="text" name="DateFin" value="%%" size=10 onChange="FormateDate(this)"></td>
		  </salon:valeur>
		  </tr>
	        <% } 
		if (listeParam.contains("CD_COLLAB")) { %>
		  <tr><td><span class="obligatoire">Collaborateur&nbsp;: </span></td>
		  <salon:DBselection valeur='<%= (String) request.getAttribute("CD_COLLAB") %>' sql="select CD_COLLAB, PRENOM from COLLAB order by PRENOM, NOM">
		     <td><select name="CD_COLLAB">
			%%
		     </select></td>
		  </salon:DBselection>
		  </tr>
	        <% } 
		if (listeParam.contains("CD_TYP_CHEV")) { %>
		  <tr><td><span class="obligatoire">Type&nbsp;de&nbsp;cheveux&nbsp;: </span></td>
		  <salon:DBselection valeur='<%= (String) request.getAttribute("CD_TYP_CHEV") %>' sql="select CD_TYP_CHEV, LIB_TYP_CHEV from TYP_CHEV order by LIB_TYP_CHEV">
		     <td><select name="CD_TYP_CHEV">
			%%
		     </select></td>
		  </salon:DBselection>
		  </tr>
	        <% } 
		if (listeParam.contains("CD_TYP_PEAU")) { %>
		  <tr><td><span class="obligatoire">Type&nbsp;de&nbsp;peau&nbsp;: </span></td>
		  <salon:DBselection valeur='<%= (String) request.getAttribute("CD_TYP_PEAU") %>' sql="select CD_TYP_PEAU, LIB_TYP_PEAU from TYP_PEAU order by LIB_TYP_PEAU">
		     <td><select name="CD_TYP_PEAU">
			%%
		     </select></td>
		  </salon:DBselection>
		  </tr>
	        <% } 
		if (listeParam.contains("CD_CATEG_CLI")) { %>
		  <tr><td><span class="obligatoire">Catégorie&nbsp;de&nbsp;clients&nbsp;: </span></td>
		  <salon:DBselection valeur='<%= (String) request.getAttribute("CD_CATEG_CLI") %>' sql="select CD_CATEG_CLI, LIB_CATEG_CLI from CATEG_CLI order by LIB_CATEG_CLI">
		     <td><select name="CD_CATEG_CLI">
			%%
		     </select></td>
		  </salon:DBselection>
		  </tr>
	        <% } 
		if (listeParam.contains("CD_TR_AGE")) { %>
		  <tr><td><span class="obligatoire">Tranche&nbsp;d'âge&nbsp;: </span></td>
		  <salon:DBselection valeur='<%= (String) request.getAttribute("CD_TR_AGE") %>' sql="select CD_TR_AGE, LIB_TR_AGE from TR_AGE order by AGE_MIN">
		     <td><select name="CD_TR_AGE">
			%%
		     </select></td>
		  </salon:DBselection>
		  </tr>
	        <% } 
		if (listeParam.contains("CD_ORIG")) { %>
		  <tr><td><span class="obligatoire">Origine&nbsp;du&nbsp;client&nbsp;: </span></td>
		  <salon:DBselection valeur='<%= (String) request.getAttribute("CD_ORIG") %>' sql="select CD_ORIG, LIB_ORIG from ORIG order by LIB_ORIG">
		     <td><select name="CD_ORIG">
			%%
		     </select></td>
		  </salon:DBselection>
		  </tr>
	        <% } 
		if (listeParam.contains("CD_MOD_REGL")) { %>
		  <tr><td><span class="obligatoire">Mode&nbsp;de&nbsp;réglement&nbsp;: </span></td>
		  <salon:DBselection valeur='<%= (String) request.getAttribute("CD_MOD_REGL") %>' sql="select CD_MOD_REGL, LIB_MOD_REGL from MOD_REGL order by LIB_MOD_REGL">
		     <td><select name="CD_MOD_REGL">
			%%
		     </select></td>
		  </salon:DBselection>
		  </tr>
	        <% } 
		if (listeParam.contains("CD_TYP_VENT")) { %>
		  <tr><td><span class="obligatoire">Type&nbsp;de&nbsp;prestation&nbsp;: </span></td>
		  <salon:DBselection valeur='<%= (String) request.getAttribute("CD_TYP_VENT") %>' sql="select CD_TYP_VENT, LIB_TYP_VENT from TYP_VENT order by LIB_TYP_VENT">
		     <td><select name="CD_TYP_VENT">
			%%
		     </select></td>
		  </salon:DBselection>
		  </tr>
	        <% } 
		if (listeParam.contains("CD_CATEG_PREST")) { %>
		  <tr><td><span class="obligatoire">Catégorie&nbsp;de&nbsp;prestation&nbsp;: </span></td>
		  <salon:DBselection valeur='<%= (String) request.getAttribute("CD_CATEG_PREST") %>' sql="select CD_CATEG_PREST, LIB_CATEG_PREST from CATEG_PREST order by LIB_CATEG_PREST">
		     <td><select name="CD_CATEG_PREST">
			%%
		     </select></td>
		  </salon:DBselection>
		  </tr>
	        <% } 
		if (listeParam.contains("CD_TYP_ART")) { %>
		  <tr><td><span class="obligatoire">Type&nbsp;d'article&nbsp;: </span></td>
		  <salon:DBselection valeur='<%= (String) request.getAttribute("CD_TYP_ART") %>' sql="select CD_TYP_ART, LIB_TYP_ART from TYP_ART order by LIB_TYP_ART">
		     <td><select name="CD_TYP_ART">
			%%
		     </select></td>
		  </salon:DBselection>
		  </tr>
	        <% } 
		if (listeParam.contains("CD_CATEG_ART")) { %>
		  <tr><td><span class="obligatoire">Catégorie&nbsp;d'article&nbsp;: </span></td>
		  <salon:DBselection valeur='<%= (String) request.getAttribute("CD_CATEG_ART") %>' sql="select CD_CATEG_ART, LIB_CATEG_ART from CATEG_ART order by LIB_CATEG_ART">
		     <td><select name="CD_CATEG_ART">
			%%
		     </select></td>
		  </salon:DBselection>
		  </tr>
	        <% } 
		if (listeParam.contains("CD_MARQUE")) { %>
		  <tr><td><span class="obligatoire">Marque&nbsp;: </span></td>
		  <salon:DBselection valeur='<%= (String) request.getAttribute("CD_MARQUE") %>' sql="select CD_MARQUE, LIB_MARQUE from MARQUE order by LIB_MARQUE">
		     <td><select name="CD_MARQUE">
			%%
		     </select></td>
		  </salon:DBselection>
		  </tr>
	        <% } 
		if (listeParam.contains("CD_TYP_MVT")) { %>
		  <tr><td><span class="obligatoire">Type&nbsp;de&nbsp;mouvement&nbsp;de&nbsp;stock&nbsp;: </span></td>
		  <salon:DBselection valeur='<%= (String) request.getAttribute("CD_TYP_MVT") %>' sql="select CD_TYP_MVT, LIB_TYP_MVT from TYP_MVT order by LIB_TYP_MVT">
		     <td><select name="CD_TYP_MVT">
			%%
		     </select></td>
		  </salon:DBselection>
		  </tr>
	        <% } 
		if (listeParam.contains("CD_TYP_MCA")) { %>
		  <tr><td><span class="obligatoire">Type&nbsp;de&nbsp;mouvement&nbsp;de&nbsp;caisse&nbsp;: </span></td>
		  <salon:DBselection valeur='<%= (String) request.getAttribute("CD_TYP_MCA") %>' sql="select CD_TYP_MCA, LIB_TYP_MCA from TYP_MCA order by LIB_TYP_MCA">
		     <td><select name="CD_TYP_MCA">
			%%
		     </select></td>
		  </salon:DBselection>
		  </tr>
	        <% } 
		if (listeParam.contains("CD_FCT")) { %>
		  <tr><td><span class="obligatoire">Type&nbsp;de&nbsp;mouvement&nbsp;de&nbsp;caisse&nbsp;: </span></td>
		  <salon:DBselection valeur='<%= (String) request.getAttribute("CD_FCT") %>' sql="select CD_FCT, LIB_FCT from FCT order by LIB_FCT">
		     <td><select name="CD_FCT">
			%%
		     </select></td>
		  </salon:DBselection>
		  </tr>
	        <% } 
		if (listeParam.contains("CD_TYP_CONTR")) { %>
		  <tr><td><span class="obligatoire">Type&nbsp;de&nbsp;contrat&nbsp;: </span></td>
		  <salon:DBselection valeur='<%= (String) request.getAttribute("CD_TYP_CONTR") %>' sql="select CD_TYP_CONTR, LIB_TYP_CONTR from TYP_CONTR order by LIB_TYP_CONTR">
		     <td><select name="CD_TYP_CONTR">
			%%
		     </select></td>
		  </salon:DBselection>
		  </tr>
	        <% } 
		if (listeParam.contains("CD_TYP_POINTAGE")) { %>
		  <tr><td><span class="obligatoire">Type&nbsp;de&nbsp;pointage&nbsp;: </span></td>
		  <salon:DBselection valeur='<%= (String) request.getAttribute("CD_TYP_POINTAGE") %>' sql="select CD_TYP_POINTAGE, LIB_TYP_POINTAGE from TYP_POINTAGE order by LIB_TYP_POINTAGE">
		     <td><select name="CD_TYP_POINTAGE">
			%%
		     </select></td>
		  </salon:DBselection>
		  </tr>
	        <% } 
		if (listeParam.contains("Genre")) { %>
		  <tr><td><span class="obligatoire">Genre : </span></td>
                  <salon:selection valeur='<%= (String) request.getAttribute("Genre") %>' valeurs='<%= "F|M" %>' libelle="Femme|Homme">
                    <td><select name="Genre">
		     %%
                    </select></td>
		  </salon:selection>
		  </tr>
	        <% } 
		if (listeParam.contains("VILLE")) { %>
		  <tr><td><span class="obligatoire">Ville : </span></td>
		  <salon:DBselection valeur='<%= (String) request.getAttribute("VILLE") %>' sql="select distinct VILLE, VILLE from CLI order by VILLE">
		     <td><select name="VILLE">
			%%
		     </select></td>
		  </salon:DBselection>
		  </tr>
	        <% } 
		if (listeParam.contains("CD_ART")) { %>
		  <tr><td><span class="obligatoire">Article&nbsp;: </span></td>
		  <salon:DBselection valeur='<%= (String) request.getAttribute("CD_ART") %>' sql="select CD_ART, LIB_ART from ART order by LIB_ART">
		     <td><select name="CD_ART">
			%%
		     </select></td>
		  </salon:DBselection>
		  </tr>
	        <% } 
		if (listeParam.contains("Nombre")) { %>
		  <tr><td><span class="obligatoire">Nombre : </span></td>
		  <salon:valeur valeurNulle="null" valeur='<%= (String) request.getAttribute("Nombre") %>' >
		     <td><input type="text" name="Nombre" value="%%" size=5></td>
		  </salon:valeur>
		  </tr>
	        <% } %>
      </table>
</form>

<script language="JavaScript">

function Valider()
{
   URL = "ficCriterePub.srv?Action=Extraction";
   for (i=0; i < document.fiche.length; i++) {
      if (document.fiche.elements[i].type == "select-one") {
	 for (j=0; j < document.fiche.elements[i].length; j++) {
	    if (document.fiche.elements[i].options[j].selected) {
	       URL += "&" + document.fiche.elements[i].name + "=" + doEscape(document.fiche.elements[i].options[j].value);
	    }
	 }
      }
      else if (document.fiche.elements[i].type == "hidden") {
	 URL += "&" + document.fiche.elements[i].name + "=" + doEscape(document.fiche.elements[i].value);
      }
      else if (document.fiche.elements[i].type == "text") {
	 URL += "&" + document.fiche.elements[i].name + "=" + doEscape(document.fiche.elements[i].value);
      }
      else {
         alert (document.fiche.elements[i].type);
      }
   }
   parent.resultFrame.location.href = URL;
}


function RetourListe()
{
   parent.location.href = "ListeCriterePub.jsp";
}

// Affichage de l'aide
function Aide()
{
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideFicheCriterePub.html");
}

</script>
</body>
</html>
