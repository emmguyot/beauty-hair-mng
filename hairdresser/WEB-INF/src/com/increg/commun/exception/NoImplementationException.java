package com.increg.commun.exception;
/**
 * Insert the type's description here.
 * Creation date: (30/03/2002 22:02:16)
 * @author: Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class NoImplementationException extends Exception {
/**
 * NoImplementationException constructor comment.
 */
public NoImplementationException() {
	super("Traitement impossible : Cette fonctionnalité n'est pas encore implémentée");
}/**
 * NoImplementationException constructor comment.
 * @param s java.lang.String
 */
public NoImplementationException(String s) {
	super(s);
}}