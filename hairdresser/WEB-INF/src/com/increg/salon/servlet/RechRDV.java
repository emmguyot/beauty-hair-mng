/*
 * Recherche/Liste des RDV 
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.increg.commun.DBSession;
import com.increg.salon.bean.FactBean;
import com.increg.salon.bean.RDVBean;
import com.increg.salon.bean.SalonSession;
import com.increg.salon.request.RDVFact;
import com.increg.util.ServletUtil;

/**
 * Recherche/Liste des RDV
 * Creation date: 15 févr. 2004
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class RechRDV extends ConnectedServlet {
    /**
     * @see com.increg.salon.servlet.ConnectedServlet
     */
    public void performTask(HttpServletRequest request, HttpServletResponse response) {

    	Log log = LogFactory.getLog(this.getClass());

        // Récupération des paramètres
        String action = request.getParameter("Action");
        String CD_COLLAB = request.getParameter("CD_COLLAB");
        String DT_DEBUT = request.getParameter("DT_DEBUT");
        String DT_FIN = request.getParameter("DT_FIN");

        // Récupère la connexion
        HttpSession mySession = request.getSession(false);
        SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
        DBSession myDBSession = mySalon.getMyDBSession();
        DateFormat formatDate = new SimpleDateFormat(mySalon.getMessagesBundle().getString("format.dateHeureSimpleSansSeconde"));
        DateFormat formatDateDB = myDBSession.getFormatDateTimeSansTZ();
        formatDate.setTimeZone(RDVBean.getTimeZone());
        formatDateDB.setTimeZone(RDVBean.getTimeZone());

        Calendar dtDebut = ServletUtil.interpreteDate(DT_DEBUT, formatDate, dtDebutDefaut());
        Calendar dtFin = ServletUtil.interpreteDate(DT_FIN, formatDate, dtFinDefault());
        if (dtFin.before(dtDebut)) {
            dtFin = dtDebut;
        }
        dtDebut.setTimeZone(RDVBean.getTimeZone());
        dtFin.setTimeZone(RDVBean.getTimeZone());
        request.setAttribute("DT_DEBUT", dtDebut);
        request.setAttribute("DT_FIN", dtFin);
        DT_DEBUT = formatDateDB.format(dtDebut.getTime());
        DT_FIN = formatDateDB.format(dtFin.getTime());

        // Constitue la requete SQL
        String reqSQL = "select * from RDV ";
        String reqSQLreel = "select * from FACT ";
        reqSQL = reqSQL + " where 1=1";
        reqSQLreel = reqSQLreel + " where CD_PAIEMENT is not null";
        if ((CD_COLLAB != null) && (CD_COLLAB.length() > 0)) {
            reqSQL = reqSQL + " and CD_COLLAB=" + CD_COLLAB;
            reqSQLreel = reqSQLreel + " and CD_COLLAB=" + CD_COLLAB;
        }
        if ((DT_DEBUT != null) && (DT_DEBUT.length() > 0)) {
            reqSQL = reqSQL + " and DT_DEBUT >= " + DBSession.quoteWith(DT_DEBUT + "+0", '\'');
            reqSQLreel = reqSQLreel + " and DT_PREST >= " + DBSession.quoteWith(DT_DEBUT + "+0", '\'');
        }
        if ((DT_FIN != null) && (DT_FIN.length() > 0)) {
            reqSQL = reqSQL + " and DT_DEBUT <= " + DBSession.quoteWith(DT_FIN + "+0", '\'');
            reqSQLreel = reqSQLreel + " and DT_PREST <= " + DBSession.quoteWith(DT_FIN + "+0", '\'');
        }
        reqSQL = reqSQL + " order by DT_DEBUT desc, CD_COLLAB";
        reqSQLreel = reqSQLreel + " order by DT_PREST desc, CD_COLLAB";


        // Interroge la Base
        try {
            ResultSet aRS = myDBSession.doRequest(reqSQL);
            TreeSet lstLignes = new TreeSet();

            while (aRS.next()) {
                RDVBean aRDV = new RDVBean(aRS);
                lstLignes.add(new RDVFact(myDBSession, aRDV, mySalon.getMessagesBundle()));
            }
            aRS.close();

            aRS = myDBSession.doRequest(reqSQLreel);
            while (aRS.next()) {
                FactBean aFact = new FactBean(aRS, mySalon.getMessagesBundle());
                aFact.getLignes(myDBSession);
                
                boolean affectationOk = false;
                Iterator iterRDV = lstLignes.iterator();
                while ((!affectationOk) && (iterRDV.hasNext())) {
                    RDVFact aRDVFact = (RDVFact) iterRDV.next();

                    if (aRDVFact.setFact(aFact)) {
                        affectationOk = true;
                    }
                }
                if (!affectationOk) {
                    RDVFact aRDVFact = new RDVFact(myDBSession, aFact, mySalon.getMessagesBundle());
                    if (aRDVFact.getDate().getTime().after(dtDebut.getTime())
                        && aRDVFact.getDate().getTime().before(dtFin.getTime())) {
                            
                        // Le RDV est bien dans l'interval...
                        lstLignes.add(aRDVFact);
                    }
                }
            }
            aRS.close();

            // Stocke le Vector pour le JSP
            request.setAttribute("Liste", new Vector(lstLignes));

            if ((action != null) && (action.equals("Duplication"))) {
                // Duplication de toutes les factures
                Iterator iterRDV = lstLignes.iterator();
                while (iterRDV.hasNext()) {
                    RDVFact aRDVFact = (RDVFact) iterRDV.next();
                    if ((aRDVFact.getRdv() != null) && (aRDVFact.getFact() == null)) {
                        mySalon.addClient(Long.toString(aRDVFact.getClient().getCD_CLI()));        
                    }
                }
            }
            else if ((action != null) && (action.equals("Accueil"))) {
                // Création de factures vides
                Iterator iterRDV = lstLignes.iterator();
                while (iterRDV.hasNext()) {
                    RDVFact aRDVFact = (RDVFact) iterRDV.next();
                    if ((aRDVFact.getRdv() != null) && (aRDVFact.getFact() == null)) {
                        mySalon.addEmptyFact(Long.toString(aRDVFact.getClient().getCD_CLI()));        
                    }
                }
            }
             
            // Passe la main
            getServletConfig().getServletContext().getRequestDispatcher("/lstRDV.jsp").forward(request, response);

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
     * Valeur par défaut de la date de fin
     * @param jour Jour de référence
     * @return Date construite
     */
    protected Calendar dtFinDefault(Date jour) {
        // J+1
        Calendar J = Calendar.getInstance();
        J.setTime(jour);
        J.setTimeZone(RDVBean.getTimeZone());
        J.set(Calendar.HOUR_OF_DAY, 23);
        J.set(Calendar.MINUTE, 59);
        J.set(Calendar.SECOND, 59);
        J.set(Calendar.MILLISECOND, 999);
        // Force le recalcul avec le TZ
        J.getTime();
        return J;
    }
    /**
     * Valeur par défaut de la date de fin
     * @return Date construite
     */
    protected Calendar dtFinDefault() {
        return dtFinDefault(new Date());
    }
    /**
     * Valeur par défaut de la date de début
     * @return Date construite
     */
    protected Calendar dtDebutDefaut() {
        // J
        Calendar J = Calendar.getInstance(RDVBean.getTimeZone());
        J.set(Calendar.HOUR_OF_DAY, J.get(Calendar.HOUR_OF_DAY) - 1);
        int offsetTZ = Calendar.getInstance().get(Calendar.ZONE_OFFSET)
                        + Calendar.getInstance().get(Calendar.DST_OFFSET);
        J.setTime(new Date(J.getTime().getTime() + offsetTZ));
//        J.set(Calendar.MINUTE, 0);
//        J.set(Calendar.SECOND, 0);
//        J.set(Calendar.MILLISECOND, 0);
        return J;
    }
}
