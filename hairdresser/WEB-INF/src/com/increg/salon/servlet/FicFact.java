/*
 * Création/Modification/Suppression d'une Facture / Historique
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.increg.commun.BasicSession;
import com.increg.commun.DBSession;
import com.increg.commun.exception.FctlException;
import com.increg.salon.bean.CollabBean;
import com.increg.salon.bean.FactBean;
import com.increg.salon.bean.HistoPrestBean;
import com.increg.salon.bean.PaiementBean;
import com.increg.salon.bean.ParamBean;
import com.increg.salon.bean.ReglementBean;
import com.increg.salon.bean.SalonSession;
import com.increg.salon.request.EditionFacture;

/**
 * Création/Modification/Suppression d'une Facture / Historique
 * Creation date: (17/08/2001 22:51:52)
 * 
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class FicFact extends ConnectedServlet {
    /**
     * @see com.increg.salon.servlet.ConnectedServlet
     */
    public void performTask(HttpServletRequest request,
            HttpServletResponse response) {

    	Log log = LogFactory.getLog(this.getClass());

    	boolean allCorrect = true;
        boolean forcage = false;

        // Récupération des paramètres
        String Action = request.getParameter("Action");
        String CD_CLI = request.getParameter("CD_CLI");
        String CD_COLLAB = request.getParameter("CD_COLLAB");
        String CD_FACT = request.getParameter("CD_FACT");
        String CD_TYP_VENT = request.getParameter("CD_TYP_VENT");
        String DT_PREST = request.getParameter("DT_PREST");
        String FACT_HISTO = request.getParameter("FACT_HISTO");
        String REMISE_FIXE = request.getParameter("REMISE_FIXE");
        String REMISE_PRC = request.getParameter("REMISE_PRC");
        String TVA = request.getParameter("TVA");

        String CD_PAIEMENT = request.getParameter("CD_PAIEMENT");
        String DT_PAIEMENT = request.getParameter("DT_PAIEMENT");

        String MOT_PASSE = request.getParameter("MOT_PASSE");

        // Données temporaires
        String tmpNbPrest = request.getParameter("NbPrest");
        String ParamSup = request.getParameter("ParamSup");

        int nbPrest = 0;
        if (tmpNbPrest != null) {
            nbPrest = Integer.parseInt(tmpNbPrest);
        }

        // Données de ligne
        String[] tab_NUM_LIG_FACT = new String[nbPrest + 1];
        String[] tab_CD_TYP_VENT = new String[nbPrest + 1];
        String[] tab_CD_MARQUE = new String[nbPrest + 1];
        String[] tab_CD_CATEG_PREST = new String[nbPrest + 1];
        String[] tab_CD_PREST = new String[nbPrest + 1];
        String[] tab_CD_COLLAB = new String[nbPrest + 1];
        String[] tab_QTE = new String[nbPrest + 1];
        String[] tab_PRX_UNIT_TTC = new String[nbPrest + 1];
        String[] tab_COMM = new String[nbPrest + 1];
        String[] tab_NIV_SATISF = new String[nbPrest + 1];

        // Boucle de chargement
        for (int i = 0; i < nbPrest; i++) {
            tab_NUM_LIG_FACT[i] = request.getParameter("NUM_LIG_FACT" + Integer.toString(i));
            tab_CD_COLLAB[i] = request.getParameter("CD_COLLAB" + Integer.toString(i));
            tab_QTE[i] = request.getParameter("QTE" + Integer.toString(i));
            tab_PRX_UNIT_TTC[i] = request.getParameter("PRX_UNIT_TTC" + Integer.toString(i));
            tab_COMM[i] = request.getParameter("COMM" + Integer.toString(i));
            tab_NIV_SATISF[i] = request.getParameter("NIV_SATISF" + Integer.toString(i));
        }
        // Ligne en cours de saisie
        tab_NUM_LIG_FACT[nbPrest] = request.getParameter("NUM_LIG_FACT" + Integer.toString(nbPrest));
        tab_CD_TYP_VENT[nbPrest] = request.getParameter("CD_TYP_VENT" + Integer.toString(nbPrest));
        tab_CD_MARQUE[nbPrest] = request.getParameter("CD_MARQUE" + Integer.toString(nbPrest));
        tab_CD_CATEG_PREST[nbPrest] = request.getParameter("CD_CATEG_PREST" + Integer.toString(nbPrest));
        tab_CD_PREST[nbPrest] = request.getParameter("CD_PREST" + Integer.toString(nbPrest));
        tab_CD_COLLAB[nbPrest] = request.getParameter("CD_COLLAB" + Integer.toString(nbPrest));
        tab_QTE[nbPrest] = request.getParameter("QTE" + Integer.toString(nbPrest));
        tab_PRX_UNIT_TTC[nbPrest] = request.getParameter("PRX_UNIT_TTC" + Integer.toString(nbPrest));
        tab_COMM[nbPrest] = request.getParameter("COMM" + Integer.toString(nbPrest));
        tab_NIV_SATISF[nbPrest] = request.getParameter("NIV_SATISF" + Integer.toString(nbPrest));

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
        
        // Récupère la connexion
        HttpSession mySession = request.getSession(false);
        SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
        DBSession myDBSession = mySalon.getMyDBSession();

        // Attributs à positionner en fin
        FactBean aFact = null;
        PaiementBean aPaiement = null;
        Vector<ReglementBean> reglements = null;
        String totPrest = "0";
        String NbPrest = "0";
        List collabs = null;
        //La liste des personnes, format CD_COLLAB puis prenom
        //Elle peut contenir les personnes presentes dans tous les cas
        //                 sauf si on crée une facture historique
        //L'ordre etant respecté, on peut avoir des valeurs triées en sortie

        try {
            if (Action == null) {
                // Première phase de création
                request.setAttribute("Action", "Creation");
                // Un bean vide
                aFact = new FactBean(mySalon.getMessagesBundle());
                // Place le code client si connu
                aFact.setCD_CLI(CD_CLI);
                aFact.setFACT_HISTO(FACT_HISTO);

                aPaiement = new PaiementBean(mySalon.getMessagesBundle());
                reglements = new Vector<ReglementBean>();
            }
            // *****************************************************************************************
            else if (Action.equals("Creation")) {
                // Crée réellement la facture

                /**
                 * Création du bean et enregistrement
                 */
                aFact = new FactBean(mySalon.getMessagesBundle());
                aPaiement = new PaiementBean(mySalon.getMessagesBundle());
                reglements = new Vector<ReglementBean>();

                try {
                    aFact.setCD_CLI(CD_CLI);
                    aFact.setCD_COLLAB(CD_COLLAB);
                    aFact.setCD_TYP_VENT(CD_TYP_VENT);
                    aFact.setDT_PREST(DT_PREST, mySalon.getLangue());
                    aFact.setFACT_HISTO(FACT_HISTO);
                    aFact.setREMISE_FIXE(REMISE_FIXE);
                    aFact.setREMISE_PRC(REMISE_PRC);
                    aFact.setTVA(TVA);
                    aFact.setCD_PAIEMENT(CD_PAIEMENT);

                    // Déjà le mode de paiement ==> Pas normal. On refuse
                    if (REGLEMENT.size() > 0) {
                        mySalon.setMessage("Erreur",
                                BasicSession.TAG_I18N + "ficFact.factureVide" + BasicSession.TAG_I18N);
                    }

                    aFact.create(myDBSession);

                    // Nouvelle facture : Ajout au encours si ce n'est pas un
                    // historique
                    if ((FACT_HISTO != null) && (FACT_HISTO.equals("N"))
                            && (aFact.getCD_PAIEMENT() == 0)) {
                        mySalon.addFact(Long.toString(aFact.getCD_FACT()));
                    }

                    mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.creationOk" + BasicSession.TAG_I18N);
                    request.setAttribute("Action", "Modification");
                } catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                    request.setAttribute("Action", Action);
                }
            }
            // *****************************************************************************************
            else if (((Action.equals("Modification")) && (CD_CLI == null))
                    || (Action.equals("Rafraichissement"))) {
                // Affichage de la fiche en modification
                request.setAttribute("Action", "Modification");

                aFact = FactBean.getFactBean(myDBSession, CD_FACT, mySalon.getMessagesBundle());
                if (aFact == null) {
                    mySalon.setMessage("Erreur", BasicSession.TAG_I18N + "ficFact.noFacture" + BasicSession.TAG_I18N);
                    mySalon.removeClient(CD_FACT);
                    allCorrect = false;
                    aFact = new FactBean(mySalon.getMessagesBundle());
                }

                if (aFact.getCD_PAIEMENT() > 0) {
                    aPaiement = PaiementBean.getPaiementBean(myDBSession, Long
                            .toString(aFact.getCD_PAIEMENT()), mySalon.getMessagesBundle());
                    reglements = aPaiement.getReglement(myDBSession);
                } else {
                    aPaiement = new PaiementBean(mySalon.getMessagesBundle());
                    reglements = new Vector<ReglementBean>();
                }

            }
            // *****************************************************************************************
            else if ((Action.equals("Modification"))
                    || (Action.equals("Rechargement"))
                    || (Action.equals("Rechargement+"))
                    || (Action.equals("Rechargement++"))
                    || (Action.equals("Impression"))
                    || (Action.equals("AjoutLigne"))
                    || (Action.equals("SuppressionLigne"))) {
                // Modification effective de la fiche avec rechargement des
                // combo éventuels
                // N.B. : Mise en commun du code de modification

                int paramSup = -1;
                if ((Action.equals("AjoutLigne"))
                        || (Action.equals("SuppressionLigne"))) {
                    paramSup = Integer.parseInt(ParamSup);
                }
                boolean paiementASuppr = false;

                /**
                 * Création du bean et enregistrement
                 */
                if (StringUtils.isEmpty(CD_FACT) || (CD_FACT.equals("0"))) {
                    // On est en création : le Bean est créé de zero
                    aFact = new FactBean(mySalon.getMessagesBundle());
                } else {
                    // Recharge à partir de la base
                    aFact = FactBean.getFactBean(myDBSession, CD_FACT, mySalon.getMessagesBundle());
                    if (assertOrError((aFact != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
                    	return;
                    }
                }

                // Initialisation par défaut
                aPaiement = new PaiementBean(mySalon.getMessagesBundle());
                reglements = new Vector<ReglementBean>();
                try {
                    myDBSession.setDansTransactions(true);

                    aFact.setCD_CLI(CD_CLI);
                    aFact.setCD_COLLAB(CD_COLLAB);
                    aFact.setCD_TYP_VENT(CD_TYP_VENT);
                    aFact.setDT_PREST(DT_PREST, mySalon.getLangue());
                    aFact.setFACT_HISTO(FACT_HISTO);
                    aFact.setREMISE_FIXE(REMISE_FIXE);
                    aFact.setREMISE_PRC(REMISE_PRC);
                    aFact.setTVA(TVA);
                    aFact.setCD_PAIEMENT(CD_PAIEMENT);

                    /**
                     * Mise à jour du paiement d'abord avant pour limiter le
                     * nbre de modif de facture
                     */

                    if (REGLEMENT.size() > 0) {

                        if (StringUtils.isNotEmpty(CD_PAIEMENT) && (!CD_PAIEMENT.equals("0"))) {
                            aPaiement = PaiementBean.getPaiementBean(
                                    myDBSession, CD_PAIEMENT, mySalon.getMessagesBundle());
                            reglements = aPaiement.getReglement(myDBSession);
                        } else {
                            // Nouveau Paiement
                            aPaiement = new PaiementBean(mySalon.getMessagesBundle());
                            reglements = new Vector<ReglementBean>();
                        }

                        if ((nbPrest > 0) || (Action.equals("AjoutLigne"))) {
                            aPaiement.setDT_PAIEMENT(DT_PAIEMENT, mySalon.getLangue());

                            if (StringUtils.isNotEmpty(CD_PAIEMENT) && (!CD_PAIEMENT.equals("0"))) {
                                aPaiement.maj(myDBSession);
                            } else {
                                aPaiement.create(myDBSession);
                                aFact.setCD_PAIEMENT(aPaiement.getCD_PAIEMENT());
                            }
                            
                            // Enregistrement nécessaire pour avoir la relation aux règlements
                            if (StringUtils.isEmpty(CD_FACT) || (CD_FACT.equals("0"))) {
                                aFact.create(myDBSession);
                            }
                            else {
                            	aFact.maj(myDBSession);
                            }
                            
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
                        } else {
                            mySalon.setMessage("Erreur", BasicSession.TAG_I18N + "ficFact.factureVide" + BasicSession.TAG_I18N);
                        }
                    } else if (StringUtils.isNotEmpty(CD_PAIEMENT) && (!CD_PAIEMENT.equals("0"))) {
                        // Annulation du paiement
                        // AFAIRE : CTRL a INCLURE
                        aFact.setCD_PAIEMENT(0);

                        // Faut-il supprimer le paiement ?
                        aPaiement = PaiementBean.getPaiementBean(myDBSession, CD_PAIEMENT, mySalon.getMessagesBundle());
                        Vector listeFact = aPaiement.getFact(myDBSession);
                        if (listeFact.size() == 1) {
                            paiementASuppr = true;
                        } else {
                            throw new FctlException(BasicSession.TAG_I18N + "ficFact.multiFacture" + BasicSession.TAG_I18N);
                        }
                    }

                    // Mise à jour de la facture avec le bon code paiement
                    if (StringUtils.isEmpty(CD_FACT) || (CD_FACT.equals("0"))) {
                        aFact.create(myDBSession);
                    } else {
                        // Pas de mise à jour tout de suite : Attente du calcul
                        // du total
                        // Sauf si le Paiement est à supprimer
                        if (paiementASuppr) {
                            aFact.maj(myDBSession);
                        }
                    }

                    if (paiementASuppr) {
                        aPaiement.delete(myDBSession);
                        aPaiement = new PaiementBean(mySalon.getMessagesBundle());
                        reglements = new Vector<ReglementBean>();
                    }

                    // Nouvelle facture ? Ajout au encours si ce n'est pas un
                    // historique
                    if ((FACT_HISTO != null) && (FACT_HISTO.equals("N"))
                            && (aPaiement.getCD_PAIEMENT() == 0)) {
                        mySalon.addFact(Long.toString(aFact.getCD_FACT()));
                    }

                    // Mise à jour des lignes de factures
                    Vector lignes = new Vector();
                    for (int i = 0; i < nbPrest; i++) {
                        // Création de la ligne et sauvegarde
                        HistoPrestBean aPrest = HistoPrestBean
                                .getHistoPrestBean(myDBSession, CD_FACT,
                                        tab_NUM_LIG_FACT[i]);

                        aPrest.setCD_CLI(CD_CLI);
                        aPrest.setCOMM(tab_COMM[i]);
                        aPrest.setDT_PREST(DT_PREST, mySalon.getLangue());
                        aPrest.setNIV_SATISF(tab_NIV_SATISF[i]);
                        aPrest.setPRX_UNIT_TTC(tab_PRX_UNIT_TTC[i]);
                        aPrest.setQTE(tab_QTE[i]);
                        aPrest.setCD_COLLAB(tab_CD_COLLAB[i]);

                        // Sauvegarde
                        if (i != paramSup) {
                            aPrest.maj(myDBSession);
                            lignes.add(aPrest);
                        } else if (Action.equals("SuppressionLigne")) {
                            aPrest.delete(myDBSession);
                        }
                    }

                    // Cas particulier de l'ajout d'une ligne
                    if (Action.equals("AjoutLigne")) {
                        // C'est une nouvelle ligne
                        HistoPrestBean aPrest = new HistoPrestBean();
                        aPrest.setCD_FACT(aFact.getCD_FACT());
                        aPrest.setCD_COLLAB(tab_CD_COLLAB[paramSup]);
                        aPrest.setCD_PREST(tab_CD_PREST[paramSup]);
                        aPrest.setCD_CLI(CD_CLI);
                        aPrest.setCOMM(tab_COMM[paramSup]);
                        aPrest.setDT_PREST(DT_PREST, mySalon.getLangue());
                        aPrest.setNIV_SATISF(tab_NIV_SATISF[paramSup]);
                        aPrest.setPRX_UNIT_TTC(tab_PRX_UNIT_TTC[paramSup]);
                        aPrest.setQTE(tab_QTE[paramSup]);
                        aPrest.create(myDBSession);
                        lignes.add(aPrest);
                    }

                    // Calcul du pied de facture + **Maj** de la facture +
                    // **Maj** du paiement si besoin
                    aFact.calculTotaux(myDBSession);

                    aFact.setLignes(lignes);

                	if ((aPaiement.getCD_PAIEMENT() != 0) && !aPaiement.verifReglement(myDBSession)) {
                		throw new FctlException(BasicSession.TAG_I18N + "ficFact.facturePartiellementPayee" + BasicSession.TAG_I18N);
                	}

                	if (aPaiement.getCD_PAIEMENT() != 0) {
                        // Paiement d'une facture : Suppression de la
                        // liste des encours
                        mySalon.removeClient(Long.toString(aFact.getCD_FACT()));
                	}
                	
                    myDBSession.endTransaction();

                    // Informe de l'enregistrement
                    mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.enregistrementOk" + BasicSession.TAG_I18N);

                    request.setAttribute("Action", "Modification");
                } catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                    request.setAttribute("Action", Action);
                    if (StringUtils.isEmpty(CD_PAIEMENT) || (CD_PAIEMENT.equals("0"))) {
                        // Reset du paiement au cas ou la création du paiement a
                        // déjà été faite
                        aFact.setCD_PAIEMENT(0);
                        aPaiement = new PaiementBean(mySalon.getMessagesBundle());
                        reglements = new Vector<ReglementBean>();
                    }
                    if (StringUtils.isEmpty(CD_FACT) || (CD_FACT.equals("0"))) {
                        aFact.setCD_FACT(0);
                    }
                }

                // Applique la modif sur la ligne de saisie
                // TOUJOURS LE CAS
                request.setAttribute("CD_TYP_VENT_SELECT", tab_CD_TYP_VENT[nbPrest]);
                if (Action.equals("Rechargement+")) {
                    request.setAttribute("CD_MARQUE_SELECT", tab_CD_MARQUE[nbPrest]);
                    request.setAttribute("CD_CATEG_PREST_SELECT", tab_CD_CATEG_PREST[nbPrest]);
                    // Recherche la dernière prestation du client dans cette
                    // Marque / Catégorie
                    HistoPrestBean lastPrest = HistoPrestBean
                            .getLastHistoPrest(myDBSession, CD_CLI,
                                    tab_CD_MARQUE[nbPrest],
                                    tab_CD_CATEG_PREST[nbPrest], null);
                    if (lastPrest != null) {
                        request.setAttribute("CD_PREST_SELECT", Long.toString(lastPrest.getCD_PREST()));
                        request.setAttribute("COMM_SELECT", lastPrest.getCOMM());
                    }

                } else if (Action.equals("Rechargement++")) {
                    request.setAttribute("CD_MARQUE_SELECT",
                            tab_CD_MARQUE[nbPrest]);
                    request.setAttribute("CD_CATEG_PREST_SELECT",
                            tab_CD_CATEG_PREST[nbPrest]);
                    request.setAttribute("CD_PREST_SELECT",
                            tab_CD_PREST[nbPrest]);
                    // Recherche la dernière prestation du client dans cette
                    // Marque / Catégorie
                    HistoPrestBean lastPrest = HistoPrestBean
                            .getLastHistoPrest(myDBSession, CD_CLI, null, null, tab_CD_PREST[nbPrest]);
                    if (lastPrest != null) {
                        request.setAttribute("COMM_SELECT", lastPrest.getCOMM());
                    }
                } else if (Action.equals("Impression")) {
                	if (StringUtils.isEmpty(mySalon.getMessage("Erreur"))) {
	                    // Crée le Bean spécifique pour l'édition
	                    EditionFacture aEditFact = new EditionFacture();
	                    aEditFact.setMyPaiement(aPaiement);
	                    aEditFact.setReglements(reglements);
	                    aEditFact.setMyFact(aFact);
	                    Vector listeEdition = new Vector(1);
	                    listeEdition.add(aEditFact);
	                    request.setAttribute("listeEdition", listeEdition);
                	}
                	else {
                		allCorrect = false;
                	}
                }
            }
            // *****************************************************************************************
            else if ((Action.equals("Suppression"))
                    || (Action.equals("Suppression+"))) {
                // Suppression de la fiche ou Forçage de la suppression

                if (Action.equals("Suppression+")) {
                    // Vérification du mot de passe
                    ParamBean paramMDP = ParamBean.getParamBean(myDBSession,
                            Integer.toString(ParamBean.CD_OP_EXCEPTIONNEL));

                    if (!paramMDP.getVAL_PARAM().equals(MOT_PASSE)) {
                        // Mot de passe incorrect
                        Action = "Suppression";
                        mySalon.setMessage("Erreur", BasicSession.TAG_I18N + "ficFact.motDePasseKo" + BasicSession.TAG_I18N);
                    }
                }

                /**
                 * Création du bean et enregistrement
                 */
                aFact = FactBean.getFactBean(myDBSession, CD_FACT, mySalon.getMessagesBundle());
                if (assertOrError((aFact != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
                	return;
                }

                // Initialisation par défaut
                aPaiement = new PaiementBean(mySalon.getMessagesBundle());
                reglements = new Vector<ReglementBean>();

                try {
                    // Vérification si paiement regroupé ou mouvements de stock
                    boolean supprimable = true;
                    Vector lstLignes = aFact.getLignes(myDBSession);

                    for (int i = 0; i < lstLignes.size(); i++) {
                        HistoPrestBean aHistoPrest = (HistoPrestBean) lstLignes.get(i);

                        if (aHistoPrest.hasMvtStk(myDBSession)) {
                            // Correspond à un mouvement : La facture n'est pas
                            // supprimable
                            supprimable = false;
                        }
                    }

                    if ((!Action.equals("Suppression+"))
                            && (aFact.getCD_PAIEMENT() != 0)) {
                        // Le Paiement a été fait : pas de droit de supprimer
                        // sauf si forçage
                        // Le forçage n'est possible que si le paiement n'est
                        // pas regroupé
                        aPaiement = PaiementBean.getPaiementBean(myDBSession,
                                CD_PAIEMENT, mySalon.getMessagesBundle());
                        reglements = aPaiement.getReglement(myDBSession);
                        if (supprimable
                                && (aPaiement.getFact(myDBSession).size() == 1)
                                && (mySalon.getMyIdent().getDroit("Facture", "Forçage"))) {
                            forcage = true;
                        } else {
                            throw new FctlException(
                                    BasicSession.TAG_I18N + "ficFact.factureReglee" + BasicSession.TAG_I18N);
                        }
                    } else {

                        if (Action.equals("Suppression+")) {

                            myDBSession.setDansTransactions(true);

                            // La facture d'abord pour éviter la contrainte de
                            // clé étrangère
                            aFact.delete(myDBSession);

                            aPaiement = PaiementBean.getPaiementBean(
                                    myDBSession, CD_PAIEMENT, mySalon.getMessagesBundle());
                            aPaiement.deletePur(myDBSession);

                            myDBSession.endTransaction();
                        } else {

                            // Suppression effective de la facture
                            aFact.delete(myDBSession);

                            // Suppression d'une facture : Suppression de la
                            // liste des encours
                            mySalon.removeClient(Long.toString(aFact
                                    .getCD_FACT()));
                        }

                        mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.suppressionOk" + BasicSession.TAG_I18N);

                        // Un bean vide
                        aFact = new FactBean(mySalon.getMessagesBundle());
                        // Place le code client si connu
                        aFact.setCD_CLI(CD_CLI);
                        aFact.setFACT_HISTO("N");

                        aPaiement = new PaiementBean(mySalon.getMessagesBundle());
                        reglements = new Vector<ReglementBean>();

                        request.setAttribute("Action", "Creation");
                    }
                } catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                    if (Action.equals("Suppression+")) {
                        request.setAttribute("Action", "Suppression");
                        forcage = true;
                    } else {
                        request.setAttribute("Action", "Modification");
                    }
                }
            }
            // *****************************************************************************************
            else {
                log.error("Action non codée : " + Action);
            }

            /**
             * Reset de la transaction pour la recherche des informations
             * complémentaires
             */
            myDBSession.cleanTransaction();

            // *****************************************************************************************
            //Récupère les collaborateurs pour mettre a jour la liste
            boolean filtrePresent = ((Action != null) && ((Action
                    .equals("Modification"))
                    || (Action.equals("AjoutLigne"))
                    || (Action.equals("SuppressionLigne"))
                    || (Action.equals("Rechargement"))
                    || (Action.equals("Rechargement+")) || (Action
                    .equals("Rechargement++"))))
                    && ((aFact != null) && (aFact.getFACT_HISTO().equals("N")));

            collabs = CollabBean.getListeCollab(myDBSession, filtrePresent);
            //IL faut egalement ajouter le collaborateur de la facture
            //Seulement si la liste ne contient pas CD_COLLAB
            CollabBean aCollab = CollabBean.getCollabBean(myDBSession, ""
                    + aFact.getCD_COLLAB());
            CollabBean.verifieEtAjoute(aCollab, collabs);

            //Il faut faire de meme avec les collaborateurs des différentes
            // factures
            Vector lignes = aFact.getLignes(myDBSession);
            int i = 1;
            for (i = 0; i < lignes.size(); i++) {
                HistoPrestBean aPrest = (HistoPrestBean) lignes.elementAt(i);
                CollabBean.verifieEtAjoute(aPrest.getCD_COLLAB(), collabs,
                        myDBSession);
            }

        } catch (Exception e) {
            mySalon.setMessage("Erreur", e.toString());
            log.error("Erreur générale : ", e);
        }

        Map<Integer, ReglementBean> mapCD_MOD_REGL = new HashMap<Integer, ReglementBean>();
        for (ReglementBean aReglement : reglements) {
			mapCD_MOD_REGL.put(aReglement.getCD_MOD_REGL(), aReglement);
		}
        // Positionne les attributs
        request.setAttribute("FactBean", aFact);
        request.setAttribute("PaiementBean", aPaiement);
    	request.setAttribute("Reglements", reglements);
    	request.setAttribute("mapCD_MOD_REGL", mapCD_MOD_REGL);
        request.setAttribute("collabs", collabs);

        if (aFact != null) {
            // Donne le total des prestations
            request.setAttribute("totPrest", aFact.getTotPrest(myDBSession)
                    .toString());
            // Donne le nombre de prestations
            request.setAttribute("NbPrest", Integer.toString(aFact.getLignes(
                    myDBSession).size()));
        } else {
            // Donne le total des prestations
            request.setAttribute("totPrest", totPrest);
            // Donne le nombre de prestations
            request.setAttribute("NbPrest", NbPrest);
        }

        try {
            if (allCorrect) {
                if (forcage) {
                    // Passe la main à la fiche de mise en garde
                    getServletConfig().getServletContext()
                            .getRequestDispatcher("/ficFactMEG.jsp").forward(
                                    request, response);
                } else if ((Action != null) && (Action.equals("Suppression+"))) {
                    // Passe la main à une fiche rechargeant la fiche facture
                    // complètement (pied compris)
                    getServletConfig().getServletContext()
                            .getRequestDispatcher("/ficFactRedirect.jsp")
                            .forward(request, response);
                } else if ((Action != null) && (Action.equals("Impression"))) {
                    // Passe la main à la fiche d'impression
                    getServletConfig().getServletContext()
                            .getRequestDispatcher("/ficFactImpr.jsp").forward(
                                    request, response);
                } else {
                    // Passe la main à la fiche de création
                    getServletConfig().getServletContext()
                            .getRequestDispatcher("/ficFact.jsp").forward(
                                    request, response);
                }
            } else {
                getServletConfig().getServletContext().getRequestDispatcher(
                        "/Erreur.jsp").forward(request, response);
            }

        } catch (Exception e) {
            log.error("Erreur à la redirection : ", e);
        }
    }

}