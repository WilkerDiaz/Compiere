@Title Uninstall Application
@Echo off


@if not "%JAVA_HOME%" == "" goto JAVA_HOME_OK
@Set JAVA=java
@Echo JAVA_HOME is not set.  
@Echo Set JAVA_HOME to the directory of your local 1.5 JDK.
@Echo If you experience problems, run utils/WinEnv.js
@Echo Example: cscript utils\WinEnv.js C:\Compiere2 "C:\Program Files\Java\jdk1.5.0_04"
goto START

:JAVA_HOME_OK
@Set JAVA=%JAVA_HOME%\bin\java


:START
@Echo =======================================
@Echo Starting uninstall process ...
@Echo =======================================

:CHECK_COMPIERE
@if not "%COMPIERE_HOME%" == "" goto COMPIERE_HOME_OK
@SET CP=lib\CInstall.jar;lib\CompiereInstall.jar;lib\jPDF.jar;lib\CCTools.jar;lib\oracle.jar;lib\jboss.jar;lib\db2.jar;lib\postgreSQL.jar;lib\sqlServer.jar
set COMPIERE_HOME=%~dp0..
@Echo COMPIERE_HOME is not set.
@Echo   You may not be able to start Compiere
@Echo   Set COMPIERE_HOME to the directory of Compiere2.
@Echo   You could set it via WinEnv.js e.g.:
@Echo     cscript WinEnv.js C:\Compiere2 C:\j2sdk1.4.2_08
@goto DONE 
:COMPIERE_HOME_OK
@SET CP=%COMPIERE_HOME%\lib\CInstall.jar;%COMPIERE_HOME%\lib\CompiereInstall.jar;%COMPIERE_HOME%\lib\jPDF.jar;%COMPIERE_HOME%\lib\CCTools.jar;%COMPIERE_HOME%\lib\oracle.jar;%COMPIERE_HOME%\lib\jboss.jar;%COMPIERE_HOME%\lib\db2.jar;%COMPIERE_HOME%\lib\postgreSQL.jar;%COMPIERE_HOME%\lib\sqlServer.jar

@Rem Set Parameters 
@SET ENTITYTYPE="%1"
@if "%1" == "" goto NO_ENTITYTYPE
@SET ENTITYTYPE="%1"

@if "%2" == "" goto SET_RECORD_DEFAULT
@SET DELETERECORD="%2"
goto DELETE_FILE

:SET_RECORD_DEFAULT
@SET DELETERECORD="N"

:DELETE_FILE
@if "%3" == ""  goto SET_FILE_DEFAULT
@SET DELETEFILE="%3" 
goto EXECUTE

:SET_FILE_DEFAULT
@SET DELETEFILE="Y"

:EXECUTE
@"%JAVA%" -Xmx258m -classpath %CP% -DCOMPIERE_HOME=%COMPIERE_HOME% org.compiere.util.UninstallComponent -E %ENTITYTYPE% -R %DELETERECORD% -F %DELETEFILE% 
goto DONE

:NO_ENTITYTYPE
@echo Usage: %0 "<EntityType>" "<DeleleRecord>" "<DeleteFile>"
@echo Example: %0 ENTY N Y

:DONE
