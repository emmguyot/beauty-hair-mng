package com.increg.salon.bean;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import com.increg.commun.DBSession;
import com.increg.commun.TimeStampBean;
import com.increg.commun.exception.FctlException;
import com.increg.util.SimpleDateFormatEG;
/**
 * Prestation à la vente
 * Creation date: (19/08/2001 20:51:29)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class PrestBean extends TimeStampBean {

    /**
     * Code de l'article correspondant si vente
     */
    protected long CD_ART;
    /**
     * Catégorie de prestation
     */
    protected int CD_CATEG_PREST;
    /**
     * Marque si vente
     */
    protected int CD_MARQUE;
    /**
     * Code de la prestation
     */
    protected long CD_PREST;
    /**
     * Type de vente (Salon, Vente)
     */
    protected int CD_TYP_VENT;
    /**
     * Unité de mesure
     */
    protected int CD_UNIT_MES;
    /**
     * Commentaire
     */
    protected java.lang.String COMM;
    /**
     * Libellé de la prestation
     */
    protected java.lang.String LIB_PREST;
    /**
     * Prix unitaire TTC
     */
    protected java.math.BigDecimal PRX_UNIT_TTC;
    /**
     * Temps de la prestation
     */
    protected int TPS_PREST;
    /**
     * Indicateur de péremption de la prestation
     */
    protected java.lang.String INDIC_PERIM;
    /**
     * Compteur d'abonnement
     */
    protected int CPT_ABONNEMENT;
    /**
     * Prestation consommatrice de l'abonnement
     */
    protected long CD_PREST_ABONNEMENT;

    /**
     * Indicateur si la prestation est consommatrice d'abonnement
     */
    protected String INDIC_ABONNEMENT;
    
    /**
     * Type de vente pour la vente au détail
     */
    public static final int TYP_VENT_DETAIL = 3;

    /**
     * FactBean constructor comment.
     */
    public PrestBean() {
        super();
        INDIC_PERIM = "N";
    }

    /**
     * PrestBean constructor comment.
     * @param rs java.sql.ResultSet
     */
    public PrestBean(ResultSet rs) {
        super(rs);
        try {
            CD_ART = rs.getLong("CD_ART");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans PrestBean (RS) : " + e.toString());
            }
        }
        try {
            CD_CATEG_PREST = rs.getInt("CD_CATEG_PREST");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans PrestBean (RS) : " + e.toString());
            }
        }
        try {
            CD_MARQUE = rs.getInt("CD_MARQUE");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans PrestBean (RS) : " + e.toString());
            }
        }
        try {
            CD_TYP_VENT = rs.getInt("CD_TYP_VENT");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans PrestBean (RS) : " + e.toString());
            }
        }
        try {
            CD_UNIT_MES = rs.getInt("CD_UNIT_MES");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans PrestBean (RS) : " + e.toString());
            }
        }
        try {
            CD_PREST = rs.getLong("CD_PREST");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans PrestBean (RS) : " + e.toString());
            }
        }
        try {
            COMM = rs.getString("COMM");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans PrestBean (RS) : " + e.toString());
            }
        }

        try {
            LIB_PREST = rs.getString("LIB_PREST");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans PrestBean (RS) : " + e.toString());
            }
        }
        try {
            PRX_UNIT_TTC = rs.getBigDecimal("PRX_UNIT_TTC", 2);
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans ClientBean (RS) : " + e.toString());
            }
        }
        try {
            TPS_PREST = rs.getInt("TPS_PREST");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans PrestBean (RS) : " + e.toString());
            }
        }
        try {
            INDIC_PERIM = rs.getString("INDIC_PERIM");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans PrestBean (RS) : " + e.toString());
            }
        }
        try {
            CPT_ABONNEMENT = rs.getInt("CPT_ABONNEMENT");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans PrestBean (RS) : " + e.toString());
            }
        }
        try {
            CD_PREST_ABONNEMENT = rs.getLong("CD_PREST_ABONNEMENT");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans PrestBean (RS) : " + e.toString());
            }
        }
        try {
            INDIC_ABONNEMENT = rs.getString("INDIC_ABONNEMENT");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans PrestBean (RS) : " + e.toString());
            }
        }
    }
    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public void create(DBSession dbConnect) throws SQLException {

        com.increg.util.SimpleDateFormatEG formatDate = new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");

        StringBuffer req = new StringBuffer("insert into PREST ");
        StringBuffer colonne = new StringBuffer("(");
        StringBuffer valeur = new StringBuffer(" values ( ");

        if (CD_PREST == 0) {
            /**
             * Numérotation automatique des prestations
             */
            String reqMax = "select nextval('seq_prest')";
            try {
                ResultSet aRS = dbConnect.doRequest(reqMax);
                CD_PREST = 1; // Par défaut

                while (aRS.next()) {
                    CD_PREST = aRS.getLong(1);
                }
                aRS.close();
            }
            catch (Exception e) {
                System.out.println("Erreur dans reqSeq : " + e.toString());
            }
        }
        colonne.append("CD_PREST,");
        valeur.append(CD_PREST);
        valeur.append(",");

        if (CD_ART != 0) {
            colonne.append("CD_ART,");
            valeur.append(CD_ART);
            valeur.append(",");
        }

        if (CD_CATEG_PREST != 0) {
            colonne.append("CD_CATEG_PREST,");
            valeur.append(CD_CATEG_PREST);
            valeur.append(",");
        }

        if (CD_MARQUE != 0) {
            colonne.append("CD_MARQUE,");
            valeur.append(CD_MARQUE);
            valeur.append(",");
        }

        if (CD_TYP_VENT != 0) {
            colonne.append("CD_TYP_VENT,");
            valeur.append(CD_TYP_VENT);
            valeur.append(",");
        }

        if (CD_UNIT_MES != 0) {
            colonne.append("CD_UNIT_MES,");
            valeur.append(CD_UNIT_MES);
            valeur.append(",");
        }

        if ((COMM != null) && (COMM.length() != 0)) {
            colonne.append("COMM,");
            valeur.append(DBSession.quoteWith(COMM, '\''));
            valeur.append(",");
        }

        if ((LIB_PREST != null) && (LIB_PREST.length() != 0)) {
            colonne.append("LIB_PREST,");
            valeur.append(DBSession.quoteWith(LIB_PREST, '\''));
            valeur.append(",");
        }

        if ((INDIC_PERIM != null) && (INDIC_PERIM.length() != 0)) {
            colonne.append("INDIC_PERIM,");
            valeur.append(DBSession.quoteWith(INDIC_PERIM, '\''));
            valeur.append(",");
        }

        if (PRX_UNIT_TTC != null) {
            colonne.append("PRX_UNIT_TTC,");
            valeur.append(PRX_UNIT_TTC.toString());
            valeur.append(",");
        }

        if (TPS_PREST != 0) {
            colonne.append("TPS_PREST,");
            valeur.append(TPS_PREST);
            valeur.append(",");
        }

        if (CPT_ABONNEMENT != 0) {
            colonne.append("CPT_ABONNEMENT,");
            valeur.append(CPT_ABONNEMENT);
            valeur.append(",");
        }

        if (CD_PREST_ABONNEMENT != 0) {
            colonne.append("CD_PREST_ABONNEMENT,");
            valeur.append(CD_PREST_ABONNEMENT);
            valeur.append(",");
        }

        if ((INDIC_ABONNEMENT != null) && (INDIC_ABONNEMENT.length() != 0)) {
            colonne.append("INDIC_ABONNEMENT,");
            valeur.append(DBSession.quoteWith(INDIC_ABONNEMENT, '\''));
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

        // Execute la création
        String[] reqs = new String[1];
        reqs[0] = req.toString();
        int[] nb = new int[1];
        nb = dbConnect.doExecuteSQL(reqs);

        if (nb[0] != 1) {
            throw (new SQLException("Création non effectuée"));
        }

        // Impact sur prestation fille ?
        if ((CD_PREST_ABONNEMENT > 0) && (CD_PREST_ABONNEMENT != CD_PREST)) {
            PrestBean prestAbon = PrestBean.getPrestBean(dbConnect, Long.toString(CD_PREST_ABONNEMENT));
            
            if (prestAbon != null) {
                prestAbon.setINDIC_ABONNEMENT("O");
                prestAbon.maj(dbConnect);
            }
        }

    }
    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public void delete(DBSession dbConnect) throws SQLException {

        StringBuffer req = new StringBuffer("delete from PREST ");
        StringBuffer where = new StringBuffer(" where CD_PREST=" + CD_PREST);

        // Constitue la requete finale
        req.append(where);

        // Execute la création
        String[] reqs = new String[1];
        reqs[0] = req.toString();
        int[] nb = new int[1];
        nb = dbConnect.doExecuteSQL(reqs);

        if (nb[0] != 1) {
            throw (new SQLException("Suppression non effectuée"));
        }
    }

    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public void maj(DBSession dbConnect) throws SQLException {

        com.increg.util.SimpleDateFormatEG formatDate = new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");

        StringBuffer req = new StringBuffer("update PREST set ");
        StringBuffer colonne = new StringBuffer("");
        StringBuffer where = new StringBuffer(" where CD_PREST=" + CD_PREST);

        colonne.append("CD_ART=");
        if (CD_ART != 0) {
            colonne.append(CD_ART);
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("CD_CATEG_PREST=");
        if (CD_CATEG_PREST != 0) {
            colonne.append(CD_CATEG_PREST);
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("CD_MARQUE=");
        if (CD_MARQUE != 0) {
            colonne.append(CD_MARQUE);
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("CD_TYP_VENT=");
        if (CD_TYP_VENT != 0) {
            colonne.append(CD_TYP_VENT);
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("CD_UNIT_MES=");
        if (CD_UNIT_MES != 0) {
            colonne.append(CD_UNIT_MES);
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("COMM=");
        if ((COMM != null) && (COMM.length() != 0)) {
            colonne.append(DBSession.quoteWith(COMM, '\''));
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("LIB_PREST=");
        if ((LIB_PREST != null) && (LIB_PREST.length() != 0)) {
            colonne.append(DBSession.quoteWith(LIB_PREST, '\''));
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("PRX_UNIT_TTC=");
        if (PRX_UNIT_TTC != null) {
            colonne.append(PRX_UNIT_TTC.toString());
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("TPS_PREST=");
        if (TPS_PREST != 0) {
            colonne.append(TPS_PREST);
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("INDIC_PERIM=");
        if ((INDIC_PERIM != null) && (INDIC_PERIM.length() != 0)) {
            colonne.append(DBSession.quoteWith(INDIC_PERIM, '\''));
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("CPT_ABONNEMENT=");
        if (CPT_ABONNEMENT != 0) {
            colonne.append(CPT_ABONNEMENT);
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("CD_PREST_ABONNEMENT=");
        if (CD_PREST_ABONNEMENT != 0) {
            colonne.append(CD_PREST_ABONNEMENT);
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("INDIC_ABONNEMENT=");
        if ((INDIC_ABONNEMENT != null) && (INDIC_ABONNEMENT.length() != 0)) {
            colonne.append(DBSession.quoteWith(INDIC_ABONNEMENT, '\''));
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("DT_MODIF=");
        DT_MODIF = Calendar.getInstance();
        colonne.append(DBSession.quoteWith(formatDate.formatEG(DT_MODIF.getTime()), '\''));

        // Constitue la requete finale
        req.append(colonne);
        req.append(where);

        // Execute la création
        String[] reqs = new String[1];
        reqs[0] = req.toString();
        int[] nb = new int[1];
        nb = dbConnect.doExecuteSQL(reqs);

        if (nb[0] != 1) {
            throw (new SQLException("Mise à jour non effectuée"));
        }

        // Impact sur prestation fille ?
        if ((CD_PREST_ABONNEMENT > 0) && (CD_PREST_ABONNEMENT != CD_PREST)) {
            PrestBean prestAbon = PrestBean.getPrestBean(dbConnect, Long.toString(CD_PREST_ABONNEMENT));
            
            if (prestAbon != null) {
                prestAbon.setINDIC_ABONNEMENT("O");
                prestAbon.maj(dbConnect);
            }
        }

    }

    /**
     * Purge des prestations
     * @param dbConnect Connexion à la base à utiliser
     * @param dateLimite Date limite de purge : Seront purgés les prestations n'ayant plus d'historiques et créées avant cette date
     * @exception FctlException En cas d'erreur durant la mise à jour
     * @return Nombre d'enregistrements purgés : -1 En cas d'erreur
     */
    public static int purge(DBSession dbConnect, java.util.Date dateLimite) throws FctlException {

        int nbEnreg = -1;

        com.increg.util.SimpleDateFormatEG formatDate = new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");

        String[] reqSQL = new String[1];

        reqSQL[0] = "delete from PREST where DT_CREAT < " + DBSession.quoteWith(formatDate.format(dateLimite), '\'') + " and (select count(*) from HISTO_PREST where CD_PREST = PREST.CD_PREST) = 0";

        dbConnect.setDansTransactions(true);

        try {
            int[] res = dbConnect.doExecuteSQL(reqSQL);

            nbEnreg = res[0];
        }
        catch (Exception e) {
            System.out.println("Erreur dans Purge des prestations: " + e.toString());
            dbConnect.cleanTransaction();
            throw new FctlException("Erreur à la purge des prestations.");
        }

        // Fin de cette transaction
        dbConnect.endTransaction();

        return nbEnreg;
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
     * Creation date: (19/08/2001 20:55:16)
     * @return int
     */
    public int getCD_CATEG_PREST() {
        return CD_CATEG_PREST;
    }

    /**
     * Insert the method's description here.
     * Creation date: (25/08/2001 19:40:24)
     * @return int
     */
    public int getCD_MARQUE() {
        return CD_MARQUE;
    }

    /**
     * Insert the method's description here.
     * Creation date: (18/08/2001 13:24:22)
     * @return long
     */
    public long getCD_PREST() {
        return CD_PREST;
    }

    /**
     * Insert the method's description here.
     * Creation date: (19/08/2001 20:55:16)
     * @return int
     */
    public int getCD_TYP_VENT() {
        return CD_TYP_VENT;
    }

    /**
     * Insert the method's description here.
     * Creation date: (19/08/2001 20:55:16)
     * @return int
     */
    public int getCD_UNIT_MES() {
        return CD_UNIT_MES;
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
     * Insert the method's description here.
     * Creation date: (19/08/2001 20:52:52)
     * @return java.lang.String
     */
    public java.lang.String getLIB_PREST() {
        return LIB_PREST;
    }

    /**
     * Donne l'indicateur de péremption O/N
     * Creation date: (19/08/2001 20:52:52)
     * @return java.lang.String
     */
    public java.lang.String getINDIC_PERIM() {
        return INDIC_PERIM;
    }

    /**
     * Création d'un Bean Prestation à partir de sa clé
     * Creation date: (19/08/2001 21:14:20)
     * @param dbConnect com.increg.salon.bean.DBSession
     * @param CD_PREST java.lang.String
     * @return Prestation chargée
     */
    public static PrestBean getPrestBean(DBSession dbConnect, String CD_PREST) {
        String reqSQL = "select * from PREST where CD_PREST=" + CD_PREST;
        PrestBean res = null;

        // Interroge la Base
        try {
            ResultSet aRS = dbConnect.doRequest(reqSQL);

            while (aRS.next()) {
                res = new PrestBean(aRS);
            }
            aRS.close();
        }
        catch (Exception e) {
            System.out.println("Erreur dans constructeur sur clé : " + e.toString());
        }
        return res;
    }

    /**
     * Création d'un Bean Prestation à partir de son article
     * Creation date: (19/08/2001 21:14:20)
     * @param dbConnect com.increg.salon.bean.DBSession
     * @param CD_ART java.lang.String
     * @return Prestation chargée
     */
    public static PrestBean getPrestBeanFromArt(DBSession dbConnect, String CD_ART) {
        String reqSQL = "select * from PREST where CD_ART=" + CD_ART;
        PrestBean res = null;

        // Interroge la Base
        try {
            ResultSet aRS = dbConnect.doRequest(reqSQL);

            while (aRS.next()) {
                res = new PrestBean(aRS);
            }
            aRS.close();
        }
        catch (Exception e) {
            System.out.println("Erreur dans constructeur sur clé : " + e.toString());
        }
        return res;
    }

    /**
     * Insert the method's description here.
     * Creation date: (18/08/2001 14:37:57)
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getPRX_UNIT_TTC() {
        return PRX_UNIT_TTC;
    }

    /**
     * Insert the method's description here.
     * Creation date: (19/08/2001 20:55:16)
     * @return int
     */
    public int getTPS_PREST() {
        return TPS_PREST;
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
     * Creation date: (19/08/2001 20:55:16)
     * @param newCD_CATEG_PREST int
     */
    public void setCD_CATEG_PREST(int newCD_CATEG_PREST) {
        CD_CATEG_PREST = newCD_CATEG_PREST;
    }

    /**
     * Insert the method's description here.
     * Creation date: (19/08/2001 20:55:16)
     * @param newCD_CATEG_PREST String
     */
    public void setCD_CATEG_PREST(String newCD_CATEG_PREST) {
        if ((newCD_CATEG_PREST != null) && (newCD_CATEG_PREST.length() != 0)) {
            CD_CATEG_PREST = Integer.parseInt(newCD_CATEG_PREST);
        }
        else {
            CD_CATEG_PREST = 0;
        }
    }

    /**
     * Insert the method's description here.
     * Creation date: (25/08/2001 19:40:24)
     * @param newCD_MARQUE int
     */
    public void setCD_MARQUE(int newCD_MARQUE) {
        CD_MARQUE = newCD_MARQUE;
    }

    /**
     * Insert the method's description here.
     * Creation date: (25/08/2001 19:40:24)
     * @param newCD_MARQUE String
     */
    public void setCD_MARQUE(String newCD_MARQUE) {
        if ((newCD_MARQUE != null) && (newCD_MARQUE.length() != 0)) {
            CD_MARQUE = Integer.parseInt(newCD_MARQUE);
        }
        else {
            CD_MARQUE = 0;
        }
    }

    /**
     * Insert the method's description here.
     * Creation date: (18/08/2001 13:24:22)
     * @param newCD_PREST long
     */
    public void setCD_PREST(long newCD_PREST) {
        CD_PREST = newCD_PREST;
    }

    /**
     * Insert the method's description here.
     * Creation date: (18/08/2001 13:24:22)
     * @param newCD_PREST String
     */
    public void setCD_PREST(String newCD_PREST) {
        if ((newCD_PREST != null) && (newCD_PREST.length() != 0)) {
            CD_PREST = Long.parseLong(newCD_PREST);
        }
        else {
            CD_PREST = 0;
        }
    }

    /**
     * Insert the method's description here.
     * Creation date: (19/08/2001 20:55:16)
     * @param newCD_TYP_VENT int
     */
    public void setCD_TYP_VENT(int newCD_TYP_VENT) {
        CD_TYP_VENT = newCD_TYP_VENT;
    }

    /**
     * Insert the method's description here.
     * Creation date: (19/08/2001 20:55:16)
     * @param newCD_TYP_VENT String
     */
    public void setCD_TYP_VENT(String newCD_TYP_VENT) {
        if ((newCD_TYP_VENT != null) && (newCD_TYP_VENT.length() != 0)) {
            CD_TYP_VENT = Integer.parseInt(newCD_TYP_VENT);
        }
        else {
            CD_TYP_VENT = 0;
        }
    }

    /**
     * Insert the method's description here.
     * Creation date: (19/08/2001 20:55:16)
     * @param newCD_UNIT_MES int
     */
    public void setCD_UNIT_MES(int newCD_UNIT_MES) {
        CD_UNIT_MES = newCD_UNIT_MES;
    }

    /**
     * Insert the method's description here.
     * Creation date: (19/08/2001 20:55:16)
     * @param newCD_UNIT_MES String
     */
    public void setCD_UNIT_MES(String newCD_UNIT_MES) {
        if ((newCD_UNIT_MES != null) && (newCD_UNIT_MES.length() != 0)) {
            CD_UNIT_MES = Integer.parseInt(newCD_UNIT_MES);
        }
        else {
            CD_UNIT_MES = 0;
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
     * Insert the method's description here.
     * Creation date: (19/08/2001 20:52:52)
     * @param newLIB_PREST java.lang.String
     */
    public void setLIB_PREST(java.lang.String newLIB_PREST) {
        LIB_PREST = newLIB_PREST;
    }

    /**
     * Positionne l'indicateur de péremption de la prestation
     * Creation date: 29 déc. 2002
     * @param newINDIC_PERIM java.lang.String
     */
    public void setINDIC_PERIM(java.lang.String newINDIC_PERIM) {
        INDIC_PERIM = newINDIC_PERIM;
    }

    /**
     * Insert the method's description here.
     * Creation date: (18/08/2001 14:37:57)
     * @param newPRX_UNIT_TTC String
     */
    public void setPRX_UNIT_TTC(String newPRX_UNIT_TTC) {

        if ((newPRX_UNIT_TTC != null) && (newPRX_UNIT_TTC.length() != 0)) {
            PRX_UNIT_TTC = new BigDecimal(newPRX_UNIT_TTC);
        }
        else {
            PRX_UNIT_TTC = null;
        }
    }

    /**
     * Insert the method's description here.
     * Creation date: (18/08/2001 14:37:57)
     * @param newPRX_UNIT_TTC java.math.BigDecimal
     */
    public void setPRX_UNIT_TTC(java.math.BigDecimal newPRX_UNIT_TTC) {
        PRX_UNIT_TTC = newPRX_UNIT_TTC;
    }

    /**
     * Insert the method's description here.
     * Creation date: (19/08/2001 20:55:16)
     * @param newTPS_PREST int
     */
    public void setTPS_PREST(int newTPS_PREST) {
        TPS_PREST = newTPS_PREST;
    }

    /**
     * Insert the method's description here.
     * Creation date: (19/08/2001 20:55:16)
     * @param newTPS_PREST String
     * @throws FctlException Si le format est incorrect
     */
    public void setTPS_PREST(String newTPS_PREST) throws FctlException {
        if ((newTPS_PREST != null) && (newTPS_PREST.length() != 0)) {
            try {
                TPS_PREST = Integer.parseInt(newTPS_PREST);
            }
            catch (Exception e) {
                throw (new FctlException("Le temps de la prestation doit être en minutes"));
            }
        }
        else {
            TPS_PREST = 0;
        }

    }

    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public java.lang.String toString() {
        return getLIB_PREST();
    }
    /**
     * @return Compteur d'abonnement
     */
    public int getCPT_ABONNEMENT() {
        return CPT_ABONNEMENT;
    }

    /**
     * @param newCPT_ABONNEMENT Compteur d'abonnement
     */
    public void setCPT_ABONNEMENT(int newCPT_ABONNEMENT) {
        CPT_ABONNEMENT = newCPT_ABONNEMENT;
    }

    /**
     * Insert the method's description here.
     * @param newCPT_ABONNEMENT String
     * @throws FctlException Si le format est incorrect
     */
    public void setCPT_ABONNEMENT(String newCPT_ABONNEMENT) throws FctlException {
        if ((newCPT_ABONNEMENT != null) && (newCPT_ABONNEMENT.length() != 0)) {
            try {
                CPT_ABONNEMENT = Integer.parseInt(newCPT_ABONNEMENT);
            }
            catch (Exception e) {
                throw (new FctlException("Le nombre pour l'abonnement n'est pas correct"));
            }
        }
        else {
            CPT_ABONNEMENT = 0;
        }

    }

    /**
     * @return true si la prestation est un abonnement
     */
    public boolean isAbonnement() {
        return (CPT_ABONNEMENT > 1);
    }

    /**
     * @return Prestation consommatrice de l'abonnement
     */
    public long getCD_PREST_ABONNEMENT() {
        return CD_PREST_ABONNEMENT;
    }

    /**
     * @param newCD_PREST_ABONNEMENT Prestation consommatrice de l'abonnement
     * @throws FctlException Si valeur incohérente
     */
    public void setCD_PREST_ABONNEMENT(long newCD_PREST_ABONNEMENT) throws FctlException {
        if ((newCD_PREST_ABONNEMENT != CD_PREST) || (newCD_PREST_ABONNEMENT == 0)) {
            CD_PREST_ABONNEMENT = newCD_PREST_ABONNEMENT;
        }
        else {
            throw (new FctlException("La prestation unitaire de l'abonnement ne peut être elle même"));
        }
    }

    /**
     * Insert the method's description here.
     * @param newCD_PREST_ABONNEMENT Prestation consommatrice de l'abonnement
     * @throws FctlException Si le format est incorrect
     */
    public void setCD_PREST_ABONNEMENT(String newCD_PREST_ABONNEMENT) throws FctlException {
        if ((newCD_PREST_ABONNEMENT != null) && (newCD_PREST_ABONNEMENT.length() != 0)) {
            try {
                setCD_PREST_ABONNEMENT(Long.parseLong(newCD_PREST_ABONNEMENT));
            }
            catch (NumberFormatException e) {
                throw (new FctlException("Le code abonnement n'est pas correct"));
            }
        }
        else {
            CD_PREST_ABONNEMENT = 0;
        }

    }

    /**
     * @return true si la prestation est une consommation d'abonnement
     */
    public boolean isConsommationAbonnement() {
        return ((INDIC_ABONNEMENT != null) && (INDIC_ABONNEMENT.equals("O")));
    }

    /**
     * @return Indicateur si la prestation est consommatrice d'abonnement
     */
    public String getINDIC_ABONNEMENT() {
        return INDIC_ABONNEMENT;
    }

    /**
     * @param string Indicateur si la prestation est consommatrice d'abonnement
     */
    public void setINDIC_ABONNEMENT(String string) {
        INDIC_ABONNEMENT = string;
    }

}
