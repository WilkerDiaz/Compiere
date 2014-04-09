#!/bin/sh
# Launch Compiere Web Client
#
# $Id: LaunchWebClient.sh 7699 2009-05-19 23:13:19Z cjandhyala $

if [ $COMPIERE_HOME ]; then
  cd $COMPIERE_HOME/utils
fi

. ./myEnvironment.sh Server

for i in firefox mozilla konqueror ; do
        if test -x "/usr/bin/$i"; then
                /usr/bin/$i "http://$COMPIERE_APPS_SERVER:$COMPIERE_WEB_PORT/apps"
                exit $?
        fi
done
