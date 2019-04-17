#!/bin/bash

export msname="springcontainerms"
export chart=$(ls ./chart/| grep $msname | head -1)
export kname="kc-"$chart
export ns="browncompute"
export CLUSTER_NAME=streamer.icp
export POSTGRESQL_URL="jdbc:postgresql://<alotofcharshere>.databases.appdomain.cloud:<>/ibmclouddb?sslmode=verify-full&sslfactory=org.postgresql.ssl.NonValidatingFactory"
export POSTGRESQL_USER="ibm_cloud_<alotofcharshere>"
export POSTGRESQL_PWD="<alotofcharshere>"
