/*
 * Fiche article avec les mouvements de stock
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
package com.increg.salon.servlet;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.increg.commun.BasicSession;
import com.increg.commun.DBSession;
import com.increg.salon.bean.ArtBean;
import com.increg.salon.bean.MvtStkBean;
import com.increg.salon.bean.SalonSession;
import com.increg.util.SimpleDateFormatEG;
/**
 * Fiche article avec mouvements
 * Creation date: (22/09/2001 19:15:33)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class FicArt_Mvt extends ConnectedServlet {

	protected Log log = LogFactory.getLog(this.getClass());
	
	/**
     * @see com.increg.salon.servlet.ConnectedServlet
     */
    public void performTask(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) {

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

        String zNbMvt = request.getParameter("NbMvt");
        String zDebMvt = request.getParameter("DebMvt");
        String ParamSup = request.getParameter("ParamSup");

        int nbMvt = 10;
        if (zNbMvt != null) {
            nbMvt = Integer.parseInt(zNbMvt);
        }

        int debMvt = 0;
        if (zDebMvt != null) {
            debMvt = Integer.parseInt(zDebMvt);
        }

        // Données de ligne
        // Ligne en cours de saisie
        String tab_DT_MVT = request.getParameter("DT_MVT");
        String tab_CD_TYP_MVT = request.getParameter("CD_TYP_MVT");
        String tab_QTE = request.getParameter("QTE");
        String tab_VAL_MVT_HT = request.getParameter("VAL_MVT_HT");
        String tab_DT_MVT_LAST = request.getParameter("DT_MVT_LAST");
        String tab_CD_FACT_LAST = request.getParameter("CD_FACT_LAST");

        // Récupère la connexion
        HttpSession mySession = request.getSession(false);
        SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
        DBSession myDBSession = mySalon.getMyDBSession();
        DateFormat formatDate = new SimpleDateFormat(mySalon.getMessagesBundle().getString("format.dateDefaut"));
        DateFormat formatDateTZ = new SimpleDateFormatEG(mySalon.getMessagesBundle().getString("format.dateDefaut"));

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
                        mySalon.setMessage("Info", BasicSession.TAG_I18N + "ficArt" + BasicSession.TAG_I18N);
                    }
                    request.setAttribute("Action", "Modification");
                }
                catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                    log.error("Erreur à la création de l'article", e);
                    request.setAttribute("Action", Action);
                }
            }
            if ((Action != null) && (Action.equals("Modification")) && (LIB_ART == null)) {
                // Affichage de la fiche en modification
                request.setAttribute("Action", "Modification");

                aArt = ArtBean.getArtBean(myDBSession, CD_ART, mySalon.getMessagesBundle());
                if (assertOrError((aArt != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
                	return;
                }
            }
            else if (
                (Action != null)
                    && ((Action.equals("Modification")) || (Action.equals("AjoutLigne")) || (Action.equals("Suivant")) || (Action.equals("Precedent")) || (Action.equals("SuppressionLigne")))) {
                // Modification effective de la fiche

                int paramSup = -1;
                if (Action.equals("AjoutLigne")) {
                    paramSup = Integer.parseInt(ParamSup);
                }
                if (Action.equals("Suivant")) {
                    debMvt += 10;
                }
                if (Action.equals("Precedent")) {
                    debMvt -= 10;
                    if (debMvt < 0) {
                        debMvt = 0;
                    }
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

                    // Cas particulier de l'ajout d'une ligne
                    if (Action.equals("AjoutLigne")) {
                        // C'est une nouvelle ligne
                        MvtStkBean aMvt = new MvtStkBean(mySalon.getMessagesBundle());
                        aMvt.setCD_ART(aArt.getCD_ART());
                        Calendar dtMvt = Calendar.getInstance();
                        dtMvt.setTime(formatDate.parse(tab_DT_MVT));
                        aMvt.setDT_MVT(dtMvt);
                        aMvt.setCD_TYP_MVT(tab_CD_TYP_MVT);
                        aMvt.setQTE(tab_QTE);
                        aMvt.setVAL_MVT_HT(tab_VAL_MVT_HT);
                        aMvt.setSTK_AVANT(aArt.getQTE_STK());
                        aMvt.setVAL_STK_AVANT(aArt.getVAL_STK_HT());
                        aMvt.create(myDBSession);
                        // Recharge l'article pour être à jour
                        aArt = ArtBean.getArtBean(myDBSession, Long.toString(aArt.getCD_ART()), mySalon.getMessagesBundle());
                    }
                    else if (Action.equals("SuppressionLigne")) {
                    	Calendar dtMvt = Calendar.getInstance();
                    	dtMvt.setTime(formatDateTZ.parse(tab_DT_MVT_LAST));
                    	dtMvt.setTimeZone(formatDateTZ.getTimeZone());
                        MvtStkBean aMvt = MvtStkBean.getMvtStkBean(myDBSession, Long.toString(aArt.getCD_ART()), dtMvt, tab_CD_FACT_LAST, mySalon.getLangue(), mySalon.getMessagesBundle());

                        if (aMvt != null) {
                            aMvt.delete(myDBSession);
                            // Recharge l'article pour être à jour
                            aArt = ArtBean.getArtBean(myDBSession, Long.toString(aArt.getCD_ART()), mySalon.getMessagesBundle());
                        }
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

                    if (CD_TYP_ART.equals("1")) {
                        // Création automatique de la prestation
                        aArt.creationPrestation(myDBSession);
                    }

                    mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.duplicationOk" + BasicSession.TAG_I18N);
                    request.setAttribute("Action", "Modification");
                }
                catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                    log.error("Erreur à la création de l'article par duplication", e);
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
                    mySalon.setMessage("Erreur", e.toString());
                    log.error("Erreur à la suppression de l'article", e);
                    request.setAttribute("Action", "Modification");
                }
            }
            else {
                System.out.println("Action non codée : " + Action);
            }
        }
        catch (Exception e) {
            mySalon.setMessage("Erreur", e.toString());
            log.error("Erreur générale", e);
            System.out.println("Note : " + e.toString());
        }

        /**
         * Reset de la transaction pour la recherche des informations complémentaires
         */
        myDBSession.cleanTransaction();

        /**
         * Recherche les mouvements de cet article
         */
        Vector listeMvt = new Vector();
        if ((CD_ART != null) && (CD_ART.length() > 0)) {
            boolean vide = true;

            while (vide) {
                String reqSQL = "select * from MVT_STK where CD_ART=" + aArt.getCD_ART() + " order by DT_MVT desc limit " + nbMvt + " offset " + debMvt;

                // Interroge la Base
                try {
                    ResultSet aRS = myDBSession.doRequest(reqSQL);

                    while (aRS.next()) {
                        /**
                         * Création du bean de consultation
                         */
                        MvtStkBean aMvt = new MvtStkBean(aRS, mySalon.getMessagesBundle());
                        listeMvt.add(aMvt);
                        vide = false;
                    }
                    aRS.close();

                    if ((vide) && (debMvt > 0)) {
                        // On est allé trop loin
                        debMvt -= 10;
                        if (debMvt < 0) {
                            debMvt = 0;
                        }
                    }
                    else {
                        // On s'arrete : Il n'y a rien a afficher
                        vide = false;
                    }

                }
                catch (Exception e) {
                    vide = false; // Stop la boucle
                    System.out.println("Erreur dans requète sur clé : " + e.toString());
                    log.error("Erreur à la recherche des mouvements", e);
                    try {
                        response.sendError(500);
                    }
                    catch (Exception e2) {
                        System.out.println("Erreur sur sendError : " + e2.toString());
                        log.error("Erreur à la redirection sur erreur 500", e);
                    }
                }
            }
        }
        request.setAttribute("DebMvt", new Integer(debMvt));
        request.setAttribute("NbMvt", new Integer(nbMvt));
        request.setAttribute("listeMvt", listeMvt);
        request.setAttribute("ArtBean", aArt);

        try {
            // Passe la main à la fiche de création
            getServletConfig().getServletContext().getRequestDispatcher("/ficArt_Mvt.jsp").forward(request, response);

        }
        catch (Exception e) {
            System.out.println("FicArt_Mvt::performTask : Erreur à la redirection : " + e.toString());
            log.error("Erreur à la redirection", e);
        }
    }
}
