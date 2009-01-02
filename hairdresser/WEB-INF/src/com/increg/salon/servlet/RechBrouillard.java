/*
 * Recherche/Liste de Brouillards
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

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.increg.commun.DBSession;
import com.increg.salon.bean.ParamBean;
import com.increg.salon.bean.SalonSession;
import com.increg.salon.bean.TypMcaBean;
import com.increg.salon.request.Brouillard;
import com.increg.util.ServletUtil;

public class RechBrouillard extends ConnectedServlet {

	protected static Log log = LogFactory.getLog(RechBrouillard.class);

/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) {


	// Récupération des paramètres
	String DT_DEBUT = request.getParameter("DT_DEBUT");
	String DT_FIN = request.getParameter("DT_FIN");

    // Récupère la connexion
    HttpSession mySession = request.getSession(false);
    SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
    DBSession myDBSession = mySalon.getMyDBSession();
    DateFormat formatDate = new SimpleDateFormat(mySalon.getMessagesBundle().getString("format.dateSimpleDefaut"));

    // J-7
    Calendar J7 = Calendar.getInstance();
    J7.add(Calendar.DAY_OF_YEAR, -7);
    Calendar dtDebut = ServletUtil.interpreteDate(DT_DEBUT, formatDate, J7);
    Calendar dtFin = ServletUtil.interpreteDate(DT_FIN, formatDate, Calendar.getInstance());
    if (dtFin.before(dtDebut)) {
        dtFin = dtDebut;
    }
    request.setAttribute("DT_DEBUT", dtDebut);
    request.setAttribute("DT_FIN", dtFin);
    DT_DEBUT = myDBSession.getFormatDate().format(dtDebut.getTime());
    DT_FIN = myDBSession.getFormatDate().format(dtFin.getTime());

    TreeMap lstLignes = new TreeMap();
    TreeMap lstType = new TreeMap();
    TreeMap lstTypeRem = new TreeMap();
    TreeMap lstTypeMca = new TreeMap();
    Brouillard brouillardTotal = new Brouillard();

    try {
    	// TODO : Passage au format date => Attention au null
        rechercheBrouillard(myDBSession, DT_DEBUT, DT_FIN, lstLignes, lstType, lstTypeRem, lstTypeMca, brouillardTotal);
    }
    catch (Exception e) {
        log.error("Erreur dans le brouillard : ", e);
        try {
            response.sendError(500);
            return;
        }
        catch (Exception e2) {
            log.error("Erreur sur sendError : ", e2);
        }
    }
    
	try {
		// Stocke les Map pour le JSP
		request.setAttribute("Liste", lstLignes);
		request.setAttribute("ListeType", lstType);
        request.setAttribute("ListeTypeRem", lstTypeRem);
		request.setAttribute("ListeTypeMca", lstTypeMca);
		request.setAttribute("Total", brouillardTotal);

		// Passe la main
		getServletConfig().getServletContext().getRequestDispatcher("/lstBrouillard.jsp").forward(request, response);

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
 * Effectue la recherche et consitue le brouillard
 * @param myDBSession Connexion à la base à utiliser
 * @param DT_DEBUT Début du brouillard (inclut)
 * @param DT_FIN Fin du brouillard (inclut)
 * @param lstLignes Lignes de brouillard constituées
 * @param lstType Liste des types de vente
 * @param lstTypeRem Liste des types de vente pour les remises
 * @param lstTypeMca Liste des modes de règlement
 * @param brouillardTotal Ligne de total du brouillard
 * @throws Exception En cas d'erreur (SQL)
 */
public static void rechercheBrouillard (DBSession myDBSession, String DT_DEBUT, String DT_FIN, TreeMap lstLignes, TreeMap lstType, TreeMap lstTypeRem, TreeMap lstTypeMca, Brouillard brouillardTotal) 
    throws Exception {
    
    // Constitue les requetes SQL
    String reqSQL_Paiement_Ventil = "select DT_PAIEMENT, PREST.CD_TYP_VENT, LIB_TYP_VENT, "
                + "sum(QTE*HISTO_PREST.PRX_UNIT_TTC) as MONTANT "
                + "from FACT, PAIEMENT, HISTO_PREST, PREST, TYP_VENT "
                + "where HISTO_PREST.CD_PREST = PREST.CD_PREST "
                + "and TYP_VENT.CD_TYP_VENT = PREST.CD_TYP_VENT "
                + "and FACT.CD_FACT = HISTO_PREST.CD_FACT "
                + "and FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT";

//    String reqSQL_Paiement_Remise = "select DT_PAIEMENT, "
//                + "sum(coalesce(REMISE_FIXE,0) "
//                + "+ round((FACT.PRX_TOT_TTC + coalesce(REMISE_FIXE,0))*(coalesce(REMISE_PRC,0)/100)/(1-coalesce(REMISE_PRC,0)/100),2)) as MONTANT "
//                + "from FACT, PAIEMENT "
//                + "where FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT";

    String reqSQL_Paiement_Remise = null;
    ParamBean repartRemise = ParamBean.getParamBean(myDBSession, Integer.toString(ParamBean.CD_REPART_REMISE));
    if ((repartRemise == null) || (repartRemise.getVAL_PARAM().equals("P"))) {
        // Si répartition proportionnelle
        reqSQL_Paiement_Remise = "select DT_PAIEMENT, PREST.CD_TYP_VENT, LIB_TYP_VENT,"
                    + "sum(QTE*HISTO_PREST.PRX_UNIT_TTC"
                    + "   *(coalesce(REMISE_PRC,0)/100 "
                    + "    + case when ((REMISE_PRC = 100) or ((coalesce(REMISE_FIXE,0) + FACT.PRX_TOT_TTC) = 0))"
                    + "      then 0"
                    + "      else coalesce(REMISE_FIXE,0)"                    + "              /((coalesce(REMISE_FIXE,0)+FACT.PRX_TOT_TTC)/(1 - coalesce(REMISE_PRC,0)/100)) "
                    + "      end"
                    + "   ))::numeric(8,2) as MONTANT "
                    + "from FACT, PAIEMENT, HISTO_PREST, PREST, TYP_VENT "
                    + "where FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT "
                    + "and FACT.CD_FACT = HISTO_PREST.CD_FACT "
                    + "and TYP_VENT.CD_TYP_VENT = PREST.CD_TYP_VENT "
                    + "and PREST.CD_PREST = HISTO_PREST.CD_PREST";
    }
    else {
        // Si répartition fixe
        reqSQL_Paiement_Remise = "select DT_PAIEMENT, FACT.CD_TYP_VENT, LIB_TYP_VENT,"
                    + "sum(coalesce(REMISE_FIXE,0) "
                    + "+ case when (REMISE_PRC = 100) "
                    + "  then 0"
                    + "  else round((FACT.PRX_TOT_TTC + coalesce(REMISE_FIXE,0))"
                    + "                                 *(coalesce(REMISE_PRC,0)/100)"
                    + "                                 /(1-coalesce(REMISE_PRC,0)/100),2)"
                    + "  end"
                    + "  ) as MONTANT "
                    + "from FACT, PAIEMENT, TYP_VENT "
                    + "where FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT "
                    + "and TYP_VENT.CD_TYP_VENT = FACT.CD_TYP_VENT ";
    }


    String reqSQL_Mvt_Encaissement = "select date_trunc('day', DT_MVT)::date as DT_MVT_JOUR, MVT_CAISSE.CD_MOD_REGL, LIB_MOD_REGL, sum(MONTANT) as MONTANT "
                + "from MVT_CAISSE, TYP_MCA, MOD_REGL "
                + "where MVT_CAISSE.CD_TYP_MCA = TYP_MCA.CD_TYP_MCA "
                + "and MOD_REGL.CD_MOD_REGL = MVT_CAISSE.CD_MOD_REGL "
                + "and MVT_CAISSE.CD_TYP_MCA=" + TypMcaBean.ENCAISSEMENT;

    if ((DT_DEBUT != null) && (DT_DEBUT.length() > 0)) {
        reqSQL_Paiement_Ventil = reqSQL_Paiement_Ventil + " and PAIEMENT.DT_PAIEMENT >= '" + DT_DEBUT + "'";
        reqSQL_Paiement_Remise = reqSQL_Paiement_Remise + " and PAIEMENT.DT_PAIEMENT >= '" + DT_DEBUT + "'";
        reqSQL_Mvt_Encaissement = reqSQL_Mvt_Encaissement + " and DT_MVT >= '" + DT_DEBUT + "'";
    }
    if ((DT_FIN != null) && (DT_FIN.length() > 0)) {
        reqSQL_Paiement_Ventil = reqSQL_Paiement_Ventil + " and PAIEMENT.DT_PAIEMENT <= '" + DT_FIN + "'";
        reqSQL_Paiement_Remise = reqSQL_Paiement_Remise + " and PAIEMENT.DT_PAIEMENT <= '" + DT_FIN + "'";
        reqSQL_Mvt_Encaissement = reqSQL_Mvt_Encaissement + " and DT_MVT < '" + DT_FIN + "'::date + 1";
    }
    reqSQL_Paiement_Ventil = reqSQL_Paiement_Ventil + " group by DT_PAIEMENT, PREST.CD_TYP_VENT, LIB_TYP_VENT";
    if ((repartRemise == null) || (repartRemise.getVAL_PARAM().equals("P"))) {
        // Si répartition proportionnelle
        reqSQL_Paiement_Remise = reqSQL_Paiement_Remise + " group by DT_PAIEMENT, PREST.CD_TYP_VENT, LIB_TYP_VENT";
    }
    else {
        reqSQL_Paiement_Remise = reqSQL_Paiement_Remise + " group by DT_PAIEMENT, FACT.CD_TYP_VENT, LIB_TYP_VENT";
    }
    reqSQL_Mvt_Encaissement = reqSQL_Mvt_Encaissement + " group by DT_MVT_JOUR, MVT_CAISSE.CD_MOD_REGL, LIB_MOD_REGL";

    // Interroge la Base
    try {
        ResultSet aRS_Paiement_Ventil = myDBSession.doRequest(reqSQL_Paiement_Ventil);

        while (aRS_Paiement_Ventil.next()) {
            // Récupère l'élément de la liste
            java.util.Date date = aRS_Paiement_Ventil.getDate("DT_PAIEMENT");

            Brouillard aBrouillard = (Brouillard) lstLignes.get(date);

            if (aBrouillard == null) {
                aBrouillard = new Brouillard();
                Calendar aCal = Calendar.getInstance();
                aCal.setTime(date);
                aBrouillard.setDT_PAIEMENT(aCal);
            }

            // Positionne les infos
            int cd = aRS_Paiement_Ventil.getInt("CD_TYP_VENT");
            BigDecimal montant = aRS_Paiement_Ventil.getBigDecimal("MONTANT", 2);
            
            aBrouillard.setENTREE(cd, montant);

            // Calcul le total
            if (brouillardTotal.getENTREE(cd) == null) {
                brouillardTotal.setENTREE(cd, montant);
            }
            else {
                brouillardTotal.setENTREE(cd, montant.add(brouillardTotal.getENTREE(cd)));
            }
                
            // Ajoute l'element modifié
            lstLignes.put(date, aBrouillard);

            lstType.put(new Integer(aRS_Paiement_Ventil.getInt("CD_TYP_VENT")), aRS_Paiement_Ventil.getString("LIB_TYP_VENT"));
        }
        aRS_Paiement_Ventil.close();
    }
    catch (Exception e) {
        log.error("Erreur dans performTask (Partie Entrée) : ", e);
        throw (e);
    }
        
    try {
        ResultSet aRS_Paiement_Remise = myDBSession.doRequest(reqSQL_Paiement_Remise);

        while (aRS_Paiement_Remise.next()) {
            // Récupère l'élément de la liste
            java.util.Date date = aRS_Paiement_Remise.getDate("DT_PAIEMENT");

            Brouillard aBrouillard = (Brouillard) lstLignes.get(date);

            if (aBrouillard == null) {
                aBrouillard = new Brouillard();
                Calendar aCal = Calendar.getInstance();
                aCal.setTime(date);
                aBrouillard.setDT_PAIEMENT(aCal);
            }

            // Positionne les infos
            int cd = aRS_Paiement_Remise.getInt("CD_TYP_VENT");
            BigDecimal montant = aRS_Paiement_Remise.getBigDecimal("MONTANT", 2);
            
            if ((montant != null) && (montant.compareTo(new BigDecimal("0.00")) > 0)) {
                aBrouillard.setREMISE(cd, montant);
    
                // Calcul le total
                if (brouillardTotal.getREMISE(cd) == null) {
                    brouillardTotal.setREMISE(cd, montant);
                }
                else {
                    brouillardTotal.setREMISE(cd, montant.add(brouillardTotal.getREMISE(cd)));
                }
                    
                // Ajoute l'element modifié
                lstLignes.put(date, aBrouillard);
    
                lstTypeRem.put(new Integer(aRS_Paiement_Remise.getInt("CD_TYP_VENT")), aRS_Paiement_Remise.getString("LIB_TYP_VENT"));
            }
        }
        aRS_Paiement_Remise.close();
    }
    catch (Exception e) {
        log.error("Erreur dans performTask (Partie Remise) : ", e);
        throw(e);
    }
        
    try {
        ResultSet aRS_Mvt_Encaissement = myDBSession.doRequest(reqSQL_Mvt_Encaissement);

        while (aRS_Mvt_Encaissement.next()) {
            // Récupère l'élément de la liste
            java.util.Date date = aRS_Mvt_Encaissement.getDate("DT_MVT_JOUR");

            Brouillard aBrouillard = (Brouillard) lstLignes.get(date);

            if (aBrouillard == null) {
                aBrouillard = new Brouillard();
                Calendar aCal = Calendar.getInstance();
                aCal.setTime(date);
                aBrouillard.setDT_PAIEMENT(aCal);
            }

            int cd = aRS_Mvt_Encaissement.getInt("CD_MOD_REGL");
            BigDecimal montant = aRS_Mvt_Encaissement.getBigDecimal("MONTANT", 2);

            // Positionne les infos
            aBrouillard.setSORTIE(cd, montant);

            // Calcul le total
            if (brouillardTotal.getSORTIE(cd) == null) {
                brouillardTotal.setSORTIE(cd, montant);
            }
            else {
                brouillardTotal.setSORTIE(cd, montant.add(brouillardTotal.getSORTIE(cd)));
            }
                
            // Ajoute l'element modifié
            lstLignes.put(date, aBrouillard);

            lstTypeMca.put(new Integer(cd), aRS_Mvt_Encaissement.getString("LIB_MOD_REGL"));
        }
        aRS_Mvt_Encaissement.close();
    }
    catch (Exception e) {
        log.error("Erreur dans performTask (Partie Encaissement) : ", e);
        throw(e);
    }
}
}
