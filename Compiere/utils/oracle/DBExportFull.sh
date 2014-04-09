echo	Compiere Full Database Export 	$Revision: 1.3 $

# $Id: DBExportFull.sh,v 1.3 2005/01/22 21:59:15 jjanke Exp $

echo Saving database $1@$COMPIERE_DB_NAME to $COMPIERE_HOME/data/ExpDatFull.dmp

if [ $# -eq 0 ] 
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

exp $1@$COMPIERE_DB_NAME FILE=$COMPIERE_HOME/data/ExpDatFull.dmp Log=$COMPIERE_HOME/data/ExpDatFull.log CONSISTENT=Y FULL=Y

cd $COMPIERE_HOME/data
jar cvfM ExpDatFull.jar ExpDatFull.dmp  ExpDatFull.log

