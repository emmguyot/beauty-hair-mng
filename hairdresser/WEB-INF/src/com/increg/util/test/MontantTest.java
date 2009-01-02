/*
 * 
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
package com.increg.util.test;

import com.increg.salon.bean.DeviseBean;
import com.increg.util.Montant;

import junit.framework.Assert;
import junit.framework.TestCase; 

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
    public void testConversionLettre () {
        DeviseBean aDevise = new DeviseBean();
        aDevise.setLIB_DEVISE("euros");
        Assert.assertEquals(m61.toLetters(aDevise), "soixante et un euros");
        Assert.assertEquals(m71.toLetters(aDevise), "soixante et onze euros");
        Assert.assertEquals(m81.toLetters(aDevise), "quatre-vingt un euros");
        Assert.assertEquals(m80.toLetters(aDevise), "quatre-vingts euros");
        Assert.assertEquals(m91.toLetters(aDevise), "quatre-vingt onze euros");
        Assert.assertEquals(m100.toLetters(aDevise), "cent euros");
        Assert.assertEquals(m200.toLetters(aDevise), "deux cents euros");
        Assert.assertEquals(m201.toLetters(aDevise), "deux cent un euros");
        Assert.assertEquals(m1000.toLetters(aDevise), "mille euros");
        Assert.assertEquals(m1001.toLetters(aDevise), "mille un euros");
        Assert.assertEquals(m2000.toLetters(aDevise), "deux mille euros");
        Assert.assertEquals(m2001.toLetters(aDevise), "deux mille un euros");
        Assert.assertEquals(m34_18.toLetters(aDevise), "trente quatre euros dix huit");

    }
    
    /**
     * Affiche toutes les correspondances
     */
    public void listeConversionLettre() {
        DeviseBean aDevise = new DeviseBean();
        aDevise.setLIB_DEVISE("euros");
        for (int i = 1; i < 10000; i++) {
            System.out.println(Integer.toString(i) + " = " + new Montant(Integer.toString(i)).toLetters(aDevise));
        }
    }
            
}
