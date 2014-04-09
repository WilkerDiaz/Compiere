@if (%COMPIERE_HOME%) == () (CALL myEnvironment.bat Server) else (CALL %COMPIERE_HOME%\utils\myEnvironment.bat Server)
@Title Compiere Server Stop - %COMPIERE_HOME%

@Rem $Id: RUN_Server2Stop.bat,v 1.12 2005/09/06 02:46:16 jjanke Exp $

@IF '%COMPIERE_APPS_TYPE%' == 'jboss' GOTO JBOSS
@GOTO UNSUPPORTED

:JBOSS
@Set NOPAUSE=Yes
@Set JBOSS_LIB=%JBOSS_HOME%\lib
@Set JBOSS_SERVERLIB=%JBOSS_HOME%\server\compiere\lib
@Set JBOSS_CLASSPATH=%COMPIERE_HOME%\lib\jboss.jar;%JBOSS_LIB%\jboss-system.jar

@CD %JBOSS_HOME%\bin
Call shutdown --server=jnp://%COMPIERE_APPS_SERVER%:%COMPIERE_JNP_PORT% --shutdown

@Echo Done Stopping Compiere Apps Server %COMPIERE_HOME% (%COMPIERE_DB_NAME%)
@GOTO END

:UNSUPPORTED
@Echo Apps Server stop of %COMPIERE_APPS_TYPE% not supported
@sleep 30
@Exit /b 1


:END
@sleep 30
@Exit /b 0
