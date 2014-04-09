#!/bin/sh
# Compiere Server Start
#
# $Id: RUN_Server2.sh,v 1.22 2005/10/26 00:38:18 jjanke Exp $

if [ $COMPIERE_HOME ]; then
  cd $COMPIERE_HOME/utils
fi

. ./myEnvironment.sh Server

# To use your own Encryption class (implementing org.compiere.util.SecureInterface),
# you need to set it here (and in the client start script) - example:
# SECURE=-DCOMPIERE_SECURE=org.compiere.util.Secure
SECURE=

# headless option if you don't have X installed on the server
JAVA_OPTS="-server $COMPIERE_JAVA_OPTIONS $SECURE -Djava.awt.headless=true"

export JAVA_OPTS

$JBOSS_HOME/bin/run.sh -c compiere -b $COMPIERE_APPS_SERVER &
