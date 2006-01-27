/*
 * 
 * Copyright (C) 2001-2006 Emmanuel Guyot <See emmguyot on SourceForge> 
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

import junit.framework.TestCase;

import org.xml.sax.SAXException;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.HttpUnitOptions;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebLink;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;

/**
 * @author guyot_e
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class AllPagesTest extends TestCase {

	/**
	 * URL de base pour accéder à l'application
	 */
	public static final String urlBase = "http://localhost:8181/salon";
	
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
    public AllPagesTest(String arg0) {
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
     * Accède à chaque page pour vérifier le bon fonctionnement
     * @throws SAXException 
     * @throws IOException 
     *
     */
    public void testPagesOk() throws SAXException, IOException {
    
        WebResponse respons;
        String url;
        
        // La connexion doit être faite
        assertNotNull(session);

        // Connexion
        url = urlBase + "/ident.srv?MOT_PASSE=MDP";
        respons = lectureURL(url);
        WebResponse menu = respons.getSubframeContents("MenuFrame");
        WebLink[] links = menu.getLinks();
        Set lstLink = new HashSet();
        for (int i = 0; i < links.length; i++) {
        	lstLink.add(links[i]);
        }
        Set lstURLComplete = new HashSet();
        Iterator iterLink = lstLink.iterator();
        while (iterLink.hasNext()) {
			WebLink link = (WebLink) iterLink.next();
        	link.mouseOver();
        	url = link.getURLString();
        	if (url.indexOf("javascript") >= 0) {
        		// Ajoute les liens correspondants
        		StringTokenizer splitter = new StringTokenizer(url); 
        		splitter.nextToken("'"); // Ignoré 
        		String urlDeplie = splitter.nextToken("'"); 
                url = urlBase + "/main.srv?menu=1&sub=" + urlDeplie;
                respons = lectureURL(url);
                WebLink[] subLinks = respons.getLinks();
                for (int i = 0; i < subLinks.length; i++) {
                	url = subLinks[i].getURLString();
                	if ((url.indexOf("javascript") == -1) && (url.indexOf("_FicheFact") == -1)) {
                		lstURLComplete.add(subLinks[i].getURLString());
                	}
                }
        	}
        	else if (url.indexOf("_FicheFact") >= 0) {
        		// Pas les liens des clients en cours
        	}
        	else {
        		lstURLComplete.add(link.getURLString());
        	}
		}

        // Nb de liens (hors pliage / dépliage) du menu
        assertEquals(lstURLComplete.size(), 52);
        
        // Chargement de chacun
        iterLink = lstURLComplete.iterator();
        while (iterLink.hasNext()) {
			String link = (String) iterLink.next();
	        url = urlBase + "/" + link;
	        respons = lectureURL(url);
	        
	        String[] frames = respons.getFrameNames();
	        for (int i = 0; i < frames.length; i++) {
	            WebResponse subPage = respons.getSubframeContents(frames[i]);
	            if ((subPage.getURL().toExternalForm().indexOf("actionFic") == -1)
	            		&& (subPage.getURL().toExternalForm().indexOf("actionLst") == -1)) {
		            assertTrue("Titre vide pour " + subPage.getURL().toExternalForm(), (subPage.getTitle() != null) && (subPage.getTitle().length() > 0));
	            }
	            else {
		            assertTrue("Page vide pour " + subPage.getURL().toExternalForm(), subPage.getContentLength() > 10);
	            }
	        }
	        if (frames.length == 0) {
	            assertTrue("Titre vide pour " + link, (respons.getTitle() != null) && (respons.getTitle().length() > 0));
	        }
        }
        
    }

}
