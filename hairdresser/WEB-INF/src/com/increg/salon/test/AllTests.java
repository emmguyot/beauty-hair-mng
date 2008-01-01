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
package com.increg.salon.test;

import com.increg.salon.bean.test.FactBeanTest;
import com.increg.salon.bean.test.MvtCaisseBeanTest;
import com.increg.salon.bean.test.MvtStkBeanTest;
import com.increg.salon.bean.test.PaiementBeanTest;
import com.increg.salon.bean.test.RDVBeanTest;
import com.increg.salon.bean.test.ResultSetTest;
import com.increg.util.test.MontantTest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    /**
     * Construit la série de tests pour le projet
     * @return série
     */
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for com.increg.salon.bean");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(MvtStkBeanTest.class));
        suite.addTest(new TestSuite(MvtCaisseBeanTest.class));
        suite.addTest(new TestSuite(PaiementBeanTest.class));
        suite.addTest(new TestSuite(MontantTest.class));
        suite.addTest(new TestSuite(RDVBeanTest.class));
        suite.addTest(new TestSuite(FactBeanTest.class));
        suite.addTest(new TestSuite(ResultSetTest.class));
        suite.addTest(new TestSuite(PerformanceTest.class));
        suite.addTest(new TestSuite(AllPagesTest.class));
        //$JUnit-END$
        return suite;
    }
}
