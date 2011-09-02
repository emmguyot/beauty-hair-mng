<%
/*
 * This program is part of InCrEG LibertyLook software http://beauty-hair-mng.sourceforge.net
 * Copyright (C) 2001-2009 Emmanuel Guyot <See emmguyot on SourceForge> 
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
<html>
<head>
<title>Installation incomplète ou incorrecte du logiciel</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
<link rel="shortcut icon" href="images/favicon.ico" >
</head>

<body class="donnees">
<h1>Installation incomplète ou incorrecte du logiciel</h1>
<p class="erreur"><%= request.getAttribute("Erreur") %></p>
<p><span class="warning">Attention : Le logiciel est mal ou seulement partiellement installé.</span> Veuillez vérifier que le fichier de configuration (config.properties) est bien dans le répertoire c:\InCrEG\Perso</p>
<p class="important">Si vous n'êtes pas familier avec cette procédure, <a href="fr/contact.html" target="_blank">contactez-nous.</a></p>
<p>Une fois le fichier au bon endroit, vous devez arrêter le logiciel (icône "feu rouge") et le relancer (icône "feu vert").</p>
</body>
</html>
