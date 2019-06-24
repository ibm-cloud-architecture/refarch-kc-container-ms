#!/bin/bash
# root_folder=$(cd $(dirname $0); cd ..; pwd)

if [[ $PWD = */scripts ]]; then
 cd ..
fi
if [[ $# -eq 0 ]];then
  kcenv="LOCAL"
else
  kcenv=$1
fi

source ../../refarch-kc/scripts/setenv.sh $kcenv
# Set basic java options
export JAVA_OPTS="${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -Djavax.net.ssl.trustStore=$JKS_LOCATION -Djavax.net.ssl.trustStorePassword=$TRUSTSTORE_PWD"
java ${JAVA_OPTS} -jar  ./target/SpringContainerMS-1.0-SNAPSHOT.jar application.SBApplication
