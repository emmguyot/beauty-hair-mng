/*
 * Classe de gestion des nombres décimaux (avec virgule)
 * Copyright (C) 2011 Emmanuel Guyot <See emmguyot on SourceForge> 
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

import java.math.BigDecimal;
import java.math.BigInteger;

public class NombreDecimal extends BigDecimal {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4925013784748294045L;

	/**
	 * Constructor for Montant.
	 * @param val .
	 */
	public NombreDecimal(String val) {
		// Big Decimal ne sait gérer que le point : Force la conversion avant la construction
		super(val.replace(',', '.'));
	}

	/**
	 * Constructor for Montant.
	 * @param val .
	 */
	public NombreDecimal(double val) {
		super(val);
	}

	/**
	 * Constructor for Montant.
	 * @param val .
	 */
	public NombreDecimal(BigInteger val) {
		super(val);
	}

	/**
	 * Constructor for Montant.
	 * @param unscaledVal .
	 * @param scale .
	 */
	public NombreDecimal(BigInteger unscaledVal, int scale) {
		super(unscaledVal, scale);
	}

}
