package com.increg.salon.servlet;

import java.sql.ResultSet;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.increg.commun.DBSession;
import com.increg.salon.bean.CollabBean;
import com.increg.salon.bean.SalonSession;

/**
 * Liste des collaborateurs
 * Creation date: (07/09/2001 13:49:45)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class RechCollab extends ConnectedServlet {
    /**
     * @see com.increg.salon.servlet.ConnectedServlet
     */
    public void performTask(HttpServletRequest request, HttpServletResponse response) {

        // Récupération du paramètre
        String INDIC_VALID = request.getParameter("INDIC_VALID");
        request.setAttribute("INDIC_VALID", INDIC_VALID);

        // Constitue la requete SQL
        String reqSQL = "select * from COLLAB";
        if ((INDIC_VALID == null) || (!INDIC_VALID.equals("on"))) {
            reqSQL = reqSQL + " where INDIC_VALID='O'";
        }
        reqSQL += " order by NOM, PRENOM";

        // Récupère la connexion
        HttpSession mySession = request.getSession(false);
        DBSession myDBSession = ((SalonSession) mySession.getAttribute("SalonSession")).getMyDBSession();

        // Interroge la Base
        try {
            ResultSet aRS = myDBSession.doRequest(reqSQL);
            Vector lstLignes = new Vector();

            while (aRS.next()) {
                lstLignes.add(new CollabBean(aRS));
            }
            aRS.close();

            // Stocke le Vector pour le JSP
            request.setAttribute("Liste", lstLignes);

            // Passe la main
            getServletConfig().getServletContext().getRequestDispatcher("/lstCollab.jsp").forward(request, response);

        }
        catch (Exception e) {
            System.out.println("Erreur dans performTask : " + e.toString());
            try {
                response.sendError(500);
            }
            catch (Exception e2) {
                System.out.println("Erreur sur sendError : " + e2.toString());
            }
        }
    }
}
