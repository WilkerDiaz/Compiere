@Echo	Compiere Database Export 	$Revision: 1.1 $

@Rem $Id: DBExport.bat,v 1.1 2006/01/11 06:55:49 jjanke Exp $
@Rem 
@Echo Saving database %1@%COMPIERE_DB_NAME% to directory %COMPIERE_HOME%\data\export

@if (%COMPIERE_HOME%) == () goto environment
@if (%COMPIERE_DB_NAME%) == () goto environment
@Rem Must have parameter: userAccount
@if (%1) == () goto usage


@Rem The Export
db2 backup db %COMPIERE_DB_NAME% online to %COMPIERE_HOME%\data\export with 4 buffers buffer 512 >> %COMPIERE_HOME%\data\export.log

@cd %COMPIERE_HOME%\Data
@Rem copy ExpDat.jar ExpDatOld.jar
@Rem jar cvfM ExpDat.jar ExpDat.dmp ExpDat.log

@goto end

:environment
@Echo Please make sure that the enviroment variables are set correctly:
@Echo		COMPIERE_HOME	e.g. D:\Compiere2
@Echo		COMPIERE_DB_NAME 	e.g. compiere.compiere.org

:usage
@echo Usage:	%0 <userAccount>
@echo Examples:	%0 compiere compiere

:end
