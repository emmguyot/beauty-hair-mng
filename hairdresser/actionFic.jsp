<html>
<head>
<title>Untitled Document</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
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
</head>

<body class="action">
<span id="ENREGISTRER" style="position:absolute; width:146px; height:31px; z-index:1; left: 15px; top: 11px; visibility: hidden">
   <a class="nohover" href="javascript:parent.mainFrame.Enregistrer()" onMouseOver="document.enregistrer_gif.src='images/enregistrer2.gif'" onMouseOut="document.enregistrer_gif.src='images/enregistrer.gif'"><img name="enregistrer_gif" src="images/enregistrer.gif" border=0 alt="Enregistrer"></a>
</span>
<span id="VALIDER" style="position:absolute; width:146px; height:31px; z-index:1; left: 15px; top: 11px; visibility: hidden">
   <a class="nohover" href="javascript:parent.mainFrame.Valider()" onMouseOver="document.valider_gif.src='images/valider2.gif'" onMouseOut="document.valider_gif.src='images/valider.gif'"><img name="valider_gif" src="images/valider.gif" border=0 alt="Valider"></a></span>
<span id="DUPLIQUER" style="position:absolute; width:146px; height:31px; z-index:1; top: 11px; left: 145px; visibility: hidden">
   <a class="nohover" href="javascript:parent.mainFrame.Dupliquer()" onMouseOver="document.dupliquer_gif.src='images/dupliquer2.gif'" onMouseOut="document.dupliquer_gif.src='images/dupliquer.gif'"><img name="dupliquer_gif" src="images/dupliquer.gif" border=0 alt="Dupliquer"></a>
</span> 
<span id="NOUVEAU" style="position:absolute; width:146px; height:31px; z-index:1; top: 11px; left: 145px; visibility: hidden">
   <a class="nohover" href="javascript:parent.mainFrame.Nouveau()" onMouseOver="document.nouveau_gif.src='images/nouveau2.gif'" onMouseOut="document.nouveau_gif.src='images/nouveau.gif'"><img name="nouveau_gif" src="images/nouveau.gif" border=0 alt="Nouveau"></a>
</span> 
<span id="IMPRIMER" style="position:absolute; left:275px; top:11px; width:146px; height:31px; z-index:1; visibility: visible" > 
   <a class="nohover" href="javascript:Imprimer()" onMouseOver="document.imprimer_gif.src='images/imprimer2.gif'" onMouseOut="document.imprimer_gif.src='images/imprimer.gif'"><img name="imprimer_gif" src="images/imprimer.gif" border=0 alt="Imprimer"></a>
</span>
<span id="SUPPRIMER" style="position:absolute; left:405px; top:11px; width:146px; height:31px; z-index:1; visibility: hidden" > 
   <a class="nohover" href="javascript:parent.mainFrame.Supprimer()" onMouseOver="document.supprimer_gif.src='images/supprimer2.gif'" onMouseOut="document.supprimer_gif.src='images/supprimer.gif'"><img name="supprimer_gif" src="images/supprimer.gif" border=0 alt="Supprimer"></a>
</span>
<span id="RETOUR_LISTE" style="position:absolute; left:535px; top:11px; width:146px; height:31px; z-index:1; visibility: hidden" > 
   <a class="nohover" href="javascript:parent.mainFrame.RetourListe()" onMouseOver="document.liste_gif.src='images/liste2.gif'" onMouseOut="document.liste_gif.src='images/liste.gif'"><img name="liste_gif" src="images/liste.gif" border=0 alt="Liste"></a>
</span>
<span id="AIDE" style="position:absolute; width:146px; height:31px; z-index:1; top: 11px; left: 665px; visibility: visible">
   <a class="nohover" href="javascript:parent.mainFrame.Aide()" onMouseOver="document.aide_gif.src='images/aide2.gif'" onMouseOut="document.aide_gif.src='images/aide.gif'"><img name="aide_gif" src="images/aide.gif" border=0 alt="Aide"></a>
</span> 
<div id="coinHD" style="position:absolute; height=20px; z-index:1; visibility:hidden"><img src="images/perso/coin_hd.gif"></div>
<div id="coinBD" style="position:absolute; height=20px; z-index:1; visibility:hidden"><img src="images/perso/coin_bd.gif"></div>
<div id="coinHG" style="position:absolute; height=20px; z-index:1; visibility:hidden"><img src="images/perso/coin_hg.gif"></div>
<div id="coinBG" style="position:absolute; height=20px; z-index:1; visibility:hidden"><img src="images/perso/coin_bg.gif"></div>
<SCRIPT FOR=window EVENT=onscroll LANGUAGE="JScript">
PlaceCoins()
PlaceCoins()
</script>
<SCRIPT FOR=window EVENT=onresize LANGUAGE="JScript">
PlaceCoins()
PlaceCoins()
</script>
<script language="JavaScript">
<!--
PlaceCoins()
PlaceCoins()

function PlaceCoins() {
   document.all["coinHD"].style.left = document.body.clientWidth - 20 + document.body.scrollLeft;
   document.all["coinHD"].style.top = document.body.scrollTop;
   MM_showHideLayers("coinHD","","show");
   document.all["coinBD"].style.left = document.body.clientWidth - 20 + document.body.scrollLeft;
   document.all["coinBD"].style.top = document.body.scrollTop + document.body.clientHeight - 20;
   MM_showHideLayers("coinBD","","show");
   document.all["coinHG"].style.left = 0 + document.body.scrollLeft;
   document.all["coinHG"].style.top = document.body.scrollTop;
   MM_showHideLayers("coinHG","","show");
   document.all["coinBG"].style.left = 0 + document.body.scrollLeft;
   document.all["coinBG"].style.top = document.body.scrollTop + document.body.clientHeight - 20;
   MM_showHideLayers("coinBG","","show");
}
// -->
</script>
</body>
</html>
