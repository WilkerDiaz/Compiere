@REM $Id: CreateUser.bat 5751 2008-07-03 06:14:34Z freyes $
@if (%COMPIERE_HOME%) == () goto environment
@if (%COMPIERE_DB_NAME%) == () (CALL %COMPIERE_HOME%\utils\myEnvironment.bat server)
@if (%COMPIERE_DB_NAME%) == () goto environment
@Rem Must have parameters systemPassword CompiereID CompierePwd
@if (%1) == () goto usage
@if (%2) == () goto usage
@if (%3) == () goto usage

@echo -------------------------------------
@echo Re-Create DB user %2  %COMPIERE_DB_NAME%
@echo -------------------------------------
sqlplus -l -s system/%1@%COMPIERE_DB_NAME% @%COMPIERE_HOME%\Utils\%COMPIERE_DB_PATH%\CreateUser.sql %2 %3
@if ERRORLEVEL 1 ((echo Unable to recreate user %2 @%COMPIERE_DB_NAME%)&&(exit 1))
@echo Done %0
@exit /b 0

:environment
@Echo Please make sure that the enviroment variables are set correctly:
@Echo		COMPIERE_HOME	e.g. D:\Compiere2
@Echo		COMPIERE_DB_NAME	e.g. dev1.compiere.org

:usage
@echo Usage:		%0 systemPassword CompiereID CompierePwd
@echo Example:	%0 manager Compiere Compiere

