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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
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
 * Test l'ensemble des pages en les parcourant (type webbot)
 */
public class AllPagesTest extends TestCase {

	/**
	 * URL de base pour accéder à l'application
	 */
	public static final String urlBase = "http://localhost:8181/";
	public static final String contexte = "salon";
	
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
        String url = urlBase + contexte + "/initPortail.srv";
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
            // Pas de plantage
            assertEquals(resp.getResponseCode(), HttpURLConnection.HTTP_OK);
            
            // Pas d'erreur applicative
            assertTrue("Erreur dans la page : " + resp.getText(), resp.getText().indexOf("<p class=\"erreur\">") == -1); 
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
        url = urlBase + contexte + "/ident.srv?MOT_PASSE=MDP";
        respons = lectureURL(url);
        
        // Interprete le menu
        WebResponse menu = respons.getSubframeContents("MenuFrame");
        WebLink[] links = menu.getLinks();
        Set lstLink = new HashSet();
        for (int i = 0; i < links.length; i++) {
        	lstLink.add(links[i]);
        }
        Map mapURLPhase2 = new HashMap();
    	Set ignore = new HashSet();
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
                url = urlBase +contexte + "/main.srv?menu=1&sub=" + urlDeplie;
                respons = lectureURL(url);
                WebLink[] subLinks = respons.getLinks();
                for (int i = 0; i < subLinks.length; i++) {
                	url = subLinks[i].getURLString();
                	if ((url.indexOf("javascript") == -1) && (url.indexOf("_FicheFact") == -1)) {
                		mapURLPhase2.put(epureURL(url), url);
                    	ignore.add(epureURL(url));
                	}
                }
        	}
        	else if (url.indexOf("_FicheFact") >= 0) {
        		// Pas les liens des clients en cours
        	}
        	else {
        		mapURLPhase2.put(epureURL(link.getURLString()), link.getURLString());
            	ignore.add(epureURL(link.getURLString()));
        	}
		}

        // Nb de liens (hors pliage / dépliage) du menu
        assertEquals(mapURLPhase2.size(), 52);

        
        
        int passe = 1;
        ignore.addAll(mapURLPhase2.keySet());
        while (!mapURLPhase2.isEmpty()) {
        	
        	System.out.println("Passe #" + passe++ + "(" + mapURLPhase2.size() + ")");
	        // Chargement de chacun
        	
	        Map lstURLPhase3 = loadTestAndExtractNew(mapURLPhase2.values(), ignore);
	        
	        ignore.addAll(mapURLPhase2.keySet());
	        mapURLPhase2 = lstURLPhase3;
        }
        
    }

    /**
     * Epure les paramètre de l'URL pour éviter de multiplier les URL à tester
     * @param url Url avec paramètre
     * @return URL sans paramètre
     */
	private String epureURL(String url) {
		
		String res = url;
		int pos = res.indexOf('=');
		while (pos != -1) {
			int pos2 = pos + 1;
			while ((pos2 < res.length()) && (Character.isDigit(res.charAt(pos2)))) {
				pos2++;
			}
			// Coupe
			res = res.substring(0, pos + 1) + res.substring(pos2); 
			
			pos = res.indexOf('=', pos + 1);
		}
		return res;
	}

	/**
	 * @param lstURL Url à charger et à analyser
	 * @param lstExcl URL déjà vu à ne pas charger
	 * @return
	 * @throws SAXException
	 */
	private Map loadTestAndExtractNew(Collection lstURL, Set lstExcl) throws SAXException {
		WebResponse respons;
		String url;
		Iterator iterLink;
		Map lstURLPhase3 = new HashMap();
        iterLink = lstURL.iterator();
        while (iterLink.hasNext()) {
			String link = (String) iterLink.next();
			
			// Ignore les URL externes
			if ((link.indexOf("http:") != 0) && (link.indexOf("mailto:") != 0)) {
				// Url Absolue ou relative
				if (link.charAt(0) == '/') {
			        url = urlBase + link;
				}
				else if (link.substring(0, 2).equals("..")) {
					// Cas des pages HTML traduites qui redirigent sur le tronc commun
			        url = urlBase + contexte + "/" + link.substring(3);
				}
				else {
			        url = urlBase + contexte + "/" + link;
				}
		        respons = lectureURL(url);
		        
		        String[] frames = respons.getFrameNames();
		        for (int i = 0; i < frames.length; i++) {
		            WebResponse subPage = respons.getSubframeContents(frames[i]);
		            if (subPage.getURL() != null) {
			            if ((subPage.getURL().toExternalForm().indexOf("actionFic") == -1)
			            		&& (subPage.getURL().toExternalForm().indexOf("actionLst") == -1)) {
				            assertTrue("Titre vide pour " + subPage.getURL().toExternalForm(), (subPage.getTitle() != null) && (subPage.getTitle().length() > 0));
				            addLinks(lstURL, lstExcl, lstURLPhase3, subPage);
			            }
			            else {
				            assertTrue("Page vide pour " + subPage.getURL().toExternalForm(), subPage.getContentLength() > 10);
				            addLinks(lstURL, lstExcl, lstURLPhase3, subPage);
			            }
		            }
		        }
		        if (frames.length == 0) {
		            assertTrue("Titre vide pour " + link, (respons.getTitle() != null) && (respons.getTitle().length() > 0));
		            addLinks(lstURL, lstExcl, lstURLPhase3, respons);
		        }
	        }
        }
		return lstURLPhase3;
	}

	/**
	 * @param lstURL
	 * @param lstExcl
	 * @param lstURLPhase3
	 * @param page
	 * @throws SAXException
	 */
	private void addLinks(Collection lstURL, Set lstExcl, Map lstURLPhase3, WebResponse page) throws SAXException {
		String url;
		// Ajoute les liens
		WebLink[] subLinks = page.getLinks();
		for (int j = 0; j < subLinks.length; j++) {
			url = subLinks[j].getURLString();
			if (url.indexOf("javascript") == -1) {
				String rUrl = epureURL(url);
				if (!lstExcl.contains(rUrl)) {
		    		lstURLPhase3.put(rUrl, url);
				}
			}
		}
	}

}
