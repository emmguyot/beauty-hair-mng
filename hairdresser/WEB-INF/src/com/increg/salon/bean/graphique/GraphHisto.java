/*
 * Histogramme
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

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.increg.commun.BasicSession;
import com.increg.commun.exception.FctlException;
import com.increg.commun.exception.NoImplementationException;

/**
 * Insert the type's description here.
 * Creation date: (30/03/2002 21:21:47)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class GraphHisto extends AbstractGraph {
    /**
     * GraphHisto constructor comment.
     */
    public GraphHisto() {
        super();
    }

    /**
     * Affichage de l'histogramme
     * Creation date: (30/03/2002 21:19:01)
     * @param g java.awt.Graphics
     * @throws FctlException En cas de trop petit graphe ou trop de données
     * @throws NoImplementationException Si le code n'est pas fini
     */
    public void paintGraph(Graphics g)
        throws NoImplementationException, FctlException {

        super.paintComponent(g);

        // Tracé : En noir
        g.setColor(Color.black);

        int nbGraphe = lstData.size();

        /**
         * Phase 1 : Calcul de l'échelle
         */
        boolean legendeVerticale = false;
        Comparable maxX = null;
        Comparable minX = null;
        Double maxY = null;
        Double minY = null;
        double largeurEchelleY = 0;

        Font myFont = getFont();
        g.setFont(myFont);
        FontMetrics fm = getFontMetrics(myFont);

        for (int num = 0; num < nbGraphe; num++) {
            TreeMap data = getData(num);
            if (data.size() > 0) {
                /**
                 * Recalcule l'échelle
                 * 1) Détermine les extrèmes min et max de X et Y
                 * 2) Détermine les tailles max des étiquettes de l'échelle X et Y
                 */
                for (Iterator i = data.keySet().iterator(); i.hasNext();) {
                    Comparable anX = (Comparable) i.next();
                    if ((maxX == null) || (anX.compareTo(maxX) > 0)) {
                        maxX = anX;
                    }
                    if ((minX == null) || (anX.compareTo(minX) < 0)) {
                        minX = anX;
                    }
                }

                for (Iterator i = data.entrySet().iterator(); i.hasNext();) {
                    Double anY =
                        new Double(toString(((Map.Entry) i.next()).getValue()));
                    if ((maxY == null) || (anY.compareTo(maxY) > 0)) {
                        maxY = anY;
                    }
                    if ((minY == null) || (anY.compareTo(minY) < 0)) {
                        minY = anY;
                    }
                    if (fm.getStringBounds(toString(anY), g).getWidth()
                        > largeurEchelleY) {
                        largeurEchelleY =
                            fm.getStringBounds(toString(anY), g).getWidth();
                    }
                }
            }
        }

        Set ensX = new TreeSet();
        for (int num = 0; num < nbGraphe; num++) {
            TreeMap data = getData(num);
            ensX.addAll(data.keySet());
        }

        if (ensX.size() > 0) {
            double milieu = (maxY.doubleValue() + minY.doubleValue()) / 2;
            if (fm.getStringBounds(Double.toString(milieu), g).getWidth()
                > largeurEchelleY) {
                largeurEchelleY =
                    fm.getStringBounds(Double.toString(milieu), g).getWidth();
            }

            // Petite marge pour voir les petites valeurs
            minY = new Double(minY.doubleValue() - 1);
            double margelargeurEchelleY = 5;
            largeurEchelleY += margelargeurEchelleY;

            double largeurFleche = 5;
            double largeurLabelX = fm.getStringBounds(labelX, g).getWidth();
            double hauteurTexte = fm.getAscent();
            double hauteurFleche = largeurFleche * 1.5;
            double margeY = 5;

            double echelleX = 1;
            double echelleY = 1;
            Point2D origine = null;

            /**
             * Largeur = LargeurEchelleY + Largeur de Flèche + (Nb Colonne + 1 blanche)*Nb Données*Largeur Rectangle + Largeur texte label X
             * Si légende horizontale
             *  Hauteur = Hauteur de Texte + Hauteur flèche + margeY + Hauteur_Graphe_Valeur100 + 1/2 hauteur de texte + Hauteur Flèche
             * Si légende verticale
             *  Hauteur = Hauteur de Texte + Hauteur flèche + margeY + Hauteur_Graphe_Valeur100 + 1/2 hauteur de texte + Hauteur Flèche + Largeur de la légende Y 
             */
            echelleX =
                (getWidth() - largeurEchelleY - largeurFleche - largeurLabelX)
                    / (ensX.size() * (nbGraphe + 1));
            if (largeurEchelleY > echelleX) {
                echelleY =
                    (getHeight()
                        - 2 * hauteurTexte
                        - hauteurFleche
                        - 0.5 * hauteurTexte
                        - hauteurFleche
                        - margeY
                        - largeurEchelleY)
                        / (maxY.doubleValue() - minY.doubleValue());
                legendeVerticale = true;
            }
            else {
                echelleY =
                    (getHeight()
                        - 2 * hauteurTexte
                        - hauteurFleche
                        - 0.5 * hauteurTexte
                        - hauteurFleche
                        - margeY)
                        / (maxY.doubleValue() - minY.doubleValue());
            }

            if ((echelleX <= 0) || (echelleY <= 0)) {
                // Taille trop petite
                echelleX = 0;
                echelleY = 0;
                throw new com.increg.commun.exception.FctlException(
                    BasicSession.TAG_I18N + "graphHisto.tropDonnees" + BasicSession.TAG_I18N);
            }

            /**
             * Affiche les axes et leur légende
             */
            origine =
                new Point2D.Double(
                    largeurEchelleY + 0.5 * (largeurFleche - 1),
                    getHeight()
                        - 1.5 * hauteurTexte
                        - 0.5 * (largeurFleche - 1));

            //*********** Axe Y ****************
            if (legendeVerticale) {
                g.drawLine(
                    (int) (origine.getX()),
                    (int) (origine.getY()),
                    (int) (origine.getX()),
                    (int) (getHeight()
                        - 1.5 * hauteurTexte
                        - 0.5 * hauteurFleche
                        - (maxY.doubleValue() - minY.doubleValue()) * echelleY
                        - margeY
                        - largeurEchelleY));
                // Fleche Y
                g.drawPolygon(
                    new int[] {
                        (int) origine.getX(),
                        (int) (origine.getX() + (largeurFleche - 1) / 2),
                        (int) (origine.getX() - (largeurFleche - 1) / 2)},
                    new int[] {
                        (int) (origine.getY()
                            - margeY
                            - hauteurFleche
                            - (maxY.doubleValue() - minY.doubleValue())
                                * echelleY
                            - largeurEchelleY),
                        (int) (origine.getY()
                            - margeY
                            - (maxY.doubleValue() - minY.doubleValue())
                                * echelleY
                            - largeurEchelleY),
                        (int) (origine.getY()
                            - margeY
                            - (maxY.doubleValue() - minY.doubleValue())
                                * echelleY
                            - largeurEchelleY)},
                    3);
                g.fillPolygon(
                    new int[] {
                        (int) origine.getX(),
                        (int) (origine.getX() + (largeurFleche - 1) / 2),
                        (int) (origine.getX() - (largeurFleche - 1) / 2)},
                    new int[] {
                        (int) (origine.getY()
                            - margeY
                            - hauteurFleche
                            - (maxY.doubleValue() - minY.doubleValue())
                                * echelleY
                            - largeurEchelleY),
                        (int) (origine.getY()
                            - margeY
                            - (maxY.doubleValue() - minY.doubleValue())
                                * echelleY
                            - largeurEchelleY),
                        (int) (origine.getY()
                            - margeY
                            - (maxY.doubleValue() - minY.doubleValue())
                                * echelleY
                            - largeurEchelleY)},
                    3);
                // Unité Y
                g.drawString(
                    labelY,
                    (int) (origine.getX()
                        + (largeurFleche - 1) / 2
                        + margelargeurEchelleY),
                    (int) (origine.getY()
                        - echelleY * (maxY.doubleValue() - minY.doubleValue())
                        - largeurEchelleY
                        - hauteurTexte));
            }
            else {
                g.drawLine(
                    (int) (origine.getX()),
                    (int) (origine.getY()),
                    (int) (origine.getX()),
                    (int) (getHeight()
                        - 1.5 * hauteurTexte
                        - 0.5 * hauteurFleche
                        - (maxY.doubleValue() - minY.doubleValue()) * echelleY
                        - margeY));
                // Fleche Y
                g.drawPolygon(
                    new int[] {
                        (int) origine.getX(),
                        (int) (origine.getX() + (largeurFleche - 1) / 2),
                        (int) (origine.getX() - (largeurFleche - 1) / 2)},
                    new int[] {
                        (int) (origine.getY()
                            - margeY
                            - hauteurFleche
                            - (maxY.doubleValue() - minY.doubleValue())
                                * echelleY),
                        (int) (origine.getY()
                            - margeY
                            - (maxY.doubleValue() - minY.doubleValue())
                                * echelleY),
                        (int) (origine.getY()
                            - margeY
                            - (maxY.doubleValue() - minY.doubleValue())
                                * echelleY)},
                    3);
                g.fillPolygon(
                    new int[] {
                        (int) origine.getX(),
                        (int) (origine.getX() + (largeurFleche - 1) / 2),
                        (int) (origine.getX() - (largeurFleche - 1) / 2)},
                    new int[] {
                        (int) (origine.getY()
                            - margeY
                            - hauteurFleche
                            - (maxY.doubleValue() - minY.doubleValue())
                                * echelleY),
                        (int) (origine.getY()
                            - margeY
                            - (maxY.doubleValue() - minY.doubleValue())
                                * echelleY),
                        (int) (origine.getY()
                            - margeY
                            - (maxY.doubleValue() - minY.doubleValue())
                                * echelleY)},
                    3);
                // Unité Y
                g.drawString(
                    labelY,
                    (int) (origine.getX()
                        + (largeurFleche - 1) / 2
                        + margelargeurEchelleY),
                    (int) (origine.getY()
                        - echelleY * (maxY.doubleValue() - minY.doubleValue())
                        - hauteurTexte));
            }
            // Graduation Y : Min
            g.drawLine(
                (int) (origine.getX() - (largeurFleche - 1) / 2),
                (int) (origine.getY()),
                (int) (origine.getX()),
                (int) (origine.getY()));
            g.drawString(
                toString(minY),
                (int) (origine.getX()
                    - (largeurFleche - 1) / 2
                    - fm.getStringBounds(toString(minY), g).getWidth()
                    - margelargeurEchelleY),
                (int) (origine.getY() + 0.5 * hauteurTexte));
            // Graduation Y : Milieu
            g.drawLine(
                (int) (origine.getX() - (largeurFleche - 1) / 2),
                (int) (origine.getY()
                    - echelleY * (milieu - minY.doubleValue())),
                (int) (origine.getX()),
                (int) (origine.getY()
                    - echelleY * (milieu - minY.doubleValue())));
            g.drawString(
                toString(new Double(milieu)),
                (int) (origine.getX()
                    - (largeurFleche - 1) / 2
                    - fm
                        .getStringBounds(toString(new Double(milieu)), g)
                        .getWidth()
                    - margelargeurEchelleY),
                (int) (origine.getY()
                    - echelleY * (milieu - minY.doubleValue())
                    + 0.5 * hauteurTexte));
            // Graduation Y : Max
            g.drawLine(
                (int) (origine.getX() - (largeurFleche - 1) / 2),
                (int) (origine.getY()
                    - echelleY * (maxY.doubleValue() - minY.doubleValue())),
                (int) (origine.getX()),
                (int) (origine.getY()
                    - echelleY * (maxY.doubleValue() - minY.doubleValue())));
            g.drawString(
                toString(maxY),
                (int) (origine.getX()
                    - (largeurFleche - 1) / 2
                    - fm.getStringBounds(toString(maxY), g).getWidth()
                    - margelargeurEchelleY),
                (int) (origine.getY()
                    - echelleY * (maxY.doubleValue() - minY.doubleValue())
                    + 0.5 * hauteurTexte));

            //********* Axe X **************
            g.drawLine(
                (int) (origine.getX()),
                (int) (origine.getY()),
                (int) (origine.getX()
                    + ensX.size() * (nbGraphe + 1) * echelleX),
                (int) (origine.getY()));
            // Fleche X
            g.drawPolygon(
                new int[] {
                    (int) (origine.getX()
                        + hauteurFleche
                        + ensX.size() * (nbGraphe + 1) * echelleX),
                    (int) (origine.getX()
                        + ensX.size() * (nbGraphe + 1) * echelleX),
                    (int) (origine.getX()
                        + ensX.size() * (nbGraphe + 1) * echelleX)},
                new int[] {
                    (int) origine.getY(),
                    (int) (origine.getY() + (largeurFleche - 1) / 2),
                    (int) (origine.getY() - (largeurFleche - 1) / 2)},
                3);
            g.fillPolygon(
                new int[] {
                    (int) (origine.getX()
                        + hauteurFleche
                        + ensX.size() * (nbGraphe + 1) * echelleX),
                    (int) (origine.getX()
                        + ensX.size() * (nbGraphe + 1) * echelleX),
                    (int) (origine.getX()
                        + ensX.size() * (nbGraphe + 1) * echelleX)},
                new int[] {
                    (int) origine.getY(),
                    (int) (origine.getY() + (largeurFleche - 1) / 2),
                    (int) (origine.getY() - (largeurFleche - 1) / 2)},
                3);
            // Label X
            int lastPosX = -10;
            int nb = 0;
            for (Iterator i = ensX.iterator(); i.hasNext();) {
                Comparable anX = (Comparable) i.next();
                String chaine = toString(anX);
                int posX =
                    (int) (origine.getX()
                        + (nb * (nbGraphe + 1) + 0.5 + (nbGraphe / 2.0))
                            * echelleX
                        - fm.getStringBounds(chaine, g).getWidth() / 2);
                int posY = (int) (origine.getY() + 1.5 * hauteurTexte);
                if (posX > lastPosX) {
                    g.drawString(chaine, posX, posY);
                    lastPosX =
                        posX + (int) fm.getStringBounds(chaine, g).getWidth();
                    // Marque sur l'axe
                    g.drawLine(
                        (int) (origine.getX()
                            + (nb * (nbGraphe + 1) + 0.5 + (nbGraphe / 2.0))
                                * echelleX),
                        (int) (origine.getY()),
                        (int) (origine.getX()
                            + (nb * (nbGraphe + 1) + 0.5 + (nbGraphe / 2.0))
                                * echelleX),
                        (int) (origine.getY() + (largeurFleche - 1) / 2));
                }
                nb++;
            }
            // Unité X
            g.drawString(
                labelX,
                (int) (origine.getX()
                    + ensX.size() * (nbGraphe + 1) * echelleX),
                (int) (origine.getY() + 1.5 * hauteurTexte));

            for (int num = 0; num < nbGraphe; num++) {
                TreeMap data = getData(num);

                /**
                 * Affiche les données
                 */
                lastPosX = -10;
                for (Iterator i = data.keySet().iterator(); i.hasNext();) {
                    Comparable anX = (Comparable) i.next();
                    Double anY = new Double(toString(data.get(anX)));

                    // Recherche l'indice dans les X
                    nb = 0;
                    boolean trouve = false;
                    for (Iterator j = ensX.iterator();
                        j.hasNext() && !trouve;
                        ) {
                        Comparable aRefX = (Comparable) j.next();
                        if (aRefX.equals(anX)) {
                            trouve = true;
                        }
                        else {
                            nb++;
                        }
                    }

                    // Dessine
                    g.setColor(getCouleur(num));
                    // Pour éviter une ligne de blanc entre les colonne, la largeur est a peu pret de echelleX
                    g.fillRect(
                        (int) (origine.getX()
                            + (nb * (nbGraphe + 1) + num + 0.5) * echelleX),
                        (int) (origine.getY()
                            - (int) (echelleY
                                * (anY.doubleValue() - minY.doubleValue()))),
                        ((int) (origine.getX()
                            + (nb * (nbGraphe + 1) + num + 1.5) * echelleX)
                            - (int) (origine.getX()
                                + (nb * (nbGraphe + 1) + num + 0.5) * echelleX)),
                        (int) (echelleY
                            * (anY.doubleValue() - minY.doubleValue())));
                    g.setColor(Color.black);
                    String chaine = toString(anY);
                    if (legendeVerticale) {
                        int posX =
                            (int) (origine.getX()
                                + (nb * (nbGraphe + 1) + num + 0.5) * echelleX
                                + (echelleX
                                    + fm.getStringBounds(chaine, g).getHeight()
                                        / 2.0)
                                    / 2);
                        int posY =
                            (int) (origine.getY()
                                - (int) (echelleY
                                    * (anY.doubleValue() - minY.doubleValue()))
                                - 1);
                        if (posX > lastPosX) {
                            ((Graphics2D) g).rotate(-Math.PI / 2, posX, posY);
                            g.drawString(chaine, posX, posY);
                            lastPosX =
                                posX
                                    + (int) (fm
                                        .getStringBounds(chaine, g)
                                        .getHeight());
                            ((Graphics2D) g).rotate(Math.PI / 2, posX, posY);
                        }
                    }
                    else {
                        int posX =
                            (int) (origine.getX()
                                + (nb * (nbGraphe + 1) + num + 0.5) * echelleX
                                + (echelleX
                                    - fm.getStringBounds(chaine, g).getWidth())
                                    / 2);
                        int posY =
                            (int) (origine.getY()
                                - (int) (echelleY
                                    * (anY.doubleValue() - minY.doubleValue()))
                                - 1);
                        if (posX > lastPosX) {
                            g.drawString(chaine, posX, posY);
                            lastPosX =
                                posX
                                    + (int) (fm
                                        .getStringBounds(chaine, g)
                                        .getWidth());
                        }
                    }
                }

            } // for
        }

        // Titre
        g.setColor(Color.black);
        g.setFont(
            getFont().deriveFont(
                Font.BOLD,
                (float) (getFont().getSize() * 1.5)));
        g.drawString(
            title,
            (int) ((getWidth()
                - getFontMetrics(g.getFont())
                    .getStringBounds(title, g)
                    .getWidth())
                / 2),
            (int) (getFontMetrics(g.getFont()).getAscent()));
    }

}