package com.increg.salon.bean;

/**
 * Bean générique de recherche
 * Creation date: (17/07/2001 08:21:33)
 * @author: Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public abstract class RechercheBean {
	private java.lang.String where;
	protected java.lang.String from;
	protected java.lang.String select;
	protected java.lang.String reqSQL;
/**
 * RechercheBean constructor comment.
 */
public RechercheBean() {
	super();
	reset();
}
/**
 * Insert the method's description here.
 * Creation date: (17/07/2001 08:32:28)
 * @param dbConnect java.sql.Connection
 */
public void afficheResultat(java.sql.Connection dbConnect) {

		
}
/**
 * Ajoute une colonne qui figurera dans le résultat.
 * Creation date: (17/07/2001 08:27:35)
 * @param table java.lang.String
 * @param colonne java.lang.String
 * @param lien boolean	: Le lien sera présent sur cette colonne ?
 * @param affichage boolean	: La colonne sera affichée ?
 * @param cle boolean	: La colonne est dans la clé ?
 */
public void ajoutColonneResultat(String table, String colonne, boolean lien, boolean affichage, boolean cle) {

	// Maj du select
	if (select.length() > 0) {
		select = select + ",";
	}
	select = select + table + "." + colonne;

		
}
/**
 * Ajoute un critère sur une colonne.
 * Creation date: (17/07/2001 08:25:54)
 * @param Table java.lang.String
 * @param Colonne java.lang.String
 * @param Valeur java.lang.String
 */
public void ajoutCritere(String table, String colonne, String valeur) {}
/**
 * Insert the method's description here.
 * Creation date: (17/07/2001 08:37:05)
 * @param table java.lang.String
 * @param colonne java.lang.String
 */
public void ajoutTri(String table, String colonne) {}
/**
 * Insert the method's description here.
 * Creation date: (17/07/2001 08:35:50)
 * @return java.lang.String
 */
protected java.lang.String getFrom() {
	return from;
}
/**
 * Insert the method's description here.
 * Creation date: (17/07/2001 13:39:50)
 * @return java.lang.String
 */
protected java.lang.String getReqSQL() {
	return reqSQL;
}
/**
 * Insert the method's description here.
 * Creation date: (17/07/2001 08:36:13)
 * @return java.lang.String
 */
protected java.lang.String getSelect() {
	return select;
}
/**
 * Insert the method's description here.
 * Creation date: (17/07/2001 08:35:09)
 * @return java.lang.String
 */
protected java.lang.String getWhere() {
	return where;
}
/**
 * Vide les critères et les colonnes résultats.
 * Creation date: (17/07/2001 08:28:28)
 */
public void reset() {
	from = "";
	where = "";
	select = "";
}
/**
 * Insert the method's description here.
 * Creation date: (17/07/2001 08:35:50)
 * @param newFrom java.lang.String
 */
protected void setFrom(java.lang.String newFrom) {
	from = newFrom;
}
/**
 * Insert the method's description here.
 * Creation date: (17/07/2001 13:39:50)
 * @param newReqSQL java.lang.String
 */
protected void setReqSQL(java.lang.String newReqSQL) {
	reqSQL = newReqSQL;
}
/**
 * Insert the method's description here.
 * Creation date: (17/07/2001 08:36:13)
 * @param newSelect java.lang.String
 */
protected void setSelect(java.lang.String newSelect) {
	select = newSelect;
}
/**
 * Insert the method's description here.
 * Creation date: (17/07/2001 08:35:09)
 * @param newWhere java.lang.String
 */
protected void setWhere(java.lang.String newWhere) {
	where = newWhere;
}
}
