package com.increg.util;

import java.io.File;

/**
 * Utilitaires pour g�rer l'aspect multiplate forme
 * @author Manu
 *
 */
public class PlatformUtil {

	public static boolean IsWindows() {
		String os = System.getProperty("os.name").toLowerCase();
	    return (os.indexOf( "win" ) >= 0); 
	}

	/**
	 * Donne la ligne de commande � passer pour supprimer une base de donn�es
	 * @param dbName
	 * @return ligne de commande
	 */
	public static String CmdDropDB(String dbName) {
		String cmd;
		if (IsWindows()) {
			cmd = System.getenv("PG_HOME") + "\\bin\\dropdb.exe " + dbName;
		}
		else {
			cmd = System.getenv("PG_HOME") + "/bin/dropdb " + dbName;
		}
		return cmd;
	}

	/**
	 * Donne la ligne de commande � passer pour restaurer une base de donn�es
	 * @param dbName
	 * @param fichierUnzip
	 * @return ligne de commande
	 */
	public static String CmdRestaure(String dbName, File fichierUnzip) {
		String cmd;
		if (IsWindows()) {
			cmd = System.getenv("PG_HOME") + "\\bin\\pg_restore.exe -v -F c -d " + dbName + " \"" + fichierUnzip.getAbsolutePath() + "\"";
		}
		else {
			cmd = System.getenv("PG_HOME") + "/bin/pg_restore -v -F c -d " + dbName + " \"" + fichierUnzip.getAbsolutePath() + "\"";
		}
		return cmd;
	}

	/**
	 * Donne la ligne de commande � passer pour sauvegarder une base de donn�es
	 * @param dbName
	 * @param fichierUnzip
	 * @return ligne de commande
	 */
	public static String CmdSauvegarde(String nomFichierTmp, String baseName) {
		String cmd;
		if (IsWindows()) {
			cmd = System.getenv("PG_HOME") + "\\bin\\pg_dump.exe -d -c -F c -Z 9 -f " + nomFichierTmp + " " + baseName;
		}
		else {
			cmd = System.getenv("PG_HOME") + "/bin/pg_dump -d -c -F c -Z 9 -f " + nomFichierTmp + " " + baseName;
		}
		return cmd;
	}

	/**
	 * Donne la ligne de commande � passer pour arr�ter le moteur de base de donn�es
	 * @return ligne de commande
	 */
	public static String CmdStopBase() {
		String cmd;
		if (IsWindows()) {
	        cmd = System.getenv("PG_HOME") + "\\bin\\pg_ctl.exe stop -m immediate";
		}
		else {
	        cmd = System.getenv("PG_HOME") + "/bin/pg_ctl stop -m immediate";
		}
		return cmd;
	}

	/**
	 * Donne la ligne de commande � passer pour supprimer le r�pertoire des bases de donn�es
	 * @return ligne de commande
	 */
	public static String CmdDelBase() {
		String cmd;
		if (IsWindows()) {
	    	cmd = "cmd /c \"rmdir /s/q " + System.getenv("PGDATA") + "\"";
		}
		else {
	    	cmd = "sh -c \"rm -rf " + System.getenv("PGDATA") + "\"";
		}
		return cmd;
	}
}
