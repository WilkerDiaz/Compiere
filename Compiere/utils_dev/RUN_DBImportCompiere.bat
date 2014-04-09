@if (%COMPIERE_HOME%) == () (CALL myEnvironment.bat Server) else (CALL %COMPIERE_HOME%\utils\myEnvironment.bat Server)
@Title Import Compiere DB (Release) - %COMPIERE_HOME% (%COMPIERE_DB_NAME%) - %COMPIERE_DB_USER%

@echo Re-Create Compiere DB User and import from Release %COMPIERE_HOME%\data - (%COMPIERE_DB_NAME%)
@dir ..\data\Compiere.dmp
@echo == The import will show warnings. This is OK ==
@pause


@echo -------------------------------------
@echo Re-Create DB user
@echo -------------------------------------
sqlplus system/%COMPIERE_DB_SYSTEM%@%COMPIERE_DB_NAME% @%COMPIERE_HOME%\Utils\oracle\CreateUser.sql %COMPIERE_DB_USER% %COMPIERE_DB_PASSWORD%

@echo -------------------------------------
@echo Import Compiere.dmp
@echo -------------------------------------
imp system/%COMPIERE_DB_SYSTEM%@%COMPIERE_DB_NAME% FILE=..\data\Compiere.dmp FROMUSER=(release) TOUSER=%COMPIERE_DB_USER% STATISTICS=RECALCULATE

@echo -------------------------------------
@if (%COMPIERE_DB_TYPE%) == (%COMPIERE_DB_PATH%) echo Create SQLJ 
@echo -------------------------------------
@if (%COMPIERE_DB_TYPE%) == (%COMPIERE_DB_PATH%) call %COMPIERE_HOME%\Utils\%COMPIERE_DB_PATH%\create %COMPIERE_DB_USER%/%COMPIERE_DB_PASSWORD%

@echo --------========--------========--------========--------
@echo System Check - The Import phase showed warnings. 
@echo This is OK as long as the following does not show errors
@echo --------========--------========--------========--------
sqlplus %COMPIERE_DB_USER%/%COMPIERE_DB_PASSWORD%@%COMPIERE_DB_NAME% @%COMPIERE_HOME%\Utils\%COMPIERE_DB_PATH%\AfterImport.sql


@pause
