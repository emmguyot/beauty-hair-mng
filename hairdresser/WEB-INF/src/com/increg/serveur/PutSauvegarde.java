/*
 * Place un fichier de sauvegarde sur le serveur
 * Copyright (C) 2001-2008 Emmanuel Guyot <See emmguyot on SourceForge> 
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
package com.increg.serveur;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;

public class PutSauvegarde extends javax.servlet.http.HttpServlet {
    /**
     * Process incoming HTTP POST requests
     * 
     * @param request
     *            Object that encapsulates the request to the servlet
     * @param response
     *            Object that encapsulates the response from the servlet
     * @throws IOException .
     * @throws javax.servlet.ServletException .
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {

        performTask(request, response);

    }

    /**
     * Process incoming requests for information
     * 
     * @param request
     *            Object that encapsulates the request to the servlet
     * @param response
     *            Object that encapsulates the response from the servlet
     */
    public void performTask(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) {

        PrintWriter out = null;
        InputStream in = null;
        FileOutputStream fileOut = null;
        java.util.ResourceBundle resconfig = java.util.ResourceBundle.getBundle("config"); //$NON-NLS-1$
        String rootPath = resconfig.getString("savePath"); //$NON-NLS-1$
        long MAX_SIZE = 1024 * 1024 * 5;
        try {

            // Ajoute le user
            java.security.Principal userPrincipal = request.getUserPrincipal();
            String user = "default";
            if (userPrincipal != null) {
                user = userPrincipal.getName();
            } else {
                // Triture le Header pour en extraire le user
                String authorization = new String(Base64.decodeBase64(request.getHeader("Authorization").substring(6).getBytes()));
                user = authorization.substring(0, authorization.indexOf(':'));
            }
            rootPath = rootPath + user + System.getProperty("file.separator");
            System.out.println(new Date().toString() + " : Début sauvegarde vers " + rootPath);

            response.setContentType("text/html");
            out = response.getWriter();
            String contentType = request.getContentType();

            if ((contentType != null) && (contentType.indexOf("multipart/form-data") >= 0)) {
                in = request.getInputStream();
                int formDataLength = request.getContentLength();
                if (formDataLength > MAX_SIZE) {
                    System.out.println("Sorry file is too large.");
                    response.sendError(500, "Le fichier est trop grand");
                    return;
                }
                byte dataBytes[] = new byte[formDataLength];
                int byteRead = 0;
                int totalBytesRead = 0;
                while ((byteRead != -1) && (totalBytesRead < formDataLength)) {
                    byteRead = in.read(dataBytes, totalBytesRead, formDataLength);
                    totalBytesRead += byteRead;
                }
                if (totalBytesRead < formDataLength) {
                    // Sauvegarde abortée
                    throw new Exception(user + " => La taille totale du fichier " + Integer.toString(formDataLength) + " n'a pas été atteinte : " + Integer.toString(totalBytesRead));
                }
                String file = new String(dataBytes);
                String saveFile = file.substring(file.indexOf("filename=\"") + 10);
                saveFile = saveFile.substring(0, saveFile.indexOf("\n"));
                saveFile = saveFile.substring(saveFile.lastIndexOf("\\") + 1, saveFile.indexOf("\""));
                saveFile = saveFile.substring(saveFile.lastIndexOf("/") + 1);
                int lastIndex = contentType.lastIndexOf("=");
                String boundary = contentType.substring(lastIndex + 1, contentType.length());
                String fileName = new String(rootPath + saveFile);
                int pos;
                pos = file.indexOf("filename=\"");
                pos = file.indexOf("\n", pos) + 1;
                pos = file.indexOf("\n", pos) + 1;
                pos = file.indexOf("\n", pos) + 1;
                int boundaryLocation = file.indexOf(boundary, pos) - 4;
                int startPos = ((file.substring(0, pos)).getBytes()).length;
                int endPos = ((file.substring(0, boundaryLocation)).getBytes()).length;
                File fileDir = new File(rootPath);
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }
                fileOut = new FileOutputStream(fileName);
                String dataString = new String(dataBytes, startPos, (endPos - startPos));

                fileOut.write(Base64.decodeBase64(dataString.getBytes()));
                fileOut.flush();
                fileOut.close();
                System.out.println(new Date().toString() + " : Fin sauvegarde " + saveFile);
                out.println("Ok");
            } //if end
            else {
                String content = request.getContentType();
                System.out.println("request not multipart /form - data " + content);
                response.sendError(500);
            }
        } //try end
        catch (Exception e) {
            System.out.println("PutSauvegarde::Error : " + e.toString());
            try {
                response.sendError(500);
            } catch (Exception e2) {
                //
            }
        }

    }
}