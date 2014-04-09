#!/bin/bash
# This script creates an additional set of configuration files for running a 
# separate JBoss Compiere instance on the same physical machine.

if [[ $# < 1 ]]; then  # check the number of command line arguments
  echo "Usage:  $0 <suffix>"
  exit 1
fi

if [ "$COMPIERE_HOME" == "" ]; then
  echo \$COMPIERE_HOME undefined
  exit 2;
fi

echo
echo -n "Creating files: "

cp $COMPIERE_HOME/Compiere.properties $COMPIERE_HOME/Compiere_$1.properties 
echo -n "$COMPIERE_HOME/Compiere_$1.properties " 

cp myEnvironment.sh myEnvironment_$1.sh
perl -pi -e "s/myEnvironment\./myEnvironment_$1\./g" myEnvironment_$1.sh
perl -pi -e 's/COMPIERE_JAVA_OPTIONS="(.*)"/COMPIERE_JAVA_OPTIONS="\1 -DPropertyFile=\$\COMPIERE_HOME\/'"Compiere_$1.properties\"/g" myEnvironment_$1.sh
echo -n "myEnvironment_$1.sh "

cp RUN_Server2.sh RUN_Server2_$1.sh
perl -pi -e "s/myEnvironment\./myEnvironment_$1\./g" RUN_Server2_$1.sh
echo -n "RUN_Server2_$1.sh "

cp RUN_Server2Stop.sh RUN_Server2Stop_$1.sh
echo -n "RUN_Server2Stop_$1.sh "
echo

echo
echo -n "Creating JBoss configuration directory: "
cp -r ../jboss/server/compiere ../jboss/server/compiere_$1
echo -n ../jboss/server/compiere_$1
echo 

echo
echo Please modify the following in $COMPIERE_HOME/Compiere_$1.properties:
grep Connection= $COMPIERE_HOME/Compiere_$1.properties

echo
echo Please modify the following in myEnvironment_$1.sh:
grep USER= myEnvironment_$1.sh
grep PASSWORD= myEnvironment_$1.sh
grep PORT= myEnvironment_$1.sh

echo
echo Please modify the following in ../jboss/server/compiere_$1/conf/jboss-service.xml
grep Port ../jboss/server/compiere_$1/conf/jboss-service.xml | grep -v "!"


