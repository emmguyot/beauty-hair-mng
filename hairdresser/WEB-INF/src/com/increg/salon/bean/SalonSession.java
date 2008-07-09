/*
 * Bean Session incluant les données d'une session LibertyLook
 * Copyright (C) 2003-2008 Emmanuel Guyot <See emmguyot on SourceForge>
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
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;

import com.increg.commun.BasicSession;
import com.increg.commun.DBSession;
import com.increg.commun.exception.ReloadNeededException;
import com.increg.salon.bean.update.UpdateBean;
import com.increg.salon.bean.update.UpdateBeanV41;
import com.increg.util.SimpleDateFormatEG;

/**
 * @author Manu
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class SalonSession extends BasicSession {

    /**
     * myDBSession Connection à la base de données
     */
    protected DBSession myDBSession;

    /**
     * myIdent Identification de l'utilisateur
     */
    protected IdentBean myIdent = null;

    /**
     * mySociete Information sur le salon de coiffure
     */
    protected SocieteBean mySociete;

    /**
     * listeFact Liste des factures en cours
     */
    protected java.util.Vector listeFact;

    /**
     * msgTicket Message à afficher sur le ticket
     */
    protected String msgTicket;

    /**
     * msgTicket Message à afficher sur le ticket sur les taxes
     */
    protected String msgTicketTaxe;

    /**
     * Indicateur si les connexions externes sont autorisées
     */
    protected boolean remoteEnable;

    /**
     * Affichage du prix en base de facture ?
     */
    protected boolean affichePrix;

    /**
     * Bean de mise à jour de la base
     */
    protected UpdateBean majBase;

    /**
     * Devise principale
     */
    protected DeviseBean devise;

    /**
     * Autres devises
     */
    protected Vector lstAutresDevises;

    /**
     * Largeur des fiches
     */
    protected String largeurFiche;

    /**
     * Nom du fichier de config
     */
    protected String config;

    /**
     * Indicateur si la connection est automatique
     */
    protected boolean autoConnect;
    
    /**
     * Indicateur si la base est optimisée
     */
    protected boolean optimizeDB;

    /**
     * Indicateur si le serveur est sécurisé par Apache en accès distant
     */
	protected boolean secureApache;

	
    /**
     * SalonSession constructor comment.
     * Constructeur utilisé en cas de perte de session
     * Le constructeur doit exister
     */
    public SalonSession() {
        msgTicket = "Mauvaise licence";
        msgTicketTaxe = "";
    }
    
    /**
     * SalonSession constructor comment.
     * @param configName Nom du fichier de config à utiliser
     * @throws Exception en cas de problème de création.
     *      Typiquement la licence n'est pas correcte
     */
    public SalonSession(String configName) throws Exception {
        super();
        ResourceBundle resconfig = null;
        try {
            // Vérification du fichier de config
            config = configName;
            resconfig = ResourceBundle.getBundle(configName);
            if (resconfig == null) {
                throw new MissingResourceException(BasicSession.TAG_I18N + "salonSession.noBundle" + BasicSession.TAG_I18N, "config.properties", "");
            }
        } catch (MissingResourceException e) {
            // Fichier de configuration invalide ou introuvable
            throw e;
        }
        myDBSession = new DBSession(configName);
        listeFact = new Vector();
        try {
            try {
                /**
                 * Mise à jour éventuelle de l'appli
                 * <b>A mettre à jour à chaque changement de version</b>
                 */
                majBase = new UpdateBeanV41(myDBSession, messagesBundle);
            } catch (Exception e) {
                System.out.println("Mise à jour de la base en erreur :");
                e.printStackTrace();
                // Il faut proposer la restauration de la base
                myDBSession = null;
                mySociete = null;
                throw new ReloadNeededException(BasicSession.TAG_I18N + "salonSession.majBaseKo" + BasicSession.TAG_I18N + e.toString()); 
            }

            optimizeDB = false;
            try {
                if ((resconfig.getString("optimize") != null) && (resconfig.getString("optimize").equals("1"))) {
                	optimizeDB = true;
                }
            } catch (Exception ignored) {
                // ignore the exception
            	optimizeDB = false;
            }

            if (optimizeDB) {
            	majBase.optimizeDatabase(myDBSession);
            }
            
            // Vérification de la base
            if (!majBase.checkDatabase(myDBSession)) {
                System.out.println("La base de données n'est pas cohérente");
                throw new ReloadNeededException(BasicSession.TAG_I18N + "salonSession.baseCorrupt" + BasicSession.TAG_I18N);
            }
                        
            // Initialisation de la société
            mySociete = new SocieteBean(myDBSession, configName);

            // Chargement de la liste des factures en attente de paiement
            String reqSQL =
                "select * from FACT where (CD_PAIEMENT=0 or CD_PAIEMENT is null) and FACT_HISTO='N' order by CD_FACT";

            ResultSet aRS = myDBSession.doRequest(reqSQL);

            while (aRS.next()) {
                try {
                    FactBean aFact = new FactBean(aRS, messagesBundle);
                    listeFact.add(aFact);
                } catch (Exception e) {
                    System.out.println("Erreur à la lecture d'une facture pour l'en cours :");
                    e.printStackTrace();
                }
            }
            aRS.close();

            remoteEnable = false;
            try {
                if ((resconfig.getString("remote") != null) && (resconfig.getString("remote").equals("1"))) {
                    remoteEnable = true;
                }
            } catch (Exception ignored) {
                // ignore the exception
                remoteEnable = false;
            }

            secureApache = false;
            try {
                if ((resconfig.getString("secureApache") != null) && (resconfig.getString("secureApache").equals("1"))) {
                	secureApache = true;
                }
            } catch (Exception ignored) {
                // ignore the exception
            	secureApache = false;
            }

            // Affichage du prix sur les factures ?
            affichePrix = false;
            ParamBean affichPrixBean =
                ParamBean.getParamBean(myDBSession, Integer.toString(ParamBean.CD_AFF_PRIX_INV));
            if (affichPrixBean != null) {
                affichePrix = affichPrixBean.getVAL_PARAM().equals("O");
            }

            // Connnection automatique ?
            autoConnect = false;
            ParamBean autoConnBean =
                ParamBean.getParamBean(myDBSession, Integer.toString(ParamBean.CD_AUTOCONNECT));
            if (autoConnBean != null) {
                autoConnect = autoConnBean.getVAL_PARAM().equals("O");
            }
        } catch (ReloadNeededException e) {
            // Fait suivre l'exception
            throw (e);
        } catch (Exception e) {
            System.out.println("Erreur dans constructeur SalonSession :");
            e.printStackTrace();
            myDBSession = null;
            mySociete = null;
            throw (new Exception(BasicSession.TAG_I18N + "salonSession.startKo" + BasicSession.TAG_I18N + e.toString()));
        }
        
    }

    /**
     * Ajoute un client à la liste des encours en dupliquant une facture
     * donnée, même si celle-ci n'a pas de prestation (vente...)
     * @param CD_CLI l'identifiant du client
     * @param CD_FACT le numéro de la facture à dupliquer
     */
    public void addClientAvecFacture(String CD_CLI, String CD_FACT) {
        boolean doublon = false;

        // Vérification en cas de doublon
        for (int i = 0; i < listeFact.size(); i++) {
            FactBean aFact = (FactBean) listeFact.get(i);

            if (aFact.getCD_CLI() == Long.parseLong(CD_CLI)) {
                doublon = true;
            }
        }

        if (!doublon) {
            // Recherche la facture donnée
            String reqSQL =
                "select FACT.CD_FACT from FACT, HISTO_PREST, PREST where FACT.CD_FACT=HISTO_PREST.CD_FACT "
                    + " and HISTO_PREST.CD_PREST=PREST.CD_PREST "
                    + " and FACT.CD_CLI="
                    + CD_CLI
                    + " and FACT.CD_FACT="
                    + CD_FACT;

            // Interroge la Base
            try {
                FactBean aFact = null;
                ResultSet aRS = myDBSession.doRequest(reqSQL);

                if (aRS.next()) {
                    // Prend le premier
                    FactBean oldFact = FactBean.getFactBean(myDBSession, Long.toString(aRS.getLong("CD_FACT")), messagesBundle);

                    // Duplique la facture
                    aFact = (FactBean) oldFact.clone(myDBSession);

                    //Verifie que le collaborateur est présent
                    //sinon, set un collab present sur la facture
                    setGoodCollab(aFact);

                    // Stocke la facture dans la liste
                    listeFact.addElement(aFact);
                } else {
                    // Pas de facture, pas possible ici !!!
                    //On recherche une facture donnée !
                    //On met une facture vide pour avancer...
                    addEmptyFact(CD_CLI);
                }

                aRS.close();

            } catch (Exception e) {
                System.out.println("Erreur dans l'ajout d'un client avec une facture precise : " + e.toString());
            }
        }

    }

    /**
     * Ajoute un client à la liste des clients en cours.
     * Duplique la dernière facture ayant une prestation, sinon 
     * crée une facture vide.
     * Creation date: (09/09/2001 21:17:45)
     * @param CD_CLI String
     */
    public void addClient(String CD_CLI) {

        boolean doublon = false;

        // Vérification en cas de doublon
        for (int i = 0; i < listeFact.size(); i++) {
            FactBean aFact = (FactBean) listeFact.get(i);

            if (aFact.getCD_CLI() == Long.parseLong(CD_CLI)) {
                doublon = true;
            }
        }

        if (!doublon) {
            // Recherche la dernière facture
            // Comportant une prestation
            String reqSQL =
                "select FACT.CD_FACT from FACT, HISTO_PREST, PREST, TYP_VENT where FACT.CD_FACT=HISTO_PREST.CD_FACT "
                    + "and HISTO_PREST.CD_PREST=PREST.CD_PREST and PREST.CD_CATEG_PREST is not null "
                    + "and PREST.CD_TYP_VENT = TYP_VENT.CD_TYP_VENT and TYP_VENT.MARQUE = 'N' "
                    + "and FACT.CD_CLI="
                    + CD_CLI
                    + " order by FACT.DT_PREST desc, FACT.DT_CREAT desc limit 1";

            // Interroge la Base
            try {
                FactBean aFact = null;
                ResultSet aRS = myDBSession.doRequest(reqSQL);

                if (aRS.next()) {
                    // Prend le premier
                    FactBean oldFact = FactBean.getFactBean(myDBSession, Long.toString(aRS.getLong("CD_FACT")), messagesBundle);

                    // Duplique la facture
                    aFact = (FactBean) oldFact.clone(myDBSession);

                    //Verifie que le collaborateur est présent
                    //sinon, set un collab present sur la facture
                    setGoodCollab(aFact);

                    // Stocke la facture dans la liste
                    listeFact.addElement(aFact);
                } else {
                    // Pas d'ancienne facture : Facture vide
                    addEmptyFact(CD_CLI);
                }

                aRS.close();

            } catch (Exception e) {
                System.out.println("Erreur dans constructeur sur clé : " + e.toString());
            }
        }
    }

    /**
     * Ajoute une facture vierge pour le client
     * Creation date: (10/09/2001 13:31:02)
     * @param CD_CLI java.lang.String
     */
    public void addEmptyFact(String CD_CLI) {

        boolean doublon = false;

        // Vérification en cas de doublon
        for (int i = 0; i < listeFact.size(); i++) {
            FactBean aFact = (FactBean) listeFact.get(i);

            if (aFact.getCD_CLI() == Long.parseLong(CD_CLI)) {
                doublon = true;
            }
        }

        if (!doublon) {
            // Création du Bean facture
            try {
                FactBean aFact = new FactBean(messagesBundle);
                aFact.setCD_CLI(CD_CLI);
                aFact.setFACT_HISTO("N");
                aFact.create(myDBSession);

                //Verifie que le collaborateur est présent
                //sinon, set un collab present sur la facture
                setGoodCollab(aFact);

                // Il faut l'ajouter
                listeFact.add(aFact);
            } catch (Exception e) {
                System.out.println("Erreur dans constructeur sur clé : " + e.toString());
            }
        }
    }

    /**
     * Verifie que le collaborateur de la facture est present
     * et sinon, met un collaborateur present. Si il n'y a pas de collaborateur
     * present, laisse la valeur actuelle dans la facture.
     * @param aFact la facture qui doit etre verifiee
     * @exception Exception jettee quand la maj de la facture ne peut pas se faire
     */
    public void setGoodCollab(FactBean aFact) throws Exception {

        //Recupere les collaborateurs presents
        List collabsPresent = PointageBean.getPresentCollabs(myDBSession);
        Iterator collabsIter = collabsPresent.iterator();
        int CD_COLLAB = aFact.getCD_COLLAB();
        int defaultCD_COLLAB = -1;

        while (collabsIter.hasNext()) {
            CollabBean aCollab = (CollabBean) collabsIter.next();
            defaultCD_COLLAB = aCollab.getCD_COLLAB();
            if (defaultCD_COLLAB == CD_COLLAB) {
                //Le collaborateur est present, on s'arrete
                break;
            }
            // Sinon On ne fait rien
        }

        if ((CD_COLLAB != defaultCD_COLLAB) && ((defaultCD_COLLAB != -1))) {
            //On peut mettre a jour le CD_COLLAB
            aFact.setCD_COLLAB(defaultCD_COLLAB);
            aFact.maj(myDBSession);
        }
        // Sinon Pas de mise a jour necessaire

    }

    /**
     * Ajotue une facture au encours en prenant garde aux doublons
     * Creation date: (10/09/2001 13:31:02)
     * @param CD_FACT java.lang.String
     */
    public void addFact(String CD_FACT) {

        boolean trouve = false;
        long CdFact = Long.parseLong(CD_FACT);

        for (int i = 0; i < listeFact.size(); i++) {
            FactBean aFact = (FactBean) listeFact.get(i);
            if (aFact.getCD_FACT() == CdFact) {
                trouve = true;
            }
        }

        if (!trouve) {
            // Il faut l'ajouter
            listeFact.add(FactBean.getFactBean(myDBSession, CD_FACT, messagesBundle));
        }
    }

    /**
     * Indique si une facture peut être créée
     * @return true si c'est le cas
     */
    public boolean peutCreerFacture() {
        return PointageBean.getPresentCollabs(myDBSession).size() != 0;
    }

    /**
     * Insert the method's description here.
     * Creation date: (15/09/2001 15:10:45)
     * @return java.lang.String
     * @param tab java.util.Object[]
     */
    public static String arrayToString(Object[] tab) {

        String list = new String();
        if (tab != null) {
            for (int i = 0; i < tab.length; i++) {
                list += tab[i] + ",";
            }
        }
        return list;

    }

    /**
     * Insert the method's description here.
     * Creation date: (03/10/2001 12:53:55)
     * @param date java.sql.Timestamp
     * @return String date convertie en chaîne
     */
    public static String dateToString(Calendar date) {

        if (date != null) {
            SimpleDateFormatEG formatDate = new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");
            formatDate.setTimeZone(date.getTimeZone());

            return formatDate.formatEG(date.getTime());
        } else {
            return "";
        }
    }

    /**
     * Insert the method's description here.
     * Creation date: (09/09/2001 21:20:15)
     * @return java.util.Vector
     */
    public java.util.Vector getListeFact() {
        return listeFact;
    }

    /**
     * Insert the method's description here.
     * Creation date: (20/08/2001 21:56:47)
     * @return String
     */
    public String getMsgTicket() {
        if (msgTicket == null) {
            ParamBean valMsg = ParamBean.getParamBean(myDBSession, Integer.toString(ParamBean.CD_MSG_TICKET));
            if (valMsg != null) {
                msgTicket = valMsg.getVAL_PARAM();
            }
        }
        return msgTicket;
    }

    /**
     * Insert the method's description here.
     * Creation date: (20/08/2001 21:56:47)
     * @return String
     */
    public String getMsgTicketTaxe() {
        if (msgTicketTaxe == null) {
            ParamBean valMsg = ParamBean.getParamBean(myDBSession, Integer.toString(ParamBean.CD_MSG_TAXE_TICKET));
            if (valMsg != null) {
                msgTicketTaxe = valMsg.getVAL_PARAM();
            }
        }
        return msgTicketTaxe;
    }

    /**
     * Insert the method's description here.
     * Creation date: (07/07/2001 20:02:11)
     * @return com.increg.salon.bean.DBSession
     */
    public DBSession getMyDBSession() {
        return myDBSession;
    }

    /**
     * Insert the method's description here.
     * Creation date: (08/07/2001 18:04:58)
     * @return com.increg.salon.bean.IdentBean
     */
    public IdentBean getMyIdent() {
        return myIdent;
    }

    /**
     * Insert the method's description here.
     * Creation date: (08/07/2001 19:21:29)
     * @return com.increg.salon.bean.SocieteBean
     */
    public SocieteBean getMySociete() {
        return mySociete;
    }

    /**
     * Fin de traitement d'un client: Suppression de la liste
     * Creation date: (09/09/2001 22:32:20)
     * @param CD_FACT java.lang.String
     */
    public void removeClient(String CD_FACT) {

        long CdFact = Long.parseLong(CD_FACT);
        boolean found = false;

        for (int i = 0; (i < listeFact.size()) && (!found); i++) {
            FactBean aFact = (FactBean) listeFact.get(i);

            if (aFact.getCD_FACT() == CdFact) {
                listeFact.remove(i);
            }
        }

    }

    /**
     * Insert the method's description here.
     * Creation date: (09/09/2001 21:20:15)
     * @param newListeFact java.util.Vector
     */
    public void setListeFact(java.util.Vector newListeFact) {
        listeFact = newListeFact;
    }

    /**
     * Insert the method's description here.
     * Creation date: (07/07/2001 20:02:11)
     * @param newMyDBSession com.increg.salon.bean.DBSession
     */
    protected void setMyDBSession(DBSession newMyDBSession) {
        myDBSession = newMyDBSession;
    }

    /**
     * Insert the method's description here.
     * Creation date: (08/07/2001 18:04:58)
     * @param newMyIdent com.increg.salon.bean.IdentBean
     */
    public void setMyIdent(IdentBean newMyIdent) {
        myIdent = newMyIdent;
    }

    /**
     * Insert the method's description here.
     * Creation date: (08/07/2001 19:21:29)
     * @param newMySociete com.increg.salon.bean.SocieteBean
     */
    protected void setMySociete(SocieteBean newMySociete) {
        mySociete = newMySociete;
    }

    /**
     * Gets the remoteEnable.
     * @return Returns a boolean
     */
    public boolean isRemoteEnable() {
        return remoteEnable;
    }

    /**
     * Sets the remoteEnable.
     * @param aRemoteEnable The remoteEnable to set
     */
    public void setRemoteEnable(boolean aRemoteEnable) {
        this.remoteEnable = aRemoteEnable;
    }

    /**
     * Returns the affichePrix.
     * @return boolean
     */
    public boolean isAffichePrix() {
        return affichePrix;
    }

    /**
     * Returns the majBase.
     * @return UpdateBean
     */
    public UpdateBean getMajBase() {
        return majBase;
    }

    /**
     * Returns the devise.
     * @return DeviseBean
     */
    public DeviseBean getDevise() {
        if (devise == null) {
            // Recharge à partir de la base
            devise = DeviseBean.getMainDeviseBean(myDBSession);
        }
        return devise;
    }

    /**
     * Returns the largeurFiche.
     * @return String
     */
    public String getLargeurFiche() {
        if (largeurFiche == null) {
            ParamBean valLargeur = ParamBean.getParamBean(myDBSession, Integer.toString(ParamBean.CD_LARGEUR));
            if (valLargeur != null) {
                largeurFiche = valLargeur.getVAL_PARAM() + "cm";
            }
        }
        return largeurFiche;
    }

    /**
     * Returns the lstAutresDevises.
     * @return Vector
     */
    public Vector getLstAutresDevises() {
        if (lstAutresDevises == null) {
            // Chargement depuis la base
            lstAutresDevises = DeviseBean.getOtherDeviseBean(myDBSession);
        }
        return lstAutresDevises;
    }

    /**
     * Reset des devises suite à un modification
     */
    public void resetDevise() {
        lstAutresDevises = null;
        devise = null;
    }

    /**
     * Vérifie si la licence est correcte
     * @param aSoc Société
     * @return true si la licence est correcte
     */
    public abstract boolean checkLicence(SocieteBean aSoc);
 
    /**
     * Indique si l'auto connection au logiciel est demandée
     * @return  true si l'auto connection est demandée
     */   
    public boolean isAutoConnect() {
        return autoConnect;
    }
    
    /**
     * Positionne le flag de connexion automatique
     * @param newAutoConnect Nouvelle valeur
     */
    public void setAutoConnect(boolean newAutoConnect) {
        autoConnect = newAutoConnect;
    }

    /**
     * 
     * @return The secureApache
     */
	public boolean isSecureApache() {
		return secureApache;
	}

	/**
	 * @param secureApache The secureApache to set.
	 */
	public void setSecureApache(boolean secureApache) {
		this.secureApache = secureApache;
	}
}
