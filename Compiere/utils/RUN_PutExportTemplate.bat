@if (%COMPIERE_HOME%) == () (CALL myEnvironment.bat Server) else (CALL %COMPIERE_HOME%\utils\myEnvironment.bat Server)
@Title Export Database ExpDat.jar - %COMPIERE_HOME%

@Rem $Id: RUN_PutExportTemplate.bat,v 1.3 2002/10/22 14:56:40 jjanke Exp $

@Echo ........ Export DB
@call %COMPIERE_DB_PATH%\DBExport %COMPIERE_DB_USER%/%COMPIERE_DB_PASSWORD%

@Rem 	Echo ........ Stop DB
@Rem	sqlplus "system/%COMPIERE_DB_SYSTEM% AS SYSDBA" @%COMPIERE_HOME%\utils\%COMPIERE_BP_PATH%\Stop.sql

@Title Transafer Database ExpDat.jar - %COMPIERE_HOME%\data
@Echo Transfer Database ExpDat.jar - %COMPIERE_HOME%\data

@Echo ........ FTP
@ping @COMPIERE_FTP_SERVER@
@cd %COMPIERE_HOME%\data
@dir ExpDat.*

@ftp -s:%COMPIERE_HOME%\utils\ftpPutExport.txt

@Echo ........ Done
@pause
