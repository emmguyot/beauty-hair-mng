/*
 * Fiche de cr�ation / modification d'un client
 * Copyright (C) 2001-2011 Emmanuel Guyot <See emmguyot on SourceForge>
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
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.increg.commun.BasicSession;
import com.increg.commun.DBSession;
import com.increg.salon.bean.ClientBean;
import com.increg.salon.bean.FactBean;
import com.increg.salon.bean.HistoPrestBean;
import com.increg.salon.bean.ISalonListeReset;
import com.increg.salon.bean.PrestBean;
import com.increg.salon.bean.SalonSession;

/**
 * Cr�ation d'un client
 * Creation date: (20/07/2001 20:12:04)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class FicCli extends ConnectedServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5399520320417324590L;
	
	protected Log log = LogFactory.getLog(this.getClass());
	
	/**
     * @see com.increg.salon.servlet.ConnectedServlet
     */
    public void performTask(HttpServletRequest request, HttpServletResponse response) {

        // R�cup�ration des param�tres
        String Action = request.getParameter("Action");
        String NbPrest = request.getParameter("NbPrest");
        String CD_CLI = request.getParameter("CD_CLI");
        String CIVILITE = request.getParameter("CIVILITE");
        String NOM = request.getParameter("NOM");
        String PRENOM = request.getParameter("PRENOM");
        String DT_ANNIV = request.getParameter("DT_ANNIV");
        String RUE = request.getParameter("RUE");
        String CD_POSTAL = request.getParameter("CD_POSTAL");
        String VILLE = request.getParameter("VILLE");
        String TEL = request.getParameter("TEL");
        String PORTABLE = request.getParameter("PORTABLE");
        String EMAIL = request.getParameter("EMAIL");
        String CD_TYP_CHEV = request.getParameter("CD_TYP_CHEV");
        String CD_TYP_PEAU = request.getParameter("CD_TYP_PEAU");
        String CD_TR_AGE = request.getParameter("CD_TR_AGE");
        String CD_ORIG = request.getParameter("CD_ORIG");
        String CD_CATEG_CLI = request.getParameter("CD_CATEG_CLI");
        String COMM = request.getParameter("COMM");
        String INDIC_VALID = request.getParameter("INDIC_VALID");
        // Filtrage des prestations
        String CD_TYP_VENT = request.getParameter("CD_TYP_VENT");

        // Valeurs par d�faut
        if (NbPrest == null) {
            NbPrest = "10";
            request.setAttribute("NbPrest", NbPrest);
        }

        // R�cup�re la connexion
        HttpSession mySession = request.getSession(false);
        final SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
        DBSession myDBSession = mySalon.getMyDBSession();
        ClientBean aCli = null;
        
        try {
            if (Action == null) {
                // Premi�re phase de cr�ation
                request.setAttribute("Action", "Creation");
                // Un bean vide
                aCli = new ClientBean(mySalon.getMessagesBundle());
                request.setAttribute("ClientBean", aCli);
            } else if (Action.equals("Creation")) {
                // Cr�e r�ellement le client

                /**
                 * Cr�ation du bean et enregistrement
                 */
                aCli = new ClientBean(mySalon.getMessagesBundle());
                aCli.setCD_CLI(CD_CLI);
                aCli.setCIVILITE(CIVILITE);
                aCli.setNOM(NOM);
                aCli.setPRENOM(PRENOM);
                aCli.setRUE(RUE);
                aCli.setCD_POSTAL(CD_POSTAL);
                aCli.setVILLE(VILLE);
                aCli.setTEL(TEL);
                aCli.setPORTABLE(PORTABLE);
                aCli.setEMAIL(EMAIL);
                aCli.setCD_TYP_CHEV(CD_TYP_CHEV);
                aCli.setCD_TYP_PEAU(CD_TYP_PEAU);
                aCli.setCD_TR_AGE(CD_TR_AGE);
                aCli.setCD_ORIG(CD_ORIG);
                aCli.setCD_CATEG_CLI(CD_CATEG_CLI);
                aCli.setCOMM(COMM);
                aCli.setINDIC_VALID(INDIC_VALID);

                try {
                    aCli.setDT_ANNIV(DT_ANNIV, mySalon.getLangue());
                    aCli.create(myDBSession);
                    mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.creationOk" + BasicSession.TAG_I18N);

                    // Recherche existance doublons
                    List<ClientBean> lstClient = ClientBean.getDoubleClientBeans(myDBSession, aCli, mySalon.getMessagesBundle());
                    if (lstClient.size() > 1) {
                        mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.existanceDoublon" + BasicSession.TAG_I18N);
                    }
                    
                    CD_CLI = Long.toString(aCli.getCD_CLI());
                    
                    request.setAttribute("Action", "Modification");
                } catch (Exception e) {
                    mySalon.setMessage("Erreur", e);
                    log.error("Erreur � la cr�ation du client", e);
                    request.setAttribute("Action", Action);
                }
                request.setAttribute("ClientBean", aCli);

            } else if ((Action.equals("Modification")) && (NOM == null)) {
                // Affichage de la fiche en modification
                request.setAttribute("Action", "Modification");

                aCli = ClientBean.getClientBean(myDBSession, CD_CLI, mySalon.getMessagesBundle());
                if (assertOrError((aCli != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
                	return;
                }
                request.setAttribute("ClientBean", aCli);

            } else if (Action.equals("Modification")) {
                // Modification effective de la fiche

                /**
                 * Cr�ation du bean et enregistrement
                 */
                aCli = ClientBean.getClientBean(myDBSession, CD_CLI, mySalon.getMessagesBundle());
                if (assertOrError((aCli != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
                	return;
                }

                aCli.setCD_CLI(CD_CLI);
                aCli.setCIVILITE(CIVILITE);
                aCli.setNOM(NOM);
                aCli.setPRENOM(PRENOM);
                aCli.setRUE(RUE);
                aCli.setCD_POSTAL(CD_POSTAL);
                aCli.setVILLE(VILLE);
                aCli.setTEL(TEL);
                aCli.setPORTABLE(PORTABLE);
                aCli.setEMAIL(EMAIL);
                aCli.setCD_TYP_CHEV(CD_TYP_CHEV);
                aCli.setCD_TYP_PEAU(CD_TYP_PEAU);
                aCli.setCD_TR_AGE(CD_TR_AGE);
                aCli.setCD_ORIG(CD_ORIG);
                aCli.setCD_CATEG_CLI(CD_CATEG_CLI);
                aCli.setCOMM(COMM);
                aCli.setINDIC_VALID(INDIC_VALID);

                try {
                    aCli.setDT_ANNIV(DT_ANNIV, mySalon.getLangue());
                    aCli.maj(myDBSession);
                    mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.enregistrementOk" + BasicSession.TAG_I18N);
                    request.setAttribute("Action", "Modification");
                } catch (Exception e) {
                    mySalon.setMessage("Erreur", e);
                    log.error("Erreur � la mise � jour du client", e);
                    request.setAttribute("Action", Action);
                }
                request.setAttribute("ClientBean", aCli);
            } else if (Action.equals("Suppression")) {
                // Modification effective de la fiche

                /**
                 * Cr�ation du bean et enregistrement
                 */
                aCli = ClientBean.getClientBean(myDBSession, CD_CLI, mySalon.getMessagesBundle());
                if (assertOrError((aCli != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
                	return;
                }

                try {
                    aCli.delete(myDBSession);
                    mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.suppressionOk" + BasicSession.TAG_I18N);
                    // Un bean vide
                    aCli = new ClientBean(mySalon.getMessagesBundle());
                    request.setAttribute("Action", "Creation");
                } catch (Exception e) {
                    mySalon.setMessage("Erreur", e);
                    log.error("Erreur � la suppression du client", e);
                    request.setAttribute("Action", "Modification");
                }
                request.setAttribute("ClientBean", aCli);
            } else if (Action.equals("Complet")) {
                // Affichage de la fiche en modification
                request.setAttribute("Action", "Modification");

                aCli = ClientBean.getClientBean(myDBSession, CD_CLI, mySalon.getMessagesBundle());
                request.setAttribute("ClientBean", aCli);
                NbPrest = Long.toString(Long.MAX_VALUE);
            } else if (Action.equals("Commentaire")) {
                // Modification d'un commentaire
                String CD_FACT = request.getParameter("CD_FACT");
                String NUM_LIG_FACT = request.getParameter("NUM_LIG_FACT");
                String COMM_FACT = request.getParameter("COMM");

                HistoPrestBean aHistoPrest = HistoPrestBean.getHistoPrestBean(myDBSession, CD_FACT, NUM_LIG_FACT);

                aHistoPrest.setCOMM(COMM_FACT);
                // R�cup�re le code client au passage
                CD_CLI = Long.toString(aHistoPrest.getCD_CLI());

                try {
                    aHistoPrest.maj(myDBSession);
                    mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.enregistrementOk" + BasicSession.TAG_I18N);
                } catch (Exception e) {
                    mySalon.setMessage("Erreur", e);
                    log.error("Erreur � la mise � jour de l'historique du client", e);
                }

                request.setAttribute("Action", "Modification");

                aCli = ClientBean.getClientBean(myDBSession, CD_CLI, mySalon.getMessagesBundle());
                request.setAttribute("ClientBean", aCli);
            } else {
                log.error("Action non cod�e : " + Action);
            }
        } catch (Exception e) {
            mySalon.setMessage("Erreur", e);
            log.error("Erreur g�n�rale", e);
        }

        /**
         * Reset de la transaction pour la recherche des informations compl�mentaires
         */
        myDBSession.cleanTransaction();

        /**
         * Recherche les prestations associ�es au client
         */
        Vector<HistoPrestBean> listePrest = new Vector<HistoPrestBean>();
        if ((CD_CLI != null) && (CD_CLI.length() > 0)) {
            String reqSQL = "select * from FACT where CD_CLI=" + CD_CLI + " order by DT_PREST desc";
            long nbPrest = Long.parseLong(NbPrest);
            long compteur = 0;

            // Interroge la Base
            try {
                ResultSet aRS = myDBSession.doRequest(reqSQL);

                while ((aRS.next()) && (compteur < nbPrest)) {
                    /**
                     * Cr�ation du bean de consultation
                     */
                    FactBean aFact = new FactBean(aRS, mySalon.getMessagesBundle());
                    Vector<HistoPrestBean> lignes = aFact.getLignes(myDBSession);

                    /**
                     * Boucle sur chaque ligne
                     */
                    for (int i = 0; (i < lignes.size()) && (compteur < nbPrest); i++) {
                        HistoPrestBean aHistoPrest = lignes.get(i);
                        boolean ajout = false;

                        if ((CD_TYP_VENT != null) && (CD_TYP_VENT.length() > 0)) {
                            // Filtrage sur Type de vente
                            PrestBean aPrest = PrestBean.getPrestBean(myDBSession, Long.toString(aHistoPrest.getCD_PREST()));
                            if (Integer.toString(aPrest.getCD_TYP_VENT()).equals(CD_TYP_VENT)) {
                                ajout = true;
                            }
                        } else {
                            ajout = true;
                        }

                        if (ajout) {
                            listePrest.add(aHistoPrest);
                            compteur++;
                        }
                    }
                }
                aRS.close();
            } catch (Exception e) {
                log.error("Erreur � la recherche des factures/prestations du client", e);
                try {
                    response.sendError(500);
                } catch (Exception e2) {
                    log.error("Erreur � la redirection sur erreur 500", e2);
                }
            }
            request.setAttribute("NbPrest", Long.toString(compteur));
        }

        request.setAttribute("listePrest", listePrest);
        ISalonListeReset resetter = new ISalonListeReset(){
            public void reset ()
            {
                mySalon.setListeClient(new Vector<ClientBean>());
            }
        };
        request.setAttribute("suivant", (aCli != null) ? suivantPossible(mySalon.getListeClient(), aCli.getCD_CLI(), resetter) : null);
        request.setAttribute("precedent", (aCli != null) ? precedentPossible(mySalon.getListeClient(), aCli.getCD_CLI(), resetter) : null);

        try {
            // Passe la main � la fiche de cr�ation
            getServletConfig().getServletContext().getRequestDispatcher("/ficCli.jsp").forward(request, response);

        } catch (Exception e) {
            log.error("Erreur � la redirection", e);
        }
    }
}
