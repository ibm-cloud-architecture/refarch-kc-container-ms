
echo "##########################################"
echo " Build Spring boot based Container microservice war and docker image  "
echo "##########################################"

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

if [[ $kcenv == "IBMCLOUD" ]]
then
  log=$(ibmcloud target | grep "Not logged in")
  if [[ -n "$log" ]]
  then
    echo "You must login to IBMCLOUD before building"
    exit
  fi
  ev=$(ibmcloud cdb deployables-show 2>&1 | grep "not a registered command")
  if [[ -n "$ev" ]]
  then
    ibmcloud plugin install cloud-databases   
  fi
  ibmcloud cdb deployment-cacert $IC_POSTGRES_SERV > postgresql.crt
fi


find target -iname "*SNAPSHOT*" -print | xargs rm -rf

docker build --network docker_default \
            --build-arg KAFKA_ENV=$kcenv \
            --build-arg KAFKA_BROKERS=${KAFKA_BROKERS} \
            --build-arg KAFKA_APIKEY=${KAFKA_APIKEY} \
            --build-arg POSTGRESQL_URL=${POSTGRESQL_URL}  \
            --build-arg POSTGRESQL_USER=${POSTGRESQL_USER} \
            --build-arg POSTGRESQL_PWD=${POSTGRESQL_PWD} \
            --build-arg JKS_LOCATION=${JKS_LOCATION} \
            --build-arg TRUSTSTORE_PWD=${TRUSTSTORE_PWD} \
            --build-arg POSTGRESQL_CA_PEM="${POSTGRESQL_CA_PEM}"  -t ibmcase/$kname .

if [[ $kcenv == "IBMCLOUD" ]]
then
   # image for private registry in IBM Cloud
   echo "Tag docker image for $kname to deploy on $kcenv"
   docker tag ibmcase/$kname  us.icr.io/ibmcaseeda/$kname 
fi

if [[ $kcenv == "ICP" ]]
then
   # image for private registry in IBM Cloud Private
   echo "Tag docker image for $kname to deploy on $kcenv"
   docker tag ibmcase/$kname  $CLUSTER_NAME:8500/$ns/$kname 
fi