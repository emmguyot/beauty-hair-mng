<html>
<head>
<title>Fiche Critères de Publipostage</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>
<frameset rows="*,1,53" cols="*" <%@ include file="include/bordure.inc" %> onload="mainFrame.Init()"> 
	<frame name="mainFrame" src="ficCriterePub.srv?<%= request.getQueryString() %>">
	<frame name="resultFrame" scrolling="NO" noresize src="">
	<frame name="bottomFrame" scrolling="NO" noresize src="actionFic.jsp">
</frameset><noframes></noframes>
</html>
