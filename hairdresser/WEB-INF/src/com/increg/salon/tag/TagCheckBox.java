package com.increg.salon.tag;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import java.io.*;

/**
 * Tag permettant d'afficher une liste de check boxes
 * @author Emmanuel Guyot
 */
public class TagCheckBox extends BodyTagSupport {
    /**
     * Le nom des elements des checkbox
     */
    private String nom = null;

    /**
     * L'action a effectuer lorsque l'utilisateur clique
     */
    protected String action = null;

    /**
     * Le tableau des valeurs qui doivent etre cochées
     */
    protected java.lang.String[] tabValeur;

    /**
     * Valeurs associées aux libellés (séparées par un |)
     */
    protected String valeurs;
    
    /**
     * Libellés de chaque case à cocher (séparée par un |)
     */
    protected String libelle;    
    
    /**
     * Longueur maxi des libellés des check box avant un retour a la ligne
     */
    protected int longueurMax = -1;
    
    /**
     * Un flag pour savoir si on coche tout ou non
     */
    protected boolean cocheTout = false;

    /**
     * La longueur totale des libelles    
     */
    protected int longueurTotale = 0;

    /**
     * @see BodyTagSupport
     */    
    public int doAfterBody() {
	
        BodyContent body = getBodyContent();

        JspWriter out = body.getEnclosingWriter();
        String texte = body.getString();
        String chaineCherchee = "%%";
        int pos = texte.indexOf(chaineCherchee);
		longueurTotale = 0;
		
        try {
            if (pos != -1) {
                // Ecrit le texte recupéréé jusqu'à chaineCherchee
                out.print(texte.substring(0, pos));

                //on est dans le cas standard, valeurs contient
                //les valeurs separees par |
                int lastPos = 0;
                int newPos = 0;
                int lastPosLib = 0;
                int newPosLib = 0;
                String val = null;
                String lib = null;
                while (lastPos != -1) {
                    newPos = valeurs.indexOf("|", lastPos);
                    if (newPos != -1) {
                        val = valeurs.substring(lastPos, newPos);
                        lastPos = newPos + 1;
                    }
                    else {
                        val = valeurs.substring(lastPos);
                        lastPos = newPos;
                    }
                    if (libelle != null) {
                        newPosLib = libelle.indexOf("|", lastPosLib);
                        if (newPosLib != -1) {
                            lib = libelle.substring(lastPosLib, newPosLib);
                            lastPosLib = newPosLib + 1;
                        }
                        else {
                            lib = libelle.substring(lastPosLib);
                            lastPosLib = newPosLib;
                        }
                    }
                    printOne(out, val, lib);
                }
                
                out.print(texte.substring(pos + chaineCherchee.length()));
            }
            else {
                out.print(texte);
            }
        }
        catch (IOException e) {
            System.out.println("doAfterBody : " + e.toString());
        }
        return SKIP_BODY;
    }

   /**
    * Affiche le HTML correspondant a une checkbox à partir des donnees passees en paramètre
    * @param out le stream sur lequel ecrire
    * @param aValeur la valeur de la checkbox
    * @param libelle le libelle de la checkbox
    * @exception IOException jetee en cas d'erreur d'écriture
    */
    protected void printOne(JspWriter out, String aValeur, String libelle)
        throws IOException {
        if (aValeur != null) {
            if (longueurMax != -1) {
                //On doit regarder si on respecte la mise en forme
                if (longueurTotale + libelle.length() > longueurMax) {
                    //Il faut revenir a la ligne
                    out.print("<br/>");
                    longueurTotale = 0;
                }
            } 
            out.print(
                "<input type=\"CHECKBOX\" name =\""
                    + nom
                    + "\"value=\""
                    + aValeur + "\"");
           if (action != null) {
               out.print(" onClick=\"" + action + "\" ");
           }
           if (tabValeur != null) {
		        // Regarde si la case doit etre cochée
		        for (int i = 0; i < tabValeur.length; i++) {
			        if (aValeur.equals(tabValeur[i])) {
		    	        out.print(" checked=\"checked\" ");
			        }
		        }                   
            } else if (cocheTout) {
                //on doit cocher la case.
    	        out.print(" checked=\"checked\" ");                
            }
                    
            out.println(">" + libelle + "</input>");
            longueurTotale += libelle.length();
        }

    }

    /**
     * Retourne la valeur du nom du groupe de checkbox.
     * @return la valeur du nom du groupe de checkbox.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Affecte la valeur du nom du groupe de checkbox.
     * @param v  la valeur à affecter au nom du groupe de checkbox.
     */
    public void setNom(String v) {
        this.nom = v;
    }

	/**
	 * Affecte une nouvelle valeur au tableau de valeurs qui seront cochées
	 * @param newTabValeur le tableau de valeurs
	 */
    public void setTabValeur(java.lang.String[] newTabValeur) {
        tabValeur = newTabValeur;
    }
 
    /**
     * Affecte une nouvelle valeur au tableau de valeurs qui seront cochées
     * @param newValeur valeurs séparées par un |
     */
    public void setTabValeur(java.lang.String newValeur) {
        if (newValeur == null) {
            tabValeur = null;
        }
        else {
            // initialise le tableau à la bonne taille
            int lastPos = 0;
            int newPos = 0;
            int indice = 0;
            while (lastPos != -1) {
                newPos = newValeur.indexOf("|", lastPos);
                if (newPos != -1) {
                    lastPos = newPos + 1;
                }
                else {
                    lastPos = newPos;
                }
                indice++;
            }
            tabValeur = new String[indice];
            // Découpe la chaine
            lastPos = 0;
            newPos = 0;
            indice = 0;
            String val = null;
            while (lastPos != -1) {
                newPos = newValeur.indexOf("|", lastPos);
                if (newPos != -1) {
                    val = newValeur.substring(lastPos, newPos);
                    lastPos = newPos + 1;
                }
                else {
                    val = newValeur.substring(lastPos);
                    lastPos = newPos;
                }
                tabValeur[indice++] = val;
            }
        }
    }
   /**
    * Affecte une nouvelle action effectuée lorsque une case est cochée.
    * @param newAction la nouvelle action 
    */   
    public void setAction(String newAction) {
    	action = newAction;    
    }
    
    /**
     * Affecte la longueur maximale des libelles des checkbox entre deux br
     * @param newLong la nouvelle longueur
     */
    public void setLongueurMax(int newLong) {
     	longueurMax = newLong;   
    }
    
    /**
     * Permet de savoir si on coche toutes les check box ou non
     * @param value true ou false
     */
    public void setCocheTout(Boolean value) {
        if (value != null) {
	        cocheTout = value.booleanValue();
        }
    }
    
    /**
     * Sets the libelle.
     * @param libelle The libelle to set
     */
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    /**
     * Sets the valeurs.
     * @param valeurs The valeurs to set
     */
    public void setValeurs(String valeurs) {
        this.valeurs = valeurs;
    }

    /**
     * @see javax.servlet.jsp.tagext.Tag#doEndTag()
     */
    public int doEndTag() throws JspException {
        // RAZ des attributs
        nom = null;
        action = null;
        tabValeur = null;
        valeurs = null;
        libelle = null;
        longueurMax = -1;
        cocheTout = false;
        longueurTotale = 0;
        return super.doEndTag();
    }

}