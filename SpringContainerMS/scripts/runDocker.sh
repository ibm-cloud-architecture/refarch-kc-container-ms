#!/bin/bash



if [[ $PWD = */scripts ]]; then
 cd ..
fi
if [[ $# -eq 0 ]];then
  kcenv="LOCAL"
else
  kcenv=$1
fi

msname="springcontainerms"
ns="greencompute"
chart=$(ls ./chart/| grep $msname)
kname="kc-"$chart
source ../../refarch-kc/scripts/setenv.sh $kcenv
echo $kname
docker rm $kname
# docker run  --name $kname -e POSTGRESQL_URL=$POSTGRESQL_URL -e  POSTGRESQL_CA_PEM=$POSTGRESQL_CA_PEM -e POSTGRESQL_USER=$POSTGRESQL_USER -e POSTGRESQL_PWD=$POSTGRESQL_PWD  -p 8080:8080 ibmcase/$kname

docker run --name $kname -e KAFKA_BROKERS=$KAFKA_BROKERS -e POSTGRESQL_URL=$POSTGRESQL_URL -e POSTGRESQL_CA_PEM="$POSTGRESQL_CA_PEM" -e POSTGRESQL_USER=$POSTGRESQL_USER -e POSTGRESQL_PWD=$POSTGRESQL_PWD -p 8080:8080 -ti  ibmcase/$kname 