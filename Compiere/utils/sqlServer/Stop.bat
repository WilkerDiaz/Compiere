@Rem $Id: Stop.bat,v 1.7 2005/01/22 21:59:15 jjanke Exp $

@Echo Stopping database ....
@sqlplus "system/%COMPIERE_DB_SYSTEM%@%COMPIERE_DB_NAME% AS SYSDBA" @%COMPIERE_HOME%\utils\%COMPIERE_DB_PATH%\Stop.sql

@Echo Stopping Listener ....
lsnrctl stop

@Echo Stopping (optional) Agent ....
agentctl stop

