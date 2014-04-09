@Rem	Configuration parameters used for Compiere command line installation
@Rem $Id: CLConfigurationTemplate.bat 8377 2010-01-27 22:39:24Z freyes $ 

@REM The publicly visible host name.  DO NOT USE localhost.
@SET PUBLIC_HOSTNAME=%COMPUTERNAME%

echo Setting Command Line Configuration ....

@Rem Web store credentials for license checking
@SET CL_SYSTEM_NAME=
@SET CL_WEBSTORE_EMAIL=
@SET CL_WEBSTORE_PASS=

@Rem 	Homes ...
@SET COMPIERE_HOME=C:\Compiere2
@SET CL_COMPIERE_HOME=%COMPIERE_HOME%
@SET CL_JAVA_HOME=%JAVA_HOME%
@SET CL_JAVA_TYPE=sun

@Rem	Database ...
@SET CL_COMPIERE_DB_TYPE=oracleXE
@SET CL_COMPIERE_DB_SERVER=%PUBLIC_HOSTNAME%
@SET CL_COMPIERE_DB_PORT=1521
@SET CL_COMPIERE_DB_NAME=xe
@SET CL_COMPIERE_DB_SYSTEM=manager
@SET CL_COMPIERE_DB_USER=compiere
@SET CL_COMPIERE_DB_PASSWORD=compiere

@Rem	Apps Server
@SET CL_COMPIERE_APPS_TYPE=jboss
@SET CL_COMPIERE_APPS_SERVER=%PUBLIC_HOSTNAME%
@SET CL_APPS_SERVER_DEPLOY_DIR=%CL_COMPIERE_HOME%\jboss\server\compiere\deploy
@SET CL_COMPIERE_JNP_PORT=1099
@SET CL_COMPIERE_WEB_PORT=80

@Rem	SSL Settings
@SET CL_COMPIERE_SSL_PORT=443
@SET CL_COMPIERE_KEYSTOREPASS=myPassword

@Rem	mail
@SET CL_MAIL_SERVER=%PUBLIC_HOSTNAME%
@SET CL_ADMIN_EMAIL=admin@%PUBLIC_HOSTNAME%
@SET CL_MAIL_USER=user
@SET CL_MAIL_PASSWORD=pass



@Echo ==========================================
@Echo Web store credentials for license checking
@Echo ==========================================
@Echo CL_SYSTEM_NAME=%CL_SYSTEM_NAME%
@Echo CL_WEBSTORE_EMAIL=%CL_WEBSTORE_EMAIL%
@Echo CL_WEBSTORE_PASS=%CL_WEBSTORE_PASS%

@Echo ==========================================
@Echo 	           Homes ...
@Echo ==========================================
@Echo CL_COMPIERE_HOME=%CL_COMPIERE_HOME%
@Echo CL_JAVA_HOME=%CL_JAVA_HOME%
@Echo CL_JAVA_TYPE=%CL_JAVA_TYPE%

@Echo ==========================================
@Echo	          Database ...
@Echo ==========================================
@Echo CL_COMPIERE_DB_TYPE=%CL_COMPIERE_DB_TYPE%
@Echo CL_COMPIERE_DB_SERVER=%CL_COMPIERE_DB_SERVER%
@Echo CL_COMPIERE_DB_PORT=%CL_COMPIERE_DB_PORT%
@Echo CL_COMPIERE_DB_NAME=%CL_COMPIERE_DB_NAME%
@Echo CL_COMPIERE_DB_SYSTEM=%CL_COMPIERE_DB_SYSTEM%
@Echo CL_COMPIERE_DB_USER=%CL_COMPIERE_DB_USER%
@Echo CL_COMPIERE_DB_PASSWORD=%CL_COMPIERE_DB_PASSWORD%

@Echo ==========================================
@Echo	          Apps Server
@Echo ==========================================
@Echo CL_COMPIERE_APPS_TYPE=%CL_COMPIERE_APPS_TYPE%
@Echo CL_COMPIERE_APPS_SERVER=%CL_COMPIERE_APPS_SERVER%
@Echo CL_APPS_SERVER_DEPLOY_DIR=%CL_APPS_SERVER_DEPLOY_DIR%
@Echo CL_COMPIERE_JNP_PORT=%CL_COMPIERE_JNP_PORT%
@Echo CL_COMPIERE_WEB_PORT=%CL_COMPIERE_WEB_PORT%

@Echo ==========================================
@Echo	         SSL Settings
@Echo ==========================================
@Echo CL_COMPIERE_SSL_PORT=%CL_COMPIERE_SSL_PORT%
@Echo CL_COMPIERE_KEYSTOREPASS=%CL_COMPIERE_KEYSTOREPASS%

@Echo ==========================================
@Echo	         mail
@Echo ==========================================
@Echo CL_MAIL_SERVER=%CL_MAIL_SERVER%
@Echo CL_ADMIN_EMAIL=%CL_ADMIN_EMAIL%
@Echo CL_MAIL_USER=%CL_MAIL_USER%
@Echo CL_MAIL_PASSWORD=%CL_MAIL_PASSWORD%

exit /b 0

