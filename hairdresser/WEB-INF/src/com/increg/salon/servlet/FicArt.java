/*
 * Fiche de création / modification d'un article
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
package com.increg.salon.servlet;

import java.util.*;
import java.sql.*;
import com.increg.salon.bean.*;
import javax.servlet.http.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.increg.commun.*;
/**
 * Création d'une prestation
 * Creation date: (20/07/2001 20:12:04)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class FicArt extends ConnectedServlet {
	
	protected Log log = LogFactory.getLog(this.getClass());
	
/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(
	javax.servlet.http.HttpServletRequest request,
	javax.servlet.http.HttpServletResponse response) {

	// Récupération des paramètres
	String Action = request.getParameter("Action");
	String CD_ART = request.getParameter("CD_ART");
	String CD_CATEG_ART = request.getParameter("CD_CATEG_ART");
	String CD_TYP_ART = request.getParameter("CD_TYP_ART");
	String CD_UNIT_MES = request.getParameter("CD_UNIT_MES");
	String COMM = request.getParameter("COMM");
	String LIB_ART = request.getParameter("LIB_ART");
	String QTE_STK = request.getParameter("QTE_STK");
	String QTE_STK_MIN = request.getParameter("QTE_STK_MIN");
	String REF_ART = request.getParameter("REF_ART");
	String VAL_STK_HT = request.getParameter("VAL_STK_HT");
    String INDIC_PERIM = request.getParameter("INDIC_PERIM");

	String nbFournParam = request.getParameter("NbFourn");
	String ParamSup = request.getParameter("ParamSup");

	int nbFourn = 0;
	if (nbFournParam != null) {
		nbFourn = Integer.parseInt(nbFournParam);
	}
	
	// Données de ligne
	String[] tab_CD_FOURN = new String[nbFourn + 1];
	String[] tab_LIB_ART = new String[nbFourn + 1];
	String[] tab_REF_ART = new String[nbFourn + 1];
	String[] tab_QTE_CMD_MIN = new String[nbFourn + 1];
	String[] tab_PRX_UNIT_HT = new String[nbFourn + 1];
	String[] tab_FOURN_PRINC = new String[nbFourn + 1];

	{ // Déclaration local de i pour éviter les effets de bord
		int i = 1;
		// Boucle de chargement
		for (i = 0; i < nbFourn; i++) {
			tab_CD_FOURN[i] = request.getParameter("CD_FOURN" + Integer.toString(i));
			tab_LIB_ART[i] = request.getParameter("LIB_ART" + Integer.toString(i));
			tab_REF_ART[i] = request.getParameter("REF_ART" + Integer.toString(i));
			tab_QTE_CMD_MIN[i] = request.getParameter("QTE_CMD_MIN" + Integer.toString(i));
			tab_PRX_UNIT_HT[i] = request.getParameter("PRX_UNIT_HT" + Integer.toString(i));
			tab_FOURN_PRINC[i] = request.getParameter("FOURN_PRINC" + Integer.toString(i));
		}
		// Ligne en cours de saisie
		tab_CD_FOURN[i] = request.getParameter("CD_FOURN" + Integer.toString(i));
		tab_LIB_ART[i] = request.getParameter("LIB_ART" + Integer.toString(i));
		tab_REF_ART[i] = request.getParameter("REF_ART" + Integer.toString(i));
		tab_QTE_CMD_MIN[i] = request.getParameter("QTE_CMD_MIN" + Integer.toString(i));
		tab_PRX_UNIT_HT[i] = request.getParameter("PRX_UNIT_HT" + Integer.toString(i));
		tab_FOURN_PRINC[i] = request.getParameter("FOURN_PRINC" + Integer.toString(i));
	}

	// Récupère la connexion
	HttpSession mySession = request.getSession(false);
	SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
	DBSession myDBSession = mySalon.getMyDBSession();

	ArtBean aArt = null;
	
	try {
		if (Action == null) {
			// Première phase de création
			request.setAttribute("Action", "Creation");
			// Un bean vide
			aArt = new ArtBean(mySalon.getMessagesBundle());
		}
		else if (Action.equals("Creation")) {
			// Crée réellement l'article

			/**
			 * Création du bean et enregistrement
			 */
			aArt = new ArtBean(mySalon.getMessagesBundle());
			aArt.setCD_ART(CD_ART);
			aArt.setCD_CATEG_ART(CD_CATEG_ART);
			aArt.setCD_TYP_ART(CD_TYP_ART);
			aArt.setCD_UNIT_MES(CD_UNIT_MES);
			aArt.setCOMM(COMM);
			aArt.setLIB_ART(LIB_ART);
			aArt.setQTE_STK(QTE_STK);
			aArt.setQTE_STK_MIN(QTE_STK_MIN);
			aArt.setREF_ART(REF_ART);
			aArt.setVAL_STK_HT(VAL_STK_HT);
            aArt.setINDIC_PERIM(INDIC_PERIM);

			try {
	            aArt.create(myDBSession);

	            mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.creationOk" + BasicSession.TAG_I18N);
				if (CD_TYP_ART.equals("1")) {
		            // Création automatique de la prestation
		            aArt.creationPrestation(myDBSession);
		            mySalon.setMessage("Info", BasicSession.TAG_I18N + "ficArt.creationOk" + BasicSession.TAG_I18N);
				}
	            request.setAttribute("Action", "Modification");
			}
			catch (Exception e) {
	            mySalon.setMessage("Erreur", e.toString());
	            log.error("Erreur à la création de l'article", e);
				request.setAttribute("Action", Action);
			}
		}
		else if ((Action.equals("Modification")) && (LIB_ART == null)) {
			// Affichage de la fiche en modification
			request.setAttribute("Action", "Modification");

			aArt = ArtBean.getArtBean(myDBSession, CD_ART, mySalon.getMessagesBundle());
            if (assertOrError((aArt != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
            	return;
            }
		}
		else if ((Action.equals ("Modification")) 
				|| (Action.equals ("AjoutLigne"))
				|| (Action.equals ("SuppressionLigne"))) {
			// Modification effective de la fiche

			int paramSup = -1;
			if ((Action.equals ("AjoutLigne"))
					|| (Action.equals ("SuppressionLigne"))) {
				paramSup = Integer.parseInt(ParamSup);
			}

			/**
			 * Création du bean et enregistrement
			 */
            if ((CD_ART == null) || (CD_ART.length() == 0) || (CD_ART.equals("0"))) {
				// On est en création : le Bean est créé de zero
				aArt = new ArtBean(mySalon.getMessagesBundle());
			}
			else {
				// Recharge à partir de la base
	            aArt = ArtBean.getArtBean(myDBSession, CD_ART, mySalon.getMessagesBundle());
                if (assertOrError((aArt != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
                	return;
                }
			}

			aArt.setCD_ART(CD_ART);
			aArt.setCD_CATEG_ART(CD_CATEG_ART);
			aArt.setCD_TYP_ART(CD_TYP_ART);
			aArt.setCD_UNIT_MES(CD_UNIT_MES);
			aArt.setCOMM(COMM);
			aArt.setLIB_ART(LIB_ART);
			aArt.setQTE_STK(QTE_STK);
			aArt.setQTE_STK_MIN(QTE_STK_MIN);
			aArt.setREF_ART(REF_ART);
			aArt.setVAL_STK_HT(VAL_STK_HT);
            aArt.setINDIC_PERIM(INDIC_PERIM);

			try {
                if ((CD_ART == null) || (CD_ART.length() == 0) || (CD_ART.equals("0"))) {
		            aArt.create(myDBSession);
					if (CD_TYP_ART.equals(Integer.toString(ArtBean.TYP_ART_VENT_DETAIL))) {
			            // Création automatique de la prestation
                        aArt.creationPrestation(myDBSession);
			            mySalon.setMessage("Info", BasicSession.TAG_I18N + "ficArt.creationOk" + BasicSession.TAG_I18N);
					}
				}
				else {
		            aArt.maj(myDBSession);
                    if (CD_TYP_ART.equals(Integer.toString(ArtBean.TYP_ART_VENT_DETAIL))) {
                        // Mise à jour de la prestation correspondante si besoin
                        aArt.syncWithPrest(myDBSession);
                    }
				}

				// Mise à jour des lignes des fournisseurs
				for (int i = 0; i < nbFourn; i++) {
					// Création de la ligne et sauvegarde
					CatFournBean aFourn = CatFournBean.getCatFournBean(myDBSession, Long.toString(aArt.getCD_ART()), tab_CD_FOURN[i], tab_QTE_CMD_MIN[i]);

					aFourn.setCD_ART(aArt.getCD_ART());
					aFourn.setLIB_ART(tab_LIB_ART[i]);
					aFourn.setREF_ART(tab_REF_ART[i]);
					aFourn.setPRX_UNIT_HT(tab_PRX_UNIT_HT[i]);
					aFourn.setFOURN_PRINC(tab_FOURN_PRINC[i]);

					// Sauvegarde
					if (i != paramSup) {
						aFourn.maj(myDBSession);
					}
					else if (Action.equals ("SuppressionLigne")) {
						aFourn.delete(myDBSession);
					}
				}

				// Cas particulier de l'ajout d'une ligne
				if (Action.equals ("AjoutLigne")) {
					// C'est une nouvelle ligne
					CatFournBean aFourn = new CatFournBean();
					aFourn.setCD_FOURN(tab_CD_FOURN[paramSup]);
					aFourn.setQTE_CMD_MIN(tab_QTE_CMD_MIN[paramSup]);
					aFourn.setCD_ART(aArt.getCD_ART());
					aFourn.setLIB_ART(tab_LIB_ART[paramSup]);
					aFourn.setREF_ART(tab_REF_ART[paramSup]);
					aFourn.setPRX_UNIT_HT(tab_PRX_UNIT_HT[paramSup]);
					aFourn.setFOURN_PRINC(tab_FOURN_PRINC[paramSup]);
					aFourn.create(myDBSession);
				}

				mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.enregistrementOk" + BasicSession.TAG_I18N);
	            request.setAttribute("Action", "Modification");
			}
			catch (Exception e) {
	            mySalon.setMessage("Erreur", e.toString());
	            log.error("Erreur à la mise à jour de l'article", e);
				request.setAttribute("Action", Action);
			}
		}
		else if (Action.equals("Duplication")) {
			// Duplication de la fiche

			/**
			 * Création du bean et enregistrement
			 */
			aArt = new ArtBean(mySalon.getMessagesBundle());

			aArt.setLIB_ART(LIB_ART);
			aArt.setCD_CATEG_ART(CD_CATEG_ART);
			aArt.setCD_TYP_ART(CD_TYP_ART);
			aArt.setCD_UNIT_MES(CD_UNIT_MES);
			aArt.setCOMM(COMM);
			aArt.setLIB_ART(LIB_ART);
			aArt.setQTE_STK("0");
			aArt.setQTE_STK_MIN(QTE_STK_MIN);
			aArt.setREF_ART(REF_ART);
			aArt.setVAL_STK_HT("0");

			try {
	            aArt.create(myDBSession);

				// Création des lignes des fournisseurs
				for (int i = 0; i < nbFourn; i++) {
					// Création de la ligne et sauvegarde
					CatFournBean aFourn = new CatFournBean();

					aFourn.setCD_ART(aArt.getCD_ART());
					aFourn.setCD_FOURN(tab_CD_FOURN[i]);
					aFourn.setQTE_CMD_MIN(tab_QTE_CMD_MIN[i]);
					aFourn.setLIB_ART(tab_LIB_ART[i]);
					aFourn.setREF_ART(tab_REF_ART[i]);
					aFourn.setPRX_UNIT_HT(tab_PRX_UNIT_HT[i]);
					aFourn.setFOURN_PRINC(tab_FOURN_PRINC[i]);

					aFourn.create(myDBSession);
				}

				if (CD_TYP_ART.equals("1")) {
		            // Création automatique de la prestation
		            aArt.creationPrestation(myDBSession);
				}

				mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.duplicationOk" + BasicSession.TAG_I18N);
	            request.setAttribute("Action", "Modification");
			}
			catch (Exception e) {
	            mySalon.setMessage("Erreur", e.toString());
	            log.error("Erreur à la duplication de l'article", e);
				request.setAttribute("Action", Action);
			}
		}
		else if (Action.equals("Suppression")) {
			// Modification effective de la fiche

			/**
			 * Création du bean et enregistrement
			 */
			aArt = ArtBean.getArtBean(myDBSession, CD_ART, mySalon.getMessagesBundle());
            if (assertOrError((aArt != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
            	return;
            }

			try {
				// Suppression des lignes Fournisseurs en même temps
	            aArt.delete(myDBSession);
	            mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.suppressionOk" + BasicSession.TAG_I18N);
	            // Un bean vide
	            aArt = new ArtBean(mySalon.getMessagesBundle());
	            request.setAttribute("Action", "Creation");
	        }	
			catch (Exception e) {
	            // Interprétation de certaines erreurs
				if (e.toString().indexOf("fk_concerne_art") != -1) {
					mySalon.setMessage("Erreur", BasicSession.TAG_I18N + "ficArt.suppressionKoMouvement" + BasicSession.TAG_I18N);
				}
				else if (e.toString().indexOf("fk_correspond_art") != -1) {
					mySalon.setMessage("Erreur", BasicSession.TAG_I18N + "ficArt.suppressionKoPrestation" + BasicSession.TAG_I18N);
				}
				else {
					mySalon.setMessage("Erreur", e.toString());
				}
	            log.error("Erreur à la suppression de l'article", e);
				request.setAttribute("Action", "Modification");
			}
		}
		else {
			log.error("Action non codée : " + Action);
		}
	}
	catch (Exception e) {
		mySalon.setMessage("Erreur", e.toString());
        log.error("Erreur générale", e);
	}

    /**
     * Reset de la transaction pour la recherche des informations complémentaires
     */
    myDBSession.cleanTransaction();

	/**
	 * Recherche les fournisseurs de cet article
	 */
	Vector listeFourn = new Vector();
	if ((CD_ART != null) && (CD_ART.length() > 0)) {
		String reqSQL = "select CAT_FOURN.* from CAT_FOURN, FOURN where FOURN.CD_FOURN = CAT_FOURN.CD_FOURN and CD_ART=" + aArt.getCD_ART() + " order by FOURN_PRINC DESC, RAIS_SOC, QTE_CMD_MIN";

		// Interroge la Base
		try {
			ResultSet aRS = myDBSession.doRequest(reqSQL);

			while (aRS.next()) {
				/**
				 * Création du bean de consultation
				 */
				CatFournBean aFourn = new CatFournBean(aRS);
				listeFourn.add(aFourn);
			}
			aRS.close();
		}
		catch (Exception e) {
            log.error("Erreur à la recherche des fournisseurs", e);
			try {
				response.sendError(500);
			}
			catch (Exception e2) {
	            log.error("Erreur à la redirection sur erreur 500", e2);
			}
		}
	}
	request.setAttribute("listeFourn", listeFourn);
	request.setAttribute("ArtBean", aArt);

	try {
		// Passe la main à la fiche de création
		getServletConfig().getServletContext().getRequestDispatcher("/ficArt.jsp").forward(request, response);

	}
	catch (Exception e) {
        log.error("Erreur à la redirection", e);
	}
}
}
