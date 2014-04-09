#!/bin/sh
#
echo Compiere Client $COMPIERE_HOME

#	Set directly to overwrite
#COMPIERE_HOME=/Compiere2
#JAVA_HOME=/usr/lib/java

##	Check Java Home
if [ $JAVA_HOME ]; then
  JAVA=$JAVA_HOME/bin/java
else
  JAVA=java
  echo JAVA_HOME is not set.
  echo   You may not be able to start Compiere
  echo   Set JAVA_HOME to the directory of your local JDK.
fi

if [ $# -lt 1 -o $# -gt 3 ]
then
    echo "Usage: $0 <EntityType> <DeleteRecord(Y|N)> <DeleteFile(Y|N)>"
    echo "Example: $0 CPRO N Y"
    exit 1
fi

ENTITYTYPE=
DELETERECORD=N
DELETEFILE=Y
if [ $# -eq 1 ]
then
    ENTITYTYPE=$1
elif [ $# -eq 2 ]
then
    DELETERECORD=$2 
elif [ $# -eq 3 ]
then
    DELETERECORD=$2 
    DELETEFILE=$3
fi

## Check Compiere Home
if [ $COMPIERE_HOME ]; then
  CLASSPATH=$COMPIERE_HOME/lib/CInstall.jar:$COMPIERE_HOME/lib/CompiereInstall.jar:$COMPIERE_HOME/lib/jPDF.jar:$COMPIERE_HOME/lib/CCTools.jar:$COMPIERE_HOME/lib/oracle.jar:$COMPIERE_HOME/lib/jboss.jar:$COMPIERE_HOME/lib/db2.jar:$COMPIERE_HOME/lib/postgreSQL.jar:$COMPIERE_HOME/lib/sqlServer.jar
else
  CLASSPATH=lib/CInstall.jar:lib/CompiereInstall.jar:lib/jPDF.jar:lib/CCTools.jar:lib/oracle.jar:lib/jboss.jar:lib/db2.jar:lib/postgreSQL.jar:lib/sqlServer.jar
  echo COMPIERE_HOME is not set
  echo   You may not be able to start Compiere
  echo   Set COMPIERE_HOME to the directory of Compiere2.
fi


$JAVA -Xmx258m -classpath $CLASSPATH -DCOMPIERE_HOME=$COMPIERE_HOME org.compiere.util.UninstallComponent -E $ENTITYTYPE -R $DELETERECORD -F $DELETEFILE

