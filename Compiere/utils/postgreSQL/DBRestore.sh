echo	Compiere Database Restore 	$Revision: 1.2 $

# $Id: DBRestore.sh,v 1.2 2005/01/22 21:59:15 jjanke Exp $
echo "============"
echo "Sample DBA scripts are not supported by Compiere and are provided AS IS without warranty of any kind."
echo "You should fully test the scripts in your environment before deploying them in production."
echo

echo	Restoring Compiere DB from $COMPIERE_HOME/data/edb.compiere.backup

if [ $# -le 2 ] 
  then
    echo "Usage:		$0 <systemAccount> <CompiereID> <CompierePWD>"
    echo "Example:	$0 system/manager compiere compiere"
    exit 1
fi
if [ "$COMPIERE_HOME" = "" -o  "$COMPIERE_DB_NAME" = "" ]
  then
    echo "Please make sure that the environment variables are set correctly:"
    echo "	COMPIERE_HOME	e.g. /Compiere2"
    echo "	COMPIERE_DB_NAME	e.g. compiere.compiere.org"
    exit 1
fi


echo -------------------------------------
echo Re-Create DB user
echo -------------------------------------
edb-psql -d mgmtsvr -U enterprisedb -r $1 -f $COMPIERE_HOME/Utils/postgreSQL/CreateDB.sql -v compiereID=$2 -v compierePW=$3
if [ $? -ne 0 ]; then
  echo "ERROR: Unable to re-create db user $2"
  exit 1
fi

echo -------------------------------------
echo Import edb.compiere.backup
echo -------------------------------------
pg_restore -d $COMPIERE_DB_NAME $COMPIERE_HOME/data/edb.compiere.backup -U $2
if [ $? -eq 1 ]; then
  echo "ERROR: Unable to import edb.compiere.backup"
  exit 1
fi


