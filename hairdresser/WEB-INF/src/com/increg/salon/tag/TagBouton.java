package com.increg.salon.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Tag gérant l'affichage d'un bouton sous forme d'image
 * Creation date: Feb 24, 2005
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class TagBouton extends TagSupport {

    /**
     * Image par défaut
     */
    protected String img = "";
    /**
     * Image quand curseur dessus
     */
    protected String imgOn = "";
    /**
     * URL sur clic
     */
    protected String url = "";
    /**
     * Texte alternatif
     */
    protected String alt = "";
    /**
     * Frame cible du lien
     */
    protected String target = "";

    /**
     * Effet de bord : Positionne l'attribut Longueur à la longueure de la chaine affichée
     * Creation date: (24/07/2001 21:51:05)
     * @return int
     */
    public int doStartTag() {

        JspWriter out = pageContext.getOut();

        try {
            String nomImage = "";
            
            // Produit le nom de l'image à partir du nom de fichier
            int lastSep = img.lastIndexOf("/");
            lastSep++;
            int extPoint = img.lastIndexOf(".");
            if (extPoint == -1) {
                nomImage = img.substring(lastSep);
            }
            else {
                nomImage = img.substring(lastSep, extPoint);
            }
            out.print("<a class=\"nohover\" href=\"");
            out.print(url);
            out.print("\" title=\"");
            out.print(alt);
            out.print("\" ");
            if (target.length() > 0) {
                out.print("target=\"");
                out.print(target);
                out.print("\"");
            }
            if (imgOn.length() > 0) {
                out.print("onMouseOver=\"document.");
                out.print(nomImage);
                out.print(".src='");
                out.print(imgOn);
                out.print("'\" onMouseOut=\"document.");
                out.print(nomImage);
                out.print(".src='");
                out.print(img);
                out.print("'\"");
            }
            out.print("><img name=\"");
            out.print(nomImage);
            out.print("\" src=\"");
            out.print(img);
            out.print("\" style=\"{vertical-align:middle; border:0}\" alt=\"");
            out.print(alt);
            out.print("\"></a>");
        } catch (IOException e) {
            System.out.println("TagBouton::doStartTag : " + e.toString());
        }
        return SKIP_BODY;
    }

    /**
     * @see javax.servlet.jsp.tagext.Tag#doEndTag()
     */
    public int doEndTag() throws JspException {
        // RAZ des attributs
        img = "";
        imgOn = "";
        url = "";
        return super.doEndTag();
    }

    /**
     * @return Image par défaut
     */
    public String getImg() {
        return img;
    }

    /**
     * @return Image quand curseur dessus
     */
    public String getImgOn() {
        return imgOn;
    }

    /**
     * @return URL sur clic
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param string Image par défaut
     */
    public void setImg(String string) {
        img = string;
    }

    /**
     * @param string Image quand curseur dessus
     */
    public void setImgOn(String string) {
        imgOn = string;
    }

    /**
     * @param string URL sur clic
     */
    public void setUrl(String string) {
        url = string;
    }

    /**
     * @return Texte alternatif
     */
    public String getAlt() {
        return alt;
    }

    /**
     * @param string Texte alternatif
     */
    public void setAlt(String string) {
        alt = string;
    }

    /**
     * @return Frame cible du lien
     */
    public String getTarget() {
        return target;
    }

    /**
     * @param string Frame cible du lien
     */
    public void setTarget(String string) {
        target = string;
    }

}
