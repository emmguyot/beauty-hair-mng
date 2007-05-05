package com.increg.salon.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.increg.commun.BasicSession;
import com.increg.commun.DBSession;
import com.increg.salon.bean.SalonSession;
import com.increg.salon.bean.TypVentBean;

/**
 * Gestion des type de ventes Creation date: 18 janv. 2003
 * 
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class FicTypVent extends ConnectedServlet {
    /**
     * @see com.increg.salon.servlet.ConnectedServlet
     */
    public void performTask(HttpServletRequest request,
            HttpServletResponse response) {

    	Log log = LogFactory.getLog(this.getClass());

        // R�cup�ration des param�tres
        String Action = request.getParameter("Action");
        String CD_TYP_VENT = request.getParameter("CD_TYP_VENT");
        String LIB_TYP_VENT = request.getParameter("LIB_TYP_VENT");
        String[] tCIVILITE = request.getParameterValues("CIVILITE");
        String CIVILITE = new String();
        if (tCIVILITE != null) {
            for (int i = 0; i < tCIVILITE.length; i++) {
                if (CIVILITE.length() > 0) {
                    CIVILITE += "|";
                }
                CIVILITE += tCIVILITE[i];
            }
        }

        String MARQUE = request.getParameter("MARQUE");
        String CD_TVA = request.getParameter("CD_TVA");

        // R�cup�re la connexion
        HttpSession mySession = request.getSession(false);
        SalonSession mySalon = (SalonSession) mySession
                .getAttribute("SalonSession");
        DBSession myDBSession = mySalon.getMyDBSession();

        TypVentBean aTypVent = null;

        try {
            if (Action == null) {
                // Premi�re phase de cr�ation
                request.setAttribute("Action", "Creation");
                // Un bean vide
                aTypVent = new TypVentBean();
            } else if (Action.equals("Creation")) {
                // Modification effective de la fiche

                /**
                 * Cr�ation du bean et enregistrement
                 */
                aTypVent = new TypVentBean();

                try {
                    aTypVent.setCD_TYP_VENT(CD_TYP_VENT);
                    aTypVent.setLIB_TYP_VENT(LIB_TYP_VENT);
                    aTypVent.setCIVILITE(CIVILITE);
                    aTypVent.setMARQUE(MARQUE);
                    aTypVent.setCD_TVA(CD_TVA);

                    aTypVent.create(myDBSession);
                    mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.creationOk" + BasicSession.TAG_I18N);
                    request.setAttribute("Action", "Modification");
                } catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                    request.setAttribute("Action", Action);
                }
            } else if ((Action.equals("Modification"))
                    && (LIB_TYP_VENT == null)) {
                // Affichage de la fiche en modification
                request.setAttribute("Action", "Modification");

                aTypVent = TypVentBean.getTypVentBean(myDBSession, CD_TYP_VENT);
                if (assertOrError((aTypVent != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
                	return;
                }
            } else if (Action.equals("Modification")) {
                // Modification effective de la fiche

                /**
                 * Cr�ation du bean et enregistrement
                 */
                aTypVent = TypVentBean.getTypVentBean(myDBSession, CD_TYP_VENT);
                if (assertOrError((aTypVent != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
                	return;
                }

                aTypVent.setCD_TYP_VENT(CD_TYP_VENT);
                aTypVent.setLIB_TYP_VENT(LIB_TYP_VENT);
                aTypVent.setCIVILITE(CIVILITE);
                aTypVent.setMARQUE(MARQUE);

                try {
                    aTypVent.setCD_TVA(CD_TVA);
                    aTypVent.maj(myDBSession);
                    mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.enregistrementOk" + BasicSession.TAG_I18N);
                } catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                }
                request.setAttribute("Action", Action);

            } else if (Action.equals("Suppression")) {
                // Modification effective de la fiche

                /**
                 * Cr�ation du bean et enregistrement
                 */
                aTypVent = TypVentBean.getTypVentBean(myDBSession, CD_TYP_VENT);
                if (assertOrError((aTypVent != null), BasicSession.TAG_I18N + "message.notFound" + BasicSession.TAG_I18N, request, response)) {
                	return;
                }

                try {
                    aTypVent.delete(myDBSession);
                    mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.suppressionOk" + BasicSession.TAG_I18N);
                    // Un bean vide
                    aTypVent = new TypVentBean();
                    request.setAttribute("Action", "Creation");
                } catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                }
                request.setAttribute("Action", Action);

            } else if (Action.equals("Duplication")) {
                // Duplique la prestation

                /**
                 * Cr�ation du bean et enregistrement
                 */
                aTypVent = new TypVentBean();

                try {
                    aTypVent.setLIB_TYP_VENT(LIB_TYP_VENT);
                    aTypVent.setCIVILITE(CIVILITE);
                    aTypVent.setMARQUE(MARQUE);
                    aTypVent.setCD_TVA(CD_TVA);

                    aTypVent.create(myDBSession);
                    mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.duplicationOk" + BasicSession.TAG_I18N);
                    request.setAttribute("Action", "Modification");
                } catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                    request.setAttribute("Action", Action);
                }
            } else {
                log.error("Action non cod�e : " + Action);
            }
        } catch (Exception e) {
            mySalon.setMessage("Erreur", e.toString());
            log.error("Erreur g�n�rale : ", e);
        }

        /**
         * Reset de la transaction pour la recherche des informations
         * compl�mentaires
         */
        myDBSession.cleanTransaction();

        request.setAttribute("TypVentBean", aTypVent);

        try {
            // Passe la main � la fiche de cr�ation
            getServletConfig().getServletContext().getRequestDispatcher(
                    "/ficTypVent.jsp").forward(request, response);

        } catch (Exception e) {
            log.error("Erreur � la redirection : ", e);
        }
    }
}
