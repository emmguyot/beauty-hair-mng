/*
 * Created on 20 févr. 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.salon.request;

import java.util.Calendar;
import java.util.SimpleTimeZone;

import com.increg.commun.DBSession;
import com.increg.salon.bean.ClientBean;
import com.increg.salon.bean.CollabBean;
import com.increg.salon.bean.FactBean;
import com.increg.salon.bean.RDVBean;

/**
 * @author Manu
 *
 * Association RDV / Fact : Correspondance entre une RDV et la facture associée
 */
public class RDVFact implements Comparable {

    /**
     * Rendez-vous : Peut-être nul
     */
    protected RDVBean rdv;
    /**
     * Facture : Peut-être nulle
     */
    protected FactBean fact;
    /**
     * Date du couple
     */
    protected Calendar date;
    /**
     * Client concerné
     */
    protected ClientBean client;
    /**
     * Collab concerné
     */
    protected CollabBean collab;
    
    /**
     * Constructeur du couple
     * @param dbConnect Connexion base à utiliser
     * @param aRDV RDV initial du couple
     */
    public RDVFact(DBSession dbConnect, RDVBean aRDV) {
        rdv = aRDV;
        client = ClientBean.getClientBean(dbConnect, Long.toString(rdv.getCD_CLI()));
        collab = CollabBean.getCollabBean(dbConnect, Long.toString(rdv.getCD_COLLAB()));
        date = rdv.getDT_DEBUT();
    }
    
    /**
     * Constructeur du couple
     * @param dbConnect Connexion base à utiliser
     * @param aFact Facture initiale du couple
     */
    public RDVFact(DBSession dbConnect, FactBean aFact) {
        fact = aFact;
        client = ClientBean.getClientBean(dbConnect, Long.toString(fact.getCD_CLI()));
        collab = CollabBean.getCollabBean(dbConnect, Long.toString(fact.getCD_COLLAB()));

        // Reconstitution de la date : Jour de DT_PREST, Heure de DT Début
        Calendar dateReconst = (Calendar) fact.getDT_PREST().clone();
        dateReconst.setTimeZone(RDVBean.getTimeZone());
        dateReconst.set(Calendar.HOUR_OF_DAY, fact.getDT_MODIF().get(Calendar.HOUR_OF_DAY));
        dateReconst.set(Calendar.MINUTE, fact.getDT_MODIF().get(Calendar.MINUTE));
        dateReconst.set(Calendar.SECOND, fact.getDT_MODIF().get(Calendar.SECOND));
        dateReconst.set(Calendar.MILLISECOND, fact.getDT_MODIF().get(Calendar.MILLISECOND));

        date = dateReconst;
    }
    
    /**
     * @return client du couple
     */
    public ClientBean getClient() {
        return client;
    }

    /**
     * @return Collab principal
     */
    public CollabBean getCollab() {
        return collab;
    }

    /**
     * @return Date du rendez-vous / Facture
     */
    public Calendar getDate() {
        return date;
    }

    /**
     * @return Facture complète
     */
    public FactBean getFact() {
        return fact;
    }

    /**
     * @return Rendez-vous complet
     */
    public RDVBean getRdv() {
        return rdv;
    }

    /**
     * @param bean client du couple
     */
    public void setClient(ClientBean bean) {
        client = bean;
    }

    /**
     * @param bean Collab principal
     */
    public void setCollab(CollabBean bean) {
        collab = bean;
    }

    /**
     * @param calendar Date du rendez-vous / Facture
     */
    public void setDate(Calendar calendar) {
        date = calendar;
    }

    /**
     * @param bean Facture complète
     * @return true si l'affectation a eu lieu
     */
    public boolean setFact(FactBean bean) {
        boolean res = false;
        if ((fact == null) && (bean != null)) {
            if (bean.getCD_CLI() == client.getCD_CLI()) {
                 
                Calendar dateArrondie = (Calendar) date.clone();
                dateArrondie.setTimeZone(SimpleTimeZone.getDefault());
                dateArrondie.set(Calendar.HOUR_OF_DAY, 0);
                dateArrondie.set(Calendar.MINUTE, 0);
                dateArrondie.set(Calendar.SECOND, 0);
                dateArrondie.set(Calendar.MILLISECOND, 0);

                if (dateArrondie.equals(bean.getDT_PREST())) {
                    fact = bean;
                    res = true;
                }
            }
        }
        return res;
    }

    /**
     * @param bean Rendez-vous complet
     * @return true si l'affectation a eu lieu
     */
    public boolean setRdv(RDVBean bean) {
        boolean res = false;
        if ((rdv == null) && (bean != null)) {
            if (bean.getCD_CLI() == client.getCD_CLI()) {
                 
                Calendar dateArrondie = bean.getDT_DEBUT();
                dateArrondie.set(Calendar.HOUR_OF_DAY, 0);
                dateArrondie.set(Calendar.MINUTE, 0);
                dateArrondie.set(Calendar.SECOND, 0);
                dateArrondie.set(Calendar.MILLISECOND, 0);
                 
                Calendar dateFact = fact.getDT_PREST();                  
                dateFact.setTimeZone(RDVBean.getTimeZone());
                dateFact.setTime(dateFact.getTime());

                if (dateArrondie.equals(dateFact)) {
                   rdv = bean;
                   date = rdv.getDT_DEBUT();
                   res = true;
                }
            }
        }
        return res;
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object o) {
        RDVFact rdv2 = (RDVFact) o;
        
        if (getDate().before(rdv2.getDate())) {
            return -1;
        }
        else if (getDate().after(rdv2.getDate())) {
            return 1;
        }
        else {
            // Dates égales
            int resCollab = collab.toString().compareTo(rdv2.getCollab().toString());
            if (resCollab != 0) {
                // Collab différents 
                return resCollab; 
            }
            else {
                // Client différents
                return client.compareTo(rdv2.getClient());
            }
        }
    }

}
