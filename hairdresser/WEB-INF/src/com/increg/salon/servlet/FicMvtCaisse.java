package com.increg.salon.servlet;

import java.util.*;
import java.sql.*;
import com.increg.salon.bean.*;
import javax.servlet.http.*;
import com.increg.commun.*;

/**
 * Création d'un mouvement de caisse
 * Creation date: (26/09/2001 08:29:27)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class FicMvtCaisse extends ConnectedServlet {
/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(
	javax.servlet.http.HttpServletRequest request,
	javax.servlet.http.HttpServletResponse response) {

	// Récupération des paramètres
	String Action = request.getParameter("Action");
	String CD_MOD_REGL = request.getParameter("CD_MOD_REGL");
	String CD_TYP_MCA = request.getParameter("CD_TYP_MCA");
	String DT_MVT = request.getParameter("DT_MVT");
	String MONTANT = request.getParameter("MONTANT");
	String COMM = request.getParameter("COMM");

	// Récupère la connexion
	HttpSession mySession = request.getSession(false);
	SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
	DBSession myDBSession = mySalon.getMyDBSession();

	MvtCaisseBean aMvt = null;
	
	try {
		if (Action == null) {
			// Première phase de création
			request.setAttribute("Action", "Creation");
			// Un bean vide
			aMvt = new MvtCaisseBean();
			// Valeur par défaut
			aMvt.setDT_MVT(Calendar.getInstance());
		}
		else if (Action.equals("Creation")) {
			// Crée réellement l'article

			/**
			 * Création du bean et enregistrement
			 */
			aMvt = new MvtCaisseBean();

			try {
	            aMvt.setCD_MOD_REGL(CD_MOD_REGL);
	            aMvt.setCD_TYP_MCA(CD_TYP_MCA);
	            aMvt.setDT_MVT(DT_MVT);
	            aMvt.setCOMM(COMM);
	            aMvt.setMONTANT(MONTANT);

	            CaisseBean aCaisse = CaisseBean.getCaisseBean(myDBSession, CD_MOD_REGL);
	            aMvt.setSOLDE_AVANT(aCaisse.getSOLDE());

	            aMvt.create(myDBSession);

	            mySalon.setMessage("Info", "Création effectuée. Vous pouvez passer un nouveau mouvement.");
	            request.setAttribute("Action", "Creation");

	            aMvt = new MvtCaisseBean();
	            // Valeur par défaut
				aMvt.setDT_MVT(Calendar.getInstance());
			}
			catch (Exception e) {
	            mySalon.setMessage("Erreur", e.toString());
				request.setAttribute("Action", Action);
			}
		}
		else {
			System.out.println ("Action non codée : " + Action);
		}
	}
	catch (Exception e) {
		mySalon.setMessage("Erreur", e.toString());
		System.out.println("Note : " + e.toString());
	}

    /**
     * Reset de la transaction pour la recherche des informations complémentaires
     */
    myDBSession.cleanTransaction();

	/**
	 * Recherche les caisses existantes
	 */
	Vector listeCaisse = new Vector();

    try {
        rechercheTouteCaisse(myDBSession, null, listeCaisse);
    }
    catch (SQLException e) {
       try {
           response.sendError(500);
       }
       catch (Exception e2) {
           System.out.println("Erreur sur sendError : " + e2.toString());
       }
    }


	request.setAttribute("listeCaisse", listeCaisse);
	request.setAttribute("MvtCaisseBean", aMvt);

	try {
		// Passe la main à la fiche de création
		getServletConfig().getServletContext().getRequestDispatcher("/ficMvtCaisse.jsp").forward(request, response);

	}
	catch (Exception e) {
		System.out.println("FicMvtCaisse::performTask : Erreur à la redirection : " + e.toString());
	}
}

/**
 * Recherche des caisses existantes
 * @param myDBSession Connexion à la base à utiliser
 * @param listeCaisse Liste des caisses produite par la méthode
 * @param utilisable Que les caisses actuelles ?
 * @throws SQLException En cas de pb SQL dans la requete
 */
static void rechercheTouteCaisse(DBSession myDBSession, String utilisable, Vector listeCaisse) throws SQLException {

    String reqSQL = "select * from CAISSE, MOD_REGL where CAISSE.CD_MOD_REGL=MOD_REGL.CD_MOD_REGL";
    if ((utilisable != null) && (utilisable.length() > 0)) {
        reqSQL = reqSQL + " AND UTILISABLE=" + DBSession.quoteWith(utilisable, '\'');
    }

    // Interroge la Base
    try {
        ResultSet aRS = myDBSession.doRequest(reqSQL);

        while (aRS.next()) {
            /**
              * Création du bean de consultation
              */
            CaisseBean aCaisse = new CaisseBean(aRS);
            listeCaisse.add(aCaisse);
        }
        aRS.close();
    }
    catch (SQLException e) {
       System.out.println("Erreur dans requète de recherche de caisse : " + e.toString());
       throw (e);
    }
}
}
