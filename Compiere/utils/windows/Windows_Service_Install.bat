@echo off

REM $Id: Windows_Service_Install.bat,v 1.2 2003/11/01 20:55:15 comdivisionys Exp $

if (%COMPIERE_HOME%) == () (CALL myEnvironment.bat Server) else (CALL %COMPIERE_HOME%\utils\myEnvironment.bat Server)

%COMPIERE_HOME%\utils\windows\JavaService.exe -install Compiere "%JAVA_HOME%\jre\bin\server\jvm.dll" -Xmx256M -Djava.class.path="%JAVA_HOME%\lib\tools.jar;%COMPIERE_HOME%\jboss\bin\run.jar" -server %COMPIERE_JAVA_OPTIONS% -Djetty.port=%COMPIERE_WEB_PORT% -Djetty.ssl=%COMPIERE_SSL_PORT% -Djetty.keystore=%COMPIERE_KEYSTORE% -Djetty.password=%COMPIERE_KEYSTORE_PASSWORD% -start org.jboss.Main -params -c compiere -b %COMPIERE_APPS_SERVER% -stop org.jboss.Main -method systemExit -out %COMPIERE_HOME%\jboss\bin\out.txt -current %COMPIERE_HOME%\jboss\bin
