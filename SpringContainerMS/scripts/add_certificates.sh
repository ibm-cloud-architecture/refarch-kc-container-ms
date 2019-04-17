#!/bin/bash

function report_error() {
	echo "Could not fin certificate(s) for postgress and / or event streams"
}

function add_certificate_to_keystore(){
  openssl x509 -in ${IN_PEM} -inform pem -out ${CERT_PATH} -outform der
  keytool -importcert -noprompt -alias $1 -keystore \
      $JAVA_HOME/lib/security/cacerts -storepass changeit -file ${CERT_PATH}
  keytool -list -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit | grep $1
}


function process_certificates(){
	CERT_PATH="certificates.der"
  IN_PEM="ca.pem"
	if [[ -n "$ES_CA_PEM" ]];then

		echo "Getting certificate from ES_CA_CERTIFICATE"
    echo "$ES_CA_PEM" >> ${IN_PEM}
    add_certificate_to_keystore postgresql
  else 
     	report_error
  fi
  rm $IN_PEM
}

process_certificates