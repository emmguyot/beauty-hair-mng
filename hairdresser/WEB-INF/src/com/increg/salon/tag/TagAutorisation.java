package com.increg.salon.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.*;

import com.increg.salon.bean.IdentBean;
import com.increg.salon.bean.SalonSession;

/**
 * Tag affichant le corps en fonction des droits de l'utilsateur
 * Creation date: 1 juin 02 18:25:28
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class TagAutorisation extends TagSupport {
	/**
	 * Entite : Entité pour laquelle les droits sont demandés
	 */
	protected String entite = null;
    /**
     * action : Action pour laquelle les droits sont demandés sur l'entité en question
     */
    protected String action = "+";
    
	/**
	 * TagInfo constructor comment.
	 */
	public TagAutorisation() {
		super();
	}
    
	/**
	 * Insert the method's description here.
	 * Creation date: (24/07/2001 21:51:05)
	 * @return int
	 */
	public int doStartTag() {
	
	    javax.servlet.http.HttpSession mySession = pageContext.getSession();
	    if ((mySession == null) || (mySession.getAttribute("SalonSession") == null) || (((SalonSession) mySession.getAttribute("SalonSession")).getMyIdent() == null)) {
	        // Perte de la connexion : Rien à faire
	        return SKIP_BODY;
	    }
	    
	    IdentBean aIdent = ((SalonSession) mySession.getAttribute("SalonSession")).getMyIdent();
	    if (aIdent.getDroit(entite, action)) {
	        return EVAL_BODY_INCLUDE;
	    }
		return SKIP_BODY;
	}

	/**
	 * Gets the entite.
	 * @return Returns a String
	 */
	public String getEntite() {
		return entite;
	}

	/**
	 * Sets the entite.
	 * @param entite The entite to set
	 */
	public void setEntite(String entite) {
		this.entite = entite;
	}

	/**
	 * Gets the action.
	 * @return Returns a String
	 */
	public String getAction() {
		return action;
	}

	/**
	 * Sets the action.
	 * @param action The action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

    /**
     * @see javax.servlet.jsp.tagext.Tag#doEndTag()
     */
    public int doEndTag() throws JspException {
        // RAZ des attributs
        action = "+";
        entite = null;        
        return super.doEndTag();
    }

}
