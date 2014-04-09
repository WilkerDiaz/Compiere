echo	Compiere Database Export 	$Revision: 1.1 $

# $Id: DBExport.sh,v 1.1 2006/01/11 06:55:49 jjanke Exp $

echo Saving database $1@$COMPIERE_DB_NAME to $COMPIERE_HOME/data/ExpDat.dmp

if [ $# -eq 0 ] 
  then
    echo "Usage:		$0 <userAccount>"
    echo "Example:	$0 compiere compiere"
    exit 1
fi
if [ "$COMPIERE_HOME" = "" -o  "$COMPIERE_DB_NAME" = "" ]
  then
    echo "Please make sure that the environment variables are set correctly:"
    echo "	COMPIERE_HOME	e.g. /Compiere2"
    echo "	COMPIERE_DB_NAME	e.g. compiere.compiere.org"
    exit 1
fi

# Cleanup
sqlplus $1/$2@$COMPIERE_DB_NAME @$COMPIERE_HOME/utils/$COMPIERE_DB_PATH/Daily.sql

# Export
exp $1/$2@$COMPIERE_DB_NAME FILE=$COMPIERE_HOME/data/ExpDat.dmp Log=$COMPIERE_HOME/data/ExpDat.log CONSISTENT=Y OWNER=$1 

cd $COMPIERE_HOME/data
jar cvfM ExpDat.jar ExpDat.dmp ExpDat.log
