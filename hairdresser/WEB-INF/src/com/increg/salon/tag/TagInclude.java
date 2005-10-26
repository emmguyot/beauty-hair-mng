package com.increg.salon.tag;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.HashMap;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import com.increg.commun.DBSession;
import com.increg.salon.bean.ParamBean;
import com.increg.salon.bean.SalonSessionImpl;
/**
 * Tag Affichant les infos de date d'un Objet Bean
 * Creation date: (22/08/2001 22:00:08)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class TagInclude extends TagSupport {

    /**
     * Cache des fichiers � charger 
     */
    protected static HashMap cache = new HashMap();

    /**
     * Date de derni�re mise � jour du cache
     */
    protected static Calendar dateRefresh = Calendar.getInstance();
    
    /**
     * fichier � r�cup�rer
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
            
            StringBuffer pageBuf = new StringBuffer();

            // V�rification du cache
            Calendar dateLimite = Calendar.getInstance();
            dateLimite.add(Calendar.HOUR, -5);
            if ((dateRefresh.after(dateLimite)) && (cache.get(curURL.toString()) != null)) {
            	// Utilisation du cache
            	pageBuf.append(cache.get(curURL.toString()));
            }
            else {
	            HttpURLConnection aCon = null;
	            aCon = (HttpURLConnection) curURL.openConnection();
	
	            // Si ok, inclut le fichier
	            aCon.setUseCaches(true);
	            BufferedInputStream pageStream = new BufferedInputStream(aCon.getInputStream());
	            // Lecture
	            byte dataBytes[] = new byte[CHUNK_SIZE];
	            int byteRead = 0;
	            while (byteRead != -1) {
	                byteRead = pageStream.read(dataBytes, 0, CHUNK_SIZE);
	
	                // Stocke dans la chaine
	                if (byteRead != -1) {
	                    pageBuf.append(new String(dataBytes, 0, byteRead));
	                }
	            }
	            
	            // Stocke dans le cache
	            cache.put(curURL.toString(), pageBuf);
	            dateRefresh = Calendar.getInstance();
            }
            out.println(pageBuf.toString());
        } catch (UnknownHostException e) {
            // Ignore cette exception
            // C'est le r�le de ce tag
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
     * @param newFile Fichier � inclure
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
