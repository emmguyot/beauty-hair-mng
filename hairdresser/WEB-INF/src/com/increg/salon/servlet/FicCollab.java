package com.increg.salon.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.increg.commun.DBSession;
import com.increg.salon.bean.CollabBean;
import com.increg.salon.bean.SalonSession;

/**
 * Création d'un collaborateur
 * Creation date: (07/09/2001 13:38:32)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class FicCollab extends ConnectedServlet {
    /**
     * @see com.increg.salon.servlet.ConnectedServlet
     */
    public void performTask(HttpServletRequest request, HttpServletResponse response) {

        // Récupération des paramètres
        String Action = request.getParameter("Action");
        String CD_COLLAB = request.getParameter("CD_COLLAB");
        String CIVILITE = request.getParameter("CIVILITE");
        String NOM = request.getParameter("NOM");
        String PRENOM = request.getParameter("PRENOM");
        String RUE = request.getParameter("RUE");
        String VILLE = request.getParameter("VILLE");
        String CD_POSTAL = request.getParameter("CD_POSTAL");
        String TEL = request.getParameter("TEL");
        String PORTABLE = request.getParameter("PORTABLE");
        String EMAIL = request.getParameter("EMAIL");
        String DT_NAIS = request.getParameter("DT_NAIS");
        String NUM_SECU = request.getParameter("NUM_SECU");
        String CD_FCT = request.getParameter("CD_FCT");
        String CD_TYP_CONTR = request.getParameter("CD_TYP_CONTR");
        String CATEG = request.getParameter("CATEG");
        String ECHELON = request.getParameter("ECHELON");
        String COEF = request.getParameter("COEF");
        String QUOTA_HEURE = request.getParameter("QUOTA_HEURE");
        String INDIC_VALID = request.getParameter("INDIC_VALID");

        // Récupère la connexion
        HttpSession mySession = request.getSession(false);
        SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
        DBSession myDBSession = mySalon.getMyDBSession();

        CollabBean aCollab = null;

        try {
            if (Action == null) {
                // Première phase de création
                request.setAttribute("Action", "Creation");
                // Un bean vide
                aCollab = new CollabBean();
            }
            else if (Action.equals("Creation")) {
                // Crée réellement la prestation

                /**
                 * Création du bean et enregistrement
                 */
                aCollab = new CollabBean();

                try {
                    aCollab.setCD_COLLAB(CD_COLLAB);
                    aCollab.setNOM(NOM);
                    aCollab.setCIVILITE(CIVILITE);
                    aCollab.setPRENOM(PRENOM);
                    aCollab.setRUE(RUE);
                    aCollab.setVILLE(VILLE);
                    aCollab.setCD_POSTAL(CD_POSTAL);
                    aCollab.setTEL(TEL);
                    aCollab.setPORTABLE(PORTABLE);
                    aCollab.setEMAIL(EMAIL);
                    aCollab.setDT_NAIS(DT_NAIS);
                    aCollab.setNUM_SECU(NUM_SECU);
                    aCollab.setCD_FCT(CD_FCT);
                    aCollab.setCD_TYP_CONTR(CD_TYP_CONTR);
                    aCollab.setCATEG(CATEG);
                    aCollab.setECHELON(ECHELON);
                    aCollab.setCOEF(COEF);
                    aCollab.setQUOTA_HEURE(QUOTA_HEURE);
                    aCollab.setINDIC_VALID(INDIC_VALID);

                    aCollab.create(myDBSession);

                    mySalon.setMessage("Info", "Création effectuée.");
                    request.setAttribute("Action", "Modification");
                }
                catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                    request.setAttribute("Action", Action);
                }
            }
            else if ((Action.equals("Modification")) && (NOM == null)) {
                // Affichage de la fiche en modification
                request.setAttribute("Action", "Modification");

                aCollab = CollabBean.getCollabBean(myDBSession, CD_COLLAB);
                request.setAttribute("CollabBean", aCollab);

            }
            else if (Action.equals("Modification")) {
                // Modification effective de la fiche

                /**
                 * Création du bean et enregistrement
                 */
                aCollab = CollabBean.getCollabBean(myDBSession, CD_COLLAB);

                try {
                    aCollab.setCD_COLLAB(CD_COLLAB);
                    aCollab.setNOM(NOM);
                    aCollab.setCIVILITE(CIVILITE);
                    aCollab.setPRENOM(PRENOM);
                    aCollab.setRUE(RUE);
                    aCollab.setVILLE(VILLE);
                    aCollab.setCD_POSTAL(CD_POSTAL);
                    aCollab.setTEL(TEL);
                    aCollab.setPORTABLE(PORTABLE);
                    aCollab.setEMAIL(EMAIL);
                    aCollab.setDT_NAIS(DT_NAIS);
                    aCollab.setNUM_SECU(NUM_SECU);
                    aCollab.setCD_FCT(CD_FCT);
                    aCollab.setCD_TYP_CONTR(CD_TYP_CONTR);
                    aCollab.setCATEG(CATEG);
                    aCollab.setECHELON(ECHELON);
                    aCollab.setCOEF(COEF);
                    aCollab.setQUOTA_HEURE(QUOTA_HEURE);
                    aCollab.setINDIC_VALID(INDIC_VALID);

                    aCollab.maj(myDBSession);
                    mySalon.setMessage("Info", "Enregistrement effectué.");
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
                aCollab = CollabBean.getCollabBean(myDBSession, CD_COLLAB);

                try {
                    aCollab.delete(myDBSession);
                    mySalon.setMessage("Info", "Suppression effectuée.");
                    // Un bean vide
                    aCollab = new CollabBean();
                    request.setAttribute("Action", "Creation");
                }
                catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                    request.setAttribute("Action", "Modification");
                }
            }
            else {
                System.out.println("Action non codée : " + Action);
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

        request.setAttribute("CollabBean", aCollab);

        try {
            // Passe la main à la fiche de création
            getServletConfig().getServletContext().getRequestDispatcher("/ficCollab.jsp").forward(request, response);

        }
        catch (Exception e) {
            System.out.println("FicCollab::performTask : Erreur à la redirection : " + e.toString());
        }
    }
}
