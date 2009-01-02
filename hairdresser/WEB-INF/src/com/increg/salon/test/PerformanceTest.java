/*
 * 
 * Copyright (C) 2004-2009 Emmanuel Guyot <See emmguyot on SourceForge> 
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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.Calendar;

import org.xml.sax.SAXException;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.HttpUnitOptions;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebLink;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;

import junit.framework.TestCase;

public class PerformanceTest extends TestCase {

	/**
	 * URL de base pour accéder à l'application
	 */
	public static final String urlBase = "http://localhost:8181/salon";
	
    /**
     * Ratio entre des milli secondes et des secondes 
     */
    public static final double RATION_MS_S = 1000.0;

    /**
     * Session J2EE
     */
    protected String session;

    /**
     * Marqueur temporel du début du test
     */
    protected Calendar debut;
     
    /**
     * Constructor for PerformanceTest.
     * @param arg0 ?
     */
    public PerformanceTest(String arg0) {
        super(arg0);
    }

    /**
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        
        debut = Calendar.getInstance();
        
        // Ouvre la connexion à l'application
        WebResponse respons;
        
        // Initialisation et contrôle de la base
        String url = urlBase + "/initPortail.srv";
        respons = lectureURL(url);
        session = respons.getNewCookieValue("JSESSIONID");
        
        // Choix de la base ?
        WebForm form = respons.getFormWithName("base");
        if (form != null) {
        	respons = form.submit();
        }
        
        // Connexion
        url = urlBase + "/ident.srv?MOT_PASSE=MDP";
        respons = lectureURL(url);
    }

    /**
     * Lecture d'une URL
     * @param url URL à lire
     * @return réponse obtenue
     */
    protected WebResponse lectureURL(String url) {
        // Pas d'analyse de script
        HttpUnitOptions.setScriptingEnabled(false);
        WebConversation wc = new WebConversation();
        if (session != null) {
            wc.addCookie("JSESSIONID", session);
//            int pos = url.indexOf('?');
//            if (pos != -1) {
//                url = url.substring(0, pos) + ";jsessionid=" + session + url.substring(pos);
//            }
//            else {
//                url = url + ";jsessionid=" + session;
//            }
        }
        WebRequest req = new GetMethodWebRequest(url);
        WebResponse resp = null;
        try {
            resp = wc.getResponse(req);
            assertEquals(resp.getResponseCode(), HttpURLConnection.HTTP_OK);
        }
        catch (MalformedURLException e) {
            // Bug programme
            fail("Mauvaise url :" + url);
        }
        catch (IOException e) {
            fail("Exception à la lecture de " + url + " : " + e);
        }
        catch (SAXException e) {
            fail("Mauvais format de réponse de " + url + " : " + e);
        }
        catch (Exception e) {
            fail("Exception général de " + url + " : " +  e);
        }
        return resp;
    }
    
    /**
     * Effectue une série de test pour voir les performances
     *
     */
    public void testPerformance() {
        
        System.out.println("testPerformance: Ne pas lancer en debug, à lancer 2 fois ? ");
        Calendar fin = Calendar.getInstance();
        long elapsed = fin.getTime().getTime() - debut.getTime().getTime();
        System.out.println("Etape intermédiaire 1 (s) : " + elapsed / RATION_MS_S);
        assertTrue(elapsed < 3000);

        // La connexion doit être faite
        assertNotNull(session);
                
        WebResponse respons;
        
        // Initialisation et contrôle de la base
        String url = urlBase + "/rechCli.srv";
        respons = lectureURL(url);
        
        for (char c = 'B'; c <= 'Z'; c++) {
            url = urlBase + "/rechCli.srv?premLettre=" + c;
            respons = lectureURL(url);
        }
        
        fin = Calendar.getInstance();
        elapsed = fin.getTime().getTime() - debut.getTime().getTime();
        System.out.println("Etape intermédiaire 2 (s) : " + elapsed / RATION_MS_S);
        assertTrue(elapsed < 10000);

        url = urlBase + "/addCli.srv?CD_CLI=652";
        respons = lectureURL(url);
        
        WebLink lienFacture = null;
        try {
            lienFacture = respons.getLinkWith("Mlle Beaulieu");
        }
        catch (SAXException e) {
            fail("Menu illisible : " + e.toString());
        }
        
        fin = Calendar.getInstance();
        elapsed = fin.getTime().getTime() - debut.getTime().getTime();
        System.out.println("Etape intermédiaire 3 (s) : " + elapsed / RATION_MS_S);
        assertTrue(elapsed < 10000);

        assertNotNull(lienFacture);
        url = urlBase + "/" + lienFacture.getURLString();
        respons = lectureURL(url);
        
        fin = Calendar.getInstance();
        elapsed = fin.getTime().getTime() - debut.getTime().getTime();
        System.out.println("Etape intermédiaire 4 (s) : " + elapsed / RATION_MS_S);
        assertTrue(elapsed < 11000);

        url = urlBase + "/addCli.srv?CD_CLI=93";
        respons = lectureURL(url);
        
        lienFacture = null;
        try {
            lienFacture = respons.getLinkWith("Mme Arnaud Martine");
        }
        catch (SAXException e) {
            fail("Menu illisible : " + e.toString());
        }
        
        fin = Calendar.getInstance();
        elapsed = fin.getTime().getTime() - debut.getTime().getTime();
        System.out.println("Etape intermédiaire 5 (s) : " + elapsed / RATION_MS_S);
        assertTrue(elapsed < 16000);

        assertNotNull(lienFacture);
        url = urlBase + "/" + lienFacture.getURLString();
        respons = lectureURL(url);

        fin = Calendar.getInstance();
        elapsed = fin.getTime().getTime() - debut.getTime().getTime();
        System.out.println("Résultat test de performance (s) : " + elapsed / RATION_MS_S);
        assertTrue(elapsed < 20000);
    }

}
