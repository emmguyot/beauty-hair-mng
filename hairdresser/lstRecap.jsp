<%
/*
 * This program is part of InCrEG LibertyLook software http://beauty-hair-mng.sourceforge.net
 * Copyright (C) 2001-2006 Emmanuel Guyot <See emmguyot on SourceForge> 
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
<%@ page import="java.util.TreeMap,java.util.Set,java.util.Iterator,java.math.BigDecimal,java.util.Calendar" %>
<%@ page import="com.increg.salon.bean.SalonSession,
                com.increg.salon.request.Recap
	        " %>
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
<title><i18n:message key="label.recapStock" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees">
<%@ include file="include/commun.jsp" %>
<script language="JavaScript">
<!--
function Init() {
   <%
   // Positionne les liens d actions
   %>
   MM_showHideLayers('NOUVEAU?bottomFrame','','hide');
}
//-->
</script>
<%
   // Récupération des paramètres
   Calendar DT_DEBUT = (Calendar) request.getAttribute("DT_DEBUT");
   Calendar DT_FIN = (Calendar) request.getAttribute("DT_FIN");
   String valeur = request.getParameter("valeur");
   String[] CD_TYP_MVT_SELECT = request.getParameterValues("CD_TYP_MVT");
   Boolean cocheTout = (Boolean) request.getAttribute("cocheTout");
%>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/lstRecap.gif"></h1>
<form name="fiche" action="rechRecap.srv" method="post">
<p>
<i18n:message key="label.entreLe" /> :
<i18n:message key="format.dateSimpleDefaut" id="formatDate" />
    <salon:date type="text" name="DT_DEBUT" valeurDate="<%= DT_DEBUT %>" valeurNulle="null" format="<%= formatDate %>" calendrier="true" onchange="document.fiche.submit()">%%</salon:date>
<i18n:message key="label.etLe" /> : 
    <salon:date type="text" name="DT_FIN" valeurDate="<%= DT_FIN %>" valeurNulle="null" format="<%= formatDate %>" calendrier="true" onchange="document.fiche.submit()">%%</salon:date>
</p>
<table><tr><td><i18n:message key="label.typeMouvement" />:</td><td>
	<salon:DBcheckbox nom = "CD_TYP_MVT" tabValeur = "<%=(String[]) CD_TYP_MVT_SELECT%>" 
	                  action="document.fiche.submit()" cocheTout = "<%= cocheTout %>"
	                  sql="select CD_TYP_MVT, LIB_TYP_MVT from TYP_MVT order by CD_TYP_MVT">
	     %%
    </salon:DBcheckbox>
</td></tr></table>
</form>
<hr>
<%
// Recupère les listes
TreeMap lstTypes = (TreeMap) request.getAttribute("ListeType");
TreeMap lstLignes = (TreeMap) request.getAttribute("Liste");
%>
<table width="100%" border="1" rules="groups">
<colgroup>
<colgroup span="<%= lstTypes.size() %>">
    <tr>
        <th rowspan=2><i18n:message key="label.article" /></th>
        <% 
        {
            Set keys = lstTypes.keySet();
            for (Iterator i= keys.iterator(); i.hasNext(); ) { %>
                <th><%= (String) lstTypes.get(i.next()) %></th>
            <%
            }
        } %>
    </tr>
    <tbody>
    <%    
	
    /** 
     * Initialise les totaux
     */
    Recap lstTotaux = new Recap();
    Set typesKeys = lstTypes.keySet();
    for (Iterator j=typesKeys.iterator(); j.hasNext(); ) { 
        Integer indice = (Integer) j.next();
        lstTotaux.setMVT(indice.intValue(), new BigDecimal("0"), new BigDecimal("0"));
    }

    String finClass = "3";
    Set keys = lstLignes.keySet();
    for (Iterator i=keys.iterator(); i.hasNext(); ) {
        Recap aRecap = (Recap) lstLignes.get(i.next());
        finClass = (finClass.equals("3")) ? "2" : "3";
	%>
	<tr class="ligneTab2">
            <td rowspan=2 class="ligneTab<%= finClass %>">
                <salon:valeur valeurNulle="null" valeur="<%= aRecap.getLIB_ART() %>" >
                    <a href="_FicheArt_Mvt.jsp?Action=Modification&CD_ART=<%= aRecap.getCD_ART() %>" target="ClientFrame">%%</a>
                </salon:valeur>
	    </td>
	    <%
            typesKeys = lstTypes.keySet();
            for (Iterator j=typesKeys.iterator(); j.hasNext(); ) { 
                Integer indice = (Integer) j.next();
                if (aRecap.getMVT(indice.intValue()) != null) {
                    lstTotaux.setMVT(indice.intValue(), 
                                    lstTotaux.getMVT(indice.intValue()).add(aRecap.getMVT(indice.intValue())),
                                    lstTotaux.getMVT_VAL(indice.intValue()).add(aRecap.getMVT_VAL(indice.intValue())));
                } %>
                <td class="Nombre">
                    <salon:valeur valeurNulle="null" valeur="<%= aRecap.getMVT(indice.intValue()) %>" >
                        %%&nbsp;
                    </salon:valeur>
                </td>
	    <%
            } %>
        </tr>
	<tr class="ligneTab3">
        <%
            typesKeys = lstTypes.keySet();
            for (Iterator j=typesKeys.iterator(); j.hasNext(); ) { 
                Integer indice = (Integer) j.next();
                %>
                <td class="Nombre">
                    <salon:valeur valeurNulle="null" valeur="<%= aRecap.getMVT_VAL(indice.intValue()) %>" >
                        %%&nbsp;<%= mySalon.getDevise().toString() %>
                    </salon:valeur>
                </td>
	    <%
            } %>
	</tr>
    <%
    }
    %>
    </tbody>
    <tfoot>
	<tr class="ligneTab2">
            <td rowspan=2></td>
	    <%
            typesKeys = lstTypes.keySet();
            for (Iterator j=typesKeys.iterator(); j.hasNext(); ) { 
                Integer indice = (Integer) j.next();
                %>
                <td class="Nombre">
                    <salon:valeur valeurNulle="null" valeur="<%= lstTotaux.getMVT(indice.intValue()) %>" >
                        %%&nbsp;
                    </salon:valeur>
                </td>
	    <% } %>
	</tr>
	<tr class="ligneTab3">
	    <%
            typesKeys = lstTypes.keySet();
            for (Iterator j=typesKeys.iterator(); j.hasNext(); ) { 
                Integer indice = (Integer) j.next();
                %>
                <td class="Nombre">
                    <salon:valeur valeurNulle="null" valeur="<%= lstTotaux.getMVT_VAL(indice.intValue()) %>" >
                        %%&nbsp;<%= mySalon.getDevise().toString() %>
                    </salon:valeur>
                </td>
	    <% } %>
	</tr>
    </tfoot>
</table>
<salon:madeBy />
<script language="JavaScript">
<!--

// Affichage de l'aide
function Aide()
{
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideListe.html");
}

//-->
</script>
</body>
</html>
