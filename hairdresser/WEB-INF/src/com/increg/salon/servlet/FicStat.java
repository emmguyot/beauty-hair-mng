package com.increg.salon.servlet;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.increg.commun.DBSession;
import com.increg.commun.exception.FctlException;
import com.increg.salon.bean.SalonSession;
import com.increg.salon.bean.StatBean;
import com.increg.salon.bean.StatHistoBean;


/**
 * Création d'une statistique
 * Creation date: (24/03/2002 10:45:33)
 * @author Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class FicStat extends ConnectedServlet {
    /**
     * @see com.increg.salon.servlet.ConnectedServlet
     */
    public void performTask(
        HttpServletRequest request,
        HttpServletResponse response) {

        // Récupération des paramètres
        String Action = request.getParameter("Action");
        String CD_STAT = request.getParameter("CD_STAT");
        String LIB_STAT = request.getParameter("LIB_STAT");
        String REQ_SQL = request.getParameter("REQ_SQL");
        String LABEL_X = request.getParameter("LABEL_X");
        String LABEL_Y = request.getParameter("LABEL_Y");

        // Récupère la connexion
        HttpSession mySession = request.getSession(false);
        SalonSession mySalon =
            (SalonSession) mySession.getAttribute("SalonSession");
        DBSession myDBSession = mySalon.getMyDBSession();

        // Resultat
        StatBean aStat = null;

        try {
            if (Action == null) {
                // Première phase de création
                request.setAttribute("Action", "Creation");
                // Un bean vide
                aStat = new StatBean();
            }
            else if (Action.equals("Creation")) {
                // Crée réellement la stat

                /**
                 * Création du bean et enregistrement
                 */
                aStat = new StatBean();
                aStat.setLIB_STAT(LIB_STAT);
                aStat.setREQ_SQL(REQ_SQL);
                aStat.setLABEL_X(LABEL_X);
                aStat.setLABEL_Y(LABEL_Y);

                try {
                    aStat.create(myDBSession);
                    mySalon.setMessage("Info", "Création effectuée.");
                    request.setAttribute("Action", "Modification");
                }
                catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                    request.setAttribute("Action", Action);
                }
            }
            else if ((Action.equals("Modification")) && (LIB_STAT == null)) {
                // Affichage de la fiche en modification
                request.setAttribute("Action", "Modification");

                aStat = StatBean.getStatBean(myDBSession, CD_STAT);
            }
            else if (Action.equals("Modification")) {
                // Modification effective de la fiche

                /**
                 * Création du bean et enregistrement
                 */
                aStat = StatBean.getStatBean(myDBSession, CD_STAT);

                aStat.setCD_STAT(CD_STAT);
                aStat.setLIB_STAT(LIB_STAT);
                aStat.setREQ_SQL(REQ_SQL);
                aStat.setLABEL_X(LABEL_X);
                aStat.setLABEL_Y(LABEL_Y);

                try {
                    aStat.maj(myDBSession);
                    mySalon.setMessage("Info", "Enregistrement effectué.");
                    request.setAttribute("Action", "Modification");
                }
                catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                    request.setAttribute("Action", Action);
                }
            }
            else if (Action.equals("Duplication")) {
                // Duplication de la fiche

                /**
                 * Création du bean et enregistrement
                 */
                aStat = new StatBean();

                aStat.setLIB_STAT(LIB_STAT);
                aStat.setREQ_SQL(REQ_SQL);
                aStat.setLABEL_X(LABEL_X);
                aStat.setLABEL_Y(LABEL_Y);

                try {
                    aStat.create(myDBSession);

                    mySalon.setMessage(
                        "Info",
                        "Duplication effectuée. Vous travaillez maintenant sur la copie.");
                    request.setAttribute("Action", "Modification");
                }
                catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                    request.setAttribute("Action", Action);
                }
            }
            else if (Action.equals("Suppression")) {
                // Modification effective de la fiche

                /**
                 * Création du bean et enregistrement
                 */
                aStat = StatBean.getStatBean(myDBSession, CD_STAT);

                // Récup des historiques associés
                Vector lstHisto = StatHistoBean.getStatHistoBean(myDBSession, CD_STAT);
                
                try {
                    // Suppresion des historiques
                    if (lstHisto.size() > 0) {
                        ((StatHistoBean) lstHisto.get(0)).deleteAllStat(myDBSession);
                    }
                    // Suppression de la stat
                    aStat.delete(myDBSession);
                    mySalon.setMessage("Info", "Suppression effectuée.");
                    // Un bean vide
                    aStat = new StatBean();
                    request.setAttribute("Action", "Creation");
                }
                catch (Exception e) {
                    mySalon.setMessage("Erreur", e.toString());
                    request.setAttribute("Action", "Modification");
                }
            }
            else if (Action.equals("Construction")) {
                // Définition du graphe de stat et de ses paramètres
                aStat = StatBean.getStatBean(myDBSession, CD_STAT);

                // Par défaut
                request.setAttribute("Couleur$0", "0x6e04f2");
                request.setAttribute("Couleur$1", "0x71b1f9");
                request.setAttribute("Couleur$2", "0xf600a2");
                request.setAttribute("Couleur$3", "0x079095");
                request.setAttribute("Couleur$4", "0xbb2603");

                // Récup des historiques associés
                Vector lstHisto = StatHistoBean.getStatHistoBean(myDBSession, CD_STAT);
                
                for (int i = 0; i < lstHisto.size(); i++) {
                    StatHistoBean aHisto = (StatHistoBean) lstHisto.get(i);
                    
                    // Positionne les valeurs par défaut
                    request.setAttribute(aHisto.getPARAM() + "$" + aHisto.getNUM_GRAPH(), aHisto.getVALUE());
                }

                // Construit la liste des paramètres à valoriser pour afficher la statistique
                Vector listeParam = aStat.getParameters();

                request.setAttribute("listeParam", listeParam);
            }
            else if (Action.equals("Graphe")) {
                // Affichage du graphe
                aStat = StatBean.getStatBean(myDBSession, CD_STAT);

                Vector lstJeuValeur = new Vector();
                Vector lstCouleur = new Vector();

                // Purge les anciens paramètres
                try {
                    Vector lstHisto = StatHistoBean.getStatHistoBean(myDBSession, CD_STAT);
                    if (lstHisto.size() > 0) {
                        ((StatHistoBean) lstHisto.get(0)).deleteAllStat(myDBSession);
                    }
                }
                catch (SQLException e) {
                    request.setAttribute("Erreur", e.toString());
                    e.printStackTrace();
                }

                for (int nb = 0; nb < 10; nb++) {

                    boolean grapheATracer = true;
                    // Vérifie que ce jeu de paramètre est présent
                    if (request.getParameter("Couleur$" + nb) != null) {
                        // Constitue la liste des paramètres pour utilisation
                        Map paramMap = new HashMap();
                        for (Enumeration i = request.getParameterNames();
                            i.hasMoreElements();
                            ) {
                            String paramName = (String) i.nextElement();
                            if ((paramName.indexOf('$') >= 0)
                                && (paramName
                                    .substring(paramName.indexOf('$') + 1)
                                    .equals(Integer.toString(nb)))) {
                                if (request.getParameter(paramName).length()
                                    > 0) {
                                    paramMap.put(
                                        paramName.substring(
                                            0,
                                            paramName.length() - 2),
                                        request.getParameter(paramName));
                                }
                                else {
                                    // Ce graphe n'est pas demandé
                                    grapheATracer = false;
                                }
                            }
                            else if (paramName.indexOf('$') == -1) {
                                paramMap.put(
                                    paramName,
                                    request.getParameter(paramName));
                            }
                        }

                        try {
                            if (grapheATracer) {
                                TreeMap listeValeurs =
                                    aStat.getData(myDBSession, paramMap);
                                lstJeuValeur.add(listeValeurs);
                                lstCouleur.add(
                                    request.getParameter("Couleur$" + nb));
                                
                                // Sauvegarde les paramètres dans l'historique pour la prochaine fois
                                try {
                                    
                                    Iterator paramIter = paramMap.keySet().iterator();
                                    while (paramIter.hasNext()) {
                                        String param = (String) paramIter.next();
                                        
                                        StatHistoBean aHisto = StatHistoBean.getStatHistoBean(myDBSession, CD_STAT, Integer.toString(nb), param);
                                        
                                        if (aHisto == null) {
                                            aHisto = new StatHistoBean();
                                            aHisto.setCD_STAT(CD_STAT);
                                            aHisto.setNUM_GRAPH(nb);
                                            aHisto.setPARAM(param);
                                            aHisto.setVALUE((String) paramMap.get(param));
                                            
                                            aHisto.create(myDBSession);
                                        }
                                        else {
                                            aHisto.setVALUE((String) paramMap.get(param));
                                    
                                            aHisto.maj(myDBSession);
                                        }
                                    }
                                }
                                catch (SQLException e1) {
                                    request.setAttribute("Erreur", e1.toString());
                                    e1.printStackTrace();
                                }
                            }
                        }
                        catch (FctlException e) {
                            // Message à afficher graphiquement
                            request.setAttribute("Erreur", e.toString());
                            e.printStackTrace();
                        }
                    }
                }
                Vector newLstJeuValeur = lstJeuValeur;
                if (lstJeuValeur.size() > 1) {
                    newLstJeuValeur =
                        aStat.regroupeData(
                            lstJeuValeur,
                            request.getParameter("PeriodeTemps"));
                }
                request.setAttribute("Liste", newLstJeuValeur);
                request.setAttribute("ListeCouleur", lstCouleur);
            }
            else {
                System.out.println("Action non codée : " + Action);
            }
        }
        catch (Exception e) {
            mySalon.setMessage("Erreur", e.toString());
            System.out.println("FicStat : " + e.toString());
        }

        /**
         * Reset de la transaction pour la recherche des informations complémentaires
         */
        myDBSession.cleanTransaction();

        request.setAttribute("StatBean", aStat);

        try {
            if ((Action != null) && (Action.equals("Graphe"))) {
                // Passe la main à la fiche de création
                getServletConfig()
                    .getServletContext()
                    .getRequestDispatcher("/imageGraph.srv")
                    .forward(request, response);
            }
            else if ((Action != null) && (Action.equals("Construction"))) {
                // Passe la main à la fiche de création
                getServletConfig()
                    .getServletContext()
                    .getRequestDispatcher("/ficStatGraph.jsp")
                    .forward(request, response);
            }
            else {
                // Passe la main à la fiche de création
                getServletConfig()
                    .getServletContext()
                    .getRequestDispatcher("/ficStat.jsp")
                    .forward(request, response);
            }
        }
        catch (Exception e) {
            System.out.println(
                "FicStat::performTask : Erreur à la redirection : "
                    + e.toString());
        }
    }
}