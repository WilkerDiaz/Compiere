# $Id: RUN_Env.sh,v 1.16 2005/01/22 21:59:15 jjanke Exp $
echo Compiere Environment Check

if [ $COMPIERE_HOME ]; then
  cd $COMPIERE_HOME/utils
fi
# Environment is read from the following script myEnvironment.sh
. ./myEnvironment.sh

echo General ...
echo PATH      = $PATH
echo CLASSPTH  = $CLASSPATH

echo .
echo Homes ...
echo COMPIERE_HOME        = $COMPIERE_HOME
echo JAVA_HOME            = $JAVA_HOME
echo COMPIERE_DB_URL      = $COMPIERE_DB_URL

echo .
echo Database ...
echo COMPIERE_DB_USER     = $COMPIERE_DB_USER
echo COMPIERE_DB_PASSWORD = $COMPIERE_DB_PASSWORD
echo COMPIERE_DB_PATH     = $COMPIERE_DB_PATH

echo .. Oracle specifics
echo COMPIERE_DB_NAME      = $COMPIERE_DB_NAME
echo COMPIERE_DB_SYSTEM   = $COMPIERE_DB_SYSTEM

echo .
echo Java Test ... should be 1.4.1
$JAVA_HOME/bin/java -version

echo .
echo Database Connection Test \(1\) ... TNS
echo Running tnsping $COMPIERE_DB_NAME
tnsping $COMPIERE_DB_NAME

echo .
echo Database Connection Test \(2\)... System
echo Running sqlplus system/$COMPIERE_DB_SYS@$COMPIERE_DB_NAME @$COMPIERE_DB_PATH/Test.sql
sqlplus system/$COMPIERE_DB_SYSTEM@$COMPIERE_DB_NAME @$COMPIERE_DB_HOME/Test.sql 

echo .
echo Checking Database Size \(3\)
sqlplus system/$COMPIERE_DB_SYSTEM@$COMPIERE_DB_NAME @$COMPIERE_DB_HOME/CheckDB.sql $COMPIERE_DB_USER

echo .
echo == It is ok for the next to fail before the Compiere Database Import Step ==
echo Database Connection Test \(4\) ... Compiere \(May not work, if not user not yet imported\)
sqlplus $COMPIERE_DB_USER/$COMPIERE_DB_PASSWORD@$COMPIERE_DB_NAME @$COMPIERE_DB_HOME/Test.sql

echo .
echo Done

