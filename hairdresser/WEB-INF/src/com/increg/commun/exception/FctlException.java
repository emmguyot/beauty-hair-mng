/*
 * Exception fontionnelle
 * Copyright (C) 2001-2009 Emmanuel Guyot <See emmguyot on SourceForge> 
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
package com.increg.commun.exception;

/**
 * Insert the type's description here.
 * Creation date: (28/08/2001 21:36:53)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class FctlException extends Exception {
	/**
	 * Param�tre du message pour substitution
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
     * @param params param�tre � utiliser dans le message
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
