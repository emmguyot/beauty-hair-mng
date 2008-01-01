/*
 * Création d'un graphe au format image
 * Copyright (C) 2002-2008 Emmanuel Guyot <See emmguyot on SourceForge> 
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
package com.increg.salon.servlet;
import java.awt.Color;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.increg.salon.bean.SalonSession;
import com.increg.salon.bean.graphique.AbstractGraph;
import com.increg.salon.bean.graphique.GraphErreur;
import com.increg.salon.bean.graphique.GraphHisto;

public class ImageGraph extends ConnectedServlet {
/**
 * @see com.increg.salon.servlet.ConnectedServlet
 */
public void performTask(
	javax.servlet.http.HttpServletRequest request,
	javax.servlet.http.HttpServletResponse response) {

	Log log = LogFactory.getLog(this.getClass());

	// Récupération des paramètres
//	String Action = request.getParameter("Action");
	Vector liste = (Vector) request.getAttribute("Liste");
	String width = request.getParameter("Width");
	String height = request.getParameter("Height");
	String LIB_STAT = request.getParameter("LIB_STAT");
	String LABEL_X = request.getParameter("LABEL_X");
	String LABEL_Y = request.getParameter("LABEL_Y");

	// Valeur par défaut
	int myWidth = 500;
	int myHeight = 500;
	if (width != null) {
		myWidth = Integer.parseInt(width);
	}
	if (height != null) {
		myHeight = Integer.parseInt(height);
	}

	// Récupère la connexion
	HttpSession mySession = request.getSession(false);
	SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");

	try {
		response.setContentType("image/gif"); 
        
        // Reconstitue la liste des couleurs
        Vector lstCouleurChaine = (Vector) request.getAttribute("ListeCouleur");
        Vector lstCouleur = new Vector();
        for (int i = 0; i < lstCouleurChaine.size(); i++) {
            lstCouleur.add(Color.decode((String) lstCouleurChaine.get(i)));
        }
        

		// Création de l'image en mémoire
		AbstractGraph graph = null;
		if ((liste == null) || (liste.size() == 0)) {
			graph = new GraphErreur((String) request.getAttribute("Erreur"));
		}
		else {
			graph = new GraphHisto();
            graph.setLstCouleur(lstCouleur);
			graph.setFont(new java.awt.Font("sans serif", java.awt.Font.PLAIN, 10));
			graph.setLstData(liste);
			graph.setLabelX(LABEL_X);
			graph.setLabelY(LABEL_Y);
			graph.setTitle(LIB_STAT);
		}

		graph.setBounds(0, 0, myWidth, myHeight);
		java.awt.image.BufferedImage image =
			new java.awt.image.BufferedImage(myWidth, myHeight, java.awt.image.BufferedImage.TYPE_INT_RGB);

		// Dessine l'image dans les bonnes dimensions
		graph.paintGraph(image.getGraphics());
			
		// Transformation en gif
		new Acme.JPM.Encoders.GifEncoder(image, response.getOutputStream()).encode();

	}
	catch (Exception e) {
		mySalon.setMessage("Erreur", e.toString());
		log.error("Erreur générale : ", e);
	}

}}