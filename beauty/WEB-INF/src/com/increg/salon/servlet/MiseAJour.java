package com.increg.salon.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.TreeSet;

import javax.servlet.http.HttpSession;

import com.increg.commun.BasicSession;
import com.increg.commun.DBSession;
import com.increg.salon.bean.ParamBean;
import com.increg.salon.bean.SalonSessionImpl;
import com.increg.util.StringInverseComp;
/**
 * Servlet de mise à jour du logiciel
 * Creation date: (16/03/2002 22:37:57)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class MiseAJour extends ConnectedServlet {
    /**
     * Process incoming requests for information
     * ATTENTION Aux objets personnalisés (Images, properties, ...)
     * 
     * @param request Object that encapsulates the request to the servlet 
     * @param response Object that encapsulates the response from the servlet
     */
    public void performTask(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) {

        final int CHUNK_SIZE = 4096;

        // Initialise une session light de traduction
        BasicSession myBasicSession = new BasicSession();
        myBasicSession.setLangue(request.getLocale());
 
        HttpSession mySession = request.getSession(false);
        SalonSessionImpl mySalon = (SalonSessionImpl) mySession.getAttribute("SalonSession");
        ResourceBundle messages = mySalon.getMessagesBundle();
        DBSession myDBSession = mySalon.getMyDBSession();

        // Récupère les paramètres
        String Action = request.getParameter("Action");
        String Type = request.getParameter("Type");
        String nomFichier = request.getParameter("nomFichier");

        // pg_restore -c -f fichier -F c
        Runtime aRuntime = Runtime.getRuntime();

        // Liste des fichiers
        TreeSet listeFichier = new TreeSet(new StringInverseComp());

        try {
            if (Action == null) {
                // Par défaut Affiche la liste du disque dur
                Action = "Liste";
                Type = Restauration.TYPE_INTERNET;
            }
            // ************
            if (Action.equals("Liste")) {
                /**
                 * Recharge la liste des fichiers en fonction du type
                 */
                if ((Type == null) || (Type.equals(Restauration.TYPE_DISQUE))) {
                    File curDir = new File("d:\\");
                    File[] fichiers = curDir.listFiles();

                    for (int i = 0; i < fichiers.length; i++) {
                        if ((!fichiers[i].isDirectory()) && (fichiers[i].getName().indexOf(".war") != -1)) {
                            // Ajoute le fichier à la liste
                            listeFichier.add(fichiers[i].getName());
                        }
                    }
                }
                else if (Type.equals(Restauration.TYPE_INTERNET)) {

                    URL curURL = null;
                    ParamBean valUrl = ParamBean.getParamBean(myDBSession, Integer.toString(ParamBean.CD_URL_MAJ));
                    if (valUrl != null) {
                        curURL = new URL(valUrl.getVAL_PARAM() + "/Institut");
                    }
                    HttpURLConnection aCon = null;
                    // 3 tentatives de connexion
                    int nbEssai = 0;
                    while ((nbEssai < 3) && (aCon == null)) {
                        nbEssai++;
                        try {
                            aCon = (HttpURLConnection) curURL.openConnection();
                        }
                        catch (Exception e) {
                            aCon = null;
                        }
                    }
                    if (aCon == null) {
                        // Connexion impossible
                        throw (new Exception(BasicSession.TAG_I18N + "message.serverKo" + BasicSession.TAG_I18N));
                    }

                    aCon.setUseCaches(false);
                    aCon.setRequestProperty("Authorization", mySalon.getTheLicence().getBasicAuthentication());

                    BufferedInputStream pageStream = new BufferedInputStream(aCon.getInputStream());
                    // Lecture
                    byte[] dataBytes = new byte[CHUNK_SIZE];
                    StringBuffer pageBuf = new StringBuffer();
                    int byteRead = 0;
                    while (byteRead != -1) {
                        byteRead = pageStream.read(dataBytes, 0, CHUNK_SIZE);

                        // Stocke dans la chaine
                        if (byteRead != -1) {
                            pageBuf.append(new String(dataBytes, 0, byteRead));
                        }
                    }
                    String page = pageBuf.toString();

                    // Interprétation de la page
                    int pos = 0;

                    while ((pos = page.indexOf(".war\"", pos)) != -1) {
                        pos = page.lastIndexOf("=\"", pos);

                        if (pos != -1) {
                            listeFichier.add(page.substring(pos + 2, page.indexOf("\"", pos + 2)));
                            pos = page.indexOf("\"", pos + 2);
                        }
                        else {
                            System.out.println("Format de la page non prévu");
                        }
                    }
                }
                else {
                    System.out.println("Type non codé : " + Type);
                }

            }
            else if (Action.equals("MiseAJour")) {

                try {
                    /**
                     * Récupération du fichier depuis le serveur si Internet
                     *   et Remplacement du fichier salon.war
                     */
                    String tomcatHome = System.getProperty("tomcat.home");
                    if (tomcatHome == null) {
                        tomcatHome = System.getProperty("catalina.home");
                    }
                    if (tomcatHome == null) {
                        // Pb
                        tomcatHome = System.getProperty("java.home");
                    }
                    if (tomcatHome == null) {
                        tomcatHome = System.getProperty("increg");
                    }
                    if (tomcatHome == null) {
                        tomcatHome = ".";
                    }
                    File fichier = new File(tomcatHome + "/../war/institut.war");
                    if ((Type != null) && (Type.equals(Restauration.TYPE_INTERNET))) {

                        URL curURL = null;
                        ParamBean valUrl = ParamBean.getParamBean(myDBSession, Integer.toString(ParamBean.CD_URL_MAJ));
                        if (valUrl != null) {
                            curURL = new URL(valUrl.getVAL_PARAM() + "/Institut/" + nomFichier);
                        }
                        HttpURLConnection aCon = (HttpURLConnection) curURL.openConnection();

                        aCon.setRequestProperty("Authorization", mySalon.getTheLicence().getBasicAuthentication());
                        aCon.setRequestProperty("Connection", "close");
                        aCon.connect();

                        if (fichier.exists()) {
                            fichier.delete();
                        }

                        BufferedOutputStream fichierStream = new BufferedOutputStream(new FileOutputStream(fichier));

                        InputStream warStream = aCon.getInputStream();
                        byte[] dataBytes = new byte[CHUNK_SIZE];
                        int byteRead = 0;
                        while (byteRead != -1) {
                            byteRead = warStream.read(dataBytes, 0, CHUNK_SIZE);

                            // Stocke dans le fichier
                            if (byteRead != -1) {
                                fichierStream.write(dataBytes, 0, byteRead);
                            }
                        }

                        fichierStream.close();
                    }
                    else {
                        // Recopie depuis un CD
                        if (fichier.exists()) {
                            fichier.delete();
                        }

                        File fichierIn = new File("d:\\" + nomFichier);

                        BufferedOutputStream fichierStream = new BufferedOutputStream(new FileOutputStream(fichier));
                        InputStream warStream = new FileInputStream(fichierIn);

                        byte[] dataBytes = new byte[CHUNK_SIZE];
                        int byteRead = 0;
                        while (byteRead != -1) {
                            byteRead = warStream.read(dataBytes, 0, CHUNK_SIZE);

                            // Stocke dans le fichier
                            if (byteRead != -1) {
                                fichierStream.write(dataBytes, 0, byteRead);
                            }
                        }

                        fichierStream.close();
                    }

                    /**
                     * Deconnexion de la base pour pouvoir restaurer
                     */
                    mySalon = null;
                    mySession.removeAttribute("SalonSession");
                    mySession.invalidate();
                    System.gc();

                    /**
                     * Messages dans les attributs car plus de bean session
                     */
                    String cmd = "bash --login -c \"touch " + fichier.getParent().replace('\\', '/') + "/../maj_afaire\"";
                    Process RestoProc = aRuntime.exec(cmd);
                    // Test sur le code retour du rm
                    if (RestoProc.waitFor() != 0) {
                        /**
                         * Messages dans les attributs car plus de bean session
                         */
                    	myBasicSession.setMessage("Erreur", BasicSession.TAG_I18N + "miseAJour.erreur" + BasicSession.TAG_I18N);
                        request.setAttribute("Erreur", myBasicSession.getMessage("Erreur"));
                        //response.getWriter().println("<html><body><h1>Erreur durant la mise à jour.<br>Merci de prendre contact avec l'assistance InCrEG pour débloquer la situation.</h1></body></html>");
                    }
                    else {
                    	myBasicSession.setMessage("Info", BasicSession.TAG_I18N + "miseAJour.succes" + BasicSession.TAG_I18N);
                        request.setAttribute("Info", myBasicSession.getMessage("Info"));
                        // Informe du résultat
                        //response.getWriter().println("<html><body><h1>La mise à jour est terminée.<br>Vous devez arrêter le logiciel et le redémarrer pour que cette mise à jour soit prise en compte.</h1></body></html>");
                    }

                }
                catch (Exception e) {
                    System.out.println("Erreur " + e.toString());
                    request.setAttribute("Erreur", e.toString());
                }

            }
            else {
                System.out.println("Action non codée : " + Action);
            }
        }
        catch (Exception e) {
            request.setAttribute("Erreur", e.toString());
            System.out.println("Note : " + e.toString());
        }

        request.setAttribute("Type", Type);
        request.setAttribute("listeFichier", listeFichier);

        try {
            // Passe la main à la forme
            if (!Action.equals("MiseAJour")) {
                getServletConfig().getServletContext().getRequestDispatcher("/ficMaj.jsp").forward(request, response);
            }
            else {
                getServletConfig().getServletContext().getRequestDispatcher("/finMaj.jsp").forward(request, response);
            }
        }
        catch (Exception e) {
            System.out.println("MiseAJour::performTask : Erreur à la redirection : " + e.toString());
        }

    }
}