<html>
<head>
<title>Rechargement de secours de la base</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
<link rel="shortcut icon" href="images/icone32.ico" >
</head>

<body class="donnees">
<h1>Rechargement de secours de la base</h1>
<p class="erreur"><%= request.getAttribute("Erreur") %></p>
<p><span class="warning">Attention : La base de données n'est plus cohérente.</span> Ceci peut provenir d'un problème durant la dernière restauration de la base. Afin de rétablir la situation, la base va être automatiquement réinitialisée. <span class="important">Ne jamais interrompre cette procédure. Cette procédure n'est pas compatible avec une configuration multi-salons, si c'est votre cas, contactez-nous.</span></p>
<p>Une fois cette réinitialisation effectuée, vous pourrez restaurer depuis Internet ou depuis votre disque pour récupérer vos données.</p>
<p class="important">Si vous n'êtes pas familier avec cette procédure, <a href="contact.html" target="_blank">contactez-nous.</a></p>
<a class="nohover" href="restaurationAuto.srv" onMouseOver="document.valider_gif.src='images/valider2.gif'" onMouseOut="document.valider_gif.src='images/valider.gif'"><img name="valider_gif" src="images/valider.gif" border=0 alt="Valider la restauration automatique"></a>
</body>
</html>
