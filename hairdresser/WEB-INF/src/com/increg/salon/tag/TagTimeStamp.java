package com.increg.salon.tag;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import java.io.*;
/**
 * Tag Affichant les infos de date d'un Objet Bean
 * Creation date: (22/08/2001 22:00:08)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class TagTimeStamp extends TagSupport {
    /**
     * bean : Object dont l'information de temps doit être affichée
     */
    protected com.increg.commun.TimeStampBean bean = null;

    /**
     * TagInfo constructor comment.
     */
    public TagTimeStamp() {
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
            java.text.DateFormat formatDate = java.text.DateFormat.getDateTimeInstance(java.text.DateFormat.SHORT, java.text.DateFormat.MEDIUM);
            String dtCreat = formatDate.format(bean.getDT_CREAT().getTime());
            String dtModif = formatDate.format(bean.getDT_MODIF().getTime());

            // Ecrit le début
            out.println("\"Cr&eacute;&eacute; le " + dtCreat + "\n\rModifi&eacute; le " + dtModif + "\"");
        } catch (IOException e) {
            System.out.println("doStartTag : " + e.toString());
        }
        return SKIP_BODY;
    }

    /**
     * Insert the method's description here.
     * Creation date: (22/08/2001 22:01:32)
     * @return com.increg.salon.bean.TimeStampBean
     */
    public com.increg.commun.TimeStampBean getBean() {
        return bean;
    }

    /**
     * Insert the method's description here.
     * Creation date: (22/08/2001 22:01:32)
     * @param newBean com.increg.salon.bean.TimeStampBean
     */
    public void setBean(com.increg.commun.TimeStampBean newBean) {
        bean = newBean;
    }
    
    /**
     * @see javax.servlet.jsp.tagext.Tag#doEndTag()
     */
    public int doEndTag() throws JspException {
        // RAZ des attributs
        bean = null;
        return super.doEndTag();
    }

}
