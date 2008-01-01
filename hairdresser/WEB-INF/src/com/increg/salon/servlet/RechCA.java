/*
 * Recherche/Liste de Chiffre d'affaires
 * Copyright (C) 2001-2008 Emmanuel Guyot <See emmguyot on SourceForge> 
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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TreeSet;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.increg.commun.DBSession;
import com.increg.salon.bean.SalonSession;
import com.increg.salon.request.CA;
import com.increg.util.ServletUtil;

public class RechCA extends ConnectedServlet {

	protected static Log log = LogFactory.getLog(RechCA.class);
/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(HttpServletRequest request, HttpServletResponse response) {


	// Récupération des paramètres
	String CD_COLLAB = request.getParameter("CD_COLLAB");
	String DT_DEBUT = request.getParameter("DT_DEBUT");
	String DT_FIN = request.getParameter("DT_FIN");

    // Récupère la connexion
    HttpSession mySession = request.getSession(false);
    SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
    DBSession myDBSession = mySalon.getMyDBSession();
    
    DateFormat formatDate = new SimpleDateFormat(mySalon.getMessagesBundle().getString("format.dateSimpleDefaut"));

	// Valeurs par défaut
	// Début de mois
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
		log.error("Erreur dans performTask : ", e);
		try {
			response.sendError(500);
		}
		catch (Exception e2) {
			log.error("Erreur sur sendError : ", e2);
		}
	}
 } 

/**
 * Effectue la recherche pour le CA et alimente les structures résultat
 * @param myDBSession Connexion à la base à utiliser
 * @param CD_COLLAB Collaborateur pour filtrer
 * @param DT_DEBUT Date de début de la période (inclut)
 * @param DT_FIN Date de fin de la période (inclut)
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
        log.error("Erreur dans RechercheCA : ", e);
        throw (e);
    }
}    
}
