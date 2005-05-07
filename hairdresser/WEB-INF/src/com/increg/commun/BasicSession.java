/*
 * Bean Session incluant la méchanique de base d'une session
 * Copyright (C) 2001-2005 Emmanuel Guyot <See emmguyot on SourceForge>
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
package com.increg.commun;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import com.increg.commun.exception.FctlException;

/**
 * Bean Session incluant la méchanique de base d'une session
 * Creation date: (08/07/2001 16:13:56)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class BasicSession {

	/**
	 * Tag indiquant que le message n'est qu'une référence à un message traduit
	 */
	public static final String TAG_I18N = "#I18N#";
	
    /**
     * Liste des messages : Clé / Message associé
     */
    private java.util.HashMap messages;

    /**
     * Langue de l'utilisateur
     */
    protected Locale langue;

	/**
	 * Messages de la langue
	 */
	protected ResourceBundle messagesBundle;

	/**
     * BasicSession constructor comment.
     */
    public BasicSession() {
        super();
        setMessages(new HashMap());
		// Bundle par défaut
		messagesBundle = ResourceBundle.getBundle("messages");
    }
    /**
     * Insert the method's description here.
     * Creation date: (08/07/2001 16:18:29)
     * @return java.lang.String
     * @param key java.lang.String
     */
    public String getMessage(String key) {
        return (String) messages.get(key);
    }
    /**
     * Insert the method's description here.
     * Creation date: (08/07/2001 16:16:00)
     * @return java.util.HashMap
     */
    protected java.util.HashMap getMessages() {
        return messages;
    }
    /**
     * Insert the method's description here.
     * Creation date: (08/07/2001 16:20:31)
     * @param key java.lang.String
     * @param value java.lang.String
     */
    public void setMessage(String key, String value) {
		
		// La valeur est literale ou à internationnaliser ?
		if ((value != null) && (value.indexOf(TAG_I18N) >= 0)) {
			// A internationaliser la clé est encadrée par 1 tag de chaque coté
			int posDebut = value.indexOf(TAG_I18N);
			String debut = value.substring(0, posDebut);
			int posFin = value.indexOf(TAG_I18N, posDebut + 1);
			String cle = value.substring(posDebut + TAG_I18N.length(), posFin);
			String fin = value.substring(posFin + TAG_I18N.length());
			
			value = debut + messagesBundle.getString(cle) + fin;
		}
        messages.put(key, value);
    }
    /**
     * Insert the method's description here.
     * Creation 7 mai 2005 10:45:52
     * @param key java.lang.String
     * @param except Exception qui doit être affichée comme message
     */
    public void setMessage(String key, Exception except) {
		
		String value = null;
		
		// La valeur est literale ou à internationnaliser ?
		if (except != null) {
			value = except.toString();
		}
		
		if ((value != null) && (value.indexOf(TAG_I18N) >= 0)) {
			// A internationaliser la clé est encadrée par 1 tag de chaque coté
			int posDebut = value.indexOf(TAG_I18N);
			String debut = value.substring(0, posDebut);
			int posFin = value.indexOf(TAG_I18N, posDebut + 1);
			String cle = value.substring(posDebut + TAG_I18N.length(), posFin);
			String fin = value.substring(posFin + TAG_I18N.length());
		
			if (except instanceof FctlException) {
				FctlException theFctlExcept = (FctlException) except;
				// Ajoute les paramètres dynamiques
				value = debut + MessageFormat.format(messagesBundle.getString(cle), theFctlExcept.getParamMessage()) + fin;
			}
			else {
				value = debut + messagesBundle.getString(cle) + fin;
			}
		}
        messages.put(key, value);
    }
    /**
     * Insert the method's description here.
     * Creation date: (08/07/2001 16:16:00)
     * @param newMessages java.util.HashMap
     */
    protected void setMessages(HashMap newMessages) {
        messages = newMessages;
    }

	/**
     * @return Langue de l'utilisateur
     */
    public Locale getLangue() {
        return langue;
    }

    /**
     * @param locale Langue de l'utilisateur
     */
    public void setLangue(Locale locale) {
		ResourceBundle res = ResourceBundle.getBundle("messages", locale);
		
		if (res != null) {
			messagesBundle = res;
	        langue = locale;
		}
    }

	/**
	 * @return Returns the messages.
	 */
	public ResourceBundle getMessagesBundle() {
		return messagesBundle;
	}

}
