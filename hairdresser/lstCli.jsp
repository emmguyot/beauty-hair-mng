<%@ page import="com.increg.salon.bean.SalonSession,
				java.util.Vector,
				com.increg.salon.bean.ClientBean" %>
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
<title>Liste des clients</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees">
<%
    // Récupération des paramètres
    String premLettre = (String) request.getAttribute("premLettre");
    String INDIC_VALID = (String) request.getAttribute("INDIC_VALID");
    Vector listeLignes = (Vector) request.getAttribute("Liste");
    int longueurCle = 2;
%>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/lstCli.gif"></h1>
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
	    <% if ((INDIC_VALID != null) && (INDIC_VALID.equals("on"))) { %> checked <% } %>
    	onClick="document.fiche.submit()" >
    <%
    if (listeLignes.size() > 20) { 
    %>
        <br/>Seconde lettre :
        <%
        String lastKey = "";
		for (int i = 0; i < listeLignes.size(); i++) {
	    	ClientBean aCli = (ClientBean) listeLignes.get(i);

            String nextKey = aCli.getNOM().substring(0,Math.min(aCli.getNOM().length(), longueurCle)); 
            if (!nextKey.equals(lastKey))  {
        %>
                <a href="#<%= nextKey %>"><%= nextKey %></a>&nbsp;&nbsp;&nbsp;
        <%
            }
            lastKey = nextKey;
        }
    }
    %>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<salon:bouton url="rechCli.srv?type=advanced" img="images/rechAvancee.gif" alt="Recherche avancée..."/>
	<input type="hidden" name="type" value="simple">
	</p>
</form>

<%@ include file="lstCli_Common.jsp" %>

</body>
</html>
