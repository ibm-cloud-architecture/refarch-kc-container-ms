#!/bin/bash

root_folder=$(cd $(dirname $0); cd ..; pwd)
. ./scripts/setenv.sh
# Set basic java options
export JAVA_OPTS="${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -Djavax.net.ssl.trustStore=mykeystore -Djavax.net.ssl.trustStorePassword=changeit"
java ${JAVA_OPTS} -jar  ${root_folder}/target/SpringContainerMS-1.0-SNAPSHOT.jar application.SBApplication
