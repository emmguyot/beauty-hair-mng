/*
 * Sauvegarde d'une base
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
package com.increg.salon.servlet;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Calendar;

import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;

import com.increg.commun.BasicSession;
import com.increg.commun.DBSession;
import com.increg.commun.Executer;
import com.increg.salon.bean.ParamBean;
import com.increg.salon.bean.SalonSession;
import com.increg.salon.bean.SalonSessionImpl;
import com.increg.util.GZipper;
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

        // Récupère les paramètres
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

                    String nomFichierTmp = nomFichier + Fichier + ".tmp";
                    nomFichier = nomFichier + Fichier + ".gz";
                    String cmd = System.getenv("PG_HOME") + "\\bin\\pg_dump.exe -d -c -F c -Z 9 -f " + nomFichierTmp + " " + myDBSession.getBaseName();
                    Executer dumpProc = new Executer(cmd);
                    if (dumpProc.runAndWait() != 0) {
                        mySalon.setMessage("Erreur", BasicSession.TAG_I18N + "sauvegarde.erreur" + BasicSession.TAG_I18N);
                    }
                    else {
                    	GZipper.gzipFile(nomFichierTmp, nomFichier);
                    	new File(nomFichierTmp).delete();
                    	
                        mySalon.setMessage("Info", BasicSession.TAG_I18N + "sauvegarde.succes" + BasicSession.TAG_I18N);
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
                                    System.out.println("<1>Erreur à l'ouverture de la connection");
                                    aCon = null;
                                }
                            }
                            if (aCon == null) {
                                // Connexion impossible
                                throw (new Exception(BasicSession.TAG_I18N + "message.serverKo" + BasicSession.TAG_I18N));
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
                                    System.out.println("<2>Erreur à la connection proprement dite");
                                }
                            }
                            if (!connectionOk) {
                                throw (new Exception(BasicSession.TAG_I18N + "message.serverKo" + BasicSession.TAG_I18N));
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
                            dataOut.write(Base64.encodeBase64(dataBytes));
                            dataOut.writeBytes("\r\n\r\n" + boundary + "--\r\n");

                            dataOut.flush();
                            dataOut.close();

                            if (aCon.getResponseCode() != HttpURLConnection.HTTP_OK) {
                                // Erreur
                            	String msg = MessageFormat.format(mySalon.getMessagesBundle().getString("sauvegarde.uploadKo"), new Object[] { aCon.getResponseMessage() });
                            	mySalon.setMessage("Erreur", msg);
                            }
                            else {
                                mySalon.setMessage("Info", BasicSession.TAG_I18N + "sauvegarde.internetSucces" + BasicSession.TAG_I18N);
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
                System.out.println("Action non codée : " + Action);
            }
        }
        catch (Exception e) {
            mySalon.setMessage("Erreur", e.toString());
            System.out.println("Note : " + e.toString());
        }

        request.setAttribute("Fichier", Fichier);

        try {
            // Passe la main à la forme
            getServletConfig().getServletContext().getRequestDispatcher("/ficSauv.jsp").forward(request, response);

        }
        catch (Exception e) {
            System.out.println("Sauvegarde::performTask : Erreur à la redirection : " + e.toString());
        }

    }
}
