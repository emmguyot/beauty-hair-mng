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
<p><span class="warning">Attention : La base de donn�es n'est plus coh�rente.</span> Ceci peut provenir d'un probl�me durant la derni�re restauration de la base. Afin de r�tablir la situation, la base va �tre automatiquement r�initialis�e. <span class="important">Ne jamais interrompre cette proc�dure. Cette proc�dure n'est pas compatible avec une configuration multi-salons, si c'est votre cas, contactez-nous.</span></p>
<p>Une fois cette r�initialisation effectu�e, vous pourrez restaurer depuis Internet ou depuis votre disque pour r�cup�rer vos donn�es.</p>
<p class="important">Si vous n'�tes pas familier avec cette proc�dure, <a href="contact.html" target="_blank">contactez-nous.</a></p>
<a class="nohover" href="restaurationAuto.srv" onMouseOver="document.valider_gif.src='images/valider2.gif'" onMouseOut="document.valider_gif.src='images/valider.gif'"><img name="valider_gif" src="images/valider.gif" border=0 alt="Valider la restauration automatique"></a>
</body>
</html>
