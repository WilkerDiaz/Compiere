# $Id: DBRestore.sh,v 1.8 2005/12/20 07:12:17 jjanke Exp $
echo	Compiere Database Restore 	$Revision: 1.8 $

echo	Restoring Compiere DB from $COMPIERE_HOME/data/ExpDat.dmp

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
sqlplus $1@$COMPIERE_DB_NAME @$COMPIERE_HOME/utils/$COMPIERE_DB_PATH/CreateUser.sql $2 $3
if [ $? -ne 0 ]; then
  echo "ERROR: Unable to re-create db user $2"
  exit 1
fi

echo -------------------------------------
echo Import ExpDat
echo -------------------------------------
imp $1@$COMPIERE_DB_NAME FILE=$COMPIERE_HOME/data/ExpDat.dmp FROMUSER=\($2\) TOUSER=$2 

echo -------------------------------------
echo Check System
echo Import may show some warnings. This is OK as long as the following does not show errors
echo -------------------------------------
sqlplus $2/$3@$COMPIERE_DB_NAME @$COMPIERE_HOME/utils/$COMPIERE_DB_PATH/AfterImport.sql
