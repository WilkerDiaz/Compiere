#!/bin/bash
#
# Configuration parameters used for Compiere command line installation
#
# $Id: CLConfigurationTemplate.sh 8377 2010-01-27 22:39:24Z freyes $

# The publicly visible host name.  DO NOT USE localhost
PUBLIC_HOSTNAME=`hostname`

echo Setting Command Line Configuration ....

#       Web store credentials for license checking
#       Be sure to enclose the value in double quotes if it has blank spaces
export CL_SYSTEM_NAME=
export CL_WEBSTORE_EMAIL=
export CL_WEBSTORE_PASS=

# 	Homes ...
export COMPIERE_HOME=/home/compiere/Compiere2
export CL_COMPIERE_HOME=${COMPIERE_HOME}
export CL_JAVA_HOME=${JAVA_HOME}
export CL_JAVA_TYPE=sun

#	Database ...
export CL_COMPIERE_DB_TYPE=oracleXE
export CL_COMPIERE_DB_SERVER=${PUBLIC_HOSTNAME}
export CL_COMPIERE_DB_PORT=1521
export CL_COMPIERE_DB_NAME=xe
export CL_COMPIERE_DB_SYSTEM=manager
export CL_COMPIERE_DB_USER=compiere
export CL_COMPIERE_DB_PASSWORD=compiere

#	Apps Server
export CL_COMPIERE_APPS_TYPE=jboss
export CL_COMPIERE_APPS_SERVER=${PUBLIC_HOSTNAME}
export CL_APPS_SERVER_DEPLOY_DIR=${COMPIERE_HOME}/jboss/server/compiere/deploy
export CL_COMPIERE_JNP_PORT=1099
export CL_COMPIERE_WEB_PORT=80

#	SSL Settings
export CL_COMPIERE_SSL_PORT=443
export CL_COMPIERE_KEYSTOREPASS=myPassword

#	mail
export CL_MAIL_SERVER=${PUBLIC_HOSTNAME}
export CL_ADMIN_EMAIL=admin@${PUBLIC_HOSTNAME}
export CL_MAIL_USER=user
export CL_MAIL_PASSWORD=pass

