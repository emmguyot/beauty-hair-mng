package com.increg.salon.servlet;

import java.sql.ResultSet;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.increg.commun.DBSession;
import com.increg.salon.bean.ClientBean;
import com.increg.salon.bean.FactBean;
import com.increg.salon.bean.HistoPrestBean;
import com.increg.salon.bean.PrestBean;
import com.increg.salon.bean.SalonSession;

/**
 * Création d'un client
 * Creation date: (20/07/2001 20:12:04)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class FicCli extends ConnectedServlet {
    /**
     * @see com.increg.salon.servlet.ConnectedServlet
     */
    public void performTask(HttpServletRequest request, HttpServletResponse response) {

        // Récupération des paramètres
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

        // Valeurs par défaut
        if (NbPrest == null) {
            NbPrest = "10";
            request.setAttribute("NbPrest", NbPrest);
        }

        // Récupère la connexion
        HttpSession mySession = request.getSession(false);
        SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
        DBSession myDBSession = mySalon.getMyDBSession();

        try {
            if (Action == null) {
                // Première phase de création
                request.setAttribute("Action", "Creation");
                // Un bean vide
                ClientBean aCli = new ClientBean();
                request.setAttribute("ClientBean", aCli);
            } else if (Action.equals("Creation")) {
                // Crée réellement le client

                /**
                 * Création du bean et enregistrement
                 */
                ClientBean aCli = new ClientBean();
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
                    aCli.setDT_ANNIV(DT_ANNIV);
                    aCli.create(myDBSession);
                    mySalon.setMessage("Info", "Création effectuée.");
                    request.setAttribute("Action", "Modification");
                } catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                    request.setAttribute("Action", Action);
                }
                request.setAttribute("ClientBean", aCli);

            } else if ((Action.equals("Modification")) && (NOM == null)) {
                // Affichage de la fiche en modification
                request.setAttribute("Action", "Modification");

                ClientBean aCli = ClientBean.getClientBean(myDBSession, CD_CLI);
                request.setAttribute("ClientBean", aCli);

            } else if (Action.equals("Modification")) {
                // Modification effective de la fiche

                /**
                 * Création du bean et enregistrement
                 */
                ClientBean aCli = ClientBean.getClientBean(myDBSession, CD_CLI);

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
                    aCli.setDT_ANNIV(DT_ANNIV);
                    aCli.maj(myDBSession);
                    mySalon.setMessage("Info", "Enregistrement effectué.");
                    request.setAttribute("Action", "Modification");
                } catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                    request.setAttribute("Action", Action);
                }
                request.setAttribute("ClientBean", aCli);
            } else if (Action.equals("Suppression")) {
                // Modification effective de la fiche

                /**
                 * Création du bean et enregistrement
                 */
                ClientBean aCli = ClientBean.getClientBean(myDBSession, CD_CLI);

                try {
                    aCli.delete(myDBSession);
                    mySalon.setMessage("Info", "Suppression effectuée.");
                    // Un bean vide
                    aCli = new ClientBean();
                    request.setAttribute("Action", "Creation");
                } catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                    request.setAttribute("Action", "Modification");
                }
                request.setAttribute("ClientBean", aCli);
            } else if (Action.equals("Complet")) {
                // Affichage de la fiche en modification
                request.setAttribute("Action", "Modification");

                ClientBean aCli = ClientBean.getClientBean(myDBSession, CD_CLI);
                request.setAttribute("ClientBean", aCli);
                NbPrest = Long.toString(Long.MAX_VALUE);
            } else if (Action.equals("Commentaire")) {
                // Modification d'un commentaire
                String CD_FACT = request.getParameter("CD_FACT");
                String NUM_LIG_FACT = request.getParameter("NUM_LIG_FACT");
                String COMM_FACT = request.getParameter("COMM");

                HistoPrestBean aHistoPrest = HistoPrestBean.getHistoPrestBean(myDBSession, CD_FACT, NUM_LIG_FACT);

                aHistoPrest.setCOMM(COMM_FACT);
                // Récupère le code client au passage
                CD_CLI = Long.toString(aHistoPrest.getCD_CLI());

                try {
                    aHistoPrest.maj(myDBSession);
                    mySalon.setMessage("Info", "Enregistrement effectué.");
                } catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                }

                request.setAttribute("Action", "Modification");

                ClientBean aCli = ClientBean.getClientBean(myDBSession, CD_CLI);
                request.setAttribute("ClientBean", aCli);
            } else {
                System.out.println("Action non codée : " + Action);
            }
        } catch (Exception e) {
            mySalon.setMessage("Erreur", e.toString());
            System.out.println("Note : " + e.toString());
        }

        /**
         * Reset de la transaction pour la recherche des informations complémentaires
         */
        myDBSession.cleanTransaction();

        /**
         * Recherche les prestations associées au client
         */
        Vector listePrest = new Vector();
        if ((CD_CLI != null) && (CD_CLI.length() > 0)) {
            String reqSQL = "select * from FACT where CD_CLI=" + CD_CLI + " order by DT_PREST desc";
            long nbPrest = Long.parseLong(NbPrest);
            long compteur = 0;

            // Interroge la Base
            try {
                ResultSet aRS = myDBSession.doRequest(reqSQL);

                while ((aRS.next()) && (compteur < nbPrest)) {
                    /**
                     * Création du bean de consultation
                     */
                    FactBean aFact = new FactBean(aRS);
                    Vector lignes = aFact.getLignes(myDBSession);

                    /**
                     * Boucle sur chaque ligne
                     */
                    for (int i = 0; (i < lignes.size()) && (compteur < nbPrest); i++) {
                        HistoPrestBean aHistoPrest = (HistoPrestBean) lignes.get(i);
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
                System.out.println("Erreur dans requète sur clé : " + e.toString());
                try {
                    response.sendError(500);
                } catch (Exception e2) {
                    System.out.println("Erreur sur sendError : " + e2.toString());
                }
            }
            request.setAttribute("NbPrest", Long.toString(compteur));
        }

        request.setAttribute("listePrest", listePrest);

        try {
            // Passe la main à la fiche de création
            getServletConfig().getServletContext().getRequestDispatcher("/ficCli.jsp").forward(request, response);

        } catch (Exception e) {
            System.out.println("FicCli::performTask : Erreur à la redirection : " + e.toString());
        }
    }
}
