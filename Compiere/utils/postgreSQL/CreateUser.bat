@REM $Id: CreateUser.bat 6450 2008-10-14 16:19:21Z freyes $

@if (%COMPIERE_HOME%) == () goto environment
@if (%COMPIERE_DB_NAME%) == () (CALL %COMPIERE_HOME%\utils\myEnvironment.bat server)
@if (%COMPIERE_DB_NAME%) == () goto environment
@Rem Must have parameters systemPassword CompiereID CompierePwd
@if (%1) == () goto usage
@if (%2) == () goto usage
@if (%3) == () goto usage

@echo -------------------------------------
@echo Re-Create DB user %2 @%COMPIERE_DB_NAME%
@echo -------------------------------------
edb-psql -a -d mgmtsvr -U enterprisedb -r %1 -f %COMPIERE_HOME%\utils\postgreSQL\CreateDB.sql -v compiereID=%2 -v compierePW=%3
@if ERRORLEVEL 1 ((echo Unable to recreate user %2 @%COMPIERE_DB_NAME%)&&(exit 1))
@echo Done %0
@exit /b 0

:environment
@Echo Please make sure that the enviroment variables are set correctly:
@Echo		COMPIERE_HOME	e.g. D:\Compiere2
@Echo		COMPIERE_DB_NAME	e.g. dev1.compiere.org

:usage
@echo Usage:		%0 <enterprisedbPassword> <CompiereID> <CompierePassword>
@echo Example:	%0 enterprisedb Compiere Compiere


