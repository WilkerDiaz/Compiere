@Title	Sign jars in %COMPIERE_HOME%\lib\
@Rem
@Rem	Sign all jars for WebStart with your certificate
@Rem	Keystore located at c:\compiere\compiere-all\keystore\myKeystore
@Rem	Keystore passwords both %KEY_PASSWORD% (default myPassword)
@Rem	Jar files are located in the deployment directory %COMPIERE_HOME%\lib
@Rem
@Rem	After this, you need to RUN_setup 
@Rem	to generate/copy the webstart/jnlp distribution

jarsigner -keystore c:\compiere\compiere-all2\keystore\myKeystore -storepass %KEY_PASSWORD% -keypass %KEY_PASSWORD% %COMPIERE_HOME%\lib\CClient.jar compiere
jarsigner -keystore c:\compiere\compiere-all2\keystore\myKeystore -storepass %KEY_PASSWORD% -keypass %KEY_PASSWORD% %COMPIERE_HOME%\lib\CTools.jar compiere
jarsigner -keystore c:\compiere\compiere-all2\keystore\myKeystore -storepass %KEY_PASSWORD% -keypass %KEY_PASSWORD% %COMPIERE_HOME%\lib\oracle.jar compiere
jarsigner -keystore c:\compiere\compiere-all2\keystore\myKeystore -storepass %KEY_PASSWORD% -keypass %KEY_PASSWORD% %COMPIERE_HOME%\lib\db2.jar compiere
jarsigner -keystore c:\compiere\compiere-all2\keystore\myKeystore -storepass %KEY_PASSWORD% -keypass %KEY_PASSWORD% %COMPIERE_HOME%\lib\jPDFPrinterDemo.jar compiere
jarsigner -keystore c:\compiere\compiere-all2\keystore\myKeystore -storepass %KEY_PASSWORD% -keypass %KEY_PASSWORD% %COMPIERE_HOME%\lib\jPDFPrinterProd.jar compiere

@Echo	After this execute RUN_setup
@pause