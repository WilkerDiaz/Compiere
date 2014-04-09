@if (%COMPIERE_HOME%) == () (CALL myEnvironment.bat Server) else (CALL %COMPIERE_HOME%\utils\myEnvironment.bat Server)
@Title Start DataBase Service  - %COMPIERE_HOME% (%COMPIERE_DB_NAME%)

@Rem $Id: RUN_DBStart.bat,v 1.6 2005/01/22 21:59:15 jjanke Exp $

@CALL %COMPIERE_DB_PATH%\Start.bat
@Echo Done starting database %COMPIERE_HOME% (%COMPIERE_DB_NAME%)

@sleep 60
