<%@ page import="java.text.SimpleDateFormat,java.util.Date" %>
<%@ page import="com.increg.salon.bean.SalonSession,com.increg.util.Montant" %>
<%
    SalonSession mySalon = (SalonSession) session.getAttribute("SalonSession");
    if (mySalon == null) {
        getServletConfig().getServletContext().getRequestDispatcher("/reconnect.html").forward(request, response);
    }
%>
<%@ taglib uri="WEB-INF/salon-taglib.tld" prefix="salon" %>
<html>
<head>
<title>Chèque</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="style/Salon.css" type="text/css">
</head>
<body class="corpsImpr" onLoad="Imprimer()">
<%@ include file="include/commun.js" %>
<%
   // Récupération des paramètres
   String montant = request.getParameter("montant");
%>

   
<table class="chequeImpr" width="400">
   <tr> 
      <td height="31" colspan="6">&nbsp;</td>
   </tr>
   <tr> 
      <td width="7" height="36"></td>
      <td width="84"></td>
      <td width="368" valign="top"><%
            String montantEnLettre = new Montant(montant).toLetters(mySalon.getDevise());
            // Premier lettre en majuscule
            montantEnLettre = montantEnLettre.substring(0,1).toUpperCase() + montantEnLettre.substring(1);
            %><%= montantEnLettre %></td>
      <td width="10"></td>
      <td width="14"></td>
      <td width="138"></td>
   </tr>
   <tr> 
      <td height="27"></td>
      <td colspan="2" valign="top"><%= mySalon.getMySociete().getRAIS_SOC() %></td>
      <td></td>
      <td colspan="2" rowspan="2" valign="top"><%= montant %></td>
   </tr>
   <tr> 
      <td height="12"></td>
      <td></td>
      <td></td>
      <td></td>
   </tr>
   <tr> 
      <td height="38"></td>
      <td></td>
      <td></td>
      <td></td>
      <td></td>
      <td valign="top"><%= mySalon.getMySociete().getVILLE() %><br>
         <%= new SimpleDateFormat("dd/MM/yyyy").format(new Date()) %></td>
   </tr>
   <tr> 
      <td height="147"></td>
      <td></td>
      <td></td>
      <td></td>
      <td></td>
      <td></td>
   </tr>
</table>
<script language="JavaScript">

// Impression du ticket
function Imprimer()
{
   window.print();
   window.close();
}

</script>
</body>
</html>
