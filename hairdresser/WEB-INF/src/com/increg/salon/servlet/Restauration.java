package com.increg.salon.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.increg.commun.DBSession;
import com.increg.commun.Executer;
import com.increg.salon.bean.ParamBean;
import com.increg.salon.bean.SalonSessionImpl;
import com.increg.util.StringInverseComp;
/**
 * Servlet de restauration de la base
 * Creation date: (05/10/2001 08:26:28)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class Restauration extends ConnectedServlet {

    /**
     * Sauvegarde Disque 
     */
    public static final String TYPE_DISQUE = "D";
    /**
     * Sauvegarde Internet 
     */
    public static final String TYPE_INTERNET = "I";
    /**
     * Sauvegarde sur média amovible
     */
    public static final String TYPE_MEDIA = "M";
    /**
     * Taille des blocs lus sur Internet
     */
    private static final int CHUNK_SIZE = 4096;

    /**
     * Process incoming requests for information
     * 
     * @param request Object that encapsulates the request to the servlet 
     * @param response Object that encapsulates the response from the servlet
     */
    public void performTask(HttpServletRequest request, HttpServletResponse response) {

        HttpSession mySession = request.getSession(false);
        SalonSessionImpl mySalon = (SalonSessionImpl) mySession.getAttribute("SalonSession");
        ResourceBundle messages = mySalon.getMessagesBundle();
        DBSession myDBSession = mySalon.getMyDBSession();

        // Récupère les paramètres
        String Action = request.getParameter("Action");
        String Type = request.getParameter("Type");
        String nomFichier = request.getParameter("nomFichier");

        // Liste des fichiers
        TreeSet listeFichier = new TreeSet(new StringInverseComp());

        try {
            if (Action == null) {
                // Par défaut Affiche la liste du disque dur
                Action = "Liste";
                Type = TYPE_DISQUE;
            }
            // ************
            if (Action.equals("Restauration")) {

                try {
                    /**
                     * Récupération du fichier depuis le serveur si Internet
                     */
                    File fichier = null;
                    if ((Type != null) && (Type.equals(TYPE_INTERNET))) {
                        URL curURL = null;
                        ParamBean valUrl = ParamBean.getParamBean(myDBSession, Integer.toString(ParamBean.CD_URL_SAVEGET));
                        if (valUrl != null) {
                            curURL = new URL(valUrl.getVAL_PARAM() + mySalon.getTheLicence().getPasswordAuthentication().getUserName() + "/" + nomFichier);
                        }
                        HttpURLConnection aCon = (HttpURLConnection) curURL.openConnection();

                        aCon.setRequestProperty("Authorization", mySalon.getTheLicence().getBasicAuthentication());
                        aCon.setRequestProperty("Connection", "close");
                        aCon.connect();

                        fichier = File.createTempFile("RestaurationInternet", "");
                        BufferedOutputStream fichierStream = new BufferedOutputStream(new FileOutputStream(fichier));

                        InputStream restauStream = aCon.getInputStream();
                        byte[] dataBytes = new byte[CHUNK_SIZE];
                        int byteRead = 0;
                        while (byteRead != -1) {
                            byteRead = restauStream.read(dataBytes, 0, CHUNK_SIZE);

                            // Stocke dans le fichier
                            if (byteRead != -1) {
                                fichierStream.write(dataBytes, 0, byteRead);
                            }
                        }

                        fichierStream.close();
                    }
                    else if ((Type != null) && (Type.equals(TYPE_MEDIA))) {
                        fichier = new File(mySalon.getSavePathMedia() + nomFichier);
                    }
                    else {
                        fichier = new File(mySalon.getSavePath() + nomFichier);
                    }

                    /**
                     * Deconnexion de la base pour pouvoir restaurer
                     */
                    String dbName = myDBSession.getBaseName();
                    mySalon = null;
                    myDBSession.closeAll();

                    File fichierUnzip = File.createTempFile("Restauration_p2", "");

                    /**
                     * Restauration proprement dite
                     * Approche à mener : zcat d'un coté
                     * grep -v NOTICE: | wc -l
                     * Si erreur : Proposer la restauration_auto
                     */
                    boolean erreur = false;
                    if (!erreur) {
                        Executer uncompress =
                            new Executer("bash --login -c \"zcat '" + fichier.getAbsolutePath().replace('\\', '/') + "' > '" + fichierUnzip.getAbsolutePath().replace('\\', '/') + "' \"");
                        if (uncompress.runAndWait() != 0) {
                            /**
                             * Messages dans les attributs car plus de bean session
                             */
                            request.setAttribute("Erreur", messages.getString("restauration.erreurP1"));
                            erreur = true;
                        }
                    }

                    // Suppression de toutes les tables et sequences pour que la restauration soit propre
                    if (!erreur) {
                        Executer dropDB = new Executer("bash --login -c \"dropdb " + dbName + "\"");
                        if (dropDB.runAndWait() != 0) {
                            /**
                             * Messages dans les attributs car plus de bean session
                             */
                            request.setAttribute("Erreur", messages.getString("restauration.erreurP2"));
                            erreur = true;
                        }
                    }

                    // Recréation de la base !!!! A la main 
                    if (!erreur) {
                        myDBSession.setBaseName("template1");
                        myDBSession.open();

                        String[] SQL = {"create database " + dbName + " with template=template0 encoding='LATIN1'"};
                        int[] cr = myDBSession.doExecuteSQL(SQL);
                        if (cr[0] != 1) {
                            /**
                             * Messages dans les attributs car plus de bean session
                             */
                            request.setAttribute("Erreur", messages.getString("restauration.erreurP3"));
                            erreur = true;
                        }

                        myDBSession.close();
                        myDBSession.setBaseName(dbName);
                    }

                    if (!erreur) {
                        // Plusieurs tentatives de restauration au cas ou                
                        int nbEssai = 0;
                        boolean done = false;

                        while (!done && (++nbEssai < 3)) {
                            if (nbEssai > 1) {
                                // Trace
                                System.out.println("Tentative de restauration N°" + nbEssai);
                            }
                            Executer resto =
                                new Executer(
                                    "bash --login -c \"/usr/bin/pg_restore.exe -v -F c -d " + dbName + " '" + fichierUnzip.getAbsolutePath().replace('\\', '/') + "' 2> Resto.err > Resto.txt \"");

                            int cr = resto.runAndWait(5 * 60 * 1000, 2000);
                            if (cr < 0) {
                                request.setAttribute("Erreur", messages.getString("restauration.erreurRelance"));
                                // Kill le process au cas où il traine
                                Executer killIt = new Executer("bash --login -c \"kill -9 `ps | grep pg_restore | grep -v grep | gawk '{ print $1 '}` \"");
                                killIt.runAndWait();
                                erreur = true;
                            }
                            else {
                                // Vérification du fichier log
                                Executer verify = new Executer("bash --login -c \"grep ERROR Resto.txt Resto.err \"");
                                cr = verify.runAndWait();

                                // Test sur le code retour du grep
                                if (cr != 1) {
                                    /**
                                     * Messages dans les attributs car plus de bean session
                                     */
                                    request.setAttribute("Erreur", messages.getString("restauration.erreur"));
                                    erreur = true;
                                }
                                else {
                                    request.setAttribute("Info", messages.getString("restauration.succes"));
                                    erreur = false;
                                    done = true;
                                }
                            }
                        }
                    }

                    /**
                     * Nettoyage des fichiers temporaires
                     */
                    if ((Type != null) && (Type.equals(Restauration.TYPE_INTERNET))) {
                        fichier.delete();
                    }
                    fichierUnzip.delete();

                    /**
                     * Reset pour repartir propre
                     */
                    myDBSession = null;
                    mySession.removeAttribute("SalonSession");
                    mySession.invalidate();
                    System.gc();
                }
                catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("Erreur", e.toString());
                }

            }
            else if (Action.equals("Suppression")) {

                if ((Type != null) && (Type.equals(Restauration.TYPE_INTERNET))) {
                    // Sauvegarde sur Internet
                    URL sauvURL = null;
                    ParamBean valUrl = ParamBean.getParamBean(myDBSession, Integer.toString(ParamBean.CD_URL_SAVESUP));
                    if (valUrl != null) {
                        sauvURL = new URL(valUrl.getVAL_PARAM());
                    }
                    HttpURLConnection aCon = null;
                    // 3 tentatives de connexion
                    int nbEssai = 0;
                    while ((nbEssai < 3) && (aCon == null)) {
                        nbEssai++;
                        try {
                            aCon = (HttpURLConnection) sauvURL.openConnection();
                        }
                        catch (Exception e) {
                            System.out.println("<1>Erreur à l'ouverture de la connection");
                            aCon = null;
                        }
                    }
                    if (aCon == null) {
                        // Connexion impossible
                        throw (new Exception(messages.getString("message.serverKo")));
                    }

                    aCon.setUseCaches(false);
                    aCon.setDoOutput(true);
                    aCon.setRequestMethod("POST");
                    aCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    aCon.setRequestProperty("Connection", "close");
                    aCon.setRequestProperty("Authorization", mySalon.getTheLicence().getBasicAuthentication());

                    nbEssai = 0;
                    boolean connectionOk = false;
                    while ((nbEssai < 3) && (!connectionOk)) {
                        nbEssai++;
                        try {
                            aCon.connect();
                            connectionOk = true;
                        }
                        catch (Exception e) {
                            System.out.println("<2>Erreur à la connection proprement dite");
                        }
                    }
                    if (!connectionOk) {
                        throw (new Exception(messages.getString("message.serverKo")));
                    }

                    DataOutputStream dataOut = new DataOutputStream(new BufferedOutputStream(aCon.getOutputStream()));

                    Enumeration paramEnum = request.getParameterNames();
                    boolean first = true;
                    while (paramEnum.hasMoreElements()) {
                        String paramName = (String) paramEnum.nextElement();
                        String paramValue = request.getParameter(paramName);

                        if (paramValue.equals("on")) {
                            // Une case de cochée : Le nom du fichier est paramName
                            // Envoi du nom du fichier
                            if (!first) {
                                dataOut.writeBytes("&");
                            }
                            dataOut.writeBytes("NOM=" + URLEncoder.encode(paramName));
                            first = false;
                        }

                    }
                    dataOut.flush();
                    dataOut.close();

                    if (aCon.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        // Erreur
                    	String msg = MessageFormat.format(mySalon.getMessagesBundle().getString("restauration.suppressionKo"), new Object[] { aCon.getResponseMessage() });
                        mySalon.setMessage("Erreur", msg);
                    }
                    else {
                        mySalon.setMessage("Info", messages.getString("restauration.suppressionInternetOk"));
                    }

                }
                else {
                    Enumeration paramEnum = request.getParameterNames();
                    String path;
                    if ((Type != null) && (Type.equals(Restauration.TYPE_MEDIA))) {
                        path = mySalon.getSavePathMedia();
                    }
                    else {
                        path = mySalon.getSavePath();
                    }
                    while (paramEnum.hasMoreElements()) {
                        String paramName = (String) paramEnum.nextElement();
                        String paramValue = request.getParameter(paramName);

                        if (paramValue.equals("on")) {
                            // Une case de cochée : Le nom du fichier est paramName
                            File fichier = new File(path + paramName);
                            if (fichier.exists()) {
                                fichier.delete();
                            }
                        }

                    }
                    mySalon.setMessage("Info", messages.getString("restauration.suppressionOk"));
                }

            }
            else if (!Action.equals("Liste") && !Action.equals("Gestion")) {
                System.out.println("Action non codée : " + Action);
            }

            /**
             * Enchaine en séquence afin de constituer la liste après opération
             */
            if (Action.equals("Liste") || Action.equals("Gestion") || Action.equals("Suppression")) {
                // Recharge la liste des fichiers en fonction du type
                if ((Type == null) || (Type.equals(TYPE_DISQUE)) || (Type.equals(TYPE_MEDIA))) {
                    String path;
                    if ((Type != null) && (Type.equals(Restauration.TYPE_MEDIA))) {
                        path = mySalon.getSavePathMedia();
                    }
                    else {
                        path = mySalon.getSavePath();
                    }
                    File curDir = new File(path);
                    File[] fichiers = curDir.listFiles();

                    if (fichiers != null) {
                        for (int i = 0; i < fichiers.length; i++) {
                            if ((!fichiers[i].isDirectory()) && (fichiers[i].getName().indexOf(".gz") != -1)) {
                                // Ajoute le fichier à la liste
                                listeFichier.add(fichiers[i].getName());
                            }
                        }
                    }
                }
                else if (Type.equals(TYPE_INTERNET)) {
                    URL curURL = null;
                    ParamBean valUrl = ParamBean.getParamBean(myDBSession, Integer.toString(ParamBean.CD_URL_SAVEGET));
                    if (valUrl != null) {
                        curURL = new URL(valUrl.getVAL_PARAM() + mySalon.getTheLicence().getPasswordAuthentication().getUserName() + "/");
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
                        throw (new Exception(messages.getString("message.serverKo")));
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

                    while ((pos = page.indexOf("[CMP]", pos)) != -1) {
                        pos = page.indexOf("=\"", pos);

                        if (pos != -1) {
                            listeFichier.add(page.substring(pos + 2, page.indexOf("\"", pos + 2)));
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
        }
        catch (Exception e) {
            mySalon.setMessage("Erreur", e.toString());
            e.printStackTrace();
            request.setAttribute("Erreur", e.toString());
        }

        request.setAttribute("Type", Type);
        request.setAttribute("listeFichier", listeFichier);

        try {
            // Passe la main à la forme
            if (Action.equals("Restauration")) {
                getServletConfig().getServletContext().getRequestDispatcher("/finRest.jsp").forward(request, response);
            }
            else if (Action.equals("Gestion") || Action.equals("Suppression")) {
                getServletConfig().getServletContext().getRequestDispatcher("/ficGestSauv.jsp").forward(request, response);
            }
            else {
                getServletConfig().getServletContext().getRequestDispatcher("/ficRest.jsp").forward(request, response);
            }

        }
        catch (Exception e) {
            System.out.println("Restauration::performTask : Erreur à la redirection : " + e.toString());
        }

    }
}
