/*
 * Objet session de l'application
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

import com.increg.commun.BasicSession;
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
     * SAVE_DIR Répertoire local de sauvegarde des bases
     */
    protected String savePath;
    /**
     * SAVE_DIR Répertoire sur disque amovible de sauvegarde des bases
     */
    protected String savePathMedia;

    /**
     * SalonSession constructor comment.
     * @param configName Nom du fichier de config à utiliser
     * @param forceSequence Forcage des séquences ?
     * @throws Exception en cas de problème de création.
     * Typiquement la licence n'est pas correcte
     */
    public SalonSessionImpl(String configName, boolean forceSequence) throws Exception {
        super(configName, forceSequence);
        try {
            /**
             * Verification de la licence par rapport à la société
             */
            theLicence = new LicenceBean(mySociete.getRAIS_SOC(), configName);

            // Récupération du chemin de sauvegarde
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
            throw (new Exception(BasicSession.TAG_I18N + "salonSession.startKo" + BasicSession.TAG_I18N + e.toString()));
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
     * @return Chemin de sauvegarde pour les médias amovibles
     */
    public String getSavePathMedia() {
        return savePathMedia;
    }
}