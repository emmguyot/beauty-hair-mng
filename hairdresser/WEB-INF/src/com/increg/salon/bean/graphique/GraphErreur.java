/*
 * Erreur sur un graphe
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
package com.increg.salon.bean.graphique;
import java.awt.geom.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
/**
 * Insert the type's description here.
 * Creation date: 7 avr. 02 15:26:14
 * @author: Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class GraphErreur extends AbstractGraph {

protected String myMessage;

/**
 * GraphErreur constructor comment.
 */
public GraphErreur(String msgErreur) {
	super();
	if (msgErreur == null) {
		myMessage = "";
	}
	else {
		myMessage = msgErreur;
	}
}
/**
 * Affichage de l'histogramme
 * Creation date: (30/03/2002 21:19:01)
 * @param g java.awt.Graphics
 */
public void paintGraph(Graphics g) throws com.increg.commun.exception.NoImplementationException, com.increg.commun.exception.FctlException {

	super.paintComponent(g);

	// Tracé : En noir
	g.setColor(Color.red);

	Font myFont = getFont();
	FontMetrics fm = getFontMetrics(myFont);
	int hauteurTexte = fm.getAscent();

	/**
	 * Découpage du texte
	 */
	int posDsTexte = 0;
	int posDsImage = 5 + hauteurTexte;
	while (posDsTexte < myMessage.length()) {
		int cutIndex = Math.min(posDsTexte + 70,myMessage.length());
		int cutIndexB = myMessage.indexOf(" ", cutIndex);
		int cutIndexNL = myMessage.indexOf("\n", cutIndex);
		if ((cutIndexB != -1) && ((cutIndexB < cutIndexNL) || (cutIndexNL == -1))) {
			cutIndex = cutIndexB;
		}
		else if (cutIndexNL != -1) {
			cutIndex = cutIndexNL;
		}
			
		String chaine = myMessage.substring(posDsTexte, cutIndex);
		posDsTexte = cutIndex;
		chaine = chaine.replace('\n', ' ');
		chaine = chaine.replace('\t', ' ');
		g.drawString(chaine, 5, posDsImage);
		
		posDsImage += hauteurTexte*1.1;
	}
}}