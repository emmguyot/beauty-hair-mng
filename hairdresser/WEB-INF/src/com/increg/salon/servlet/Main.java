package com.increg.salon.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet principale pour affichage menus et par principale
 * Permet uniquement la vérif de la connection
 * Creation date: 28 sept. 2003
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class Main extends ConnectedServlet {
/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(HttpServletRequest request, HttpServletResponse response) {

    String menu = request.getParameter("menu");
    
	try {
        if (menu != null) {
            // Passe la main à la fiche de création
            getServletConfig().getServletContext().getRequestDispatcher("/Menu.jsp").forward(request, response);
        }
        else {
            // Passe la main à la fiche de création
            getServletConfig().getServletContext().getRequestDispatcher("/Main.jsp").forward(request, response);
        }

	}
	catch (Exception e) {
		System.out.println("Main::performTask : Erreur à la redirection : " + e.toString());
	}
}
}
