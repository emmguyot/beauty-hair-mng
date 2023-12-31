<%
/*
 * This program is part of InCrEG LibertyLook software http://beauty-hair-mng.sourceforge.net
 * Copyright (C) 2001-2009 Emmanuel Guyot <See emmguyot on SourceForge> 
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
%>
	<!--
	 Partie Haute de la fiche article
	-->
	<p> 
		<salon:valeur valeurNulle="0" valeur="<%= aArt.getCD_ART() %>" >
		  <input type="hidden" name="CD_ART" value="%%" >
	        </salon:valeur>
		<input type="hidden" name="Action" value="<%=Action%>">
	        <input type="hidden" name="ParamSup" value="">
		<span class="obligatoire"><i18n:message key="label.typeArticle" /> :</span> 
		<salon:DBselection valeur="<%= aArt.getCD_TYP_ART() %>" sql="select CD_TYP_ART, LIB_TYP_ART from TYP_ART order by LIB_TYP_ART">
		  <select name="CD_TYP_ART">
		     %%
		  </select>
	        </salon:DBselection>
                <% if (aArt.getINDIC_MIXTE().equals("O")) { %>
                    <span class="readonly"><i18n:message key="label.articleMixte" /></span>
                <% } %>
	<br>
		<span class="obligatoire"><i18n:message key="label.categorieArticle" /> :</span> 
		<salon:DBselection valeur="<%= aArt.getCD_CATEG_ART() %>" sql="select CD_CATEG_ART, LIB_CATEG_ART from CATEG_ART order by LIB_CATEG_ART">
		  <select name="CD_CATEG_ART">
		     %%
		  </select>
	        </salon:DBselection>
		<span class="obligatoire"><i18n:message key="label.libelle" /> :</span> 
		<salon:valeur valeurNulle="null" valeur="<%= aArt.getLIB_ART() %>" >
		  <input type="text" name="LIB_ART" value="%%" size=40 onChange="set('LIB_ART')">
	        </salon:valeur>
		<span class="obligatoire"><i18n:message key="label.articlePerime" /> :</span> 
                <i18n:message key="valeur.nonOui" id="valeurNonOui_common" />
                <salon:selection valeur='<%= aArt.getINDIC_PERIM() %>' valeurs='<%= "N|O" %>' libelle="<%= valeurNonOui_common %>">
                    <select name="INDIC_PERIM">
                        %%
                    </select>
                </salon:selection>
	</p>
	<p>
		<span class="facultatif"><i18n:message key="label.refArticle" /> :</span> 
		<salon:valeur valeurNulle="null" valeur="<%= aArt.getREF_ART() %>">
		  <input type="text" name="REF_ART" value="%%"  size=10 onChange="set('REF_ART')">
	        </salon:valeur>
		<span class="obligatoire"><i18n:message key="label.stockRupture" /> :</span> 
		<salon:valeur valeurNulle="null" valeur="<%= aArt.getQTE_STK_MIN() %>" >
		  <input type="text" name="QTE_STK_MIN" value="%%" size=5>
	        </salon:valeur>
		<span class="obligatoire"><i18n:message key="label.qteStock" /> :</span> 
		<salon:valeur valeurNulle="null" valeur="<%= aArt.getQTE_STK() %>" >
		  <span class="readonly" style="{vertical-align: top}">%%</span>
		  <input type="hidden" name="QTE_STK" value="%%">
	        </salon:valeur>
		<span class="obligatoire"><i18n:message key="label.valeurStockUnit" /> :</span> 
		<salon:valeur valeurNulle="null" valeur="<%= aArt.getVAL_STK_HT() %>" >
		  <span class="readonly" style="{vertical-align: top}">%%</span>
		  <input type="hidden" name="VAL_STK_HT" value="%%">
	        </salon:valeur>
	</p>
	<p>
	 <span class="facultatif"><i18n:message key="label.commentaire" /> :</span> 
	    <salon:valeur valeurNulle="null" valeur="<%= aArt.getCOMM() %>" >
		<textarea name="COMM" cols="40" rows="2">%%</textarea>
	    </salon:valeur>
	</p>
<hr>
