/*
 * Création d'un paiement 
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
package com.increg.salon.servlet;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.increg.commun.BasicSession;
import com.increg.commun.DBSession;
import com.increg.commun.exception.FctlException;
import com.increg.salon.bean.FactBean;
import com.increg.salon.bean.PaiementBean;
import com.increg.salon.bean.ReglementBean;
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

    	Log log = LogFactory.getLog(this.getClass());

        // Récupération des paramètres
        String Action = request.getParameter("Action");
        String CD_PAIEMENT = request.getParameter("CD_PAIEMENT");
        String DT_PAIEMENT = request.getParameter("DT_PAIEMENT");

        // Données de règlement
        TreeMap<String, String> REGLEMENT = new TreeMap<String, String>();
        Enumeration paramEnum = request.getParameterNames();
        while (paramEnum.hasMoreElements()) {
			String paramName = (String) paramEnum.nextElement();
			
			if (paramName.startsWith("REGLEMENT") && StringUtils.isNotEmpty(request.getParameter(paramName))) {
				REGLEMENT.put(paramName.substring("REGLEMENT".length()),
							request.getParameter(paramName));
			}
		}

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
        Vector<ReglementBean> reglements = null;
        BigDecimal totPrest = null;

        try {
            if (Action == null) {
                // Première phase de création
                request.setAttribute("Action", "Creation");
                // Un bean vide
                aPaiement = new PaiementBean(mySalon.getMessagesBundle());
                aPaiement.setDT_PAIEMENT(aPaiement.getDT_PAIEMENT_defaut());
                reglements = new Vector<ReglementBean>();
                totPrest = new BigDecimal(0);
            } else if (Action.equals("Rafraichissement")) {
                aPaiement = new PaiementBean(mySalon.getMessagesBundle());

                try {
                    aPaiement.setCD_PAIEMENT(CD_PAIEMENT);
                    aPaiement.setDT_PAIEMENT(DT_PAIEMENT, mySalon.getLangue());
                    reglements = new Vector<ReglementBean>();

                    // enregistre les paiements des factures
                    for (int i = 0; i < listeCoche.size(); i++) {
                        FactBean aFact = FactBean.getFactBean(myDBSession, (String) listeCoche.get(i), mySalon.getMessagesBundle());
                        if (totPrest == null) {
                            totPrest = aFact.getPRX_TOT_TTC();
                        } else {
                            totPrest = totPrest.add(aFact.getPRX_TOT_TTC());
                        }
                    }
                    
                    // Prépare les règlements
                	for (Iterator iterReglement = REGLEMENT.keySet().iterator(); iterReglement.hasNext();) {
						String key = (String) iterReglement.next();
						ReglementBean aReglement = new ReglementBean(mySalon.getMessagesBundle());
						aReglement.setCD_MOD_REGL(key);
						aReglement.setCD_PAIEMENT(aPaiement.getCD_PAIEMENT());
						aReglement.setMONTANT(REGLEMENT.get(key));
            			reglements.add(aReglement);
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
                	myDBSession.setDansTransactions(true);
                	
                    aPaiement.setCD_PAIEMENT(CD_PAIEMENT);
                    aPaiement.setDT_PAIEMENT(DT_PAIEMENT, mySalon.getLangue());

                    aPaiement.create(myDBSession);
                    reglements = new Vector<ReglementBean>();

                	// enregistre les paiements des factures
                    for (int i = 0; i < listeCoche.size(); i++) {
                        FactBean aFact = FactBean.getFactBean(myDBSession, (String) listeCoche.get(i), mySalon.getMessagesBundle());

                        aFact.setCD_PAIEMENT(aPaiement.getCD_PAIEMENT());
                        aFact.maj(myDBSession);
                    }

                    // Prépare les règlements
                	for (Iterator iterReglement = REGLEMENT.keySet().iterator(); iterReglement.hasNext();) {
						String key = (String) iterReglement.next();
						ReglementBean aReglement = new ReglementBean(mySalon.getMessagesBundle());
						aReglement.setCD_MOD_REGL(key);
						aReglement.setCD_PAIEMENT(aPaiement.getCD_PAIEMENT());
						aReglement.setMONTANT(REGLEMENT.get(key));
						aReglement.create(myDBSession);
            			reglements.add(aReglement);
					}

                    totPrest = aPaiement.calculTotaux(myDBSession);

                	if ((aPaiement.getCD_PAIEMENT() != 0) && !aPaiement.verifReglement(myDBSession)) {
                		throw new FctlException(BasicSession.TAG_I18N + "ficFact.facturePartiellementPayee" + BasicSession.TAG_I18N);
                	}

                    // Paiement d'une facture : Suppression de la liste des
                    // encours
                    for (int i = 0; i < listeCoche.size(); i++) {
                        FactBean aFact = FactBean.getFactBean(myDBSession, (String) listeCoche.get(i), mySalon.getMessagesBundle());

                        mySalon.removeClient(Long.toString(aFact.getCD_FACT()));
                    }

                    myDBSession.endTransaction();
                    
                    mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.creationOk" + BasicSession.TAG_I18N);
                    request.setAttribute("Action", "Modification");
                } catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                    request.setAttribute("Action", Action);
                }
            } else if ((Action.equals("Modification")) && (listeFact.size() == 0) && (REGLEMENT.size() == 0)) {
                // Affichage de la fiche en modification
                request.setAttribute("Action", "Modification");

                aPaiement = PaiementBean.getPaiementBean(myDBSession, CD_PAIEMENT, mySalon.getMessagesBundle());
                if (assertOrError((aPaiement != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
                	return;
                }
                totPrest = aPaiement.calculTotaux(myDBSession);
                reglements = aPaiement.getReglement(myDBSession);
            } else if (Action.equals("Modification") || (Action.equals("Impression"))) {
                // Modification effective de la fiche

                /**
                 * Création du bean et enregistrement
                 */
                aPaiement = PaiementBean.getPaiementBean(myDBSession, CD_PAIEMENT, mySalon.getMessagesBundle());
                if (assertOrError((aPaiement != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
                	return;
                }
                totPrest = aPaiement.calculTotaux(myDBSession);
                reglements = aPaiement.getReglement(myDBSession);

                try {
                	myDBSession.setDansTransactions(true);
                	
                    aPaiement.setCD_PAIEMENT(CD_PAIEMENT);
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

                    if (REGLEMENT.size() > 0) {
                        // Gestion des réglements
                        TreeMap<String, String> workReglement = (TreeMap<String, String>) REGLEMENT.clone();
                    	for (ReglementBean aReglement : reglements) {
                    		if (workReglement.containsKey(Integer.toString(aReglement.getCD_MOD_REGL()))) {
                    			aReglement.setMONTANT(workReglement.get(Integer.toString(aReglement.getCD_MOD_REGL())));
                    			workReglement.remove(Integer.toString(aReglement.getCD_MOD_REGL())); // Traité
                    			aReglement.maj(myDBSession);
                    		}
                    		else {
                    			// Paiement inutile
                    			reglements.remove(aReglement);
                    			aReglement.delete(myDBSession);
                    		}
						}
                    	
                    	// Faut-il en ajouter ?
                    	for (Iterator iterRestant = workReglement.keySet().iterator(); iterRestant.hasNext();) {
							String key = (String) iterRestant.next();
							ReglementBean aReglement = new ReglementBean(mySalon.getMessagesBundle());
							aReglement.setCD_MOD_REGL(key);
							aReglement.setCD_PAIEMENT(aPaiement.getCD_PAIEMENT());
							aReglement.setMONTANT(workReglement.get(key));
							aReglement.create(myDBSession);
                			reglements.add(aReglement);
						}
                    }
                    
                    totPrest = aPaiement.calculTotaux(myDBSession);
                    
                	if ((aPaiement.getCD_PAIEMENT() != 0) && !aPaiement.verifReglement(myDBSession)) {
                		throw new FctlException(BasicSession.TAG_I18N + "ficFact.facturePartiellementPayee" + BasicSession.TAG_I18N);
                	}

                	myDBSession.endTransaction();
                	
                    mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.enregistrementOk" + BasicSession.TAG_I18N);
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
                if (assertOrError((aPaiement != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
                	return;
                }

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
                    mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.suppressionOk" + BasicSession.TAG_I18N);
                    // Un bean vide
                    aPaiement = new PaiementBean(mySalon.getMessagesBundle());
                    reglements = new Vector<ReglementBean>();
                    totPrest = new BigDecimal(0);
                    request.setAttribute("Action", "Creation");
                } catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                    request.setAttribute("Action", "Modification");
                }
            } else {
                log.error("Action non codée : " + Action);
            }
        } catch (Exception e) {
            mySalon.setMessage("Erreur", e.toString());
            log.error("Erreur générale : ", e);
        }

        /**
         * Reset de la transaction pour la recherche des informations
         * complémentaires
         */
        myDBSession.cleanTransaction();

        if ((Action == null) || (!Action.equals("Impression"))) {
            String reqSQL = "select FACT.* from FACT, CLI where FACT.FACT_HISTO = 'N' and FACT.CD_CLI=CLI.CD_CLI and (CD_PAIEMENT is null or CD_PAIEMENT=" + aPaiement.getCD_PAIEMENT()
                    + ") order by FACT.DT_CREAT, NOM, PRENOM, CLI.CD_CLI";

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
                log.error("Erreur dans requète pour liste : ", e);
                try {
                    response.sendError(500);
                } catch (Exception e2) {
                    log.error("Erreur sur sendError : ", e2);
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

        Map<Integer, ReglementBean> mapCD_MOD_REGL = new HashMap<Integer, ReglementBean>();
        for (ReglementBean aReglement : reglements) {
			mapCD_MOD_REGL.put(aReglement.getCD_MOD_REGL(), aReglement);
		}

        request.setAttribute("PaiementBean", aPaiement);
        request.setAttribute("listeFact", listeFact);
    	request.setAttribute("Reglements", reglements);
    	request.setAttribute("mapCD_MOD_REGL", mapCD_MOD_REGL);
        // Donne le total des prestations
        request.setAttribute("totPrest", totPrest);

        try {
            if ((Action != null) && (Action.equals("Impression"))) {
                // Passe la main à la fiche d'impression
                getServletConfig().getServletContext().getRequestDispatcher("/ficFactImpr.jsp").forward(request, response);
            } else {
                // Passe la main à la fiche de création
                getServletConfig().getServletContext().getRequestDispatcher("/ficPaiement.jsp").forward(request, response);
            }

        } catch (Exception e) {
            log.error("Erreur à la redirection : ", e);
        }
    }
}