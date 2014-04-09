# $Id: ImportReference.sh,v 1.1 2006/01/11 06:55:49 jjanke Exp $
echo	Compiere Reference Database Import 	$Revision: 1.1 $

echo	Importing Reference DB from $COMPIERE_HOME/data/Reference.dmp

if [ $# -eq 2 ] 
  then
    echo "Usage:		$0 <systemAccount>"
    echo "Example:	$0 system/manager"
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
echo Re-Create new user
echo -------------------------------------
sqlplus $1@$COMPIERE_DB_NAME @$COMPIERE_HOME/utils/$COMPIERE_DB_PATH/CreateUser.sql Reference Compiere

echo -------------------------------------
echo Import Reference
echo -------------------------------------
echo imp $1@$COMPIERE_DB_NAME FILE=$COMPIERE_HOME/data/Reference.dmp FROMUSER=\(reference\) TOUSER=reference
imp $1@$COMPIERE_DB_NAME FILE=$COMPIERE_HOME/data/Reference.dmp FROMUSER=\(reference\) TOUSER=reference

echo -------------------------------------
echo Check System
echo Import may show some warnings. This is OK as long as the following does not show errors
echo -------------------------------------
sqlplus reference/compiere@$COMPIERE_DB_NAME @$COMPIERE_HOME/utils/$COMPIERE_DB_PATH/AfterImport.sql

