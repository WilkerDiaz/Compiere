@Rem $Id: RUN_TrlExport.bat,v 1.4 2005/09/16 00:49:37 jjanke Exp $

@if (%COMPIERE_HOME%) == () (CALL myEnvironment.bat Server) else (CALL %COMPIERE_HOME%\utils\myEnvironment.bat Server)
@Title Export Translation - %COMPIERE_HOME% (%COMPIERE_DB_NAME%)

@SET AD_LANGUAGE=de_DE
@SET DIRECTORY=%COMPIERE_HOME%\data\%AD_LANGUAGE%

@echo This Procedure exports language %AD_LANGUAGE% into directory %DIRECTORY%
@pause

@"%JAVA_HOME%\bin\java" -cp %CLASSPATH% org.compiere.process.TranslationMgr %DIRECTORY% %AD_LANGUAGE% export

@pause
