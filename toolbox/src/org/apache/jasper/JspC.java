// Decompiled by DJ v3.4.4.74 Copyright 2003 Atanas Neshkov  Date: 06/10/2003 13:25:59
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   JspC.java
// Origine : Tomcat 3.3.1

package org.apache.jasper;

import java.io.*;
import java.util.*;
import javax.servlet.ServletException;
import org.apache.jasper.compiler.CommandLineCompiler;
import org.apache.jasper.compiler.Compiler;
import org.apache.jasper.runtime.JspLoader;
import org.apache.jasper.servlet.JasperLoader;
import org.apache.tomcat.util.compat.Jdk11Compat;
import org.apache.tomcat.util.log.Log;

// Referenced classes of package org.apache.jasper:
//            CommandLineContext, JasperException, Options, Constants

public class JspC implements Options {

    public boolean getKeepGenerated() {
        return true;
    }

    public boolean getLargeFile() {
        return largeFile;
    }

    public boolean getMappedFile() {
        return mappedFile;
    }

    public Object getProtectionDomain() {
        return null;
    }

    public boolean getSendErrorToClient() {
        return true;
    }

    public boolean getClassDebugInfo() {
        return false;
    }

    public String getIeClassId() {
        return ieClassId;
    }

    public int getJspVerbosityLevel() {
        return jspVerbosityLevel;
    }

    public File getScratchDir() {
        return scratchDir;
    }

    public Class getJspCompilerPlugin() {
        return null;
    }

    public String getJspCompilerPath() {
        return null;
    }

    public String getJavaEncoding() {
        return "UTF-8";
    }

    public String getClassPath() {
        return System.getProperty("java.class.path");
    }

    private void pushBackArg() {
        if (!fullstop)
            argPos--;
    }

    private String nextArg() {
        if (argPos >= args.length || (fullstop = "--".equals(args[argPos])))
            return null;
        else
            return args[argPos++];
    }

    private String nextFile() {
        if (fullstop)
            argPos++;
        if (argPos >= args.length)
            return null;
        else
            return args[argPos++];
    }

    public JspC(String arg[], PrintStream log) {
        largeFile = false;
        mappedFile = false;
        jspVerbosityLevel = 3;
        ieClassId = "clsid:8AD9C840-044E-11D1-B3E9-00805F499D93";
        dieOnExit = false;
        fullstop = false;
        args = arg;
        int verbosityLevel = 2;
        dieLevel = 0;
        die = dieLevel;
        String rt = System.getProperty("jsp.runtime.package");
        if (rt != null) {
            Constants.JSP_RUNTIME_PACKAGE = rt;
            Constants.JSP_SERVLET_BASE = rt + ".HttpJspBase";
        }
        String tok;
        while ((tok = nextArg()) != null) {
            if (tok.equals("-q")) {
                verbosityLevel = 2;
                continue;
            }
            if (tok.equals("-v")) {
                verbosityLevel = 3;
                continue;
            }
            if (tok.startsWith("-v")) {
                try {
                    verbosityLevel = Integer.parseInt(tok.substring("-v".length()));
                }
                catch (NumberFormatException numberformatexception) {
                    log.println("Verbosity level " + tok.substring("-v".length()) + " is not valid.  Option ignored.");
                }
                continue;
            }
            if (tok.equals("-d")) {
                tok = nextArg();
                if (tok != null) {
                    scratchDir = new File((new File(tok)).getAbsolutePath());
                    dirset = true;
                }
                else {
                    scratchDir = null;
                }
                continue;
            }
            if (tok.equals("-dd")) {
                tok = nextArg();
                if (tok != null) {
                    scratchDir = new File((new File(tok)).getAbsolutePath());
                    dirset = false;
                }
                else {
                    scratchDir = null;
                }
                continue;
            }
            if (tok.equals("-p")) {
                targetPackage = nextArg();
                continue;
            }
            if (tok.equals("-c")) {
                targetClassName = nextArg();
                continue;
            }
            if (tok.equals("-uribase")) {
                uriBase = nextArg();
                continue;
            }
            if (tok.equals("-uriroot")) {
                uriRoot = nextArg();
                continue;
            }
            if (tok.equals("-webinc")) {
                webxmlFile = nextArg();
                if (webxmlFile != null)
                    webxmlLevel = 10;
                continue;
            }
            if (tok.equals("-webxml")) {
                webxmlFile = nextArg();
                if (webxmlFile != null)
                    webxmlLevel = 20;
                continue;
            }
            if (tok.equals("-mapped")) {
                mappedFile = true;
                continue;
            }
            if (tok.startsWith("-die")) {
                try {
                    dieLevel = Integer.parseInt(tok.substring("-die".length()));
                }
                catch (NumberFormatException numberformatexception1) {
                    dieLevel = 1;
                }
                die = dieLevel;
                continue;
            }
            pushBackArg();
            break;
        }
        Constants.jasperLog = Log.getLog("JASPER_LOG", this);
    }

    public boolean parseFile(PrintStream log, String file, Writer servletout, Writer mappingout) {
        try {
            JasperLoader loader = new JasperLoader();
            loader.setParentClassLoader(getClass().getClassLoader());
            loader.setOptions(this);
            CommandLineContext clctxt = new CommandLineContext(loader, getClassPath(), file, uriBase, uriRoot, false, this);
            jdkCompat.setContextClassLoader(loader);
            if (targetClassName != null && targetClassName.length() > 0) {
                clctxt.setServletClassName(targetClassName);
                clctxt.lockClassName();
            }
            if (targetPackage != null) {
                clctxt.setServletPackageName(targetPackage);
                clctxt.lockPackageName();
            }
            if (dirset)
                clctxt.setOutputInDirs(true);
            File uriDir = new File(clctxt.getRealPath("/"));
            if (uriDir.exists()) {
                if ((new File(uriDir, "WEB-INF/classes")).exists())
                    loader.addJar(clctxt.getRealPath("/WEB-INF/classes"));
                File lib = new File(clctxt.getRealPath("WEB-INF/lib"));
                if (lib.exists() && lib.isDirectory()) {
                    String libs[] = lib.list();
                    for (int i = 0; i < libs.length; i++)
                        try {
                            loader.addJar(lib.getCanonicalPath() + File.separator + libs[i]);
                        }
                        catch (IOException ioe) {
                            throw new RuntimeException(ioe.toString());
                        }

                }
            }
            CommandLineCompiler clc = new CommandLineCompiler(clctxt);
            clc.compile();
            targetClassName = null;
            String thisServletName;
            String thisInternalName;
            if (clc.getPackageName() == null) {
                // Modif EG : Prefixe de JSP pour éviter les doublons avec servlet
                thisInternalName = "jsp" + clc.getClassName();
                thisServletName = clc.getClassName();
            }
            else {
                thisServletName = clc.getPackageName() + '.' + clc.getClassName();
                thisInternalName = thisServletName;
            }
            if (servletout != null) {
                servletout.write("\n\t<servlet>\n\t\t<servlet-name>");
                servletout.write(thisInternalName);
                servletout.write("</servlet-name>\n\t\t<servlet-class>");
                servletout.write(thisServletName);
                servletout.write("</servlet-class>\n\t</servlet>\n");
            }
            if (mappingout != null) {
                mappingout.write("\n\t<servlet-mapping>\n\t\t<servlet-name>");
                mappingout.write(thisInternalName);
                mappingout.write("</servlet-name>\n\t\t<url-pattern>");
                mappingout.write(file.replace('\\', '/'));
                mappingout.write("</url-pattern>\n\t</servlet-mapping>\n");
            }
            return true;
        }
        catch (JasperException je) {
            Constants.message("jspc.error.jasperException", new Object[] { file, je }, 1);
            je.printStackTrace();
            if (je.getRootCause() != null)
                je.getRootCause().printStackTrace();
            if (dieLevel != 0)
                dieOnExit = true;
        }
        catch (FileNotFoundException fne) {
            Constants.message("jspc.error.fileDoesNotExist", new Object[] { fne.getMessage()}, 2);
        }
        catch (Exception e) {
            Constants.message("jspc.error.generalException", new Object[] { file, e }, 1);
            e.printStackTrace();
            if (dieLevel != 0)
                dieOnExit = true;
        }
        return false;
    }

    public void parseFiles(PrintStream log) throws JasperException {
        boolean scratchDirSet = scratchDir != null;
        boolean urirootSet = uriRoot != null;
        if (scratchDir == null) {
            String temp = System.getProperty("java.io.tempdir");
            if (temp == null)
                temp = "";
            scratchDir = new File((new File(temp)).getAbsolutePath());
        }
        File f;
        for (f = new File(args[argPos]); !f.exists();) {
            boolean webApp = false;
            if ("-webapp".equals(args[argPos])) {
                webApp = true;
                if (args.length > argPos + 1) {
                    f = new File(args[argPos + 1]);
                }
                else {
                    Constants.message("jspc.error.emptyWebApp", 1);
                    return;
                }
            }
            if (!f.exists()) {
                Constants.message("jspc.error.fileDoesNotExist", new Object[] { f }, 2);
                argPos++;
                if (webApp)
                    argPos++;
                if (argPos >= args.length)
                    return;
                f = new File(args[argPos]);
            }
        }

        if (uriRoot == null) {
            if ("-webapp".equals(args[argPos]))
                if (args.length > argPos + 1)
                    f = new File(args[argPos + 1]);
                else
                    return;
            String tUriBase = uriBase;
            if (tUriBase == null)
                tUriBase = "/";
            try {
                if (f.exists()) {
                    String fParent;
                    for (f = new File(f.getCanonicalPath()); f != null; f = new File(fParent)) {
                        File g = new File(f, "WEB-INF");
                        if (g.exists() && g.isDirectory()) {
                            uriRoot = f.getCanonicalPath();
                            uriBase = tUriBase;
                            Constants.message("jspc.implicit.uriRoot", new Object[] { uriRoot }, 3);
                            break;
                        }
                        if (f.exists() && f.isDirectory())
                            tUriBase = "/" + f.getName() + "/" + tUriBase;
                        fParent = f.getParent();
                        if (fParent != null)
                            continue;
                        f = new File(args[argPos]);
                        fParent = f.getParent();
                        if (fParent == null)
                            fParent = File.separator;
                        uriRoot = (new File(fParent)).getCanonicalPath();
                        uriBase = "/";
                        break;
                    }

                }
            }
            catch (IOException ioexception) {
            }
        }
        String file = nextFile();
        File froot = new File(uriRoot);
        String ubase = null;
        try {
            ubase = froot.getCanonicalPath();
        }
        catch (IOException ioexception1) {
        }
        for (; file != null; file = nextFile())
            if ("-webapp".equals(file)) {
                String base = nextFile();
                if (base == null) {
                    Constants.message("jspc.error.emptyWebApp", 1);
                    return;
                }
                String oldRoot = uriRoot;
                if (!urirootSet)
                    uriRoot = base;
                Vector pages = new Vector();
                Stack dirs = new Stack();
                dirs.push(base);
                if (extensions == null) {
                    extensions = new Vector();
                    extensions.addElement("jsp");
                }
                while (!dirs.isEmpty()) {
                    String s = dirs.pop().toString();
                    f = new File(s);
                    if (f.exists() && f.isDirectory()) {
                        String files[] = f.list();
                        for (int i = 0; i < files.length; i++) {
                            File f2 = new File(s, files[i]);
                            if (f2.isDirectory()) {
//                                dirs.push(f2.getPath());
                                log.println("Répertoire ignoré = " + f2.getPath());
                            }
                            else {
                                String ext = files[i].substring(files[i].lastIndexOf('.') + 1);
                                if (extensions.contains(ext))
                                    pages.addElement(s + File.separatorChar + files[i]);
                            }
                        }

                    }
                }
                String ubaseOld = ubase;
                File frootOld = froot;
                froot = new File(uriRoot);
                try {
                    ubase = froot.getCanonicalPath();
                }
                catch (IOException ioexception3) {
                }
                Writer mapout;
                CharArrayWriter servletout;
                CharArrayWriter mappingout;
                File realWebXml = null;
                BufferedReader xmlIS = null;
                try {
                    if (webxmlLevel >= 10) {
                        File fmapings = new File(webxmlFile);
                        mapout = new FileWriter(fmapings);
                        servletout = new CharArrayWriter();
                        mappingout = new CharArrayWriter();
                        
                        // Modif EG
                        // Ajoute le debut du vrai fichier Web.XML
                        realWebXml = new File(fmapings.getParent() + "/web.xml");
                        log.println("WebXml = " + realWebXml.toString());
                        xmlIS = new BufferedReader(new FileReader(realWebXml));
                        String ligne = null;
                        do {
                            ligne = xmlIS.readLine();
                            if ((ligne != null) && (ligne.indexOf("**** Include Here ***") == -1)) {
                                mapout.write(ligne);
                            }
                        }
                        while ((ligne != null) && (ligne.indexOf("**** Include Here ***") == -1));
                        
                    }
                    else {
                        mapout = null;
                        servletout = null;
                        mappingout = null;
                    }
                    if (webxmlLevel >= 20)
                        mapout.write(Constants.getString("jspc.webxml.header"));
                    else if (webxmlLevel >= 10)
                        mapout.write(Constants.getString("jspc.webinc.header"));
                }
                catch (IOException ioexception4) {
                    mapout = null;
                    servletout = null;
                    mappingout = null;
                }
                String nextjsp;
                for (Enumeration e = pages.elements(); e.hasMoreElements(); parseFile(log, nextjsp, servletout, mappingout)) {
                    nextjsp = e.nextElement().toString();
                    try {
                        if (ubase != null) {
                            File fjsp = new File(nextjsp);
                            String s = fjsp.getCanonicalPath();
                            if (s.startsWith(ubase))
                                nextjsp = s.substring(ubase.length());
                        }
                    }
                    catch (IOException ioexception6) {
                    }
                    if (nextjsp.startsWith("." + File.separatorChar))
                        nextjsp = nextjsp.substring(2);
                }

                uriRoot = oldRoot;
                ubase = ubaseOld;
                froot = frootOld;
                if (mapout != null)
                    try {
                        // Modif EG : Eclate l'écriture...
                        servletout.writeTo(mapout);
                        String ligne = null;
                        do {
                            ligne = xmlIS.readLine();
                            if ((ligne != null) && (ligne.indexOf("**** Include Here - Part 2 ***") == -1)) {
                                mapout.write(ligne);
                            }
                        }
                        while ((ligne != null) && (ligne.indexOf("**** Include Here - Part 2 ***") == -1));
                        mappingout.writeTo(mapout);
                        if (webxmlLevel >= 20)
                            mapout.write(Constants.getString("jspc.webxml.footer"));
                        else if (webxmlLevel >= 10)
                            mapout.write(Constants.getString("jspc.webinc.footer"));
                        do {
                            ligne = xmlIS.readLine();
                            if (ligne != null) {
                                mapout.write(ligne);
                            }
                        }
                        while (ligne != null);
                        xmlIS.close();
                        mapout.close();
                    }
                    catch (IOException ioexception5) {
                    }
            }
            else {
                try {
                    if (ubase != null) {
                        File fjsp = new File(file);
                        String s = fjsp.getCanonicalPath();
                        if (s.startsWith(ubase))
                            file = s.substring(ubase.length());
                    }
                }
                catch (IOException ioexception2) {
                }
                parseFile(log, file, null, null);
            }

        if (dieOnExit)
            System.exit(die);
    }

    public static void main(String arg[]) {
        if (arg.length == 0)
            System.out.println(Constants.getString("jspc.usage"));
        else
            try {
                JspC jspc = new JspC(arg, System.out);
                jspc.parseFiles(System.out);
            }
            catch (JasperException je) {
                System.err.print("error:");
                System.err.println(je.getMessage());
                if (die != 0)
                    System.exit(die);
            }
    }

    public static final String DEFAULT_IE_CLASS_ID = "clsid:8AD9C840-044E-11D1-B3E9-00805F499D93";
    public static final String SWITCH_VERBOSE = "-v";
    public static final String SWITCH_QUIET = "-q";
    public static final String SWITCH_OUTPUT_DIR = "-d";
    public static final String SWITCH_OUTPUT_SIMPLE_DIR = "-dd";
    public static final String SWITCH_IE_CLASS_ID = "-ieplugin";
    public static final String SWITCH_PACKAGE_NAME = "-p";
    public static final String SWITCH_CLASS_NAME = "-c";
    public static final String SWITCH_FULL_STOP = "--";
    public static final String SWITCH_URI_BASE = "-uribase";
    public static final String SWITCH_URI_ROOT = "-uriroot";
    public static final String SWITCH_FILE_WEBAPP = "-webapp";
    public static final String SWITCH_WEBAPP_INC = "-webinc";
    public static final String SWITCH_WEBAPP_XML = "-webxml";
    public static final String SWITCH_MAPPED = "-mapped";
    public static final String SWITCH_DIE = "-die";
    public static final int NO_WEBXML = 0;
    public static final int INC_WEBXML = 10;
    public static final int ALL_WEBXML = 20;
    public static final int DEFAULT_DIE_LEVEL = 1;
    public static final int NO_DIE_LEVEL = 0;
    boolean largeFile;
    boolean mappedFile;
    int jspVerbosityLevel;
    File scratchDir;
    String ieClassId;
    String targetPackage;
    String targetClassName;
    String uriBase;
    String uriRoot;
    String webxmlFile;
    int webxmlLevel;
    int dieLevel;
    boolean dieOnExit;
    static int die;
    boolean dirset;
    Vector extensions;
    int argPos;
    boolean fullstop;
    String args[];
    static Jdk11Compat jdkCompat = Jdk11Compat.getJdkCompat();

}