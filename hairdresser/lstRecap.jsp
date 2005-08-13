<%@ page import="java.util.TreeMap,java.util.Set,java.util.Iterator,java.math.BigDecimal,java.util.Date" %>
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
<title>Récap des stocks</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees">
<%@ include file="include/commun.js" %>
<script language="JavaScript">
<!--
function Init() {
   <%
   // Positionne les liens d'actions
   %>
   MM_showHideLayers('NOUVEAU?bottomFrame','','hide');
}
//-->
</script>
<%
   // Récupération des paramètres
   Date DT_DEBUT = (Date) request.getAttribute("DT_DEBUT");
   Date DT_FIN = (Date) request.getAttribute("DT_FIN");
   String valeur = request.getParameter("valeur");
   String[] CD_TYP_MVT_SELECT = request.getParameterValues("CD_TYP_MVT");
   Boolean cocheTout = (Boolean) request.getAttribute("cocheTout");
%>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/lstRecap.gif"></h1>
<form name="fiche" action="rechRecap.srv" method="post">
<p>
Entre le :
    <salon:date type="text" name="DT_DEBUT" valeurDate="<%= DT_DEBUT %>" valeurNulle="null" format="dd/MM/yyyy" calendrier="true" onchange="document.fiche.submit()">%%</salon:date>
   et le : 
    <salon:date type="text" name="DT_FIN" valeurDate="<%= DT_FIN %>" valeurNulle="null" format="dd/MM/yyyy" calendrier="true" onchange="document.fiche.submit()">%%</salon:date>
</p>
<table><tr><td>Type&nbsp;de&nbsp;mouvement&nbsp;:</td><td>
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
        <th rowspan=2>Article</th>
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
    window.open("aideListe.html");
}

//-->
</script>
</body>
</html>
