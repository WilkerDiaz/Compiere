# $Id: RUN_TrlExport.sh,v 1.4 2005/01/22 21:59:15 jjanke Exp $

if [ $COMPIERE_HOME ]; then
  cd $COMPIERE_HOME/utils
fi
. ./myEnvironment.sh Server

echo	Export Compiere Translation - $COMPIERE_HOME \($COMPIERE_DB_NAME\)

# need to change this to reflect your language
export  AD_LANGUAGE=ca_ES

export  DIRECTORY=$COMPIERE_HOME/data/$AD_LANGUAGE

echo	This Procedure exports language $AD_LANGUAGE to directory $DIRECTORY

$JAVA_HOME/bin/java -cp $CLASSPATH org.compiere.process.TranslationMgr $DIRECTORY $AD_LANGUAGE export 


echo	Done
