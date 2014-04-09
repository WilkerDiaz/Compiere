@if (%COMPIERE_HOME%) == () (CALL myEnvironment.bat Server) else (CALL %COMPIERE_HOME%\utils\myEnvironment.bat Server)
@Title Download Compiere.jar Database into %COMPIERE_HOME%\data

@Rem $Id: RUN_GetCompiereTemplate.bat,v 1.1 2002/10/08 04:31:20 jjanke Exp $

@Echo Download Compiere.jar Database into %COMPIERE_HOME%\data

@ping @COMPIERE_FTP_SERVER@
@cd %COMPIERE_HOME%\data
@del Compiere.jar

@ftp -s:%COMPIERE_HOME%\utils\ftpGetCompiere.txt

@Echo Unpacking ...
@jar xvf Compiere.jar

@Echo ........ Received

@cd %COMPIERE_HOME%\utils
@START RUN_ImportCompiere.bat

@pause
