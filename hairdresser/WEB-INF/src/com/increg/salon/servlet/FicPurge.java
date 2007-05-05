package com.increg.salon.servlet;

import java.text.MessageFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.increg.commun.BasicSession;
import com.increg.commun.DBSession;
import com.increg.salon.bean.ArtBean;
import com.increg.salon.bean.ClientBean;
import com.increg.salon.bean.CollabBean;
import com.increg.salon.bean.FactBean;
import com.increg.salon.bean.HistoPrestBean;
import com.increg.salon.bean.MvtCaisseBean;
import com.increg.salon.bean.MvtStkBean;
import com.increg.salon.bean.PaiementBean;
import com.increg.salon.bean.PointageBean;
import com.increg.salon.bean.PrestBean;
import com.increg.salon.bean.RDVBean;
import com.increg.salon.bean.SalonSession;
import com.increg.util.SimpleDateFormatEG;

/**
 * Purge des données
 * Creation date: 8 déc. 2003
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class FicPurge extends ConnectedServlet {
    /**
     * @see com.increg.salon.servlet.ConnectedServlet
     */
    public void performTask(HttpServletRequest request, HttpServletResponse response) {

    	Log log = LogFactory.getLog(this.getClass());

        String Action = request.getParameter("Action");
        String date = request.getParameter("Date");
        Boolean paiement = new Boolean(request.getParameter("Paiement") != null);
        Boolean histoPrest = new Boolean(request.getParameter("Histo_prest") != null);
        Boolean rdv = new Boolean(request.getParameter("RDV") != null);
        Boolean mvtCaisse = new Boolean(request.getParameter("Mvt_caisse") != null);
        Boolean mvtStk = new Boolean(request.getParameter("Mvt_stk") != null);
        Boolean client = new Boolean(request.getParameter("Client") != null);
        Boolean art = new Boolean(request.getParameter("Art") != null);
        Boolean prest = new Boolean(request.getParameter("Prest") != null);
        Boolean clientPerime = new Boolean(request.getParameter("Client_perime") != null);
        Boolean pointage = new Boolean(request.getParameter("Pointage") != null);
        Boolean collab = new Boolean(request.getParameter("Collab") != null);

        // Récupère la connexion
        HttpSession mySession = request.getSession(false);
        SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
        DBSession myDBSession = mySalon.getMyDBSession();
        ResourceBundle msgBundle = mySalon.getMessagesBundle();
		SimpleDateFormatEG formatDate = new SimpleDateFormatEG(msgBundle.getString("format.dateSimpleDefaut"));

        String msgInfo = "";


        if ((Action != null) && (Action.equals("Purge"))) {
            try {
                // Vérification du format de la date
                Date dateLimite = formatDate.parse(date);
    
                /**
                 * Ordre à respecter
                 * - Mvt_stk, Mvt_Caisse, Histo_Prest / Facture, Pointage
                 * - Paiement, Art, Prest, Collab 
                 * - Client, Client_périmé
                 */ 
    
                myDBSession.setDansTransactions(true);
    
    
                int nbEnreg = 0;
                                      
                if (mvtStk.booleanValue()) {
                    nbEnreg = MvtStkBean.purge(myDBSession, dateLimite);   
                    if (nbEnreg > 0) {
                    	msgInfo += MessageFormat.format(msgBundle.getString("ficPurge.purgeStockNb"), new Object[] { Integer.toString(nbEnreg) });
                    }            
                }
                if (mvtCaisse.booleanValue()) {
                    nbEnreg = MvtCaisseBean.purge(myDBSession, dateLimite);
                    if (nbEnreg > 0) {
                    	msgInfo += MessageFormat.format(msgBundle.getString("ficPurge.purgeCaisseNb"), new Object[] { Integer.toString(nbEnreg) });
                    }            
                }
                if (histoPrest.booleanValue()) {
                    nbEnreg = HistoPrestBean.purge(myDBSession, dateLimite);            
                    if (nbEnreg > 0) {
                    	msgInfo += MessageFormat.format(msgBundle.getString("ficPurge.purgeHistoryNb"), new Object[] { Integer.toString(nbEnreg) });
                    }
                    FactBean.purge(myDBSession, dateLimite);            
                }
                if (rdv.booleanValue()) {
                    nbEnreg = RDVBean.purge(myDBSession, dateLimite);            
                    if (nbEnreg > 0) {
                    	msgInfo += MessageFormat.format(msgBundle.getString("ficPurge.purgeRDVNb"), new Object[] { Integer.toString(nbEnreg) });
                    }
                }
                if (pointage.booleanValue()) {
                    nbEnreg = PointageBean.purge(myDBSession, dateLimite);
                    if (nbEnreg > 0) {
                    	msgInfo += MessageFormat.format(msgBundle.getString("ficPurge.purgePointageNb"), new Object[] { Integer.toString(nbEnreg) });
                    }
                }
                if (paiement.booleanValue()) {
                    nbEnreg = PaiementBean.purge(myDBSession, dateLimite);            
                    if (nbEnreg > 0) {
                    	msgInfo += MessageFormat.format(msgBundle.getString("ficPurge.purgePaiementNb"), new Object[] { Integer.toString(nbEnreg) });
                    }
                }
                if (prest.booleanValue()) {
                    nbEnreg = PrestBean.purge(myDBSession, dateLimite);            
                    if (nbEnreg > 0) {
                    	msgInfo += MessageFormat.format(msgBundle.getString("ficPurge.purgePrestNb"), new Object[] { Integer.toString(nbEnreg) });
                    }
                }
                if (art.booleanValue()) {
                    nbEnreg = ArtBean.purge(myDBSession, dateLimite); 
                    if (nbEnreg > 0) {
                    	msgInfo += MessageFormat.format(msgBundle.getString("ficPurge.purgeArticlesNb"), new Object[] { Integer.toString(nbEnreg) });
                    }
                }
                if (collab.booleanValue()) {
                    nbEnreg = CollabBean.purge(myDBSession, dateLimite);
                    if (nbEnreg > 0) {
                    	msgInfo += MessageFormat.format(msgBundle.getString("ficPurge.purgeCollabNb"), new Object[] { Integer.toString(nbEnreg) });
                    }
                }
                if (client.booleanValue()) {
                    nbEnreg = ClientBean.purge(myDBSession, dateLimite);            
                    if (nbEnreg > 0) {
                    	msgInfo += MessageFormat.format(msgBundle.getString("ficPurge.purgeClientNb"), new Object[] { Integer.toString(nbEnreg) });
                    }
                }
                if (clientPerime.booleanValue()) {
                    nbEnreg = ClientBean.purgePerime(myDBSession, dateLimite);
                    if (nbEnreg > 0) {
                    	msgInfo += MessageFormat.format(msgBundle.getString("ficPurge.purgeClientInvalideNb"), new Object[] { Integer.toString(nbEnreg) });
                    }
                }
                
                myDBSession.endTransaction();            
                mySalon.setMessage("Info", BasicSession.TAG_I18N + "ficPurge.purgeOk" + BasicSession.TAG_I18N);
    
            }
            catch (Exception e) {
                mySalon.setMessage("Erreur", e.toString());
                log.error("Erreur générale : ", e);
                myDBSession.cleanTransaction();
            }
        }
        
        request.setAttribute("Paiement", paiement);
        request.setAttribute("Histo_prest", histoPrest);
        request.setAttribute("RDV", rdv);
        request.setAttribute("Mvt_caisse", mvtCaisse);
        request.setAttribute("Mvt_stk", mvtStk);
        request.setAttribute("Client", client);
        request.setAttribute("Art", art);
        request.setAttribute("Prest", prest);
        request.setAttribute("Client_perime", clientPerime);
        request.setAttribute("Pointage", pointage);
        request.setAttribute("Collab", collab);
        request.setAttribute("MsgInfo", msgInfo);

        /**
         * Reset de la transaction pour la recherche des informations complémentaires
         */
        myDBSession.cleanTransaction();

        try {
            // Passe la main à la fiche de création
            getServletConfig().getServletContext().getRequestDispatcher("/ficPurge.jsp").forward(request, response);
        }
        catch (Exception e) {
            log.error("Erreur à la redirection : ", e);
        }
    }
}
