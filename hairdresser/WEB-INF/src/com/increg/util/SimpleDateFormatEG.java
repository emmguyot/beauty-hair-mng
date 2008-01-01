/*
 * 
 * Copyright (C) 2001-2008 Emmanuel Guyot <See emmguyot on SourceForge> 
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

import java.util.Calendar;

public class SimpleDateFormatEG extends java.text.SimpleDateFormat {
	private static final int millisPerHour = 60 * 60 * 1000;
	private static final int millisPerMinute = 60 * 1000;

/**
 * SimpleDateFormatEG constructor comment.
 */
public SimpleDateFormatEG() {
	super();
}
/**
 * SimpleDateFormatEG constructor comment.
 * @param pattern java.lang.String
 */
public SimpleDateFormatEG(String pattern) {
	super(pattern);
}
/**
 * SimpleDateFormatEG constructor comment.
 * @param pattern java.lang.String
 * @param formatData java.text.DateFormatSymbols
 */
public SimpleDateFormatEG(String pattern, java.text.DateFormatSymbols formatData) {
	super(pattern, formatData);
}
/**
 * SimpleDateFormatEG constructor comment.
 * @param pattern java.lang.String
 * @param loc java.util.Locale
 */
public SimpleDateFormatEG(String pattern, java.util.Locale loc) {
	super(pattern, loc);
}
	// Pad the shorter numbers up to maxCount digits.
	private String zeroPaddingNumber(long value, int minDigits, int maxDigits)
	{
		numberFormat.setMinimumIntegerDigits(minDigits);
		numberFormat.setMaximumIntegerDigits(maxDigits);
		return numberFormat.format(value);
	}
	/**
 * Insert the method's description here.
 * Creation date: (28/10/2001 18:07:23)
 * @return java.lang.String
 * @param date java.util.Date
 */
public String formatEG(java.util.Date date) {
	String orig = format(date);

	int value = calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET);

	if (value < 0)
	{
		orig = orig + "-";
		value = -value; // suppress the '-' sign for text display.
	}
	else {
		orig = orig + "+";
	}
	orig = orig +  zeroPaddingNumber((int)(value/millisPerHour), 2, 2) +
					  zeroPaddingNumber(
										(int)((value%millisPerHour)/millisPerMinute), 2, 2);

	return orig;
}
}
