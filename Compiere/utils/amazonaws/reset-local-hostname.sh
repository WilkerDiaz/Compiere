#!/bin/sh
# Copyright Compiere, Inc
# $Id: reset-local-hostname.sh 7847 2009-07-07 18:26:21Z freyes $

cd $COMPIERE_HOME

LOCAL_HOSTNAME=`$COMPIERE_HOME/utils/amazonaws/ec2-get-meta-data.sh local-hostname`

for file in ./CompiereEnv.properties ./Compiere.properties ./Environment.properties ./utils/myEnvironment.sh; do
    if ! ( grep -q $LOCAL_HOSTNAME $file ) then
        if [ ! -r $file.orig ]; then
            cp $file $file.orig
        fi
        perl -pi -e "s/[\w-.]*?internal/${LOCAL_HOSTNAME}/g" $file
        echo Updated $file
    fi
done

PUBLIC_HOSTNAME=`$COMPIERE_HOME/utils/amazonaws/ec2-get-meta-data.sh public-hostname`

for file in ./lib/compiereDirect.jnlp; do
    if ! ( grep -q $PUBLIC_HOSTNAME $file ) then
        if [ ! -r $file.orig ]; then
            cp $file $file.orig
        fi
        perl -pi -e "s/[\w-.]*?internal/${PUBLIC_HOSTNAME}/g" $file
        perl -pi -e "s/[\w-.]*?amazonaws.com/${PUBLIC_HOSTNAME}/g" $file
        echo Updated $file
    fi
done
