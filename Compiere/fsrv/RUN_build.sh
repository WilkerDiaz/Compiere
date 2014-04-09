# Compile FSRV
# Copyright 2007-2009 Compiere, Inc.
# $Id: RUN_build.sh 8180 2009-11-15 22:45:49Z freyes $

.  ../utils_dev/myDevEnv.sh	#call environment
if [ ! $COMPIERE_ENV==Y ] ; then
    echo "Can't set developemeent environment - check myDevEnv.sh"
    exit 1
fi

echo running Ant
$JAVA_HOME/bin/java -Dant.home="." $ANT_PROPERTIES org.apache.tools.ant.Main clean
if [ $? -ne 0 ]; then
  echo "cmrp clean error"
  exit 1
fi

$JAVA_HOME/bin/java -Dant.home="." $ANT_PROPERTIES org.apache.tools.ant.Main dist
if [ $? -ne 0 ]; then
  echo "cmrp build error"
  exit 1
fi

echo
echo "Done ..."