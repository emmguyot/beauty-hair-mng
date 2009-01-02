/*
 * Lecteur d'un flux avec trace
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

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Manu
 *
 * Classe qui vide un stream dans la console : Utile pour les batchs
 */
public class StreamPumper extends Thread {

	protected Log log = LogFactory.getLog(this.getClass());
	
    /**
     * Stream à vider
     */
    protected InputStream is;
    /**
     * Texte préfixant les sorties
     */
    protected String type;
    /**
     * Fichier de sortie
     */
    protected String fichier;

    /**
     * Compteur de ligne
     */
    protected int nbLignes = 0;

    /**
     * Constructeur
     * @param is Stream à vider
     * @param type Type (texte préfixant les sorties)
     */
    public StreamPumper(InputStream is, String type, String fichierOut) {
        this.is = is;
        this.type = type;
        this.fichier = fichierOut;
    }

    /**
     * Corps du thread
     */
    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            
            FileWriter fw = null;
            if (fichier != null) {
            	fw = new FileWriter(fichier);
            }
            
            String line = null;
            while ((line = br.readLine()) != null) {
                log.debug(type + ">" + line);
                if (fw != null) {
                	fw.write(type + ">" + line + "\n");
                }
                nbLignes ++;
            }
            
            if (fw != null) {
            	fw.close();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

	/**
	 * @return Returns the nbLignes.
	 */
	public int getNbLignes() {
		return nbLignes;
	}
}
