#!/bin/bash
#
# FileName:	compiere.sh
# Description:	Compiere ERP automated startup and shutdown
# Vendor:	Compiere, Inc.
# Created:	Jan 6, 2009
# Author:	Gary Wu
#
# FileTarget:	/etc/init.d/compiere
# FileOwner:	root.root
# FilePerms:	0755
#
# chkconfig:	2345 97 06
# $Id: compiere.sh 7928 2009-07-30 20:18:38Z freyes $

# Source fuction library
if [ -f /lib/lsb/init-functions ]
then
        . /lib/lsb/init-functions
elif [ -f /etc/init.d/functions ]
then
        . /etc/init.d/functions
fi


# initialization
# adjust these variables to your environment
ORACLE_SID=XE
COMPIERE_USER=compiere
COMPIERE_HOME=/home/compiere/Compiere2
LOG_FILE=$COMPIERE_HOME/log/compiere.log

export COMPIERE_HOME

 
RETVAL=0
COMPIERESTATUS=

getoracleestatus() {
    ORACLESTATUSSTRING=$(ps -u oracle -f | grep smon)
    echo $ORACLESTATUSSTRING= | grep $ORACLE_SID &> /dev/null
    ORACLESTATUS=$?
}

getcompierestatus() {
    COMPIERESTATUSSTRING=$(ps -u compiere -f | grep org.jboss.Main)
    echo $COMPIERESTATUSSTRING | grep org.jboss.Main &> /dev/null
    COMPIERESTATUS=$?
}

start () {
    getoracleestatus
    if [ $ORACLESTATUS -ne 0 ] ; then
	echo "Oracle is unavailable"
	return 1
    fi

    getcompierestatus
    if [ $COMPIERESTATUS -eq 0 ] ; then
	echo "Compiere is already running"
	return 1
    fi

    echo "Starting Compiere ERP..."

    echo -n "  Resetting local hostname: "
    su $COMPIERE_USER -c "$COMPIERE_HOME/utils/amazonaws/reset-local-hostname.sh >> $LOG_FILE 2>&1"
    RETVAL=$?
    if [ $RETVAL -eq 0 ] ; then
	echo success
    else
	echo failure
	return 1
    fi

    echo -n "  Updating Compiere license: "
    su $COMPIERE_USER -c "$COMPIERE_HOME/utils/CommandLine/CLUpdateLicense.sh $COMPIERE_HOME/utils/CommandLine/CLConfiguration.sh >> $LOG_FILE 2>&1"
    RETVAL=$?
    if [ $RETVAL -eq 0 ] ; then
	echo success
    else
	echo failure
	return 1
    fi

    echo -n "  Starting application server: "
    su $COMPIERE_USER -c "cd $COMPIERE_HOME/utils; ./RUN_Server2.sh >> $LOG_FILE 2>&1"
    RETVAL=$?
    if [ $RETVAL -eq 0 ] ; then
	echo success
    else
	echo failure
    fi
    return $RETVAL
}

stop () {
    getcompierestatus
    if [ $COMPIERESTATUS -ne 0 ] ; then
	echo "Compiere is already stopped"
	return 1
    fi
    echo -n "Stopping Compiere ERP: "
    su $COMPIERE_USER -c "cd $COMPIERE_HOME/utils; ./RUN_Server2Stop.sh >> $LOG_FILE 2>&1 &"
    RETVAL=$?
    if [ $RETVAL -eq 0 ] ; then
        # wait for server to be confirmed as halted in logfile
 	while [ $COMPIERESTATUS -eq 0 ] ; do
		getcompierestatus
		sleep 1
        done
	echo success
    else
	echo failure
    fi
    return $RETVAL
}

restart () {
    stop
    start
}

condrestart () {
    getcompierestatus
    if [ $COMPIERESTATUS -eq 0 ] ; then
	restart
    fi
}

rhstatus () {
    getcompierestatus
    if [ $COMPIERESTATUS -eq 0 ] ; then
	echo
	echo "Compiere is running:"
	ps -u compiere -f | grep org.jboss.Main
	echo
    else
	echo "Compiere is stopped"
    fi
}

case "$1" in
    start)
	start
        ;;
    stop)
	stop
        ;;
    reload)
	restart
	;;
    restart)
	restart
	;;
    condrestart)
	condrestart
	;;
    status)
	rhstatus
	;;
    *)
        echo $"Usage: $0 {start|stop|reload|restart|condrestart|status}"
        exit 1
esac
 
exit 0

