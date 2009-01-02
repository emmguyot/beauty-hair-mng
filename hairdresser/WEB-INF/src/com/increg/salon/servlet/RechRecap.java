/*
 * Recherche/Liste de récap de stock
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.increg.commun.DBSession;
import com.increg.salon.bean.MvtStkBean;
import com.increg.salon.bean.SalonSession;
import com.increg.salon.bean.TypMvtBean;
import com.increg.salon.request.Recap;
import com.increg.util.ServletUtil;

public class RechRecap extends ConnectedServlet {
    /**
     * @see com.increg.salon.servlet.ConnectedServlet
     */
    public void performTask(HttpServletRequest request, HttpServletResponse response) {

    	Log log = LogFactory.getLog(this.getClass());

        // Récupération des paramètres
        String DT_DEBUT = request.getParameter("DT_DEBUT");
        String DT_FIN = request.getParameter("DT_FIN");
        String CD_TYP_MVT[] = request.getParameterValues("CD_TYP_MVT");

        boolean all = false;//Permet de savoir si il faut cocher toutes les cases ou non

        // Récupère la connexion
        HttpSession mySession = request.getSession(false);
        SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
        DBSession myDBSession = mySalon.getMyDBSession();
        DateFormat formatDate = new SimpleDateFormat(mySalon.getMessagesBundle().getString("format.dateSimpleDefaut"));

        // Valeurs par défaut
        if ((DT_DEBUT == null) || (DT_FIN == null)) {
            all = true;
        }
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

        // Constitue les requetes SQL (Hors Inventaire et Avec Inventaire)
        String reqSQL = "select ART.CD_ART, LIB_ART, TYP_MVT.CD_TYP_MVT, " + "LIB_TYP_MVT, ";
        String reqSQL_INV = "select ART.CD_ART, LIB_ART, TYP_MVT.CD_TYP_MVT, " + "LIB_TYP_MVT, ";

        reqSQL = reqSQL + "sum(QTE) as TOTAL, sum(QTE*VAL_MVT_HT) as TOTALV ";
        reqSQL_INV = reqSQL_INV + "QTE as TOTAL, QTE*VAL_MVT_HT as TOTALV ";

        reqSQL = reqSQL + "from MVT_STK, ART, TYP_MVT "
                        + "where MVT_STK.CD_ART = ART.CD_ART "
                        + "and TYP_MVT.CD_TYP_MVT = MVT_STK.CD_TYP_MVT "
                        + "and SENS_MVT<>'" + TypMvtBean.SENS_INVENTAIRE + "'";
        reqSQL_INV = reqSQL_INV + "from MVT_STK, ART, TYP_MVT "
                        + "where MVT_STK.CD_ART = ART.CD_ART "
                        + "and TYP_MVT.CD_TYP_MVT = MVT_STK.CD_TYP_MVT "
                        + "and SENS_MVT='" + TypMvtBean.SENS_INVENTAIRE + "'";

        if ((DT_DEBUT != null) && (DT_DEBUT.length() > 0)) {
            reqSQL = reqSQL + " and MVT_STK.DT_MVT >= '" + DT_DEBUT + "'";
            reqSQL_INV = reqSQL_INV + " and MVT_STK.DT_MVT >= '" + DT_DEBUT + "'";
        }
        if ((DT_FIN != null) && (DT_FIN.length() > 0)) {
            reqSQL = reqSQL + " and MVT_STK.DT_MVT < '" + DT_FIN + "'::date + 1";
            reqSQL_INV = reqSQL_INV + " and MVT_STK.DT_MVT < '" + DT_FIN + "'::date + 1";
        }
        if (CD_TYP_MVT != null) {
            reqSQL = reqSQL + " and TYP_MVT.CD_TYP_MVT in (";
            reqSQL_INV = reqSQL_INV + " and TYP_MVT.CD_TYP_MVT in (";
            for (int i = 0; i < CD_TYP_MVT.length; i++) {
                reqSQL = reqSQL + CD_TYP_MVT[i] + ",";
                reqSQL_INV = reqSQL_INV + CD_TYP_MVT[i] + ",";
            }
            reqSQL = reqSQL.substring(0, reqSQL.length() - 1) + ")";
            reqSQL_INV = reqSQL_INV.substring(0, reqSQL_INV.length() - 1) + ")";
        }
        reqSQL = reqSQL + " group by LIB_ART, ART.CD_ART, LIB_TYP_MVT, TYP_MVT.CD_TYP_MVT";
        reqSQL_INV = reqSQL_INV + " order by LIB_ART, ART.CD_ART, LIB_TYP_MVT, TYP_MVT.CD_TYP_MVT, DT_MVT";

        TreeMap lstLignes = new TreeMap();
        TreeMap lstType = new TreeMap();
        try {
            ResultSet aRS = myDBSession.doRequest(reqSQL);
            Set lstCles = new HashSet();

            while (aRS.next()) {
                // Récupère l'élément de la liste
                String cle = aRS.getString("LIB_ART") + aRS.getString("CD_ART");
                lstCles.add(cle);

                Recap aRecap = (Recap) lstLignes.get(cle);

                if (aRecap == null) {
                    aRecap = new Recap();
                    aRecap.setCD_ART(aRS.getInt("CD_ART"));
                    aRecap.setLIB_ART(aRS.getString("LIB_ART"));
                }

                // Positionne les infos
                int cd = aRS.getInt("CD_TYP_MVT");
                BigDecimal total = new BigDecimal(aRS.getDouble("TOTAL"));
                total = total.setScale(2, BigDecimal.ROUND_HALF_UP);
                BigDecimal totalV = new BigDecimal(aRS.getDouble("TOTALV"));
                totalV = totalV.setScale(2, BigDecimal.ROUND_HALF_UP);

                aRecap.setMVT(cd, total, totalV);

                // Ajoute l'element modifié
                lstLignes.put(cle, aRecap);

                lstType.put(new Integer(aRS.getInt("CD_TYP_MVT")), aRS.getString("LIB_TYP_MVT"));
            }
            aRS.close();


            // *****************************************************************************************
            // Partie Inventaire
            ResultSet aRS_Inv = myDBSession.doRequest(reqSQL_INV);

            while (aRS_Inv.next()) {
                // Récupère l'élément de la liste
                String cle = aRS_Inv.getString("LIB_ART") + aRS_Inv.getString("CD_ART");
                lstCles.add(cle);

                Recap aRecap = (Recap) lstLignes.get(cle);

                if (aRecap == null) {
                    aRecap = new Recap();
                    aRecap.setCD_ART(aRS_Inv.getInt("CD_ART"));
                    aRecap.setLIB_ART(aRS_Inv.getString("LIB_ART"));
                }

                // Positionne les infos
                int cd = aRS_Inv.getInt("CD_TYP_MVT");
                BigDecimal total = new BigDecimal(aRS_Inv.getDouble("TOTAL"));
                total = total.setScale(2, BigDecimal.ROUND_HALF_UP);
                BigDecimal totalV = new BigDecimal(aRS_Inv.getDouble("TOTALV"));
                totalV = totalV.setScale(2, BigDecimal.ROUND_HALF_UP);

                aRecap.setMVT(cd, total, totalV);

                // Ajoute l'element modifié
                lstLignes.put(cle, aRecap);

                lstType.put(new Integer(aRS_Inv.getInt("CD_TYP_MVT")), aRS_Inv.getString("LIB_TYP_MVT"));
            }
            aRS.close();
            
            // *****************************************************************************************

 
            //Maintenant, mets à jour les informations concernant le stock en fin de periode
            //IL faut refaire une requete ici.
            reqSQL =
                "select MVT_STK.CD_ART, MVT_STK.STK_AVANT, MVT_STK.QTE, MVT_STK.VAL_STK_AVANT,"
                    + "       MVT_STK.VAL_MVT_HT, TYP_MVT.SENS_MVT, ART.LIB_ART"
                    + " from MVT_STK, TYP_MVT, ART "
                    + " where MVT_STK.CD_TYP_MVT = TYP_MVT.CD_TYP_MVT"
                    + "       and MVT_STK.CD_ART = ART.CD_ART";
                                        
            if ((DT_DEBUT != null) && (DT_DEBUT.length() > 0)) {
                reqSQL = reqSQL + " and MVT_STK.DT_MVT >= '" + DT_DEBUT + "'";
            }
            if ((DT_FIN != null) && (DT_FIN.length() > 0)) {
                reqSQL = reqSQL + " and MVT_STK.DT_MVT < '" + DT_FIN + "'::date + 1";
            }
            if (CD_TYP_MVT != null) {
                reqSQL = reqSQL + " and TYP_MVT.CD_TYP_MVT in (";
                for (int i = 0; i < CD_TYP_MVT.length; i++) {
                    reqSQL = reqSQL + CD_TYP_MVT[i] + ",";
                }
                reqSQL = reqSQL.substring(0, reqSQL.length() - 1) + ")";
            }
            
            //On doit ordonner par date de modif pour pouvoir considérer que le dernier mouvement
            reqSQL = reqSQL + " order by MVT_STK.DT_MODIF desc";

            //Execute la requete
            aRS = myDBSession.doRequest(reqSQL);

            // Map pour mémoriser les éléments déjà traités (Optimisation)
            HashMap stockFinPeriode = new HashMap();
            
            while (aRS.next() && (lstCles.size() > 0)) {
                lstType.put(new Integer(Recap.NB_MVT_MAX), mySalon.getMessagesBundle().getString("label.stockFinPeriode"));
                String cle = aRS.getString("LIB_ART") + aRS.getString("CD_ART");

                BigDecimal valeurPeriode = (BigDecimal) stockFinPeriode.get(cle);
                if (valeurPeriode == null) {
                    //recap existe deja forcement, on le modifie
                    Recap aRecap = (Recap) lstLignes.get(cle);

                    //On n'a pas encore traite cet objet
                    BigDecimal stockAvant = aRS.getBigDecimal("STK_AVANT", 2);
                    BigDecimal valeurAvant = aRS.getBigDecimal("VAL_STK_AVANT", 2);
                    BigDecimal quantiteMouvement = aRS.getBigDecimal("QTE", 2);
                    BigDecimal valeurMouvement = aRS.getBigDecimal("VAL_MVT_HT", 2);

                    String typeMvt = aRS.getString("SENS_MVT");
                    MvtStkBean.Stock stock = MvtStkBean.calculStock(quantiteMouvement, stockAvant, valeurMouvement, valeurAvant, typeMvt);

                    //C'est la valeur monnetaire qui nous interesse
                    valeurPeriode = stock.getValeur().multiply(stock.getQte());
                    //On veut la quantité en stock
                    BigDecimal stockPeriode = stock.getQte();

                    //On a traite l'objet identifié par sa cle, on
                    //peut le supprimer de la liste de cles
                    lstCles.remove(cle);
                    valeurPeriode = valeurPeriode.setScale(2, BigDecimal.ROUND_HALF_UP);
                    stockFinPeriode.put(cle, stockPeriode);
                    aRecap.setMVT(Recap.NB_MVT_MAX, stockPeriode, valeurPeriode);

                }
            }

            //Maintenant, il faut regarder si toutes les cles ont ete traitees.
            if (lstCles.size() != 0) {
                throw new Exception("Incohérence dans la base");
            }

            aRS.close();

        }
        catch (Exception e) {
            log.error("Erreur dans performTask : ", e);
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
            request.setAttribute("cocheTout", new Boolean(all));

            // Passe la main
            getServletConfig().getServletContext().getRequestDispatcher("/lstRecap.jsp").forward(request, response);

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
}
