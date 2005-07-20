package com.increg.salon.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.increg.commun.BasicSession;
import com.increg.commun.DBSession;
import com.increg.salon.bean.SalonSession;
import com.increg.salon.bean.TvaBean;

/**
 * Gestion des taux de TVA Creation date: 18 janv. 2003
 * 
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class FicTxTVA extends ConnectedServlet {
    /**
     * @see com.increg.salon.servlet.ConnectedServlet
     */
    public void performTask(HttpServletRequest request, HttpServletResponse response) {

        // Récupération des paramètres
        String Action = request.getParameter("Action");
        String CD_TVA = request.getParameter("CD_TVA");
        String LIB_TVA = request.getParameter("LIB_TVA");
        String TX_TVA = request.getParameter("TX_TVA");

        // Récupère la connexion
        HttpSession mySession = request.getSession(false);
        SalonSession mySalon = (SalonSession) mySession
                .getAttribute("SalonSession");
        DBSession myDBSession = mySalon.getMyDBSession();

        TvaBean aTva = null;

        try {
            if (Action == null) {
                // Première phase de création
                request.setAttribute("Action", "Creation");
                // Un bean vide
                aTva = new TvaBean();
            } else if (Action.equals("Creation")) {
                // Modification effective de la fiche

                /**
                 * Création du bean et enregistrement
                 */
                aTva = new TvaBean();

                try {
                    aTva.setCD_TVA(CD_TVA);
                    aTva.setLIB_TVA(LIB_TVA);
                    aTva.setTX_TVA(TX_TVA);

                    aTva.create(myDBSession);
                    mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.creationOk" + BasicSession.TAG_I18N);
                    request.setAttribute("Action", "Modification");
                } catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                    request.setAttribute("Action", Action);
                }
            } else if ((Action.equals("Modification"))
                    && (LIB_TVA == null)) {
                // Affichage de la fiche en modification
                request.setAttribute("Action", "Modification");

                aTva = TvaBean.getTvaBean(myDBSession, CD_TVA);
            } else if (Action.equals("Modification")) {
                // Modification effective de la fiche

                /**
                 * Création du bean et enregistrement
                 */
                aTva = TvaBean.getTvaBean(myDBSession, CD_TVA);

                aTva.setCD_TVA(CD_TVA);
                aTva.setLIB_TVA(LIB_TVA);
                aTva.setTX_TVA(TX_TVA);

                try {
                    aTva.maj(myDBSession);
                    mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.enregistrementOk" + BasicSession.TAG_I18N);
                } catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                }
                request.setAttribute("Action", Action);

            } else if (Action.equals("Suppression")) {
                // Modification effective de la fiche

                /**
                 * Création du bean et enregistrement
                 */
                aTva = TvaBean.getTvaBean(myDBSession, CD_TVA);

                try {
                    aTva.delete(myDBSession);
                    mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.suppressionOk" + BasicSession.TAG_I18N);
                    // Un bean vide
                    aTva = new TvaBean();
                    request.setAttribute("Action", "Creation");
                } catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                }
                request.setAttribute("Action", Action);

            } else if (Action.equals("Duplication")) {
                // Duplique la prestation

                /**
                 * Création du bean et enregistrement
                 */
                aTva = new TvaBean();

                try {
                    aTva.setLIB_TVA(LIB_TVA);
                    aTva.setTX_TVA(TX_TVA);

                    aTva.create(myDBSession);
                    mySalon
                            .setMessage("Info",
                                    BasicSession.TAG_I18N + "message.duplicationOk" + BasicSession.TAG_I18N);
                    request.setAttribute("Action", "Modification");
                } catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                    request.setAttribute("Action", Action);
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

        request.setAttribute("TvaBean", aTva);

        try {
            // Passe la main à la fiche de création
            getServletConfig().getServletContext().getRequestDispatcher(
                    "/ficTxTVA.jsp").forward(request, response);

        } catch (Exception e) {
            System.out
                    .println("FicTxTva::performTask : Erreur à la redirection : "
                            + e.toString());
        }
    }
}
