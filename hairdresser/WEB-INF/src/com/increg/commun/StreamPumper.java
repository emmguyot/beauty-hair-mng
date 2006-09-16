package com.increg.commun;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Manu
 *
 * Classe qui vide un stream dans la console : Utile pour les batchs
 */
public class StreamPumper extends Thread {

    /**
     * Stream à vider
     */
    protected InputStream is;
    /**
     * Texte préfixant les sorties
     */
    protected String type;

    /**
     * Compteur de ligne
     */
    protected int nbLignes = 0;

    /**
     * Constructeur
     * @param is Stream à vider
     * @param type Type (texte préfixant les sorties)
     */
    public StreamPumper(InputStream is, String type) {
        this.is = is;
        this.type = type;
    }

    /**
     * Corps du thread
     */
    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println(type + ">" + line);
                nbLignes ++;
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
