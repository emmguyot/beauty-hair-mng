<%
/*
 * This program is part of InCrEG LibertyLook software http://beauty-hair-mng.sourceforge.net
 * Copyright (C) 2001-2008 Emmanuel Guyot <See emmguyot on SourceForge> 
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
<%@ page import="com.increg.salon.bean.SalonSession" %>
<%
    SalonSession salonLocal = (SalonSession) session.getAttribute("SalonSession");
    if (salonLocal != null) {
%>
<script language="JavaScript" src="include/<%= salonLocal.getLangue().getLanguage() %>/dateHome.js"> </script>
<%
    }
%>
<script src="include/core.jsp" language="JavaScript"></script>
<script src="include/AnchorPosition.js" language="JavaScript"></script>
<script src="include/PopupWindow.js" language="JavaScript"></script>
<script src="include/date.js" language="JavaScript"></script>
<script src="include/CalendarPopup.js" language="JavaScript"></script>
<script language="JavaScript">
    top.document.title = document.title;
</script>
