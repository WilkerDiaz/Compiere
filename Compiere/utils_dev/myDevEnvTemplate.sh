#	My Development Environment
#
#	This script sets variable for compiling Compiere from source
#	
# 	$Id: myDevEnvTemplate.sh 9213 2010-09-13 23:48:39Z freyes $
#
#  Check the following parameters:
#  -------------------------------

if [ $JAVA_HOME ]; then
  export PATH=$JAVA_HOME/bin:$PATH	
else
  echo JAVA_HOME is not set.
  echo You may not be able to build Compiere
  echo Set JAVA_HOME to the directory of your local JDK.
fi


#Set Compiere Source Directory - default one dir up from place where script resides

SAVED_DIR=`pwd`			#save current dir
cd `dirname $0`/..		#change dir to one up form place where script resides - doesn not work with sym links
export COMPIERE_SOURCE=`pwd`	#this is compiere source
cd $SAVED_DIR			#back to the saved directory

echo COMPIERE_SOURCE is $COMPIERE_SOURCE

if [ ! -d $COMPIERE_SOURCE/client ] ; then
	echo "** COMPIERE_SOURCE NOT found"
fi  

#	Passwords for the keystore
export KEYTOOL_PASS=$KEY_PASSWORD
if [ ! $KEYTOOL_PASS ] ; then
	export KEYTOOL_PASS=myPassword
fi

#	Keystore & FTP Password
export ANT_PROPERTIES="-Dpassword=$KEYTOOL_PASS -DftpPassword=$FTP_PASSWORD"

#	Ant to send email after completion - change or delete
#export ANT_PROPERTIES="$ANT_PROPERTIES -DMailLogger.mailhost=xxx -DMailLogger.from=xxxx -DMailLogger.failure.to=xxxx -DMailLogger.success.to=xxxx"

#	Automatic Installation - Where Compiere2 will be unzipped
export COMPIERE_ROOT=/compiere

#	Automatic Installation - Resulting Home Directory
export COMPIERE_HOME=$COMPIERE_ROOT/Compiere2

#	Automatic Installation - Share for final Installers
export COMPIERE_INSTALL=/compiere/install
if [ ! -d $COMPIERE_INSTALL ] ; then
    mkdir -p $COMPIERE_INSTALL
fi  

#  ---------------------------------------------------------------
#  In most cases you don't need to change anything below this line
#  If you need to define something manually do it above this line,
#  it should work, since most variables are checked before set.
#  ---------------------------------------------------------------

export CURRENTDIR=`pwd`

#  Set Version
export COMPIERE_VERSION=Custom
export COMPIERE_VERSION_FILE=C362
export COMPIERE_VENDOR=Unsupported

export ENCODING=UTF-8

#	ClassPath
if  [ ! -f $JAVA_HOME/lib/tools.jar ] ; then
	echo "** Need full Java SDK **"
fi
export CLASSPATH=$CLASSPATH:$JAVA_HOME/lib/tools.jar

if  [ ! -f $COMPIERE_SOURCE/tools/lib/ant.jar ] ;then
	echo "** Ant.jar NOT found **"
fi
export CLASSPATH=$CLASSPATH:$COMPIERE_SOURCE/tools/lib/ant.jar:$COMPIERE_SOURCE/tools/lib/ant-launcher.jar:$COMPIERE_SOURCE/tools/lib/ant-swing.jar:$COMPIERE_SOURCE/tools/lib/ant-commons-net.jar:$COMPIERE_SOURCE/tools/lib/commons-net.jar
#export CLASSPATH=$CLASSPATH:$COMPIERE_SOURCE/jboss/lib/xml-apis.jar

#	Set XDoclet 1.1.2 Environment
export XDOCLET_HOME=$COMPIERE_SOURCE/tools

#	.
#	This is the keystore for code signing.
#	Replace it with the official certificate.
#	Note that this is not the SSL certificate.
#	.

if [ ! -d $COMPIERE_SOURCE/keystore ] ; then
    mkdir $COMPIERE_SOURCE/keystore			#create dir
fi    
	    
# check 	
if  [ ! -f $COMPIERE_SOURCE/keystore/myKeystore ] || [ ! "keytool -list -alias compiere -keyStore $COMPIERE_SOURCE/keystore/myKeystore -storepass $KEYTOOL_PASS" ] ; then		     
     # 	This is a dummy keystore for localhost SSL		     
     #	replace it with your SSL certifificate.		     
     #	Please note that a SSL certificate is 		     
     #	different from the code signing certificate.
     #	The SSL does not require an alias of compiere and		     
     #	there should only be one certificate in the keystore
		     
    HOSTNAME=`hostname`
			
			
    echo No Keystore found, creating for $HOSTNAME ...
			    
    KEYTOOL_DNAME="CN=$HOSTNAME, OU=myName, O=CompiereUser, L=myTown, ST=myState, C=US"

    keytool -genkey -keyalg rsa -alias compiere -dname "$KEYTOOL_DNAME" -keypass $KEYTOOL_PASS -validity 365 -keystore $COMPIERE_SOURCE/keystore/myKeystore -storepass $KEYTOOL_PASS
    keytool -selfcert -alias compiere -dname "$KEYTOOL_DNAME" -keypass $KEYTOOL_PASS -validity 180 -keystore $COMPIERE_SOURCE/keystore/myKeystore -storepass $KEYTOOL_PASS
fi

# Set COMPIERE_ENV for all other scripts.
export COMPIERE_ENV=Y
