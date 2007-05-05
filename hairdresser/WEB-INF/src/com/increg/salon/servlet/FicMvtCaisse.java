package com.increg.salon.servlet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.sql.*;
import com.increg.salon.bean.*;
import javax.servlet.http.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.increg.commun.*;

/**
 * Cr�ation d'un mouvement de caisse
 * Creation date: (26/09/2001 08:29:27)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class FicMvtCaisse extends ConnectedServlet {

	protected static Log log = LogFactory.getLog(FicMvtCaisse.class);

/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(
	javax.servlet.http.HttpServletRequest request,
	javax.servlet.http.HttpServletResponse response) {

	// R�cup�ration des param�tres
	String Action = request.getParameter("Action");
	String CD_MOD_REGL = request.getParameter("CD_MOD_REGL");
	String CD_TYP_MCA = request.getParameter("CD_TYP_MCA");
	String DT_MVT = request.getParameter("DT_MVT");
	String MONTANT = request.getParameter("MONTANT");
	String COMM = request.getParameter("COMM");

	// R�cup�re la connexion
	HttpSession mySession = request.getSession(false);
	SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
	DBSession myDBSession = mySalon.getMyDBSession();
    DateFormat formatDate = new SimpleDateFormat(mySalon.getMessagesBundle().getString("format.dateDefaut"));

	MvtCaisseBean aMvt = null;
	
	try {
		if (Action == null) {
			// Premi�re phase de cr�ation
			request.setAttribute("Action", "Creation");
			// Un bean vide
			aMvt = new MvtCaisseBean();
			// Valeur par d�faut
			aMvt.setDT_MVT(Calendar.getInstance());
		}
		else if (Action.equals("Creation")) {
			// Cr�e r�ellement l'article

			/**
			 * Cr�ation du bean et enregistrement
			 */
			aMvt = new MvtCaisseBean();

			try {
	            aMvt.setCD_MOD_REGL(CD_MOD_REGL);
	            aMvt.setCD_TYP_MCA(CD_TYP_MCA);
                Calendar dtMvt = Calendar.getInstance();
                dtMvt.setTime(formatDate.parse(DT_MVT));
                aMvt.setDT_MVT(dtMvt);
	            aMvt.setCOMM(COMM);
	            aMvt.setMONTANT(MONTANT);

	            CaisseBean aCaisse = CaisseBean.getCaisseBean(myDBSession, CD_MOD_REGL);
	            aMvt.setSOLDE_AVANT(aCaisse.getSOLDE());

	            aMvt.create(myDBSession);

	            mySalon.setMessage("Info", BasicSession.TAG_I18N + "ficMvtCaisse.creationOk" + BasicSession.TAG_I18N);
	            request.setAttribute("Action", "Creation");

	            aMvt = new MvtCaisseBean();
	            // Valeur par d�faut
				aMvt.setDT_MVT(Calendar.getInstance());
			}
			catch (Exception e) {
	            mySalon.setMessage("Erreur", e.toString());
				request.setAttribute("Action", Action);
			}
		}
		else {
			log.error("Action non cod�e : " + Action);
		}
	}
	catch (Exception e) {
		mySalon.setMessage("Erreur", e.toString());
		log.error("Erreur g�n�rale : ", e);
	}

    /**
     * Reset de la transaction pour la recherche des informations compl�mentaires
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
           log.error("Erreur sur sendError : ", e2);
       }
    }


	request.setAttribute("listeCaisse", listeCaisse);
	request.setAttribute("MvtCaisseBean", aMvt);

	try {
		// Passe la main � la fiche de cr�ation
		getServletConfig().getServletContext().getRequestDispatcher("/ficMvtCaisse.jsp").forward(request, response);

	}
	catch (Exception e) {
		log.error("Erreur � la redirection : ", e);
	}
}

/**
 * Recherche des caisses existantes
 * @param myDBSession Connexion � la base � utiliser
 * @param listeCaisse Liste des caisses produite par la m�thode
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
              * Cr�ation du bean de consultation
              */
            CaisseBean aCaisse = new CaisseBean(aRS);
            listeCaisse.add(aCaisse);
        }
        aRS.close();
    }
    catch (SQLException e) {
       log.error("Erreur dans requ�te de recherche de caisse : ", e);
       throw (e);
    }
}
}
