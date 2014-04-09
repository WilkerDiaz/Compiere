#!/bin/sh
# Compiere Server Start
#
# $Id: RUN_Server2Stop.sh,v 1.8 2005/09/06 02:46:16 jjanke Exp $

if [ $COMPIERE_HOME ]; then
  cd $COMPIERE_HOME/utils
fi

. ./myEnvironment.sh Server
echo Compiere Server Stop - $COMPIERE_HOME \($COMPIERE_DB_NAME\)

JBOSS_LIB=$JBOSS_HOME/lib
export JBOSS_LIB
JBOSS_SERVERLIB=$JBOSS_HOME/server/compiere/lib
export JBOSS_SERVERLIB
JBOSS_CLASSPATH=$COMPIERE_HOME/lib/jboss.jar:$JBOSS_LIB/jboss-system.jar:
export JBOSS_CLASSPATH

echo shutdown.sh --server=jnp://$COMPIERE_APPS_SERVER:$COMPIERE_JNP_PORT
$JBOSS_HOME/bin/shutdown.sh --server=jnp://$COMPIERE_APPS_SERVER:$COMPIERE_JNP_PORT --shutdown
