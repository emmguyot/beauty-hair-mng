<%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %>
<html>
<head>
<title>Untitled Document</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
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

<body bgcolor="#FFFFFF" text="#000000" class="action">
<span id="NOUVEAU" style="position:absolute; width:130px; height:31px; z-index:1; left: 15px; top: 11px; visibility: visible">
	<salon:bouton url="javascript:parent.mainFrame.Nouveau()" imgOn="images/nouveau2.gif" img="images/nouveau.gif" alt="Nouveau" />
</span>&nbsp;&nbsp; 
<span id="AIDE" style="position:absolute; width:130px; height:31px; z-index:1; top: 11px; left: 260px; visibility: visible">
	<salon:bouton url="javascript:parent.mainFrame.Aide()" imgOn="images/aide2.gif" img="images/aide.gif" alt="Aide" />
</span> 
<span id="IMPRIMER" style="position:absolute; left:145px; top:11px; width:146px; height:31px; z-index:1; visibility: visible" > 
   <salon:bouton url="javascript:Imprimer()" imgOn="images/imprimer2.gif" img="images/imprimer.gif" alt="Imprimer" />
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
