<%@ page import="java.util.TreeSet, java.util.Iterator" %>
<%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %>
<html>
<head>
<title>Liste des sauvegardes</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees">
<%@ include file="include/commun.js" %>
<script language="JavaScript">
<!--
<%
   // Récupération des paramètres
   String Action = request.getParameter("Action");
   String Type = request.getParameter("Type");
   TreeSet listeFichier = (TreeSet) request.getAttribute("listeFichier");
%>
   var Action="<%=Action%>";

function Init() {
    // Juste le lien supprimer
    MM_showHideLayers('SUPPRIMER?bottomFrame','','show');
    MM_showHideLayers('ENREGISTRER?bottomFrame','','hide');
}
//-->
</script>
<h1><img src="images/titres/ficGestSauv.gif"></h1>
<form name="fiche" action="restauration.srv" method="post">
<p>
    <input type="hidden" name="Action" value="Gestion">
    Type de sauvegarde :
	<salon:selection valeur="<%= Type %>" valeurs='<%= "D|M|I" %>' libelle="Disque dur (tous les jours)|Média amovible (une fois par semaine)|Internet (une fois par semaine)">
        <select name="Type" onChange="rechargeListe()">
            %%
        </select>
    </salon:selection>
<hr>
<table width="100%" border="1" >
	<tr>
		<th>Sélection<br/>pour suppression<br/><a href="javascript:selection()">Tout sélectionner</a></th>
		<th>Nom de la sauvegarde</th>
	</tr>
        <%
        for (Iterator i=listeFichier.iterator(); i.hasNext(); ) {
                String nom = (String) i.next();
        %>
            <tr>
            <td align="right"><salon:valeur valeur="<%= nom %>" valeurNulle="null"><input type="checkbox" name="%%"></salon:valeur></td>
            <td>
	    <salon:valeur valeur="<%= nom %>" valeurNulle="null"> %% </salon:valeur>
            </td>
            </tr>
        <% } %>
</table>
</form>
<script language="JavaScript">

function rechargeListe()
{
   document.fiche.Action.value = "Gestion";
   document.fiche.submit();
}

// Sélectionne toutes les sauvegardes
function selection()
{
    for (var i = 0; i < document.fiche.elements.length; i++) {
        var aCheckBox = document.fiche.elements[i];

        if (aCheckBox.type == "checkbox") {
            aCheckBox.checked = true;
        }
    }
}

// Suppression de la prestation
function Supprimer()
{
    var ok = false;
    for (var i = 0; !ok && (i < document.fiche.elements.length); i++) {
        var aCheckBox = document.fiche.elements[i];

        if (aCheckBox.checked == true) {
            ok = true;
        }
    }
    if (ok) {
        if (confirm ("Cette suppression est définitive. Confirmez-vous la suppression des sauvegardes sélectionnées ?")) {
            document.fiche.Action.value = "Suppression";
            document.fiche.submit();
        }
    }
}

// Affichage de l'aide
function Aide()
{
    window.open("aideFiche.html");
}



</script>
</body>
</html>
