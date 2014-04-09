@Title Update local Compiere DB
@Rem	$Id: UpdateCompiere.bat,v 1.4 2004/11/01 06:06:16 jjanke Exp $

@dir database\DatabaseBuild.sql

@Echo	requires manual entry of exit

sqlplus compiere/compiere @database\DatabaseBuild.sql

sqlplus compiere/compiere @maintain\Maintenance\DBA_Recompile_Run.sql