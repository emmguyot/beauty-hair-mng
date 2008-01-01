/*
 * Connexion base
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
package com.increg.commun;

import java.lang.ref.SoftReference;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Vector;

import com.increg.util.SimpleDateFormatEG;

/**
 * Bean Session pour la Gestion de Salon de Coiffure
 * Creation date: (07/07/2001 18:28:05)
 * @author: Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */

/**
 * Classe g�rant la connexion � la base et les acc�s
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class DBSession {
    
    /**
     * Toutes les instances actives de connexion � la base de donn�es
     * Les �l�ments de ce vecteur sont des SoftReference 
     */
    private static Vector activeInstances = new Vector(); 
    /**
     * Properties de config
     */
	private ResourceBundle resconfig;
    /**
     * Connection � la base de donn�es
     */
	private Connection dbConnect;
    /**
     * Indicateur si une transaction est en cours
     */
	protected boolean dansTransactions = false;
    /**
     * Indicateur si une transaction est d�marr�e
     */
	protected boolean transactionStarted = false;
    /**
     * Donnees de connexion � la base : Nom de la base
     */
    protected String base;
    /**
     * Nom de l'utilisateur base de donn�es
     */
    protected String user;
    /**
     * Nombre d'imbrications d'appel pour transactions
     */
    protected int nbImbricTransac = 0;
    
    /**
     * SalonSession constructor comment.
     * 
     */
    public DBSession() {
        this("config");
    }
    
    /**
     * SalonSession constructor comment.
     * @param configName Nom du fichier de config � utiliser
     */
    public DBSession(String configName) {
    	super();
    
    	// Connection � la base
    	try {
    		resconfig = ResourceBundle.getBundle(configName);
    
    		base = resconfig.getString("base");
    		user = resconfig.getString("user");
            open();
    	}
    	catch (Exception e) {
    		System.out.println("Pb � la connexion BD : " + e.toString());
    		dbConnect = null;
    	}
    	
    }

    /**
     * Ouverture de la base
     * @throws ClassNotFoundException .
     * @throws SQLException .
     */
    public void open() throws ClassNotFoundException, SQLException {

        String className = resconfig.getString("class");
        String pwd = resconfig.getString("password");
        
        Class.forName(className);
        dbConnect = DriverManager.getConnection(base, user, pwd);
        
        synchronized (activeInstances) {
            // Ajoute la connexion � la liste des connexions actives
            activeInstances.add(new SoftReference(dbConnect));
        }
        
        String reqSQL[] = {""}; 
        try {
            // Initialise le TimeZone pour qu'il soit coh�rent entre la base et Java
            Calendar now = Calendar.getInstance();
            int tzOffset = -(now.get(Calendar.DST_OFFSET) + now.get(Calendar.ZONE_OFFSET)) / (60 * 60 * 1000);            reqSQL[0] = "set timezone to \"GMT";
            if (tzOffset > 0) {
                reqSQL[0] += "+" + tzOffset + "\"";
            }
            else {
                reqSQL[0] += tzOffset + "\"";
            }
            doExecuteSQL(reqSQL);
        }
        catch (SQLException e) {
            System.out.println ("Erreur � l'initialistion de la Timezone : " + e.toString());
            System.out.println ("Requete : " + reqSQL[0]);
        }
    }
    
    /**
     * Annule les transactions en cours
     * Creation date: (20/09/2001 22:17:53)
     */
    public void cleanTransaction() {
    	setDansTransactions(false);
    	transactionStarted = false;
        nbImbricTransac = 0;
    	try {
    		dbConnect.rollback();
    		dbConnect.setAutoCommit(true);
    	}
        catch (Exception e) {
            System.err.println("Erreur dans cleanTransaction : ");
            e.printStackTrace();
        }
        catch (Throwable t) {
            System.err.println("Erreur dans cleanTransaction : ");
            t.printStackTrace();
        }
    }
    /**
     * Execute une s�rie de requete SQL d'un seul tenant
     * Creation date: (07/07/2001 18:52:34)
     * @return int[] Nombre de lignes mise � jour
     * @param queries java.lang.String[] diff�rentes requ�tes SQL dans l'ordre d'ex�cution
     * @exception java.sql.SQLException The exception description.
     */
    public synchronized int[] doExecuteSQL(String[] queries) throws java.sql.SQLException {
    
    	int[] res = new int[queries.length];
    	
    	// Mode transaction
    	if ((queries.length > 1) || (dansTransactions && !transactionStarted)) {
    		dbConnect.setAutoCommit(false);
    	}
    	try {
    		Statement stmt = dbConnect.createStatement();
    
    		// Chaque requ�te : une par une
    		for (int i = 0; i < queries.length; i++) {
    			res[i] = stmt.executeUpdate(queries[i]);
    		}
    		if ((queries.length > 1) && (!dansTransactions)) {
    			dbConnect.commit();
    			dbConnect.setAutoCommit(true);
    		}
    	}
    	catch (SQLException e) {
    		if ((queries.length > 1) && (!dansTransactions)) {
    			dbConnect.rollback();
    			dbConnect.setAutoCommit(true);
    		}
    		throw (e);
    	}
    	
    	return res;
    }
    
    /**
     * Effectue une requ�te de type select : C'est � dire retournant des lignes
     * Creation date: (07/07/2001 19:46:26)
     * @return java.sql.ResultSet
     * @param query java.lang.String
     * @exception java.sql.SQLException The exception description.
     */
    public synchronized ResultSet doRequest(String query) throws java.sql.SQLException {
    
    	ResultSet rs = null;
    	try {
    		Statement stmt = dbConnect.createStatement();
    		rs = stmt.executeQuery(query);
    	}
    	catch (SQLException e) {
    		throw e;
    	}
    	return rs;
    }
    /**
     * Termine la transaction tout en g�rant la pile.
     * Si la pile des transactions est vide, un commit est effectu�
     * Creation date: (20/09/2001 22:17:53)
     */
    public void endTransaction() {
        nbImbricTransac--;
        if (nbImbricTransac <= 0) {
        	setDansTransactions(false);
        	transactionStarted = false;
            try {
                dbConnect.commit();
                dbConnect.setAutoCommit(true);
            }
            catch (Exception e) {
                System.out.println ("Erreur au commit de la Transaction : " + e.toString());
            }
        }
    }
    /**
     * Code to perform when this object is garbage collected.
     * 
     * Any exception thrown by a finalize method causes the finalization to
     * halt. But otherwise, it is ignored.
     * @throws Throwable Exception au cas o�
     */
    protected void finalize() throws Throwable {
    	// Fermeture de la connexion � la base
    	if (dbConnect != null) {
    		dbConnect.close();

            removeRef();
    	}
    	// This implementation simply forwards the message to super.  You may replace or supplement this.
    	super.finalize();
    }

    /**
     * Suppression de la r�f�rence de la connexion suite � la d�connexion base
     */
    private void removeRef() {
        synchronized (activeInstances) {            
            // Suppression de la liste des connexions actives
            Iterator instanceIterator = activeInstances.iterator();
            while (instanceIterator.hasNext()) {
                SoftReference dbConnectRef = (SoftReference) instanceIterator.next();
            
                if ((dbConnectRef.get() == null) || (dbConnectRef.get() == dbConnect)) {
                    // Suppression de l'instance purg�e
                    instanceIterator.remove();
                }
            }
        }
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (20/09/2001 22:17:28)
     * @return boolean
     */
    public boolean isDansTransactions() {
    	return dansTransactions;
    }

    /**
     * G�n�re une cha�ne quot�e avec le caract�re en question
     * Creation date: (07/07/2001 18:52:34)
     * @return String Chaine modifi�e
     * @param sValue java.lang.String Chaine � quoter
     * @param quote java.lang.char Quote � utiliser
     */
    public static String quoteWith(String sValue, char quote) {
    	char c;
    
    	// Rien � Faire ?
    	if (quote == ' ') {
    		return sValue;
        }
    
    	StringBuffer ret = new StringBuffer();
    	ret.append(quote);
    
    	for (int i = 0; i < sValue.length(); i++) {
    		c = sValue.charAt(i);
    		if (quote == c) {
    			ret.append(c);
    		}
    		ret.append(c);
    	}
    	ret.append(quote);
    
    	return ret.toString();
    }

    /**
     * Indique le d�but d'une transaction logique (avec la notion de pile)
     * Creation date: (20/09/2001 22:17:28)
     * @param newDansTransactions boolean
     */
    public void setDansTransactions(boolean newDansTransactions) {
        if (newDansTransactions) {
            nbImbricTransac++;
        }
    	dansTransactions = newDansTransactions;
    }

    /**
     * Donne le nom de la base de donn�es
     * @return String
     */
    public String getBaseName() {
        
        int pos = base.lastIndexOf(":");
        return base.substring(pos + 1);
    }

    /**
     * Donne le nom de la base de donn�es
     * @param newBase Base cible
     */
    public void setBaseName(String newBase) {
        
        int pos = base.lastIndexOf(":");
        base = base.substring(1, pos + 1) + newBase;
    }

    /**
     * Method getUser.
     * @return String
     */
    public String getUser() {
        return user;
    }

    /**
     * Fermeture de la connexion base de donn�es
     */
    public void close() {
        if (dbConnect != null) {
            try {
                dbConnect.close();
                removeRef();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
            dbConnect = null;
        }
    }
    
    /**
     * Fermeture de toutes les connexions base de donn�es en cours
     * !!! Tr�s Violent !!!!
     */
    public void closeAll() {
        synchronized (activeInstances) {            
            // Suppression de la liste des connexions actives
            Iterator instanceIterator = activeInstances.iterator();
            while (instanceIterator.hasNext()) {
                SoftReference dbConnectRef = (SoftReference) instanceIterator.next();
            
                if (dbConnectRef.get() != null) {
                    Connection aDbConnect = (Connection) dbConnectRef.get();

                    // Fermeture de l'instance                    
                    try {
                        aDbConnect.close();
                    }
                    catch (SQLException e) {
                        e.printStackTrace();
                        System.out.println("Impossible de fermer une connexion base de donnees");
                    }
                }
                instanceIterator.remove();
            }
        }
    }

	/**
	 * @param cal date � passer en texte
	 * @return Returns the formatDateAvecTZ.
	 * Cr�ation � chaque fois car non threadsafe
	 */
	public String formatDateTimeAvecTZ(Calendar cal) {
		return new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss").formatEG(cal.getTime());
	}

	/**
	 * @return Returns the formatDateSansTZ.
	 * Cr�ation � chaque fois car non threadsafe
	 */
    public DateFormat getFormatDateTimeSansTZ() {
		return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	}    

    /**
	 * @return Returns the formatDate.
	 * Cr�ation � chaque fois car non threadsafe
	 */
    public DateFormat getFormatDate() {
		return new SimpleDateFormat("dd/MM/yyyy");
	}    
}
