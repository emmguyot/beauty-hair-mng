package com.increg.commun.exception;

/**
 * Insert the type's description here.
 * Creation date: (28/08/2001 21:36:53)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class FctlException extends Exception {
	/**
	 * Paramètre du message pour substitution
	 */
	protected Object[] paramMessage;
	
    /**
     * FctlException constructor comment.
     */
    public FctlException() {
        super();
		paramMessage = null;
    }
    /**
     * FctlException constructor comment.
     * @param s java.lang.String
     */
    public FctlException(String s) {
        super(s);
		paramMessage = null;
    }

	/**
     * FctlException constructor comment.
     * @param s java.lang.String
     * @param params paramètre à utiliser dans le message
     */
    public FctlException(String s, Object[] params) {
        super(s);
		paramMessage = params;
    }
	/**
	 * @return Returns the paramMessage.
	 */
	public Object[] getParamMessage() {
		return paramMessage;
	}
}
