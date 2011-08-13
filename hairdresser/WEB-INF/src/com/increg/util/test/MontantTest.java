/*
 * 
 * Copyright (C) 2001-2011 Emmanuel Guyot <See emmguyot on SourceForge> 
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
package com.increg.util.test;

import java.math.BigDecimal;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.increg.util.Montant;

public class MontantTest extends TestCase {

    private Montant m61;
    private Montant m71;
    private Montant m81;
    private Montant m80;
    private Montant m91;
    private Montant m100;
    private Montant m200;
    private Montant m201;
    private Montant m1000;
    private Montant m1001;
    private Montant m2000;
    private Montant m2001;
    private Montant m34_18;
    
	/**
	 * Constructor for MontantTest.
	 * @param arg0 s   
	 */
	public MontantTest(String arg0) {
		super(arg0);
	}

	/**
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
        m61 = new Montant("61");
        m71 = new Montant("71");
        m81 = new Montant("81");
        m80 = new Montant("80");
        m91 = new Montant("91");
        m100 = new Montant("100");
        m200 = new Montant("200");
        m201 = new Montant("201");
        m1000 = new Montant("1000");
        m1001 = new Montant("1001");
        m2000 = new Montant("2000");
        m2001 = new Montant("2001");
        m34_18 = new Montant("34.18");
	}

    /**
     * Test la bonne conversion
     */
    public void testVirgule () {
    
    	Montant m = new Montant("10,21");
    	Assert.assertTrue("Constructeur avec virgule erroné", m.equals(new BigDecimal("10.21")));
    }
    
    /**
     * Test la bonne conversion
     */
    public void testConversionLettre () {
        Assert.assertEquals(m61.toLetters("euros"), "soixante et un euros");
        Assert.assertEquals(m71.toLetters("euros"), "soixante et onze euros");
        Assert.assertEquals(m81.toLetters("euros"), "quatre-vingt un euros");
        Assert.assertEquals(m80.toLetters("euros"), "quatre-vingts euros");
        Assert.assertEquals(m91.toLetters("euros"), "quatre-vingt onze euros");
        Assert.assertEquals(m100.toLetters("euros"), "cent euros");
        Assert.assertEquals(m200.toLetters("euros"), "deux cents euros");
        Assert.assertEquals(m201.toLetters("euros"), "deux cent un euros");
        Assert.assertEquals(m1000.toLetters("euros"), "mille euros");
        Assert.assertEquals(m1001.toLetters("euros"), "mille un euros");
        Assert.assertEquals(m2000.toLetters("euros"), "deux mille euros");
        Assert.assertEquals(m2001.toLetters("euros"), "deux mille un euros");
        Assert.assertEquals(m34_18.toLetters("euros"), "trente quatre euros dix huit");

    }
    
    /**
     * Affiche toutes les correspondances
     */
    public void listeConversionLettre() {
        for (int i = 1; i < 10000; i++) {
            System.out.println(Integer.toString(i) + " = " + new Montant(Integer.toString(i)).toLetters("euros"));
        }
    }
            
}
