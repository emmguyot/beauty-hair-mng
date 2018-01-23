select * from art, categ_art, typ_art
where indic_perim = 'N'
and art.cd_categ_art=categ_art.cd_categ_art
and art.cd_typ_art=typ_art.cd_typ_art

select * from prest
left outer join categ_prest on prest.cd_categ_prest = categ_prest.cd_categ_prest
left outer join marque on prest.cd_marque = marque.cd_marque, 
typ_vent
where indic_perim='N'
and prest.cd_typ_vent = typ_vent.cd_typ_vent

select cd_cli,	civilite,	nom,	prenom, 	 regexp_replace(rue, E'[\\n\\r]+', ' ', 'g' ),	ville,	cd_postal,	tel,	portable,	email,
dt_anniv,	regexp_replace(comm, E'[\\n\\r]+', ' ', 'g' ),	dt_creat,	dt_modif,	indic_valid,	lib_typ_chev,	categ_cli.cd_categ_cli,	lib_categ_cli,	tr_age.cd_tr_age,	lib_tr_age,
age_min,	age_max,	orig.cd_orig,	lib_orig,	typ_peau.cd_typ_peau,	lib_typ_peau
from cli
left outer join typ_chev on cli.cd_typ_chev = typ_chev.cd_typ_chev
left outer join categ_cli on cli.cd_categ_cli = categ_cli.cd_categ_cli
left outer join tr_age on cli.cd_tr_age = tr_age.cd_tr_age
left outer join orig on cli.cd_orig = orig.cd_orig
left outer join typ_peau on cli.cd_typ_peau = typ_peau.cd_typ_peau

select cli.cd_cli,	civilite,	nom,	prenom, prest.lib_prest, regexp_replace(histo_prest.comm, E'[\\n\\r]+', ' ', 'g' ),
histo_prest.dt_prest 
from cli
join histo_prest on cli.cd_cli = histo_prest.cd_cli
join prest on prest.cd_prest = histo_prest.cd_prest
where histo_prest.comm is not null
order by histo_prest.dt_prest desc


select * from histo_prest where cd_cli=2022