package com.increg.salon.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.increg.commun.BasicSession;
import com.increg.commun.DBSession;
import com.increg.salon.bean.SalonSession;
import com.increg.salon.bean.SocieteBean;

/**
 * Création d'un fournisseur Creation date: (03/09/2001 21:57:30)
 * 
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class FicSoc extends ConnectedServlet {
    /**
     * @see com.increg.salon.servlet.ConnectedServlet
     */
    public void performTask(HttpServletRequest request,
            HttpServletResponse response) {

        // Récupération des paramètres
        String Action = request.getParameter("Action");
        String RAIS_SOC = request.getParameter("RAIS_SOC");
        String CIVILITE_GER = request.getParameter("CIVILITE_GER");
        String NOM_GER = request.getParameter("NOM_GER");
        String PRENOM_GER = request.getParameter("PRENOM_GER");
        String RUE = request.getParameter("RUE");
        String CD_POSTAL = request.getParameter("CD_POSTAL");
        String VILLE = request.getParameter("VILLE");
        String TEL = request.getParameter("TEL");
        String PORTABLE = request.getParameter("PORTABLE");
        String EMAIL = request.getParameter("EMAIL");
        String CD_SIRET = request.getParameter("CD_SIRET");
        String CD_APE = request.getParameter("CD_APE");
        String FLG_SALON = request.getParameter("FLG_SALON");
        String FLG_INSTITUT = request.getParameter("FLG_INSTITUT");

        // Récupère la connexion
        HttpSession mySession = request.getSession(false);
        SalonSession mySalon = (SalonSession) mySession
                .getAttribute("SalonSession");
        DBSession myDBSession = mySalon.getMyDBSession();

        SocieteBean aSoc = null;
        try {
            if (Action == null) {
                // Première phase de création
                request.setAttribute("Action", "Modification");
                // Un bean vide
                aSoc = mySalon.getMySociete();
            } else if ((Action.equals("Modification")) && (RAIS_SOC == null)) {
                // Affichage de la fiche en modification
                request.setAttribute("Action", "Modification");

                aSoc = mySalon.getMySociete();
            } else if (Action.equals("Modification")) {
                // Modification effective de la fiche

                /**
                 * Création du bean et enregistrement
                 */
                aSoc = new SocieteBean(myDBSession, "");

                aSoc.setRAIS_SOC(RAIS_SOC);
                aSoc.setCIVILITE_GER(CIVILITE_GER);
                aSoc.setNOM_GER(NOM_GER);
                aSoc.setPRENOM_GER(PRENOM_GER);
                aSoc.setRUE(RUE);
                aSoc.setCD_POSTAL(CD_POSTAL);
                aSoc.setVILLE(VILLE);
                aSoc.setTEL(TEL);
                aSoc.setPORTABLE(PORTABLE);
                aSoc.setEMAIL(EMAIL);
                aSoc.setCD_SIRET(CD_SIRET);
                aSoc.setCD_APE(CD_APE);
                aSoc.setFLG_SALON(FLG_SALON);
                aSoc.setFLG_INSTITUT(FLG_INSTITUT);

                try {
                    if (mySalon.checkLicence(aSoc)) {
                        aSoc.maj(myDBSession);
                        mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.enregistrementOk" + BasicSession.TAG_I18N);
                        // Maj du Bean session
                        mySalon.getMySociete().setRAIS_SOC(RAIS_SOC);
                        mySalon.getMySociete().setCIVILITE_GER(CIVILITE_GER);
                        mySalon.getMySociete().setNOM_GER(NOM_GER);
                        mySalon.getMySociete().setPRENOM_GER(PRENOM_GER);
                        mySalon.getMySociete().setRUE(RUE);
                        mySalon.getMySociete().setCD_POSTAL(CD_POSTAL);
                        mySalon.getMySociete().setVILLE(VILLE);
                        mySalon.getMySociete().setTEL(TEL);
                        mySalon.getMySociete().setPORTABLE(PORTABLE);
                        mySalon.getMySociete().setEMAIL(EMAIL);
                        mySalon.getMySociete().setCD_SIRET(CD_SIRET);
                        mySalon.getMySociete().setCD_APE(CD_APE);
                        mySalon.getMySociete().setFLG_SALON(FLG_SALON);
                        mySalon.getMySociete().setFLG_INSTITUT(FLG_INSTITUT);
                    } else {
                        mySalon
                                .setMessage(
                                        "Erreur",
                                        "Cette modification n'est pas autorisée car vous n'avez pas la licence correspondante.");
                    }
                } catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                }
                request.setAttribute("Action", "Modification");
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

        request.setAttribute("SocBean", aSoc);

        try {
            // Passe la main à la fiche de création
            getServletConfig().getServletContext().getRequestDispatcher(
                    "/ficSoc.jsp").forward(request, response);

        } catch (Exception e) {
            System.out
                    .println("FicSoc::performTask : Erreur à la redirection : "
                            + e.toString());
        }
    }
}