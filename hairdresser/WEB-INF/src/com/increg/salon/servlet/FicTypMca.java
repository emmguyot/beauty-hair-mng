package com.increg.salon.servlet;

import com.increg.salon.bean.*;
import javax.servlet.http.*;
import com.increg.commun.*;

/**
 * Gestion des types de mouvement de caisse
 * Creation date: (19/09/2001 13:34:23)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class FicTypMca extends ConnectedServlet {
/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(
	javax.servlet.http.HttpServletRequest request,
	javax.servlet.http.HttpServletResponse response) {

	// R�cup�ration des param�tres
	String Action = request.getParameter("Action");
	String CD_TYP_MCA = request.getParameter("CD_TYP_MCA");
	String LIB_TYP_MCA = request.getParameter("LIB_TYP_MCA");
	String SENS_MCA = request.getParameter("SENS_MCA");

	// R�cup�re la connexion
	HttpSession mySession = request.getSession(false);
	SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
	DBSession myDBSession = mySalon.getMyDBSession();

	TypMcaBean aTypMca = null;
	
	try {
		if (Action == null) {
			// Premi�re phase de cr�ation
			request.setAttribute("Action", "Creation");
			// Un bean vide
			aTypMca = new TypMcaBean();
		}
		else if (Action.equals("Creation")) {
			// Cr�e r�ellement la prestation

			/**
			 * Cr�ation du bean et enregistrement
			 */
			aTypMca = new TypMcaBean();
			aTypMca.setCD_TYP_MCA(CD_TYP_MCA);
			aTypMca.setLIB_TYP_MCA(LIB_TYP_MCA);
			aTypMca.setSENS_MCA(SENS_MCA);

			try {
	            aTypMca.create(myDBSession);
	            mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.creationOk" + BasicSession.TAG_I18N);
	            request.setAttribute("Action", "Modification");
			}
			catch (Exception e) {
	            mySalon.setMessage("Erreur", e.toString());
				request.setAttribute("Action", Action);
			}
		}
		else if ((Action.equals("Modification")) && (LIB_TYP_MCA == null)) {
			// Affichage de la fiche en modification
			request.setAttribute("Action", "Modification");

			aTypMca = TypMcaBean.getTypMcaBean(myDBSession, CD_TYP_MCA);
		}
		else if (Action.equals("Modification")) {
			// Modification effective de la fiche

			/**
			 * Cr�ation du bean et enregistrement
			 */
			aTypMca = TypMcaBean.getTypMcaBean(myDBSession, CD_TYP_MCA);

			aTypMca.setCD_TYP_MCA(CD_TYP_MCA);
			aTypMca.setLIB_TYP_MCA(LIB_TYP_MCA);
            if (!aTypMca.getSENS_MCA().equals(SENS_MCA)) {
                // Modification du sens : V�rification si possible
                if (aTypMca.sensIsModifiable(myDBSession)) {
                    aTypMca.setSENS_MCA(SENS_MCA);
                }
                else {
                    mySalon.setMessage("Erreur", BasicSession.TAG_I18N + "ficTypMvt.typeUtilise" + BasicSession.TAG_I18N);
                }
            }

			try {
	            aTypMca.maj(myDBSession);
	            mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.enregistrementOk" + BasicSession.TAG_I18N);
	            request.setAttribute("Action", "Modification");
			}
			catch (Exception e) {
	            mySalon.setMessage("Erreur", e.toString());
				request.setAttribute("Action", Action);
			}
		}
		else if (Action.equals("Suppression")) {
			// Modification effective de la fiche

			/**
			 * Cr�ation du bean et enregistrement
			 */
			aTypMca = TypMcaBean.getTypMcaBean(myDBSession, CD_TYP_MCA);

			try {
	            aTypMca.delete(myDBSession);
	            mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.suppressionOk" + BasicSession.TAG_I18N);
	            // Un bean vide
	            aTypMca = new TypMcaBean();
	            request.setAttribute("Action", "Creation");
	        }	
			catch (Exception e) {
	            mySalon.setMessage("Erreur", e.toString());
				request.setAttribute("Action", "Modification");
			}
		}
		else if (Action.equals("Duplication")) {
			// Duplique la prestation

			/**
			 * Cr�ation du bean et enregistrement
			 */
			aTypMca = new TypMcaBean();
			aTypMca.setLIB_TYP_MCA(LIB_TYP_MCA);
			aTypMca.setSENS_MCA(SENS_MCA);

			try {
	            aTypMca.create(myDBSession);
	            mySalon.setMessage("Info", BasicSession.TAG_I18N + "message.duplicationOk" + BasicSession.TAG_I18N);
	            request.setAttribute("Action", "Modification");
			}
			catch (Exception e) {
	            mySalon.setMessage("Erreur", e.toString());
				request.setAttribute("Action", Action);
			}
		}
		else {
			System.out.println ("Action non cod�e : " + Action);
		}
	}
	catch (Exception e) {
		mySalon.setMessage("Erreur", e.toString());
		System.out.println("Note : " + e.toString());
	}

    /**
     * Reset de la transaction pour la recherche des informations compl�mentaires
     */
    myDBSession.cleanTransaction();

	request.setAttribute("TypMcaBean", aTypMca);
	
	try {
		// Passe la main � la fiche de cr�ation
		getServletConfig().getServletContext().getRequestDispatcher("/ficTypMca.jsp").forward(request, response);

	}
	catch (Exception e) {
		System.out.println("FicTypMca::performTask : Erreur � la redirection : " + e.toString());
	}
}
}
