package com.increg.util;

/**
 * Insert the type's description here.
 * Creation date: (13/01/2002 10:27:23)
 * @author: Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
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
}
