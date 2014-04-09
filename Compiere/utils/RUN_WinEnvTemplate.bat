@Title Set Windows Environment
@Rem $Id: RUN_WinEnvTemplate.bat,v 1.4 2005/09/08 21:56:00 jjanke Exp $

@Echo ===================================
@Echo Setup Client Environment
@Echo ===================================

@cscript //nologo @COMPIERE_HOME@\utils\WinEnv.js "@COMPIERE_HOME@" "@JAVA_HOME@"

