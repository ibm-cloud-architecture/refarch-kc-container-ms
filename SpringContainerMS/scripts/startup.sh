#!/bin/bash
root_folder=$(cd $(dirname $0); cd ..; pwd)

scripts/add_certificates.sh
echo $JAVA_HOME
export JKS_LOCATION=${JAVA_HOME}"/jre/lib/security/cacerts"

# Set basic java options
# export JAVA_OPTS="${JAVA_OPTS} -Djavax.net.ssl.trustStore=${JAVA_HOME}/lib/security/cacerts -Djavax.net.ssl.trustStorePassword=changeit"
export JAVA_OPTS="${JAVA_OPTS} -Djavax.net.ssl.trustStore=clienttruststore -Djavax.net.ssl.trustStorePassword=changeit"
echo ${JAVA_OPTS}
java ${JAVA_OPTS} -jar  ./app.jar 
