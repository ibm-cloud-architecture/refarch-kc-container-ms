#!/bin/bash

root_folder=$(cd $(dirname $0); cd ..; pwd)
. ./scripts/setenv.sh
java -jar -Djavax.net.ssl.trustStore=mykeystore -Djavax.net.ssl.trustStorePassword=changeit ${root_folder}/target/SpringContainerMS-1.0-SNAPSHOT.jar application.SBApplication
