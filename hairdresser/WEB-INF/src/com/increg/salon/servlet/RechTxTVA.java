package com.increg.salon.servlet;

import java.sql.ResultSet;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import com.increg.commun.DBSession;
import com.increg.salon.bean.SalonSession;
import com.increg.salon.bean.TvaBean;

/**
 * Liste des donn�es de r�f�rence
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

        // Constitue la requete SQL
        String reqSQL = "select * from TVA order by LIB_TVA";

        // R�cup�re la connexion
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
            System.out.println("Erreur dans performTask : " + e.toString());
            try {
                response.sendError(500);
            } catch (Exception e2) {
                System.out.println("Erreur sur sendError : " + e2.toString());
            }
        }
    }
}