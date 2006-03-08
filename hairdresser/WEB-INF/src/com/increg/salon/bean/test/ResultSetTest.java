/*
 * Created on 12 avr. 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.salon.bean.test;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.increg.commun.DBSession;

import junit.framework.TestCase;

/**
 * @author Manu
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ResultSetTest extends TestCase {

    /**
     * Constructor for ResultSetTest.
     * @param arg0 .
     */
    public ResultSetTest(String arg0) {
        super(arg0);
    }

    /**
     * Test for java.math.BigDecimal getBigDecimal(int)
     * @throws SQLException en cas de pb
     */
    public void testGetBigDecimalint() throws SQLException {
        String sql = "select PRX_TOT_HT from FACT";
        
        DBSession dbConnect = new DBSession();
        ResultSet rs = dbConnect.doRequest(sql);
        while (rs.next()) {
        	// -1 car bug sur pilote JDBC 
            BigDecimal dec = rs.getBigDecimal(1, -1);
            BigDecimal dec2 = rs.getBigDecimal(1, 2);
            
            assertEquals(dec.setScale(2, BigDecimal.ROUND_HALF_UP), dec2);
        }
    }

}
