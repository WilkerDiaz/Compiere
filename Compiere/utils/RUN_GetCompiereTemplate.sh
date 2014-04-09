#!/bin/sh
# Download Compiere
#
# $Id: RUN_GetCompiereTemplate.sh,v 1.3 2005/01/22 21:59:15 jjanke Exp $

if [ $COMPIERE_HOME ]; then
  cd $COMPIERE_HOME/utils
fi
. ./myEnvironment.sh Server
echo Download Compiere Database - $COMPIERE_HOME \($COMPIERE_DB_NAME\)

echo Download Compiere Database as jar into $COMPIERE_HOME/data

ping @COMPIERE_FTP_SERVER@
cd $COMPIERE_HOME/data
rm Compiere.jar

ftp -s:$COMPIERE_HOME/utils/ftpGetCompiere.txt

echo Unpacking 
jar xvf Compiere.jar

echo ........ Received

cd $COMPIERE_HOME/utils
sh RUN_ImportCompiere.sh
