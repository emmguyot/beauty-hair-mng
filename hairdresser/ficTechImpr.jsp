<%@ page import="java.util.Vector" %>
<%@ page import="com.increg.salon.bean.SalonSession,
	       com.increg.salon.bean.ClientBean,
	       com.increg.salon.bean.HistoPrestBean,
	       com.increg.salon.bean.PrestBean,
	       com.increg.salon.bean.DonneeRefBean" %>
<%
    SalonSession mySalon = (SalonSession) session.getAttribute("SalonSession");
    if (mySalon == null) {
        getServletConfig().getServletContext().getRequestDispatcher("/reconnect.html").forward(request, response);
    }
%>
<%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %>
<html>
<head>
<title>Fiche Technique</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="corpsImpr" onLoad="Imprimer()">
<%@ include file="include/commun.js" %>
<%
   // Récupération des paramètres
   ClientBean aCli = (ClientBean) request.getAttribute("ClientBean");
   Vector listeComment = (Vector) request.getAttribute("listeComment");
   String NbComment = (String) request.getAttribute("NbComment");
   String[] CD_CATEG_PREST_SELECT = request.getParameterValues("CD_CATEG_PREST");
%>
<table class="ficheImpr" style="{ width: <%= mySalon.getLargeurFiche() %> }" width="400">
	<tr> 
		<td width="44" valign="top"><span class="obligatoire">Client</span> 
			: </td>
		<td valign="top" colspan="5"> <span class="readonly"><%= aCli.toString() %></span> 
		</td>
	</tr>
	<tr> 
		<td colspan="4" valign="top"><span class="facultatif">Date 
			d'anniversaire</span> : </td>
		<td valign="top" colspan="2"> <salon:valeur valeur="<%= aCli.getDT_ANNIV() %>" valeurNulle="null"> 
			<span class="readonly">%%</span> </salon:valeur> </td>
	</tr>
	<tr> 
		<td colspan="3" valign="top"><span class="facultatif">Type 
			de cheveux</span> : </td>
		<td valign="top" colspan="3"> 
			<%
	       DonneeRefBean typChev = DonneeRefBean.getDonneeRefBean(mySalon.getMyDBSession(), "TYP_CHEV", Integer.toString(aCli.getCD_TYP_CHEV()));
	       if (typChev != null) { %>
			<salon:valeur valeur="<%= typChev.toString() %>" valeurNulle="null"> 
			<span class="readonly">%%</span> </salon:valeur> 
			<% } %>
			&nbsp; </td>
	</tr>
	<tr> 
		<td height="0"></td>
		<td width="22"></td>
		<td width="34"></td>
		<td width="13"></td>
		<td width="164"></td>
		<td width="4"></td>
	</tr>
	<% 
                int i=1;
                long CD_CATEG_PREST = 0;
                String lastComm = "";
                long lastCD_PREST = 0;
                boolean firstComm = false;
                HistoPrestBean lastPrest = null;
                for (i=0; i< listeComment.size(); i++) { 
		     HistoPrestBean aPrest = (HistoPrestBean) listeComment.get(i);
		     PrestBean thePrest = PrestBean.getPrestBean(mySalon.getMyDBSession(), 
							       Long.toString(aPrest.getCD_PREST())); %>
                    <% if (CD_CATEG_PREST != thePrest.getCD_CATEG_PREST()) {
                        if ((CD_CATEG_PREST != 0) && (!firstComm)) { %>
                                <td colspan="4" class="small"> idem depuis le
                                    <salon:valeur valeur="<%= lastPrest.getDT_PREST() %>" valeurNulle="null">
                                        %%
                                    </salon:valeur>
                                </td>
                            </tr>
                        <% } %>
                        <tr class="LigneTab1"> 
                            <td class="tabDonnees" colspan="6"> <salon:valeur valeurNulle="null" valeur='<%= DonneeRefBean.getDonneeRefBean(mySalon.getMyDBSession(), "CATEG_PREST", Integer.toString(thePrest.getCD_CATEG_PREST())).toString() %>' > 
                            <span class="readonly">%%</span> </salon:valeur> </td>
                        </tr>
                    <%	lastComm = "";
                        firstComm = true;
                    } %>
                    <% if ((lastCD_PREST != aPrest.getCD_PREST()) || (!lastComm.equals(aPrest.getCOMM()))) {
                        if ((CD_CATEG_PREST != 0) && (!firstComm)) { %>
                                <td colspan="4" class="small"> idem depuis le
                                    <salon:valeur valeur="<%= lastPrest.getDT_PREST() %>" valeurNulle="null">
                                        %%
                                    </salon:valeur>
                                </td>
                            </tr>
                        <% } %>
                        <tr> 
                            <td colspan="2" valign="top" class="tabDonnees"> <salon:valeur valeur="<%= aPrest.getDT_PREST() %>" valeurNulle="null"> 
                                %% </salon:valeur> </td>
                            <td colspan="4" valign="top"> 
                            <salon:valeur valeur="<%= thePrest.toString() %>" valeurNulle="null">
                                %%<br>
                            </salon:valeur>
                            <salon:valeur valeur="<%= aPrest.getCOMM() %>" valeurNulle="null" expand="true"> %% </salon:valeur> </td>
                        <%  lastComm = aPrest.getCOMM();
                            lastCD_PREST = aPrest.getCD_PREST();
                            firstComm = true;
                    } else { 
                        if (firstComm) { 
                            firstComm = false; %>
                            <tr>
                                <td colspan="2" class="tabDonnees">
                                    <salon:valeur valeur="<%= aPrest.getDT_PREST() %>" valeurNulle="null">
                                        %%
                                    </salon:valeur>
                                </td>
                        <% }
                    } 
                    CD_CATEG_PREST = thePrest.getCD_CATEG_PREST();
                    lastPrest = aPrest;
                }  
                if ((CD_CATEG_PREST != 0) && (!firstComm)) { %>
                        <td colspan="4" class="small"> idem depuis le
                            <salon:valeur valeur="<%= lastPrest.getDT_PREST() %>" valeurNulle="null">
                                %%
                            </salon:valeur>
                        </td>
                    </tr>
                <% } %>
	<tr> 
		<td colspan="6" valign="top"> 
			<hr>
			<salon:valeur valeurNulle="null" valeur="<%= aCli.getCOMM() %>" expand="true"> 
			<span class="readonly">%%</span> </salon:valeur> </td>
        </tr>
</table>

<script language="JavaScript">
// Fonctions d'action

// Impression du ticket
function Imprimer()
{
   window.print();
   window.close();
}

</script>
</body>
</html>
