#!/bin/bash
# Adapt the following setting according your own services credentials on ICP or IBMCLOUD
# Microservice name
export msname="springcontainerms"
export chart=$(ls ./chart/| grep $msname | head -1)

export kname="kc-"$chart
# namespace to be used in kubernetes cluster
export ns="browncompute"
export CLUSTER_NAME=streamer.icp

export ic_postgres_serv=Green-DB-PostgreSQL

if [[ $# -eq 0 ]];then
  kcenv="LOCAL"
else
  kcenv=$1
fi

case "$kcenv" in
   IBMCLOUD)
        export KAFKA_BROKERS="kafka03-prod02.messagehub.services.us-south.bluemix.net:9093,kafka01-prod02.messagehub.services.us-south.bluemix.net:9093,kafka02-prod02.messagehub.services.us-south.bluemix.net:9093,kafka04-prod02.messagehub.services.us-south.bluemix.net:9093,kafka05-prod02.messagehub.services.us-south.bluemix.net:9093"
        export KAFKA_APIKEY=""
        export KAFKA_ADMIN_URL="https://kafka-admin-prod02.messagehub.services.us-south.bluemix.net:443"
        export KAFKA_ENV="IBMCLOUD"
        export POSTGRESQL_CA_PEM="$(cat ./postgresql.crt)"
        export POSTGRESQL_URL="jdbc:postgresql://<puturlhere:portnumber>/ibmclouddb?sslmode=verify-full&sslfactory=org.postgresql.ssl.NonValidatingFactory"
        export POSTGRESQL_USER="ibm_cloud_c...."
        export POSTGRESQL_PWD="2da3"
        ;;
   ICP)
        export KAFKA_BROKERS="172.16.50.228:30873"
        export KAFKA_ENV="ICP"
        export KAFKA_APIKEY="g....A"
    ;;
   LOCAL)
        export KAFKA_BROKERS="kafka1:9092"
        export KAFKA_ENV="LOCAL"
        export POSTGRESQL_URL="jdbc:postgresql://localhost:5432/postgres"
        export POSTGRESQL_USER="postgres"
        export POSTGRESQL_PWD="supersecret"
   ;;
esac


echo $KAFKA_ENV
echo $KAFKA_BROKERS
echo $POSTGRESQL_URL