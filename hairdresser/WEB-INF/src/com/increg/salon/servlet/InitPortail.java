package com.increg.salon.servlet;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.MissingResourceException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ibm.ejs.security.util.Base64Coder;
import com.increg.commun.exception.ReloadNeededException;
import com.increg.salon.bean.IdentBean;
import com.increg.salon.bean.MultiConfigBean;
import com.increg.salon.bean.SalonSession;
import com.increg.salon.bean.SalonSessionImpl;

/**
 * Servlet d'accès au logiciel : Initialisation de la session
 * Creation date: (08/07/2001 15:26:39)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class InitPortail extends HttpServlet {
/**
 * Process incoming HTTP GET requests 
 * 
 * @param request Object that encapsulates the request to the servlet 
 * @param response Object that encapsulates the response from the servlet
 * @throws IOException ...
 * @throws ServletException ...
 */
public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	performTask(request, response);

}
/**
 * Process incoming HTTP POST requests 
 * 
 * @param request Object that encapsulates the request to the servlet 
 * @param response Object that encapsulates the response from the servlet
 * @throws IOException ...
 * @throws ServletException ...
 */
public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	performTask(request, response);

}
/**
 * Returns the servlet info string.
 * @return Info
 */
public String getServletInfo() {

	return super.getServletInfo();

}
/**
 * Initializes the servlet.
 */
public void init() {
	// insert code to initialize the servlet here

}
/**
 * Process incoming requests for information
 * 
 * @param request Object that encapsulates the request to the servlet 
 * @param response Object that encapsulates the response from the servlet
 */
public void performTask(HttpServletRequest request, HttpServletResponse response) {

	try {
		// Création de la session
		HttpSession mySession = request.getSession(true);

        String numBase = request.getParameter("numBase");
        
        // Recherche si multiconfig
        MultiConfigBean aMCbean = new MultiConfigBean();
        if ((numBase == null) && (aMCbean.getNbConfig() > 0)) {
            // Nous sommes en multi config
            request.setAttribute("MultiConfigBean", aMCbean);
            getServletConfig().getServletContext().getRequestDispatcher("/choixBase.jsp").forward(request, response);
        }
        else {
            
            String fichConfig = "config";
            if (numBase != null) {
                // La base a été choisie : Mise à jour de la config avant d'aller plus loin
                fichConfig = aMCbean.setConfigChoisie(Integer.parseInt(numBase));
            }

            SalonSession mySalon = null;
            boolean reloadNeeded = false;
            boolean badConfig = false;
            try {            
        		// Affectation du Bean Session
        		mySalon = new SalonSessionImpl(fichConfig);
            }
            catch (ReloadNeededException e) {
                request.setAttribute("Erreur", e.toString());
                reloadNeeded = true;
            }
            catch (MissingResourceException e) {
                request.setAttribute("Erreur", e.toString());
                badConfig = true;
            }
            catch (Throwable e) {
                // Pas prévu
                try {
                    response.sendError(500);
                } 
                catch (Exception e2) {
                    System.out.println("sendError en erreur ");
                    e2.printStackTrace();
                }
                return;
            }
    
            java.security.Principal userPrincipal = request.getUserPrincipal();
            String user = null;
            if (userPrincipal != null) {
                user = userPrincipal.getName();
            }
            else {
                // Triture le Header pour en extraire le user
                if (request.getHeader("Authorization") != null) {
                    String authorization = Base64Coder.base64Decode(request.getHeader("Authorization").substring(6));
                    user = authorization.substring(0, authorization.indexOf(':'));
                }
            }
            
            // Accès distant ? Il faut vérifier que c'est autorisé
    	    if ((!request.getRemoteAddr().equals ("127.0.0.1")) 
    	            && (request.getRemoteAddr().indexOf("192.168.0.") == -1)) {
    	        if ((user != null) 
    	                && (mySalon != null) && (mySalon.isRemoteEnable())) {
    	            System.out.println("Connexion de " + user + " depuis " + request.getRemoteAddr() + " le " + new Date().toString());
    	        }
    	        else if ((user == null) 
    	        		&& (mySalon != null) 
    	        		&& (mySalon.isRemoteEnable())
    	        		&& (!mySalon.isSecureApache())) {
    	            System.out.println("Connexion depuis " + request.getRemoteAddr() + " le " + new Date().toString());
    	        }
    	        else {
    	            System.out.println("Tentative d'intrusion à partir de " + request.getRemoteAddr());
    	            try {
                        response.sendError(500);
                    } 
                    catch (Exception e) {
                        System.out.println("sendError en erreur ");
                        e.printStackTrace();
                    }
    	            return;
    	        }
    	    }
            
            if (reloadNeeded) {
                getServletConfig().getServletContext().getRequestDispatcher("/Reload.jsp").forward(request, response);
            }
            else if (badConfig) {
                getServletConfig().getServletContext().getRequestDispatcher("/InstallKo.jsp").forward(request, response);
            }
            else {
        		mySession.setAttribute("SalonSession", mySalon);
				mySalon.setLangue(request.getLocale());
                
                IdentBean aIdent = null;
                if (user != null) {
                    aIdent = IdentBean.getIdentBeanFromUser(mySalon.getMyDBSession(), user);
                }
                 
                // Connection automatique ?
                if (mySalon.isAutoConnect()) {
                    // Connection transparente
                    getServletConfig().getServletContext().getRequestDispatcher("/ident.srv?MOT_PASSE=MDP").forward(request, response);
                }
                else if (aIdent != null) {
                    // Tente une connexion automatique à partir du nom de l'utilisateur
                    getServletConfig().getServletContext().getRequestDispatcher("/ident.srv?MOT_PASSE=" + URLEncoder.encode(aIdent.getMOT_PASSE())).forward(request, response);
                }
                else {
                    // Affiche le portail
                    /** 
                     * Forward pour conserver l'unité de transaction 
                     * et pour pouvoir passer des paramètres via Bean Request
                     **/
                    getServletConfig().getServletContext().getRequestDispatcher("/Portail.jsp").forward(request, response);
                }
            
            }
    
        }
	}
	catch (Throwable theException) {
		System.out.println("Erreur dans InitPortail : ");
        theException.printStackTrace();
	}
}
}