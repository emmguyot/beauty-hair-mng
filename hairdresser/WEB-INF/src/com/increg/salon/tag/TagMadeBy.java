package com.increg.salon.tag;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import java.io.*;

/**
 * Tag Affichant une information de provenance de l'état (InCrEG) pour publicité
 * Creation date: 19 juin 02 01:10:50
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class TagMadeBy extends TagSupport {

    /**
     * TagInfo constructor comment.
     */
    public TagMadeBy() {
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
            out.println("<p style=\"{font-size:7pt; color: #888888}\">Etat réalisé par InCrEG LibertyLook.</p>");
        } catch (IOException e) {
            System.out.println("doStartTag : " + e.toString());
        }
        return SKIP_BODY;
    }
}
