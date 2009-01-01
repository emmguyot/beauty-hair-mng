/*
 * Vérification comportement BigDecimal
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
import com.increg.commun.exception.NoDatabaseException;

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
    public void testGetBigDecimalint() throws SQLException, NoDatabaseException {
        String sql = "select PRX_TOT_HT from FACT where PRX_TOT_HT is not null";
        
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
