<%@ page import="java.util.Vector,java.math.BigDecimal,java.util.Calendar,java.util.Date" %>
<%@ page import="com.increg.salon.request.CA
	       " %>
<%@ page import="com.increg.salon.bean.SalonSession" %>
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
<title>Chiffre d'affaires</title>
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
   String CD_COLLAB = request.getParameter("CD_COLLAB");
   Date DT_DEBUT = (Date) request.getAttribute("DT_DEBUT");
   Date DT_FIN = (Date) request.getAttribute("DT_FIN");
%>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/lstCA.gif"></h1>
<form name="fiche" action="rechCA.srv" method="post">
<p>
Collaborateur :
<salon:DBselection valeur="<%= CD_COLLAB %>" sql="select CD_COLLAB, PRENOM from COLLAB order by PRENOM, NOM">
   <select name="CD_COLLAB" onChange="document.fiche.submit()">
      <option value="">( Tous )</option>
      %%
   </select>
</salon:DBselection>
</p>
<p>
Entre le :
    <salon:date type="text" name="DT_DEBUT" valeurDate="<%= DT_DEBUT %>" valeurNulle="null" format="dd/MM/yyyy" calendrier="true" onchange="document.fiche.submit()">%%</salon:date>
   et le : 
    <salon:date type="text" name="DT_FIN" valeurDate="<%= DT_FIN %>" valeurNulle="null" format="dd/MM/yyyy" calendrier="true" onchange="document.fiche.submit()">%%</salon:date>
</p>
</form>
<hr>
<%
// Recupère les listes
Vector lstTypes = (Vector) request.getAttribute("ListeType");
Vector lstLignes = (Vector) request.getAttribute("Liste");
%>
<table width="100%" border="1" rules="groups">
<colgroup>
<colgroup>
<colgroup span="<%= lstTypes.size() %>">
<colgroup>
	<tr>
		<th>Mois</th>
		<th>Collaborateur</th>
		<% for (int i=0; i< lstTypes.size(); i++) { %>
		  <th><%= (String) lstTypes.get(i) %></th>
	        <% } %>
		<th>CA TTC total<br/>Hors remises</th>
	</tr>
	<tbody>
	<%    
    BigDecimal fullTotal = new BigDecimal(0);
    BigDecimal fullTotalType[] = new BigDecimal[lstTypes.size()];
	for (int j=0; j < lstTypes.size(); j++) {
		fullTotalType[j] = new BigDecimal(0);
	}
	for (int i=0; i< lstLignes.size(); ) {
	    CA aCA = (CA) lstLignes.get(i);
	%>
	<tr>
	    <td class="tabDonnees">
	    <% 
		  java.text.SimpleDateFormat formatDate  = new java.text.SimpleDateFormat("MMMM yyyy");
		  String mois = formatDate.format(aCA.getDT_PREST().getTime());
		  Calendar DT_PREST_orig = aCA.getDT_PREST();
          String PRENOM_orig = aCA.getPRENOM();
	    %>
	       <salon:valeur valeurNulle="null" valeur="<%= mois %>" >
		     %%
	       </salon:valeur>
	    </td>
	    <td class="tabDonnees">
	       <salon:valeur valeurNulle="null" valeur="<%= aCA.getPRENOM() %>" >
		     %%
	       </salon:valeur>
	    </td>
	    <%
	    BigDecimal total = new BigDecimal(0);
	    total.setScale(2);
	    for (int j=0; j< lstTypes.size(); j++) { %>
	       <td class="Nombre">
		  
	       <%
	       if (DT_PREST_orig.equals(aCA.getDT_PREST())
                    && PRENOM_orig.equals(aCA.getPRENOM())
					&& (aCA.getLIB_TYP_VENT() != null) 
					&& (aCA.getLIB_TYP_VENT().equals((String) lstTypes.get(j)))) { %>
			  <salon:valeur valeurNulle="null" valeur="<%= aCA.getMONTANT() %>" >
			     %%
			  </salon:valeur>
	       <%
	          if (aCA.getMONTANT() != null) {
			    total = total.add(aCA.getMONTANT());
		      }
      		  fullTotalType[j] = fullTotalType[j].add(aCA.getMONTANT());
		      
		      i++;
		      if (i < lstLignes.size()) {
			    aCA = (CA) lstLignes.get(i);
		      }
		      else {
			    aCA = new CA();
		      }
		   }
		   else { %>
		      &nbsp;
	       <%
	       } %>
	       </td>
	    <%
	    } 
        fullTotal = fullTotal.add(total);
        %>
	    <td class="Nombre">
	       <%= total %>
	    </td>
	</tr>
	<%
	}
	%>
	</tbody>
        <tfoot>
        <tr>
            <td colspan="2" class="Total">Total</td>
            <%
			for (int j=0; j < lstTypes.size(); j++) {
			%>
            <td class="Nombre">
			  <salon:valeur valeurNulle="null" valeur="<%= fullTotalType[j] %>" >
			     %%
			  </salon:valeur>
            </td>
            <%
			}
			%>
            <td class="Nombre">
                <salon:valeur valeur="<%= fullTotal %>" valeurNulle="null"> %% </salon:valeur>
            </td>
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
