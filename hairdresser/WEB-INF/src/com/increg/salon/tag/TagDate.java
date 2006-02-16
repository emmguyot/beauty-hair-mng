package com.increg.salon.tag;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.increg.commun.BasicSession;
import com.increg.util.ServletUtil;
import com.increg.util.SimpleDateFormatEG;


/**
 * Tag gérant l'affichage d'un champ date
 * Creation date: 3 mars 2004
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class TagDate extends BodyTagSupport {

    /**
     * Valeur à afficher si date
     */
    protected Calendar valeurDate = null;
    /**
     * valeur inhibant l'affichage
     */
    protected java.lang.String valeurNulle = "";
    /**
     * Format d'affichage des dates
     */
    protected String format = null;
    /**
     * Timezone de référence pour les dates/heures
     */
    protected boolean timezone = false;
    /**
     * Indicateur si heure décimale
     */
    protected boolean heureDec = false;
    /**
     * Indicateur si le calendrier est affiché
     */
    protected boolean calendrier = false;
    /**
     * Nom du champ
     */
    protected String name = "";
    /**
     * Type du champ
     */
    protected String type = "text";
    /**
     * Action sur changement
     */
    protected String onchange = null;
    
	public TagDate() {
		super();
		reset();
	}

    /**
     * Effet de bord : Positionne l'attribut Longueur à la longueure de la chaine affichée
     * Creation date: (24/07/2001 21:51:05)
     * @return int
     */
    public int doAfterBody() {

        BodyContent body = getBodyContent();

        JspWriter out = body.getEnclosingWriter();
        String texte = body.getString();
        String chaineCherchee = "%%";
        int oldPos = 0;
        int pos = texte.indexOf(chaineCherchee);
        String valeur = null;
        int taille = format.length();

        try {
            pageContext.getRequest().setAttribute("Longueur", new Integer(0));
            while (pos != -1) {
                // Ecrit la partie avant
                out.print(texte.substring(oldPos, pos));

                if (valeurDate != null) {
                    // Formatte maintenant la date
                    if (timezone) {
                        SimpleDateFormatEG formatDate = new SimpleDateFormatEG(format);
                        formatDate.setTimeZone(valeurDate.getTimeZone());
                        valeur = formatDate.formatEG(valeurDate.getTime());
                    } else {
                        SimpleDateFormat formatDate = new SimpleDateFormat(format);
                        formatDate.setTimeZone(valeurDate.getTimeZone());
                        valeur = formatDate.format(valeurDate.getTime());
                    }

                    // suppression des heures si à 0
                    int posDate = valeur.indexOf("00:00:00");
                    if (posDate != -1) {
                        // pos - 1 pour supprimer le blanc d'avant
                        valeur = valeur.substring(0, posDate - 1);
                    }
                }

                String aAfficher = "";
                if (!((valeur == null) && (valeurNulle == null)) || ((valeur != null) && (!valeur.equals(valeurNulle)))) {

                    if (heureDec) {
                        try {
                            double valeurDec = Double.parseDouble(valeur);
                            aAfficher = Long.toString((long) valeurDec) + ":";
                            long minute = (long) (((valeurDec * 100) % 100) * 60 / 100);
                            if (minute > 10) {
                                aAfficher += Long.toString(minute);
                            } else {
                                aAfficher += "0" + Long.toString(minute);
                            }
                        } catch (NumberFormatException e) {
                            aAfficher = "###";
                        }
                    }
                    else {
                        aAfficher = ServletUtil.htmlEncode(valeur);
                    }
                    pageContext.getRequest().setAttribute("Longueur", new Integer(valeur.length()));
                }
                if ((type != null) && (type.equals("readonly"))) {
                    out.print("<span class=\"readonly\">" + aAfficher + "</span>"
                        + "<input type=\"hidden\" name=\"" + name + "\" value=\"" + aAfficher + "\"> "
                    );
                }
                else {
                    if (onchange == null) {
                        onchange = "null";
                    }
                    if (calendrier) {
                        out.print("<a href=\"#\" onclick=\"precedent(document.fiche." + name + ",'" + format + "');" + onchange + "\"><img src=\"images/precedent.gif\"></a> ");
                    }
                    String verifFormat = "";
                    if ((format.indexOf("MM") >= 0) && (format.indexOf("HH") >= 0)) {
                        verifFormat = "onchange=\"FormateDateHeure(this)?" + onchange + ":null\"";
                    }
                    else if ((format.indexOf("MM") >= 0) && (format.indexOf("yy") >= 0)) {
                        verifFormat = "onchange=\"FormateDate(this)?" + onchange + ":null\"";
                    }
                    else if (format.indexOf("HH") >= 0) {
                        verifFormat = "onchange=\"FormateHeure(this)?" + onchange + ":null\"";
                    }
                    else {
                        // Pas de vérif
                        verifFormat = "onchange=\"" + onchange + "\"";
                    }
                    out.print("<input type=\"" + type + "\" size=\"" + taille + "\" maxlength=\"" + taille + "\" name=\"" + name + "\" value=\"" + aAfficher + "\" " + verifFormat + ">");
                    if (calendrier) {
                        out.print(" <a href=\"#\" onclick=\"suivant(document.fiche." + name + ",'" + format + "');" + onchange + "\"><img src=\"images/suivant.gif\"></a>"
                            + "<a href=\"#\" onclick=\"calendrier(document.fiche." + name + ",'" + format + "','" + onchange + "')\" id=\"dt_" + name + "_dt\" name=\"dt_" + name + "_dt\"><img src=\"images/calendrier.gif\"></a>"
                            );
                    }
                }

                oldPos = pos + chaineCherchee.length();
                pos = texte.indexOf(chaineCherchee, oldPos);
            }

            out.print(texte.substring(oldPos));
        } catch (IOException e) {
            System.out.println("doAfterBody : " + e.toString());
        }
        return SKIP_BODY;
    }
    /**
     * Insert the method's description here.
     * Creation date: (02/11/2001 15:50:06)
     * @return java.lang.String
     */
    public java.lang.String getFormat() {
        return format;
    }
    /**
     * Insert the method's description here.
     * Creation date: (02/11/2001 16:00:26)
     * @return java.util.Calendar
     */
    public java.util.Calendar getValeurDate() {
        return valeurDate;
    }
    /**
     * Insert the method's description here.
     * Creation date: (24/07/2001 17:13:52)
     * @return java.lang.String
     */
    protected java.lang.String getValeurNulle() {
        return valeurNulle;
    }
    /**
     * Insert the method's description here.
     * Creation date: (02/11/2001 16:13:43)
     * @return boolean
     */
    public boolean isTimezone() {
        return timezone;
    }
    /**
     * Insert the method's description here.
     * Creation date: (26/08/2001 16:17:48)
     * @param newCalendrier String
     */
    public void setCalendrier(String newCalendrier) {
        if (newCalendrier != null) {
            calendrier = newCalendrier.equals("true");
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (26/08/2001 16:17:48)
     * @param newCalendrier boolean
     */
    public void setCalendrier(boolean newCalendrier) {
        calendrier = newCalendrier;
    }
    /**
     * Insert the method's description here.
     * Creation date: (02/11/2001 15:50:06)
     * @param newFormat java.lang.String
     */
    public void setFormat(java.lang.String newFormat) {
        format = newFormat;
    }
    /**
     * Insert the method's description here.
     * Creation date: (02/11/2001 16:13:43)
     * @param newTimezone String
     */
    public void setTimezone(String newTimezone) {
        if (newTimezone != null) {
            timezone = newTimezone.equals("true");
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (02/11/2001 16:13:43)
     * @param newTimezone boolean
     */
    public void setTimezone(boolean newTimezone) {
        timezone = newTimezone;
    }
    /**
     * Insert the method's description here.
     * Creation date: (24/07/2001 17:13:52)
     * @param newValeur java.util.Calendar
     */
    public void setValeurDate(Date newValeur) {
        if (newValeur == null) {
            valeurDate = null;
        } else {
            valeurDate = Calendar.getInstance();
            valeurDate.setTime(newValeur);
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (24/07/2001 17:13:52)
     * @param newValeurNulle java.lang.String
     */
    public void setValeurNulle(java.lang.String newValeurNulle) {
        if (newValeurNulle.equals("null")) {
            valeurNulle = null;
        } else {
            valeurNulle = newValeurNulle;
        }
    }
    /**
     * Insert the method's description here.
     * Creation date: (02/11/2001 16:00:26)
     * @param newValeurDate java.util.Calendar
     */
    public void setValeurDate(java.util.Calendar newValeurDate) {
        valeurDate = newValeurDate;
    }
    /**
     * Gets the heureDec.
     * @return Returns a boolean
     */
    public boolean getHeureDec() {
        return heureDec;
    }

    /**
     * Sets the heureDec.
     * @param heureDec The heureDec to set
     */
    public void setHeureDec(boolean heureDec) {
        this.heureDec = heureDec;
    }

    /**
     * Returns the heureDec.
     * @return boolean
     */
    public boolean isHeureDec() {
        return heureDec;
    }

    /**
     * @see javax.servlet.jsp.tagext.Tag#doEndTag()
     */
    public int doEndTag() throws JspException {
        // RAZ des attributs
        reset();
        return super.doEndTag();
    }

	/**
	 * Reset des attributs 
	 */
	private void reset() {
		valeurNulle = "";
        calendrier = false;
	    if ((pageContext == null) 
	    		|| (pageContext.getSession() == null) 
	    		|| (pageContext.getSession().getAttribute("SalonSession") == null)) {
	        // Perte de la connexion : valeur par défaut
	        format = "dd/MM/yyyy HH:mm:ss";
	    }
	    else {
		    HttpSession mySession = pageContext.getSession();
			BasicSession myBasicSession = (BasicSession) mySession.getAttribute("SalonSession");
	        format = myBasicSession.getMessagesBundle().getString("format.dateDefaut");
	    }
        timezone = false;
        valeurDate = null;
        heureDec = false;
        name = "";
        type = "text";
	}

    /**
     * @return Nom du champ
     */
    public String getName() {
        return name;
    }

    /**
     * @return Type du champ
     */
    public String getType() {
        return type;
    }

    /**
     * @param string Nom du champ
     */
    public void setName(String string) {
        name = string;
    }

    /**
     * @param string Type du champ
     */
    public void setType(String string) {
        type = string;
    }

    /**
     * @param string Action sur changement
     */
    public void setOnchange(String string) {
        onchange = string;
    }
}
