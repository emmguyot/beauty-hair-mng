<%@ page import="com.increg.salon.bean.SalonSession" %>
<%
    SalonSession mySalon = (SalonSession) session.getAttribute("SalonSession");
    if (mySalon == null) {
        getServletConfig().getServletContext().getRequestDispatcher("/reconnect.html").forward(request, response);
    }
%>
<%@ taglib uri="../WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<i18n:bundle baseName="messages" locale="<%= mySalon.getLangue() %>"/>
function MM_reloadPage(init) {  //reloads the window if Nav4 resized
    if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
        document.MM_pgW=innerWidth; document.MM_pgH=innerHeight; onresize=MM_reloadPage; }}
    else if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH) location.reload();
}
MM_reloadPage(true);

function MM_findObj(n, d) { //v4.01
    var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
        d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
    if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
    for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
    if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_showHideLayers() { //v3.0
    var i,p,v,obj,args=MM_showHideLayers.arguments;
    for (i=0; i<(args.length-2); i+=3) if ((obj=MM_findObj(args[i]))!=null) { v=args[i+2];
        if (obj.style) { obj=obj.style; v=(v=='show')?'visible':(v='hide')?'hidden':v; }
        obj.visibility=v; }
}

function FormateDate(obj)
{
    ok = true;
    if (obj.value != "") {
        nbChiffre = 0;
        DansChiffre = false;
        for (i=0; i < obj.value.length; i++) {
            if ((obj.value.charAt(i) >= '0') && (obj.value.charAt(i) <= '9')) {
                if (! DansChiffre) {
                    DansChiffre = true;
                    nbChiffre++;
                }
            }
            else if (obj.value.charAt(i) == '/') {
                if (DansChiffre) {
                    DansChiffre = false;
                }
                else {
                    ok = false;
                }
            }
            else {
                ok = false;
            }
        }
        if (nbChiffre != 3) {
            ok = false;
        }
    }
    if (ok == false) {
        alert ("<i18n:message key="message.formatDateDefaut" />");
    }
    return ok;
}

function FormateDateHeure(obj)
{
    ok = true;
    if (obj.value != "") {
        nbChiffre = 0;
        DansChiffre = false;
        for (i=0; i < obj.value.length; i++) {
            if ((obj.value.charAt(i) >= '0') && (obj.value.charAt(i) <= '9')) {
                if (! DansChiffre) {
                    DansChiffre = true;
                    nbChiffre++;
                }
            }
            else if ((obj.value.charAt(i) == '/') && (nbChiffre < 3)) {
                if (DansChiffre) {
                    DansChiffre = false;
                }
                else {
                    ok = false;
                }
            }
            else if ((obj.value.charAt(i) == ' ') && (nbChiffre == 3)) {
                if (DansChiffre) {
                    DansChiffre = false;
                }
                else {
                    ok = false;
                }
            }
            else if ((obj.value.charAt(i) == ':') && (nbChiffre > 3)) {
                if (DansChiffre) {
                    DansChiffre = false;
                }
                else {
                    ok = false;
                }
            }
            else {
                ok = false;
            }
        }
        if ((nbChiffre != 5) && (nbChiffre != 6)) {
            ok = false;
        }
    }
    if (ok == false) {
        alert ("<i18n:message key="message.formatDateHeureDefaut" />");
    }
    return ok;
}

function FormateHeure(obj)
{
    ok = true;
    if (obj.value != "") {
        nbChiffre = 0;
        DansChiffre = false;
        for (i=0; i < obj.value.length; i++) {
            if ((obj.value.charAt(i) >= '0') && (obj.value.charAt(i) <= '9')) {
                if (! DansChiffre) {
                    DansChiffre = true;
                    nbChiffre++;
                }
            }
            else if (obj.value.charAt(i) == ':') {
                if (DansChiffre) {
                    DansChiffre = false;
                }
                else {
                    ok = false;
                }
            }
            else {
                ok = false;
            }
        }
        if (nbChiffre != 2) {
            ok = false;
        }
    }
    if (ok == false) {
        alert ("<i18n:message key="message.formatHeureDefaut" />");
    }
    return ok;
}

function doEscape(sValue)
{
    var i, j, c;
    var sRet = "";

    if (sValue == null) return null;
    sValue = "" + sValue;
    for (i=0; i<sValue.length; i++)
    {
        c = sValue.charAt(i);
        if ((c >= "0" && c <= "9") ||
            (c >= "a" && c <= "z") ||
            (c >= "A" && c <= "Z"))
            sRet += c;
        else if (c=="+")
            sRet += "%2B";
        else
            sRet += escape(c);
    }

    return sRet;
}
