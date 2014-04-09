#!/bin/sh
#
# $Id: RUN_Compiere2.sh,v 1.18 2005/07/21 16:44:54 jjanke Exp $
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

## Check Compiere Home
if [ $COMPIERE_HOME ]; then
  CLASSPATH=$COMPIERE_HOME/lib/Compiere.jar:$COMPIERE_HOME/lib/CompiereCLib.jar:$COMPIERE_HOME/lib/webservices-client.jar
else
  CLASSPATH=lib/Compiere.jar:lib/CompiereCLib.jar:lib/webservices-client.jar
  echo COMPIERE_HOME is not set
  echo   You may not be able to start Compiere
  echo   Set COMPIERE_HOME to the directory of Compiere2.
fi


# To switch between multiple installs, copy the created Compiere.properties file
# Select the configuration by setting the PROP variable
PROP=
#PROP=-DPropertyFile=test.properties

#  To use your own Encryption class (implementing org.compiere.util.SecureInterface),
#  you need to set it here (and in the server start script) - example:
#  SECURE=-DCOMPIERE_SECURE=org.compiere.util.Secure
SECURE=

$JAVA -Xms32m -Xmx512m -DCOMPIERE_HOME=$COMPIERE_HOME $PROP $SECURE -classpath $CLASSPATH org.compiere.Compiere

