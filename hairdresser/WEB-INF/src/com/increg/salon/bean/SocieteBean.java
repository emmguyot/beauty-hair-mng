/*
 * Gestion de l'entité Société : Gérant, adresse, ... 
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
package com.increg.salon.bean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.ResourceBundle;

import com.increg.commun.BasicSession;
import com.increg.commun.DBSession;
import com.increg.commun.TimeStampBean;
import com.increg.util.SimpleDateFormatEG;


/**
 * Gestion de l'entité Société : Gérant, adresse, ...
 * Creation date: (08/07/2001 19:00:22)
 * 
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class SocieteBean extends TimeStampBean {
    /**
     * Raison Sociale / Nom du salon
     */
    protected String RAIS_SOC;

    /**
     * Civilité du gérant
     */
    protected String CIVILITE_GER;

    /**
     * Nom du gérant
     */
    protected String NOM_GER;

    /**
     * Prénom du gérant
     */
    protected String PRENOM_GER;

    /**
     * Rue du salon
     */
    protected String RUE;

    /**
     * Ville du salon
     */
    protected String VILLE;

    /**
     * Code postal du salon
     */
    protected String CD_POSTAL;

    /**
     * Téléphone du salon
     */
    protected String TEL;

    /**
     * Portable du gérant
     */
    protected String PORTABLE;

    /**
     * Email du gérant
     */
    protected String EMAIL;

    /**
     * Code Siret du salon
     */
    protected String CD_SIRET;

    /**
     * Code APE du salon
     */
    protected String CD_APE;

    /**
     * Flag de gestion de salon
     */
    protected String FLG_SALON;

    /**
     * Flag de gestion d'institut
     */
    protected String FLG_INSTITUT;

    /**
     * SocieteBean constructor comment.
     */
    public SocieteBean() {
        super();
        RAIS_SOC = "";
        CIVILITE_GER = "";
        NOM_GER = "";
        PRENOM_GER = "";
        RUE = "";
        VILLE = "";
        CD_POSTAL = "";
        TEL = "";
        PORTABLE = "";
        EMAIL = "";
        CD_SIRET = "";
        CD_APE = "";
        FLG_SALON = "O";
        FLG_INSTITUT = "O";
    }

    /**
     * SocieteBean constructor comment.
     * 
     * @param aDBSession
     *            Connexion à la base à utiliser
     * @param configName
     *            Fichier de configuration actif
     * @throws Exception
     *             En cas d'erreur SQL
     */
    public SocieteBean(DBSession aDBSession, String configName)
            throws Exception {
        super();
        // Initialise le composant à partir de la base
        try {
            boolean found = false;
            String req = "select * from SOC";
            ResultSet rs = aDBSession.doRequest(req);
            while (rs.next()) {
                RAIS_SOC = rs.getString("RAIS_SOC");
                CIVILITE_GER = rs.getString("CIVILITE_GER");
                DT_CREAT.setTime(rs.getTimestamp("DT_CREAT"));
                DT_MODIF.setTime(rs.getTimestamp("DT_MODIF"));
                NOM_GER = rs.getString("NOM_GER");
                PRENOM_GER = rs.getString("PRENOM_GER");
                RUE = rs.getString("RUE");
                VILLE = rs.getString("VILLE");
                CD_POSTAL = rs.getString("CD_POSTAL");
                TEL = rs.getString("TEL");
                PORTABLE = rs.getString("PORTABLE");
                EMAIL = rs.getString("EMAIL");
                CD_SIRET = rs.getString("CD_SIRET");
                CD_APE = rs.getString("CD_APE");
                FLG_INSTITUT = rs.getString("FLG_INSTITUT");
                FLG_SALON = rs.getString("FLG_SALON");
                found = true;
            }
            rs.close();

            if (!found) {
                // Création de la société à partir du fichier de config
                ResourceBundle resconfig = ResourceBundle.getBundle(configName);

                RAIS_SOC = resconfig.getString("RAIS_SOC");
                CIVILITE_GER = resconfig.getString("CIVILITE_GER");
                NOM_GER = resconfig.getString("NOM_GER");
                PRENOM_GER = resconfig.getString("PRENOM_GER");
                RUE = resconfig.getString("RUE");
                VILLE = resconfig.getString("VILLE");
                CD_POSTAL = resconfig.getString("CD_POSTAL");
                TEL = resconfig.getString("TEL");
                PORTABLE = resconfig.getString("PORTABLE");
                EMAIL = resconfig.getString("EMAIL");
                CD_SIRET = resconfig.getString("CD_SIRET");
                CD_APE = resconfig.getString("CD_APE");
                FLG_SALON = "O";
                FLG_INSTITUT = "O";

                DT_CREAT.setTime(Calendar.getInstance().getTime());
                DT_MODIF.setTime(Calendar.getInstance().getTime());

                create(aDBSession);
            }
        } catch (Exception e) {
            System.out.println("Erreur constructeur SocieteBean : "
                    + e.toString());
            throw (e);
        }
    }

    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public void create(DBSession dbConnect) throws java.sql.SQLException {

        SimpleDateFormatEG formatDate = new SimpleDateFormatEG(
                "dd/MM/yyyy HH:mm:ss");

        StringBuffer req = new StringBuffer("insert into SOC ");
        StringBuffer colonne = new StringBuffer("(");
        StringBuffer valeur = new StringBuffer(" values ( ");

        if ((RAIS_SOC != null) && (RAIS_SOC.length() != 0)) {
            colonne.append("RAIS_SOC,");
            valeur.append(DBSession.quoteWith(RAIS_SOC, '\''));
            valeur.append(",");
        }

        if ((CIVILITE_GER != null) && (CIVILITE_GER.length() != 0)) {
            colonne.append("CIVILITE_GER,");
            valeur.append(DBSession.quoteWith(CIVILITE_GER, '\''));
            valeur.append(",");
        }

        if ((NOM_GER != null) && (NOM_GER.length() != 0)) {
            colonne.append("NOM_GER,");
            valeur.append(DBSession.quoteWith(NOM_GER, '\''));
            valeur.append(",");
        }

        if ((PRENOM_GER != null) && (PRENOM_GER.length() != 0)) {
            colonne.append("PRENOM_GER,");
            valeur.append(DBSession.quoteWith(PRENOM_GER, '\''));
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

        if ((CD_SIRET != null) && (CD_SIRET.length() != 0)) {
            colonne.append("CD_SIRET,");
            valeur.append(DBSession.quoteWith(CD_SIRET, '\''));
            valeur.append(",");
        }

        if ((CD_APE != null) && (CD_APE.length() != 0)) {
            colonne.append("CD_APE,");
            valeur.append(DBSession.quoteWith(CD_APE, '\''));
            valeur.append(",");
        }

        if ((FLG_SALON != null) && (FLG_SALON.length() != 0)) {
            colonne.append("FLG_SALON,");
            valeur.append(DBSession.quoteWith(FLG_SALON, '\''));
            valeur.append(",");
        }

        if ((FLG_INSTITUT != null) && (FLG_INSTITUT.length() != 0)) {
            colonne.append("FLG_INSTITUT,");
            valeur.append(DBSession.quoteWith(FLG_INSTITUT, '\''));
            valeur.append(",");
        }

        if (DT_CREAT != null) {
            colonne.append("DT_CREAT,");
            valeur.append(DBSession.quoteWith(formatDate.formatEG(DT_CREAT
                    .getTime()), '\''));
            valeur.append(",");
        }

        if (DT_MODIF != null) {
            colonne.append("DT_MODIF,");
            valeur.append(DBSession.quoteWith(formatDate.formatEG(DT_MODIF
                    .getTime()), '\''));
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
            throw (new SQLException(BasicSession.TAG_I18N + "message.creationKo" + BasicSession.TAG_I18N));
        }

    }

    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public void delete(DBSession dbConnect) throws java.sql.SQLException {
    }

    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public void maj(DBSession dbConnect) throws java.sql.SQLException {

        com.increg.util.SimpleDateFormatEG formatDate = new SimpleDateFormatEG(
                "dd/MM/yyyy HH:mm:ss");

        StringBuffer req = new StringBuffer("update SOC set ");
        StringBuffer colonne = new StringBuffer("");

        colonne.append("CD_SIRET=");
        if ((CD_SIRET != null) && (CD_SIRET.length() != 0)) {
            colonne.append(DBSession.quoteWith(CD_SIRET, '\''));
        } else {
            colonne.append("''");
        }
        colonne.append(",");

        colonne.append("CD_APE=");
        if ((CD_APE != null) && (CD_APE.length() != 0)) {
            colonne.append(DBSession.quoteWith(CD_APE, '\''));
        } else {
            colonne.append("''");
        }
        colonne.append(",");

        colonne.append("CD_POSTAL=");
        if ((CD_POSTAL != null) && (CD_POSTAL.length() != 0)) {
            colonne.append(DBSession.quoteWith(CD_POSTAL, '\''));
        } else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("CIVILITE_GER=");
        if ((CIVILITE_GER != null) && (CIVILITE_GER.length() != 0)) {
            colonne.append(DBSession.quoteWith(CIVILITE_GER, '\''));
        } else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("EMAIL=");
        if ((EMAIL != null) && (EMAIL.length() != 0)) {
            colonne.append(DBSession.quoteWith(EMAIL, '\''));
        } else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("NOM_GER=");
        if ((NOM_GER != null) && (NOM_GER.length() != 0)) {
            colonne.append(DBSession.quoteWith(NOM_GER, '\''));
        } else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("PORTABLE=");
        if ((PORTABLE != null) && (PORTABLE.length() != 0)) {
            colonne.append(DBSession.quoteWith(PORTABLE, '\''));
        } else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("PRENOM_GER=");
        if ((PRENOM_GER != null) && (PRENOM_GER.length() != 0)) {
            colonne.append(DBSession.quoteWith(PRENOM_GER, '\''));
        } else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("RAIS_SOC=");
        if ((RAIS_SOC != null) && (RAIS_SOC.length() != 0)) {
            colonne.append(DBSession.quoteWith(RAIS_SOC, '\''));
        } else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("RUE=");
        if ((RUE != null) && (RUE.length() != 0)) {
            colonne.append(DBSession.quoteWith(RUE, '\''));
        } else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("VILLE=");
        if ((VILLE != null) && (VILLE.length() != 0)) {
            colonne.append(DBSession.quoteWith(VILLE, '\''));
        } else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("TEL=");
        if ((TEL != null) && (TEL.length() != 0)) {
            colonne.append(DBSession.quoteWith(TEL, '\''));
        } else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("FLG_SALON=");
        if ((FLG_SALON != null) && (FLG_SALON.length() != 0)) {
            colonne.append(DBSession.quoteWith(FLG_SALON, '\''));
        } else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("FLG_INSTITUT=");
        if ((FLG_INSTITUT != null) && (FLG_INSTITUT.length() != 0)) {
            colonne.append(DBSession.quoteWith(FLG_INSTITUT, '\''));
        } else {
            colonne.append("NULL");
        }
        colonne.append(",");

        colonne.append("DT_MODIF=");
        DT_MODIF = Calendar.getInstance();
        colonne.append(DBSession.quoteWith(formatDate.formatEG(DT_MODIF
                .getTime()), '\''));

        // Constitue la requete finale
        req.append(colonne);

        // Execute la création
        String[] reqs = new String[1];
        reqs[0] = req.toString();
        int[] nb = new int[1];
        nb = dbConnect.doExecuteSQL(reqs);

        if (nb[0] != 1) {
            throw (new SQLException(BasicSession.TAG_I18N + "message.enregistrementKo" + BasicSession.TAG_I18N));
        }

    }

    /**
     * Insert the method's description here. Creation date: (08/07/2001
     * 19:06:07)
     * 
     * @return java.lang.String
     */
    public java.lang.String getCD_APE() {
        return CD_APE;
    }

    /**
     * Insert the method's description here. Creation date: (08/07/2001
     * 19:06:07)
     * 
     * @return java.lang.String
     */
    public java.lang.String getCD_POSTAL() {
        return CD_POSTAL;
    }

    /**
     * Insert the method's description here. Creation date: (08/07/2001
     * 19:06:07)
     * 
     * @return java.lang.String
     */
    public java.lang.String getCD_SIRET() {
        return CD_SIRET;
    }

    /**
     * Insert the method's description here. Creation date: (08/07/2001
     * 19:06:07)
     * 
     * @return java.lang.String
     */
    public java.lang.String getCIVILITE_GER() {
        return CIVILITE_GER;
    }

    /**
     * Insert the method's description here. Creation date: (08/07/2001
     * 19:06:07)
     * 
     * @return java.lang.String
     */
    public java.lang.String getEMAIL() {
        return EMAIL;
    }

    /**
     * Insert the method's description here. Creation date: (08/07/2001
     * 19:06:07)
     * 
     * @return java.lang.String
     */
    public java.lang.String getNOM_GER() {
        return NOM_GER;
    }

    /**
     * Insert the method's description here. Creation date: (08/07/2001
     * 19:06:07)
     * 
     * @return java.lang.String
     */
    public java.lang.String getPORTABLE() {
        return PORTABLE;
    }

    /**
     * Insert the method's description here. Creation date: (08/07/2001
     * 19:06:07)
     * 
     * @return java.lang.String
     */
    public java.lang.String getPRENOM_GER() {
        return PRENOM_GER;
    }

    /**
     * Insert the method's description here. Creation date: (08/07/2001
     * 19:06:07)
     * 
     * @return java.lang.String
     */
    public java.lang.String getRAIS_SOC() {
        return RAIS_SOC;
    }

    /**
     * Insert the method's description here. Creation date: (08/07/2001
     * 19:06:07)
     * 
     * @return java.lang.String
     */
    public java.lang.String getRUE() {
        return RUE;
    }

    /**
     * Insert the method's description here. Creation date: (08/07/2001
     * 19:06:07)
     * 
     * @return java.lang.String
     */
    public java.lang.String getTEL() {
        return TEL;
    }

    /**
     * Insert the method's description here. Creation date: (08/07/2001
     * 19:06:07)
     * 
     * @return java.lang.String
     */
    public java.lang.String getVILLE() {
        return VILLE;
    }

    /**
     * Insert the method's description here. Creation date: (08/07/2001
     * 19:06:07)
     * 
     * @param newCD_APE
     *            java.lang.String
     */
    public void setCD_APE(java.lang.String newCD_APE) {
        CD_APE = newCD_APE;
    }

    /**
     * Insert the method's description here. Creation date: (08/07/2001
     * 19:06:07)
     * 
     * @param newCD_POSTAL
     *            java.lang.String
     */
    public void setCD_POSTAL(java.lang.String newCD_POSTAL) {
        CD_POSTAL = newCD_POSTAL;
    }

    /**
     * Insert the method's description here. Creation date: (08/07/2001
     * 19:06:07)
     * 
     * @param newCD_SIRET
     *            java.lang.String
     */
    public void setCD_SIRET(java.lang.String newCD_SIRET) {
        CD_SIRET = newCD_SIRET;
    }

    /**
     * Insert the method's description here. Creation date: (08/07/2001
     * 19:06:07)
     * 
     * @param newCIVILITE_GER
     *            java.lang.String
     */
    public void setCIVILITE_GER(java.lang.String newCIVILITE_GER) {
        CIVILITE_GER = newCIVILITE_GER;
    }

    /**
     * Insert the method's description here. Creation date: (08/07/2001
     * 19:06:07)
     * 
     * @param newEMAIL
     *            java.lang.String
     */
    public void setEMAIL(java.lang.String newEMAIL) {
        EMAIL = newEMAIL;
    }

    /**
     * Insert the method's description here. Creation date: (08/07/2001
     * 19:06:07)
     * 
     * @param newNOM_GER
     *            java.lang.String
     */
    public void setNOM_GER(java.lang.String newNOM_GER) {
        NOM_GER = newNOM_GER;
    }

    /**
     * Insert the method's description here. Creation date: (08/07/2001
     * 19:06:07)
     * 
     * @param newPORTABLE
     *            java.lang.String
     */
    public void setPORTABLE(java.lang.String newPORTABLE) {
        PORTABLE = newPORTABLE;
    }

    /**
     * Insert the method's description here. Creation date: (08/07/2001
     * 19:06:07)
     * 
     * @param newPRENOM_GER
     *            java.lang.String
     */
    public void setPRENOM_GER(java.lang.String newPRENOM_GER) {
        PRENOM_GER = newPRENOM_GER;
    }

    /**
     * Insert the method's description here. Creation date: (08/07/2001
     * 19:06:07)
     * 
     * @param newRAIS_SOC
     *            java.lang.String
     */
    public void setRAIS_SOC(java.lang.String newRAIS_SOC) {
        RAIS_SOC = newRAIS_SOC;
    }

    /**
     * Insert the method's description here. Creation date: (08/07/2001
     * 19:06:07)
     * 
     * @param newRUE
     *            java.lang.String
     */
    public void setRUE(java.lang.String newRUE) {
        RUE = newRUE;
    }

    /**
     * Insert the method's description here. Creation date: (08/07/2001
     * 19:06:07)
     * 
     * @param newTEL
     *            java.lang.String
     */
    public void setTEL(java.lang.String newTEL) {
        TEL = newTEL;
    }

    /**
     * Insert the method's description here. Creation date: (08/07/2001
     * 19:06:07)
     * 
     * @param newVILLE
     *            java.lang.String
     */
    public void setVILLE(java.lang.String newVILLE) {
        VILLE = newVILLE;
    }

    /**
     * @return Returns the fLG_INSTITUT.
     */
    public String getFLG_INSTITUT() {
        return FLG_INSTITUT;
    }
    /**
     * @param flg_institut The fLG_INSTITUT to set.
     */
    public void setFLG_INSTITUT(String flg_institut) {
        FLG_INSTITUT = flg_institut;
    }
    /**
     * @return Returns the fLG_SALON.
     */
    public String getFLG_SALON() {
        return FLG_SALON;
    }
    /**
     * @param flg_salon The fLG_SALON to set.
     */
    public void setFLG_SALON(String flg_salon) {
        FLG_SALON = flg_salon;
    }

    /**
     * Indique si la société est une salon de coiffure
     * @return true si c'est le cas
     */
    public boolean isSalon() {
        return ((FLG_SALON != null) && (FLG_SALON.charAt(0) == 'O'));
    }

    /**
     * Indique si la société est un institut de beauté
     * @return true si c'est le cas
     */
    public boolean isInstitut() {
        return ((FLG_INSTITUT != null) && (FLG_INSTITUT.charAt(0) == 'O'));
    }

    /**
     * @see com.increg.salon.bean.TimeStampBean
     */
    public java.lang.String toString() {
        return getRAIS_SOC();
    }
}