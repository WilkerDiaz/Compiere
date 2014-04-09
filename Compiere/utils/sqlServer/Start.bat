@Rem $Id: Start.bat,v 1.7 2005/01/22 21:59:15 jjanke Exp $

@Echo Starting Listener ....
lsnrctl start

@Echo Starting Database ....
@sqlplus "system/%COMPIERE_DB_SYSTEM%@%COMPIERE_DB_NAME% AS SYSDBA" @%COMPIERE_HOME%\utils\%COMPIERE_DB_PATH%\Start.sql

@Echo Starting optional agent ....
agentctl start

@Echo ------------------------
lsnrctl status
