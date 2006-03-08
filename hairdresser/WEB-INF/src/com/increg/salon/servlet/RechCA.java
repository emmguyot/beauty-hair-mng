package com.increg.salon.servlet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TreeSet;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.increg.commun.DBSession;
import com.increg.salon.bean.SalonSession;
import com.increg.salon.request.CA;
import com.increg.util.ServletUtil;
/**
 * Recherche/Liste de Chiffre d'affaires
 * Creation date: (15/10/2001 14:09:47)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class RechCA extends ConnectedServlet {
/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(HttpServletRequest request, HttpServletResponse response) {

	// R�cup�ration des param�tres
	String CD_COLLAB = request.getParameter("CD_COLLAB");
	String DT_DEBUT = request.getParameter("DT_DEBUT");
	String DT_FIN = request.getParameter("DT_FIN");

    // R�cup�re la connexion
    HttpSession mySession = request.getSession(false);
    SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
    DBSession myDBSession = mySalon.getMyDBSession();
    
    DateFormat formatDate = new SimpleDateFormat(mySalon.getMessagesBundle().getString("format.dateSimpleDefaut"));

	// Valeurs par d�faut
	// D�but de mois
	Calendar J7 = Calendar.getInstance();
	J7.add(Calendar.DAY_OF_YEAR, 1 - J7.get(Calendar.DAY_OF_MONTH));
    Calendar dtDebut = ServletUtil.interpreteDate(DT_DEBUT, formatDate, J7);
    Calendar dtFin = ServletUtil.interpreteDate(DT_FIN, formatDate, Calendar.getInstance());
    if (dtFin.before(dtDebut)) {
        dtFin = dtDebut;
    }
    request.setAttribute("DT_DEBUT", dtDebut);
    request.setAttribute("DT_FIN", dtFin);
    DT_DEBUT = myDBSession.getFormatDate().format(dtDebut.getTime());
    DT_FIN = myDBSession.getFormatDate().format(dtFin.getTime());
	
    try {
        Vector lstLignes = new Vector();
        TreeSet lstType = new TreeSet();

        rechercheCA(myDBSession, CD_COLLAB, DT_DEBUT, DT_FIN, lstLignes, lstType);
        
		// Stocke le Vector pour le JSP
		request.setAttribute("Liste", lstLignes);
		request.setAttribute("ListeType", new Vector(lstType));

		// Passe la main
		getServletConfig().getServletContext().getRequestDispatcher("/lstCA.jsp").forward(request, response);

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

/**
 * Effectue la recherche pour le CA et alimente les structures r�sultat
 * @param myDBSession Connexion � la base � utiliser
 * @param CD_COLLAB Collaborateur pour filtrer
 * @param DT_DEBUT Date de d�but de la p�riode (inclut)
 * @param DT_FIN Date de fin de la p�riode (inclut)
 * @param lstLignes Lignes de chiffre d'affaires
 * @param lstType Type de vente
 * @throws SQLException En cas d'erreur
 */ 
public static void rechercheCA (DBSession myDBSession, String CD_COLLAB, String DT_DEBUT, String DT_FIN, Vector lstLignes, TreeSet lstType)
    throws SQLException {
    
    // Constitue la requete SQL
    String reqSQL = "select PRENOM, date_trunc('month', HISTO_PREST.DT_PREST)::date as MONTH_PREST, LIB_TYP_VENT, "
                + "sum(QTE*HISTO_PREST.PRX_UNIT_TTC) as MONTANT "
                + "from FACT, HISTO_PREST, PREST, COLLAB, TYP_VENT "
                + "where HISTO_PREST.CD_PREST = PREST.CD_PREST "
                + "and HISTO_PREST.CD_COLLAB = COLLAB.CD_COLLAB "
                + "and TYP_VENT.CD_TYP_VENT = PREST.CD_TYP_VENT "
                + "and FACT.CD_FACT = HISTO_PREST.CD_FACT "
                + "and FACT.CD_PAIEMENT is not null";

    if ((CD_COLLAB != null) && (CD_COLLAB.length() > 0)) {
        reqSQL = reqSQL + " and HISTO_PREST.CD_COLLAB=" + CD_COLLAB;
    }
    if ((DT_DEBUT != null) && (DT_DEBUT.length() > 0)) {
        reqSQL = reqSQL + " and HISTO_PREST.DT_PREST >= '" + DT_DEBUT + "'";
    }
    if ((DT_FIN != null) && (DT_FIN.length() > 0)) {
        reqSQL = reqSQL + " and HISTO_PREST.DT_PREST <= '" + DT_FIN + "'";
    }
    reqSQL = reqSQL + " group by MONTH_PREST, PRENOM, LIB_TYP_VENT";

    // Interroge la Base
    try {
        ResultSet aRS = myDBSession.doRequest(reqSQL);

        while (aRS.next()) {
            CA aCA = new CA();

            aCA.setPRENOM(aRS.getString("PRENOM"));
            java.util.Date DtPrest = aRS.getDate("MONTH_PREST");
            Calendar DT_PREST = Calendar.getInstance();
            DT_PREST.setTime(DtPrest);
            aCA.setDT_PREST(DT_PREST);
            aCA.setLIB_TYP_VENT(aRS.getString("LIB_TYP_VENT"));
            aCA.setMONTANT(aRS.getBigDecimal("MONTANT", 2));

            lstLignes.add(aCA);

            lstType.add(aCA.getLIB_TYP_VENT());
        }
        aRS.close();
    }
    catch (SQLException e) {
        System.out.println("Erreur dans RechercheCA : " + e.toString());
        throw (e);
    }
}    
}
