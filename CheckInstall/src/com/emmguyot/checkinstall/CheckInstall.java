/*
 * Application de vérification de la bonne installation
 * Copyright (C) 2014 Emmanuel Guyot <See emmguyot on SourceForge> 
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
package com.emmguyot.checkinstall;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

import javax.swing.*;

public class CheckInstall extends JPanel {

    private JTextArea taskOutput;
    protected JLabel textIntro;
    	
    public CheckInstall() {
        super(new BorderLayout());
 
        //Create the demo's UI.
        textIntro = new JLabel("Vérification en cours...");
 
        taskOutput = new JTextArea(5, 20);
        taskOutput.setMargin(new Insets(5,5,5,5));
        taskOutput.setEditable(false);
 
        JPanel panel = new JPanel();
        panel.add(textIntro);
 
        add(panel, BorderLayout.PAGE_START);
        add(new JScrollPane(taskOutput), BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
 
        new TaskCheckInstall().execute();
    }
	
    /**
     * Create the GUI and show it. As with all GUI code, this must run
     * on the event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Vérification de l'installation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Create and set up the content pane.
        JComponent newContentPane = new CheckInstall();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);
 
        //Display the window.
        frame.pack();
        frame.setBounds(10, 10, 600, 200);
        frame.setVisible(true);
    }
 
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
        
	}

	class TaskCheckInstall extends SwingWorker<String, String> {
		
		/**
		 * @see javax.swing.SwingWorker#process(java.util.List)
		 */
		@Override
		protected void process(List<String> chunks) {
			super.process(chunks);
			taskOutput.setText(chunks.get(chunks.size() - 1));
		}

		/**
		 * @see javax.swing.SwingWorker#done()
		 */
		@Override
		protected void done() {
			super.done();
			textIntro.setText("Vérification terminée");
		}

		protected boolean checkPort(int numPort) {
			// tentative de connexion au port
			try {
				Socket socket = new Socket("localhost", numPort);
				socket.close();
				return false;
			}
			catch (Exception x) {
				x.printStackTrace();
			}
			return true;
		}

		@Override
		protected String doInBackground() throws Exception {
			String res = "";
			if (!checkPort(80)) {
				res += "Le port 80 est actuellement utilisé par une autre application\n";
				publish(res);
				
				int[] tabAutrePort = new int[] { 81, 8080, 8181, 82, 8282 };
				boolean substitution = false;
				for (int i : tabAutrePort) {
					if (checkPort(i)) {
						res += "Le port " + Integer.toString(i) + " sera utilisé.\n";
						publish(res);
						
						substitue ("c:\\InCrEG\\Tomcat\\conf\\server.xml", 
									"<Connector port=\"80\" maxHttpHeaderSize=\"8192\" maxThreads=\"100\" minSpareThreads=\"4\" maxSpareThreads=\"25\" enableLookups=\"false\" redirectPort=\"8443\" acceptCount=\"100\" connectionTimeout=\"20000\" disableUploadTimeout=\"true\" />",
									"<Connector port=\"" + Integer.toString(i) + "\" maxHttpHeaderSize=\"8192\" maxThreads=\"100\" minSpareThreads=\"4\" maxSpareThreads=\"25\" enableLookups=\"false\" redirectPort=\"8443\" acceptCount=\"100\" connectionTimeout=\"20000\" disableUploadTimeout=\"true\" />"
								);	
						
						substitue ("c:\\InCrEG\\InCrEG LibertyLook.url", 
								"URL=http://localhost/salon/",
								"URL=http://localhost:" + Integer.toString(i) + "/salon/"
							);	
					
						substitue ("c:\\InCrEG\\InCrEG LibertyLook.url", 
								"URL=http://localhost/institut/",
								"URL=http://localhost:" + Integer.toString(i) + "/institut/"
							);	

						substitution = true;
						break;
					}
				}
				
				if (!substitution) {
					res += "Aucun port de substitution n'a été trouvé. Vous devrez désactiver des applications puis relancer l'installation.\n";
					publish(res);
				}
				
			}
			else {
				res += "Le port 80 est bien disponible pour LibertyLook.\n";
				publish(res);
			}
			
			res += "Fin des tests.\n";
			publish(res);
			return res;
		}

		private void substitue(String fichier, String ligneAvant, String ligneApres) {
			
			String fichierOut = fichier + ".new";
		    String NL = System.getProperty("line.separator");
		    Scanner scanner = null;
		    Writer out = null;
		    boolean erreur = false;
		    
			try {
			    scanner = new Scanner(new FileInputStream(fichier), "UTF8");
			    out = new OutputStreamWriter(new FileOutputStream(fichierOut), "UTF8");
				
				while (scanner.hasNextLine()) {
					String ligne = scanner.nextLine();
					if (ligne.indexOf(ligneAvant) != -1) {
						out.write(ligneApres + NL);
					}
					else {
						out.write(ligne + NL);
					}
				}
			}
			catch (FileNotFoundException fnfe) {
				fnfe.printStackTrace();
				erreur = true;
			}
			catch (UnsupportedEncodingException uee) {
				uee.printStackTrace();
				erreur = true;
			}
			catch (IOException ioe) {
				ioe.printStackTrace();
				erreur = true;
			}
		    finally{
		    	if (scanner != null) {
		    		scanner.close();
		    	}
		    	if (out != null) {
		    		try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
						erreur = true;
					}
		    	}
		    }
			
			if (!erreur) {
				// Déplace les fichiers
				File oldFile = new File(fichier + ".old");
				if (oldFile.exists()) {
					oldFile.delete();
				}
				File fichierServer = new File(fichier); 
				fichierServer.renameTo(oldFile);
				
				File fichierNew = new File(fichierOut);
				fichierNew.renameTo(fichierServer);
			}
		}
	}


}
