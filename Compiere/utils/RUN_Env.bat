@Title Compiere Environment Check

@Rem $Id: RUN_Env.bat,v 1.16 2005/01/22 21:59:15 jjanke Exp $

@if (%COMPIERE_HOME%) == () (CALL myEnvironment.bat) else (CALL %COMPIERE_HOME%\utils\myEnvironment.bat)

@Echo General ...
@Echo PATH      = %PATH%
@Echo CLASSPTH  = %CLASSPATH%

@Echo .
@Echo Homes ...
@Echo COMPIERE_HOME        = %COMPIERE_HOME%
@Echo JAVA_HOME            = %JAVA_HOME%
@Echo COMPIERE_DB_URL      = %COMPIERE_DB_URL%

@Echo .
@Echo Database ...
@Echo COMPIERE_DB_USER     = %COMPIERE_DB_USER%
@Echo COMPIERE_DB_PASSWORD = %COMPIERE_DB_PASSWORD%
@Echo COMPIERE_DB_PATH     = %COMPIERE_DB_PATH%

@Echo .. Oracle specifics
@Echo COMPIERE_DB_NAME      = %COMPIERE_DB_NAME%
@Echo COMPIERE_DB_SYSTEM   = %COMPIERE_DB_SYSTEM%

@"%JAVA_HOME%\bin\java" -version

@Echo .
@Echo Java Version should be "1.5"
@Echo ---------------------------------------------------------------
@Pause

@Echo .
@Echo ---------------------------------------------------------------
@Echo Database Connection Test (1) ... %COMPIERE_DB_NAME%
@Echo If this fails, verify the COMPIERE_DB_NAME setting with Oracle Net Manager
@Echo You should see an OK at the end
@Pause
tnsping %COMPIERE_DB_NAME%

@Echo .
@Echo ---------------------------------------------------------------
@Echo Database Connection Test (3) ... system/%COMPIERE_DB_SYSTEM% in %COMPIERE_DB_HOME%
@Echo If this test fails, verify the system password in COMPIERE_DB_SYSTEM
@Pause
sqlplus system/%COMPIERE_DB_SYSTEM%@%COMPIERE_DB_NAME% @%COMPIERE_DB_HOME%\Test.sql

@Echo .
@Echo ---------------------------------------------------------------
@Echo Checking Database Size
@Pause
sqlplus system/%COMPIERE_DB_SYSTEM%@%COMPIERE_DB_NAME% @%COMPIERE_DB_HOME%\CheckDB.sql %COMPIERE_DB_USER%

@Echo .
@Echo ---------------------------------------------------------------
@Echo Database Connection Test (4) ... %COMPIERE_DB_USER%/%COMPIERE_DB_PASSWORD%
@Echo This may fail, if you have not imported the Compiere database yet - Just enter EXIT and run this script again after the import
@Pause
sqlplus %COMPIERE_DB_USER%/%COMPIERE_DB_PASSWORD%@%COMPIERE_DB_NAME% @%COMPIERE_DB_HOME%\Test.sql

@Echo .
@Echo Done
@pause
