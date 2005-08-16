<%
    // Recupère la liste
    boolean peutCreerFacture = mySalon.peutCreerFacture();
    Vector lstLignes = (Vector) request.getAttribute("Liste");
%>
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

            String nextKey = aCli.getNOM().substring(0,Math.min(aCli.getNOM().length(), longueurCle)); 

            if ((lstLignes.size() > 20) 
                    && (i > 0) 
                    && (!nextKey.equals(lastKey)))  {
            %>
                <tr><td>&nbsp;</td><td colspan="4" class="ligneTab3"><a name="<%= nextKey %>"></a><b><%= nextKey %></b>&nbsp;&nbsp;&nbsp;<a href="#"><img src="images/haut.gif"></a></td></tr>
            <%
            }
            lastKey = nextKey;
	%>
	<tr><td width="75">
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
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideListeCli.html");
}

</script>
