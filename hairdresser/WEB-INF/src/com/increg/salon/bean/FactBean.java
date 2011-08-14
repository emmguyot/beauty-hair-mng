/*
 * Bean gérant les factures des clients
 * Copyright (C) 2001-2011 Emmanuel Guyot <See emmguyot on SourceForge> 
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
package com.increg.salon.bean;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Vector;

import com.increg.commun.BasicSession;
import com.increg.commun.DBSession;
import com.increg.commun.TimeStampBean;
import com.increg.commun.exception.FctlException;
import com.increg.salon.request.RecapVente;
import com.increg.salon.request.TVA;
import com.increg.util.Montant;
import com.increg.util.NombreDecimal;
import com.increg.util.SimpleDateFormatEG;

/**
 * Gestion d'une facture (entête et pied)
 * Creation date: (17/08/2001 20:08:57)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class FactBean extends TimeStampBean {
    /**
     * Code de la facture
     */
    protected long CD_FACT;
    /**
     * Code du client concerné
     */
    protected long CD_CLI;
    /**
     * Date de la prestation
     */
    protected java.util.Calendar DT_PREST = null;
    /**
     * Collaborateur principal
     */
    protected int CD_COLLAB;
    /**
     * Remise en pourcentage
     */
    protected java.math.BigDecimal REMISE_PRC;
    /**
     * Code de la fiche paiement
     */
    protected long CD_PAIEMENT;
    /**
     * Code de la fiche paiement avant modif
     */
    protected long CD_PAIEMENT_INIT;
    /**
     * Type de vente principal
     */
    protected int CD_TYP_VENT;
    /**
     * Fausse facture pour conserver un historique ?
     */
    protected String FACT_HISTO;
    /**
     * Lignes de facture
     */
    protected Vector<HistoPrestBean> lignes;
    /**
     * Prix total Hors Taxes
     */
    protected java.math.BigDecimal PRX_TOT_HT;
    /**
     * Prix de la facture TTC
     */
    protected java.math.BigDecimal PRX_TOT_TTC;
    /**
     * Remise fixe effectuée
     */
    protected java.math.BigDecimal REMISE_FIXE;
    /**
     * Total des prestations
     */
    protected java.math.BigDecimal totPrest;
    /**
     * Montant de la TVA
     */
    protected java.math.BigDecimal TVA;
    /**
     * Type de répartition de la remise
     */
    protected String repartRemise = null;
    /**
     * Répartition de la TVA entre les différents taux
     * Non stocké en base
     */
    protected HashMap<TvaBean, BigDecimal> repartTVA;
    
    /**
     * FactBean constructor comment.
     * @param rb Messages localisés
     */
    public FactBean(ResourceBundle rb) {
        super(rb);

        // Place la date du jour en date de prestation
        DT_PREST = Calendar.getInstance();
        DT_PREST.clear(Calendar.HOUR_OF_DAY);
        DT_PREST.clear(Calendar.HOUR);
        DT_PREST.clear(Calendar.MINUTE);
        DT_PREST.clear(Calendar.SECOND);
        DT_PREST.clear(Calendar.MILLISECOND);

        CD_PAIEMENT_INIT = CD_PAIEMENT;
        repartTVA = new HashMap<TvaBean, BigDecimal>();
    }
    /**
     * FactBean constructor comment.
     * @param rs java.sql.ResultSet
     * @param rb Messages localisés
     */
    public FactBean(ResultSet rs, ResourceBundle rb) {
        super(rs, rb);
        try {
            CD_CLI = rs.getLong("CD_CLI");
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans FactBean (RS) : " + e.toString());
            }
        }
        try {
            CD_COLLAB = rs.getInt("CD_COLLAB");
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans FactBean (RS) : " + e.toString());
            }
        }
        try {
            CD_FACT = rs.getLong("CD_FACT");
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans FactBean (RS) : " + e.toString());
            }
        }
        try {
            CD_PAIEMENT = rs.getLong("CD_PAIEMENT");
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans FactBean (RS) : " + e.toString());
            }
        }
        try {
            CD_TYP_VENT = rs.getInt("CD_TYP_VENT");
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans FactBean (RS) : " + e.toString());
            }
        }

        try {
            java.util.Date DtPrest = rs.getDate("DT_PREST");
            if (DtPrest != null) {
                DT_PREST = Calendar.getInstance();
                DT_PREST.setTime(DtPrest);
            } else {
                DT_PREST = null;
            }
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans FactBean (RS) : " + e.toString());
            }
        }
        try {
            FACT_HISTO = rs.getString("FACT_HISTO");
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans FactBean (RS) : " + e.toString());
            }
        }
        try {
            PRX_TOT_HT = rs.getBigDecimal("PRX_TOT_HT", 2);
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans FactBean (RS) : " + e.toString());
            }
        }
        try {
            PRX_TOT_TTC = rs.getBigDecimal("PRX_TOT_TTC", 2);
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans ClientBean (RS) : " + e.toString());
            }
        }
        try {
            REMISE_FIXE = rs.getBigDecimal("REMISE_FIXE", 2);
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans FactBean (RS) : <REMISE_FIXE> " + e.toString());
            }
        }
        try {
            REMISE_PRC = rs.getBigDecimal("REMISE_PRC", 2);
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans FactBean (RS) : <REMISE_PRC> " + e.toString());
            }
        }
        try {
            TVA = rs.getBigDecimal("TVA", 2);
        } catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans FactBean (RS) : " + e.toString());
            }
        }

        CD_PAIEMENT_INIT = CD_PAIEMENT;
        repartTVA = new HashMap<TvaBean, BigDecimal>();

    }
    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public void create(DBSession dbConnect) throws SQLException, com.increg.commun.exception.FctlException {

        com.increg.util.SimpleDateFormatEG formatDate = new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");

        StringBuffer req = new StringBuffer("insert into FACT ");
        StringBuffer colonne = new StringBuffer("(");
        StringBuffer valeur = new StringBuffer(" values ( ");

        if (CD_FACT == 0) {
            /**
             * Numérotation automatique des codes factures
             */
            String reqMax = "select nextval('seq_fact')";
            try {
                ResultSet aRS = dbConnect.doRequest(reqMax);
                CD_FACT = 1; // Par défaut

                while (aRS.next()) {
                    CD_FACT = aRS.getLong(1);
                }
                aRS.close();
            } catch (Exception e) {
                System.out.println("Erreur dans reqSeq : " + e.toString());
            }
        }
        colonne.append("CD_FACT,");
        valeur.append(CD_FACT);
        valeur.append(",");

        if (CD_CLI != 0) {
            colonne.append("CD_CLI,");
            valeur.append(CD_CLI);
            valeur.append(",");
        }

        if (CD_COLLAB != 0) {
            colonne.append("CD_COLLAB,");
            valeur.append(CD_COLLAB);
            valeur.append(",");
        }

        if (CD_PAIEMENT != 0) {
            colonne.append("CD_PAIEMENT,");
            valeur.append(CD_PAIEMENT);
            valeur.append(",");
        }

        if (CD_TYP_VENT != 0) {
            colonne.append("CD_TYP_VENT,");
            valeur.append(CD_TYP_VENT);
            valeur.append(",");
        }

        if (DT_PREST != null) {
            colonne.append("DT_PREST,");
            valeur.append(DBSession.quoteWith(formatDate.formatEG(DT_PREST.getTime()), '\''));
            valeur.append(",");
        }

        if ((FACT_HISTO != null) && (FACT_HISTO.length() != 0)) {
            colonne.append("FACT_HISTO,");
            valeur.append(DBSession.quoteWith(FACT_HISTO, '\''));
            valeur.append(",");
        }

        if (PRX_TOT_HT != null) {
            colonne.append("PRX_TOT_HT,");
            valeur.append(PRX_TOT_HT.toString());
            valeur.append(",");
        }

        if (PRX_TOT_TTC != null) {
            colonne.append("PRX_TOT_TTC,");
            valeur.append(PRX_TOT_TTC.toString());
            valeur.append(",");
        }

        if (REMISE_FIXE != null) {
            colonne.append("REMISE_FIXE,");
            valeur.append(REMISE_FIXE.toString());
            valeur.append(",");
        }

        if (REMISE_PRC != null) {
            colonne.append("REMISE_PRC,");
            valeur.append(REMISE_PRC.toString());
            valeur.append(",");
        }

        if (TVA != null) {
            colonne.append("TVA,");
            valeur.append(TVA.toString());
            valeur.append(",");
        }

        if (DT_CREAT != null) {
            colonne.append("DT_CREAT,");
            valeur.append(DBSession.quoteWith(formatDate.formatEG(DT_CREAT.getTime()), '\''));
            valeur.append(",");
        }

        if (DT_MODIF != null) {
            colonne.append("DT_MODIF,");
            valeur.append(DBSession.quoteWith(formatDate.formatEG(DT_MODIF.getTime()), '\''));
            valeur.append(",");
        }

        // retire les dernières virgules
        colonne.setLength(colonne.length() - 1);
        valeur.setLength(valeur.length() - 1);

        // Constitue la requete finale
        req.append(colonne);
        req.append(")");
        req.append(valeur);
        req.append(")");

        // Debut de la transaction
        dbConnect.setDansTransactions(true);

        // Fait les mouvements de stock
        mouvemente(dbConnect, false);

        // Execute la création
        String[] reqs = new String[1];
        reqs[0] = req.toString();
        int[] nb = new int[1];
        nb = dbConnect.doExecuteSQL(reqs);

        if (nb[0] != 1) {
            throw (new SQLException(BasicSession.TAG_I18N + "message.creationKo" + BasicSession.TAG_I18N));
        }

        // Fin de la transaction
        dbConnect.endTransaction();
    }
    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public void delete(DBSession dbConnect) throws SQLException, com.increg.commun.exception.FctlException {

        /**
         * Vérification de la possibilité de supprimer cette facture
         * Elle ne doit pas avoir été réglée
         */
        //if (getCD_MOD_REGL() != 0) {
        //throw (new SQLException("Suppression non effectuée : Cette facture a été payée."));
        //}

        /**
         * Suppression effective
         */

        StringBuffer reqLigne = new StringBuffer("delete from HISTO_PREST ");
        StringBuffer whereLigne = new StringBuffer(" where CD_FACT=" + CD_FACT);

        // Constitue la requete finale
        reqLigne.append(whereLigne);

        StringBuffer req = new StringBuffer("delete from FACT ");
        StringBuffer where = new StringBuffer(" where CD_FACT=" + CD_FACT);

        // Constitue la requete finale
        req.append(where);

        // Debut de la transaction
        dbConnect.setDansTransactions(true);

        // Fait les mouvements de stock
        mouvemente(dbConnect, true);

        try {
            // Execute la création
            String[] reqs = new String[2];
            reqs[0] = reqLigne.toString();
            reqs[1] = req.toString();
            int[] nb = new int[2];
            nb = dbConnect.doExecuteSQL(reqs);

            if (nb[1] != 1) {
                throw (new SQLException(BasicSession.TAG_I18N + "message.suppressionKo" + BasicSession.TAG_I18N));
            }
        } catch (Exception e) {
            // Fin de la transaction
            dbConnect.cleanTransaction();
            throw ((SQLException) e);
        }
        // Fin de la transaction
        dbConnect.endTransaction();

    }
    /**
     * Insert the method's description here.
     * Creation date: (17/08/2001 21:20:23)
     * @return long
     */
    public long getCD_CLI() {
        return CD_CLI;
    }
    /**
     * Insert the method's description here.
     * Creation date: (17/08/2001 21:21:42)
     * @return int
     */
    public int getCD_COLLAB() {
        return CD_COLLAB;
    }
    /**
     * Insert the method's description here.
     * Creation date: (17/08/2001 21:19:23)
     * @return long
     */
    public long getCD_FACT() {
        return CD_FACT;
    }
    /**
     * Insert the method's description here.
     * Creation date: (17/08/2001 21:21:11)
     * @return java.util.Calendar
     */
    public java.util.Calendar getDT_PREST() {
        return DT_PREST;
    }
    /**
     * Insert the method's description here.
     * Creation date: (17/08/2001 21:24:26)
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getREMISE_PRC() {
        return REMISE_PRC;
    }
    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public void maj(DBSession dbConnect) throws SQLException, com.increg.commun.exception.FctlException {

        com.increg.util.SimpleDateFormatEG formatDate = new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");

        StringBuffer req = new StringBuffer("update FACT set ");
        StringBuffer colonne = new StringBuffer("");
        StringBuffer where = new StringBuffer(" where CD_FACT=" + CD_FACT);

        colonne.append("CD_CLI=");
        if (CD_CLI != 0) {
            colonne.append(CD_CLI);
        } else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("CD_COLLAB=");
        if (CD_COLLAB != 0) {
            colonne.append(CD_COLLAB);
        } else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("CD_TYP_VENT=");
        if (CD_TYP_VENT != 0) {
            colonne.append(CD_TYP_VENT);
        } else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("CD_PAIEMENT=");
        if (CD_PAIEMENT != 0) {
            colonne.append(CD_PAIEMENT);
        } else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("DT_PREST=");
        if (DT_PREST != null) {
            colonne.append(DBSession.quoteWith(formatDate.formatEG(DT_PREST.getTime()), '\''));
        } else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("FACT_HISTO=");
        if ((FACT_HISTO != null) && (FACT_HISTO.length() != 0)) {
            colonne.append(DBSession.quoteWith(FACT_HISTO, '\''));
        } else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("PRX_TOT_HT=");
        if (PRX_TOT_HT != null) {
            colonne.append(PRX_TOT_HT.toString());
        } else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("PRX_TOT_TTC=");
        if (PRX_TOT_TTC != null) {
            colonne.append(PRX_TOT_TTC.toString());
        } else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("REMISE_FIXE=");
        if (REMISE_FIXE != null) {
            colonne.append(REMISE_FIXE.toString());
        } else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("REMISE_PRC=");
        if (REMISE_PRC != null) {
            colonne.append(REMISE_PRC.toString());
        } else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("TVA=");
        if (TVA != null) {
            colonne.append(TVA.toString());
        } else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("DT_MODIF=");
        DT_MODIF = Calendar.getInstance();
        colonne.append(DBSession.quoteWith(formatDate.formatEG(DT_MODIF.getTime()), '\''));

        // Constitue la requete finale
        req.append(colonne);
        req.append(where);

        // Debut de la transaction
        dbConnect.setDansTransactions(true);

        // Fait les mouvements de stock
        mouvemente(dbConnect, false);

        // Execute la création
        String[] reqs = new String[1];
        reqs[0] = req.toString();
        int[] nb = new int[1];
        nb = dbConnect.doExecuteSQL(reqs);

        if (nb[0] != 1) {
            throw (new SQLException(BasicSession.TAG_I18N + "message.enregistrementKo" + BasicSession.TAG_I18N));
        }

        // Fin de la transaction
        dbConnect.endTransaction();
    }

    /**
     * Purge des factures
     * @param dbConnect Connexion à la base à utiliser
     * @param dateLimite Date limite de purge : Seront purgés les factures avant cette date qui sont vides
     * @exception FctlException En cas d'erreur durant la mise à jour
     * @return Nombre d'enregistrements purgés : -1 En cas d'erreur
     */
    public static int purge(DBSession dbConnect, java.util.Date dateLimite) throws FctlException {

        int nbEnreg = -1;

        com.increg.util.SimpleDateFormatEG formatDate = new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");

        String[] reqSQL = new String[1];

        reqSQL[0] = "delete from FACT where DT_PREST < " + DBSession.quoteWith(formatDate.format(dateLimite), '\'') + " and (select count(*) from HISTO_PREST where CD_FACT = FACT.CD_FACT) = 0";

        dbConnect.setDansTransactions(true);

        try {
            int[] res = dbConnect.doExecuteSQL(reqSQL);

            nbEnreg = res[0];
        } catch (Exception e) {
            System.out.println("Erreur dans Purge des factures : " + e.toString());
            dbConnect.cleanTransaction();
            throw new FctlException(BasicSession.TAG_I18N + "factBean.purgeKo" + BasicSession.TAG_I18N);
        }

        // Fin de cette transaction
        dbConnect.endTransaction();

        return nbEnreg;
    }

    /**
     * Insert the method's description here.
     * Creation date: (17/08/2001 21:20:23)
     * @param newCD_CLI long
     */
    public void setCD_CLI(long newCD_CLI) {
        CD_CLI = newCD_CLI;
    }
    /**
     * Insert the method's description here.
     * Creation date: (17/08/2001 21:21:42)
     * @param newCD_COLLAB int
     */
    public void setCD_COLLAB(int newCD_COLLAB) {
        CD_COLLAB = newCD_COLLAB;
    }
    /**
     * Insert the method's description here.
     * Creation date: (17/08/2001 21:19:23)
     * @param newCD_FACT long
     */
    public void setCD_FACT(long newCD_FACT) {
        CD_FACT = newCD_FACT;
    }
    /**
     * Insert the method's description here.
     * Creation date: (17/08/2001 21:21:11)
     * @param newDT_PREST java.util.Calendar
     */
    public void setDT_PREST(java.util.Calendar newDT_PREST) {
        DT_PREST = newDT_PREST;
    }
    /**
     * Insert the method's description here.
     * Creation date: (17/08/2001 21:24:26)
     * @param newREMISE_PRC java.math.BigDecimal
     */
    public void setREMISE_PRC(java.math.BigDecimal newREMISE_PRC) {
        REMISE_PRC = newREMISE_PRC;
    }
    /**
     * Calcul le pied de facture compte tenu des lignes de facture en base
     * Creation date: (20/08/2001 21:25:47)
     * @param dbConnect Connexion à la base à utiliser
     * @param txTVA Taux de la TVA à utiliser pour le calcul
     * @exception Exception En cas d'erreur grave
     * @deprecated Cette méthode ne doit plus être utilisée. Le taux de TVA doit être issu du type de vente
     */
    public void calculTotaux(DBSession dbConnect, BigDecimal txTVA) throws Exception {

        totPrest = null;
        getTotPrest(dbConnect);

        if (totPrest == null) {
            totPrest = new BigDecimal(0);
            totPrest.setScale(2);
        }

        BigDecimal remiseTot = new BigDecimal(0);
        // Applique les remises
        if (REMISE_PRC != null) {
            // TOT_TTC = tot*(1-%) <=> TOT_TTC = tot - tot*%
            BigDecimal remise = totPrest.multiply(REMISE_PRC).divide(new BigDecimal(100), BigDecimal.ROUND_HALF_UP);
            remise = remise.setScale(2, BigDecimal.ROUND_HALF_UP);
            PRX_TOT_TTC = totPrest.subtract(remise);
            remiseTot = remiseTot.add(remise);
        } else {
            PRX_TOT_TTC = totPrest;
        }
        if (REMISE_FIXE != null) {
            PRX_TOT_TTC = PRX_TOT_TTC.subtract(REMISE_FIXE);
            remiseTot = remiseTot.add(REMISE_FIXE);
        }

        // Calcul de la TVA
        /**
         * Révision du calcul le 28/12/2001
         * Avant : TVA = TTC*tx/100
         * Après : TVA = TTC*tx/100/(1+tx/100)
         */
        BigDecimal coef = txTVA.setScale(5, BigDecimal.ROUND_HALF_UP).divide(new BigDecimal(100).add(txTVA), BigDecimal.ROUND_HALF_UP);
        TVA = PRX_TOT_TTC.multiply(coef);
        TVA = TVA.setScale(2, BigDecimal.ROUND_HALF_UP);

        // Calcul du HT
        PRX_TOT_HT = PRX_TOT_TTC.subtract(TVA);

        /**
         * Calcul de la TVA par ligne
         */
        getLignes(dbConnect);
        
        // Calcul la somme par type de vente
        HashMap<Integer, BigDecimal> sommeTypeVent = new HashMap<Integer, BigDecimal>();
        Vector<PrestBean> lstPrest = new Vector<PrestBean>();
        for (int i = 0; i < lignes.size(); i++) {
            HistoPrestBean aLigne = (HistoPrestBean) lignes.get(i);
            PrestBean aPrest = PrestBean.getPrestBean(dbConnect, Long.toString(aLigne.getCD_PREST()));
            Integer cdTypVent = new Integer(aPrest.getCD_TYP_VENT());
            
            BigDecimal somme = (BigDecimal) sommeTypeVent.get(cdTypVent);
            if (somme == null) {
                somme = new BigDecimal("0").setScale(2); 
            }
            
            somme = somme.add(aLigne.getQTE().multiply(aLigne.getPRX_UNIT_TTC()));
            sommeTypeVent.put(cdTypVent, somme);
            lstPrest.add(aPrest);
        }
        
        for (int i = 0; i < lignes.size(); i++) {
            HistoPrestBean aLigne = (HistoPrestBean) lignes.get(i);
            PrestBean aPrest = (PrestBean) lstPrest.get(i);
            
            BigDecimal TTC = aLigne.getQTE().multiply(aLigne.getPRX_UNIT_TTC());
            
            aLigne.setTVA(new BigDecimal(0));
            if (getRepartRemise(dbConnect).equals("P")) {
                if (totPrest.compareTo(new BigDecimal("0")) != 0) { 
                    TTC = TTC.subtract(remiseTot.multiply(TTC).divide(totPrest, BigDecimal.ROUND_HALF_UP));
                }
            } else if (CD_TYP_VENT == aPrest.getCD_TYP_VENT()) {
                BigDecimal somme = (BigDecimal) sommeTypeVent.get(new Integer(aPrest.getCD_TYP_VENT()));
                if (somme.compareTo(new BigDecimal("0")) != 0) { 
                    TTC = TTC.subtract(remiseTot.multiply(TTC).divide(somme, BigDecimal.ROUND_HALF_UP));
                }
            }
            aLigne.setTVA(TTC.multiply(coef).setScale(2, BigDecimal.ROUND_HALF_UP));
            aLigne.setPRX_TOT_TTC(TTC.setScale(2, BigDecimal.ROUND_HALF_UP));
            aLigne.setPRX_TOT_HT(TTC.subtract(aLigne.getTVA()));
            
            // Sauvegarde en base
            aLigne.maj(dbConnect);
        }
        
        try {
            // Sauvegarde en base
            maj(dbConnect);

            // Maj du paiement si besoin
            if (getCD_PAIEMENT() != 0) {
                PaiementBean myPaiement = PaiementBean.getPaiementBean(dbConnect, Long.toString(getCD_PAIEMENT()), message);
                myPaiement.calculTotaux(dbConnect);
            }
        } catch (Exception e) {
            System.out.println("FactBean ==> Erreur dans maj & calculTotaux : " + e.toString());
            throw (e);
        }
    }
    /**
     * Calcul le pied de facture compte tenu des lignes de facture en base
     * Creation date: (20/08/2001 21:25:47)
     * @param dbConnect Connexion à la base à utiliser
     * @exception Exception En cas d'erreur grave
     */
    public void calculTotaux(DBSession dbConnect) throws Exception {

        totPrest = null;
        getTotPrest(dbConnect);

        if (totPrest == null) {
            totPrest = new BigDecimal(0);
            totPrest.setScale(2);
        }

        BigDecimal remiseTot = new BigDecimal(0);
        // Applique les remises
        if (REMISE_PRC != null) {
            // TOT_TTC = tot*(1-%) <=> TOT_TTC = tot - tot*%
            BigDecimal remise = totPrest.multiply(REMISE_PRC).divide(new BigDecimal(100), BigDecimal.ROUND_HALF_UP);
            remise = remise.setScale(2, BigDecimal.ROUND_HALF_UP);
            PRX_TOT_TTC = totPrest.subtract(remise);
            remiseTot = remiseTot.add(remise);
        } else {
            PRX_TOT_TTC = totPrest;
        }
        if (REMISE_FIXE != null) {
            PRX_TOT_TTC = PRX_TOT_TTC.subtract(REMISE_FIXE);
            remiseTot = remiseTot.add(REMISE_FIXE);
        }

        // Calcul de la TVA

        /**
         * Calcul de la TVA par ligne
         */
        getLignes(dbConnect);
        
        // Calcul la somme par type de vente
        HashMap<Integer, BigDecimal> sommeTypeVent = new HashMap<Integer, BigDecimal>();
        Vector<PrestBean> lstPrest = new Vector<PrestBean>();
        for (int i = 0; i < lignes.size(); i++) {
            HistoPrestBean aLigne = (HistoPrestBean) lignes.get(i);
            PrestBean aPrest = PrestBean.getPrestBean(dbConnect, Long.toString(aLigne.getCD_PREST()));
            Integer cdTypVent = new Integer(aPrest.getCD_TYP_VENT());
            
            BigDecimal somme = (BigDecimal) sommeTypeVent.get(cdTypVent);
            if (somme == null) {
                somme = new BigDecimal("0").setScale(2); 
            }
            
            somme = somme.add(aLigne.getQTE().multiply(aLigne.getPRX_UNIT_TTC()));
            sommeTypeVent.put(cdTypVent, somme);
            lstPrest.add(aPrest);
        }
        
        TVA = new BigDecimal("0.00");
        for (int i = 0; i < lignes.size(); i++) {
            HistoPrestBean aLigne = (HistoPrestBean) lignes.get(i);
            PrestBean aPrest = (PrestBean) lstPrest.get(i);
            TypVentBean aTypVent = TypVentBean.getTypVentBean(dbConnect, Integer.toString(aPrest.getCD_TYP_VENT()));
            TvaBean aTva = TvaBean.getTvaBean(dbConnect, Integer.toString(aTypVent.getCD_TVA()));
            
            BigDecimal coef = aTva.getTX_TVA().setScale(5, BigDecimal.ROUND_HALF_UP).divide(new BigDecimal(100).add(aTva.getTX_TVA()), BigDecimal.ROUND_HALF_UP);
            
            BigDecimal TTC = aLigne.getQTE().multiply(aLigne.getPRX_UNIT_TTC());
            
            aLigne.setTVA(new BigDecimal(0));
            if (getRepartRemise(dbConnect).equals("P")) {
                if (totPrest.compareTo(new BigDecimal("0")) != 0) { 
                    TTC = TTC.subtract(remiseTot.multiply(TTC).divide(totPrest, BigDecimal.ROUND_HALF_UP));
                }
            } else if (CD_TYP_VENT == aPrest.getCD_TYP_VENT()) {
                BigDecimal somme = (BigDecimal) sommeTypeVent.get(new Integer(aPrest.getCD_TYP_VENT()));
                if (somme.compareTo(new BigDecimal("0")) != 0) { 
                    TTC = TTC.subtract(remiseTot.multiply(TTC).divide(somme, BigDecimal.ROUND_HALF_UP));
                }
            }
            aLigne.setTVA(TTC.multiply(coef).setScale(2, BigDecimal.ROUND_HALF_UP));
            aLigne.setPRX_TOT_TTC(TTC.setScale(2, BigDecimal.ROUND_HALF_UP));
            aLigne.setPRX_TOT_HT(TTC.subtract(aLigne.getTVA()));
            
            // Sauvegarde en base
            aLigne.maj(dbConnect);
            
            // Ajoute la TVA à la facture
            TVA = TVA.add(aLigne.getTVA());
            BigDecimal TVApartielle = (BigDecimal) repartTVA.get(aTva);
            if (TVApartielle == null) {
                TVApartielle = aLigne.getTVA();
            } else {
                TVApartielle = TVApartielle.add(aLigne.getTVA());
            }
            repartTVA.put(aTva, TVApartielle);
            
        }
        
        TVA = TVA.setScale(2, BigDecimal.ROUND_HALF_UP);

        // Calcul du HT
        PRX_TOT_HT = PRX_TOT_TTC.subtract(TVA);

        try {
            // Sauvegarde en base
            maj(dbConnect);
        } catch (Exception e) {
            System.out.println("FactBean ==> Erreur dans maj & calculTotaux : " + e.toString());
            throw (e);
        }
    }
    /**
     * Clone la facture pour en obtenir une nouvelle avec les mêmes prestations
     * mais non payée, et les prix sont ceux du catalogue.
     * Creation date: (09/09/2001 21:36:27)
     * @param dbConnect Connexion à la base à utiliser
     * @return com.increg.salon.bean.FactBean
     */
    public Object clone(DBSession dbConnect) {

        FactBean newFact = new FactBean(message);

        // Copie du corps
        newFact.setCD_CLI(CD_CLI);
        newFact.setCD_COLLAB(CD_COLLAB);
        newFact.setCD_TYP_VENT(CD_TYP_VENT);
        newFact.setFACT_HISTO("N");
        newFact.setREMISE_FIXE(REMISE_FIXE);
        newFact.setREMISE_PRC(REMISE_PRC);

        // Création de la facture en base pour obtenir son numéro
        try {
            newFact.create(dbConnect);

            // Récupère les lignes à dupliquer
            getLignes(dbConnect);

            for (int i = 0; i < lignes.size(); i++) {
                HistoPrestBean aHisto = (HistoPrestBean) lignes.get(i);

                aHisto.setCD_FACT(newFact.getCD_FACT());
                aHisto.setNIV_SATISF(0);
                aHisto.setDT_PREST(newFact.getDT_PREST());
                // Recherche le prix du catalogue
                String reqSQL = "select PRX_UNIT_TTC from PREST where CD_PREST=" + aHisto.getCD_PREST();
                try {
                    ResultSet aRS = dbConnect.doRequest(reqSQL);

                    while (aRS.next()) {
                        aHisto.setPRX_UNIT_TTC(aRS.getBigDecimal("PRX_UNIT_TTC", 2));
                    }
                    aRS.close();
                } catch (Exception e) {
                    System.out.println("Erreur à la recherche du prix catalogue : " + e.toString());
                }

                // Création de l'historique en base
                aHisto.create(dbConnect);
            }

            // Calcul les totaux
            newFact.calculTotaux(dbConnect);
        } catch (Exception e) {
            System.out.println("Erreur à la duplication de la facture : " + e.toString());
            newFact = null;
        }

        return newFact;
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (26/08/2001 21:54:59)
     * @return long
     */
    public long getCD_PAIEMENT() {
        return CD_PAIEMENT;
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (18/08/2001 21:58:06)
     * @return int
     */
    public int getCD_TYP_VENT() {
        return CD_TYP_VENT;
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (17/08/2001 21:28:14)
     * @return char
     */
    public String getFACT_HISTO() {

        if (FACT_HISTO == null) {
            return "";
        } else {
            return FACT_HISTO;
        }
    }
    
    /**
     * Création d'un Bean Facture à partir de sa clé
     * Creation date: (18/08/2001 17:05:45)
     * @param dbConnect com.increg.salon.bean.DBSession
     * @param CD_FACT java.lang.String
     * @param rb Messages localisés
     * @return Facture correspondante au code
     */
    public static FactBean getFactBean(DBSession dbConnect, String CD_FACT, ResourceBundle rb) {
        String reqSQL = "select * from FACT where CD_FACT=" + CD_FACT;
        FactBean res = null;

        // Interroge la Base
        try {
            ResultSet aRS = dbConnect.doRequest(reqSQL);

            while (aRS.next()) {
                res = new FactBean(aRS, rb);
            }
            aRS.close();
        } catch (Exception e) {
            System.out.println("Erreur dans constructeur sur clé : " + e.toString());
        }
        return res;
    }

    /**
     * Donne le nombre de facture du client
     * @param dbConnect
     * @param CD_CLI
     * @param CD_FACT_EXCLUE Facture à exclure
     * @return Nombre
     */
    public static Integer getNbFactBeanByClientBean(DBSession dbConnect, String CD_CLI, String CD_FACT_EXCLUE) {
        String reqSQL = "select count(*) from FACT where CD_CLI=" + CD_CLI + " and CD_FACT <> " + CD_FACT_EXCLUE;
        Integer res = 0;

        // Interroge la Base
        try {
            ResultSet aRS = dbConnect.doRequest(reqSQL);

            while (aRS.next()) {
                res = aRS.getInt(1);
            }
            aRS.close();
        } catch (Exception e) {
            System.out.println("Erreur dans getNbFactBeanByClientBean : " + e.toString());
        }
        return res;
    }

    /**
     * Insert the method's description here.
     * Creation date: (18/08/2001 15:24:27)
     * @return java.util.Vector
     */
    public Vector<HistoPrestBean> getLignes() {
        return lignes;
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (18/08/2001 15:24:27)
     * @return java.util.Vector
     * @param dbConnect DBSession
     */
    public Vector<HistoPrestBean> getLignes(DBSession dbConnect) {

        if (lignes == null) {
            /**
             * Chargement des lignes de facture
             */
            String reqSQL = "select * from HISTO_PREST where CD_FACT=" + CD_FACT + " order by NUM_LIG_FACT";

            try {
                ResultSet aRS = dbConnect.doRequest(reqSQL);
                lignes = new Vector<HistoPrestBean>();

                while (aRS.next()) {
                    lignes.add(new HistoPrestBean(aRS));
                }
                aRS.close();
            } catch (Exception e) {
                System.out.println("Erreur dans getLignes : " + e.toString());
            }
        }

        return lignes;
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (17/08/2001 21:26:26)
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getPRX_TOT_HT() {
        return PRX_TOT_HT;
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (17/08/2001 21:26:51)
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getPRX_TOT_TTC() {
        return PRX_TOT_TTC;
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (17/08/2001 21:26:51)
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getPRX_TOT_TTC_Franc() {
        if (PRX_TOT_TTC != null) {
            return PRX_TOT_TTC.multiply(new BigDecimal(6.55957)).setScale(2, BigDecimal.ROUND_HALF_UP);
        } else {
            return null;
        }
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (17/08/2001 21:26:05)
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getREMISE_FIXE() {
        return REMISE_FIXE;
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (20/08/2001 21:42:17)
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getTotPrest() {
        return totPrest;

    }
    
    /**
     * Insert the method's description here.
     * Creation date: (20/08/2001 21:42:17)
     * @param dbConnect Connexion base à utiliser
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getTotPrest(DBSession dbConnect) {
        if (totPrest == null) {
            // Calcul en direct
            String reqSQL = "select sum(QTE*PRX_UNIT_TTC) from HISTO_PREST where CD_FACT=" + CD_FACT;

            // Interroge la Base
            try {
                totPrest = new BigDecimal(0);
                totPrest.setScale(2);
                ResultSet aRS = dbConnect.doRequest(reqSQL);

                while (aRS.next()) {
                    BigDecimal tempTot = aRS.getBigDecimal(1, 2);
                    if (tempTot != null) {
                        totPrest = tempTot;
                    }
                }
                aRS.close();
            } catch (Exception e) {
                System.out.println("Erreur dans getTotPrest : " + e.toString());
            }
        }
        return totPrest;

    }
    
    /**
     * Insert the method's description here.
     * Creation date: (17/08/2001 21:27:16)
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getTVA() {
        return TVA;
    }
    
    /**
     * Obtention de la TVA de la facture pour un certain taux
     * @param aTva Taux demandé
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getTVA(TvaBean aTva) {
        return (BigDecimal) repartTVA.get(aTva);
    }
    
    /**
     * Obtention des taux de TVA utilisés par la facture
     * @return Ensemble des taux de TVA utilisés par la facture
     */
    public Set<TvaBean> getTxTVA() {
        return repartTVA.keySet();
    }

    /**
     * Fusion de deux factures
     * Creation date: (18/08/2001 17:05:45)
     * @param dbConnect com.increg.salon.bean.DBSession
     * @param aCD_FACT java.lang.String
     */
    public void merge(DBSession dbConnect, String aCD_FACT) {

        String reqSQL = "select * from FACT where CD_FACT=" + aCD_FACT;
        FactBean res = null;

        // Interroge la Base
        try {
            ResultSet aRS = dbConnect.doRequest(reqSQL);

            while (aRS.next()) {
                res = new FactBean(aRS, message);

                // Fusion proprement dite
                if (lignes == null) {
                    getLignes(dbConnect);
                }
                if (res.getLignes(dbConnect) != null) {
                    lignes.addAll(res.getLignes());
                }
                if (res.getPRX_TOT_HT() != null) {
                    if (PRX_TOT_HT == null) {
                        PRX_TOT_HT = new BigDecimal(0);
                    }
                    PRX_TOT_HT = PRX_TOT_HT.add(res.getPRX_TOT_HT());
                }
                if (res.getPRX_TOT_TTC() != null) {
                    if (PRX_TOT_TTC == null) {
                        PRX_TOT_TTC = new BigDecimal(0);
                    }
                    PRX_TOT_TTC = PRX_TOT_TTC.add(res.getPRX_TOT_TTC());
                }
                if (res.getREMISE_FIXE() != null) {
                    if (REMISE_FIXE == null) {
                        REMISE_FIXE = new BigDecimal(0);
                    }
                    REMISE_FIXE = REMISE_FIXE.add(res.getREMISE_FIXE());
                }
                if (totPrest == null) {
                    getTotPrest(dbConnect);
                }
                // REMISE_PRC ajouter à la remise fixe pour éviter les problèmes d'arrondis
                if (REMISE_PRC != null) {
                    REMISE_FIXE = REMISE_FIXE.add(REMISE_PRC.multiply(getTotPrest()).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP));
                    REMISE_PRC = null;
                }
                res.getTotPrest(dbConnect);

                // REMISE_PRC ajouter à la remise fixe pour éviter les problèmes d'arrondis
                BigDecimal rCur = res.getREMISE_PRC();
                if (rCur != null) {
                    REMISE_FIXE = REMISE_FIXE.add(rCur.multiply(res.getTotPrest()).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP));
                }

                totPrest = totPrest.add(res.getTotPrest());
                if (res.getTVA() != null) {
                    if (TVA == null) {
                        TVA = new BigDecimal(0);
                    }
                    TVA = TVA.add(res.getTVA());
                }
            }
            aRS.close();

            // Facture fictive : Supprime le N°
            this.CD_FACT = 0;
        } catch (Exception e) {
            System.out.println("Erreur dans constructeur sur clé : " + e.toString());
        }
    }
    
    /**
     * Effectue les mouvements de stock correspondant à cette facture
     * Creation date: (24/09/2001 09:21:28)
     * @param dbConnect com.increg.salon.bean.DBSession
     * @param undo Annulation d'une facture ?
     * @throws FctlException .
     * @throws SQLException En cas d'erreur
     */
    protected void mouvemente(DBSession dbConnect, boolean undo) throws SQLException, FctlException {

        if ((undo && (CD_PAIEMENT != 0)) 
            || (CD_PAIEMENT_INIT != CD_PAIEMENT)) {
            /**
             * 1ère partie : Mouvements de stock
             * Ne sont concernées que les lignes de type Vente
             */

            // Charge les lignes si nécessaire
            getLignes(dbConnect);

            for (int i = 0; i < lignes.size(); i++) {

                HistoPrestBean aHisto;
                if (undo || (CD_PAIEMENT == 0)) {
                    // Parcours dans l'ordre inverse
                    aHisto = (HistoPrestBean) lignes.get(lignes.size() - i - 1);
                } else {
                    aHisto = (HistoPrestBean) lignes.get(i);
                }
                PrestBean aPrest = PrestBean.getPrestBean(dbConnect, Long.toString(aHisto.getCD_PREST()));

                if (aHisto.hasMvtStk(dbConnect)) {
                    // Cette ligne implique un mouvement
                    ArtBean aArt = ArtBean.getArtBean(dbConnect, Long.toString(aPrest.getCD_ART()), message);
                    MvtStkBean aMvt = new MvtStkBean(message);

                    aMvt.setCD_ART(aPrest.getCD_ART());
                    aMvt.setCD_FACT(CD_FACT);
                    aMvt.setCD_TYP_MVT(TypMvtBean.VENTE);
                    aMvt.setCOMM("Facture " + CD_FACT);
                    aMvt.setDT_MVT(Calendar.getInstance());
                    if (undo || (CD_PAIEMENT == 0)) {
                        // Mouvement inverse
                        aMvt.setQTE(aHisto.getQTE().negate());
                    } else {
                        aMvt.setQTE(aHisto.getQTE());
                    }
                    aMvt.setSTK_AVANT(aArt.getQTE_STK());
                    aMvt.setVAL_MVT_HT(aArt.getVAL_STK_HT());
                    aMvt.setVAL_STK_AVANT(aArt.getVAL_STK_HT());

                    aMvt.create(dbConnect);
                } else if (aPrest.isConsommationAbonnement()) {
                    // Consommation d'un abonnements du client
                    ClientBean aCli = ClientBean.getClientBean(dbConnect, Long.toString(CD_CLI), message);
                    if (!aCli.consommeAbonnement(aPrest.getCD_PREST(), aHisto.getQTE().intValue(), undo || (CD_PAIEMENT == 0))) {
                        // Le client n'a pas l'abonnement
                        throw new FctlException(BasicSession.TAG_I18N + "factBean.abonnementManquant" + BasicSession.TAG_I18N);
                    } else {
                        aCli.maj(dbConnect);
                    }
                } else if (aPrest.isAbonnement()) {
                    // Impacte le client avec l'achat de l'abonnement
                    ClientBean aCli = ClientBean.getClientBean(dbConnect, Long.toString(CD_CLI), message);
                    if (!aCli.consommeAbonnement(aPrest.getCD_PREST_ABONNEMENT(), aHisto.getQTE().multiply(new BigDecimal(aPrest.getCPT_ABONNEMENT())).negate().intValue(), undo || (CD_PAIEMENT == 0))) {
                        // Problème...
                        throw new FctlException(BasicSession.TAG_I18N + "factBean.abonnementErreur" + BasicSession.TAG_I18N);
                    } else {
                        aCli.maj(dbConnect);
                    }
                }
            }

        }

        // Reset pour ne pas faire deux fois le mouvement
        CD_PAIEMENT_INIT = CD_PAIEMENT;
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (17/08/2001 21:20:23)
     * @param newCD_CLI String
     */
    public void setCD_CLI(String newCD_CLI) {

        if ((newCD_CLI != null) && (newCD_CLI.length() != 0)) {
            CD_CLI = Long.parseLong(newCD_CLI);
        } else {
            CD_CLI = 0;
        }
    }

    /**
     * Insert the method's description here.
     * Creation date: (17/08/2001 21:21:42)
     * @param newCD_COLLAB String
     */
    public void setCD_COLLAB(String newCD_COLLAB) {
        if ((newCD_COLLAB != null) && (newCD_COLLAB.length() != 0)) {
            CD_COLLAB = Integer.parseInt(newCD_COLLAB);
        } else {
            CD_COLLAB = 0;
        }
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (17/08/2001 21:19:23)
     * @param newCD_FACT String
     */
    public void setCD_FACT(String newCD_FACT) {

        if ((newCD_FACT != null) && (newCD_FACT.length() != 0)) {
            CD_FACT = Long.parseLong(newCD_FACT);
        } else {
            CD_FACT = 0;
        }
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (26/08/2001 21:54:59)
     * @param newCD_PAIEMENT long
     */
    public void setCD_PAIEMENT(long newCD_PAIEMENT) {
        CD_PAIEMENT = newCD_PAIEMENT;
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (26/08/2001 21:54:59)
     * @param newCD_PAIEMENT String
     * @throws FctlException Si la facture est historique
     */
    public void setCD_PAIEMENT(String newCD_PAIEMENT) throws FctlException {

        if ((newCD_PAIEMENT != null) && (newCD_PAIEMENT.length() != 0) && (!newCD_PAIEMENT.equals("0"))) {
            // Vérification que pas historique
            if ((FACT_HISTO == null) || (FACT_HISTO.equals("N"))) {
                CD_PAIEMENT = Long.parseLong(newCD_PAIEMENT);
            } else {
                throw new FctlException(BasicSession.TAG_I18N + "factBean.paiementHistorique" + BasicSession.TAG_I18N);
            }
        } else {
            CD_PAIEMENT = 0;
        }
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (18/08/2001 21:58:06)
     * @param newCD_TYP_VENT int
     */
    public void setCD_TYP_VENT(int newCD_TYP_VENT) {
        CD_TYP_VENT = newCD_TYP_VENT;
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (18/08/2001 21:58:06)
     * @param newCD_TYP_VENT String
     */
    public void setCD_TYP_VENT(String newCD_TYP_VENT) {
        if ((newCD_TYP_VENT != null) && (newCD_TYP_VENT.length() != 0)) {
            CD_TYP_VENT = Integer.parseInt(newCD_TYP_VENT);
        } else {
            CD_TYP_VENT = 0;
        }
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (17/08/2001 21:21:11)
     * @param newDT_PREST String
     * @param aLocale Configuration pour parser la date
     * @throws Exception En cas de format erroné
     */
    public void setDT_PREST(String newDT_PREST, Locale aLocale) throws Exception {

        if ((newDT_PREST != null) && (newDT_PREST.length() != 0)) {
            DT_PREST = Calendar.getInstance();

            java.text.DateFormat formatDate = java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT, aLocale);
            try {
                DT_PREST.setTime(formatDate.parse(newDT_PREST));
            } catch (Exception e) {
                System.out.println("Erreur de conversion : " + e.toString());
                DT_PREST = null;
                throw (new Exception(BasicSession.TAG_I18N + "factBean.formatDatePrestation" + BasicSession.TAG_I18N));
            }
        } else {
            DT_PREST = null;
        }
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (17/08/2001 21:28:14)
     * @param newFACT_HISTO String
     */
    public void setFACT_HISTO(String newFACT_HISTO) {
        FACT_HISTO = newFACT_HISTO;
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (18/08/2001 15:24:27)
     * @param newLignes java.util.Vector
     */
    public void setLignes(Vector<HistoPrestBean> newLignes) {
        lignes = newLignes;
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (17/08/2001 21:26:26)
     * @param newPRX_TOT_HT String
     */
    public void setPRX_TOT_HT(String newPRX_TOT_HT) {

        if ((newPRX_TOT_HT != null) && (newPRX_TOT_HT.length() != 0)) {
            PRX_TOT_HT = new Montant(newPRX_TOT_HT);
        } else {
            PRX_TOT_HT = null;
        }
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (17/08/2001 21:26:26)
     * @param newPRX_TOT_HT java.math.BigDecimal
     */
    public void setPRX_TOT_HT(java.math.BigDecimal newPRX_TOT_HT) {
        PRX_TOT_HT = newPRX_TOT_HT;
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (17/08/2001 21:26:51)
     * @param newPRX_TOT_TTC String
     */
    public void setPRX_TOT_TTC(String newPRX_TOT_TTC) {

        if ((newPRX_TOT_TTC != null) && (newPRX_TOT_TTC.length() != 0)) {
            PRX_TOT_TTC = new Montant(newPRX_TOT_TTC);
        } else {
            PRX_TOT_TTC = null;
        }
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (17/08/2001 21:26:51)
     * @param newPRX_TOT_TTC java.math.BigDecimal
     */
    public void setPRX_TOT_TTC(java.math.BigDecimal newPRX_TOT_TTC) {
        PRX_TOT_TTC = newPRX_TOT_TTC;
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (17/08/2001 21:26:05)
     * @param newREMISE_FIXE String
     */
    public void setREMISE_FIXE(String newREMISE_FIXE) {

        if ((newREMISE_FIXE != null) && (newREMISE_FIXE.length() != 0)) {
            REMISE_FIXE = new Montant(newREMISE_FIXE);
        } else {
            REMISE_FIXE = null;
        }
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (17/08/2001 21:26:05)
     * @param newREMISE_FIXE java.math.BigDecimal
     */
    public void setREMISE_FIXE(java.math.BigDecimal newREMISE_FIXE) {
        REMISE_FIXE = newREMISE_FIXE;
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (17/08/2001 21:24:26)
     * @param newREMISE_PRC String
     */
    public void setREMISE_PRC(String newREMISE_PRC) {

        if ((newREMISE_PRC != null) && (newREMISE_PRC.length() != 0)) {
            REMISE_PRC = new NombreDecimal(newREMISE_PRC);
        } else {
            REMISE_PRC = null;
        }
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (20/08/2001 21:42:17)
     * @param newTotPrest java.math.BigDecimal
     */
    public void setTotPrest(java.math.BigDecimal newTotPrest) {
        totPrest = newTotPrest;
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (17/08/2001 21:27:16)
     * @param newTVA String
     */
    public void setTVA(String newTVA) {

        if ((newTVA != null) && (newTVA.length() != 0)) {
            TVA = new Montant(newTVA);
        } else {
            TVA = null;
        }
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (17/08/2001 21:27:16)
     * @param newTVA java.math.BigDecimal
     */
    public void setTVA(java.math.BigDecimal newTVA) {
        TVA = newTVA;
    }
    
    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public java.lang.String toString() {
        return "";
    }
    
    /**
     * Calcule la répartition de la TVA sur une période
     * @param myDBSession Connexion base à utiliser
     * @param DT_DEBUT Début de la périodeà étudier
     * @param DT_FIN Fin de la période à utiliser
     * @return Liste correspondante à chaque élément de répartition (Type de répartition)
     * @throws SQLException en cas de gros pb
     */
    public static Vector<TVA> calculTVARepartie(DBSession myDBSession, String DT_DEBUT, String DT_FIN) throws SQLException {

        Vector<TVA> lstLignes = new Vector<TVA>();
    
        // Recherche le type de répatition
        String reqSQL = null;
    
        reqSQL = "select PREST.CD_TYP_VENT, sum(HISTO_PREST.PRX_TOT_HT) as HT, sum(HISTO_PREST.PRX_TOT_TTC) as TTC, sum(HISTO_PREST.TVA) as TVA "
                + "from HISTO_PREST, PREST, FACT, PAIEMENT "
                + "where FACT.CD_PAIEMENT = PAIEMENT.CD_PAIEMENT "
                + "and FACT.CD_FACT = HISTO_PREST.CD_FACT "
                + "and PREST.CD_PREST = HISTO_PREST.CD_PREST";
    
        if ((DT_DEBUT != null) && (DT_DEBUT.length() > 0)) {
            reqSQL = reqSQL + " and PAIEMENT.DT_PAIEMENT >= '" + DT_DEBUT + "'";
        }
        if ((DT_FIN != null) && (DT_FIN.length() > 0)) {
            reqSQL = reqSQL + " and PAIEMENT.DT_PAIEMENT < '" + DT_FIN + "'::date + 1";
        }
        reqSQL = reqSQL + " group by PREST.CD_TYP_VENT";
    
        // Interroge la Base
        ResultSet aRS = myDBSession.doRequest(reqSQL);

        while (aRS.next()) {

            // Récupère l'élément de la liste
            TVA aTVA = new TVA();

            aTVA.setCD_TYP_VENT(aRS.getInt("CD_TYP_VENT"));
            aTVA.setTotal(aRS.getBigDecimal("TVA", 2));
            aTVA.setTotalHT(aRS.getBigDecimal("HT", 2));
            aTVA.setTotalTTC(aRS.getBigDecimal("TTC", 2));

            lstLignes.add(aTVA);
        }
        aRS.close();
    
    
        return lstLignes;
    }

    /**
     * Calcule les ventes en déduisant la TVA pour chaque
     * @param myDBSession Connexion base à utiliser
     * @param DT_DEBUT Début de la périodeà étudier
     * @param DT_FIN Fin de la période à utiliser
     * @return Liste correspondante à chaque élément de répartition (Type de répartition)
     * @throws SQLException en cas de gros pb
     */
    public static Vector<RecapVente> calculVente(DBSession myDBSession, String DT_DEBUT, String DT_FIN) throws SQLException {

        Vector<RecapVente> lstLignes = new Vector<RecapVente>();


        String reqSQL = "select PREST.CD_PREST, sum(HISTO_PREST.QTE) as QTE, sum(HISTO_PREST.PRX_TOT_HT) as HT, sum(HISTO_PREST.PRX_TOT_TTC) as TTC, sum(HISTO_PREST.TVA) as TVA "
                + "from PREST, HISTO_PREST, TYP_VENT, FACT, PAIEMENT "
                + "where PREST.CD_PREST=HISTO_PREST.CD_PREST "
                + "and PREST.CD_TYP_VENT=TYP_VENT.CD_TYP_VENT "
                + "and HISTO_PREST.CD_FACT=FACT.CD_FACT "
                + "and FACT.CD_PAIEMENT=PAIEMENT.CD_PAIEMENT "
                + "and FACT_HISTO='N' "
                + "and TYP_VENT.MARQUE='O' ";
                
        if ((DT_DEBUT != null) && (DT_DEBUT.length() > 0)) {
            reqSQL = reqSQL + " and PAIEMENT.DT_PAIEMENT >= '" + DT_DEBUT + "'";
        }
        if ((DT_FIN != null) && (DT_FIN.length() > 0)) {
            reqSQL = reqSQL + " and PAIEMENT.DT_PAIEMENT < '" + DT_FIN + "'::date + 1";
        }
                
        reqSQL = reqSQL + " group by PREST.CD_PREST";
        
        // Interroge la Base
        ResultSet aRS = myDBSession.doRequest(reqSQL);

        while (aRS.next()) {

            // Récupère l'élément de la liste
            RecapVente aRecap = new RecapVente();

            aRecap.setPrest(PrestBean.getPrestBean(myDBSession, aRS.getString("CD_PREST")));
            aRecap.setTVA(aRS.getBigDecimal("TVA", 2));
            aRecap.setHT(aRS.getBigDecimal("HT", 2));
            aRecap.setTTC(aRS.getBigDecimal("TTC", 2));
            aRecap.setQte(aRS.getBigDecimal("QTE", 2));

            lstLignes.add(aRecap);
        }
        aRS.close();
    
    
        return lstLignes;
    }

    /**
     * @param myDBSession Connexion à la base
     * @return Type de répartition de la remise
     */
    public String getRepartRemise(DBSession myDBSession) {
        if (repartRemise == null) {
            ParamBean repartRemiseBean = ParamBean.getParamBean(myDBSession, Integer.toString(ParamBean.CD_REPART_REMISE));
            if (repartRemiseBean != null) {
                repartRemise = repartRemiseBean.getVAL_PARAM();
            }
        }
        return repartRemise;
    }

    /**
     * @param string Type de répartition de la remise
     */
    public void setRepartRemise(String string) {
        repartRemise = string;
    }

}
