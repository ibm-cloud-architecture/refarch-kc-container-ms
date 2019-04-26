#!/bin/bash

root_folder=$(cd $(dirname $0); cd ..; pwd)
. ../scripts/setenv.sh LOCAL
echo $kname
docker rm $kname
# docker run  --name $kname -e POSTGRESQL_URL=$POSTGRESQL_URL -e  POSTGRESQL_CA_PEM=$POSTGRESQL_CA_PEM -e POSTGRESQL_USER=$POSTGRESQL_USER -e POSTGRESQL_PWD=$POSTGRESQL_PWD  -p 8080:8080 ibmcase/$kname

docker run -ti --name $kname -e POSTGRESQL_URL=$POSTGRESQL_URL -e POSTGRESQL_CA_PEM="${POSTGRESQL_CA_PEM}" -e POSTGRESQL_USER=$POSTGRESQL_USER -e POSTGRESQL_PWD=$POSTGRESQL_PWD -p 8080:8080 ibmcase/$kname bash