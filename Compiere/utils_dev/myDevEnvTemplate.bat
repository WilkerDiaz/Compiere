@Rem	My Development Environment
@Rem
@Rem	This script sets variable for compiling Compiere from source
@Rem	
@Rem	
@Rem 	$Id: myDevEnvTemplate.bat 9213 2010-09-13 23:48:39Z freyes $


@Rem  Check the following parameters:
@Rem  -------------------------------

@Rem	Set Java Home
@SET JAVA_HOME=C:\Program Files\Java\jdk1.6.0_21
@IF NOT EXIST "%JAVA_HOME%\bin" ECHO "** JAVA_HOME NOT found"
@SET PATH=%JAVA_HOME%\bin;%PATH%

@Rem	Set Compiere Source Directory
@SET COMPIERE_SOURCE=C:\Compiere\compiere-all
@IF NOT EXIST "%COMPIERE_SOURCE%" ECHO "** COMPIERE_SOURCE NOT found"

@Rem	Passwords for the keystore
@SET KEYTOOL_PASS=%KEY_PASSWORD%
@IF "%KEYTOOL_PASS%"=="" SET KEYTOOL_PASS=myPassword

@Rem	Keystore & FTP Password
@SET ANT_PROPERTIES=-Dpassword=%KEYTOOL_PASS% -DftpPassword=%FTP_PASSWORD%

@Rem	Ant to send email after completion - change or delete
@SET ANT_PROPERTIES=%ANT_PROPERTIES% -DMailLogger.mailhost=xxx -DMailLogger.from=xxxx -DMailLogger.failure.to=xxxx -DMailLogger.success.to=xxxx

@Rem	Automatic Installation - Where Compiere2 will be unzipped
@SET COMPIERE_ROOT=C:\
@Rem	Automatic Installation - Resulting Home Directory
@SET COMPIERE_HOME=%COMPIERE_ROOT%Compiere2
@Rem	Automatic Installation - Share for final Installers
@SET COMPIERE_INSTALL=C:\Install
@IF NOT EXIST %COMPIERE_INSTALL% Mkdir %COMPIERE_INSTALL%


@Rem  ---------------------------------------------------------------
@Rem  In most cases you don't need to change anything below this line
@Rem  If you need to define something manually do it above this line,
@Rem  it should work, since most variables are checked before set.
@Rem  ---------------------------------------------------------------

@SET CURRENTDIR=%CD%

@Rem Set Version
@SET COMPIERE_VERSION=Custom
@SET COMPIERE_VERSION_FILE=C362
@SET COMPIERE_VENDOR=Unsupported

@SET ENCODING=UTF-8

@Rem	ClassPath
@IF NOT EXIST "%JAVA_HOME%\lib\tools.jar" ECHO "** Need Full Java SDK **"
@SET CLASSPATH=%CLASSPATH%;%JAVA_HOME%\lib\tools.jar

@IF NOT EXIST %COMPIERE_SOURCE%\tools\lib\ant.jar ECHO "** Ant.jar NOT found **"
@SET CLASSPATH=%CLASSPATH%;%COMPIERE_SOURCE%\tools\lib\ant.jar;%COMPIERE_SOURCE%\tools\lib\ant-launcher.jar;%COMPIERE_SOURCE%\tools\lib\ant-swing.jar;%COMPIERE_SOURCE%\tools\lib\ant-commons-net.jar;%COMPIERE_SOURCE%\tools\lib\commons-net-1.4.0.jar
@Rem SET CLASSPATH=%CLASSPATH%;%COMPIERE_SOURCE%\jboss\lib\xml-apis.jar


@Rem	Set XDoclet 1.1.2 Environment
@SET XDOCLET_HOME=%COMPIERE_SOURCE%\tools

@Rem	Java Keystore for signing jars
@IF NOT EXIST %COMPIERE_SOURCE%\keystore MKDIR %COMPIERE_SOURCE%\keystore
@IF EXIST "%COMPIERE_SOURCE%\keystore\myKeystore" GOTO CHECKKEYVALUE

:CREATECOMPIEREKEY
@Echo No Java Keystore found, creating ...
@Rem	.
@Rem	This is the keystore for code signing.
@Rem	Replace it with the official certificate.
@Rem	Note that this is not the SSL certificate.
@Rem	.

SET KEYTOOL_DNAME="CN=myName, OU=myName, O=myOrg, L=myTown, ST=myState, C=US"

"%JAVA_HOME%\bin\keytool" -genkey -keyalg rsa -alias compiere -dname %KEYTOOL_DNAME% -keypass %KEYTOOL_PASS% -validity 365 -keystore %COMPIERE_SOURCE%\keystore\myKeystore -storepass %KEYTOOL_PASS%

"%JAVA_HOME%\bin\keytool" -selfcert -alias compiere -dname %KEYTOOL_DNAME% -keypass %KEYTOOL_PASS% -validity 180 -keystore %COMPIERE_SOURCE%\keystore\myKeystore -storepass %KEYTOOL_PASS%

:CHECKKEYVALUE
@"%JAVA_HOME%\bin\keytool" -list -alias compiere -keyStore %COMPIERE_SOURCE%\keystore\myKeystore -storepass %KEYTOOL_PASS%
@IF ERRORLEVEL 1 GOTO :CREATECOMPIEREKEY

@Rem Set COMPIERE_ENV for all other scripts.
@SET COMPIERE_ENV=Y
