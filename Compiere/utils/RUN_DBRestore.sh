# $Id: RUN_DBRestore.sh,v 1.9 2005/01/22 21:59:15 jjanke Exp $
if [ $COMPIERE_HOME ]; then
  cd $COMPIERE_HOME/utils
fi
. ./myEnvironment.sh Server
echo Restore Compiere Database from Export- $COMPIERE_HOME \($COMPIERE_DB_NAME\)


echo Re-Create Compiere User and import $COMPIERE_HOME/data/ExpDat.dmp
echo == The import will show warnings. This is OK ==
ls -lsa $COMPIERE_HOME/data/ExpDat.dmp
echo Press enter to continue ...
read in

# Parameter: <systemAccount> <compiereID> <compierePwd>
sh $COMPIERE_DB_PATH/DBRestore.sh system/$COMPIERE_DB_SYSTEM $COMPIERE_DB_USER $COMPIERE_DB_PASSWORD
