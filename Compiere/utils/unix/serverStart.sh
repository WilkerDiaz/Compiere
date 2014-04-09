#!/bin/bash
#
# FileName:	compiere.init
# Description:	compiere erp software startup and shutdown
# Vendor:	K.K. Alice
# Created:	05. April 2004
# Author:	S. Christians
#
# FileTarget:	/etc/init.d/compiere
# FileOwner:	root.root
# FilePerms:	0755
#
# chkconfig:	2345 97 06
# $Id: serverStart.sh,v 1.2 2004/05/09 04:53:29 jjanke Exp $

# initialization
# adjust these variables to your environment
EXECDIR=/opt/compiere/Compiere2
ENVFILE=/opt/compiere/.bash_profile

. /etc/rc.d/init.d/functions
 
RETVAL=0
COMPIERESTATUS=

getcompierestatus() {
    COMPIERESTATUSSTRING=$(ps -ax | grep -v grep | grep $EXECDIR)
    echo $COMPIERESTATUSSTRING | grep $EXECDIR &> /dev/null
    COMPIERESTATUS=$?
}

start () {
    getcompierestatus
    if [ $COMPIERESTATUS -eq 0 ] ; then
	echo "compiere is already running"
	return 1
    fi
    echo -n "Starting Compiere ERP: "
    source $ENVFILE 
    # we need to stay root for logging
    # (compiere user has no write access to /var/log/...)
    su -c "cd $EXECDIR/utils;$EXECDIR/utils/RUN_Server2.sh &> /var/log/compiere.log &"
    RETVAL=$?
    if [ $RETVAL -eq 0 ] ; then
	# wait for server to be confirmed as started in logfile
	STATUSTEST=0
	while [ $STATUSTEST -eq 0 ] ; do
	tail -n 1 /var/log/compiere.log | grep 'INFO.*\[Server\].*Started in' &> /dev/null && STATUSTEST=1
	done
	# avoid race conditions
	sleep 5
	echo_success
	echo
    else
	echo_failure
	echo
    fi
    return $RETVAL
}

stop () {
    getcompierestatus
    if [ $COMPIERESTATUS -ne 0 ] ; then
	echo "compiere is already stopped"
	return 1
    fi
    echo -n "Stopping Compiere ERP: "
    source $ENVFILE 
    su -c "cd $EXECDIR/utils;$EXECDIR/utils/RUN_Server2Stop.sh &> /dev/null &"
    RETVAL=$?
    if [ $RETVAL -eq 0 ] ; then
	# wait for server to be confirmed as halted in logfile
	STATUSTEST=0
	while [ $STATUSTEST -eq 0 ] ; do
	tail -n 1 /var/log/compiere.log | grep 'Halting VM' &> /dev/null && STATUSTEST=1
	done
	# avoid race conditions
	sleep 5
	echo_success
	echo
    else
	echo_failure
	echo
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
	echo "compiere is running:"
	ps -ax | grep -v grep | grep $EXECDIR | sed 's/^[[:space:]]*\([[:digit:]]*\).*:[[:digit:]][[:digit:]][[:space:]]\(.*\)/\1 \2/'
	echo
    else
	echo "compiere is stopped"
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

