# $Id: RUN_DBStop.sh,v 1.4 2005/01/22 21:59:15 jjanke Exp $
if [ $COMPIERE_HOME ]; then
  cd $COMPIERE_HOME/utils
fi
. ./myEnvironment.sh Server
echo Stop DataBase Service - $COMPIERE_HOME \($COMPIERE_DB_NAME\)


sh $COMPIERE_DB_PATH/Stop.sh

