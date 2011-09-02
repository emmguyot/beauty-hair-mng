<%
/*
 * This program is part of InCrEG LibertyLook software http://beauty-hair-mng.sourceforge.net
 * Copyright (C) 2001-2011 Emmanuel Guyot <See emmguyot on SourceForge> 
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
%><%@ page import="java.util.Vector,com.increg.salon.bean.ClientBean" %><%
%><%@ page import="com.increg.salon.bean.SalonSession" %><%
SalonSession mySalon = com.increg.salon.servlet.ConnectedServlet.CheckOrGoHome(request, response);
%><%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %><%
%><%@ taglib uri="WEB-INF/taglibs-i18n.tld" prefix="i18n" %><%
%><i18n:bundle baseName="messages" locale="<%= mySalon.getLangue() %>"/><%
%>Civilité;Nom;Prénom;Adresse;Code Postal;Ville;Téléphone;Portable;Email;Date Anniversaire<%
    response.setContentType("application/download; name=clients.csv"); 
    response.setHeader("Content-Disposition","attachment; filename=clients.csv;");
    // Recupère la liste
    Vector lstLignes = (Vector) request.getAttribute("Liste");
        
    for (int i=0; i< lstLignes.size(); i++) {
        ClientBean aCli = (ClientBean) lstLignes.get(i);
        %>
<%      %><salon:valeur valeurNulle="0" valeur="<%= aCli.getCIVILITE() %>" includeHTML="true">%%</salon:valeur>;<%
        %><salon:valeur valeurNulle="0" valeur="<%= aCli.getNOM() %>" includeHTML="true">"%%"</salon:valeur>;<%
        %><salon:valeur valeurNulle="0" valeur="<%= aCli.getPRENOM() %>" includeHTML="true">"%%"</salon:valeur>;<%
        %><salon:valeur valeurNulle="0" valeur="<%= aCli.getRUE() %>" includeHTML="true">"%%"</salon:valeur>;<%
        %><salon:valeur valeurNulle="0" valeur="<%= aCli.getCD_POSTAL() %>" includeHTML="true">%%</salon:valeur>;<%
        %><salon:valeur valeurNulle="0" valeur="<%= aCli.getVILLE() %>" includeHTML="true">"%%"</salon:valeur>;<%
        %><salon:valeur valeurNulle="0" valeur="<%= aCli.getTEL() %>" includeHTML="true">%%</salon:valeur>;<%
        %><salon:valeur valeurNulle="0" valeur="<%= aCli.getPORTABLE() %>" includeHTML="true">%%</salon:valeur>;<%
        %><salon:valeur valeurNulle="0" valeur="<%= aCli.getEMAIL() %>" includeHTML="true">%%</salon:valeur>;<%
        %><salon:valeur valeurNulle="0" valeur="<%= aCli.getDT_ANNIV() %>" includeHTML="true">%%</salon:valeur><%
    }
%>
