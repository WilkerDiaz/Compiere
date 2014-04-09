@Echo Compiere EDB Database Restore 
@echo.
@echo Disclaimer:
@echo ============
@echo Sample scripts are NOT supported by Compiere and are provided AS IS without warranty of any kind.
@echo You should fully test the scripts in your environment before deploying them in production.
@echo.

@Rem $Id: DBRestore.bat,v 1.2 2005/01/22 21:59:15 jjanke Exp $

@Echo	Restoring Compiere DB from %COMPIERE_HOME%\data\ExpDat.dmp

@if (%COMPIERE_HOME%) == () goto environment
@if (%COMPIERE_DB_NAME%) == () goto environment
@Rem Must have parameter: systemAccount compiereID CompierePwd
@if (%1) == () goto usage
@if (%2) == () goto usage
@if (%3) == () goto usage

@SET PGPASSWORD=%3

@echo -------------------------------------
@echo Re-Create DB user
@echo -------------------------------------
edb-psql -d mgmtsvr -U enterprisedb -r %1 -f %COMPIERE_HOME%\Utils\postgreSQL\CreateDB.sql -v compiereID=%2 -v compierePW=%3
@if ERRORLEVEL 1 ((echo ERROR re-creating user %2)&&(goto ERROR))

@echo -------------------------------------
@echo Import
@echo -------------------------------------
pg_restore -d %COMPIERE_DB_NAME% %COMPIERE_HOME%\data\edb.compiere.backup -U %2
IF %ERRORLEVEL% EQU "1" (ECHO %ERRORLEVEL% & ECHO %0 & (EXIT /B 1))

@goto end

:environment
@Echo Please make sure that the enviroment variables are set correctly:
@Echo		COMPIERE_HOME	e.g. D:\Compiere2
@Echo		COMPIERE_DB_NAME e.g. dev1.compiere.org

:usage
@echo Usage:		%0% <enterprisedb_Password> <CompiereID> <CompierePwd>
@echo Example:	%0% enterprisedb compiere compiere

:end
