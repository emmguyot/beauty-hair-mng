	<!--
	 Partie Haute de la fiche article
	-->
	<p> 
		<salon:valeur valeurNulle="0" valeur="<%= aArt.getCD_ART() %>" >
		  <input type="hidden" name="CD_ART" value="%%" >
	        </salon:valeur>
		<input type="hidden" name="Action" value="<%=Action%>">
	        <input type="hidden" name="ParamSup" value="">
		<span class="obligatoire">Type d'article :</span> 
		<salon:DBselection valeur="<%= aArt.getCD_TYP_ART() %>" sql="select CD_TYP_ART, LIB_TYP_ART from TYP_ART order by LIB_TYP_ART">
		  <select name="CD_TYP_ART">
		     %%
		  </select>
	        </salon:DBselection>
                <% if (aArt.getINDIC_MIXTE().equals("O")) { %>
                    <span class="readonly">Mixte</span>
                <% } %>
	<br>
		<span class="obligatoire">Catégorie :</span> 
		<salon:DBselection valeur="<%= aArt.getCD_CATEG_ART() %>" sql="select CD_CATEG_ART, LIB_CATEG_ART from CATEG_ART order by LIB_CATEG_ART">
		  <select name="CD_CATEG_ART">
		     %%
		  </select>
	        </salon:DBselection>
		<span class="obligatoire">Libellé :</span> 
		<salon:valeur valeurNulle="null" valeur="<%= aArt.getLIB_ART() %>" >
		  <input type="text" name="LIB_ART" value="%%" size=40 onChange="set('LIB_ART')">
	        </salon:valeur>
		<span class="obligatoire">Article périmé :</span> 
                <salon:selection valeur='<%= aArt.getINDIC_PERIM() %>' valeurs='<%= "N|O" %>' libelle="Non|Oui">
                    <select name="INDIC_PERIM">
                        %%
                    </select>
                </salon:selection>
	</p>
	<p>
		<span class="facultatif">Référence article :</span> 
		<salon:valeur valeurNulle="null" valeur="<%= aArt.getREF_ART() %>">
		  <input type="text" name="REF_ART" value="%%"  size=10 onChange="set('REF_ART')">
	        </salon:valeur>
		<span class="obligatoire">Stock de rupture :</span> 
		<salon:valeur valeurNulle="null" valeur="<%= aArt.getQTE_STK_MIN() %>" >
		  <input type="text" name="QTE_STK_MIN" value="%%" size=5>
	        </salon:valeur>
		<span class="obligatoire">Quantité en stock :</span> 
		<salon:valeur valeurNulle="null" valeur="<%= aArt.getQTE_STK() %>" >
		  <span class="readonly" style="{vertical-align: top}">%%</span>
		  <input type="hidden" name="QTE_STK" value="%%">
	        </salon:valeur>
		<span class="obligatoire">Valeur unitaire du stock :</span> 
		<salon:valeur valeurNulle="null" valeur="<%= aArt.getVAL_STK_HT() %>" >
		  <span class="readonly" style="{vertical-align: top}">%%</span>
		  <input type="hidden" name="VAL_STK_HT" value="%%">
	        </salon:valeur>
	</p>
	<p>
	 <span class="facultatif">Commentaire :</span> 
	    <salon:valeur valeurNulle="null" valeur="<%= aArt.getCOMM() %>" >
		<textarea name="COMM" cols="40" rows="2">%%</textarea>
	    </salon:valeur>
	</p>
<hr>
