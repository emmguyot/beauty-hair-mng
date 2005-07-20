package com.increg.salon.servlet;

import javax.servlet.http.HttpSession;

import com.increg.commun.BasicSession;
import com.increg.commun.DBSession;
import com.increg.salon.bean.PrestBean;
import com.increg.salon.bean.SalonSession;

/**
 * Création d'une prestation
 * Creation date: (20/07/2001 20:12:04)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class FicPrest extends ConnectedServlet {
    /**
     * @see com.increg.salon.servlet.ConnectedServlet
     */
    public void performTask(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) {

        // Récupération des paramètres
        String Action = request.getParameter("Action");
        String CD_PREST = request.getParameter("CD_PREST");
        String LIB_PREST = request.getParameter("LIB_PREST");
        String CD_TYP_VENT = request.getParameter("CD_TYP_VENT");
        String CD_CATEG_PREST = request.getParameter("CD_CATEG_PREST");
        String CD_MARQUE = request.getParameter("CD_MARQUE");
        String PRX_UNIT_TTC = request.getParameter("PRX_UNIT_TTC");
        String CD_UNIT_MES = request.getParameter("CD_UNIT_MES");
        String CD_ART = request.getParameter("CD_ART");
        String TPS_PREST = request.getParameter("TPS_PREST");
        String CPT_ABONNEMENT = request.getParameter("CPT_ABONNEMENT");
        String CD_PREST_ABONNEMENT = request.getParameter("CD_PREST_ABONNEMENT");
        String COMM = request.getParameter("COMM");
        String INDIC_PERIM = request.getParameter("INDIC_PERIM");
        String INDIC_ABONNEMENT = request.getParameter("INDIC_ABONNEMENT");

        // Récupère la connexion
        HttpSession mySession = request.getSession(false);
        SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
        DBSession myDBSession = mySalon.getMyDBSession();

        PrestBean aPrest = null;
        try {
            if (Action == null) {
                // Première phase de création
                request.setAttribute("Action", "Creation");
                // Un bean vide
                aPrest = new PrestBean();
            }
            else if (Action.equals("Creation")) {
                // Crée réellement la prestation

                /**
                 * Création du bean et enregistrement
                 */
                aPrest = new PrestBean();
                aPrest.setCD_PREST(CD_PREST);
                aPrest.setLIB_PREST(LIB_PREST);
                aPrest.setCD_TYP_VENT(CD_TYP_VENT);
                aPrest.setCD_CATEG_PREST(CD_CATEG_PREST);
                aPrest.setCD_MARQUE(CD_MARQUE);
                aPrest.setPRX_UNIT_TTC(PRX_UNIT_TTC);
                aPrest.setCD_UNIT_MES(CD_UNIT_MES);
                aPrest.setCD_ART(CD_ART);
                aPrest.setCOMM(COMM);
                aPrest.setINDIC_PERIM(INDIC_PERIM);
                aPrest.setINDIC_ABONNEMENT(INDIC_ABONNEMENT);

                try {
                    aPrest.setCPT_ABONNEMENT(CPT_ABONNEMENT);
                    aPrest.setCD_PREST_ABONNEMENT(CD_PREST_ABONNEMENT);
                    aPrest.setTPS_PREST(TPS_PREST);

                    aPrest.create(myDBSession);
                    mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.creationOk" + BasicSession.TAG_I18N);
                    request.setAttribute("Action", "Modification");
                }
                catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                    request.setAttribute("Action", Action);
                }
            }
            else if ((Action.equals("Modification")) && (LIB_PREST == null)) {
                // Affichage de la fiche en modification
                request.setAttribute("Action", "Modification");

                aPrest = PrestBean.getPrestBean(myDBSession, CD_PREST);
            }
            else if (Action.equals("Rechargement")) {
                // Rechargement : La prestation peut être ou pas déjà créée
                /**
                 * Création du bean et enregistrement
                 */
                if ((CD_PREST == null) || (CD_PREST.length() == 0)) {
                    aPrest = new PrestBean();
                }
                else {
                    aPrest = PrestBean.getPrestBean(myDBSession, CD_PREST);
                }

                aPrest.setCD_PREST(CD_PREST);
                aPrest.setLIB_PREST(LIB_PREST);
                aPrest.setCD_TYP_VENT(CD_TYP_VENT);
                aPrest.setCD_CATEG_PREST(CD_CATEG_PREST);
                aPrest.setCD_MARQUE(CD_MARQUE);
                aPrest.setPRX_UNIT_TTC(PRX_UNIT_TTC);
                aPrest.setCD_UNIT_MES(CD_UNIT_MES);
                aPrest.setCD_ART(CD_ART);
                aPrest.setCOMM(COMM);
                aPrest.setINDIC_PERIM(INDIC_PERIM);
                aPrest.setINDIC_ABONNEMENT(INDIC_ABONNEMENT);

                try {
                    aPrest.setCPT_ABONNEMENT(CPT_ABONNEMENT);
                    aPrest.setCD_PREST_ABONNEMENT(0); // Réinitialise la prestation fille pour une question de cohérence
                    aPrest.setTPS_PREST(TPS_PREST);

                    if ((CD_PREST == null) || (CD_PREST.length() == 0)) {
                        request.setAttribute("Action", "Creation");
                    }
                    else {
                        request.setAttribute("Action", "Modification");
                    }
                }
                catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                    request.setAttribute("Action", Action);
                }
            }
            else if (Action.equals("Modification")) {
                // Modification effective de la fiche

                /**
                 * Création du bean et enregistrement
                 */
                aPrest = PrestBean.getPrestBean(myDBSession, CD_PREST);

                aPrest.setCD_PREST(CD_PREST);
                aPrest.setLIB_PREST(LIB_PREST);
                aPrest.setCD_TYP_VENT(CD_TYP_VENT);
                aPrest.setCD_CATEG_PREST(CD_CATEG_PREST);
                aPrest.setCD_MARQUE(CD_MARQUE);
                aPrest.setPRX_UNIT_TTC(PRX_UNIT_TTC);
                aPrest.setCD_UNIT_MES(CD_UNIT_MES);
                aPrest.setCD_ART(CD_ART);
                aPrest.setCOMM(COMM);
                aPrest.setINDIC_PERIM(INDIC_PERIM);
                aPrest.setINDIC_ABONNEMENT(INDIC_ABONNEMENT);

                try {
                    aPrest.setCPT_ABONNEMENT(CPT_ABONNEMENT);
                    aPrest.setCD_PREST_ABONNEMENT(CD_PREST_ABONNEMENT);
                    aPrest.setTPS_PREST(TPS_PREST);

                    aPrest.maj(myDBSession);
                    mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.enregistrementOk" + BasicSession.TAG_I18N);
                    request.setAttribute("Action", "Modification");
                }
                catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                    request.setAttribute("Action", Action);
                }
            }
            else if (Action.equals("Duplication")) {
                // Duplication de la fiche

                /**
                 * Création du bean et enregistrement
                 */
                aPrest = new PrestBean();

                aPrest.setLIB_PREST(LIB_PREST);
                aPrest.setCD_TYP_VENT(CD_TYP_VENT);
                aPrest.setCD_CATEG_PREST(CD_CATEG_PREST);
                aPrest.setCD_MARQUE(CD_MARQUE);
                aPrest.setPRX_UNIT_TTC(PRX_UNIT_TTC);
                aPrest.setCD_UNIT_MES(CD_UNIT_MES);
                aPrest.setCD_ART(CD_ART);
                aPrest.setCOMM(COMM);
                aPrest.setINDIC_ABONNEMENT(INDIC_ABONNEMENT);

                try {
                    aPrest.setCPT_ABONNEMENT(CPT_ABONNEMENT);
                    aPrest.setCD_PREST_ABONNEMENT(CD_PREST_ABONNEMENT);
                    aPrest.setTPS_PREST(TPS_PREST);

                    aPrest.create(myDBSession);
                    mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.duplicationOk" + BasicSession.TAG_I18N);
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
                aPrest = PrestBean.getPrestBean(myDBSession, CD_PREST);

                try {
                    aPrest.delete(myDBSession);
                    mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.suppressionOk" + BasicSession.TAG_I18N);
                    // Un bean vide
                    aPrest = new PrestBean();
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

            request.setAttribute("PrestBean", aPrest);

        }
        catch (Exception e) {
            mySalon.setMessage("Erreur", e.toString());
            System.out.println("Note : " + e.toString());
        }

        /**
         * Reset de la transaction pour la recherche des informations complémentaires
         */
        myDBSession.cleanTransaction();

        try {
            // Passe la main à la fiche de création
            getServletConfig().getServletContext().getRequestDispatcher("/ficPrest.jsp").forward(request, response);

        }
        catch (Exception e) {
            System.out.println("FicCli::performTask : Erreur à la redirection : " + e.toString());
        }
    }
}
