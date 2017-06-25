/*
 * Mise � jour de la base de donn�es
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
 * Bean g�rant la mise � jour de la base lors du passage d'une version � une autre
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
     * Messages localis�s � utiliser
     */
    protected ResourceBundle messages;
    
   	protected Log log = LogFactory.getLog(this.getClass());

   	/**
     * Constructor for UpdateBean.
     * @param dbConnect Connection � la base
     * @param rb Messages localis�s
     * @throws Exception En cas de probl�me bloquant
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
     * Mise � jour �ventuelle de l'appli
     */
	public static UpdateBean getDerniereVersion(DBSession myDBSession, ResourceBundle messagesBundle, boolean forceSequence) throws Exception {
		// A mettre � jour � chaque changement de version
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
     * D�duit la version � partir de l'�tat en base
     * @param dbConnect Connexion utilisable pour la base
     * @throws Exception Erreur acc�s base ou Version non d�duite
     */
    protected void deduitVersion(DBSession dbConnect) throws Exception {
        // Pas trouv�
        throw new ReloadNeededException("Version actuelle non trouvee.");
    }
    
    /**
     * Mise � jour de la base
     * @param dbConnect Connexion utilisable pour la base
     * @throws Exception En cas de pb de mise � jour
     */
    protected void majVersion(DBSession dbConnect) throws Exception {
    }
    
    /**
     * V�rification que la base est correcte, coh�rente
     * @param dbConnect Connexion utilisable pour la base
     * @return true si la base est correcte, false sinon
     * @throws ReloadNeededException En cas d'erreur avec un message explicite
     */
    public boolean checkDatabase(DBSession dbConnect) throws ReloadNeededException {
        return true;
    }

    /**
     * V�rification que la base est correcte, coh�rente
     * @param dbConnect Connexion utilisable pour la base
     * @param lstTables Liste des tables � v�rifier
     * @param lstSeq Liste des s�quences � v�rifier
     * @return true si la base est correcte, false sinon
     * @throws ReloadNeededException En cas d'erreur avec un message explicite
     */
    public boolean checkDatabase(DBSession dbConnect, String[] lstTables, String[] lstSeq) throws ReloadNeededException {
        return true;
    }

    /**
     * Optimization de la base de donn�es
     * @param dbConnect Connection � utiliser
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
