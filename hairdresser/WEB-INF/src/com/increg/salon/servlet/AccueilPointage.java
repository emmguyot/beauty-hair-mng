package com.increg.salon.servlet;

import java.util.Calendar;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.increg.commun.DBSession;
import com.increg.salon.bean.CollabBean;
import com.increg.salon.bean.FeteBean;
import com.increg.salon.bean.PointageBean;
import com.increg.salon.bean.SalonSession;
import com.increg.util.SimpleDateFormatEG;

/**
 * Gestion de l'accueil avec pointage des collabs
 * Creation date: (29/09/2001 10:11:38)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class AccueilPointage extends ConnectedServlet {
    /**
     * @see com.increg.salon.servlet.ConnectedServlet
     */
    public void performTask(HttpServletRequest request, HttpServletResponse response) {

        String Action = request.getParameter("Action");

        // Récupère la connexion
        HttpSession mySession = request.getSession(false);
        SalonSession mySalon = (SalonSession) mySession.getAttribute("SalonSession");
        DBSession myDBSession = mySalon.getMyDBSession();
        SimpleDateFormatEG formatDate = new SimpleDateFormatEG("dd/MM/yyyy HH:mm:ss");

        // Liste des collab cochés
        Vector lstCoche = new Vector();
        Enumeration factEnum = request.getParameterNames();
        while (factEnum.hasMoreElements()) {
            String aName = (String) factEnum.nextElement();
            if (aName.indexOf("CD_COLLAB") != -1) {
                lstCoche.add(aName.substring(9));
            }
        }

        Vector lstCollab = new Vector();
        Vector lstPointage = new Vector();
        // Recherche la fête du jour
        Vector lstFete = FeteBean.getFeteBean(myDBSession);

        try {
            // Consitue la liste des collab et de leur dernier pointage
            
            // Récupère tous les collabs valides
            lstCollab = new Vector(CollabBean.getAllCollabsAsList(myDBSession));

            for (Iterator iterCollab = lstCollab.iterator(); iterCollab.hasNext();) {
                CollabBean aCollab = (CollabBean) iterCollab.next();
                
                boolean majFaite = false;

                // Recherche le dernier pointage
                PointageBean aPointage =
                    PointageBean.getPointageBean(
                        myDBSession,
                        Integer.toString(aCollab.getCD_COLLAB()),
                        formatDate.formatEG(Calendar.getInstance().getTime()));

                if ((aPointage == null)
                    || ((aPointage.getDT_FIN() != null) && (aPointage.getDT_FIN().getTime().compareTo(new java.util.Date()) < 0))) {
                    // Nouveau Pointage
                    aPointage = new PointageBean();
                    aPointage.setCD_COLLAB(aCollab.getCD_COLLAB());
                    aPointage.setCD_TYP_POINTAGE(PointageBean.TYP_PRESENCE); // PRESENCE
                }

                // Doit-on faire une maj ?
                for (int i = 0; i < lstCoche.size(); i++) {
                    if (Integer.parseInt((String) lstCoche.get(i)) == aCollab.getCD_COLLAB()) {
                        if (aPointage.getDT_DEBUT() == null) {
                            aPointage.setDT_DEBUT(Calendar.getInstance());
                            try {
                                aPointage.create(myDBSession);
                            }
                            catch (Exception e) {
                                mySalon.setMessage("Erreur", e.toString());
                                /**
                                 * Reset de la transaction pour la recherche des informations complémentaires
                                 */
                                myDBSession.cleanTransaction();
                            }
                        }
                        majFaite = true;
                    }
                }

                if ((majFaite == false) && (Action != null)) {
                    if (aPointage.getDT_DEBUT() == null) {
                        // Pas de pointage
                        aPointage = null;
                    }
                    else if (aPointage.getDT_FIN() == null) {
                        aPointage.setDT_FIN(Calendar.getInstance());
                        try {
                            aPointage.maj(myDBSession);
                        }
                        catch (Exception e) {
                            mySalon.setMessage("Erreur", e.toString());
                            /**
                             * Reset de la transaction pour la recherche des informations complémentaires
                             */
                            myDBSession.cleanTransaction();
                        }
                    }
                }

                lstPointage.add(aPointage);
            }
        }
        catch (Exception e) {
            mySalon.setMessage("Erreur", e.toString());
            System.out.println("Note : " + e.toString());
        }

        request.setAttribute("lstCollab", lstCollab);
        request.setAttribute("lstPointage", lstPointage);
        request.setAttribute("lstFete", lstFete);

        try {
            // Passe la main à la fiche de création
            getServletConfig().getServletContext().getRequestDispatcher("/Accueil.jsp").forward(request, response);

        }
        catch (Exception e) {
            System.out.println("AccueilPointage::performTask : Erreur à la redirection : " + e.toString());
        }
    }
}
