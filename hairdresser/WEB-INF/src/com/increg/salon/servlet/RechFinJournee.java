package com.increg.salon.servlet;

import com.increg.commun.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import com.increg.salon.bean.*;
import com.increg.salon.request.*;
import javax.servlet.http.*;

/**
 * Recherche/Liste des informations de synth�se de la journ�e
 * Creation date: (15/10/2001 14:09:47)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class RechFinJournee extends ConnectedServlet {
/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) {

    // R�cup�re la connexion
    HttpSession mySession = request.getSession(false);
    SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
    DBSession myDBSession = mySalon.getMyDBSession();

	// Date du jour
	Calendar dtJour = Calendar.getInstance(); 
	request.setAttribute("DT_JOUR", dtJour);
	String DT_JOUR  = myDBSession.getFormatDate().format(dtJour.getTime());

    TreeMap lstLignesB = new TreeMap();
    TreeMap lstTypeB = new TreeMap();
    TreeMap lstTypeRemB = new TreeMap();
    TreeMap lstTypeMcaB = new TreeMap();
    Brouillard brouillardTotal = new Brouillard();

    Vector lstLignesC = new Vector();
    TreeSet lstTypeC = new TreeSet();

    Vector listeCaisse = new Vector();
    
    try {
        // Calcule le brouillard pour avoir la r�partition pour tout le salon
        RechBrouillard.rechercheBrouillard(myDBSession, DT_JOUR, DT_JOUR, lstLignesB, lstTypeB, lstTypeRemB, lstTypeMcaB, brouillardTotal);
        
        // Calcule la ventilation par personne
        RechCA.rechercheCA(myDBSession, null, DT_JOUR, DT_JOUR, lstLignesC, lstTypeC);
        
        // Recherche les soldes de caisse pour aider � recompter
        FicMvtCaisse.rechercheTouteCaisse(myDBSession, "O", listeCaisse);
        
    }
    catch (Exception e) {
        try {
            response.sendError(500);
            return;
        }
        catch (Exception e2) {
            System.out.println ("Erreur sur sendError : " + e2.toString());
        }
    }
    
    try {
        // Stocke les Map pour le JSP
        request.setAttribute("ListeB", lstLignesB);
        request.setAttribute("ListeTypeB", lstTypeB);
        request.setAttribute("ListeTypeRemB", lstTypeRemB);
        request.setAttribute("ListeTypeMcaB", lstTypeMcaB);
        request.setAttribute("TotalB", brouillardTotal);

        // Stocke le Vector pour le JSP
        request.setAttribute("ListeC", lstLignesC);
        request.setAttribute("ListeTypeC", new Vector(lstTypeC));
        
        request.setAttribute("ListeCaisse", new Vector(listeCaisse));
        
		// Passe la main
		getServletConfig().getServletContext().getRequestDispatcher("/lstFinJournee.jsp").forward(request, response);

	}
	catch (Exception e) {
		System.out.println ("Erreur dans performTask : " + e.toString());
		try {
			response.sendError(500);
		}
		catch (Exception e2) {
			System.out.println ("Erreur sur sendError : " + e2.toString());
		}
	}
}
}
