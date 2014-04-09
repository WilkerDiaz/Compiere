# $Id: Start.sh,v 1.6 2005/01/22 21:59:15 jjanke Exp $

sqlplus "system/$COMPIERE_DB_SYSTEM@$COMPIERE_DB_NAME AS SYSDBA" @$COMPIERE_HOME/utils/$COMPIERE_DB_PATH/Start.sql
