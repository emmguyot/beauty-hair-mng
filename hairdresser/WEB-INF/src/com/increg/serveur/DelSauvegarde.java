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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;

public class DelSauvegarde extends javax.servlet.http.HttpServlet {
    /**
     * Process incoming HTTP POST requests
     * 
     * @param request
     *            Object that encapsulates the request to the servlet
     * @param response
     *            Object that encapsulates the response from the servlet
     * @throws IOException .
     * @throws ServletException .
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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
        ResourceBundle resconfig = ResourceBundle.getBundle("config"); //$NON-NLS-1$
        String rootPath = resconfig.getString("savePath"); //$NON-NLS-1$
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
            System.out.println(new Date().toString() + " : Gestion sauvegarde de " + rootPath);

            response.setContentType("text/html");
            out = response.getWriter();

            File fileDir = new File(rootPath);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }

            String[] tabNom = request.getParameterValues("NOM");
            for (int i = 0; i < tabNom.length; i++) {
                // Interdit les / qui permettraient de changer de répertoire
                if (tabNom[i].indexOf('/') == -1) {
                    File aFile = new File(rootPath, tabNom[i]);
                    if (aFile.exists()) {
                        aFile.delete();
                    }
                }
            }
            System.out.println(new Date().toString() + " : Gestion sauvegarde suppression de " + tabNom.length + " fichiers");
            out.println("Ok");
        } //try end
        catch (Exception e) {
            System.out.println("DelSauvegarde::Error : " + e.toString());
            try {
                response.sendError(500);
            } catch (Exception e2) {
                //
            }
        }

    }
}