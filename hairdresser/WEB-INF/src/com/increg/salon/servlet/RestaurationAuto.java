/*
 * Restauration base d'urgence
 * Copyright (C) 2001-2009 Emmanuel Guyot <See emmguyot on SourceForge> 
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms 
 * of the GNU General Public License as published by the Free Software Foundation; either 
 * version 2 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; 
 * if not, write to the 
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 */
package com.increg.salon.servlet;

import java.text.MessageFormat;

import javax.servlet.http.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.increg.commun.BasicSession;
import com.increg.commun.DBSession;
import com.increg.commun.Executer;
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

	Log log = LogFactory.getLog(this.getClass());
	
    HttpSession mySession = request.getSession(false);
    
	Runtime aRuntime = Runtime.getRuntime();

	// Liste des fichiers
    boolean error = false;
	
    // Initialise une session light de traduction
    BasicSession myBasicSession = new BasicSession();
    myBasicSession.setLangue(request.getLocale());

    try {
        /**
         * Hypothèses :
         *  La base est lancée, dont ipc-daemon aussi
         */
        
        /**
         * Deconnexion de la base pour pouvoir restaurer
         */
        SalonSessionImpl mySalon = (SalonSessionImpl) mySession.getAttribute("SalonSession");
        mySession.removeAttribute("SalonSession");
        
        // La session peut ne pas être là en cas de coup dur!!
        if (mySalon != null) {
        	DBSession myDBSession = mySalon.getMyDBSession();
        	myDBSession.closeAll();
        	myDBSession = null;
        }
        mySalon = null;
        mySession.invalidate();
        System.gc();
                
        /**
         * Partie 1 : Arrêt de la base
         */
        log.info("Arret de la base de donnees");
        String cmd = System.getenv("PG_HOME") + "\\bin\\pg_ctl.exe stop -m immediate";
        log.info("Arret de la base de donnees : " + cmd);
        Executer dumpProc = new Executer(cmd);
        if (dumpProc.runAndWait() != 0) {
            // Impossible d'arrêter la base
        	myBasicSession.setMessage("Erreur", BasicSession.TAG_I18N + "restaurationAuto.erreurArretBase" + BasicSession.TAG_I18N);
            request.setAttribute("Erreur", myBasicSession.getMessage("Erreur"));
            error = true;
        }
        
        // Petite tempo pour laisser le temps de libérer le répertoire
        Thread.sleep(500);
        
        /**
         * Partie 2 : Suppression du répertoire de données
         * <b>Non compatible avec la solution multi salons!!!</b>
         */
        if (!error) {
        	log.info("Suppression de la base");
            cmd = "cmd /c \"rmdir /s/q " + System.getenv("PGDATA") + "\"";
            dumpProc = new Executer(cmd);
            // Test sur le code retour 
            if (dumpProc.runAndWait() != 0) {
                // Impossible de supprimer la base
            	myBasicSession.setMessage("Erreur", BasicSession.TAG_I18N + "restaurationAuto.erreurSupprBase" + BasicSession.TAG_I18N);
                request.setAttribute("Erreur", myBasicSession.getMessage("Erreur"));
                error = true;
            }
        }
	}
	catch (Exception e) {
		log.error("Erreur", e);
		String msg = MessageFormat.format(myBasicSession.getMessagesBundle().getString("restaurationAuto.erreur"), new Object[] { e.toString() });
        request.setAttribute("Erreur", msg);
	}

	try {
		// Passe la main à la forme
        getServletConfig().getServletContext().getRequestDispatcher("/finReload.jsp").forward(request, response);
	}
	catch (Exception e) {
		log.error("RestaurationAuto::performTask : Erreur à la redirection", e);
	}

}
}
