package com.increg.salon.bean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Vector;

import com.increg.commun.DBSession;
import com.increg.commun.TimeStampBean;
import com.increg.util.SimpleDateFormatEG;

/**
 * Bean historique de statistiques génériques
 * Creation date: Aug 2, 2003
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class StatHistoBean extends TimeStampBean {
    /**
     * Code de la statistique
     */
    protected long CD_STAT;
    /**
     * Numéro du graphe
     */
    protected int NUM_GRAPH;
    /**
     * Paramètre
     */
    protected String PARAM;
    /**
     * Valeur du paramètre
     */
    protected String VALUE;

    /**
     * ClientBean constructor comment.
     */
    public StatHistoBean() {
        super();
    }

    /**
     * ClientBean à partir d'un RecordSet.
     */
    public StatHistoBean(ResultSet rs) {
        super(rs);
        try {
            CD_STAT = rs.getLong("CD_STAT");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans StatHistoBean (RS) : " + e.toString());
            }
        }
        try {
            NUM_GRAPH = rs.getInt("NUM_GRAPH");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println("Erreur dans StatHistoBean (RS) : " + e.toString());
            }
        }
        try {
            PARAM = rs.getString("PARAM");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println(
                    "Erreur dans StatHistoBean (RS) : " + e.toString());
            }
        }
        try {
            VALUE = rs.getString("VALUE");
        }
        catch (SQLException e) {
            if (e.getErrorCode() != 1) {
                System.out.println(
                    "Erreur dans StatHistoBean (RS) : " + e.toString());
            }
        }
    }

    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public void create(DBSession dbConnect) throws java.sql.SQLException {

        SimpleDateFormatEG formatDate =
            new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");

        StringBuffer req = new StringBuffer("insert into STAT_HISTO ");
        StringBuffer colonne = new StringBuffer("(");
        StringBuffer valeur = new StringBuffer(" values ( ");

        colonne.append("CD_STAT,");
        valeur.append(CD_STAT);
        valeur.append(",");

        colonne.append("NUM_GRAPH,");
        valeur.append(NUM_GRAPH);
        valeur.append(",");

        if ((PARAM != null) && (PARAM.length() != 0)) {
            colonne.append("PARAM,");
            valeur.append(DBSession.quoteWith(PARAM, '\''));
            valeur.append(",");
        }

        if ((VALUE != null) && (VALUE.length() != 0)) {
            colonne.append("VALUE,");
            valeur.append(DBSession.quoteWith(VALUE, '\''));
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

        // Execute la création
        String[] reqs = new String[1];
        reqs[0] = req.toString();
        int[] nb = new int[1];
        nb = dbConnect.doExecuteSQL(reqs);

        if (nb[0] != 1) {
            throw (new SQLException("Création non effectuée"));
        }
    }

    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public void delete(DBSession dbConnect) throws java.sql.SQLException {

        StringBuffer req = new StringBuffer("delete from STAT_HISTO ");
        StringBuffer where =
            new StringBuffer(
                " where CD_STAT="
                    + CD_STAT
                    + " and NUM_GRAPH="
                    + NUM_GRAPH
                    + " and PARAM="
                    + DBSession.quoteWith(PARAM, '\''));

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
    public void deleteAllStat(DBSession dbConnect)
        throws java.sql.SQLException {

        StringBuffer req = new StringBuffer("delete from STAT_HISTO ");
        StringBuffer where = new StringBuffer(" where CD_STAT=" + CD_STAT);

        // Constitue la requete finale
        req.append(where);

        // Execute la création
        String[] reqs = new String[1];
        reqs[0] = req.toString();
        dbConnect.doExecuteSQL(reqs);
    }

    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public void maj(DBSession dbConnect) throws java.sql.SQLException {

        SimpleDateFormatEG formatDate =
            new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");

        StringBuffer req = new StringBuffer("update STAT_HISTO set ");
        StringBuffer colonne = new StringBuffer("");
        StringBuffer where =
            new StringBuffer(
                " where CD_STAT="
                    + CD_STAT
                    + " and NUM_GRAPH="
                    + NUM_GRAPH
                    + " and PARAM="
                    + DBSession.quoteWith(PARAM, '\''));

        colonne.append("VALUE=");
        if ((VALUE != null) && (VALUE.length() != 0)) {
            colonne.append(DBSession.quoteWith(VALUE, '\''));
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

        // Execute la création
        String[] reqs = new String[1];
        reqs[0] = req.toString();
        int[] nb = new int[1];
        nb = dbConnect.doExecuteSQL(reqs);

        if (nb[0] != 1) {
            throw (new SQLException("Mise à jour non effectuée"));
        }
    }

    /**
     * Création d'un Bean historique Statistique à partir de sa clé
     * Creation date: (18/08/2001 17:05:45)
     * @param dbConnect com.increg.salon.bean.DBSession
     * @param CD_STAT String
     * @param NUM_GRAPH String
     * @param PARAM String
     * @return Bean chargé
     */
    public static StatHistoBean getStatHistoBean(
        DBSession dbConnect,
        String CD_STAT,
        String NUM_GRAPH,
        String PARAM) {
        String reqSQL =
            "select * from STAT_HISTO where CD_STAT="
                + CD_STAT
                + " and NUM_GRAPH="
                + NUM_GRAPH
                + " and PARAM="
                + DBSession.quoteWith(PARAM, '\'');
        StatHistoBean res = null;

        // Interroge la Base
        try {
            ResultSet aRS = dbConnect.doRequest(reqSQL);

            while (aRS.next()) {
                res = new StatHistoBean(aRS);
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
     * Création d'un vecteur de Beans historique Statistique à partir de sa clé
     * Creation date: (18/08/2001 17:05:45)
     * @param dbConnect com.increg.salon.bean.DBSession
     * @param CD_STAT java.lang.String
     * @return Vecteur de beans chargés
     */
    public static Vector getStatHistoBean(
        DBSession dbConnect,
        String CD_STAT) {
        String reqSQL = "select * from STAT_HISTO where CD_STAT=" + CD_STAT;
        Vector res = new Vector();

        // Interroge la Base
        try {
            ResultSet aRS = dbConnect.doRequest(reqSQL);

            while (aRS.next()) {
                res.add(new StatHistoBean(aRS));
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
     * Creation date: (18/07/2001 22:45:39)
     * @return long
     */
    public long getCD_STAT() {
        return CD_STAT;
    }

    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:45:39)
     * @param newCD_STAT long
     */
    public void setCD_STAT(long newCD_STAT) {
        CD_STAT = newCD_STAT;
    }

    /**
     * Insert the method's description here.
     * Creation date: (18/07/2001 22:45:39)
     * @param newCD_STAT long
     */
    public void setCD_STAT(String newCD_STAT) {

        if (newCD_STAT.length() != 0) {
            CD_STAT = Long.parseLong(newCD_STAT);
        }
        else {
            CD_STAT = 0;
        }
    }

    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public java.lang.String toString() {
        return getPARAM();
    }

    /**
     * @return Numéro du graphe
     */
    public int getNUM_GRAPH() {
        return NUM_GRAPH;
    }

    /**
     * @return Nom du paramètre
     */
    public String getPARAM() {
        return PARAM;
    }

    /**
     * @return Valeur du paramètre
     */
    public String getVALUE() {
        return VALUE;
    }

    /**
     * @param i Numéro du graphe
     */
    public void setNUM_GRAPH(int i) {
        NUM_GRAPH = i;
    }

    /**
     * @param string Nom du Paramètre
     */
    public void setPARAM(String string) {
        PARAM = string;
    }

    /**
     * @param string Valeur du paramètre
     */
    public void setVALUE(String string) {
        VALUE = string;
    }

}