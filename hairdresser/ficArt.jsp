<%@ page import="java.util.Vector" %>
<%@ page import="com.increg.salon.bean.SalonSession,
	       com.increg.salon.bean.ArtBean,
	       com.increg.salon.bean.CatFournBean,
	       com.increg.salon.bean.FournBean
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
<title>Fiche article</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees" onLoad="Init();document.fiche.LIB_ART.focus()">
<%@ include file="include/commun.js" %>
<script language="JavaScript">
<!--
<%
   // R�cup�ration des param�tres
   String Action = (String) request.getAttribute("Action");
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
<h1><img src="images/titres/ficArt.gif" alt=<salon:TimeStamp bean="<%= aArt %>" />></h1>
<salon:message salonSession="<%= mySalon %>" />
<form method="post" action="ficArt.srv" name="fiche">

<%@ include file="ficArt_Common.jsp" %>

<% if (Action != "Creation") { %>
   <table border="0" cellspacing="0" width="100%">
   <tr>
      <td align="right"> <span class="souslien"> 
		<a href="_FicheArt_Mvt.jsp?Action=Modification&CD_ART=<%= aArt.getCD_ART() %>" target="ClientFrame">Mouvements sur l'article</a> </span> </td>
   </tr>
   </table>
<% } %>
   <table border="0" width="100%">
	 <tr>
		  <th>Fournisseur</th>
		  <th>Libell�</th>
		  <th>R�f�rence</th>
		  <th>Commande<br>mini</th>
		  <th>Prix<br>unitaire HT</th>
		  <th>Principal</th>
		  <th>&nbsp;</th>
	 </tr>
	 <%
	 // Recup�re la liste
	 Vector listeFourn = (Vector) request.getAttribute("listeFourn");
	 String finClass = "1";
	 int i = 1;
	       
	 %>
	 <input type="hidden" name="NbFourn" value="<%= listeFourn.size() %>">
	 <%
	 for (i=0; i< listeFourn.size(); i++) {
	       CatFournBean aFourn = (CatFournBean) listeFourn.get(i);
	       finClass = (finClass.equals("1")) ? "2" : "1";
	 %>
	 <tr class="ligneTab2">
	    <td class="tabDonnees">
	        <salon:valeur valeurNulle="null" valeur="<%= aFourn.getCD_FOURN() %>" >
		  <input type="hidden" name="CD_FOURN<%= i %>" value="%%">
                  <a href="_FicheFourn.jsp?Action=Modification&CD_FOURN=%%" target="_parent">
	        </salon:valeur>
	        <salon:valeur valeurNulle="null" valeur="<%= FournBean.getFournBean(mySalon.getMyDBSession(), Integer.toString(aFourn.getCD_FOURN())).toString() %>" >
		  %%
	        </salon:valeur>
                </a>
	    </td>
	    <td class="tabDonnees"><salon:valeur valeurNulle="null" valeur="<%= aFourn.getLIB_ART() %>" >
		  <input type="text" name="LIB_ART<%= i %>" value="%%">
	        </salon:valeur>
	    </td>
	    <td class="tabDonnees"><salon:valeur valeur="<%= aFourn.getREF_ART() %>" valeurNulle="null">
		  <input type="text" name="REF_ART<%= i %>" value="%%" size=10>
	       </salon:valeur>
	    </td>
	    <td class="tabDonnees"><salon:valeur valeur="<%= aFourn.getQTE_CMD_MIN() %>" valeurNulle="null">
			%%
		  <input type="hidden" name="QTE_CMD_MIN<%= i %>" value="%%">
	       </salon:valeur>
	    </td>
	    <td class="tabDonnees"><salon:valeur valeur="<%= aFourn.getPRX_UNIT_HT() %>" valeurNulle="null">
		  <input class="Nombre" type="text" name="PRX_UNIT_HT<%= i %>" value="%%" size="5">
	       </salon:valeur>
	    </td>
	    <td class="tabDonnees">
		<salon:selection valeur="<%= aFourn.getFOURN_PRINC() %>" valeurs='<%= "N|O" %>' libelle="Non|Oui">
		  <select name="FOURN_PRINC<%= i %>" onChange="exclusionPrinc(<%= i %>)">
		     %%
		  </select>
		</salon:selection>
	    </td>
	    <td class="tabDonnees">
		  <a href="javascript:SupprimerLigne(<%= i %>)"><img src=images/moins.gif width="15" height="15" border="0" alt="Supprimer la ligne"></a>
	    </td>
	 </tr>
	 <%
	 }
	 // ****************** Ligne vide *************************************
	 %>
	 <tr class="ligneTab1">
	    <td class="tabDonnees">
	       <salon:DBselection valeur="<%= (String) null %>" sql="select CD_FOURN, RAIS_SOC from FOURN order by RAIS_SOC">
		     <select name="CD_FOURN<%= i %>">
			%%
		     </select>
	       </salon:DBselection>
	    </td>
	    <td class="tabDonnees">
	       <salon:valeur valeurNulle="null" valeur="<%= aArt.getLIB_ART() %>" >
		  <input type="text" name="LIB_ART<%= i %>" value="%%">
	        </salon:valeur>
	    </td>
	    <td class="tabDonnees"><salon:valeur valeur="<%= aArt.getREF_ART() %>" valeurNulle="null">
		  <input type="text" name="REF_ART<%= i %>" value="%%" size=10>
	       </salon:valeur>
	    </td>
	    <td class="tabDonnees"><salon:valeur valeur="<%= 0 %>" valeurNulle="null">
		  <input class="Nombre" type="text" name="QTE_CMD_MIN<%= i %>" value="%%" size=5>
	       </salon:valeur>
	    </td>
	    <td class="tabDonnees"><salon:valeur valeur="<%= 0 %>" valeurNulle="null">
		  <input class="Nombre" type="text" name="PRX_UNIT_HT<%= i %>" value="%%" size=5>
	       </salon:valeur>
	    </td>
	    <td class="tabDonnees">
		<salon:selection valeur='<%= (listeFourn.size()>0) ? "N" : "O" %>' valeurs='<%= "N|O" %>' libelle="Non|Oui">
		  <select name="FOURN_PRINC<%= i %>" onChange="exclusionPrinc(<%= i %>)">
		     %%
		  </select>
		</salon:selection>
	    </td>
	    <td class="tabDonnees">
		  <a href="javascript:AjouterLigne(<%= i %>)"><img src=images/plus.gif width="15" height="15" border="0" alt="Ajouter la ligne"></a>
	    </td>
	 </tr>
   </table>
</form>

<script language="JavaScript">
// Fonctions d'action
function set(Champs)
{
   document.fiche.elements[Champs + document.fiche.NbFourn.value].value = document.fiche.elements[Champs].value;
}

function exclusionPrinc(numFourn)
{
   if (document.fiche.elements["FOURN_PRINC" + numFourn].options[1].selected) {
      // Passage � Oui : Tous les autres � Non
      for ( i = 0 ; i <= document.fiche.NbFourn.value ; i++ ) {
	 if (i != numFourn) {
	    document.fiche.elements["FOURN_PRINC" + i].options[1].selected = false;
	    document.fiche.elements["FOURN_PRINC" + i].options[0].selected = true;
	 }
      }
   }
}

// Contr�le des donn�es avant enregistrement
function ControleEnreg ()
{
   // Verification des donn�es obligatoires
   if (document.fiche.LIB_ART.value == "") {
      alert ("Le libell� doit �tre saisi. L'enregistrement n'a pas pu avoir lieu.");
      return false;
   }
   return true;
}

// Supprime une ligne de prestation
function SupprimerLigne(NumPrest)
{
   // Verification des donn�es obligatoires
   if (! ControleEnreg()) {
      return;
   }
   document.fiche.Action.value = "SuppressionLigne";
   document.fiche.ParamSup.value = NumPrest;
   document.fiche.submit();
}

// Ajoute une ligne de prestation
function AjouterLigne(NumPrest)
{
   // Verification des donn�es obligatoires
   if (! ControleEnreg()) {
      return;
   }
   document.fiche.Action.value = "AjoutLigne";
   document.fiche.ParamSup.value = NumPrest;
   document.fiche.submit();
}

// Enregistrement des donn�es du client
function Enregistrer()
{
   // Verification des donn�es obligatoires
   if (document.fiche.LIB_ART.value == "") {
      alert ("Le libell� doit �tre saisi. L'enregistrement n'a pas pu avoir lieu.");
      return;
   }
   document.fiche.submit();
}

// Suppression du client
function Supprimer()
{
   if ((document.fiche.CD_ART.value != "0") && (document.fiche.CD_ART.value != "")) {
       if (confirm ("Cette suppression est d�finitive. Confirmez-vous cette action ?")) {
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
    window.open("aideFicheArt.html");
}

</script>
</body>
</html>
