package com.increg.salon.servlet;

import com.increg.salon.bean.*;
import javax.servlet.http.*;
import java.math.BigDecimal;

/**
 * Calcul le rendu d'espèces
 * Creation date: (07/07/2002 19:53:14)
 * @author Alexandre GUYOT <alexandre.guyot@laposte.net>
 */
public class FicRenduMonnaie extends ConnectedServlet {

    /**
     * @see com.increg.salon.servlet.ConnectedServlet
     */
    public void performTask(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) {

        // Récupération des paramètres
        String montant = request.getParameter("montant");
        String montantRegle = request.getParameter("montantRegle");

        // Récupère la connexion
        HttpSession mySession = request.getSession(false);
        SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");

        BigDecimal aRendre = new BigDecimal(0);
        try {
            aRendre = new BigDecimal(montantRegle).subtract(new BigDecimal(montant));
            if (aRendre.signum() < 0) { 
                // Montant trop faible
                mySalon.setMessage("Erreur", "Montant réglé trop faible");
            }
        }
        catch (Throwable t) {
            mySalon.setMessage("Erreur", t.toString());
            System.out.println("Note : " + t.toString());
        }

        request.setAttribute("montant", montant);
        request.setAttribute("montantRegle", montantRegle);
        request.setAttribute("aRendre", aRendre);

        try {
            // Passe la main à la fiche d'affichage
            getServletConfig().getServletContext().getRequestDispatcher("/ficRenduMonnaie.jsp").forward(request, response);

        }
        catch (Exception e) {
            System.out.println("FicTypEsp::performTask : Erreur à la redirection : " + e.toString());
        }
    }
}
