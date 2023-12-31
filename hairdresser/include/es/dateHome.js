
function jourSemaine(day) {
   var x = new Array("Domingo", "Lunes", "Martes");
   x = x.concat("Mi�rcoles","Jueves", "Viernes");
   x = x.concat("S&aacute;bado");
   return x[day];
}

function listeJourHeaderSemaine() {
   var x = new Array("Do", "Lu", "Ma");
   x = x.concat("Mi","Je", "Vi");
   x = x.concat("S&aacute;");
   return x;
}

function listeMoisEntierComplet() {
   var x = new Array("Enero", "Febrero", "Marzo");
   x = x.concat("Abril","Mayo", "Junio", "Julio");
   x = x.concat("Agosto", "Septiembre", "Octubre", "Noviembre");
   x = x.concat("Diciembre");
   return x;
}

function listeMoisComplet() {
   var x = new Array("Enero", "Feb.", "Marzo");
   x = x.concat("Abril","Mayo", "Junio", "Julio");
   x = x.concat("Ago.", "Sep.", "Oct.", "Nov.");
   x = x.concat("Dic.");
   return x;
}

function moisComplet(month) {
   var x = new Array("Enero", "Feb.", "Marzo");
   x = x.concat("Abril","Mayo", "Junio", "Julio");
   x = x.concat("Ago.", "Sep.", "Oct.", "Nov.");
   x = x.concat("Dic.");
   return x[month];
}

function aujourdhui() {
    return "Hoy";
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
   MM_findObj("pendule").innerHTML = dispTime;
   setTimeout("clock()", 1000);
}
