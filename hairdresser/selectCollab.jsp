<%
/*
 * Choisi le collab à affecter
 * Copyright (C) 2011-2012 Emmanuel Guyot <See emmguyot on SourceForge> 
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
<%@ page contentType="text/javascript" %>
<%@ page import="java.util.List" %>
<%@ page import="com.increg.salon.bean.SalonSession,
				com.increg.salon.bean.CollabBean,
				com.increg.salon.bean.FactBean" %>
<%
SalonSession mySalon = com.increg.salon.servlet.ConnectedServlet.CheckOrGoHome(request, response);
%>
<%@ taglib prefix="fmt" uri="WEB-INF/fmt.tld" %>
<%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %>
<fmt:setBundle basename="messages" scope="session" />

/* Script Javascript */
<%
   // Récupération des paramètres
   List<CollabBean> lstCollab = (List<CollabBean>) request.getAttribute("lstCollab");
   FactBean fact = mySalon.getListeFact().lastElement();
%>

if ($('#dialog').length == 0) {
	$("form").append('<div id="dialog" style="display:none"></div>');
}

var contenu = '<form method="post" name="fiche">'
	+ '<p>' + "<fmt:message key="selectCollab.description" />" + '</p>'
	+ "<fmt:message key="label.collaborateur" /> : "
	+ '<salon:selection valeur="<%= fact.getCD_COLLAB() %>" valeurs="<%= lstCollab %>"><select name="CD_COLLAB" id="CD_COLLAB">%%</select></salon:selection>'
	+ '</form>';

$('#dialog').html(contenu);

$('#dialog').dialog({
	title: '<fmt:message key="title.selectCollab" />',
	autoOpen: true,
	width: 600,
	buttons: {
		'<fmt:message key="bouton.Valider" />': function() {
			$(this).dialog("close"); 
			$.ajaxSetup({
				cache: false
			});
			$.getScript('addCli.srv?Action=Modification&CD_FACT=<%= fact.getCD_FACT() %>&CD_COLLAB=' + $("#CD_COLLAB").val());
		}, 
		'<fmt:message key="bouton.Annuler" />': function() { 
			$(this).dialog("close"); 
			$.getScript('refreshMenu.js');
		} 
	},
	close: function() {
		$.getScript('refreshMenu.js');
	}
});

