package com.increg.salon.bean;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.MissingResourceException;

import com.ibm.ejs.security.util.Base64Coder;
import com.increg.commun.exception.UnauthorisedUserException;
/**
 * Gestion de la licence de l'application : Version sans DLL pour Linux
 * Creation date: (30/09/2001 14:07:29)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 * 
 * <b>Attention : Toute modification de cette classe doit �tre report�e 
 * d'une mani�re ou d'une autre sur la classe �quivalente dans salon_serveur</b>
 */
public class LicenceBean extends Authenticator {

    /**
     * Pointeur sur la resource de configuration contenant le code
     */
    private java.util.ResourceBundle resconfig;
    /**
     * Code de la licence
     */
    protected String code;
    /**
     * Raison sociale du salon
     */
    protected String RAIS_SOC;
    /**
     * Version LIGHT ?
     */
    private static boolean light = false;
     
    /**
     * Donne l'utilisateur correspondant � la raison sociale
     * @param aRAIS_SOC Raison sociale � convertir
     * @return user associ� � la raison sociale
     */
    protected static String getUser(String aRAIS_SOC) {
    
        StringBuffer user = new StringBuffer();
        
        for (int i = 0; i < aRAIS_SOC.length(); i++) {
            if (aRAIS_SOC.charAt(i) == ' ') {
                user.append('_');
            }
            else if (aRAIS_SOC.charAt(i) == ':') {
                user.append('.');
            }
            else {
                user.append(aRAIS_SOC.charAt(i));
            }
        }

        return user.toString();
    }
    
    /**
     * Retourne le user et mot de passe pour l'acc�s Web
     * @return l'authentification correspondante � la licence
     */
    public PasswordAuthentication getPasswordAuthentication() {

        char[] pwd = code.toCharArray();
        String user = getUser(RAIS_SOC);

        return new PasswordAuthentication(user, pwd);
    }

    /**
     * V�rifie si le code correspond � la raison sociale
     * @param aRAIS_SOC Raison sociale
     * @param code Code d�clar�
     * @param use Utilisation
     * @return r�sultat de la comparaison : True si c'est bon 
     *          Passage au GPL : Cette m�thode retourne toujours <b>true</b>
     */
    protected static boolean isCodeCorrect(String aRAIS_SOC, String code, String use) {
        return true;
    }
    
    /**
     * LicenceBean constructor comment.
     * @param aRAIS_SOC Raison sociale du salon
     * @param configName Nom du fichier de config � utiliser
     * @throws UnauthorisedUserException en cas d'erreur de licence
     */
    public LicenceBean(String aRAIS_SOC, String configName) throws UnauthorisedUserException {
        super();

        RAIS_SOC = aRAIS_SOC;

        try {
            if ((RAIS_SOC == null) || (RAIS_SOC.length() == 0)) {
                throw (new Exception("La soci�t� n'est pas initialis�e"));
            }

            // Chargement du fichier de config
            resconfig = java.util.ResourceBundle.getBundle(configName);

            try {
                code = resconfig.getString("licence"); //$NON-NLS-1$

                if (!isCodeCorrect(RAIS_SOC, code, "")) {
                    // Pas bon
                    throw (new UnauthorisedUserException());
                }
            }
            catch (MissingResourceException e) {
                // Pas de licence => Version Light ?
                throw (new UnauthorisedUserException("Code licence introuvable"));
            }

        }
        catch (Exception e) {
            System.out.println("LicenceBean :" + e.toString());
            throw (new UnauthorisedUserException("Vous n'avez pas la licence ad�quate pour utiliser ce logiciel (" + e.toString() + ")"));
        }
    }
    
    /**
     * Donne l'authentification Basic
     * Creation date: (13/10/2001 21:59:42)
     * @return Chaine correspondante � une authentification simple
     */
    public String getBasicAuthentication() {
        PasswordAuthentication aPwd = getPasswordAuthentication();
        String res =
            Base64Coder.base64Encode(
                aPwd.getUserName() + ":" + new String(aPwd.getPassword()));
        return "Basic " + res;
    }
}
