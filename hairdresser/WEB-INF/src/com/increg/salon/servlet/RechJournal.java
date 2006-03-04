package com.increg.salon.servlet;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.increg.commun.DBSession;
import com.increg.salon.bean.ModReglBean;
import com.increg.salon.bean.MvtCaisseBean;
import com.increg.salon.bean.SalonSession;
import com.increg.salon.request.Journal;
import com.increg.util.ServletUtil;
/**
 * Recherche/Liste de Brouillards
 * Creation date: (15/10/2001 14:09:47)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class RechJournal extends ConnectedServlet {
/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(HttpServletRequest request, HttpServletResponse response) {

	// Récupération des paramètres
	String DT_DEBUT = request.getParameter("DT_DEBUT");
	String DT_FIN = request.getParameter("DT_FIN");
	String CD_MOD_REGL = request.getParameter("CD_MOD_REGL");

    // Récupère la connexion
    HttpSession mySession = request.getSession(false);
    SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
    DBSession myDBSession = mySalon.getMyDBSession();
    DateFormat formatDate = new SimpleDateFormat(mySalon.getMessagesBundle().getString("format.dateSimpleDefaut"));

	// Valeurs par défaut
    Calendar dtJour = Calendar.getInstance();
    Calendar dtDebut = ServletUtil.interpreteDate(DT_DEBUT, formatDate, dtJour);
    Calendar dtFin = ServletUtil.interpreteDate(DT_FIN, formatDate, dtJour);
    if (dtFin.before(dtDebut)) {
        dtFin = dtDebut;
    }
    request.setAttribute("DT_DEBUT", dtDebut);
    request.setAttribute("DT_FIN", dtFin);
    DT_DEBUT = myDBSession.getFormatDate().format(dtDebut.getTime());
    DT_FIN = myDBSession.getFormatDate().format(dtFin.getTime());
	if (CD_MOD_REGL == null) {
		CD_MOD_REGL = Integer.toString(ModReglBean.MOD_REGL_ESP);
	}
	request.setAttribute("CD_MOD_REGL", CD_MOD_REGL);

	// Constitue les requetes SQL
	String reqSQL_Mvt_Caisse = "select date_trunc('day', DT_MVT)::date as DT_MVT_JOUR, DT_MVT, SENS_MCA, TYP_MCA.CD_TYP_MCA, LIB_TYP_MCA, "
				+ "MONTANT, SOLDE_AVANT, CD_MOD_REGL "
				+ "from MVT_CAISSE, TYP_MCA "
				+ "where MVT_CAISSE.CD_TYP_MCA = TYP_MCA.CD_TYP_MCA ";

	if ((CD_MOD_REGL != null) && (CD_MOD_REGL.length() > 0)) {
		if (Integer.parseInt(CD_MOD_REGL) == ModReglBean.MOD_REGL_CHQ) {
			CD_MOD_REGL = CD_MOD_REGL + "," + Integer.toString(ModReglBean.MOD_REGL_CHQ_FRF);
		}
		else if (Integer.parseInt(CD_MOD_REGL) == ModReglBean.MOD_REGL_ESP) {
			CD_MOD_REGL = CD_MOD_REGL + "," + Integer.toString(ModReglBean.MOD_REGL_ESP_FRF);
		}
		reqSQL_Mvt_Caisse = reqSQL_Mvt_Caisse + " and MVT_CAISSE.CD_MOD_REGL in (" + CD_MOD_REGL + ")";
	}
	if ((DT_DEBUT != null) && (DT_DEBUT.length() > 0)) {
		reqSQL_Mvt_Caisse = reqSQL_Mvt_Caisse + " and DT_MVT >= '" + DT_DEBUT + "'";
	}
	if ((DT_FIN != null) && (DT_FIN.length() > 0)) {
		reqSQL_Mvt_Caisse = reqSQL_Mvt_Caisse + " and DT_MVT < '" + DT_FIN + "'::date + 1";
	}
	reqSQL_Mvt_Caisse = reqSQL_Mvt_Caisse + " order by DT_MVT, LIB_TYP_MCA, TYP_MCA.CD_TYP_MCA, SENS_MCA";

	TreeMap lstLignes = new TreeMap();
	TreeMap lstType = new TreeMap();
	Journal journalTotal = new Journal();
	try {
		ResultSet aRS_Solde = myDBSession.doRequest(reqSQL_Mvt_Caisse);
		java.util.Date date_avant = new java.util.Date(0);
		HashSet typePaiement = new HashSet();

		while (aRS_Solde.next()) {
			// Récupère l'élément de la liste
			java.util.Date date = aRS_Solde.getDate("DT_MVT_JOUR");
			Integer CdModRegl = new Integer(aRS_Solde.getInt("CD_MOD_REGL"));

            Journal aJournal = (Journal) lstLignes.get(date);

            if (aJournal == null) {
                aJournal = new Journal();
                Calendar aCal = Calendar.getInstance();
                aCal.setTime(date);
                aJournal.setDT_PAIEMENT(aCal);
            }

            // Positionne les infos
            int cd = aRS_Solde.getInt("CD_TYP_MCA");
            BigDecimal montant = aRS_Solde.getBigDecimal("MONTANT", 2);
            // Ajoute le mouvement aux totaux
            aJournal.addSORTIE(cd, montant);
            aJournal.setSENS(cd, aRS_Solde.getString("SENS_MCA"));

            // Calcul le total
            journalTotal.addSORTIE(cd, montant);

            // Ajoute l'element modifié
            lstLignes.put(date, aJournal);
            lstType.put(new Integer(cd), aRS_Solde.getString("LIB_TYP_MCA"));  
            
			if ((!date.equals(date_avant)) || (!typePaiement.contains(CdModRegl))) {
				// Positionne les infos
				BigDecimal valeur = aRS_Solde.getBigDecimal("SOLDE_AVANT", 2);
				aJournal.setSOLDE_INIT(aJournal.getSOLDE_INIT().add(valeur));
                aJournal.setSOLDE_FINAL(aJournal.getSOLDE_INIT());
                
				// Ajoute l'element modifié
				lstLignes.put(date, aJournal);

				if (!date.equals(date_avant)) {
					// Changement de date

					// RAZ des variables pour un nouveau tour
					date_avant = date;
					typePaiement.clear();
				}
				typePaiement.add(CdModRegl);
				/**
				 * Attention à ne pas ajouter deux fois le solde CHQ et ESP
				 */
				if (CdModRegl.intValue() == ModReglBean.MOD_REGL_CHQ) {
					typePaiement.add(new Integer(ModReglBean.MOD_REGL_CHQ_FRF));
				}
				else if (CdModRegl.intValue() == ModReglBean.MOD_REGL_CHQ_FRF) {
					typePaiement.add(new Integer(ModReglBean.MOD_REGL_CHQ));
				}
				else if (CdModRegl.intValue() == ModReglBean.MOD_REGL_ESP) {
					typePaiement.add(new Integer(ModReglBean.MOD_REGL_ESP_FRF));
				}
				else if (CdModRegl.intValue() == ModReglBean.MOD_REGL_ESP_FRF) {
					typePaiement.add(new Integer(ModReglBean.MOD_REGL_ESP));
				}
			}

            aJournal.setSOLDE_FINAL(MvtCaisseBean.calculSolde(myDBSession, cd, aJournal.getSOLDE_FINAL(), montant));
            lstLignes.put(date, aJournal);

		}
		aRS_Solde.close();
	}
	catch (Exception e) {
		System.out.println ("Erreur dans performTask (Partie Solde) : " + e.toString());
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
		request.setAttribute("Liste", lstLignes);
		request.setAttribute("ListeType", lstType);
		request.setAttribute("Total", journalTotal);

		// Passe la main
		getServletConfig().getServletContext().getRequestDispatcher("/lstJournal.jsp").forward(request, response);

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
