/*
 * Graphe (classe abstraite pour tous les autres)
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
package com.increg.salon.bean.graphique;
import java.text.*;
import java.awt.*;
import java.util.*;
/**
 * Définition du comportement d'un graphe
 * Creation date: (30/03/2002 18:12:53)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public abstract class AbstractGraph extends javax.swing.JPanel {
    /**
     * Légende des X
     */
    protected java.lang.String labelX;
    /**
     * Légende des Y
     */
    protected java.lang.String labelY;
    /**
     * Liste des données chaque élément est une TreeMap Absisse / Valeur
     */
    protected java.util.Vector lstData;
    /**
     * Titre du graphe
     */
    protected java.lang.String title;
    /**
     * Liste des couleurs du graphe
     */
    protected Vector lstCouleur;
    /**
     * AbstractGraph constructor comment.
     */
    public AbstractGraph() {
        super(true);
        labelX = "";
        labelY = "";
        title = "";
        lstData = new Vector();
        lstCouleur = new Vector();
        setBackground(Color.white);
        setForeground(Color.black);
    }

    /**
     * Insert the method's description here.
     * Creation date: (30/03/2002 22:50:19)
     * @param nb Numéro de la couleur
     * @return java.awt.Color
     */
    public java.awt.Color getCouleur(int nb) {
        return (Color) lstCouleur.get(nb);
    }

    /**
     * Insert the method's description here.
     * Creation date: (30/03/2002 21:16:28)
     * @param nb Numéro du jeu de données
     * @return java.util.TreeMap
     */
    public java.util.TreeMap getData(int nb) {
        return (TreeMap) lstData.get(nb);
    }

    /**
     * Insert the method's description here.
     * Creation date: (30/03/2002 18:14:30)
     * @return java.lang.String
     */
    public java.lang.String getLabelX() {
        return labelX;
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (30/03/2002 18:14:49)
     * @return java.lang.String
     */
    public java.lang.String getLabelY() {
        return labelY;
    } 
    
    /**
     * Insert the method's description here.
     * Creation date: (30/03/2002 21:17:03)
     * @return java.lang.String
     */
    public java.lang.String getTitle() {
        return title;
    } 
    
    /**
     * Affichage du graphique
     * Creation date: (30/03/2002 21:19:01)
     * @param g java.awt.Graphics
     */
    public void paintGraph(Graphics g) throws com.increg.commun.exception.NoImplementationException, com.increg.commun.exception.FctlException {
        super.paintComponent(g);
    } 
    
    /**
     * Insert the method's description here.
     * Creation date: (30/03/2002 18:14:30)
     * @param newLabelX java.lang.String
     */
    public void setLabelX(java.lang.String newLabelX) {
        labelX = newLabelX;
    } 
    
    /**
     * Insert the method's description here.
     * Creation date: (30/03/2002 18:14:49)
     * @param newLabelY java.lang.String
     */
    public void setLabelY(java.lang.String newLabelY) {
        labelY = newLabelY;
    } 
    
    /**
     * Insert the method's description here.
     * Creation date: (30/03/2002 21:17:03)
     * @param newTitle java.lang.String
     */
    public void setTitle(java.lang.String newTitle) {
        title = newTitle;
    } 
    
    /**
     * Conversion générique en chaîne
     * Creation date: (30/03/2002 22:00:37)
     * @return java.lang.String
     * @param anObj java.lang.Object
     * @exception com.increg.salon.exception.NoImplementationException Si l'objet n'est pas géré.
     */
    public String toString(Object anObj) throws com.increg.commun.exception.NoImplementationException {

        if (anObj.getClass().getName().equals("java.lang.Integer")) {
            return ((Integer) anObj).toString();
        } else if (anObj.getClass().getName().equals("java.lang.Double")) {
            return ((Double) anObj).toString();
        } else if (anObj.getClass().getName().equals("java.util.Date")) {

            SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
            return formatDate.format((Date) anObj);
        } else if (anObj.getClass().getName().equals("java.util.Calendar")) {

            SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
            return formatDate.format(((Calendar) anObj).getTime());
        } else if (anObj.getClass().getName().equals("java.sql.Timestamp")) {

            SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
            return formatDate.format(((Date) anObj));
        } else if (anObj.getClass().getName().equals("java.lang.String")) {
            return (String) anObj;
        } else {
            throw new com.increg.commun.exception.NoImplementationException("Le traitement pour " + anObj.getClass().getName() + " n'est pas encore prêt.");
        }
    }
    
    /**
     * Returns the lstCouleur.
     * @return Vector
     */
    public Vector getLstCouleur() {
        return lstCouleur;
    }

    /**
     * Returns the lstData.
     * @return java.util.Vector
     */
    public java.util.Vector getLstData() {
        return lstData;
    }

    /**
     * Sets the lstCouleur.
     * @param lstCouleur The lstCouleur to set
     */
    public void setLstCouleur(Vector lstCouleur) {
        this.lstCouleur = lstCouleur;
    }

    /**
     * Sets the lstData.
     * @param lstData The lstData to set
     */
    public void setLstData(java.util.Vector lstData) {
        this.lstData = lstData;
    }

}