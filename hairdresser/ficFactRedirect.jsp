<html>
<head>
<title>Redirection après suppression de factures</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="Expires" content="">
</head>

<body>
<script language="JavaScript">
top.ClientFrame.location.href='_FicheFact.jsp?CD_CLI=<%= request.getParameter("CD_CLI") %>&FACT_HISTO=N';
</script>
</body>
</html>
