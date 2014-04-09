@Rem $Id: RUN_DBImport.bat,v 1.13 2006/01/23 04:56:03 jjanke Exp $

@if (%COMPIERE_HOME%) == () (CALL myEnvironment.bat Server) else (CALL %COMPIERE_HOME%\utils\myEnvironment.bat Server)
@Title Compiere Database Import (car) - %COMPIERE_HOME% (%COMPIERE_DB_NAME%)

@echo -------------------------------------
@echo Start Importing %COMPIERE_DB_USER%
@echo -------------------------------------
@rem set CLASSPATH=%CLASSPATH%;%COMPIERE_HOME%\lib\CInstall.jar;%COMPIERE_HOME%\lib\MigrateClient.jar;%COMPIERE_HOME%\lib\oracle.jar;%COMPIERE_HOME%\lib\postgreSQL.jar
set CLASSPATH=%COMPIERE_HOME%\lib\j2ee.jar;%COMPIERE_HOME%\lib\CCTools.jar;%COMPIERE_HOME%\lib\Compiere.jar;%COMPIERE_HOME%\lib\CInstall.jar;%COMPIERE_HOME%\lib\MigrateClient_.jar;%COMPIERE_HOME%\lib\oracle.jar;%COMPIERE_HOME%\lib\postgreSQL.jar

"%COMPIERE_JAVA%" %COMPIERE_JAVA_OPTIONS% -cp %CLASSPATH% com.compiere.migrate.DBImport -t %COMPIERE_DB_TYPE% -h %COMPIERE_DB_SERVER% -p %COMPIERE_DB_PORT% -n %COMPIERE_DB_NAME% -U %COMPIERE_DB_USER% -P %COMPIERE_DB_PASSWORD% -d %COMPIERE_HOME%\data

@IF ERRORLEVEL 1 ((echo ERROR executing %0 %*) && (exit /b 1))

@echo.
@echo %0 %* : OK
@echo.
