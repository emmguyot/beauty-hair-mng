package com.increg.salon.servlet;

import com.increg.commun.*;
import java.util.*;
import java.sql.*;
import com.increg.salon.bean.*;

import javax.servlet.http.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * Recherche/Liste d'articles pour inventaire
 * Creation date: 21 août 02
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class FicInventaire extends ConnectedServlet {

    /**
     * Classe interne représentant un item d'inventaire avec les données saisies
     */    
    private class ItemInventaire {
        /**
         * Code de l'article inventorié
         */
        private String CD_ART = "";
        /**
         * Quantité en stock
         */
        private String QTE_STK = "";
        /**
         * Valeur unitaire du stock
         */
        private String VAL_STK_HT = "";
        /**
         * Returns the cD_ART.
         * @return String
         */
        public String getCD_ART() {
            return CD_ART;
        }

        /**
         * Returns the qTE_STK.
         * @return String
         */
        public String getQTE_STK() {
            return QTE_STK;
        }

        /**
         * Returns the vAL_STK_HT.
         * @return String
         */
        public String getVAL_STK_HT() {
            return VAL_STK_HT;
        }

        /**
         * Sets the cD_ART.
         * @param cD_ART The cD_ART to set
         */
        public void setCD_ART(String cD_ART) {
            CD_ART = cD_ART;
        }

        /**
         * Sets the qTE_STK.
         * @param qTE_STK The qTE_STK to set
         */
        public void setQTE_STK(String qTE_STK) {
            QTE_STK = qTE_STK;
        }

        /**
         * Sets the vAL_STK_HT.
         * @param vAL_STK_HT The vAL_STK_HT to set
         */
        public void setVAL_STK_HT(String vAL_STK_HT) {
            VAL_STK_HT = vAL_STK_HT;
        }

    }
    
/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) {

	Log log = LogFactory.getLog(this.getClass());

	// Récupération des paramètres
    String Action = request.getParameter("Action");
	String CD_TYP_ART = request.getParameter("CD_TYP_ART");
	String CD_CATEG_ART = request.getParameter("CD_CATEG_ART");
    String CD_TYP_MVT = request.getParameter("CD_TYP_MVT");

    // Récupère la connexion
    HttpSession mySession = request.getSession(false);
    SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
    DBSession myDBSession = mySalon.getMyDBSession();
    
    // Données saisies
    HashMap listeInventaire = new HashMap();
    Enumeration invEnum = request.getParameterNames();
    while (invEnum.hasMoreElements()) {
        String aName = (String) invEnum.nextElement();
        String value = request.getParameter(aName);
        if ((value != null) && (value.length() > 0)) {
            if (aName.indexOf("QTE_STK") != -1) {
                ItemInventaire aItem = (ItemInventaire) listeInventaire.get(aName.substring(7));
                if (aItem == null) {
                    aItem = new ItemInventaire();
                    aItem.setCD_ART(aName.substring(7));
                }
                aItem.setQTE_STK(value);
                listeInventaire.put(aName.substring(7), aItem);
            }
            if (aName.indexOf("VAL_STK_HT") != -1) {
                ItemInventaire aItem = (ItemInventaire) listeInventaire.get(aName.substring(10));
                if (aItem == null) {
                    aItem = new ItemInventaire();
                    aItem.setCD_ART(aName.substring(10));
                }
                aItem.setVAL_STK_HT(value);
                listeInventaire.put(aName.substring(10), aItem);
            }
        }
    }

    if ((Action != null) && (Action.equals("Creation"))) {
        // Création des mouvements d'inventaires
        try {  // Gestion des exceptions globalement pour gérer une unité logique

            myDBSession.setDansTransactions(true);
            Set articles = listeInventaire.keySet();
            for (Iterator i = articles.iterator(); i.hasNext();) {
                String CD_ART = (String) i.next();
                MvtStkBean aMvt = new MvtStkBean(myDBSession, CD_ART);
                ItemInventaire aItem = (ItemInventaire) listeInventaire.get(CD_ART);
                if ((aItem.getQTE_STK() != null) && (aItem.getQTE_STK().length() > 0)
                    && (aItem.getVAL_STK_HT() != null) && (aItem.getVAL_STK_HT().length() > 0)) {
                    aMvt.setCD_TYP_MVT(CD_TYP_MVT);
                    aMvt.setCOMM("Inventaire global");
                    aMvt.setQTE(aItem.getQTE_STK());
                    aMvt.setVAL_MVT_HT(aItem.getVAL_STK_HT());
                    aMvt.create(myDBSession);
                }
            }
            myDBSession.endTransaction();
            mySalon.setMessage("Info", BasicSession.TAG_I18N + "ficInventaire.creationOk" + BasicSession.TAG_I18N);
            request.setAttribute("RAZ", "ok");
        }
        catch (Exception e) {
            myDBSession.cleanTransaction();
            mySalon.setMessage("Erreur", e.toString());
        }
    }

    /**
     * Reset de la transaction pour la recherche des informations complémentaires
     */
    myDBSession.cleanTransaction();

	/**
     * Constitue la requete SQL
     * Pour obtenir la liste des articles
     */
	String reqSQL = "select * from ART ";
	reqSQL = reqSQL + " where INDIC_PERIM='N'";
	if ((CD_TYP_ART != null) && (CD_TYP_ART.length() > 0)) {
		reqSQL = reqSQL + " and CD_TYP_ART=" + CD_TYP_ART;
	}
	if ((CD_CATEG_ART != null) && (CD_CATEG_ART.length() > 0)) {
		reqSQL = reqSQL + " and CD_CATEG_ART=" + CD_CATEG_ART;
	}
	
	reqSQL = reqSQL + " order by LIB_ART, CD_TYP_ART, CD_CATEG_ART";

	// Interroge la Base
	try {
		ResultSet aRS = myDBSession.doRequest(reqSQL);
		Vector lstLignes = new Vector();
		Vector lstMvt = new Vector();

		while (aRS.next()) {
			ArtBean aArt = new ArtBean(aRS, mySalon.getMessagesBundle());
			lstLignes.add(aArt);
			// Dernier mouvement
			String reqSQL2 = "select * from MVT_STK, TYP_MVT where MVT_STK.CD_TYP_MVT=TYP_MVT.CD_TYP_MVT and SENS_MVT='" + TypMvtBean.SENS_INVENTAIRE + "' and CD_ART=" + aArt.getCD_ART() + " order by DT_MVT desc limit 1";
			ResultSet aRS2 = myDBSession.doRequest(reqSQL2);

			if (aRS2.next()) {
				lstMvt.add(new MvtStkBean(aRS2, mySalon.getMessagesBundle()));
			}
			else {
				lstMvt.add(new MvtStkBean(mySalon.getMessagesBundle()));
			}
		}
		aRS.close();

		// Stocke le Vector pour le JSP
		request.setAttribute("Liste", lstLignes);
		request.setAttribute("ListeMvt", lstMvt);
		
		// Passe la main
		getServletConfig().getServletContext().getRequestDispatcher("/ficInventaire.jsp").forward(request, response);

	}
	catch (Exception e) {
		log.error("Erreur dans performTask : ", e);
		try {
			response.sendError(500);
		}
		catch (Exception e2) {
			log.error("Erreur sur sendError : ", e2);
		}
	}
 } 
}
