; Script generated by the HM NIS Edit Script Wizard.

; HM NIS Edit Wizard helper defines
!define PRODUCT_NAME "InCrEG LibertyLook"
!define PRODUCT_VERSION "4.7"
!define PRODUCT_VERSION_FULL "${PRODUCT_VERSION}.1.1"
!define PRODUCT_COPYRIGHT "2002-2014 Val�rie Guyot, Alexandre Guyot, Emmanuel Guyot et Angel"
!define PRODUCT_PUBLISHER "SourceForge"
!define PRODUCT_WEB_SITE "http://libertylook.emmguyot.com/"
!define PRODUCT_UNINST_KEY "Software\Microsoft\Windows\CurrentVersion\Uninstall\${PRODUCT_NAME}"
!define PRODUCT_UNINST_ROOT_KEY "HKLM"
!define PRODUCT_STARTMENU_REGVAL "NSIS:StartMenuDir"
!define ORIG_DIR "c:\InCrEG.salon"
!define LICENCE_TXT "..\..\LicenceGPL.txt"

SetCompressor lzma

; MUI 1.67 compatible ------
!include "MUI.nsh"

; MUI Settings
!define MUI_ABORTWARNING
!define MUI_ICON "..\..\images\favicon.ico"
!define MUI_UNICON "..\..\images\favicon.ico"

; Welcome page
!define MUI_WELCOMEFINISHPAGE_BITMAP "images\install\welcome.bmp"
!define MUI_UNWELCOMEFINISHPAGE_BITMAP "images\install\welcome.bmp"
!define MUI_WELCOMEPAGE_TITLE "Installation de $(^NameDA)"
!insertmacro MUI_PAGE_WELCOME

; License page
!insertmacro MUI_PAGE_LICENSE "${LICENCE_TXT}"
; Start menu page
var ICONS_GROUP
!define MUI_STARTMENUPAGE_NODISABLE
!define MUI_STARTMENUPAGE_DEFAULTFOLDER "${PRODUCT_NAME}"
!define MUI_STARTMENUPAGE_REGISTRY_ROOT "${PRODUCT_UNINST_ROOT_KEY}"
!define MUI_STARTMENUPAGE_REGISTRY_KEY "${PRODUCT_UNINST_KEY}"
!define MUI_STARTMENUPAGE_REGISTRY_VALUENAME "${PRODUCT_STARTMENU_REGVAL}"
!insertmacro MUI_PAGE_STARTMENU Application $ICONS_GROUP
; Instfiles page
!insertmacro MUI_PAGE_INSTFILES
; Finish page
!define MUI_FINISHPAGE_SHOWREADME "$INSTDIR\Manuel.url"
!define MUI_FINISHPAGE_SHOWREADME_TEXT "Afficher le manuel utilisateur"
!define MUI_FINISHPAGE_RUN_TEXT "Ex�cuter l'outil de v�rification de l'environnement"
!define MUI_FINISHPAGE_RUN
!define MUI_FINISHPAGE_RUN_FUNCTION "LaunchVerif"
!insertmacro MUI_PAGE_FINISH

; Uninstaller pages
!insertmacro MUI_UNPAGE_INSTFILES

; Language files
!insertmacro MUI_LANGUAGE "French"

; Reserve files
!insertmacro MUI_RESERVEFILE_INSTALLOPTIONS

!include "strfunc.nsh"

; MUI end ------

Name "${PRODUCT_NAME} ${PRODUCT_VERSION}"
OutFile "c:/build/dist/InCrEG_LibertyLook_salon_${PRODUCT_VERSION}.exe"
InstallDir "c:\InCrEG\"
ShowInstDetails nevershow
ShowUnInstDetails nevershow
VIAddVersionKey ProductName "${PRODUCT_NAME}"
VIAddVersionKey CompanyName "${PRODUCT_PUBLISHER}"
VIAddVersionKey ProductVersion "${PRODUCT_VERSION}"
VIAddVersionKey FileVersion "${PRODUCT_VERSION}"
VIAddVersionKey FileDescription "${PRODUCT_NAME}"
VIAddVersionKey LegalCopyright "${PRODUCT_COPYRIGHT}"
VIProductVersion "${PRODUCT_VERSION_FULL}"

Section "Fichiers principaux" SEC00
  ; N�ttoyage avant
  RMDir /r "$SMPROGRAMS\$ICONS_GROUP"
  Delete "$INSTDIR\*.*"
  RMDir /r "$INSTDIR\install"
  RMDir /r "$INSTDIR\jdk"
  RMDir /r "$INSTDIR\pgsql"
  RMDir /r "$INSTDIR\tomcat"
  RMDir /r "$INSTDIR\war"
  RMDir /r "$INSTDIR\Temp"
  ; Ajout des fichiers
  SetOutPath "$INSTDIR"
  SetOverwrite ifnewer
  File "${ORIG_DIR}\*.*"
  File /r "${ORIG_DIR}\install"
  File /r "${ORIG_DIR}\jdk"
  File /r "${ORIG_DIR}\perso"
  File /r "${ORIG_DIR}\pgsql"
  File /r "${ORIG_DIR}\tomcat"
  File /r "${ORIG_DIR}\war"
  
  CreateDirectory "$INSTDIR\Base"
  CreateDirectory "$INSTDIR\jdk\jre\lib\applet"
  CreateDirectory "$INSTDIR\jdk\jre\lib\ext"
  CreateDirectory "$INSTDIR\Sauvegardes"
  CreateDirectory "$INSTDIR\Temp"
  CreateDirectory "$INSTDIR\tomcat\common\classes"
  CreateDirectory "$INSTDIR\tomcat\common\endorsed"
  CreateDirectory "$INSTDIR\tomcat\conf\Catalina\localhost"
  CreateDirectory "$INSTDIR\tomcat\logs"
  CreateDirectory "$INSTDIR\tomcat\server\classes"
  CreateDirectory "$INSTDIR\tomcat\server\webapps"
  CreateDirectory "$INSTDIR\tomcat\shared\classes"
  CreateDirectory "$INSTDIR\tomcat\temp"
  CreateDirectory "$INSTDIR\tomcat\webapps"
  CreateDirectory "$INSTDIR\tomcat\work"
  
  CreateShortCut "$SMPROGRAMS\$ICONS_GROUP\1 - D�marrage ${PRODUCT_NAME}.lnk" "$INSTDIR\DropMyRights.exe" "$INSTDIR\LaunchWeb.bat" "$INSTDIR\LaunchWeb.ico" 0 SW_SHOWMINIMIZED
  CreateShortCut "$DESKTOP\1 - D�marrage ${PRODUCT_NAME}.lnk" "$INSTDIR\DropMyRights.exe" "$INSTDIR\LaunchWeb.bat" "$INSTDIR\LaunchWeb.ico" 0 SW_SHOWMINIMIZED
  CreateShortCut "$SMSTARTUP\1 - D�marrage ${PRODUCT_NAME}.lnk" "$INSTDIR\DropMyRights.exe" "$INSTDIR\LaunchWeb.bat" "$INSTDIR\LaunchWeb.ico" 0 SW_SHOWMINIMIZED
  ;
  WriteIniStr "$INSTDIR\${PRODUCT_NAME}.url" "InternetShortcut" "URL" "http://localhost/salon/"
  CreateShortCut "$SMPROGRAMS\$ICONS_GROUP\2 - ${PRODUCT_NAME}.lnk" "$INSTDIR\${PRODUCT_NAME}.url" "" "$INSTDIR\favicon.ico"
  CreateShortCut "$DESKTOP\2 - ${PRODUCT_NAME}.lnk" "$INSTDIR\${PRODUCT_NAME}.url" "" "$INSTDIR\favicon.ico"
  ;
  CreateShortCut "$SMPROGRAMS\$ICONS_GROUP\3 - Arr�t ${PRODUCT_NAME}.lnk" "$INSTDIR\StopWeb.bat" "" "$INSTDIR\StopWeb.ico" 0 SW_SHOWMINIMIZED
  CreateShortCut "$DESKTOP\3 - Arr�t ${PRODUCT_NAME}.lnk" "$INSTDIR\StopWeb.bat" "" "$INSTDIR\StopWeb.ico" 0 SW_SHOWMINIMIZED
  ;
  WriteIniStr "$INSTDIR\Manuel.url" "InternetShortcut" "URL" "${PRODUCT_WEB_SITE}Guide-utilisation-logiciel-salon-de-coiffure.193.0.html"
  CreateShortCut "$SMPROGRAMS\$ICONS_GROUP\Manuel Utilisateur.lnk" "$INSTDIR\Manuel.url"

  WriteRegStr HKEY_CLASSES_ROOT "InternetShortcut\shell\open\command" "" "iexplore -k %l"
  
SectionEnd

Section -Core
  SetOutPath "$INSTDIR"
  SetOverwrite ifnewer
  File /oname=Licence.txt "${LICENCE_TXT}"
SectionEnd


Section -Post
  WriteUninstaller "$INSTDIR\uninst.exe"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DisplayName" "$(^Name)"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "UninstallString" "$INSTDIR\uninst.exe"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DisplayVersion" "${PRODUCT_VERSION}"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "${PRODUCT_STARTMENU_REGVAL}" "$ICONS_GROUP"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "URLInfoAbout" "${PRODUCT_WEB_SITE}"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "Publisher" "${PRODUCT_PUBLISHER}"
SectionEnd

Function un.onUninstSuccess
  HideWindow
  MessageBox MB_ICONINFORMATION|MB_OK "$(^Name) a �t� d�sinstall� avec succ�s de votre ordinateur."
FunctionEnd

Function un.onInit
  MessageBox MB_ICONQUESTION|MB_YESNO|MB_DEFBUTTON2 "�tes-vous certains de vouloir d�sinstaller totalement $(^Name) et tous ses composants ?" IDYES +2
  Abort
FunctionEnd

Function CheckWindowsVersion

   Push $R0
   Push $R1

   ReadRegStr $R0 HKLM "SOFTWARE\Microsoft\Windows NT\CurrentVersion" "CurrentVersion"

   IfErrors lbl_error lbl_winnt

   lbl_winnt:
             StrCpy $R1 $R0 1

             StrCmp $R1 '3' lbl_winnt_x
             StrCmp $R1 '4' lbl_winnt_x

             StrCpy $R1 $R0 3

             StrCmp $R1 '5.0' lbl_winnt_2000
             StrCmp $R1 '5.1' lbl_winnt_XP
             StrCmp $R1 '5.2' lbl_winnt_2003
             StrCmp $R1 '6.0' lbl_winvista
             StrCmp $R1 '6.1' lbl_win7
             StrCmp $R1 '6.2' lbl_win8
             StrCmp $R1 '6.3' lbl_win8 lbl_error

   lbl_winnt_x:
               StrCpy $R0 "NT $R0" 6
               Goto lbl_done

   lbl_winnt_2000:
                  Strcpy $R0 '2000'
                  Goto lbl_done

   lbl_winnt_XP:
                Strcpy $R0 'XP'
                Goto lbl_done

   lbl_winnt_2003:
                  Strcpy $R0 '2003'
                  Goto lbl_done

   lbl_winvista:
                  Strcpy $R0 'Vista'
                  Goto lbl_done

   lbl_win7:
                  Strcpy $R0 'W7'
                  Goto lbl_done

   lbl_win8:
                  Strcpy $R0 'W8'
                  Goto lbl_done

   lbl_error:
                  Strcpy $R0 ''
   lbl_done:

            Pop $R1
            Exch $R0

FunctionEnd

Function .onInit
  Push $R0
  call CheckWindowsVersion
  Pop $R0
  StrCmp $R0 "" 0 versionOk
  MessageBox MB_ICONSTOP|MB_OK "Ce logiciel ne peut pas fonctionner sur cette version de Windows."
  Abort
  versionOk:
  Pop $R0
FunctionEnd

Section Uninstall
  ReadRegStr $ICONS_GROUP ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "${PRODUCT_STARTMENU_REGVAL}"

  RMDir /r "$SMPROGRAMS\$ICONS_GROUP"
  Delete "$INSTDIR\*.*"
  RMDir /r "$INSTDIR\install"
  RMDir /r "$INSTDIR\jdk"
  RMDir /r "$INSTDIR\perso"
  RMDir /r "$INSTDIR\pgsql"
  RMDir /r "$INSTDIR\tomcat"
  RMDir /r "$INSTDIR\war"
  RMDir /r "$INSTDIR\Base"
  RMDir /r "$INSTDIR\Temp"

  Delete "$DESKTOP\1 - D�marrage ${PRODUCT_NAME}.lnk"
  Delete "$SMSTARTUP\1 - D�marrage ${PRODUCT_NAME}.lnk"
  Delete "$DESKTOP\2 - ${PRODUCT_NAME}.lnk"
  Delete "$DESKTOP\3 - Arr�t ${PRODUCT_NAME}.lnk"

  DeleteRegKey ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}"
  SetAutoClose true
SectionEnd

Function LaunchVerif
   Exec '"$INSTDIR\jdk\jre\bin\javaw.exe" -cp $INSTDIR\install com.emmguyot.checkinstall.CheckInstall'
FunctionEnd