<%
/*
 * This program is part of InCrEG LibertyLook software http://beauty-hair-mng.sourceforge.net
 * Copyright (C) 2001-2008 Emmanuel Guyot <See emmguyot on SourceForge> 
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
<%@ page import="java.util.Vector" %>
<%@ page import="com.increg.salon.bean.SalonSession,
	       com.increg.salon.bean.PrestBean,
               com.increg.salon.bean.TypVentBean" %>
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
<title><i18n:message key="ficPrest.title" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="donnees" onLoad="Init();document.fiche.CD_TYP_VENT.focus()">
<%@ include file="include/commun.jsp" %>
<script language="JavaScript">
<!--
<%
   // Récupération des paramètres
   String Action = (String) request.getAttribute("Action");
   PrestBean aPrest = (PrestBean) request.getAttribute("PrestBean");
%>
   var Action="<%=Action%>";

function Init() {
   <%
   // Positionne les liens d'actions
   if (! Action.equals("Creation")) {
      %>
      MM_showHideLayers('SUPPRIMER?bottomFrame','','show');
      MM_showHideLayers('ENREGISTRER?bottomFrame','','show');
      MM_showHideLayers('DUPLIQUER?bottomFrame','','show');
      <%
   }
   if (Action.equals("Creation")) { %>
      // Pas de lien supprimer
      MM_showHideLayers('SUPPRIMER?bottomFrame','','hide');
      MM_showHideLayers('ENREGISTRER?bottomFrame','','show');
      MM_showHideLayers('DUPLIQUER?bottomFrame','','show');
   <%
   } %>
   MM_showHideLayers('RETOUR_LISTE?bottomFrame','','show');
}
//-->
</script>
<h1><img src="images/<%= mySalon.getLangue().getLanguage() %>/titres/ficPrest.gif" alt=<salon:TimeStamp bean="<%= aPrest %>" />></h1>
<salon:message salonSession="<%= mySalon %>" />
<form method="post" action="ficPrest.srv" name="fiche">
	<p> 
		<salon:valeur valeurNulle="0" valeur="<%= aPrest.getCD_PREST() %>" >
		  <input type="hidden" name="CD_PREST" value="%%" >
	        </salon:valeur>
		<input type="hidden" name="Action" value="<%=Action%>">
		<span class="obligatoire"><i18n:message key="label.typePrest" /> :</span> 
		<salon:DBselection valeur="<%= aPrest.getCD_TYP_VENT() %>" sql="select CD_TYP_VENT, LIB_TYP_VENT from TYP_VENT order by LIB_TYP_VENT">
		  <select name="CD_TYP_VENT" onChange="Recharge()">
		     %%
		  </select>
        </salon:DBselection>
		<% 
		  // Cherche la valeur en cours de selection
		  String CD_TYP_VENT = Integer.toString(aPrest.getCD_TYP_VENT());
		  CD_TYP_VENT = ((CD_TYP_VENT.length() == 0) || (CD_TYP_VENT.equals("0"))) ? 
					     (String) request.getAttribute("Premier") : 
					     CD_TYP_VENT;
	        %>
		<span id="CATEGORIE" style="position:absolute; visibility:visible">
		<span class="obligatoire"><i18n:message key="label.categorie" /> :</span> 
		<salon:DBselection valeur="<%= aPrest.getCD_CATEG_PREST() %>" sql="select CD_CATEG_PREST, LIB_CATEG_PREST from CATEG_PREST order by LIB_CATEG_PREST">
		  <select name="CD_CATEG_PREST">
		     <option value=""></option>
		     %%
		  </select>
		</salon:DBselection>
		</span>
		<span id="MARQUE" style="position:absolute; visibility:visible">
		<span class="obligatoire"><i18n:message key="label.marque" /> :</span> 
		<salon:DBselection valeur="<%= aPrest.getCD_MARQUE() %>" sql="select CD_MARQUE, LIB_MARQUE from MARQUE order by LIB_MARQUE">
		  <select name="CD_MARQUE">
		     <option value=""></option>
		     %%
		  </select>
		</salon:DBselection>
		</span>
	 </p>
	 <p>
		<span class="obligatoire"><i18n:message key="label.libelle" /> :</span> 
		<salon:valeur valeurNulle="null" valeur="<%= aPrest.getLIB_PREST() %>" > 
		<input type="text" name="LIB_PREST" value="%%" size="40">
	        </salon:valeur>
		<span class="obligatoire"><i18n:message key="label.prixUnitaireTTC" /> :</span> 
		<salon:valeur valeurNulle="null" valeur="<%= aPrest.getPRX_UNIT_TTC() %>" >
		  <input type="text" name="PRX_UNIT_TTC" value="%%" class="Nombre" size="6">
	        </salon:valeur>
		<span class="obligatoire"><%= mySalon.getDevise().toString() %></span>
                &nbsp;&nbsp;
		<span class="obligatoire"><i18n:message key="label.prestationPerime" /> :</span> 
                <i18n:message key="valeur.nonOui" id="paramNonOui" />
                <salon:selection valeur='<%= aPrest.getINDIC_PERIM() %>' valeurs='<%= "N|O" %>' libelle="<%= paramNonOui %>">
                    <select name="INDIC_PERIM">
                        %%
                    </select>
                </salon:selection>
	</p>
    <p><span class="facultatif"><i18n:message key="label.commentaire" /> :</span> 
	    <salon:valeur valeurNulle="null" valeur="<%= aPrest.getCOMM() %>" >
		<textarea name="COMM" cols="40" rows="2">%%</textarea>
	    </salon:valeur>
	</p>
	<span id="TPS" style="position:absolute; visibility:visible">
		<p>
			<span class="facultatif"><i18n:message key="label.tpsMoyen" /> :</span> 
			<salon:valeur valeurNulle="0" valeur="<%= aPrest.getTPS_PREST() %>" > 
			  <input type="text" name="TPS_PREST" value="%%" size="5">
	        </salon:valeur>
	     </p>
	     <p>
	        <span class="important"><i18n:message key="label.abonnement" /></span> :
	     </p>
	     <blockquote>
			<p>
				<span class="obligatoire"><i18n:message key="label.eltAbonnement" /> :</span> 
                <salon:selection valeur='<%= aPrest.getINDIC_ABONNEMENT() %>' valeurs='<%= "N|O" %>' libelle="<%= paramNonOui %>">
                    <select name="INDIC_ABONNEMENT" onchange="switchAbonnement()">
                        %%
                    </select>
                </salon:selection>
			</p>
		    <p><span id="ABON" style="position:relative; visibility:visible">
				<span class="facultatif"><i18n:message key="label.prestationElt" /> :</span> 
				<salon:DBselection valeur="<%= aPrest.getCD_PREST_ABONNEMENT() %>" sql='<%= "select CD_PREST, LIB_PREST from PREST where INDIC_ABONNEMENT = \'O\' and CD_TYP_VENT=" + CD_TYP_VENT + " order by LIB_PREST" %>'>
				  <select name="CD_PREST_ABONNEMENT">
				     <option value=""></option>
				     %%
				  </select>
				</salon:DBselection>
	          	<%
	            	if (aPrest.getCD_PREST_ABONNEMENT() > 0) { 
	         	%>
	            	    <a href="_FichePrest.jsp?Action=Modification&CD_PREST=<%= aPrest.getCD_PREST_ABONNEMENT() %>" target="ClientFrame">...</a> 
	          	<%
	            	}
	          	%>
				<span class="facultatif"><i18n:message key="label.nbPrestation" /> :</span> 
				<salon:valeur valeurNulle="0" valeur="<%= aPrest.getCPT_ABONNEMENT() %>" > 
				  <input type="text" name="CPT_ABONNEMENT" value="%%" size="4">
		        </salon:valeur>
			</span></p>
	     </blockquote>
		</p>
	</span>
	<span id="ARTICLE" style="position:absolute; visibility:visible"><p>
		<span class="obligatoire"><i18n:message key="label.articleStock" /> :</span>
		<salon:DBselection valeur="<%= aPrest.getCD_ART() %>" sql="select CD_ART, LIB_ART from ART where CD_TYP_ART=1 order by LIB_ART">
		  <select name="CD_ART">
		     <option value=""></option>
		     %%
		  </select>
                  <%
                    if (aPrest.getCD_ART() > 0) { 
                  %>
                        <a href="_FicheArt.jsp?Action=Modification&CD_ART=<%= aPrest.getCD_ART() %>" target="ClientFrame">...</a> 
                  <%
                    }
                  %>
		</salon:DBselection>
	</p></span>

<script language="JavaScript">
// Fonctions d'action
var Partie=0;

// Maj des champs visibles au chargement
chgTypVent();

function chgTypVent()
{
    isVent = false;
    <%
    Vector liste = TypVentBean.getLstTypVentMarque(mySalon.getMyDBSession());

    for (int i=0; i < liste.size(); i++) {
        TypVentBean aTypVent = (TypVentBean) liste.get(i);

        %>
        for (n = 0; n < document.fiche.CD_TYP_VENT.options.length; n++) {
            if ((document.fiche.CD_TYP_VENT.options[n].selected) 
                && (document.fiche.CD_TYP_VENT.options[n].value == "<%= aTypVent.getCD_TYP_VENT() %>")) {
                isVent = true;
            }
        } <%
    } // for
    %>
    if (isVent) {
      MM_showHideLayers('MARQUE','','show');
      MM_showHideLayers('ARTICLE','','show');
      MM_showHideLayers('TPS','','hide');
      MM_showHideLayers('ABON','','hide');
      document.fiche.TPS_PREST.value = "";
      MM_showHideLayers('CATEGORIE','','hide');
      document.fiche.CD_CATEG_PREST.options[0].selected = true;
      Partie = 1;
    }
    else {
      MM_showHideLayers('MARQUE','','hide');
      document.fiche.CD_MARQUE.options[0].selected = true;
      MM_showHideLayers('ARTICLE','','hide');
      document.fiche.CD_ART.options[0].selected = true;
      MM_showHideLayers('TPS','','show');
      MM_showHideLayers('ABON','','show');
      MM_showHideLayers('CATEGORIE','','show');
      Partie = 2;
	  switchAbonnement();
    }
}

function switchAbonnement()
{
	if (document.fiche.INDIC_ABONNEMENT.options[1].selected) {
      	MM_showHideLayers('ABON','','hide');
		document.fiche.CPT_ABONNEMENT.value='';
	}
	else {
      	MM_showHideLayers('ABON','','show');
	}
}
// Recharge l'écran pour mettre à jour les abonnements
function Recharge()
{
  	document.fiche.Action.value = "Rechargement";
   	document.fiche.submit();
}

// Enregistrement des données de la prestation
function Enregistrer()
{
   // Verification des données obligatoires
   if ((document.fiche.LIB_PREST.value == "") 
	 || ((Partie == 1) && (document.fiche.CD_MARQUE.options[0].selected))
	 || ((Partie == 1) && (document.fiche.CD_ART.options[0].selected))
	 || ((Partie == 2) && (document.fiche.CD_CATEG_PREST.options[0].selected))) {
      alert ("<i18n:message key="ficPrest.donneesManquante" />");
      return;
   }
   document.fiche.submit();
}

// Suppression de la prestation
function Supprimer()
{
    if ((document.fiche.CD_PREST.value != "0") && (document.fiche.CD_PREST.value != "")) {
        if (confirm ("Cette suppression est définitive. Confirmez-vous cette action ?")) {
            document.fiche.Action.value = "Suppression";
            document.fiche.submit();
        }
    }
}

// Duplication de la prestation
function Dupliquer()
{
   if ((document.fiche.LIB_PREST.value == "") 
	 || ((Partie == 1) && (document.fiche.CD_MARQUE.options[0].selected))
	 || ((Partie == 1) && (document.fiche.CD_ART.options[0].selected))
	 || ((Partie == 2) && (document.fiche.CD_CATEG_PREST.options[0].selected))) {
      alert ("<i18n:message key="ficPrest.donneesManquante" />");
      return;
   }
   document.fiche.Action.value = "Duplication";
   document.fiche.submit();
}

function RetourListe()
{
   if (document.fiche.LIB_PREST.value != "") {
      parent.location.href = "ListePrest.jsp?premLettre=" + document.fiche.LIB_PREST.value.charAt(0).toUpperCase();
   }
   else {
      parent.location.href = "ListePrest.jsp?premLettre=A";
   }
}

// Affichage de l'aide
function Aide()
{
    window.open("<%= mySalon.getLangue().getLanguage() %>/aideFiche.html");
}

</script>
</form>
</body>
</html>
