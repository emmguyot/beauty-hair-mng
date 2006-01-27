<%
/*
 * This program is part of InCrEG LibertyLook software http://beauty-hair-mng.sourceforge.net
 * Copyright (C) 2001-2006 Emmanuel Guyot <See emmguyot on SourceForge> 
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
<%@ page import="com.increg.salon.bean.SalonSession,com.increg.salon.bean.IdentBean,
   java.util.Vector,com.increg.salon.bean.FactBean,com.increg.salon.bean.ClientBean" %>
<%
    SalonSession mySalon = (SalonSession) session.getAttribute("SalonSession");
    if (mySalon == null) {
        getServletConfig().getServletContext().getRequestDispatcher("/reconnect.html").forward(request, response);
    }
%>
<%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %>
<%@ taglib uri="WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<i18n:bundle baseName="messages" locale="<%= mySalon.getLangue() %>"/>
<html>
<head>
<title><%=mySalon.getMySociete().getRAIS_SOC()%></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
<script src="include/core.jsp" language="JavaScript"></script>
<script language="JavaScript">
<!--
function MM_reloadPage(init) {  //reloads the window if Nav4 resized
  if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
    document.MM_pgW=innerWidth; document.MM_pgH=innerHeight; onresize=MM_reloadPage; }}
  else if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH) location.reload();
}
MM_reloadPage(true);

<%    String sub = request.getParameter("sub");
      IdentBean aIdent = mySalon.getMyIdent(); %>

// -->
</script>
</head>

<body class="menu" onLoad="clock()">
<p align="center"><a href="main.srv" target="_top"><font color="#000000"><%=mySalon.getMySociete().getRAIS_SOC()%><br>
	<img src="images/perso/Logo_petit.gif" width="100" height="52" border="0"></font></a></p>
<p><salon:autorisation entite="RDV"><a href="ListeRDV.jsp" target="ClientFrame"><img src="images/rdv.gif" align=middle border=0 width="21" height="21"><i18n:message key="label.RDV" /></a><br></salon:autorisation>
        <salon:autorisation entite="Clients"><a href="ListeCli.jsp" target="ClientFrame"><img src="images/client.gif" align=middle border=0 width="21" height="21"><i18n:message key="label.clients" /></a><br></salon:autorisation>
	<salon:autorisation entite="Prestations"><a href="ListePrest.jsp" target="ClientFrame"><img src="images/prest.gif" align=middle border=0 width="21" height="21"><i18n:message key="label.prestations" /></a><br></salon:autorisation>
<salon:autorisation entite="Stock">
<% if ((sub != null) && (sub.equals("stock"))) { %>
	<a href="javascript:subMenu('')"><img src="images/stock.gif" align=middle border=0 width="21" height="21"><i18n:message key="label.stock" /></a><br>
	<hr><font size=-1>
	<a href="ListeArt.jsp" target="ClientFrame"><i18n:message key="label.article" /></a><br>
	<a href="_FicheAchat.jsp" target="ClientFrame"><i18n:message key="label.achats" /></a><br>
        <a href="ListeMvtStk.jsp" target="ClientFrame"><i18n:message key="label.mouvements" /></a><br>
	<a href="ListeRecap.jsp" target="ClientFrame"><i18n:message key="label.recap" /></a><br>
	<a href="_FicheInventaire.jsp" target="ClientFrame"><i18n:message key="label.inventaire" /></a><br>
	<a href="ListeFourn.jsp" target="ClientFrame"><i18n:message key="label.fournisseurs" /></a><br>
	</font><hr>
<% } else { %> 
	<a href="javascript:subMenu('stock')"><img src="images/stock.gif" align=middle border=0 width="21" height="21"><i18n:message key="label.stock" /></a><br>
<% } %>
</salon:autorisation>

<salon:autorisation entite="Comptabilité">
<% if ((sub != null) && (sub.equals("compta"))) { %>
	<a href="javascript:subMenu('')"><img src="images/compta.gif" align=middle border=0 width="21" height="21"><i18n:message key="label.comptabilite" /></a><br>
	<hr><font size=-1>
	<a href="ListeMvtCaisse.jsp" target="ClientFrame"><i18n:message key="label.mvtCaisse" /></a><br>
	<a href="ListeJournal.jsp" target="ClientFrame"><i18n:message key="label.journal" /></a><br>
	<a href="ListeBrouillard.jsp" target="ClientFrame"><i18n:message key="label.brouillard" /></a><br>
	<a href="_FicheReFact.jsp" target="ClientFrame"><i18n:message key="label.reeditionFacture" /></a><br>
	<a href="ListeVente.jsp" target="ClientFrame"><i18n:message key="label.recapVentes" /></a><br>
	<a href="ListeTVA.jsp" target="ClientFrame"><i18n:message key="label.TVA" /></a><br>
	<a href="ListeCA.jsp" target="ClientFrame"><i18n:message key="label.ca" /></a><br>
	<a href="ListeFinJournee.jsp" target="ClientFrame"><i18n:message key="label.finJournee" /></a><br>
	</font><hr>
<% } else { %> 
	<a href="javascript:subMenu('compta')"><img src="images/compta.gif" align=middle border=0 width="21" height="21"><i18n:message key="label.comptabilite" /></a><br>
<% } %>
</salon:autorisation>

<salon:autorisation entite="Personnel">
<% if ((sub != null) && (sub.equals("personnel"))) { %>
	<a href="javascript:subMenu('')"><img src="images/collab.gif" align=middle border=0 width="21" height="21"><i18n:message key="label.personnel" /></a><br>
	<hr><font size=-1>
	<a href="ListeCollab.jsp" target="ClientFrame"><i18n:message key="label.collaborateurs" /></a><br>
	<a href="ListePointage.jsp" target="ClientFrame"><i18n:message key="label.pointage" /></a><br>
	<a href="ListePresence.jsp" target="ClientFrame"><i18n:message key="label.presences" /></a><br>
	</font><hr>
<% } else { %> 
	<a href="javascript:subMenu('personnel')"><img src="images/collab.gif" align=middle border=0 width="21" height="21"><i18n:message key="label.personnel" /></a><br>
<% } %>
</salon:autorisation>

<salon:autorisation entite="Statistiques">
   <a href="ListeStat.jsp" target="ClientFrame"><img src="images/stat.gif" align=middle border=0 width="21" height="21"><i18n:message key="label.stats" /></a><br>
</salon:autorisation>
<salon:autorisation entite="Publipostages">
   <a href="ListeCriterePub.jsp" target="ClientFrame"><img src="images/pub.gif" align=middle border=0 width="21" height="21"><i18n:message key="label.publipostage" /></a>
</salon:autorisation>
</p>
<salon:autorisation entite="Administration">
<% if ((sub != null) && (sub.equals("admin"))) { %>
    <p><a href="javascript:subMenu('')"><img src="images/admin.gif" align=middle border=0 width="21" height="21"><i18n:message key="label.admin"/></a>
    <hr>
    <nobr><font size=-1>
    <salon:autorisation entite="Sauvegarde">
        <a href="_FicheSauv.jsp" target="ClientFrame"><i18n:message key="label.sauvegarde" /></a><br>
    </salon:autorisation>
    <salon:autorisation entite="Restauration">
        <a href="_FicheRest.jsp" target="ClientFrame"><i18n:message key="label.restauration" /></a><br>
    </salon:autorisation>
    <salon:autorisation entite="MiseAJour">
        <a href="_FicheMaj.jsp" target="ClientFrame"><i18n:message key="label.miseAJour" /></a><br>
    </salon:autorisation>
    <salon:autorisation entite="GestionSauvegardes">
        <a href="_FicheGestSauv.jsp" target="ClientFrame"><i18n:message key="label.gestionSauvegarde" /></a><br>
        <a href="_FichePurge.jsp" target="ClientFrame"><i18n:message key="label.purge" /></a>
    </salon:autorisation>
    <salon:autorisation entite="Parametrage"></p><p>
        <a href="ListeDonneeRef.jsp?nomTable=CATEG_ART&chaineTable=Cat%e9gories%20d'articles" target="ClientFrame"><i18n:message key="label.categArticle" /></a><br/>
        <a href="ListeDonneeRef.jsp?nomTable=CATEG_CLI&chaineTable=Cat%e9gories%20de%20clients" target="ClientFrame"><i18n:message key="label.categClient" /></a><br/>
        <a href="ListeDonneeRef.jsp?nomTable=CATEG_PREST&chaineTable=Cat%e9gories%20des%20prestations" target="ClientFrame"><i18n:message key="label.categPrest" /></a><br/>
        <a href="ListeDevise.jsp" target="ClientFrame"><i18n:message key="label.devises" /></a><br/>
        <a href="ListeFete.jsp" target="ClientFrame"><i18n:message key="label.fetes" /></a><br/>
        <a href="ListeDonneeRef.jsp?nomTable=FCT&chaineTable=Fonctions" target="ClientFrame"><i18n:message key="label.fonctions" /></a><br/>
        <a href="ListeDonneeRef.jsp?nomTable=MARQUE&chaineTable=Marques" target="ClientFrame"><i18n:message key="label.marques" /></a><br/>
        <a href="ListeModRegl.jsp" target="ClientFrame"><i18n:message key="label.modeRegls" /></a><br/>
        <a href="ListeDonneeRef.jsp?nomTable=ORIG&chaineTable=Origines%20de%20clients" target="ClientFrame"><i18n:message key="label.origines" /></a><br/>
        <a href="ListeDonneeRef.jsp?nomTable=PROFIL&chaineTable=Profils%20utilisateurs" target="ClientFrame"><i18n:message key="label.profils" /></a><br/>
        <a href="ListeTxTVA.jsp" target="ClientFrame"><i18n:message key="label.txTVAs" /></a><br/>
        <a href="ListeTrAge.jsp" target="ClientFrame"><i18n:message key="label.tranchesAges" /></a><br/>
        <a href="ListeDonneeRef.jsp?nomTable=TYP_ART&chaineTable=Types%20d'articles" target="ClientFrame"><i18n:message key="label.typesArticles" /></a><br/>
    	<% if (mySalon.getMySociete().isSalon()) { %>
        <a href="ListeDonneeRef.jsp?nomTable=TYP_CHEV&chaineTable=Types%20de%20cheveux" target="ClientFrame"><i18n:message key="label.typesCheveux" /></a><br/>
    	<% } %>
        <a href="ListeDonneeRef.jsp?nomTable=TYP_CONTR&chaineTable=Types%20de%20contrats" target="ClientFrame"><i18n:message key="label.typesContrats" /></a><br/>
        <a href="ListeTypMca.jsp" target="ClientFrame" title="Types de mouvements caisse"><i18n:message key="label.typesMouvementsCaisse" /></a><br/>
        <a href="ListeTypMvt.jsp" target="ClientFrame" title="Types de mouvements stock"><i18n:message key="label.typesMouvementsStock" /></a><br/>
    	<% if (mySalon.getMySociete().isInstitut()) { %>
        <a href="ListeDonneeRef.jsp?nomTable=TYP_PEAU&chaineTable=Types%20de%20peau" target="ClientFrame"><i18n:message key="label.typesPeau" /></a><br/>
    	<% } %>
        <a href="ListeDonneeRef.jsp?nomTable=TYP_POINTAGE&chaineTable=Types%20de%20pointages" target="ClientFrame"><i18n:message key="label.typesPointage" /></a><br/>
        <a href="ListeTypVent.jsp" target="ClientFrame"><i18n:message key="label.typesPrest" /></a><br/>
        </p><p>
    <!--      <a href="ListeDonneeRef.jsp?nomTable=UNIT_MES&chaineTable=Unit%e9s%20de%20mesures" target="ClientFrame"><i18n:message key="label.unitesMesure" /></a><br> -->
        <a href="_FicheSoc.jsp" target="ClientFrame"><i18n:message key="label.infoSociete" /></a><br>
        <a href="ListeParam.jsp" target="ClientFrame"><i18n:message key="label.parametrage" /></a><br>
        <a href="ListeIdent.jsp" target="ClientFrame"><i18n:message key="label.identifications" /></a>
    </salon:autorisation>
      <hr></p>
      </font></nobr>
<% } else { %> 
   <p><a href="javascript:subMenu('admin')"><img src="images/admin.gif" align=middle border=0 width="21" height="21"><i18n:message key="label.admin" /></a></p>
<% } %>
</salon:autorisation>

<table cellspacing=0 border=0 width=100%>
   <tr><td><img src="images/perso/encours.gif" width="127" height="21"></td></tr>
<%
   // Affiche les clients en cours
   Vector listeFact = mySalon.getListeFact();

   for (int i=0; i < listeFact.size(); i++) {
      FactBean aFact = (FactBean) listeFact.get(i);
      ClientBean aCli = ClientBean.getClientBean(mySalon.getMyDBSession(), Long.toString(aFact.getCD_CLI()), mySalon.getMessagesBundle());
      String chaine = aCli.toString();
      %>
	<tr><td class="ligneTab4"><nobr><font size=-1><a href="_FicheFact.jsp?Action=Modification&CD_FACT=<%= aFact.getCD_FACT() %>" target="ClientFrame" title="<%= chaine %>"><%= chaine.substring(0,Math.min(20,chaine.length())) %></a></font></nobr></td></tr>
      <%
   }

%>
</table>

<div id="coinHD" style="position:absolute; height=20px; z-index:1; visibility:hidden"><img src="images/perso/coin_hd.gif"></div>
<div id="coinBD" style="position:absolute; height=20px; z-index:1; visibility:hidden"><img src="images/perso/coin_bd.gif"></div>
<div id="coinHG" style="position:absolute; height=20px; z-index:1; visibility:hidden"><img src="images/perso/coin_hg.gif"></div>
<div id="coinBG" style="position:absolute; height=20px; z-index:1; visibility:hidden"><img src="images/perso/coin_bg.gif"></div>

<p style="text-align: center">
<img src="images/horloge.gif" style="float: left; vertical-align: middle;margin-top: 15px;margin-bottom: 15px;"><span id="pendule" style="text-align: center"></span>
</p>
<script language="JavaScript" src="include/<%= mySalon.getLangue().getLanguage() %>/dateHome.js"> </script>

<p class="tabDonnees">
<font size=-1>
<a href="<%= mySalon.getLangue().getLanguage() %>/histo.html" target="ClientFrame"><%@ include file="include/version.inc" %></a><br/>
<a href="<%= mySalon.getLangue().getLanguage() %>/contact.html" target="ClientFrame">&copy; 2002-2006</a></font></p>

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

function subMenu(url) {
    if (top.ClientFrame.location.href.indexOf("accueilPointage.srv") == -1) {
        top.location.href = "main.srv?sub=" + url;
    }
    else {
        top.MenuFrame.location.href = "main.srv?menu=1&sub=" + url;
    }
}
// -->
</script>
</body>
</html>
