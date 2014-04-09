@if (%COMPIERE_HOME%) == () (CALL myEnvironment.bat Server) else (CALL %COMPIERE_HOME%\utils\myEnvironment.bat Server)
@Title Stop DataBase Service  - %COMPIERE_HOME% (%COMPIERE_DB_NAME%)

@Rem $Id: RUN_DBStop.bat,v 1.5 2005/01/22 21:59:15 jjanke Exp $

@CALL %COMPIERE_DB_PATH%\Stop.bat
@Echo Done stopping database %COMPIERE_HOME% (%COMPIERE_DB_NAME%)

@sleep 60
