<%@ page import="com.increg.salon.bean.SalonSession" %>
<%
    SalonSession mySalon = (SalonSession) session.getAttribute("SalonSession");
    if (mySalon == null) {
        getServletConfig().getServletContext().getRequestDispatcher("/reconnect.html").forward(request, response);
    }
%>
<html>
<head>
<% 
if (mySalon.getMySociete() != null) { %>
   <title><%= mySalon.getMySociete().getRAIS_SOC() %></title>
<% 
}
else { %>
   <title>InCrEG</title>
<%
} %>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>
<frameset cols="150,*" rows="*" <%@ include file="include/bordure.inc" %>> 
	<frame name="MenuFrame" noresize src="Menu.jsp?<%= request.getQueryString() %>">
	<frame name="ClientFrame" noresize src="accueilPointage.srv">
</frameset>
<noframes> 
<body bgcolor="#FFFFFF" text="#000000">
</body>
</noframes> 
</html>

