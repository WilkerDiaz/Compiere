# $Id: Stop.sh,v 1.7 2005/01/22 21:59:15 jjanke Exp $

# In a multi-instance environment set the environment first
# SET ORACLE_SID=
# export ORACLE_SID

sqlplus "system/$COMPIERE_DB_SYSTEM@$COMPIERE_DB_NAME AS SYSDBA" @$COMPIERE_HOME/utils/$COMPIERE_DB_PATH/Stop.sql

