<%@ page import="com.increg.salon.bean.SalonSession,java.util.Vector,com.increg.salon.bean.ClientBean" %>
<%
    SalonSession mySalon = (SalonSession) session.getAttribute("SalonSession");
    if (mySalon == null) {
        getServletConfig().getServletContext().getRequestDispatcher("/reconnect.html").forward(request, response);
    }
%>
<%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %>
<html>
<head>
<title>Liste des clients</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees">
<%
    // Récupération des paramètres
    String premLettre = (String) request.getAttribute("premLettre");
    String INDIC_VALID = (String) request.getAttribute("INDIC_VALID");
    boolean peutCreerFacture = mySalon.peutCreerFacture();
    // Recupère la liste
    Vector lstLignes = (Vector) request.getAttribute("Liste");
    %>
<h1><img src="images/titres/lstCli.gif"></h1>
<form name="fiche" action="rechCli.srv" method="post">
<p>Premi&egrave;re lettre du nom : 
<input type="hidden" name="premLettre" value="<%= premLettre %>">
<%
    String lien = "";
    if ((INDIC_VALID != null) && (INDIC_VALID.length() > 0)) {
        lien = lien + "&INDIC_VALID=" + INDIC_VALID;
    }
    // Affiche toutes les lettres avec un lien permettant de filtrer par cette lettre
    for (char c='A'; Character.isUpperCase(c); c++) { 
        if ((premLettre != null) && (premLettre.charAt(0) == c)) { %>
            <%=c %>
        <% } else { %>
            <a href="rechCli.srv?premLettre=<%= c + lien %>"><%=c %></a>     
    <% }
    }
%>
&nbsp;&nbsp; Affiche anciens clients : 
    <input type="checkbox" name="INDIC_VALID"
    <% if ((INDIC_VALID != null) && (INDIC_VALID.equals("on"))) { %>
    checked 
    <% } %>
    onClick="document.fiche.submit()" >
    <%
    if (lstLignes.size() > 20) { 
    %>
        <br/>

        Seconde lettre :
        <%
        String lastKey = "";
	for (int i = 0; i < lstLignes.size(); i++) {
	    ClientBean aCli = (ClientBean) lstLignes.get(i);

            String nextKey = aCli.getNOM().substring(0,Math.min(aCli.getNOM().length(),2)); 
            if (!nextKey.equals(lastKey))  {
        %>
                <a href="#<%= nextKey %>"><%= nextKey %></a>&nbsp;&nbsp;&nbsp;
        <%
            }
            lastKey = nextKey;
        }
    }
    %>
</p>
</form>
<hr>
<table width="100%" border="1" >
	<tr>
		<th></th>
		<th>Nom client</th>
		<th>Adresse</th>
		<th>Ville</th>
	</tr>
	<%
        String lastKey = "";
	    
	for (int i = 0; i < lstLignes.size(); i++) {
	    ClientBean aCli = (ClientBean) lstLignes.get(i);

            String nextKey = aCli.getNOM().substring(0,Math.min(aCli.getNOM().length(),2)); 

            if ((lstLignes.size() > 20) 
                    && (i > 0) 
                    && (!nextKey.equals(lastKey)))  {
            %>
                <tr><td>&nbsp;</td><td colspan="4" class="ligneTab3"><a name="<%= nextKey %>"></a><b><%= nextKey %></b>&nbsp;&nbsp;&nbsp;<a href="#"><img src="images/haut.gif"></a></td></tr>
            <%
            }
            lastKey = nextKey;
	%>
	<tr>
	    <td width=75>
	    <%
            if (peutCreerFacture) { %>
	      <a href="addCli.srv?CD_CLI=<%= aCli.getCD_CLI() %>" title="Dupliquer sa facture" target=MenuFrame><img src=images/plus.gif border="0" width="15" height="15"></a>
	      <a href="addCli.srv?Vide=1&CD_CLI=<%= aCli.getCD_CLI() %>" title="Accueillir ce client" target=MenuFrame><img src=images/plus2.gif border="0" width="15" height="15"></a>
	    <%
            } 
            else { %>
	      <img src=images/plusNon.gif border="0" width="15" height="15" alt="Action impossible : Pas de collaborateur présent"/>
	      <img src=images/plus2Non.gif border="0" width="15" height="15" alt="Action impossible : Pas de collaborateur présent"/>
	    <%
            } %>
	    <a href="ficTech.srv?Action=Impression&CD_CLI=<%= aCli.getCD_CLI() %>" title="Imprimer la fiche technique" target="_blank"><img src=images/Tech.gif border="0" width="15" height="15"></a>
	    <a href="_FicheRDV.jsp?Action=Creation&CD_CLI=<%= aCli.getCD_CLI() %>" title="Ajouter un rendez-vous" target="ClientFrame"><img src=images/boutonRDV.gif border="0" width="15" height="15"></a></td>

	    <td><a href="_FicheCli.jsp?Action=Modification&CD_CLI=<%= aCli.getCD_CLI() %>" target="ClientFrame"><%= aCli.toStringListe() %></a></td>
	    <td>
		<salon:valeur valeurNulle="0" valeur="<%= aCli.getRUE() %>" expand="true">
		  %%
	        </salon:valeur>
	    &nbsp;</td>
	    <td><%= aCli.getVILLE() %>&nbsp;</td>
	    
	</tr>
	<%
	}
	%>
</table>
<script language="JavaScript">
function Nouveau()
{
   parent.location.href = "_FicheCli.jsp";
}

// Affichage de l'aide
function Aide()
{
    window.open("aideListeCli.html");
}

</script>
</body>
</html>
