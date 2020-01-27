#!/bin/bash
root_folder=$(cd $(dirname $0); cd ..; pwd)


echo "############## Startup script ################"
echo $JAVA_HOME
echo $KAFKA_ENV
echo $POSTGRESQL_URL
echo $POSTGRESQL_CA_PEM
echo $KAFKA_BROKERS
echo $TRUSTSTORE_PWD
echo $JKS_LOCATION
export JKS_LOCATION=${JAVA_HOME}"/jre/lib/security/cacerts"

scripts/add_certificates.sh

# Set basic java options
# Default trustStorePassword is `changeit`. It is not related to the EventStreams truststore file and its associated password.
# https://www.ibm.com/support/knowledgecenter/en/SSYKE2_7.1.0/com.ibm.java.security.component.71.doc/security-component/keytoolDocs/cacertsfile.html
export JAVA_OPTS="${JAVA_OPTS}  -Djavax.net.ssl.trustStore=$JKS_LOCATION -Djavax.net.ssl.trustStorePassword=changeit"
#export JAVA_OPTS="${JAVA_OPTS} -Djavax.net.ssl.trustStore=clienttruststore -Djavax.net.ssl.trustStorePassword=changeit"
echo ${JAVA_OPTS}
java ${JAVA_OPTS} -jar  ./app.jar
