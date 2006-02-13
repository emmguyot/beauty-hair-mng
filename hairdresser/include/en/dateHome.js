
function jourSemaine(day) {
   var x = new Array("Sunday", "Monday", "Tuesday");
   x = x.concat("Wednesday","Thursday", "Friday");
   x = x.concat("Saturday");
   return x[day];
}

function listeJourHeaderSemaine() {
   var x = new Array("Su", "Mo", "Tu");
   x = x.concat("We","Th", "Fr");
   x = x.concat("Sa");
   return x;
}

function listeMoisEntierComplet() {
   var x = new Array("January", "February", "March");
   x = x.concat("April","May", "June", "July");
   x = x.concat("August", "September", "October", "November");
   x = x.concat("December");
   return x;
}

function listeMoisComplet() {
   var x = new Array("Jan.", "Feb.", "March");
   x = x.concat("April","May", "June", "July");
   x = x.concat("Aug.", "Sep.", "Oct.", "Nov.");
   x = x.concat("Dec.");
   return x;
}

function moisComplet(month) {
   var x = new Array("Jan.", "Feb.", "March");
   x = x.concat("April","May", "June", "July");
   x = x.concat("Aug.", "Sep.", "Oct.", "Nov.");
   x = x.concat("Dec.");
   return x[month];
}

function aujourdhui() {
    return "Today";
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
   dispTime += "<br/><small>" + jourSemaineCh +  "<br/>" + mois + " " + jour + " " + annee + "</small>";
   if (document.layers) {
      document.layers.pendule.document.write(dispTime);
      document.layers.pendule.document.close();
   }
   else if (document.all)
      pendule.innerHTML = dispTime;
   setTimeout("clock()", 1000);
}
