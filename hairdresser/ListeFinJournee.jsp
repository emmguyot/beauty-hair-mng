<html>
<head>
<title>Edition de la fin de journée</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>
<frameset rows="*,53" cols="*" <%@ include file="include/bordure.inc" %> onload="mainFrame.Init()"> 
	<frame name="mainFrame" src="rechFinJournee.srv?<%= request.getQueryString() %>">
	<frame name="bottomFrame" scrolling="NO" noresize src="actionLst.jsp">
</frameset><noframes></noframes>
</html>
