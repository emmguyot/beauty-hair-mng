<html>
<head>
<title>Récap des stocks</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>
<frameset rows="*,53" cols="*" <%@ include file="include/bordure.inc" %> onload="mainFrame.Init()"> 
	<frame name="mainFrame" src="rechRecap.srv?<%= request.getQueryString() %>">
	<frame name="bottomFrame" scrolling="NO" noresize src="actionLst.jsp">
</frameset><noframes></noframes>
</html>
