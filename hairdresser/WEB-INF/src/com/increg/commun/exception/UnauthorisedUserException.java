package com.increg.commun.exception;

/**
 * Insert the type's description here.
 * Creation date: (28/08/2001 21:36:53)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class UnauthorisedUserException extends Exception {
    /**
     * FctlException constructor comment.
     */
    public UnauthorisedUserException() {
        super();
    }
    /**
     * FctlException constructor comment.
     * @param s java.lang.String
     */
    public UnauthorisedUserException(String s) {
        super(s);
    }
}
