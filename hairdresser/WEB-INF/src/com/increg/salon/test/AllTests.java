package com.increg.salon.test;

import com.increg.salon.bean.test.FactBeanTest;
import com.increg.salon.bean.test.MvtCaisseBeanTest;
import com.increg.salon.bean.test.MvtStkBeanTest;
import com.increg.salon.bean.test.PaiementBeanTest;
import com.increg.salon.bean.test.RDVBeanTest;
import com.increg.util.test.MontantTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Manu
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
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
        //$JUnit-END$
        return suite;
    }
}
