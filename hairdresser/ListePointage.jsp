<html>
<head>
<title>Liste des pointages</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>
<frameset rows="*,53" cols="*" <%@ include file="include/bordure.inc" %>> 
	<frame name="mainFrame" src="rechPointage.srv?<%= request.getQueryString() %>">
	<frame name="bottomFrame" scrolling="NO" noresize src="actionLst.jsp">
</frameset><noframes></noframes>
</html>
