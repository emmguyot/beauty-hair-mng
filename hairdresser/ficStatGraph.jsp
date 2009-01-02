<%
/*
 * This program is part of InCrEG LibertyLook software http://beauty-hair-mng.sourceforge.net
 * Copyright (C) 2001-2008 Emmanuel Guyot <See emmguyot on SourceForge> 
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
<%@ taglib uri="WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<i18n:bundle baseName="messages" locale="<%= mySalon.getLangue() %>"/>
<html>
<head>
<title><i18n:message key="title.ficStatGraph" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees">
<%@ include file="include/commun.jsp" %>
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
    <h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/ficStat.gif" alt=<salon:TimeStamp bean="<%= aStat %>" />><br><span class="ssTitre">%%</span></h1>
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
		  <tr><td><span class="obligatoire"><i18n:message key="label.periode" /> : </span></td>
                  <i18n:message key="valeur.periode" id="valeurPeriode" />
                  <salon:selection valeur='<%= (String) request.getAttribute("PeriodeTemps$0") %>' valeurs='<%= "month|day" %>' libelle="<%= valeurPeriode %>">
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
                    <i18n:message key="label.grapheNumero" /><%= i+1 %></span></td>
                <%
                } %>
                </tr>
		<%
                if (listeParam.contains("DateDebut")) { %>
		  <tr><td><span class="obligatoire"><i18n:message key="label.dtDebut" />: </span></td>
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
		  <tr><td><span class="obligatoire"><i18n:message key="label.dtFin" />: </span></td>
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
		  <tr><td><span class="obligatoire"><i18n:message key="label.collaborateur" />&nbsp;: </span></td>
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
		  <tr><td><span class="obligatoire"><i18n:message key="label.typeCheveux" />: </span></td>
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
		if (listeParam.contains("CD_TYP_PEAU")) { %>
		  <tr><td><span class="obligatoire"><i18n:message key="label.typePeau" />: </span></td>
                  <%
                  for (int i = 0; i < nbItem; i++ ) { %>
		  <salon:DBselection valeur='<%= (String) request.getAttribute("CD_TYP_PEAU$" + i) %>' sql="select CD_TYP_PEAU, LIB_TYP_PEAU from TYP_PEAU order by LIB_TYP_PEAU">
		     <td><select name="CD_TYP_PEAU$<%= i %>">
			%%
		     </select></td>
		  </salon:DBselection>
                  <% 
                  } %>
		  </tr>
	        <% } 
		if (listeParam.contains("CD_CATEG_CLI")) { %>
		  <tr><td><span class="obligatoire"><i18n:message key="label.categorieClient" />: </span></td>
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
		  <tr><td><span class="obligatoire"><i18n:message key="label.trancheAge" />: </span></td>
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
		  <tr><td><span class="obligatoire"><i18n:message key="label.origine" />: </span></td>
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
		  <tr><td><span class="obligatoire"><i18n:message key="label.modePaiement" />: </span></td>
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
		  <tr><td><span class="obligatoire"><i18n:message key="label.typePrest" />: </span></td>
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
		  <tr><td><span class="obligatoire"><i18n:message key="label.categoriePrest" />: </span></td>
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
		  <tr><td><span class="obligatoire"><i18n:message key="label.typeArticle" />: </span></td>
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
		  <tr><td><span class="obligatoire"><i18n:message key="label.categorieArticle" />: </span></td>
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
		  <tr><td><span class="obligatoire"><i18n:message key="label.marque" />: </span></td>
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
		  <tr><td><span class="obligatoire"><i18n:message key="label.typeMouvementStock" />: </span></td>
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
		  <tr><td><span class="obligatoire"><i18n:message key="label.typeMouvementCaisse" />: </span></td>
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
		  <tr><td><span class="obligatoire"><i18n:message key="label.fonction" />: </span></td>
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
		  <tr><td><span class="obligatoire"><i18n:message key="label.typeContrat" />: </span></td>
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
		  <tr><td><span class="obligatoire"><i18n:message key="label.typePointage" />: </span></td>
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
		  <tr><td><span class="obligatoire"><i18n:message key="label.genre" /> : </span></td>
                  <i18n:message key="valeur.femmeHomme" id="paramFemmeHomme" />
                  <%
                  for (int i = 0; i < nbItem; i++ ) { %>
                  <salon:selection valeur='<%= (String) request.getAttribute("Genre$" + i) %>' valeurs='<%= "F|M" %>' libelle="<%= paramFemmeHomme %>">
                    <td><select name="Genre$<%= i %>">
		     %%
                    </select></td>
		  </salon:selection>
                  <% 
                  } %>
		  </tr>
	        <% } %>
                <tr>
                    <td><span class="obligatoire"><i18n:message key="label.couleur" />:</span></td>
                  <%
                  for (int i = 0; i < nbItem; i++ ) { %>
                    <salon:selection valeur='<%= (String) request.getAttribute("Couleur$" + i) %>' 
                            valeurs='<%= "0x950793|0xf600a2|0xee6ff9|0xbe8e8e|0xbb2603|0xf97f6f|0xffa200|0xfdd343|0xfaf982|0xbe9e08|0x25656c|0x079095|0x03c3c1|0xacd0c7|0x9db4bf|0x71b1f9|0x0792fd|0x4f70a1|0x796694|0x997cec|0x6e04f2" %>'
                            libelle="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;|&nbsp;|&nbsp;|&nbsp;|&nbsp;|&nbsp;|&nbsp;|&nbsp;|&nbsp;|&nbsp;|&nbsp;|&nbsp;|&nbsp;|&nbsp;|&nbsp;|&nbsp;|&nbsp;|&nbsp;|&nbsp;|&nbsp;">
                    <td><select name="Couleur$<%= i %>" class="important" onchange="changeCouleurUnitaire(this)">
                        %%
                    </select></td>
                    </salon:selection>
                  <% 
                  } %>
                </tr>
      </table>

       <p class="tabDonnees">
	        <a href="javascript:Zoom()"><img name="graph" src="images/bspacer.gif" alt="<i18n:message key="message.zoom" />" border="0"></a>
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
    changeCouleurUnitaire(document.fiche.elements["Couleur$" + i]);
}

function changeCouleurUnitaire(combo) {
    for (j=0; j < combo.length; j++) {
        if (combo.options[j].selected) {
            combo.style.color = "#" + combo.options[j].value.substring(2);
            combo.style.background = "#" + combo.options[j].value.substring(2);
        }
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


function RetourListe()
{
   parent.location.href = "ListeStat.jsp";
}

// Affichage de l'aide
function Aide()
{
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideFicheStat.html");
}

</script>
</body>
</html>
