<%@ page import="java.util.Vector,com.increg.salon.bean.ClientBean" %><%
%><%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %><%
%>Civilité;Nom;Prénom;Adresse;Code Postal;Ville;Téléphone;Portable;Email;Date Anniversaire<%
    response.setContentType("application/download; name=clients.csv"); 
    response.setHeader("Content-Disposition","attachment; filename=clients.csv;");
    // Recupère la liste
    Vector lstLignes = (Vector) request.getAttribute("Liste");
        
    for (int i=0; i< lstLignes.size(); i++) {
        ClientBean aCli = (ClientBean) lstLignes.get(i);
        %>
<%      %><salon:valeur valeurNulle="0" valeur="<%= aCli.getCIVILITE() %>" expand="true">%%</salon:valeur>;<%
        %><salon:valeur valeurNulle="0" valeur="<%= aCli.getNOM() %>" expand="true">%%</salon:valeur>;<%
        %><salon:valeur valeurNulle="0" valeur="<%= aCli.getPRENOM() %>" expand="true">%%</salon:valeur>;<%
        %><salon:valeur valeurNulle="0" valeur="<%= aCli.getRUE() %>" expand="false">"%%"</salon:valeur>;<%
        %><salon:valeur valeurNulle="0" valeur="<%= aCli.getCD_POSTAL() %>" expand="true">%%</salon:valeur>;<%
        %><salon:valeur valeurNulle="0" valeur="<%= aCli.getVILLE() %>" expand="true">%%</salon:valeur>;<%
        %><salon:valeur valeurNulle="0" valeur="<%= aCli.getTEL() %>" expand="true">%%</salon:valeur>;<%
        %><salon:valeur valeurNulle="0" valeur="<%= aCli.getPORTABLE() %>" expand="true">%%</salon:valeur>;<%
        %><salon:valeur valeurNulle="0" valeur="<%= aCli.getEMAIL() %>" expand="true">%%</salon:valeur>;<%
        %><salon:valeur valeurNulle="0" valeur="<%= aCli.getDT_ANNIV() %>" expand="true">%%</salon:valeur><%
    }
%>
