/*
 * Classe Montant représentant un montant à payer, ...
 * Copyright (C) 2001-2011 Emmanuel Guyot <See emmguyot on SourceForge> 
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
package com.increg.util;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Montant extends NombreDecimal {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4081894448465851635L;

	/**
	 * Constructor for Montant.
	 * @param val .
	 */
	public Montant(String val) {
		super(val);
	}

	/**
	 * Constructor for Montant.
	 * @param val .
	 */
	public Montant(double val) {
		super(val);
	}

	/**
	 * Constructor for Montant.
	 * @param val .
	 */
	public Montant(BigInteger val) {
		super(val);
	}

	/**
	 * Constructor for Montant.
	 * @param unscaledVal .
	 * @param scale .
	 */
	public Montant(BigInteger unscaledVal, int scale) {
		super(unscaledVal, scale);
	}

	/**
	 * toLetters : Conversion du montant en lettre
     * @param libelleDevise Devise du montant
	 * @return String Chaine représentant le nombre en lettre
	 */
	public String toLetters(String libelleDevise) {
		return toLetters(this, libelleDevise);
	}

	/**
	 * toLetters : Conversion du montant en lettre
	 * @param montant Montant à traduire
     * @param libelleDevise Devise du montant
	 * @return String Chaine représentant le nombre en lettre
	 */
	public static final String toLetters(BigDecimal montant, String libelleDevise) {

        StringBuffer res = new StringBuffer();

        String chaineTotale = montant.multiply(new BigDecimal(100)).setScale(0).toString();
        
        numToChar(chaineTotale.substring(0, chaineTotale.length() - 2), 'M', res);
        
        if (res.charAt(res.length() - 1) != ' ') {
            res.append(" ");
        }
        res.append(libelleDevise);

        /**
         * Partie décimale
         */
        if (!chaineTotale.substring(chaineTotale.length() - 2).equals("00")) {
            numToChar(chaineTotale.substring(chaineTotale.length() - 2), 'M', res);
        }
		return res.toString().trim();
	}

    /**
     * Unité en lettre
     */
	private static String unite[] = { "zéro", "un", "deux", "trois", "quatre", "cinq", "six", "sept", "huit", "neuf" };
    
    /**
     * Dizaine en lettre
     */
	private static String dizaine[] =
		{ "", "dix", "vingt", "trente", "quarante", "cinquante", "soixante", "soixante", "quatre-vingt", "quatre-vingt" };
        
    /**
     * Centaine
     */
	private static String centaine = "cent";

    /**
     * Entre 10 et 20
     */    
	private static String deriveDix[] =
		{ "dix", "onze", "douze", "treize", "quatorze", "quinze", "seize", "dix sept", "dix huit", "dix neuf" };

    /**
     * Conversion d'un nombre à 3 chiffres en lettre
     * @param chiffreATraduire (E) Chiffre à traduire sous forme de chaine de 3 caractères
     * @param Genre (E) F ou M pour féminin masculin
     * @param Resultat (E/S) Chaine contenant le résultat concaténé à la valeur en entrée
     * @param quantiteSingulier (E) Chaine correspondante au singulier de la centaine (de mille, millions, ...)
     * @param quantitePluriel (E) Chaine correspondante au pluriel de la centaine (de mille, millions, ...)
     * @param Un_Necessaire (E) Indicateur si l'unité est affiché ou pas
     * @param unused1 (E) for future use
     * @param unused3 (E) for future use
     */
	private static void centToChar(
		String chiffreATraduire,
		int Genre,
		StringBuffer Resultat,
		String quantiteSingulier,
		String quantitePluriel,
		boolean unused1,
		boolean Un_Necessaire,
		boolean unused3) {
		char Chiffre1, Chiffre2, Chiffre3;

		Chiffre1 = chiffreATraduire.charAt(0);
		// Cas particulier de 100 raccourci ?
		if (Chiffre1 == '1') {
			Resultat.append(" " + centaine);
		}
		else if (Chiffre1 != '0') {
			Resultat.append(" " + unite[Chiffre1 - '0'] + " " + centaine);
		}
		// dizaine et unité
		Chiffre2 = chiffreATraduire.charAt(1);
		Chiffre3 = chiffreATraduire.charAt(2);
		switch (Chiffre2) {
			case '0' :
				if ((Chiffre1 == '0') && (Chiffre3 == '1')) {
					if (quantiteSingulier.length() == 0) {
						Resultat.append(" " + unite[Chiffre3 - '0']);
					}
					else {
						if (Un_Necessaire) {
							Resultat.append(" " + unite[1] + " " + quantiteSingulier);
						}
						else {
							Resultat.append(" " + quantiteSingulier);
						}
					}
				}
				else if (Chiffre3 != '0') {
					Resultat.append(" " + unite[Chiffre3 - '0'] + " " + quantitePluriel);
				}
				else if (Chiffre1 != '0') {
                    if (Chiffre1 != '1') {
                        Resultat.append("s");
                    }
					Resultat.append(" " + quantitePluriel);
				}
                    

				break;
            case '1' :
                Resultat.append(" " + deriveDix[Chiffre3 - '0'] + " " + quantitePluriel);
                break;
            case '7' :
                if (Chiffre3 == '1') {
                    Resultat.append(" " + dizaine[Chiffre2 - '0'] + " et " + deriveDix[Chiffre3 - '0'] + " " + quantitePluriel);
                }
                else {
                    Resultat.append(" " + dizaine[Chiffre2 - '0'] + " " + deriveDix[Chiffre3 - '0'] + " " + quantitePluriel);
                }
                break;
            case '8' :
                if (Chiffre3 != '0') {
                    Resultat.append(" " + dizaine[Chiffre2 - '0'] + " " + unite[Chiffre3 - '0'] + " " + quantitePluriel);
                }
                else {
                    Resultat.append(" " + dizaine[Chiffre2 - '0'] + "s " + quantitePluriel);
                }
                break;
            case '9' :
                Resultat.append(" " + dizaine[Chiffre2 - '0'] + " " + deriveDix[Chiffre3 - '0'] + " " + quantitePluriel);
                break;
			default :
                if (Chiffre3 == '1') {
                    Resultat.append(" " + dizaine[Chiffre2 - '0'] + " et " + unite[Chiffre3 - '0'] + " " + quantitePluriel);
                }
				else if (Chiffre3 != '0') {
					Resultat.append(" " + dizaine[Chiffre2 - '0'] + " " + unite[Chiffre3 - '0'] + " " + quantitePluriel);
				}
				else {
					Resultat.append(" " + dizaine[Chiffre2 - '0'] + " " + quantitePluriel);
				}
		}
	}

    /**
     * Conversion d'un chiffre en lettre
     * @param chiffreATraduire chiffre en base 10 à traduire
     * @param Genre genre du nombre
     * @param Resultat (E/S) Chaine produite
     */
	private static void numToChar(String chiffreATraduire, int Genre, StringBuffer Resultat) {
		String Chiffre_Complet;
		String cent;

		/**
		 * Chiffre compléter de 000 devant
		 */
		Chiffre_Complet = ("000000000000" + chiffreATraduire).substring(chiffreATraduire.length());

		// centaine de milliard
		cent = Chiffre_Complet.substring(0, 3);
		centToChar(cent, Genre, Resultat, "milliard", "milliards", true, true, true);

		// centaine de million
		cent = Chiffre_Complet.substring(3, 6);
		centToChar(cent, Genre, Resultat, "million", "millions", true, true, true);

		// centaine de millier
		cent = Chiffre_Complet.substring(6, 9);
		centToChar(cent, Genre, Resultat, "mille", "mille", true, false, false);

		// centaine
		cent = Chiffre_Complet.substring(9, 12);
		centToChar(cent, Genre, Resultat, "", "", true, true, true);
	}


}