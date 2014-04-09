@Title Build Compiere Installer
@Rem Copyright Compiere, Inc.
@Rem  $Id: RUN_build.bat 8082 2009-09-17 23:17:21Z freyes $
@Echo off

@CALL ..\utils_dev\myDevEnv.bat
@IF NOT "%COMPIERE_ENV%"=="Y" GOTO NOBUILD

@echo Cleanup ...
@"%JAVA_HOME%\bin\java" -Dant.home="." %ANT_PROPERTIES% org.apache.tools.ant.Main clean

@echo Building ...
@"%JAVA_HOME%\bin\java" -Dant.home="." %ANT_PROPERTIES% org.apache.tools.ant.Main 

@Echo Done ...
@sleep 60
@exit /b

:NOBUILD
@Echo Check myDevEnv.bat (copy from myDevEnvTemplate.bat)
@Pause
