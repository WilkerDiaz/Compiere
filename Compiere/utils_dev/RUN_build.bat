@Title Build Compiere Clean
@Rem $Header: /cvsroot/compiere/utils_dev/RUN_build.bat,v 1.22 2005/09/08 21:56:11 jjanke Exp $

@CALL myDevEnv.bat
@IF NOT %COMPIERE_ENV%==Y GOTO NOBUILD

@echo Cleanup ...
@"%JAVA_HOME%\bin\java" -Dant.home="." %ANT_PROPERTIES% org.apache.tools.ant.Main clean

@echo Building ...
@"%JAVA_HOME%\bin\java" -Xms64m -Xmx512m -Dant.home="." %ANT_PROPERTIES% org.apache.tools.ant.Main -logger org.apache.tools.ant.listener.MailLogger complete
@IF ERRORLEVEL 1 goto ERROR

dir %COMPIERE_INSTALL%

@Echo Done ...
@pause
@exit /b

:NOBUILD
@Echo Check myDevEnv.bat (copy from myDevEnvTemplate.bat)

:ERROR
@Color fc

@Pause
@color