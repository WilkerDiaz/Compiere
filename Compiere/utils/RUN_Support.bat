@if (%COMPIERE_HOME%) == () (CALL myEnvironment.bat Server) else (CALL %COMPIERE_HOME%\utils\myEnvironment.bat Server)
@Title Compiere Support - %COMPIERE_HOME% (%COMPIERE_DB_NAME%)
@"%COMPIERE_JAVA%"w %COMPIERE_JAVA_OPTIONS% -cp %CLASSPATH% com.compiere.client.Support
