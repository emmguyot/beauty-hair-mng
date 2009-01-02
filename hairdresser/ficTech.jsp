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
<%@ taglib uri="WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<i18n:bundle baseName="messages" locale="<%= mySalon.getLangue() %>"/>
<html>
<head>
<title><i18n:message key="ficTech.title" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees" >
<%@ include file="include/commun.jsp" %>
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
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/ficTech.gif" alt=<salon:TimeStamp bean="<%= aCli %>" />></h1>
<salon:message salonSession="<%= mySalon %>" />
<form method="post" action="ficTech.srv" name="fiche">
<input type="hidden" name="Action" value="" >
<salon:valeur valeurNulle="0" valeur="<%= aCli.getCD_CLI() %>" >
   <input type="hidden" name="CD_CLI" value="%%" >
</salon:valeur>
      <table width="100%"> 
	<tr>
	<td class="label"><span class="obligatoire"><i18n:message key="label.client" /></span> : </td>
			<td> <span class="readonly"><a href="_FicheCli.jsp?Action=Modification&CD_CLI=<%= aCli.getCD_CLI() %>" target="ClientFrame"><%= aCli.toString() %></a></span> 
			</td>
	<% if (mySalon.getMySociete().isSalon()) { %>
	<td class="label"><span class="facultatif"><i18n:message key="label.typeCheveux" /></span> : </td>
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
	<% }
	   if (mySalon.getMySociete().isInstitut()) { %>
	<td class="label"><span class="facultatif"><i18n:message key="label.typePeau" /></span> : </td>
	<td>
	    <%
	       DonneeRefBean typPeau = DonneeRefBean.getDonneeRefBean(mySalon.getMyDBSession(), "TYP_PEAU", Integer.toString(aCli.getCD_TYP_PEAU()));
	       if (typPeau != null) { %>
		  <salon:valeur valeur="<%= typPeau.toString() %>" valeurNulle="null">
		     <span class="readonly">%%</span>
		  </salon:valeur>
	    <% } %>
	    &nbsp;
	</td>
	<% } %>
	<td class="label"><span class="facultatif"><i18n:message key="label.dtAnniversaire" /></span> : </td>
	<td>
		  <salon:valeur valeur="<%= aCli.getDT_ANNIV() %>" valeurNulle="null">
			   <span class="readonly">%%</span>
		  </salon:valeur>
        </td>
	</tr>
	<tr>
	<td class="label"><span class="obligatoire"><i18n:message key="label.categoriePrest" /></span> : </td>
	<td>
		<salon:DBcheckbox nom = "CD_CATEG_PREST" tabValeur = "<%=(String[]) CD_CATEG_PREST_SELECT%>" 
		                  action="document.fiche.submit()" longueurMax = "25" cocheTout = "<%= cocheTout %>"
		                  sql="select CD_CATEG_PREST, LIB_CATEG_PREST from CATEG_PREST order by LIB_CATEG_PREST">
		     %%
	    </salon:DBcheckbox>
	</td>
	<td class="label"><span class="obligatoire"><i18n:message key="label.nbHistorique" /></span> : </td>
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
                                <td class="small"> <i18n:message key="ficTech.idem" />
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
                                <td class="small"> <i18n:message key="ficTech.idem" />
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
                        <td class="small"> <i18n:message key="ficTech.idem" />
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
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideFicheTech.html");
}

</script>
</body>
</html>
