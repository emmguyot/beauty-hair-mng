package com.increg.salon.servlet;

import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.increg.commun.DBSession;
import com.increg.salon.bean.FactBean;
import com.increg.salon.bean.SalonSession;

/**
 * Recherche/Liste des ventes
 * Creation date: (28/12/2001 22:31:45)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class RechVente extends ConnectedServlet {

    /**
     * @see com.increg.salon.servlet.ConnectedServlet
     */
    public void performTask(HttpServletRequest request, HttpServletResponse response) {

        // Récupération des paramètres
        String DT_DEBUT = request.getParameter("DT_DEBUT");
        String DT_FIN = request.getParameter("DT_FIN");

        DateFormat formatDate = DateFormat.getDateInstance(DateFormat.SHORT);

        // Récupère la connexion
        HttpSession mySession = request.getSession(false);
        SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
        DBSession myDBSession = mySalon.getMyDBSession();

        // Valeurs par défaut
        if (DT_DEBUT == null) {
            // Début de mois
            Calendar J7 = Calendar.getInstance();
            J7.add(Calendar.DAY_OF_YEAR, 1 - J7.get(Calendar.DAY_OF_MONTH));
            DT_DEBUT = formatDate.format(J7.getTime());
        }
        if (DT_FIN == null) {
            DT_FIN = formatDate.format(Calendar.getInstance().getTime());
        }
        try {
            if ((DT_DEBUT != null) && (DT_DEBUT.length() > 0)) {
                request.setAttribute("DT_DEBUT", formatDate.parse(DT_DEBUT));
            }
            if ((DT_FIN != null) && (DT_FIN.length() > 0)) {
                request.setAttribute("DT_FIN", formatDate.parse(DT_FIN));
            }
        }
        catch (ParseException e) {
            mySalon.setMessage("Erreur", e.toString());
            e.printStackTrace();
        }

        Vector lstLignes = new Vector();

        try {
            lstLignes = FactBean.calculVente(myDBSession, DT_DEBUT, DT_FIN);
        }
        catch (Exception e) {
            System.out.println("Erreur dans performTask : " + e.toString());
            try {
                response.sendError(HttpURLConnection.HTTP_INTERNAL_ERROR);
                return;
            }
            catch (Exception e2) {
                System.out.println("Erreur sur sendError : " + e2.toString());
            }
        }

        try {
            // Stocke le Vector pour le JSP
            request.setAttribute("Liste", lstLignes);

            // Passe la main
            getServletConfig().getServletContext().getRequestDispatcher("/lstVente.jsp").forward(request, response);

        }
        catch (Exception e) {
            System.out.println("Erreur dans performTask : " + e.toString());
            try {
                response.sendError(HttpURLConnection.HTTP_INTERNAL_ERROR);
            }
            catch (Exception e2) {
                System.out.println("Erreur sur sendError : " + e2.toString());
            }
        }
    }

}
