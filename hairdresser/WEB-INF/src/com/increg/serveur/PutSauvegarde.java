package com.increg.serveur;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.ejs.security.util.Base64Coder;

/**
 * Place un fichier de sauvegarde sur le serveur Creation date: (10/10/2001
 * 22:31:54)
 * 
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
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
                String authorization = Base64Coder.base64Decode(request.getHeader("Authorization").substring(6));
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

                fileOut.write(Base64Coder.base64Decode(dataString.getBytes()));
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