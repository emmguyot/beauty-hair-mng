package com.increg.commun.exception;
/**
 * Insert the type's description here.
 * Creation date: (30/03/2002 22:02:16)
 * @author: Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class ReloadNeededException extends Exception {
/**
 * NoImplementationException constructor comment.
 */
public ReloadNeededException() {
	super("La base doit être rechargée, car elle n'est plus cohérente.");
}/**
 * NoImplementationException constructor comment.
 * @param s java.lang.String
 */
public ReloadNeededException(String s) {
	super(s);
}}