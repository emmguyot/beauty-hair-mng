package com.increg.salon.request;

import java.util.*;
import com.increg.salon.bean.*;
/**
 * Bean de facture papier
 * Creation date: (04/11/2001 16:42:31)
 * @author: Emmanuel GUYOT <emmguyot@wanadoo.fr>
 */
public class EditionFacture {
	PaiementBean myPaiement;
	FactBean myFact;
/**
 * EditionFacture constructor comment.
 */
public EditionFacture() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (04/11/2001 17:07:08)
 * @return com.increg.salon.bean.FactBean
 */
public com.increg.salon.bean.FactBean getMyFact() {
	return myFact;
}
/**
 * Insert the method's description here.
 * Creation date: (04/11/2001 16:44:20)
 * @return com.increg.salon.bean.PaiementBean
 */
public com.increg.salon.bean.PaiementBean getMyPaiement() {
	return myPaiement;
}
/**
 * Insert the method's description here.
 * Creation date: (04/11/2001 17:07:08)
 * @param newMyFact com.increg.salon.bean.FactBean
 */
public void setMyFact(com.increg.salon.bean.FactBean newMyFact) {
	myFact = newMyFact;
}
/**
 * Insert the method's description here.
 * Creation date: (04/11/2001 16:44:20)
 * @param newMyPaiement com.increg.salon.bean.PaiementBean
 */
public void setMyPaiement(com.increg.salon.bean.PaiementBean newMyPaiement) {
	myPaiement = newMyPaiement;
}
}
