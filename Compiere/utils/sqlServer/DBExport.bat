@Echo	Compiere Database Export 	$Revision: 1.8 $

@Rem $Id: DBExport.bat,v 1.8 2005/04/27 17:45:01 jjanke Exp $
@Rem 
@Echo Saving database %1@%COMPIERE_DB_NAME% to %COMPIERE_HOME%\data\ExpDat.dmp

@if (%COMPIERE_HOME%) == () goto environment
@if (%COMPIERE_DB_NAME%) == () goto environment
@Rem Must have parameter: userAccount
@if (%1) == () goto usage

@Rem Clanup
@sqlplus %1/%2@%COMPIERE_DB_NAME% @%COMPIERE_HOME%\utils\%COMPIERE_DB_PATH%\Daily.sql

@Rem The Export
@exp %1/%2@%COMPIERE_DB_NAME% FILE=%COMPIERE_HOME%\data\ExpDat.dmp Log=%COMPIERE_HOME%\data\ExpDat.log CONSISTENT=Y STATISTICS=NONE OWNER=%1

@cd %COMPIERE_HOME%\Data
@copy ExpDat.jar ExpDatOld.jar
@jar cvfM ExpDat.jar ExpDat.dmp ExpDat.log

@goto end

:environment
@Echo Please make sure that the enviroment variables are set correctly:
@Echo		COMPIERE_HOME	e.g. D:\Compiere2
@Echo		COMPIERE_DB_NAME 	e.g. compiere.compiere.org

:usage
@echo Usage:	%0 <userAccount>
@echo Examples:	%0 compiere compiere

:end
