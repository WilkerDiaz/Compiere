@Title Compiere Clean
@Rem $Header: /cvsroot/compiere/utils_dev/RUN_clean.bat,v 1.4 2005/09/16 00:49:29 jjanke Exp $

@CALL myDevEnv.bat
@IF NOT %COMPIERE_ENV%==Y GOTO NOBUILD

@echo Cleanup ...
@"%JAVA_HOME%\bin\java" -Dant.home="." %ANT_PROPERTIES% org.apache.tools.ant.Main clean

@sleep 60
@exit
:NOBUILD
@Echo Check myDevEnv.bat (copy from myDevEnvTemplate.bat)
@Pause