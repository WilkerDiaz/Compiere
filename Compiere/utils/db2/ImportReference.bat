@Echo	Compiere Reference Database Import 	$Revision: 1.1 $

@Rem $Id: ImportReference.bat,v 1.1 2006/01/11 06:55:49 jjanke Exp $

@Echo	Importing Reference DB from %COMPIERE_HOME%\data\Reference.dmp

@if (%COMPIERE_HOME%) == () goto environment
@if (%COMPIERE_DB_NAME%) == () goto environment
@Rem Must have parameter: systemAccount
@if (%1) == () goto usage

@echo -------------------------------------
@echo Re-Create new user
@echo -------------------------------------
@sqlplus %1@%COMPIERE_DB_NAME% @%COMPIERE_HOME%\Utils\%COMPIERE_DB_PATH%\CreateUser.sql Reference Compiere

@echo -------------------------------------
@echo Import Reference
@echo -------------------------------------
imp %1@%COMPIERE_DB_NAME% FILE=%COMPIERE_HOME%\data\Reference.dmp FROMUSER=(reference) TOUSER=reference

@echo -------------------------------------
@echo Check System
@echo Import may show some warnings. This is OK as long as the following does not show errors
@echo -------------------------------------
@sqlplus reference/compiere@%COMPIERE_DB_NAME% @%COMPIERE_HOME%\Utils\%COMPIERE_DB_PATH%\AfterImport.sql

@goto end

:environment
@Echo Please make sure that the enviroment variables are set correctly:
@Echo		COMPIERE_HOME	e.g. D:\Compiere2
@Echo		COMPIERE_DB_NAME e.g. dev1.compiere.org

:usage
@echo Usage:		%0 <systemAccount>
@echo Examples:	%0 system/manager

:end
