package com.increg.salon.servlet;

import com.increg.commun.*;
import java.util.*;
import com.increg.salon.bean.*;
import com.increg.salon.request.*;
import javax.servlet.http.*;

/**
 * Recherche/Liste des informations de synthèse de la journée
 * Creation date: (15/10/2001 14:09:47)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class RechFinJournee extends ConnectedServlet {
/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) {

	// Récupération des paramètres
	String DT_JOUR = request.getParameter("DT_JOUR");

	java.text.DateFormat formatDate  = java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT);

	// Valeurs par défaut
	if (DT_JOUR == null) {
		// Date du jour
		DT_JOUR  = formatDate.format(Calendar.getInstance().getTime());
	}
	request.setAttribute("DT_JOUR", DT_JOUR);

	// Récupère la connexion
	HttpSession mySession = request.getSession(false);
	DBSession myDBSession = ((SalonSession) mySession.getAttribute("SalonSession")).getMyDBSession();

    TreeMap lstLignesB = new TreeMap();
    TreeMap lstTypeB = new TreeMap();
    TreeMap lstTypeRemB = new TreeMap();
    TreeMap lstTypeMcaB = new TreeMap();
    Brouillard brouillardTotal = new Brouillard();

    Vector lstLignesC = new Vector();
    TreeSet lstTypeC = new TreeSet();

    Vector listeCaisse = new Vector();
    
    try {
        // Calcule le brouillard pour avoir la répartition pour tout le salon
        RechBrouillard.rechercheBrouillard(myDBSession, DT_JOUR, DT_JOUR, lstLignesB, lstTypeB, lstTypeRemB, lstTypeMcaB, brouillardTotal);
        
        // Calcule la ventilation par personne
        RechCA.rechercheCA(myDBSession, null, DT_JOUR, DT_JOUR, lstLignesC, lstTypeC);
        
        // Recherche les soldes de caisse pour aider à recompter
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
