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
<body class="donnees" >
<%@ include file="include/commun.js" %>
<script language="JavaScript">
<!--
<%
   // Récupération des paramètres
   ClientBean aCli = (ClientBean) request.getAttribute("ClientBean");
   Vector listeComment = (Vector) request.getAttribute("listeComment");
   String NbComment = (String) request.getAttribute("NbComment");
   String[] CD_CATEG_PREST_SELECT = request.getParameterValues("CD_CATEG_PREST");
   Boolean cocheTout = (Boolean) request.getAttribute("cocheTout");
%>

function Init() {
   MM_showHideLayers('SUPPRIMER?bottomFrame','','hide');
   MM_showHideLayers('ENREGISTRER?bottomFrame','','hide');
   MM_showHideLayers('IMPRIMER?bottomFrame','','show');
   MM_showHideLayers('DUPLIQUER?bottomFrame','','hide');
}
//-->
</script>
<h1><img src="images/titres/ficTech.gif" alt=<salon:TimeStamp bean="<%= aCli %>" />></h1>
<salon:message salonSession="<%= mySalon %>" />
<form method="post" action="ficTech.srv" name="fiche">
<input type="hidden" name="Action" value="" >
<salon:valeur valeurNulle="0" valeur="<%= aCli.getCD_CLI() %>" >
   <input type="hidden" name="CD_CLI" value="%%" >
</salon:valeur>
      <table width="100%"> 
	<tr>
	<td class="label"><span class="obligatoire">Client</span> : </td>
			<td> <span class="readonly"><a href="_FicheCli.jsp?Action=Modification&CD_CLI=<%= aCli.getCD_CLI() %>" target="ClientFrame"><%= aCli.toString() %></a></span> 
			</td>
	<td class="label"><span class="facultatif">Type de peau</span> : </td>
	<td>
	    <%
	       DonneeRefBean typChev = DonneeRefBean.getDonneeRefBean(mySalon.getMyDBSession(), "TYP_CHEV", Integer.toString(aCli.getCD_TYP_CHEV()));
	       if (typChev != null) { %>
		  <salon:valeur valeur="<%= typChev.toString() %>" valeurNulle="null">
		     <span class="readonly">%%</span>
		  </salon:valeur>
	    <% } %>
	    &nbsp;
	</td>
	<td class="label"><span class="facultatif">Date d'anniversaire</span> : </td>
	<td>
		  <salon:valeur valeur="<%= aCli.getDT_ANNIV() %>" valeurNulle="null">
			   <span class="readonly">%%</span>
		  </salon:valeur>
        </td>
	</tr>
	<tr>
	<td class="label"><span class="obligatoire">Catégories de prestation</span> : </td>
	<td>
		<salon:DBcheckbox nom = "CD_CATEG_PREST" tabValeur = "<%=(String[]) CD_CATEG_PREST_SELECT%>" 
		                  action="document.fiche.submit()" longueurMax = "25" cocheTout = "<%= cocheTout %>"
		                  sql="select CD_CATEG_PREST, LIB_CATEG_PREST from CATEG_PREST order by LIB_CATEG_PREST">
		     %%
	    </salon:DBcheckbox>
	</td>
	<td class="label"><span class="obligatoire">Nombre d'historiques</span> : </td>
	<td class="tabDonneesGauche">
	    <salon:valeur valeurNulle="null" valeur="<%= NbComment %>" >
	       <input type="text" name="NbComment" size="2" maxlength="2" value="%%" onChange="document.fiche.submit()">
	    </salon:valeur>
	</td>
	 </tr>
	 </table>
	<hr>
	<table border="0" cellspacing="0">
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
                                <td class="small"> idem depuis le
                                    <salon:valeur valeur="<%= lastPrest.getDT_PREST() %>" valeurNulle="null">
                                        %%
                                    </salon:valeur>
                                </td>
                            </tr>
                        <% } %>
                        <tr class="LigneTab1">
                            <td class="tabDonnees" colspan="2">
			      <salon:valeur valeurNulle="null" valeur='<%= DonneeRefBean.getDonneeRefBean(mySalon.getMyDBSession(), "CATEG_PREST", Integer.toString(thePrest.getCD_CATEG_PREST())).toString() %>' >
				 <span class="readonly">%%</span>
			      </salon:valeur>
                            </td>
                        </tr>
			<% lastComm = "";
                           firstComm = true;
                    } %>
                    <% if ((lastCD_PREST != aPrest.getCD_PREST()) || (!lastComm.equals(aPrest.getCOMM()))) {
                        if ((CD_CATEG_PREST != 0) && (!firstComm)) { %>
                                <td class="small"> idem depuis le
                                    <salon:valeur valeur="<%= lastPrest.getDT_PREST() %>" valeurNulle="null">
                                        %%
                                    </salon:valeur>
                                </td>
                            </tr>
                        <% } %>
                        <tr> 
                        <td class="tabDonnees" width="100">
                            <salon:valeur valeur="<%= aPrest.getDT_PREST() %>" valeurNulle="null">
                                %%
                            </salon:valeur>
                        </td>
                        <td>
                            <salon:valeur valeur="<%= thePrest.toString() %>" valeurNulle="null">
                                %%<br>
                            </salon:valeur>
                            <salon:valeur valeur="<%= aPrest.getCOMM() %>" valeurNulle="null" expand="true">
                                %%
                            </salon:valeur>
                        </td>
                    <%  lastComm = aPrest.getCOMM();
                        lastCD_PREST = aPrest.getCD_PREST();
                        firstComm = true;
                    } else {
                        if (firstComm) { 
                            firstComm = false; %>
                            <tr>
                                <td class="tabDonnees" width="100">
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
                        <td class="small"> idem depuis le
                            <salon:valeur valeur="<%= lastPrest.getDT_PREST() %>" valeurNulle="null">
                                %%
                            </salon:valeur>
                        </td>
                    </tr>
                <% } %>
	</table>
	<hr>
	    <salon:valeur valeurNulle="null" valeur="<%= aCli.getCOMM() %>" expand="true">
	       <span class="readonly">%%</span>
	    </salon:valeur>

</form>

<script language="JavaScript">
// Fonctions d'action

// Impression du ticket
function Imprimer()
{
   document.fiche.Action.value = "Impression";
   document.fiche.target="_blank";
   document.fiche.submit();
   document.fiche.Action.value = "";
   document.fiche.target="";
}

// Affichage de l'aide
function Aide()
{
    window.open("aideFicheTech.html");
}

</script>
</body>
</html>
