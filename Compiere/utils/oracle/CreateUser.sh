# $Id: CreateUser.sh 6450 2008-10-14 16:19:21Z freyes $
if [ $# -le 2 ] 
  then
    echo "Usage:		$0 <systemPassword> <CompiereID> <CompierePWD>"
    echo "Example:	$0 manager compiere compiere"
    exit 1
fi

if [ "$COMPIERE_HOME" = "" ]
  then
    echo "Please make sure that the environment variables are set correctly:"
    echo "	COMPIERE_HOME	e.g. /Compiere2"
    echo "	COMPIERE_DB_NAME	e.g. compiere.compiere.org"
    exit 1
fi
. $COMPIERE_HOME/utils/myEnvironment.sh Server

echo -------------------------------------
echo Re-Create DB user $2 @ $COMPIERE_DB_NAME
echo -------------------------------------
echo sqlplus system/$1@$COMPIERE_DB_NAME @$COMPIERE_HOME/utils/$COMPIERE_DB_PATH/CreateUser.sql $2 $3
sqlplus -s -l system/$1@$COMPIERE_DB_NAME @$COMPIERE_HOME/utils/$COMPIERE_DB_PATH/CreateUser.sql $2 $3
status=$?
if [ $status -ne 0 ]; then
    echo;echo
    echo "ERROR $status: Unable to create user $2"
    echo "===================================="
    date
    echo $0 $*
    exit 1  
fi
