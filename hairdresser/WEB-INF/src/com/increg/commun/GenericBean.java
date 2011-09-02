/*
 * Bean générique définissant l'interface
 * Copyright (C) 2001-2009 Emmanuel Guyot <See emmguyot on SourceForge> 
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
package com.increg.commun;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.increg.commun.exception.FctlException;

/**
 * Bean générique définissant l'interface
 * Creation date: (21/08/2001 22:00:27)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public abstract class GenericBean {

	// Logger par défaut
	protected Log log = LogFactory.getLog(this.getClass());
	
	/**
	 * Bundle contenant les messages
	 */
	protected ResourceBundle message = null;
	
	/**
	 * Insert the method's description here.
	 * Creation date: (18/07/2001 22:56:22)
	 */
	public GenericBean() {
		super();
	}

	/**
	 * Insert the method's description here.
	 * Creation date: (18/07/2001 22:56:22)
	 * @param rb Messages à utiliser en fonction de la localisation
	 */
	public GenericBean(ResourceBundle rb) {
		super();
		message = rb;
	}
	/**
	 * Constructeur à partir d'un élément en base
	 * Creation date: (18/07/2001 22:56:22)
	 * @param rs Résultat de la requete
	 */
	public GenericBean(ResultSet rs) {
		super();
	}
	/**
	 * Création du bean en base
	 * Creation date: (23/07/2001 15:32:03)
	 * @param dbConnect Connexion base à utiliser
	 * @exception SQLException The exception description.
	 * @exception FctlException En cas d'erreur non technique
	 */
	public abstract void create(DBSession dbConnect) throws SQLException, FctlException;
	/**
	 * Suppression du bean de la base
	 * Creation date: (23/07/2001 15:32:03)
	 * @param dbConnect Connexion base à utiliser
	 * @exception SQLException The exception description.
	 * @exception FctlException En cas d'erreur non technique
	 */
	public abstract void delete(DBSession dbConnect) throws SQLException, FctlException;
	/**
	 * Sauvegarde du bean en base
	 * Creation date: (23/07/2001 15:32:03)
	 * @param dbConnect Connexion base à utiliser
	 * @exception SQLException The exception description.
	 * @exception FctlException En cas d'erreur non technique
	 */
	public abstract void maj(DBSession dbConnect) throws SQLException, FctlException;
	/**
	 * Affichage sous forme texte du bean
	 * Creation date: (18/08/2001 17:20:48)
	 * @return Chaine formatée
	 */
	public abstract String toString();
	/**
	 * Purge des données
	 * @param dbConnect Connexion à la base à utiliser
	 * @param dateLimite Date limite de purge : Seront purgés les objets avant cette date
	 * @exception FctlException En cas d'erreur durant la mise à jour
	 * @return Nombre d'enregistrements purgés : -1 En cas d'erreur
	 */
	public static int purge(DBSession dbConnect, Date dateLimite) throws FctlException {
	    throw new FctlException("Fonction non implémentée");
	}
}
