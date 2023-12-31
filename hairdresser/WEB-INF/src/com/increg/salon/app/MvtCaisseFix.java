/*
 * Test et remets en coh�rence les mouvements de caisse
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
package com.increg.salon.app;

import java.util.Locale;

import com.increg.commun.DBSession;
import com.increg.commun.exception.NoDatabaseException;
import com.increg.salon.bean.ModReglBean;
import com.increg.salon.bean.MvtCaisseBean;

/**
 * @author Manu
 *
 * Test des mouvements de caisse
 * 
 */
public class MvtCaisseFix {

    /**
      *  Connexion � la base de donn�e  
      */
    private DBSession aDBSession;


    /**
     * Constructor for MvtCaisseBeanTest.
     * @throws NoDatabaseException 
     */
    public MvtCaisseFix() throws NoDatabaseException {
    	aDBSession = new DBSession();
    }

    public static void main (java.lang.String[] args) {
        try {
            MvtCaisseFix aMvtCaisseFix = new MvtCaisseFix();
            aMvtCaisseFix.testCheckAndFix();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Test de la v�rification d'une caisse
     * @throws Exception En cas d'erreur programme
     */
    public void testCheckAndFix() throws Exception {
        
        // Par d�faut une caisse est bonne
        MvtCaisseBean.checkAndFix(aDBSession, Long.toString(ModReglBean.MOD_REGL_ESP), "01/01/2001 10:10:10", Locale.getDefault());
        MvtCaisseBean.checkAndFix(aDBSession, Long.toString(ModReglBean.MOD_REGL_CHQ), "01/01/2001 10:10:10", Locale.getDefault());
        MvtCaisseBean.checkAndFix(aDBSession, Long.toString(ModReglBean.MOD_REGL_CB), "01/01/2001 10:10:10", Locale.getDefault());
        
    }

}
