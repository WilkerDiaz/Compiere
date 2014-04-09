#!/bin/bash
# Copyright Compiere, Inc
# $Id: init-volumes.sh 7847 2009-07-07 18:26:21Z freyes $


# DBTYPE is one of "SE" or "XE"
DBTYPE="SE"

PROGRAM="Compiere Cloud Edition EBS Volume Initialization"
LOGFILE=`pwd`/init-volumes.log

echo 
echo $PROGRAM
echo
echo "WARNING: This command will destroy all existing data on volumes /dev/sdh and /dev/sdi."
echo "Only run this command on newly allocated volumes that have yet to be initialized."
echo



if [ `whoami` != root ]; then
  echo This script must be run as root.  Aborting.
  exit 1
fi;



echo $PROGRAM >> $LOGFILE
echo "Started at " `date`  >> $LOGFILE

read -p "Type \"Yes\" to continue, or Ctrl-C to abort: " confirm
if [ $confirm != "Yes" ]; then
  echo Aborting.
  exit 1
fi

echo
echo Proceeding with EBS Volume Initialization.
echo

echo Checking for availability of attached volumes

for q in /dev/sdh /dev/sdi; do
    if [ ! -e $q ]; then
	echo $q not found.  Please attach a volume to $q using the Amazon EC2 console.
	exit 2
    fi
done


echo Checking for currently mounted files systems

for q in /dev/sdh /dev/sdi; do
    mount -t ext3 | grep -q $q
    if [ $? -ne 1 ]; then 
	echo $q is still in active use.  Please un-mount any file systems mounted on $q.
	exit 3
    fi
done



echo -n Creating partitions...
sfdisk /dev/sdh <<EOF  >> $LOGFILE 2>&1
,8,L
,,L
EOF
sfdisk /dev/sdi <<EOF  >> $LOGFILE 2>&1
,,L
EOF
echo

echo -n Creating file systems...
mkfs -t ext3 /dev/sdh1  >> $LOGFILE 2>&1
mkfs -t ext3 /dev/sdh2  >> $LOGFILE 2>&1
mkfs -t ext3 /dev/sdi1  >> $LOGFILE 2>&1
echo

echo -n Creating mount points...
mkdir -p /mnt/compiere
mkdir -p /mnt/oracle/oradata
mkdir -p /mnt/oracle/flash_recovery_area
echo

echo -n Mounting file systems...
mount /mnt/compiere
mount /mnt/oracle/oradata
mount /mnt/oracle/flash_recovery_area
echo



if [ $DBTYPE == "XE" ]; then
    ORACLE_SID="XE"
    ORADATA_DIR="/usr/lib/oracle/xe/oradata"
    FLASH_RECOVERY_AREA_DIR="/usr/lib/oracle/xe/app/oracle/flash_recovery_area"
elif  [ $DBTYPE == "SE" ]; then
    ORACLE_SID="SE"
    ORADATA_DIR="/home/oracle/product/10.2.0/server/oradata"
    FLASH_RECOVERY_AREA_DIR="/home/oracle/product/10.2.0/server/flash_recovery_area"
else
    echo "Unsupported DBTYPE.  Aborting."
    exit 3
fi



echo -n Unpacking archives...

if [ -h /home/compiere/Compiere2/data ]; then
    mkdir -p /mnt/compiere/data
    cd /home/compiere/Compiere2
    echo tar xvfz data.tar.gz
    tar xvfz data.tar.gz >> $LOGFILE 2>&1
else
    echo mv -f /home/compiere/Compiere2/data /mnt/compiere
    mv -f /home/compiere/Compiere2/data /mnt/compiere
    ln -s -f /mnt/compiere/data /home/compiere/Compiere2/data
fi

if [ -h $ORADATA_DIR ]; then
    cd $ORADATA_DIR/..
    echo tar xvfz oradata.tar.gz
    tar xvfz oradata.tar.gz >> $LOGFILE 2>&1
else
    echo mv -f $ORADATA_DIR/$ORACLE_SID /mnt/oracle/oradata
    mv -f $ORADATA_DIR/$ORACLE_SID /mnt/oracle/oradata
    rm -r -f $ORADATA_DIR
    ln -s -f /mnt/oracle/oradata $ORADATA_DIR
fi

if [ -h $FLASH_RECOVERY_AREA_DIR ]; then
    cd $FLASH_RECOVERY_AREA_DIR/..
    echo tar xvfz flash_recovery_area.tar.gz
    tar xvfz flash_recovery_area.tar.gz >> $LOGFILE 2>&1
else
    echo mv -f $FLASH_RECOVERY_AREA_DIR/$ORACLE_SID /mnt/oracle/flash_recovery_area
    mv -f $FLASH_RECOVERY_AREA_DIR/$ORACLE_SID /mnt/oracle/flash_recovery_area
    rm -r -f $FLASH_RECOVERY_AREA_DIR
    ln -s -f /mnt/oracle/flash_recovery_area $FLASH_RECOVERY_AREA_DIR
fi
echo


cd

echo
echo $PROGRAM has completed successfully.


echo $PROGRAM has completed successfully. >> $LOGFILE
echo "Completed at " `date`  >> $LOGFILE

