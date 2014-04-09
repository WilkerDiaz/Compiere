@Title Compiere Migration
@Rem  $Id: CLMigrate.bat 8377 2010-01-27 22:39:24Z freyes $
@Echo off

@IF NOT [%1]==[] goto ARG_PASS
@Echo Configuration file missing
@Echo Please edit the configuration file appropriately and pass the file name to this script
@Echo Usage: CLMigrate.bat CLConfiguration.bat
@EXIT /B 1

:ARG_PASS
echo Calling CLInstall "--migrate y"
@CALL CLInstall.bat %1 "--migrate y"
@IF ERRORLEVEL = 1 ((echo Migration error)&&(EXIT /B 1))

