<html>
<head>
<title>Fiche Technique</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>
<frameset rows="*,53" cols="*" <%@ include file="include/bordure.inc" %> onload="mainFrame.Init()"> 
	<frame name="mainFrame" src="ficTech.srv?<%= request.getQueryString() %>">
	<frame name="bottomFrame" scrolling="NO" noresize src="actionFic.jsp">
</frameset><noframes></noframes>
</html>
