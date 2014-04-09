#!/bin/bash
# Copyright Compiere, Inc
# $Id: CLInstall.sh 9222 2010-09-17 00:26:20Z freyes $

echo Install Compiere Server

cat License.txt

if !(command -v sqlplus > /dev/null) then
    echo sqlplus not found
fi

if !(command -v curl > /dev/null) then
    echo curl not found
fi

if [ $# -lt 1 ]; then
	echo Configuration file missing
	echo Please edit the configuration file appropriately and pass the file name to this script
	echo Usage: $0 CLConfiguration.sh
      exit 1
fi

echo calling configuration script
. $1
if [ $? -gt 0 ]; then
	echo ===========================================
	echo Error calling configuration script $1	
	echo ===========================================
	exit 1
fi

echo ==========================================
echo Web store credentials for license checking
echo ==========================================
echo CL_SYSTEM_NAME=$CL_SYSTEM_NAME
echo CL_WEBSTORE_EMAIL=$CL_WEBSTORE_EMAIL
echo CL_WEBSTORE_PASS=$CL_WEBSTORE_PASS

echo ==========================================
echo 	           Homes ...
echo ==========================================
echo CL_COMPIERE_HOME=$CL_COMPIERE_HOME
echo CL_JAVA_HOME=$CL_JAVA_HOME
echo CL_JAVA_TYPE=$CL_JAVA_TYPE

echo ==========================================
echo	          Database ...
echo ==========================================
echo CL_COMPIERE_DB_TYPE=$CL_COMPIERE_DB_TYPE
echo CL_COMPIERE_DB_SERVER=$CL_COMPIERE_DB_SERVER
echo CL_COMPIERE_DB_PORT=$CL_COMPIERE_DB_PORT
echo CL_COMPIERE_DB_NAME=$CL_COMPIERE_DB_NAME
echo CL_COMPIERE_DB_SYSTEM=$CL_COMPIERE_DB_SYSTEM
echo CL_COMPIERE_DB_USER=$CL_COMPIERE_DB_USER
echo CL_COMPIERE_DB_PASSWORD=$CL_COMPIERE_DB_PASSWORD

echo ==========================================
echo	          Apps Server
echo ==========================================
echo CL_COMPIERE_APPS_TYPE=$CL_COMPIERE_APPS_TYPE
echo CL_COMPIERE_APPS_SERVER=$CL_COMPIERE_APPS_SERVER
echo CL_APPS_SERVER_DEPLOY_DIR=$CL_APPS_SERVER_DEPLOY_DIR
echo CL_COMPIERE_JNP_PORT=$CL_COMPIERE_JNP_PORT
echo CL_COMPIERE_WEB_PORT=$CL_COMPIERE_WEB_PORT

echo ==========================================
echo	         SSL Settings
echo ==========================================
echo CL_COMPIERE_SSL_PORT=$CL_COMPIERE_SSL_PORT
echo CL_COMPIERE_KEYSTOREPASS=$CL_COMPIERE_KEYSTOREPASS

echo ==========================================
echo	         mail
echo ==========================================
echo CL_MAIL_SERVER=$CL_MAIL_SERVER
echo CL_ADMIN_EMAIL=$CL_ADMIN_EMAIL
echo CL_MAIL_USER=$CL_MAIL_USER
echo CL_MAIL_PASSWORD=$CL_MAIL_PASSWORD


if [ $JAVA_HOME ]; then
  JAVA=$JAVA_HOME/bin/java
  KEYTOOL=$JAVA_HOME/bin/keytool
else
  JAVA=java
  KEYTOOL=keytool
  echo JAVA_HOME is not set.
  echo You may not be able to start the Setup
  echo Set JAVA_HOME to the directory of your local JDK.
fi


echo ===================================
echo Setup
echo ===================================
CP=$CL_COMPIERE_HOME/lib/CInstall.jar:$CL_COMPIERE_HOME/lib/CompiereInstall.jar:$CL_COMPIERE_HOME/lib/jPDF.jar:$CL_COMPIERE_HOME/lib/CCTools.jar:$CL_COMPIERE_HOME/lib/oracle.jar:$CL_COMPIERE_HOME/lib/jboss.jar:$CL_COMPIERE_HOME/lib/db2.jar:$CL_COMPIERE_HOME/lib/postgreSQL.jar:$CL_COMPIERE_HOME/lib/sqlServer.jar

# Parameter
ARGS="--javahome $CL_JAVA_HOME --javatype $CL_JAVA_TYPE --compierehome $CL_COMPIERE_HOME --keystorepass $CL_COMPIERE_KEYSTOREPASS --appservhost $CL_COMPIERE_APPS_SERVER --appservtype $CL_COMPIERE_APPS_TYPE --appservdeploydir $CL_APPS_SERVER_DEPLOY_DIR --appservjnpport $CL_COMPIERE_JNP_PORT --appservwebport $CL_COMPIERE_WEB_PORT --appservsslport $CL_COMPIERE_SSL_PORT --dbhost $CL_COMPIERE_DB_SERVER --dbtype $CL_COMPIERE_DB_TYPE --dbname $CL_COMPIERE_DB_NAME --dbport $CL_COMPIERE_DB_PORT --dbsystempass $CL_COMPIERE_DB_SYSTEM --dbuser $CL_COMPIERE_DB_USER --dbpass $CL_COMPIERE_DB_PASSWORD --mailservhost $CL_MAIL_SERVER --adminemail $CL_ADMIN_EMAIL --mailuser $CL_MAIL_USER --mailpass $CL_MAIL_PASSWORD"

$JAVA -Xmx258m -classpath $CP -DCOMPIERE_HOME=$CL_COMPIERE_HOME org.compiere.install.ConfigurationData $ARGS

if [ $? -gt 0 ]; then
	echo ===========================================
	echo An error occurred while running the program
	echo Please check log files for details
	echo ===========================================
	exit 1
fi


echo Continue with component install

echo $JAVA -Xmx500m -classpath $CP -DCOMPIERE_HOME=$CL_COMPIERE_HOME com.compiere.client.CommandLineInstall --email $CL_WEBSTORE_EMAIL --sysname $CL_SYSTEM_NAME --pwd $CL_WEBSTORE_PASS $2

$JAVA -Xmx500m -classpath $CP -DCOMPIERE_HOME=$CL_COMPIERE_HOME com.compiere.client.CommandLineInstall --email $CL_WEBSTORE_EMAIL --sysname "$CL_SYSTEM_NAME" --pwd "$CL_WEBSTORE_PASS" $2

if [ $? -gt 0 ]; then
	echo ===========================================
	echo An error occurred while running the program
	echo Please check log files for details
	echo ===========================================
	exit 1
fi

echo ===================================
echo Make .sh executable & set Env
echo ===================================

echo Setting up environment for Updating License Information
cd $COMPIERE_HOME/utils
. ./RUN_UnixEnv.sh
. ./myEnvironment.sh Server
cd CommandLine

echo Updating Licenses
$COMPIERE_JAVA $COMPIERE_JAVA_OPTIONS -cp $CLASSPATH com.compiere.client.UpdateLicense --sysname "$CL_SYSTEM_NAME" --email $CL_WEBSTORE_EMAIL --password "$CL_WEBSTORE_PASS"

if [ $? -gt 0 ]; then
	echo ===========================================
	echo An error occurred while running the Update License program
	echo Please check log file in $COMPIERE_HOME/log directory for details ...
	echo ===========================================
	exit 1
fi
echo .
echo Compiere has been installed succesfully
