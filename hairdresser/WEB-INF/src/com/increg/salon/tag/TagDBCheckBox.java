package com.increg.salon.tag;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import com.increg.commun.DBSession;
import com.increg.salon.bean.SalonSession;

import java.io.*;

import java.sql.*;

/**
 * Tag permettant d'afficher une liste de check boxes
 * @author Alexandre Guyot <alexandre.guyot@laposte.net>
 */
public class TagDBCheckBox extends BodyTagSupport {
    /**
     * Le nom des elements des checkbox
     */
    private String nom = null;

    /**
     * Le sql utilisé pour génerer les valeurs des checkboxes
     */
    private String sql = null;

    /**
     * L'action a effectuer lorsque l'utilisateur clique
     */
    protected String action = null;

    /**
     * Le tableau des valeurs qui doivent etre cochées
     */
    protected java.lang.String[] tabValeur;
    
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
     * La session de DB uytilisée pour rappatrier les informations
     * */
    protected DBSession myDBSession = null;

    /**
     * @see javax.servlet.jsp.tagext.Tag#doAfterBody()
     */
    public int doAfterBody() {
	
        if (myDBSession == null) {
            // Récupére la connexion base de données
            javax.servlet.http.HttpSession mySession = pageContext.getSession();
            if ((mySession == null)
                || (mySession.getAttribute("SalonSession") == null)
                || (((SalonSession) mySession.getAttribute("SalonSession")).getMyIdent()
                    == null)) {
                // Perte de la connexion : Rien à faire
                return SKIP_BODY;
            }

            myDBSession =
                ((SalonSession) mySession.getAttribute("SalonSession")).getMyDBSession();
        }

        BodyContent body = getBodyContent();

        JspWriter out = body.getEnclosingWriter();
        String texte = body.getString();
        String chaineCherchee = "%%";
        int pos = texte.indexOf(chaineCherchee);
		longueurTotale = 0;
		
        try {
            if (pos != -1) {
                // Ecrit lLe texte recupéréé jusqu'à chaineCherchee
                out.print(texte.substring(0, pos));

                try {
                    ResultSet aRS = myDBSession.doRequest(sql);

                    while (aRS.next()) {
                        printOne(out, aRS.getString(1), aRS.getString(2));
                    }

                    aRS.close();
                }
                catch (java.sql.SQLException sqlErr) {
                    System.out.println(
                        "Erreur SQL sur requète >" + sql + "< : " + sqlErr.toString());
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
                   
            // Rend le libellé insecable
            out.print(">");
            for (int i = 0; i < libelle.length(); i++) {
                if (libelle.charAt(i) != ' ') {
                    out.print(libelle.charAt(i));
                }
                else {
                    out.print("&nbsp;");
                }
            }
            out.println("</input>");
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
     * Retourne la valeur du SQL utilisée pour récuperer les paramètres. 
     * @return la valeur du SQL utilisée pour récuperer les paramètres. 
     */
    public String getSql() {
        return sql;
    }

    /**
     * Affecte la valeur du SQL utilisée pour récuperer les paramètres.
     * @param v  la valeur à affecter au SQL utilisé pour récuperer les paramètres. 
     */
    public void setSql(String v) {
        this.sql = v;
    }

	/**
	 * Affecte une nouvelle valeur au tableau de valeurs qui seront cochées
	 * @param newTabValeur le tableau de valeurs
	 */
    public void setTabValeur(java.lang.String[] newTabValeur) {
        tabValeur = newTabValeur;
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
     * @see javax.servlet.jsp.tagext.Tag#doEndTag()
     */
    public int doEndTag() throws JspException {
        // RAZ des attributs
        nom = null;
        sql = null;
        action = null;
        tabValeur = null;
        longueurMax = -1;
        cocheTout = false;
        longueurTotale = 0;
        myDBSession = null;
        return super.doEndTag();
    }

}