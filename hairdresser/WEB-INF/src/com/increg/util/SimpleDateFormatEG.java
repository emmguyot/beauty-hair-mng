package com.increg.util;

import java.util.*;
/**
 * Insert the type's description here.
 * Creation date: (28/10/2001 18:04:17)
 * @author: Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
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
