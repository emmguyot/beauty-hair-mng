package com.increg.commun;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.increg.commun.exception.FctlException;

/**
 * Bean générique définissant l'interface
 * Creation date: (21/08/2001 22:00:27)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public abstract class GenericBean {

/**
 * Insert the method's description here.
 * Creation date: (18/07/2001 22:56:22)
 */
public GenericBean() {
	super();
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
