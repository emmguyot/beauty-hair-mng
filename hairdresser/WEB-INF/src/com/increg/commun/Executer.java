/*
 * Lanceur de programmes externes
 * Copyright (C) 2001-2010 Emmanuel Guyot <See emmguyot on SourceForge> 
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.increg.util.PlatformUtil;

/**
 * @author Manu
 *
 * Execute des process externes proprement en gérant les E/S
 */
public class Executer {

   	protected Log log = LogFactory.getLog(this.getClass());

	public static final String NOM_FICHIER_ERR = PlatformUtil.CheminStdErr();
	public static final String NOM_FICHIER_STD = PlatformUtil.CheminStdStd();

    /**
     * Commande à executer
     */
    protected String command;
    /**
     * Variables d'environnement à utiliser pour le programme
     */
    protected static String[] env;
    /**
     * Pompe des sorties d'erreur
     */
    protected StreamPumper errorPump;
    /**
     * Pompe des sorties standard
     */
    protected StreamPumper outputPump;
    /**
     * Processus actif
     */    
    protected Process currentProc;
    
    /**
     * Constructeur
     * @param ligneCommande Ligne de commande à lancer
     */
    public Executer(String ligneCommande) {
        command = ligneCommande;
    }
    
    /**
     * Execute la ligne et attend patiemment sa fin
     * @return exit status
     */
    public int runAndWait() {
        
        int cr = runAsync();
        if (cr == 0) {
			try {
				try {
					// Attente
					cr = currentProc.waitFor();
				} catch (InterruptedException e) {
					log.error("Interruption de : " + command, e);
					cr = -2;
				}
			} finally {
				errorPump.interrupt();
				outputPump.interrupt();

				if (cr == 0) {
					if (errorPump.getNbLignes() != 0) {
						cr = -55;
					}
				}
			}
		}
        return cr;
    }

    /**
	 * Execute la ligne et retourne en laissant se terminer tranquillement le
	 * process
	 * 
	 * @return exit 0 si ok
	 */
    public int runAsync() {
        
    	log.info("Lancement du processus : " + command);
    	
        Runtime aRuntime = Runtime.getRuntime();

        try {
            // Execute la commande
            currentProc = aRuntime.exec(command, env);
        }
        catch (IOException e) {
            log.error("Erreur à l'exécution de : " + command, e);
            return -1;
        }
        
        // Pompes pour les messages
        errorPump = new StreamPumper(currentProc.getErrorStream(), "(E) ", NOM_FICHIER_ERR);
        outputPump = new StreamPumper(currentProc.getInputStream(), "(S) ", NOM_FICHIER_STD);
        
        // Démarrage des pompes
        errorPump.start();
        outputPump.start();
        
        return 0;
    }

    /**
     * Execute la ligne et attend sa fin mais pas trop
     * @param timeout durée maximale d'attente (ms)
     * @param step Interval de temps entre chaque vérification (ms)
     * @return exit status (Négatif = Erreur dans la gestion du processus)
     */
    public int runAndWait(long timeout, long step) {

        int cr = -1;
        boolean encore = true;
        int nbPas = 0;
        int nbPasMax = (int) (timeout / step);
    
        runAsync();

        try {
	        do {
	            try {
	                // Attente pour laisser tourner le process
	                Thread.sleep(step);
	                cr = currentProc.exitValue();
	                // C'est fini
	                encore = false;
	            }
	            catch (IllegalThreadStateException e) {
	                // Le process n'est pas terminé
	                nbPas++;
	                // Max = 5 minutes
	                encore = (nbPas < nbPasMax);
	                if (!encore) {
	                    // Kill le process : Timeout
	                    log.error("Timeout : Arret force");
	                    currentProc.destroy();
	                    cr = -3;
	                }
	            }
	            catch (IllegalArgumentException e) {
	                log.error("Erreur ", e);
	                encore = false;
	                cr = -4;
	            }
	            catch (IllegalMonitorStateException e) {
	                log.error("Erreur ", e);
	                encore = false;
	                cr = -5;
	            }
	            catch (InterruptedException e) {
	                log.error("Erreur ", e);
	                encore = false;
	                cr = -6;
	            }
	        }
	        while (encore);
        }
        finally {
        	errorPump.interrupt();
        	outputPump.interrupt();
        }
        
        return cr;
    }

    /**
     * Arrête le processus en cours
     */
    public void stop() {
        try {
            currentProc.exitValue();
            
            // Le process est déjà fini
        }
        catch (IllegalThreadStateException e) {
            // Le process n'est pas terminé
            // Kill le process 
            currentProc.destroy();
        }        
    }

    /**
     * @return Variables d'environnement sous forme de VAR=VALUE
     */
    public static String[] getEnv() {
        return env;
    }

    /**
     * @param strings Variables d'environnement sous forme de VAR=VALUE
     */
    public static void setEnv(String[] strings) {
        env = strings;
    }

}
