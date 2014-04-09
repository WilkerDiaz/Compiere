#!/bin/bash
# Copyright Compiere, Inc
# $Id: CLMigrate.sh 7847 2009-07-07 18:26:21Z freyes $
echo Install Compiere Server

if [ $# -lt 1 ]; then
	echo Configuration file missing
	echo Please edit the configuration file appropriately and pass the file name to this script
	echo Usage: $0 CLConfiguration.sh
      exit 1
fi

./CLInstall.sh $1 '--migrate y'
if [ $? -gt 0 ]; then
	echo ===========================================
	echo An error occurred while running the program
	echo Please check log files for details
	echo ===========================================
	exit 1
fi

echo Compiere Migration completed. Check log files for details.
