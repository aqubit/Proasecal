@echo off

set basedir=%~f0
:strip
set removed=%basedir:~-1%
set basedir=%basedir:~0,-1%
if NOT "%removed%"=="\" goto strip
set LICMAN_HOME=%basedir%
set LOCAL_JAVA=%LICMAN_HOME%\jre7\bin\javaw.exe
start "Licman" /B "%LOCAL_JAVA%" -jar -Xmx512m -splash:"proasecal.png" -XX:-UseParallelGC licman-1.0.0.jar

