#!/bin/bash

function report_error() {
	echo "Could not find certificate(s) for $1"
}

function add_certificate_to_keystore(){
  jre=$(echo $JAVA_HOME | sed 's#.*/##')
  if [[ "$jre" != "jre" ]]
  then
    export JAVA_HOME=$JAVA_HOME/jre
  fi
  echo $JAVA_HOME
  echo $JKS_LOCATION
  echo $TRUSTSTORE_PWD
  # $JAVA_HOME/lib/security/cacerts
  openssl x509 -in ${IN_PEM} -inform pem -out ${CERT_PATH} -outform der
  keytool -importcert -noprompt -alias $1 -keystore $JKS_LOCATION \
       -storepass $TRUSTSTORE_PWD -file ${CERT_PATH}
  keytool -list -keystore $JKS_LOCATION -storepass $TRUSTSTORE_PWD | grep $1
}


function process_certificates(){
	CERT_PATH="certificates.der"
  IN_PEM="ca.pem"
  touch $IN_PEM
	if [[ -n "$POSTGRESQL_CA_PEM" ]];then

		echo "Getting certificate from POSTGRESQL_CA_CERTIFICATE"
    echo "$POSTGRESQL_CA_PEM" > ${IN_PEM}
    add_certificate_to_keystore postgresql
  else 
     	report_error postgresql
  fi
  if [[ -n "$ES_CA_PEM" ]];then
		echo "Getting certificate from ES_CA_CERTIFICATE"
    echo "$ES_CA_PEM" > ${IN_PEM}
    add_certificate_to_keystore eventstreams
  else 
     	report_error event_streams
  fi
  rm $IN_PEM
}

if [[ $KAFKA_ENV == "IBMCLOUD" ]]
then
  echo " -> Process certificates"
  process_certificates
  echo " -> Done"
fi