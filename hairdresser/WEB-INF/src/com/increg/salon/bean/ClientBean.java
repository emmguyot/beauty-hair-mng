package com.increg.salon.bean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.increg.commun.DBSession;
import com.increg.commun.TimeStampBean;
import com.increg.commun.exception.FctlException;
import com.increg.util.SimpleDateFormatEG;


/**
 * Bean de gestion de client
 * Creation date: (18/07/2001 22:43:26)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class ClientBean extends TimeStampBean implements Comparable {
    /**
     * Code client interne
     */
    protected long CD_CLI;
    /**
     * Civilité (M., Mme, Mle) du client
     */
    protected String CIVILITE;
    /**
     * Nom du client
     */
    protected String NOM;
    /**
     * Prénom du client
     */
    protected String PRENOM;
    /**
     * Adresse du client
     */
    protected String RUE;
    /**
     * Ville du client
     */
    protected String VILLE;
    /**
     * Code postal 
     */
    protected String CD_POSTAL;
    /**
     * Téléphone du client
     */
    protected String TEL;
    /**
     * Portable du client
     */
    protected String PORTABLE;
    /**
     * Adresse email du client
     */
    protected String EMAIL;
    /**
     * Date d'anniversaire du client
     */
    protected Calendar DT_ANNIV;
    /**
     * Type de cheveux
     */
    protected int CD_TYP_CHEV;
    /**
     * Type de peau
     */
    protected int CD_TYP_PEAU;
    /**
     * Catégorie de client
     */
    protected int CD_CATEG_CLI;
    /**
     * Tranche d'âge du client
     */
    protected int CD_TR_AGE;
    /**
     * Origine du client
     */
    protected int CD_ORIG;
    /**
     * Commentaire associé au client
     */
    protected String COMM;
    /**
     * Etat de validité du client
     */
    protected String INDIC_VALID;
    /**
     * Abonnement du client
     */
    protected HashMap abonnements;
    
    /**
     * ClientBean constructor comment.
     */
    public ClientBean() {
        super();
        INDIC_VALID = "O";
        abonnements = new HashMap();
    }
    /**
     * ClientBean à partir d'un RecordSet.
     * @param rs ResultSet dans lequel piocher les données
     */
    public ClientBean(ResultSet rs) {
        super(rs);
        try {
            CD_CATEG_CLI = rs.getInt("CD_CATEG_CLI");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans ClientBean (RS) : " + e.toString());
            }
        }
        try {
            CD_CLI = rs.getLong("CD_CLI");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans ClientBean (RS) : " + e.toString());
            }
        }
        try {
            CD_ORIG = rs.getInt("CD_ORIG");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans ClientBean (RS) : " + e.toString());
            }
        }
        try {
            CD_POSTAL = rs.getString("CD_POSTAL");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans ClientBean (RS) : " + e.toString());
            }
        }
        try {
            CD_TR_AGE = rs.getInt("CD_TR_AGE");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans ClientBean (RS) : " + e.toString());
            }
        }
        try {
            CD_TYP_CHEV = rs.getInt("CD_TYP_CHEV");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans ClientBean (RS) : " + e.toString());
            }
        }
        try {
            CD_TYP_PEAU = rs.getInt("CD_TYP_PEAU");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans ClientBean (RS) : " + e.toString());
            }
        }
        try {
            CIVILITE = rs.getString("CIVILITE");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans ClientBean (RS) : " + e.toString());
            }
        }
        try {
            COMM = rs.getString("COMM");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans ClientBean (RS) : " + e.toString());
            }
        }
        try {
            Date DtAnniv = rs.getDate("DT_ANNIV");
            if (DtAnniv != null) {
                DT_ANNIV = Calendar.getInstance();
                DT_ANNIV.setTime(DtAnniv);
            }
            else {
                DT_ANNIV = null;
            }
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans ClientBean (RS) : " + e.toString());
            }
        }
        try {
            EMAIL = rs.getString("EMAIL");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans ClientBean (RS) : " + e.toString());
            }
        }
        try {
            NOM = rs.getString("NOM");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans ClientBean (RS) : " + e.toString());
            }
        }
        try {
            PORTABLE = rs.getString("PORTABLE");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans ClientBean (RS) : " + e.toString());
            }
        }
        try {
            PRENOM = rs.getString("PRENOM");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans ClientBean (RS) : " + e.toString());
            }
        }
        try {
            RUE = rs.getString("RUE");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans ClientBean (RS) : " + e.toString());
            }
        }
        try {
            TEL = rs.getString("TEL");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans ClientBean (RS) : " + e.toString());
            }
        }
        try {
            VILLE = rs.getString("VILLE");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans ClientBean (RS) : " + e.toString());
            }
        }
        try {
            INDIC_VALID = rs.getString("INDIC_VALID");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans ClientBean (RS) : " + e.toString());
            }
        }
        try {
            abonnements = new HashMap();
            ResultSet rs2 = rs.getStatement().getConnection().createStatement().executeQuery("select * from ABO_CLI where CD_CLI=" + CD_CLI);
            while (rs2.next()) {
                abonnements.put(new Long(rs2.getLong("CD_PREST")), new Integer(rs2.getInt("CPT")));
            }
            rs2.close();
        }
        catch (SQLException e1) {
            System.out.println("Erreur dans ClientBean (RS2) : " + e1.toString());
        }
    }
    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public void create(DBSession dbConnect) throws SQLException {

        com.increg.util.SimpleDateFormatEG formatDate =
            new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");

        setDefaultCD_TR_AGE(dbConnect);

        StringBuffer req = new StringBuffer("insert into CLI ");
        StringBuffer colonne = new StringBuffer("(");
        StringBuffer valeur = new StringBuffer(" values ( ");

        if (CD_CLI == 0) {
            /**
             * Numérotation automatique des codes clients
             */
            String reqMax = "select nextval('seq_cli')";
            try {
                ResultSet aRS = dbConnect.doRequest(reqMax);
                CD_CLI = 1; // Par défaut

                while (aRS.next()) {
                    CD_CLI = aRS.getLong(1);
                }
                aRS.close();
            }
            catch (Exception e) {
                System.out.println("Erreur dans reqSeq : " + e.toString());
            }
        }
        colonne.append("CD_CLI,");
        valeur.append(CD_CLI);
        valeur.append(",");

        if ((CIVILITE != null) && (CIVILITE.length() != 0)) {
            colonne.append("CIVILITE,");
            valeur.append(DBSession.quoteWith(CIVILITE, '\''));
            valeur.append(",");
        }

        if ((NOM != null) && (NOM.length() != 0)) {
            colonne.append("NOM,");
            valeur.append(DBSession.quoteWith(NOM, '\''));
            valeur.append(",");
        }

        if ((PRENOM != null) && (PRENOM.length() != 0)) {
            colonne.append("PRENOM,");
            valeur.append(DBSession.quoteWith(PRENOM, '\''));
            valeur.append(",");
        }

        if ((RUE != null) && (RUE.length() != 0)) {
            colonne.append("RUE,");
            valeur.append(DBSession.quoteWith(RUE, '\''));
            valeur.append(",");
        }

        if ((VILLE != null) && (VILLE.length() != 0)) {
            colonne.append("VILLE,");
            valeur.append(DBSession.quoteWith(VILLE, '\''));
            valeur.append(",");
        }

        if ((CD_POSTAL != null) && (CD_POSTAL.length() != 0)) {
            colonne.append("CD_POSTAL,");
            valeur.append(DBSession.quoteWith(CD_POSTAL, '\''));
            valeur.append(",");
        }

        if ((TEL != null) && (TEL.length() != 0)) {
            colonne.append("TEL,");
            valeur.append(DBSession.quoteWith(TEL, '\''));
            valeur.append(",");
        }

        if ((PORTABLE != null) && (PORTABLE.length() != 0)) {
            colonne.append("PORTABLE,");
            valeur.append(DBSession.quoteWith(PORTABLE, '\''));
            valeur.append(",");
        }

        if ((EMAIL != null) && (EMAIL.length() != 0)) {
            colonne.append("EMAIL,");
            valeur.append(DBSession.quoteWith(EMAIL, '\''));
            valeur.append(",");
        }

        if (DT_ANNIV != null) {
            colonne.append("DT_ANNIV,");
            valeur.append(
                DBSession.quoteWith(
                    formatDate.formatEG(DT_ANNIV.getTime()),
                    '\''));
            valeur.append(",");
        }

        if (CD_TYP_CHEV != 0) {
            colonne.append("CD_TYP_CHEV,");
            valeur.append(CD_TYP_CHEV);
            valeur.append(",");
        }

        if (CD_TYP_PEAU != 0) {
            colonne.append("CD_TYP_PEAU,");
            valeur.append(CD_TYP_PEAU);
            valeur.append(",");
        }

        if (CD_CATEG_CLI != 0) {
            colonne.append("CD_CATEG_CLI,");
            valeur.append(CD_CATEG_CLI);
            valeur.append(",");
        }

        if (CD_TR_AGE != 0) {
            colonne.append("CD_TR_AGE,");
            valeur.append(CD_TR_AGE);
            valeur.append(",");
        }

        if (CD_ORIG != 0) {
            colonne.append("CD_ORIG,");
            valeur.append(CD_ORIG);
            valeur.append(",");
        }

        if ((COMM != null) && (COMM.length() != 0)) {
            colonne.append("COMM,");
            valeur.append(DBSession.quoteWith(COMM, '\''));
            valeur.append(",");
        }

        if ((INDIC_VALID != null) && (INDIC_VALID.length() != 0)) {
            colonne.append("INDIC_VALID,");
            valeur.append(DBSession.quoteWith(INDIC_VALID, '\''));
            valeur.append(",");
        }

        if (DT_CREAT != null) {
            colonne.append("DT_CREAT,");
            valeur.append(
                DBSession.quoteWith(
                    formatDate.formatEG(DT_CREAT.getTime()),
                    '\''));
            valeur.append(",");
        }

        if (DT_MODIF != null) {
            colonne.append("DT_MODIF,");
            valeur.append(
                DBSession.quoteWith(
                    formatDate.formatEG(DT_MODIF.getTime()),
                    '\''));
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

        // Execute la création & la mise à jour des abonnements
        String[] reqs = new String[1 + 1 + abonnements.size()];
        int i = 0;
        reqs[i++] = req.toString();
        reqs[i++] = "delete from ABO_CLI where CD_CLI=" + CD_CLI;

        // Abonnements
        Set keys = abonnements.keySet();
        Iterator keyIter = keys.iterator();
        while (keyIter.hasNext()) {
            Long CD_PREST = (Long) keyIter.next();
            Integer CPT = (Integer) abonnements.get(CD_PREST);
            
            reqs[i++] = "insert into ABO_CLI (CD_CLI, CD_PREST, CPT) values ("
                        + CD_CLI + ", " + CD_PREST + ", " + CPT + ")"; 
        }
        int[] nb = new int[reqs.length];
        nb = dbConnect.doExecuteSQL(reqs);

        if (nb[0] != 1) {
            throw (new SQLException("Création non effectuée"));
        }

    }
    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public void delete(DBSession dbConnect) throws java.sql.SQLException {

        setDefaultCD_TR_AGE(dbConnect);

        StringBuffer req = new StringBuffer("delete from CLI ");
        StringBuffer req2 = new StringBuffer("delete from ABO_CLI ");
        StringBuffer where = new StringBuffer(" where CD_CLI=" + CD_CLI);

        // Constitue la requete finale
        req.append(where);
        req2.append(where);

        // Execute la création
        String[] reqs = new String[2];
        reqs[0] = req.toString();
        reqs[1] = req2.toString();
        int[] nb = new int[2];
        nb = dbConnect.doExecuteSQL(reqs);

        if (nb[0] != 1) {
            throw (new SQLException("Suppression non effectuée"));
        }

    }
    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:51:42)
     * @return int
     */
    public int getCD_CATEG_CLI() {
        return CD_CATEG_CLI;
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:45:39)
     * @return long
     */
    public long getCD_CLI() {
        return CD_CLI;
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:52:07)
     * @return int
     */
    public int getCD_ORIG() {
        return CD_ORIG;
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:48:09)
     * @return java.lang.String
     */
    public java.lang.String getCD_POSTAL() {
        if (CD_POSTAL == null) {
            return "";
        }
        else {
            return CD_POSTAL;
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:51:57)
     * @return int
     */
    public int getCD_TR_AGE() {
        return CD_TR_AGE;
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:51:23)
     * @return int
     */
    public int getCD_TYP_CHEV() {
        return CD_TYP_CHEV;
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:51:23)
     * @return int
     */
    public int getCD_TYP_PEAU() {
        return CD_TYP_PEAU;
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:46:26)
     * @return java.lang.String
     */
    public java.lang.String getCIVILITE() {
        if (CIVILITE == null) {
            return "";
        }
        else {
            return CIVILITE;
        }
    }
    /**
     * Création d'un Bean Client à partir de sa clé
     * Creation date: (18/08/2001 17:05:45)
     * @param dbConnect com.increg.salon.bean.DBSession
     * @param CD_CLI java.lang.String
     * @return Bean créée
     */
    public static ClientBean getClientBean(
        DBSession dbConnect,
        String CD_CLI) {
        String reqSQL = "select * from CLI where CD_CLI=" + CD_CLI;
        ClientBean res = null;

        // Interroge la Base
        try {
            ResultSet aRS = dbConnect.doRequest(reqSQL);

            while (aRS.next()) {
                res = new ClientBean(aRS);
            }
            aRS.close();
        }
        catch (Exception e) {
            System.out.println(
                "Erreur dans constructeur sur clé : " + e.toString());
        }
        return res;
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:52:23)
     * @return java.lang.String
     */
    public String getCOMM() {
        if (COMM == null) {
            return "";
        }
        else {
            return COMM;
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:49:19)
     * @return java.util.Calendar
     */
    public Calendar getDT_ANNIV() {
        return DT_ANNIV;
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:48:46)
     * @return java.lang.String
     */
    public String getEMAIL() {
        if (EMAIL == null) {
            return "";
        }
        else {
            return EMAIL;
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:46:51)
     * @return java.lang.String
     */
    public String getNOM() {

        if (NOM == null) {
            return "";
        }
        else {
            return NOM;
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:48:37)
     * @return java.lang.String
     */
    public String getPORTABLE() {

        if (PORTABLE == null) {
            return "";
        }
        else {
            return PORTABLE;
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:47:09)
     * @return java.lang.String
     */
    public String getPRENOM() {

        if (PRENOM == null) {
            return "";
        }
        else {
            return PRENOM;
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:47:24)
     * @return java.lang.String
     */
    public String getRUE() {

        if (RUE == null) {
            return "";
        }
        else {
            return RUE;
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:48:21)
     * @return java.lang.String
     */
    public String getTEL() {

        if (TEL == null) {
            return "";
        }
        else {
            return TEL;
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:47:47)
     * @return java.lang.String
     */
    public String getVILLE() {

        if (VILLE == null) {
            return "";
        }
        else {
            return VILLE;
        }
    }
    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public void maj(DBSession dbConnect) throws SQLException {

        SimpleDateFormatEG formatDate =
            new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");

        setDefaultCD_TR_AGE(dbConnect);

        StringBuffer req = new StringBuffer("update CLI set ");
        StringBuffer colonne = new StringBuffer("");
        StringBuffer where = new StringBuffer(" where CD_CLI=" + CD_CLI);

        colonne.append("CIVILITE=");
        if ((CIVILITE != null) && (CIVILITE.length() != 0)) {
            colonne.append(DBSession.quoteWith(CIVILITE, '\''));
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("NOM=");
        if ((NOM != null) && (NOM.length() != 0)) {
            colonne.append(DBSession.quoteWith(NOM, '\''));
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("PRENOM=");
        if ((PRENOM != null) && (PRENOM.length() != 0)) {
            colonne.append(DBSession.quoteWith(PRENOM, '\''));
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("RUE=");
        if ((RUE != null) && (RUE.length() != 0)) {
            colonne.append(DBSession.quoteWith(RUE, '\''));
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("VILLE=");
        if ((VILLE != null) && (VILLE.length() != 0)) {
            colonne.append(DBSession.quoteWith(VILLE, '\''));
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("CD_POSTAL=");
        if ((CD_POSTAL != null) && (CD_POSTAL.length() != 0)) {
            colonne.append(DBSession.quoteWith(CD_POSTAL, '\''));
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("TEL=");
        if ((TEL != null) && (TEL.length() != 0)) {
            colonne.append(DBSession.quoteWith(TEL, '\''));
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("PORTABLE=");
        if ((PORTABLE != null) && (PORTABLE.length() != 0)) {
            colonne.append(DBSession.quoteWith(PORTABLE, '\''));
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("EMAIL=");
        if ((EMAIL != null) && (EMAIL.length() != 0)) {
            colonne.append(DBSession.quoteWith(EMAIL, '\''));
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("DT_ANNIV=");
        if (DT_ANNIV != null) {
            colonne.append(
                DBSession.quoteWith(
                    formatDate.formatEG(DT_ANNIV.getTime()),
                    '\''));
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("CD_TYP_CHEV=");
        if (CD_TYP_CHEV != 0) {
            colonne.append(CD_TYP_CHEV);
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("CD_TYP_PEAU=");
        if (CD_TYP_PEAU != 0) {
            colonne.append(CD_TYP_PEAU);
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("CD_CATEG_CLI=");
        if (CD_CATEG_CLI != 0) {
            colonne.append(CD_CATEG_CLI);
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("CD_TR_AGE=");
        if (CD_TR_AGE != 0) {
            colonne.append(CD_TR_AGE);
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("CD_ORIG=");
        if (CD_ORIG != 0) {
            colonne.append(CD_ORIG);
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

        colonne.append("INDIC_VALID=");
        if ((INDIC_VALID != null) && (INDIC_VALID.length() != 0)) {
            colonne.append(DBSession.quoteWith(INDIC_VALID, '\''));
        }
        else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("DT_MODIF=");
        DT_MODIF = Calendar.getInstance();
        colonne.append(
            DBSession.quoteWith(formatDate.formatEG(DT_MODIF.getTime()), '\''));

        // Constitue la requete finale
        req.append(colonne);
        req.append(where);

        // Execute la création & la mise à jour des abonnements
        String[] reqs = new String[1 + 1 + abonnements.size()];
        int i = 0;
        reqs[i++] = req.toString();
        reqs[i++] = "delete from ABO_CLI where CD_CLI=" + CD_CLI;

        // Abonnements
        Set keys = abonnements.keySet();
        Iterator keyIter = keys.iterator();
        while (keyIter.hasNext()) {
            Long CD_PREST = (Long) keyIter.next();
            Integer CPT = (Integer) abonnements.get(CD_PREST);
            
            reqs[i++] = "insert into ABO_CLI (CD_CLI, CD_PREST, CPT) values ("
                        + CD_CLI + ", " + CD_PREST + ", " + CPT + ")"; 
        }
        int[] nb = new int[reqs.length];
        nb = dbConnect.doExecuteSQL(reqs);

        if (nb[0] != 1) {
            throw (new SQLException("Mise à jour non effectuée"));
        }

    }

    /**
     * Purge des clients
     * @param dbConnect Connexion à la base à utiliser
     * @param dateLimite Date limite de purge : Seront purgés les clients n'ayant plus d'historique et créés avant cette date
     * @exception FctlException En cas d'erreur durant la mise à jour
     * @return Nombre d'enregistrements purgés : -1 En cas d'erreur
     */
    public static int purge(DBSession dbConnect, java.util.Date dateLimite) throws FctlException {
        
        return purge(dbConnect, dateLimite, "");
    }

    /**
     * Purge des clients
     * @param dbConnect Connexion à la base à utiliser
     * @param dateLimite Date limite de purge : Seront purgés les clients n'ayant plus d'historique et créés avant cette date
     * @param filtre Fitlre supplémentaire
     * @exception FctlException En cas d'erreur durant la mise à jour
     * @return Nombre d'enregistrements purgés : -1 En cas d'erreur
     */
    public static int purge(DBSession dbConnect, java.util.Date dateLimite, String filtre) throws FctlException {
        
        int nbEnreg = -1;
        
        com.increg.util.SimpleDateFormatEG formatDate = new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");
        
        String selection = "select CD_CLI from CLI where DT_CREAT < " + DBSession.quoteWith(formatDate.format(dateLimite), '\'')
                + " and (select count(*) from HISTO_PREST where CD_CLI = CLI.CD_CLI) = 0"
                + " and (select count(*) from FACT where CD_CLI = CLI.CD_CLI) = 0"
                + " and (select count(*) from RDV where CD_CLI = CLI.CD_CLI) = 0"
                + filtre;

        
        dbConnect.setDansTransactions(true);

                
        try {
            ResultSet rs = dbConnect.doRequest(selection);
            nbEnreg = 0;
            
            // Suppression un à un
            while (rs.next()) {
                String[] reqSQL = new String[2];
         
                reqSQL[0] = "delete from ABO_CLI where CD_CLI=" + rs.getString("CD_CLI");
                reqSQL[1] = "delete from CLI where CD_CLI=" + rs.getString("CD_CLI");

                int[] res = dbConnect.doExecuteSQL(reqSQL);
            
                nbEnreg += res[1];
            }
            rs.close();

        }
        catch (Exception e) {
            System.out.println("Erreur dans Purge des clients: " + e.toString());
            dbConnect.cleanTransaction();
            throw new FctlException("Erreur à la purge des clients.");
        }
        
        // Fin de cette transaction
        dbConnect.endTransaction();
        
        return nbEnreg;
    }


    /**
     * Purge des clients à partir des périmés seulement
     * @param dbConnect Connexion à la base à utiliser
     * @param dateLimite Date limite de purge : Seront purgés les clients n'ayant plus d'historique et créés avant cette date
     * @exception FctlException En cas d'erreur durant la mise à jour
     * @return Nombre d'enregistrements purgés : -1 En cas d'erreur
     */
    public static int purgePerime(DBSession dbConnect, java.util.Date dateLimite) throws FctlException {
        
        return purge(dbConnect, dateLimite, " and CLI.INDIC_VALID = 'N'");
    }

    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:51:42)
     * @param newCD_CATEG_CLI int
     */
    public void setCD_CATEG_CLI(int newCD_CATEG_CLI) {
        CD_CATEG_CLI = newCD_CATEG_CLI;
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:51:42)
     * @param newCD_CATEG_CLI int
     */
    public void setCD_CATEG_CLI(String newCD_CATEG_CLI) {

        if ((newCD_CATEG_CLI != null) && (newCD_CATEG_CLI.length() != 0)) {
            CD_CATEG_CLI = Integer.parseInt(newCD_CATEG_CLI);
        }
        else {
            CD_CATEG_CLI = 0;
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:45:39)
     * @param newCD_CLI long
     */
    public void setCD_CLI(long newCD_CLI) {
        CD_CLI = newCD_CLI;
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:45:39)
     * @param newCD_CLI long
     */
    public void setCD_CLI(String newCD_CLI) {

        if (newCD_CLI.length() != 0) {
            CD_CLI = Long.parseLong(newCD_CLI);
        }
        else {
            CD_CLI = 0;
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:52:07)
     * @param newCD_ORIG int
     */
    public void setCD_ORIG(int newCD_ORIG) {
        CD_ORIG = newCD_ORIG;
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:52:07)
     * @param newCD_ORIG int
     */
    public void setCD_ORIG(String newCD_ORIG) {

        if ((newCD_ORIG != null) && (newCD_ORIG.length() != 0)) {
            CD_ORIG = Integer.parseInt(newCD_ORIG);
        }
        else {
            CD_ORIG = 0;
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:48:09)
     * @param newCD_POSTAL java.lang.String
     */
    public void setCD_POSTAL(String newCD_POSTAL) {
        CD_POSTAL = newCD_POSTAL;
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:51:57)
     * @param newCD_TR_AGE int
     */
    public void setCD_TR_AGE(int newCD_TR_AGE) {
        CD_TR_AGE = newCD_TR_AGE;
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:51:57)
     * @param newCD_TR_AGE int
     */
    public void setCD_TR_AGE(String newCD_TR_AGE) {

        if ((newCD_TR_AGE != null) && (newCD_TR_AGE.length() != 0)) {
            CD_TR_AGE = Integer.parseInt(newCD_TR_AGE);
        }
        else {
            CD_TR_AGE = 0;
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:51:23)
     * @param newCD_TYP_CHEV int
     */
    public void setCD_TYP_CHEV(int newCD_TYP_CHEV) {
        CD_TYP_CHEV = newCD_TYP_CHEV;
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:51:23)
     * @param newCD_TYP_PEAU int
     */
    public void setCD_TYP_PEAU(int newCD_TYP_PEAU) {
        CD_TYP_PEAU = newCD_TYP_PEAU;
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:51:23)
     * @param newCD_TYP_CHEV int
     */
    public void setCD_TYP_CHEV(String newCD_TYP_CHEV) {

        if ((newCD_TYP_CHEV != null) && (newCD_TYP_CHEV.length() != 0)) {
            CD_TYP_CHEV = Integer.parseInt(newCD_TYP_CHEV);
        }
        else {
            CD_TYP_CHEV = 0;
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:51:23)
     * @param newCD_TYP_PEAU int
     */
    public void setCD_TYP_PEAU(String newCD_TYP_PEAU) {

        if ((newCD_TYP_PEAU != null) && (newCD_TYP_PEAU.length() != 0)) {
            CD_TYP_PEAU = Integer.parseInt(newCD_TYP_PEAU);
        }
        else {
            CD_TYP_PEAU = 0;
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:46:26)
     * @param newCIVILITE java.lang.String
     */
    public void setCIVILITE(String newCIVILITE) {
        CIVILITE = newCIVILITE;
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:52:23)
     * @param newCOMM java.lang.String
     */
    public void setCOMM(String newCOMM) {
        COMM = newCOMM;
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:51:57)
     * @param dbConnect DBSession 
     */
    public void setDefaultCD_TR_AGE(DBSession dbConnect) {
        if (DT_ANNIV != null) {
            TrAgeBean aTrAge = TrAgeBean.getTrAgeBean(dbConnect, DT_ANNIV);

            if (aTrAge != null) {
                CD_TR_AGE = aTrAge.getCD_TR_AGE();
            }
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:49:19)
     * @param newDT_ANNIV java.util.Calendar
     * @throws Exception En cas d'erreur de conversion
     */
    public void setDT_ANNIV(String newDT_ANNIV) throws Exception {
        if (newDT_ANNIV.length() != 0) {
            DT_ANNIV = Calendar.getInstance();

            DateFormat formatDate =
                DateFormat.getDateInstance(DateFormat.SHORT);
            try {
                DT_ANNIV.setTime(formatDate.parse(newDT_ANNIV));
            }
            catch (Exception e) {
                System.out.println("Erreur de conversion : " + e.toString());
                DT_ANNIV = null;
                throw (
                    new Exception("Erreur de conversion de la date d'anniversaire"));
            }
        }
        else {
            DT_ANNIV = null;
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:49:19)
     * @param newDT_ANNIV java.util.Calendar
     */
    public void setDT_ANNIV(Calendar newDT_ANNIV) {
        DT_ANNIV = newDT_ANNIV;
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:48:46)
     * @param newEMAIL java.lang.String
     */
    public void setEMAIL(String newEMAIL) {
        EMAIL = newEMAIL;
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:46:51)
     * @param newNOM java.lang.String
     */
    public void setNOM(String newNOM) {
        NOM = newNOM;
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:48:37)
     * @param newPORTABLE java.lang.String
     */
    public void setPORTABLE(String newPORTABLE) {
        PORTABLE = newPORTABLE;
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:47:09)
     * @param newPRENOM java.lang.String
     */
    public void setPRENOM(String newPRENOM) {
        PRENOM = newPRENOM;
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:47:24)
     * @param newRUE java.lang.String
     */
    public void setRUE(String newRUE) {
        RUE = newRUE;
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:48:21)
     * @param newTEL java.lang.String
     */
    public void setTEL(String newTEL) {
        TEL = newTEL;
    }
    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:47:47)
     * @param newVILLE java.lang.String
     */
    public void setVILLE(String newVILLE) {
        VILLE = newVILLE;
    }
    /**
     * @return Indicateur si le client est encore valide (O/N)
     */
    public String getINDIC_VALID() {
        return INDIC_VALID;
    }

    /**
     * @param string Indicateur si le client est encore valide (O/N)
     */
    public void setINDIC_VALID(String string) {
        INDIC_VALID = string;
    }

    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public String toString() {
        return getCIVILITE() + " " + getNOM() + " " + getPRENOM();
    }

    /**
     * Formate le client pour une liste
     * @return Chaine correspondante
     */
    public String toStringListe() {
        String res = getNOM() + " " + getPRENOM();
        if (getCIVILITE().length() > 0) {
            res = res + " (" + getCIVILITE() + ")";
        }
        return res;
    }
    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object o) {
        ClientBean client2 = (ClientBean) o;
        return (getNOM() + " " + getPRENOM()).compareTo(client2.getNOM() + " " + client2.getPRENOM());
    }

    /**
     * Consomme une unité d'abonnement
     * @param CD_PREST Prestation concernée
     * @param qte Quantité à consommer (< 0 pour alimenter)
     * @param undo Annulation de la consommation ?
     * @return true si la consommation a réussi
     */
    public boolean consommeAbonnement(long CD_PREST, int qte, boolean undo) {
        
        // Recherche de l'abonnement
        Long cle = new Long(CD_PREST);
        Integer compteur = (Integer) abonnements.get(cle);
        
        // Décrémente
        boolean res;
        
        if (undo) {
            qte = -qte;
        }
        
        if (qte > 0) {
            // Consommation réelle
            res = (compteur != null) && ((compteur.intValue() - qte) >= 0);
            if (res) {
                // Décremente
                compteur = new Integer(compteur.intValue() - qte);
            }
        }
        else {
            // Alimentation : Toujours ok
            res = true;
            if (compteur == null) {
                compteur = new Integer(0);
            }
            compteur = new Integer(compteur.intValue() - qte);
        }
        abonnements.put(cle, compteur);
        
        return res;
    }
    
    /**
     * 
     * @return Abonnements du client
     */
    public HashMap getAbonnements() {
        return abonnements;
    }

}
