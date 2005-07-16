/*
 * Création d'un paiement 
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

import java.sql.ResultSet;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import com.increg.commun.DBSession;
import com.increg.salon.bean.FactBean;
import com.increg.salon.bean.PaiementBean;
import com.increg.salon.bean.SalonSession;
import com.increg.salon.request.EditionFacture;

/**
 * Création d'un paiement 
 * Creation date: (10/09/2001 21:47:36)
 * 
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class FicPaiement extends ConnectedServlet {
    /**
     * @see com.increg.salon.servlet.ConnectedServlet
     */
    public void performTask(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) {

        // Récupération des paramètres
        String Action = request.getParameter("Action");
        String CD_PAIEMENT = request.getParameter("CD_PAIEMENT");
        String CD_MOD_REGL = request.getParameter("CD_MOD_REGL");
        String DT_PAIEMENT = request.getParameter("DT_PAIEMENT");

        // Liste des factures cochées
        Vector listeCoche = new Vector();
        Enumeration factEnum = request.getParameterNames();
        while (factEnum.hasMoreElements()) {
            String aName = (String) factEnum.nextElement();
            if (aName.indexOf("AFFECT") != -1) {
                listeCoche.add(aName.substring(6));
            }
        }

        // Récupère la connexion
        HttpSession mySession = request.getSession(false);
        SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
        DBSession myDBSession = mySalon.getMyDBSession();

        PaiementBean aPaiement = null;
        Vector listeFact = new Vector();

        try {
            if (Action == null) {
                // Première phase de création
                request.setAttribute("Action", "Creation");
                // Un bean vide
                aPaiement = new PaiementBean(mySalon.getMessagesBundle());
                aPaiement.setDT_PAIEMENT(aPaiement.getDT_PAIEMENT_defaut());
            } else if (Action.equals("Rafraichissement")) {
                aPaiement = new PaiementBean(mySalon.getMessagesBundle());

                try {
                    aPaiement.setCD_PAIEMENT(CD_PAIEMENT);
                    aPaiement.setCD_MOD_REGL(CD_MOD_REGL);
                    aPaiement.setDT_PAIEMENT(DT_PAIEMENT, mySalon.getLangue());

                    // enregistre les paiements des factures
                    for (int i = 0; i < listeCoche.size(); i++) {
                        FactBean aFact = FactBean.getFactBean(myDBSession, (String) listeCoche.get(i), mySalon.getMessagesBundle());
                        if (aPaiement.getPRX_TOT_TTC() == null) {
                            aPaiement.setPRX_TOT_TTC(aFact.getPRX_TOT_TTC());
                        } else {
                            aPaiement.setPRX_TOT_TTC(aPaiement.getPRX_TOT_TTC().add(aFact.getPRX_TOT_TTC()));
                        }
                    }
                } catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                }

                if ((CD_PAIEMENT != null) && (CD_PAIEMENT.length() > 0) && (!CD_PAIEMENT.equals("0"))) {
                    request.setAttribute("Action", "Modification");
                } else {
                    request.setAttribute("Action", "Creation");
                }
                request.setAttribute("Etat", "Memoire");
            } else if (Action.equals("Creation") || (Action.equals("Impression") && ((CD_PAIEMENT == null) || (CD_PAIEMENT.length() == 0) || (CD_PAIEMENT.equals("0"))))) {
                // Crée réellement la prestation

                /**
                 * Création du bean et enregistrement
                 */
                aPaiement = new PaiementBean(mySalon.getMessagesBundle());

                try {
                    aPaiement.setCD_PAIEMENT(CD_PAIEMENT);
                    aPaiement.setCD_MOD_REGL(CD_MOD_REGL);
                    aPaiement.setDT_PAIEMENT(DT_PAIEMENT, mySalon.getLangue());

                    aPaiement.create(myDBSession);

                    // enregistre les paiements des factures
                    for (int i = 0; i < listeCoche.size(); i++) {
                        FactBean aFact = FactBean.getFactBean(myDBSession, (String) listeCoche.get(i), mySalon.getMessagesBundle());

                        aFact.setCD_PAIEMENT(aPaiement.getCD_PAIEMENT());
                        aFact.maj(myDBSession);

                        // Paiement d'une facture : Suppression de la liste des
                        // encours
                        mySalon.removeClient(Long.toString(aFact.getCD_FACT()));
                    }

                    aPaiement.calculTotaux(myDBSession);

                    mySalon.setMessage("Info", "Création effectuée.");
                    request.setAttribute("Action", "Modification");
                } catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                    request.setAttribute("Action", Action);
                }
            } else if (((Action.equals("Modification")) && (CD_MOD_REGL == null)) || (Action.equals("Rafraichissement"))) {
                // Affichage de la fiche en modification
                request.setAttribute("Action", "Modification");

                aPaiement = PaiementBean.getPaiementBean(myDBSession, CD_PAIEMENT, mySalon.getMessagesBundle());
            } else if (Action.equals("Modification") || (Action.equals("Impression"))) {
                // Modification effective de la fiche

                /**
                 * Création du bean et enregistrement
                 */
                aPaiement = PaiementBean.getPaiementBean(myDBSession, CD_PAIEMENT, mySalon.getMessagesBundle());

                try {
                    aPaiement.setCD_PAIEMENT(CD_PAIEMENT);
                    aPaiement.setCD_MOD_REGL(CD_MOD_REGL);
                    aPaiement.setDT_PAIEMENT(DT_PAIEMENT, mySalon.getLangue());

                    aPaiement.maj(myDBSession);

                    Vector aListeFact = aPaiement.getFact(myDBSession);
                    for (int i = 0; i < aListeFact.size(); i++) {
                        FactBean aFact = (FactBean) aListeFact.get(i);

                        // Supprime les paiements des Factures utilisant ce
                        // paiement
                        aFact.setCD_PAIEMENT(0);
                        mySalon.addFact(Long.toString(aFact.getCD_FACT()));
                    }

                    // enregistre les paiements des factures
                    for (int i = 0; i < listeCoche.size(); i++) {
                        boolean trouve = false;
                        // Recherche la facture parmis celle en mémoire
                        for (int j = 0; (j < aListeFact.size()) && (!trouve); j++) {
                            FactBean aFact = (FactBean) aListeFact.get(j);
                            if (aFact.getCD_FACT() == Long.parseLong((String) listeCoche.get(i))) {
                                aFact.setCD_PAIEMENT(aPaiement.getCD_PAIEMENT());

                                // Paiement d'une facture : Suppression de la
                                // liste des encours
                                mySalon.removeClient(Long.toString(aFact.getCD_FACT()));

                                trouve = true;
                            }
                        }

                        if (!trouve) {
                            // Nouvelle facture pour ce paiement
                            FactBean aFact = FactBean.getFactBean(myDBSession, (String) listeCoche.get(i), mySalon.getMessagesBundle());

                            aFact.setCD_PAIEMENT(aPaiement.getCD_PAIEMENT());
                            aListeFact.add(aFact);

                            // Paiement d'une facture : Suppression de la liste
                            // des encours
                            mySalon.removeClient(Long.toString(aFact.getCD_FACT()));
                        }
                    }

                    // Enregistre les factures
                    for (int i = 0; i < aListeFact.size(); i++) {
                        FactBean aFact = (FactBean) aListeFact.get(i);
                        aFact.maj(myDBSession);
                    }

                    aPaiement.calculTotaux(myDBSession);

                    mySalon.setMessage("Info", "Enregistrement effectué.");
                    request.setAttribute("Action", "Modification");
                } catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                    request.setAttribute("Action", Action);
                }
            } else if (Action.equals("Suppression")) {
                // Modification effective de la fiche

                /**
                 * Création du bean et enregistrement
                 */
                aPaiement = PaiementBean.getPaiementBean(myDBSession, CD_PAIEMENT, mySalon.getMessagesBundle());

                try {
                    // Supprime les paiements des Factures utilisant ce paiement
                    Vector aListeFact = aPaiement.getFact(myDBSession);
                    for (int i = 0; i < aListeFact.size(); i++) {
                        FactBean aFact = (FactBean) aListeFact.get(i);

                        // Supprime les paiements des Factures utilisant ce
                        // paiement
                        aFact.setCD_PAIEMENT(0);
                        aFact.maj(myDBSession);
                        mySalon.addFact(Long.toString(aFact.getCD_FACT()));
                    }
                    aPaiement.delete(myDBSession);
                    mySalon.setMessage("Info", "Suppression effectuée.");
                    // Un bean vide
                    aPaiement = new PaiementBean(mySalon.getMessagesBundle());
                    request.setAttribute("Action", "Creation");
                } catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                    request.setAttribute("Action", "Modification");
                }
            } else {
                System.out.println("Action non codée : " + Action);
            }
        } catch (Exception e) {
            mySalon.setMessage("Erreur", e.toString());
            System.out.println("Note : " + e.toString());
        }

        /**
         * Reset de la transaction pour la recherche des informations
         * complémentaires
         */
        myDBSession.cleanTransaction();

        if ((Action == null) || (!Action.equals("Impression"))) {
            String reqSQL = "select * from FACT, CLI where FACT.FACT_HISTO = 'N' and FACT.CD_CLI=CLI.CD_CLI and (CD_PAIEMENT is null or CD_PAIEMENT=" + aPaiement.getCD_PAIEMENT()
                    + ") order by FACT.DT_CREAT, NOM, PRENOM";

            // Interroge la Base
            try {
                ResultSet aRS = myDBSession.doRequest(reqSQL);

                while (aRS.next()) {
                    /**
                     * Création du bean de consultation
                     */
                    FactBean aFact = new FactBean(aRS, mySalon.getMessagesBundle());
                    listeFact.add(aFact);
                }

                aRS.close();
            } catch (Exception e) {
                System.out.println("Erreur dans requète pour liste : " + e.toString());
                try {
                    response.sendError(500);
                } catch (Exception e2) {
                    System.out.println("Erreur sur sendError : " + e2.toString());
                }
            }
        } else {
            // Crée le Bean spécifique pour l'édition
            EditionFacture aEditFact = new EditionFacture();
            aEditFact.setMyPaiement(aPaiement);

            // Création en mémoire d'une facture fictive contenant toutes les
            // lignes
            FactBean aFact = null;

            for (int i = 0; i < listeCoche.size(); i++) {
                if (aFact == null) {
                    aFact = FactBean.getFactBean(myDBSession, (String) listeCoche.get(i), mySalon.getMessagesBundle());
                } else {
                    aFact.merge(myDBSession, (String) listeCoche.get(i));
                }
            }
            aEditFact.setMyFact(aFact);
            Vector listeEdition = new Vector(1);
            listeEdition.add(aEditFact);
            request.setAttribute("listeEdition", listeEdition);
        }

        request.setAttribute("PaiementBean", aPaiement);
        request.setAttribute("listeFact", listeFact);

        try {
            if ((Action != null) && (Action.equals("Impression"))) {
                // Passe la main à la fiche d'impression
                getServletConfig().getServletContext().getRequestDispatcher("/ficFactImpr.jsp").forward(request, response);
            } else {
                // Passe la main à la fiche de création
                getServletConfig().getServletContext().getRequestDispatcher("/ficPaiement.jsp").forward(request, response);
            }

        } catch (Exception e) {
            System.out.println("FicPaiement::performTask : Erreur à la redirection : " + e.toString());
        }
    }
}