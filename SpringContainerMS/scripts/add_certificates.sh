#!/bin/bash

function report_error() {
	echo "Could not fin certificate(s) for postgress and / or event streams"
}

function add_certificate_to_keystore(){
    keytool -importcert -noprompt -alias $1 -keystore \
      $JAVA_HOME/lib/security/cacerts -storepass changeit -file $CERT_PATH
}


function process_certificates(){
	CERT_PATH="/etc/ssl/certs/certificates.crt"
	if [ -n "$ES_CA_CERTIFICATE" ] 
	then
		echo "Getting certificate from ES_CA_CERTIFICATE"
        echo $ES_CA_CERTIFICATE >> ${CERT_PATH}
        add_certificate_to_keystore event_stream
     else 
     	report_error()
     fi
}
