package com.increg.salon.servlet;

import javax.servlet.http.*;

import com.increg.commun.DBSession;
import com.increg.salon.bean.SalonSessionImpl;

/**
 * Servlet de restauration automatique de la base : Pour les cas d'urgence
 * Creation date: 27 déc. 2002
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class RestaurationAuto extends HttpServlet {

/**
 * Process incoming HTTP GET requests 
 * 
 * @param request Object that encapsulates the request to the servlet 
 * @param response Object that encapsulates the response from the servlet
 * @throws java.io.IOException .
 * @throws javax.servlet.ServletException .
 */
public void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {

    performTask(request, response);
}

/**
 * Process incoming requests for information
 * 
 * @param request Object that encapsulates the request to the servlet 
 * @param response Object that encapsulates the response from the servlet
 */
public void performTask(HttpServletRequest request, HttpServletResponse response) {

    HttpSession mySession = request.getSession(false);
    
	Runtime aRuntime = Runtime.getRuntime();

	// Liste des fichiers
    boolean error = false;
	
	try {
        /**
         * Hypothèses :
         *  La base est lancée, dont ipc-daemon aussi
         */
        
        /**
         * Deconnexion de la base pour pouvoir restaurer
         */
        mySession.removeAttribute("SalonSession");
        SalonSessionImpl mySalon = (SalonSessionImpl) mySession.getAttribute("SalonSession");
        DBSession myDBSession = mySalon.getMyDBSession();
        myDBSession.closeAll();
        myDBSession = null;
        mySalon = null;
        mySession.invalidate();
        System.gc();
                
        /**
         * Partie 1 : Arrêt de la base
         */
        System.out.println("Arret de la base de donnees");
        String cmd = "bash --login -c \"pg_ctl stop -m immediate 2>&1 > ~/RestoAuto_p1.txt\"";
        Process aProc = aRuntime.exec(cmd);
        // Test sur le code retour 
        if (aProc.waitFor() != 0) {
            // Impossible d'arrêter la base
            request.setAttribute("Erreur", "La base n'a pas pu être arrêtée. La restauration n'a pas été faite.");
            error = true;
        }
        
        /**
         * Partie 2 : Suppression du répertoire de données
         * <b>Non compatible avec la solution multi salons!!!</b>
         */
        if (!error) {
            System.out.println("Suppression de la base");
            cmd = "bash --login -c \"rm -rf $PGDATA\"";
            aProc = aRuntime.exec(cmd);
            // Test sur le code retour 
            if (aProc.waitFor() != 0) {
                // Impossible de supprimer la base
                request.setAttribute("Erreur", "La base n'a pas pu être supprimée. La restauration n'a pas été faite.");
                error = true;
            }
        }
        
        /**
         * Partie 3 : Arrêt et redémarrage du daemon IPC
         */
        if (!error) {
            System.out.println("Arret de ipc-daemon");
            cmd = "bash --login -c \"kill `ps | grep ipc-daemon | gawk '{ print $1 }'`\"";
            aProc = aRuntime.exec(cmd);
            // Test sur le code retour 
            if (aProc.waitFor() != 0) {
                // Impossible 
                request.setAttribute("Erreur", "Le processus ipc n'a pu être réinitialisé. La restauration n'a pas été faite.");
                error = true;
            }
        }
	}
	catch (Exception e) {
		System.out.println("Note : " + e.toString());
        request.setAttribute("Erreur", "Erreur durant la restauration automatique." + e.toString());
	}

	try {
		// Passe la main à la forme
        getServletConfig().getServletContext().getRequestDispatcher("/finReload.jsp").forward(request, response);
	}
	catch (Exception e) {
		System.out.println("RestaurationAuto::performTask : Erreur à la redirection : " + e.toString());
	}

}
}
