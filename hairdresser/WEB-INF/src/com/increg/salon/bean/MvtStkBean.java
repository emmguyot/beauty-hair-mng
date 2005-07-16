/*
 * Bean de gestion d'un mouvement de stock
 * Copyright (C) 2001-2005 Emmanuel Guyot <See emmguyot on SourceForge> 
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

import java.sql.*;
import java.text.ParseException;
import java.util.*;
import java.util.Date;
import java.math.*;
import com.increg.commun.*;
import com.increg.commun.exception.FctlException;
import com.increg.util.*;

/**
 * Mouvement de stock
 * Creation date: (16/09/2001 19:29:41)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class MvtStkBean extends TimeStampBean {
    /** 
     * Code de l'article mouvementé
     */
    protected long CD_ART;
    /**
     * Commentaire du mouvement
     */
    protected java.lang.String COMM;
    /**
     * Facture liée à ce mouvement
     */
    protected long CD_FACT;
    /**
     * Type du mouvement
     */
    protected int CD_TYP_MVT;
    /**
     * Date du mouvement
     */
    protected Calendar DT_MVT;
    /**
     * Quantité mouvementée
     */
    protected java.math.BigDecimal QTE;
    /**
     * Quantité en stock de l'article avant le mouvement
     */
    protected java.math.BigDecimal STK_AVANT;
    /**
     * Valeur unitaire des articles mouvementés
     */
    protected java.math.BigDecimal VAL_MVT_HT;
    /**
     * Valeur unitaire de l'article avant mouvement
     */
    protected java.math.BigDecimal VAL_STK_AVANT;
    /**
     * Code commande du fournisseur (facultatif)
     */
    protected java.lang.String CD_CMD_FOURN;

    /**
     * FactBean constructor comment.
     * @param rb Messages localisés
     */
    public MvtStkBean(ResourceBundle rb) {
        super(rb);
    }
    /**
     * MvtStkBean constructor comment.
     * @param rs java.sql.ResultSet
     * @param rb Messages localisés
     */
    public MvtStkBean(ResultSet rs, ResourceBundle rb) {
        super(rs, rb);
        try {
            CD_ART = rs.getLong("CD_ART");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans MvtStkBean (RS) : " + e.toString());
            }
        }
        try {
            CD_FACT = rs.getLong("CD_FACT");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans MvtStkBean (RS) : " + e.toString());
            }
        }
        try {
            CD_TYP_MVT = rs.getInt("CD_TYP_MVT");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans MvtStkBean (RS) : " + e.toString());
            }
        }
        try {
            COMM = rs.getString("COMM");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans MvtStkBean (RS) : " + e.toString());
            }
        }

        try {
            DT_MVT = Calendar.getInstance();
            DT_MVT.setTime(rs.getTimestamp("DT_MVT"));
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans MvtStkBean (RS) : " + e.toString());
            }
        }
        try {
            QTE = rs.getBigDecimal("QTE", 2);
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans MvtStkBean (RS) : " + e.toString());
            }
        }
        try {
            STK_AVANT = rs.getBigDecimal("STK_AVANT", 2);
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans MvtStkBean (RS) : " + e.toString());
            }
        }
        try {
            VAL_MVT_HT = rs.getBigDecimal("VAL_MVT_HT", 2);
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans MvtStkBean (RS) : " + e.toString());
            }
        }
        try {
            VAL_STK_AVANT = rs.getBigDecimal("VAL_STK_AVANT", 2);
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans MvtStkBean (RS) : " + e.toString());
            }
        }
        try {
            CD_CMD_FOURN = rs.getString("CD_CMD_FOURN");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans MvtStkBean (RS) : " + e.toString());
            }
        }
    }
    /**
     * Constructeur pour nouveau mouvement d'un article
     * @param dbConnect Connexion base à utiliser
     * @param CD_ART Code de l'article
     */
    public MvtStkBean(DBSession dbConnect, String CD_ART) {
        super();
        ArtBean aArt = ArtBean.getArtBean(dbConnect, CD_ART, message);
        setCD_ART(CD_ART);
        setSTK_AVANT(aArt.getQTE_STK());
        setVAL_STK_AVANT(aArt.getVAL_STK_HT());
        setDT_MVT(Calendar.getInstance());
        // Arrondi car la base ne stocke pas les millisecondes
        DT_MVT.clear(Calendar.MILLISECOND);
    }
    
    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public void create(DBSession dbConnect) throws SQLException, com.increg.commun.exception.FctlException {

        com.increg.util.SimpleDateFormatEG formatDate = new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");

        StringBuffer req = new StringBuffer("insert into MVT_STK ");
        StringBuffer colonne = new StringBuffer("(");
        StringBuffer valeur = new StringBuffer(" values ( ");

        if (CD_ART != 0) {
            colonne.append("CD_ART,");
            valeur.append(CD_ART);
            valeur.append(",");
        }

        if (CD_FACT != 0) {
            colonne.append("CD_FACT,");
            valeur.append(CD_FACT);
            valeur.append(",");
        }

        if (DT_MVT != null) {
            colonne.append("DT_MVT,");
            valeur.append(DBSession.quoteWith(formatDate.formatEG(DT_MVT.getTime()), '\''));
            valeur.append(",");
        }

        if (CD_TYP_MVT != 0) {
            colonne.append("CD_TYP_MVT,");
            valeur.append(CD_TYP_MVT);
            valeur.append(",");
        }

        if ((COMM != null) && (COMM.length() != 0)) {
            colonne.append("COMM,");
            valeur.append(DBSession.quoteWith(COMM, '\''));
            valeur.append(",");
        }

        if (QTE != null) {
            colonne.append("QTE,");
            valeur.append(QTE.toString());
            valeur.append(",");
        }

        if (STK_AVANT != null) {
            colonne.append("STK_AVANT,");
            valeur.append(STK_AVANT.toString());
            valeur.append(",");
        }

        if (VAL_MVT_HT != null) {
            colonne.append("VAL_MVT_HT,");
            valeur.append(VAL_MVT_HT.toString());
            valeur.append(",");
        }

        if (VAL_STK_AVANT != null) {
            colonne.append("VAL_STK_AVANT,");
            valeur.append(VAL_STK_AVANT.toString());
            valeur.append(",");
        }

        if ((CD_CMD_FOURN != null) && (CD_CMD_FOURN.length() != 0)) {
            colonne.append("CD_CMD_FOURN,");
            valeur.append(DBSession.quoteWith(CD_CMD_FOURN, '\''));
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

        // Mise à jour du stock en conséquence
        majStk(dbConnect, QTE, false);
        // Mise à jour de l'article si besoin
        majArt(dbConnect, false);

        // Execute la création
        String[] reqs = new String[1];
        reqs[0] = req.toString();
        int[] nb = new int[0];
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

        com.increg.util.SimpleDateFormatEG formatDate = new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");

        StringBuffer req = new StringBuffer("delete from MVT_STK ");
        StringBuffer where = new StringBuffer(" where CD_ART=" + CD_ART + " and DT_MVT=" + DBSession.quoteWith(formatDate.formatEG(DT_MVT.getTime()), '\''));
        if (CD_FACT == 0) {
            where.append(" and CD_FACT is null");
        }
        else {
            where.append(" and CD_FACT=" + CD_FACT);
        }

        // Constitue la requete finale
        req.append(where);

        // Debut de la transaction
        dbConnect.setDansTransactions(true);

        // Vérifie qu'il s'agit bien du dernier mouvement de stock
        String reqSQL = "select max(DT_MVT) from MVT_STK where CD_ART=" + CD_ART;
        ResultSet aRS = dbConnect.doRequest(reqSQL);
        while (aRS.next()) {
            java.util.Date lastDT_MVT = aRS.getTimestamp(1);
            if (lastDT_MVT.compareTo(DT_MVT.getTime()) != 0) {
                throw (new FctlException(BasicSession.TAG_I18N + "mvtStkBean.deleteKo" + BasicSession.TAG_I18N));
            }
        }
        aRS.close();
                    
        // Inverse la mise à jour du stock
        majStk(dbConnect, QTE, true);

        // Execute la création
        String[] reqs = new String[1];
        reqs[0] = req.toString();
        int[] nb = new int[1];
        nb = dbConnect.doExecuteSQL(reqs);

        if (nb[0] != 1) {
            throw (new SQLException(BasicSession.TAG_I18N + "message.suppressionOk" + BasicSession.TAG_I18N));
        }

        // Inverse la mise à jour de l'article
        majArt(dbConnect, true);

        // Fin de la transaction
        dbConnect.endTransaction();

    }

    /**
     * Insert the method's description here.
     * Creation date: (19/08/2001 20:55:16)
     * @return long
     */
    public long getCD_ART() {
        return CD_ART;
    }

    /**
     * Insert the method's description here.
     * Creation date: (18/08/2001 13:25:05)
     * @return java.lang.String
     */
    public java.lang.String getCOMM() {
        return COMM;
    }

    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public void maj(DBSession dbConnect) throws com.increg.commun.exception.FctlException {

        throw (new com.increg.commun.exception.FctlException(BasicSession.TAG_I18N + "mvtStkBean.noUpdate" + BasicSession.TAG_I18N));
    }

    /**
     * Insert the method's description here.
     * Creation date: (19/08/2001 20:55:16)
     * @param newCD_ART long
     */
    public void setCD_ART(long newCD_ART) {
        CD_ART = newCD_ART;
    }
    /**
     * Insert the method's description here.
     * Creation date: (19/08/2001 20:55:16)
     * @param newCD_ART String
     */
    public void setCD_ART(String newCD_ART) {
        if ((newCD_ART != null) && (newCD_ART.length() != 0)) {
            CD_ART = Long.parseLong(newCD_ART);
        }
        else {
            CD_ART = 0;
        }
    }

    /**
     * Insert the method's description here.
     * Creation date: (18/08/2001 13:25:05)
     * @param newCOMM java.lang.String
     */
    public void setCOMM(java.lang.String newCOMM) {
        COMM = newCOMM;
    }

    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public java.lang.String toString() {
        return "";
    }

    /**
     * Insert the method's description here.
     * Creation date: (24/09/2001 15:57:21)
     * @return long
     */
    public long getCD_FACT() {
        return CD_FACT;
    }
    /**
     * Insert the method's description here.
     * Creation date: (16/09/2001 19:31:26)
     * @return int
     */
    public int getCD_TYP_MVT() {
        return CD_TYP_MVT;
    }
    /**
     * Insert the method's description here.
     * Creation date: (16/09/2001 19:31:27)
     * @return java.util.Calendar
     */
    public java.util.Calendar getDT_MVT() {
        return DT_MVT;
    }
    /**
     * Création d'un Bean Article à partir de sa clé
     * Creation date: (19/08/2001 21:14:20)
     * @param dbConnect com.increg.salon.bean.DBSession
     * @param CD_ART java.lang.String
     * @param DT_MVT Date du mouvement
     * @param CD_FACT Facture concernée
     * @param aLocale Configuration pour parser la date
     * @throws Exception Si le format est incorrect
     * @param rb Messages localisés
     * @return Mouvement correspondant à la clé
     */
    public static MvtStkBean getMvtStkBean(DBSession dbConnect, String CD_ART, String DT_MVT, String CD_FACT, Locale aLocale, ResourceBundle rb) throws Exception {

        java.text.DateFormat formatDate =
            java.text.DateFormat.getDateTimeInstance(
                java.text.DateFormat.SHORT,
                java.text.DateFormat.MEDIUM, aLocale);
        Date dtMvt = null;
        try {
            dtMvt = formatDate.parse(DT_MVT);
        }
        catch (ParseException e) {
            System.out.println("Erreur de conversion : " + e.toString());
            throw (new Exception(BasicSession.TAG_I18N + "mvtStkBean.formatDateMvt" + BasicSession.TAG_I18N));
		}

        // Passage en GMT +0
        java.text.DateFormat formatDateStd =
            java.text.DateFormat.getDateTimeInstance(
                java.text.DateFormat.SHORT,
                java.text.DateFormat.MEDIUM);
        DT_MVT = formatDateStd.format(dtMvt);
        
    	String reqSQL = "select * from MVT_STK where CD_ART=" + CD_ART + " and DT_MVT='" + DT_MVT + "'";
        if ((CD_FACT == null) || (CD_FACT.equals("")) || (CD_FACT.equals("0"))) {
            reqSQL = reqSQL + " and CD_FACT is null";
        }
        else {
            reqSQL = reqSQL + " and CD_FACT=" + CD_FACT;
        }
        MvtStkBean res = null;

        // Interroge la Base
        try {
            ResultSet aRS = dbConnect.doRequest(reqSQL);

            while (aRS.next()) {
                res = new MvtStkBean(aRS, rb);
            }
            aRS.close();
        }
        catch (Exception e) {
            System.out.println("Erreur dans constructeur sur clé : " + e.toString());
        }
        return res;
    }
    /**
     * Création d'un Bean Article à partir de sa clé
     * Creation date: 3 nov. 2002
     * @param dbConnect com.increg.salon.bean.DBSession
     * @param CD_CMD_FOURN N° de commande fournisseur
     * @param rb Messages localisés
     * @return liste des Mouvements correspondant à la commande
     */
    public static Vector getMvtStkBeanFromCmd(DBSession dbConnect, String CD_CMD_FOURN, ResourceBundle rb) {
        String reqSQL = "select * from MVT_STK where CD_CMD_FOURN=" + CD_CMD_FOURN + " order by DT_MVT";
        Vector lstMvt = new Vector();

        // Interroge la Base
        try {
            ResultSet aRS = dbConnect.doRequest(reqSQL);

            while (aRS.next()) {
                MvtStkBean res = new MvtStkBean(aRS, rb);
                lstMvt.add(res);
            }
            aRS.close();
        }
        catch (Exception e) {
            System.out.println("Erreur dans recherche sur commande : " + e.toString());
        }
        return lstMvt;
    }
    /**
     * Insert the method's description here.
     * Creation date: (16/09/2001 19:31:27)
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getQTE() {
        return QTE;
    }
    /**
     * Insert the method's description here.
     * Creation date: (16/09/2001 19:31:27)
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getSTK_AVANT() {
        return STK_AVANT;
    }
    /**
     * Insert the method's description here.
     * Creation date: (16/09/2001 19:31:27)
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getVAL_MVT_HT() {
        return VAL_MVT_HT;
    }
    /**
     * Insert the method's description here.
     * Creation date: (02/11/2001 14:55:04)
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getVAL_STK_AVANT() {
        return VAL_STK_AVANT;
    }
    
    /**
     * Construit la requete de maj des données articles
     * Creation date: (17/09/2001 22:19:13)
     * @param dbConnect Connexion à la base à utiliser
     * @param qte Quantité mouvementée
     * @param undo indique si c'est un mouvement inverse
     * @throws SQLException pb de mise à jour
     * @throws FctlException Cas non gérés
     */
    protected void majStk(DBSession dbConnect, BigDecimal qte, boolean undo) throws SQLException, FctlException {

        ArtBean aArt = ArtBean.getArtBean(dbConnect, Long.toString(CD_ART), message);
        if (!undo) {
            // Tiens compte du sens du mouvement
            TypMvtBean myTypMvt = TypMvtBean.getTypMvtBean(dbConnect, Integer.toString(CD_TYP_MVT));
            Stock stock = calculStock(qte, STK_AVANT, VAL_MVT_HT, VAL_STK_AVANT, myTypMvt.getSENS_MVT());
            aArt.setQTE_STK(stock.getQte());
            aArt.setVAL_STK_HT(stock.getValeur());

        }
        else {
            // Reprend les valeurs d'avant
            aArt.setQTE_STK(STK_AVANT);
            if (VAL_STK_AVANT.compareTo(new BigDecimal(0)) >= 0) {
                aArt.setVAL_STK_HT(VAL_STK_AVANT);
            }
            else {
                throw (new com.increg.commun.exception.FctlException("La valeur précédente du stock est inconnue : Action impossible"));
            }
        }

        aArt.maj(dbConnect);
    }

    /**
     * Calcule la valeur moyenne d'un article du stock ainsi que
     * la quantite en stock a partir d'un etat precedent dans le stock et d'un mouvement
     * donné
     * @param qteMvt La quantite mouvementee
     * @param stkAvant La quantite en stock avant le mouvement
     * @param valMvt La valeur du mouvement
     * @param valAvant La valeur de l'article avant le mouvement
     * @param typMvt le type de mouvement
     * @return une structure stock contenant la valeur et le stock
     */
    public static Stock calculStock(BigDecimal qteMvt, BigDecimal stkAvant, BigDecimal valMvt, BigDecimal valAvant, String typMvt) {
        Stock stock = new Stock();
        
        if (typMvt.equals(TypMvtBean.SENS_SORTIE)) {
            stock.setQte(stkAvant.add(qteMvt.negate()));
            stock.setValeur(valAvant);
        }
        else if (typMvt.equals(TypMvtBean.SENS_ENTREE)) {
            if (stkAvant.compareTo(new BigDecimal(0)) < 0) {
                // Le stock d'avant est négatif : Ignore la valeur du stock précédent
                stock.setValeur(valMvt);
            }
            else {
                if (stkAvant.add(qteMvt).compareTo(new BigDecimal(0)) == 0) {
                    stock.setValeur(new BigDecimal(0));
                }
                else {
                    stock.setValeur(stkAvant.multiply(valAvant).add(qteMvt.multiply(valMvt)).divide(stkAvant.add(qteMvt), 2, BigDecimal.ROUND_HALF_UP));
                }
            }
            // Mise à jour de la quantité du stock
            stock.setQte(stkAvant.add(qteMvt));
        }
        else if (typMvt.equals(TypMvtBean.SENS_INVENTAIRE)) {
            stock.setQte(qteMvt);
            stock.setValeur(valMvt);
        }

        return stock;

    }


    /**
     * Purge des mouvements de stocks
     * @param dbConnect Connexion à la base à utiliser
     * @param dateLimite Date limite de purge : Seront purgés les mouvements avant cette date
     * @exception FctlException En cas d'erreur durant la mise à jour
     * @return Nombre d'enregistrements purgés : -1 En cas d'erreur
     */
    public static int purge(DBSession dbConnect, java.util.Date dateLimite) throws FctlException {
        
        int nbEnreg = -1;
        
        com.increg.util.SimpleDateFormatEG formatDate = new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");

        String reqSQL[] = new String[1];
         
        reqSQL[0] = "delete from MVT_STK where DT_MVT < " + DBSession.quoteWith(formatDate.format(dateLimite), '\'');
        
        dbConnect.setDansTransactions(true);

        try {
            int[] res = dbConnect.doExecuteSQL(reqSQL);
            
            nbEnreg = res[0];
        }
        catch (Exception e) {
            System.out.println("Erreur dans Purge des mouvements de stock : " + e.toString());
            dbConnect.cleanTransaction();
            throw new FctlException(BasicSession.TAG_I18N + "mvtStkBean.purgeKo" + BasicSession.TAG_I18N);
        }
        
        // Fin de cette transaction
        dbConnect.endTransaction();
        
        return nbEnreg;
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (24/09/2001 15:57:21)
     * @param newCD_FACT long
     */
    public void setCD_FACT(long newCD_FACT) {
        CD_FACT = newCD_FACT;
    }
    /**
     * Insert the method's description here.
     * Creation date: (24/09/2001 15:57:21)
     * @param newCD_FACT String
     */
    public void setCD_FACT(String newCD_FACT) {
        if ((newCD_FACT != null) && (newCD_FACT.length() != 0)) {
            CD_FACT = Long.parseLong(newCD_FACT);
        }
        else {
            CD_FACT = 0;
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (16/09/2001 19:31:27)
     * @param newCD_TYP_MVT int
     */
    public void setCD_TYP_MVT(int newCD_TYP_MVT) {
        CD_TYP_MVT = newCD_TYP_MVT;
    }
    /**
     * Insert the method's description here.
     * Creation date: (01/09/2001 15:36:14)
     * @param newCD_TYP_MVT String
     */
    public void setCD_TYP_MVT(String newCD_TYP_MVT) {
        if ((newCD_TYP_MVT != null) && (newCD_TYP_MVT.length() != 0)) {
            CD_TYP_MVT = Integer.parseInt(newCD_TYP_MVT);
        }
        else {
            CD_TYP_MVT = 0;
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (16/09/2001 19:31:27)
     * @param newDT_MVT String
     * @param aLocale Configuration pour parser la date
     * @exception Exception en cas d'erreur de format de date
     */
    public void setDT_MVT(String newDT_MVT, Locale aLocale) throws Exception {

        if ((newDT_MVT != null) && (newDT_MVT.length() != 0)) {
            DT_MVT = Calendar.getInstance();

            java.text.DateFormat formatDate = java.text.DateFormat.getDateTimeInstance(java.text.DateFormat.SHORT, java.text.DateFormat.MEDIUM, aLocale);
            try {
                DT_MVT.setTime(formatDate.parse(newDT_MVT));
            }
            catch (Exception e) {
                System.out.println("Erreur de conversion : " + e.toString());
                DT_MVT = null;
                throw (new Exception(BasicSession.TAG_I18N + "mvtStkBean.formatDateMvt" + BasicSession.TAG_I18N));
            }
        }
        else {
            DT_MVT = null;
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (16/09/2001 19:31:27)
     * @param newDT_MVT java.util.Calendar
     */
    public void setDT_MVT(java.util.Calendar newDT_MVT) {
        DT_MVT = newDT_MVT;
        DT_MVT.clear(Calendar.MILLISECOND);
    }
    /**
     * Insert the method's description here.
     * Creation date: (01/09/2001 14:59:04)
     * @param newQTE String
     */
    public void setQTE(String newQTE) {

        if ((newQTE != null) && (newQTE.length() != 0)) {
            QTE = new BigDecimal(newQTE);
        }
        else {
            QTE = null;
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (16/09/2001 19:31:27)
     * @param newQTE java.math.BigDecimal
     */
    public void setQTE(java.math.BigDecimal newQTE) {
        QTE = newQTE;
    }
    /**
     * Insert the method's description here.
     * Creation date: (01/09/2001 14:59:04)
     * @param newSTK_AVANT String
     */
    public void setSTK_AVANT(String newSTK_AVANT) {

        if ((newSTK_AVANT != null) && (newSTK_AVANT.length() != 0)) {
            STK_AVANT = new BigDecimal(newSTK_AVANT);
        }
        else {
            STK_AVANT = null;
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (16/09/2001 19:31:27)
     * @param newSTK_AVANT java.math.BigDecimal
     */
    public void setSTK_AVANT(java.math.BigDecimal newSTK_AVANT) {
        STK_AVANT = newSTK_AVANT;
    }
    /**
     * Insert the method's description here.
     * Creation date: (01/09/2001 14:59:04)
     * @param newVAL_MVT_HT String
     */
    public void setVAL_MVT_HT(String newVAL_MVT_HT) {

        if ((newVAL_MVT_HT != null) && (newVAL_MVT_HT.length() != 0)) {
            VAL_MVT_HT = new BigDecimal(newVAL_MVT_HT);
        }
        else {
            VAL_MVT_HT = null;
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (16/09/2001 19:31:27)
     * @param newVAL_MVT_HT java.math.BigDecimal
     */
    public void setVAL_MVT_HT(java.math.BigDecimal newVAL_MVT_HT) {
        VAL_MVT_HT = newVAL_MVT_HT;
    }
    /**
     * Insert the method's description here.
     * Creation date: (01/09/2001 14:59:04)
     * @param newVAL_STK_AVANT String
     */
    public void setVAL_STK_AVANT(String newVAL_STK_AVANT) {

        if ((newVAL_STK_AVANT != null) && (newVAL_STK_AVANT.length() != 0)) {
            VAL_STK_AVANT = new BigDecimal(newVAL_STK_AVANT);
        }
        else {
            VAL_STK_AVANT = null;
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (02/11/2001 14:55:04)
     * @param newVAL_STK_AVANT java.math.BigDecimal
     */
    public void setVAL_STK_AVANT(java.math.BigDecimal newVAL_STK_AVANT) {
        VAL_STK_AVANT = newVAL_STK_AVANT;
    }

    /**
     * Mise à jour de l'article : Mixité de l'article
     * @param dbConnect Connexion à la base à utiliser
     * @param undo indique si c'est un mouvement inverse
     * @throws SQLException pb de mise à jour
     */
    protected void majArt(DBSession dbConnect, boolean undo) throws SQLException {

        // Recherche si le type du mouvement influe sur la mixité de l'article
        TypMvtBean aTypMvt = TypMvtBean.getTypMvtBean(dbConnect, Integer.toString(CD_TYP_MVT));
        if (aTypMvt.getTRANSF_MIXTE().equals("O")) {

            if (undo) {
                // Recherche si d'autres mouvements de ce type existent pour cette article
                String reqSQL = "select * from MVT_STK where CD_ART=" + Long.toString(CD_ART) + " and CD_TYP_MVT in (select CD_TYP_MVT from TYP_MVT where TRANSF_MIXTE='O') limit 1";
                ResultSet rs = dbConnect.doRequest(reqSQL);
                if (!rs.next()) {
                    // Il n'y en a pas : Reset du flag Mixte
                    ArtBean aArt = ArtBean.getArtBean(dbConnect, Long.toString(CD_ART), message);
                    aArt.setINDIC_MIXTE("N");
                    aArt.maj(dbConnect);
                }
                rs.close();
            }
            else {
                // Passe l'article à mixte
                ArtBean aArt = ArtBean.getArtBean(dbConnect, Long.toString(CD_ART), message);
                if (!aArt.getINDIC_MIXTE().equals("O")) {
                    aArt.setINDIC_MIXTE("O");
                    aArt.maj(dbConnect);
                }
            }
        }
    }

    /**
     * Returns the cD_CMD_FOURN.
     * @return java.lang.String
     */
    public java.lang.String getCD_CMD_FOURN() {
        return CD_CMD_FOURN;
    }

    /**
     * Sets the cD_CMD_FOURN.
     * @param cD_CMD_FOURN The cD_CMD_FOURN to set
     */
    public void setCD_CMD_FOURN(java.lang.String cD_CMD_FOURN) {
        CD_CMD_FOURN = cD_CMD_FOURN;
    }

    /***************************************************************************************************************************/
    /***************************************************************************************************************************/
    /***************************************************************************************************************************/
    /**
     * La representation de la paire (qte,valeur) du stock a un instant t
     */
    public static class Stock {
        /**
         * La quantite en stock
         */
        private BigDecimal qte = null;
        /**
         * La valeur en stock
         */
        private BigDecimal valeur = null;

        /**
         * Un constructeur vide
         */
        public Stock() {
        }

        /**
         * Construit un nouveau stock avec des valeurs par defaut
         * @param qte la quantite en stock
         * @param valeur la valeur en stock
         */
        public Stock(BigDecimal qte, BigDecimal valeur) {
            this.qte = qte;
            this.valeur = valeur;
        }

        /**
         * Set la quantite en stock
         * @param qte la quantite a setter
         */
        public void setQte(BigDecimal qte) {
            this.qte = qte;
        }

        /**
         * Set la valeur en stock
         * @param valeur la valeur a setter
         */
        public void setValeur(BigDecimal valeur) {
            this.valeur = valeur;
        }

        /**
         * Retourne la quantite en stock
         * @return la quantite en stock
         */
        public BigDecimal getQte() {
            return qte;
        }

        /**
         * Retourne la valeur en stock
         * @return la valeur en stock
         */
        public BigDecimal getValeur() {
            return valeur;
        }

        /**
         * Test si deux objets stock sont egaux
         * @param obj le stock a tester
         * @return true si egaux, sinon faux
         */
        public boolean equals(Object obj) {
            Stock tmp = (Stock) obj;
            return getValeur().equals(tmp.getValeur()) && getQte().equals(tmp.getQte());
        }

        /**
         * Renvoie la valeur toString de cet objet
         * @return la valeur toString de cet objet
         */
        public String toString() {
            StringBuffer value = new StringBuffer();
            value.append(getClass());
            value.append("{Quantite : ");
            value.append(qte);
            value.append(" ,Valeur : ");
            value.append(valeur);
            value.append("}");
            return value.toString();
        }
    } //Fin de la classe Stock

} //Fin de la class MvtStkBean
