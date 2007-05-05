package com.increg.salon.servlet;

import java.sql.ResultSet;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.increg.commun.DBSession;
import com.increg.salon.bean.SalonSession;
import com.increg.salon.bean.TvaBean;

/**
 * Liste des données de référence
 * Creation date: 26 janv. 2005
 * 
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class RechTxTVA extends ConnectedServlet {
    /**
     * @see com.increg.salon.servlet.ConnectedServlet
     */
    public void performTask(javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response) {

    	Log log = LogFactory.getLog(this.getClass());

        // Constitue la requete SQL
        String reqSQL = "select * from TVA order by LIB_TVA";

        // Récupère la connexion
        HttpSession mySession = request.getSession(false);
        DBSession myDBSession = ((SalonSession) mySession
                .getAttribute("SalonSession")).getMyDBSession();

        // Interroge la Base
        try {
            ResultSet aRS = myDBSession.doRequest(reqSQL);
            Vector lstLignes = new Vector();

            while (aRS.next()) {
                lstLignes.add(new TvaBean(aRS));
            }
            aRS.close();

            // Stocke le Vector pour le JSP
            request.setAttribute("Liste", lstLignes);

            // Passe la main
            getServletConfig().getServletContext().getRequestDispatcher(
                    "/lstTxTVA.jsp").forward(request, response);

        } catch (Exception e) {
            log.error("Erreur dans performTask : ", e);
            try {
                response.sendError(500);
            } catch (Exception e2) {
                log.error("Erreur sur sendError : ", e2);
            }
        }
    }
}
