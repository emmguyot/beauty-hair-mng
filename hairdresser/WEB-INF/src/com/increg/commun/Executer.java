package com.increg.commun;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author Manu
 *
 * Execute des process externes proprement en gérant les E/S
 */
public class Executer {


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
                // Attente
                return currentProc.waitFor();
            }
            catch (InterruptedException e) {
                System.out.println ("Interruption de : " + command);
                return -2;
            }
        }
        else {
            return cr;
        }
    }

    /**
     * Execute la ligne et retourne en laissant se terminer tranquillement le process
     * @return exit 0 si ok
     */
    public int runAsync() {
        
        Runtime aRuntime = Runtime.getRuntime();

        try {
            // Execute la commande
            currentProc = aRuntime.exec(command, env);
        }
        catch (IOException e) {
            System.out.println ("Erreur à l'exécution de : " + command);
            return -1;
        }
        
        // Pompes pour les messages
        Calendar aCal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH;mm");
        String chDate = format.format(aCal.getTime());
        errorPump = new StreamPumper(currentProc.getErrorStream(), chDate + " (E) ");
        outputPump = new StreamPumper(currentProc.getInputStream(), chDate + " (S) ");
        
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
                    System.out.println ("Timeout : Arret force");
                    currentProc.destroy();
                    cr = -3;
                }
            }
            catch (IllegalArgumentException e) {
                System.out.println ("Erreur " + e.toString());
                encore = false;
                cr = -4;
            }
            catch (IllegalMonitorStateException e) {
                System.out.println ("Erreur " + e.toString());
                encore = false;
                cr = -5;
            }
            catch (InterruptedException e) {
                System.out.println ("Erreur " + e.toString());
                encore = false;
                cr = -6;
            }
        }
        while (encore);
        
        return cr;
    }

    /**
     * Arrête le processus en cours
     */
    public void stop() {
        try {
            int cr = currentProc.exitValue();
            
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
