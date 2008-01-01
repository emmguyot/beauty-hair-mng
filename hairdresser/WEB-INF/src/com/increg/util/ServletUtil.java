/*
 * 
 * Copyright (C) 2002-2008 Emmanuel Guyot <See emmguyot on SourceForge> 
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
package com.increg.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;

public class ServletUtil {
	/**
	 * ServletUtil constructor comment.
	 */
	public ServletUtil() {
		super();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (07/10/2001 19:14:45)
	 * @return java.lang.String
	 * @param aUurl java.lang.String
	 */
	public static String doEscape(String aUrl) {
		char c;
		StringBuffer ret = new StringBuffer("");
	
		if (aUrl == null) return null;
		for (int i=0; i<aUrl.length(); i++) {
		   c = aUrl.charAt(i);
		   if ((c >= '0' && c <= '9') ||
			   (c >= 'a' && c <= 'z') ||
			   (c >= 'A' && c <= 'Z'))
			  ret.append(c);
		   else if (c == '+')
			  ret.append("%2B");
		   else
			  ret.append("%" + Integer.toHexString(c));
		}
	
		return ret.toString();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (24/07/2001 16:26:52)
	 * @return java.lang.String
	 * @param chaine java.lang.String
	 */
	public static String htmlEncode(String chaine) {
		char c;
		StringBuffer ret = new StringBuffer("");
	
		for (int i = 0; i < chaine.length(); i++) {
			c = chaine.charAt(i);
			if (c == '<')
				ret.append("&lt;");
			else if (c == '\'')
				ret.append("&#39;");
			else if (c == '>')
				ret.append("&gt;");
			else if (c == '&')
				ret.append("&amp;");
			else if (c == '"')
				ret.append("&quot;");
	//        else if (c == '\n')
	//        	ret.append("<br>");
			else
				ret.append(c);
		}
	
		return ret.toString();
	}
	/**
	 * @param dtSaisie Date saisie
	 * @param formatDate format de la date pour l'interprétation
	 * @param defaultValue valeur par défaut de la date
	 * @return date à utiliser
	 */
	public static Calendar interpreteDate(String dtSaisie, DateFormat formatDate, Calendar defaultValue) {
		// Valeurs par défaut
	    Calendar date = null;
	    if (dtSaisie == null) {
	        date = defaultValue;
	    }
	    try {
	        if (date == null) {
	            date = Calendar.getInstance(defaultValue.getTimeZone());
	            date.setTime(formatDate.parse(dtSaisie));
	        } 
	    }
	    catch (ParseException e1) {
	        // Valeurs par défaut
	        date = defaultValue;
	    }
		return date;
	}

}
