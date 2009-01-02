<%
/*
 * This program is part of InCrEG LibertyLook software http://beauty-hair-mng.sourceforge.net
 * Copyright (C) 2001-2009 Emmanuel Guyot <See emmguyot on SourceForge> 
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
        <i18n:message key="label.dupliquerFacture" id="dupliquerFacture" />
        <i18n:message key="label.accueilClient" id="accueilClient" />
        <i18n:message key="erreur.pasCollaborateurPresent" id="pasCollab" />
        <i18n:message key="label.imprimerFicheTech" id="impFichTech" />
        <i18n:message key="label.ajouterRDV" id="ajoutRDV" />
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
	<tr><td width="80">
    <%
        if (peutCreerFacture) { %>
	      <a href="addCli.srv?CD_CLI=<%= aCli.getCD_CLI() %>" title="<%= dupliquerFacture %>" target=MenuFrame><img src=images/plus.gif border="0" width="15" height="15"></a>
	      <a href="addCli.srv?Vide=1&CD_CLI=<%= aCli.getCD_CLI() %>" title="<%= accueilClient %>" target=MenuFrame><img src=images/plus2.gif border="0" width="15" height="15"></a>
    <%
        } 
        else { %>
	      <img src=images/plusNon.gif border="0" width="15" height="15" alt="<%= pasCollab %>"/>
	      <img src=images/plus2Non.gif border="0" width="15" height="15" alt="<%= pasCollab %>"/>
    <%
        } %>
	    <a href="ficTech.srv?Action=Impression&CD_CLI=<%= aCli.getCD_CLI() %>" title="<%= impFichTech %>" target="_blank"><img src=images/Tech.gif border="0" width="15" height="15"></a>
	    <a href="_FicheRDV.jsp?Action=Creation&CD_CLI=<%= aCli.getCD_CLI() %>" title="<%= ajoutRDV %>" target="ClientFrame"><img src=images/boutonRDV.gif border="0" width="15" height="15"></a></td>

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
function Init() {
   <%
   // Positionne les liens d actions
   %>
}

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
