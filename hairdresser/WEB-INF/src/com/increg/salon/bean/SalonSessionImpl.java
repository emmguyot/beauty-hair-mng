package com.increg.salon.bean;

import com.increg.commun.exception.UnauthorisedUserException;

/**
 * Objet Session de l'application
 * Creation date: (07/07/2001 20:00:40)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class SalonSessionImpl extends SalonSession {
    /**
     * theLicence Licence de l'utilisateur
     */
    protected LicenceBean theLicence;
    /**
     * SAVE_DIR R�pertoire local de sauvegarde des bases
     */
    protected String savePath;
    /**
     * SAVE_DIR R�pertoire sur disque amovible de sauvegarde des bases
     */
    protected String savePathMedia;

    /**
     * SalonSession constructor comment.
     * @param configName Nom du fichier de config � utiliser
     * @throws Exception en cas de probl�me de cr�ation.
     * Typiquement la licence n'est pas correcte
     */
    public SalonSessionImpl(String configName) throws Exception {
        super(configName);
        try {
            /**
             * Verification de la licence par rapport � la soci�t�
             */
            theLicence = new LicenceBean(mySociete.getRAIS_SOC(), configName);

            // R�cup�ration du chemin de sauvegarde
            java.util.ResourceBundle resconfig =
                java.util.ResourceBundle.getBundle(configName);
            //$NON-NLS-1$

            savePath = resconfig.getString("savepath"); //$NON-NLS-1$
            
            ParamBean valMedia = ParamBean.getParamBean(myDBSession, Integer.toString(ParamBean.CD_PATH_MEDIA));
            if (valMedia != null) {
                savePathMedia = valMedia.getVAL_PARAM();
            }
        }
        catch (Exception e) {
            System.out.println("Erreur dans constructeur SalonSessionImpl :");
            e.printStackTrace();
            myDBSession = null;
            mySociete = null;
            throw (new Exception("D�marrage impossible : " + e.toString()));
        }
    }

    /**
     * Insert the method's description here.
     * Creation date: (13/10/2001 21:50:10)
     * @return com.increg.salon.bean.LicenceBean
     */
    public LicenceBean getTheLicence() {
        return theLicence;
    }
    /**
     * Insert the method's description here.
     * Creation date: (13/10/2001 21:50:10)
     * @param newTheLicence com.increg.salon.bean.LicenceBean
     */
    public void setTheLicence(LicenceBean newTheLicence) {
        theLicence = newTheLicence;
    }
    /**
     * Gets the savePath.
     * @return Returns a String
     */
    public String getSavePath() {
        return savePath;
    }

    /**
     * Sets the savePath.
     * @param savePath The savePath to set
     */
    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    /**
     * @see com.increg.salon.bean.SalonSession#checkLicence(com.increg.salon.bean.SocieteBean)
     */
    public boolean checkLicence(SocieteBean aSoc) {
        
        if (theLicence != null) {
            try {
                new LicenceBean(aSoc.getRAIS_SOC(), config);
                // Pas d'esxception : Tout va bien
                return true;
            }
            catch (UnauthorisedUserException e) {
                // Ignore
            }
        }
        
        // Dans tous les autres cas : Ce n'est pas bon
        return false;
    }

    /**
     * @return Chemin de sauvegarde pour les m�dias amovibles
     */
    public String getSavePathMedia() {
        return savePathMedia;
    }
}