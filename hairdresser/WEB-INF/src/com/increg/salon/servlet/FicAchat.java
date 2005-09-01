/*
 * Fiche de création des achats (séries de mouvements d'approvisionnements)
 * Copyright (C) 2001-2005 Emmanuel Guyot <See emmguyot on SourceForge> 
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
package com.increg.salon.servlet;

import java.util.*;
import java.math.BigDecimal;
import java.sql.*;
import com.increg.salon.bean.*;
import javax.servlet.http.*;
import com.increg.commun.*;
/**
 * Fiche de création des achats
 * Creation date: 3 nov. 2002
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class FicAchat extends ConnectedServlet {
/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(
	javax.servlet.http.HttpServletRequest request,
	javax.servlet.http.HttpServletResponse response) {

	// Récupération des paramètres
	String Action = request.getParameter("Action");
    String CD_FOURN = request.getParameter("CD_FOURN");
    String CD_CMD_FOURN = request.getParameter("CD_CMD_FOURN");
    String CD_TYP_MVT = request.getParameter("CD_TYP_MVT");
    String paramSup1 = request.getParameter("paramSup1");
    String paramSup2 = request.getParameter("paramSup2");
    
    // Ligne en cours de saisie
    String CD_CATEG_ART = request.getParameter("CD_CATEG_ART");
    String CD_ART = request.getParameter("CD_ART");
    String QTE = request.getParameter("QTE");
    String VAL_MVT_HT = request.getParameter("VAL_MVT_HT");

    // Conversion des paramètres
    BigDecimal bdQTE = null;
    if ((QTE != null) && (QTE.length() > 0)) {
        bdQTE = new BigDecimal(QTE);
    }
    BigDecimal bdVAL_MVT_HT = null;
    if ((VAL_MVT_HT != null) && (VAL_MVT_HT.length() > 0)) {
        bdVAL_MVT_HT = new BigDecimal(VAL_MVT_HT);
    }
    
	// Récupère la connexion
	HttpSession mySession = request.getSession(false);
	SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
	DBSession myDBSession = mySalon.getMyDBSession();

    Vector lstLignes = new Vector(); // Mouvements de la commande
    Vector lstMvt = new Vector(); // Mouvement historique d'achat
    BigDecimal bdQTE_STK = null;
    BigDecimal bdVAL_STK_HT = null;
    Calendar DT_MVT = null;
    
	
	try {
        if (Action == null) {
            // Première phase de création
        }
		else if ((Action.equals ("Rechargement")) 
					|| (Action.equals ("Rechargement+"))
                    || (Action.equals ("RechargementFourn"))) {

			// Rechargement pour compléter la ligne
            if (Action.equals("Rechargement+")) {
                // Positionne la valeur par défaut du mouvement
                ArtBean aArt = ArtBean.getArtBean(myDBSession, CD_ART, mySalon.getMessagesBundle());
                if (assertOrError((aArt != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
                	return;
                }
                bdQTE = new BigDecimal("1");
                bdVAL_MVT_HT = aArt.getVAL_STK_HT();
                // et les valeurs de l'article
                bdQTE_STK = aArt.getQTE_STK();
                // TODO Utilisation du prix tarif
                bdVAL_STK_HT = aArt.getVAL_STK_HT();
                MvtStkBean aMvt = getPreviousMvt(myDBSession, CD_CMD_FOURN, CD_TYP_MVT, aArt.getCD_ART(), mySalon.getMessagesBundle());
                if (aMvt != null) {
                    DT_MVT = aMvt.getDT_MVT();
                }
                else {
                    DT_MVT = null;
                }
            }
            else if (Action.equals("RechargementFourn")) {
                // RAZ de la catégorie et de l'article
                CD_ART = null;
                CD_CATEG_ART = null;
            }
		}
        else if ((Action.equals ("AjoutLigne"))) {

            // Validation de la ligne
            try {
                
                // C'est une nouvelle ligne
                MvtStkBean aMvt = new MvtStkBean(myDBSession, CD_ART);
                aMvt.setCD_TYP_MVT(CD_TYP_MVT);
                aMvt.setQTE(QTE);
                aMvt.setVAL_MVT_HT(VAL_MVT_HT);
                aMvt.setCD_CMD_FOURN(CD_CMD_FOURN);
                aMvt.create(myDBSession);
                mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.enregistrementOk" + BasicSession.TAG_I18N);
            }
            catch (Exception e) {
                mySalon.setMessage("Erreur", e.toString());
            }
        }
        else if ((Action.equals ("SuppressionLigne"))) {

            // Suppression d'une ligne
            try {
                
                // C'est une nouvelle ligne
                MvtStkBean aMvt = MvtStkBean.getMvtStkBean(myDBSession, paramSup1, paramSup2, null, mySalon.getLangue(), mySalon.getMessagesBundle());
                if (assertOrError((aMvt != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
                	return;
                }
                aMvt.delete(myDBSession);
                mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.suppressionOk" + BasicSession.TAG_I18N);
            }
            catch (Exception e) {
                mySalon.setMessage("Erreur", e.toString());
            }
            CD_ART = null;
            CD_CATEG_ART = null;
        }
		else {
			System.out.println ("Action non codée : " + Action);
		}
	}
	catch (Exception e) {
		mySalon.setMessage("Erreur", e.toString());
		System.out.println("Note : " + e.toString());
	}

    /**
     * Reset de la transaction pour la recherche des informations complémentaires
     */
    myDBSession.cleanTransaction();

	/**
	 * Recherche les mouvements de cette commande
	 */
	if ((CD_CMD_FOURN != null) && (CD_CMD_FOURN.length() > 0)) {

        String reqSQL = "select * from MVT_STK where CD_CMD_FOURN=" + DBSession.quoteWith(CD_CMD_FOURN, '\'') + " order by DT_MVT";

        // Interroge la Base
        try {
            ResultSet aRS = myDBSession.doRequest(reqSQL);

            while (aRS.next()) {
                /**
                 * Création du bean de consultation
                 */
                MvtStkBean aMvt = new MvtStkBean(aRS, mySalon.getMessagesBundle());
                lstLignes.add(aMvt);
                
                /**
                 * Recherche les anciens mouvements d'achat
                 */
                if (CD_TYP_MVT != null) {
                    lstMvt.add(getPreviousMvt(myDBSession, CD_CMD_FOURN, CD_TYP_MVT, aMvt.getCD_ART(), mySalon.getMessagesBundle()));
                }
                else {
                    lstMvt.add(null);
                }
            }
            aRS.close();

        }
        catch (Exception e) {
            System.out.println("Erreur dans requète sur commande : " + e.toString());
            try {
                response.sendError(500);
            }
            catch (Exception e2) {
                System.out.println("Erreur sur sendError : " + e2.toString());
            }
        }
	}
    
    
	request.setAttribute("Liste", lstLignes);
    request.setAttribute("ListeMvt", lstMvt);

    request.setAttribute("QTE_STK", bdQTE_STK);
    request.setAttribute("VAL_STK_HT", bdVAL_STK_HT);
    request.setAttribute("DT_MVT", DT_MVT);
    request.setAttribute("QTE", bdQTE);
    request.setAttribute("VAL_MVT_HT", bdVAL_MVT_HT);
    request.setAttribute("CD_ART", CD_ART);
    request.setAttribute("CD_CATEG_ART", CD_CATEG_ART);

	try {
		// Passe la main à la fiche de création
		getServletConfig().getServletContext().getRequestDispatcher("/ficAchat.jsp").forward(request, response);

	}
	catch (Exception e) {
		System.out.println("FicAchat::performTask : Erreur à la redirection : " + e.toString());
	}
}

/**
 * Recherche le mouvement précédent pour ce type de mouvement
 * @param myDBSession Connexion à la base à utiliser
 * @param CD_CMD_FOURN N° de commande en cours de saisie pour exclusion
 * @param CD_TYP_MVT Type de mouvement concerné
 * @param CD_ART Article concerné
 * @param message Messages localisés
 * @return Mouvement chargé (éventuellement NULL)
 */
protected MvtStkBean getPreviousMvt (DBSession myDBSession, String CD_CMD_FOURN, String CD_TYP_MVT, long CD_ART, ResourceBundle message) {

    String reqSQLmvt = "select * from MVT_STK "
                + " where (CD_CMD_FOURN is null or CD_CMD_FOURN<>" + DBSession.quoteWith(CD_CMD_FOURN, '\'') + ")"
                + " and CD_TYP_MVT=" + CD_TYP_MVT + " and CD_ART=" + Long.toString(CD_ART) 
                + " order by DT_MVT desc limit 1";
                
    MvtStkBean anOldMvt = null;
    // Interroge la Base
    try {
        ResultSet aRSmvt = myDBSession.doRequest(reqSQLmvt);

        while (aRSmvt.next()) {
            /**
             * Création du bean de consultation
             */
            anOldMvt = new MvtStkBean(aRSmvt, message);
        }
        aRSmvt.close();

    }
    catch (Exception e) {
        System.out.println("Erreur dans requète sur historique achat : " + e.toString());
    }
    return anOldMvt;
}
}
