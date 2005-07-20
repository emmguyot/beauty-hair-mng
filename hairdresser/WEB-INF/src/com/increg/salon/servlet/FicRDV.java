/*
 * Fiche Rendez-vous
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

import java.net.HttpURLConnection;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.increg.commun.BasicSession;
import com.increg.commun.DBSession;
import com.increg.salon.bean.ClientBean;
import com.increg.salon.bean.CollabBean;
import com.increg.salon.bean.FactBean;
import com.increg.salon.bean.RDVBean;
import com.increg.salon.bean.SalonSession;

/**
 * Création d'un RDV
 * Creation date: 15 févr. 2004
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class FicRDV extends ConnectedServlet {
    /**
     * @see com.increg.salon.servlet.ConnectedServlet
     */
    public void performTask(HttpServletRequest request, HttpServletResponse response) {

        // Récupération des paramètres
        String Action = request.getParameter("Action");
        String CD_COLLAB = request.getParameter("CD_COLLAB");
        String CD_CLI = request.getParameter("CD_CLI");
        String DT_DEBUT = request.getParameter("DT_DEBUT");
        String HR_DEBUT = request.getParameter("HR_DEBUT");
        String DUREE = request.getParameter("DUREE");
        String COMM = request.getParameter("COMM");
        // En cas de création client
        String NOM = request.getParameter("NOM");
        String PRENOM = request.getParameter("PRENOM");

        // Concatene les heures pour former un timestamp
        if ((DT_DEBUT != null) && (HR_DEBUT != null)) {
            DT_DEBUT = DT_DEBUT + " " + HR_DEBUT;
            DT_DEBUT = DT_DEBUT.trim();
        }

        // Récupère la connexion
        HttpSession mySession = request.getSession(false);
        SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
        DBSession myDBSession = mySalon.getMyDBSession();

        RDVBean aRDV = null;
        Vector dispo = new Vector();
        List collabs = null;

        try {
            if (Action == null) {
                // Première phase de création
                request.setAttribute("Action", "Creation");
                // Un bean vide
                aRDV = new RDVBean();
            }
            else if (Action.equals("Creation") && (CD_COLLAB == null)) {
                // Création : Première partie
                // Première phase de création
                request.setAttribute("Action", "Creation");
                // Un bean vide : Juste le client
                aRDV = new RDVBean();
                aRDV.setCD_CLI(CD_CLI);

                // Positionne par défaut le collaborateur de la dernière fois
                String reqSQL = "select * from FACT where CD_CLI=" + CD_CLI + " order by DT_PREST desc limit 1";
                // Interroge la Base
                try {
                    ResultSet aRS = myDBSession.doRequest(reqSQL);

                    if (aRS.next()) {
                        FactBean aFact = new FactBean(aRS, mySalon.getMessagesBundle());
                        aRDV.setCD_COLLAB(aFact.getCD_COLLAB());
                    }
                    aRS.close();
                }
                catch (Exception e) {
                    System.out.println("Erreur dans requète sur facture historique: " + e.toString());
                    try {
                        response.sendError(HttpURLConnection.HTTP_INTERNAL_ERROR);
                    }
                    catch (Exception e2) {
                        System.out.println("Erreur sur sendError : " + e2.toString());
                    }
                }

            }
            else if ((Action.equals("Creation")) || (Action.equals("Creation+"))) {
                // Crée réellement le RDV voir le client au passage
                
                /**
                 * Création du bean et enregistrement
                 */
                aRDV = new RDVBean();

                try {
                    if (Action.equals("Creation+")) {
                        // Création du client
                        ClientBean aClient = new ClientBean();
                        aClient.setNOM(NOM);
                        aClient.setPRENOM(PRENOM);
                        aClient.create(myDBSession);
                        
                        aRDV.setCD_CLI(aClient.getCD_CLI());
                    }
                    else {
                        aRDV.setCD_CLI(CD_CLI);
                    }

                    aRDV.setCD_COLLAB(CD_COLLAB);
                    aRDV.setDUREE(DUREE);
                    aRDV.setDT_DEBUT(DT_DEBUT, mySalon.getLangue());
                    aRDV.setCOMM(COMM);

                    aRDV.create(myDBSession);

                    mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.creationOk" + BasicSession.TAG_I18N);
                    if (!aRDV.verifChevauchement(myDBSession, true)) {
                        // Chevauchement
                        mySalon.setMessage("Info", BasicSession.TAG_I18N + "ficRDV.conflitRDV" + BasicSession.TAG_I18N);
                    }
                    request.setAttribute("Action", "Modification");
                }
                catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                    request.setAttribute("Action", Action);
                }
            }
            else if ((Action.equals("Modification")) && (CD_COLLAB == null)) {
                // Affichage de la fiche en modification
                request.setAttribute("Action", "Modification");

                aRDV = RDVBean.getRDVBean(myDBSession, CD_CLI, DT_DEBUT, mySalon.getLangue());

            }
            else if (Action.equals("Modification")) {
                // Modification effective de la fiche

                /**
                 * Création du bean et enregistrement
                 */
                aRDV = RDVBean.getRDVBean(myDBSession, CD_CLI, DT_DEBUT, mySalon.getLangue());

                try {
                    aRDV.setCD_COLLAB(CD_COLLAB);
                    aRDV.setDUREE(DUREE);
                    aRDV.setCOMM(COMM);

                    aRDV.maj(myDBSession);

                    mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.enregistrementOk" + BasicSession.TAG_I18N);
                    if (!aRDV.verifChevauchement(myDBSession, true)) {
                        // Chevauchement
                        mySalon.setMessage("Info", BasicSession.TAG_I18N + "ficRDV.conflitRDV" + BasicSession.TAG_I18N);
                    }
                    request.setAttribute("Action", "Modification");
                }
                catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                    request.setAttribute("Action", Action);
                }
            }
            else if (Action.equals("Suppression")) {
                // Modification effective de la fiche

                /**
                 * Création du bean et enregistrement
                 */
                RDVBean aRDVtoDelete = RDVBean.getRDVBean(myDBSession, CD_CLI, DT_DEBUT, mySalon.getLangue());

                try {
                    aRDVtoDelete.delete(myDBSession);
                    mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.suppressionOk" + BasicSession.TAG_I18N);
                    // Un bean vide
                    aRDV = new RDVBean();
                    aRDV.setCD_CLI(CD_CLI);
                    aRDV.setCD_COLLAB(CD_COLLAB);
                    aRDV.setDT_DEBUT(DT_DEBUT, mySalon.getLangue());
                    aRDV.setDUREE(DUREE);
                    aRDV.setCOMM(COMM);
                    request.setAttribute("Action", "Creation");
                }
                catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                    request.setAttribute("Action", "Modification");
                }
            }
            else if (Action.equals("Rechargement")) {
                // Rechargement pour les dispos

                /**
                 * Création du bean et enregistrement
                 */
                boolean exist = false;
                aRDV = RDVBean.getRDVBean(myDBSession, CD_CLI, DT_DEBUT, mySalon.getLangue());
                if (aRDV == null) {
                    aRDV = new RDVBean();
                    request.setAttribute("Action", "Creation");
                    exist = false;
                }
                else {
                    request.setAttribute("Action", "Modification");
                    exist = true;
                }

                try {
                    aRDV.setCD_COLLAB(CD_COLLAB);
                    aRDV.setCD_CLI(CD_CLI);
                    aRDV.setDUREE(DUREE);
                    aRDV.setCOMM(COMM);
                    // Date en dernier en cas d'erreur de format
                    aRDV.setDT_DEBUT(DT_DEBUT, mySalon.getLangue());

                    if (!aRDV.verifChevauchement(myDBSession, exist)) {
                        // Chevauchement
                        mySalon.setMessage("Info", BasicSession.TAG_I18N + "ficRDV.conflitRDV" + BasicSession.TAG_I18N);
                    }
                }
                catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                    request.setAttribute("Action", Action);
                }
            }
            else {
                System.out.println("Action non codée : " + Action);
            }

            /**
             * Reset de la transaction pour la recherche des informations complémentaires
             */
            myDBSession.cleanTransaction();

            // *****************************************************************************************
            //Récupère les collaborateurs pour mettre a jour la liste
            collabs = CollabBean.getListeCollab(myDBSession, false);
            //IL faut egalement ajouter le collaborateur de la facture
            //Seulement si la liste ne contient pas CD_COLLAB
            CollabBean aCollab = CollabBean.getCollabBean(myDBSession, "" + aRDV.getCD_COLLAB());
            CollabBean.verifieEtAjoute(aCollab, collabs);

            // Recherche les rendez-vous du même jour et du collab pour afficher la disponibilité
            if ((aRDV.getCD_COLLAB() > 0) && (aRDV.getDT_DEBUT() != null)) {
                // A partir de 8h
                Calendar date = (Calendar) aRDV.getDT_DEBUT().clone();
                date.set(Calendar.HOUR_OF_DAY, 8);
                date.set(Calendar.MINUTE, 0);
                date.setTime(date.getTime());
                int duree = 15;
                boolean precedentLibre = false;
                // RDV qui sera stocké dans les dispo : Durées variables
                RDVBean dispoRDV = new RDVBean();
                dispoRDV = new RDVBean();
                dispoRDV.setCD_COLLAB(aRDV.getCD_COLLAB());
                dispoRDV.setDUREE(duree);
                // RDV de test : Toujours 1/4 d'heure
                RDVBean testRDV = new RDVBean();
                testRDV.setCD_COLLAB(aRDV.getCD_COLLAB());
                testRDV.setDUREE(duree);

                do {
                    // Ce quart d'heure est libre ?
                    testRDV.setDT_DEBUT((Calendar) date.clone());

                    if (testRDV.verifChevauchement(myDBSession, false)) {
                        // C'est dispo
                        if (precedentLibre) {
                            // Allonge la durée
                            dispoRDV.setDUREE(dispoRDV.getDUREE() + duree);
                        }
                        else {
                            // Un nouveau RDV
                            dispoRDV.setDT_DEBUT(testRDV.getDT_DEBUT());
                            dispo.add(dispoRDV);
                        }
                        precedentLibre = true;
                    }
                    else {
                        precedentLibre = false;
                        // Reset le RDV
                        dispoRDV = new RDVBean();
                        dispoRDV.setCD_COLLAB(aRDV.getCD_COLLAB());
                        dispoRDV.setDUREE(duree);
                    }

                    // Quart d'heure suivant
                    date.add(Calendar.MINUTE, duree);
                }
                while (date.get(Calendar.HOUR_OF_DAY) < 20);
            }

            request.setAttribute("Dispo", dispo);
        }
        catch (Exception e) {
            mySalon.setMessage("Erreur", e.toString());
            System.out.println("FicRDV::Note : " + e.toString());
        }

        request.setAttribute("RDVBean", aRDV);
        request.setAttribute("collabs", collabs);

        try {
            // Passe la main à la fiche de création
            getServletConfig().getServletContext().getRequestDispatcher("/ficRDV.jsp").forward(request, response);

        }
        catch (Exception e) {
            System.out.println("FicRDV::performTask : Erreur à la redirection : " + e.toString());
        }
    }
}
