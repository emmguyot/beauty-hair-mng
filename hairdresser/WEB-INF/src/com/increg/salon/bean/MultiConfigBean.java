package com.increg.salon.bean;

import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.TreeMap;

import com.increg.commun.exception.FctlException;

/**
 * @author Manu
 *
 * Permet la gestion de multiples salon
 */
public class MultiConfigBean {

    /**
     * Nombre de configurations disponibles
     */
    private int nbConfig;
    /**
     * Numéro de la configuration choisie
     */
    private int configChoisie = -1;

    /**
     * Tableau des bases disponibles
     * Indexé par le numéro de la config
     */
    private String base[];    
    
    /**
     * Nombre max de configurations gérées
     */
    private static final int NB_CONFIG_MAX = 100;
    
    /**
     * Constructeur : Initialisation des informations interne ==> Multiple ou pas ?
     */
    public MultiConfigBean () {
        nbConfig = 0;
        base = new String[NB_CONFIG_MAX];
        for (int i = 0; i < NB_CONFIG_MAX; i++) {
            try {
                ResourceBundle resconfig = ResourceBundle.getBundle("config" + Integer.toString(i));
        
                String aBase = resconfig.getString("base");
                resconfig.getString("class");
                resconfig.getString("user");
                resconfig.getString("password");
                resconfig.getString("licence");
                resconfig.getString("savepath");

                // On est arrivé ici, le fichier de config est bon
                int indice = aBase.lastIndexOf(":");
                if (indice > 0) {
                    aBase = aBase.substring(indice + 1);
                }
                base[i] = aBase;
                nbConfig++;
            } catch (MissingResourceException e) {
                // Le fichier n'existe pas : Pas grave
                base[i] = null;
            }
        }
    }
    /**
     * Returns the configChoisie.
     * @return int
     */
    public int getConfigChoisie() {
        return configChoisie;
    }

    /**
     * Returns the nbConfig.
     * @return int
     */
    public int getNbConfig() {
        return nbConfig;
    }

    /**
     * Sets the configChoisie.
     * @param configChoisie The configChoisie to set
     * @throws FctlException En cas de problème à la prise en compte
     * @return nom du fichier de config à utiliser
     */
    public String setConfigChoisie(int configChoisie) throws FctlException {
        this.configChoisie = configChoisie;
        return "config" + Integer.toString(configChoisie);
    }

    /**
     * Sets the nbConfig.
     * @param nbConfig The nbConfig to set
     */
    public void setNbConfig(int nbConfig) {
        this.nbConfig = nbConfig;
    }

    /**
     * Returns the base.
     * @return String[]
     */
    public String[] getBase() {
        return base;
    }

    /**
     * Sets the base.
     * @param base The base to set
     */
    public void setBase(String[] base) {
        this.base = base;
    }

    /**
     * Retourne les bases sous forme de map
     * @return la Map des bases : Clé l'indice, Valeur texte de la base
     */
    public Map base2Map() {
        TreeMap aMap = new TreeMap();
        
        for (int i = 0; i < NB_CONFIG_MAX; i++) {
            if (base[i] != null) {
                aMap.put(new Integer(i), base[i]);
            }
        }
        
        return aMap;
    }
}
