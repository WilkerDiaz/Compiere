@echo on
@Echo	Compiere DB2 Database Import		$Revision: 1.1 $

@Rem $Id: ImportCompiere.bat,v 1.1 2006/01/11 06:55:49 jjanke Exp $

@rem Echo	Importing Compiere DB2 DB from %COMPIERE_HOME%\data

@if (%COMPIERE_HOME%) == () (CALL ..\myEnvironment.bat Server) else (CALL %COMPIERE_HOME%\utils\myEnvironment.bat Server)
@Rem Must have the driver where DB2 database residents
@ if (%1) == () goto usage


@set db2BackupStamp = "20061228174656"

@echo ----------------------------------------------------
@echo load DB2 Database compiere from %COMPIERE_HOME%\data
@echo ----------------------------------------------------

@rem DB2BATCH -d COMPIERE -f RESTOREDB2.SQL -a compiere/%1
@db2cmd db2 RESTORE DATABASE compiere FROM ""%COMPIERE_HOME%\data"" TAKEN AT 20061228174656 TO ""%1"" INTO compiere WITH 6 BUFFERS BUFFER 1024 PARALLELISM 1 WITHOUT PROMPTING


@goto end

:environment
@Echo Please make sure that the enviroment variables are set correctly:
@Echo		COMPIERE_HOME	e.g. D:\Compiere2

:usage
@echo Usage:		%0 <db-drive>
@echo Example:	%0 C:

:end
