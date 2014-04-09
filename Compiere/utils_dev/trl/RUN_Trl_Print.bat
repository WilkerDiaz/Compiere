@Rem	Print translation files

@Rem	Parameter - file to process otherwise default
SET FILE=%1%
IF '%FILE%'=='' SET FILE=AD_Element_Trl_en_US.xml
IF '%COMPIERE_SOURCE%'=='' SET COMPIERE_SOURCE=D:\Compiere
IF '%COMPIERE_TRL%'=='' SET COMPIERE_TRL=en_US

@Echo ... %FILE%

@java org.apache.xalan.xslt.Process -in %COMPIERE_SOURCE%\data\%COMPIERE_TRL%\%FILE% -xsl trl_Print.xsl -out %COMPIERE_SOURCE%\data\%COMPIERE_TRL%\p_%FILE%

@pause
