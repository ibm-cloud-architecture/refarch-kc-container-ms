#!/bin/bash
root_folder=$(cd $(dirname $0); cd ..; pwd)

scripts/add_certificates.sh
echo "############## Startup script ################"
echo $JAVA_HOME
echo $POSTGRESQL_URL
echo $POSTGRESQL_CA_PEM
echo $KAFKA_BROKERS
echo $TRUSTSTORE_PWD

export JKS_LOCATION=${JAVA_HOME}"/jre/lib/security/cacerts"

# Set basic java options
export JAVA_OPTS="${JAVA_OPTS}  -Djavax.net.ssl.trustStore=$JKS_LOCATION -Djavax.net.ssl.trustStorePassword=$TRUSTSTORE_PWD"
# export JAVA_OPTS="${JAVA_OPTS} -Djavax.net.ssl.trustStore=clienttruststore -Djavax.net.ssl.trustStorePassword=changeit"
echo ${JAVA_OPTS}
java ${JAVA_OPTS} -jar  ./app.jar 
