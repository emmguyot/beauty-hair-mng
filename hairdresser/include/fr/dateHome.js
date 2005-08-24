
function jourSemaine(day) {
   var x = new Array("Dimanche", "Lundi", "Mardi");
   x = x.concat("Mercredi","Jeudi", "Vendredi");
   x = x.concat("Samedi");
   return x[day];
}

function moisComplet(month) {
   var x = new Array("Janv.", "Fév.", "Mars");
   x = x.concat("Avril","Mai", "Juin", "Juil.");
   x = x.concat("Août", "Sep.", "Oct.", "Nov.");
   x = x.concat("Déc.");
   return x[month];
}

function clock() {
   if (!document.layers && !document.all) return;
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
   if (document.layers) {
      document.layers.pendule.document.write(dispTime);
      document.layers.pendule.document.close();
   }
   else if (document.all)
      pendule.innerHTML = dispTime;
   setTimeout("clock()", 1000);
}
