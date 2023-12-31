<%
/*
 * This program is part of InCrEG LibertyLook software http://beauty-hair-mng.sourceforge.net
 * Copyright (C) 2001-2012 Emmanuel Guyot <See emmguyot on SourceForge> 
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
<%@ page import="com.increg.salon.bean.SalonSession" %>
<%
SalonSession mySalon = com.increg.salon.servlet.ConnectedServlet.CheckOrGoHome(request, response);
%>
<%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %>
<%@ taglib uri="WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<i18n:bundle baseName="messages" locale="<%= mySalon.getLangue() %>"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
<link rel="stylesheet" href="style/jquery-ui-1.8.16.custom.css" type="text/css">
</head>
<script language="JavaScript">
<!--
<!--
function MM_reloadPage(init) {  //reloads the window if Nav4 resized
  if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
    document.MM_pgW=innerWidth; document.MM_pgH=innerHeight; onresize=MM_reloadPage; }}
  else if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH) location.reload();
}
MM_reloadPage(true);
// -->

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_showHideLayers() { //v3.0
  var i,p,v,obj,args=MM_showHideLayers.arguments;
  for (i=0; i<(args.length-2); i+=3) if ((obj=MM_findObj(args[i]))!=null) { v=args[i+2];
    if (obj.style) { obj=obj.style; v=(v=='show')?'visible':(v='hide')?'hidden':v; }
    obj.visibility=v; }
}
function Imprimer() {
	if (parent.mainFrame.Imprimer) {
		parent.mainFrame.Imprimer();
	}
	else {
		parent.mainFrame.focus();
		parent.mainFrame.print();
	}
}
//-->
</script>

<body class="action">
<span id="NOUVEAU" style="position:absolute; width:130px; height:31px; z-index:1; left: 15px; top: 11px; visibility: visible">
    <i18n:message key="bouton.Nouveau" id="paramBouton1" />
    <salon:bouton url="javascript:parent.mainFrame.Nouveau()" 
                imgOn="<%= \"images/\" + mySalon.getLangue().getLanguage() + \"/nouveau2.gif\" %>" 
                img="<%= \"images/\" + mySalon.getLangue().getLanguage() + \"/nouveau.gif\" %>" 
                alt="<%= paramBouton1 %>"/>
</span>&nbsp;&nbsp; 
<span id="DOUBLON" style="position:absolute; width:130px; height:31px; z-index:1; left: 15px; top: 11px; visibility: hidden">
    <i18n:message key="bouton.Doublon" id="paramBouton1b" />
    <salon:bouton url="javascript:parent.mainFrame.Doublon()" 
                imgOn="<%= \"images/\" + mySalon.getLangue().getLanguage() + \"/regrouper2.gif\" %>" 
                img="<%= \"images/\" + mySalon.getLangue().getLanguage() + \"/regrouper.gif\" %>" 
                alt="<%= paramBouton1b %>"/>
</span>&nbsp;&nbsp; 
<span id="AIDE" style="position:absolute; width:130px; height:31px; z-index:1; top: 11px; left: 260px; visibility: visible">
   <i18n:message key="bouton.Aide" id="paramBouton2" />
   <salon:bouton url="javascript:parent.mainFrame.Aide()" 
                imgOn="<%= \"images/\" + mySalon.getLangue().getLanguage() + \"/aide2.gif\" %>" 
                img="<%= \"images/\" + mySalon.getLangue().getLanguage() + \"/aide.gif\" %>" 
                alt="<%= paramBouton2 %>"/>
</span> 
<span id="IMPRIMER" style="position:absolute; left:145px; top:11px; width:146px; height:31px; z-index:1; visibility: visible" > 
   <i18n:message key="bouton.Imprimer" id="paramBouton3" />
   <salon:bouton url="javascript:Imprimer()" 
                imgOn="<%= \"images/\" + mySalon.getLangue().getLanguage() + \"/imprimer2.gif\" %>" 
                img="<%= \"images/\" + mySalon.getLangue().getLanguage() + \"/imprimer.gif\" %>" 
                alt="<%= paramBouton3 %>"/>
</span>
<div id="coinHD" style="position:absolute; height:20px; z-index:1; visibility:hidden"><img src="../perso/coin_hd.gif"></div>
<div id="coinBD" style="position:absolute; height:20px; z-index:1; visibility:hidden"><img src="../perso/coin_bd.gif"></div>
<div id="coinHG" style="position:absolute; height:20px; z-index:1; visibility:hidden"><img src="../perso/coin_hg.gif"></div>
<div id="coinBG" style="position:absolute; height:20px; z-index:1; visibility:hidden"><img src="../perso/coin_bg.gif"></div>
<script language="JavaScript">
<!--
Place2Coins();
window.onresize = Place2Coins;
window.onscroll = Place2Coins;

function Place2Coins() {
    PlaceCoins();
    PlaceCoins();
}

function PlaceCoins() {
   MM_findObj("coinHD").style.left = document.body.clientWidth - 20 + document.body.scrollLeft;
   MM_findObj("coinHD").style.top = document.body.scrollTop;
   MM_showHideLayers("coinHD","","show");
   MM_findObj("coinBD").style.left = document.body.clientWidth - 20 + document.body.scrollLeft;
   MM_findObj("coinBD").style.top = document.body.scrollTop + document.body.clientHeight - 20;
   MM_showHideLayers("coinBD","","show");
   MM_findObj("coinHG").style.left = 0 + document.body.scrollLeft;
   MM_findObj("coinHG").style.top = document.body.scrollTop;
   MM_showHideLayers("coinHG","","show");
   MM_findObj("coinBG").style.left = 0 + document.body.scrollLeft;
   MM_findObj("coinBG").style.top = document.body.scrollTop + document.body.clientHeight - 20;
   MM_showHideLayers("coinBG","","show");
}
// -->
</script>
</body>
</html>
