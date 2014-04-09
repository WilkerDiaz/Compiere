@if (%COMPIERE_HOME%) == () (CALL myEnvironment.bat Server) else (CALL %COMPIERE_HOME%\utils\myEnvironment.bat Server)
@Title Compiere Server Start - %COMPIERE_HOME% (%COMPIERE_APPS_TYPE%)

@Rem $Id: RUN_Server2.bat,v 1.24 2005/10/26 00:38:18 jjanke Exp $

@Rem  To use your own Encryption class (implementing org.compiere.util.SecureInterface),
@Rem  you need to set it here (and in the client start script) - example:
@Rem  SET SECURE=-DCOMPIERE_SECURE=org.compiere.util.Secure
@SET SECURE=


@IF '%COMPIERE_APPS_TYPE%' == 'jboss' GOTO JBOSS
@GOTO UNSUPPORTED

:JBOSS
@Set NOPAUSE=Yes
@Set JAVA_OPTS=-server %COMPIERE_JAVA_OPTIONS% %SECURE%

@Echo Start Compiere Apps Server %COMPIERE_HOME% (%COMPIERE_DB_NAME%)
@Call %JBOSS_HOME%\bin\run -c compiere -b %COMPIERE_APPS_SERVER%
@Echo Done Compiere Apps Server %COMPIERE_HOME% (%COMPIERE_DB_NAME%)
@GOTO END

:UNSUPPORTED
@Echo Apps Server start of %COMPIERE_APPS_TYPE% not supported

:END
@Sleep 60
@Exit

