package com.increg.salon.servlet;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import javax.servlet.http.HttpSession;

import com.ibm.ejs.security.util.Base64Coder;
import com.increg.commun.DBSession;
import com.increg.commun.Executer;
import com.increg.salon.bean.ParamBean;
import com.increg.salon.bean.SalonSession;
import com.increg.salon.bean.SalonSessionImpl;
/**
 * Servlet de sauvegarde de la base
 * Creation date: (05/10/2001 08:26:28)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class Sauvegarde extends ConnectedServlet {

    /**
     * Process incoming requests for information
     * 
     * @param request Object that encapsulates the request to the servlet 
     * @param response Object that encapsulates the response from the servlet
     */
    public void performTask(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) {

        HttpSession mySession = request.getSession(false);
        SalonSessionImpl mySalon = (SalonSessionImpl) mySession.getAttribute("SalonSession");
        DBSession myDBSession = ((SalonSession) mySession.getAttribute("SalonSession")).getMyDBSession();

        // R�cup�re les param�tres
        String Action = request.getParameter("Action");
        String Type = request.getParameter("Type");
        String Fichier = request.getParameter("Fichier");

        // pg_dump -c -f fichier -F c -Z 9 salon
        java.text.SimpleDateFormat formatDate = new java.text.SimpleDateFormat("yyyyMMdd_HHmm");

        try {
            if (Action == null) {
                Fichier = "SauvegardeDB_" + formatDate.format(Calendar.getInstance().getTime());
            }
            else if (Action.equals("Sauvegarde")) {

                try {
                    String nomFichier;
                    if ((Type != null) && (Type.equals(Restauration.TYPE_MEDIA))) {
                        nomFichier = mySalon.getSavePathMedia();
                    }
                    else {
                        nomFichier = mySalon.getSavePath();
                    }

                    nomFichier = nomFichier + Fichier + ".gz";
                    Executer dumpProc =
                        new Executer("bash --login -c \"" + "/usr/bin/pg_dump -c -F c -Z 9 " + myDBSession.getBaseName() + " 2> /dev/null | gzip -9 2> /dev/null > '" + nomFichier + "'\"");
                    if (dumpProc.runAndWait() != 0) {
                        mySalon.setMessage("Erreur", "La sauvegarde ne s'est pas bien d�roul�e.");
                    }
                    else {
                        mySalon.setMessage("Info", "La sauvegarde est termin�e.");
                        if ((Type != null) && (Type.equals(Restauration.TYPE_INTERNET))) {
                            // Sauvegarde sur Internet
                            URL sauvURL = null;
                            ParamBean valUrl = ParamBean.getParamBean(myDBSession, Integer.toString(ParamBean.CD_URL_SAVEPUT));
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
                                    System.out.println("<1>Erreur � l'ouverture de la connection");
                                    aCon = null;
                                }
                            }
                            if (aCon == null) {
                                // Connexion impossible
                                throw (new Exception("Impossible de se connecter au serveur."));
                            }
                            File fichier = new File(nomFichier);
                            String boundary = "------------7d1199c2401b4";

                            aCon.setUseCaches(false);
                            aCon.setDoOutput(true);
                            aCon.setRequestMethod("POST");
                            aCon.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
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
                                    System.out.println("<2>Erreur � la connection proprement dite");
                                }
                            }
                            if (!connectionOk) {
                                throw (new Exception("Impossible de se connecter au serveur"));
                            }

                            // Lecture du fichier
                            FileInputStream fichierStream = new FileInputStream(fichier);
                            byte[] dataBytes = new byte[(int) fichier.length()];
                            int byteRead = 0;
                            int totalBytesRead = 0;
                            while (totalBytesRead < fichier.length()) {
                                byteRead = fichierStream.read(dataBytes, totalBytesRead, (int) fichier.length());
                                totalBytesRead += byteRead;
                            }
                            fichierStream.close();

                            // String dataString = new String(dataBytes);
                            DataOutputStream dataOut = new DataOutputStream(new BufferedOutputStream(aCon.getOutputStream()));

                            // Positionne l'entete dans le corps
                            // aCon.setRequestProperty("Content-Length",Long.toString(fichier.length()));
                            dataOut.writeBytes(boundary + "\r\n");
                            dataOut.writeBytes("Content-Disposition: form-data; name=\"NOM\"; filename=\"" + nomFichier + "\"\r\n");
                            dataOut.writeBytes("Content-Type: application/octet-stream\r\n");
                            // dataOut.writeBytes("Content-transfer-encoding: binary\r\n");
                            dataOut.writeBytes("\r\n");

                            // Envoi du fichier
                            dataOut.write(Base64Coder.base64Encode(dataBytes));
                            dataOut.writeBytes("\r\n\r\n" + boundary + "--\r\n");

                            dataOut.flush();
                            dataOut.close();

                            if (aCon.getResponseCode() != HttpURLConnection.HTTP_OK) {
                                // Erreur
                                mySalon.setMessage("Erreur", "Probl�me au d�pot du fichier sur le serveur (" + aCon.getResponseMessage() + ")");
                            }
                            else {
                                mySalon.setMessage("Info", "La sauvegarde sur internet est termin�e.");
                            }

                            /**
                             * Nettoyage des fichiers temporaires
                             */
                            fichier.delete();
                        }

                    }
                }
                catch (Exception e) {
                    System.out.println("Erreur " + e.toString());
                    mySalon.setMessage("Erreur", e.toString());
                }

            }
            else {
                System.out.println("Action non cod�e : " + Action);
            }
        }
        catch (Exception e) {
            mySalon.setMessage("Erreur", e.toString());
            System.out.println("Note : " + e.toString());
        }

        request.setAttribute("Fichier", Fichier);

        try {
            // Passe la main � la forme
            getServletConfig().getServletContext().getRequestDispatcher("/ficSauv.jsp").forward(request, response);

        }
        catch (Exception e) {
            System.out.println("Sauvegarde::performTask : Erreur � la redirection : " + e.toString());
        }

    }
}
