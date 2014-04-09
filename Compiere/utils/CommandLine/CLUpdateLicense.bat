@Rem  $Id: CLUpdateLicense.bat 7828 2009-07-01 20:46:40Z freyes $
@Echo off

@Rem pre requisites:
@Rem	1) This script needs to be placed in  %COMPIERE_HOME%\utils\CommandLine directory of a successful Compiere Installation
@Rem	2) Database connection information is obtained from Compiere.properties in  %COMPIERE_HOME% directory.
@Rem	Hence needs a valid Compiere.properties in %COMPIERE_HOME% directory
@Rem	3) Database needs to be up and running

@CALL ..\myEnvironment.bat Server
@Title Compiere Support - %COMPIERE_HOME% (%COMPIERE_DB_NAME%)

@Echo Calling UpdateLicense ...

@Rem ==================================================================================
@Rem Please edit the command below to supply the System Name, web store email and password
@Rem Usage: "%COMPIERE_JAVA%"w %COMPIERE_JAVA_OPTIONS% -cp %COMPIERE_HOME%\utils\UpdateLicense.jar;%CLASSPATH% com.compiere.client.UpdateLicense --sysname xxx --email xxx --password xxx
@Rem Without any arguments the program uses the credentials stored earlier
@Rem If the values have blank spaces be sure to enclose them in double quotes
@Rem ==================================================================================

@"%COMPIERE_JAVA%" %COMPIERE_JAVA_OPTIONS% -cp %CLASSPATH% com.compiere.client.UpdateLicense

@Echo ErrorLevel = %ERRORLEVEL%
@IF NOT ERRORLEVEL = 1 GOTO NEXT
@Echo **********************************************
@Echo An error occurred while running the program. 
@Echo Please check the log files for details...
@Echo **********************************************
@Exit /B 1
:NEXT
@Echo *********************************************************
@Echo The program has completed successfully without any errors
@Echo Please check the log files for details...
@Echo *********************************************************

