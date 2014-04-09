@Title Build FSRV
@Rem Copyright 2007-2009 Compiere, Inc.
@Rem $Id: RUN_build.bat 8180 2009-11-15 22:45:49Z freyes $

@CALL ..\utils_dev\myDevEnv.bat
@IF NOT "%COMPIERE_ENV%"=="Y" ((echo ======^> myDevEnv.bat ERROR!!)&&(pause)&&(exit /b 1))

@echo Cleaning FSRV...
@"%JAVA_HOME%\bin\java" -Dant.home="." %ANT_PROPERTIES% org.apache.tools.ant.Main clean
@IF ERRORLEVEL 1 ((echo ======^> fsrv Clean ERROR!!)&&(pause)&&(exit /b 1))

@echo Building FSRV...
@"%JAVA_HOME%\bin\java" -Dant.home="." %ANT_PROPERTIES% org.apache.tools.ant.Main dist
@IF ERRORLEVEL 1 ((echo ======^> fsrv Build ERROR!!)&&(pause)&&(exit /b 1))

@Echo Done ...
@pause
@exit /b 0

