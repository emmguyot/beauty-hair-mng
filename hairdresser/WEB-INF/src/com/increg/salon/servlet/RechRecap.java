package com.increg.salon.servlet;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.increg.commun.DBSession;
import com.increg.salon.bean.MvtStkBean;
import com.increg.salon.bean.SalonSession;
import com.increg.salon.bean.TypMvtBean;
import com.increg.salon.request.Recap;

/**
 * Recherche/Liste de r�cap de stock
 * Creation date: (02/11/2001 17:28:45)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class RechRecap extends ConnectedServlet {
    /**
     * @see com.increg.salon.servlet.ConnectedServlet
     */
    public void performTask(HttpServletRequest request, HttpServletResponse response) {

        // R�cup�ration des param�tres
        String DT_DEBUT = request.getParameter("DT_DEBUT");
        String DT_FIN = request.getParameter("DT_FIN");
        String CD_TYP_MVT[] = request.getParameterValues("CD_TYP_MVT");

        boolean all = false;//Permet de savoir si il faut cocher toutes les cases ou non

        DateFormat formatDate = DateFormat.getDateInstance(DateFormat.SHORT);

        // R�cup�re la connexion
        HttpSession mySession = request.getSession(false);
        SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
        DBSession myDBSession = mySalon.getMyDBSession();

        // Valeurs par d�faut
        if (DT_DEBUT == null) {
            // D�but de mois
            Calendar J7 = Calendar.getInstance();
            J7.add(Calendar.DAY_OF_YEAR, 1 - J7.get(Calendar.DAY_OF_MONTH));
            DT_DEBUT = formatDate.format(J7.getTime());
            all = true;
        }
        if (DT_FIN == null) {
            DT_FIN = formatDate.format(Calendar.getInstance().getTime());
            all = true;
        }
        try {
            if ((DT_DEBUT != null) && (DT_DEBUT.length() > 0)) {
                request.setAttribute("DT_DEBUT", formatDate.parse(DT_DEBUT));
            }
            if ((DT_FIN != null) && (DT_FIN.length() > 0)) {
                request.setAttribute("DT_FIN", formatDate.parse(DT_FIN));
            }
        }
        catch (ParseException e) {
            mySalon.setMessage("Erreur", e.toString());
            e.printStackTrace();
        }

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
                // R�cup�re l'�l�ment de la liste
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

                // Ajoute l'element modifi�
                lstLignes.put(cle, aRecap);

                lstType.put(new Integer(aRS.getInt("CD_TYP_MVT")), aRS.getString("LIB_TYP_MVT"));
            }
            aRS.close();


            // *****************************************************************************************
            // Partie Inventaire
            ResultSet aRS_Inv = myDBSession.doRequest(reqSQL_INV);

            while (aRS_Inv.next()) {
                // R�cup�re l'�l�ment de la liste
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

                // Ajoute l'element modifi�
                lstLignes.put(cle, aRecap);

                lstType.put(new Integer(aRS_Inv.getInt("CD_TYP_MVT")), aRS_Inv.getString("LIB_TYP_MVT"));
            }
            aRS.close();
            
            // *****************************************************************************************

 
            //Maintenant, mets � jour les informations concernant le stock en fin de periode
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
            
            //On doit ordonner par date de modif pour pouvoir consid�rer que le dernier mouvement
            reqSQL = reqSQL + " order by MVT_STK.DT_MODIF desc";

            //Execute la requete
            aRS = myDBSession.doRequest(reqSQL);

            // Map pour m�moriser les �l�ments d�j� trait�s (Optimisation)
            HashMap stockFinPeriode = new HashMap();
            
            while (aRS.next() && (lstCles.size() > 0)) {
                lstType.put(new Integer(Recap.NB_MVT_MAX), "Stock en fin de p�riode");
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
                    //On veut la quantit� en stock
                    BigDecimal stockPeriode = stock.getQte();

                    //On a traite l'objet identifi� par sa cle, on
                    //peut le supprimer de la liste de cles
                    lstCles.remove(cle);
                    valeurPeriode = valeurPeriode.setScale(2, BigDecimal.ROUND_HALF_UP);
                    stockFinPeriode.put(cle, stockPeriode);
                    aRecap.setMVT(Recap.NB_MVT_MAX, stockPeriode, valeurPeriode);

                }
            }

            //Maintenant, il faut regarder si toutes les cles ont ete traitees.
            if (lstCles.size() != 0) {
                throw new Exception("Incoh�rence dans la base");
            }

            aRS.close();

        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur dans performTask : " + e.toString());
            try {
                response.sendError(500);
                return;
            }
            catch (Exception e2) {
                System.out.println("Erreur sur sendError : " + e2.toString());
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
            System.out.println("Erreur dans performTask : " + e.toString());
            try {
                response.sendError(500);
            }
            catch (Exception e2) {
                System.out.println("Erreur sur sendError : " + e2.toString());
            }
        }
    }
}
