package com.increg.salon.tag;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import com.increg.commun.DBSession;
import com.increg.salon.bean.ParamBean;
import com.increg.salon.bean.SalonSession;
import com.increg.salon.bean.SalonSessionImpl;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
/**
 * Tag Affichant les infos de date d'un Objet Bean
 * Creation date: (22/08/2001 22:00:08)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class TagInclude extends TagSupport {

    /**
     * fichier à récupérer
     */
    protected String file = null;
 
    /**
     * Taille du tampon
     */
    protected static final int CHUNK_SIZE = 4096;
    
    /**
     * TagInfo constructor comment.
     */
    public TagInclude() {
        super();
    }
    /**
     * Insert the method's description here.
     * Creation date: (24/07/2001 21:51:05)
     * @return int
     */
    public int doStartTag() {

        JspWriter out = pageContext.getOut();

        try {
            // Teste la connection
            SalonSessionImpl mySalon = (SalonSessionImpl) pageContext.getAttribute("SalonSession", PageContext.SESSION_SCOPE);
            DBSession myDBSession = mySalon.getMyDBSession();

            URL curURL = null;
            ParamBean valUrl = ParamBean.getParamBean(myDBSession, Integer.toString(ParamBean.CD_URL_INFO));
            if (valUrl != null) {
                curURL = new URL(valUrl.getVAL_PARAM() + file);
            }
            HttpURLConnection aCon = null;
            aCon = (HttpURLConnection) curURL.openConnection();

            // Si ok, inclut le fichier
            aCon.setUseCaches(true);
            BufferedInputStream pageStream = new BufferedInputStream(aCon.getInputStream());
            // Lecture
            byte dataBytes[] = new byte[CHUNK_SIZE];
            StringBuffer pageBuf = new StringBuffer();
            int byteRead = 0;
            while (byteRead != -1) {
                byteRead = pageStream.read(dataBytes, 0, CHUNK_SIZE);

                // Stocke dans la chaine
                if (byteRead != -1) {
                    pageBuf.append(new String(dataBytes, 0, byteRead));
                }
            }
            out.println(pageBuf.toString());
        } catch (UnknownHostException e) {
            // Ignore cette exception
            // C'est le rôle de ce tag
        } catch (IOException e) {
            System.out.println("TagInclude : " + e.toString());
        }
        return SKIP_BODY;
    }

    /**
     * Insert the method's description here.
     * Creation date: (23/08/2001 13:06:27)
     * @return String
     */
    public String getFile() {
        return file;
    }

    /**
     * Insert the method's description here.
     * Creation date: (23/08/2001 13:06:27)
     * @param newFile Fichier à inclure
     */
    public void setFile(String newFile) {
        file = newFile;
    }

    /**
     * @see javax.servlet.jsp.tagext.Tag#doEndTag()
     */
    public int doEndTag() throws JspException {
        // RAZ des attributs
        file = null;
        return super.doEndTag();
    }

}
