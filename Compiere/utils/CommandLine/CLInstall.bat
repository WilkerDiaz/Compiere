@Title Install Compiere 
@Rem  $Id: CLInstall.bat 9222 2010-09-17 00:26:20Z freyes $
@Echo off

TYPE License.txt

@IF NOT [%1]==[] goto ARG_PASS
@Echo Configuration file missing
@Echo Please edit the configuration file appropriately and pass the file name to this script
@Echo Usage: CLInstall.bat CLConfiguration.bat
@EXIT /B 1

:ARG_PASS
@Echo Calling configuration script
@CALL %1
@IF ERRORLEVEL 1 ((echo.)&&(echo ERROR calling configuration script %1)&&(exit /b 1))

set migrate=%~2
@@IF NOT "%migrate%"=="" echo Additional Parameters: %migrate%

@if not "%JAVA_HOME%" == "" goto JAVA_HOME_OK
@Set JAVA=java
@Echo JAVA_HOME is not set.  
@Echo You may not be able to start the required Setup !!
@Echo Set JAVA_HOME to the directory of your local 1.6 JDK.
@Echo If you experience problems, run utils/WinEnv.js
@Echo Example: cscript utils\WinEnv.js C:\Compiere2 "C:\Program Files\Java\jdk1.6.0_04"
goto START

:JAVA_HOME_OK
@Set JAVA=%JAVA_HOME%\bin\java


:START
@Echo =======================================
@Echo Starting Setup 
@Echo =======================================
@SET CP=%CL_COMPIERE_HOME%\lib\CInstall.jar;%CL_COMPIERE_HOME%\lib\CompiereInstall.jar;%CL_COMPIERE_HOME%\lib\jPDF.jar;%CL_COMPIERE_HOME%\lib\CCTools.jar;%CL_COMPIERE_HOME%\lib\oracle.jar;%CL_COMPIERE_HOME%\lib\jboss.jar;%CL_COMPIERE_HOME%\lib\db2.jar;%CL_COMPIERE_HOME%\lib\postgreSQL.jar;%CL_COMPIERE_HOME%\lib\sqlServer.jar;

@Rem Parameter
@SET ARGS=--javahome "%CL_JAVA_HOME%" --javatype %CL_JAVA_TYPE% --compierehome %CL_COMPIERE_HOME% --keystorepass %CL_COMPIERE_KEYSTOREPASS% --appservhost %CL_COMPIERE_APPS_SERVER% --appservtype %CL_COMPIERE_APPS_TYPE% --appservdeploydir "%CL_APPS_SERVER_DEPLOY_DIR%" --appservjnpport %CL_COMPIERE_JNP_PORT% --appservwebport %CL_COMPIERE_WEB_PORT% --appservsslport %CL_COMPIERE_SSL_PORT% --dbhost %CL_COMPIERE_DB_SERVER% --dbtype %CL_COMPIERE_DB_TYPE% --dbname %CL_COMPIERE_DB_NAME% --dbport %CL_COMPIERE_DB_PORT% --dbsystempass %CL_COMPIERE_DB_SYSTEM% --dbuser %CL_COMPIERE_DB_USER% --dbpass %CL_COMPIERE_DB_PASSWORD% --mailservhost %CL_MAIL_SERVER% --adminemail %CL_ADMIN_EMAIL% --mailuser %CL_MAIL_USER% --mailpass %CL_MAIL_PASSWORD% 

@"%JAVA%" -Xmx258m -classpath %CP% -DCOMPIERE_HOME=%CL_COMPIERE_HOME% org.compiere.install.ConfigurationData %ARGS%
@Echo ErrorLevel = %ERRORLEVEL%

@IF NOT ERRORLEVEL = 1 GOTO FILEINSTALL
@Echo ***************************************
@Echo Check the error message above.
@Echo ***************************************
@Echo Make sure that the environment is set correctly!
@Echo Make sure the paths and names are correct in the configuartion file provided - %1
@Echo ***************************************
@Exit /b 1   


:FILEINSTALL
@Echo Continue with Component Install
@Rem Pause
@"%JAVA%" -Xmx500m -classpath %CP% -DCOMPIERE_HOME=%CL_COMPIERE_HOME% com.compiere.client.CommandLineInstall --email %CL_WEBSTORE_EMAIL% --sysname "%CL_SYSTEM_NAME%" --pwd "%CL_WEBSTORE_PASS%" %migrate%
@Echo ErrorLevel = %ERRORLEVEL%

@IF NOT ERRORLEVEL = 1 GOTO UPDATELICENSE
@Echo ***************************************
@Echo Check the error message above.
@Echo ***************************************
@Echo Make sure that the environment is set correctly!
@Echo Make sure the paths and names are correct in the configuartion file provided - %1
@Echo ***************************************
@Exit /B 1

:UPDATELICENSE
@Rem ===================================
@Rem Setup Compiere Environment for Updating License Information
@Rem ===================================
@CALL ..\RUN_WinEnv.bat
@CALL ..\myEnvironment.bat
@ECHO continue with Updating Licenses
@"%COMPIERE_JAVA%" %COMPIERE_JAVA_OPTIONS% -cp %CLASSPATH%;%CP% com.compiere.client.UpdateLicense --sysname "%CL_SYSTEM_NAME%" --email %CL_WEBSTORE_EMAIL% --password "%CL_WEBSTORE_PASS%"
@Echo ErrorLevel = %ERRORLEVEL%
@IF NOT ERRORLEVEL = 1 GOTO NEXT2
@Echo **********************************************
@Echo An error occurred while running the Update License
@Echo Please check the log file in %COMPIERE_HOME%\log directory for details...
@Echo **********************************************
@Rem Pause
@Exit /B 1

:NEXT2
@Echo *********************************************************
@Echo The program Update License has completed successfully without any errors.
@Echo *********************************************************
@Echo Compiere has been installed succesfully.
@Rem Pause
@Exit /b 0

