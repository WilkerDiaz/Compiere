@Title Build Tools
@Rem   $Header: /cvsroot/compiere/tools/RUN_build.bat,v 1.16 2005/09/11 02:28:11 jjanke Exp $

@CALL ..\utils_dev\myDevEnv.bat
@IF NOT %COMPIERE_ENV%==Y GOTO NOBUILD

@echo Cleanup ...
@"%JAVA_HOME%\bin\java" -Dant.home="." %ANT_PROPERTIES% org.apache.tools.ant.launch.Launcher clean

@echo Building ...
@"%JAVA_HOME%\bin\java" -Dant.home="." %ANT_PROPERTIES% org.apache.tools.ant.launch.Launcher toolsDistribution

@Echo Done ...
@sleep 60
@exit

:NOBUILD
@Echo Check myDevEnv.bat (copy from myDevEnvTemplate.bat)
@Pause