package com.increg.commun;

import java.util.HashMap;;

/**
 * Bean Session incluant la méchanique de base d'une session
 * Creation date: (08/07/2001 16:13:56)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class BasicSession {

    /**
     * Liste des messages : Clé / Message associé
     */
    private java.util.HashMap messages;
    /**
     * BasicSession constructor comment.
     */
    public BasicSession() {
        super();
        setMessages(new HashMap());
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
}
