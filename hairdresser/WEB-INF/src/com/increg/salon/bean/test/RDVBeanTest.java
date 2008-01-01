/*
 * Test des RDV
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
package com.increg.salon.bean.test;

import java.sql.ResultSet;
import java.util.Calendar;

import com.increg.commun.DBSession;
import com.increg.salon.bean.RDVBean;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author Manu
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class RDVBeanTest extends TestCase {

    /**
     * Connexion à la base de donnée  
     */
    private DBSession aDBSession = new DBSession("config");

    /**
     * Constructor for PaiementBeanTest.
     * @param arg0 .
     */
    public RDVBeanTest(String arg0) {
        super(arg0);
    }

    /**
     * Vérification du test de chevauchement des RDV
     * @throws Exception en cas d'erreur programme
     */
    public void testChevauchement() throws Exception {

        RDVBean aRDV = null;
        RDVBean aRDV2 = null;
        RDVBean aRDV3 = null;
        RDVBean aRDV4 = null;
        
        Calendar debut = Calendar.getInstance();
        try {
            // Recherche un client au hazard
            String reqCli = "select min(CD_CLI) from CLI";
            ResultSet rs = aDBSession.doRequest(reqCli);
            long cdCli = 0;
            if (rs.next()) { cdCli = rs.getLong(1); }
            rs.close();

            aRDV = new RDVBean();
            aRDV.setCD_CLI(cdCli);
            aRDV.setCD_COLLAB(1);
            aRDV.setCOMM("Super coupe compliquée");
            aRDV.setDT_DEBUT(debut);
            aRDV.setDUREE("15");
            aRDV.create(aDBSession);

            debut = Calendar.getInstance();
            debut.add(Calendar.MINUTE, 1);
            
            aRDV2 = new RDVBean();
            aRDV2.setCD_CLI(cdCli);
            aRDV2.setCD_COLLAB(1);
            aRDV2.setCOMM("Super coupe compliquée - Conflit");
            aRDV2.setDT_DEBUT(debut);
            aRDV2.setDUREE("15");
            
            Assert.assertFalse(aRDV2.verifChevauchement(aDBSession, false));
            
            aRDV2.create(aDBSession);
            Assert.assertFalse(aRDV2.verifChevauchement(aDBSession, true));
            
            debut = Calendar.getInstance();
            debut.set(Calendar.SECOND, debut.get(Calendar.SECOND) + 1); // Décallage pour éviter les doublons de clés
            aRDV3 = new RDVBean();
            aRDV3.setCD_CLI(cdCli);
            aRDV3.setCD_COLLAB(2);
            aRDV3.setCOMM("Super coupe compliquée - Ok");
            aRDV3.setDT_DEBUT(debut);
            aRDV3.setDUREE("15");
            
            Assert.assertTrue(aRDV3.verifChevauchement(aDBSession, false));
            
            aRDV3.create(aDBSession);
            
            aRDV4 = new RDVBean();
            aRDV4.setCD_CLI(cdCli);
            aRDV4.setCD_COLLAB(2);
            aRDV4.setCOMM("Super coupe compliquée - Non Conflit");
            debut = Calendar.getInstance();
            debut.add(Calendar.MINUTE, 16);
            aRDV4.setDT_DEBUT(debut);
            aRDV4.setDUREE("15");
            
            Assert.assertTrue(aRDV4.verifChevauchement(aDBSession, false));
                        
        }
        catch (Exception e) {
            System.out.println("testChevauchement : " + e.toString());
            throw (e);
        }
        finally {
            if (aRDV != null) { aRDV.delete(aDBSession); }
            if (aRDV2 != null) { aRDV2.delete(aDBSession); }
            if (aRDV3 != null) { aRDV3.delete(aDBSession); }
        }
        
        
    }

}
