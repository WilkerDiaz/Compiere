@Rem call with parameter
@Rem	1 - sourcepath entry
@Rem	2 - destination entry
@Rem	3 - parameters
@Rem assumes that you have a file packages.txt in the calling directory

@Rem $Id: documentation.bat 6409 2008-10-04 00:07:46Z freyes $
@CALL ..\utils_dev\myDevEnv.bat

@Set CLASSPATH=..\lib\Compiere.jar;..\lib\CCTools.jar;..\lib\oracle.jar;..\lib\db2.jar;..\lib\postgresql.jar;..\lib\jPDF.jar
@Set CLASSPATH=%CLASSPATH%;..\lib\CSTools.jar;..\lib\jboss.jar;..\
@Set CLASSPATH=%CLASSPATH%;..\tools\lib\j2ee.jar;..\tools\lib\junit.jar

javadoc -sourcepath %1 -d %2 -use -author -breakiterator -version -link http://java.sun.com/javase/6/docs/api -link http://java.sun.com/j2ee/1.4/docs/api -splitindex -windowtitle "Compiere %COMPIERE_VERSION% API Documentation" -doctitle "Compiere<sup>TM</sup> API Documentation" -header "<b>Compiere %COMPIERE_VERSION%</b>" -bottom "Copyright (c) 1999-2008 Compiere, Inc." -overview doc\overview.html  -J-Xmx256m -subpackages org.compiere