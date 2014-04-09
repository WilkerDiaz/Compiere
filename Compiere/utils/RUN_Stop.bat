@if (%COMPIERE_HOME%) == () (CALL myEnvironment.bat Server) else (CALL %COMPIERE_HOME%\utils\myEnvironment.bat Server)
@Title Stop Compiere  - %COMPIERE_HOME% (%COMPIERE_DB_NAME%)

@Rem $Id: RUN_Stop.bat,v 1.11 2005/01/22 21:59:15 jjanke Exp $

@CALL %COMPIERE_HOME%\utils\RUN_Server2Stop.bat

@CALL %COMPIERE_DB_PATH%\Stop.bat

