# $Id$
echo	Compiere PostgreSQL Database Export 

echo Exporting database user $COMPIERE_DB_USER@$COMPIERE_DB_NAME to $COMPIERE_HOME/data/ExpDat.dump.tar.gz

export PGPASSWORD=$COMPIERE_DB_PASSWORD
pg_dump -F c -f $COMPIERE_HOME/data/ExpDat.dump.tar.gz -h $COMPIERE_DB_SERVER -p $COMPIERE_DB_PORT -U $COMPIERE_DB_USER -v $COMPIERE_DB_NAME

