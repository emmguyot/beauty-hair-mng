package com.increg.salon.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.increg.commun.BasicSession;

/**
 * Tag Affichant les infos de date d'un Objet Bean
 * Creation date: (22/08/2001 22:00:08)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class TagMessage extends TagSupport {
    
    /**
     * Session contenant les messages
     */
    protected BasicSession salonSession = null;

    /**
     * TagInfo constructor comment.
     */
    public TagMessage() {
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
            boolean flagEcriture = false;
            
            if ((salonSession.getMessage("Info") != null) && (salonSession.getMessage("Info").length() > 0)) {
                // Ecrit le message d'information
                out.println("<p class=\"info\">" + salonSession.getMessage("Info") + "</p>");
                flagEcriture = true;
            }
            if ((salonSession.getMessage("Erreur") != null) && (salonSession.getMessage("Erreur").length() > 0)) {
                out.println("<p class=\"erreur\">" + salonSession.getMessage("Erreur") + "</p>");
                flagEcriture = true;
            }
            if (!flagEcriture) {
                // Ligne blanche pour éviter que l'écran se décale
                out.println("<p class=\"info\">&nbsp;</p>");
            }
        } catch (IOException e) {
            System.out.println("doStartTag : " + e.toString());
        }
        return SKIP_BODY;
    }

    /**
     * Insert the method's description here.
     * Creation date: (23/08/2001 13:06:27)
     * @return com.increg.salon.bean.SalonSession
     */
    public BasicSession getSalonSession() {
        return salonSession;
    }

    /**
     * Insert the method's description here.
     * Creation date: (23/08/2001 13:06:27)
     * @param newSalonSession com.increg.salon.bean.SalonSession
     */
    public void setSalonSession(BasicSession newSalonSession) {
        salonSession = newSalonSession;
    }
    
    /**
     * @see javax.servlet.jsp.tagext.Tag#doEndTag()
     */
    public int doEndTag() throws JspException {
        // RAZ des attributs
        salonSession = null;
        return super.doEndTag();
    }

}
