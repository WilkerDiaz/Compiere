#!/bin/sh
#
if [ $COMPIERE_HOME ]; then
	cd $COMPIERE_HOME/utils
fi
. ./myEnvironment.sh Server

$COMPIERE_JAVA $COMPIERE_JAVA_OPTIONS -cp $CLASSPATH com.compiere.client.Support

