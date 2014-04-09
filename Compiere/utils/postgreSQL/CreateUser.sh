# $Id: CreateUser.sh 6450 2008-10-14 16:19:21Z freyes $
if [ $# -le 2 ] 
  then
    echo "Usage:		$0 <enterprisedbPassword> <CompiereID> <CompierePWD>"
    echo "Example:	$0 enterprisedb compiere compiere"
    exit 1
fi

if [ "$COMPIERE_HOME" = "" ]
  then
    echo "ERROR: COMPIERE_HOME variable not defined"
    exit 1
fi

echo -------------------------------------
echo Re-Create DB user $2
echo -------------------------------------
echo edb-psql -d mgmtsvr -U enterprisedb -r $1 -f $COMPIERE_HOME/utils/postgreSQL/CreateDB.sql -v compiereID=$2 -v compierePW=$3
edb-psql -a -d mgmtsvr -U enterprisedb -r $1 -f $COMPIERE_HOME/utils/postgreSQL/CreateDB.sql -v compiereID=$2 -v compierePW=$3
status=$?
if [ $status -ne 0 ]; then
    echo;echo
    echo "ERROR $status : edb-psql -d mgmtsvr -U enterprisedb -r $1 -f $COMPIERE_HOME/utils/postgreSQL/CreateDB.sql -v compiereID=$2 -v compierePW=$3"
    echo "===================================="
    date
    echo $0 $*
    exit 1  
fi
