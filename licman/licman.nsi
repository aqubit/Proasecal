
;--------------------------------

; The name of the installer
Name "LicMan Proasecal 2012"

; The file to write
OutFile "target\licman_installer.exe"

; The default installation directory
InstallDir $PROGRAMFILES\LicMan

; Registry key to check for directory (so if you install again, it will 
; overwrite the old one automatically)
InstallDirRegKey HKLM "Software\LicMan" "Install_Dir"

; Request application privileges for Windows Vista
RequestExecutionLevel admin

;--------------------------------

; Pages

Page components
Page directory
Page instfiles

UninstPage uninstConfirm
UninstPage instfiles

;--------------------------------

; First is default
LoadLanguageFile "${NSISDIR}\Contrib\Language files\Spanish.nlf"

;--------------------------------

;--------------------------------

Function .onInit

	;Language selection dialog

	Push ""
	Push ${LANG_SPANISH}
	Push Spanish
	Push A ; A means auto count languages
	       ; for the auto count to work the first empty push (Push "") must remain
	LangDLL::LangDialog "Installer Language" "Please select the language of the installer"

	Pop $LANGUAGE
	StrCmp $LANGUAGE "cancel" 0 +2
		Abort
FunctionEnd


; The stuff to install
Section "LicMan (required)"

  SectionIn RO
  
  ; Set output path to the installation directory.
  SetOutPath $INSTDIR
  
  SetOverwrite on

  ; Put file there
  File /r "target\licman-1.0.0\lib"
  File "target\licman-1.0.0.jar"
  File /r "installer\*.*"
  File /r "D:\sfw\Java32\jre7"
  File /r "D:\workspace\proasecal\plugin\target\plugin-1.0.0"

  ; Write the installation path into the registry
  WriteRegStr HKLM SOFTWARE\LicMan "Install_Dir" "$INSTDIR"
  
  ; Write the uninstall keys for Windows
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\LicMan" "DisplayName" "LicMan Proasecal 2012"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\LicMan" "UninstallString" '"$INSTDIR\uninstall.exe"'
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\LicMan" "NoModify" 1
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\LicMan" "NoRepair" 1
  WriteUninstaller "uninstall.exe"
  
SectionEnd

; Optional section (can be disabled by the user)
Section "Start Menu Shortcuts"

  SectionIn RO
  CreateDirectory "$SMPROGRAMS\LicMan"
  CreateShortCut "$SMPROGRAMS\LicMan\Uninstall.lnk" "$INSTDIR\uninstall.exe" "" "$INSTDIR\uninstall.exe" 0
  CreateShortCut "$SMPROGRAMS\LicMan\LicMan Proasecal 2012.lnk" "$INSTDIR\licman.bat" "" "$INSTDIR\licman.bat" 0
  
SectionEnd

;--------------------------------

; Uninstaller

Section "Uninstall"
  
  ; Remove registry keys
  DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\LicMan"
  DeleteRegKey HKLM SOFTWARE\NSIS_LicMan

  ; Remove files and uninstaller
  Push  "$INSTDIR"
  
  ; Remove shortcuts, if any
  Delete "$SMPROGRAMS\LicMan\*.*"

  ; Remove directories used
  RMDir "$SMPROGRAMS\LicMan"
  RMDir /r "$INSTDIR"

SectionEnd
