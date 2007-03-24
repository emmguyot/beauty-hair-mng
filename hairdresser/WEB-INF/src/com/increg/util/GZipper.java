/*
 * Utilitaire pour gérer le GZip qui était autrefois géré dans cygwin
 * Copyright (C) 2001-2007 Emmanuel Guyot <See emmguyot on SourceForge> 
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GZipper {
	// Log 
	static protected Log log = LogFactory.getLog(GZipper.class);
	
	/**
	 * Gzip un fichier
	 * @param fichierIn
	 * @param fichierOut
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void gzipFile(String fichierIn, String fichierOut) throws FileNotFoundException, IOException {
		try {
			// Specify gzip file name
			GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(fichierOut));

			// Specify the input file to be compressed
			FileInputStream in = new FileInputStream(fichierIn);

			// Transfer bytes from the input file
			// to the gzip output stream
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();

			// Finish creation of gzip file
			out.finish();
			out.close();
		} catch (FileNotFoundException e) {
			// Fichier d'entrée inexistant
			log.error("Fichier d'entrée inexistant", e);
			throw e;
		} catch (IOException e) {
			// Problème à la génération
			log.error("Problème à la génération du fichier", e);
			throw e;
		}
	}

	/**
	 * dégzip un fichier
	 * @param fichierIn
	 * @param fichierOut
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void gunzipFile(String fichierIn, String fichierOut) throws FileNotFoundException, IOException {
		try {
			// Specify gzip file name
			GZIPInputStream in = new GZIPInputStream(new FileInputStream(fichierIn));

			// Specify the input file to be compressed
			FileOutputStream out = new FileOutputStream(fichierOut);

			// Transfer bytes from the input file
			// to the gzip output stream
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();

			out.close();
		} catch (FileNotFoundException e) {
			// Fichier d'entrée inexistant
			log.error("Fichier d'entrée inexistant", e);
			throw e;
		} catch (IOException e) {
			// Problème à la génération
			log.error("Problème à la génération du fichier", e);
			throw e;
		}
	}
}
