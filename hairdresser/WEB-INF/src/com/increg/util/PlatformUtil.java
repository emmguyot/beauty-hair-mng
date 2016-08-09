/*
 * Utilitaires pour gérer l'aspect multiplate forme
 * Copyright (C) 2010 Emmanuel Guyot <See emmguyot on SourceForge> 
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
package com.increg.util;

import java.io.File;

public class PlatformUtil {

	public static boolean IsWindows() {
		String os = System.getProperty("os.name").toLowerCase();
	    return (os.indexOf( "win" ) >= 0); 
	}

	/**
	 * Donne la ligne de commande à passer pour supprimer une base de données
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
	 * Donne la ligne de commande à passer pour restaurer une base de données
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
	 * Donne la ligne de commande à passer pour sauvegarder une base de données
	 * @param dbName
	 * @param fichierUnzip
	 * @return ligne de commande
	 */
	public static String CmdSauvegarde(String nomFichierTmp, String baseName) {
		String cmd;
		if (IsWindows()) {
			cmd = System.getenv("PG_HOME") + "\\bin\\pg_dump.exe --inserts -c -F c -Z 9 -f \"" + nomFichierTmp + "\" " + baseName;
		}
		else {
			cmd = System.getenv("PG_HOME") + "/bin/pg_dump --inserts -c -F c -Z 9 -f \"" + nomFichierTmp + "\" " + baseName;
		}
		return cmd;
	}

	/**
	 * Donne la ligne de commande à passer pour arrêter le moteur de base de données
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
	 * Donne la ligne de commande à passer pour supprimer le répertoire des bases de données
	 * @return ligne de commande
	 */
	public static String CmdDelBase() {
		String cmd;
		if (IsWindows()) {
	    	cmd = "cmd /c \"rmdir /s/q " + System.getenv("PGDATA") + "\"";
		}
		else {
	    	cmd = "rm -rf " + System.getenv("PGDATA");
		}
		return cmd;
	}

	public static String CheminStdErr() {
		String chemin;
		if (IsWindows()) {
			chemin = System.getenv("INCREG_a") + "\\Temp\\exec.err";
		}
		else {
			chemin = System.getenv("InCrEG") + "/temp/exec.err";
		}
		return chemin;
	}

	public static String CheminStdStd() {
		String chemin;
		if (IsWindows()) {
			chemin = System.getenv("INCREG_a") + "\\Temp\\exec.std";
		}
		else {
			chemin = System.getenv("InCrEG") + "/temp/exec.std";
		}
		return chemin;
	}

	public static String CmdCreeBase(String dbName) {
		String cmd;
		if (IsWindows()) {
	    	cmd = "create database " + dbName + " with template=template0 encoding='LATIN1'";
		}
		else {
	    	cmd = "create database " + dbName + " with template=template0 encoding='UTF8'";
		}
		return cmd;
	}
}
