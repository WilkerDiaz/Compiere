#!/bin/sh
# Copyright Compiere, Inc
# $Id: ec2-get-meta-data.sh 7847 2009-07-07 18:26:21Z freyes $

curl -s http://169.254.169.254/latest/meta-data/$@
