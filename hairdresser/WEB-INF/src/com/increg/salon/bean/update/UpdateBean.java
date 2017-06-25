/*
 * Mise à jour de la base de données
 * Copyright (C) 2001-2017 Emmanuel Guyot <See emmguyot on SourceForge> 
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
package com.increg.salon.bean.update;

import java.sql.SQLException;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.increg.commun.DBSession;
import com.increg.commun.exception.ReloadNeededException;

/**
 * Bean gérant la mise à jour de la base lors du passage d'une version à une autre
 * @since 9 juin 02 12:19:12
 * @version 1.0
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class UpdateBean {

    /**
     * Version du logiciel
     */
    protected String version;
    
    /**
     * Messages localisés à utiliser
     */
    protected ResourceBundle messages;
    
   	protected Log log = LogFactory.getLog(this.getClass());

   	/**
     * Constructor for UpdateBean.
     * @param dbConnect Connection à la base
     * @param rb Messages localisés
     * @throws Exception En cas de problème bloquant
     */
    public UpdateBean(DBSession dbConnect, ResourceBundle rb) throws Exception {
        super();

        version = null;
        messages = rb;
        deduitVersion(dbConnect);

        System.out.println("Version base detectee : " + version);
        
        majVersion(dbConnect);
    }

    /**
     * Mise à jour éventuelle de l'appli
     */
	public static UpdateBean getDerniereVersion(DBSession myDBSession, ResourceBundle messagesBundle, boolean forceSequence) throws Exception {
		// A mettre à jour à chaque changement de version
		return new UpdateBeanV49(myDBSession, messagesBundle, forceSequence);
	}

	/**
     * Gets the version.
     * @return Returns a String
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the version.
     * @param aVersion The version to set
     */
    public void setVersion(String aVersion) {
        this.version = aVersion;
    }

    /**
     * Déduit la version à partir de l'état en base
     * @param dbConnect Connexion utilisable pour la base
     * @throws Exception Erreur accès base ou Version non déduite
     */
    protected void deduitVersion(DBSession dbConnect) throws Exception {
        // Pas trouvé
        throw new ReloadNeededException("Version actuelle non trouvee.");
    }
    
    /**
     * Mise à jour de la base
     * @param dbConnect Connexion utilisable pour la base
     * @throws Exception En cas de pb de mise à jour
     */
    protected void majVersion(DBSession dbConnect) throws Exception {
    }
    
    /**
     * Vérification que la base est correcte, cohérente
     * @param dbConnect Connexion utilisable pour la base
     * @return true si la base est correcte, false sinon
     * @throws ReloadNeededException En cas d'erreur avec un message explicite
     */
    public boolean checkDatabase(DBSession dbConnect) throws ReloadNeededException {
        return true;
    }

    /**
     * Vérification que la base est correcte, cohérente
     * @param dbConnect Connexion utilisable pour la base
     * @param lstTables Liste des tables à vérifier
     * @param lstSeq Liste des séquences à vérifier
     * @return true si la base est correcte, false sinon
     * @throws ReloadNeededException En cas d'erreur avec un message explicite
     */
    public boolean checkDatabase(DBSession dbConnect, String[] lstTables, String[] lstSeq) throws ReloadNeededException {
        return true;
    }

    /**
     * Optimization de la base de données
     * @param dbConnect Connection à utiliser
     */
    public void optimizeDatabase(DBSession dbConnect) {
        // Nettoyage de la base
        try {
            dbConnect.doExecuteSQL(new String[] {"vacuum"});
        }
        catch (SQLException e) {
            e.printStackTrace();
            // Ignore l'erreur
        }

        // Optimisation de la base
        try {
            dbConnect.doExecuteSQL(new String[] {"vacuum analyze"});
        }
        catch (SQLException e) {
            e.printStackTrace();
            // Ignore l'erreur
        }
    }
}
