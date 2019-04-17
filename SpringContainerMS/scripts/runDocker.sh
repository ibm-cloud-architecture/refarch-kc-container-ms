#!/bin/bash

root_folder=$(cd $(dirname $0); cd ..; pwd)
. ./scripts/setenv.sh
docker run -e  POSTGRESQL_URL=$POSTGRESQL_URL -e POSTGRESQL_USER=$POSTGRESQL_USER -e POSTGRESQL_PWD=$POSTGRESQL_PWD  -p 8080:8080 ibmcase/$kname
