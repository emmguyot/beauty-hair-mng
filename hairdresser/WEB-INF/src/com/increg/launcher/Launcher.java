/*
 * Created on Aug 6, 2003
 *
 */
package com.increg.launcher;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.increg.commun.Executer;

/**
 * @author guyot_e
 *
 * Fenêtre de lancement de l'environnement et de l'application
 */
public class Launcher extends JFrame implements Runnable, WindowListener, ActionListener {

    /**
     * Bouton fermer la fenêtre et toute l'application
     */
    private JButton fermerButton;
     
    /**
     * Bouton lançant l'application 
     */ 
    private JButton startButton;

    /**
     * Bouton arrêtant l'application 
     */ 
    private JButton stopButton;

    /**
     * Configuration sous forme de properties
     */
    private ResourceBundle resconfig;
    
    /**
     * Process d'IE qui tourne
     */
    private Executer runIE;

    /**
     * Constructeur
     *
     */
    public Launcher() {
        super();
        // Création de la fenêtre
        setTitle("Centre de contrôle InCrEG");
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        resconfig = ResourceBundle.getBundle(getClass().getPackage().getName() + ".launcher");

        // Ajoute les éléments à la fenêtre
        addEveryComponents();
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        // Ajoute la gestion des événements
        addWindowListener(this);
        
        // Cette fenêtre n'est pas modale
        setResizable(false);
        setVisible(true);
    }
    
    /**
     * @see java.lang.Runnable#run()
     */
    public void run() {
        
    }

    /**
     * Execution du lanceur
     * @param args Arguments ==> Aucun d'attendu
     */
    public static void main(String[] args) {
        Launcher myLaunchApp = new Launcher();
        myLaunchApp.run();
    }

    /**
     * Ajoute tous les composants d'affichage
     *
     */
    public void addEveryComponents() {
        
        getContentPane().add(Box.createVerticalStrut(12));
        getContentPane().add(getIconBox());
        getContentPane().add(Box.createVerticalStrut(17));
        getContentPane().add(getActionBox());
        getContentPane().add(Box.createVerticalStrut(11));
    }

    /**
     * @return Box contenant les boutons d'actions
     */
    private Box getActionBox() {
        Box actionBox = Box.createHorizontalBox();
        fermerButton = new JButton("Fermer");
        
        actionBox.add(Box.createGlue());
        actionBox.add(fermerButton);
        actionBox.add(Box.createHorizontalStrut(11));

        // Ajoute la gestion des événements
        fermerButton.addActionListener(this);
        
        return actionBox;
    }

    /**
     * @return Box contenant les icônes de lancement et de fermeture
     */
    private Box getIconBox() {
        Box iconBox = Box.createHorizontalBox();
        startButton = new JButton(new ImageIcon(resconfig.getString("InCrEG") + "/salon/images/LaunchWeb.gif"));
        startButton.getInsets(new Insets(0, 0, 0, 0));
        stopButton = new JButton(new ImageIcon(resconfig.getString("InCrEG") + "/salon/images/StopWeb.gif"));
        stopButton.getInsets(new Insets(0, 0, 0, 0));
        
        iconBox.add(Box.createGlue());
        iconBox.add(startButton);
        iconBox.add(Box.createHorizontalStrut(11));
        iconBox.add(stopButton);
        iconBox.add(Box.createGlue());

        // Ajoute la gestion des événements
        startButton.addActionListener(this);
        stopButton.addActionListener(this);
        
        return iconBox;
    }

    /**
     * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
     */
    public void windowActivated(WindowEvent e) {
    }

    /**
     * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
     */
    public void windowClosed(WindowEvent e) {
    }

    /**
     * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
     */
    public void windowClosing(WindowEvent e) {
        // Fermeture de toute l'application
        doStop();
        // Fermeture de la fenêtre
        dispose();
    }

    /**
     * @see java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent)
     */
    public void windowDeactivated(WindowEvent e) {
    }

    /**
     * @see java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent)
     */
    public void windowDeiconified(WindowEvent e) {
    }

    /**
     * @see java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
     */
    public void windowIconified(WindowEvent e) {
    }

    /**
     * @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
     */
    public void windowOpened(WindowEvent e) {
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == fermerButton) {
            doStop();
            dispose();
        }
        else if (e.getSource() == startButton) {
            doStart();
        }
        else if (e.getSource() == stopButton) {
            doStop();
        }
        
    }

    /**
     * Arrête l'application
     */
    private synchronized void doStop() {
        if (runIE != null) {
            
            // Arrêtes IE
            runIE.stop();
            
            // TODO Arrêt de Tomcat
            Executer runTomcat = new Executer(resconfig.getString("stopTC"));
            runTomcat.runAndWait(30000, 50);

            // TODO Lancement de Postgre (avec éventuellement IPC-Daemon)
            Executer runPostgre = new Executer(resconfig.getString("stopPG"));
            runPostgre.runAndWait(30000, 50);
        
            runIE = null;
        }
    }

    /**
     * Démarre l'application
     */
    private synchronized void doStart() {
        if (runIE == null) {
            
            // Execute une mise à jour de la version si besoin
            doMaj();
            
            // TODO Lancement de Postgre (avec éventuellement IPC-Daemon)
            Executer runPostgre = new Executer(resconfig.getString("startPG"));
            runPostgre.runAndWait(30000, 50);
        
            // TODO Lancement de Tomcat
            Executer runTomcat = new Executer(resconfig.getString("startTC"));
            runTomcat.runAsync();
        
            // Tempo pour laisser tomcat se lancer
            try {
                Thread.sleep(5000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        
            // TODO Lancement de IE sur la bonne page
            runIE = new Executer(resconfig.getString("startIE"));
            runIE.runAsync();
        }
        else {
            JOptionPane.showMessageDialog(this, "L'application a déjà été lancée.", "Lancement", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Mise à jour de l'application
     */
    private void doMaj() {
        File flagMaj = new File(resconfig.getString("InCrEG") + "/maj_afaire"); 
        if (flagMaj.exists()) {
            // Mise à jour à faire
            File appDir = new File(resconfig.getString("InCrEG") + "/salon");
            appDir.l
            flagMaj.delete();        
        }
    }

    /**
     * Positionne les variables d'environnements
     *
     */
    private void setEnv() {
        Vector lstEnv = new Vector();
        
        lstEnv.add("InCrEG=" + resconfig.getString("InCrEG")); 
        lstEnv.add("PGDATA=" + resconfig.getString("InCrEG") + "/base"); 
        lstEnv.add("PGDATESTYLE=European,SQL"); 
        lstEnv.add("JAVA_HOME=" + resconfig.getString("InCrEG") + "/jdk");
        lstEnv.add("CLASSPATH=" + resconfig.getString("InCrEG") + "/jdk/lib;" 
                                + resconfig.getString("InCrEG") + "/jdk/lib/tools.jar;"
                                + resconfig.getString("InCrEG") + "/cygwin/usr/share/postgresql/java/postgresql.jar;"
                                + resconfig.getString("InCrEG") + "/perso;"
                                );
        lstEnv.add("PATH=" + resconfig.getString("InCrEG") + "/cygwin/bin;" 
                            + resconfig.getString("InCrEG") + "/perso;"
                            + resconfig.getString("InCrEG") + "/salon/WEB-INF/classes;"
                            );
        lstEnv.add("CATALINA_HOME=" + resconfig.getString("InCrEG") + "/Tomcat"); 
        lstEnv.add("CATALINA_OPTS=-Dsun.net.inetaddr.ttl=0 -Duser.language=fr");
        
        Executer.setEnv((String[]) lstEnv.toArray()); 
    }
}
