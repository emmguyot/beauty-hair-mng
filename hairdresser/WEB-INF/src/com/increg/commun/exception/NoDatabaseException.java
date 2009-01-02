/*
 * Exception indiquant que la base n'est pas cohérente
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

import com.increg.commun.BasicSession;

/**
 * Insert the type's description here.
 * Creation date: (30/03/2002 22:02:16)
 * @author: Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class NoDatabaseException extends Exception {
/**
 * NoImplementationException constructor comment.
 */
public NoDatabaseException() {
	super(BasicSession.TAG_I18N + "ReloadNeededException.exception" + BasicSession.TAG_I18N);
}/**
 * NoImplementationException constructor comment.
 * @param s java.lang.String
 */
public NoDatabaseException(String s) {
	super(s);
}}