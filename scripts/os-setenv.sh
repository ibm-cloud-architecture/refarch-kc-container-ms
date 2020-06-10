source ../../../refarch-kc/scripts/setenv.sh IBMCLOUD
oc set env dc/springcontainermgr KAFKA_BROKERS=$KAFKA_BROKERS
oc set env dc/springcontainermgr KAFKA_ENV=$KAFKA_ENV
oc set env dc/springcontainermgr KAFKA_APIKEY=$KAFKA_APIKEY
oc set env dc/springcontainermgr POSTGRESQL_URL=$POSTGRESQL_URL
oc set env dc/springcontainermgr POSTGRESQL_USER=$POSTGRESQL_USER
oc set env dc/springcontainermgr POSTGRESQL_PWD=$POSTGRESQL_PWD
