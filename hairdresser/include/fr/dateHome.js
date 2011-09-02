
function jourSemaine(day) {
   var x = new Array("Dimanche", "Lundi", "Mardi");
   x = x.concat("Mercredi","Jeudi", "Vendredi");
   x = x.concat("Samedi");
   return x[day];
}

function listeJourHeaderSemaine() {
   var x = new Array("Di", "Lu", "Ma");
   x = x.concat("Me","Je", "Ve");
   x = x.concat("Sa");
   return x;
}

function listeMoisEntierComplet() {
   var x = new Array("Janvier", "F�vrier", "Mars");
   x = x.concat("Avril","Mai", "Juin", "Juillet");
   x = x.concat("Ao�t", "Septembre", "Octobre", "Novembre");
   x = x.concat("D�cembre");
   return x;
}

function listeMoisComplet() {
   var x = new Array("Janv.", "F�v.", "Mars");
   x = x.concat("Avril","Mai", "Juin", "Juil.");
   x = x.concat("Ao�t", "Sep.", "Oct.", "Nov.");
   x = x.concat("D�c.");
   return x;
}

function moisComplet(month) {
   var x = new Array("Janv.", "F�v.", "Mars");
   x = x.concat("Avril","Mai", "Juin", "Juil.");
   x = x.concat("Ao�t", "Sep.", "Oct.", "Nov.");
   x = x.concat("D�c.");
   return x[month];
}

function aujourdhui() {
    return "Aujourd'hui";
}

function clock() {
   var digital = new Date();
   var hours = digital.getHours();
   var minutes = digital.getMinutes();
   var seconds = digital.getSeconds();
   if (minutes <= 9) minutes = "0" + minutes;
   if (seconds <= 9) seconds = "0" + seconds;
   dispTime = hours + ":" + minutes + ":" + seconds;
   var jourSemaineCh = jourSemaine(digital.getDay());
   var jour = digital.getDate();
   var mois = moisComplet(digital.getMonth());
   var annee = digital.getFullYear();
   dispTime += "<br/><small>" + jourSemaineCh +  "<br/>" + jour + " " + mois + " " + annee + "</small>";
   MM_findObj("pendule").innerHTML = dispTime;
   setTimeout("clock()", 1000);
}
